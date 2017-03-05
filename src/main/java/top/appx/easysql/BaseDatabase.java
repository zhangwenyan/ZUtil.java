package top.appx.easysql;
import top.appx.easysql.data.DataRow;
import top.appx.easysql.data.DataTable;
import top.appx.zutil.info.PageInfo;
import top.appx.zutil.info.PageResultInfo;

import java.sql.*;
/**
 * Created by qq799 on 2017/2/26.
 */
public abstract class BaseDatabase implements AutoCloseable {
    protected Connection _connection;

    public int execute(String sql,Object... params) throws SQLException {
        PreparedStatement pstmt =null;
        try {
            pstmt = _connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                String v = null;
                if (param != null) {
                    v = param.toString();
                }
                pstmt.setString(i, v);
            }
            return pstmt.executeUpdate();
        }finally {
            if(pstmt!=null){
                pstmt.close();
            }
        }
    }

    public DataTable queryDataTable(String sql,Object... params) throws SQLException{
        PreparedStatement pstmt = null;
        try{
            pstmt = _connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                String v = null;
                if (param != null) {
                    v = param.toString();
                }
                pstmt.setString(i, v);
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
/*    public PageResultInfo<T> queryPage(T bean,PageInfo<T> pageInfo) throws SQLException{


    }*/


    public abstract void close() throws Exception;
}
