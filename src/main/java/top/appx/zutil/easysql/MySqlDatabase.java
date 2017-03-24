package top.appx.zutil.easysql;

/**
 * Created by qq799 on 2017/2/26.
 */
public class MySqlDatabase extends BaseDatabase {

    public MySqlDatabase(String url, String user, String password){
        this._connection = MysqlUtil.getConn(url,user,password);
    }

    @Override
    protected String getLimitString(String sql, int page, int pageSize) {
        return sql +" limit " +(page-1)*pageSize+","+pageSize;
    }

}
