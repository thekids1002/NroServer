package nro.giftcode;

import nro.main.DataSource;
import nro.main.Service;
import nro.player.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GiftCodeManager {
    private ArrayList<GiftCode> listGiftCode = new ArrayList<>();
    private static GiftCodeManager instance;
    public static GiftCodeManager gI() {
        if (instance == null) {
            instance = new GiftCodeManager();
        }
        return instance;
    }

    public void init() {
        Connection conn = null;
        try {
            conn = DataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM giftcode");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                GiftCode giftcode = new GiftCode();
                giftcode.code = rs.getString("code");
                giftcode.Soluong = rs.getInt("soluong");
                

                JSONArray jar = (JSONArray) JSONValue.parse(rs.getString("detail"));
                if (jar != null) {
                    for (int i = 0; i < jar.size(); ++i) {
                        JSONObject jsonObj = (JSONObject) jar.get(i);
                        giftcode.detail.put(Integer.parseInt(jsonObj.get("id").toString()), Integer.parseInt(jsonObj.get("quantity").toString()));
                        jsonObj.clear();
                    }
                }
                listGiftCode.add(giftcode);
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
    }

    public GiftCode checkUseGiftCode(int idPlayer, String code) {
        for (GiftCode giftCode: listGiftCode) {
            if (giftCode.code.equals(code) && giftCode.Soluong > 0 && !giftCode.isUsedGiftCode(idPlayer)) {
                giftCode.Soluong -= 1;
                giftCode.addPlayerUsed(idPlayer);
                return giftCode;
            }
        }
        return null;
    }

    
    public void checkInfomationGiftCode(Player p) {
        StringBuilder sb = new StringBuilder();
        for (GiftCode giftCode: listGiftCode) {
            sb.append("Code: ").append(giftCode.code).append(", Count Left: ").append(giftCode.Soluong).append("\b");
        }
        Service.chatNPC(p, (short)24, sb.toString());
    }
}
