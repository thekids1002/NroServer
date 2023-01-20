package nro.clan;

import java.util.ArrayList;
import nro.player.Player;
import nro.player.PlayerManger;
import nro.player.PlayerDAO;
import nro.main.Util;
import nro.main.Service;
import nro.io.Message;
import nro.io.Session;
import nro.task.TaskService;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Calendar;
import java.util.Date;

public class ClanService {

    private static ClanService instance;

    public static ClanService gI() {
        if (instance == null) {
            instance = new ClanService();
        }
        return instance;
    }

    public void resetCountGas() {
        Timer timerCount = new Timer();
        TimerTask tCount = new TimerTask() {
            public void run() {
                for (Clan clan : ClanManager.gI().getClans()) {
                    if (clan != null) {
                        clan.cOpenGas = (byte) 3;
                    }
                }
                timerCount.cancel();
            }
        ;
        };

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date dateSchedule = calendar.getTime();
        Date dateNow = new Date();
        if (dateNow.after(dateSchedule)) {
            calendar.setTime(dateSchedule);
            calendar.add(Calendar.DATE, 1);
            dateSchedule = calendar.getTime();
        }

        long period = 24 * 60 * 60 * 1000;

        timerCount.schedule(tCount, dateSchedule, period);
    }

    public void clanInfo(Session session, Player pl) {
        Message msg;
        Clan clan = pl.clan;
        try {
            msg = new Message(-53);
            ArrayList<Member> members = clan.members;
            msg.writer().writeInt(clan.id);
            msg.writer().writeUTF(clan.name);
            msg.writer().writeUTF(clan.slogan);
            msg.writer().writeByte(clan.imgID);
            msg.writer().writeUTF(Util.powerToString(clan.powerPoint));
            msg.writer().writeUTF(clan.leaderName);//leaderName
            msg.writer().writeByte(members.size());
            msg.writer().writeByte(clan.maxMember);
            msg.writer().writeByte(pl.rolePT); //role
            msg.writer().writeInt(clan.clanPoint);
            msg.writer().writeByte(clan.level);
            for (Member member : members) {
                msg.writer().writeInt(member.id);
                msg.writer().writeShort(member.head);
                msg.writer().writeShort(member.headICON);
                msg.writer().writeShort(member.leg);
                msg.writer().writeShort(member.body);
                msg.writer().writeUTF(member.name);//playerName
                msg.writer().writeByte(member.role);
                msg.writer().writeUTF(Util.powerToString(member.powerPoint));
                msg.writer().writeInt(0);
                msg.writer().writeInt(0);
                msg.writer().writeInt(0);
                msg.writer().writeInt(0);
                msg.writer().writeInt(member.joinTime);
            }
            msg.writer().writeByte((byte) 0);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void clanMember(Session session, int clanId) {
        Message msg;
        ArrayList<Member> members = ClanManager.gI().getMemberByIdClan(clanId);
        try {
            msg = new Message(-50);
            msg.writer().writeByte(members.size());
            for (Member member : members) {
                msg.writer().writeInt(member.id);
                msg.writer().writeShort(member.head);
                msg.writer().writeShort(member.headICON);
                msg.writer().writeShort(member.leg);
                msg.writer().writeShort(member.body);
                msg.writer().writeUTF(member.name);//playerName
                msg.writer().writeByte(member.role);
                msg.writer().writeUTF(Util.powerToString(member.powerPoint));
                msg.writer().writeInt(0);
                msg.writer().writeInt(0);
                msg.writer().writeInt(0);
                msg.writer().writeInt(member.joinTime);
            }
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void searchClan(Session session, String text) {
        Message msg;
        try {
            ArrayList<Clan> clans = ClanManager.gI().search(text);
            msg = new Message(-47);
            msg.writer().writeByte((byte) clans.size());
            if (clans.size() > 0) {
                for (Clan clan : clans) {
                    msg.writer().writeInt(clan.id);
                    msg.writer().writeUTF(clan.name);
                    msg.writer().writeUTF(clan.slogan);
                    msg.writer().writeByte(clan.imgID);
                    String powerPoint = Util.powerToString(clan.powerPoint);
                    msg.writer().writeUTF(powerPoint);
                    msg.writer().writeUTF(clan.leaderName);//leaderName
                    //int currMember = ClanDB.getMembers(clan.id).size();
                    msg.writer().writeByte(clan.currMember);
                    msg.writer().writeByte(clan.maxMember);
                    msg.writer().writeInt((int) (clan.time / 1000));
                }
            }
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            System.out.println("-47 " + e.toString());
        }
    }

    public void clanIconImage(Session session) {
        Message m = null;
        try {
            m = new Message(-62);
            m.writer().writeByte((byte) 0);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 1027);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 8);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 1025);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 7);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 1028);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 6);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 1031);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 5);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 1029);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 4);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 1028);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 3);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 1032);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 2);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 1033);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 1);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 1026);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 18);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 1069);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 17);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 1068);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 16);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 1072);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 15);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 1070);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 14);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 1067);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 13);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 1066);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 12);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 1065);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 11);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 1064);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 10);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 1063);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 9);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 1072);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 36);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 4120);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 35);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 4123);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 34);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 4121);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 33);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 4122);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 32);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 4124);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 19);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 1038);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 22);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 1035);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 21);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 1039);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 20);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 1044);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 29);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 1084);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 27);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 1042);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 26);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 1037);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 25);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 1036);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 24);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 1040);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-62);
            m.writer().writeByte((byte) 23);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 1043);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void clanIconImageInfo(Session session, byte type) {
        Message m = null;
        try {
            m = new Message(-46);
            m.writer().writeByte(type);
            m.writer().writeByte((byte) 34);

            m.writer().writeByte((byte) 0);
            m.writer().writeUTF("Cờ Xám");
            m.writer().writeInt(10000);
            m.writer().writeInt(1);
            m.writer().writeByte((byte) 8);
            m.writer().writeUTF("Cờ Xanh Dạ");
            m.writer().writeInt(10000);
            m.writer().writeInt(1);
            m.writer().writeByte((byte) 7);
            m.writer().writeUTF("Cờ Tím");
            m.writer().writeInt(10000);
            m.writer().writeInt(1);
            m.writer().writeByte((byte) 6);
            m.writer().writeUTF("Cờ Vàng");
            m.writer().writeInt(10000);
            m.writer().writeInt(1);
            m.writer().writeByte((byte) 5);
            m.writer().writeUTF("Cờ Cam");
            m.writer().writeInt(10000);
            m.writer().writeInt(1);
            m.writer().writeByte((byte) 4);
            m.writer().writeUTF("Cờ Hồng");
            m.writer().writeInt(10000);
            m.writer().writeInt(1);
            m.writer().writeByte((byte) 3);
            m.writer().writeUTF("Cờ Xanh Biển");
            m.writer().writeInt(10000);
            m.writer().writeInt(1);
            m.writer().writeByte((byte) 2);
            m.writer().writeUTF("Cờ Xanh Lá");
            m.writer().writeInt(10000);
            m.writer().writeInt(1);
            m.writer().writeByte((byte) 1);
            m.writer().writeUTF("Cờ Đen");
            m.writer().writeInt(10000);
            m.writer().writeInt(1);
            m.writer().writeByte((byte) 18);
            m.writer().writeUTF("Khăn Xanh Dạ");
            m.writer().writeInt(50000);
            m.writer().writeInt(1);
            m.writer().writeByte((byte) 17);
            m.writer().writeUTF("Khăn Hồng");
            m.writer().writeInt(50000);
            m.writer().writeInt(1);
            m.writer().writeByte((byte) 16);
            m.writer().writeUTF("Khăn Đỏ");
            m.writer().writeInt(50000);
            m.writer().writeInt(1);
            m.writer().writeByte((byte) 15);
            m.writer().writeUTF("Khăn Xám");
            m.writer().writeInt(50000);
            m.writer().writeInt(1);
            m.writer().writeByte((byte) 14);
            m.writer().writeUTF("Khăn Nâu");
            m.writer().writeInt(50000);
            m.writer().writeInt(1);
            m.writer().writeByte((byte) 13);
            m.writer().writeUTF("Khăn Tím");
            m.writer().writeInt(50000);
            m.writer().writeInt(1);
            m.writer().writeByte((byte) 12);
            m.writer().writeUTF("Khăn Vàng");
            m.writer().writeInt(50000);
            m.writer().writeInt(1);
            m.writer().writeByte((byte) 11);
            m.writer().writeUTF("Khăn Xanh Dương");
            m.writer().writeInt(50000);
            m.writer().writeInt(1);
            m.writer().writeByte((byte) 10);
            m.writer().writeUTF("Khăn Xanh Lá");
            m.writer().writeInt(50000);
            m.writer().writeInt(1);
            m.writer().writeByte((byte) 9);
            m.writer().writeUTF("Khăn Đỏ");
            m.writer().writeInt(50000);
            m.writer().writeInt(1);
            m.writer().writeByte((byte) 27);
            m.writer().writeUTF("Giỏ Chuối");
            m.writer().writeInt(100000);
            m.writer().writeInt(1);
            m.writer().writeByte((byte) 26);
            m.writer().writeUTF("Giỏ Cà Rốt");
            m.writer().writeInt(100000);
            m.writer().writeInt(1);
            m.writer().writeByte((byte) 25);
            m.writer().writeUTF("Giỏ Củ Cải Trắng");
            m.writer().writeInt(100000);
            m.writer().writeInt(1);
            m.writer().writeByte((byte) 24);
            m.writer().writeUTF("Giỏ Dưa Hấu");
            m.writer().writeInt(100000);
            m.writer().writeInt(1);
            m.writer().writeByte((byte) 23);
            m.writer().writeUTF("Giỏ Bơ");
            m.writer().writeInt(100000);
            m.writer().writeInt(1);
            m.writer().writeByte((byte) 36);
            m.writer().writeUTF("Lồng Đèn Hội An");
            m.writer().writeInt(0);
            m.writer().writeInt(50);
            m.writer().writeByte((byte) 35);
            m.writer().writeUTF("Lồng Đèn Ông Trăng");
            m.writer().writeInt(0);
            m.writer().writeInt(50);
            m.writer().writeByte((byte) 34);
            m.writer().writeUTF("Lồng Đèn Kéo Quân");
            m.writer().writeInt(0);
            m.writer().writeInt(50);
            m.writer().writeByte((byte) 33);
            m.writer().writeUTF("Lồng Đèn Cá Chép");
            m.writer().writeInt(0);
            m.writer().writeInt(50);
            m.writer().writeByte((byte) 32);
            m.writer().writeUTF("Lồng Đèn Ông Sao");
            m.writer().writeInt(0);
            m.writer().writeInt(50);
            m.writer().writeByte((byte) 19);
            m.writer().writeUTF("Balo");
            m.writer().writeInt(0);
            m.writer().writeInt(200);
            m.writer().writeByte((byte) 22);
            m.writer().writeUTF("Mai Rùa");
            m.writer().writeInt(0);
            m.writer().writeInt(300);
            m.writer().writeByte((byte) 21);
            m.writer().writeUTF("Gậy");
            m.writer().writeInt(0);
            m.writer().writeInt(400);
            m.writer().writeByte((byte) 20);
            m.writer().writeUTF("Đao");
            m.writer().writeInt(0);
            m.writer().writeInt(500);
            m.writer().writeByte((byte) 29);
            m.writer().writeUTF("Gậy Phép");
            m.writer().writeInt(0);
            m.writer().writeInt(600);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    synchronized public boolean createNewClan(byte idICON, String name, Player player) {
        name = Util.validateClan(name);
        if (name.length() > 20) {
            name = name.substring(0, 20);
        }
        int price = priceCreateClan(idICON);
        if (price < 1000) {
            if (player.ngoc < price) {
                player.sendAddchatYellow("Không đủ ngọc để tạo bang hội");
                return false;
            }
            player.ngoc -= price;
        } else {
            if (player.vang < (long) price) {
                player.sendAddchatYellow("Không đủ vàng để tạo bang hội");
                return false;
            }
            player.vang -= (long) price;
        }
        Clan newClan = new Clan();
        newClan.id = (int) (ClanManager.gI().getLastClanID() + 1);
        newClan.name = name;
        newClan.imgID = idICON;
        newClan.slogan = " ";
        newClan.leaderID = player.id;
        newClan.leaderName = player.name;
        newClan.tcreate = System.currentTimeMillis();
        newClan.topen = System.currentTimeMillis() + (long) 86400000;
        //assign flag create new clan to update DB
        newClan.flagCreate = true;
        //ADD LEADER VAO NEW CLAN
        Member member = new Member();
        member.id = player.id;
        member.name = player.name;
        member.powerPoint = player.power;
        member.role = (byte) 0;
        member.head = player.head;
        member.headICON = player.iconIDHead();
        if (player.gender == 1) {
            member.body = (short) 59;
            member.leg = (short) 60;
        } else {
            member.body = (short) 57;
            member.leg = (short) 58;
        }
        member.donate = 0;
        member.receiveDonate = 0;
        member.clanPoint = 0;
        member.currPoint = 0;
        member.joinTime = (int) (System.currentTimeMillis() / 1000);
        newClan.members.add(member);

        //CREATE NEW CLAN
        ClanManager.gI().getClans().add(newClan);
        //ADD TO DATABASE
        ClanDAO.create(newClan);

        player.clan = newClan;
        player.rolePT = (byte) 0;
        return true;
    }

    public int priceCreateClan(byte idICON) {
        if (idICON >= 0 && idICON <= 8) {
            return 10000;
        } else if (idICON >= 9 && idICON <= 18) {
            return 50000;
        } else if (idICON >= 23 && idICON <= 27) {
            return 100000;
        } else if (idICON >= 32 && idICON <= 36) {
            return 50;
        } else if (idICON == 19) {
            return 200;
        } else if (idICON == 22) {
            return 300;
        } else if (idICON == 21) {
            return 400;
        } else if (idICON == 20) {
            return 500;
        } else if (idICON == 29) {
            return 600;
        }
        return 600;
    }

    public void getBagBangNew(Session session, byte idBag) {
        Message m = null;
        try {
            m = new Message(-63);
            m.writer().writeByte(idBag);
            if (idBag == (byte) 0) {
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 1017);
                m.writer().writeShort((short) 1018);
            } else if (idBag == (byte) 1) {
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 1015);
                m.writer().writeShort((short) 1016);
            } else if (idBag == (byte) 2) {
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 1001);
                m.writer().writeShort((short) 1002);
            } else if (idBag == (byte) 3) {
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 1003);
                m.writer().writeShort((short) 1004);
            } else if (idBag == (byte) 4) {
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 1011);
                m.writer().writeShort((short) 1012);
            } else if (idBag == (byte) 5) {
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 1009);
                m.writer().writeShort((short) 1010);
            } else if (idBag == (byte) 6) {
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 1005);
                m.writer().writeShort((short) 1006);
            } else if (idBag == (byte) 7) {
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 1011);
                m.writer().writeShort((short) 1012);
            } else if (idBag == (byte) 8) {
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 1013);
                m.writer().writeShort((short) 1014);
            } else if (idBag == (byte) 9) {
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 1061);
                m.writer().writeShort((short) 1062);
            } else if (idBag == (byte) 10) {
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 1045);
                m.writer().writeShort((short) 1046);
            } else if (idBag == (byte) 11) {
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 1047);
                m.writer().writeShort((short) 1048);
            } else if (idBag == (byte) 12) {
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 1049);
                m.writer().writeShort((short) 1050);
            } else if (idBag == (byte) 13) {
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 1051);
                m.writer().writeShort((short) 1052);
            } else if (idBag == (byte) 14) {
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 1053);
                m.writer().writeShort((short) 1054);
            } else if (idBag == (byte) 15) {
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 1059);
                m.writer().writeShort((short) 1060);
            } else if (idBag == (byte) 16) {
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 1061);
                m.writer().writeShort((short) 1062);
            } else if (idBag == (byte) 17) {
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 1055);
                m.writer().writeShort((short) 1056);
            } else if (idBag == (byte) 18) {
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 1057);
                m.writer().writeShort((short) 1058);
            } else if (idBag == (byte) 23) {
                m.writer().writeByte((byte) 1);
                m.writer().writeShort((short) 996);
            } else if (idBag == (byte) 24) {
                m.writer().writeByte((byte) 1);
                m.writer().writeShort((short) 998);
            } else if (idBag == (byte) 25) {
                m.writer().writeByte((byte) 1);
                m.writer().writeShort((short) 989);
            } else if (idBag == (byte) 26) {
                m.writer().writeByte((byte) 1);
                m.writer().writeShort((short) 990);
            } else if (idBag == (byte) 27) {
                m.writer().writeByte((byte) 1);
                m.writer().writeShort((short) 997);
            } else if (idBag == (byte) 32) {
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 4033);
                m.writer().writeShort((short) 4084);
            } else if (idBag == (byte) 33) {
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 4034);
                m.writer().writeShort((short) 4035);
            } else if (idBag == (byte) 34) {
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 4036);
                m.writer().writeShort((short) 4037);
            } else if (idBag == (byte) 35) {
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 4038);
                m.writer().writeShort((short) 4039);
            } else if (idBag == (byte) 36) {
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 4040);
                m.writer().writeShort((short) 4041);
            } else if (idBag == (byte) 19) {
                m.writer().writeByte((byte) 1);
                m.writer().writeShort((short) 1024);
            } else if (idBag == (byte) 22) {
                m.writer().writeByte((byte) 1);
                m.writer().writeShort((short) 1000);
            } else if (idBag == (byte) 21) {
                m.writer().writeByte((byte) 1);
                m.writer().writeShort((short) 999);
            } else if (idBag == (byte) 20) {
                m.writer().writeByte((byte) 1);
                m.writer().writeShort((short) 995);
            } else if (idBag == (byte) 29) {
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 1073);
                m.writer().writeShort((short) 1074);
            } else if (idBag == (byte) 37) {
                m.writer().writeByte((byte) 1);
                m.writer().writeShort((short) 2315);
            } else if (idBag == (byte) 38) { //PHONG LON
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 9628);
                m.writer().writeShort((short) 9629);
            } else if (idBag == (byte) 39) { //DAO PHAY
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 9702);
                m.writer().writeShort((short) 9703);
            } else if (idBag == (byte) 40) { //CAY KEM
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 9603);
                m.writer().writeShort((short) 9604);
            } else if (idBag == (byte) 41) { //ca heo
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 9606);
                m.writer().writeShort((short) 9607);
            } else if (idBag == (byte) 42) { //con dieu
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 9609);
                m.writer().writeShort((short) 9610);
            } else if (idBag == (byte) 43) {//dieu rong
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 9612);
                m.writer().writeShort((short) 9613);
            } else if (idBag == (byte) 44) {//meo mun
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 9622);
                m.writer().writeShort((short) 9623);
            } else if (idBag == (byte) 45) { //xien ca
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 9625);
                m.writer().writeShort((short) 9626);
            } else if (idBag == (byte) 46) { //van luot song
                m.writer().writeByte((byte) 1);
                m.writer().writeShort((short) 9593);
            } else if (idBag == (byte) 47) { //kiem anh sang
                m.writer().writeByte((byte) 7);
                m.writer().writeShort((short) 9642);
                m.writer().writeShort((short) 9643);
                m.writer().writeShort((short) 9644);
                m.writer().writeShort((short) 9645);
                m.writer().writeShort((short) 9646);
                m.writer().writeShort((short) 9647);
                m.writer().writeShort((short) 9648);
            } else if (idBag == (byte) 48) { //Mjolnir
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 9830);
                m.writer().writeShort((short) 9831);
            } else if (idBag == (byte) 49) { //Stormbreaker
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 9820);
                m.writer().writeShort((short) 9821);
            } else if (idBag == (byte) 50) { //quat 3 tieu
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 9834);
                m.writer().writeShort((short) 9835);
            } else if (idBag == (byte) 51) { //bo hoa hong
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 8929);
                m.writer().writeShort((short) 8930);
            } else if (idBag == (byte) 52) { //bo hoa vang
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 8931);
                m.writer().writeShort((short) 8932);
            } else if (idBag == (byte) 53) {//NGOC RONG NAMEC
                m.writer().writeByte((byte) 1);
                m.writer().writeShort((short) 2287);
            } else if (idBag == (byte) 54) {//LONG DEN LON
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 9968);
                m.writer().writeShort((short) 9969);
            } else if (idBag == (byte) 55) {//LONG DEN COVI
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 7224);
                m.writer().writeShort((short) 7225);
            } else if (idBag == (byte) 56) {//LONG DEN CON TAU
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 7228);
                m.writer().writeShort((short) 7229);
            } else if (idBag == (byte) 57) {//LONG DEN CON GA
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 7231);
                m.writer().writeShort((short) 7232);
            } else if (idBag == (byte) 58) {//LONG DEN CON BUOM
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 7234);
                m.writer().writeShort((short) 7235);
            } else if (idBag == (byte) 59) {//LONG DEN DOREMON
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 7237);
                m.writer().writeShort((short) 7238);
            } else if (idBag == (byte) 60) {//NON THIEN THAN
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 7335);
                m.writer().writeShort((short) 7336);
            } else if (idBag == (byte) 61) {//MA TROI
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 7344);
                m.writer().writeShort((short) 7345);
            } else if (idBag == (byte) 62) {//HON MA GOKU
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 7346);
                m.writer().writeShort((short) 7347);
            } else if (idBag == (byte) 63) {//HON MA CADIC
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 7348);
                m.writer().writeShort((short) 7349);
            } else if (idBag == (byte) 64) {//HON MA PICOLO
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 7350);
                m.writer().writeShort((short) 7351);
            } else if (idBag == (byte) 65) {//CAY THONG
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 7391);
                m.writer().writeShort((short) 7392);
            } else if (idBag == (byte) 66) {//TUI QUA
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 7393);
                m.writer().writeShort((short) 7394);
            } else if (idBag == (byte) 67) {//CAY TRUC
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 7595);
                m.writer().writeShort((short) 7596);
            } else if (idBag == (byte) 68) {//KIEM Z
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 7840);
                m.writer().writeShort((short) 7841);
            } else if (idBag == (byte) 69) {//TRAI BONG
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 9359);
                m.writer().writeShort((short) 9360);
            } else if (idBag == (byte) 70) {//CUP VANG
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 9400);
                m.writer().writeShort((short) 9401);
            } else if (idBag == (byte) 71) {//CO CO DONG
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 9403);
                m.writer().writeShort((short) 9404);
            } else if (idBag == (byte) 72) {//VO OC
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 9600);
                m.writer().writeShort((short) 9601);
            } else if (idBag == (byte) 73) {//CO HOA DANG
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 9839);
                m.writer().writeShort((short) 9840);
            } else if (idBag == (byte) 74) {//CO HOA SEN
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 9841);
                m.writer().writeShort((short) 9842);
            } else if (idBag == (byte) 75) {//LUOI HAI THAN CHET
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 7017);
                m.writer().writeShort((short) 7018);
            } else if (idBag == (byte) 76) {//CANH DOI DRACULA
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 7020);
                m.writer().writeShort((short) 7021);
            } else if (idBag == (byte) 77) {//BONG TUYET
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 7064);
                m.writer().writeShort((short) 7065);
            } else if (idBag == (byte) 78) {//CHAU CAY AN THIT
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 10487);
                m.writer().writeShort((short) 10488);
            } else if (idBag == (byte) 79) {//DINH BA SATAN
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 10604);
                m.writer().writeShort((short) 10605);
            } else if (idBag == (byte) 80) {//CHOI PHU THUY
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 10606);
                m.writer().writeShort((short) 10607);
            } else if (idBag == (byte) 81) {//CANH THIEN THAN
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 10608);
                m.writer().writeShort((short) 10609);
            } else if (idBag == (byte) 82) {//CANH THIEN THAN 2
                m.writer().writeByte((byte) 2);
                m.writer().writeShort((short) 10610);
                m.writer().writeShort((short) 10611);
            }
