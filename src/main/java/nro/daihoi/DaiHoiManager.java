package nro.daihoi;

import java.util.ArrayList;
import nro.player.Player;
import nro.map.Map;

public class DaiHoiManager {
    private static DaiHoiManager instance;
    
    public boolean openDHVT = false;
    public ArrayList<Integer> lstIDPlayers = new ArrayList<>();
    public ArrayList<Integer> lstIDPlayers2 = new ArrayList<>();
    public ArrayList<Player> lstPlayers = new ArrayList<>();
    public ArrayList<Player> lstPlayers2 = new ArrayList<>();
    public Map mapDhvt;
    public long tOpenDHVT = 0;
    public long tNextRound = 0;
    public byte roundNow = 0;
    public byte typeDHVT = 0;

    public int hourDHVT = 0;

    public static DaiHoiManager gI() {
        if (instance == null) {
            instance = new DaiHoiManager();
        }
        return instance;
    }

    public String nameRoundDHVT() {
        if(typeDHVT == (byte)1) {
            return "Nhi Đồng";
        } else if(typeDHVT == (byte)2) {
            return "Siêu Cấp 1";
        } else if(typeDHVT == (byte)3) {
            return "Siêu Cấp 2";
        } else if(typeDHVT == (byte)4) {
            return "Siêu Cấp 3";
        } else if(typeDHVT == (byte)5) {
            return "Ngoại Hạng";
        }
        return "Ngoại Hạng";
    }
    public String costRoundDHVT() {
        if(typeDHVT == (byte)1) {
            return "2 ngọc";
        } else if(typeDHVT == (byte)2) {
            return "4 ngọc";
        } else if(typeDHVT == (byte)3) {
            return "6 ngọc";
        } else if(typeDHVT == (byte)4) {
            return "8 ngọc";
        } else if(typeDHVT == (byte)5) {
            return "10000 vàng";
        }
        return "10000 vàng";
    }

    public boolean isAssignDHVT(int id) {
        for (Integer lstIDPlayer : lstIDPlayers) {
            if (lstIDPlayer == id) {
                return true;
            }
        }
        return false;
    }
}
