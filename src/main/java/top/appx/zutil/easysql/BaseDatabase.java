package top.appx.zutil.easysql;

import top.appx.zutil.eweb.MsgException;
import top.appx.zutil.eweb.PageInfo;
import top.appx.zutil.eweb.PageResultInfo;
import top.appx.zutil.ReflectUtil;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qq799 on 2017/2/26.
 */
public abstract class BaseDatabase<R> implements DatabaseMethodInterface,AutoCloseable {
    protected Connection _connection;
    protected abstract String getLimitString(String sql, int page, int pageSize);


    @Override
    public int execute(String sql, Object... paramValues) throws SQLException {
        return _execute(sql, false, paramValues).getCount();
    }


    /**
     * 执行数据库更新语句，返回影响的记录数量（和自动生成的id等(if generatedKey)）
     *
     * @param sql
     * @param generatedKey
     * @param paramValues
     * @return
     * @throws SQLException
     */
    private ExecuteResult _execute(String sql, boolean generatedKey, Object... paramValues) throws SQLException {
        System.out.println(sql);
        ExecuteResult er = new ExecuteResult();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            if (generatedKey) {
                pstmt = _connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            } else {
                pstmt = _connection.prepareStatement(sql);
            }
            for (int i = 0; i < paramValues.length; i++) {
                Object param = paramValues[i];
                String v = null;
                if (param != null) {
                    v = param.toString();
                }
                pstmt.setString(i + 1, v);
            }
            er.setCount(pstmt.executeUpdate());
            if (generatedKey) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    Object resultObj = rs.getObject(1);
                    er.setGeneratedKey(resultObj);
                } else {
                    throw new EasySqlException("no generatedKey");
                }
            }

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
        return er;
    }



    @Override
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


    @Override
    public int total(String sql, Object... paramValues) throws SQLException {
        return Integer.parseInt(queryScalar(sql,paramValues).toString());
    }


    @Override
    public Object queryScalar(String sql, Object... paramValues) throws SQLException{
        DataTable dt = queryDataTable(sql, paramValues);
        return dt.get(0).scalar();
    }


    @Override
    public PageResultInfo<DataRow> queryPage(String sql, int page, int pageSize, Object... paramValues) throws SQLException {
       return (PageResultInfo<DataRow>) queryPage(sql,DataRow.class,page,pageSize,paramValues);
    }

    @Override
    public PageResultInfo<R> queryPage(String sql, Class<?> entityClass, int page, int pageSize, Object... paramValues) throws SQLException{
        PageResultInfo<R> pageResultInfo = new PageResultInfo<>();
        String sqlCount = DBUtil.getCountSql(sql);
        String sqlPage = this.getLimitString(sql, page, pageSize);
        int total = this.total(sqlCount, paramValues);
        pageResultInfo.setTotal(total);
        DataTable dt = queryDataTable(sqlPage, paramValues);
        pageResultInfo.setRows(dataTableToEntityList(dt,entityClass));     ;
        return pageResultInfo;
    }

    @Override
    public PageResultInfo<R> queryPage(PageInfo pageInfo, Restrain... restrains) throws SQLException{
        Class<?> c = pageInfo.getQuery().getClass();
        Table table = c.getAnnotation(Table.class);
        if(table==null){
            throw new EasySqlException("entity must have Table annotation");
        }
        String sql = "select * from "+table.value()+" where 1=1 ";
        List<Object> paramValues = new ArrayList<>();
        sql += transSql(pageInfo.getQuery(),paramValues,restrains);
        return queryPage(sql,pageInfo.getQuery().getClass(),pageInfo.getPage(),pageInfo.getPageSize(),paramValues.toArray());
    }



    @Override
    public List<R> queryBySql(String sql, Class<?> entityClass, Object... paramValues) throws SQLException {
        DataTable dt = queryDataTable(sql, paramValues);
        return dataTableToEntityList(dt, entityClass);
    }
    private String transSql(Object entity,List<Object> paramValues,Restrain... restrains){
        Class<?> c = entity.getClass();
        Method[] methods = c.getDeclaredMethods();
        String sql = "";
        for(Method method:methods){
            if(method.getName().startsWith("get")){
                Object value = null;
                try{
                    value = method.invoke(entity);
                }catch (Exception e){
                    throw new EasySqlException(e);
                }
                String pName = method.getName().substring(3);
                if (!ReflectUtil.isDefaultValue(value)) {
                    if (value.getClass().equals(String.class) && value.toString().length()!=0) {
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
        return sql;
    }


    @Override
    public List<R> queryByEntity(Object entity, Restrain... restrains) throws SQLException {
        Class<?> c = entity.getClass();
        Table table = c.getAnnotation(Table.class);
        if(table==null){
            throw new EasySqlException("entity must have Table annotation");
        }
        String tbName = table.value();
        String sql = "select * from " + tbName + " where 1=1 ";
        List<Object> paramValues = new ArrayList<Object>();
        sql += transSql(entity,paramValues,restrains);
        return queryBySql(sql, c, paramValues.toArray());
    }



    @Override
    public int update(Object entity) throws SQLException {
        Class<?> c = entity.getClass();
        Table table = c.getAnnotation(Table.class);
        if(table==null){
            throw new EasySqlException("entity must have Table annotation");
        }
        String tbName = table.value();
        String sql = "update " + tbName + " set ";
        Method[] methods = c.getDeclaredMethods();
        List<Object> paramValues = new ArrayList<Object>();
        for (Method method : methods) {
            if (method.getName().startsWith("get")  && !method.getName().equals("getId")) {
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

    @Override
    public int save(Object entity) throws SQLException{
        return _save(entity,false);
    }
    @Override
    public int saveAutoSetId(Object entity) throws SQLException{
        return _save(entity,true);
    }
    /**
     * 将实体类持久化到数据库中
     * @param entity
     * @param autoSetId 是否自动设置id
     * @return
     * @throws SQLException
     */
    private int _save(Object entity, boolean autoSetId) throws SQLException {
        Class<?> c = entity.getClass();
        Table table = c.getAnnotation(Table.class);
        if(table==null){
            throw new EasySqlException("entity must have Table annotation");
        }
        String tbName = table.value();
        String sql = "insert into " + tbName + "(";
        Method[] methods = c.getDeclaredMethods();
        List<Object> paramValues = new ArrayList<Object>();
        int pCount = 0;
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
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
        ExecuteResult er = _execute(sql, autoSetId, paramValues.toArray());
        if (autoSetId) {
            Method method = null;
            try {
                method = c.getMethod("setId", int.class);
            } catch (NoSuchMethodException e) {
            }
            try {
                method = c.getMethod("setId", Integer.class);
            } catch (NoSuchMethodException e) {
            }
            if (method == null) {
                throw new EasySqlException("entity must has setId(int) or setId(Integer) method");
            }
            try {
                method.invoke(entity, Integer.parseInt(er.getGeneratedKey().toString()));
            } catch (Exception ex) {
                throw new EasySqlException(ex);
            }
        }
        return er.getCount();
    }






    @Override
    public int remove(Object entity) throws SQLException {
        try {
            Table table = entity.getClass().getAnnotation(Table.class);
            if(table==null){
                throw new EasySqlException("entity must have Table annotation");
            }
            String tbName = table.value();
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
        if(DataRow.class.equals(entityClass)){
            return (List<R>) dataTable;
        }
        try {
            List<Object> list = new ArrayList<>();
            Method[] methods = entityClass.getDeclaredMethods();
            for (DataRow dataRow : dataTable) {
                Object entity = entityClass.newInstance();
                for (Method method : methods) {
                    if (method.getName().startsWith("set")) {
                        String pName = method.getName().substring(3);
                        pName =  pName.substring(0,1).toLowerCase()+pName.substring(1);
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

    public  void beginTransaction() throws SQLException {
            _connection.setAutoCommit(false);
    }
    public  void commitTranscation() throws SQLException{
        try {
            _connection.commit();
        }finally {
            _connection.setAutoCommit(true);
        }
    }
    public  void rollbackTranscation()throws SQLException{
        try {
            _connection.rollback();
        }finally {
            _connection.setAutoCommit(true);
        }
    }

    @Override
    public  void close() throws SQLException{

        if(_connection!=null){
            _connection.close();
        }
    }
}
