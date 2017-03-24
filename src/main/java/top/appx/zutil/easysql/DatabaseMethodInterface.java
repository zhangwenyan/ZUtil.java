package top.appx.zutil.easysql;

import top.appx.zutil.eweb.PageInfo;
import top.appx.zutil.eweb.PageResultInfo;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by mrz on 2017/3/22.
 */
public interface DatabaseMethodInterface {
    /**
     * 执行数据库更新语句，返回影响的记录数量
     *
     * @param sql
     * @param paramValues
     * @return
     * @throws SQLException
     */
    int execute(String sql,Object... paramValues) throws SQLException;
    /**
     * 查询返回结果
     *
     * @param sql
     * @param paramValues
     * @return
     * @throws SQLException
     */
    DataTable queryDataTable(String sql, Object... paramValues) throws SQLException;

    /**
     * 执行sql语句返回唯一数字
     * @param sql
     * @param paramValues
     * @return
     * @throws SQLException
     */
    int total(String sql, Object... paramValues) throws SQLException;

    /**
     * 执行sql语句返回唯一单结果
     * @param sql
     * @param paramValues
     * @return
     * @throws SQLException
     */
    Object queryScalar(String sql, Object... paramValues) throws SQLException;

    /**
     * 根据sql分页查询
     *
     * @param sql
     * @param page
     * @param pageSize
     * @param paramValues
     * @return
     * @throws SQLException
     */
    PageResultInfo<DataRow> queryPage(String sql, int page, int pageSize, Object... paramValues) throws SQLException;

    PageResultInfo queryPage(String sql, Class<?> entityClass, int page, int pageSize, Object... paramValues) throws SQLException;

    /**
     * 分页查询
     * @param pageInfo
     * @param restrains
     * @return
     * @throws SQLException
     */
    PageResultInfo queryPage(PageInfo pageInfo,Restrain... restrains) throws SQLException;

    /**
     * sql查询返回实体类的集合
     *
     * @param sql
     * @param entityClass
     * @param paramValues
     * @return
     * @throws Exception
     */
    List queryBySql(String sql, Class<?> entityClass, Object... paramValues) throws SQLException;

    /**
     * 快速查询方法
     *
     * @param entity    方法会根据这个对象构造查询语句,对于int会使用等于,对于string会使用like....
     * @param restrains 附加查询限制条件
     * @return 返回实体类对象的集合
     * @throws Exception
     */
    List queryByEntity(Object entity, Restrain... restrains) throws SQLException;
    /**
     * 根据entity的id修改
     *
     * @param entity
     * @throws SQLException
     */
    int update(Object entity) throws SQLException;

    /**
     * 将entity保存到数据库中
     * @param entity
     * @return
     * @throws SQLException
     */
    int save(Object entity) throws SQLException;

    /**
     * 将entity保存到数据库中，并自动设置产生的id
     * @param entity
     * @return
     * @throws SQLException
     */
    int saveAutoSetId(Object entity) throws SQLException;
    /**
     * 删除单个数据,根据id删除
     * @param entity
     * @return
     * @throws SQLException
     */
    int remove(Object entity) throws SQLException;

}
