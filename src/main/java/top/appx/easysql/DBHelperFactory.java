package top.appx.easysql;

/**
 * Created by qq799 on 2017/2/26.
 */
public class DBHelperFactory {
    private DBHelperFactory(){}
    public static MySqlDBHelper createMySqlDBHelper(String url,String user,String password){
        return new MySqlDBHelper(url,user,password);
    }
}
