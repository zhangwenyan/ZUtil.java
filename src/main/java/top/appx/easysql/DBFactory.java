package top.appx.easysql;

/**
 * Created by qq799 on 2017/2/26.
 */
public class DBFactory {
    private DBFactory(){}
    public static MySqlDatabase createMysqlDatabase(String url, String user, String password){
        return new MySqlDatabase(url,user,password);
    }
}
