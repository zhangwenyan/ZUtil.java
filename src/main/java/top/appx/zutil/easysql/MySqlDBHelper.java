package top.appx.zutil.easysql;

import static top.appx.zutil.easysql.DBFactory.createMysqlDatabase;

/**
 * Created by qq799 on 2017/2/26.
 */
public class MySqlDBHelper extends BaseDBHelper {
    private String _url;
    private String _user;
    private String _password;

    public MySqlDBHelper(String url,String user,String password){
        this._url = url;
        this._user = user;
        this._password = password;
    }

    @Override
    public BaseDatabase createDatabase() {
        return new MySqlDatabase(this._url,this._user,this._password);
    }
}
