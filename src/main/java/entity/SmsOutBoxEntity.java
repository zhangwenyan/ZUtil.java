package entity;

import top.appx.easysql.Table;

/**
 * Created by lhzxd on 2017/3/13.
 */
@Table("smsOutBox")
public class SmsOutBoxEntity {
    private Integer id;
    private String mbno;
    private String msg;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMbno() {
        return mbno;
    }

    public void setMbno(String mbno) {
        this.mbno = mbno;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
