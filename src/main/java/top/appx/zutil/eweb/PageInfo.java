package top.appx.zutil.eweb;

/**
 * Created by qq799 on 2017/2/26.
 */
public class PageInfo {
    private int page;
    private int pageSize;
    public Object query;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setRows(int rows){
        this.pageSize = rows;
    }

    public Object getQuery() {
        return query;
    }

    public void setQuery(Object query) {
        this.query = query;
    }
}
