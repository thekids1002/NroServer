package nro.main;

import java.sql.*;

import nro.constant.Constant;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class HelperDAO {
    public static String getTopPower() {
        StringBuffer sb = new StringBuffer("");

        String SELECT_TOP_POWER = "SELECT name, power FROM player ORDER BY power DESC LIMIT " + Constant.MAX_TOP_POWER;
        PreparedStatement ps;
        ResultSet rs;
        Connection conn = null;
        try {
            conn = DataSource.getConnection();
            ps = conn.prepareStatement(SELECT_TOP_POWER);
            conn.setAutoCommit(false);

            rs = ps.executeQuery();
            byte i = 1;
            while(rs.next()) {
                sb.append(i).append(".").append(rs.getString("name")).append(": ").append(rs.getString("power")).append("\b");
                i++;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    public static String getTopCard() {
        StringBuffer sb = new StringBuffer("");

        String SELECT_TOP_CARD = "SELECT name, recharge FROM player WHERE recharge > 0 ORDER BY recharge DESC LIMIT " + Constant.MAX_TOP_CARD;
        PreparedStatement ps;
        ResultSet rs;
        Connection conn = null;
        try {
            conn = DataSource.getConnection();
            ps = conn.prepareStatement(SELECT_TOP_CARD);
            conn.setAutoCommit(false);

            rs = ps.executeQuery();
            byte i = 1;
            while(rs.next()) {
                sb.append(i).append(".").append(rs.getString("name")).append(": ").append(rs.getString("recharge")).append("\b");
                i++;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}
