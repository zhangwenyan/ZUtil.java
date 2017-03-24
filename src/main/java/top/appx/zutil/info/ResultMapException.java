package top.appx.zutil.info;

/**
 * Created by mrz on 2017/3/24.
 */
public class ResultMapException extends RuntimeException {
    public ResultMapException(String msg){
        super(msg);
    }
    public ResultMapException(Exception ex){
        super(ex);
    }
}
