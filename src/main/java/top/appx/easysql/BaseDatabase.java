package top.appx.easysql;

import entity.UserEntity;
import sun.reflect.generics.scope.MethodScope;
import top.appx.eweb.PageResultInfo;
import top.appx.zutil.ReflectUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by qq799 on 2017/2/26.
 */
public abstract class BaseDatabase<R> implements AutoCloseable {
    protected Connection _connection;

    protected abstract String getLimitString(String sql, int page, int pageSize);

    /**
     * 执行数据库更新语句
     *
     * @param sql
     * @param paramValues
     * @return
     * @throws SQLException
     */
    public int execute(String sql, Object... paramValues) throws SQLException {
        System.out.println(sql);
        try (PreparedStatement pstmt = _connection.prepareStatement(sql)) {
            for (int i = 0; i < paramValues.length; i++) {
                Object param = paramValues[i];
                String v = null;
                if (param != null) {
                    v = param.toString();
                }
                pstmt.setString(i + 1, v);
            }
            return pstmt.executeUpdate();
        }
    }

    /**
     * 查询返回结果
     *
     * @param sql
     * @param paramValues
     * @return
     * @throws SQLException
     */
    public DataTable queryDataTable(String sql, Object... paramValues) throws SQLException {
        System.out.println(sql);
        try (PreparedStatement pstmt = _connection.prepareStatement(sql)) {
            for (int i = 0; i < paramValues.length; i++) {
                Object param = paramValues[i];
                String v = null;
                if (param != null) {
                    v = param.toString();
                }
                pstmt.setString(i + 1, v);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                return DBUtil.resultToDataTable(rs);
            }
        }
    }

    /**
     * 执行sql语句返回唯一数字
     *
     * @param sql
     * @param paramValues
     * @return
     * @throws SQLException
     */
    public int total(String sql, Object... paramValues) throws SQLException {
        DataTable dt = queryDataTable(sql, paramValues);
        DataRow s = dt.getRows().get(0);
        return Integer.parseInt(dt.getRows().get(0).get(0).toString());
    }

    /**
     * 根据sql分页查询
     *
     * @param sql
     * @param page
     * @param pageSize
     * @param paramValues
     * @return
     * @throws SQLException
     */
    public PageResultInfo<DataRow> queryPage(String sql, int page, int pageSize, Object... paramValues) throws SQLException {
        PageResultInfo<DataRow> pageResultInfo = new PageResultInfo<>();
        String sqlCount = DBUtil.getCountSql(sql);
        String sqlPage = this.getLimitString(sql, page, pageSize);
        int total = this.total(sqlCount, paramValues);
        pageResultInfo.setTotal(total);
        DataTable dt = queryDataTable(sqlPage, paramValues);
        pageResultInfo.setRows(dt.getRows());
        return pageResultInfo;
    }

    /**
     * sql查询返回实体类的集合
     *
     * @param sql
     * @param entityClass
     * @param paramValues
     * @return
     * @throws Exception
     */
    public List<R> queryBySql(String sql, Class<?> entityClass, Object... paramValues) throws SQLException {
        DataTable dt = queryDataTable(sql, paramValues);
        return dataTableToEntityList(dt, entityClass);
    }

    /**
     * 快速查询方法
     *
     * @param entity    方法会根据这个对象构造查询语句,对于int会使用等于,对于string会使用like....
     * @param restrains 附加查询限制条件
     * @return 返回实体类对象的集合
     * @throws Exception
     */
    public List<R> queryByEntity(Object entity, Restrain... restrains) throws SQLException {
        Class<?> c = entity.getClass();
        Table table = c.getAnnotation(Table.class);
        String tbName = table.value();
        String sql = "select * from " + tbName + " where 1=1 ";
        Method[] methods = c.getMethods();
        List<Object> paramValues = new ArrayList<Object>();
        for (Method method : methods) {
            if (method.getName().startsWith("get") && !method.getName().equals("getClass")) {
                Object value = null;
                try {
                    value = method.invoke(entity);
                } catch (Exception e) {
                    throw  new EasySqlException(e);
                }
                String pName = method.getName().substring(3);
                if (!ReflectUtil.isDefaultValue(value)) {
                    if (value.getClass().equals(String.class)) {
                        sql += " and " + pName + " like ?";
                        paramValues.add("%" + value + "%");
                    } else if (value.getClass().equals(Integer.class)) {
                        sql += " and " + pName + "=?";
                        paramValues.add(value);
                    }
                }
            }
        }

        for (Restrain restrain : restrains) {
            switch (restrain.getRestrainType()) {
                case eq:
                    sql += " and " + restrain.getKey() + "=?";
                    paramValues.add(restrain.getValues()[0]);
                    break;
            }

        }

        return queryBySql(sql, c, paramValues.toArray());
    }


