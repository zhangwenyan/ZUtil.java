package top.appx.zutil.easysql;

/**
 * Created by lhzxd on 2017/3/13.
 */
public enum RestrainType {
    /**
     * 介于两者之间
     */
    between,
    /**
     * 等于
     */
    eq,
    /**
     * 大于
     */
    lt,
    /**
     * 小于
     */
    gt,
    /**
     * 不是
     */
    not,
    /**
     * 包含(同in)
     */
    inc,
    /**
     * 不包含
     */
    notin,
    /**
     * 正序排列
     */
    order,
    /**
     * 倒序排列
     */
    orderdesc,
    /**
     * 模糊查询
     */
    like,

    /**
     * 增加一个约束语句比如值可以是name like '%111%'
     */
    add,
}
