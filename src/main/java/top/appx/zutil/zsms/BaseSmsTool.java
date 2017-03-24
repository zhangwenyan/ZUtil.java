package top.appx.zutil.zsms;

import java.io.Closeable;

/**
 * Created by lhzxd on 2017/3/5.
 */
public abstract class BaseSmsTool implements Closeable {
    protected boolean isDispose;
    public  abstract void sendSms(String mbno,String msg);
    public  abstract void init();
    public abstract String getMsg();
   // public abstract

}
