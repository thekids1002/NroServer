package nro.clan;

import java.util.ArrayList;

public class ClanManager {

    private ArrayList<Clan> clans;

    private static ClanManager instance;

    public static ClanManager gI() {
        if (instance == null) {
            instance = new ClanManager();
        }
        return instance;
    }

    public void init() {
        clans = ClanDAO.load();
        ClanService.gI().resetCountGas();
    }

    public ArrayList<Clan> getClans() {
        return clans;
    }

    public Clan getClanById(int id) {
        for (Clan clan : clans) {
            if (clan.id == id) {
                return clan;
            }
        }
        return null;
    }

    public int getClansSize() {
        return clans.size();
    }

    public int getLastClanID() {
        if(clans.size() > 0) {
            return clans.get(clans.size() - 1).id;
        } else {
            return 0;
        }
    }
    
    public ArrayList<Clan> search(String text){
        ArrayList<Clan> listClan = new ArrayList<>();
        for (Clan clan : clans) {
            if(text == "") {
                listClan.add(clan);
                if(listClan.size() >= 10) {
                    break;
                }
            } else {
                if (clan.name.startsWith(text)){
                    listClan.add(clan);
                    if(listClan.size() >= 10) {
                        break;
                    }
                }
            }
        }
        return listClan;
    }
    
    public ArrayList<Member> getMemberByIdClan(int id){
        for (Clan clan : clans) {
            if (clan.id == id) {
                return clan.members;
            }
        }
        return null;
    }
}
