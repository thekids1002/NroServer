package nro.map;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import nro.main.DataSource;


public class MapData {
    public static ArrayList<WayPoint> loadListWayPoint(int mapId) {
        ArrayList<WayPoint> wayPoints = new ArrayList<>();
//        Connection conn = DBService.gI().getConnectionLocal();
        Connection conn = null;
        try {
            conn = DataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM map_waypoint WHERE map_id=?");
            ps.setInt(1, mapId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                WayPoint wp = new WayPoint();
                wp.minX = rs.getShort("min_x");
                wp.minY = rs.getShort("min_y");
                wp.maxX = rs.getShort("max_x");
                wp.maxY = rs.getShort("max_y");
                wp.name = rs.getString("name");
                wp.isEnter = rs.getBoolean("is_enter");
                wp.isOffline = rs.getBoolean("is_offline");
                wp.goMap = rs.getInt("go_map");
                wp.goX = rs.getShort("go_x");
                wp.goY = rs.getShort("go_y"); 
                wayPoints.add(wp);
            }
            conn.close();
        } catch (SQLException e) {
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
        return wayPoints;
    }
}
