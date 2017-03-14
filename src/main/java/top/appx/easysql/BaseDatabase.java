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

    public int execute(String sql,Object... params) throws SQLException {
        System.out.println(sql);
        PreparedStatement pstmt =null;
        try {
            pstmt = _connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                String v = null;
                if (param != null) {
                    v = param.toString();
                }
                pstmt.setString(i+1, v);
            }
            return pstmt.executeUpdate();
        }finally {
            if(pstmt!=null){
                pstmt.close();
            }
        }
    }

    public DataTable queryDataTable(String sql,Object... params) throws SQLException{
        System.out.println(sql);
        PreparedStatement pstmt = null;
        try{
            pstmt = _connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                String v = null;
                if (param != null) {
                    v = param.toString();
                }
                pstmt.setString(i+1, v);
            }
            ResultSet rs =  pstmt.executeQuery();
            return DBUtil.resultToDataTable(rs);
        }finally {
            if(pstmt!=null){
                pstmt.close();
            }
        }
    }
    protected abstract String getLimitString(String sql,int page,int pageSize);

    public int total(String sql,Object... paramValues) throws SQLException {
        DataTable dt = queryDataTable(sql,paramValues);
        DataRow s = dt.getRows().get(0);
        return Integer.parseInt(dt.getRows().get(0).get(0).toString());
    }

    public PageResultInfo<DataRow> queryPage(String sql, int page,int pageSize, Object... paramValues) throws SQLException {
        PageResultInfo<DataRow> pageResultInfo = new PageResultInfo<>();
        String sqlCount = DBUtil.getCountSql(sql);
        String sqlPage = this.getLimitString(sql,page,pageSize);
        int total = this.total(sqlCount,paramValues);
        pageResultInfo.setTotal(total);
        DataTable dt = queryDataTable(sqlPage,paramValues);
        pageResultInfo.setRows(dt.getRows());
        return pageResultInfo;
    }
    public List<R> queryBySql(String sql,Class<?> entityClass,Object... paramValues)throws Exception{
        DataTable dt = queryDataTable(sql,paramValues);
        return dataTableToEntityList(dt,entityClass);
    }
    public List<R> queryByEntity(Object entity,Restrain... restrains) throws Exception{
        Class<?> c = entity.getClass();
        Table table = c.getAnnotation(Table.class);
        String tbName = table.value();
        String sql = "select * from "+tbName+" where 1=1 ";
        Method[] methods = c.getMethods();
        List<Object> paramValues = new ArrayList<Object>();
        for (Method method : methods) {
            if(method.getName().startsWith("get") && !method.getName().equals("getClass")){
                Object value = method.invoke(entity);
                String pName = method.getName().substring(3);
                if(!ReflectUtil.isDefaultValue(value)){
                    if(value.getClass().equals(String.class)){
                        sql += " and "+ pName+" like ?";
                        paramValues.add("%"+value+"%");
                    }
                    else if(value.getClass().equals(Integer.class)){
                        sql += " and "+pName+"=?";
                        paramValues.add(value);
                    }
                }
            }
        }

        for (Restrain restrain : restrains) {
            switch (restrain.getRestrainType()){
                case eq:
                    sql += " and "+restrain.getKey()+"=?";
                    paramValues.add(restrain.getValues()[0]);
                    break;
            }

        }

        return queryBySql(sql,paramValues.toArray());
    }

    public List<R> queryPageBySql(String sql,int page,int pageSize,Restrain...  restrains)throws Exception{

    }
    public List<R> queryPageByEntity(Object entity,int page,int pageSize,Restrain... restrains)throws Exception{


        return null;
    }

    private List<R> dataTableToEntityList(DataTable dataTable,Class<?> entityClass)throws Exception{
        List<Object> list = new ArrayList<>();
        Method[] methods = entityClass.getMethods();
        for (DataRow dataRow : dataTable.getRows()) {
            Object entity = entityClass.newInstance();
            for (Method method : methods) {
                if(method.getName().startsWith("set")){
                    String pName = method.getName().substring(3);
                    Object value = dataRow.get(pName);
                    method.invoke(entity,value);
                }
            }
            list.add(entity);
        }

        return (List<R>)list;
    }


    public void modify(Object entity) throws SQLException {
        Class<?> c = entity.getClass();
        Table table = c.getAnnotation(Table.class);
        String tbName = table.value();
        String sql = "update "+tbName+" set ";
        Method[] methods = c.getMethods();
        List<Object> paramValues = new ArrayList<Object>();
        for (Method method : methods) {
            if(method.getName().startsWith("get") && !method.getName().equals("getClass") && !method.getName().equals("getId")){
                try {
                    Object value = method.invoke(entity);
                    if (!ReflectUtil.isDefaultValue(value)) {
                        String pName = method.getName().substring(3);
                        sql += pName+"=?,";
                        paramValues.add(value);
                    }
                }catch (Exception ex){
                    throw new EasySqlException(ex);
                }
            }
        }
        sql = sql.substring(0, sql.length()-1);
        try {
            Method getIdMethod = c.getMethod("getId");
            Object id = getIdMethod.invoke(entity);
            sql += " where id=?";
            paramValues.add(id);
        }catch (Exception ex){
            throw new EasySqlException(ex);
        }
        execute(sql,paramValues.toArray());
    }
    public void add(Object entity) throws SQLException {
        Class<?> c = entity.getClass();
        Table table = c.getAnnotation(Table.class);
        String tbName = table.value();
        String sql = "insert into "+tbName+"(";
        Method[] methods = c.getMethods();
        List<Object> paramValues = new ArrayList<Object>();
        int pCount = 0;
        for (Method method : methods) {
            if(method.getName().startsWith("get") && !method.getName().equals("getClass")){
                try {
                    Object value = method.invoke(entity);
                    if (!ReflectUtil.isDefaultValue(value)) {
                        String pName = method.getName().substring(3);
                        sql += pName + ",";
                        pCount++;
                        paramValues.add(value);
                    }
                }catch (Exception ex){
                    throw new EasySqlException(ex);
                }
            }
        }
        sql = sql.substring(0,sql.length()-1);
        sql+=") values(";

        for(int i=0;i<pCount;i++){
            sql +="?,";
        }
        sql = sql.substring(0,sql.length()-1);
        sql +=")";
        execute(sql,paramValues.toArray());
    }
    /**
     * 删除单个数据,根据id删除
     * @param entity
     * @throws SQLException
     */
    public void del(Object entity) throws SQLException{
        try {
            String tbName = entity.getClass().getAnnotation(Table.class).value();
            Object id = entity.getClass().getMethod("getId").invoke(entity);
            String sql = "delete from "+tbName+" where id=?";
            execute(sql,id);
        }
        catch (SQLException e1){
            throw  e1;
        }
        catch (Exception ex){
            throw new EasySqlException(ex);
        }
    }

    



    public abstract void close() throws Exception;
}
