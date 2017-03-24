package top.appx.zutil.zsms;

import com.alibaba.fastjson.JSONObject;
import top.appx.zutil.DateTimeUtil;
import top.appx.zutil.HttpUtil;
import top.appx.zutil.PasswordUtil;
import top.appx.zutil.StringUtil;
import top.appx.zutil.info.ResultMap;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lhzxd on 2017/3/5.
 */

public class SmsTool_Alidayu extends BaseSmsTool {

    private String smsFreeSignName;//签名
    private String defaultSmsTemplateCode;//默认模板编号
    private String alidayu_appkey;//
    private String alidayu_secret;//
    private List<SmsTemplate> smsTemplateList;

    public SmsTool_Alidayu(String smsFreeSignName, String defaultSmsTemplateCode, String alidayu_appkey, String alidayu_secret, List<SmsTemplate> smsTemplateList){
        this.smsFreeSignName = smsFreeSignName;
        this.defaultSmsTemplateCode = defaultSmsTemplateCode;
        this.alidayu_appkey = alidayu_appkey;
        this.alidayu_secret = alidayu_secret;
        this.smsTemplateList = smsTemplateList;
    }

    @Override
    public void sendSms(String mbno, String msg) {
        try {
            msg = msg.replace(".", "．");
            String smsTemplateCode = null;
            String smsParam = null;

            //#region 匹配模板
            try {
                for (SmsTemplate smsTemplate : smsTemplateList) {
                    String regStr = smsTemplate.getContent().replaceAll("\\$\\{[a-z0-9]+\\}", "(.{0,15})");
                    regStr = regStr.replaceAll("\\[", "\\\\[");
                    regStr = regStr.replaceAll("]", "\\]");
                    Pattern pattern = Pattern.compile("^" + regStr + "$");
                    Matcher m = pattern.matcher(msg);
                    if (m.find()) {
                        Pattern pattern1 = Pattern.compile("\\$\\{([a-z0-9]+)\\}");
                        Matcher m1 = pattern1.matcher(smsTemplate.getContent());
                        HashMap<String, String> map = new HashMap<String, String>();
                        for (int i = 0; i < m.groupCount(); i++) {
                            m1.find();
                            map.put(m1.group(1),m.group(i+1));
                            System.out.println(m1.group(1)+","+m.group(i+1));
                        }
                        smsTemplateCode = smsTemplate.getCode();
                        smsParam = JSONObject.toJSONString(map);
                    }
                }
            } catch (Exception ex) {
                System.out.println("代码有缺陷，匹配模板时出错:" + ex.getMessage());
            }
            //#endregion 匹配模板

            if (smsTemplateCode == null) {
                smsTemplateCode = this.defaultSmsTemplateCode;
                smsParam = JSONObject.toJSONString(ResultMap.data("msg", msg));
            }

            if (StringUtil.isNullOrEmpty(smsTemplateCode) || smsParam == null) {
                throw new SmsToolErrorException("短信内容没有匹配的模板");
            }

            Map<String, Object> map =
                    ResultMap.data("app_key", this.alidayu_appkey)
                            .p("format", "json")
                            .p("v", "2.0")
                            .p("method", "alibaba.aliqin.fc.sms.num.send")
                            .p("timestamp", DateTimeUtil.now("yyyy-MM-dd HH:mm:ss"))
                            .p("sign_method", "md5")
                            .p("sms_type", "normal")
                            .p("sms_free_sign_name", this.smsFreeSignName)
                            .p("rec_num", mbno)
                            .p("sms_param", smsParam)
                            .p("sms_template_code", smsTemplateCode);
                            map.put("sign", signTopRequest(map, alidayu_secret, "md5"));


            String postStr = "";
            for(String key:map.keySet()){
                if(postStr!=""){
                    postStr += "&";
                }
                postStr+=key+"="+ URLEncoder.encode(map.get(key).toString(),"utf-8");
            }
            String rr = HttpUtil.httpPost("http://gw.api.taobao.com/router/rest",postStr);


            SmsResult rdy = JSONObject.parseObject(rr,SmsResult.class);
            if (rdy.alibaba_aliqin_fc_sms_num_send_response != null && rdy.alibaba_aliqin_fc_sms_num_send_response.result.success)
            {
                return;
            }  else if (rdy.error_response != null && rdy.error_response.sub_msg != null)
            {
                throw new SmsToolErrorException(rdy.error_response.sub_msg);
            }
            else
            {
                throw new SmsToolErrorException("失败");
            }

        }catch (Exception ex){
            throw new SmsToolErrorException(ex.getMessage());
        }


    }

    @Override
    public void init() {

    }

    @Override
    public String getMsg() {
        return "阿里大于短信平台";
    }

    @Override
    public void close() throws IOException {
        this.isDispose = true;
    }

    public static String signTopRequest(Map<String, Object> params, String secret, String signMethod) throws IOException {
        // 第一步：检查参数是否已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();
        if ("md5".equals(signMethod)) {
            query.append(secret);
        }
        for (String key : keys) {
            String value = params.get(key).toString();
            if (!StringUtil.isNullOrEmpty(key) &&!StringUtil.isNullOrEmpty(value)) {
                query.append(key).append(value);
            }
        }

        // 第三步：使用MD5/HMAC加密
        byte[] bytes;

        query.append(secret);
        bytes = PasswordUtil.md5(query.toString());

        // 第四步：把二进制转化为大写的十六进制
        return StringUtil.bytesToHexString(bytes).toUpperCase();
    }

}
