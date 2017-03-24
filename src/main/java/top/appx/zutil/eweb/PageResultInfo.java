package top.appx.zutil.eweb;

import java.util.List;

/**
 * Created by qq799 on 2017/2/26.
 */
public class PageResultInfo<T> {
    private int total;
    private List<T> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
