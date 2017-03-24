package top.appx.zutil.easysql;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by qq799 on 2017/2/26.
 */
public class DataRow extends HashMap<String,Object>{

    public DataRow(DataTable table){
        this.table = table;
    }
    /**
     * 所属DataTable
     */
    private DataTable table;
    //public DataTable getTable(){
      //  return table;
   // }

/*    @Override
    public Object get(Object key) {
        return null;
    }*/


    public Object scalar(){
        return this.get(this.keySet().iterator().next());
    }
}
