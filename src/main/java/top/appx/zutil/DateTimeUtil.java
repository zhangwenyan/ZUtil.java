package top.appx.zutil;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by qq799 on 2017/2/25.
 */
public class DateTimeUtil {
    public static Timestamp now(){
        return new Timestamp(System.currentTimeMillis());
    }
    public static String now(String pattern){
        return format(now(),pattern);
    }
    public static String format(Timestamp dt,String pattern){
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String dateString = formatter.format(dt);
        return dateString;
    }
}
