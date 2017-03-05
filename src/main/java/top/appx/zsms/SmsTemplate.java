package top.appx.zsms;

/**
 * Created by lhzxd on 2017/3/5.
 */
public class SmsTemplate{
    private String code;
    private String content;

    public  SmsTemplate(String code,String content){
        this.code = code;
        this.content = content;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}