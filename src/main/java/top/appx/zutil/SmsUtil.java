package top.appx.zutil;

import top.appx.zsms.ZSmsMethod;

/**
 * Created by qq799 on 2017/2/26.
 */
public class SmsUtil {
    private SmsUtil(){}
    public static void sendSms(String mbno,String msg){
        ZSmsMethod.sendSms(mbno,msg);
    }
}
