import entity.SmsOutBoxEntity;
import entity.UserEntity;
import org.junit.Test;
import top.appx.easysql.BaseDatabase;
import top.appx.easysql.DBFactory;
import top.appx.easysql.DataRow;
import top.appx.easysql.Restrain;
import top.appx.eweb.PageResultInfo;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by lhzxd on 2017/3/12.
 */
public class TEasysql {
    private String url ="jdbc:mysql://localhost:3306/eweb?useUnicode=true&characterEncoding=utf-8";
    private String user = "root";
    private String password = "root";

    @Test
    public void t1() throws Exception {
        BaseDatabase db = DBFactory.createMysqlDatabase(url,user,password);


    /*    UserEntity userEntity = new UserEntity();
        userEntity.setUsername("aaa");
        userEntity.setPassword("aasdfsad");
        db.saveAutoSetId(userEntity);
        System.out.println(userEntity.getId());
*/
    UserEntity userEntity = new UserEntity();
    userEntity.setUsername("aaa");

    PageResultInfo<DataRow> pageResultInfo = db.queryPage("select * from user",1,20);

    System.out.println(pageResultInfo.getTotal());
    System.out.println(pageResultInfo.getRows().get(0).get("username"));



    }
}
