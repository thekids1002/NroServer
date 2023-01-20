package nro.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import nro.item.ItemTemplate;
import nro.skill.SkillTemplate;
import nro.main.DataSource;
import nro.io.Session;

public class PlayerDAO {

    synchronized public static boolean create(Session userId, String name, int gender, int head) {
        String CREATE_PLAYER = "INSERT INTO player(account_id,name,power,vang,luong,luong_khoa,gender,head,where_id,where_x,where_y,limit_power,hp_goc,mp_goc,dame_goc,def_goc,crit_goc,tiem_nang,maxluggage,maxbox,skill,itembody,itembag,itembox,nhapthe,amulet,noitai,bean,task,starblack,card,itemuse,friends,thanhvien,sotien,sukien,nhanqua) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String UPDATE_HAVECHAR = "UPDATE account SET havechar=1 WHERE id=?";
        boolean check = false;
        JSONArray jarr = new JSONArray();
        JSONObject put = new JSONObject();

        ResultSet rss = null;
        int playerid = userId.userId;
        Connection conn = null;
        try {
            conn = DataSource.getConnection();
            PreparedStatement ps = null;
            ps = conn.prepareStatement(CREATE_PLAYER, Statement.RETURN_GENERATED_KEYS);
            conn.setAutoCommit(false);
            ps.setInt(1, playerid);
            ps.setString(2, name);
            //sức mạnh
            ps.setLong(3, 1200);
            //vàng
            ps.setLong(4, 20000);
            //ngọc
            ps.setInt(5, 20);
            //ngọc tím
            ps.setInt(6, 0);
            ps.setInt(7, gender);
            ps.setInt(8, head);
            ps.setInt(9, gender + 39);
            ps.setInt(10, 180);
            ps.setInt(11, 384);
            ps.setInt(12, 0);
            switch (gender) {
                case 0:
                    ps.setInt(13, 200);
                    ps.setInt(14, 100);
                    ps.setInt(15, 12);
                    break;
                case 1:
                    ps.setInt(13, 100);
                    ps.setInt(14, 200);
                    ps.setInt(15, 12);
                    break;
                case 2:
                    ps.setInt(13, 100);
                    ps.setInt(14, 100);
                    ps.setInt(15, 15);
                    break;
            }
            ps.setInt(16, 0);
            ps.setInt(17, 0);
            ps.setLong(18, 1200);
            ps.setLong(19, 20);
            ps.setLong(20, 20);
            switch (gender) {
                case 0:
                    put.put((Object) "id", (Object) 0);
                    put.put((Object) "point", (Object) 1);
                    put.put((Object) "lastuse", (Object) 0);
                    jarr.add(put);
                    ps.setString(21, jarr.toJSONString());
                    jarr.clear();
                    break;
                case 1:
                    put.put((Object) "id", (Object) 2);
                    put.put((Object) "point", (Object) 1);
                    put.put((Object) "lastuse", (Object) 0);
                    jarr.add(put);
                    ps.setString(21, jarr.toJSONString());
                    jarr.clear();
                    break;
                case 2:
                    put.put((Object) "id", (Object) 4);
                    put.put((Object) "point", (Object) 1);
                    put.put((Object) "lastuse", (Object) 0);
                    jarr.add(put);
                    ps.setString(21, jarr.toJSONString());
                    jarr.clear();
                    break;
            }
            jarr.clear();
            ps.setString(22, "[]");
            ps.setString(23, "[]");
            ps.setString(24, "[]");
            ps.setInt(25, 0);
            ps.setString(26, "[{\"id\":213,\"point\":0},{\"id\":214,\"point\":0},{\"id\":215,\"point\":0},{\"id\":216,\"point\":0},{\"id\":217,\"point\":0},{\"id\":218,\"point\":0},{\"id\":219,\"point\":0},{\"id\":522,\"point\":0},{\"id\":761,\"point\":0},{\"id\":762,\"point\":0}]");
            ps.setString(27, "[]");
            ps.setString(28, "[{\"level\":1,\"time\":0,\"max\":5}]");
            ps.setString(29, "[{\"index\":0,\"count\":0}]");
            ps.setString(30, "[{\"index\":1,\"time\":0,\"timeend\":0},{\"index\":2,\"time\":0,\"timeend\":0},{\"index\":3,\"time\":0,\"timeend\":0},{\"index\":4,\"time\":0,\"timeend\":0},{\"index\":5,\"time\":0,\"timeend\":0},{\"index\":6,\"time\":0,\"timeend\":0},{\"index\":7,\"time\":0,\"timeend\":0}]");
            ps.setString(31, "[{\"id\":828,\"amount\":0,\"level\":0,\"use\":1,\"unlock\":0},{\"id\":829,\"amount\":0,\"level\":0,\"use\":1,\"unlock\":0},{\"id\":830,\"amount\":0,\"level\":0,\"use\":1,\"unlock\":0},{\"id\":831,\"amount\":0,\"level\":0,\"use\":0,\"unlock\":0},{\"id\":832,\"amount\":0,\"level\":0,\"use\":0,\"unlock\":0},{\"id\":833,\"amount\":0,\"level\":0,\"use\":0,\"unlock\":0},{\"id\":834,\"amount\":0,\"level\":0,\"use\":0,\"unlock\":0},{\"id\":835,\"amount\":0,\"level\":0,\"use\":0,\"unlock\":0},{\"id\":836,\"amount\":0,\"level\":0,\"use\":0,\"unlock\":0},{\"id\":837,\"amount\":0,\"level\":0,\"use\":0,\"unlock\":0},{\"id\":838,\"amount\":0,\"level\":0,\"use\":0,\"unlock\":0},{\"id\":839,\"amount\":0,\"level\":0,\"use\":0,\"unlock\":0},{\"id\":840,\"amount\":0,\"level\":0,\"use\":0,\"unlock\":0},{\"id\":841,\"amount\":0,\"level\":0,\"use\":0,\"unlock\":0},{\"id\":842,\"amount\":0,\"level\":0,\"use\":0,\"unlock\":0},{\"id\":859,\"amount\":0,\"level\":0,\"use\":0,\"unlock\":0},{\"id\":956,\"amount\":0,\"level\":0,\"use\":1,\"unlock\":0}]");
            ps.setString(32, "[]");
            ps.setString(33, "[]");
            ps.setInt(34, 0);
            ps.setInt(35, 0);
            ps.setInt(36, 0);
            ps.setInt(37, 0);
            if (ps.executeUpdate() == 1) {
                check = true;
            }
            conn.commit();
            //UPDATE HAVE PET
            ps = conn.prepareStatement(UPDATE_HAVECHAR);
            conn.setAutoCommit(false);
            ps.setInt(1, playerid);
            if (ps.executeUpdate() == 1) {
            }
            conn.commit();
            //UPDATE HAVE PET
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
        return check;
    }

    public static void updateDB(Player player) {
        String UPDATE_PLAYER = "UPDATE player SET power=?,vang=?,luong=?,luong_khoa=?,clan_id=?,task_id=?,head=?,where_id=?,where_x=?,where_y=?,maxluggage=?,maxbox=?,hp_goc=?,"
                + "mp_goc=?,dame_goc=?,def_goc=?,crit_goc=?,tiem_nang=?,itembody=?,itembag=?,itembox=?,nhapthe=?,skill=?,amulet=?,limit_power=?,noitai=?,count_noitai=?,role_pt=?,bean=?,"
                + "task=?,starblack=?,nhapthe2=?,hasmabu=?,istennis=?,card=?,logout=?,itemuse=?,crit_nr=?,friends=?,thanhvien=?,sotien=?,sukien=?,nhanqua=? WHERE account_id=?";
        String UPDATE_PET = "UPDATE pet SET power=?,head=?,hp_goc=?,mp_goc=?,dame_goc=?,def_goc=?,crit_goc=?,tiem_nang=?,itembody=?,skill=?,gender=?,name=?,limit_power=?,stamina=?,ismabu=?,isdie=?,isberus=? WHERE account_id=?";
        String INSERT_PET = "INSERT INTO pet (name, power, head, gender, account_id, hp_goc, mp_goc, dame_goc, def_goc, crit_goc, tiem_nang, limit_power, skill, itembody) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String UPDATE_ISON = "UPDATE account SET isOn=? WHERE id=?";
        Connection conn;
        JSONArray jarr = new JSONArray();
        PreparedStatement ps;
        try {
//            conn = DataSource.connUpdateLogout;
            conn = DataSource.getConnectionLogout();
            ps = conn.prepareStatement(UPDATE_PLAYER);
            conn.setAutoCommit(false);
            ps.setLong(1, player.power);
            ps.setLong(2, player.vang);
            ps.setInt(3, player.ngoc);
            ps.setInt(4, player.ngocKhoa);
            ps.setInt(40, player.thanhvien);
            ps.setInt(41, player.sotien);//cuoi
            ps.setInt(42, player.pointSuKien);//cuoi
             ps.setInt(43, player.nhanqua);//cuoi
//            ps.setInt(5, player.clanid); //SERVER 2
            if (player.clan != null) {
                ps.setInt(5, player.clan.id);
            } else {
                ps.setInt(5, -1);
            }
            ps.setInt(6, player.taskId);
            ps.setInt(7, player.head);
            ps.setInt(8, player.map.id);
            ps.setInt(9, player.x);
            ps.setInt(10, player.y);
            ps.setInt(11, player.maxluggage);
            ps.setInt(12, player.maxBox);
            ps.setInt(13, player.hpGoc);
            ps.setInt(14, player.mpGoc);
            ps.setInt(15, player.damGoc);
            ps.setInt(16, player.defGoc);
            ps.setInt(17, player.critGoc);
            ps.setLong(18, player.tiemNang);
            byte j;
            for (j = 0; j < player.ItemBody.length; ++j) {
                if (player.ItemBody[j] != null && player.ItemBody[j].id != -1) {
                    if ((player.ItemBody[j].timeHSD > 0 && (player.ItemBody[j].timeHSD - 3600000) > System.currentTimeMillis()) || player.ItemBody[j].timeHSD == 0) {
                        jarr.add(ItemTemplate.ObjectItem(player.ItemBody[j], j));
                    }
                }
            }
            ps.setString(19, jarr.toJSONString());
            jarr.clear();
            for (j = 0; j < player.ItemBag.length; ++j) {
                if (player.ItemBag[j] != null && player.ItemBag[j].id != -1) {
                    jarr.add(ItemTemplate.ObjectItem(player.ItemBag[j], j));
                }
            }
            ps.setString(20, jarr.toJSONString());
            jarr.clear();
            for (j = 0; j < player.ItemBox.length; ++j) {
                if (player.ItemBox[j] != null && player.ItemBox[j].id != -1) {
                    jarr.add(ItemTemplate.ObjectItem(player.ItemBox[j], j));
                }
            }
            ps.setString(21, jarr.toJSONString());
            jarr.clear();
            ps.setInt(22, player.NhapThe);
            for (j = 0; j < player.listSkill.size(); j++) {
                if (player.listSkill.get(j) != null) {
                    jarr.add(SkillTemplate.ObjectSkill(player.listSkill.get(j), player.getSkillByIDTemplate(player.listSkill.get(j).skillId).lastTimeUseThisSkill));
                }
            }
            ps.setString(23, jarr.toJSONString());
            jarr.clear();
            for (j = 0; j < player.listAmulet.size(); j++) {
                if (player.listAmulet.get(j) != null) {
                    jarr.add(player.listAmulet.get(j).ObjectAmulet()); ////////
                }
            }
            ps.setString(24, jarr.toJSONString());
            jarr.clear();
            ps.setInt(25, (int) player.limitPower);
            //NOITAI
            jarr.add(player.noiTai.ObjectNoiTai());
            ps.setString(26, jarr.toJSONString());
            jarr.clear();
            //END NOI TAI
            ps.setInt(27, (int) (player.countMoNoiTai));
            //ROLE BANG HOI
            ps.setInt(28, (int) (player.rolePT));
            //DAU THAN
            jarr.add(player.ObjectBean());
            ps.setString(29, jarr.toJSONString());
            jarr.clear();
            //TASK
            jarr.add(player.ObjectTask());
            ps.setString(30, jarr.toJSONString());
            jarr.clear();
            //NGOC RONG SAO DEN
            for (j = (byte) 0; j < (byte) 7; j++) {
                jarr.add(player.ObjectNRSD(j)); ////////
            }
            ps.setString(31, jarr.toJSONString());
            jarr.clear();
            ps.setInt(32, player.isPorata2 ? 1 : 0); //nhap the 2

            ps.setInt(33, player.hasTrungMabu ? 1 : 0); //Has Trung MaBu
            ps.setInt(34, player.isTennis ? 1 : 0); //chien thuyen tennis
            //RADA CARD
            for (j = (byte) 0; j < player.cards.size(); j++) {
                jarr.add(player.ObjectCard(player.cards.get(j))); ////////
            }
            ps.setString(35, jarr.toJSONString());
            jarr.clear();
            //TIME LOGOUT
            ps.setString(36, Long.toString(System.currentTimeMillis() + 20000));

            //ITEM USE
            long timeItem = 0;
            if (player.timerCSKB != null) {
                timeItem = player.timeEndCSKB - System.currentTimeMillis();
                if (timeItem > 30000) {
                    jarr.add(player.ObjectItemUse(379, timeItem));
                }
            }
            if (player.timerCN != null) {
                timeItem = player.timeEndCN - System.currentTimeMillis();
                if (timeItem > 30000) {
                    jarr.add(player.ObjectItemUse(381, timeItem));
                }
            }
            if (player.timerBH != null) {
                timeItem = player.timeEndBH - System.currentTimeMillis();
                if (timeItem > 30000) {
                    jarr.add(player.ObjectItemUse(382, timeItem));
                }
            }
            if (player.timerBK != null) {
                timeItem = player.timeEndBK - System.currentTimeMillis();
                if (timeItem > 30000) {
                    jarr.add(player.ObjectItemUse(383, timeItem));
                }
            }
            if (player.timerGX != null) {
                timeItem = player.timeEndGX - System.currentTimeMillis();
                if (timeItem > 30000) {
                    jarr.add(player.ObjectItemUse(384, timeItem));
                }
            }
            if (player.timerTM != null) {
                timeItem = player.timeEndTM - System.currentTimeMillis();
                if (timeItem > 30000) {
                    jarr.add(player.ObjectItemUse(1016, timeItem));
                }
            }
            if (player.timerTA != null) {
                timeItem = player.timeEndTA - System.currentTimeMillis();
                if (timeItem > 30000) {
                    jarr.add(player.ObjectItemUse(player.idTAUse, timeItem));
                }
            }
            ps.setString(37, jarr.toJSONString());
            jarr.clear();
            //CRIT UOC RONG THAN
            ps.setInt(38, player.critNr);
            //FRIEND
            for (j = (byte) 0; j < player.friends.size(); j++) {
                jarr.add(player.ObjectFriend(player.friends.get(j)));
            }
            ps.setString(39, jarr.toJSONString());
            jarr.clear();

            ps.setInt(44, player.id);
            if (ps.executeUpdate() == 1) {
            }
            conn.commit();
            //UPDATE STATUS LOGIN
            ps = conn.prepareStatement(UPDATE_ISON);
            conn.setAutoCommit(false);
            ps.setInt(1, 0);
            ps.setInt(2, player.id);
            if (ps.executeUpdate() == 1) {
            }
            conn.commit();

            if (player.havePet == 1) {
                ps = conn.prepareStatement(UPDATE_PET);
                conn.setAutoCommit(false);
                ps.setLong(1, player.detu.power);
                ps.setInt(2, player.detu.head);
                ps.setInt(3, player.detu.hpGoc);
                ps.setInt(4, player.detu.mpGoc);
                ps.setInt(5, player.detu.damGoc);
                ps.setInt(6, player.detu.defGoc);
                ps.setInt(7, player.detu.critGoc);
                ps.setLong(8, player.detu.tiemNang);
                for (j = 0; j < player.detu.ItemBody.length; ++j) {
                    if (player.detu.ItemBody[j] != null && player.detu.ItemBody[j].id != -1) {
                        jarr.add(ItemTemplate.ObjectItem(player.detu.ItemBody[j], j));
                    }
                }
                ps.setString(9, jarr.toJSONString());
//                System.out.println("OBJECT ITEAM BODY PET: " + jarr.toJSONString());
                jarr.clear();
                for (j = 0; j < player.detu.listSkill.size(); j++) {
                    if (player.detu.listSkill.get(j) != null) {
                        jarr.add(SkillTemplate.ObjectSkillPet(player.detu.listSkill.get(j)));
                    }
                }
                ps.setString(10, jarr.toJSONString());
//                System.out.println("OBJECT SKILL PET: " + jarr.toJSONString());
                jarr.clear();
                ps.setInt(11, player.detu.gender);
                ps.setString(12, player.detu.name);
                ps.setInt(13, (int) player.detu.limitPower);
                ps.setInt(14, (int) player.detu.stamina);
                ps.setInt(15, player.detu.isMabu ? 1 : 0);
                ps.setInt(16, player.detu.isdie ? 1 : 0);
                ps.setInt(17, player.detu.isBerus ? 1 : 0);
                ps.setInt(18, player.id);
                if (ps.executeUpdate() == 1) {
                }
                conn.commit();
            }
            if (player.isNewPet) {
                player.isNewPet = false;

                ps = conn.prepareStatement(INSERT_PET);
                conn.setAutoCommit(false);
                ps.setString(1, player.detu.name);
                ps.setLong(2, player.detu.power);
                ps.setInt(3, player.detu.head);
                ps.setInt(4, player.detu.gender);
                ps.setInt(5, player.id);
                ps.setInt(6, player.detu.hpGoc);
                ps.setInt(7, player.detu.mpGoc);
                ps.setInt(8, player.detu.damGoc);
                ps.setInt(9, player.detu.defGoc);
                ps.setInt(10, player.detu.critGoc);
                ps.setLong(11, player.detu.tiemNang);
                ps.setInt(12, (int) player.detu.limitPower);
                jarr.clear();
                for (j = 0; j < player.detu.listSkill.size(); j++) {
                    if (player.detu.listSkill.get(j) != null) {
                        jarr.add(SkillTemplate.ObjectSkillPet(player.detu.listSkill.get(j)));
                    }
                }
//                System.out.println("OBJECT SKILL PET: " + jarr.toJSONString());
                ps.setString(13, jarr.toJSONString());
                ps.setString(14, "[]");
                jarr.clear();
                if (ps.executeUpdate() == 1) {
                }
                conn.commit();
            }
            if (!DataSource.flagUpdate) {
                conn.close();
            }
        } catch (Exception e) {
        }
    }

    public static void updateNRSD(int id, byte idNRSD) {
        String UPDATE_NRSD = "UPDATE player SET starblack=? WHERE id=?";
        JSONArray jarr = new JSONArray();
        JSONObject put = new JSONObject();

        ResultSet rss = null;
        int playerid = id;
        Connection conn = null;
        try {
            conn = DataSource.getConnection();
            PreparedStatement ps = null;
            //UPDATE NRSD
            ps = conn.prepareStatement(UPDATE_NRSD);
            conn.setAutoCommit(false);
            //NGOC RONG SAO DEN
            for (byte j = 0; j < (byte) 7; j++) {
                JSONObject putNRSD = new JSONObject();
                putNRSD.put((Object) "index", (Object) j);
                if (j == idNRSD) {
                    putNRSD.put((Object) "time", (Object) (System.currentTimeMillis() - (long) 3600000));
                    putNRSD.put((Object) "timeend", (Object) (System.currentTimeMillis() + (long) (86400000)));
                } else {
                    putNRSD.put((Object) "time", (Object) (0));
                    putNRSD.put((Object) "timeend", (Object) (0));
                }
                jarr.add(putNRSD); ////////
            }
            ps.setString(1, jarr.toJSONString());
            jarr.clear();

            ps.setInt(2, playerid);
            if (ps.executeUpdate() == 1) {
            }
            conn.commit();
            //UPDATE NRSD
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

    public static void logTradeDB(int id1, String name1, int id2, String name2, String item1, String item2, Connection conn) {
        String INSERT_LOG = "INSERT INTO logtrade (id_1, name_1, id_2, name_2, item_1, item_2) VALUES (?,?,?,?,?,?)";
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(INSERT_LOG, Statement.RETURN_GENERATED_KEYS);
            conn.setAutoCommit(false);
            ps.setInt(1, id1);
            ps.setString(2, name1);
            ps.setInt(3, id2);
            ps.setString(4, name2);
            ps.setString(5, item1);
            ps.setString(6, item2);
            if (ps.executeUpdate() == 1) {
            }
            conn.commit();
        } catch (Exception e) {
        }
    }

    public static void updateDBAuto(Player player, Connection conn) {
        String UPDATE_PLAYER = "UPDATE player SET power=?,vang=?,luong=?,luong_khoa=?,clan_id=?,task_id=?,head=?,where_id=?,where_x=?,where_y=?,maxluggage=?,maxbox=?,hp_goc=?,"
                + "mp_goc=?,dame_goc=?,def_goc=?,crit_goc=?,tiem_nang=?,itembody=?,itembag=?,itembox=?,nhapthe=?,skill=?,amulet=?,limit_power=?,noitai=?,count_noitai=?,role_pt=?,bean=?,"
                + "task=?,starblack=?,nhapthe2=?,hasmabu=?,istennis=?,card=?,logout=?,itemuse=?,crit_nr=?,friends=?,thanhvien=?,sotien=?,sukien=?,nhanqua=? WHERE account_id=?";
        String UPDATE_PET = "UPDATE pet SET power=?,head=?,hp_goc=?,mp_goc=?,dame_goc=?,def_goc=?,crit_goc=?,tiem_nang=?,itembody=?,skill=?,gender=?,name=?,limit_power=?,stamina=?,ismabu=?,isdie=?,isberus=? WHERE account_id=?";
        String INSERT_PET = "INSERT INTO pet (name, power, head, gender, account_id, hp_goc, mp_goc, dame_goc, def_goc, crit_goc, tiem_nang, limit_power, skill, itembody) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        JSONArray jarr = new JSONArray();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(UPDATE_PLAYER);
            conn.setAutoCommit(false);
            ps.setLong(1, player.power);
            ps.setLong(2, player.vang);
            ps.setInt(3, player.ngoc);
            ps.setInt(4, player.ngocKhoa);
            ps.setInt(40, player.thanhvien);
            ps.setInt(41, player.sotien);
            ps.setInt(42, player.pointSuKien);
            ps.setInt(43, player.pointSuKien);
//            ps.setInt(5, player.clanid); //SERVER 2
            if (player.clan != null) {
                ps.setInt(5, player.clan.id);
            } else {
                ps.setInt(5, -1);
            }
            ps.setInt(6, player.taskId);
            ps.setInt(7, player.head);
            ps.setInt(8, player.map.id);
            ps.setInt(9, player.x);
            ps.setInt(10, player.y);
            ps.setInt(11, player.maxluggage);
            ps.setInt(12, player.maxBox);
            ps.setInt(13, player.hpGoc);
            ps.setInt(14, player.mpGoc);
            ps.setInt(15, player.damGoc);
            ps.setInt(16, player.defGoc);
            ps.setInt(17, player.critGoc);
            ps.setLong(18, player.tiemNang);
            byte j;
            for (j = 0; j < player.ItemBody.length; ++j) {
                if (player.ItemBody[j] != null && player.ItemBody[j].id != -1) {
                    if ((player.ItemBody[j].timeHSD > 0 && (player.ItemBody[j].timeHSD - 3600000) > System.currentTimeMillis()) || player.ItemBody[j].timeHSD == 0) {
                        jarr.add(ItemTemplate.ObjectItem(player.ItemBody[j], j));
                    }
                }
            }
            ps.setString(19, jarr.toJSONString());
            jarr.clear();
            for (j = 0; j < player.ItemBag.length; ++j) {
                if (player.ItemBag[j] != null && player.ItemBag[j].id != -1) {
                    jarr.add(ItemTemplate.ObjectItem(player.ItemBag[j], j));
                }
            }
            ps.setString(20, jarr.toJSONString());
            jarr.clear();
            for (j = 0; j < player.ItemBox.length; ++j) {
                if (player.ItemBox[j] != null && player.ItemBox[j].id != -1) {
                    jarr.add(ItemTemplate.ObjectItem(player.ItemBox[j], j));
                }
            }
            ps.setString(21, jarr.toJSONString());
            jarr.clear();
            ps.setInt(22, player.NhapThe);
            for (j = 0; j < player.listSkill.size(); j++) {
                if (player.listSkill.get(j) != null) {
                    jarr.add(SkillTemplate.ObjectSkill(player.listSkill.get(j), player.getSkillByIDTemplate(player.listSkill.get(j).skillId).lastTimeUseThisSkill));
                }
            }
            ps.setString(23, jarr.toJSONString());
            jarr.clear();
            for (j = 0; j < player.listAmulet.size(); j++) {
                if (player.listAmulet.get(j) != null) {
                    jarr.add(player.listAmulet.get(j).ObjectAmulet()); ////////
                }
            }
            ps.setString(24, jarr.toJSONString());
            jarr.clear();
            ps.setInt(25, (int) player.limitPower);
            //NOITAI
            jarr.add(player.noiTai.ObjectNoiTai());
            ps.setString(26, jarr.toJSONString());
            jarr.clear();
            //END NOI TAI
            ps.setInt(27, (int) (player.countMoNoiTai));
            //ROLE BANG HOI
            ps.setInt(28, (int) (player.rolePT));
            //DAU THAN
            jarr.add(player.ObjectBean());
            ps.setString(29, jarr.toJSONString());
            jarr.clear();
            //TASK
            jarr.add(player.ObjectTask());
            ps.setString(30, jarr.toJSONString());
            jarr.clear();
            //NGOC RONG SAO DEN
            for (j = (byte) 0; j < (byte) 7; j++) {
                jarr.add(player.ObjectNRSD(j)); ////////
            }
            ps.setString(31, jarr.toJSONString());
            jarr.clear();
            ps.setInt(32, player.isPorata2 ? 1 : 0); //nhap the 2

            ps.setInt(33, player.hasTrungMabu ? 1 : 0); //Has Trung MaBu
            ps.setInt(34, player.isTennis ? 1 : 0); //chien thuyen tennis
            //RADA CARD
            for (j = (byte) 0; j < player.cards.size(); j++) {
                jarr.add(player.ObjectCard(player.cards.get(j))); ////////
            }
            ps.setString(35, jarr.toJSONString());
            jarr.clear();
            //TIME LOGOUT
            ps.setString(36, Long.toString(System.currentTimeMillis() + 20000));

            //ITEM USE
            long timeItem = 0;
            if (player.timerCSKB != null) {
                timeItem = player.timeEndCSKB - System.currentTimeMillis();
                if (timeItem > 30000) {
                    jarr.add(player.ObjectItemUse(379, timeItem));
                }
            }
            if (player.timerCN != null) {
                timeItem = player.timeEndCN - System.currentTimeMillis();
                if (timeItem > 30000) {
                    jarr.add(player.ObjectItemUse(381, timeItem));
                }
            }
            if (player.timerBH != null) {
                timeItem = player.timeEndBH - System.currentTimeMillis();
                if (timeItem > 30000) {
                    jarr.add(player.ObjectItemUse(382, timeItem));
                }
            }
            if (player.timerBK != null) {
                timeItem = player.timeEndBK - System.currentTimeMillis();
                if (timeItem > 30000) {
                    jarr.add(player.ObjectItemUse(383, timeItem));
                }
            }
            if (player.timerGX != null) {
                timeItem = player.timeEndGX - System.currentTimeMillis();
                if (timeItem > 30000) {
                    jarr.add(player.ObjectItemUse(384, timeItem));
                }
            }
            if (player.timerTM != null) {
                timeItem = player.timeEndTM - System.currentTimeMillis();
                if (timeItem > 30000) {
                    jarr.add(player.ObjectItemUse(1016, timeItem));
                }
            }
            if (player.timerTA != null) {
                timeItem = player.timeEndTA - System.currentTimeMillis();
                if (timeItem > 30000) {
                    jarr.add(player.ObjectItemUse(player.idTAUse, timeItem));
                }
            }
            ps.setString(37, jarr.toJSONString());
            jarr.clear();
            //CRIT UOC RONG THAN
            ps.setInt(38, player.critNr);
            //FRIEND
            for (j = (byte) 0; j < player.friends.size(); j++) {
                jarr.add(player.ObjectFriend(player.friends.get(j)));
            }
            ps.setString(39, jarr.toJSONString());
            jarr.clear();

            ps.setInt(44, player.id);
            if (ps.executeUpdate() == 1) {
            }
            conn.commit();

            if (player.havePet == 1) {
                ps = conn.prepareStatement(UPDATE_PET);
                conn.setAutoCommit(false);
                ps.setLong(1, player.detu.power);
                ps.setInt(2, player.detu.head);
                ps.setInt(3, player.detu.hpGoc);
                ps.setInt(4, player.detu.mpGoc);
                ps.setInt(5, player.detu.damGoc);
                ps.setInt(6, player.detu.defGoc);
                ps.setInt(7, player.detu.critGoc);
                ps.setLong(8, player.detu.tiemNang);
                for (j = 0; j < player.detu.ItemBody.length; ++j) {
                    if (player.detu.ItemBody[j] != null && player.detu.ItemBody[j].id != -1) {
                        jarr.add(ItemTemplate.ObjectItem(player.detu.ItemBody[j], j));
                    }
                }
                ps.setString(9, jarr.toJSONString());
//                System.out.println("OBJECT ITEAM BODY PET: " + jarr.toJSONString());
                jarr.clear();
                for (j = 0; j < player.detu.listSkill.size(); j++) {
                    if (player.detu.listSkill.get(j) != null) {
                        jarr.add(SkillTemplate.ObjectSkillPet(player.detu.listSkill.get(j)));
                    }
                }
                ps.setString(10, jarr.toJSONString());
//                System.out.println("OBJECT SKILL PET: " + jarr.toJSONString());
                jarr.clear();
                ps.setInt(11, player.detu.gender);
                ps.setString(12, player.detu.name);
                ps.setInt(13, (int) player.detu.limitPower);
                ps.setInt(14, (int) player.detu.stamina);
                ps.setInt(15, player.detu.isMabu ? 1 : 0);
                ps.setInt(16, player.detu.isdie ? 1 : 0);
                ps.setInt(17, player.detu.isBerus ? 1 : 0);
                ps.setInt(18, player.id);
                if (ps.executeUpdate() == 1) {
                }
                conn.commit();
            }
            if (player.isNewPet) {
                player.isNewPet = false;

                ps = conn.prepareStatement(INSERT_PET);
                conn.setAutoCommit(false);
                ps.setString(1, player.detu.name);
                ps.setLong(2, player.detu.power);
                ps.setInt(3, player.detu.head);
                ps.setInt(4, player.detu.gender);
                ps.setInt(5, player.id);
                ps.setInt(6, player.detu.hpGoc);
                ps.setInt(7, player.detu.mpGoc);
                ps.setInt(8, player.detu.damGoc);
                ps.setInt(9, player.detu.defGoc);
                ps.setInt(10, player.detu.critGoc);
                ps.setLong(11, player.detu.tiemNang);
                ps.setInt(12, (int) player.detu.limitPower);
                jarr.clear();
                for (j = 0; j < player.detu.listSkill.size(); j++) {
                    if (player.detu.listSkill.get(j) != null) {
                        jarr.add(SkillTemplate.ObjectSkillPet(player.detu.listSkill.get(j)));
                    }
                }
//                System.out.println("OBJECT SKILL PET: " + jarr.toJSONString());
                ps.setString(13, jarr.toJSONString());
                ps.setString(14, "[]");
                jarr.clear();
                if (ps.executeUpdate() == 1) {
                }
                conn.commit();
            }
//            conn.close();
        } catch (Exception e) {
        }
    }

    public static void changePassword(int id, String pass) {
        String UPDATE_PASS = "UPDATE account SET password=? WHERE id=?";
        Connection conn = null;
        try {
            conn = DataSource.getConnection();
            PreparedStatement ps = null;
            //UPDATE NRSD
            ps = conn.prepareStatement(UPDATE_PASS);
            conn.setAutoCommit(false);
            //NGOC RONG SAO DEN
            ps.setString(1, pass);

            ps.setInt(2, id);
            if (ps.executeUpdate() == 1) {
            }
            conn.commit();
            //UPDATE NRSD
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
}
