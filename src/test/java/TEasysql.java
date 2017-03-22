import entity.SmsOutBoxEntity;
import entity.UserEntity;
import org.junit.Test;
import top.appx.easysql.BaseDatabase;
import top.appx.easysql.DBFactory;
import top.appx.easysql.Restrain;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by lhzxd on 2017/3/12.
 */
public class TEasysql {
    private String url ="jdbc:mysql://localhost:3306/smsdb?useUnicode=true&characterEncoding=utf-8";
    private String user = "root";
    private String password = "youotech";

    @Test
    public void t1() throws Exception {
        BaseDatabase db = DBFactory.createMysqlDatabase(url,"root","youotech");
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("aaa");
        userEntity.setPassword("aasdfsad");
        db.save(userEntity);

    }
}
