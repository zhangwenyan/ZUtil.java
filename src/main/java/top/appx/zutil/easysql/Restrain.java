package top.appx.zutil.easysql;

/**
 * Created by lhzxd on 2017/3/13.
 */
public class Restrain {
    private String key;
    private Object[] values;
    private RestrainType restrainType;
    private Restrain(String key,Object[] values,RestrainType restrainType){
        this.key = key;
        this.values = values;
        this.restrainType = restrainType;
    }

    public static Restrain between(String key, Object lo, Object hi)
    {
        return new Restrain(key, new Object[] { lo, hi }, RestrainType.between);
    }
    public static Restrain eq(String key, Object value)
    {
        return new Restrain(key, new Object[] { value }, RestrainType.eq);
    }
    public static Restrain lt(String key, Object value)
    {
        return new Restrain(key, new Object[] { value }, RestrainType.lt);
    }
    public static Restrain gt(String key, Object value)
    {
        return new Restrain(key, new Object[] { value }, RestrainType.gt);
    }
    public static Restrain not(String key, Object value)
    {
        return new Restrain(key, new Object[] { value }, RestrainType.not);
    }
    public static Restrain in(String key, Object[] values)
    {
        return new Restrain(key, values, RestrainType.inc);
    }
    public static Restrain notIn(String key, Object[] values)
    {
        return new Restrain(key, values, RestrainType.notin);
    }
    public static Restrain order(String key)
    {
        return new Restrain(key, null, RestrainType.order);
    }
    public static Restrain orderDesc(String key)
    {
        return new Restrain(key, null, RestrainType.orderdesc);
    }
    public static Restrain like(String key, String value)
    {
        return new Restrain(key, new Object[] { value }, RestrainType.like);
    }
    public static Restrain add(String value)
    {
        return new Restrain(null, new Object[] { value }, RestrainType.add);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    public RestrainType getRestrainType() {
        return restrainType;
    }

    public void setRestrainType(RestrainType restrainType) {
        this.restrainType = restrainType;
    }
}

