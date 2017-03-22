package top.appx.easysql;

import java.util.HashMap;

/**
 * Created by qq799 on 2017/2/26.
 */
public class DataRow{
    private HashMap<String,Object> map = new HashMap<>();

    public DataRow(DataTable table){
        this.table = table;
    }
    /**
     * 所属DataTable
     */
    private DataTable table;
    public DataTable getTable(){
        return table;
    }


    public void put(String key,Object value){
        map.put(key.toLowerCase(),value);
    }
    public Object get(String key){
        return map.get(key.toLowerCase());
    }

    public Object scalar(){
        return map.get( map.keySet().iterator().next());
    }
}
