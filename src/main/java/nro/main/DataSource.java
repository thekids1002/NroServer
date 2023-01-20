package nro.main;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {
    private static final String DB_URL = "jdbc:mysql://127.0.0.1/ngocrong";
    private static final String USER = "root";
    private static final String PASS = "";
    private static final HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    public static Connection connLogin = null;
    public static Connection connSaveData = null;
    public static Connection connUpdateLogout = null;

    public static Connection connCreate = null;

    public static boolean flagLogin = true;
    public static boolean flagUpdate = true;
    public static boolean flagCreate = true;

    public static int maximumPoolSize = 100;

    static {
        config.setJdbcUrl(DB_URL);
        config.setUsername(USER);
        config.setPassword(PASS);
        config.setAutoCommit(false);
        config.setMaximumPoolSize(maximumPoolSize);
        config.setConnectionTimeout(3000);
        config.addDataSourceProperty("cachePrepStmts", "true");
//        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSize", "50");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        ds = new HikariDataSource(config);

        try {
//            connLogin = ds.getConnection();
            connSaveData = ds.getConnection();
//            connUpdateLogout = ds.getConnection();
//            connCreate = ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private DataSource() { }

    public static Connection getConnection() throws SQLException {
//        System.out.println("SIZE POOL: " + ds.getMaximumPoolSize());
        countActiveConnection();
        return ds.getConnection();
    }

    public static Connection getConnectionLogin() {
        try {
            Connection conn = ds.getConnection();
//            if(conn == null) {
//                flagLogin = true;
//                return connLogin;
//            }
//            if(connLogin == null) {
//                connLogin = conn;
//                flagLogin = true;
//                return connLogin;
//            }
            flagLogin = false;
            return conn;
        } catch (SQLException e) {
            flagLogin = true;
            return connLogin;
        }
    }

    public static Connection getConnectionCreate() {
        try {
            Connection conn = ds.getConnection();
//            if(conn == null) {
//                flagCreate = true;
//                return connCreate;
//            }
//            if(connCreate == null) {
//                connCreate = conn;
//                flagCreate = true;
//                return connCreate;
//            }
            flagCreate = false;
            return conn;
        } catch (SQLException e) {
            flagCreate = true;
            return connCreate;
        }
    }

    public static Connection getConnectionLogout() {
        try {
            Connection conn = ds.getConnection();
//            if(conn == null) {
//                flagUpdate = true;
//                return connUpdateLogout;
//            }
//            if(connUpdateLogout == null) {
//                connUpdateLogout = conn;
//                flagUpdate = true;
//                return connUpdateLogout;
//            }
            flagUpdate = false;
            return conn;
        } catch (SQLException e) {
            flagUpdate = true;
            return connUpdateLogout;
        }
    }

    public static void countActiveConnection() {
        int countActive = ds.getHikariPoolMXBean().getActiveConnections();
        if(countActive >= maximumPoolSize - 1) {
            ds.close();

            ds = new HikariDataSource(config);

            try {
//            connLogin = ds.getConnection();
                connSaveData = ds.getConnection();
//            connUpdateLogout = ds.getConnection();
//            connCreate = ds.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
//        Util.log("countConnectionActive: " + countActive);
    }
}
