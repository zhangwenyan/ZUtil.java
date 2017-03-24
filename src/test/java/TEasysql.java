import org.junit.Test;
import t.UserEntity;
import top.appx.zutil.easysql.BaseDatabase;
import top.appx.zutil.easysql.DBFactory;
import top.appx.zutil.easysql.DataRow;
import top.appx.zutil.easysql.Restrain;
import top.appx.zutil.eweb.PageInfo;
import top.appx.zutil.eweb.PageResultInfo;

/**
 * Created by lhzxd on 2017/3/12.
 */
public class TEasysql {
    private String url ="jdbc:mysql://localhost:3306/zweb?useUnicode=true&characterEncoding=utf-8";
    private String user = "root";
    private String password = "root";

    @Test
    public void t1() throws Exception {
        BaseDatabase db = DBFactory.createMysqlDatabase(url,user,password);

        PageInfo pageInfo = new PageInfo();
        pageInfo.setQuery(new UserEntity());
        pageInfo.setPage(1);
        pageInfo.setPageSize(10);

        PageResultInfo<UserEntity> pageResultInfo = db.queryPage(pageInfo);
        System.out.println(pageResultInfo.getRows().get(0).getUsername());

    /*    UserEntity userEntity = new UserEntity();
        userEntity.setUsername("aaa");
        userEntity.setPassword("aasdfsad");
        db.saveAutoSetId(userEntity);
        System.out.println(userEntity.getId());
*/
/*
    UserEntity userEntity = new UserEntity();
    userEntity.setUsername("aaa");

    PageResultInfo<DataRow> pageResultInfo = db.queryPage("select * from user",1,20);

    System.out.println(pageResultInfo.getTotal());
    System.out.println(pageResultInfo.getRows().get(0).get("username"));
*/



    }
}
