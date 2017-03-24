package top.appx.zutil.easysql;

/**
 * Created by mrz on 2017/3/22.
 */
public class ExecuteResult {
    private int count;
    private Object generatedKey;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Object getGeneratedKey() {
        return generatedKey;
    }

    public void setGeneratedKey(Object generatedKey) {
        this.generatedKey = generatedKey;
    }
}
