package top.appx.easysql;

import top.appx.eweb.PageResultInfo;

import java.sql.SQLException;

import static top.appx.easysql.DBFactory.createMysqlDatabase;

/**
 * Created by qq799 on 2017/2/26.
 */
public class MySqlDBHelper {
    private String _url;
    private String _user;
    private String _password;

    public MySqlDBHelper(String url,String user,String password){
        this._url = url;
        this._user = user;
        this._password = password;
    }
    public int execute(String sql,Object... paramValues) throws SQLException {
        try(MySqlDatabase db = createMysqlDatabase(this._url,this._user,this._password)){
            return db.execute(sql,paramValues);
        }
        catch (SQLException e0){
            throw e0;
        }
        catch (Exception e) {
            throw new EasySqlException(e);
        }
    }
    public DataTable queryDataTable(String sql,Object... paramValues) throws SQLException{
       try(MySqlDatabase db = createMysqlDatabase(this._url,this._user,this._password)){
           return  db.queryDataTable(sql,paramValues);
       }catch (SQLException e0){
           throw e0;
       }
       catch (Exception e){
            throw  new EasySqlException(e);
       }
    }

    public PageResultInfo<DataRow> queryPage(String sql, int page,int pageSize,Object... paramValues) throws SQLException {
        try(MySqlDatabase db = createMysqlDatabase(this._url,this._user,this._password)){


            return db.queryPage(sql,page,pageSize,paramValues);
        }
        catch (SQLException e0){
            throw e0;
        }
        catch (Exception e){
            throw new EasySqlException(e);
        }
    }

}
