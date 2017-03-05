package top.appx.zutil.info;

import java.util.HashMap;

/**
 * Created by qq799 on 2017/2/25.
 */
public class ResultMap extends HashMap<String,Object> {
    public static ResultMap data(String key,Object value){
        ResultMap rm = new ResultMap();
        rm.put(key,value);
        return rm;
    }
    public ResultMap p(String key,Object value){
        this.put(key,value);
        return this;
    }

}
