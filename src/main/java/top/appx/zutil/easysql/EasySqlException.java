package top.appx.zutil.easysql;

/**
 * Created by qq799 on 2017/2/26.
 */
public class EasySqlException extends RuntimeException {
    public EasySqlException(String msg){
        super(msg);
    }
    public EasySqlException(Exception ex){
        super(ex);
    }
}
