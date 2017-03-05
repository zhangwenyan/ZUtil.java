package top.appx.easysql.data;

import java.util.HashMap;

/**
 * Created by qq799 on 2017/2/26.
 */
public class DataRow extends HashMap<String,Object> {

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
}
