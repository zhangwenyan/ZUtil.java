import org.junit.Test;

/**
 * Created by lhzxd on 2017/3/12.
 */
public class T1 {
    @Test
    public void t1(){
        Integer a = null;
        Object obj = a;

        if(obj.equals(null)){
            System.out.println("asdfasd");
        }
    }
}
