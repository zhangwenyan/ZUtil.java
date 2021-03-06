package top.appx.zutil.easysql;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qq799 on 2017/2/26.
 */
public class DBUtil {
    private DBUtil(){}

    public static List resultToList(ResultSet rs, Class<?> entityClass) {
        try {
            List list = new ArrayList();
            while (rs.next()) {
                Object entity = entityClass.newInstance();
                Method[] methods = entityClass.getMethods();
                for (Method method : methods) {
                    if (method.getName().startsWith("set")) {
                        String paramName = method.getName().substring(3);
                        Object value = rs.getObject(paramName);
                        if (value != null) {
                            Class typeClass = method.getParameterTypes()[0].getClass();
                            method.invoke(entity, value);
                        }
                    }
                }
                list.add(entity);
            }
            return list;
        } catch (Exception ex) {
            throw new EasySqlException(ex);
        }
    }


    public static DataTable resultToDataTable(ResultSet rs){
        try {
            DataTable dt = new DataTable();
            while (rs.next()) {
                int cc = rs.getMetaData().getColumnCount();
                DataRow dr = new DataRow(dt);
                for (int i = 0; i < cc; i++) {
                    String key = rs.getMetaData().getColumnName(i + 1);
                    Object value = rs.getObject(i + 1);
                    dr.put(key, value);
                }
                dt.add(dr);
            }
            return dt;
        }catch (Exception ex){
            throw new EasySqlException(ex);
        }
    }

    public static String getCountSql(String sql){
        return "select count(*) from ("+sql+") as easysql_count";
    }
/*
    public static String getSqlCount(String sql)
    {
        if (sql.IndexOf("order ") == -1)
        {
            return "select count(*) from ("+sql+ ") as easysql_tb";
        }
        else
        {
            return "select count(*) from (" + sql.Substring(0,sql.IndexOf("order ")) + ") as easysql_tb";
        }
    }
*/


}
