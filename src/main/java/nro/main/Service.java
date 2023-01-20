package nro.main;

import nro.part.Part;
import nro.part.PartImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import nro.io.Session;
import nro.clan.Clan;
import nro.clan.ClanService;
import nro.clan.ClanManager;
import nro.item.Item;
import nro.item.ItemSell;
import nro.item.ItemOption;
import nro.item.ItemTemplate;
import nro.item.useItem;
import nro.map.Map;
import nro.map.Mob;
import nro.map.Npc;
import nro.map.Zone;
import nro.map.ItemMap;
import nro.map.MapTemplate;
import nro.map.WayPoint;
import nro.player.Player;
import nro.player.Detu;
import nro.player.Boss;
import nro.player.PlayerManger;
import nro.task.TaskService;
import nro.skill.Skill;
import static nro.main.GameScr.saveFile;
import nro.io.Message;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Calendar;
import java.util.Date;
import nro.clan.ClanDAO;

public class Service {

    private static Service instance;

    public static Service gI() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    public void LoadDeTu(Player p, byte type) {
        Message m = null;
        if (type != 2) {
            try {
                m = new Message(-107);
                m.writer().writeByte(type);
                m.writer().flush();
                p.session.sendMessage(m);
                m.cleanup();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (m != null) {
                    m.cleanup();
                }
            }
            return;
        }
        if (p.detu == null) {
            p.sendAddchatYellow("Bạn chưa có đệ tử, hãy tiêu diệt Broly và thu nhận đệ tử cho mình");
            return;
        }
        try {
            m = new Message(-107);
            m.writer().writeByte(2); // type2 hien thi thong tin de tu
            m.writer().writeShort(p.detu.PartHead());
            m.writer().writeByte(p.detu.ItemBody.length);
            for (Item item : p.detu.ItemBody) {
                if (item == null) {
                    m.writer().writeShort(-1);
                } else {
                    m.writer().writeShort(item.template.id);
                    m.writer().writeInt(item.quantity);
                    m.writer().writeUTF(item.getInfo());
                    m.writer().writeUTF(item.getContent());
                    ArrayList<ItemOption> itemOptions = item.itemOptions;
                    m.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        m.writer().writeByte(itemOption.optionTemplate.id);
                        m.writer().writeShort(itemOption.param);
                    }

                }
            }
            m.writer().writeInt(p.detu.hp);
            m.writer().writeInt(p.detu.getHpFull());
            m.writer().writeInt(p.detu.mp);
            m.writer().writeInt(p.detu.getMpFull());
            m.writer().writeInt(p.detu.getDamFull());
            m.writer().writeUTF(p.detu.name);
            m.writer().writeUTF(p.detu.getStringLevel());
            m.writer().writeLong(p.detu.power);
            m.writer().writeLong(p.detu.tiemNang);
            m.writer().writeByte(p.statusPet); //status detu 0: di theo, 1 bao ve 2 tan cong 3 ve nha 4 hop the 5 hop the vinh vien
            m.writer().writeShort(p.detu.stamina);
            m.writer().writeShort(1000);
            m.writer().writeByte(p.detu.getCritFull());
            m.writer().writeShort(p.detu.getDefFull());
            m.writer().writeByte(p.detu.listSkill.size());
            m.writer().writeShort(p.detu.listSkill.get(0).skillId);
            if (p.detu.listSkill.get(1).skillId == -1) {
                m.writer().writeShort(p.detu.listSkill.get(1).skillId);
                m.writer().writeUTF("Cần 150 triệu sức mạnh để mở");
            } else {
                m.writer().writeShort(p.detu.listSkill.get(1).skillId);
            }
            if (p.detu.listSkill.get(2).skillId == -1) {
                m.writer().writeShort(p.detu.listSkill.get(2).skillId);
                m.writer().writeUTF("Cần 1,5 tỉ sức mạnh để mở");
            } else {
                m.writer().writeShort(p.detu.listSkill.get(2).skillId);
            }
            if (p.detu.listSkill.get(3).skillId == -1) {
                m.writer().writeShort(p.detu.listSkill.get(3).skillId);
                m.writer().writeUTF("Cần 20 tỉ sức mạnh để mở");
            } else {
                m.writer().writeShort(p.detu.listSkill.get(3).skillId);
            }
//            for (Skill skill : p.detu.listSkill) {
//                m.writer().writeShort(skill.skillId);
//                if(skill.skillId == -1) {
//                    if(skill.point == 1) {
//                        m.writer().writeUTF("Cần 150 triệu sức mạnh để mở");
//                    } else if (skill.point == 2) {
//                        m.writer().writeUTF("Cần 1,5 tỉ sức mạnh để mở");
//                    } else {
//                        m.writer().writeUTF("Cần 20 tỉ sức mạnh để mở");
//                    }
//                }
//            }
            m.writer().flush();
            p.session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loginDelay(Session session, short time) {
        Message m = null;
        try {
            m = new Message(122);
            m.writer().writeShort(time);
            m.writer().flush();

            session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loginThongBao(Session session) {
        Message m = null;
        try {
            m = new Message(-70);
            m.writer().writeShort(1139);
            m.writer().writeUTF("|7|Thông Báo Máy Chủ\n|2|Cập nhật nhận quà giáng sinh tại NPC Santa\nChúc các bạn có 1 mùa giáng sinh thật là vui vẻ");
            m.writer().writeByte(0);
            //return message;
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void LoadDeTuFollow(Player player, int type) {
        try {
            player.detu.id = (-player.id - 100000);
            player.detu.x = (short) (player.x - 25);
            player.detu.y = player.y;
//            PlayerManger.gI().getPlayers().add(player.detu);
            player.zone.pets.add(player.detu); //add detu vao list

            //NEU LOAD DE TU O MAP COOL
            if (player.map.MapCold()) {
                player.zone.upDownPointPETMapCool(player);
            }
            //NEU LOAD DE TU O MAP COOL
            for (Player pl : player.zone.players) {
                player.zone.loadInfoDeTu(pl.session, player.detu);
            }
            if (player.cPk != 0) {
                Service.gI().changeFlagPKPet(player, player.cPk);
            }
        } catch (Exception e) {
        }
    }

    public void LoadPetFollow(Player p) {
        try {
            p.pet.id = (-p.id - 300000);
            p.pet.x = (short) (p.x - 50);
            p.pet.y = p.y;
            p.zone.pet2s.add(p.pet); //add pet vao list

            for (Player pl : p.zone.players) {
                p.zone.loadInfoPet(pl.session, p.pet);
            }
        } catch (Exception e) {
        }
    }

    public short getHeadPet2(int idItem) {
        switch (idItem) {
            case 1114:
                return (short) 1158;
            case 1107:
                return (short) 1155;
            case 1046:
                return (short) 95;
            case 1040:
                return (short) 1092;
            case 1039:
                return (short) 1089;
            case 1008:
                return (short) 1074;
            case 967:
                return (short) 1050;
            case 944:
                return (short) 972;
            case 943:
                return (short) 969;
            case 942:
                return (short) 966;
            case 936:
                return (short) 718;
            case 919:
                return (short) 934;
            case 918:
                return (short) 925;
            case 917:
                return (short) 928;
            case 916:
                return (short) 931;
            case 910:
                return (short) 894;
            case 909:
                return (short) 897;
            case 908:
                return (short) 891;
            case 893:
                return (short) 885;
            case 892:
                return (short) 882;
        }
        return (short) (-1);
    }

    public void LoadCaiTrang(Player p, int type, int head, int body, int leg) {
//        System.out.println("ismonkey ao that day: " + p.isMonkey);
        Message m = null;
        try {
            m = new Message(-90);
            m.writer().writeByte(type);
            m.writer().writeInt(p.id);
            m.writer().writeShort(head);
            m.writer().writeShort(body);
            m.writer().writeShort(leg);
            m.writer().writeShort(0);
            m.writer().flush();
            if (p.zone.players.size() > 0) {
                for (Player _p : p.zone.players) {
                    if (_p != null && _p.session != null) {
                        _p.session.sendMessage(m);
                    }
                }
            } else {
                p.session.sendMessage(m);
            }
            m.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void LoadBodyPlayerChange(Player _player, int type, int head, int body, int leg) {
        Message m = null;
        try {
            m = new Message(-90);
            m.writer().writeByte(type);
            m.writer().writeInt(_player.id);
            m.writer().writeShort(head);
            m.writer().writeShort(body);
            m.writer().writeShort(leg);
            m.writer().writeShort(0);
            m.writer().flush();
            for (Player p : _player.zone.players) {
                p.session.sendMessage(m);
            }
            m.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    //LOAD BODY DETU CHANGE TO ALL PLAYER 
    public void LoadBodyDetuChange(ArrayList<Player> listPlayer, int type, int idpet, int head, int body, int leg) {
        Message m = null;
        try {
            m = new Message(-90);
            m.writer().writeByte(type);
            m.writer().writeInt(idpet);
            m.writer().writeShort(head);
            m.writer().writeShort(body);
            m.writer().writeShort(leg);
            m.writer().writeShort(0);
            m.writer().flush();
            for (Player p : listPlayer) {
                p.session.sendMessage(m);
            }
            m.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void sendTB(Session p, int avatar, String s, int typechat) {
        Message m = null;
        try {
            m = new Message(-70);
            m.writer().writeShort(avatar);
            m.writer().writeUTF(s);
            m.writer().writeByte(typechat);
            p.sendMessage(m);
            m.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    //SEND EFFECT
    public void sendEffectServer(byte loop, byte layer, byte ideff, short x, short y, short loopcount, Player p) {
        Message m = null;
        try {
            m = new Message(113);
            m.writer().writeByte(loop); //loop
            m.writer().writeByte(layer); //layer
            m.writer().writeByte(ideff); //ideff
            m.writer().writeShort(x);
            m.writer().writeShort(y);
            m.writer().writeShort(loopcount);
            for (Player _p : p.zone.players) {
                _p.session.sendMessage(m);
            }
            m.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void MagicTree(Player player, byte type) {
        Message m = null;
        try {
            m = new Message(-34);
            m.writer().writeByte(type);
            if (type == (byte) 0) {
                if (player.levelTree < 3) {
                    m.writer().writeShort((short) 84);
                } else if (player.levelTree >= 3 && player.levelTree < 5) {
                    m.writer().writeShort((short) 85);
                } else if (player.levelTree >= 5 && player.levelTree < 7) {
                    m.writer().writeShort((short) 86);
                } else {
                    m.writer().writeShort((short) (80 + player.levelTree));
                }
                m.writer().writeUTF("Đậu Thần Cấp " + player.levelTree);
                m.writer().writeShort(278); //x
                m.writer().writeShort(336); //y
                m.writer().writeByte(player.levelTree); //level
                long timeMax = (long) (player.maxBean * player.levelTree * 60000);
                long timeCool = System.currentTimeMillis() - player.lastTimeTree;
                if (timeCool > 0) {
                    player.currentBean = (byte) (timeCool > timeMax ? player.maxBean : (byte) (timeCool / (long) (player.levelTree * 60000)));
                } else {
                    player.currentBean = (byte) 0;
                }
                m.writer().writeShort(player.currentBean); //current bean
                m.writer().writeShort(player.maxBean); //max bean
                m.writer().writeUTF("Đang Kết Hạt");
                if (player.upMagicTree) {
//                    m.writer().writeInt(Server.gI().menu.timeUpMagicTree(player.levelTree));
                    m.writer().writeInt((int) ((player.lastTimeTree - System.currentTimeMillis()) / 1000));
                } else {
                    m.writer().writeInt(60 * (int) player.levelTree); //second
                }
                m.writer().writeByte(0);
                if (player.upMagicTree) {
                    m.writer().writeBoolean(true);
                } else {
                    m.writer().writeBoolean(false);
                }
            } else if (type == (byte) 2) {
                m.writer().writeShort(5);
                m.writer().writeInt(Server.gI().menu.timeUpMagicTree(player.levelTree));
            }
            player.session.sendMessage(m);
            m.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void SellItem(Player player, byte actionBuy, byte typeBuy, short indexBuy) {
        try {
            Item item = player.getIndexBag(indexBuy);
            if (item != null) {
                if (actionBuy == 0) {
                    if (item.template.id == 457) {
                        Service.gI().SaleDone(player, typeBuy, "Bạn có muốn bán\n" + "x1" + player.ItemBag[indexBuy].template.name + "\nVới giá 500tr vàng không?", indexBuy);
                    } else {
                        Service.gI().SaleDone(player, typeBuy, "Bạn có muốn bán\n" + "x" + player.ItemBag[indexBuy].quantity + " " + player.ItemBag[indexBuy].template.name + "\nVới giá 1 vàng không?", indexBuy);
                    }
                } else if (actionBuy == 1) {
                    if(item.template.id == 457 && player.ItemBag[indexBuy].quantity >= 1){
                        player.vang += 500000000L;
                        player.updateQuantityItemBag(indexBuy,player.ItemBag[indexBuy].quantity -1);
                        if(player.ItemBag[indexBuy].quantity == 1 ){
                            player.removeItemBag(indexBuy);
                        }
                    }
                    else{
                         player.removeItemBag(indexBuy);
                         player.vang += 1;
                    }
                   
                    Service.gI().updateItemBag(player);
                   
                    Service.gI().buyDone(player);
                } else {
                    player.sendAddchatYellow("Bán vật phẩm không thành công");
                }
            }
        } catch (Exception e) {
            player.sendAddchatYellow("Bán vật phẩm không thành công");
            e.printStackTrace();
        }
    }

    public void SaleDone(Player p, short type, String info, short id) {
        Message m;
        try {
            m = new Message(7);
            m.writer().writeByte(type);
            m.writer().writeShort(id);
            m.writer().writeUTF(info);
            p.session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UseItem(Player p, byte index, short id) {
        try {
            Item item = p.getIndexBag(index);
            if (item != null) {
                useItem.uesItem(p, item, (int) index, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DropDoneBody(Player p, byte type, String info, byte id, byte where) {
        Message m;
        try {
            m = new Message(-43);
            m.writer().writeByte(type);
            m.writer().writeByte(where);
            m.writer().writeByte(id);
            m.writer().writeUTF(info);
            p.session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DropDone(Player p, byte type, String info, byte id) {
        Message m;
        try {
            m = new Message(-43);
            m.writer().writeByte(type);
            m.writer().writeByte((byte) 1);
            m.writer().writeByte(id);
            m.writer().writeUTF(info);
            p.session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void PlayerMenu(Session s, Player p) {
        if (p != null) {
            Message m;
            try {
                m = new Message(-79);
                m.writer().writeInt(-1);
                m.writer().writeLong(p.power);
                m.writer().writeUTF("");
                s.sendMessage(m);
                m.cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void requestItemInfo(Player p, int typeUI, int indexUI) {
        Message message = null;
        try {
            message = new Message(35);
            message.writer().writeByte(typeUI);
            message.writer().writeByte(indexUI);
            p.session.sendMessage(message);
        } catch (Exception ex) {

        } finally {
            message.cleanup();
        }
    }

    public static void createCachePart() {
        try {
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bas);
            ByteArrayOutputStream parts = new ByteArrayOutputStream();
            DataOutputStream ds = new DataOutputStream(parts);
            ds = new DataOutputStream(parts);
            ds.writeShort(MainManager.parts.size());
            for (Part p : MainManager.parts) {
                ds.writeByte(p.type);
                for (PartImage pi : p.pi) {
                    ds.writeShort(pi.id);
                    ds.writeByte(pi.dx);
                    ds.writeByte(pi.dy);
                }
            }
            ds.flush();
            dos.writeInt(parts.toByteArray().length);
            dos.write(parts.toByteArray());
            byte[] ab = bas.toByteArray();
            saveFile("res/cache/vhalloween/data/NR_part", ab);
            ds.close();
            parts.close();
            dos.close();
            bas.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void serverMessage(Session session, String text) {
        Message msg;
        try {
            msg = new Message(-26);
            msg.writer().writeUTF(text);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public static void chatNPC(Player p, Short idnpc, String chat) {
        Message m = null;
        try {
            m = new Message(38);
            m.writer().writeShort(idnpc);
            m.writer().writeUTF(chat);
            m.writer().flush();
            p.session.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void loginDe(Session session, short second) {
        Message msg;
        try {
            msg = new Message(122);
            msg.writer().writeShort(second);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void versionImageSource(Session session) {
        Message msg;
        try {
            msg = new Message(-74);
            msg.writer().writeByte(0);
//            msg.writer().writeInt(5714013);
            msg.writer().writeInt(5880861);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sizeImageSource(Session session) {
        Message msg;
        try {
            msg = new Message(-74);
            msg.writer().writeByte(1);
//            msg.writer().writeShort(958);
//            if(session.zoomLevel == 2) {
//                msg.writer().writeShort(958);
////                msg.writer().writeShort(996);
//            } else if (session.zoomLevel == 4 || session.zoomLevel == 3) {
//                msg.writer().writeShort(955);
//            } else {
//                msg.writer().writeShort(85);
//            }
            //5880861
            if (session.zoomLevel == 2) {
//                msg.writer().writeShort(996);
                msg.writer().writeShort(997);
            } else if (session.zoomLevel == 4) {
                msg.writer().writeShort(993);
            } else if (session.zoomLevel == 3) {
                msg.writer().writeShort(992);
            } else {
                msg.writer().writeShort(92);
            }
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

//    public void imageSource(Session session) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Message msg;
//                try {
//                    
//                    File file = new File("data/res/DataX"+session.zoomLevel);
//                    Scanner scaner = new Scanner(file);
//                    String[] data2 = new String[958];
//                    int i = 0;
//                    while (scaner.hasNextLine()) {
//                        String data = scaner.nextLine();
//                        data2[i] = data;
//                        byte[] res = FileIO.readFile("data/res/5714013" + data2[i]);
//                        msg = new Message(-74);
//                        msg.writer().writeByte(2);
//                        msg.writer().writeUTF(data2[i]);
//                        msg.writer().writeInt(res.length);
//                        msg.writer().write(res);
//                        session.sendMessage(msg);
//                        msg.cleanup();
//                        i++;
//                    }
//                    scaner.close();
//
//                    msg = new Message(-74);
//                    msg.writer().writeByte(3);
//                    msg.writer().writeInt(5714013);
//                    session.sendMessage(msg);
//                    msg.cleanup();
//                } catch (Exception e) {
//                }
//            }
//        }).start();
//    }
    ///////////////////////IMAGEEEEEEEEEEEEEEE SOURCE VERSION 1
//    public void imageSource(Session session) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Message msg;
//                try {
////                    File myObj = new File("data/res/5714013.txt");
////                    Scanner sc = new Scanner(myObj);
//                    File file = new File("data/res/5714013.txt");
//                    Scanner scaner = new Scanner(file);
//                    String[] data2 = new String[958];
//                    int i = 0;
//                    while (scaner.hasNextLine()) {
//                        String data = scaner.nextLine();
//                        data2[i] = data;
//                        byte[] res = FileIO.readFile("data/res/5714013" + data2[i]);
//                        msg = new Message(-74);
//                        msg.writer().writeByte(2);
//                        msg.writer().writeUTF(data2[i]);
//                        msg.writer().writeInt(res.length);
//                        msg.writer().write(res);
////                        Util.debug(data2[i] + "  size img  " + res.length);
//                        session.sendMessage(msg);
//                        msg.cleanup();
//                        //    msg.writer().write(res);
//
//                        //    msg.cleanup();
//                        //      Thread.sleep(500);
//                        i++;
//                    }
//                    scaner.close();
//
//                    msg = new Message(-74);
//                    msg.writer().writeByte(3);
//                    msg.writer().writeInt(5714013);
//                    session.sendMessage(msg);
//                    msg.cleanup();
//                } catch (Exception e) {
//                }
//            }
//        }).start();
//    }
    public void imageSource(Session session) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg;
                try {
                    if (session.zoomLevel == 2) {
                        File file = new File("data/res/5880861x2.txt"); //VERSION 2
                        Scanner scaner = new Scanner(file);
//                        String[] data2 = new String[996];
                        String[] data2 = new String[997];
                        int i = 0;
                        while (scaner.hasNextLine()) {
                            String data = scaner.nextLine();
                            data2[i] = data;
                            byte[] res = FileIO.readFile("data/res/5880861/x2" + data2[i]);
                            msg = new Message(-74);
                            msg.writer().writeByte(2);
                            msg.writer().writeUTF(data2[i]);
                            msg.writer().writeInt(res.length);
                            msg.writer().write(res);
                            session.sendMessage(msg);
                            msg.cleanup();
                            i++;
                        }
                        scaner.close();
                    } else if (session.zoomLevel == 4) {
                        File file = new File("data/res/5880861x4.txt"); //VERSION 2
                        Scanner scaner = new Scanner(file);
                        String[] data2 = new String[993];
                        int i = 0;
                        while (scaner.hasNextLine()) {
                            String data = scaner.nextLine();
                            data2[i] = data;
                            byte[] res = FileIO.readFile("data/res/5880861/x4" + data2[i]);
                            msg = new Message(-74);
                            msg.writer().writeByte(2);
                            msg.writer().writeUTF(data2[i]);
                            msg.writer().writeInt(res.length);
                            msg.writer().write(res);
                            session.sendMessage(msg);
                            msg.cleanup();
                            i++;
                        }
                        scaner.close();
                    } else if (session.zoomLevel == 3) {
                        File file = new File("data/res/5880861x3.txt"); //VERSION 2
                        Scanner scaner = new Scanner(file);
                        String[] data2 = new String[992];
                        int i = 0;
                        while (scaner.hasNextLine()) {
                            String data = scaner.nextLine();
                            data2[i] = data;
                            byte[] res = FileIO.readFile("data/res/5880861/x3" + data2[i]);
                            msg = new Message(-74);
                            msg.writer().writeByte(2);
                            msg.writer().writeUTF(data2[i]);
                            msg.writer().writeInt(res.length);
                            msg.writer().write(res);
                            session.sendMessage(msg);
                            msg.cleanup();
                            i++;
                        }
                        scaner.close();
                    } else {
                        File file = new File("data/res/5880861x1.txt"); //VERSION 2
                        Scanner scaner = new Scanner(file);
                        String[] data2 = new String[92];
                        int i = 0;
                        while (scaner.hasNextLine()) {
                            String data = scaner.nextLine();
                            data2[i] = data;
                            byte[] res = FileIO.readFile("data/res/5880861/x1" + data2[i]);
                            msg = new Message(-74);
                            msg.writer().writeByte(2);
                            msg.writer().writeUTF(data2[i]);
                            msg.writer().writeInt(res.length);
                            msg.writer().write(res);
                            session.sendMessage(msg);
                            msg.cleanup();
                            i++;
                        }
                        scaner.close();
                    }

                    msg = new Message(-74);
                    msg.writer().writeByte(3);
                    msg.writer().writeInt(5880861);
                    session.sendMessage(msg);
                    msg.cleanup();
                } catch (Exception e) {
                }
            }
        }).start();
    }

    public void itemBg(Session session, int id) {
        Message msg;
        try {
//            Util.log("item_bg ID: " + id);
            byte[] item_bg = FileIO.readFile("data/map/item_bg/" + id);
            msg = new Message(-31);
            msg.writer().write(item_bg);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void bgTemp(Session session, int id) {
        Message msg;
        try {
//            Util.log("bgTemp ID: " + id);
            byte[] bg_temp = FileIO.readFile("data/bg_temp/x" + session.zoomLevel + "/" + id);
            msg = new Message(-32);
            msg.writer().write(bg_temp);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void effData(Session session, int id) {
        Message msg;
        try {
//            Util.log("effData ID: " + id);
            byte[] eff_data = FileIO.readFile("data/eff_data/x" + session.zoomLevel + "/" + id);
            msg = new Message(-66);
            msg.writer().write(eff_data);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void requestModTemplate(Session session, int id) {
        Message msg;
        try {
            if (session.zoomLevel == 2) {
                byte[] mob = FileIO.readFile("data/mob/" + id);
                msg = new Message(11);
                //            msg.writer().writeInt(id);
                //            msg.writer().writeInt(mob.length);
                msg.writer().write(mob);
                session.sendMessage(msg);
                msg.cleanup();
            } else if (session.zoomLevel != 2 && session.zoomLevel > 0 && session.zoomLevel < 5) {
                byte[] mob = FileIO.readFile("data/mob/x" + session.zoomLevel + "/" + id);
                msg = new Message(11);
                msg.writer().write(mob);
                session.sendMessage(msg);
                msg.cleanup();
            } else {
                byte[] mob = FileIO.readFile("data/mob/x1/" + id);
                msg = new Message(11);
                msg.writer().write(mob);
                session.sendMessage(msg);
                msg.cleanup();
            }
        } catch (Exception e) {
        }
    }

    public void sendMessage(Session session, int cmd, String filename) {
        Message msg;
        try {
            msg = new Message(cmd);
            msg.writer().write(FileIO.readFile("data/msg/" + filename));
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendNewMount(Session session, String filename) {
//        if(filename.equals("aura_0_0") || filename.equals("aura_0_1") || filename.equals("aura_1_1") || filename.equals("aura_1_0") || filename.equals("mount_1_0")
//        || filename.equals("mount_1_1")
//        || filename.equals("mount_2_0") || filename.equals("mount_2_1") || filename.equals("mount_3_0") || filename.equals("mount_3_1") || filename.equals("mount_4_0")
//        || filename.equals("mount_4_1") || filename.equals("mount_5_0") || filename.equals("mount_5_1") || filename.equals("mount_6_0") || filename.equals("mount_6_1")
//        || filename.equals("mount_7_0") || filename.equals("mount_7_1") || filename.equals("mount_8_0") || filename.equals("mount_8_1") || filename.equals("mount_9_0")
//        || filename.equals("mount_9_1") || filename.equals("mount_10_0") || filename.equals("mount_10_1") || filename.equals("set_eff_0_0") || filename.equals("set_eff_0_1")
//        || filename.equals("set_eff_1_0") || filename.equals("set_eff_1_1") || filename.equals("set_eff_4_0") || filename.equals("set_eff_4_1")) {
        Message msg;
        try {
            msg = new Message(66);
            msg.writer().write(FileIO.readFile("data/mount/x" + session.zoomLevel + "/" + filename));
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
//        }
    }

    public void updateVersion(Session session) {
        Message msg;
        try {
            msg = messageNotMap((byte) 4);
            msg.writer().writeByte(Server.manager.vsData);
            msg.writer().writeByte(Server.manager.vsMap);
            msg.writer().writeByte(Server.manager.vsSkill);
            msg.writer().writeByte(Server.manager.vsItem);
            msg.writer().writeByte(0);
            //send exp
//            msg.writer().writeByte(15);
            msg.writer().writeByte(27);
            msg.writer().writeLong(1000);
            msg.writer().writeLong(3000);
            msg.writer().writeLong(15000);
            msg.writer().writeLong(40000);
            msg.writer().writeLong(90000);
            msg.writer().writeLong(170000);
            msg.writer().writeLong(340000);
            msg.writer().writeLong(700000);
            msg.writer().writeLong(1500000);
            msg.writer().writeLong(15000000);
            msg.writer().writeLong(150000000);
            msg.writer().writeLong(1500000000);
            msg.writer().writeLong(5000000000L);
            msg.writer().writeLong(10000000000L);
            msg.writer().writeLong(40000000000L);
            msg.writer().writeLong(50010000000L);
            msg.writer().writeLong(60010000000L);
            msg.writer().writeLong(70010000000L);
            msg.writer().writeLong(81000000000L);
            msg.writer().writeLong(100010000000L);
            msg.writer().writeLong(130010000000L);
            msg.writer().writeLong(160010000000L);
            msg.writer().writeLong(180010000000L);
            msg.writer().writeLong(200010000000L);
            msg.writer().writeLong(230010000000L);
            msg.writer().writeLong(260010000000L);
            msg.writer().writeLong(300010000000L);
            msg.writer().write(FileIO.readFile("data/NRexp"));
            session.sendMessage(msg);
            msg.cleanup();

//            ByteArrayInputStream is = new ByteArrayInputStream(FileIO.readFile("data/1632811838304_-28_4_r"));
//            DataInputStream dis = new DataInputStream(is);
//            byte _Caplen = dis.readByte();
//            System.out.println("===================================================================================================================");
//            System.out.println("CAPTION LENT: " + _Caplen);
//            for(int i=0;i<_Caplen; i++) {
//                System.out.println("CAPTION EXP " + i + ": " + dis.readLong());
//            }
//            msg = new Message(-28);
//            msg.writer().write(FileIO.readFile("data/1632811838304_-28_4_r"));
//            session.sendMessage(msg);
//            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void updateData(Session session) {
        Message msg;
        try {
            msg = new Message(-87);
//            msg.writer().write(FileIO.readFile("data/NRdata_v47"));
            msg.writer().write(FileIO.readFile("data/1632811838531_-87_r"));
//            session.doSendMessage(msg);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
//        System.out.println("update data");
    }

    public void updateMap(Session session) {
        Message msg;
        try {
            //msg = messageNotMap((byte) 6);
            msg = new Message(-28);
//            msg.writer().write(FileIO.readFile("data/NRmap_v25"));
            msg.writer().write(FileIO.readFile("data/1632811838538_-28_6_r"));
//            session.doSendMessage(msg);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
//        System.out.println("update map");
    }

    public void updateSkill(Session session) {
        Message msg;
        try {
            //msg = messageNotMap((byte) 7);
            msg = new Message(-28);
//            msg.writer().write(FileIO.readFile("data/NRskill_v5"));
            msg.writer().write(FileIO.readFile("data/1632811838545_-28_7_r"));
//            session.doSendMessage(msg);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
//        System.out.println("update skill");
    }

    public void updateItem(Session session) {
        Message msg;
        try {
            //msg = messageNotMap((byte) 8);
            msg = new Message(-28);
            //msg.writer().write(FileIO.readFile("data/NRitem_v90_0"));
            msg.writer().write(FileIO.readFile("data/1632811838554_-28_8_r"));
//            session.doSendMessage(msg);
            session.sendMessage(msg);
            msg.cleanup();

            msg = new Message(-28);
            //msg.writer().write(FileIO.readFile("data/NRitem_v90_1"));
            msg.writer().write(FileIO.readFile("data/1632811838561_-28_8_r"));
//            session.doSendMessage(msg);
            session.sendMessage(msg);
            msg.cleanup();

            msg = new Message(-28);
            //msg.writer().write(FileIO.readFile("data/NRitem_v90_2"));
            msg.writer().write(FileIO.readFile("data/1632811838570_-28_8_r"));
//            session.doSendMessage(msg);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
//        System.out.println("update item");
    }

    public void tileSet(Session session, int id) {
        Message msg;
        try {
            //msg = messageNotMap((byte) 6);
            msg = new Message(-82);
            msg.writer().write(FileIO.readFile("data/map/tile_set/" + id));
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void tileSetReal(Session session) {
        Message msg;
        try {
            //msg = messageNotMap((byte) 6);
            msg = new Message(-82);
            msg.writer().write(FileIO.readFile("data/map/tile_set_real/20220812195141001_-82_r"));
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void mapInfo(Session session, Player pl) {
        Message msg;
        try {
            Map map = pl.map;
            msg = new Message(-24);
            msg.writer().writeByte(map.id);
            msg.writer().writeByte(map.template.planetId);
            msg.writer().writeByte(map.template.tileId);
            msg.writer().writeByte(map.template.bgId);
            msg.writer().writeByte(map.template.type);
            msg.writer().writeUTF(map.template.name);
            msg.writer().writeByte(0);
            msg.writer().writeShort(pl.x);
            msg.writer().writeShort(pl.y);

            // Load WayPoint class Map Template
            //    ArrayList<WayPoint> wayPoints = map.wayPoints;
            msg.writer().writeByte(map.template.wayPoints.length);
            for (WayPoint wp : map.template.wayPoints) {
                msg.writer().writeShort(wp.minX);
                msg.writer().writeShort(wp.minY);
                msg.writer().writeShort(wp.maxX);
                msg.writer().writeShort(wp.maxY);
                msg.writer().writeBoolean(wp.isEnter);
                msg.writer().writeBoolean(false);
                msg.writer().writeUTF(map.template.name);
            }
            // Load mob class Map Template
            msg.writer().writeByte(map.template.mobs.length);
            for (Mob mob : map.template.mobs) {
                msg.writer().writeBoolean(false);
                msg.writer().writeBoolean(false);
                msg.writer().writeBoolean(false);
                msg.writer().writeBoolean(false);
                msg.writer().writeBoolean(false);
                msg.writer().writeByte(mob.tempId);
                msg.writer().writeByte(0);
                msg.writer().writeInt(mob.hp);
                msg.writer().writeByte(mob.level);
                msg.writer().writeInt((mob.maxHp));
                msg.writer().writeShort(mob.pointX);
                msg.writer().writeShort(mob.pointY);
                msg.writer().writeByte(mob.status);
                msg.writer().writeByte(0);
                msg.writer().writeBoolean(false);
            }

            msg.writer().writeByte(0);

            // Load NPC class Map Template
            msg.writer().writeByte(map.template.npcs.length);
            for (Npc npc : map.template.npcs) {
                msg.writer().writeByte(npc.status);
                msg.writer().writeShort(npc.cx);
                msg.writer().writeShort(npc.cy);
                msg.writer().writeByte(npc.tempId);
                msg.writer().writeShort(npc.avartar);
            }

            msg.writer().writeByte(0);

            // bg item
            byte[] bgItem = FileIO.readFile("data/map/bg/" + map.id);
            msg.writer().write(bgItem);

            // eff item
            byte[] effItem = FileIO.readFile("data/map/eff/" + map.id);
            msg.writer().write(effItem);

            msg.writer().writeByte(map.bgType);
            msg.writer().writeByte(0);
            msg.writer().writeByte(0);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public static void PlayerAttack(Player p, Mob[] mob) {
        Message msg = null;
        try {
            msg = new Message(45);
            msg.writer().writeInt(p.id);
            msg.writer().writeByte(p.CSkill);
            for (byte i = 0; i < mob.length; i++) {
                if (mob[i] != null) {
                    msg.writer().writeByte(mob[i].tempId);
                }
            }
            msg.writer().flush();
            p.session.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public void addPlayer(Session session, Player player) {
        Message msg;
        try {
            msg = new Message(-5);
            msg.writer().writeInt(player.id);
            msg.writer().writeInt(-1);
            msg.writer().writeByte(10);
            msg.writer().writeBoolean(false);
            msg.writer().writeByte(0);
            msg.writer().writeByte(2);
            msg.writer().writeByte(2);
            msg.writer().writeShort(27);
            msg.writer().writeUTF(player.name);
            msg.writer().writeInt(54760);
            msg.writer().writeInt(54760);
            msg.writer().writeShort(player.PartBody());
            msg.writer().writeShort(player.Leg());
            msg.writer().writeByte(8);
            msg.writer().writeByte(-1);
            msg.writer().writeShort(player.x);
            msg.writer().writeShort(player.y);
            msg.writer().writeShort(0);
            msg.writer().writeShort(0);
            msg.writer().writeByte(0);
            msg.writer().writeByte(0);
            msg.writer().writeByte(0);
            msg.writer().writeShort(348);
            msg.writer().writeByte(0);
            msg.writer().writeByte(0);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removePlayer(Session session, Player player) {
        Message msg;
        try {
            msg = new Message(-6);
            msg.writer().writeInt(player.id);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void resetPoint(Session session, int x, int y) {
        Message msg;
        try {
            msg = new Message(46);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            session.sendMessage(msg);
            msg.cleanup();

        } catch (Exception e) {
        }
    }

    public void login2(Session session, String user) {
        Message msg;
        try {
            msg = new Message(-101);
            msg.writer().writeUTF(user);
            session.sendMessage(msg);
            msg.cleanup();

        } catch (Exception e) {
        }
    }

    public void mapTemp(Session session, int id) {
        Message msg;
        try {
            //msg = messageNotMap((byte) 6);
            msg = new Message(-28);
            msg.writer().write(FileIO.readFile("data/map/temp/" + id));
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void updateBag(Session session) {
        Message msg;
        try {
            msg = new Message(-64);
            msg.writer().writeInt(0);// id char
            msg.writer().writeByte(0);// id bag
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void updateBagNew(Session session, int id, byte idBag) {
        Message msg;
        try {
            msg = new Message(-64);
            msg.writer().writeInt(id);// id char
            msg.writer().writeByte(idBag);// id bag
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void updateBody(Session session) {
        Message msg;
        try {
            msg = new Message(-90);
            msg.writer().writeByte(1);
            msg.writer().writeInt(1);
            msg.writer().writeShort(177);
            msg.writer().writeShort(178);
            msg.writer().writeShort(179);
            msg.writer().writeByte(0);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void updateSloganClan(Session session, String text) {
        ClanManager.gI().getClanById(session.player.clan.id).slogan = text;
//        ClanDAO.updateSlogan(session.player.clan.id, text);
        Message msg;
        try {
            msg = new Message(-46);
            msg.writer().writeByte(4);
            msg.writer().writeByte(session.player.clan.imgID);
            msg.writer().writeUTF(text);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void clearMap(Session session) {
        Message msg;
        try {
            msg = new Message(-22);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void messagePlayerMenu(Session session, int charId) {
        Message message = null;
        try {
            message = new Message(-30);
            message.writer().writeByte(63);
            message.writer().writeInt(charId);
            session.sendMessage(message);
        } catch (Exception ex) {
        }
    }

    public void playerMove(Session session, Player pl) {
        Message msg;
        try {
            msg = new Message(-7);
            msg.writer().writeInt(pl.id);
            msg.writer().writeShort(pl.x);
            msg.writer().writeShort(pl.y);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void stamina(Session session) {
        Message msg;
        try {
            msg = new Message(-68);
            msg.writer().writeShort(10000);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void maxStamina(Session session) {
        Message msg;
        try {
            msg = new Message(-69);
            msg.writer().writeShort(10000);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void activePoint(Session session) {
        Message msg;
        try {
            msg = new Message(-97);
            msg.writer().writeInt(1000);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void loadPoint(Session session, Player pl) {
        Message msg;
        try {
            msg = new Message(-42);
            msg.writer().writeInt(pl.hpGoc);
            msg.writer().writeInt(pl.mpGoc);
            msg.writer().writeInt(pl.damGoc);
            msg.writer().writeInt(pl.getHpFull());// hp full
            msg.writer().writeInt(pl.getMpFull());// mp full
            if (pl.hp > pl.getHpFull()) {
                pl.hp = pl.getHpFull();
            }
            if (pl.mp > pl.getMpFull()) {
                pl.mp = pl.getMpFull();
            }
            msg.writer().writeInt(pl.hp);// hp
            msg.writer().writeInt(pl.mp);// mp
            msg.writer().writeByte(pl.getSpeed());// speed
            msg.writer().writeByte(20);
            msg.writer().writeByte(20);
            msg.writer().writeByte(1);
            msg.writer().writeInt(pl.getDamFull());// dam full
            msg.writer().writeInt(pl.getDefFull());// def full
            msg.writer().writeByte(pl.getCritFull());// crit full
            msg.writer().writeLong(pl.tiemNang);
            msg.writer().writeShort(100);
            msg.writer().writeShort(pl.defGoc);
            msg.writer().writeByte(pl.critGoc);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buyDone(Player p) {
        Message m;
        try {
            m = new Message(6);
            m.writer().writeLong(p.vang);
            m.writer().writeInt(p.ngoc);
            m.writer().writeInt(p.ngocKhoa);
            m.writer().writeInt(p.thanhvien);
            m.writer().writeInt(p.sotien);
            m.writer().writeInt(p.pointSuKien);
            m.writer().writeInt(p.nhanqua);
            p.session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
        }
    }

    public void loadPlayer(Session session, Player pl) {
        Message msg;
        try {
            msg = messageSubCommand((byte) 0);
            msg.writer().writeInt(pl.id);
            msg.writer().writeByte(pl.taskId);
            msg.writer().writeByte(pl.gender);
            msg.writer().writeShort(pl.PartHead());
            msg.writer().writeUTF(pl.name);
            msg.writer().writeByte(0);
            msg.writer().writeByte(0);
            msg.writer().writeLong(pl.power);
            msg.writer().writeShort(0);
            msg.writer().writeShort(0);
            msg.writer().writeByte(pl.gender);
            //--------skill---------
            ArrayList<Skill> skills = pl.skill;
            msg.writer().writeByte(skills.size());
            for (Skill skill : skills) {
                msg.writer().writeShort(skill.skillId);
            }
            //---vang---luong--luongKhoa
//            msg.writer().writeInt(pl.vang);
            msg.writer().writeLong(pl.vang);
            msg.writer().writeInt(pl.ngocKhoa);
            msg.writer().writeInt(pl.ngoc);

            if (true) {
                msg.writer().writeByte(pl.ItemBody.length);
                for (int i = 0; i < pl.ItemBody.length; i++) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeByte(pl.ItemBag.length);
                for (int i = 0; i < pl.ItemBag.length; i++) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeByte(pl.ItemBox.length);
                for (int i = 0; i < pl.ItemBox.length; i++) {
                    msg.writer().writeShort(-1);
                }
            } else {
                //--------itemBody---------
                Item[] itemsBody = pl.ItemBody;
                msg.writer().writeByte(itemsBody.length);
                for (Item item : itemsBody) {
                    if (item == null) {
                        msg.writer().writeShort(-1);
                    } else {
                        msg.writer().writeShort((short) item.template.id);
                        msg.writer().writeInt(item.quantity);
                        msg.writer().writeUTF(item.getInfo());
                        msg.writer().writeUTF(item.getContent());
                        ArrayList<ItemOption> itemOptions = item.itemOptions;
                        msg.writer().writeByte(itemOptions.size());
                        for (ItemOption itemOption : itemOptions) {
                            msg.writer().writeByte(itemOption.optionTemplate.id);
                            msg.writer().writeShort((short) itemOption.param);
                        }
                    }

                }

                //--------itemBag---------
                Item[] itemsBag = pl.ItemBag;
                msg.writer().writeByte(itemsBag.length);
                for (int i = 0; i < itemsBag.length; i++) {
                    Item item = itemsBag[i];
                    if (item == null) {
                        msg.writer().writeShort(-1);
                    } else {
                        msg.writer().writeShort((short) item.template.id);
                        msg.writer().writeInt(item.quantity);
                        msg.writer().writeUTF(item.getInfo());
                        msg.writer().writeUTF(item.getContent());
                        ArrayList<ItemOption> itemOptions = item.itemOptions;
                        msg.writer().writeByte(itemOptions.size());
                        for (ItemOption itemOption : itemOptions) {
                            msg.writer().writeByte(itemOption.optionTemplate.id);
                            msg.writer().writeShort((short) itemOption.param);
                        }
                    }

                }
                //--------itemBox---------
                Item[] itemsBox = pl.ItemBox;
                msg.writer().writeByte(itemsBox.length);
                for (int i = 0; i < itemsBox.length; i++) {
                    Item item = itemsBox[i];
                    if (item == null) {
                        msg.writer().writeShort(-1);
                    } else {
                        msg.writer().writeShort((short) item.template.id);
                        msg.writer().writeInt(item.quantity);
                        msg.writer().writeUTF(item.getInfo());
                        msg.writer().writeUTF(item.getContent());
                        ArrayList<ItemOption> itemOptions = item.itemOptions;
                        msg.writer().writeByte(itemOptions.size());
                        for (ItemOption itemOption : itemOptions) {
                            msg.writer().writeByte(itemOption.optionTemplate.id);
                            msg.writer().writeShort((short) itemOption.param);
                        }
                    }
                }
                //-----------------
            }
            //       msg.writer().writeShort(252);
//            msg.writer().write(FileIO.readFile("res/cache/headv219"));

//            msg.writer().write(FileIO.readFile("res/cache/v221/head"));
            ByteArrayInputStream is = new ByteArrayInputStream(FileIO.readFile("res/cache/vhalloween/head"));
            DataInputStream dis = new DataInputStream(is);
            short lengthHead = dis.readShort();
//            msg.writer().writeShort(lengthHead);
            msg.writer().writeShort((short) 301); //vhalloween
//            msg.writer().writeShort((short)297); //v222
//            msg.writer().writeShort((short)289); //v221
            for (short i = 0; i < lengthHead; i++) {
                msg.writer().writeShort(dis.readShort());
                msg.writer().writeShort(dis.readShort());
            }
            //black goku
            msg.writer().writeShort((short) 550);
            msg.writer().writeShort((short) 6102);
            //hoa da
            msg.writer().writeShort((short) 454);
            msg.writer().writeShort((short) 4423);
            //bill
            msg.writer().writeShort((short) 508);
            msg.writer().writeShort((short) 5067);
            //champa
            msg.writer().writeShort((short) 511);
            msg.writer().writeShort((short) 5068);
            //whis
            msg.writer().writeShort((short) 838);
            msg.writer().writeShort((short) 7710);
            //cadic
            msg.writer().writeShort((short) 645);
            msg.writer().writeShort((short) 8208);
            //nappa
            msg.writer().writeShort((short) 648);
            msg.writer().writeShort((short) 6089);
//            msg.writer().writeShort(992);
//            msg.writer().writeShort(8928);
            //-----------------
            msg.writer().writeShort(514);
            msg.writer().writeShort(515);
            msg.writer().writeShort(537);
            msg.writer().writeByte(pl.NhapThe);
            msg.writer().writeInt(1632811835);
//            msg.writer().writeByte(0);
            msg.writer().writeByte(1);

            //AURA EFF
            if (pl.idAura != (short) (-1)) {
                msg.writer().writeShort(pl.idAura);
                msg.writer().writeByte((byte) 0);
                msg.writer().writeShort((short) 0);
            }
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateItemBody(Player player) {
        Message msg;
        try {
            Item[] itemsBody = player.ItemBody;
            msg = new Message(-37);
            msg.writer().writeByte(0);
            msg.writer().writeShort(player.PartHead());
            msg.writer().writeByte(itemsBody.length);
            for (Item item : itemsBody) {
                if (item == null) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    ArrayList<ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }

                }
            }
            player.session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            System.out.println("-37 " + e.toString());
        }
    }

    public void updateItemBag(Player player) {
        Message msg;
        try {
            Item[] itemsBody = player.ItemBag;
            msg = new Message(-36);
            msg.writer().writeByte(0);
            msg.writer().writeByte(itemsBody.length);
            for (Item item : itemsBody) {
                if (item == null) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    ArrayList<ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }

                }
            }
            player.session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateItemBox(Player player) {
        Message msg;
        try {
            Item[] itemsBox = player.ItemBox;
            msg = new Message(-35);
            msg.writer().writeByte(0);
            msg.writer().writeByte(itemsBox.length);
            for (Item item : itemsBox) {
                if (item == null) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    ArrayList<ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }

                }
            }
            player.session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void upgrade(Player player, byte action, byte indexUI) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(5);
            msg.writer().writeShort(indexUI);
            player.session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            System.out.println("-107 " + e.toString());
        }
    }

    public void statusDetu(Player player) {
        Message msg;
        try {
            msg = new Message(31);
            msg.writer().writeInt(player.id);
            msg.writer().writeByte(1);
            msg.writer().writeShort(6);
            msg.writer().writeByte(1);
            msg.writer().writeByte(1);
            player.session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            System.out.println("-107 " + e.toString());
        }
    }

    public void CHAGE_MOD_BODY(Player player) {
        Message msg;
        try {
            msg = new Message(-84);
            msg.writer().writeByte(player.ItemBody.length);
            msg.writer().writeInt(player.hp);
            player.session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void deTu(Player player) {
        Message msg;
        try {
            msg = new Message(-107);
            Item[] itemsBody = new Item[7];
            msg.writer().writeByte(2);
            msg.writer().writeShort(player.PartHead());
            msg.writer().writeByte(itemsBody.length);
            for (Item item : itemsBody) {
                if (item == null) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    ArrayList<ItemOption> itemOptions = item.itemOptions;
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }

                }
            }
            msg.writer().writeByte(player.hp);
            msg.writer().writeByte(player.getHpFull());
            msg.writer().writeByte(player.mp);
            msg.writer().writeByte(player.getMpFull());
            msg.writer().writeByte(player.getDamFull());
            msg.writer().writeUTF(player.name);
            msg.writer().writeUTF("10000");
            msg.writer().writeLong(player.power);
            msg.writer().writeLong(player.tiemNang);
            msg.writer().writeByte(0);
            msg.writer().writeShort(0);
            msg.writer().writeShort(0);
            msg.writer().writeByte(player.getCritFull());
            msg.writer().writeShort(player.getDefFull());
            Skill[] skills = new Skill[4];
            msg.writer().writeByte(4);
            for (int i = 0; i < 4; i++) {
                if (skills[i] != null) {
                    msg.writer().writeShort(skills[i].skillId);
                } else {
                    msg.writer().writeShort(-1);
                    msg.writer().writeUTF("Yêu cầu sức mạnh đạt");
                }
            }
            player.session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            System.out.println("-107 " + e.toString());
        }
    }

    public void GET_PLAYER_MENU(Player player) {
        Message msg;
        try {
            msg = new Message(63);
            msg.writer().writeByte(1);
            msg.writer().writeUTF("Chủ Thân");
            msg.writer().writeUTF("Đệ tử");
            msg.writer().writeShort(1);
            player.session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            System.out.println("-63 " + e.toString());
        }
    }

    public void chat(Session session, int playerId, String text) {
        Message msg;
        try {
            msg = new Message(44);
            msg.writer().writeInt(playerId);
            msg.writer().writeUTF(text);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendTB(Player p, String title, String s) {
        Message m = null;
        try {
            m = new Message(92);
            m.writer().writeUTF(title);
            m.writer().writeUTF(s);
            m.writer().flush();
            p.session.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }

    }

    public void serverTB(Player p, String str) {
        Message m = null;
        try {
            m = new Message(93);
            m.writer().writeUTF(str);
            m.writer().flush();
            p.session.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    //Client chát server
    public void ChatGolbaL(Player p, String str) {
        Message m = null;
        try {
            m = new Message(92);
            m.writer().writeUTF(p.name);
            m.writer().writeUTF("|5|" + str);
            m.writer().writeInt(p.id);
            m.writer().writeShort(p.PartHead());
            //head ICON
            m.writer().writeShort(p.iconIDHead());

            String bodyLeg = Service.gI().writePartBodyLeg(p);
            String[] arrOfStr = bodyLeg.split(",", 2);
            m.writer().writeShort(Short.parseShort(arrOfStr[0]));
//            m.writer().writeShort(p.PartBody());
            if (p.clan != null) {
                m.writer().writeShort((short) p.clan.imgID);
            } else {
                m.writer().writeShort(-1);
            }
            m.writer().writeShort(Short.parseShort(arrOfStr[1]));
//            m.writer().writeShort(p.Leg());
            m.writer().writeByte(0);
            PlayerManger.gI().SendMessageServer(m);
            m.writer().flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void ChatPrivate(Player p, Player pSend, String str) {
        if (p != null && pSend != null) {
            Message m = null;
            try {
                m = new Message(92);
                m.writer().writeUTF(pSend.name);
                m.writer().writeUTF("|2|" + str);
                m.writer().writeInt(pSend.id);
                m.writer().writeShort(pSend.PartHead());
                m.writer().writeShort(pSend.iconIDHead());
                String bodyLeg = Service.gI().writePartBodyLeg(pSend);
                String[] arrOfStr = bodyLeg.split(",", 2);
                m.writer().writeShort(Short.parseShort(arrOfStr[0]));
                //            m.writer().writeShort(p.PartBody());
                if (pSend.clan != null) {
                    m.writer().writeShort((short) pSend.clan.imgID);
                } else {
                    m.writer().writeShort(-1);
                }
                m.writer().writeShort(Short.parseShort(arrOfStr[1]));
                //            m.writer().writeShort(p.Leg());
                m.writer().writeByte((byte) 1);
                p.session.sendMessage(m);
                pSend.session.sendMessage(m);
                m.writer().flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (m != null) {
                    m.cleanup();
                }
            }
        }
    }

    private Message messageNotLogin(byte command) throws IOException {
        Message ms = new Message(-29);
        ms.writer().writeByte(command);
        return ms;
    }

    private Message messageNotMap(byte command) throws IOException {
        Message ms = new Message(-28);
        ms.writer().writeByte(command);
        return ms;
    }

    private Message messageSubCommand(byte command) throws IOException {
        Message ms = new Message(-30);
        ms.writer().writeByte(command);
        return ms;
    }
    //Rồng Namek
//     public void RongNamek(Session p) {
//        Message m = null;
//        try {
//            m = new Message(-83);
//            m.writer().writeByte(0);
//            m.writer().writeShort(p.player.map.id);
//            m.writer().writeShort(66);
//            m.writer().writeInt(0);
//	    m.writer().writeInt(1);
//	    m.writer().writeUTF("Ahuhu");
//	    m.writer().writeShort(p.player.x);
//	    m.writer().writeShort(p.player.y);
//	    m.writer().writeByte(1);
//            m.writer().flush();
//            p.sendMessage(m);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if(m != null) {
//                m.cleanup();
//            }
//        }
//    }

    public void drawFlagPK(Player player) {
        Message m = null;
        try {
            m = new Message(-103);
            m.writer().writeByte(0);
            m.writer().writeByte(12);

            // thao co
            m.writer().writeShort(363); // id flag
            m.writer().writeByte(1); //so luong option trong mang
            m.writer().writeByte(73); // id option
            m.writer().writeShort(0); // gia tri option
            for (int i = 364; i <= 370; i++) {
                m.writer().writeShort(i); // id flag
                m.writer().writeByte(1); //so luong option trong mang
                m.writer().writeByte(88); // id option
                m.writer().writeShort(5); // gia tri option
            }
            //co den
            m.writer().writeShort(371); // id flag
            m.writer().writeByte(1); //so luong option trong mang
            m.writer().writeByte(88); // id option
            m.writer().writeShort(10); // gia tri option
            //co xanhduong
            m.writer().writeShort(747); // id flag
            m.writer().writeByte(1); //so luong option trong mang
            m.writer().writeByte(88); // id option
            m.writer().writeShort(5); // gia tri option
            //co kaio
            m.writer().writeShort(519);
            m.writer().writeByte(1); //so luong option trong mang
            m.writer().writeByte(88); // id option
            m.writer().writeShort(5); // gia tri option
            //co mabu
            m.writer().writeShort(520);
            m.writer().writeByte(1); //so luong option trong mang
            m.writer().writeByte(88); // id option
            m.writer().writeShort(5); // gia tri option

            m.writer().flush();
            player.session.sendMessage(m);
            m.cleanup();

        } catch (Exception var2) {
            var2.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    //doi co cho player
    public void changeFlagPK(Player player, byte type) {
//        player.cPk = type;
        Message m = null;
        if (type != 0) {
            //draw flag gui hinh anh flag sang truoc
            try {
                m = new Message(-103);
                m.writer().writeByte((byte) 2);
                m.writer().writeByte(type);
                switch (type) {
                    case 1:
                        m.writer().writeShort(2330);
                        break;
                    case 2:
                        m.writer().writeShort(2323);
                        break;
                    case 3:
                        m.writer().writeShort(2327);
                        break;
                    case 4:
                        m.writer().writeShort(2326);
                        break;
                    case 5:
                        m.writer().writeShort(2324);
                        break;
                    case 6:
                        m.writer().writeShort(2329);
                        break;
                    case 7:
                        m.writer().writeShort(2328);
                        break;
                    case 8:
                        m.writer().writeShort(2331);
                        break;
                    case 9:
                        m.writer().writeShort(2325);
                        break;
                    case 10:
                        m.writer().writeShort(4386);
                        break;
                    case 11:
                        m.writer().writeShort(4385);
                        break;
                }
//                m.writer().writeShort(2330);
                m.writer().flush();
                for (Player p : player.zone.players) {
                    if (p != null && p.session != null) {
                        p.session.sendMessage(m);
                    }
                }
                m.cleanup();

            } catch (Exception var2) {
                var2.printStackTrace();
            } finally {
                if (m != null) {
                    m.cleanup();
                }
            }
            //send type flag
            try {
                m = new Message(-103);
                m.writer().writeByte(1);
                m.writer().writeInt(player.id);
                m.writer().writeByte(type);
                m.writer().flush();
                for (Player p : player.zone.players) {
                    if (p != null && p.session != null) {
                        p.session.sendMessage(m);
                    }
                }
                m.cleanup();

            } catch (Exception var2) {
                var2.printStackTrace();
            } finally {
                if (m != null) {
                    m.cleanup();
                }
            }
        } else {
            try {
                m = new Message(-103);
                m.writer().writeByte(1);
                m.writer().writeInt(player.id);
                m.writer().writeByte(0);
                m.writer().flush();
                for (Player p : player.zone.players) {
                    if (p != null && p.session != null) {
                        p.session.sendMessage(m);
                    }
                }
                m.cleanup();

            } catch (Exception var2) {
                var2.printStackTrace();
            } finally {
                if (m != null) {
                    m.cleanup();
                }
            }
        }

    }

    public void changeFlagPKPet(Player player, byte type) {
//        player.cPk = type;
        Message m = null;
        if (type != 0) {
            //draw flag gui hinh anh flag sang truoc
            try {
                m = new Message(-103);
                m.writer().writeByte(2);
                m.writer().writeByte(type);
                switch (type) {
                    case 1:
                        m.writer().writeShort(2330);
                        break;
                    case 2:
                        m.writer().writeShort(2323);
                        break;
                    case 3:
                        m.writer().writeShort(2327);
                        break;
                    case 4:
                        m.writer().writeShort(2326);
                        break;
                    case 5:
                        m.writer().writeShort(2324);
                        break;
                    case 6:
                        m.writer().writeShort(2329);
                        break;
                    case 7:
                        m.writer().writeShort(2328);
                        break;
                    case 8:
                        m.writer().writeShort(2331);
                        break;
                    case 9:
                        m.writer().writeShort(2325);
                        break;
                    case 10:
                        m.writer().writeShort(4386);
                        break;
                    case 11:
                        m.writer().writeShort(4385);
                        break;
                }
//                m.writer().writeShort(2330);
                m.writer().flush();
                for (Player p : player.zone.players) {
                    if (p != null && p.session != null) {
                        p.session.sendMessage(m);
                    }
                }
                m.cleanup();

            } catch (Exception var2) {
                var2.printStackTrace();
            } finally {
                if (m != null) {
                    m.cleanup();
                }
            }
            //send type flag
            try {
                m = new Message(-103);
                m.writer().writeByte(1);
                m.writer().writeInt(player.detu.id);
                m.writer().writeByte(type);
                m.writer().flush();
                for (Player p : player.zone.players) {
                    if (p != null && p.session != null) {
                        p.session.sendMessage(m);
                    }
                }
                m.cleanup();

            } catch (Exception var2) {
                var2.printStackTrace();
            } finally {
                if (m != null) {
                    m.cleanup();
                }
            }
        } else {
            try {
                m = new Message(-103);
                m.writer().writeByte(1);
                m.writer().writeInt(player.detu.id);
                m.writer().writeByte(0);
                m.writer().flush();
                for (Player p : player.zone.players) {
                    if (p != null && p.session != null) {
                        p.session.sendMessage(m);
                    }
                }
                m.cleanup();

            } catch (Exception var2) {
                var2.printStackTrace();
            } finally {
                if (m != null) {
                    m.cleanup();
                }
            }
        }

    }

    //send pet hoi sinh
    public void petLiveFromDead(Player _player) {
        _player.zone.chat(_player.detu, "Sư phụ ơi con đây rồi!");
        Message m = null;
        try {
            _player.detu.hp = _player.detu.getHpFull();
            _player.detu.mp = _player.detu.getMpFull();
            _player.detu.isdie = false;

            m = new Message(84);
            m.writer().writeInt(_player.detu.id);
            m.writer().writeShort(_player.x);
            m.writer().writeShort(_player.y);
            m.writer().flush();
            for (Player pl : _player.zone.players) {
                pl.session.sendMessage(m);
            }
            m.cleanup();
        } catch (Exception var2) {
            var2.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    //send pet hoi sinh
    public void SendPetLiveFromDead(Player _player) {
        _player.zone.chat(_player.detu, "Sư phụ ơi con đây rồi!");
        Message m = null;
        try {
            _player.detu.hp = _player.detu.getHpFull();
            _player.detu.mp = _player.detu.getMpFull();
            _player.detu.isdie = false;

            m = new Message(84);
            m.writer().writeInt(_player.detu.id);
            m.writer().writeShort(_player.detu.x);
            m.writer().writeShort(_player.detu.y);
            m.writer().flush();
            for (Player pl : _player.zone.players) {
                pl.session.sendMessage(m);
            }
            m.cleanup();
        } catch (Exception var2) {
            var2.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    //PPET COMEBACK
    public void petComebackLogin(Player p) {
        Timer timerHs = new Timer();
        TimerTask tt = new TimerTask() {
            public void run() {
                if (p.detu != null && p.detu.isdie) {
                    p.timerHSDe = null;
                    p.statusPet = (byte) 0;
                    p.detu.hp = p.detu.getHpFull();
                    p.detu.mp = p.detu.getMpFull();
                    p.detu.isdie = false;
                    p.petfucus = 1;
                    LoadDeTuFollow(p, 1);
                    p.zone.chat(p.detu, "Sư phụ ơi con đây rồi!");
//                    SendPetLiveFromDead(p); //dang tan cong nen hoi sinh tai cho
                }
                timerHs.cancel();
            }
        ;
        };
        timerHs.schedule(tt, 10000);
        p.timerHSDe = timerHs;
    }
//    public void setSessionLogin(Player p, Session session) {
//        Timer timerHs = new Timer();
//        TimerTask tt = new TimerTask() {
//            public void run()
//            {
//                if(session.checkSendTrue()) {
//                    session.isLogin = true;
//                    p.setSession = null;
//                }
//                timerHs.cancel();
//            };
//        };
//        timerHs.schedule(tt, 10000);
//        p.setSession = timerHs;
//    }

    //code chay the nay cho nhanh haha =)))
    public void openUITransport(Player player) {
        player.moveToSaoDen = false;
        Message m = null;
        int idMapHere = player.map.id;
        int i = 0;
        try {
            m = new Message(-91);
            if (idMapHere == 47 || idMapHere == 45 || idMapHere == 0 || idMapHere == 7 || idMapHere == 14 || idMapHere == 5 || idMapHere == 20 || idMapHere == 13
                    || idMapHere == 27 || idMapHere == 19 || idMapHere == 79 || idMapHere == 84 || (idMapHere == 21 && player.gender == 0) || (idMapHere == 22 && player.gender == 1)
                    || (idMapHere == 23 && player.gender == 2) || (player.map.id == 24 && player.gender == 0) || (player.map.id == 25 && player.gender == 1) || (player.map.id == 26 && player.gender == 2)) {
                m.writer().writeByte(13);
            } else {
                m.writer().writeByte(14);
            }

            if ((idMapHere != 21 && player.gender == 0) || (idMapHere != 22 && player.gender == 1) || (idMapHere != 23 && player.gender == 2)) {
                m.writer().writeUTF("Về nhà");
                if (player.gender == 0) {
                    m.writer().writeUTF("Trái Đất");
                    player.mapTransport[i] = 21;
                    i++;
                } else if (player.gender == 1) {
                    m.writer().writeUTF("Namếc");
                    player.mapTransport[i] = 22;
                    i++;
                } else {
                    m.writer().writeUTF("Xay da");
                    player.mapTransport[i] = 23;
                    i++;
                }
            }

            if (player.map.id != 47) {
                m.writer().writeUTF("Rừng Karin");
                m.writer().writeUTF("Trái Đất");
                player.mapTransport[i] = 47;
                i++;
            }
            if (player.map.id != 45) {
                m.writer().writeUTF("Thần điện");
                m.writer().writeUTF("Trái Đất");
                player.mapTransport[i] = 45;
                i++;
            }
            if (player.map.id != 0) {
                m.writer().writeUTF("Làng Aru");
                m.writer().writeUTF("Trái Đất");
                player.mapTransport[i] = 0;
                i++;
            }
            if (player.map.id != 7) {
                m.writer().writeUTF("Làng Mori");
                m.writer().writeUTF("Namếc");
                player.mapTransport[i] = 7;
                i++;
            }
            if (player.map.id != 14) {
                m.writer().writeUTF("Làng Kakarot");
                m.writer().writeUTF("Xay da");
                player.mapTransport[i] = 14;
                i++;
            }
            if (player.map.id != 5) {
                m.writer().writeUTF("Đảo Kamê");
                m.writer().writeUTF("Trái Đất");
                player.mapTransport[i] = 5;
                i++;
            }
            if (player.map.id != 20) {
                m.writer().writeUTF("Vách núi đen");
                m.writer().writeUTF("Xay da");
                player.mapTransport[i] = 20;
                i++;
            }
            if (player.map.id != 13) {
                m.writer().writeUTF("Đảo Guru");
                m.writer().writeUTF("Namếc");
                player.mapTransport[i] = 13;
                i++;
            }
            if ((player.map.id != 24 && player.gender == 0) || (player.map.id != 25 && player.gender == 1) || (player.map.id != 26 && player.gender == 2)) {
                m.writer().writeUTF("Trạm tàu vũ trụ");
                m.writer().writeUTF("Trạm tàu vũ trụ");
                if (player.gender == 0) {
                    player.mapTransport[i] = 24;
                    i++;
                } else if (player.gender == 1) {
                    player.mapTransport[i] = 25;
                    i++;
                } else {
                    player.mapTransport[i] = 26;
                    i++;
                }
            }
            if (player.map.id != 27) {
                m.writer().writeUTF("Rừng Bamboo");
                m.writer().writeUTF("Trái đất");
                player.mapTransport[i] = 27;
                i++;
            }
            if (player.map.id != 19) {
                m.writer().writeUTF("Thành phố Vegeta");
                m.writer().writeUTF("Xay da");
                player.mapTransport[i] = 19;
                i++;
            }
            if (player.map.id != 79) {
                m.writer().writeUTF("Núi khỉ đỏ");
                m.writer().writeUTF("Fide");
                player.mapTransport[i] = 79;
                i++;
            }
            if (player.map.id != 84) {
                m.writer().writeUTF("Siêu thị");
                m.writer().writeUTF("Trái đất");
                player.mapTransport[i] = 84;
                i++;
            }

            m.writer().flush();
            player.session.sendMessage(m);
            m.cleanup();

        } catch (Exception var2) {
            var2.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void openUISaoDen(Player p) {
        p.moveToSaoDen = true;
        Message m = null;
        int i = 0;
        try {
            m = new Message(-91);
            m.writer().writeByte((byte) 7);

            m.writer().writeUTF("Hành tinh M-2");
            m.writer().writeUTF("Vũ trụ");
            p.mapSaoDen[i] = 85;
            i++;
            m.writer().writeUTF("Hành tinh Polaris");
            m.writer().writeUTF("Vũ trụ");
            p.mapSaoDen[i] = 86;
            i++;
            m.writer().writeUTF("Hành tinh Cretaceous");
            m.writer().writeUTF("Vũ trụ");
            p.mapSaoDen[i] = 87;
            i++;
            m.writer().writeUTF("Hành tinh Monmaasu");
            m.writer().writeUTF("Vũ trụ");
            p.mapSaoDen[i] = 88;
            i++;
            m.writer().writeUTF("Hành tinh Rudeeze");
            m.writer().writeUTF("Vũ trụ");
            p.mapSaoDen[i] = 89;
            i++;
            m.writer().writeUTF("Hành tinh Gelbo");
            m.writer().writeUTF("Vũ trụ");
            p.mapSaoDen[i] = 90;
            i++;
            m.writer().writeUTF("Hành tinh Tigere");
            m.writer().writeUTF("Vũ trụ");
            p.mapSaoDen[i] = 91;
            i++;

            m.writer().flush();
            p.session.sendMessage(m);
            m.cleanup();

        } catch (Exception var2) {
            var2.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    //NANG CAP ITEM
    public void EpStarItem(Player _player, byte _index, byte _index2) {
        Item _item = _player.ItemBag[_index];
        Item _item2 = _player.ItemBag[_index2];
        int starItem = 0;
        int starUse = 0;
        String _info = "";
        if (_item == null || _item2 == null) {
            return;
        }
        if ((_item.template.type == 0 || _item.template.type == 1 || _item.template.type == 2 || _item.template.type == 3 || _item.template.type == 4 || _item.template.type == 32)
                && ((_item2.template.id >= 14 && _item2.template.id <= 20) || (_item2.template.type == 30))) {
//            (_item2.template.type != 0 && _item2.template.type !=  1 &&_item2.template.type !=  2 && _item2.template.type !=  3 && _item2.template.type !=  4)
            starItem = _item.getParamItemByID(107); //so spl tat ca
            starUse = _item.getParamItemByID(102);//so spl da ep
            _info = _item.getInfoEpStar(_player, _item2.id);
            _player._itemUpStar = _item;
            _player._itemUseEpStar = _item2;
            _player._indexUpStar = _index;
            _player._indexEpStar = _index2;
        } else if ((_item2.template.type == 0 || _item2.template.type == 1 || _item2.template.type == 2 || _item2.template.type == 3 || _item2.template.type == 4 || _item2.template.type == 32)
                && ((_item.template.id >= 14 && _item.template.id <= 20) || (_item.template.type == 30))) {
//            (_item.template.type != 0 && _item.template.type !=  1 &&_item.template.type !=  2 && _item.template.type !=  3 && _item.template.type !=  4)
            starItem = _item2.getParamItemByID(107); //so spl tat ca
            starUse = _item2.getParamItemByID(102);//so spl da ep
            _info = _item2.getInfoEpStar(_player, _item.id);
            _player._itemUpStar = _item2;
            _player._itemUseEpStar = _item;
            _player._indexUpStar = _index2;
            _player._indexEpStar = _index;
        } else {
            Service.gI().serverMessage(_player.session, "Không thể kết hợp!");
            return;
        }
        if (starUse < 8 && starUse < starItem && starItem > 0 && starItem <= 8 && _player._itemUseEpStar.quantity >= 1 && _player._checkDapDo && (System.currentTimeMillis() - _player._timeDapDo) >= 1000) {
//            Util.log("STRING INFO ITEM: " + _info);
            Message m = null;
            try {
                m = new Message(32);
                m.writer().writeShort(21);
                m.writer().writeUTF(_info);
                if (_player.ngoc >= 10) {
                    m.writer().writeByte(2);
                    m.writer().writeUTF("Nâng cấp 10 ngọc");
                    m.writer().writeUTF("Từ chối");
                } else {
                    m.writer().writeByte(1);
                    m.writer().writeUTF("Cần 10 ngọc");
                }

                m.writer().flush();
                _player.session.sendMessage(m);
                m.cleanup();
            } catch (Exception var2) {
                var2.printStackTrace();
            } finally {
                if (m != null) {
                    m.cleanup();
                }
            }
        } else {
            Service.gI().serverMessage(_player.session, "Vật phẩm của bạn không có sao pha lê!");
        }
    }

    public void sendEpStarItem(Player _player) {
        Item _itemUp = _player._itemUpStar; //Item duoc ep
        Item _itemEp = _player._itemUseEpStar; //item ep va mat
        Message m = null;
        if (_itemUp == null || _itemEp == null) {
            return;
        }
        if (_itemUp.template.type != 0 && _itemUp.template.type != 1 && _itemUp.template.type != 2 && _itemUp.template.type != 3 && _itemUp.template.type != 4 && _itemUp.template.type != 32) {
            return;
        }
        int _saoDaEp = _itemUp.getParamItemByID(102);
        int _saoTong = _itemUp.getParamItemByID(107);
        if ((System.currentTimeMillis() - _player._timeDapDo) >= 1000 && _player._checkDapDo && (_saoDaEp < _saoTong) && _saoDaEp < 8 && _saoTong <= 8) {
            _player._timeDapDo = System.currentTimeMillis();
            _player._checkDapDo = false;
            boolean chkAddUseDone = false;
            if (_itemUp != null && _itemEp != null && _player.ngoc >= 10) {
                _player.ngoc -= 10;
                updateVangNgoc(_player);

                int itemOptionEp = _itemUp.mapItemEpStarToOption(_itemEp.id); //id item option
                if (_player.ItemBag[_player._indexUpStar].getParamItemByID(itemOptionEp) == 0) { //khong co param cua option day
                    if (_player.ItemBag[_player._indexUpStar].getParamItemByID(102) == 0) {
                        ItemOption itemOptionNew = new ItemOption(102, 1);
                        _player.ItemBag[_player._indexUpStar].itemOptions.add(itemOptionNew);
                        chkAddUseDone = true;
                    } else {
                        for (int i = 0; i < _player.ItemBag[_player._indexUpStar].itemOptions.size(); i++) {
                            if (_player.ItemBag[_player._indexUpStar].itemOptions.get(i).id == 102) { //update so sao phale da ep
//                                _player.ItemBag[_player._indexUpStar].itemOptions.get(i).param += 1;
                                _player.ItemBag[_player._indexUpStar].itemOptions.get(i).param = (_player.ItemBag[_player._indexUpStar].itemOptions.get(i).param + 1) > 8 ? 8 : (_player.ItemBag[_player._indexUpStar].itemOptions.get(i).param + 1);
                                chkAddUseDone = true;
                                break;
                            }
                        }
                    }
                    //Them OPTION PARAM CUA CHI SO MOI CHUWA Co
                    if (chkAddUseDone) {
                        ItemOption itemOptionNew = new ItemOption(itemOptionEp, _itemUp.mapItemEpParamPerStar(_itemEp.id));
                        _player.ItemBag[_player._indexUpStar].itemOptions.add(itemOptionNew);
                        chkAddUseDone = false;
                        _player._checkDapDo = true;
                    }
                } else {
                    for (int i = 0; i < _player.ItemBag[_player._indexUpStar].itemOptions.size(); i++) {
                        if (_player.ItemBag[_player._indexUpStar].itemOptions.get(i).id == 102) { //update so sao phale da ep
//                            _player.ItemBag[_player._indexUpStar].itemOptions.get(i).param += 1;
                            _player.ItemBag[_player._indexUpStar].itemOptions.get(i).param = (_player.ItemBag[_player._indexUpStar].itemOptions.get(i).param + 1) > 8 ? 8 : (_player.ItemBag[_player._indexUpStar].itemOptions.get(i).param + 1);
                            chkAddUseDone = true;
                            for (int j = 0; j < _player.ItemBag[_player._indexUpStar].itemOptions.size(); j++) {
                                if (_player.ItemBag[_player._indexUpStar].itemOptions.get(j).id == itemOptionEp && chkAddUseDone) {
                                    _player.ItemBag[_player._indexUpStar].itemOptions.get(j).param += _itemUp.mapItemEpParamPerStar(_itemEp.id);
                                    chkAddUseDone = false;
                                    _player._checkDapDo = true;
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
                _player.ItemBag[_player._indexEpStar].quantity -= 1;
                if (_player.ItemBag[_player._indexEpStar].quantity == 0) {
                    _player.ItemBag[_player._indexEpStar] = null;
                }
                updateItemBag(_player);
                sendUpStarSuccess(_player);
            }
        }
        //RESET ALL
        resetItemDapDo(_player);
    }

    public void upgradeStarItem(Player _player, byte _index) {
        Item _item = _player.ItemBag[_index];
        if (_item == null) {
            return;
        }
        if (_item.template.type != 0 && _item.template.type != 1 && _item.template.type != 2 && _item.template.type != 3 && _item.template.type != 4 && _item.template.type != 32) {
            Service.gI().serverMessage(_player.session, "Không thể nâng cấp!");
            return;
        } else {
            _player._itemUpStar = _item;
            _player._indexUpStar = _index;
            String _info = _player._itemUpStar.getInfoUpStar(_player);
            if (_player._itemUpStar.getParamItemByID(107) < 8) {
                //            Util.log("STRING INFO ITEM: " + _info);
                Message m = null;
                try {
                    m = new Message(32);
                    m.writer().writeShort(21);
                    m.writer().writeUTF(_info);
                    if (_player.vang >= (_player._itemUpStar.getGoldUpStar(_player._itemUpStar._starItem) * 1000000L) && (_player.ngoc >= (_player._itemUpStar._starItem + 1))) {
                        m.writer().writeByte(2);
                        m.writer().writeUTF("Nâng cấp");
                        m.writer().writeUTF("Từ chối");
                    } else {
                        m.writer().writeByte(1);
                        m.writer().writeUTF("Cần " + _player._itemUpStar.getGoldUpStar(_player._itemUpStar._starItem) + "Tr\nvàng, " + (_player._itemUpStar._starItem + 1) + " ngọc");
                    }

                    m.writer().flush();
                    _player.session.sendMessage(m);
                    m.cleanup();
                } catch (Exception var2) {
                    var2.printStackTrace();
                } finally {
                    if (m != null) {
                        m.cleanup();
                    }
                }
            } else {
                Service.gI().serverMessage(_player.session, "Trang bị của ngươi đã Max Sao Pha Lê!");
            }
        }

    }

    public void sendUpStarItem(Player _player) {
        Item _itemUp = _player._itemUpStar;
        if (_itemUp == null) {
            return;
        }
        if (_itemUp.template.type != 0 && _itemUp.template.type != 1 && _itemUp.template.type != 2 && _itemUp.template.type != 3 && _itemUp.template.type != 4 && _itemUp.template.type != 32) {
            Service.gI().serverMessage(_player.session, "Không thể nâng cấp!");
            return;
        } else {
            Message m = null;
            if ((System.currentTimeMillis() - _player._timeDapDo) >= 1000 && _player._checkDapDo) {
                _player._timeDapDo = System.currentTimeMillis();
                _player._checkDapDo = false;
                if (_itemUp != null && (_player.vang >= (_itemUp.getGoldUpStar(_itemUp._starItem) * 1000000L)) && (_player.ngoc >= (_itemUp._starItem + 1)) && (_itemUp.getParamItemByID(107) < 8)) {
                    _player.vang -= (_itemUp.getGoldUpStar(_itemUp._starItem) * 1000000L);
                    _player.ngoc -= (_itemUp._starItem + 1);
                    int rdUp = Util.nextInt(0, 100);
                    if (_itemUp._starItem == 7) {
                        rdUp = Util.nextInt(0, 200);
                    }
                    if (rdUp <= _itemUp.getPercentUpStar(_itemUp._starItem)) {
                        updateVangNgoc(_player);
                        if (_player.ItemBag[_player._indexUpStar].getParamItemByID(107) == 0) {
                            ItemOption itemOptionNew = new ItemOption(107, 1);
                            _player.ItemBag[_player._indexUpStar].itemOptions.add(itemOptionNew);
                        } else {
                            for (int i = 0; i < _player.ItemBag[_player._indexUpStar].itemOptions.size(); i++) {
                                if (_player.ItemBag[_player._indexUpStar].itemOptions.get(i).id == 107) {
                                    _player.ItemBag[_player._indexUpStar].itemOptions.get(i).param = (_player.ItemBag[_player._indexUpStar].itemOptions.get(i).param + 1) > 8 ? 8 : (_player.ItemBag[_player._indexUpStar].itemOptions.get(i).param + 1);
                                }
                            }
                        }
                        updateItemBag(_player);
                        sendUpStarSuccess(_player);
                    } else {
                        updateVangNgoc(_player);
                        sendUpStarError(_player);
                    }
                }
                _player._checkDapDo = true;
            }
        }
        //RESET ALL
        resetItemDapDo(_player);
    }

    public void sendUpStarSuccess(Player _player) {
        Message m = null;
        try {
            m = new Message(-81);
            m.writer().writeByte(2);
            m.writer().flush();
            _player.session.sendMessage(m);
            m.cleanup();
        } catch (Exception var2) {
            var2.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void sendUpStarError(Player _player) {
        Message m = null;
        try {
            m = new Message(-81);
            m.writer().writeByte(3);
            m.writer().flush();
            _player.session.sendMessage(m);
            m.cleanup();
        } catch (Exception var2) {
            var2.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void sendOpenItem(Player _player, short IDICON, short IDICONOPEN) {
        Message m = null;
        try {
            m = new Message(-81);
            m.writer().writeByte(6);
            m.writer().writeShort((short) IDICONOPEN);
            m.writer().writeShort(IDICON);
            m.writer().flush();
            _player.session.sendMessage(m);
            m.cleanup();
        } catch (Exception var2) {
            var2.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void updateVangNgoc(Player _player) {
        Message m = null;
        try {
            m = new Message(-30);
            m.writer().writeByte(4);
            m.writer().writeLong(_player.vang); //vang
            m.writer().writeInt(_player.ngoc); //ngoc
            m.writer().writeInt(_player.hp); //hp
            m.writer().writeInt(_player.mp); //mp
            m.writer().writeInt(_player.ngocKhoa); //ngockhoa
            m.writer().flush();
            _player.session.sendMessage(m);
            m.cleanup();
        } catch (Exception var2) {
            var2.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void upgradeLevelItem(Player _player, byte _index, byte _index2, byte _index3) {
        Item _item = _player.ItemBag[_index]; //get item 1
        Item _item2 = _player.ItemBag[_index2]; //get item 2
        if (_item == null || _item2 == null) {
            return;
        }
        Item _daBaoVe = null;
        if (_index3 != (byte) (-1)) {
            _daBaoVe = _player.ItemBag[_index3]; //get da bao ve
            if (_daBaoVe == null) {
                return;
            }
        }
        int lvItem = 0;
        int starUse = 0;
        String _info = "";
        if ((_item.template.type == 0 || _item.template.type == 1 || _item.template.type == 2 || _item.template.type == 3 || _item.template.type == 4)
                && (_item2.template.type != 0 && _item2.template.type != 1 && _item2.template.type != 2 && _item2.template.type != 3 && _item2.template.type != 4)) {
            if (_item.template.type == 0 && _item2.id == 223) { // type 0:ao, id 223 la titan
                _info = _item.getInfoUpgradeItem(_player, 223, _index2);
            } else if (_item.template.type == 1 && _item2.id == 222) { // type 1:quan, id 222 la ruby
                _info = _item.getInfoUpgradeItem(_player, 222, _index2);
            } else if (_item.template.type == 2 && _item2.id == 224) { // type 2:gang, id 224 la tat
                _info = _item.getInfoUpgradeItem(_player, 224, _index2);
            } else if (_item.template.type == 3 && _item2.id == 221) { // type 3:giay, id 221 la saphia
                _info = _item.getInfoUpgradeItem(_player, 221, _index2);
            } else if (_item.template.type == 4 && _item2.id == 220) { // type 4:rada, id 220 la luc bao
                _info = _item.getInfoUpgradeItem(_player, 220, _index2);
            }

            lvItem = _item.getParamItemByID(72); // level do
            _player._itemUpStar = _item;
            _player._itemUseEpStar = _item2;
            _player._indexUpStar = _index;
            _player._indexEpStar = _index2;
            if (_index3 != (byte) (-1)) {
                _player._indexDaBaoVe = _index3;
                _player._itemDaBaoVe = _daBaoVe;
            }
        } else if (_item2.template.type == 0 || _item2.template.type == 1 || _item2.template.type == 2 || _item2.template.type == 3 || _item2.template.type == 4
                && (_item.template.type != 0 && _item.template.type != 1 && _item.template.type != 2 && _item.template.type != 3 && _item.template.type != 4)) {
            if (_item2.template.type == 0 && _item.id == 223) { // type 0:ao, id 223 la titan
                _info = _item2.getInfoUpgradeItem(_player, 223, _index);
            } else if (_item2.template.type == 1 && _item.id == 222) { // type 1:quan, id 222 la ruby
                _info = _item2.getInfoUpgradeItem(_player, 222, _index);
            } else if (_item2.template.type == 2 && _item.id == 224) { // type 2:gang, id 224 la tat
                _info = _item2.getInfoUpgradeItem(_player, 224, _index);
            } else if (_item2.template.type == 3 && _item.id == 221) { // type 3:giay, id 221 la saphia
                _info = _item2.getInfoUpgradeItem(_player, 221, _index);
            } else if (_item2.template.type == 4 && _item.id == 220) { // type 4:rada, id 220 la luc bao
                _info = _item2.getInfoUpgradeItem(_player, 220, _index);
            }
            lvItem = _item2.getParamItemByID(72); //so spl tat ca
            _player._itemUpStar = _item2;
            _player._itemUseEpStar = _item;
            _player._indexUpStar = _index2;
            _player._indexEpStar = _index;
            if (_index3 != (byte) (-1)) {
                _player._indexDaBaoVe = _index3;
                _player._itemDaBaoVe = _daBaoVe;
            }
        } else {
            Service.gI().serverMessage(_player.session, "Không thể kết hợp!");
            return;
        }
        if (lvItem < 8 && _info != "") {
//        if(lvItem < 7 && _player._itemUseEpStar.quantity >= _item.getCountStoneUpItem(lvItem) && _info != "") {
//            Util.log("INFOOOOOOOOOOOOOOOOO: " + _info);
            Message m = null;
            try {
                m = new Message(32);
                m.writer().writeShort(21);
                m.writer().writeUTF(_info);
                if (_player.vang >= _item.getGoldUpItem(lvItem)) {
                    m.writer().writeByte(2);
                    m.writer().writeUTF("Nâng cấp\n" + _item.getGoldUpItem(lvItem) + " k vàng");
                    m.writer().writeUTF("Từ chối");
                } else {
                    m.writer().writeByte(1);
                    m.writer().writeUTF("Cần " + _item.getGoldUpItem(lvItem) + " k vàng");
                }

                m.writer().flush();
                _player.session.sendMessage(m);
                m.cleanup();
            } catch (Exception var2) {
                var2.printStackTrace();
            } finally {
                if (m != null) {
                    m.cleanup();
                }
            }
        } else {
            Service.gI().serverMessage(_player.session, "Không thể kết hợp!");
        }
    }

    public void sendUpLevelItem(Player _player) {
        Item _itemUp = _player._itemUpStar;
        Item _stone = _player._itemUseEpStar;
        if (_itemUp == null || _stone == null) {
            return;
        }
        Item _daBaoVe = null;
        if (_player._itemDaBaoVe != null && _player._indexDaBaoVe != (byte) (-1)) {
            _daBaoVe = _player._itemDaBaoVe;
            if (_daBaoVe == null) {
                return;
            }
        }
        Message m = null;
        if (_itemUp.template.type != 0 && _itemUp.template.type != 1 && _itemUp.template.type != 2 && _itemUp.template.type != 3 && _itemUp.template.type != 4) {
            return;
        }
        if ((System.currentTimeMillis() - _player._timeDapDo) >= 1000 && _player._checkDapDo) {
            _player._timeDapDo = System.currentTimeMillis();
            _player._checkDapDo = false;
            if (_itemUp != null && _stone != null && (_player.vang >= (_itemUp.getGoldUpItem(_itemUp._levelItem) * 1000)) && (_player.ItemBag[_player._indexEpStar].quantity >= _itemUp.getCountStoneUpItem(_itemUp._levelItem))) {
                _player.vang -= (_itemUp.getGoldUpItem(_itemUp._levelItem) * 1000);
                _player.ItemBag[_player._indexEpStar].quantity -= _itemUp.getCountStoneUpItem(_itemUp._levelItem);
                if (_player.ItemBag[_player._indexEpStar].quantity == 0) {
                    _player.ItemBag[_player._indexEpStar] = null;
                }
                int rdUp = Util.nextInt(0, 100);
                if (rdUp <= _itemUp.getPercentUpItem(_itemUp._levelItem)) {
                    updateVangNgoc(_player);
                    if (_player.ItemBag[_player._indexUpStar].getParamItemByID(72) == 0) {
                        ItemOption itemOptionNew = new ItemOption(72, 1);
                        _player.ItemBag[_player._indexUpStar].itemOptions.add(itemOptionNew);

                        if (_player.ItemBag[_player._indexUpStar].getParamItemByID(_itemUp.mapStoneUpToOption(_stone.id)) != 0) {
                            for (int i = 0; i < _player.ItemBag[_player._indexUpStar].itemOptions.size(); i++) {
                                if (_player.ItemBag[_player._indexUpStar].itemOptions.get(i).id == _itemUp.mapStoneUpToOption(_stone.id)) {
                                    if (_player.ItemBag[_player._indexUpStar].template.type == 4) { //RADA THI TANG 1 chi mang
                                        _player.ItemBag[_player._indexUpStar].itemOptions.get(i).param += 1;
                                    } else {
                                        _player.ItemBag[_player._indexUpStar].itemOptions.get(i).param = (int) Math.ceil(_player.ItemBag[_player._indexUpStar].itemOptions.get(i).param * 1.1);
                                    }
                                }
                            }
                        } else if (_stone.id == 222) {
                            for (int i = 0; i < _player.ItemBag[_player._indexUpStar].itemOptions.size(); i++) {
                                if (_player.ItemBag[_player._indexUpStar].itemOptions.get(i).id == 22) {
                                    _player.ItemBag[_player._indexUpStar].itemOptions.get(i).param = (int) Math.ceil(_player.ItemBag[_player._indexUpStar].itemOptions.get(i).param * 1.1);
                                }
                            }
                        } else if (_stone.id == 221) {
                            for (int i = 0; i < _player.ItemBag[_player._indexUpStar].itemOptions.size(); i++) {
                                if (_player.ItemBag[_player._indexUpStar].itemOptions.get(i).id == 23) {
                                    _player.ItemBag[_player._indexUpStar].itemOptions.get(i).param = (int) Math.ceil(_player.ItemBag[_player._indexUpStar].itemOptions.get(i).param * 1.1);
                                }
                            }
                        }
                    } else {
                        if (_player.ItemBag[_player._indexUpStar].getParamItemByID(_itemUp.mapStoneUpToOption(_stone.id)) != 0) {
                            for (int i = 0; i < _player.ItemBag[_player._indexUpStar].itemOptions.size(); i++) {
                                if (_player.ItemBag[_player._indexUpStar].itemOptions.get(i).id == 72) {
                                    _player.ItemBag[_player._indexUpStar].itemOptions.get(i).param += 1;
                                }
                                if (_player.ItemBag[_player._indexUpStar].itemOptions.get(i).id == _itemUp.mapStoneUpToOption(_stone.id)) {
                                    if (_player.ItemBag[_player._indexUpStar].template.type == 4) { //RADA THI TANG 1 chi mang
                                        _player.ItemBag[_player._indexUpStar].itemOptions.get(i).param += 1;
                                    } else {
                                        _player.ItemBag[_player._indexUpStar].itemOptions.get(i).param = (int) Math.ceil(_player.ItemBag[_player._indexUpStar].itemOptions.get(i).param * 1.1);
                                    }
                                }
                            }
                        } else if (_stone.id == 222) {
                            for (int i = 0; i < _player.ItemBag[_player._indexUpStar].itemOptions.size(); i++) {
                                if (_player.ItemBag[_player._indexUpStar].itemOptions.get(i).id == 72) {
                                    _player.ItemBag[_player._indexUpStar].itemOptions.get(i).param += 1;
                                }
                                if (_player.ItemBag[_player._indexUpStar].itemOptions.get(i).id == 22) {
                                    _player.ItemBag[_player._indexUpStar].itemOptions.get(i).param = (int) Math.ceil(_player.ItemBag[_player._indexUpStar].itemOptions.get(i).param * 1.1);
                                }
                            }
                        } else if (_stone.id == 221) {
                            for (int i = 0; i < _player.ItemBag[_player._indexUpStar].itemOptions.size(); i++) {
                                if (_player.ItemBag[_player._indexUpStar].itemOptions.get(i).id == 72) {
                                    _player.ItemBag[_player._indexUpStar].itemOptions.get(i).param += 1;
                                }
                                if (_player.ItemBag[_player._indexUpStar].itemOptions.get(i).id == 23) {
                                    _player.ItemBag[_player._indexUpStar].itemOptions.get(i).param = (int) Math.ceil(_player.ItemBag[_player._indexUpStar].itemOptions.get(i).param * 1.1);
                                }
                            }
                        }
                    }
                    sendUpStarSuccess(_player);
                } else {
                    if (_daBaoVe == null) {
                        if (_player.ItemBag[_player._indexUpStar].getParamItemByID(_itemUp.mapStoneUpToOption(_stone.id)) != 0) {
                            if (_itemUp._levelItem == 2 || _itemUp._levelItem == 4 || _itemUp._levelItem == 6 || _itemUp._levelItem == 7) {
                                for (int i = 0; i < _player.ItemBag[_player._indexUpStar].itemOptions.size(); i++) {
                                    if (_player.ItemBag[_player._indexUpStar].itemOptions.get(i).id == 72) {
                                        _player.ItemBag[_player._indexUpStar].itemOptions.get(i).param -= 1;
                                    }
                                    if (_player.ItemBag[_player._indexUpStar].itemOptions.get(i).id == _itemUp.mapStoneUpToOption(_stone.id)) {
                                        if (_player.ItemBag[_player._indexUpStar].template.type == 4) { //RADA THI TANG 1 chi mang
                                            _player.ItemBag[_player._indexUpStar].itemOptions.get(i).param -= 1;
                                        } else {
                                            _player.ItemBag[_player._indexUpStar].itemOptions.get(i).param = (int) Math.floor(_player.ItemBag[_player._indexUpStar].itemOptions.get(i).param * 0.9);
                                        }
                                    }
                                }
                            }
                        } else if (_stone.id == 222) {
                            if (_itemUp._levelItem == 2 || _itemUp._levelItem == 4 || _itemUp._levelItem == 6 || _itemUp._levelItem == 7) {
                                for (int i = 0; i < _player.ItemBag[_player._indexUpStar].itemOptions.size(); i++) {
                                    if (_player.ItemBag[_player._indexUpStar].itemOptions.get(i).id == 72) {
                                        _player.ItemBag[_player._indexUpStar].itemOptions.get(i).param -= 1;
                                    }
                                    if (_player.ItemBag[_player._indexUpStar].itemOptions.get(i).id == 22) {
                                        _player.ItemBag[_player._indexUpStar].itemOptions.get(i).param = (int) Math.floor(_player.ItemBag[_player._indexUpStar].itemOptions.get(i).param * 0.9);
                                    }
                                }
                            }
                        } else if (_stone.id == 221) {
                            if (_itemUp._levelItem == 2 || _itemUp._levelItem == 4 || _itemUp._levelItem == 6 || _itemUp._levelItem == 7) {
                                for (int i = 0; i < _player.ItemBag[_player._indexUpStar].itemOptions.size(); i++) {
                                    if (_player.ItemBag[_player._indexUpStar].itemOptions.get(i).id == 72) {
                                        _player.ItemBag[_player._indexUpStar].itemOptions.get(i).param -= 1;
                                    }
                                    if (_player.ItemBag[_player._indexUpStar].itemOptions.get(i).id == 23) {
                                        _player.ItemBag[_player._indexUpStar].itemOptions.get(i).param = (int) Math.floor(_player.ItemBag[_player._indexUpStar].itemOptions.get(i).param * 0.9);
                                    }
                                }
                            }
                        }
                    } else if (_player.ItemBag[_player._indexDaBaoVe] != null && _player.ItemBag[_player._indexDaBaoVe].template.id == 987 && _player.ItemBag[_player._indexDaBaoVe].quantity >= 1) {
                        _player.ItemBag[_player._indexDaBaoVe].quantity -= 1;
                        if (_player.ItemBag[_player._indexDaBaoVe].quantity <= 0) {
                            _player.ItemBag[_player._indexDaBaoVe] = null;
                        }
                    }
                    updateVangNgoc(_player);
                    sendUpStarError(_player);
                }
                updateItemBag(_player);
            }
            _player._checkDapDo = true;
        }
        //RESET ALL
        resetItemDapDo(_player);
    }

    public void resetItemDapDo(Player p) {
        p._itemUpStar = null;
        p._indexUpStar = -1;
        p._itemUseEpStar = null;
        p._indexEpStar = -1;
        p._itemDaBaoVe = null;
        p._indexDaBaoVe = -1;
        p._itemUseEpStar2 = null;
        p._indexEpStar2 = -1;
    }

    //NANG CAP BONG TAI PORATA
    public void upgradePorata(Player _player, byte _index, byte _index2) {
        Item _item = _player.ItemBag[_index]; //get item 1
        Item _item2 = _player.ItemBag[_index2]; //get item 2
        String _info = "";
        if (_item == null || _item2 == null) {
            return;
        }
        if ((_item.template.id == 933 && _item2.template.id == 454) || (_item2.template.id == 933 && _item.template.id == 454)) {
            _info = "|2|Bông tai Porata [+2]\b|2|Tỉ lệ thành công: 50%\b|2|Cần 9999 Mảnh vỡ bông tai\b|2|Cần 500 Tr vàng\b|2|Cần 5000 ngọc\b|7|Thất bại -999 Mảnh vỡ bông tai";
            if (_item.template.id == 933 && _item2.template.id == 454 && _item.getParamItemByID(31) >= 9999) {
                _player._itemUpStar = _item2;
                _player._itemUseEpStar = _item;
                _player._indexUpStar = _index2;
                _player._indexEpStar = _index;
            } else if (_item2.template.id == 933 && _item.template.id == 454 && _item2.getParamItemByID(31) >= 9999) {
                _player._itemUpStar = _item;
                _player._itemUseEpStar = _item2;
                _player._indexUpStar = _index;
                _player._indexEpStar = _index2;
            } else {
                Service.gI().serverMessage(_player.session, "Thiếu nguyên liệu!");
                return;
            }
        } else {
            Service.gI().serverMessage(_player.session, "Không thể kết hợp!");
            return;
        }
        if (_info != "") {
            Message m = null;
            try {
                m = new Message(32);
                m.writer().writeShort(21);
                m.writer().writeUTF(_info);
                if (_player.vang >= 500000000 && _player.ngoc >= 5000) {
                    m.writer().writeByte(2);
                    m.writer().writeUTF("Nâng cấp\n500 Tr vàng\n5000 ngọc");
                    m.writer().writeUTF("Từ chối");
                } else {
                    m.writer().writeByte(1);
                    m.writer().writeUTF("Cần\n500 Tr vàng\n5000 ngọc");
                }

                m.writer().flush();
                _player.session.sendMessage(m);
                m.cleanup();
            } catch (Exception var2) {
                var2.printStackTrace();
            } finally {
                if (m != null) {
                    m.cleanup();
                }
            }
        } else {
            Service.gI().serverMessage(_player.session, "Không thể kết hợp!");
        }
    }

    public void sendUpPorata(Player _player) {
        Item _itemUp = _player._itemUpStar;
        Item _stone = _player._itemUseEpStar;
        Message m = null;
        if (_itemUp == null || _stone == null || _itemUp.template.id != 454 || _stone.template.id != 933) {
            return;
        }
        if ((System.currentTimeMillis() - _player._timeDapDo) >= 1000 && _player._checkDapDo) {
            _player._timeDapDo = System.currentTimeMillis();
            _player._checkDapDo = false;
            if (_itemUp != null && _stone != null && (_player.vang >= 500000000) && (_player.ngoc >= 5000) && (_player.ItemBag[_player._indexEpStar].getParamItemByID(31) >= 9999)) {
                _player.vang -= 500000000;
                _player.ngoc -= 5000;
                updateVangNgoc(_player);
                int rdUp = Util.nextInt(0, 100);
                if (rdUp <= 49) {
                    _player.ItemBag[_player._indexEpStar] = null;

                    _player.ItemBag[_player._indexUpStar] = null;
                    ItemSell itemPorata = ItemSell.getItemSell(921, (byte) 1);
                    Item _PORATA2 = new Item(itemPorata.item);
                    _PORATA2.itemOptions.clear();
                    _PORATA2.itemOptions.add(new ItemOption(72, 2));
                    _player.addItemToBag(_PORATA2);

                    sendUpStarSuccess(_player);
                } else {
                    _player.ItemBag[_player._indexEpStar].setNewParam(31, (_player.ItemBag[_player._indexEpStar].getParamItemByID(31) - 999));
                    sendUpStarError(_player);
                }
                updateItemBag(_player);
            }
            _player._checkDapDo = true;
        }
        //RESET ALL
        resetItemDapDo(_player);
    }

    //MO CHI SO PORATA 2
    public void openOptionPorata(Player _player, byte _index, byte _index2, byte _index3) {
        Item _item = _player.ItemBag[_index]; //get item 1
        Item _item2 = _player.ItemBag[_index2]; //get item 2
        Item _item3 = _player.ItemBag[_index3];
        if (_item == null || _item2 == null || _item3 == null) {
            return;
        }
        String _info = "|2|Bông tai Porata [+2]\b|2|Tỉ lệ thành công: 50%\b|2|Cần 99 Mảnh hồn bông tai\b|2|Cần 1 Đá xanh lam\b|1|+1 Chỉ số ngẫu nhiên";
        if ((_item.template.id == 921 && _item2.template.id == 934 && _item3.template.id == 935) || (_item.template.id == 921 && _item2.template.id == 935 && _item3.template.id == 934)) {
            _player._itemUpStar = _item;
            _player._itemUseEpStar = _item2;
            _player._itemUseEpStar2 = _item3;
            _player._indexUpStar = _index;
            _player._indexEpStar = _index2;
            _player._indexEpStar2 = _index3;
        } else if ((_item.template.id == 934 && _item2.template.id == 921 && _item3.template.id == 935) || (_item.template.id == 935 && _item2.template.id == 921 && _item3.template.id == 934)) {
            _player._itemUpStar = _item2;
            _player._itemUseEpStar = _item;
            _player._itemUseEpStar2 = _item3;
            _player._indexUpStar = _index2;
            _player._indexEpStar = _index;
            _player._indexEpStar2 = _index3;
        } else if ((_item.template.id == 934 && _item2.template.id == 935 && _item3.template.id == 921) || (_item.template.id == 935 && _item2.template.id == 934 && _item3.template.id == 921)) {
            _player._itemUpStar = _item3;
            _player._itemUseEpStar = _item;
            _player._itemUseEpStar2 = _item2;
            _player._indexUpStar = _index3;
            _player._indexEpStar = _index;
            _player._indexEpStar2 = _index2;
        } else {
            Service.gI().serverMessage(_player.session, "Không thể kết hợp!");
            return;
        }
        if (_info != "") {
            Message m = null;
            try {
                m = new Message(32);
                m.writer().writeShort(21);
                m.writer().writeUTF(_info);
                if (_player.ngoc >= 2500) {
                    m.writer().writeByte(2);
                    m.writer().writeUTF("Nâng cấp\n2500 ngọc");
                    m.writer().writeUTF("Từ chối");
                } else {
                    m.writer().writeByte(1);
                    m.writer().writeUTF("Cần\n2500 ngọc");
                }

                m.writer().flush();
                _player.session.sendMessage(m);
                m.cleanup();
            } catch (Exception var2) {
                var2.printStackTrace();
            } finally {
                if (m != null) {
                    m.cleanup();
                }
            }
        } else {
            Service.gI().serverMessage(_player.session, "Không thể kết hợp!");
        }
    }

    public void sendOpenOptionPorata(Player _player) {
        Item _itemUp = _player._itemUpStar;
        Item _manhHon = _player._itemUseEpStar;
        Item _daXLam = _player._itemUseEpStar2;

        if (_itemUp == null || _manhHon == null || _daXLam == null || _itemUp.template.id != 921 || _manhHon.template.id != 934 || _daXLam.template.id != 935) {
            return;
        }
        if ((System.currentTimeMillis() - _player._timeDapDo) >= 1000 && _player._checkDapDo) {
            _player._timeDapDo = System.currentTimeMillis();
            _player._checkDapDo = false;
            if (_itemUp != null && _manhHon != null && _daXLam != null && (_player.ngoc >= 2500) && (_player.ItemBag[_player._indexEpStar].quantity >= 99) && (_player.ItemBag[_player._indexEpStar2].quantity >= 1)) {
                _player.ngoc -= 2500;
                _player.ItemBag[_player._indexEpStar] = null;
                _player.ItemBag[_player._indexEpStar2].quantity -= 1;
                if (_player.ItemBag[_player._indexEpStar2].quantity <= 0) {
                    _player.ItemBag[_player._indexEpStar2] = null;
                }
                updateVangNgoc(_player);
                int rdUp = Util.nextInt(0, 100);
                if (rdUp <= 49) {
                    rdUp = Util.nextInt(0, 9);
                    if (_player.ItemBag[_player._indexUpStar].template.id == 921) {
                        _player.ItemBag[_player._indexUpStar].itemOptions.clear();
                        _player.ItemBag[_player._indexUpStar].itemOptions.add(new ItemOption(72, 2));
                        if (rdUp == 0) {
                            _player.ItemBag[_player._indexUpStar].itemOptions.add(new ItemOption(50, Util.nextInt(5, 16)));
                        } else if (rdUp == 1) {
                            _player.ItemBag[_player._indexUpStar].itemOptions.add(new ItemOption(77, Util.nextInt(5, 16)));
                        } else if (rdUp == 2) {
                            _player.ItemBag[_player._indexUpStar].itemOptions.add(new ItemOption(103, Util.nextInt(5, 16)));
                        } else if (rdUp == 3) {
                            _player.ItemBag[_player._indexUpStar].itemOptions.add(new ItemOption(108, Util.nextInt(5, 16)));
                        } else if (rdUp == 4) {
                            _player.ItemBag[_player._indexUpStar].itemOptions.add(new ItemOption(94, Util.nextInt(5, 11)));
                        } else if (rdUp == 5) {
                            _player.ItemBag[_player._indexUpStar].itemOptions.add(new ItemOption(14, Util.nextInt(2, 11)));
                        } else if (rdUp == 6) {
                            _player.ItemBag[_player._indexUpStar].itemOptions.add(new ItemOption(80, Util.nextInt(5, 16)));
                        } else if (rdUp == 7) {
                            _player.ItemBag[_player._indexUpStar].itemOptions.add(new ItemOption(81, Util.nextInt(5, 16)));
                        } else if (rdUp == 8) {
                            _player.ItemBag[_player._indexUpStar].itemOptions.add(new ItemOption(175, Util.nextInt(5, 16)));
                        }
                    }
                    sendUpStarSuccess(_player);
                } else {
                    sendUpStarError(_player);
                }
                updateItemBag(_player);
            }
            _player._checkDapDo = true;
        }
        //RESET ALL
        resetItemDapDo(_player);
    }

    //NANG CAP ITEM HEART
    public void upgradeItemHeart(Player _player, byte _index, byte _index2) {
        Item _item = _player.ItemBag[_index]; //get item 1
        Item _item2 = _player.ItemBag[_index2]; //get item 2
        String _info = "";
        if (_item == null || _item2 == null) {
            return;
        }
        if ((_item.template.id >= 650 && _item.template.id <= 662 && _item2.template.id >= 1069 && _item2.template.id <= 1073)
                || (_item2.template.id >= 650 && _item2.template.id <= 662 && _item.template.id >= 1069 && _item.template.id <= 1073)) {
            if (_player.ItemBag[_index].template.type == (byte) 27) {
                _item = _player.ItemBag[_index2];
                _item2 = _player.ItemBag[_index];
                byte temp = _index2;
                _index2 = _index;
                _index = temp;
            }
            if (_item.getParamItemByID(102) > 0 || _item.getParamItemByID(107) > 0 || _item.getParamItemByID(72) > 0) {
                Service.gI().serverMessage(_player.session, "Chỉ có thể nâng cấp trang bị hủy diệt chưa nâng cấp, chưa pha lê hóa");
                return;
            }
            if ((_item.template.type == (byte) 0 && _item2.template.id == 1069) || (_item.template.type == (byte) 1 && _item2.template.id == 1070)
                    || (_item.template.type == (byte) 2 && _item2.template.id == 1071) || (_item.template.type == (byte) 3 && _item2.template.id == 1072)
                    || (_item.template.type == (byte) 4 && _item2.template.id == 1073)) {
                _info = "|2|" + _item.template.name + "\b|2|Tỉ lệ thành công: 10%\b|2|Cần 99 Mảnh trang bị Thần\b|2|Cần 500 Tr vàng\b|2|Cần 5000 ngọc\b|7|Thất bại -10 Mảnh trang bị Thần";

                if (_item2.quantity >= 99) {
                    _player._itemUpStar = _item;
                    _player._indexUpStar = _index;
                    _player._itemUseEpStar = _item2;
                    _player._indexEpStar = _index2;
                } else {
                    Service.gI().serverMessage(_player.session, "Thiếu nguyên liệu!");
                    return;
                }
            } else {
                Service.gI().serverMessage(_player.session, "Không thể kết hợp!");
                return;
            }
        } else {
            Service.gI().serverMessage(_player.session, "Không thể kết hợp!");
            return;
        }
        if (_info != "") {
            Message m = null;
            try {
                m = new Message(32);
                m.writer().writeShort(21);
                m.writer().writeUTF(_info);
                if (_player.vang >= 500000000 && _player.ngoc >= 5000) {
                    m.writer().writeByte(2);
                    m.writer().writeUTF("Nâng cấp\n500 Tr vàng\n5000 ngọc");
                    m.writer().writeUTF("Từ chối");
                } else {
                    m.writer().writeByte(1);
                    m.writer().writeUTF("Cần\n500 Tr vàng\n5000 ngọc");
                }

                m.writer().flush();
                _player.session.sendMessage(m);
                m.cleanup();
            } catch (Exception var2) {
                var2.printStackTrace();
            } finally {
                if (m != null) {
                    m.cleanup();
                }
            }
        } else {
            Service.gI().serverMessage(_player.session, "Không thể kết hợp!");
        }
    }

    public void sendUpItemHeart(Player _player) {
        Item _itemUp = _player._itemUpStar;
        Item _stone = _player._itemUseEpStar;
        Message m = null;
        if (_itemUp == null || _stone == null || _itemUp.template.id < 650 || _itemUp.template.id > 662 || _stone.template.id < 1069 || _stone.template.id > 1073) {
            return;
        }
        if ((System.currentTimeMillis() - _player._timeDapDo) >= 1000 && _player._checkDapDo) {
            _player._timeDapDo = System.currentTimeMillis();
            _player._checkDapDo = false;
            if ((_player.vang >= 500000000) && (_player.ngoc >= 5000) && (_player.ItemBag[_player._indexEpStar].quantity >= 99)) {
                _player.vang -= 500000000;
                _player.ngoc -= 5000;
                updateVangNgoc(_player);
                int rdUp = Util.nextInt(0, 100);
                if (rdUp <= 99) {
                    _player.ItemBag[_player._indexEpStar] = null;

                    _stone = ItemSell.getItemNotSell(_itemUp.template.id + 406);
                    Item itemHeart = new Item(_stone);
                    itemHeart.itemOptions.clear();

                    for (int i = 0; i < _itemUp.itemOptions.size(); i++) {
                        if (_itemUp.itemOptions.get(i).id == 21 || _itemUp.itemOptions.get(i).id == 30) {
                            itemHeart.itemOptions.add(new ItemOption(_itemUp.itemOptions.get(i).id, _itemUp.itemOptions.get(i).param));
                        } else {
                            itemHeart.itemOptions.add(new ItemOption(_itemUp.itemOptions.get(i).id, (int) (_itemUp.itemOptions.get(i).param * 1.2)));
                        }
                    }
                    _player.ItemBag[_player._indexUpStar] = null;
                    _player.addItemToBag(itemHeart);

                    sendUpStarSuccess(_player);
                } else {
                    _player.ItemBag[_player._indexEpStar].quantity -= 10;
                    if (_player.ItemBag[_player._indexEpStar].quantity <= 0) {
                        _player.ItemBag[_player._indexEpStar] = null;
                    }
                    sendUpStarError(_player);
                }
                updateItemBag(_player);
            }
            _player._checkDapDo = true;
        }
        //RESET ALL
        resetItemDapDo(_player);
    }

    //EP MANH DA VUN
    public void EpStoneBreak(Player _player, byte _index1, byte _index2) {
        Item _item = _player.ItemBag[_index1]; //get item 1
        Item _item2 = _player.ItemBag[_index2]; //get item 2
        if (_item == null || _item2 == null) {
            return;
        }
        if (_item.template.id == 225 && _item2.template.id == 226) { //225 da vun, 226/nuoc phep
            if (_item.quantity >= 10 && _item2.quantity >= 1) {
                Message m = null;
                try {
                    m = new Message(32);
                    m.writer().writeShort(21);
                    m.writer().writeUTF("Ta sẽ phù phép để tạo thành một viên đá nâng cấp ngẫu nhiên");
                    m.writer().writeByte(2);
                    m.writer().writeUTF("Nâng cấp");
                    m.writer().writeUTF("Từ chối");
                    m.writer().flush();
                    _player.session.sendMessage(m);
                    m.cleanup();
                } catch (Exception var2) {
                    var2.printStackTrace();
                } finally {
                    if (m != null) {
                        m.cleanup();
                    }
                }
                _player._itemUpStar = _item;
                _player._itemUseEpStar = _item2;
                _player._indexUpStar = _index1;
                _player._indexEpStar = _index2;
            } else {
                Service.gI().serverMessage(_player.session, "Cần ít nhất 10 mảnh đá vụn và 1 bình nước phép!");
            }
        } else if (_item.template.id == 226 && _item2.template.id == 225) {
            if (_item2.quantity >= 10 && _item.quantity >= 1) {
                Message m = null;
                try {
                    m = new Message(32);
                    m.writer().writeShort(21);
                    m.writer().writeUTF("Ta sẽ phù phép để tạo thành một viên đá nâng cấp ngẫu nhiên");
                    m.writer().writeByte(2);
                    m.writer().writeUTF("Nâng cấp");
                    m.writer().writeUTF("Từ chối");
                    m.writer().flush();
                    _player.session.sendMessage(m);
                    m.cleanup();
                } catch (Exception var2) {
                    var2.printStackTrace();
                } finally {
                    if (m != null) {
                        m.cleanup();
                    }
                }
                _player._itemUpStar = _item2;
                _player._itemUseEpStar = _item;
                _player._indexUpStar = _index2;
                _player._indexEpStar = _index1;
            } else {
                Service.gI().serverMessage(_player.session, "Cần ít nhất 10 mảnh đá vụn và 1 bình nước phép!");
            }
        } else {
            Service.gI().serverMessage(_player.session, "Không thể kết hợp!");
        }
    }

    public void sendEpDaVun(Player _player) {
        Item _daVun = _player._itemUpStar;
        Item _nuocPhep = _player._itemUseEpStar;
        if (_daVun == null || _nuocPhep == null) {
            return;
        }
        if ((System.currentTimeMillis() - _player._timeDapDo) >= 1000 && _player._checkDapDo) {
            _player._timeDapDo = System.currentTimeMillis();
            _player._checkDapDo = false;
            if (_daVun != null && _nuocPhep != null) {
                _player.ItemBag[_player._indexUpStar].quantity -= 10;
                _player.ItemBag[_player._indexEpStar].quantity -= 1;
                if (_player.ItemBag[_player._indexUpStar].quantity == 0) {
                    _player.ItemBag[_player._indexUpStar] = null;
                }
                if (_player.ItemBag[_player._indexEpStar].quantity == 0) {
                    _player.ItemBag[_player._indexEpStar] = null;
                }
                int rdDaVun = Util.nextInt(0, 5);
                Item daNangCap = null;
                if (rdDaVun == 0) {
                    daNangCap = ItemSell.getItemNotSell(220);
                } else if (rdDaVun == 1) {
                    daNangCap = ItemSell.getItemNotSell(221);
                } else if (rdDaVun == 2) {
                    daNangCap = ItemSell.getItemNotSell(222);
                } else if (rdDaVun == 3) {
                    daNangCap = ItemSell.getItemNotSell(223);
                } else if (rdDaVun == 4) {
                    daNangCap = ItemSell.getItemNotSell(224);
                }

                Message m = null;
                try {
                    m = new Message(-81);
                    m.writer().writeByte(4);
                    if (rdDaVun == 0) {
                        m.writer().writeShort(1420);
                    } else if (rdDaVun == 1) {
                        m.writer().writeShort(1419);
                    } else if (rdDaVun == 2) {
                        m.writer().writeShort(1416);
                    } else if (rdDaVun == 3) {
                        m.writer().writeShort(1417);
                    } else if (rdDaVun == 4) {
                        m.writer().writeShort(1418);
                    }
                    m.writer().flush();
                    _player.session.sendMessage(m);
                    m.cleanup();
                } catch (Exception var2) {
                    var2.printStackTrace();
                } finally {
                    if (m != null) {
                        m.cleanup();
                    }
                }
                _player.addItemToBag(daNangCap);
                Service.gI().updateItemBag(_player);
            }
            _player._checkDapDo = true;
        }
        //RESET ALL
        resetItemDapDo(_player);
    }

    public void EpDragonBall(Player _player, byte _index) {
        Item _item = _player.ItemBag[_index]; //ngoc rong
        if (_item == null) {
            return;
        }
        if (_item.template.id >= 17 && _item.template.id <= 20) { //4s den 7s
//        if(_item.template.id >= 15 && _item.template.id <=20) { //2s den 7s
            if (_item.quantity >= 7) {
                Message m = null;
                try {
                    m = new Message(32);
                    m.writer().writeShort(21);
                    m.writer().writeUTF("Ta sẽ phù phép để biến 7 viên ngọc rồng cùng cấp thành 1 viên có phẩm chất cao hơn");
                    m.writer().writeByte(2);
                    m.writer().writeUTF("Nâng cấp");
                    m.writer().writeUTF("Từ chối");
                    m.writer().flush();
                    _player.session.sendMessage(m);
                    m.cleanup();
                } catch (Exception var2) {
                    var2.printStackTrace();
                } finally {
                    if (m != null) {
                        m.cleanup();
                    }
                }
                _player._itemUpStar = _item;
                _player._indexUpStar = _index;
            } else {
                Service.gI().serverMessage(_player.session, "Cần ít nhất 7 viên ngọc rồng cùng cấp!");
            }
        } else {
            Service.gI().serverMessage(_player.session, "Chỉ được phép ép ngọc rồng từ 4 đến 7 sao!");
        }
    }

    public void sendEpNgocRong(Player _player) {
        Item _ngocRong = _player._itemUpStar;
        if (_ngocRong == null) {
            return;
        }
        if ((System.currentTimeMillis() - _player._timeDapDo) >= 1000 && _player._checkDapDo && _ngocRong.template.id >= 17 && _ngocRong.template.id <= 20 && _ngocRong.quantity >= 7) {
            _player._timeDapDo = System.currentTimeMillis();
            _player._checkDapDo = false;
            if (_ngocRong != null) {
                _player.ItemBag[_player._indexUpStar].quantity -= 7;
                if (_player.ItemBag[_player._indexUpStar].quantity == 0) {
                    _player.ItemBag[_player._indexUpStar] = null;
                }
                Item nrNangCap = ItemSell.getItemNotSell((_ngocRong.template.id - 1));

                Message m = null;
                try {
                    m = new Message(-81);
                    m.writer().writeByte(5);
                    m.writer().writeShort((_ngocRong.template.id + 404));
                    m.writer().flush();
                    _player.session.sendMessage(m);
                    m.cleanup();
                } catch (Exception var2) {
                    var2.printStackTrace();
                } finally {
                    if (m != null) {
                        m.cleanup();
                    }
                }
                _player.addItemToBag(nrNangCap);
                Service.gI().updateItemBag(_player);
            }

            _player._checkDapDo = true;
        }
        //RESET ALL
        resetItemDapDo(_player);
    }

    //THONG BAO DUOI MAN HINH
    public void sendThongBaoServer(String text) {
        Message m = null;
        try {
            m = new Message(93);
            m.writer().writeUTF(text);
            m.writer().flush();
            for (Player p : PlayerManger.gI().getPlayers()) {
                if (p != null) {
                    p.session.sendMessage(m);
                }
            }
            m.cleanup();
        } catch (Exception var2) {
            var2.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void sendCoolDownSkill(Player p, short idskill, int time) {
        Message m = null;
        try {
            m = new Message(-94);
//            m.writer().writeInt(5);
            m.writer().writeShort(idskill); //ID SKILL
            m.writer().writeInt(time); //Time con lai// miligiay
            m.writer().flush(); //
            p.session.sendMessage(m);
            m.cleanup();
        } catch (Exception var2) {
            var2.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void coolDownAllSkill(Player p) {
        Message m = null;
        int time = 0;
        try {
            m = new Message(-94);
            for (Skill skill : p.skill) {
                time = (int) (skill.coolDown - (System.currentTimeMillis() - skill.lastTimeUseThisSkill)) < 0 ? 0 : (int) (skill.coolDown - (System.currentTimeMillis() - skill.lastTimeUseThisSkill));
                m.writer().writeShort(skill.skillId); //ID SKILL
                m.writer().writeInt(time); //Time con lai// miligiay
            }
            m.writer().flush(); //
            p.session.sendMessage(m);
            m.cleanup();
        } catch (Exception var2) {
            var2.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    //CHECK CAN ATTACK PLAYER
    public boolean checkCanAttackChar(Player player, Player _pTarget) {
        if (((_pTarget.cPk != (byte) 0 && _pTarget.cPk != player.cPk && player.cPk != (byte) 0) || (_pTarget.cPk == (byte) 8 && player.cPk != (byte) 0) || (_pTarget.cPk != (byte) 0 && player.cPk == (byte) 8)
                || (player.typePk == _pTarget.typePk && player.typePk != (byte) 0 && _pTarget.typePk != (byte) 0) || (_pTarget.typePk == (byte) 5)
                || (player.typePk == (byte) 5)) && _pTarget.id != player.id) {
            return true;
        }
        return false;
    }

    //CHECK CAN ATTACK BOSS
    public boolean checkCanAttackBoss(Boss boss) {
        if (boss.typePk == (byte) 5) {
            return true;
        }
        return false;
    }

    //CHECK CAN ATTACK DE TU
    public boolean checkCanAttackDeTu(Player player, Detu _pTarget) {
        if (((_pTarget.cPk != (byte) 0 && _pTarget.cPk != player.cPk && player.cPk != (byte) 0) || (_pTarget.cPk == (byte) 8 && player.cPk != (byte) 0) || (_pTarget.cPk != (byte) 0 && player.cPk == (byte) 8)) && _pTarget.id != player.id) {
            return true;
        }
        return false;
    }

    // BOSSSSSSSSSSSSSSSSSSSSS SERVICE
    public void initKuKu() {
        int idMap = 68;
        short xKu = (short) 758;
        short yKu = (short) 408;
        idMap = Util.nextInt(68, 73);
        int IDZONE = Util.nextInt(0, Server.gI().maps[idMap].area.length);
        if (idMap == 69) {
            xKu = (short) 808;
            yKu = (short) 384;
        } else if (idMap == 70) {
            xKu = (short) 301;
            yKu = (short) 360;
        } else if (idMap == 71) {
            xKu = (short) 282;
            yKu = (short) 168;
        } else if (idMap == 72) {
            xKu = (short) 1017;
            yKu = (short) 312;
        }
        Boss _rKuku = new Boss(110, (byte) 7, xKu, yKu);
        Server.gI().maps[idMap].area[IDZONE].bossMap.add(_rKuku);
        Server.gI().maps[idMap].area[IDZONE].loadBossNoPet(_rKuku);
        Server.gI().mapKUKU = idMap;
        Server.gI().khuKUKU = IDZONE;
        sendThongBaoServer("BOSS " + _rKuku.name + " vừa xuất hiện tại " + Server.gI().maps[idMap].template.name);
//        Util.log("INIT KUKU XONG KHU MAP" + Server.gI().maps[idMap].template.name + ", " + IDZONE);
    }

    public void initMAPDAUDINH() {
        int idMap = 64;
        short xKu = (short) 794;
        short yKu = (short) 312;
        idMap = Util.nextInt(63, 68);
        int IDZONE = Util.nextInt(0, Server.gI().maps[idMap].area.length);
        if (idMap == 65) {
            xKu = (short) 1246;
            yKu = (short) 240;
        } else if (idMap == 63) {
            xKu = (short) 695;
            yKu = (short) 144;
        } else if (idMap == 66) {
            xKu = (short) 993;
            yKu = (short) 360;
        } else if (idMap == 67) {
            xKu = (short) 972;
            yKu = (short) 720;
        }
        Boss _rMapDinh = new Boss(115, (byte) 8, xKu, yKu);
        Server.gI().maps[idMap].area[IDZONE].bossMap.add(_rMapDinh);
        Server.gI().maps[idMap].area[IDZONE].loadBossNoPet(_rMapDinh);
        Server.gI().mapMDD = idMap;
        Server.gI().khuMDD = IDZONE;
        sendThongBaoServer("BOSS " + _rMapDinh.name + " vừa xuất hiện tại " + Server.gI().maps[idMap].template.name);
//        Util.log("INIT _rMapDinh XONG MAP " + Server.gI().maps[idMap].template.name + ", " + IDZONE);
    }

    public void initRAMBO() {
        int idMap = 73;
        short xKu = (short) 324;
        short yKu = (short) 168;
        idMap = Util.nextInt(73, 77);
        int IDZONE = Util.nextInt(0, Server.gI().maps[idMap].area.length);
        if (idMap == 74) {
            xKu = (short) 532;
            yKu = (short) 336;
        } else if (idMap == 75) {
            xKu = (short) 515;
            yKu = (short) 216;
        } else if (idMap == 76) {
            xKu = (short) 845;
            yKu = (short) 240;
        } else if (idMap == 77) {
            xKu = (short) 701;
            yKu = (short) 288;
        }
        Boss _rRambo = new Boss(120, (byte) 9, xKu, yKu);
        Server.gI().maps[idMap].area[IDZONE].bossMap.add(_rRambo);
        Server.gI().maps[idMap].area[IDZONE].loadBossNoPet(_rRambo);
        Server.gI().mapRAMBO = idMap;
        Server.gI().khuRAMBO = IDZONE;
        sendThongBaoServer("BOSS " + _rRambo.name + " vừa xuất hiện tại " + Server.gI().maps[idMap].template.name);
//        Util.log("INIT _rRambo XONG MAP " + Server.gI().maps[idMap].template.name + ", " + IDZONE);
    }

    public void supportTDST() {
        Timer supportTDST = new Timer();
        TimerTask TDST = new TimerTask() {
            public void run() {
                Server.gI().supportNV = true;
                Timer endSupportTDST = new Timer();
                TimerTask endTDST = new TimerTask() {
                    public void run() {
                        Server.gI().supportNV = false;
                    }
                ;
                };
                endSupportTDST.schedule(endTDST, 7200000);
            }
        ;
        };
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 17); //SERVER 1
//        calendar.set(Calendar.HOUR_OF_DAY, 5); //SERVER 2
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

        supportTDST.schedule(TDST, dateSchedule, period);
    }

    public void initTDST() {
        int idMap = Util.nextInt(0, 3);
        short xSo4 = (short) 1392;
        short ySo4 = (short) 240;
        short xSo3 = (short) 1361;
        short xSo2 = (short) 1330;
        short xSo1 = (short) 1300;
        short xSo0 = (short) 1270;
        if (idMap == 0) {
            idMap = 81;
        } else if (idMap == 1) {
            idMap = 82;
            xSo4 = (short) 1446;
            xSo3 = (short) 1416;
            xSo2 = (short) 1386;
            xSo1 = (short) 1356;
            xSo0 = (short) 1326;
            ySo4 = (short) 336;
        } else {
            idMap = 78;
            xSo4 = (short) 150;
            xSo3 = (short) 180;
            xSo2 = (short) 210;
            xSo1 = (short) 240;
            xSo0 = (short) 270;
            ySo4 = (short) 360;
        }
        int IDZONE = Util.nextInt(0, Server.gI().maps[idMap].area.length);
        Boss _So4 = new Boss(125, (byte) 10, xSo4, ySo4);
//        Boss _So3 = new Boss(126, (byte)11, xSo3, ySo4);
//        Boss _So2 = new Boss(127, (byte)12, xSo2, ySo4);
//        Boss _So1 = new Boss(128, (byte)13, xSo1, ySo4);
//        Boss _So0 = new Boss(129, (byte)14, xSo0, ySo4);
        Boss _So3 = new Boss(150, (byte) 11, xSo3, ySo4);
        Boss _So2 = new Boss(151, (byte) 12, xSo2, ySo4);
        Boss _So1 = new Boss(152, (byte) 13, xSo1, ySo4);
        Boss _So0 = new Boss(153, (byte) 14, xSo0, ySo4);

        Server.gI().maps[idMap].area[IDZONE].bossMap.add(_So4);
        Server.gI().maps[idMap].area[IDZONE].loadBossNoPet(_So4);
        sendThongBaoServer("BOSS " + _So4.name + " vừa xuất hiện tại " + Server.gI().maps[idMap].template.name);

        Server.gI().maps[idMap].area[IDZONE].loadInfoBoss(_So3);
        Server.gI().maps[idMap].area[IDZONE].bossMap.add(_So3);
        sendThongBaoServer("BOSS " + _So3.name + " vừa xuất hiện tại " + Server.gI().maps[idMap].template.name);
        Server.gI().maps[idMap].area[IDZONE].loadInfoBoss(_So2);
        Server.gI().maps[idMap].area[IDZONE].bossMap.add(_So2);
        sendThongBaoServer("BOSS " + _So2.name + " vừa xuất hiện tại " + Server.gI().maps[idMap].template.name);
        Server.gI().maps[idMap].area[IDZONE].loadInfoBoss(_So1);
        Server.gI().maps[idMap].area[IDZONE].bossMap.add(_So1);
        sendThongBaoServer("BOSS " + _So1.name + " vừa xuất hiện tại " + Server.gI().maps[idMap].template.name);
        Server.gI().maps[idMap].area[IDZONE].loadInfoBoss(_So0);
        Server.gI().maps[idMap].area[IDZONE].bossMap.add(_So0);
        sendThongBaoServer("BOSS " + _So0.name + " vừa xuất hiện tại " + Server.gI().maps[idMap].template.name);
        Server.gI().mapTDST = idMap;
        Server.gI().khuTDST = IDZONE;
        Util.log("INIT TDST XONG MAP " + Server.gI().maps[idMap].template.name + ", " + IDZONE);
    }

    public void initAndroid1920() {
        int idMap = Util.nextInt(0, 3);
        short xA19 = (short) 245;
        short yA19 = (short) 360;
        short xA20 = (short) 301;
        if (idMap == 0) {
            idMap = 92;
        } else if (idMap == 1) {
            idMap = 93;
            xA19 = (short) 221;
            xA20 = (short) 298;
        } else {
            idMap = 94;
            xA19 = (short) 503;
            yA19 = (short) 312;
            xA20 = (short) 570;
        }
        int IDZONE = Util.nextInt(0, Server.gI().maps[idMap].area.length);
        Boss _Android19 = new Boss(135, (byte) 18, xA19, yA19);
        Boss _Android20 = new Boss(136, (byte) 19, xA20, yA19);

        Server.gI().maps[idMap].area[IDZONE].bossMap.add(_Android19);
        Server.gI().maps[idMap].area[IDZONE].loadBossNoPet(_Android19);
        sendThongBaoServer("BOSS " + _Android19.name + " vừa xuất hiện tại " + Server.gI().maps[idMap].template.name);

        Server.gI().maps[idMap].area[IDZONE].bossMap.add(_Android20);
        Server.gI().maps[idMap].area[IDZONE].loadBossNoPet(_Android20);
        sendThongBaoServer("BOSS " + _Android20.name + " vừa xuất hiện tại " + Server.gI().maps[idMap].template.name);
        Util.log("INIT ANDROID 19,20 XONG MAP " + Server.gI().maps[idMap].template.name + ", " + IDZONE);
    }

    public void initAndroid15() {
        int IDZONE = Util.nextInt(0, Server.gI().maps[101].area.length);
        Boss _Android15 = new Boss(137, (byte) 20, (short) 186, (short) 336);
        Boss _Android14 = new Boss(138, (byte) 21, (short) 235, (short) 336);
        Boss _Android13 = new Boss(139, (byte) 22, (short) 277, (short) 336);

        Server.gI().maps[101].area[IDZONE].bossMap.add(_Android15);
        Server.gI().maps[101].area[IDZONE].loadBossNoPet(_Android15);
        sendThongBaoServer("BOSS " + _Android15.name + " vừa xuất hiện tại " + Server.gI().maps[101].template.name);

        Server.gI().maps[101].area[IDZONE].bossMap.add(_Android14);
        Server.gI().maps[101].area[IDZONE].loadBossNoPet(_Android14);
        sendThongBaoServer("BOSS " + _Android14.name + " vừa xuất hiện tại " + Server.gI().maps[101].template.name);

        Server.gI().maps[101].area[IDZONE].bossMap.add(_Android13);
        Server.gI().maps[101].area[IDZONE].loadBossNoPet(_Android13);
        sendThongBaoServer("BOSS " + _Android13.name + " vừa xuất hiện tại " + Server.gI().maps[101].template.name);
        Util.log("INIT ANDROID 15,14,13 XONG MAP " + Server.gI().maps[101].template.name + ", " + IDZONE);
    }

    public void initBossNgoc() {
        int IDZONE = Util.nextInt(0, Server.gI().maps[5].area.length);
        int IDZONE1 = Util.nextInt(0, Server.gI().maps[0].area.length);
        int IDZONE2 = Util.nextInt(0, Server.gI().maps[7].area.length);
        Boss _rati = new Boss(490, (byte) 53, (short) 763, (short) 408);
        Boss _antrom = new Boss(201, (byte) 55, (short) 1195, (short) 408);
        Boss _rati1 = new Boss(490, (byte) 53, (short) 960, (short) 360);
        Boss _antrom1 = new Boss(201, (byte) 55, (short) 1195, (short) 408);
        Boss _rati2 = new Boss(490, (byte) 53, (short) 386, (short) 360);
        Boss _antrom2 = new Boss(201, (byte) 55, (short) 964, (short) 336);

        Server.gI().maps[5].area[IDZONE].bossMap.add(_rati);
        Server.gI().maps[5].area[IDZONE].loadBossNoPet(_rati);
        sendThongBaoServer("BOSS " + _rati.name + " vừa xuất hiện tại " + Server.gI().maps[5].template.name);

        Server.gI().maps[5].area[IDZONE].bossMap.add(_antrom);
        Server.gI().maps[5].area[IDZONE].loadBossNoPet(_antrom);
        sendThongBaoServer("BOSS " + _antrom.name + " vừa xuất hiện tại " + Server.gI().maps[5].template.name);

        Server.gI().maps[0].area[IDZONE1].bossMap.add(_rati1);
        Server.gI().maps[0].area[IDZONE1].loadBossNoPet(_rati1);
        sendThongBaoServer("BOSS Ngọc " + _rati1.name + " vừa xuất hiện tại " + Server.gI().maps[0].template.name);

        Server.gI().maps[0].area[IDZONE1].bossMap.add(_antrom1);
        Server.gI().maps[0].area[IDZONE1].loadBossNoPet(_antrom1);
        sendThongBaoServer("BOSS Ngọc " + _antrom1.name + " vừa xuất hiện tại " + Server.gI().maps[0].template.name);

        Server.gI().maps[7].area[IDZONE2].bossMap.add(_rati2);
        Server.gI().maps[7].area[IDZONE2].loadBossNoPet(_rati2);
        sendThongBaoServer("BOSS Ngọc " + _rati2.name + " vừa xuất hiện tại " + Server.gI().maps[7].template.name);

        Server.gI().maps[7].area[IDZONE2].bossMap.add(_antrom2);
        Server.gI().maps[7].area[IDZONE2].loadBossNoPet(_antrom2);
        sendThongBaoServer("BOSS Ngọc " + _antrom2.name + " vừa xuất hiện tại " + Server.gI().maps[7].template.name);
        Util.log("INIT Boss Ngoc XONG MAP " + Server.gI().maps[5].template.name + ", " + IDZONE);
    }

    public void initPicPoc() {
        int idMap = Util.nextInt(95, 98);
        short xPoc = (short) 151;
        short xPic = (short) 186;
        short xKK = (short) 228;
        short yPoc = (short) 384;
        if (idMap == 97) {
            xPoc = (short) 179;
            xPic = (short) 224;
            xKK = (short) 263;
            yPoc = (short) 192;
        }
        int IDZONE = Util.nextInt(0, Server.gI().maps[idMap].area.length);
        Boss _POC = new Boss(140, (byte) 23, xPoc, yPoc);
//        Boss _PIC = new Boss(141, (byte)24, xPic, yPoc);
//        Boss _KK = new Boss(142, (byte)25, xKK, yPoc);
        Boss _PIC = new Boss(154, (byte) 24, xPic, yPoc);
        Boss _KK = new Boss(155, (byte) 25, xKK, yPoc);

        Server.gI().maps[idMap].area[IDZONE].bossMap.add(_POC);
        Server.gI().maps[idMap].area[IDZONE].loadBossNoPet(_POC);
        sendThongBaoServer("BOSS " + _POC.name + " vừa xuất hiện tại " + Server.gI().maps[idMap].template.name);

        Server.gI().maps[idMap].area[IDZONE].bossMap.add(_PIC);
        Server.gI().maps[idMap].area[IDZONE].loadInfoBoss(_PIC);
        sendThongBaoServer("BOSS " + _PIC.name + " vừa xuất hiện tại " + Server.gI().maps[idMap].template.name);

        Server.gI().maps[idMap].area[IDZONE].bossMap.add(_KK);
        Server.gI().maps[idMap].area[IDZONE].loadInfoBoss(_KK);
        sendThongBaoServer("BOSS " + _KK.name + " vừa xuất hiện tại " + Server.gI().maps[idMap].template.name);
        Util.log("INIT PICPOCKINGKONG XONG MAP " + Server.gI().maps[idMap].template.name + ", " + IDZONE);
    }

    public void initXenGinder() {
        int IDZONE = Util.nextInt(0, Server.gI().maps[98].area.length);
        Boss _XenGinder = new Boss(143, (byte) 26, (short) 277, (short) 384);

        Server.gI().maps[98].area[IDZONE].bossMap.add(_XenGinder);
        Server.gI().maps[98].area[IDZONE].loadBossNoPet(_XenGinder);
        sendThongBaoServer("BOSS " + _XenGinder.name + " vừa xuất hiện tại " + Server.gI().maps[98].template.name);
        Util.log("INIT XEN GINDER XONG MAP " + Server.gI().maps[98].template.name + ", " + IDZONE);
    }

    public void initXenVoDai() {
        int IDZONE = Util.nextInt(0, Server.gI().maps[100].area.length);
        Boss _XenVoDai = new Boss(146, (byte) 28, (short) 361, (short) 288);
        _XenVoDai.callXenCon = System.currentTimeMillis();
        Server.gI().maps[100].area[IDZONE].bossMap.add(_XenVoDai);
        Server.gI().maps[100].area[IDZONE].loadBossNoPet(_XenVoDai);
        sendThongBaoServer("BOSS " + _XenVoDai.name + " vừa xuất hiện tại " + Server.gI().maps[100].template.name);

        Util.log("INIT XEN Vo DAI XONG MAP " + Server.gI().maps[100].template.name + ", " + IDZONE);
    }

    public void initChilled() {
        int IDZONE = Util.nextInt(0, Server.gI().maps[159].area.length);
        Boss _Chilled = new Boss(500, (byte) 40, (short) 982, (short) 336);

        Server.gI().maps[159].area[IDZONE].bossMap.add(_Chilled);
        Server.gI().maps[159].area[IDZONE].loadBossNoPet(_Chilled);
        sendThongBaoServer("BOSS " + _Chilled.name + " vừa xuất hiện tại " + Server.gI().maps[159].template.name);
        Util.log("INIT _Chilled XONG MAP " + Server.gI().maps[159].template.name + ", " + IDZONE);
    }

    public void initZamasu() {
        int IDZONE = Util.nextInt(0, Server.gI().maps[97].area.length);
        Boss _Zamasu = new Boss(528, (byte) 48, (short) 1335, (short) 192);

        Server.gI().maps[97].area[IDZONE].bossMap.add(_Zamasu);
        Server.gI().maps[97].area[IDZONE].loadBossNoPet(_Zamasu);
        sendThongBaoServer("BOSS " + _Zamasu.name + " vừa xuất hiện tại " + Server.gI().maps[97].template.name);
        Util.log("INIT ZAMASU XONG MAP " + Server.gI().maps[97].template.name + ", " + IDZONE);
    }

    public void initBillWhis() {
        int IDZONE = Util.nextInt(0, Server.gI().maps[5].area.length);
        Boss _Bill = new Boss(529, (byte) 49, (short) 354, (short) 288);
        Server.gI().maps[5].area[IDZONE].bossMap.add(_Bill);
        Server.gI().maps[5].area[IDZONE].loadBossNoPet(_Bill);
        sendThongBaoServer("BOSS " + _Bill.name + " vừa xuất hiện tại " + Server.gI().maps[5].template.name);
        Boss _Whis = new Boss(530, (byte) 50, (short) 305, (short) 288);
        Server.gI().maps[5].area[IDZONE].bossMap.add(_Whis);
        Server.gI().maps[5].area[IDZONE].loadBossNoPet(_Whis);
        sendThongBaoServer("BOSS " + _Whis.name + " vừa xuất hiện tại " + Server.gI().maps[5].template.name);
        Util.log("INIT initBillWhis XONG MAP " + Server.gI().maps[5].template.name + ", " + IDZONE);
    }

    //NGOC RONG SAO DEN
    public void upHPPlayer(Player p) {
        Message m = null;
        try {
            m = new Message(-30);
            m.writer().writeByte(5);
            m.writer().writeInt(p.hp);
            m.writer().flush();
            p.session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //INIT NGOC RONG SAO DEN
    public void initNgocRongSaoDen() {
        Timer timerNRSD = new Timer();
        TimerTask NRSD = new TimerTask() {
            public void run() {
                for (int i = 84; i < 91; i++) {
                    for (int j = 0; j < Server.gI().maps[i].area.length; j++) {
                        Item itemMap = ItemSell.getItemNotSell((i + 288));
                        ItemMap item = new ItemMap();
                        item.playerId = -1;
                        if (i == 84) {
                            item.x = (short) 1003;
                            item.y = (short) 360;
                        } else if (i == 85) {
                            item.x = (short) 693;
                            item.y = (short) 336;
                        } else if (i == 86) {
                            item.x = (short) 739;
                            item.y = (short) 384;
                        } else if (i == 87) {
                            item.x = (short) 723;
                            item.y = (short) 336;
                        } else if (i == 88) {
                            item.x = (short) 881;
                            item.y = (short) 192;
                        } else if (i == 89) {
                            item.x = (short) 763;
                            item.y = (short) 240;
                        } else if (i == 90) {
                            item.x = (short) 915;
                            item.y = (short) 360;
                        }
                        item.itemMapID = Server.gI().maps[i].area[j].itemsMap.size();
                        item.itemTemplateID = (short) (i + 288);
//                        item.itemMapID = (i + 288);
//                        item.itemTemplateID = (short) item.itemMapID;
                        itemMap.template = ItemTemplate.ItemTemplateID((i + 288));
                        item.item = itemMap;
                        Server.gI().maps[i].area[j].itemsMap.add(item);
                        Server.gI().maps[i].area[j].timePickNRSD = System.currentTimeMillis() + (long) 1800000;
                    }
                }
                Server.gI().zoneTimeEndNRSD = System.currentTimeMillis() + (long) (3600000);
                Server.gI().openNRSD = true;
                Timer timerEndNRSD = new Timer();
                TimerTask endNRSD = new TimerTask() {
                    public void run() {
                        if (Server.gI().openNRSD) {
                            Server.gI().openNRSD = false;
                            //DAY HET NGUOI CHOI RA NGOAI
                            for (int i = 84; i < 91; i++) {
                                for (int j = 0; j < Server.gI().maps[i].area.length; j++) {
                                    //ALL PLAYER VE TRAM TAU VU TRU
                                    byte size = (byte) Server.gI().maps[i].area[j].players.size();
                                    for (byte k = 0; k < size; k++) {
                                        if (Server.gI().maps[i].area[j].players.get(0) != null) {
                                            Server.gI().maps[i].area[j].players.get(0).sendAddchatYellow("Trò chơi tìm ngọc đã kết thúc. Hẹn gặp lại bạn vào 20h tối mai");
                                            Server.gI().maps[i].area[j].goMapTransport(Server.gI().maps[i].area[j].players.get(0), (int) Server.gI().maps[i].area[j].players.get(0).gender + 24);
                                        }
                                    }
                                }
                            }
                            //CLEAR XHP NGOC RONG SAO DEN
                            for (Player pall : PlayerManger.gI().getPlayers()) {
                                if (pall != null && pall.xHPSaoDen > (byte) 0) {
                                    pall.xHPSaoDen = (byte) 0;
                                    pall.hp = pall.hp > pall.getHpFull() ? pall.getHpFull() : pall.hp;
                                }
                            }
                        }
                    }
                ;
                };
                timerEndNRSD.schedule(endNRSD, 3600000);
            }
        ;
        };
//        timerNRSD.schedule(NRSD, 10000);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 20); //SERVER 1
//        calendar.set(Calendar.HOUR_OF_DAY, 8); //SERVER 2
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

        timerNRSD.schedule(NRSD, dateSchedule, period);
    }

    //INIT MABU 12h
    public void initBossMabu() {
        int IDZONE = Util.nextInt(0, Server.gI().maps[98].area.length);
        Boss _drabula = null;
        for (int i = 0; i < Server.gI().maps[111].area.length; i++) { //CONG PHI THUYEN
            _drabula = new Boss((i + 200), (byte) 36, (short) 379, (short) 336);
            _drabula.lastTimeUseChargeSkill = System.currentTimeMillis();
            Server.gI().maps[111].area[i].bossMap.add(_drabula);
            Server.gI().maps[111].area[i].loadBossNoPet(_drabula);
        }
        for (int i = 0; i < Server.gI().maps[112].area.length; i++) { //PHONG CHO
            _drabula = new Boss((i + 300), (byte) 37, (short) 379, (short) 336);
            Server.gI().maps[112].area[i].bossMap.add(_drabula);
            Server.gI().maps[112].area[i].loadBossNoPet(_drabula);
        }
        for (int i = 0; i < Server.gI().maps[113].area.length; i++) { //Ai 1
            _drabula = new Boss((i + 350), (byte) 37, (short) 379, (short) 336);
            Server.gI().maps[113].area[i].bossMap.add(_drabula);
            Server.gI().maps[113].area[i].loadBossNoPet(_drabula);
        }
        for (int i = 0; i < Server.gI().maps[114].area.length; i++) { //Ai 2
            _drabula = new Boss((i + 400), (byte) 38, (short) 379, (short) 336);
            Server.gI().maps[114].area[i].bossMap.add(_drabula);
            Server.gI().maps[114].area[i].loadBossNoPet(_drabula);
        }
        for (int i = 0; i < Server.gI().maps[115].area.length; i++) { //Ai 3
            _drabula = new Boss((i + 250), (byte) 36, (short) 379, (short) 336);
            _drabula.lastTimeUseChargeSkill = System.currentTimeMillis();
            Server.gI().maps[115].area[i].bossMap.add(_drabula);
            Server.gI().maps[115].area[i].loadBossNoPet(_drabula);
        }
        for (int i = 0; i < Server.gI().maps[116].area.length; i++) { //PHONG CHI HUY
            _drabula = new Boss((i + 450), (byte) 39, (short) 379, (short) 336);
            Server.gI().maps[116].area[i].bossMap.add(_drabula);
            Server.gI().maps[116].area[i].loadBossNoPet(_drabula);
        }
    }

    public void initMabu12h() {
        Util.log("INIT MABU 12h");
        Timer timerMabu = new Timer();
        TimerTask Mabu = new TimerTask() {
            public void run() {
                //init Boss
                initBossMabu();
                Server.gI().zoneTimeEndNRSD = System.currentTimeMillis() + (long) (3600000);
                Server.gI().openMabu = true;
                Timer timerEndMabu = new Timer();
                TimerTask endMabu = new TimerTask() {
                    public void run() {
                        if (Server.gI().openMabu) {
                            Server.gI().openMabu = false;
                            //DAY HET NGUOI CHOI RA NGOAI
                            for (int i = 111; i < 117; i++) {
                                for (int j = 0; j < Server.gI().maps[i].area.length; j++) {
                                    //ALL PLAYER VE TRAM TAU VU TRU
                                    byte size = (byte) Server.gI().maps[i].area[j].players.size();
                                    for (byte k = 0; k < size; k++) {
                                        if (Server.gI().maps[i].area[j].players.get(0) != null) {
                                            Server.gI().maps[i].area[j].players.get(0).sendAddchatYellow("Đã hết thời gian tiêu diệt Ma bư, hẹn gặp lại vào ngày mai");
                                            Server.gI().maps[i].area[j].goMapTransport(Server.gI().maps[i].area[j].players.get(0), (int) Server.gI().maps[i].area[j].players.get(0).gender + 24);
                                        }
                                    }
                                    //CLEAR BOSS
                                    Server.gI().maps[i].area[j].bossMap.get(0).isdie = true;
                                }
                            }
                        }
                    }
                ;
                };
                timerEndMabu.schedule(endMabu, 3600000);
            }
        ;
        };
//        timerMabu.schedule(Mabu, 10000);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);//SERVER 1
//        calendar.set(Calendar.HOUR_OF_DAY, 0); //SERVER 2
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

        timerMabu.schedule(Mabu, dateSchedule, period);
    }

    //SP QUA MAP MABU
    public void setPowerPoint(Player p, String str, short point, short maxP, short sec) {
        Message m = null;
        try {
            m = new Message(-115);
            m.writer().writeUTF(str);
            m.writer().writeShort(point);
            m.writer().writeShort(maxP);
            m.writer().writeShort(sec);
            m.writer().flush();
            if (p != null && p.session != null) {
                p.session.sendMessage(m);
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

    public void setTrungMabuPoint(ArrayList<Player> listPlayer, byte point) {
        Message m = null;
        try {
            m = new Message(-117);
            m.writer().writeByte(point);
            m.writer().flush();
            for (Player p : listPlayer) {
                if (p != null && p.session != null) {
                    p.session.sendMessage(m);
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

    public void useRingBlack(Player p) {
//        Timer timerTauNgam = new Timer();
//        TimerTask tauNgam = new TimerTask() {
//            public void run()
//            {
//                if(p.waitTransport) {
        Map maptele = MainManager.getMapid(160);
        Controller.getInstance().teleportToMAP(p, maptele);
        if (p.taskId == (short) 31 && p.crrTask.index == (byte) 1) {
            TaskService.gI().updateCountTask(p);
        }
//                    p.timeTauNgam = null;
//                }
//                timerTauNgam.cancel();
//            };
//        };
//        timerTauNgam.schedule(tauNgam, time);
//        p.timeTauNgam = timerTauNgam;
    }

    //****************************************************************************
    public void initNgocRongNamec(byte type) { //type 0:INIT NGOC RONG, type 1: INIT HOA THACH NGOC RONG
        Timer timerNRNM = new Timer();
        TimerTask NRNM = new TimerTask() {
            public void run() {
                ArrayList<Integer> listMap = new ArrayList<>();
//                ArrayList<Integer> mapNRNM = new ArrayList<>();
//                listMap.add(7);listMap.add(7);listMap.add(7);listMap.add(7);listMap.add(7);listMap.add(7);
//                listMap.add(7);listMap.add(7);listMap.add(7);listMap.add(7);
                listMap.add(8);
                listMap.add(9);
                listMap.add(10);
                listMap.add(11);
                listMap.add(12);
                listMap.add(13);
                listMap.add(31);
                listMap.add(32);
                listMap.add(33);
                listMap.add(34);
                int index = -1;
                int idZone = -1;
                for (byte i = 0; i < (byte) 7; i++) {
                    index = Util.nextInt(0, listMap.size());
//                    mapNRNM.add(listMap.get(index));
                    //random khu
                    idZone = Util.nextInt(0, Server.gI().maps[listMap.get(index)].area.length);
//                    idZone = 0;
                    Server.gI().mapNrNamec[i] = listMap.get(index);
                    Server.gI().nameNrNamec[i] = Server.gI().maps[listMap.get(index)].template.name;
                    Server.gI().zoneNrNamec[i] = (byte) idZone;
                    //init ngoc rong namek
                    Item itemMap = null;
                    if (type == (byte) 0) {
                        itemMap = ItemSell.getItemNotSell((int) (i + 353));
                    } else {
                        itemMap = ItemSell.getItemNotSell(362);
                    }
                    ItemMap item = new ItemMap();
                    item.playerId = -1;
                    if (listMap.get(index) == 8) {
                        item.x = (short) 553;
                        item.y = (short) 288;
                    } else if (listMap.get(index) == 9) {
                        item.x = (short) 634;
                        item.y = (short) 432;
                    } else if (listMap.get(index) == 10) {
                        item.x = (short) 711;
                        item.y = (short) 288;
                    } else if (listMap.get(index) == 11) {
                        item.x = (short) 1078;
                        item.y = (short) 336;
                    } else if (listMap.get(index) == 12) {
                        item.x = (short) 1300;
                        item.y = (short) 288;
                    } else if (listMap.get(index) == 13) {
                        item.x = (short) 323;
                        item.y = (short) 432;
                    } else if (listMap.get(index) == 31) {
                        item.x = (short) 606;
                        item.y = (short) 312;
                    } else if (listMap.get(index) == 32) {
                        item.x = (short) 650;
                        item.y = (short) 360;
                    } else if (listMap.get(index) == 33) {
                        item.x = (short) 1325;
                        item.y = (short) 360;
                    } else if (listMap.get(index) == 34) {
                        item.x = (short) 643;
                        item.y = (short) 432;
                    } else if (listMap.get(index) == 7) {
                        item.x = (short) 643;
                        item.y = (short) 432;
                    }

                    item.itemMapID = Server.gI().maps[listMap.get(index)].area[idZone].itemsMap.size();
//                    item.itemMapID = (int)(i + 353);
                    if (type == (byte) 0) {
                        item.itemTemplateID = (short) (i + 353);
//                        item.itemTemplateID = (short) item.itemMapID;
                        itemMap.template = ItemTemplate.ItemTemplateID((int) (i + 353));
                    } else {
                        item.itemTemplateID = (short) 362;
                        itemMap.template = ItemTemplate.ItemTemplateID(362);
                    }
                    item.item = itemMap;
                    Server.gI().maps[listMap.get(index)].area[idZone].itemsMap.add(item);
//                    Util.log("NGOC RONG NAMEC: MAP" + Server.gI().maps[listMap.get(index)].template.name + ", KHU: " + idZone);
                    //remove id map khoi arraylist khoi tao
                    listMap.remove(index);
                }
            }
        ;
        };
        timerNRNM.schedule(NRNM, 5000);
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 13);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        Date dateSchedule = calendar.getTime();
//        Date dateNow = new Date();
//        if(dateNow.after(dateSchedule)) {
//            calendar.setTime(dateSchedule);
//            calendar.add(Calendar.DATE, 1);
//            dateSchedule = calendar.getTime();
//        }
//        
//        long period = 24 * 60 * 60 * 1000;
//
//        timerNRSD.schedule(NRSD, dateSchedule, period);
    }

    public void callDragonNamec(Player p) {
        //SEND EFFECT CHO ALL MAP GOI RONG
        sendEffectServer((byte) 1, (byte) 1, (byte) 20, (short) 341, (short) 582, (short) 1, p);

        Message m = null;
        try {
            m = new Message(-83);
            m.writer().writeByte(0);
            m.writer().writeShort((short) p.map.id); //id map
            m.writer().writeShort((short) p.map.bgId); //bg id map
            m.writer().writeByte(p.zone.id); //khu vuc
            m.writer().writeInt(p.id);
            m.writer().writeUTF("");
            m.writer().writeShort((short) 341); //x rong
            m.writer().writeShort((short) 432);//y rong
            m.writer().writeByte((byte) 1); //1 la rong namek
            m.writer().flush();
            for (Player _p : p.zone.players) {
                _p.session.sendMessage(m);
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

    public void doneDragonNamec(Player p) {
        Player _p = null;
        for (int i = 0; i < 7; i++) {
            _p = PlayerManger.gI().getPlayerByUserID(Server.gI().idpNrNamec[i]);
            if (_p != null && _p.session != null) {
//                _p.itemLung = null;
                _p.imgNRSD = (byte) 0;
                loadPoint(_p.session, _p);

                Server.gI().pNrNamec[i] = "";
                Server.gI().idpNrNamec[i] = -1;

                _p.nrNamec = 0;
                _p.zone.resetBagClan(_p);
                //UPDATE TYKE PK
                _p.typePk = (byte) 0;
                setTypePK(_p, (byte) 0);
            }
        }
        //INIT HOA THACH NGOC RONG
    }

    //REMOVE EFF CALL DRAGON
    public void endEffCallDragon(Player p) {
        Message msg = null;
        try {
            msg = new Message(-83);
            msg.writer().writeByte(1);
            msg.writer().flush();
            for (Player _p : p.zone.players) {
                _p.session.sendMessage(msg);
            }
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    //SET TYPE PK
    public void setTypePK(Player p, byte type) {
        Message m = null;
        try {
            m = new Message(-30);
            m.writer().writeByte((byte) 35);
            m.writer().writeInt(p.id); //ID PLAYER
            m.writer().writeByte(type); //TYPE PK
            m.writer().flush();
            for (Player _p : p.zone.players) {
                if (_p != null && _p.session != null) {
                    _p.session.sendMessage(m);
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

    public void removeStoneNrNamec() {
        Zone _zone = null;
        for (byte i = 0; i < (byte) 7; i++) {
            _zone = Server.gI().maps[Server.gI().mapNrNamec[i]].area[Server.gI().zoneNrNamec[i]];
            int idItem = (int) (i + 353);
            for (byte j = 0; j < _zone.itemsMap.size(); j++) {
                if (_zone.itemsMap.get(j).itemMapID == idItem) {
                    _zone.itemsMap.remove(j);
                    _zone.removeItemNOW((short) idItem);
                }
            }
        }
    }

    public void reInitNrNamec(long time) {
        if (time > 0) {
            Timer timerNRNM = new Timer();
            TimerTask NRNM = new TimerTask() {
                public void run() {
                    //REMOVE ALL HOA THACH NGOC RONG
                    removeStoneNrNamec();
                    //INIT LAI NGOC RONG NAMEC
                    initNgocRongNamec((byte) 0);
                }
            ;
            };
            timerNRNM.schedule(NRNM, time);
        }
    }

    //TELEPORT NGOCRONG NAMEC
    public void teleportToNrNamec(Player p) {
        if (p.idNrNamecGo >= (byte) 0 && p.idNrNamecGo <= (byte) 6) {
            int idMAP = Server.gI().mapNrNamec[p.idNrNamecGo];
            int idZone = Server.gI().zoneNrNamec[p.idNrNamecGo];
            Map map = Server.gI().maps[idMAP];
            short _goX = 0;
            short _goY = 10;
            int _rdLocation = 0;
            if (idMAP == 43) { // || mapid == 84
                if (map.template.npcs.length > 1) {
                    _rdLocation = Util.nextInt(0, (map.template.npcs.length - 1)); //get index npc random
                }
                _goX = (short) (map.template.npcs[_rdLocation].cx);
            } else {
                _rdLocation = Util.nextInt(0, (map.template.arMobid.length - 1)); //get index mob random
                _goX = map.template.arrMobx[_rdLocation];
                _goY = map.template.arrMoby[_rdLocation];
            }
            p.x = _goX;
            p.y = _goY;
            byte errornext = -1;
            if (errornext == -1) {
                if (map.area[idZone].players.size() < map.template.maxplayers) {
                    //leave suphu va de tu khoi map
                    if (p.petfucus == 1) {
                        p.zone.leaveDetu(p, p.detu);
                    }
                    if (p.pet2Follow == 1 && p.pet != null) {
                        p.zone.leavePETTT(p.pet);
                    }
                    p.zone.leave(p);
                    //nhan vat bay tu tren troi xuong
                    // map clear
                    Message m = null;
                    try {
                        m = new Message(-22);
                        m.writer().flush();
                        p.session.sendMessage(m);
                        m.cleanup();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    map.area[idZone].Enter(p);
                    return;
                } else {
                    errornext = 0;
                }
            }

            p.zone.EnterCapsule(p);
            switch (errornext) {
                case 0:
                    p.sendAddchatYellow("Bản đồ quá tải.");
                    return;
            }
        }
    }

    //ROT NGOC RONG RA DAT
    public void dropDragonBall(Player p) {
        if (p.imgNRSD == (byte) 37) {
            p.imgNRSD = (byte) 0;
            p.timeWINNRSD = 0;
            p.zone.resetBagClan(p);
            ItemMap itemM = p.zone.createNewItemMap((p.map.id + 287), -1, p.x, p.y);
            p.zone.addItemToMap(itemM, -1, p.x, p.y);
        } else if (p.imgNRSD == (byte) 53 && p.nrNamec != 0) {
            //NEU LA NGOC RONG 1 SAO THI RESET CAN CALL
            if (p.nrNamec == 353) {
                Server.gI().firstNrNamec = true;
                Server.gI().timeNrNamec = 0;
            }
            p.imgNRSD = (byte) 0;
            p.zone.resetBagClan(p);
            ItemMap itemM = p.zone.createNewItemMap(p.nrNamec, -1, p.x, p.y);
            Server.gI().pNrNamec[(int) (p.nrNamec - 353)] = "";
            Server.gI().idpNrNamec[(int) (p.nrNamec - 353)] = -1;
            p.nrNamec = 0;
            //UPDATE TYKE PK
            p.typePk = (byte) 0;
            Service.gI().setTypePK(p, (byte) 0);
            if (!p.zone.isHasItemInMap(itemM) && p.zone.map.MapDropNrNamec()) {
                p.zone.addItemToMap(itemM, -1, p.x, p.y);
            }
        }
    }

    //CHECK SAME OF ARRAY
    public boolean isSameMapNrNamec() {
        return (Server.gI().mapNrNamec[0] == 7) && (Server.gI().mapNrNamec[1] == 7) && (Server.gI().mapNrNamec[2] == 7) && (Server.gI().mapNrNamec[3] == 7) && (Server.gI().mapNrNamec[4] == 7) && (Server.gI().mapNrNamec[5] == 7) && (Server.gI().mapNrNamec[6] == 7);
    }

    public boolean isSameZoneNrNamec() {
        return (Server.gI().zoneNrNamec[0] == Server.gI().zoneNrNamec[1]) && (Server.gI().zoneNrNamec[2] == Server.gI().zoneNrNamec[0]) && (Server.gI().zoneNrNamec[3] == Server.gI().zoneNrNamec[0]) && (Server.gI().zoneNrNamec[4] == Server.gI().zoneNrNamec[0]) && (Server.gI().zoneNrNamec[5] == Server.gI().zoneNrNamec[0]) && (Server.gI().zoneNrNamec[6] == Server.gI().zoneNrNamec[0]);
    }

    //*************************Hirudegarn************************************
    public void initHirudegarn() {
        Timer timerNRSD = new Timer();
        TimerTask NRSD = new TimerTask() {
            public void run() {
                Server.gI().openHiru = true;
                for (int i = 0; i < Server.gI().maps.length; i++) {
                    if (Server.gI().maps[i].template.id == 126) {
                        int indexM = -1;
                        for (int ix = 0; ix < Server.gI().maps[i].area[0].mobs.size(); ix++) {
                            if (Server.gI().maps[i].area[0].mobs.get(ix).template.tempId == 70) {
                                indexM = ix;
                                break;
                            }
                        }
                        for (int j = 0; j < Server.gI().maps[i].area.length; j++) {
//                            if(Server.gI().maps[i].area[j].getMobByTempID(70) != null) {
//                                Server.gI().maps[i].area[j].refreshMob(70);
//                            }
                            if (indexM != -1) {
                                Server.gI().maps[i].area[j].refreshMob(indexM);
                            }
                        }
                        break;
                    }
                }
                Timer timerEndNRSD = new Timer();
                TimerTask endNRSD = new TimerTask() {
                    public void run() {
                        if (Server.gI().openHiru) {
                            Server.gI().openHiru = false;
                            //DAY HET NGUOI CHOI RA NGOAI
                            for (int i = 0; i < Server.gI().maps.length; i++) {
                                if (Server.gI().maps[i].template.id == 126) {
                                    for (int j = 0; j < Server.gI().maps[i].area.length; j++) {
                                        //ALL PLAYER VE TRAM TAU VU TRU
                                        byte size = (byte) Server.gI().maps[i].area[j].players.size();
                                        for (byte k = 0; k < size; k++) {
                                            if (Server.gI().maps[i].area[j].players.get(0) != null) {
                                                Server.gI().maps[i].area[j].players.get(0).sendAddchatYellow("Thời gian tiêu diệt Hirudegarn đã kết thúc");
                                                Server.gI().maps[i].area[j].goMapTransport(Server.gI().maps[i].area[j].players.get(0), (int) Server.gI().maps[i].area[j].players.get(0).gender + 24);
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                ;
                };
                timerEndNRSD.schedule(endNRSD, 3600000);
            }
        ;
        };
//        timerNRSD.schedule(NRSD, 5000);
        
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 22); //SERVER 1
//        calendar.set(Calendar.HOUR_OF_DAY, 10); //SERVER 2
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

        timerNRSD.schedule(NRSD, dateSchedule, period);
    }

    //******************************************MAP UP YARDRAT******************
    public void initMapYardrat() {
        Timer timerYardrat = new Timer();
        TimerTask yardrat = new TimerTask() {
            public void run() {
                Boss _boss = null;
                for (int i = 0; i < Server.gI().maps[127].area.length; i++) {
                    _boss = new Boss(510, (byte) 44, (short) 140, (short) 456);
                    _boss.setXYStart(510);
                    Server.gI().maps[127].area[i].bossMap.add(_boss);
                    Server.gI().maps[127].area[i].loadBossNoCharge(_boss);
                    _boss = new Boss(511, (byte) 44, (short) 379, (short) 456);
                    _boss.setXYStart(511);
                    Server.gI().maps[127].area[i].bossMap.add(_boss);
                    Server.gI().maps[127].area[i].loadBossNoCharge(_boss);
                    _boss = new Boss(512, (byte) 44, (short) 627, (short) 456);
                    _boss.setXYStart(512);
                    Server.gI().maps[127].area[i].bossMap.add(_boss);
                    Server.gI().maps[127].area[i].loadBossNoCharge(_boss);
                    _boss = new Boss(513, (byte) 44, (short) 780, (short) 456);
                    _boss.setXYStart(513);
                    Server.gI().maps[127].area[i].bossMap.add(_boss);
                    Server.gI().maps[127].area[i].loadBossNoCharge(_boss);
                    _boss = new Boss(514, (byte) 44, (short) 1058, (short) 456);
                    _boss.setXYStart(514);
                    Server.gI().maps[127].area[i].bossMap.add(_boss);
                    Server.gI().maps[127].area[i].loadBossNoCharge(_boss);
                    _boss = new Boss(515, (byte) 45, (short) 1220, (short) 456);
                    _boss.setXYStart(515);
                    Server.gI().maps[127].area[i].bossMap.add(_boss);
                    Server.gI().maps[127].area[i].loadBossNoCharge(_boss);
                }
                for (int i = 0; i < Server.gI().maps[128].area.length; i++) {
                    _boss = new Boss(516, (byte) 45, (short) 186, (short) 456);
                    _boss.setXYStart(516);
                    Server.gI().maps[128].area[i].bossMap.add(_boss);
                    Server.gI().maps[128].area[i].loadBossNoCharge(_boss);
                    _boss = new Boss(517, (byte) 45, (short) 374, (short) 456);
                    _boss.setXYStart(517);
                    Server.gI().maps[128].area[i].bossMap.add(_boss);
                    Server.gI().maps[128].area[i].loadBossNoCharge(_boss);
                    _boss = new Boss(518, (byte) 45, (short) 631, (short) 432);
                    _boss.setXYStart(518);
                    Server.gI().maps[128].area[i].bossMap.add(_boss);
                    Server.gI().maps[128].area[i].loadBossNoCharge(_boss);
                    _boss = new Boss(519, (byte) 45, (short) 785, (short) 432);
                    _boss.setXYStart(519);
                    Server.gI().maps[128].area[i].bossMap.add(_boss);
                    Server.gI().maps[128].area[i].loadBossNoCharge(_boss);
                    _boss = new Boss(520, (byte) 45, (short) 1046, (short) 456);
                    _boss.setXYStart(520);
                    Server.gI().maps[128].area[i].bossMap.add(_boss);
                    Server.gI().maps[128].area[i].loadBossNoCharge(_boss);
                    _boss = new Boss(521, (byte) 46, (short) 1240, (short) 432);
                    _boss.setXYStart(521);
                    Server.gI().maps[128].area[i].bossMap.add(_boss);
                    Server.gI().maps[128].area[i].loadBossNoCharge(_boss);
                }
                for (int i = 0; i < Server.gI().maps[129].area.length; i++) {
                    _boss = new Boss(522, (byte) 46, (short) 238, (short) 456);
                    _boss.setXYStart(522);
                    Server.gI().maps[129].area[i].bossMap.add(_boss);
                    Server.gI().maps[129].area[i].loadBossNoCharge(_boss);
                    _boss = new Boss(523, (byte) 46, (short) 413, (short) 456);
                    _boss.setXYStart(523);
                    Server.gI().maps[129].area[i].bossMap.add(_boss);
                    Server.gI().maps[129].area[i].loadBossNoCharge(_boss);
                    _boss = new Boss(524, (byte) 46, (short) 595, (short) 456);
                    _boss.setXYStart(524);
                    Server.gI().maps[129].area[i].bossMap.add(_boss);
                    Server.gI().maps[129].area[i].loadBossNoCharge(_boss);
                    _boss = new Boss(525, (byte) 46, (short) 840, (short) 456);
                    _boss.setXYStart(525);
                    Server.gI().maps[129].area[i].bossMap.add(_boss);
                    Server.gI().maps[129].area[i].loadBossNoCharge(_boss);
                    _boss = new Boss(526, (byte) 46, (short) 1050, (short) 456);
                    _boss.setXYStart(526);
                    Server.gI().maps[129].area[i].bossMap.add(_boss);
                    Server.gI().maps[129].area[i].loadBossNoCharge(_boss);
                    _boss = new Boss(527, (byte) 47, (short) 1233, (short) 456);
                    _boss.setXYStart(527);
                    Server.gI().maps[129].area[i].bossMap.add(_boss);
                    Server.gI().maps[129].area[i].loadBossNoCharge(_boss);
                }
            }
        ;
        };

        timerYardrat.schedule(yardrat, 5000);
    }
    //******************************************END MAP UP YARDRAT******************

    //DOANH TRAI
    public void initMapDoanhTrai(Clan clan, int idMap, byte index) {
//        MapTemplate doanhTraiTemp = map.template.getMapTemplateByIDMap(53);
        MapTemplate doanhTraiTemp = MapTemplate.getMapTempByIDMap(idMap);
        Map doanhTrai = new Map(doanhTraiTemp);
        doanhTrai.area[0].updateMobAuto();
        doanhTrai.start();
        clan.doanhTrai[index] = doanhTrai;
        if (index == (byte) 0) {
            Server.gI().cDoanhTrai = (short) (Server.gI().cDoanhTrai + 1) > (short) 200 ? (short) 200 : (short) (Server.gI().cDoanhTrai + 1);
            clan.openDoanhTrai = true;
            clan.topen = System.currentTimeMillis();
            //UPDATE TIME OPEN DOANH TRAI LEN DATABASE
            ClanDAO.updateTimeOpenDT(clan.id);
            //TIME CON LAI
            Timer timerConLaiDT = new Timer();
            TimerTask conlaiDT = new TimerTask() {
                public void run() {
                    if (clan.openDoanhTrai) {
                        for (byte i = 0; i < clan.doanhTrai.length; i++) {
                            if (clan.doanhTrai[i] != null) {
                                byte size = (byte) (clan.doanhTrai[i].area[0].players.size());
                                for (byte k = 0; k < size; k++) {
                                    if (clan.doanhTrai[i].area[0].players.get(0) != null) {
                                        clan.doanhTrai[i].area[0].players.get(0).sendAddchatYellow("Còn " + (30 - (int) ((System.currentTimeMillis() - clan.topen) / 60000)) + " phút trại Độc Nhãn sẽ kết thúc");
                                    }
                                }
                            }
                        }
                    } else {
                        timerConLaiDT.cancel();
                    }
                }
            ;
            };
            timerConLaiDT.schedule(conlaiDT, 0, 300000);

            //END DOANH TRAI
            Timer timerEndDT = new Timer();
            TimerTask endDT = new TimerTask() {
                public void run() {
                    if (clan.openDoanhTrai) {
                        Server.gI().cDoanhTrai = (short) (Server.gI().cDoanhTrai - 1) < (short) 0 ? (short) 0 : (short) (Server.gI().cDoanhTrai - 1);
                        clan.openDoanhTrai = false;
                        //DAY HET NGUOI CHOI RA NGOAI
                        for (byte i = 0; i < clan.doanhTrai.length; i++) {
                            //ALL PLAYER VE TRAM TAU VU TRU
                            if (clan.doanhTrai[i] != null) {
                                byte size = (byte) (clan.doanhTrai[i].area[0].players.size());
                                for (byte k = 0; k < size; k++) {
                                    if (clan.doanhTrai[i].area[0].players.get(0) != null) {
                                        clan.doanhTrai[i].area[0].players.get(0).sendAddchatYellow("Thời gian danh trại Độc Nhãn đã kết thúc");
                                        clan.doanhTrai[i].area[0].goMapTransport(clan.doanhTrai[i].area[0].players.get(0), (int) clan.doanhTrai[i].area[0].players.get(0).gender + 24);
                                    }
                                }
                            }
                        }
                        clan.doanhTrai = null;
                        clan.doanhTrai = new Map[10];
                        timerEndDT.cancel();
                    }
                }
            ;
            };
            timerEndDT.schedule(endDT, 1800000);
        } else if (index == (byte) 2) { //TUONG THANH3 INIT TRUNG UY TRANG
            Boss _TUT = new Boss(150, (byte) 31, (short) 923, (short) 384);
            clan.doanhTrai[2].area[0].bossMap.add(_TUT);
            loadBossDoanhTrai(_TUT, clan.doanhTrai[2].area[0]);
        } else if (index == (byte) 5) {
            Boss _TUXL = new Boss(151, (byte) 32, (short) 1088, (short) 384);
            clan.doanhTrai[5].area[0].bossMap.add(_TUXL);
            loadBossDoanhTrai(_TUXL, clan.doanhTrai[5].area[0]);
        } else if (index == (byte) 6) {
            Boss _TUTHEP = new Boss(152, (byte) 33, (short) 830, (short) 312);
            clan.doanhTrai[6].area[0].bossMap.add(_TUTHEP);
            loadBossDoanhTrai(_TUTHEP, clan.doanhTrai[6].area[0]);
        } else if (index == (byte) 8) { //NINJA AO TIM
            Boss _TUTHEP = new Boss(153, (byte) 34, (short) 994, (short) 312);
            clan.doanhTrai[8].area[0].bossMap.add(_TUTHEP);
            loadBossDoanhTrai(_TUTHEP, clan.doanhTrai[8].area[0]);
        } else if (index == (byte) 9) { //ROBOT VE SI
            Boss _TUTHEP = new Boss(158, (byte) 35, (short) 1443, (short) 312);
            clan.doanhTrai[9].area[0].bossMap.add(_TUTHEP);
            loadBossDoanhTrai(_TUTHEP, clan.doanhTrai[9].area[0]);
            Boss _TUTHEP2 = new Boss(159, (byte) 35, (short) 1493, (short) 312);
            clan.doanhTrai[9].area[0].bossMap.add(_TUTHEP2);
            loadBossDoanhTrai(_TUTHEP2, clan.doanhTrai[9].area[0]);
            Boss _TUTHEP3 = new Boss(160, (byte) 35, (short) 1393, (short) 312);
            clan.doanhTrai[9].area[0].bossMap.add(_TUTHEP3);
            loadBossDoanhTrai(_TUTHEP3, clan.doanhTrai[9].area[0]);
            Boss _TUTHEP4 = new Boss(161, (byte) 35, (short) 1343, (short) 312);
            clan.doanhTrai[9].area[0].bossMap.add(_TUTHEP4);
            loadBossDoanhTrai(_TUTHEP4, clan.doanhTrai[9].area[0]);
        }
    }

    //HUY GIAO DICH KHI DI SANG MAP KHAC
    public void destroyGiaoDich(Player player) {
        if (player._friendGiaoDich != null && player._friendGiaoDich._friendGiaoDich != null) { //HUY GIAO DICH
            //SEND TAT UI GIAO DICH
            //ADD ITEM TO ME
            for (int i = 0; i < player._indexGiaoDich.size(); i++) {
                byte indexME = player.getIndexBagNotItem();
//                                Item _item = new Item(item);
//                                _item.quantity += (item.quantity - 1);
                player.ItemBag[indexME] = player._itemGiaoDich.get(i);
            }
            //ADD ITEM TO FRIEND
            for (int i = 0; i < player._friendGiaoDich._indexGiaoDich.size(); i++) {
                byte indexFRIEND = player._friendGiaoDich.getIndexBagNotItem();
//                                Item _item = new Item(item);
//                                _item.quantity += (item.quantity - 1);
                player._friendGiaoDich.ItemBag[indexFRIEND] = player._friendGiaoDich._itemGiaoDich.get(i);
            }
            if (player._goldGiaoDich > 0) {
                long _VANG = ((long) player.vang + (long) player._goldGiaoDich) > 2000000000L ? 2000000000L : ((long) player.vang + (long) player._goldGiaoDich);
                player.vang = _VANG;
            }
            if (player._friendGiaoDich._goldGiaoDich > 0) {
                long _VANG2 = ((long) player._friendGiaoDich.vang + (long) player._friendGiaoDich._goldGiaoDich) > 2000000000L ? 2000000000L : ((long) player._friendGiaoDich.vang + (long) player._friendGiaoDich._goldGiaoDich);
                player._friendGiaoDich.vang = _VANG2;
            }
            //SEND HUY GIAO DICH
            //SEND TAT UI GIAO DICH
            Message m = null;
            try {
                m = new Message(-86);
                m.writer().writeByte(7);
                m.writer().flush();
                player.session.sendMessage(m);
                player._friendGiaoDich.session.sendMessage(m);
                m.cleanup();
            } catch (Exception var2) {
                var2.printStackTrace();
            } finally {
                if (m != null) {
                    m.cleanup();
                }
            }
            //UPDATE BAG CHO TOI
            Service.gI().updateItemBag(player);
            Service.gI().buyDone(player);
            //UPDATE BAG CHO FRIEND
            Service.gI().updateItemBag(player._friendGiaoDich);
            Service.gI().buyDone(player._friendGiaoDich);
            try {
                m = new Message(-86);
                m.writer().writeByte(7);
                m.writer().flush();
                player.session.sendMessage(m);
                player._friendGiaoDich.session.sendMessage(m);
                m.cleanup();
            } catch (Exception var2) {
                var2.printStackTrace();
            } finally {
                if (m != null) {
                    m.cleanup();
                }
            }
            player._friendGiaoDich.sendAddchatYellow("Giao dịch đã bị hủy");

            player._isGiaoDich = false;
            player._friendGiaoDich._isGiaoDich = false;
            //CLEAR VARIABLE SAU KHI GIAO DICH XONG CHO FRIEND
            player._friendGiaoDich._indexGiaoDich.clear();
            player._friendGiaoDich._itemGiaoDich.clear();
            player._friendGiaoDich._friendGiaoDich = null;
            player._friendGiaoDich._confirmGiaoDich = false;
            player._friendGiaoDich._goldGiaoDich = 0;
            //CLEAR VARIABLE SAU KHI GIAO DICH XONG CHO TOI
            player._indexGiaoDich.clear();
            player._itemGiaoDich.clear();
            player._friendGiaoDich = null;
            player._confirmGiaoDich = false;
            player._goldGiaoDich = 0;
        }
    }

    //DOANH TRAI
    public void loadBossDoanhTrai(Boss _boss, Zone _zone) {
        Message msg;
        try {
            _zone.loadInfoBoss(_boss); //tha broly theo server roi khong can ve cho all player trong map nua
            _boss.zone = _zone;
            // lap trinh broly tu dong
            Timer timerBroly = new Timer();
            TimerTask timeBroly = new TimerTask() {
                public void run() {
                    if (_boss.isdie) {
                        timerBroly.cancel();
                        return;
                    } else {
                        Player _charTarget = _zone.getCharNearest(_boss.x, _boss.y, 200); //get char gan nhat
                        long timeNow = System.currentTimeMillis();
                        if ((timeNow - _boss.lastTimeUseChargeSkill) > 15000 && _boss.hp < _boss.hpFull) {
                            _boss.lastTimeUseChargeSkill = timeNow;

                        }

                        if (_charTarget != null && !_boss.isTTNL) {
                            _zone.bossAttackChar(_boss, _charTarget);
                        }
                    }
                }
            };
            timerBroly.schedule(timeBroly, 0, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String writePartBodyLeg(Player player) {
        if (player.isMonkey) {
            return "193,194";
//            m.writer().writeShort(193);
//            m.writer().writeShort(194);
        } else if ((player.ItemBody[5] != null && player.ItemBody[5].template.level != 0 && player.ItemBody[5].template.part == (short) (-1) && (player.ItemBody[5].template.id != 601 && player.ItemBody[5].template.id != 602 && player.ItemBody[5].template.id != 603
                && player.ItemBody[5].template.id != 639 && player.ItemBody[5].template.id != 640 && player.ItemBody[5].template.id != 641)) || player.NhapThe == 1) {
            if (player.NhapThe == 1) {
                return (String.valueOf(player.PartHead() + 1) + "," + String.valueOf(player.PartHead() + 2));
//                m.writer().writeShort(player.PartHead() + 1);
//                m.writer().writeShort(player.PartHead() + 2);
            } else if (player.ItemBody[5] != null && player.ItemBody[5].template.id == 604) {//CAI TRANG VIP
                return "472,473";
//                m.writer().writeShort(472);
//                m.writer().writeShort(473);
            } else if (player.ItemBody[5] != null && player.ItemBody[5].template.id == 605) {//CAI TRANG VIP
                return "476,477";
//                m.writer().writeShort(476);
//                m.writer().writeShort(477);
            } else if (player.ItemBody[5] != null && player.ItemBody[5].template.id == 606) {//CAI TRANG VIP
                return "474,475";
//                m.writer().writeShort(474);
//                m.writer().writeShort(475);
            } else if (player.ItemBody[5] != null && (player.ItemBody[5].template.id == 545 || player.ItemBody[5].template.id == 546)) {//CAI TRANG VIP
                return "458,459";
            } else if (player.ItemBody[5] != null && player.ItemBody[5].template.id >= 592 && player.ItemBody[5].template.id <= 594) {//YADART
                return "523,524";
            } else if (player.ItemBody[5].template.id == 545 || player.ItemBody[5].template.id == 546 || player.ItemBody[5].template.id == 857 || player.ItemBody[5].template.id == 858) {
                return (String.valueOf(player.PartBody()) + "," + String.valueOf(player.Leg()));
            } else {
                return (String.valueOf(player.PartHead() + 1) + "," + String.valueOf(player.PartHead() + 2));
//                m.writer().writeShort(player.PartHead() + 1);
//                m.writer().writeShort(player.PartHead() + 2);
            }
        } else {
            return (String.valueOf(player.PartBody()) + "," + String.valueOf(player.Leg()));
//            m.writer().writeShort(player.PartBody());
//            m.writer().writeShort(player.Leg());
        }
    }

    public String writePartBodyLegDetu(Detu player) {
        if (player.isMonkey) {
            return "193,194";
        } else if ((player.ItemBody[5] != null && player.ItemBody[5].template.level != 0 && player.ItemBody[5].template.part == (short) (-1) && (player.ItemBody[5].template.id != 601 && player.ItemBody[5].template.id != 602 && player.ItemBody[5].template.id != 603
                && player.ItemBody[5].template.id != 639 && player.ItemBody[5].template.id != 640 && player.ItemBody[5].template.id != 641)) || player.NhapThe == 1 || player.isSoSinh) {
            if (player.NhapThe == 1 || player.isSoSinh) {
                return (String.valueOf(player.PartHead() + 1) + "," + String.valueOf(player.PartHead() + 2));
            } else if (player.ItemBody[5] != null && player.ItemBody[5].template.id == 604) {//CAI TRANG VIP
                return "472,473";
            } else if (player.ItemBody[5] != null && player.ItemBody[5].template.id == 605) {//CAI TRANG VIP
                return "476,477";
            } else if (player.ItemBody[5] != null && player.ItemBody[5].template.id == 606) {//CAI TRANG VIP
                return "474,475";
            } else if (player.ItemBody[5] != null && (player.ItemBody[5].template.id == 545 || player.ItemBody[5].template.id == 546)) {//CAI TRANG VIP
                return "458,459";
            } else if (player.ItemBody[5] != null && player.ItemBody[5].template.id >= 592 && player.ItemBody[5].template.id <= 594) {//YADART
                return "523,524";
            } else if (player.ItemBody[5].template.id == 545 || player.ItemBody[5].template.id == 546 || player.ItemBody[5].template.id == 857 || player.ItemBody[5].template.id == 858) {
                return (String.valueOf(player.PartBody()) + "," + String.valueOf(player.Leg()));
            } else {
                return (String.valueOf(player.PartHead() + 1) + "," + String.valueOf(player.PartHead() + 2));
            }
        } else {
            return (String.valueOf(player.PartBody()) + "," + String.valueOf(player.Leg()));
        }
    }

    public void loadCaiTrangTemp(Player _bTarget) {
        if ((_bTarget.ItemBody[5] != null && _bTarget.ItemBody[5].template.level != 0) || _bTarget.NhapThe == 1) {
            if (_bTarget.NhapThe == 1) {
                LoadCaiTrang(_bTarget, 1, _bTarget.PartHead(), _bTarget.PartHead() + 1, _bTarget.PartHead() + 2);
            } else if (_bTarget.ItemBody[5] != null && _bTarget.ItemBody[5].template.id == 604) {//CAI TRANG VIP
                LoadCaiTrang(_bTarget, 1, _bTarget.PartHead(), 472, 473);
            } else if (_bTarget.ItemBody[5] != null && _bTarget.ItemBody[5].template.id == 605) {//CAI TRANG VIP
                LoadCaiTrang(_bTarget, 1, _bTarget.PartHead(), 476, 477);
            } else if (_bTarget.ItemBody[5] != null && _bTarget.ItemBody[5].template.id == 606) {//CAI TRANG VIP
                LoadCaiTrang(_bTarget, 1, _bTarget.PartHead(), 474, 475);
            } else if (_bTarget.ItemBody[5].template.id >= 592 && _bTarget.ItemBody[5].template.id <= 594) { //YADART
                if (_bTarget.id == 1) {
                    LoadCaiTrang(_bTarget, 1, 126, 523, 524);
                } else {
                    LoadCaiTrang(_bTarget, 1, _bTarget.PartHead(), 523, 524);
                }
            } else if (_bTarget.ItemBody[5] != null && (_bTarget.ItemBody[5].template.id != 601 && _bTarget.ItemBody[5].template.id != 602
                    && _bTarget.ItemBody[5].template.id != 603 && _bTarget.ItemBody[5].template.id != 639 && _bTarget.ItemBody[5].template.id != 640 && _bTarget.ItemBody[5].template.id != 641)) {
                if (_bTarget.ItemBody[5].template.part != (short) (-1) || _bTarget.ItemBody[5].template.id == 545 || _bTarget.ItemBody[5].template.id == 546 || _bTarget.ItemBody[5].template.id == 857 || _bTarget.ItemBody[5].template.id == 858) {
                    LoadCaiTrang(_bTarget, 1, _bTarget.PartHead(), _bTarget.PartBody(), _bTarget.Leg());
                } else {
                    LoadCaiTrang(_bTarget, 1, _bTarget.PartHead(), _bTarget.PartHead() + 1, _bTarget.PartHead() + 2);
                }
            }
        } else {
            LoadCaiTrang(_bTarget, 1, _bTarget.PartHead(), _bTarget.PartBody(), _bTarget.Leg());
        }
    }

    //TRUNG MABU
    public void isTrungMabu(Player p) {
        Message m = null;
        try {
            m = new Message(-122);
            m.writer().writeShort((short) 50);
            m.writer().writeByte((byte) 1);
            m.writer().writeShort((short) 4664);
            m.writer().writeByte((byte) 0); //INDEX
            m.writer().writeInt(1000); //second
            m.writer().flush();
            p.session.sendMessage(m);
            m.cleanup();

            m = new Message(-117);
            m.writer().writeByte((byte) 101);
            m.writer().flush();
            p.session.sendMessage(m);
            m.cleanup();
        } catch (Exception var2) {
            var2.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    //ITEM DEO LUNG
    public void setItemBagNew(Player player, int idItem) {
        byte idBag = idItemToIdBag(idItem);
        if (idBag != (byte) (-1)) {
            player.imgNRSD = idBag;
            if (player.map.MapHome()) {
                updateBagNew(player.session, player.id, idBag);
                ClanService.gI().getBagBangNew(player.session, idBag);
            } else {
                for (Player _p : player.zone.players) {
                    //UPDATE BAG SAU LUNG
                    updateBagNew(_p.session, player.id, idBag);
                    //GET BAG SAU LUNG
                    ClanService.gI().getBagBangNew(_p.session, idBag);
                    //            if(_p.id != player.id) {
                    //                ClanService.gI().loadUpdateInfoMember(_p.session, player);
                    //            }
                }
            }
        }
    }

    public byte idItemToIdBag(int idItem) {
        switch (idItem) {
            case 1001:
                return (byte) 38;
            case 1028:
                return (byte) 39;
            case 995:
                return (byte) 40;
            case 996:
                return (byte) 41;
            case 997:
                return (byte) 42;
            case 998:
                return (byte) 43;
            case 999:
                return (byte) 44;
            case 1000:
                return (byte) 45;
            case 1007:
                return (byte) 46;
            case 1013:
                return (byte) 47;
            case 1021:
                return (byte) 48;
            case 1022:
                return (byte) 49;
            case 1023:
                return (byte) 50;
            case 954:
                return (byte) 51;
            case 955:
                return (byte) 52;
            case 1047:
                return (byte) 54;
            case 467:
                return (byte) 32;
            case 468:
                return (byte) 33;
            case 469:
                return (byte) 34;
            case 470:
                return (byte) 35;
            case 471:
                return (byte) 36;
            case 800:
                return (byte) 55;
            case 801:
                return (byte) 56;
            case 802:
                return (byte) 57;
            case 803:
                return (byte) 58;
            case 804:
                return (byte) 59;
            case 805:
                return (byte) 60;
            case 814:
                return (byte) 61;
            case 815:
                return (byte) 62;
            case 816:
                return (byte) 63;
            case 817:
                return (byte) 64;
            case 822:
                return (byte) 65;
            case 823:
                return (byte) 66;
            case 852:
                return (byte) 67;
            case 865:
                return (byte) 68;
            case 966:
                return (byte) 69;
            case 982:
                return (byte) 70;
            case 983:
                return (byte) 71;
            case 994:
                return (byte) 72;
            case 1030:
                return (byte) 73;
            case 1031:
                return (byte) 74;
            case 740:
                return (byte) 75;
            case 741:
                return (byte) 76;
            case 745:
                return (byte) 77;
            case 1100:
                return (byte) 78;
            case 1108:
                return (byte) 79;
            case 1109:
                return (byte) 80;
            case 1110:
                return (byte) 81;
            case 1111:
                return (byte) 82;
        }
        return (byte) (-1);
    }

    public void refreshItemPlayer(Player p, byte type, byte index, Item item) { //type = 0: item body, type = 1: item bag
        Message m = null;
        try {
            m = new Message(100);
            m.writer().writeByte(type);
            m.writer().writeByte(index);
            m.writer().writeShort((short) item.template.id);

            m.writer().writeInt(item.quantity);
            m.writer().writeUTF(item.getInfo());
            m.writer().writeUTF(item.getContent());
            m.writer().writeByte(item.itemOptions.size());
            for (ItemOption itemOption : item.itemOptions) {
                m.writer().writeByte(itemOption.optionTemplate.id);
                m.writer().writeShort(itemOption.param);
            }
            m.writer().flush();
            p.session.sendMessage(m);
            m.cleanup();
        } catch (Exception var2) {
            var2.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    //LEAVE ALL
    public void leaveOutMap(Player p) {
        if (p.petfucus == 1) {
            p.zone.leaveDetu(p, p.detu);
        }
        if (p.pet2Follow == 1 && p.pet != null) {
            p.zone.leavePETTT(p.pet);
        }
        p.zone.leave(p);
    }

    //*************************************INIT KHI GA****************************
    public void initKhiGa(Clan clan, int level) {
//        MapTemplate doanhTraiTemp = map.template.getMapTemplateByIDMap(53);
        Server.gI().cKhiGas = (short) (Server.gI().cKhiGas + 1) > (short) 200 ? (short) 200 : (short) (Server.gI().cKhiGas + 1);
        clan.cOpenGas = (byte) (clan.cOpenGas - 1) < (byte) 0 ? (byte) 0 : (byte) (clan.cOpenGas - 1);
        clan.openKhiGas = true;
        clan.lvGas = level;
        clan.topenGas = System.currentTimeMillis();
        MapTemplate mapTemp1 = MapTemplate.getMapTempByIDMap(149); //THANH PHO SATAN
        Map map1 = new Map(mapTemp1);
        map1.area[0].updateMobAuto();
        map1.start();
        //INIT HP, DAME MOB
        if (map1.area[0].mobs.size() > 0) {
            for (Mob _mob : map1.area[0].mobs) {
                _mob.maxHp = (int) (_mob.maxHp * 20 * level);
                _mob.hp = _mob.maxHp;
            }
        }
        map1.area[0].mobs.get(3).isboss = true;
        map1.area[0].mobs.get(3).hp = map1.area[0].mobs.get(3).maxHp = map1.area[0].mobs.get(3).maxHp * 10;
        map1.area[0].mobs.get(5).isboss = true;
        map1.area[0].mobs.get(5).hp = map1.area[0].mobs.get(5).maxHp = map1.area[0].mobs.get(5).maxHp * 10;
        map1.area[0].mobs.get(6).isboss = true;
        map1.area[0].mobs.get(6).hp = map1.area[0].mobs.get(6).maxHp = map1.area[0].mobs.get(6).maxHp * 10;
        map1.area[0].mobs.get(13).isboss = true;
        map1.area[0].mobs.get(13).hp = map1.area[0].mobs.get(13).maxHp = map1.area[0].mobs.get(13).maxHp * 10;

        clan.khiGas[0] = map1;

        mapTemp1 = MapTemplate.getMapTempByIDMap(147); //SA MAC
        map1 = new Map(mapTemp1);
        map1.area[0].updateMobAuto();
        map1.start();
        //INIT HP, DAME MOB
        if (map1.area[0].mobs.size() > 0) {
            for (Mob _mob : map1.area[0].mobs) {
                _mob.maxHp = (int) (_mob.maxHp * 20 * level);
                _mob.hp = _mob.maxHp;
            }
        }
        map1.area[0].mobs.get(3).isboss = true;
        map1.area[0].mobs.get(3).hp = map1.area[0].mobs.get(3).maxHp = map1.area[0].mobs.get(3).maxHp * 10;
        map1.area[0].mobs.get(6).isboss = true;
        map1.area[0].mobs.get(6).hp = map1.area[0].mobs.get(6).maxHp = map1.area[0].mobs.get(6).maxHp * 10;
        map1.area[0].mobs.get(5).isboss = true;
        map1.area[0].mobs.get(5).hp = map1.area[0].mobs.get(5).maxHp = map1.area[0].mobs.get(5).maxHp * 10;
        map1.area[0].mobs.get(13).isboss = true;
        map1.area[0].mobs.get(13).hp = map1.area[0].mobs.get(13).maxHp = map1.area[0].mobs.get(13).maxHp * 10;
        clan.khiGas[1] = map1;

        mapTemp1 = MapTemplate.getMapTempByIDMap(152); //VUNG DAT BANG GIA
        map1 = new Map(mapTemp1);
        map1.area[0].updateMobAuto();
        map1.start();
        //INIT HP, DAME MOB
        if (map1.area[0].mobs.size() > 0) {
            for (Mob _mob : map1.area[0].mobs) {
                _mob.maxHp = (int) (_mob.maxHp * 20 * level);
                _mob.hp = _mob.maxHp;
            }
        }
        map1.area[0].mobs.get(3).isboss = true;
        map1.area[0].mobs.get(3).hp = map1.area[0].mobs.get(3).maxHp = map1.area[0].mobs.get(3).maxHp * 10;
        map1.area[0].mobs.get(6).isboss = true;
        map1.area[0].mobs.get(6).hp = map1.area[0].mobs.get(6).maxHp = map1.area[0].mobs.get(6).maxHp * 10;
        map1.area[0].mobs.get(5).isboss = true;
        map1.area[0].mobs.get(5).hp = map1.area[0].mobs.get(5).maxHp = map1.area[0].mobs.get(5).maxHp * 10;
        map1.area[0].mobs.get(13).isboss = true;
        map1.area[0].mobs.get(13).hp = map1.area[0].mobs.get(13).maxHp = map1.area[0].mobs.get(13).maxHp * 10;
        clan.khiGas[2] = map1;

        mapTemp1 = MapTemplate.getMapTempByIDMap(151); //HANH TINH BONG TOI
        map1 = new Map(mapTemp1);
        map1.area[0].updateMobAuto();
        map1.start();
        //INIT HP, DAME MOB
        if (map1.area[0].mobs.size() > 0) {
            for (Mob _mob : map1.area[0].mobs) {
                _mob.maxHp = (int) (_mob.maxHp * 20 * level);
                _mob.hp = _mob.maxHp;
            }
        }
        map1.area[0].mobs.get(3).isboss = true;
        map1.area[0].mobs.get(3).hp = map1.area[0].mobs.get(3).maxHp = map1.area[0].mobs.get(3).maxHp * 10;
        map1.area[0].mobs.get(6).isboss = true;
        map1.area[0].mobs.get(6).hp = map1.area[0].mobs.get(6).maxHp = map1.area[0].mobs.get(6).maxHp * 10;
        map1.area[0].mobs.get(5).isboss = true;
        map1.area[0].mobs.get(5).hp = map1.area[0].mobs.get(5).maxHp = map1.area[0].mobs.get(5).maxHp * 10;
        map1.area[0].mobs.get(13).isboss = true;
        map1.area[0].mobs.get(13).hp = map1.area[0].mobs.get(13).maxHp = map1.area[0].mobs.get(13).maxHp * 10;
        clan.khiGas[3] = map1;

        mapTemp1 = MapTemplate.getMapTempByIDMap(148); //LAU DAI LYCHEE
        map1 = new Map(mapTemp1);
        map1.area[0].updateMobAuto();
        map1.start();
        //INIT HP, DAME MOB
        if (map1.area[0].mobs.size() > 0) {
            for (Mob _mob : map1.area[0].mobs) {
                _mob.maxHp = (int) (_mob.maxHp * 20 * level);
                _mob.hp = _mob.maxHp;
            }
        }
        map1.area[0].mobs.get(3).isboss = true;
        map1.area[0].mobs.get(3).hp = map1.area[0].mobs.get(3).maxHp = map1.area[0].mobs.get(3).maxHp * 10;
        map1.area[0].mobs.get(6).isboss = true;
        map1.area[0].mobs.get(6).hp = map1.area[0].mobs.get(6).maxHp = map1.area[0].mobs.get(6).maxHp * 10;
        map1.area[0].mobs.get(5).isboss = true;
        map1.area[0].mobs.get(5).hp = map1.area[0].mobs.get(5).maxHp = map1.area[0].mobs.get(5).maxHp * 10;
        map1.area[0].mobs.get(10).isboss = true;
        map1.area[0].mobs.get(10).hp = map1.area[0].mobs.get(10).maxHp = map1.area[0].mobs.get(10).maxHp * 10;
        clan.khiGas[4] = map1;

        //TIME CON LAI
//        Util.log("AJCJKAHCKA :" + level);
        Timer timerConLaiDT = new Timer();
        TimerTask conlaiDT = new TimerTask() {
            public void run() {
                if (clan.openKhiGas) {
                    for (byte i = 0; i < clan.khiGas.length; i++) {
                        if (clan.khiGas[i] != null) {
                            byte size = (byte) (clan.khiGas[i].area[0].players.size());
                            for (byte k = 0; k < size; k++) {
                                if (clan.khiGas[i].area[0].players.get(0) != null) {
                                    clan.khiGas[i].area[0].players.get(0).sendAddchatYellow("Còn " + (30 - (int) ((System.currentTimeMillis() - clan.topenGas) / 60000)) + " phút Khí Ga sẽ kết thúc");
                                }
                            }
                        }
                    }
                } else {
                    timerConLaiDT.cancel();
                }
            }
        ;
        };
        timerConLaiDT.schedule(conlaiDT, 0, 300000);
//        Util.log("AJCJKAHCKA :" + level);
        //END KHI GAS
        Timer timerEndDT = new Timer();
        TimerTask endDT = new TimerTask() {
            public void run() {
                if (clan.openKhiGas) {
                    Server.gI().cKhiGas = (short) (Server.gI().cKhiGas - 1) < (short) 0 ? (short) 0 : (short) (Server.gI().cKhiGas - 1);
                    clan.openKhiGas = false;
                    //DAY HET NGUOI CHOI RA NGOAI
                    for (byte i = 0; i < clan.khiGas.length; i++) {
                        //ALL PLAYER VE TRAM TAU VU TRU
                        if (clan.khiGas[i] != null) {
                            byte size = (byte) (clan.khiGas[i].area[0].players.size());
                            for (byte k = 0; k < size; k++) {
                                if (clan.khiGas[i].area[0].players.get(0) != null) {
                                    clan.khiGas[i].area[0].players.get(0).sendAddchatYellow("Thời gian Khí Ga đã kết thúc");
                                    clan.khiGas[i].area[0].goMapTransport(clan.khiGas[i].area[0].players.get(0), (int) clan.khiGas[i].area[0].players.get(0).gender + 24);
                                }
                            }
                        }
                    }
                    clan.khiGas = null;
                    clan.khiGas = new Map[5];
                    timerEndDT.cancel();
                }
            }
        ;
        };
        timerEndDT.schedule(endDT, 1800000);
        clan.timerGas = timerEndDT;
//        Util.log("AJCJKAHCKA :" + level);
    }

    public void endKhiGas(int idClan) {
        Clan clan = ClanManager.gI().getClanById(idClan);
        if (clan != null) {
            //DAY HET NGUOI CHOI RA NGOAI
            for (byte i = 0; i < clan.khiGas.length; i++) {
                //ALL PLAYER VE TRAM TAU VU TRU
                if (clan.khiGas[i] != null) {
                    byte size = (byte) (clan.khiGas[i].area[0].players.size());
                    for (byte k = 0; k < size; k++) {
                        if (clan.khiGas[i].area[0].players.get(0) != null) {
                            clan.khiGas[i].area[0].players.get(0).sendAddchatYellow("Khí Ga kết thúc sau 30 giây");
                        }
                    }
                }
            }
            Timer timerEndDT = new Timer();
            TimerTask endDT = new TimerTask() {
                public void run() {
                    if (clan.openKhiGas) {
                        Server.gI().cKhiGas = (short) (Server.gI().cKhiGas - 1) < (short) 0 ? (short) 0 : (short) (Server.gI().cKhiGas - 1);
                        clan.openKhiGas = false;
                        //DAY HET NGUOI CHOI RA NGOAI
                        for (byte i = 0; i < clan.khiGas.length; i++) {
                            //ALL PLAYER VE TRAM TAU VU TRU
                            if (clan.khiGas[i] != null) {
                                byte size = (byte) (clan.khiGas[i].area[0].players.size());
                                for (byte k = 0; k < size; k++) {
                                    if (clan.khiGas[i].area[0].players.get(0) != null) {
                                        clan.khiGas[i].area[0].players.get(0).sendAddchatYellow("Thời gian Khí Ga đã kết thúc");
                                        clan.khiGas[i].area[0].goMapTransport(clan.khiGas[i].area[0].players.get(0), (int) clan.khiGas[i].area[0].players.get(0).gender + 24);
                                    }
                                }
                            }
                        }
                        clan.khiGas = null;
                        clan.khiGas = new Map[5];
                        clan.timerGas.cancel();
                    }
                }
            ;
            };
            timerEndDT.schedule(endDT, 30000);
        }
    }

    public void initLychee(Player p) {
        if (p.map.id == 148 && p.zone.chkAllMobDieDT() && p.clan.openKhiGas && p.clan.khiGas[4] != null) {
            Boss _Lychee = new Boss(502, (byte) 42, (short) 592, (short) 480);
            _Lychee.lvGas = p.clan.lvGas;
            _Lychee.hpGoc = _Lychee.hpGoc * _Lychee.lvGas;
            _Lychee.hp = _Lychee.hpGoc;
            _Lychee.hpFull = _Lychee.hpGoc;
            _Lychee.damFull = _Lychee.damFull * _Lychee.lvGas;
            _Lychee.idClan = p.clan.id;

            p.clan.khiGas[4].area[0].bossMap.add(_Lychee);
            p.clan.khiGas[4].area[0].loadBossNoPet(_Lychee);
        }
    }

    //***********************************GIAP LUYEN TAP***********************
    public void upTimeGLT(Player p) {
        if (p.ItemBody[6] != null && p.ItemBody[6].template.type == (byte) 32 && !p.ItemBody[6].maxTimeGLT) {
            if (p.ItemBody[6].timeGLT <= 0) {
                for (ItemOption itemOption : p.ItemBody[6].itemOptions) {
                    if (itemOption.optionTemplate.id == 9) {
                        p.ItemBody[6].timeGLT = (long) (itemOption.param * 60000);
                    }
                }
            }
            if (p.gender == (byte) 1) {
//                p.ItemBody[6].timeGLT += 400;
                p.ItemBody[6].timeGLT += 800;
            } else {
//                p.ItemBody[6].timeGLT += 500;
                p.ItemBody[6].timeGLT += 1000;
            }
            p.ItemBody[6].cUpTimeGLT = (byte) (p.ItemBody[6].cUpTimeGLT + 1) > (byte) 120 ? (byte) 120 : (byte) (p.ItemBody[6].cUpTimeGLT + 1);
//            Util.log("CUPTIME GLT: " + p.ItemBody[6].cUpTimeGLT + ", TIME: " + p.ItemBody[6].timeGLT);
            if (p.ItemBody[6].cUpTimeGLT >= (byte) 120) {
                p.ItemBody[6].cUpTimeGLT = (byte) 0;
                for (ItemOption itemOption : p.ItemBody[6].itemOptions) {
                    if (itemOption.optionTemplate.id == 9) {
                        int timeUp = 0;
                        if (p.ItemBody[6].id == 531 || p.ItemBody[6].id == 536) {
                            timeUp = (int) (p.ItemBody[6].timeGLT / 60000) > 10000 ? 10000 : (int) (p.ItemBody[6].timeGLT / 60000);
                            if (timeUp == 10000) {
                                p.ItemBody[6].maxTimeGLT = true;
                                p.ItemBody[6].timeGLT = (long) (600000000);
                            }
                        } else if (p.ItemBody[6].id == 530 || p.ItemBody[6].id == 535) {
                            timeUp = (int) (p.ItemBody[6].timeGLT / 60000) > 1000 ? 1000 : (int) (p.ItemBody[6].timeGLT / 60000);
                            if (timeUp == 1000) {
                                p.ItemBody[6].maxTimeGLT = true;
                                p.ItemBody[6].timeGLT = (long) (60000000);
                            }
                        } else {
                            timeUp = (int) (p.ItemBody[6].timeGLT / 60000) > 100 ? 100 : (int) (p.ItemBody[6].timeGLT / 60000);
                            if (timeUp == 100) {
                                p.ItemBody[6].maxTimeGLT = true;
                                p.ItemBody[6].timeGLT = (long) (6000000);
                            }
                        }
                        itemOption.param = timeUp;
                        break;
                    }
                }
                refreshItemPlayer(p, (byte) 0, (byte) 6, p.ItemBody[6]);
            }
        }
    }

    public void timeGiapLuyenTap(Player p, byte type) {
        Timer timerGiapLT = new Timer();
        TimerTask giapLT = new TimerTask() {
            public void run() {
                p.giapLuyenTap.timeGLT = (p.giapLuyenTap.timeGLT - 60000) <= 0 ? 0 : (p.giapLuyenTap.timeGLT - 60000);
                int timeDown = (int) (p.giapLuyenTap.timeGLT / 60000) <= 0 ? 0 : (int) (p.giapLuyenTap.timeGLT / 60000);
                for (ItemOption itemOption : p.giapLuyenTap.itemOptions) {
                    if (itemOption.optionTemplate.id == 9) {
                        itemOption.param = timeDown;
                        break;
                    }
                }
                if (p.giapLuyenTap.maxTimeGLT) {
                    p.giapLuyenTap.maxTimeGLT = false;
                }
                for (byte i = 0; i < p.ItemBag.length; i++) {
                    if (p.ItemBag[i] != null && p.ItemBag[i].id != -1 && p.ItemBag[i].template.type == 32 && p.ItemBag[i].useNow) {
                        refreshItemPlayer(p, (byte) 1, i, p.ItemBag[i]);
                        break;
                    }
                }
                if (timeDown <= 0) {
                    p.giapLuyenTap.useNow = false;
                    p.giapLuyenTap = null;
                    p.bonusGLT = (byte) 0;
                    loadPoint(p.session, p);
                    p.timerGLT = null;
                    timerGiapLT.cancel();
                }
            }
        ;
        };
        timerGiapLT.schedule(giapLT, 0, 60000);
        p.timerGLT = timerGiapLT;
    }

    //***********************************TELEPORT BY TAU NGAM******************
    public void transportTauNgam(Player p, short time, byte type) { //type 0 la tau tuonglai, type 1 la tau ngam
        Message m = null;
        try {
            m = new Message(-105);
            m.writer().writeShort(time);
            m.writer().writeByte(type);
            m.writer().flush();
            p.session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void teleportByTauNgam(Player p, int idMap, long time) {
        Timer timerTauNgam = new Timer();
        TimerTask tauNgam = new TimerTask() {
            public void run() {
                if (p.waitTransport) {
                    Map maptele = MainManager.getMapid(idMap);
                    Controller.getInstance().teleportToMAP(p, maptele);
                    if (idMap == 160 && p.taskId == (short) 31 && p.crrTask.index == (byte) 1) {
                        TaskService.gI().updateCountTask(p);
                    }
                    p.timeTauNgam = null;
                }
                timerTauNgam.cancel();
            }
        ;
        };
        timerTauNgam.schedule(tauNgam, time);
        p.timeTauNgam = timerTauNgam;
    }

    //CLIENT INPUT
    public void clientInput(Player p, String title, String text, byte type) {
        Message m = null;
        try {
            m = new Message(-125);
            m.writer().writeUTF(title);
            m.writer().writeByte((byte) 1);
            m.writer().writeUTF(text);
            m.writer().writeByte(type);
            m.writer().flush();
            p.session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    // CHECK NHIEM VU QUA MAP
    public boolean checkTaskGoMap(Player p, int mapid) {
        if (p.taskId < (short) 8 && ((mapid >= 45 && mapid <= 50) || mapid == 12 || mapid == 18)) {
            return false;
        } else if (p.taskId < (short) 9 && !p.zone.MapSoSinh(mapid)) {
            return false;
        } else if (p.taskId < (short) 20 && mapid >= 63) {
            return false;
        } else if (((mapid >= 63 && mapid <= 67) || mapid >= 73) && (p.taskId < (short) 21 || (p.taskId == (short) 21 && p.crrTask.index < (byte) 1))) {
            return false;
        } else if (mapid >= 73 && (p.taskId < (short) 21 || (p.taskId == (short) 21 && p.crrTask.index < (byte) 2))) {
            return false;
        } else if (mapid >= 78 && (p.taskId < (short) 21 || (p.taskId == (short) 21 && p.crrTask.index < (byte) 3))) {
            return false;
        } else if (mapid >= 92 && p.taskId < (short) 23) {
            return false;
        } else if (p.taskId < (short) 26 && mapid >= 97) {
            return false;
        } else if (p.taskId < (short) 27 && mapid >= 97 && mapid != 104) {
            return false;
        } else if (p.taskId < (short) 28 && mapid >= 100 && mapid != 104) {
            return false;
        } else if (p.taskId < (short) 29 && mapid >= 103 && mapid != 104) {
            return false;
        }
        return true;
    }

    //USE ITEM LUC LOGIN
    public void sendItemBuff(Player p) {
        if (p.timerCSKB != null) {
            showIconBuff(p, 2758, p.secondCSKB);
        }
        if (p.timerCN != null) {
            showIconBuff(p, 2754, p.secondCN);
        }
        if (p.timerBH != null) {
            showIconBuff(p, 2755, p.secondBH);
        }
        if (p.timerBK != null) {
            showIconBuff(p, 2756, p.secondBK);
        }
        if (p.timerGX != null) {
            showIconBuff(p, 2757, p.secondGX);
        }
        if (p.timerTM != null) {
            showIconBuff(p, 9068, p.secondTM);
        }
        if (p.timerTA != null) {
            if (p.idTAUse >= 663 && p.idTAUse <= 667) {
                showIconBuff(p, (p.idTAUse + 5661), p.secondTA);
            } else if (p.idTAUse == 465 || p.idTAUse == 466) {
                showIconBuff(p, (p.idTAUse + 3577), p.secondTA);
            } else if (p.idTAUse == 472 || p.idTAUse == 473) {
                showIconBuff(p, (p.idTAUse + 3653), p.secondTA);
            }
//            showIconBuff(p, (p.idTAUse + 5661), p.secondTA);
        }
    }

    public void showIconBuff(Player p, int idIcon, short time) {
        Message m = null;
        try {
            m = new Message(-106);
            m.writer().writeShort((short) idIcon);
            m.writer().writeShort(time);
            m.writer().flush();
            p.session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void itemBuffLogin(Player p, int id, long time) {
        short second = (short) (time / 1000);
        if (p.cItemBuff < (byte) 5) {
            p.cItemBuff = (byte) (p.cItemBuff + 1) > (byte) 5 ? (byte) 5 : (byte) (p.cItemBuff + 1);
            p.idItemBuff.add(id);
            if (id == 379) { //MAY DO CAPSULE
                //            showIconBuff(p, 2758, second);
                p.secondCSKB = second;
                p.useMayDoCapsule = true;
                p.timeEndCSKB = (System.currentTimeMillis() + time);
                // TASK END TDLT NEU HET TIME
                Timer resetMayDo = new Timer();
                TimerTask rsMayDo = new TimerTask() {
                    public void run() {
                        p.cItemBuff = (byte) (p.cItemBuff - 1) < (byte) 0 ? (byte) 0 : (byte) (p.cItemBuff - 1);
                        p.removeIdBuff(id);
                        p.useMayDoCapsule = false;
                        p.timeEndCSKB = 0;
                        p.timerCSKB = null;
                        resetMayDo.cancel();
                    }
                ;
                };
                resetMayDo.schedule(rsMayDo, time);
                p.timerCSKB = resetMayDo;
            } else if (id == 381) { //USE CUONG NO
                //            showIconBuff(p, 2754, second);
                p.secondCN = second;
                p.useCuongNo = true;
                p.timeEndCN = (System.currentTimeMillis() + time);
                if (p.timerCN != null) {
                    p.timerCN.cancel();
                }

                //            Service.gI().loadPoint(p.session, p);
                // TASK END CUONG NO NEU HET TIME
                Timer resetCN = new Timer();
                TimerTask rsCN = new TimerTask() {
                    public void run() {
                        p.cItemBuff = (byte) (p.cItemBuff - 1) < (byte) 0 ? (byte) 0 : (byte) (p.cItemBuff - 1);
                        p.removeIdBuff(id);
                        p.useCuongNo = false;
                        p.timeEndCN = 0;
                        p.timerCN = null;
                        Service.gI().loadPoint(p.session, p);
                        resetCN.cancel();
                    }
                ;
                };
                resetCN.schedule(rsCN, time);
                p.timerCN = resetCN;
                return;
            } else if (id == 382) { //USE BO HUYET
                //            showIconBuff(p, 2755, second);
                p.secondBH = second;
                p.useBoHuyet = true;
                p.timeEndBH = (System.currentTimeMillis() + time);
                if (p.timerBH != null) {
                    p.timerBH.cancel();
                }
                //            Service.gI().loadPoint(p.session, p);
                // TASK END BO HUYET NEU HET TIME
                Timer resetBH = new Timer();
                TimerTask rsBH = new TimerTask() {
                    public void run() {
                        p.cItemBuff = (byte) (p.cItemBuff - 1) < (byte) 0 ? (byte) 0 : (byte) (p.cItemBuff - 1);
                        p.removeIdBuff(id);
                        p.useBoHuyet = false;
                        p.timeEndBH = 0;
                        p.timerBH = null;
                        Service.gI().loadPoint(p.session, p);
                        resetBH.cancel();
                    }
                ;
                };
                resetBH.schedule(rsBH, time);
                p.timerBH = resetBH;
            } else if (id == 383) { //USE BO KHI
                //            showIconBuff(p, 2756, second);
                p.secondBK = second;
                p.useBoKhi = true;
                p.timeEndBK = (System.currentTimeMillis() + time);
                if (p.timerBK != null) {
                    p.timerBK.cancel();
                }
                //            Service.gI().loadPoint(p.session, p);
                // TASK END BO KHI NEU HET TIME
                Timer resetBK = new Timer();
                TimerTask rsBK = new TimerTask() {
                    public void run() {
                        p.cItemBuff = (byte) (p.cItemBuff - 1) < (byte) 0 ? (byte) 0 : (byte) (p.cItemBuff - 1);
                        p.removeIdBuff(id);
                        p.useBoKhi = false;
                        p.timeEndBK = 0;
                        p.timerBK = null;
                        Service.gI().loadPoint(p.session, p);
                        resetBK.cancel();
                    }
                ;
                };
                resetBK.schedule(rsBK, time);
                p.timerBK = resetBK;
            } else if (id == 384) { //USE GIAP XEN
                //            showIconBuff(p, 2757, second);
                p.secondGX = second;
                p.useGiapXen = true;
                p.timeEndGX = (System.currentTimeMillis() + time);
                if (p.timerGX != null) {
                    p.timerGX.cancel();
                }
                // TASK END GIAP XEN NEU HET TIME
                Timer resetGX = new Timer();
                TimerTask rsGX = new TimerTask() {
                    public void run() {
                        p.cItemBuff = (byte) (p.cItemBuff - 1) < (byte) 0 ? (byte) 0 : (byte) (p.cItemBuff - 1);
                        p.removeIdBuff(id);
                        p.useGiapXen = false;
                        p.timeEndGX = 0;
                        p.timerGX = null;
                        resetGX.cancel();
                    }
                ;
                };
                resetGX.schedule(rsGX, time);
                p.timerGX = resetGX;
            } else if (id == 385) { //USE AN DANH TODO:// UPDATE SAU
                //            showIconBuff(p, 2760, second);
            } else if (id == 1016 || id == 1017) { //USE THUOC MO
                //            showIconBuff(p, 9068, second);
                p.secondTM = second;
                p.useThuocMo = true;
                p.timeEndTM = (System.currentTimeMillis() + time);
                if (p.timerTM != null) {
                    p.timerTM.cancel();
                }

                //            Service.gI().loadPoint(p.session, p);
                Timer resetTM = new Timer();
                TimerTask rsTM = new TimerTask() {
                    public void run() {
                        p.cItemBuff = (byte) (p.cItemBuff - 1) < (byte) 0 ? (byte) 0 : (byte) (p.cItemBuff - 1);
                        p.removeIdBuff(id);
                        p.useThuocMo = false;
                        p.timeEndTM = 0;
                        p.timerTM = null;
                        Service.gI().loadPoint(p.session, p);
                        resetTM.cancel();
                    }
                ;
                };
                resetTM.schedule(rsTM, time);
                p.timerTM = resetTM;
            } else if ((id >= 663 && id <= 667) || id == 465 || id == 466 || id == 472 || id == 473) {//USE THUC AN
                //            showIconBuff(p, (id + 5661), second);
                p.secondTA = second;
                p.useThucAn = true;
                p.idTAUse = id;
                p.timeEndTA = (System.currentTimeMillis() + time);
                if (id >= 663 && id <= 667) {
                    if (p.havePet == 1) {
                        p.detu.useThucAn = true;
                    }
                }

                if (p.timerTA != null) {
                    p.timerTA.cancel();
                }

                //            Service.gI().loadPoint(p.session, p);
                Timer resetTA = new Timer();
                TimerTask rsTA = new TimerTask() {
                    public void run() {
                        p.cItemBuff = (byte) (p.cItemBuff - 1) < (byte) 0 ? (byte) 0 : (byte) (p.cItemBuff - 1);
                        p.removeIdBuff(id);
                        p.useThucAn = false;
                        if (p.havePet == 1) {
                            p.detu.useThucAn = false;
                        }
                        p.timerTA = null;
                        Service.gI().loadPoint(p.session, p);
                        resetTA.cancel();
                    }
                ;
                };
                resetTA.schedule(rsTA, time);
                p.timerTA = resetTA;
            }
        }
    }

    public void initPet2(Player p, int id) {
        if (p.pet2Follow == (byte) 1) {
            p.zone.leavePETTT(p.pet);
        }
        p.pet2Follow = (byte) 1;
        short headpet2 = getHeadPet2(id);
        p.pet = new Detu();
        p.pet.initPet(headpet2, (short) (headpet2 + 1), (short) (headpet2 + 2));
        p.pet.id = (-p.id - 300000);
        if (!p.map.MapHome()) {
            LoadPetFollow(p);
        }
        //load point moi cho ME
        loadPoint(p.session, p);
        //send uphp to all player khac trong map
        p.updateHpToPlayerInMap(p, p.hp);

        try {
            p.sortBag();
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateItemBag(p);
        updateItemBody(p);
    }
//    public void loadBossLikePlayer(Boss pl, ArrayList<Player> players) {
//        Message msg = null;
//        try {
//            msg = new Message(-30);
//            msg.writer().writeByte(0);
//            msg.writer().writeInt(pl.id);
//            msg.writer().writeByte(pl.taskId);
//            msg.writer().writeByte(pl.gender);
//            msg.writer().writeShort(pl.BossPartHead());
//            msg.writer().writeUTF(pl.name);
//            msg.writer().writeByte(0);
//            msg.writer().writeByte(0);
//            msg.writer().writeLong(pl.power);
//            msg.writer().writeShort(0);
//            msg.writer().writeShort(0);
//            msg.writer().writeByte(pl.gender);
//            //--------skill---------
//            ArrayList<Skill> skills = pl.skill;
//            msg.writer().writeByte(skills.size());
//            for (Skill skill : skills) {
//                msg.writer().writeShort(skill.skillId);
//            }
//            msg.writer().writeLong(pl.vang);
//            msg.writer().writeInt(pl.ngocKhoa);
//            msg.writer().writeInt(pl.ngoc);
//            msg.writer().writeByte(0);
//            msg.writer().writeByte(0);
//            msg.writer().writeByte(0);
//
//     //       msg.writer().writeShort(252);
//            msg.writer().write(FileIO.readFile("res/cache/head"));
////            msg.writer().writeShort(992);
////            msg.writer().writeShort(8928);
//            //-----------------
//            msg.writer().writeShort(514);
//            msg.writer().writeShort(515);
//            msg.writer().writeShort(537);
//            msg.writer().writeByte(pl.NhapThe);
//            msg.writer().writeInt(1632811835);
//            msg.writer().writeByte(0);
//            for(Player p: players) {
//                p.session.sendMessage(msg);
//            }
//            msg.cleanup();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
