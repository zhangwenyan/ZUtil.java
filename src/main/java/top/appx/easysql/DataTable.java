package top.appx.easysql;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qq799 on 2017/2/26.
 */
public class DataTable {
    private List<DataRow> rows;
    public List<DataRow> getRows(){
        if(rows==null){
            rows = new ArrayList<DataRow>();
        }
        return rows;
    }
}
