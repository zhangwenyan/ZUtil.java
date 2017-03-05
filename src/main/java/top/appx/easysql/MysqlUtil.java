package top.appx.easysql;

import java.sql.*;
/**
 * Created by qq799 on 2017/2/26.
 */
public class MysqlUtil {
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("加载驱动异常");
            e.printStackTrace();
        }
    }

    public static Connection getConn(String url,String user,String password) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url,user, password);
        } catch (SQLException e) {
            System.out.println("获取数据库连接异常");
            e.printStackTrace();
        }
        return conn;
    }

    public static void close(ResultSet rs, PreparedStatement pstmt,Connection conn) {

        try {
            if (rs != null)
                rs.close();
            if (pstmt != null)
                pstmt.close();
            if (conn != null)
                pstmt.close();
        } catch (SQLException e) {
            System.out.println("关闭数据库对象异常");
            e.printStackTrace();
        }

    }

    public static void close(PreparedStatement pstmt, Connection conn) {
        try {
            if (pstmt != null)
                pstmt.close();
            if (conn != null)
                pstmt.close();
        } catch (SQLException e) {
            System.out.println("关闭数据库对象异常");
            e.printStackTrace();
        }
    }
}
