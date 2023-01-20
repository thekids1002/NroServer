package nro.item;

import java.sql.*;

import nro.main.DataSource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import nro.part.Part;

public class ItemDAO {

    public static Item load(int itemId) {
        Item item = null;
        Connection conn = null;
        try {
            conn = DataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM item WHERE id=? LIMIT 1");
            ps.setInt(1, itemId);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                item = new Item();
                item.quantity = rs.getInt("quantity");
                item.template = ItemTemplates.get(rs.getShort("id"));
            }
            ps = conn.prepareStatement("SELECT * FROM item WHERE id=?");
            ps.setInt(1, itemId);
            rs = ps.executeQuery();
            while (rs.next()) {
                ItemOption option = new ItemOption();
                option.optionTemplate = ItemData.iOptionTemplates[rs.getInt("option_id")];
                option.param = rs.getShort("param");
                if (item != null) {
                    item.itemOptions.add(option);
                }
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
        return item;
    }

    public static void insertPartDB(int i, byte type, Part part) {
        String INSERT_LOG = "INSERT INTO parts (id, type, pi) VALUES (?,?,?)";
        PreparedStatement ps;
        try {
            Connection conn = DataSource.getConnection();
            ps = conn.prepareStatement(INSERT_LOG, Statement.RETURN_GENERATED_KEYS);
            conn.setAutoCommit(false);
            ps.setInt(1, i);
            ps.setInt(2, (int)type);

            JSONArray jarr = new JSONArray();
            for (int j = 0; j < part.pi.length; j++) {
                JSONObject objPart = new JSONObject();
                objPart.put((Object)"dx", (Object)part.pi[j].dx);
                objPart.put((Object)"dy", (Object)part.pi[j].dy);
                objPart.put((Object)"id", (Object)part.pi[j].id);
                jarr.add(objPart);
            }
            ps.setString(3, jarr.toJSONString());
            jarr.clear();
            ps.executeUpdate();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertItemDB(int id, byte type, byte gender, String name, String description, byte level, int strRequire, int iconID, short part) {
        String INSERT_LOG = "INSERT INTO item (id, type, gender, name, description, level, strRequire, iconID, part) VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps;
        try {
            Connection conn = DataSource.getConnection();
            ps = conn.prepareStatement(INSERT_LOG, Statement.RETURN_GENERATED_KEYS);
            conn.setAutoCommit(false);
            ps.setInt(1, id);
            ps.setInt(2, (int)type);
            ps.setInt(3, (int)gender);
            ps.setString(4, name);
            ps.setString(5, description);
            ps.setInt(6, (int)level);
            ps.setInt(7, (int)strRequire);
            ps.setInt(8, iconID);
            ps.setInt(9, (int)part);
            ps.executeUpdate();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
