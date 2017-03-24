package t;

import top.appx.zutil.easysql.Table;

/**
 * Created by mrz on 2017/3/23.
 */
@Table("menu")
public class MenuEntity {
    private Boolean open;

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }
}
