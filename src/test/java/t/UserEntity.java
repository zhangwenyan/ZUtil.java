package t;

import top.appx.zutil.easysql.Table;
import top.appx.zutil.eweb.Param;

/**
 * Created by mrz on 2017/3/23.
 */
@Table("user")
public class UserEntity {

    public void tt(@Param("ptest") String aaa){
        System.out.println("aaa");
    }
    private Integer id;
    private String username;
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
