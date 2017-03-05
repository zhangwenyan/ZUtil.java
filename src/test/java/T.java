import org.junit.Test;
import top.appx.easysql.MySqlDatabase;
import top.appx.zsms.SmsTemplate;
import top.appx.zsms.SmsTool_Alidayu;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qq799 on 2017/2/26.
 */
public class T {

    @Test
    public  void t1(){

        List<SmsTemplate> smsTemplateList = new ArrayList<SmsTemplate>();
        smsTemplateList.add(new SmsTemplate("SMS_26180244","您好${name}今天是${time}"));

        SmsTool_Alidayu smsTool_alidayu = new SmsTool_Alidayu("燎火","SMS_5075620","23300185","8b5196bef2e1ebcf5d1f75123503e2a4cd8",smsTemplateList);
        smsTool_alidayu.sendSms("17681109309","您好呀今天是星期天");
    }
}
