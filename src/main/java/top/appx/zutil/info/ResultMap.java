package top.appx.zutil.info;

import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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

    public ResultMap p(Object value){
        if(value instanceof HashMap){
            HashMap<String,Object> hashMap = (HashMap<String,Object>)value;
            Iterator<String> iterator = hashMap.keySet().iterator();
            while (iterator.hasNext()){
                String key = iterator.next();
                this.put(key,hashMap.get(key));
            }
            return this;
        }

        Method[] methods = value.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if(method.getName().startsWith("get")){
                String pName = method.getName().substring(3);
                pName = pName.substring(0,1).toLowerCase()+pName.substring(1);
                try {
                    this.put(pName,method.invoke(value));
                } catch (Exception e) {
                    throw new ResultMapException(e);
                }
            }
        }
        return this;
    }

}
