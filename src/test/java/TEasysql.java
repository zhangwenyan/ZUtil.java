import org.junit.Test;
import t.MenuEntity;
import t.UserEntity;
import top.appx.zutil.easysql.*;
import top.appx.zutil.eweb.PageInfo;
import top.appx.zutil.eweb.PageResultInfo;
import top.appx.zutil.eweb.Param;
import top.appx.zutil.info.ResultMap;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

/**
 * Created by lhzxd on 2017/3/12.
 */
public class TEasysql {
    private String url ="jdbc:mysql://localhost:3306/zweb?useUnicode=true&characterEncoding=utf-8";
    private String user = "root";
    private String password = "root";

    @Test
    public void t3()throws Exception{


    }
    @Test
    public void t1() throws Exception {
        BaseDatabase db = DBFactory.createMysqlDatabase(url,user,password);


        List<MenuEntity> list = db.queryBySql("select * from menu", MenuEntity.class);

     //   db.commTrans();


   //     DataTable dt = db.queryDataTable("select * from user");


       // PageInfo pageInfo =new PageInfo();
       // pageInfo.setPage(1);
      //  pageInfo.setPageSize(20);
       // pageInfo.setQuery(new UserEntity());
      //  PageResultInfo pageResultInfo = db.queryPage(pageInfo);

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

    @Test
    public void t2(){
        UserEntity userEntity = new UserEntity();
        Method[] methods = userEntity.getClass().getDeclaredMethods();
        for (Method method : methods) {
            System.out.println(method.getName());
        }
    }

}
