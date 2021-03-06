package top.appx.zutil.easysql;

import top.appx.zutil.eweb.PageInfo;
import top.appx.zutil.eweb.PageResultInfo;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by lhzxd on 2017/3/17.
 */
public abstract   class BaseDBHelper<R> implements DatabaseMethodInterface {

    public abstract BaseDatabase createDatabase();

    /**
     * 执行数据库更新语句
     *
     * @param sql
     * @param paramValues
     * @return
     * @throws SQLException
     */
    @Override
    public int execute(String sql,Object... paramValues) throws SQLException {
        try(BaseDatabase db = createDatabase()){
            return db.execute(sql,paramValues);
        }
        catch (SQLException e0){
            throw e0;
        }
        catch (Exception e) {
            throw new EasySqlException(e);
        }
    }
    /**
     * 查询返回结果
     *
     * @param sql
     * @param paramValues
     * @return
     * @throws SQLException
     */
    @Override
    public DataTable queryDataTable(String sql,Object... paramValues) throws SQLException{
        try(BaseDatabase db = createDatabase()){
            return  db.queryDataTable(sql,paramValues);
        }catch (SQLException e0){
            throw e0;
        }
        catch (Exception e){
            throw  new EasySqlException(e);
        }
    }
    /**
     * 执行sql语句返回唯一数字
     *
     * @param sql
     * @param paramValues
     * @return
     * @throws SQLException
     */
    @Override
    public int total(String sql, Object... paramValues) throws SQLException {
        try(BaseDatabase db = createDatabase()) {
            return db.total(sql, paramValues);
        }
    }

    @Override
    public Object queryScalar(String sql, Object... paramValues) throws SQLException {
        return null;
    }

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
    @Override
    public PageResultInfo<DataRow> queryPage(String sql, int page, int pageSize, Object... paramValues) throws SQLException {
        try(BaseDatabase db = createDatabase()){
            return db.queryPage(sql,page,pageSize,paramValues);
        }
    }

    @Override
    public PageResultInfo queryPage(String sql, Class<?> entityClass, int page, int pageSize, Object... paramValues) throws SQLException {
        try(BaseDatabase db = createDatabase()){
            return db.queryPage(sql,entityClass,page,pageSize,paramValues);
        }
    }

    @Override
    public PageResultInfo queryPage(PageInfo pageInfo, Restrain... restrains) throws SQLException {
        try(BaseDatabase db = createDatabase()){
            return db.queryPage(pageInfo,restrains);
        }
    }

    /**
     * sql查询返回实体类的集合
     *
     * @param sql
     * @param entityClass
     * @param paramValues
     * @return
     * @throws Exception
     */
    @Override
    public List<R> queryBySql(String sql, Class<?> entityClass, Object... paramValues) throws SQLException {
        try(BaseDatabase db = createDatabase()){
            return db.queryBySql(sql,entityClass,paramValues);
        }
    }


    /**
     * 快速查询方法
     *
     * @param entity    方法会根据这个对象构造查询语句,对于int会使用等于,对于string会使用like....
     * @param restrains 附加查询限制条件
     * @return 返回实体类对象的集合
     * @throws Exception
     */
    @Override
    public List<R> queryByEntity(Object entity, Restrain... restrains) throws SQLException {
        try(BaseDatabase db = createDatabase()){
            return db.queryByEntity(entity,restrains);
        }
    }

    /**
     * 根据entity的id修改
     *
     * @param entity
     * @throws SQLException
     */
    @Override
    public int update(Object entity) throws SQLException {
        try(BaseDatabase db = createDatabase()){
            return db.update(entity);
        }
    }

    /**
     * 将实体类持久化到数据库中
     * @param entity
     * @return
     * @throws SQLException
     */
    @Override
    public int save(Object entity) throws SQLException {
        try(BaseDatabase db = createDatabase()){
            return db.save(entity);
        }
    }

    @Override
    public int saveAutoSetId(Object entity) throws SQLException {
        try(BaseDatabase db=  createDatabase()){
            return db.saveAutoSetId(entity);
        }
    }

    /**
     * 删除单个数据,根据id删除
     *
     * @param entity
     * @throws SQLException
     */
    @Override
    public int remove(Object entity) throws SQLException {
        try(BaseDatabase db = createDatabase()){
            return db.remove(entity);
        }
    }




}
