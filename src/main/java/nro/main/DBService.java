package nro.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBService {

    private static DBService instance;

    public static DBService gI() {
        if (instance == null) {
            instance = new DBService();
        }
        return instance;
    }

    public Connection getConnection() {
        Connection conn = null;
        try {
            String url = "jdbc:mysql://127.0.0.1/ngocrong";
//            String url = "jdbc:mysql://103.166.182.237:3306/server2";
            String user = "root";
//            String user = "foouser4";
            String password = "";
//            String password = "AAbbcc123abc";

            // create a connection to the database
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
    
    public static void main(String[] args) {
        Connection cn = DBService.gI().getConnection();
        if (cn != null) {
            System.out.println("ok");
        } else {
            System.out.println("lỗi");
        }
    }
    
    public static int resultSize(ResultSet rs){
        int count = 0;
        try {
            rs.last();
            count = rs.getRow();
            rs.beforeFirst();
        } catch (SQLException e) {
        }
        return count;
    }
}