//            else if(idBag == (byte)83) {//CAY NAP AM
//                m.writer().writeByte((byte)2);
//                m.writer().writeShort((short)10487);
//                m.writer().writeShort((short)10488);
//            } else if(idBag == (byte)84) {//CON CA
//                m.writer().writeByte((byte)2);
//                m.writer().writeShort((short)10487);
//                m.writer().writeShort((short)10488);
//            }
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void updateBagClan(Player player, byte idBagNew) {
        int price = priceCreateClan(idBagNew);
        if (price < 1000) {
            if (player.ngoc < price) {
                player.sendAddchatYellow("Không đủ ngọc để thay đổi biểu tượng bang hội");
                return;
            }
            player.ngoc -= price;
        } else {
            if (player.vang < (long) price) {
                player.sendAddchatYellow("Không đủ vàng để thay đổi biểu tượng bang hội");
                return;
            }
            player.vang -= (long) price;
        }
        // UPDATE ITEM BAG;
        player.clan.imgID = idBagNew;

        //ADD TO DATABASE
        ClanDAO.update(player.clan);
    }

    //SEND INVITE BANG
    public void inviteClanSend(Session session, String strInvite, int idClan, int code) {
        Message m = null;
        try {
            m = new Message(-57);
            m.writer().writeUTF(strInvite);   //STRING INVITE
            m.writer().writeInt(idClan); //CLAN ID
            m.writer().writeInt(code); //CODE
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    //ADD PLAYER TO CLAN
    public void addPlayerToClan(Player playerAdd, int idClan) {
        //GET CLAN JOIN
        Clan clanJoin = ClanManager.gI().getClanById(idClan);
        //ADD NEW MEMBER TO CLAN
        if (clanJoin.members.size() < 10) {
            Member memberNew = createNewMember(playerAdd, (byte) 2);
//            if(ClanDAO.add(clanJoin.id, memberNew)) {
            clanJoin.members.add(memberNew);
            //ASSIGN CLAN TO PLAYER JOIN
            playerAdd.clan = clanJoin;
            //CHECK NHIEM VU GIA NHAP BANG HOI
            if (playerAdd.taskId == (short) 12 && playerAdd.crrTask.index == (byte) 0) {
                TaskService.gI().updateCountTask(playerAdd);
            }
            //SEND MESSAGE JOIN CLAN
            int id = (int) (clanJoin.messages.size() + 1);
            ClanMessage clanMess = new ClanMessage(playerAdd, id, (byte) 0, playerAdd.name + " vừa gia nhập bang hội", (byte) 0); //PLAYER, IDMESS, TYPEMEss, TExtMess, COLORMESS
            clanJoin.messages.add(clanMess);
            if (clanJoin.messages.size() > 10) {
                clanJoin.messages.remove(0); //REMOVE MESSAGE DAU TIEN;
            }
            //SEND MESSAGE
            //SEND CHAT TO CLAN
            sendMessageToClan(clanJoin);
            //SEND ITEMBAG CLAN TO ALL PLAYER IN MAP AND PLAYER JOIN
            if (playerAdd.zone.players.size() > 0) {
                for (Player _p : playerAdd.zone.players) {

                    //UPDATE BAG SAU LUNG
                    Service.gI().updateBagNew(_p.session, playerAdd.id, clanJoin.imgID);
                    //GET BAG SAU LUNG
                    ClanService.gI().getBagBangNew(_p.session, clanJoin.imgID);
                    if (_p.id != playerAdd.id) {
                        loadUpdateInfoMember(_p.session, playerAdd);
                    }
                }
            }
//            }
        } else {
            playerAdd.sendAddchatYellow("Bang hội đối phương đã đạt số lượng thành viên tối đa");
        }
    }

    //LOAD LAI PLAYER KHI VUA VAO BANG CHO CA MAP
    public void loadUpdateInfoMember(Session session, Player player) {
        Message m = null;
        try {
            m = new Message(-30);
            m.writer().writeByte((byte) 7);
            m.writer().writeInt(player.id);
            if (player.clan != null) {
                m.writer().writeInt(player.clan.id);
            } else {
                m.writer().writeInt(-1);
            }
            m.writer().writeByte(player.getLevelPower()); //level power
//            m.writer().writeByte((byte)10);
            m.writer().writeBoolean(false); //vo hinh hay khong
            m.writer().writeByte(player.typePk); //type pk
            m.writer().writeByte(player.gender); //class
            m.writer().writeByte(player.gender); //gender
            m.writer().writeShort(player.PartHead()); //get head
            m.writer().writeUTF(player.name);
            m.writer().writeInt(player.hp);
            m.writer().writeInt(player.getHpFull());

            String bodyLeg = Service.gI().writePartBodyLeg(player);
            String[] arrOfStr = bodyLeg.split(",", 2);
            m.writer().writeShort(Short.parseShort(arrOfStr[0]));
            m.writer().writeShort(Short.parseShort(arrOfStr[1]));

            if (player.imgNRSD > (byte) 0) {
                m.writer().writeByte(player.imgNRSD);
            } else {
                if (player.clan != null) {
                    m.writer().writeByte(player.clan.imgID); //ID BIEU TUONGBANG
                } else {
                    m.writer().writeByte(-1);
                }
            }
            m.writer().writeByte(0);
            m.writer().writeShort(player.x);
            m.writer().writeShort(player.y);
            m.writer().writeByte(0); //numeffect
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public Member createNewMember(Player player, byte roleMember) {
        //ADD LEADER VAO NEW CLAN
        Member member = new Member();
        member.id = player.id;
        member.name = player.name;
        member.powerPoint = player.power;
        member.role = roleMember;
        member.head = player.head;
        member.headICON = player.iconIDHead();
        if (player.gender == 1) {
            member.body = (short) 59;
            member.leg = (short) 60;
        } else {
            member.body = (short) 57;
            member.leg = (short) 58;
        }
        member.donate = 0;
        member.receiveDonate = 0;
        member.clanPoint = 0;
        member.currPoint = 0;
        member.joinTime = (int) (System.currentTimeMillis() / 1000);
        return member;
    }

    public void updateClanType0(Session session, Member member) {
        if (session != null) {
            Message m = null;
            try {
                m = new Message(-52);
                m.writer().writeByte((byte) 0);
                m.writer().writeInt(member.id);
                m.writer().writeShort(member.head);
                m.writer().writeShort(member.headICON);
                m.writer().writeShort(member.leg);
                m.writer().writeShort(member.body);
                m.writer().writeUTF(member.name);
                m.writer().writeByte(member.role);
                m.writer().writeUTF(Util.powerToString(member.powerPoint));
                m.writer().writeInt(member.donate);
                m.writer().writeInt(member.receiveDonate);
                m.writer().writeInt(member.clanPoint);
                m.writer().writeInt(member.joinTime);
                m.writer().flush();
                session.sendMessage(m);
                m.cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (m != null) {
                    m.cleanup();
                }
            }
        }
    }

    //SEND MESSAGE TO ALL PLAYER IN CLAN
    public void sendMessageToClan(Clan clan) {
        for (byte i = 0; i < clan.messages.size(); i++) {
            clan.messages.get(i).id = (int) (i + 1);
//            System.out.println("MESSAGE CLAN " + i + " ID: " + clan.messages.get(i).id + ", TYPE: " + clan.messages.get(i).type + "PLAYER: " + clan.messages.get(i).playerName + "TEXT: " + clan.messages.get(i).text);
            if (clan.messages.get(i).type == (byte) 2) {
                messageXinVaoBANG(clan, clan.messages.get(i));
            } else if (clan.messages.get(i).type == (byte) 1) {
                messageXinDau(clan, clan.messages.get(i));
            } else {
                messageClanText(clan, clan.messages.get(i));
            }
        }
        for (Member _member : clan.members) {
            Player _playerSend = PlayerManger.gI().getPlayerByUserID(_member.id);
            if (_playerSend != null && _playerSend.session != null) {
                clanInfo(_playerSend.session, _playerSend);
            }
        }
    }

    //SEND CHAT CLAN TO ME LUC DANG NHAP
    public void sendMessageClanToMe(Player player) {
        if (player != null && player.session != null) {
            Message m = null;
            for (byte i = 0; i < player.clan.messages.size(); i++) {
                player.clan.messages.get(i).id = (int) (i + 1);
                if (player.clan.messages.get(i).type == (byte) 2) {
                    messageXinVaoToPC(player, player.clan.messages.get(i));
                } else if (player.clan.messages.get(i).type == (byte) 1) {
                    try {
                        m = new Message(-51);
                        m.writer().writeByte(player.clan.messages.get(i).type); //type message
                        m.writer().writeInt(player.clan.messages.get(i).id); //id clan message
                        m.writer().writeInt(player.clan.messages.get(i).playerId);
                        m.writer().writeUTF(player.clan.messages.get(i).playerName);
                        m.writer().writeByte(player.clan.messages.get(i).role); //Role send message
                        m.writer().writeInt(player.clan.messages.get(i).time);
                        m.writer().writeByte(player.clan.messages.get(i).recieve);
                        m.writer().writeByte((byte) 5);
                        m.writer().writeByte((byte) 0); //check is new
                        m.writer().flush();
                        player.session.sendMessage(m);
                        m.cleanup();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (m != null) {
                            m.cleanup();
                        }
                    }
                } else {
                    try {
                        m = new Message(-51);
                        m.writer().writeByte(player.clan.messages.get(i).type); //type message
                        m.writer().writeInt(player.clan.messages.get(i).id); //id clan message
                        m.writer().writeInt(player.clan.messages.get(i).playerId);
                        m.writer().writeUTF(player.clan.messages.get(i).playerName);
                        m.writer().writeByte(player.clan.messages.get(i).role); //Role send message
                        m.writer().writeInt(player.clan.messages.get(i).time);
                        m.writer().writeUTF(player.clan.messages.get(i).text); //String message
                        m.writer().writeByte(player.clan.messages.get(i).color);    //color message //Theo Role
                        m.writer().flush();
                        player.session.sendMessage(m);
                        m.cleanup();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (m != null) {
                            m.cleanup();
                        }
                    }
                }
            }
            clanInfo(player.session, player);
        }
    }

    public void messageClanText(Clan clan, ClanMessage clanMess) {
        Message m = null;
        try {
            m = new Message(-51);
            m.writer().writeByte(clanMess.type); //type message
            m.writer().writeInt(clanMess.id); //id clan message
            m.writer().writeInt(clanMess.playerId);
            m.writer().writeUTF(clanMess.playerName);
            m.writer().writeByte(clanMess.role); //Role send message
            m.writer().writeInt(clanMess.time);
            m.writer().writeUTF(clanMess.text); //String message
            m.writer().writeByte(clanMess.color);    //color message //Theo Role
            m.writer().flush();
            for (Member _member : clan.members) {
                Player _playerSend = PlayerManger.gI().getPlayerByUserID(_member.id);
                if (_playerSend != null && _playerSend.session != null) {
                    _playerSend.session.sendMessage(m);
                }
            }
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    //XIN DAU THAN
    public void messageXinDau(Clan clan, ClanMessage clanMess) {
        Message m = null;
        try {
            m = new Message(-51);
            m.writer().writeByte(clanMess.type); //type message
            m.writer().writeInt(clanMess.id); //id clan message
            m.writer().writeInt(clanMess.playerId);
            m.writer().writeUTF(clanMess.playerName);
            m.writer().writeByte(clanMess.role); //Role send message
            m.writer().writeInt(clanMess.time);
            m.writer().writeByte(clanMess.recieve);
            m.writer().writeByte((byte) 5);
            m.writer().writeByte((byte) 0); //check is new
            m.writer().flush();
            for (Member _member : clan.members) {
                Player _playerSend = PlayerManger.gI().getPlayerByUserID(_member.id);
                if (_playerSend != null) {
                    _playerSend.session.sendMessage(m);
                }
            }
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    //SEND MESSAGE XIN VAO PT
    public void messageXinVaoBANG(Clan clan, ClanMessage clanMess) {
        for (Member _member : clan.members) {
            Player _playerSend = PlayerManger.gI().getPlayerByUserID(_member.id);
            if (_playerSend != null) {
                messageXinVaoToPC(_playerSend, clanMess);
            }
        }
    }

    public void messageXinVaoToPC(Player player, ClanMessage clanMess) {
        Message m = null;
        try {
            m = new Message(-51);
            m.writer().writeByte((byte) 2); //type message
            m.writer().writeInt(clanMess.id); //id clan message
            m.writer().writeInt(clanMess.playerId);
            m.writer().writeUTF(clanMess.playerName);
            m.writer().writeByte((byte) (-1)); //Role send message
            m.writer().writeInt(clanMess.time);
            m.writer().flush();
            player.session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    //XOAS MESSAGE XIN VAO BANG NEU SPAM NHIEU
    public void checkHasMessageClan(Clan clan, int playerid) {
        if (clan.messages.size() > 0) {
            for (byte i = 0; i < clan.messages.size(); i++) {
                if (clan.messages.get(i).playerId == playerid && clan.messages.get(i).type == (byte) 2) {
                    clan.messages.remove(i);
                }
            }
            //REMOVE XONG UPDATE LAI ID
            for (byte i = 0; i < clan.messages.size(); i++) {
                clan.messages.get(i).id = (int) (i + 1);
            }
        }
    }

    //PHONG CHU BANG, PHO BANG
    public void changeRolePhoBang(int idPlayer, Player player, byte roleNew) {
        Player _playerSend = PlayerManger.gI().getPlayerByUserID(idPlayer);
        ClanDAO.changeRole(idPlayer, (int) roleNew, player.clan.id);

        if (_playerSend != null) {
            _playerSend.rolePT = (byte) roleNew;
        }
        Member _member = player.clan.getMemberByID(idPlayer);
        _member.role = (byte) roleNew;

        int id = (int) (player.clan.messages.size() + 1);
        ClanMessage clanMess = new ClanMessage(player, id, (byte) 0, "Vừa phong phó bang cho " + _member.name, (byte) 0); //PLAYER, IDMESS, TYPEMEss, TExtMess, COLORMESS
        if (roleNew == (byte) 0) {
            player.rolePT = (byte) 2;
            ClanDAO.changeRole(player.id, 2, player.clan.id);
            Member _memberPCOld = player.clan.getMemberByID(player.id);
            _memberPCOld.role = (byte) 2;
            //SET CHU BANG MOI
            player.clan.leaderID = idPlayer;
            player.clan.leaderName = _member.name;
            clanMess.text = "Vừa nhường chủ bang cho " + _member.name;
            clanMess.role = (byte) 2;
        } else if (roleNew == (byte) 2) {
            clanMess.text = "Vừa cắt chức của " + _member.name;
        }
        player.clan.messages.add(clanMess);
        if (player.clan.messages.size() > 10) {
            player.clan.messages.remove(0); //REMOVE MESSAGE DAU TIEN;
            for (ClanMessage _clanMess : player.clan.messages) {
                _clanMess.id -= 1;
            }
        }
        //SEND CHAT TO CLAN
        sendMessageToClan(player.clan);
    }

    //LOAI THANH VIEN KHOI BANG
    public void loaiThanhVien(int idPlayer, Player player) {
        Player _playerSend = PlayerManger.gI().getPlayerByUserID(idPlayer);
//        ClanDAO.removeClanMember(idPlayer);

        if (_playerSend != null) {
            _playerSend.rolePT = (byte) 2;
            _playerSend.clan = null;
//            searchClan(_playerSend.session, "");

            Message m = null;
            try {
                m = new Message(-53);
                m.writer().writeInt(-1);
                m.writer().flush();
                _playerSend.session.sendMessage(m);
                m.cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (m != null) {
                    m.cleanup();
                }
            }
            //SEND ITEMBAG CLAN TO ALL PLAYER IN MAP AND PLAYER JOIN
            if (_playerSend.zone.players.size() > 0) {
                for (Player _p : _playerSend.zone.players) {

                    //UPDATE BAG SAU LUNG
                    Service.gI().updateBagNew(_p.session, _playerSend.id, (byte) (-1));
                    //GET BAG SAU LUNG
                    ClanService.gI().getBagBangNew(_p.session, (byte) (-1));
                    if (_p.id != _playerSend.id) {
                        loadUpdateInfoMember(_p.session, _playerSend);
                    }
                }
            }
        }
//        ClanDAO.removeClanMember(idPlayer);
        byte indexRemove = -1;
        for (byte i = 0; i < player.clan.members.size(); i++) {
            if (player.clan.members.get(i).id == idPlayer) {
                indexRemove = (byte) (i++);
                break;
            }
        }
        String nameRemove = player.clan.removeMemberByID(idPlayer);

        int id = (int) (player.clan.messages.size() + 1);
        ClanMessage clanMess = new ClanMessage(player, id, (byte) 0, "Vừa trục xuất " + nameRemove + " khỏi bang", (byte) 2); //PLAYER, IDMESS, TYPEMEss, TExtMess, COLORMESS
        player.clan.messages.add(clanMess);
        if (player.clan.messages.size() > 10) {
            player.clan.messages.remove(0); //REMOVE MESSAGE DAU TIEN;
            for (ClanMessage _clanMess : player.clan.messages) {
                _clanMess.id -= 1;
            }
        }
        //SEND CHAT TO CLAN
        sendMessageToClan(player.clan);
    }

    //ROI KHOI BANG HOI
    public void leaveThanhVien(Player player) {
        //GET CLAN SE ROI
        Clan clan = ClanManager.gI().getClanById(player.clan.id);
        //REMOVE CLAN TREN DATABASE
        ClanDAO.removeClanMember(player.id);

        player.rolePT = (byte) 2;
        player.clan = null;

        Message m = null;
        try {
            m = new Message(-53);
            m.writer().writeInt(-1);
            m.writer().flush();
            player.session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
        //SEND ITEMBAG CLAN TO ALL PLAYER IN MAP AND PLAYER JOIN
        if (player.zone.players.size() > 0) {
            for (Player _p : player.zone.players) {

                //UPDATE BAG SAU LUNG
                Service.gI().updateBagNew(_p.session, player.id, (byte) (-1));
                //GET BAG SAU LUNG
                ClanService.gI().getBagBangNew(_p.session, (byte) (-1));
                if (_p.id != player.id) {
                    loadUpdateInfoMember(_p.session, player);
                }
            }
        }
//        ClanDAO.removeClanMember(idPlayer);
        byte indexRemove = -1;
        for (byte i = 0; i < clan.members.size(); i++) {
            if (clan.members.get(i).id == player.id) {
                indexRemove = (byte) (i++);
                break;
            }
        }
        String nameRemove = clan.removeMemberByID(player.id);

        int id = (int) (clan.messages.size() + 1);
        ClanMessage clanMess = new ClanMessage(player, id, (byte) 0, "Đã rời khỏi bang", (byte) 2); //PLAYER, IDMESS, TYPEMEss, TExtMess, COLORMESS
        clan.messages.add(clanMess);
        if (clan.messages.size() > 10) {
            clan.messages.remove(0); //REMOVE MESSAGE DAU TIEN;
            for (ClanMessage _clanMess : clan.messages) {
                _clanMess.id -= 1;
            }
        }
        //SEND CHAT TO CLAN
        sendMessageToClan(clan);
    }

    //UPDATE LAI LIST MEM KHI LOAI THANH VIEN
    public void clanUpdateListMem(Session session, byte index) {
        Message m = null;
        try {
            m = new Message(-52);
            m.writer().writeByte((byte) 1); //type message
            m.writer().writeByte(index);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    //GIAI TAN BANG HOI
    public void distoryClan(Player p) {
        if (p.clan.leaderID == p.id) {
            ClanDAO.distory(p.clan.id);
            Clan clanDelete = ClanManager.gI().getClanById(p.clan.id);
            //assign flag delete to clan
            clanDelete.flagDestroy = true;
            for (Member _mem : clanDelete.members) {
                Player _p = PlayerManger.gI().getPlayerByUserID(_mem.id);
                if (_p != null) {
                    _p.rolePT = (byte) 2;
                    _p.clan = null;

                    Message m = null;
                    try {
                        m = new Message(-53);
                        m.writer().writeInt(-1);
                        m.writer().flush();
                        _p.session.sendMessage(m);
                        m.cleanup();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (m != null) {
                            m.cleanup();
                        }
                    }
                    //SEND ITEMBAG CLAN TO ALL PLAYER IN MAP AND PLAYER JOIN
                    if (_p.zone.players.size() > 0) {
                        for (Player _player : _p.zone.players) {

                            //UPDATE BAG SAU LUNG
                            Service.gI().updateBagNew(_player.session, _p.id, (byte) (-1));
                            //GET BAG SAU LUNG
                            ClanService.gI().getBagBangNew(_player.session, (byte) (-1));
                            if (_player.id != _p.id) {
                                loadUpdateInfoMember(_player.session, _p);
                            }
                        }
                    }
                }
            }
            ClanManager.gI().getClans().remove(clanDelete);
        } else {
            p.sendAddchatYellow("Chức năng chỉ dành cho chủ bang");
        }
    }

    public void setupNRSDClan(Player p, byte id) {
        for (Member _mem : p.clan.members) {
            Player _p = PlayerManger.gI().getPlayerByUserID(_mem.id);
            if (_p != null) {
                if (_p.id != p.id) {
                    _p.timeNRSD[id] = System.currentTimeMillis() - (long) 3600000;
                    _p.timeEndNRSD[id] = System.currentTimeMillis() + (long) (86400000);
                    _p.indexNRSD.add(id);
                    Service.gI().loadPoint(_p.session, _p);
                }
            } else {
                //update len DB luon
                PlayerDAO.updateNRSD(_mem.id, id);
            }
        }
    }

    //update data clan to database when sutdown server
    public void updateDataClanToDB() {
        for (Clan clan : ClanManager.gI().getClans()) {
            if (clan.flagCreate) {
                ClanDAO.create(clan);
                for (Member mem : clan.members) {
                    if (mem.id != clan.leaderID) {
                        ClanDAO.add(clan.id, mem);
                    }
                }
            } else if (clan.flagDestroy) {
                ClanDAO.distory(clan.id);
            } else {
                ClanDAO.updateAllDataClan(clan);
                for (Member mem : clan.members) {
                    ClanDAO.add(clan.id, mem);
                }
            }
        }
    }
}