    /**
     * 根据entity的id修改
     *
     * @param entity
     * @throws SQLException
     */
    public int update(Object entity) throws SQLException {
        Class<?> c = entity.getClass();
        Table table = c.getAnnotation(Table.class);
        String tbName = table.value();
        String sql = "update " + tbName + " set ";
        Method[] methods = c.getMethods();
        List<Object> paramValues = new ArrayList<Object>();
        for (Method method : methods) {
            if (method.getName().startsWith("get") && !method.getName().equals("getClass") && !method.getName().equals("getId")) {
                try {
                    Object value = method.invoke(entity);
                    if (!ReflectUtil.isDefaultValue(value)) {
                        String pName = method.getName().substring(3);
                        sql += pName + "=?,";
                        paramValues.add(value);
                    }
                } catch (Exception ex) {
                    throw new EasySqlException(ex);
                }
            }
        }
        sql = sql.substring(0, sql.length() - 1);
        try {
            Method getIdMethod = c.getMethod("getId");
            Object id = getIdMethod.invoke(entity);
            sql += " where id=?";
            paramValues.add(id);
        } catch (Exception ex) {
            throw new EasySqlException(ex);
        }
        return execute(sql, paramValues.toArray());
    }

    /**
     * 将实体类持久化到数据库中
     *
     * @param entity
     * @throws SQLException
     */
    public int save(Object entity) throws SQLException {
        Class<?> c = entity.getClass();
        Table table = c.getAnnotation(Table.class);
        String tbName = table.value();
        String sql = "insert into " + tbName + "(";
        Method[] methods = c.getMethods();
        List<Object> paramValues = new ArrayList<Object>();
        int pCount = 0;
        for (Method method : methods) {
            if (method.getName().startsWith("get") && !method.getName().equals("getClass")) {
                try {
                    Object value = method.invoke(entity);
                    if (!ReflectUtil.isDefaultValue(value)) {
                        String pName = method.getName().substring(3);
                        sql += pName + ",";
                        pCount++;
                        paramValues.add(value);
                    }
                } catch (Exception ex) {
                    throw new EasySqlException(ex);
                }
            }
        }
        sql = sql.substring(0, sql.length() - 1);
        sql += ") values(";

        for (int i = 0; i < pCount; i++) {
            sql += "?,";
        }
        sql = sql.substring(0, sql.length() - 1);
        sql += ")";
        return execute(sql, paramValues.toArray());
    }


    /**
     * 删除单个数据,根据id删除
     * @param entity
     * @return
     * @throws SQLException
     */
    public int remove(Object entity) throws SQLException {
        try {
            String tbName = entity.getClass().getAnnotation(Table.class).value();
            Object id = entity.getClass().getMethod("getId").invoke(entity);
            String sql = "delete from " + tbName + " where id=?";
            return execute(sql, id);
        } catch (SQLException e1) {
            throw e1;
        } catch (Exception ex) {
            throw new EasySqlException(ex);
        }
    }

    /**
     * 将DataTable对象转换为entityClas实体类对象
     *
     * @param dataTable
     * @param entityClass
     * @return
     * @throws Exception
     */
    private List<R> dataTableToEntityList(DataTable dataTable, Class<?> entityClass) {
        try {
            List<Object> list = new ArrayList<>();
            Method[] methods = entityClass.getMethods();
            for (DataRow dataRow : dataTable.getRows()) {
                Object entity = entityClass.newInstance();
                for (Method method : methods) {
                    if (method.getName().startsWith("set")) {
                        String pName = method.getName().substring(3);
                        Object value = dataRow.get(pName);
                        method.invoke(entity, value);
                    }
                }
                list.add(entity);
            }
            return (List<R>) list;
        }catch (Exception ex){
            throw new EasySqlException(ex);
        }
    }

    public  void close() throws SQLException{
        if(_connection!=null){
            _connection.close();
        }
    }
}
