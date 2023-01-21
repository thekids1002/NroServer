package nro.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import nro.item.Item;
import nro.item.ItemOption;
import nro.item.ItemSell;
import nro.item.ItemTemplate;
import nro.player.Player;
import nro.player.Detu;
import nro.player.Boss;
import nro.player.PlayerManger;
import nro.skill.Skill;
import nro.skill.SkillData;
import nro.clan.ClanService;
import nro.main.Controller;
import nro.main.FileIO;
import nro.main.Server;
import nro.main.MainManager;
import nro.main.Service;
import nro.main.Util;
import nro.io.Message;
import nro.io.Session;
import nro.daihoi.DaiHoiService;
import nro.item.ItemBuff;

import nro.task.TaskService;
import nro.task.TaskManager;
import nro.task.ResetHuytSaoTask;
import nro.task.ResetTroiTask;
import nro.task.ResetProtectTask;
import nro.task.ResetBlindTask;
import nro.task.ResetDCTTTask;
import nro.task.ResetSleepTask;
import nro.task.ResetSocolaTask;
import nro.task.ResetDeTrungUpTask;
import nro.task.DelayUseBomTask;

public class Zone {

    public ArrayList<Mob> mobs = new ArrayList();
    public ArrayList<ItemMap> itemsMap = new ArrayList();
    public Map map;
    public ArrayList<Player> players = new ArrayList<>();
//    public ArrayList<Player> players = new ArrayList<>();
    public ArrayList<Detu> pets = new ArrayList<>();
    public ArrayList<Detu> pet2s = new ArrayList<>();
    public ArrayList<Boss> bossMap = new ArrayList<>();
    public Object LOCK = new Object();
    public byte id;
    public boolean haveBROLY = false;
    public boolean isHaveSuperMob = false;
    //CHECK ANDROID
    public boolean isHoiSinhA19 = false;
    public boolean isHoiSinhA15 = false;
    public boolean isHoiSinhBill = false;
    //TIME CAN PICK NGOC RONG SAO DEN
    public long timePickNRSD = 0;
    //TIME HOI SINH MABU
    public long timeMabu12 = 0;

    public void AddItemGa() {
        try {
            for (Player p : players) {
                if (p != null && p.session != null) {
                    if (p.map.id == 21 + p.gender) {
                        ItemMap itemMap = new ItemMap();
                        Item item = new Item();
                        if (p.gender == 2) {
                            itemMap.x = 619;
                            itemMap.y = 325;
                        } else if (p.gender == 1) {
                            itemMap.x = 43;
                            itemMap.y = 320;
                        } else {
                            itemMap.x = 623;
                            itemMap.y = 320;
                        }
                        itemMap.playerId = p.id;
                        itemMap.itemTemplateID = 74;
                        itemMap.item = item;
                        Message m = new Message(68);
                        itemMap.itemMapID = 74;
                        itemsMap.add(itemMap);
                        sendMessage(m);
                        m.writer().writeShort(itemMap.itemMapID);
                        m.writer().writeShort(itemMap.itemTemplateID);
                        m.writer().writeShort(itemMap.x);
                        m.writer().writeShort(itemMap.y);
                        m.writer().writeInt(itemMap.playerId);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DelItemMap(Player p) {
        ArrayList<ItemMap> itemss = new ArrayList<>();
        for (int i = 0; i < itemsMap.size(); i++) {
            if (p.id == itemsMap.get(i).playerId) {
                itemss.add(itemsMap.get(i));
            }
        }
        if (itemss.size() > 0) {
            itemsMap.removeAll(itemss);
        }
    }

    public void updateItemMap() {
        int i;
        ItemMap itemMap;
        for (i = this.itemsMap.size() - 1; i >= 0; i--) {
            itemMap = this.itemsMap.get(i);
            if (itemMap != null) {
                if (itemMap.itemTemplateID != (short) 353 && itemMap.itemTemplateID != (short) 354 && itemMap.itemTemplateID != (short) 355 && itemMap.itemTemplateID != (short) 356 && itemMap.itemTemplateID != (short) 357
                        && itemMap.itemTemplateID != (short) 358 && itemMap.itemTemplateID != (short) 359 && itemMap.itemTemplateID != (short) 372 && itemMap.itemTemplateID != (short) 373 && itemMap.itemTemplateID != (short) 374
                        && itemMap.itemTemplateID != (short) 375 && itemMap.itemTemplateID != (short) 376 && itemMap.itemTemplateID != (short) 377 && itemMap.itemTemplateID != (short) 378 && itemMap.itemTemplateID != (short) 74
                        && itemMap.itemTemplateID != (short) 78) {
                    if (System.currentTimeMillis() >= itemMap.removedelay) {
                        this.removeItemMapMessage(itemMap.itemMapID);
                        this.itemsMap.remove(i);
                        i--;
                    } else if (itemMap.removedelay - System.currentTimeMillis() < 5000L && itemMap.playerId != -1) {
                        itemMap.playerId = -1;
                    }
                }
            }
        }
    }

    public void removeItemMapMessage(int itemmapid) {
        Message m = null;
        try {
            m = new Message(-21);
            m.writer().writeShort((short) itemmapid);
            m.writer().flush();
            this.sendMessage(m);
        } catch (IOException var5) {
            var5.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public Zone(Map map, byte id) {
        this.map = map;
        this.id = id;
    }

    public void leave(Player p) {
//        synchronized (this) {
        if (p.petfucus == 1) {
            p.statusPet = 0;
        }
        //send dang co de trung namek thi xoa chim khoi map
        if (p.chimFollow == (byte) 1) {
            for (byte i = 0; i < players.size(); i++) {
                if (players.get(i) != null && players.get(i).session != null && players.get(i).id != p.id) {
                    useDeTrungForMe(players.get(i), p, (byte) 7);
                }
            }
//                for(Player _p: players) {
//                    if(_p != null && _p.session != null && _p.id != p.id) {
//                        useDeTrungForMe(_p, p, (byte)7);
//                    }
//                }
        }
        if (players.contains(p)) {
            players.remove(p);
            removeMessage(p.id);
            //numplayers--;
        }
//        }
    }

    public void leaveDetu(Player _player, Detu _detu) {
        if (pets.contains(_detu)) {
            pets.remove(_detu);
            try {
                Message m = new Message(-6);
                m.writer().writeInt(_detu.id);
                for (byte i = 0; i < players.size(); i++) {
                    if (players.get(i) != null) {
                        players.get(i).session.sendMessage(m);
                    }
                }
                m.writer().flush();
                m.cleanup();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void leaveDEEEEE(Detu _detu) {
        if (pets.contains(_detu)) {
            pets.remove(_detu);
            try {
                Message m = new Message(-6);
                m.writer().writeInt(_detu.id);
                for (byte i = 0; i < players.size(); i++) {
                    if (players.get(i) != null) {
                        players.get(i).session.sendMessage(m);
                    }
                }
                m.writer().flush();
                m.cleanup();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void leavePETTT(Detu _pet) {
        if (pet2s.contains(_pet)) {
            pet2s.remove(_pet);
            try {
                Message m = new Message(-6);
                m.writer().writeInt(_pet.id);
                for (byte i = 0; i < players.size(); i++) {
                    if (players.get(i) != null) {
                        players.get(i).session.sendMessage(m);
                    }
                }
                m.writer().flush();
                m.cleanup();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeMessage(int id) {
        try {
            Message m = new Message(-6);
            m.writer().writeInt(id);

            sendMessage(m);
            m.writer().flush();
            m.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //item Handler
    public boolean CheckItemIDExist(int itemMapId) {
        for (ItemMap itemMap : itemsMap) {
            if (itemMap.itemMapID == itemMapId) {
                return true;
            }
        }
        return false;
    }

    public void PickItemDrop(Player p, short itemMapId) throws IOException {
        for (int i = 0; i < itemsMap.size(); i++) {
            ItemMap itemPick = itemsMap.get(i);
            if (itemPick.itemMapID == itemMapId) {
                if (itemPick.itemTemplateID == (short) 74) {
                    p.update();
                    Message m = new Message(-20);
                    m.writer().writeShort(itemMapId);
                    m.writer().writeUTF("Bạn vừa ăn Đùi Gà Nướng");
                    p.session.sendMessage(m);
                    m.cleanup();
                    return;
                } else if (itemPick.itemTemplateID == (short) 78) {
                    Message m = new Message(-20);
                    m.writer().writeShort(itemMapId);
                    m.writer().writeUTF("Wow, một đứa bé thật dễ thương!");
                    p.session.sendMessage(m);
                    m.cleanup();
                    if (p.taskId == (short) 3 && p.crrTask.index == (byte) 1) {//NHIEM VU SAO BANG
                        TaskService.gI().updateCountTask(p);
                    }
                    return;
                } else if (itemPick.itemTemplateID == (short) 77) {
                    Message m = new Message(-20);
                    m.writer().writeShort(itemMapId);
                    p.ngoc += 1;
                    Service.gI().buyDone(p);
                    m.writer().writeUTF("Bạn nhận được 1 ngọc xanh");
                    p.session.sendMessage(m);
                    m.cleanup();
                    return;
                } else if (itemPick.itemTemplateID == (short) 861) {
                    Message m = new Message(-20);
                    m.writer().writeShort(itemMapId);
                    p.ngocKhoa += 1;
                    Service.gI().buyDone(p);
                    m.writer().writeUTF("Bạn nhận được 1 hồng ngọc");
                    p.session.sendMessage(m);
                    m.cleanup();
                    return;
                } else {
                    //            Util.log("SO LUONG itEM MAP: " + itemsMap.size());
                    //            for (int i = 0; i < itemsMap.size(); i++) {
                    //                ItemMap itemPick = itemsMap.get(i);
                    //                if (itemPick.itemMapID == itemMapId) {
                    if (itemPick.playerId == p.id || itemPick.playerId == -1) {
                        if (itemPick.item.template.id == 76 || itemPick.item.template.id == 188 || itemPick.item.template.id == 189 || itemPick.item.template.id == 190) { //TRUONG HOP ITEM LA VANG
                            //int rdVang = Util.nextInt(100, 1000);
                            int rdVang = Util.nextInt(20000, 50000);
                            //CHECK SET TANG VANG VA CAI TRANG DRACULA
                            rdVang += (int) (rdVang * Util.getPercentDouble(p.getPercentGold()));
                            //CHECK NOI TAI VANG
                            if (p.noiTai.id != 0 && p.noiTai.idSkill == (byte) (-2)) {
                                rdVang += (int) (rdVang * Util.getPercentDouble((int) p.noiTai.param));
                            }
                            //END NOI TAI TANG VANG
                            p.vang = ((long) (p.vang + rdVang) > 2000000000L ? 2000000000L : (long) (p.vang + rdVang));

                            itemsMap.remove(i);
                            Message m = new Message(-20);
                            m.writer().writeShort(itemMapId);
                            m.writer().writeUTF("");
                            m.writer().writeShort(rdVang);
                            m.writer().writeShort(rdVang);
                            p.session.sendMessage(m);

                            m = new Message(-19);
                            m.writer().writeShort(itemPick.itemMapID);
                            m.writer().writeInt(p.id);
                            sendMyMessage(p, m);
                            m.cleanup();
                            Service.gI().buyDone(p);
                        } else if (itemPick.item.template.id == 516) { //SOCOLA NAMEK
                            p.hp = p.getHpFull();
                            p.mp = p.getMpFull();
                            postUpdateHP(p);
                            postUpdateKI(p);

                            itemsMap.remove(i);
                            Message m = new Message(-20);
                            m.writer().writeShort(itemMapId);
                            m.writer().writeUTF("");
                            p.session.sendMessage(m);
                            m = new Message(-19);
                            m.writer().writeShort(itemPick.itemMapID);
                            m.writer().writeInt(p.id);
                            sendMyMessage(p, m);
                            m.cleanup();
                        } else { //CAC ITEM CON LAI
                            if (p.addItemToBag(itemPick.item)) {
                                //CHECK NHAT VAT PHAM NHIEM VU
                                if (p.taskId == (short) 2 && p.crrTask.index == (byte) 0 && itemPick.item.template.id == 73) { //NHIEM VU DUI GA
                                    TaskService.gI().updateCountTask(p);
                                } else if (p.taskId == (short) 3 && p.crrTask.index == (byte) 1 && itemPick.item.template.id == 78) {//NHIEM VU SAO BANG
                                    p.sendAddchatYellow("Wow, một đứa bé thật dễ thương!");
                                    TaskService.gI().updateCountTask(p);
                                } else if (p.taskId == (short) 29 && p.crrTask.index == (byte) 1 && itemPick.item.template.id == 380) { //NHIEM VU NHAT CAPSULE KI BI
                                    TaskService.gI().updateCountTask(p);
                                } else if (itemPick.itemTemplateID >= (short) 555 && itemPick.itemTemplateID <= (short) 567) {
                                    Service.gI().sendThongBaoServer(p.name + " Vừa nhặt được " + itemPick.item.template.name + " tại " + map.template.name);
                                }
                                itemsMap.remove(i);
                                Message m = new Message(-20);
                                m.writer().writeShort(itemMapId);
                                m.writer().writeUTF("");
                                p.session.sendMessage(m);
                                m = new Message(-19);
                                m.writer().writeShort(itemPick.itemMapID);
                                m.writer().writeInt(p.id);
                                sendMyMessage(p, m);
                                m.cleanup();
                            }
                            p.updateItemBag();
                        }
                    } else {
                        p.sendAddchatYellow("Không thể nhặt vật phẩm của người khác");
                    }
//                            break;
                    //                }
                    //            }
                }
                break;
            }
        }
    }

    public void updateIDItemMap() {
        for (int i = 0; i < itemsMap.size(); i++) {
            itemsMap.get(i).itemMapID = i;
        }
    }

    public int getItemMapID() {
        for (int i = 0; i < itemsMap.size(); i++) {
            if (!CheckItemIDExist(i)) {
                return i;
            }
        }
        return -1;
    }

    public ItemMap getItemMapByID(int itemMapId) {
        for (int i = 0; i < itemsMap.size(); i++) {
            if (itemsMap.get(i).itemMapID == itemMapId) {
                return itemsMap.get(i);
            }
        }
        return null;
    }

    public void removeItemDrop(Player p, int itemMapId) {
        try {
            for (int i = 0; i < itemsMap.size(); i++) {
                if (itemsMap.get(i).itemMapID == itemMapId) {
                    Message m = new Message(-20);
                    m.writer().writeShort((short) itemMapId);
                    m.writer().writeUTF("");
                    p.session.sendMessage(m);
                    m = new Message(-19);
                    m.writer().writeShort((short) itemsMap.get(i).itemMapID);
                    m.writer().writeInt(p.id);
                    sendMyMessage(p, m);
                    m.cleanup();
                    itemsMap.remove(i);
                    break;
                }
            }
        } catch (Exception e) {
        }
    }

    public void removeItemNOW(short itemmapid) {
        Message m = null;
        try {
            m = new Message(-21);
            m.writer().writeShort(itemmapid);
            m.writer().flush();
//            for(Player p: this.players) {
//                if(p != null && p.session != null) {
//                    p.session.sendMessage(m);
//                }
//            }
            for (byte i = 0; i < this.players.size(); i++) {
                if (this.players.get(i) != null) {
                    this.players.get(i).session.sendMessage(m);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void PlayerDropItem(Player p, Item item) {
        try {
            ItemMap itemMap = new ItemMap();
            itemMap.x = p.x;
            itemMap.y = p.y;
            itemMap.playerId = p.id;
            itemMap.itemTemplateID = (short) item.template.id;
            itemMap.item = item;
            Message m = new Message(68);
            for (int i = 0; i < itemsMap.size() + 1; i++) {
                if (!CheckItemIDExist(i)) {
                    itemMap.itemMapID = i;
                    itemsMap.add(itemMap);
                    sendMessage(m);
                    break;
                }
            }
            m.writer().writeShort(itemMap.itemMapID);
            m.writer().writeShort(itemMap.itemTemplateID);
            m.writer().writeShort(itemMap.x);
            m.writer().writeShort(itemMap.y);
            m.writer().writeInt(itemMap.playerId);

        } catch (Exception e) {
        }
    }

    //---
    public void VGo(Player p, Message m, Map mapold) throws IOException {
        m.cleanup();
        for (byte i = 0; i < map.template.wayPoints.length; i++) {
            WayPoint vg = map.template.wayPoints[i];

            if (p.x + 100 >= vg.minX && p.x <= vg.maxX + 100 && p.y + 100 >= vg.minY && p.y <= vg.maxY + 100) {
                int mapid;
                mapid = vg.goMap;
                //DANG DEO NGOC RONG NAMEK THI ROT
                if (mapid != 7 && mapid != 8 && mapid != 9 && mapid != 10 && mapid != 11 && mapid != 12 && mapid != 13 && mapid != 31 && mapid != 32 && mapid != 33
                        && mapid != 34 && mapid != 43) {
                    Service.gI().dropDragonBall(p);
//                    if(p.imgNRSD == (byte)53 && p.nrNamec != 0) {
//                        //NEU LA NGOC RONG 1 SAO THI RESET CAN CALL
//                        if(p.nrNamec == 353) {
//                            Server.gI().firstNrNamec = true;
//                            Server.gI().timeNrNamec = 0;
//                        }
//                        p.imgNRSD = (byte)0;
//                        p.zone.resetBagClan(p);
//                        ItemMap itemM = p.zone.createNewItemMap(p.nrNamec, -1, p.x, p.y);
//                        Server.gI().pNrNamec[(int)(p.nrNamec - 353)] = "";
//                        Server.gI().idpNrNamec[(int)(p.nrNamec - 353)] = -1;
//                        p.nrNamec = 0;
//                        //UPDATE TYKE PK
//                        p.typePk = (byte)0;
//                        Service.gI().setTypePK(p, (byte)0);
//                        p.zone.addItemToMap(itemM, -1, p.x, p.y);
//                    }
                }
                if (mapid >= 53 && mapid <= 62 && p.clan != null && p.clan.openDoanhTrai) {
                    if (mapid == 53) {
                        if (p.clan.openDoanhTrai && p.clan.doanhTrai[0] != null) { //NEU CO MAP 1 THI DUOC QUA LUON
                            if (p.petfucus == 1) {
                                leaveDetu(p, p.detu);
                            }
                            if (p.pet2Follow == 1 && p.pet != null) {
                                p.zone.leavePETTT(p.pet);
                            }
                            leave(p);
                            if (map.id == 58) {
                                p.x = (short) 1627;
                                p.y = (short) 432;
                            } else {
                                p.x = (short) 1627;
                                p.y = (short) 432;
                            }
                            p.clan.doanhTrai[0].area[0].Enter(p);
                            return;
                        }
                    } else if (mapid == 58) {
                        if (p.clan.openDoanhTrai && p.clan.doanhTrai[1] != null) { //NEU CO MAP 2 THI DUOC QUA LUON
                            if (p.petfucus == 1) {
                                leaveDetu(p, p.detu);
                            }
                            if (p.pet2Follow == 1 && p.pet != null) {
                                p.zone.leavePETTT(p.pet);
                            }
                            leave(p);
                            if (map.id == 53) {
                                p.x = (short) 60;
                                p.y = (short) 432;
                            } else if (map.id == 59) {
                                p.x = (short) 1631;
                                p.y = (short) 264;
                            }
                            //INIT HP, DAME MOB
                            int hpMobNew = p.getDamFull() * 6;
                            if (p.clan.doanhTrai[1].area[0].mobs.size() > 0) {
                                if (hpMobNew > p.clan.doanhTrai[1].area[0].mobs.get(0).maxHp) {
                                    for (Mob _mob : p.clan.doanhTrai[1].area[0].mobs) {
                                        _mob.maxHp = hpMobNew;
                                        _mob.hp = _mob.maxHp;
                                    }
                                }
                            }
                            p.clan.doanhTrai[1].area[0].Enter(p);
                            return;
                        } else {
                            if (chkAllMobDieDT()) {
                                Service.gI().initMapDoanhTrai(p.clan, 58, (byte) 1);
                                if (p.petfucus == 1) {
                                    leaveDetu(p, p.detu);
                                }
                                if (p.pet2Follow == 1 && p.pet != null) {
                                    p.zone.leavePETTT(p.pet);
                                }
                                leave(p);
                                p.x = (short) 60;
                                p.y = (short) 432;

                                //INIT HP, DAME MOB
                                if (p.clan.doanhTrai[1].area[0].mobs.size() > 0) {
                                    for (Mob _mob : p.clan.doanhTrai[1].area[0].mobs) {
                                        _mob.maxHp = p.getDamFull() * 6;
                                        _mob.hp = _mob.maxHp;
                                    }
                                }
                                p.clan.doanhTrai[1].area[0].Enter(p);
                                return;
                            }
                        }
                    } else if (mapid == 59) {
                        if (p.clan.openDoanhTrai && p.clan.doanhTrai[2] != null) { //NEU CO MAP 3 THI DUOC QUA LUON
                            if (p.petfucus == 1) {
                                leaveDetu(p, p.detu);
                            }
                            if (p.pet2Follow == 1 && p.pet != null) {
                                p.zone.leavePETTT(p.pet);
                            }
                            leave(p);
                            if (map.id == 58) {
                                p.x = (short) 45;
                                p.y = (short) 240;
                            } else if (map.id == 60) {
                                p.x = (short) 1629;
                                p.y = (short) 312;
                            }

                            //INIT HP, DAME TRUNG UY TRANG
                            Boss _TUT = p.clan.doanhTrai[2].area[0].getBossByType((byte) 31);
                            if (_TUT != null) {
                                int hpTUTNew = p.getDamFull() * 30;
                                if (hpTUTNew > _TUT.hp) {
                                    _TUT.hp = hpTUTNew;
                                }
                                int dameTUTNew = (int) (p.getHpFull() * 0.05);
                                if (dameTUTNew > _TUT.damGoc) {
                                    _TUT.damGoc = dameTUTNew;
                                    _TUT.damFull = dameTUTNew;
                                }
                                //INIT HP, DAME MOB
                                int hpMobNew = p.getDamFull() * 6;
                                if (p.clan.doanhTrai[2].area[0].mobs.size() > 0) {
                                    if (hpMobNew > p.clan.doanhTrai[2].area[0].mobs.get(0).maxHp) {
                                        for (Mob _mob : p.clan.doanhTrai[2].area[0].mobs) {
                                            _mob.maxHp = hpMobNew;
                                            _mob.hp = _mob.maxHp;
                                        }
                                    }
                                }
                            }
                            p.clan.doanhTrai[2].area[0].Enter(p);
                            return;
                        } else {
                            if (chkAllMobDieDT()) {
                                Service.gI().initMapDoanhTrai(p.clan, 59, (byte) 2);
                                //INIT HP, DAME TRUNG UY TRANG
                                Boss _TUT = p.clan.doanhTrai[2].area[0].getBossByType((byte) 31);
                                _TUT.hp = p.getDamFull() * 30;
                                int hpChar = p.getHpFull();
                                if (hpChar > 10000) {
                                    _TUT.damGoc = (int) (p.getHpFull() * 0.05);
                                } else {
                                    _TUT.damGoc = 500;
                                }
                                _TUT.damFull = _TUT.damGoc;

                                if (p.petfucus == 1) {
                                    leaveDetu(p, p.detu);
                                }
                                if (p.pet2Follow == 1 && p.pet != null) {
                                    p.zone.leavePETTT(p.pet);
                                }
                                leave(p);
                                p.x = (short) 45;
                                p.y = (short) 240;
                                //INIT HP, DAME MOB
                                if (p.clan.doanhTrai[2].area[0].mobs.size() > 0) {
                                    for (Mob _mob : p.clan.doanhTrai[2].area[0].mobs) {
                                        _mob.maxHp = p.getDamFull() * 6;
                                        _mob.hp = _mob.maxHp;
                                    }
                                }
                                p.clan.doanhTrai[2].area[0].Enter(p);
                                return;
                            }
                        }
                    } else if (mapid == 60) {
                        if (p.clan.openDoanhTrai && p.clan.doanhTrai[3] != null) { //NEU CO MAP 4 THI DUOC QUA LUON
                            if (p.petfucus == 1) {
                                leaveDetu(p, p.detu);
                            }
                            if (p.pet2Follow == 1 && p.pet != null) {
                                p.zone.leavePETTT(p.pet);
                            }
                            leave(p);
                            if (map.id == 59) {
                                p.x = (short) 47;
                                p.y = (short) 384;
                            } else if (map.id == 61) {
                                p.x = (short) 1630;
                                p.y = (short) 384;
                            }
                            //INIT HP, DAME MOB
                            int hpMobNew = p.getDamFull() * 6;
                            if (p.clan.doanhTrai[3].area[0].mobs.size() > 0) {
                                if (hpMobNew > p.clan.doanhTrai[3].area[0].mobs.get(0).maxHp) {
                                    for (Mob _mob : p.clan.doanhTrai[3].area[0].mobs) {
                                        _mob.maxHp = hpMobNew;
                                        _mob.hp = _mob.maxHp;
                                    }
                                }
                            }
                            p.clan.doanhTrai[3].area[0].Enter(p);
                            return;
                        } else {
                            if (chkAllMobDieDT()) {
                                Service.gI().initMapDoanhTrai(p.clan, 60, (byte) 3);
                                if (p.petfucus == 1) {
                                    leaveDetu(p, p.detu);
                                }
                                if (p.pet2Follow == 1 && p.pet != null) {
                                    p.zone.leavePETTT(p.pet);
                                }
                                leave(p);
                                p.x = (short) 47;
                                p.y = (short) 384;
                                //INIT HP, DAME MOB
                                if (p.clan.doanhTrai[3].area[0].mobs.size() > 0) {
                                    for (Mob _mob : p.clan.doanhTrai[3].area[0].mobs) {
                                        _mob.maxHp = p.getDamFull() * 6;
                                        _mob.hp = _mob.maxHp;
                                    }
                                }
                                p.clan.doanhTrai[3].area[0].Enter(p);
                                return;
                            }
                        }
                    } else if (mapid == 61) {
                        if (p.clan.openDoanhTrai && p.clan.doanhTrai[4] != null) {
                            if (p.petfucus == 1) {
                                leaveDetu(p, p.detu);
                            }
                            if (p.pet2Follow == 1 && p.pet != null) {
                                p.zone.leavePETTT(p.pet);
                            }
                            leave(p);
                            if (map.id == 60) {
                                p.x = (short) 63;
                                p.y = (short) 384;
                            } else if (map.id == 62) {
                                p.x = (short) 1631;
                                p.y = (short) 384;
                            }
                            //INIT HP, DAME MOB
                            int hpMobNew = p.getDamFull() * 6;
                            if (p.clan.doanhTrai[4].area[0].mobs.size() > 0) {
                                if (hpMobNew > p.clan.doanhTrai[4].area[0].mobs.get(0).maxHp) {
                                    for (Mob _mob : p.clan.doanhTrai[4].area[0].mobs) {
                                        _mob.maxHp = hpMobNew;
                                        _mob.hp = _mob.maxHp;
                                    }
                                }
                            }
                            p.clan.doanhTrai[4].area[0].Enter(p);
                            return;
                        } else {
                            if (chkAllMobDieDT()) {
                                Service.gI().initMapDoanhTrai(p.clan, 61, (byte) 4);

                                if (p.petfucus == 1) {
                                    leaveDetu(p, p.detu);
                                }
                                if (p.pet2Follow == 1 && p.pet != null) {
                                    p.zone.leavePETTT(p.pet);
                                }
                                leave(p);
                                p.x = (short) 63;
                                p.y = (short) 384;
                                //INIT HP, DAME MOB
                                if (p.clan.doanhTrai[4].area[0].mobs.size() > 0) {
                                    for (Mob _mob : p.clan.doanhTrai[4].area[0].mobs) {
                                        _mob.maxHp = p.getDamFull() * 6;
                                        _mob.hp = _mob.maxHp;
                                    }
                                }
                                p.clan.doanhTrai[4].area[0].Enter(p);
                                return;
                            }
                        }
                    } else if (mapid == 62) {
                        if (p.clan.openDoanhTrai && p.clan.doanhTrai[5] != null) {
                            if (p.petfucus == 1) {
                                leaveDetu(p, p.detu);
                            }
                            if (p.pet2Follow == 1 && p.pet != null) {
                                p.zone.leavePETTT(p.pet);
                            }
                            leave(p);
                            if (map.id == 61) {
                                p.x = (short) 63;
                                p.y = (short) 384;
                            } else if (map.id == 55) {
                                p.x = (short) 1399;
                                p.y = (short) 384;
                            }

                            //INIT HP, DAME TRUNG UY XANH LO
                            Boss _TUT = p.clan.doanhTrai[5].area[0].getBossByType((byte) 32);
                            if (_TUT != null) {
                                int hpTUTNew = p.getDamFull() * 30;
                                if (hpTUTNew > _TUT.hp) {
                                    _TUT.hp = hpTUTNew;
                                }
                                int dameTUTNew = (int) (p.getHpFull() * 0.06);
                                if (dameTUTNew > _TUT.damGoc) {
                                    _TUT.damGoc = dameTUTNew;
                                    _TUT.damFull = dameTUTNew;
                                }
                                //INIT HP, DAME MOB
                                int hpMobNew = p.getDamFull() * 6;
                                if (p.clan.doanhTrai[5].area[0].mobs.size() > 0) {
                                    if (hpMobNew > p.clan.doanhTrai[5].area[0].mobs.get(0).maxHp) {
                                        for (Mob _mob : p.clan.doanhTrai[5].area[0].mobs) {
                                            _mob.maxHp = hpMobNew;
                                            _mob.hp = _mob.maxHp;
                                        }
                                    }
                                }
                            }
                            p.clan.doanhTrai[5].area[0].Enter(p);
                            return;
                        } else {
                            if (chkAllMobDieDT()) {
                                Service.gI().initMapDoanhTrai(p.clan, 62, (byte) 5);
                                //INIT HP, DAME TRUNG UY XANH LO
                                Boss _TUT = p.clan.doanhTrai[5].area[0].getBossByType((byte) 32);
                                _TUT.hp = p.getDamFull() * 30;
                                int hpChar = p.getHpFull();
                                if (hpChar > 10000) {
                                    _TUT.damGoc = (int) (p.getHpFull() * 0.06);
                                } else {
                                    _TUT.damGoc = 500;
                                }
                                _TUT.damFull = _TUT.damGoc;

                                if (p.petfucus == 1) {
                                    leaveDetu(p, p.detu);
                                }
                                if (p.pet2Follow == 1 && p.pet != null) {
                                    p.zone.leavePETTT(p.pet);
                                }
                                leave(p);
                                p.x = (short) 63;
                                p.y = (short) 384;
                                //INIT HP, DAME MOB
                                if (p.clan.doanhTrai[5].area[0].mobs.size() > 0) {
                                    for (Mob _mob : p.clan.doanhTrai[5].area[0].mobs) {
                                        _mob.maxHp = p.getDamFull() * 6;
                                        _mob.hp = _mob.maxHp;
                                    }
                                }
                                p.clan.doanhTrai[5].area[0].Enter(p);
                                return;
                            }
                        }
                    } else if (mapid == 55) {
                        if (p.clan.openDoanhTrai && p.clan.doanhTrai[6] != null) {
                            if (p.petfucus == 1) {
                                leaveDetu(p, p.detu);
                            }
                            if (p.pet2Follow == 1 && p.pet != null) {
                                p.zone.leavePETTT(p.pet);
                            }
                            leave(p);
                            if (map.id == 62) {
                                p.x = (short) 64;
                                p.y = (short) 312;
                            } else if (map.id == 56) {
                                p.x = (short) 1536;
                                p.y = (short) 312;
                            }
                            //INIT HP, DAME TRUNG UY THEP
                            Boss _TUT = p.clan.doanhTrai[6].area[0].getBossByType((byte) 33);
                            if (_TUT != null) {
                                int hpTUTNew = p.getDamFull() * 30;
                                if (hpTUTNew > _TUT.hp) {
                                    _TUT.hp = hpTUTNew;
                                }
                                int dameTUTNew = (int) (p.getHpFull() * 0.06);
                                if (dameTUTNew > _TUT.damGoc) {
                                    _TUT.damGoc = dameTUTNew;
                                    _TUT.damFull = dameTUTNew;
                                }
                                //INIT HP, DAME MOB
                                int hpMobNew = p.getDamFull() * 6;
                                if (p.clan.doanhTrai[6].area[0].mobs.size() > 0) {
                                    if (hpMobNew > p.clan.doanhTrai[6].area[0].mobs.get(0).maxHp) {
                                        for (Mob _mob : p.clan.doanhTrai[6].area[0].mobs) {
                                            _mob.maxHp = hpMobNew;
                                            _mob.hp = _mob.maxHp;
                                        }
                                    }
                                }
                            }
                            p.clan.doanhTrai[6].area[0].Enter(p);
                            return;
                        } else {
                            if (chkAllMobDieDT()) {
                                Service.gI().initMapDoanhTrai(p.clan, 55, (byte) 6);

                                //INIT HP, DAME TRUNG UY THEP
                                Boss _TUT = p.clan.doanhTrai[6].area[0].getBossByType((byte) 33);
                                _TUT.hp = p.getDamFull() * 30;
                                int hpChar = p.getHpFull();
                                if (hpChar > 10000) {
                                    _TUT.damGoc = (int) (p.getHpFull() * 0.06);
                                } else {
                                    _TUT.damGoc = 500;
                                }
                                _TUT.damFull = _TUT.damGoc;

                                if (p.petfucus == 1) {
                                    leaveDetu(p, p.detu);
                                }
                                if (p.pet2Follow == 1 && p.pet != null) {
                                    p.zone.leavePETTT(p.pet);
                                }
                                leave(p);
                                p.x = (short) 64;
                                p.y = (short) 312;
                                //INIT HP, DAME MOB
                                if (p.clan.doanhTrai[6].area[0].mobs.size() > 0) {
                                    for (Mob _mob : p.clan.doanhTrai[6].area[0].mobs) {
                                        _mob.maxHp = p.getDamFull() * 6;
                                        _mob.hp = _mob.maxHp;
                                    }
                                }
                                p.clan.doanhTrai[6].area[0].Enter(p);
                                return;
                            }
                        }
                    } else if (mapid == 56) {
                        if (p.clan.openDoanhTrai && p.clan.doanhTrai[7] != null) {
                            if (p.petfucus == 1) {
                                leaveDetu(p, p.detu);
                            }
                            if (p.pet2Follow == 1 && p.pet != null) {
                                p.zone.leavePETTT(p.pet);
                            }
                            leave(p);
                            if (map.id == 55) {
                                p.x = (short) 1532;
                                p.y = (short) 312;
                            } else if (map.id == 54) {
                                p.x = (short) 109;
                                p.y = (short) 312;
                            }
                            //INIT HP, DAME MOB
                            int hpMobNew = p.getDamFull() * 6;
                            if (p.clan.doanhTrai[7].area[0].mobs.size() > 0) {
                                if (hpMobNew > p.clan.doanhTrai[7].area[0].mobs.get(0).maxHp) {
                                    for (Mob _mob : p.clan.doanhTrai[7].area[0].mobs) {
                                        _mob.maxHp = hpMobNew;
                                        _mob.hp = _mob.maxHp;
                                    }
                                }
                            }
                            p.clan.doanhTrai[7].area[0].Enter(p);
                            return;
                        } else {
                            if (chkAllMobDieDT()) {
                                Service.gI().initMapDoanhTrai(p.clan, 56, (byte) 7);

                                if (p.petfucus == 1) {
                                    leaveDetu(p, p.detu);
                                }
                                if (p.pet2Follow == 1 && p.pet != null) {
                                    p.zone.leavePETTT(p.pet);
                                }
                                leave(p);
                                p.x = (short) 1532;
                                p.y = (short) 312;
                                //INIT HP, DAME MOB
                                if (p.clan.doanhTrai[7].area[0].mobs.size() > 0) {
                                    for (Mob _mob : p.clan.doanhTrai[7].area[0].mobs) {
                                        _mob.maxHp = p.getDamFull() * 6;
                                        _mob.hp = _mob.maxHp;
                                    }
                                }
                                p.clan.doanhTrai[7].area[0].Enter(p);
                                return;
                            }
                        }
                    } else if (mapid == 54) {
                        if (p.clan.openDoanhTrai && p.clan.doanhTrai[8] != null) {
                            if (p.petfucus == 1) {
                                leaveDetu(p, p.detu);
                            }
                            if (p.pet2Follow == 1 && p.pet != null) {
                                p.zone.leavePETTT(p.pet);
                            }
                            leave(p);
                            if (map.id == 56) {
                                p.x = (short) 140;
                                p.y = (short) 312;
                            } else if (map.id == 57) {
                                p.x = (short) 1312;
                                p.y = (short) 312;
                            }

                            //INIT HP, DAME NINJA AO TIM
                            Boss _TUT = p.clan.doanhTrai[8].area[0].getBossByType((byte) 34);
                            if (_TUT != null) {
                                int hpTUTNew = p.getDamFull() * 30;
                                if (hpTUTNew > _TUT.hp) {
                                    _TUT.hp = hpTUTNew;
                                }
                                int dameTUTNew = (int) (p.getHpFull() * 0.06);
                                if (dameTUTNew > _TUT.damGoc) {
                                    _TUT.damGoc = dameTUTNew;
                                    _TUT.damFull = dameTUTNew;
                                }
                            }
                            p.clan.doanhTrai[8].area[0].Enter(p);
                            return;
                        } else {
                            if (chkAllMobDieDT()) {
                                Service.gI().initMapDoanhTrai(p.clan, 54, (byte) 8);

                                //INIT HP, DAME NINJA AO TIM
                                Boss _TUT = p.clan.doanhTrai[8].area[0].getBossByType((byte) 34);
                                _TUT.hp = p.getDamFull() * 30;
                                int hpChar = p.getHpFull();
                                if (hpChar > 10000) {
                                    _TUT.damGoc = (int) (p.getHpFull() * 0.06);
                                } else {
                                    _TUT.damGoc = 500;
                                }
                                _TUT.damFull = _TUT.damGoc;

                                if (p.petfucus == 1) {
                                    leaveDetu(p, p.detu);
                                }
                                if (p.pet2Follow == 1 && p.pet != null) {
                                    p.zone.leavePETTT(p.pet);
                                }
                                leave(p);
                                p.x = (short) 140;
                                p.y = (short) 312;
                                p.clan.doanhTrai[8].area[0].Enter(p);
                                return;
                            }
                        }
                    } else if (mapid == 57) {
                        if (p.clan.openDoanhTrai && p.clan.doanhTrai[9] != null) {
                            if (p.petfucus == 1) {
                                leaveDetu(p, p.detu);
                            }
                            if (p.pet2Follow == 1 && p.pet != null) {
                                p.zone.leavePETTT(p.pet);
                            }
                            leave(p);
                            if (map.id == 54) {
                                p.x = (short) 1527;
                                p.y = (short) 312;
                            }
                            //INIT HP, DAME NINJA AO TIM
                            Boss _TUT = p.clan.doanhTrai[9].area[0].getBossByID(-200158);
                            Boss _TUT2 = p.clan.doanhTrai[9].area[0].getBossByID(-200159);
                            Boss _TUT3 = p.clan.doanhTrai[9].area[0].getBossByID(-200160);
                            Boss _TUT4 = p.clan.doanhTrai[9].area[0].getBossByID(-200161);
                            int hpTUTNew = p.getDamFull() * 30;
                            int dameTUTNew = (int) (p.getHpFull() * 0.06);
                            if (_TUT != null) {
                                if (hpTUTNew > _TUT.hp) {
                                    _TUT.hp = hpTUTNew;
                                }
                                if (dameTUTNew > _TUT.damGoc) {
                                    _TUT.damGoc = dameTUTNew;
                                    _TUT.damFull = dameTUTNew;
                                }
                            }
                            if (_TUT2 != null) {
                                if (hpTUTNew > _TUT2.hp) {
                                    _TUT2.hp = hpTUTNew;
                                }
                                if (dameTUTNew > _TUT2.damGoc) {
                                    _TUT2.damGoc = dameTUTNew;
                                    _TUT2.damFull = dameTUTNew;
                                }
                            }
                            if (_TUT3 != null) {
                                if (hpTUTNew > _TUT3.hp) {
                                    _TUT3.hp = hpTUTNew;
                                }
                                if (dameTUTNew > _TUT3.damGoc) {
                                    _TUT3.damGoc = dameTUTNew;
                                    _TUT3.damFull = dameTUTNew;
                                }
                            }
                            if (_TUT4 != null) {
                                if (hpTUTNew > _TUT4.hp) {
                                    _TUT4.hp = hpTUTNew;
                                }
                                if (dameTUTNew > _TUT4.damGoc) {
                                    _TUT4.damGoc = dameTUTNew;
                                    _TUT4.damFull = dameTUTNew;
                                }
                            }
                            //INIT HP, DAME MOB
                            int hpMobNew = p.getDamFull() * 6;
                            if (p.clan.doanhTrai[9].area[0].mobs.size() > 0) {
                                if (hpMobNew > p.clan.doanhTrai[9].area[0].mobs.get(0).maxHp) {
                                    for (Mob _mob : p.clan.doanhTrai[9].area[0].mobs) {
                                        _mob.maxHp = hpMobNew;
                                        _mob.hp = _mob.maxHp;
                                    }
                                }
                            }
                            p.clan.doanhTrai[9].area[0].Enter(p);
                            return;
                        } else {
                            if (chkAllMobDieDT()) {
                                Service.gI().initMapDoanhTrai(p.clan, 57, (byte) 9);

                                //INIT HP, DAME NINJA AO TIM
                                Boss _TUT = p.clan.doanhTrai[9].area[0].getBossByID(-200158);
                                Boss _TUT2 = p.clan.doanhTrai[9].area[0].getBossByID(-200159);
                                Boss _TUT3 = p.clan.doanhTrai[9].area[0].getBossByID(-200160);
                                Boss _TUT4 = p.clan.doanhTrai[9].area[0].getBossByID(-200161);
                                _TUT.hp = p.getDamFull() * 30;
                                _TUT2.hp = _TUT.hp;
                                _TUT3.hp = _TUT.hp;
                                _TUT4.hp = _TUT.hp;
                                int hpChar = p.getHpFull();
                                if (hpChar > 10000) {
                                    _TUT.damGoc = (int) (p.getHpFull() * 0.06);
                                    _TUT2.damGoc = _TUT.damGoc;
                                    _TUT3.damGoc = _TUT.damGoc;
                                    _TUT4.damGoc = _TUT.damGoc;
                                } else {
                                    _TUT.damGoc = 500;
                                    _TUT2.damGoc = _TUT.damGoc;
                                    _TUT3.damGoc = _TUT.damGoc;
                                    _TUT4.damGoc = _TUT.damGoc;
                                }
                                _TUT.damFull = _TUT.damGoc;
                                _TUT2.damFull = _TUT.damGoc;
                                _TUT3.damFull = _TUT.damGoc;
                                _TUT4.damFull = _TUT.damGoc;
                                if (p.petfucus == 1) {
                                    leaveDetu(p, p.detu);
                                }
                                if (p.pet2Follow == 1 && p.pet != null) {
                                    p.zone.leavePETTT(p.pet);
                                }
                                leave(p);
                                p.x = (short) 1527;
                                p.y = (short) 312;
                                //INIT HP, DAME MOB
                                if (p.clan.doanhTrai[9].area[0].mobs.size() > 0) {
                                    for (Mob _mob : p.clan.doanhTrai[9].area[0].mobs) {
                                        _mob.maxHp = p.getDamFull() * 6;
                                        _mob.hp = _mob.maxHp;
                                    }
                                }
                                p.clan.doanhTrai[9].area[0].Enter(p);
                                return;
                            }
                        }
                    }
                    p.sendAddchatYellow("Tiêu diệt hết tất cả trước khi qua ải tiếp theo!");
                    p.map = mapold;
                    Service.gI().resetPoint(p.session, p.x - 50, p.y);
                    Service.gI().buyDone(p);
                    return;
                } else if (mapid >= 147 && mapid <= 152 && mapid != 150 && p.clan != null && p.clan.openKhiGas) {
                    if (mapid == 149 && p.clan.khiGas[0] != null) {//NEU CO MAP 1 THI DUOC QUA LUON
                        Service.gI().leaveOutMap(p);
                        if (map.id == 147) { //sa mac di qua
                            p.x = (short) 1476;
                            p.y = (short) 336;
                        } else {
                            p.x = (short) 63;
                            p.y = (short) 336;
                        }
                        p.clan.khiGas[0].area[0].Enter(p);
                        return;
                    } else if (mapid == 147 && p.clan.khiGas[1] != null) {//NEU CO MAP 2 THI DUOC QUA LUON
                        if ((map.id == 149 && chkAllMobDieDT()) || (map.id == 152)) {
                            Service.gI().leaveOutMap(p);
                            if (map.id == 149) { //tp satan di qua
                                p.x = (short) 65;
                                p.y = (short) 792;
                            } else {
                                p.x = (short) 1470;
                                p.y = (short) 792;
                            }
                            p.clan.khiGas[1].area[0].Enter(p);
                        } else {
                            p.sendAddchatYellow("Tiêu diệt hết tất cả trước khi qua ải tiếp theo!");
                            p.map = mapold;
                            Service.gI().resetPoint(p.session, p.x - 50, p.y);
                            Service.gI().buyDone(p);
                        }
                        return;
                    } else if (mapid == 152 && p.clan.khiGas[2] != null) {//NEU CO MAP 3 THI DUOC QUA LUON
                        if ((map.id == 147 && chkAllMobDieDT()) || (map.id == 151)) {
                            Service.gI().leaveOutMap(p);
                            if (map.id == 147) { //tp satan di qua
                                p.x = (short) 1574;
                                p.y = (short) 168;
                            } else {
                                p.x = (short) 59;
                                p.y = (short) 168;
                            }
                            p.clan.khiGas[2].area[0].Enter(p);
                        } else {
                            p.sendAddchatYellow("Tiêu diệt hết tất cả trước khi qua ải tiếp theo!");
                            p.map = mapold;
                            Service.gI().resetPoint(p.session, p.x - 50, p.y);
                            Service.gI().buyDone(p);
                        }
                        return;
                    } else if (mapid == 151 && p.clan.khiGas[3] != null) {//NEU CO MAP 3 THI DUOC QUA LUON
                        if ((map.id == 152 && chkAllMobDieDT()) || (map.id == 148)) {
                            Service.gI().leaveOutMap(p);
                            if (map.id == 152) { //bang gia di qua
                                p.x = (short) 65;
                                p.y = (short) 288;
                            } else {
                                p.x = (short) 1618;
                                p.y = (short) 288;
                            }
                            p.clan.khiGas[3].area[0].Enter(p);
                        } else {
                            p.sendAddchatYellow("Tiêu diệt hết tất cả trước khi qua ải tiếp theo!");
                            p.map = mapold;
                            Service.gI().resetPoint(p.session, p.x + 50, p.y);
                            Service.gI().buyDone(p);
                        }
                        return;
                    } else if (mapid == 148 && p.clan.khiGas[4] != null) {//NEU CO MAP 3 THI DUOC QUA LUON
                        if (map.id == 151 && chkAllMobDieDT()) {
                            Service.gI().leaveOutMap(p);
                            p.x = (short) 63;
                            p.y = (short) 480;
                            p.clan.khiGas[4].area[0].Enter(p);
                        } else {
                            p.sendAddchatYellow("Tiêu diệt hết tất cả trước khi qua ải tiếp theo!");
                            p.map = mapold;
                            Service.gI().resetPoint(p.session, p.x - 50, p.y);
                            Service.gI().buyDone(p);
                        }
                        return;
                    }
                    return;
                } else {
                    if (mapid != 84 && mapid != 85 && mapid != 86 && mapid != 87 && mapid != 88 && mapid != 89 && mapid != 90 && mapid != 91 && mapid != 102 && mapid != 114
                            && mapid != 115 && mapid != 116 && mapid != 117 && mapid != 118 && mapid != 119 && mapid != 120 && mapid != 122 && mapid != 123 && mapid != 124
                            && mapid != 131 && mapid != 132 && mapid != 133 && mapid != 147 && mapid != 148 && mapid != 149 && mapid != 151 && mapid != 152) {
                        if (p.taskId < (short) 8 && ((mapid >= 45 && mapid <= 50) || mapid == 12 || mapid == 18)) {
                            p.sendAddchatYellow("Nhiệm vụ chưa hoàn thành không thể tới đây!");
                            p.map = mapold;
                            Service.gI().resetPoint(p.session, p.x - 50, p.y);
                            Service.gI().buyDone(p);
                            return;
                        } else if (p.taskId < (short) 9 && !MapSoSinh(mapid)) {
                            p.sendAddchatYellow("Nhiệm vụ chưa hoàn thành không thể tới đây!");
                            p.map = mapold;
                            Service.gI().resetPoint(p.session, p.x - 50, p.y);
                            Service.gI().buyDone(p);
                            return;
                        } else if (p.taskId < (short) 20 && mapid >= 63) {
                            p.sendAddchatYellow("Nhiệm vụ chưa hoàn thành không thể tới đây!");
                            p.map = mapold;
                            if (mapid >= 78) {
                                Service.gI().resetPoint(p.session, p.x - 50, p.y);
                            } else {
                                Service.gI().resetPoint(p.session, p.x + 50, p.y);
                            }
                            Service.gI().buyDone(p);
                            return;
                        } else if (((mapid >= 63 && mapid <= 67) || mapid >= 73) && (p.taskId < (short) 21 || (p.taskId == (short) 21 && p.crrTask.index < (byte) 1))) {
                            p.sendAddchatYellow("Nhiệm vụ chưa hoàn thành không thể tới đây!");
                            p.map = mapold;
                            if (mapid >= 78) {
                                Service.gI().resetPoint(p.session, p.x - 50, p.y);
                            } else {
                                Service.gI().resetPoint(p.session, p.x + 50, p.y);
                            }
                            Service.gI().buyDone(p);
                            return;
                        } else if (mapid >= 73 && (p.taskId < (short) 21 || (p.taskId == (short) 21 && p.crrTask.index < (byte) 2))) {
                            p.sendAddchatYellow("Nhiệm vụ chưa hoàn thành không thể tới đây!");
                            p.map = mapold;
                            if (mapid >= 78) {
                                Service.gI().resetPoint(p.session, p.x - 50, p.y);
                            } else {
                                Service.gI().resetPoint(p.session, p.x + 50, p.y);
                            }
                            Service.gI().buyDone(p);
                            return;
                        } else if (mapid >= 78 && (p.taskId < (short) 21 || (p.taskId == (short) 21 && p.crrTask.index < (byte) 3))) {
                            p.sendAddchatYellow("Nhiệm vụ chưa hoàn thành không thể tới đây!");
                            p.map = mapold;
                            if (mapid >= 78) {
                                Service.gI().resetPoint(p.session, p.x - 50, p.y);
                            } else {
                                Service.gI().resetPoint(p.session, p.x + 50, p.y);
                            }
                            Service.gI().buyDone(p);
                            return;
                        } else if (mapid >= 92 && p.taskId < (short) 23) {
                            p.sendAddchatYellow("Nhiệm vụ chưa hoàn thành không thể tới đây!");
                            p.map = mapold;
                            if (mapid >= 78) {
                                Service.gI().resetPoint(p.session, p.x - 50, p.y);
                            } else {
                                Service.gI().resetPoint(p.session, p.x + 50, p.y);
                            }
                            Service.gI().buyDone(p);
                            return;
                        } else if (p.taskId < (short) 26 && mapid >= 97) {
                            p.sendAddchatYellow("Nhiệm vụ chưa hoàn thành không thể tới đây!");
                            p.map = mapold;
                            if (mapid >= 78) {
                                Service.gI().resetPoint(p.session, p.x - 50, p.y);
                            } else {
                                Service.gI().resetPoint(p.session, p.x + 50, p.y);
                            }
                            Service.gI().buyDone(p);
                            return;
                        } else if (p.taskId < (short) 27 && mapid >= 97 && mapid != 104) {
                            p.sendAddchatYellow("Nhiệm vụ chưa hoàn thành không thể tới đây!");
                            p.map = mapold;
                            if (mapid >= 78) {
                                Service.gI().resetPoint(p.session, p.x - 50, p.y);
                            } else {
                                Service.gI().resetPoint(p.session, p.x + 50, p.y);
                            }
                            Service.gI().buyDone(p);
                            return;
                        } else if (p.taskId < (short) 28 && mapid >= 100 && mapid != 104) {
                            p.sendAddchatYellow("Nhiệm vụ chưa hoàn thành không thể tới đây!");
                            p.map = mapold;
                            if (mapid >= 78) {
                                Service.gI().resetPoint(p.session, p.x - 50, p.y);
                            } else {
                                Service.gI().resetPoint(p.session, p.x + 50, p.y);
                            }
                            Service.gI().buyDone(p);
                            return;
                        } else if (p.taskId < (short) 29 && mapid >= 103 && mapid != 104) {
                            p.sendAddchatYellow("Nhiệm vụ chưa hoàn thành không thể tới đây!");
                            p.map = mapold;
                            Service.gI().resetPoint(p.session, p.x - 50, p.y);
                            Service.gI().buyDone(p);
                            return;
                        }
                    }
//                    else {
                    if (p.petfucus == 1) {
                        leaveDetu(p, p.detu);
                    }
                    if (p.pet2Follow == 1 && p.pet != null) {
                        p.zone.leavePETTT(p.pet);
                    }
                    leave(p);

                    //                int mapid;
                    //                mapid = vg.goMap;
                    Map ma = MainManager.getMapid(mapid);

                    for (byte j = 0; j < ma.template.wayPoints.length; j++) {
                        WayPoint vg2 = ma.template.wayPoints[j];
                        if (vg2.goMap == map.id) {
                            p.x = (short) (vg2.goX);
                            p.y = (short) (vg2.goY);
                        }
                    }
                    byte errornext = -1;
                    if (errornext == -1) {
                        //                    if(mapid == 21 || mapid == 22 || mapid == 23) {
                        //                        p.x = vg.goX;
                        //                        p.y = vg.goY;
                        //                        ma.area[0].EnterHome(p);
                        //                        return;
                        //                    } else {
                        for (byte j = 0; j < ma.area.length; j++) {
                            if (ma.area[j].players.size() < ma.template.maxplayers) {
                                //                            p.map.id = mapid;
                                p.x = vg.goX;
                                p.y = vg.goY;
                                ma.area[j].Enter(p);

                                return;
                            }
                            if (j == ma.area.length - 1) {
                                ma.area[Util.nextInt(0, ma.area.length)].Enter(p);
                                errornext = 0;
                                return;
                            }
                        }
                        //                    }
                    }
                    // send title set -82
                    //                Service.gI().tileSet(p.session, 1);
                    Enter(p);
                    switch (errornext) {
                        case 0:
                            p.sendAddchatYellow("Bản đồ quá tải.");
                            return;
                    }
                }
//                }

            }

        }
    }

    public boolean MapSoSinh(int id) { //NV < bai su hoc vo
        return (id >= 0 && id <= 4) || (id >= 7 && id <= 9) || id == 11 || id == 12 || (id >= 14 && id <= 18) || (id >= 21 && id <= 26) || (id >= 39 && id <= 50);
    }

    public void EnterHome(Player pl) {
//        synchronized (this) {
        Message msg;
        try {
//                players.add(pl);
//                if(pl.petfucus == 1) {
//                    pets.add(pl.detu);
//                }
            pl.zone = this;
            pl.map = map; ///////////////////////////////////////////////////////////////////////TEST VGO MOI
            msg = new Message(-24);
            msg.writer().writeByte(map.id);
            msg.writer().writeByte(map.template.planetId);
            msg.writer().writeByte(map.template.tileId);
            msg.writer().writeByte(map.template.bgId);
            msg.writer().writeByte(map.template.type);
            msg.writer().writeUTF(map.template.name);
            msg.writer().writeByte(id);
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
                msg.writer().writeBoolean(wp.isOffline);
                msg.writer().writeUTF(wp.name);
            }
            // Load mob class Map Template
            msg.writer().writeByte(mobs.size());
            for (short i = 0; i < mobs.size(); i++) {
                Mob mob = mobs.get(i);
                msg.writer().writeBoolean(false);
                msg.writer().writeBoolean(false);
                msg.writer().writeBoolean(false);
                msg.writer().writeBoolean(false);
                msg.writer().writeBoolean(false);
                msg.writer().writeByte(mob.template.tempId);
                msg.writer().writeByte(0);
                msg.writer().writeInt(mob.hp);
                msg.writer().writeByte(mob.level);
                msg.writer().writeInt(mob.maxHp);
                msg.writer().writeShort(mob.pointX);
                msg.writer().writeShort(mob.pointY);
                msg.writer().writeByte(mob.status);
                msg.writer().writeByte(0);
                msg.writer().writeBoolean(false); //is boss
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
            //load item Drop In Map
            msg.writer().writeByte(itemsMap.size());
            for (ItemMap itemMap : itemsMap) {
                msg.writer().writeShort(itemMap.itemMapID);
                msg.writer().writeShort(itemMap.itemTemplateID);
                msg.writer().writeShort(itemMap.x);
                msg.writer().writeShort(itemMap.y);
                msg.writer().writeInt(itemMap.playerId);
            }

            // bg item
            byte[] bgItem = FileIO.readFile("data/map/bg/" + map.id);
            msg.writer().write(bgItem);

            // eff item
            byte[] effItem = FileIO.readFile("data/map/eff/" + map.id);
            msg.writer().write(effItem);

            msg.writer().writeByte(map.bgType);
            msg.writer().writeByte(0); //teleport ?
            msg.writer().writeByte(0);

            pl.session.sendMessage(msg);
//                pl.zone.joinMapjoinMap(pl);
            msg.cleanup();
        } catch (Exception e) {
        }
//        }
    }

    public void Enter(Player pl) {
        synchronized (this) {
            Message msg;
            try {
                pl.zone = this;
                pl.map = map; ///////////////////////////////////////////////////////////////////////TEST VGO MOI
                if (map.MapNrNamec() && pl.nrNamec != 0) {
                    Server.gI().mapNrNamec[(int) (pl.nrNamec - 353)] = pl.map.id;
                    Server.gI().nameNrNamec[(int) (pl.nrNamec - 353)] = pl.map.template.name;
                    Server.gI().zoneNrNamec[(int) (pl.nrNamec - 353)] = pl.zone.id;
                    Server.gI().pNrNamec[(int) (pl.nrNamec - 353)] = pl.name;
                }
                if (!map.MapHome()) {
                    players.add(pl);
                    if (pl.petfucus == 1) {
                        //NEU LOAD DE TU O MAP COOL
                        upDownPointPETMapCool(pl);
                        //NEU LOAD DE TU O MAP COOL
                        pets.add(pl.detu);
                    }
                    if (pl.pet2Follow == 1 && pl.pet != null) {
                        pet2s.add(pl.pet);
                    }
                }
                //BAT CO KHI VAO NGOC RONG SAO DEN
                if ((map.id >= 85 && map.id <= 91) || (map.id >= 114 && map.id <= 120)) {
                    byte flagtype = (byte) (Util.nextInt(1, 10));
                    if (map.id >= 114 && map.id <= 120) {
                        flagtype = (byte) (Util.nextInt(10, 12));
                    }
                    pl.cPk = flagtype;
                    pl.detu.cPk = flagtype;
                    Service.gI().changeFlagPK(pl, flagtype);
                    if (pl.petfucus == 1) {
                        Service.gI().changeFlagPKPet(pl, flagtype);
                    }
                }
                //CHECK MAP COOLER GIAM HP, MP, Suc danh 50%
                upDownPointMapCool(pl);
                upDownPointSaoDen(pl);
                //CHECK NEU DANG GIAO DICH THI HUY GIAO DICH
                if (pl._isGiaoDich) {
                    Service.gI().destroyGiaoDich(pl);
                }

//                pl.zone = this;
//                pl.map = map; ///////////////////////////////////////////////////////////////////////TEST VGO MOI
                msg = new Message(-24);
                msg.writer().writeByte((byte) map.id);
                msg.writer().writeByte(map.template.planetId);
                msg.writer().writeByte(map.template.tileId);
                msg.writer().writeByte(map.template.bgId);
                msg.writer().writeByte(map.template.type);
                msg.writer().writeUTF(map.template.name);
                msg.writer().writeByte(id);
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
                    msg.writer().writeBoolean(wp.isOffline);
                    msg.writer().writeUTF(wp.name);
                }
                // Load mob class Map Template
                msg.writer().writeByte(mobs.size());
                for (short i = 0; i < mobs.size(); i++) {
                    Mob mob = mobs.get(i);
                    msg.writer().writeBoolean(false);
                    msg.writer().writeBoolean(false);
                    msg.writer().writeBoolean(false);
                    msg.writer().writeBoolean(false);
                    msg.writer().writeBoolean(false);
                    msg.writer().writeByte((byte) mob.template.tempId);
                    if (mob.template.tempId == 70) {
                        msg.writer().writeByte((byte) mob.typeHiru);
                    } else {
                        msg.writer().writeByte((byte) 0);
                    }
                    msg.writer().writeInt(mob.hp);
                    msg.writer().writeByte(mob.level);
                    msg.writer().writeInt(mob.maxHp);
                    msg.writer().writeShort(mob.pointX);
                    msg.writer().writeShort(mob.pointY);
                    msg.writer().writeByte(mob.status);
                    msg.writer().writeByte((byte) 0);
//                    if(mob.template.tempId == 70) {
//                        msg.writer().writeBoolean(true);
//                    } else {
                    msg.writer().writeBoolean(false); //is boss
//                    }
                }
                msg.writer().writeByte((byte) 0);

                // Load NPC class Map Template
                msg.writer().writeByte(map.template.npcs.length);
                for (Npc npc : map.template.npcs) {
                    msg.writer().writeByte((byte) npc.status);
                    msg.writer().writeShort(npc.cx);
                    msg.writer().writeShort(npc.cy);
                    msg.writer().writeByte((byte) npc.tempId);
                    msg.writer().writeShort((short) npc.avartar);
//                    Util.log(": " + npc.status + ", " + npc.cx + ", " + npc.cy + ", " + npc.tempId + ", " + npc.avartar);
                }
                //load item Drop In Map
                if (map.id == 22 || map.id == 21 || map.id == 23) {
//                    removeItemMapById(pl.id);
                    Item itemMap = ItemSell.getItemNotSell(74);
                    ItemMap item = new ItemMap();
                    item.playerId = pl.id;
                    if (map.id == 21 || map.id == 23) {
                        item.x = (short) 630;
                    } else {
                        item.x = (short) 55;
                    }
                    item.y = (short) 320;

                    item.itemMapID = 74;
                    item.itemTemplateID = (short) item.itemMapID;
                    itemMap.template = ItemTemplate.ItemTemplateID(74);
                    item.item = itemMap;

                    msg.writer().writeByte((byte) 1);
                    msg.writer().writeShort((short) item.itemMapID);
                    msg.writer().writeShort(item.itemTemplateID);
                    msg.writer().writeShort(item.x);
                    msg.writer().writeShort(item.y);
                    msg.writer().writeInt(item.playerId);
//                    itemsMap.add(item);
                } else if ((map.id == 42 || map.id == 43 || map.id == 44) && pl.taskId == (short) 3 && pl.crrTask.index == (byte) 1) { //NHIEM VU SAO BANG
                    Item itemMap = ItemSell.getItemNotSell(78);
                    ItemMap item = new ItemMap();
                    item.playerId = pl.id;
                    if (map.id == 42) {
                        item.x = (short) 106;
                        item.y = (short) 288;
                    } else if (map.id == 43) {
                        item.x = (short) 102;
                        item.y = (short) 264;
                    } else {
                        item.x = (short) 96;
                        item.y = (short) 288;
                    }
                    item.itemMapID = 78;
                    item.itemTemplateID = (short) item.itemMapID;
                    itemMap.template = ItemTemplate.ItemTemplateID(78);
                    item.item = itemMap;
                    msg.writer().writeByte((byte) 1);
                    msg.writer().writeShort((short) item.itemMapID);
                    msg.writer().writeShort(item.itemTemplateID);
                    msg.writer().writeShort(item.x);
                    msg.writer().writeShort(item.y);
                    msg.writer().writeInt(item.playerId);
//                        itemsMap.add(item);
                } else {
                    msg.writer().writeByte(itemsMap.size());
                    for (ItemMap itemMap : itemsMap) {
                        msg.writer().writeShort((short) itemMap.itemMapID);
                        msg.writer().writeShort(itemMap.itemTemplateID);
                        msg.writer().writeShort(itemMap.x);
                        msg.writer().writeShort(itemMap.y);
                        msg.writer().writeInt(itemMap.playerId);
                    }
                }
                // bg item
                if ((map.id >= 153) || (map.id >= 122 && map.id <= 124) || (map.id >= 147 && map.id <= 148)) {
                    if ((map.id >= 153 && map.id <= 161) || (map.id >= 122 && map.id <= 124)) {
                        byte[] bgItem = FileIO.readFile("data/map/bg/" + map.id);
                        msg.writer().write(bgItem);
                    } else {
                        msg.writer().writeShort((short) 0);
                        msg.writer().writeShort((short) 0);
                    }
//                    byte[] bgItem = FileIO.readFile("data/map/bg/0");
//                    msg.writer().write(bgItem);
//
//                    // eff item
//                    byte[] effItem = FileIO.readFile("data/map/eff/0");
//                    msg.writer().write(effItem);
                } else {
                    byte[] bgItem = FileIO.readFile("data/map/bg/" + map.id);
                    msg.writer().write(bgItem);

                    // eff item
                    byte[] effItem = FileIO.readFile("data/map/eff/" + map.id);
                    msg.writer().write(effItem);
                }

                msg.writer().writeByte(map.bgType);
                msg.writer().writeByte((byte) 0); //teleport ?
                msg.writer().writeByte((byte) 0);
                pl.session.sendMessage(msg);

                if (!map.MapHome()) {
                    pl.zone.joinMap(pl);
                }
                msg.cleanup();
                //CHECK NHIEM VU DEN DIA DIEM
                if (pl.taskId == (short) 8 && pl.crrTask.index == (byte) 3 && map.id == 47) {
                    if (pl.power < (pl.getPowerLimit() * 1000000000L)) {
                        pl.tiemNang += pl.crrTask.bonus;
                        pl.power += pl.crrTask.bonus;
                        pl.ngoc += 30000;
                        pl.ngocKhoa += 30000;
                        pl.vang += 100000000;
                    }
                    pl.UpdateSMTN((byte) 2, pl.crrTask.bonus);
                    pl.sendAddchatYellow("Bạn vừa được thưởng " + Util.powerToString(pl.crrTask.bonus) + " sức mạnh");
                    TaskService.gI().setupNextNewTask(pl, (byte) (pl.taskId + (byte) 1));
                } else if (pl.taskId >= (short) 25 && pl.taskId < (short) 29 && pl.crrTask.index == (byte) 0 && (int) (TaskManager.gI().mapTASK0[pl.taskId][pl.crrTask.index]) == map.id) {
                    //CHECK NHIEM VU DEN DIA DIEM TU ANDOID 19 DEN SIEU BO HUNG
                    TaskService.gI().updateCountTask(pl);
                } else if (pl.taskId == (short) 29 && pl.crrTask.index == (byte) 2 && (int) (TaskManager.gI().mapTASK0[pl.taskId][pl.crrTask.index]) == map.id) {
                    TaskService.gI().updateCountTask(pl);
                } else if (pl.taskId == (short) 32 && pl.crrTask.index == (byte) 0 && (int) (TaskManager.gI().mapTASK0[pl.taskId][pl.crrTask.index]) == map.id) {
                    TaskService.gI().updateCountTask(pl);
                }
//                if(map.id == 21) {
//                    Service.gI().isTrungMabu(pl);
//                }
            } catch (Exception e) {
            }
        }
    }

    public void EnterCapsule(Player pl) {
        synchronized (this) {
            Message msg;
            try {
                if (!map.MapHome()) {
                    players.add(pl);
                    if (pl.petfucus == 1) {
                        //NEU LOAD DE TU O MAP COOL
                        upDownPointPETMapCool(pl);
                        //NEU LOAD DE TU O MAP COOL
                        pets.add(pl.detu);
                    }
                    if (pl.pet2Follow == 1 && pl.pet != null) {
                        pet2s.add(pl.pet);
                    }
                }
                //BAT CO KHI VAO NGOC RONG SAO DEN
                if ((map.id >= 85 && map.id <= 91) || (map.id == 114)) {
                    byte flagtype = (byte) (Util.nextInt(1, 10));
                    if (map.id == 114) {
                        flagtype = (byte) (Util.nextInt(10, 12));
                    }
                    pl.cPk = flagtype;
                    pl.detu.cPk = flagtype;
                    Service.gI().changeFlagPK(pl, flagtype);
                    if (pl.petfucus == 1) {
                        Service.gI().changeFlagPKPet(pl, flagtype);
                    }
                }
                //CHECK MAP COOLER GIAM HP, MP, Suc danh 50%
                upDownPointMapCool(pl);

                pl.zone = this;
                pl.map = map; ////////////////////////////TEST ENTER MAP MOI

                upDownPointSaoDen(pl);
                msg = new Message(-24);
                msg.writer().writeByte((byte) map.id);
                msg.writer().writeByte(map.template.planetId);
                msg.writer().writeByte(map.template.tileId);
                msg.writer().writeByte(map.template.bgId);
                msg.writer().writeByte(map.template.type);
                msg.writer().writeUTF(map.template.name);
                msg.writer().writeByte(id);
                msg.writer().writeShort(pl.x); //x
//                msg.writer().writeShort(pl.y);
                msg.writer().writeShort(10); // y

                // Load WayPoint class Map Template
                //    ArrayList<WayPoint> wayPoints = map.wayPoints;
                msg.writer().writeByte(map.template.wayPoints.length);
                for (WayPoint wp : map.template.wayPoints) {
                    msg.writer().writeShort(wp.minX);
                    msg.writer().writeShort(wp.minY);
                    msg.writer().writeShort(wp.maxX);
                    msg.writer().writeShort(wp.maxY);
                    msg.writer().writeBoolean(wp.isEnter);
                    msg.writer().writeBoolean(wp.isOffline);
                    msg.writer().writeUTF(wp.name);
                }
                // Load mob class Map Template
                msg.writer().writeByte(mobs.size());
                for (short i = 0; i < mobs.size(); i++) {
                    Mob mob = mobs.get(i);
                    msg.writer().writeBoolean(false);
                    msg.writer().writeBoolean(false);
                    msg.writer().writeBoolean(false);
                    msg.writer().writeBoolean(false);
                    msg.writer().writeBoolean(false);
                    msg.writer().writeByte((byte) mob.template.tempId);
                    msg.writer().writeByte((byte) 0);
                    msg.writer().writeInt(mob.hp);
                    msg.writer().writeByte(mob.level);
                    msg.writer().writeInt(mob.maxHp);
                    msg.writer().writeShort(mob.pointX);
                    msg.writer().writeShort(mob.pointY);
                    msg.writer().writeByte(mob.status);
                    msg.writer().writeByte((byte) 0);
                    msg.writer().writeBoolean(false); //is boss
                }

                msg.writer().writeByte((byte) 0);

                // Load NPC class Map Template
                msg.writer().writeByte(map.template.npcs.length);
                for (Npc npc : map.template.npcs) {
                    msg.writer().writeByte((byte) npc.status);
                    msg.writer().writeShort(npc.cx);
                    msg.writer().writeShort(npc.cy);
                    msg.writer().writeByte((byte) npc.tempId);
                    msg.writer().writeShort((short) npc.avartar);
                }
//                if(map.id >= 85 && map.id <= 91) {
//                    Item itemMap = ItemSell.getItemNotSell((map.id + 287));
//                    ItemMap item = new ItemMap();
//                    item.playerId = -1;
//                    if(map.id == 85) {
//                        item.x = (short)1003;
//                        item.y = (short)360;
//                    } else if(map.id == 86) {
//                        item.x = (short)693;
//                        item.y = (short)336;
//                    } else if(map.id == 87) {
//                        item.x = (short)739;
//                        item.y = (short)384;
//                    } else if(map.id == 88) {
//                        item.x = (short)723;
//                        item.y = (short)336;
//                    } else if(map.id == 89) {
//                        item.x = (short)881;
//                        item.y = (short)192;
//                    } else if(map.id == 90) {
//                        item.x = (short)763;
//                        item.y = (short)240;
//                    } else if(map.id == 91) {
//                        item.x = (short)915;
//                        item.y = (short)360;
//                    }
//
//                    item.itemMapID = (map.id + 287);
//                    item.itemTemplateID = (short) item.itemMapID;
//                    itemMap.template = ItemTemplate.ItemTemplateID((map.id + 287));
//                    item.item = itemMap;
//                    itemsMap.add(item);
//                }
                //load item Drop In Map
                if (map.id == 22 || map.id == 21 || map.id == 23) {
                    Item itemMap = ItemSell.getItemNotSell(74);
                    ItemMap item = new ItemMap();
                    item.playerId = pl.id;
                    if (map.id == 21 || map.id == 23) {
                        item.x = (short) 630;
                    } else {
                        item.x = (short) 55;
                    }
                    item.y = (short) 320;

                    item.itemMapID = 74;
                    item.itemTemplateID = (short) item.itemMapID;
                    itemMap.template = ItemTemplate.ItemTemplateID(74);
                    item.item = itemMap;

                    msg.writer().writeByte((byte) 1);
                    msg.writer().writeShort((short) item.itemMapID);
                    msg.writer().writeShort(item.itemTemplateID);
                    msg.writer().writeShort(item.x);
                    msg.writer().writeShort(item.y);
                    msg.writer().writeInt(item.playerId);
//                    itemsMap.add(item);
                } else {
                    msg.writer().writeByte(itemsMap.size());
                    for (ItemMap itemMap : itemsMap) {
                        msg.writer().writeShort((short) itemMap.itemMapID);
                        msg.writer().writeShort(itemMap.itemTemplateID);
                        msg.writer().writeShort(itemMap.x);
                        msg.writer().writeShort(itemMap.y);
                        msg.writer().writeInt(itemMap.playerId);
                    }
                }

                if ((map.id >= 153) || (map.id >= 122 && map.id <= 124) || (map.id >= 147 && map.id <= 148)) {
                    if ((map.id >= 153 && map.id <= 161) || (map.id >= 122 && map.id <= 124)) {
                        byte[] bgItem = FileIO.readFile("data/map/bg/" + map.id);
                        msg.writer().write(bgItem);
                    } else {
                        msg.writer().writeShort((short) 0);
                        msg.writer().writeShort((short) 0);
                    }
                } else {
                    byte[] bgItem = FileIO.readFile("data/map/bg/" + map.id);
                    msg.writer().write(bgItem);

                    // eff item
                    byte[] effItem = FileIO.readFile("data/map/eff/" + map.id);
                    msg.writer().write(effItem);
                }

                msg.writer().writeByte(map.bgType);
                if (pl.isTennis) {
                    msg.writer().writeByte((byte) 3);
                } else {
                    msg.writer().writeByte((byte) 1); //teleport ? typeteleport cuar nguoidung capsule
                }
                msg.writer().writeByte((byte) 0);
                pl.session.sendMessage(msg);
                if (map.id != 21 && map.id != 22 && map.id != 23) {
                    pl.zone.joinMapCapsule(pl);
                }
                msg.cleanup();
            } catch (Exception e) {
            }
        }
    }

    //REMOVE ITEM MAP BY ID
    public void removeItemMapById(int idItem) {
        for (int i = 0; i < itemsMap.size(); i++) {
            if (itemsMap.get(i).itemMapID == idItem) {
                itemsMap.remove(i);
                break;
            }
        }
    }

    public void upDownPointSaoDen(Player p) {
        if (map.MapSaoDen() && !p.isMapSaoDen) {
            p.isMapSaoDen = true;
            if (p.xHPSaoDen > (byte) 0) {
                p.hp = p.hp > p.getHpFull() ? p.getHpFull() : p.hp;
                Service.gI().loadPoint(p.session, p);
            }
        } else if (!map.MapSaoDen() && p.isMapSaoDen) {
            p.isMapSaoDen = false;
            if (p.xHPSaoDen > (byte) 0) {
                p.hp = p.hp > p.getHpFull() ? p.getHpFull() : p.hp;
                Service.gI().loadPoint(p.session, p);
            }
        }
    }

    //GIAM CHI SO KHI O MAP COOOL
    public void upDownPointMapCool(Player pl) {
        if (map.MapCold() && !pl.checkCTCold() && !pl.isDownPointCold) {
            pl.isDownPointCold = true;
            pl.sendAddchatYellow("Sức tấn công và HP của bạn bị giảm đi 50% vì ảnh hưởng bởi cái lạnh");
            pl.hp = pl.hp > pl.getHpFull() ? pl.getHpFull() : pl.hp;
            Service.gI().loadPoint(pl.session, pl);
        } else if ((!map.MapCold() && pl.isDownPointCold) || (map.MapCold() && pl.checkCTCold())) {
            pl.isDownPointCold = false;
            pl.sendAddchatYellow("Tấn công và HP của bạn đã phục hồi do không còn ảnh hưởng bởi cái lạnh");
            Service.gI().loadPoint(pl.session, pl);
        }
    }

    //GIAM CHI SO CUA DE KHI O MAP COOOL
    public void upDownPointPETMapCool(Player pl) {
        if (map.MapCold() && !pl.detu.checkCTCold() && !pl.detu.isDownPointCold) {
            pl.detu.isDownPointCold = true;
            pl.detu.hp = pl.detu.hp > pl.detu.getHpFull() ? pl.detu.getHpFull() : pl.detu.hp;
        } else if ((!map.MapCold() && pl.detu.isDownPointCold) || (map.MapCold() && pl.detu.checkCTCold())) {
            pl.detu.isDownPointCold = false;
        }
    }

    public void joinMap(Player p) {
        Message msg;
        try {
            for (byte i = 0; i < players.size(); i++) {
                if (players.get(i) != null && players.get(i).id != p.id) {
                    loadInfoPlayer(players.get(i).session, p);
                    //send chim cua minh cho nguoi khac
                    if (p.chimFollow == (byte) 1) {
                        useDeTrungForMe(players.get(i), p, (byte) 0);
                    }
                    if (p.petfucus == 1) {
                        loadInfoDeTu(players.get(i).session, p.detu);
                    }
                    if (p.pet2Follow == (byte) 1 && p.pet != null) {
                        loadInfoPet(players.get(i).session, p.pet);
                    }
                }
            }
//            for (Player pl : players) {
//                if (p != pl) {
//                    loadInfoPlayer(pl.session, p);
//                    //send chim cua minh cho nguoi khac
//                    if(p.chimFollow == (byte)1) {
//                        useDeTrungForMe(pl, p, (byte)0);
//                    }
//                    if(p.petfucus == 1) {
//                        loadInfoDeTu(pl.session, p.detu);
//                    }
//                    if(p.pet2Follow == (byte)1 && p.pet != null) {
//                        loadInfoPet(pl.session, p.pet);
//                    }
//                }
//            }
//            for (Detu de : pets) {
//                Util.log("PET ID: " + de.id);
//                if (de == p.detu && p.petfucus == 0) {
//                    return;
//                }
//                loadInfoDeTu(p.session, de);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void joinMapCapsule(Player p) {
        try {
            Message msg = new Message(-5);
            msg.writer().writeInt(p.id); //id detu
            if (p.clan != null) {
                msg.writer().writeInt(p.clan.id);
            } else {
                msg.writer().writeInt(794); //id clan
            }
            msg.writer().writeByte(p.getLevelPower()); //level player viet 1 function get level
            msg.writer().writeBoolean(false);  //co dang vo hinh hay khong
            msg.writer().writeByte(p.typePk);  //type pk
            msg.writer().writeByte(p.gender); // get nClass teeamplate skill theo gender
            msg.writer().writeByte(p.gender); // get gender detu
            if (p.isMonkey) {
                Skill skl = p.getSkillById(13);
                if (skl.point == 1) {
                    msg.writer().writeShort(192);
                } else if (skl.point == 2) {
                    msg.writer().writeShort(195);
                } else if (skl.point == 3) {
                    msg.writer().writeShort(196);
                } else if (skl.point == 4) {
                    msg.writer().writeShort(199);
                } else if (skl.point == 5) {
                    msg.writer().writeShort(197);
                } else if (skl.point == 6) {
                    msg.writer().writeShort(200);
                } else if (skl.point == 7) {
                    msg.writer().writeShort(198);
                }
            } else {
                msg.writer().writeShort(p.PartHead()); // part head de tu
            }
            msg.writer().writeUTF(p.name);  // name de tu
            msg.writer().writeInt(p.hp); // hp de tu hien tai
            msg.writer().writeInt(p.getHpFull()); // hp full de tu

            String bodyLeg = Service.gI().writePartBodyLeg(p);
            String[] arrOfStr = bodyLeg.split(",", 2);
            msg.writer().writeShort(Short.parseShort(arrOfStr[0]));
            msg.writer().writeShort(Short.parseShort(arrOfStr[1]));

            msg.writer().writeByte((byte) 8); // bag
            msg.writer().writeByte((byte) (-1)); //b gui sang khong thay dung
            msg.writer().writeShort(p.x); // x 
            msg.writer().writeShort((short) 10); // y
            msg.writer().writeShort((byte) 0); //  eff 5 buff hp
            msg.writer().writeShort((byte) 0); // eff 5 buff mp
            msg.writer().writeByte((byte) 0); // so luong eff char
//                msg.writer().writeByte(0); // eff template id
//                msg.writer().writeInt(0);  //time start
//                msg.writer().writeInt(0);  //time length
//                msg.writer().writeShort(-1); // param eff
            if (p.isTennis) {
                msg.writer().writeByte((byte) 3);
            } else {
                msg.writer().writeByte((byte) 1); // b76 type teleport cua nguoi nhin thay bay den
            }
            msg.writer().writeByte((p.isMonkey == true ? (byte) 1 : (byte) 0)); //ismonkey
            msg.writer().writeShort(p.getMount()); // id can dau van, van bay ,....
            msg.writer().writeByte(p.cPk); // cflag
            msg.writer().writeByte(p.NhapThe); // is hop the?
            msg.writer().flush();
            for (byte i = 0; i < players.size(); i++) {
                if (players.get(i) != null && players.get(i).id != p.id) {
                    players.get(i).session.sendMessage(msg);
                }
            }
//            for(Player _pl: players) {
//                if(_pl.id != p.id) {
//                    _pl.session.sendMessage(msg); 
//                }
//            }
            msg.cleanup();

            for (byte i = 0; i < players.size(); i++) {
                if (players.get(i) != null && players.get(i).id != p.id) {
                    //UPDATE ITEM BAG SAU LUNG CUA PHAN TREN
                    if (p.imgNRSD > (byte) 0) {
                        Service.gI().updateBagNew(players.get(i).session, p.id, p.imgNRSD);
                        ClanService.gI().getBagBangNew(players.get(i).session, p.imgNRSD);
                    } else {
                        if (p.clan != null) {
                            //UPDATE BAG SAU LUNG
                            Service.gI().updateBagNew(players.get(i).session, p.id, p.clan.imgID);
                            //GET BAG SAU LUNG
                            ClanService.gI().getBagBangNew(players.get(i).session, p.clan.imgID);
                        }
                    }
                    //send chim cua minh cho nguoi khac
                    if (p.chimFollow == (byte) 1) {
                        useDeTrungForMe(players.get(i), p, (byte) 0);
                    }
                    if (p.petfucus == 1) {
                        loadInfoDeTu(players.get(i).session, p.detu);
                    }
                    if (p.pet2Follow == (byte) 1 && p.pet != null) {
                        loadInfoPet(p.session, p.pet);
                    }
                }
            }
//            for(Player _pl: players) {
//                if(_pl.id != p.id) {
//                    //UPDATE ITEM BAG SAU LUNG CUA PHAN TREN
//                    if(p.imgNRSD > (byte)0) {
//                        Service.gI().updateBagNew(_pl.session, p.id, p.imgNRSD);
//                        ClanService.gI().getBagBangNew(_pl.session, p.imgNRSD);
//                    } else {
//                        if (p.clan != null) {
//                            //UPDATE BAG SAU LUNG
//                            Service.gI().updateBagNew(_pl.session, p.id, p.clan.imgID);
//                            //GET BAG SAU LUNG
//                            ClanService.gI().getBagBangNew(_pl.session, p.clan.imgID);
//                        }
//                    }
//                    //send chim cua minh cho nguoi khac
//                    if(p.chimFollow == (byte)1) {
//                        useDeTrungForMe(_pl, p, (byte)0);
//                    }
//                    if(p.petfucus == 1) {
//                        loadInfoDeTu(_pl.session, p.detu);
//                    }
//                    if(p.pet2Follow == (byte)1 && p.pet != null) {
//                        loadInfoPet(p.session, p.pet);
//                    }
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Mob getMob(int id) {
        short i;
        for (i = 0; i < this.mobs.size(); i++) {
            if (this.mobs.get(i) != null && (this.mobs.get(i)).tempId == id) {
                return this.mobs.get(i);
            }
        }
        return null;
    }

    public Mob getMobByTempID(int id) {
        for (byte i = 0; i < this.mobs.size(); i++) {
            if (this.mobs.get(i) != null && this.mobs.get(i).template.tempId == id) {
                return this.mobs.get(i);
            }
        }
        return null;
    }

    // send message to all player in map ngoai nguoi choi
    public void sendMyMessage(Player p, Message m) {

        for (byte i = 0; i < players.size(); i++) {
            if (players.get(i) != null && players.get(i).id != p.id) {
                players.get(i).session.sendMessage(m);
            }
        }
//        for (Player player : players) {
//            if (p.id != player.id) {
//                player.session.sendMessage(m);
//            }
//        }
    }

    // send message to me
    public void sendMessageToMe(Message m, Player p) {
        p.session.sendMessage(m);
    }

    public Player getPlayerByID(int id) {
        for (byte i = 0; i < players.size(); i++) {
            if (players.get(i) != null && players.get(i).id == id) {
                return players.get(i);
            }
        }
//        for (Player player : players) {
//            if (id == player.id) {
//                return player;
//            }
//        }
        return null;
    }

    public void sendMessage(Message m) {
        try {
            for (byte i = 0; i < players.size(); i++) {
                if (players.get(i) != null) {
                    players.get(i).session.sendMessage(m);
                }
            }
//            for (Player player : players) {
//                if (player != null && player.session != null) {
//                    player.session.sendMessage(m);
//                }
//            }
        } catch (Exception var3) {
            var3.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }

    }

    private void MobStartDie(Player p, int dame, Mob mob, boolean fatal, ArrayList<ItemMap> itemDrops) throws IOException {
        try {
            Message m = new Message(-12);
            m.writer().writeByte(mob.tempId);
            m.writer().writeInt(dame);
            m.writer().writeBoolean(fatal);
            m.writer().writeByte(itemDrops.size());
            if (itemDrops.size() > 0) {
                for (ItemMap itemMap : itemDrops) {
                    m.writer().writeShort(itemMap.itemMapID);
                    m.writer().writeShort(itemMap.item.template.id);
                    m.writer().writeShort(mob.pointX); //X END
                    m.writer().writeShort(mob.pointY);  //Y END ITEM ROI
                    m.writer().writeInt(p.id);
                }
            }
            sendMessage(m);
            m.writer().flush();
            m.cleanup();
            //CHECK BUA THU HUT
            if (p.getBuaThuHut() && itemDrops.size() > 0) {
                PickItemDrop(p, (short) itemDrops.get(0).itemMapID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void attachedMob(int dame, int mobid, boolean fatal) throws IOException {
        Message m = new Message(-9);
        m.writer().writeByte(mobid);
        Mob mob = getMob(mobid);
        m.writer().writeInt(mob.hp);
        m.writer().writeInt(dame);
        m.writer().writeBoolean(fatal);//flag
        //eff boss
        //5 khói
        m.writer().writeByte(-1);
        m.writer().flush();
        sendMessage(m);
    }

    public void PlayerDead(Player p) throws IOException {
        if (p.isdie = true) {
            Message m = new Message(-17);
            m.writer().writeByte(p.cPk);
            m.writer().writeShort(p.x);
            m.writer().writeShort(p.y);
            m.writer().flush();
            p.session.sendMessage(m);
            m.cleanup();
        } else {
            Message m = new Message(-16);
            m.writer().flush();
            p.session.sendMessage(m);
            m.cleanup();
        }
    }

    public void attackMob(Session session, Player pl, int id) {
        Message msg;
        try {
            msg = new Message(54);
            msg.writer().writeInt(pl.id);
            msg.writer().writeByte(pl.selectSkill.skillId);
            msg.writer().writeByte(id);
            session.sendMessage(msg);
            msg.cleanup();

        } catch (Exception e) {
        }
    }

    public void MOB_MAX_HP(Player p, int mob, int hp) throws IOException {
        Message m;
        try {
            m = new Message(-9);
            m.writer().writeByte(mob);
            m.writer().writeInt(hp);
            p.session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
        }
    }

    public short getItemMapNotId() {
        short itemmapid = 0;
        while (true) {
            boolean isset = false;
            for (int i = this.itemsMap.size() - 1; i >= 0; --i) {
                if (this.itemsMap.get(i) != null && (this.itemsMap.get(i)).itemMapID == itemmapid) {
                    isset = true;
                }
            }
            if (!isset) {
                return itemmapid;
            }
            itemmapid++;
        }
    }

    public void FightMob(Player p, Message m) throws IOException {
        byte mobId = m.reader().readByte();
        int damage = 0;
        Mob mob = getMob((int) mobId);

        if (mob != null) {
            int miss = Util.nextInt(0, 10);
            Skill skillPlayerUse = p.getSkillByIDTemplate(p.idSkillselect);
            long _timeNOW = System.currentTimeMillis();
            short xSkill = (short) skillPlayerUse.dx;
            short ySkill = (short) skillPlayerUse.dy;
            if (skillPlayerUse.template.id == 0 || skillPlayerUse.template.id == 9 || skillPlayerUse.template.id == 2 || skillPlayerUse.template.id == 17 || skillPlayerUse.template.id == 4
                    || skillPlayerUse.template.id == 1 || skillPlayerUse.template.id == 3 || skillPlayerUse.template.id == 5) {
                xSkill = (short) (xSkill + 100); //100
                ySkill = (short) (ySkill + 100); //100
            }
            //CHECK TAM DANH CUA SKILL
            if ((((Math.abs(p.x - mob.pointX) <= xSkill) && (Math.abs(p.y - mob.pointY) <= ySkill)) || (mobId == (byte) 70)) && !p.isTroi) {
                //use skill troi xayda
                if (p.idSkillselect == 23 && p.mp >= skillPlayerUse.manaUse && !p.isdie && !mob.isDie && !p.checkPlayerBiKhongChe() && (_timeNOW - skillPlayerUse.lastTimeUseThisSkill > (long) (skillPlayerUse.coolDown))) {
                    skillPlayerUse.lastTimeUseThisSkill = _timeNOW; //gan time hien tai
                    //CHECK NOI TAI TROI TANG SAT THUONG DON KE TIEP
                    if (p.noiTai.id != 0 && skillPlayerUse.template.id == p.noiTai.idSkill) {
                        p.upDameAfterNoiTai = true;
                    }
                    p.mp -= skillPlayerUse.manaUse;
                    postUpdateKI(p);
                    p.isTroi = true;
                    mob.isFreez = true;
                    p.MOBHOLD = mob;
                    // send effect troi
                    try {
                        m = new Message(-124);
                        m.writer().writeByte(1);
                        m.writer().writeByte(1);
                        m.writer().writeByte(32);
                        m.writer().writeByte(mobId);
                        m.writer().writeInt(p.id);
                        m.writer().flush();
                        for (Player player : players) {
                            if (player != null) {
                                player.session.sendMessage(m);
                            }
                        }
                        ResetTroiTask troiTask = new ResetTroiTask(players, mob, p);
                        Timer timerTroi = new Timer();
                        timerTroi.schedule(troiTask, (skillPlayerUse.damage * 1000));
                        m.cleanup();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
                //use thoi mien
                if (p.idSkillselect == 22 && p.mp >= skillPlayerUse.manaUse && !p.isdie && !mob.isDie && !p.checkPlayerBiKhongChe() && (_timeNOW - skillPlayerUse.lastTimeUseThisSkill > (long) (skillPlayerUse.coolDown))) {
                    skillPlayerUse.lastTimeUseThisSkill = _timeNOW; //gan time hien tai
                    //CHECK NOI TAI THOI MIEN TANG SAT THUONG DON KE TIEP
                    if (p.noiTai.id != 0 && skillPlayerUse.template.id == p.noiTai.idSkill) {
                        p.upDameAfterNoiTai = true;
                    }
                    p.mp -= skillPlayerUse.manaUse;
                    postUpdateKI(p);
                    mob.isSleep = true;
                    // send effect thoi mien
                    try {
                        m = new Message(-124);
                        m.writer().writeByte(1);
                        m.writer().writeByte(1);
                        m.writer().writeByte(41);
                        m.writer().writeByte(mobId);
                        m.writer().flush();
                        for (Player player : players) {
                            if (player != null) {
                                player.session.sendMessage(m);
                                ResetSleepTask sleepTask = new ResetSleepTask(player, mob);
                                Timer timerSleep = new Timer();
                                timerSleep.schedule(sleepTask, (skillPlayerUse.damage * 1000));
                            }
                        }
                        m.cleanup();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        m = new Message(54);
                        m.writer().writeInt(p.id);
                        m.writer().writeByte(skillPlayerUse.skillId);
                        m.writer().writeByte(mob.tempId);
                        for (byte i = 0; i < players.size(); i++) {
                            if (players.get(i) != null && players.get(i).id != p.id) {
                                players.get(i).session.sendMessage(m);
                            }
                        }
                        m.writer().flush();
                        m.cleanup();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
                //use socola
                if (p.idSkillselect == 18) {
                    int _manaSocola = (int) ((Util.getPercentDouble(skillPlayerUse.manaUse) * p.getMpFull()));
                    if (p.mp >= _manaSocola && !p.checkPlayerBiKhongChe() && !p.isdie && !mob.isDie && (_timeNOW - skillPlayerUse.lastTimeUseThisSkill > (long) (skillPlayerUse.coolDown))) {
                        skillPlayerUse.lastTimeUseThisSkill = _timeNOW; //gan time hien tai
                        //CHECK NOI TAI SOCOLA TANG SAT THUONG DON KE TIEP
                        if (p.noiTai.id != 0 && skillPlayerUse.template.id == p.noiTai.idSkill) {
                            p.upDameAfterNoiTai = true;
                        }
                        p.mp -= _manaSocola;
                        postUpdateKI(p);
                        // send effect socola
                        mob.isSocola = skillPlayerUse.damage;
                        try {
                            m = new Message(-112);
                            m.writer().writeByte(1);
                            m.writer().writeByte(mobId);
                            m.writer().writeShort((short) (Util.nextInt(4127, 4133)));
                            m.writer().flush();
                            for (byte i = 0; i < players.size(); i++) {
                                if (players.get(i) != null) {
                                    players.get(i).session.sendMessage(m);
                                    ResetSocolaTask socolaTask = new ResetSocolaTask(players.get(i), mob);
                                    Timer timerSocola = new Timer();
                                    timerSocola.schedule(socolaTask, 30000);
                                }
                            }
                            m.cleanup();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        setSkillPaint(skillPlayerUse.skillId, p, 8);
                        return;
                    }
                    return;
                }
                //dich chuyen tuc thoi
                if (p.idSkillselect == 20 && p.mp >= skillPlayerUse.manaUse && !p.isdie && !mob.isDie && !p.checkPlayerBiKhongChe() && (_timeNOW - skillPlayerUse.lastTimeUseThisSkill > (long) (skillPlayerUse.coolDown))) {
                    skillPlayerUse.lastTimeUseThisSkill = _timeNOW; //gan time hien tai
                    //CHECK NOI TAI THOI MIEN TANG SAT THUONG DON KE TIEP
                    if (p.noiTai.id != 0 && skillPlayerUse.template.id == p.noiTai.idSkill) {
                        p.upDameAfterNoiTai = true;
                    }
                    // DCTT XONG KHONG MISS SKILL
                    p.noMiss = true;
                    p.mp -= skillPlayerUse.manaUse;
                    p.PEMCRIT = 1;
                    postUpdateKI(p);
                    mob.isDCTT = true;
                    try {
                        m = new Message(123);
                        m.writer().writeInt(p.id);
                        m.writer().writeShort(mob.pointX);
                        m.writer().writeShort(mob.pointY);
                        m.writer().writeByte(1);
                        m.writer().flush();
                        for (byte i = 0; i < players.size(); i++) {
                            if (players.get(i) != null) {
                                players.get(i).session.sendMessage(m);
                            }
                        }
                        m.cleanup();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        m = new Message(54);
                        m.writer().writeInt(p.id);
                        m.writer().writeByte(skillPlayerUse.skillId);
                        m.writer().writeByte(mob.tempId);
                        for (byte i = 0; i < players.size(); i++) {
                            if (players.get(i) != null && players.get(i).id != p.id) {
                                players.get(i).session.sendMessage(m);
                            }
                        }
                        m.writer().flush();
                        m.cleanup();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // gui hieu ung choang cho client
                    try {
                        m = new Message(-124);
                        m.writer().writeByte(1);
                        m.writer().writeByte(1);
                        m.writer().writeByte(40);
                        m.writer().writeByte(mobId);
                        m.writer().flush();
                        for (byte i = 0; i < players.size(); i++) {
                            if (players.get(i) != null) {
                                players.get(i).session.sendMessage(m);
                                ResetDCTTTask dcttTask = new ResetDCTTTask(players.get(i), mob);
                                Timer timerDCTT = new Timer();
                                timerDCTT.schedule(dcttTask, skillPlayerUse.damage);
                            }
                        }
                        m.cleanup();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //SEND DAME CHO CLIENT
                    int damageDCTT = p.getDamFull();
                    //CHECK BUA BAT TU
                    if (p.hp == 1 && p.getBuaBatTu()) {
                        damageDCTT = 0;
                        p.sendAddchatYellow("Bạn đang được bùa bất tử bảo vệ, không thể tấn công");
                    } else if (damageDCTT >= mob.maxHp && mob.hp == mob.maxHp) {
                        damageDCTT = mob.maxHp - 1;
                    } else if (damageDCTT >= mob.hp) {
                        damageDCTT = mob.hp;
                    } else if (mob.hp == 1) {
                        damageDCTT = 1;
                    }

                    long upSmTnDCTT = (long) (damageDCTT / 2);
                    upSmTnDCTT = p.getPercentUpTnSm(upSmTnDCTT); //get tnsm spl tnsm
                    if (p.cPk > 0 && p.cPk < 12 && p.cPk != 8) { //flag
                        upSmTnDCTT = (int) (upSmTnDCTT * 1.05);
                    } else if (p.cPk == 8) {
                        upSmTnDCTT = (int) (upSmTnDCTT * 1.1);
                    }
                    upSmTnDCTT = upSmTnDCTT * p.getBuaTNSM(); //bua tnsm
                    //CHECK NOI TANG TNSM
                    if (p.noiTai.id != 0 && p.noiTai.idSkill == (byte) (-3)) {
                        upSmTnDCTT += (long) (upSmTnDCTT * Util.getPercentDouble((int) p.noiTai.param));
                    }
                    //END NOI TAI TANG TNSM
                    //GIAM TNSM
                    upSmTnDCTT = (long) (upSmTnDCTT / 2);
                    if (p.power >= 40000000000L) {
                        upSmTnDCTT = (long) (upSmTnDCTT / 5);
                    } else if (p.power >= 50000000000L) {
                        upSmTnDCTT = (long) (upSmTnDCTT / 7);
                    } else if (p.power >= 60000000000L) {
                        upSmTnDCTT = (long) (upSmTnDCTT / 8);
                    } else if (p.power >= 70000000000L) {
                        upSmTnDCTT = (long) (upSmTnDCTT / 10);
                    }
                    if (mob.template.tempId == 0) {
                        damageDCTT = 10;
                        upSmTnDCTT = 1;
                    }
                    //MAP NGU HANH SON, TNSM x2
                    if (p.map.id >= 122 && p.map.id <= 124) {
                        upSmTnDCTT = upSmTnDCTT * 4;
                    }
                    //MAP THANH DIA GIAM TNSM
                    if (p.map.id >= 156 && p.map.id <= 159) {
                        upSmTnDCTT = (long) (upSmTnDCTT / 5);
                    }
                    //MAP KHI GAS GIAM TNSM
                    if (p.map.MapKhiGas()) {
                        upSmTnDCTT = (long) (upSmTnDCTT / 500);
                    }
                    upSmTnDCTT = upSmTnDCTT > 0 ? upSmTnDCTT : 1;
                    if (upSmTnDCTT > 0 && p.power < (p.getPowerLimit() * 1000000000L)) {
                        p.tiemNang += upSmTnDCTT;
                        p.power += upSmTnDCTT;
                        p.UpdateSMTN((byte) 2, upSmTnDCTT);
                        if (p.clan != null) {
                            upTNMemBerClanInMap(p, upSmTnDCTT);
                        }
                    }
                    //SET PLAYER TARGET CHO MOB
                    mob.pTarget = p;

                    mob.updateHP(-damageDCTT);
                    if (mob.isDie) {
                        ArrayList<ItemMap> itemDrop = new ArrayList<>();
                        MobStartDie(p, damageDCTT, mob, true, itemDrop);
                        //CHECK INIT BOSS MAP KHI GAS
                        Service.gI().initLychee(p);
                    } else {
                        attachedMob(damageDCTT, mob.tempId, true); //player dame mob
                        if (mob.template.tempId != 0 && (!mob.isFreez) && (!mob.isBlind) && (!mob.isDCTT) && (!mob.isSleep) && mob.isboss) {
                            loadMobAttached(mob.tempId, p.id, 0); //mob dame player
                        }
                        //dame hut mau
                        sendHutHP(p, damageDCTT, true);
                        //dame hut mau
                        //dame hut ki
                        sendHutKI(p, damageDCTT);
                        //dame hut ki
                    }
                    return;
                }
                //vua dung DCTT THI KHONG MISS SKILL
                if (p.noMiss) {
                    p.noMiss = false;
                    miss = 7;
                }
                if (miss < 8) {
                    damage = Util.nextInt((int) (p.getDamFull() * 0.9 * Util.getPercentDouble(skillPlayerUse.damage)), (int) (p.getDamFull() * Util.getPercentDouble(skillPlayerUse.damage)));
                }
                int kiUse = skillPlayerUse.manaUse;
                if (p.mp >= kiUse && !p.isdie && !mob.isDie && mob.hp > 0 && !p.checkPlayerBiKhongChe() && (_timeNOW - skillPlayerUse.lastTimeUseThisSkill > (long) (skillPlayerUse.coolDown)) && (_timeNOW >= p.timeCanSkill)) {
                    //CHECK NOI TAI LAZE CO GIAM THOI GIAN HOI CHIEU
                    if (p.noiTai.id != 0 && skillPlayerUse.template.id == p.noiTai.idSkill && p.idSkillselect == 11) { //LAZE
                        long _timeDOWN = (long) (Util.getPercentDouble((int) (p.noiTai.param)) * skillPlayerUse.coolDown);
                        skillPlayerUse.lastTimeUseThisSkill = _timeNOW - _timeDOWN;
                        //SEND TIME DOWN NOI TAI
                        Service.gI().sendCoolDownSkill(p, skillPlayerUse.skillId, (int) ((long) skillPlayerUse.coolDown - _timeDOWN));
                    } else {
                        skillPlayerUse.lastTimeUseThisSkill = _timeNOW;//gan time hien tai
                        if (skillPlayerUse.template.id == (byte) 1 || skillPlayerUse.template.id == (byte) 3 || skillPlayerUse.template.id == (byte) 5 || skillPlayerUse.template.id == (byte) 11) {
                            p.timeCanSkill = _timeNOW + (long) (500);
                        } else {
                            p.timeCanSkill = _timeNOW + (long) (skillPlayerUse.coolDown);
                        }
                    }
                    //CHECK KAIOKEN DE TRU 10% HP
                    if (skillPlayerUse.template.id == 9 && p.hp > (int) (p.getHpFull() * 0.1)) {
                        p.hp -= (int) (p.getHpFull() * 0.1);
                        postUpdateHP(p);
                    }
                    p.mp -= kiUse;
                    int fantashi = Util.nextInt(0, 100);
                    boolean fatal = p.getCritFull() >= fantashi;
                    //NEU BI TROI THI DAM AUTO CHI MANG
                    if (mob.isFreez || (p.noiTai.id != 0 && p.noiTai.idSkill == (byte) (-4) && p.hp <= (int) (p.getHpFull() * Util.getPercentDouble((int) p.noiTai.param)))) { //CHECK NOI TAI crit
                        fatal = true;
                    }
                    if (fatal || p.PEMCRIT > 0) {
                        damage = damage * 2;
                        p.PEMCRIT = 0;
                        fatal = true;
                    }
                    //CHECK BUA MANH ME
                    if (p.getBuaManhMe()) {
                        damage = (int) (damage * 1.5);
                    }
                    // GIAM DAME SAU KHI BI THOI MIEN
                    if (p.downDAME > 0) {
                        damage -= (int) (damage * Util.getPercentDouble((int) p.downDAME));
                    }
                    //CHECK DAME % TANG LEN SAU KHI DUNG SKILL VA CO NOI TAIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
                    if (p.upDameAfterNoiTai && p.noiTai.id != 0) {//% DAME DON KE TIEP
                        p.upDameAfterNoiTai = false;
                        damage += (int) (damage * Util.getPercentDouble((int) p.noiTai.param));
                    } else if (skillPlayerUse.template.id == p.noiTai.idSkill) { //%DAM CHIEU DAM VA CHUONG
                        damage += (int) (damage * Util.getPercentDouble((int) p.noiTai.param));
                    } else if (p.upDameAfterKhi) { //HOA KHI TANG DAME
                        damage += (int) (damage * Util.getPercentDouble((int) p.noiTai.param));
                    }
                    //CHECK SET KICH HOAT SONGOKU
                    if (p.getSetKichHoatFull() == (byte) 3 && skillPlayerUse.template.id == (byte) 1) {
                        damage = damage * 2;
                    } else if (p.getSetKichHoatFull() == (byte) 7 && skillPlayerUse.template.id == (byte) 4) { //SET KICH HOAT KAKAROT
                        damage = damage * 2;
                    } else if (p.getSetKichHoatFull() == (byte) 5 && skillPlayerUse.template.id == (byte) 17) { //SET KICH HOAT OC TIEU
                        damage = damage * 2;
                    }
                    //CHECK DAME TANG DO HIEU UNG CAI TRANG
                    damage = p.dameUpByCaiTrang(damage);
                    //END CHECK SET KICH HOAT SONGOKU
                    //CHECK CAI TRANG X3,X4 CHUONG MOI PHUT
                    if ((System.currentTimeMillis() >= p.timeX3X4) && p.isChuongX3X4) {
                        p.isChuongX3X4 = false;
                    }
                    if (p.ItemBody[5] != null && (p.ItemBody[5].template.id == 710 || p.ItemBody[5].template.id == 711) && !p.isChuongX3X4 && (System.currentTimeMillis() >= p.timeX3X4)) {
                        int perX3X4 = Util.nextInt(0, 10);
                        if (perX3X4 <= 1) {
                            p.isChuongX3X4 = true;
                            p.timeX3X4 = System.currentTimeMillis() + 60000;
                            if (p.ItemBody[5].template.id == 710) {
                                damage = damage * 3;
                            } else if (p.ItemBody[5].template.id == 711) {
                                damage = damage * 4;
                            }
                        }
                    }
                    //CHECK TANG TIME GIAP LUYEN TAP
                    Service.gI().upTimeGLT(p);
                    // dame laze
                    if (p.idSkillselect == 11) {
                        damage = (int) (p.mp * Util.getPercentDouble((int) skillPlayerUse.damage));
                        //CHECK SET KICH HOAT LAZE
                        if (p.getSetKichHoatFull() == (byte) 4) {
                            damage = (int) (damage * 1.5);
                        }
                        p.mp = 0;
                    } else {
                        //CHECK BUA BAT TU
                        if (p.hp == 1 && p.getBuaBatTu() || p.hp == 1 && p.getSetKichHoatFull() == (byte) 6) {
                            damage = 0;
                            p.sendAddchatYellow("Bạn đang được bùa bất tử bảo vệ, không thể tấn công");
                        } else if (damage >= mob.maxHp && mob.hp == mob.maxHp) {
                            damage = mob.maxHp - 1;
                        } else if (damage >= mob.hp) {
                            damage = mob.hp;
                        } else if (mob.hp == 1) {
                            damage = 1;
                        }

                        long upSmTn = (long) (damage / 2);
//                        long upSmTn = (long) (damage * 10);
                        upSmTn = p.getPercentUpTnSm(upSmTn); //get tnsm spl tnsm
                        if (p.cPk > 0 && p.cPk < 12 && p.cPk != 8) { //flag
                            upSmTn = (int) (upSmTn * 1.05);
                        } else if (p.cPk == 8) {
                            upSmTn = (int) (upSmTn * 1.1);
                        }
                        upSmTn = upSmTn * p.getBuaTNSM(); //bua tnsm
                        //CHECK NOI TANG TNSM
                        if (p.noiTai.id != 0 && p.noiTai.idSkill == (byte) (-3)) {
                            upSmTn += (long) (upSmTn * Util.getPercentDouble((int) p.noiTai.param));
                        }
                        //END NOI TAI TANG TNSM
                        upSmTn = (long) (upSmTn / 2);
                        if (p.power >= 40000000000L) {
                            upSmTn = (long) (upSmTn / 5);
                        } else if (p.power >= 50000000000L) {
                            upSmTn = (long) (upSmTn / 7);
                        } else if (p.power >= 60000000000L) {
                            upSmTn = (long) (upSmTn / 8);
                        } else if (p.power >= 70000000000L) {
                            upSmTn = (long) (upSmTn / 10);
                        }
                        //MAP NGU HANH SON, TNSM x2
                        if (p.map.id >= 122 && p.map.id <= 124) {
                            upSmTn = upSmTn * 4;
                        }
                        //MAP THANH DIA GIAM TNSM
                        if (p.map.id >= 156 && p.map.id <= 159) {
                            upSmTn = (long) (upSmTn / 5);
                        }
                        //MAP KHI GAS GIAM TNSM
                        if (p.map.MapKhiGas()) {
                            upSmTn = (long) (upSmTn / 500);
                        }
                        if (mob.template.tempId == 0) {
                            damage = 10;
                            upSmTn = 1;
                        }
                        if (upSmTn < 1) {
                            upSmTn = 1;
                        }
                        upSmTn = upSmTn > 0 ? upSmTn : 1;
                        if (upSmTn > 0 && p.power < (p.getPowerLimit() * 1000000000L)) {
                            p.tiemNang += upSmTn;
                            p.power += upSmTn;
                            p.UpdateSMTN((byte) 2, upSmTn);
                            if (p.clan != null) {
                                upTNMemBerClanInMap(p, upSmTn);
                            }
                            //CHECK NHIEM VU LEN SUC MANH
                            if (p.taskId == (short) 7 && p.crrTask.index == (byte) 0 && p.power >= (long) 16000) {
                                TaskService.gI().updateCountTask(p);
                            } else if (p.taskId == (short) 8 && p.crrTask.index == (byte) 0 && p.power >= (long) 40000) {
                                TaskService.gI().updateCountTask(p);
                            } else if (p.taskId == (short) 14 && p.crrTask.index == (byte) 0 && p.power >= (long) 200000) {
                                TaskService.gI().updateCountTask(p);
                            } else if (p.taskId == (short) 15 && p.crrTask.index == (byte) 0 && p.power >= (long) 500000) {
                                TaskService.gI().updateCountTask(p);
                            } else if (p.taskId == (short) 17 && p.crrTask.index == (byte) 0 && p.power >= (long) 1500000) {
                                TaskService.gI().updateCountTask(p);
                            } else if (p.taskId == (short) 18 && p.crrTask.index == (byte) 0 && p.power >= (long) 5000000) {
                                TaskService.gI().updateCountTask(p);
                            } else if (p.taskId == (short) 19 && p.crrTask.index == (byte) 0 && p.power >= (long) 15000000) {
                                TaskService.gI().updateCountTask(p);
                            } else if (p.taskId == (short) 20 && p.crrTask.index == (byte) 0 && p.power >= (long) 50000000) {
                                TaskService.gI().updateCountTask(p);
                            }
                        }
                    }
                    //SET PLAYER TARGET CHO MOB
                    mob.pTarget = p;

                    mob.updateHP(-damage);
                    if (mob.isDie) {
                        //CHECK UPDATE NHIEM VU
                        //CAC NHIEM VU DANH QUAI
                        if (p.taskId != (short) 8 && p.taskId != (short) 14 && p.taskId != (short) 2 && p.taskId != (short) 30) {
                            if (p.taskId == (short) 13 || p.taskId == (short) 15 || p.taskId == (short) 25 || p.taskId == (short) 27 || p.taskId == (short) 28) {
//                            if(checkMemberClanInMap(p)) {
                                if (mob.template.tempId == TaskManager.gI().mobTASK0[p.taskId][p.crrTask.index] && p.gender == (byte) 0) {
                                    TaskService.gI().updateCountTask(p);
                                } else if (mob.template.tempId == TaskManager.gI().mobTASK1[p.taskId][p.crrTask.index] && p.gender == (byte) 1) {
                                    TaskService.gI().updateCountTask(p);
                                } else if (mob.template.tempId == TaskManager.gI().mobTASK2[p.taskId][p.crrTask.index] && p.gender == (byte) 2) {
                                    TaskService.gI().updateCountTask(p);
                                }
//                            }
                            } else if (p.taskId == (short) 31) {
                                if (p.crrTask.index == (byte) 3 && (mob.template.tempId == 80 || mob.template.tempId == 81)) {
                                    TaskService.gI().updateCountTask(p);
                                } else if (p.crrTask.index == (byte) 8 && (mob.template.tempId == 80 || mob.template.tempId == 81) && p.map.id == 161) {
                                    TaskService.gI().updateCountTask(p);
                                }
                            } else if (p.taskId == (short) 32) {
                                if (p.crrTask.index == (byte) 4 && (mob.template.tempId == 80 || mob.template.tempId == 81) && p.map.id == 162) {
                                    TaskService.gI().updateCountTask(p);
                                }
                            } else {
                                if (mob.template.tempId == TaskManager.gI().mobTASK0[p.taskId][p.crrTask.index] && p.gender == (byte) 0) {
                                    TaskService.gI().updateCountTask(p);
                                } else if (mob.template.tempId == TaskManager.gI().mobTASK1[p.taskId][p.crrTask.index] && p.gender == (byte) 1) {
                                    TaskService.gI().updateCountTask(p);
                                } else if (mob.template.tempId == TaskManager.gI().mobTASK2[p.taskId][p.crrTask.index] && p.gender == (byte) 2) {
                                    TaskService.gI().updateCountTask(p);
                                }
                            }
                        }
                        //check TRUNG MABU
                        if (mob.template.tempId == 70 && mob.typeHiru == (byte) 2) {
                            int rdMabu = Util.nextInt(0, 5);
                            if (rdMabu < 1) {
                                p.hasTrungMabu = true;
                                p.sendAddchatYellow("Bạn vừa nhận được đệ tử Mabư, quay về nhà gặp Ông Già để thao tác");
                            }
                        }
                        //CHECK INIT BOSS MAP KHI GAS
                        Service.gI().initLychee(p);

                        //CHECK UPDATE NHIEM VU
                        ArrayList<ItemMap> itemDrop = new ArrayList<>();
                        if (p.taskId == (short) 2) {
                            if ((p.gender == (byte) 0 && mob.template.tempId == TaskManager.gI().mobTASK0[p.taskId][p.crrTask.index])
                                    || (p.gender == (byte) 1 && mob.template.tempId == TaskManager.gI().mobTASK1[p.taskId][p.crrTask.index])
                                    || (p.gender == (byte) 2 && mob.template.tempId == TaskManager.gI().mobTASK2[p.taskId][p.crrTask.index])) {
                                Item itemMap = ItemSell.getItemNotSell(73);
                                ItemMap item = new ItemMap();
                                item.playerId = p.id;
                                item.x = mob.pointX;
                                item.y = mob.pointY;
//                            item.itemMapID = 73;
                                item.itemMapID = itemsMap.size();
//                            item.itemTemplateID = (short)item.itemMapID;
                                item.itemTemplateID = (short) 73;
                                itemMap.template = ItemTemplate.ItemTemplateID(73);
                                item.item = itemMap;
                                itemDrop.add(item);
                                itemsMap.addAll(itemDrop);
                            }
                        } else if (mob.isSocola > 0) {
                            Item itemMap = ItemSell.getItemNotSell(516);
                            ItemMap item = new ItemMap();
                            item.playerId = p.id;
                            item.x = mob.pointX;
                            item.y = mob.pointY;
//                        item.itemMapID = 516;
                            item.itemMapID = itemsMap.size();
//                        item.itemTemplateID = (short) item.itemMapID;
                            item.itemTemplateID = (short) 516;
                            itemMap.template = ItemTemplate.ItemTemplateID(516);
                            item.item = itemMap;
                            itemDrop.add(item);
                            itemsMap.addAll(itemDrop);
                        } else {
                            if (mob.template.tempId != 0) {
                                int idItemNotSell[] = {17, 188, 189, 190, 18, 19, 20, 441, 442, 443, 444, 445, 446, 447, 17, 188, 189, 190, 225, 17, 188, 189, 190, 18, 19, 20, 76, 188, 189, 190, 18, 19, 20, 85, 993};
                                int idItemMap[] = {18, 19, 20, 188, 189, 190, 220, 221, 222, 223, 224, 225};
                                int percentDrop = Util.nextInt(0, 10);
                                int percentDropItemGod = Util.nextInt(0, 10000);
                                int percentDropKichHoat = Util.nextInt(0, 10000);
                                int perTA = Util.nextInt(0, 1000);
                                int perSPL = Util.nextInt(0, 600);
//                                int percentDropItemGod = Util.nextInt(0, 1000);
//                                int percentDropKichHoat = Util.nextInt(0, 1000);
//                                int perTA = Util.nextInt(0, 1000);
//                                int perSPL = Util.nextInt(0, 1000);
                                if (percentDropKichHoat < 100 && map.MapSetKichHoat()) { //ITEM ROT LA ITEM KICH HOAT
                                    int id = Util.nextInt(0, 5); //RANDOM TU 0,1,2,3,4
                                    int idItemKichHoat[][] = {{0, 6, 21, 27, 12}, {1, 7, 22, 28, 12}, {2, 8, 23, 29, 12}};
                                    int optionItemKichHoat[][] = {{127, 128, 129, 139, 140, 141}, {130, 131, 132, 142, 143, 144}, {133, 134, 135, 136, 137, 138}};
                                    ItemSell sellKichHoat = ItemSell.getItemSell(idItemKichHoat[(int) p.gender][id], (byte) 0);
                                    ItemMap itemROI = new ItemMap();
                                    itemROI.playerId = p.id;
                                    itemROI.x = mob.pointX;
                                    itemROI.y = mob.pointY;
                                    itemROI.itemMapID = itemsMap.size();
                                    itemROI.itemTemplateID = (short) (idItemKichHoat[(int) p.gender][id]);
                                    Item _ITEMKICHHOAT = new Item(sellKichHoat.item);
                                    int rdOptionKichHoat = Util.nextInt(0, 3); //RANDOM 0,1,2
                                    _ITEMKICHHOAT.itemOptions.add(new ItemOption(optionItemKichHoat[(int) p.gender][rdOptionKichHoat], 0));
                                    _ITEMKICHHOAT.itemOptions.add(new ItemOption(optionItemKichHoat[(int) p.gender][rdOptionKichHoat + 3], 0));
                                    _ITEMKICHHOAT.itemOptions.add(new ItemOption(30, 0)); //OPTION KHONG THE GIAO DICH
                                    if (perSPL < 100) {
                                        _ITEMKICHHOAT.itemOptions.add(new ItemOption(107, Util.nextInt(1, 4)));
                                    }
                                    itemROI.item = _ITEMKICHHOAT;
                                    itemDrop.add(itemROI);
                                    itemsMap.addAll(itemDrop);
                                } else if (percentDropKichHoat > 100 && percentDropKichHoat < 1000 && map.MapSetKichHoat()) { //ITEM ROT LA ITEM KICH HOAT
                                    int id = Util.nextInt(0, 5); //RANDOM TU 0,1,2,3,4
                                    int idItemKichHoat[][] = {{0, 6, 21, 27, 12}, {1, 7, 22, 28, 12}, {2, 8, 23, 29, 12}};
                                    ItemSell sellKichHoat = ItemSell.getItemSell(idItemKichHoat[(int) p.gender][id], (byte) 0);
                                    ItemMap itemROI = new ItemMap();
                                    itemROI.playerId = p.id;
                                    itemROI.x = mob.pointX;
                                    itemROI.y = mob.pointY;
                                    itemROI.itemMapID = itemsMap.size();
                                    itemROI.itemTemplateID = (short) (idItemKichHoat[(int) p.gender][id]);
                                    Item _ITEMKICHHOAT = new Item(sellKichHoat.item);
                                    int percentSaoPhaLe = Util.nextInt(0, 100);
                                    if (perSPL < 100) {
                                        _ITEMKICHHOAT.itemOptions.add(new ItemOption(107, Util.nextInt(1, 4)));
                                    }
                                    itemROI.item = _ITEMKICHHOAT;
                                    itemDrop.add(itemROI);
                                    itemsMap.addAll(itemDrop);
                                } else if (percentDrop < 3 && map.MapCell() && p.useMayDoCapsule) { //ITEM ROT KHI DUNG RADA DO CAPSULE
                                    int idItemNotSell2[] = {17, 225, 18, 19, 20, 441, 442, 443, 444, 445, 446, 447, 17, 188, 189, 190, 225, 17, 225, 18, 19, 20, 441, 442, 443, 444, 445, 446, 447, 76, 188, 189, 190, 225, 380};
                                    int id = Util.nextInt(0, 35);
                                    if (id >= 30) {
                                        id = 34;
                                    }
                                    Item itemMap = ItemSell.getItemNotSell(idItemNotSell2[id]);
                                    ItemMap item = new ItemMap();
                                    item.playerId = p.id;
                                    item.x = mob.pointX;
                                    item.y = mob.pointY;
                                    item.itemMapID = itemsMap.size();
                                    item.itemTemplateID = (short) idItemNotSell2[id];
                                    itemMap.template = ItemTemplate.ItemTemplateID(idItemNotSell2[id]);
                                    item.item = itemMap;
                                    itemDrop.add(item);
                                    itemsMap.addAll(itemDrop);
                                } else if (percentDropItemGod < 10 && map.MapCold()) {
                                    ItemSell itemGOD = null;
                                    int _ITEMMAPID = -1;
                                    int idItemGod[] = {555, 556, 561, 562, 563, 557, 558, 561, 564, 565, 559, 560, 561, 566, 567};
                                    percentDropItemGod = Util.nextInt(0, 15);
                                    itemGOD = ItemSell.getItemSell(idItemGod[percentDropItemGod], (byte) 1);
                                    _ITEMMAPID = idItemGod[percentDropItemGod];
                                    if (_ITEMMAPID >= 555 && _ITEMMAPID <= 567) {
                                        ItemMap itemROI = new ItemMap();
                                        itemROI.playerId = p.id;
                                        itemROI.x = mob.pointX;
                                        itemROI.y = mob.pointY;
                                        itemROI.itemMapID = itemsMap.size();
                                        itemROI.itemTemplateID = (short) _ITEMMAPID;
                                        Item _ITEMGOD = new Item(itemGOD.item);
                                        _ITEMGOD.itemOptions.clear();
                                        if (_ITEMGOD.template.id == 555 || _ITEMGOD.template.id == 557 || _ITEMGOD.template.id == 559) { //AO THAN
                                            _ITEMGOD.itemOptions.add(new ItemOption(47, Util.nextInt(500, 1001)));
                                        } else if (_ITEMGOD.template.id == 556) { //QUAN THAN TRAI DAT
                                            _ITEMGOD.itemOptions.add(new ItemOption(6, (Util.nextInt(350, 551) * 100)));
                                            _ITEMGOD.itemOptions.add(new ItemOption(27, Util.nextInt(9000, 10000)));
                                        } else if (_ITEMGOD.template.id == 558) {//QUAN THAN NAMEK
                                            _ITEMGOD.itemOptions.add(new ItemOption(6, (Util.nextInt(350, 501) * 100)));
                                            _ITEMGOD.itemOptions.add(new ItemOption(27, Util.nextInt(9000, 9500)));
                                        } else if (_ITEMGOD.template.id == 560) { //QUAN THAN XAYDA
                                            _ITEMGOD.itemOptions.add(new ItemOption(6, (Util.nextInt(350, 601) * 100)));
                                            _ITEMGOD.itemOptions.add(new ItemOption(27, Util.nextInt(9500, 10500)));
                                        } else if (_ITEMGOD.template.id == 562) { //GANG THAN TD
                                            _ITEMGOD.itemOptions.add(new ItemOption(0, (Util.nextInt(300, 571) * 10)));
                                        } else if (_ITEMGOD.template.id == 564) { //GANG THAN NAMEK
                                            _ITEMGOD.itemOptions.add(new ItemOption(0, (Util.nextInt(300, 551) * 10)));
                                        } else if (_ITEMGOD.template.id == 566) { //GANG THAN XAYDA
                                            _ITEMGOD.itemOptions.add(new ItemOption(0, (Util.nextInt(300, 601) * 10)));
                                        } else if (_ITEMGOD.template.id == 563) { //GIAY THAN TD
                                            _ITEMGOD.itemOptions.add(new ItemOption(7, (Util.nextInt(350, 551) * 100)));
                                            _ITEMGOD.itemOptions.add(new ItemOption(28, Util.nextInt(9000, 10000)));
                                        } else if (_ITEMGOD.template.id == 565) { //GIAY THAN NAMEK
                                            _ITEMGOD.itemOptions.add(new ItemOption(7, (Util.nextInt(350, 601) * 100)));
                                            _ITEMGOD.itemOptions.add(new ItemOption(28, Util.nextInt(9500, 10500)));
                                        } else if (_ITEMGOD.template.id == 567) { //GIAY THAN XAYDA
                                            _ITEMGOD.itemOptions.add(new ItemOption(7, (Util.nextInt(350, 501) * 100)));
                                            _ITEMGOD.itemOptions.add(new ItemOption(28, Util.nextInt(9000, 9500)));
                                        } else { //NHAN THAN LINH
                                            _ITEMGOD.itemOptions.add(new ItemOption(14, Util.nextInt(10, 16)));
                                        }
                                        if (perSPL < 10) {//RA THEM SAO PHA LE
                                            _ITEMGOD.itemOptions.add(new ItemOption(107, Util.nextInt(3, 7)));
                                        } else if (perSPL > 10 && perSPL < 300) {
                                            _ITEMGOD.itemOptions.add(new ItemOption(107, Util.nextInt(1, 4)));
                                        }
                                        _ITEMGOD.itemOptions.add(new ItemOption(21, 17));
                                        itemROI.item = _ITEMGOD;
                                        itemDrop.add(itemROI);
                                        itemsMap.addAll(itemDrop);
                                    }
                                } else if (perTA < 60 && p.checkFullSetThan() && map.MapCold()) {
                                    int idThucAn[] = {663, 664, 665, 666, 667};
                                    perTA = Util.nextInt(0, 5);
                                    Item itemMap = ItemSell.getItemNotSell(idThucAn[perTA]);
                                    ItemMap item = new ItemMap();
                                    item.playerId = p.id;
                                    item.x = mob.pointX;
                                    item.y = mob.pointY;
                                    item.itemMapID = itemsMap.size();
                                    item.itemTemplateID = (short) idThucAn[perTA];
                                    itemMap.template = ItemTemplate.ItemTemplateID(idThucAn[perTA]);
                                    item.item = itemMap;
                                    itemDrop.add(item);
                                    itemsMap.addAll(itemDrop);
                                } else if (perTA < 300 && map.MapFarmThienSu() && p.checkSetByLevel((byte) 14)) {
                                    int idThienSu[] = {1066, 1067, 1068, 1069, 1070};
                                    perTA = Util.nextInt(0, 5);
                                    Item itemMap = ItemSell.getItemNotSell(idThienSu[perTA]);
                                    ItemMap item = new ItemMap();
                                    item.playerId = p.id;
                                    item.x = mob.pointX;
                                    item.y = mob.pointY;
                                    item.itemMapID = itemsMap.size();
                                    item.itemTemplateID = (short) idThienSu[perTA];
                                    itemMap.template = ItemTemplate.ItemTemplateID(idThienSu[perTA]);
                                    item.item = itemMap;
                                    itemDrop.add(item);
                                    itemsMap.addAll(itemDrop);
                                } else if (perTA < 500 && map.MapThanhDia()) {
                                    int id = Util.nextInt(0, 40);
                                    if (id >= 20) {
                                        if (map.MapThanhDia1()) {
                                            id = 933;
                                        } else if (map.MapThanhDia2()) {
                                            id = 934;
                                        }
                                    } else {
                                        id = idItemNotSell[id];
                                    }
                                    Item itemMap = ItemSell.getItemNotSell(id);
                                    Item itemPorata = new Item(itemMap);
                                    itemPorata.isDrop = (byte) 1;
                                    ItemMap item = new ItemMap();
                                    item.playerId = p.id;
                                    item.x = mob.pointX;
                                    item.y = mob.pointY;
//                                item.itemMapID = id;
//                                item.itemTemplateID = (short) item.itemMapID;
                                    item.itemMapID = itemsMap.size();
                                    item.itemTemplateID = (short) id;
                                    itemPorata.template = ItemTemplate.ItemTemplateID(id);
                                    item.item = itemPorata;
                                    itemDrop.add(item);
                                    itemsMap.addAll(itemDrop);
                                } else if (percentDrop < 3) {
//                                int id = Util.nextInt(2,19);
                                    int id = Util.nextInt(0, 33);
                                    //                    Item itemMap = ItemSell.getItem(id);
                                    if (p.taskId != (short) 31) {
                                        if (p.gender == (byte) 0) {
//                                            if (p.taskId == (short) 8 && id < 9 && mob.template.tempId == TaskManager.gI().mobTASK0[p.taskId][p.crrTask.index]) {
                                            if (p.taskId == (short) 8 && id < 20 && mob.template.tempId == TaskManager.gI().mobTASK0[p.taskId][p.crrTask.index]) {
                                                id = 6; //NHIEM VU NGOC RONG 7SAO
                                            } else if (p.taskId == (short) 14 && id < 20 && mob.template.tempId == TaskManager.gI().mobTASK0[p.taskId][p.crrTask.index]) {
                                                id = 33; //NHIEM VU TRUYEN TRANH DOREMON TAP 2
                                            }
                                        } else if (p.gender == (byte) 1) {
                                            if (p.taskId == (short) 8 && id < 20 && mob.template.tempId == TaskManager.gI().mobTASK1[p.taskId][p.crrTask.index]) {
                                                id = 6; //NHIEM VU NGOC RONG 7SAO
                                            } else if (p.taskId == (short) 14 && id < 20 && mob.template.tempId == TaskManager.gI().mobTASK1[p.taskId][p.crrTask.index]) {
                                                id = 33; //NHIEM VU TRUYEN TRANH DOREMON TAP 2
                                            }
                                        } else if (p.gender == (byte) 2) {
                                            if (p.taskId == (short) 8 && id < 20 && mob.template.tempId == TaskManager.gI().mobTASK2[p.taskId][p.crrTask.index]) {
                                                id = 6; //NHIEM VU NGOC RONG 7SAO
                                            } else if (p.taskId == (short) 14 && id < 20 && mob.template.tempId == TaskManager.gI().mobTASK2[p.taskId][p.crrTask.index]) {
                                                id = 33; //NHIEM VU TRUYEN TRANH DOREMON TAP 2
                                            }
                                        }
                                    } else {
                                        if (p.crrTask.index == (byte) 7 && p.map.id == 161) {
                                            id = 34; //NHIEM VU NHAT THUC ĂN CHO BARDOCK
                                        }
                                    }
                                    Item itemMap = ItemSell.getItemNotSell(idItemNotSell[id]);
                                    ItemMap item = new ItemMap();
                                    item.playerId = p.id;
                                    item.x = mob.pointX;
                                    item.y = mob.pointY;
//                                item.itemMapID = idItemNotSell[id];
//                                item.itemTemplateID = (short) item.itemMapID;
                                    item.itemMapID = itemsMap.size();
                                    item.itemTemplateID = (short) idItemNotSell[id];
                                    itemMap.template = ItemTemplate.ItemTemplateID(idItemNotSell[id]);
                                    item.item = itemMap;
                                    itemDrop.add(item);
                                    itemsMap.addAll(itemDrop);

                                    //CHECK NHIEM VU TIM NGOC RONG 7 SAO
                                    if (p.gender == (byte) 0) {
                                        if (p.taskId == (short) 8 && p.crrTask.index == (byte) 1 && itemMap.template.id == 20 && mob.template.tempId == TaskManager.gI().mobTASK0[p.taskId][p.crrTask.index]) {
                                            TaskService.gI().updateCountTask(p);
                                        } else if (p.taskId == (short) 14 && p.crrTask.index == (byte) 1 && itemMap.template.id == 85 && mob.template.tempId == TaskManager.gI().mobTASK0[p.taskId][p.crrTask.index]) {
                                            TaskService.gI().updateCountTask(p);
                                        }
                                    } else if (p.gender == (byte) 1) {
                                        if (p.taskId == (short) 8 && p.crrTask.index == (byte) 1 && itemMap.template.id == 20 && mob.template.tempId == TaskManager.gI().mobTASK1[p.taskId][p.crrTask.index]) {
                                            TaskService.gI().updateCountTask(p);
                                        } else if (p.taskId == (short) 14 && p.crrTask.index == (byte) 1 && itemMap.template.id == 85 && mob.template.tempId == TaskManager.gI().mobTASK1[p.taskId][p.crrTask.index]) {
                                            TaskService.gI().updateCountTask(p);
                                        }
                                    } else if (p.gender == (byte) 2) {
                                        if (p.taskId == (short) 8 && p.crrTask.index == (byte) 1 && itemMap.template.id == 20 && mob.template.tempId == TaskManager.gI().mobTASK2[p.taskId][p.crrTask.index]) {
                                            TaskService.gI().updateCountTask(p);
                                        } else if (p.taskId == (short) 14 && p.crrTask.index == (byte) 1 && itemMap.template.id == 85 && mob.template.tempId == TaskManager.gI().mobTASK2[p.taskId][p.crrTask.index]) {
                                            TaskService.gI().updateCountTask(p);
                                        }
                                    }
                                }
                            }
                        }
                        //đồng bộ mob chết
                        MobStartDie(p, damage, mob, fatal, itemDrop);
                    } else {
                        attachedMob(damage, mob.tempId, fatal); //player dame mob
                        if (mob.template.tempId != 0 && (!mob.isFreez) && (!mob.isBlind) && (!mob.isDCTT) && (!mob.isSleep) && mob.isboss) {
                            loadMobAttached(mob.tempId, p.id, 0); //mob dame player
                        }
                        //dame hut mau
                        sendHutHP(p, damage, true);
                        //dame hut mau
                        //dame hut ki
                        sendHutKI(p, damage);
                        //dame hut ki
                    }

                    //CHIM ATTACK MOB
                    if (p.chimFollow == (byte) 1 && !mob.isDie && p.dameChim > 0) {
                        //DAME CHIM
                        damage = (int) (damage * Util.getPercentDouble(p.dameChim));
                        if (p.getSetKichHoatFull() == (byte) 6) {
                            damage = (int) (damage * 2);
                        }
                        damage = damage > 1 ? damage : 1;
                        long upSmTn = (long) (damage / 2) > 1 ? (long) (damage / 2) : 1;
                        upSmTn = p.getPercentUpTnSm(upSmTn); //get tnsm spl tnsm
                        if (p.cPk > 0 && p.cPk < 12 && p.cPk != 8) { //flag
                            upSmTn = (int) (upSmTn * 1.05);
                        } else if (p.cPk == 8) {
                            upSmTn = (int) (upSmTn * 1.1);
                        }
                        upSmTn = upSmTn * p.getBuaTNSM(); //bua tnsm
                        //CHECK NOI TANG TNSM
                        if (p.noiTai.id != 0 && p.noiTai.idSkill == (byte) (-3)) {
                            upSmTn += (long) (upSmTn * Util.getPercentDouble((int) p.noiTai.param));
                        }
                        //MAP NGU HANH SON, TNSM x2
                        if (p.map.id >= 122 && p.map.id <= 124) {
                            upSmTn = upSmTn * 4;
                        }
                        //MAP THANH DIA GIAM TNSM
                        if (p.map.id >= 156 && p.map.id <= 159) {
                            upSmTn = (long) (upSmTn / 5);
                        }
                        //MAP KHI GAS GIAM TNSM
                        if (p.map.MapKhiGas()) {
                            upSmTn = (long) (upSmTn / 500);
                        }
                        //END NOI TAI TANG TNSM
                        upSmTn = (long) (upSmTn / 2);
                        if (p.power >= 40000000000L) {
                            upSmTn = (long) (upSmTn / 5);
                        } else if (p.power >= 50000000000L) {
                            upSmTn = (long) (upSmTn / 7);
                        } else if (p.power >= 60000000000L) {
                            upSmTn = (long) (upSmTn / 8);
                        } else if (p.power >= 70000000000L) {
                            upSmTn = (long) (upSmTn / 10);
                        }
                        if (mob.template.tempId == 0) {
                            damage = 10;
                            upSmTn = 1;
                        }
                        if (upSmTn < 1) {
                            upSmTn = 1;
                        }
                        upSmTn = upSmTn > 0 ? upSmTn : 1;
                        if (upSmTn > 0 && p.power < (p.getPowerLimit() * 1000000000L)) {
                            p.tiemNang += upSmTn;
                            p.power += upSmTn;
                            p.UpdateSMTN((byte) 2, upSmTn);
                            if (p.clan != null) {
                                upTNMemBerClanInMap(p, upSmTn);
                            }
                            //CHECK NHIEM VU LEN SUC MANH
                            if (p.taskId == (short) 7 && p.crrTask.index == (byte) 0 && p.power >= (long) 16000) {
                                TaskService.gI().updateCountTask(p);
                            } else if (p.taskId == (short) 8 && p.crrTask.index == (byte) 0 && p.power >= (long) 40000) {
                                TaskService.gI().updateCountTask(p);
                            } else if (p.taskId == (short) 14 && p.crrTask.index == (byte) 0 && p.power >= (long) 200000) {
                                TaskService.gI().updateCountTask(p);
                            } else if (p.taskId == (short) 15 && p.crrTask.index == (byte) 0 && p.power >= (long) 500000) {
                                TaskService.gI().updateCountTask(p);
                            } else if (p.taskId == (short) 17 && p.crrTask.index == (byte) 0 && p.power >= (long) 1500000) {
                                TaskService.gI().updateCountTask(p);
                            } else if (p.taskId == (short) 18 && p.crrTask.index == (byte) 0 && p.power >= (long) 5000000) {
                                TaskService.gI().updateCountTask(p);
                            } else if (p.taskId == (short) 19 && p.crrTask.index == (byte) 0 && p.power >= (long) 15000000) {
                                TaskService.gI().updateCountTask(p);
                            } else if (p.taskId == (short) 20 && p.crrTask.index == (byte) 0 && p.power >= (long) 50000000) {
                                TaskService.gI().updateCountTask(p);
                            }
                        }
                        mob.updateHP(-damage);
                        if (mob.isDie) {
                            //CHECK UPDATE NHIEM VU
                            //CAC NHIEM VU DANH QUAI
                            if (p.taskId != (short) 8 && p.taskId != (short) 14 && p.taskId != (short) 2 && p.taskId != (short) 30) {
                                if (p.taskId == (short) 13 || p.taskId == (short) 15 || p.taskId == (short) 25 || p.taskId == (short) 27 || p.taskId == (short) 28) {
//                                if(checkMemberClanInMap(p)) {
                                    if (mob.template.tempId == TaskManager.gI().mobTASK0[p.taskId][p.crrTask.index] && p.gender == (byte) 0) {
                                        TaskService.gI().updateCountTask(p);
                                    } else if (mob.template.tempId == TaskManager.gI().mobTASK1[p.taskId][p.crrTask.index] && p.gender == (byte) 1) {
                                        TaskService.gI().updateCountTask(p);
                                    } else if (mob.template.tempId == TaskManager.gI().mobTASK2[p.taskId][p.crrTask.index] && p.gender == (byte) 2) {
                                        TaskService.gI().updateCountTask(p);
                                    }
//                                }
                                } else if (p.taskId == (short) 31) {
                                    if (p.crrTask.index == (byte) 3 && (mob.template.tempId == 80 || mob.template.tempId == 81)) {
                                        TaskService.gI().updateCountTask(p);
                                    } else if (p.crrTask.index == (byte) 8 && (mob.template.tempId == 80 || mob.template.tempId == 81) && p.map.id == 161) {
                                        TaskService.gI().updateCountTask(p);
                                    }
                                } else if (p.taskId == (short) 32) {
                                    if (p.crrTask.index == (byte) 4 && (mob.template.tempId == 80 || mob.template.tempId == 81) && p.map.id == 162) {
                                        TaskService.gI().updateCountTask(p);
                                    }
                                } else {
                                    if (mob.template.tempId == TaskManager.gI().mobTASK0[p.taskId][p.crrTask.index] && p.gender == (byte) 0) {
                                        TaskService.gI().updateCountTask(p);
                                    } else if (mob.template.tempId == TaskManager.gI().mobTASK1[p.taskId][p.crrTask.index] && p.gender == (byte) 1) {
                                        TaskService.gI().updateCountTask(p);
                                    } else if (mob.template.tempId == TaskManager.gI().mobTASK2[p.taskId][p.crrTask.index] && p.gender == (byte) 2) {
                                        TaskService.gI().updateCountTask(p);
                                    }
                                }
                            }
                            //check TRUNG MABU
                            if (mob.template.tempId == 70 && mob.typeHiru == (byte) 2) {
                                int rdMabu = Util.nextInt(0, 10);
                                if (rdMabu < 1) {
                                    p.hasTrungMabu = true;
                                    p.sendAddchatYellow("Bạn vừa nhận được đệ tử Mabư, quay về nhà gặp Ông Già để thao tác");
                                }
                            }
                            //CHECK INIT BOSS MAP KHI GAS
                            Service.gI().initLychee(p);

                            //CHECK UPDATE NHIEM VU
                            ArrayList<ItemMap> itemDrop2 = new ArrayList<>();
                            int percentDrop2 = Util.nextInt(0, 10);
                            if (mob.template.tempId != 0 && percentDrop2 < 3) {
                                int idItemNotSell2[] = {20, 19, 18, 225, 225, 225, 225, 225, 225, 220, 221, 222, 223, 224, 220, 221, 222, 223, 224, 17};
                                int id2 = Util.nextInt(0, 21);
                                Item itemMap2 = ItemSell.getItemNotSell(idItemNotSell2[id2]);
                                ItemMap item2 = new ItemMap();
                                item2.playerId = p.id;
                                item2.x = mob.pointX;
                                item2.y = mob.pointY;
                                item2.itemMapID = itemsMap.size();
                                item2.itemTemplateID = (short) idItemNotSell2[id2];
                                itemMap2.template = ItemTemplate.ItemTemplateID(idItemNotSell2[id2]);
                                item2.item = itemMap2;
                                itemDrop2.add(item2);
                                itemsMap.addAll(itemDrop2);
                            }
                            MobStartDie(p, damage, mob, fatal, itemDrop2);
                        } else {
                            chimDameMob(p, mob, damage);
                        }
                    }

                    //send player attack to all player in map
                    try {
                        m = new Message(54);
                        m.writer().writeInt(p.id);
                        if (p.isMonkey && (p.idSkillselect == 4)) {
                            m.writer().writeByte(105);
                        } else if (p.isMonkey && (p.idSkillselect == 5)) {
                            m.writer().writeByte(106);
                        } else {
                            m.writer().writeByte(skillPlayerUse.skillId);
                        }
                        m.writer().writeByte(mob.tempId);
                        for (byte i = 0; i < players.size(); i++) {
                            if (players.get(i) != null) {
                                players.get(i).session.sendMessage(m);
                            }
                        }
                        m.writer().flush();
                        m.cleanup();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //            if(p.isMonkey && (p.idSkillselect == 4 || p.idSkillselect == 5)) {
                    //                sendMonkeyAttack(p, mob, damage);
                    //            } else {
                    // ve skill cua player nay cho cac player cung khu khac
                    //                setSkillPaint(skillPlayerUse.skillId, p, 8);
                    //            }
                }
            }
        }
    }

    public void sendHutHP(Player _player, int _damage, boolean isAttackMob) {
        int dameHutHp = 0;
        if (isAttackMob) {
            dameHutHp = (int) (Util.getPercentDouble((int) _player.getPercentHutHp()) * _damage);
        } else if (!isAttackMob && _player.ItemBody[5] != null && _player.ItemBody[5].template.id == 448) {
            dameHutHp = (int) (Util.getPercentDouble((int) (_player.getPercentHutHp() - 50)) * _damage);
        } else {
            dameHutHp = (int) (Util.getPercentDouble((int) _player.getPercentHutHp()) * _damage);
        }
        _player.hp += dameHutHp; //hutmau;
        if (_player.hp > _player.getHpFull()) {
            _player.hp = _player.getHpFull();
        }

        Message ms = null;
        try {
            ms = new Message(-30);
            ms.writer().writeByte(5);
            ms.writer().writeInt(_player.hp);
            ms.writer().flush();
            _player.session.sendMessage(ms);
            ms.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postUpdateHP(Player _player) {
        try {
            Message ms = new Message(-30);
            ms.writer().writeByte(5);
            ms.writer().writeInt(_player.hp);
            ms.writer().flush();
            _player.session.sendMessage(ms);
            ms.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postUpdateKI(Player _player) {
        try {
            Message ms = new Message(-30);
            ms.writer().writeByte(6);
            ms.writer().writeInt(_player.mp);
            ms.writer().flush();
            _player.session.sendMessage(ms);
            ms.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendHutKI(Player _player, int _damage) {
        int dameHutKi = (int) (Util.getPercentDouble((int) _player.getPercentHutKi()) * _damage);
        _player.mp += dameHutKi; //hutmau
        if (_player.mp > _player.getMpFull()) {
            _player.mp = _player.getMpFull();
        }

        try {
            Message ms = new Message(-30);
            ms.writer().writeByte(6);
            ms.writer().writeInt(_player.mp);
            ms.writer().flush();
            _player.session.sendMessage(ms);
            ms.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMonkeyAttack(Player pl, Mob mob, int damage) {
        Message m = null;
        try {
            for (byte i = 0; i < players.size(); i++) {
                if (players.get(i) != null) {
                    m = new Message(-95);
                    m.writer().writeByte(5);
                    m.writer().writeInt(pl.id);
                    if (pl.idSkillselect == 4) {
                        m.writer().writeByte(105);
                    } else {
                        m.writer().writeByte(106);
                    }
                    m.writer().writeInt(mob.tempId);
                    m.writer().writeInt(damage);
                    m.writer().writeInt(mob.hp);
                    m.writer().flush();
                    players.get(i).session.sendMessage(m);
                    m.cleanup();
                }
            }
//            for(Player p: players) {
//                m = new Message(-95);
//                m.writer().writeByte(5);
//                m.writer().writeInt(pl.id);
//                if(pl.idSkillselect == 4) {
//                    m.writer().writeByte(105);
//                } else {
//                    m.writer().writeByte(106);
//                }
//                m.writer().writeInt(mob.tempId);
//                m.writer().writeInt(damage);
//                m.writer().writeInt(mob.hp);
//                m.writer().flush();
//                p.session.sendMessage(m);
//                m.cleanup();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void loadPlayers(Player p) {
        try {
            for (byte i = 0; i < players.size(); i++) {
                if (players.get(i) != null) {
                    if (p != players.get(i)) {
                        loadInfoPlayer(p.session, players.get(i));
                        if (players.get(i).chimFollow == (byte) 1) {
                            useDeTrungForMe(p, players.get(i), (byte) 0);
                        }
                    }

                    if (players.get(i).cPk != 0) {
                        Service.gI().changeFlagPK(players.get(i), players.get(i).cPk);
                        if (players.get(i).petfucus == 1) {
                            Service.gI().changeFlagPKPet(players.get(i), players.get(i).cPk);
                        }
                    }
                }
            }
//            for (Player pl : players) {
//                if (p != pl) {
//                    loadInfoPlayer(p.session, pl);
//                    if(pl.chimFollow == (byte)1) {
//                        useDeTrungForMe(p, pl, (byte)0);
//                    }
//                }
//                
//                if(pl.cPk != 0) {
//                    Service.gI().changeFlagPK(pl, pl.cPk);
//                    if(pl.petfucus == 1) {
//                        Service.gI().changeFlagPKPet(pl, pl.cPk);
//                    }
//                }
//            }
            for (Detu de : pets) {
                loadInfoDeTu(p.session, de);
            }
            for (Detu pet2 : pet2s) {
                loadInfoPet(p.session, pet2);
            }
            for (Boss b : bossMap) {
                loadInfoBoss(b);
            }
            for (Mob _m : mobs) {
                if (_m.isboss) {
                    sendEffSuperMob(p, _m.tempId);
                }
            }
//            for(Player pls : players) {
//                if(pls.cPk != 0) {
//                    Service.gI().changeFlagPK(pls, pls.cPk);
//                    if(pls.petfucus == 1) {
//                        Service.gI().changeFlagPKPet(pls, pls.cPk);
//                    }
//                }
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // SEND EFFECT SIEU QUAI
    public void sendEffSuperMob(Player _p, int _idMob) {
        Message m = null;
        try {
            m = new Message(-75);
            m.writer().writeByte(_idMob);
            m.writer().writeByte(1);
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
    }

    public void loadInfoPlayer(Session _session, Player _player) {
        Message msg;
        try {
            msg = new Message(-5);
            msg.writer().writeInt(_player.id);
            if (_player.clan != null) {
                msg.writer().writeInt(_player.clan.id);
            } else {
                msg.writer().writeInt(-1);
            }
//            msg.writer().writeByte(10);
            msg.writer().writeByte(_player.getLevelPower());
            msg.writer().writeBoolean(false);
            msg.writer().writeByte(_player.typePk);
            msg.writer().writeByte(_player.gender);
            msg.writer().writeByte(_player.gender);
            if (_player.isMonkey) {
                Skill skl = _player.getSkillById(13);
                if (skl.point == 1) {
                    msg.writer().writeShort(192);
                } else if (skl.point == 2) {
                    msg.writer().writeShort(195);
                } else if (skl.point == 3) {
                    msg.writer().writeShort(196);
                } else if (skl.point == 4) {
                    msg.writer().writeShort(199);
                } else if (skl.point == 5) {
                    msg.writer().writeShort(197);
                } else if (skl.point == 6) {
                    msg.writer().writeShort(200);
                } else if (skl.point == 7) {
                    msg.writer().writeShort(198);
                }
            } else {
                msg.writer().writeShort(_player.PartHead());
            }
            msg.writer().writeUTF(_player.name);
            msg.writer().writeInt(_player.hp);
            msg.writer().writeInt(_player.getHpFull());

            String bodyLeg = Service.gI().writePartBodyLeg(_player);
            String[] arrOfStr = bodyLeg.split(",", 2);
            msg.writer().writeShort(Short.parseShort(arrOfStr[0]));
            msg.writer().writeShort(Short.parseShort(arrOfStr[1]));

            msg.writer().writeByte(8);
            msg.writer().writeByte(-1);
            msg.writer().writeShort(_player.x);
            msg.writer().writeShort(_player.y);
            msg.writer().writeShort(0);
            msg.writer().writeShort(0);
            msg.writer().writeByte((byte) 0);

//            msg.writer().writeByte(0);
//            msg.writer().writeInt(0);
//            msg.writer().writeInt(0);
//            msg.writer().writeShort(-1);
            msg.writer().writeByte((byte) 0);
            msg.writer().writeByte((byte) 0);
            msg.writer().writeShort(_player.getMount());
            msg.writer().writeByte((byte) 0);
            msg.writer().writeByte((byte) 0);

            //AURA EFF
            if (_player.idAura != (short) (-1)) {
                msg.writer().writeShort(_player.idAura);
                msg.writer().writeByte((byte) 0);
                msg.writer().writeShort((short) 0);
            }
            _session.sendMessage(msg);
            msg.cleanup();

            if (_player.imgNRSD > (byte) 0) {
                Service.gI().updateBagNew(_session, _player.id, _player.imgNRSD);
                ClanService.gI().getBagBangNew(_session, _player.imgNRSD);
            } else {
                if (_player.clan != null) {
                    //UPDATE BAG SAU LUNG
                    Service.gI().updateBagNew(_session, _player.id, _player.clan.imgID);
                    //GET BAG SAU LUNG
                    ClanService.gI().getBagBangNew(_session, _player.clan.imgID);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadInfoDeTu(Session _session, Detu _detu) {
        Message msg;
        try {
            msg = new Message(-5);
            msg.writer().writeInt(_detu.id); //id detu
            if (_detu.clan != null) {
                msg.writer().writeInt(_detu.clan.id);
            } else {
                msg.writer().writeInt(-1); //id clan
            }
            msg.writer().writeByte(_detu.getLevelPower()); //level player viet 1 function get level
            msg.writer().writeBoolean(false);  //co dang vo hinh hay khong
            msg.writer().writeByte(_detu.typePk);  //type pk
            msg.writer().writeByte(_detu.gender); // get nClass teeamplate skill theo gender
            msg.writer().writeByte(_detu.gender); // get gender detu
            if (_detu.isMonkey) {
                Skill skl = _detu.getSkillById(13);
                if (skl.point == 1) {
                    msg.writer().writeShort(192);
                } else if (skl.point == 2) {
                    msg.writer().writeShort(195);
                } else if (skl.point == 3) {
                    msg.writer().writeShort(196);
                } else if (skl.point == 4) {
                    msg.writer().writeShort(199);
                } else if (skl.point == 5) {
                    msg.writer().writeShort(197);
                } else if (skl.point == 6) {
                    msg.writer().writeShort(200);
                } else if (skl.point == 7) {
                    msg.writer().writeShort(198);
                }
            } else {
                msg.writer().writeShort(_detu.PartHead()); // part head de tu
            }
            msg.writer().writeUTF("$" + _detu.name);  // name de tu
//            msg.writer().writeInt(_detu.hpGoc); // hp de tu hien tai
            msg.writer().writeInt(_detu.hp); // hp de tu hien tai
            msg.writer().writeInt(_detu.getHpFull()); // hp full de tu

            String bodyLeg = Service.gI().writePartBodyLegDetu(_detu);
            String[] arrOfStr = bodyLeg.split(",", 2);
            msg.writer().writeShort(Short.parseShort(arrOfStr[0]));
            msg.writer().writeShort(Short.parseShort(arrOfStr[1]));

            msg.writer().writeByte(8); // bag
            msg.writer().writeByte(-1); //b gui sang khong thay dung
            msg.writer().writeShort(_detu.x); // x 
            msg.writer().writeShort(_detu.y); // y
            msg.writer().writeShort(0); //  eff 5 buff hp
            msg.writer().writeShort(0); // eff 5 buff mp
            msg.writer().writeByte(0); // so luong eff char
//            msg.writer().writeByte(0); // eff template id
//            msg.writer().writeInt(0);  //time start
//            msg.writer().writeInt(0);  //time length
//            msg.writer().writeShort((short)(-1)); // param eff
            msg.writer().writeByte(0); // b76
            msg.writer().writeByte(0); //ismonkey
            msg.writer().writeShort((short) (-1)); // id can dau van, van bay ,....
            msg.writer().writeByte(0); // cflag
            msg.writer().writeByte(0); // is hop the?
            _session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadInfoPet(Session _session, Detu _pet) {
        Message msg;
        try {
            msg = new Message(-5);
            msg.writer().writeInt(_pet.id); //id detu
            msg.writer().writeInt(-1); //id clan
            msg.writer().writeByte((byte) 0); //level player viet 1 function get level
            msg.writer().writeBoolean(false);  //co dang vo hinh hay khong
            msg.writer().writeByte(_pet.typePk);  //type pk
            msg.writer().writeByte(_pet.gender); // get nClass teeamplate skill theo gender
            msg.writer().writeByte(_pet.gender); // get gender pet
            msg.writer().writeShort(_pet.headpet2); // part head pet
            msg.writer().writeUTF("");  // name pet
            msg.writer().writeInt(5000000); // hp de tu hien tai
            msg.writer().writeInt(5000000); // hp full de tu
            msg.writer().writeShort(_pet.bodypet2);
            msg.writer().writeShort(_pet.legpet2);
            msg.writer().writeByte(-1); // bag
            msg.writer().writeByte(-1); //b gui sang khong thay dung
            msg.writer().writeShort(_pet.x); // x 
            msg.writer().writeShort(_pet.y); // y
            msg.writer().writeShort(0); //  eff 5 buff hp
            msg.writer().writeShort(0); // eff 5 buff mp
            msg.writer().writeByte(0); // so luong eff char
//            msg.writer().writeByte(0); // eff template id
//            msg.writer().writeInt(0);  //time start
//            msg.writer().writeInt(0);  //time length
//            msg.writer().writeShort(-1); // param eff
            msg.writer().writeByte(0); // b76
            msg.writer().writeByte(0); //ismonkey
            msg.writer().writeShort(-1); // id can dau van, van bay ,....
            msg.writer().writeByte(0); // cflag
            msg.writer().writeByte(0); // is hop the?
            _session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //FIGHTBOSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS -60
    public void FightBoss(Player p, int bossId) throws IOException {
        int damage = 0;
        Message m = null;
        Boss _bTarget = getBossByID(bossId);

//        Mob[] arMob = new Mob[10];
//        arMob[0] = mob;
        int miss = Util.nextInt(0, 10);
        if (_bTarget._typeBoss >= (byte) 31 && _bTarget._typeBoss <= (byte) 35) {
            miss = Util.nextInt(7, 10);
        }
//        Skill skillUse = p.getSkillById(p.idSkillselect);
//        SkillData skilldata = new SkillData();
//        Skill skillPlayerUse = skilldata.getSkillBySkillTemplate(p.gender, skillUse.skillId, skillUse.point);
        Skill skillPlayerUse = p.getSkillByIDTemplate(p.idSkillselect);
        long _timeNOW = System.currentTimeMillis();
        short xSkill = (short) skillPlayerUse.dx;
        short ySkill = (short) skillPlayerUse.dy;
        if (skillPlayerUse.template.id == 0 || skillPlayerUse.template.id == 9 || skillPlayerUse.template.id == 2 || skillPlayerUse.template.id == 17 || skillPlayerUse.template.id == 4
                || skillPlayerUse.template.id == 1 || skillPlayerUse.template.id == 3 || skillPlayerUse.template.id == 5) {
            xSkill = (short) (xSkill + 100);
            ySkill = (short) (ySkill + 100);
        }
        //CHECK TAM DANH CUA SKILL
        if (((Math.abs(p.x - _bTarget.x) <= xSkill) && (Math.abs(p.y - _bTarget.y) <= ySkill)) && Service.gI().checkCanAttackBoss(_bTarget) && !p.isTroi) {
            //use skill troi xayda
            if (p.idSkillselect == 23 && p.mp >= skillPlayerUse.manaUse && !p.isdie && !_bTarget.isdie && !p.checkPlayerBiKhongChe() && (_timeNOW - skillPlayerUse.lastTimeUseThisSkill > (long) (skillPlayerUse.coolDown))) {
                skillPlayerUse.lastTimeUseThisSkill = _timeNOW; //gan time hien tai
                //CHECK NOI TAI TROI TANG SAT THUONG DON KE TIEP
                if (p.noiTai.id != 0 && skillPlayerUse.template.id == p.noiTai.idSkill) {
                    p.upDameAfterNoiTai = true;
                }
                p.mp -= skillPlayerUse.manaUse;
                postUpdateKI(p);
                p.isTroi = true;
                _bTarget.isCharFreez = true;
                p.CHARHOLD = _bTarget;
                // send effect troi
                try {
                    m = new Message(-124);
                    m.writer().writeByte(1); //b5
                    m.writer().writeByte(0); //b6
                    m.writer().writeByte(32); //num3
                    m.writer().writeInt(_bTarget.id); //id bi troi
                    m.writer().writeInt(p.id); //id troi
                    m.writer().flush();
                    for (Player pl : players) {
                        pl.session.sendMessage(m);
                    }
                    Timer timerHold = new Timer();
                    TimerTask bossHold = new TimerTask() {
                        public void run() {
                            //XOA TROI BOSS
                            Message m = null;
                            _bTarget.isCharFreez = false;
                            //remove setting troi, va effect tren nguoi mob
                            try {
                                m = new Message(-124);
                                m.writer().writeByte(0); //b5
                                m.writer().writeByte(0); //b6
                                m.writer().writeByte(32);
                                m.writer().writeInt(_bTarget.id);
                                m.writer().flush();
                                for (Player pl : players) {
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
                            if (p != null) {
//                                    if(!p.isTroi) {
//                                        timerHold.cancel();
//                                    } else {

                                p.isTroi = false;
                                //remove effect troi cua player
                                try {
                                    m = new Message(-124);
                                    m.writer().writeByte(2);
                                    m.writer().writeByte(0);
                                    m.writer().writeInt(p.id);
                                    m.writer().flush();
                                    for (Player pl : players) {
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
//                                    }
                            }
                            timerHold.cancel();
                        }
                    ;
                    };
                    timerHold.schedule(bossHold, (skillPlayerUse.damage * 1000));

                    m.cleanup();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            //use thoi mien
            if (p.idSkillselect == 22 && p.mp >= skillPlayerUse.manaUse && !p.isdie && !_bTarget.isdie && !p.checkPlayerBiKhongChe() && (_timeNOW - skillPlayerUse.lastTimeUseThisSkill > (long) (skillPlayerUse.coolDown))) {
                skillPlayerUse.lastTimeUseThisSkill = _timeNOW; //gan time hien tai
                //CHECK NOI TAI THOI MIEN TANG SAT THUONG DON KE TIEP
                if (p.noiTai.id != 0 && skillPlayerUse.template.id == p.noiTai.idSkill) {
                    p.upDameAfterNoiTai = true;
                }
                // DCTT XONG KHONG MISS SKILL
                p.noMiss = true;
                p.mp -= skillPlayerUse.manaUse;
                postUpdateKI(p);
                _bTarget.isCharSleep = true;
                // send effect thoi mien
                try {
                    m = new Message(-124);
                    m.writer().writeByte(1);
                    m.writer().writeByte(0);
                    m.writer().writeByte(41);
                    m.writer().writeInt(_bTarget.id);
                    m.writer().flush();
                    for (Player pl : players) {
                        pl.session.sendMessage(m);
                        Timer timerSleep = new Timer();
                        TimerTask bossSleep = new TimerTask() {
                            public void run() {
                                Message ms = null;
                                _bTarget.isCharSleep = false;
                                //remove setting thoi mien, va effect tren nguoi mob
                                try {
                                    ms = new Message(-124);
                                    ms.writer().writeByte(0);
                                    ms.writer().writeByte(0);
                                    ms.writer().writeByte(41);
                                    ms.writer().writeInt(_bTarget.id);
                                    ms.writer().flush();
                                    pl.session.sendMessage(ms);
                                    ms.cleanup();
                                } catch (Exception var2) {
                                    var2.printStackTrace();
                                } finally {
                                    if (ms != null) {
                                        ms.cleanup();
                                    }
                                }
                            }
                        ;
                        };
                        timerSleep.schedule(bossSleep, (skillPlayerUse.damage * 1000));
                    }
                    m.cleanup();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                setSkillPaint(skillPlayerUse.skillId, p, 8);
                return;
            }
            //        use socola
            //        if(p.idSkillselect == 18) {
            //            int _manaSocola = (int)(skillPlayerUse.manaUse*p.getMpFull());
            //            if(p.mp >= _manaSocola) {
            //                p.mp -= _manaSocola;
            //                postUpdateKI(p);
            //                _bTarget.isCharSocola = true;
            //                // send effect socola
            ////                mob.isSocola = skillPlayerUse.damage;
            //                try {
            //                    m = new Message(-112);
            //                    m.writer().writeByte(1);
            //                    m.writer().writeByte(mobId);
            //                    m.writer().writeShort((short)(Util.nextInt(4127,4133)));
            //                    m.writer().flush();
            //                    for(Player pl: players) {
            //                        pl.session.sendMessage(m);
            //                        ResetSocolaTask socolaTask = new ResetSocolaTask(pl, mob);
            //                        Timer timerSocola = new Timer();
            //                        timerSocola.schedule(socolaTask, 30000);
            //                    }
            //                    m.cleanup();
            //                } catch (Exception e) {
            //                    e.printStackTrace();
            //                }
            //
            //                setSkillPaint(skillPlayerUse.skillId, p, 8);
            //                return;
            //            }
            //        }
            //dich chuyen tuc thoi
            if (p.idSkillselect == 20 && p.mp >= skillPlayerUse.manaUse && !p.isdie && !_bTarget.isdie && !p.checkPlayerBiKhongChe() && (_timeNOW - skillPlayerUse.lastTimeUseThisSkill > (long) (skillPlayerUse.coolDown))) {
                skillPlayerUse.lastTimeUseThisSkill = _timeNOW; //gan time hien tai
                //CHECK NOI TAI DICH CHUYEN TUC THOI TANG SAT THUONG DON KE TIEP
                if (p.noiTai.id != 0 && skillPlayerUse.template.id == p.noiTai.idSkill) {
                    p.upDameAfterNoiTai = true;
                }
                // DCTT XONG KHONG MISS SKILL
                p.noMiss = true;
                p.mp -= skillPlayerUse.manaUse;
                p.PEMCRIT = 1;
                postUpdateKI(p);
                _bTarget.isCharDCTT = true;
                try {
                    m = new Message(123);
                    m.writer().writeInt(p.id);
                    m.writer().writeShort(_bTarget.x);
                    m.writer().writeShort(_bTarget.y);
                    m.writer().writeByte(1);
                    m.writer().flush();
                    for (byte i = 0; i < players.size(); i++) {
                        if (players.get(i) != null) {
                            players.get(i).session.sendMessage(m);
                        }
                    }
                    m.cleanup();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //            try {
                //                m = new Message(54);
                //                m.writer().writeInt(p.id);
                //                m.writer().writeByte(skillPlayerUse.skillId);
                //                m.writer().writeByte(_bTarget.id);
                //                for(Player _p: players) {
                //                    if(_p.id != p.id) {
                //                        _p.session.sendMessage(m);
                //                    }
                //                }
                //                m.writer().flush();
                //                m.cleanup();
                //            } catch (Exception e) {
                //                e.printStackTrace();
                //            }
                // gui hieu ung choang cho client
                try {
                    m = new Message(-124);
                    m.writer().writeByte(1);
                    m.writer().writeByte(0);
                    m.writer().writeByte(40);
                    m.writer().writeInt(_bTarget.id);
                    m.writer().flush();
                    for (Player pl : players) {
                        pl.session.sendMessage(m);

                        Timer dcttTask = new Timer();
                        TimerTask bossDCTT = new TimerTask() {
                            public void run() {
                                Message m = null;
                                _bTarget.isCharDCTT = false;
                                //remove setting troi, va effect tren nguoi mob
                                try {
                                    m = new Message(-124);
                                    m.writer().writeByte(0);
                                    m.writer().writeByte(0);
                                    m.writer().writeByte(40);
                                    m.writer().writeInt(_bTarget.id);
                                    m.writer().flush();
                                    pl.session.sendMessage(m);
                                    m.cleanup();
                                } catch (Exception var2) {
                                    var2.printStackTrace();
                                } finally {
                                    if (m != null) {
                                        m.cleanup();
                                    }
                                }
                            }
                        ;
                        };
                        dcttTask.schedule(bossDCTT, (skillPlayerUse.damage));
                    }
                    m.cleanup();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //SEND DAME CHO CLIENT
                int damageDCTT = p.getDamFull();

                //BOSS BROLY
                if (_bTarget.isBOSS && (_bTarget._typeBoss == 1 || (_bTarget._typeBoss >= (byte) 44 && _bTarget._typeBoss <= (byte) 47))) {
                    damageDCTT = (int) (_bTarget.hpFull * 0.01);
                }
                if (_bTarget.isBOSS && (_bTarget._typeBoss == 53 || _bTarget._typeBoss == 54 || _bTarget._typeBoss == 55)) {
                    damageDCTT = 1;
                }
                _bTarget.hp -= damageDCTT;
                if (_bTarget.hp <= 0) {
                    _bTarget.isdie = true;
                    _bTarget.isTTNL = false;
                    _bTarget.hp = 0;
                }
                //CHECK NHIEM VU CHILLED
                if (p.taskId == (byte) 32 && p.crrTask.index == (byte) 1 && (_bTarget._typeBoss == (byte) 40 || _bTarget._typeBoss == (byte) 41)) {
                    TaskService.gI().updateCountTask(p);
                }
                if (_bTarget.isdie && _bTarget.typePk == 5) {
                    //set lai type PK khi boss chet
                    _bTarget.typePk = 1;
                    //send dame
                    dameChar(_bTarget.id, _bTarget.hp, damageDCTT, true);
                    //REMOVE ALL KHONG CHE KHI BOSS CHET
                    _bTarget.removePlayerKhongChe();
                    if (_bTarget._typeBoss != 1 && _bTarget._typeBoss != 2) { //Broly khong roi do
                        ArrayList<ItemMap> itemDrop = new ArrayList<>();
                        if (_bTarget._typeBoss == 3 || _bTarget._typeBoss == 5 || (_bTarget._typeBoss >= (byte) 7 && _bTarget._typeBoss <= (byte) 30)) {
                            if (_bTarget._typeBoss != (byte) 29) {
                                Service.gI().sendThongBaoServer(p.name + " vừa tiêu diệt " + _bTarget.name + " mọi người đều ngưỡng mộ");
                            }
                            if (_bTarget._typeBoss == (byte) 7) {
                                Server.gI().mapKUKU = 0;
                                Server.gI().khuKUKU = 0;
                            } else if (_bTarget._typeBoss == (byte) 8) {
                                Server.gI().mapMDD = 0;
                                Server.gI().khuMDD = 0;
                            } else if (_bTarget._typeBoss == (byte) 9) {
                                Server.gI().mapRAMBO = 0;
                                Server.gI().khuRAMBO = 0;
                            } else if (_bTarget._typeBoss == (byte) 14) {
                                Server.gI().mapTDST = 0;
                                Server.gI().khuTDST = 0;
                            }
                        } else if (_bTarget._typeBoss == 4 || _bTarget._typeBoss == 6) {
                            Service.gI().sendThongBaoServer(p.name + " vừa tiêu diệt " + _bTarget.name + " mọi người đều ngưỡng mộ");
                            ItemMap item = dropItemGOD(p, _bTarget.x, _bTarget.y);

                            //ADD ITEM TO MAP
                            if (item != null) {
                                try {
                                    itemDrop.add(item);
                                    itemsMap.addAll(itemDrop);
                                    m = new Message(68);
                                    m.writer().writeShort(item.itemMapID);
                                    m.writer().writeShort(item.item.template.id);
                                    m.writer().writeShort(_bTarget.x);
                                    m.writer().writeShort(_bTarget.y);
                                    m.writer().writeInt(p.id);
                                    m.writer().flush();
                                    for (byte i = 0; i < players.size(); i++) {
                                        if (players.get(i) != null) {
                                            players.get(i).session.sendMessage(m);
                                        }
                                    }
                                    m.cleanup();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (Util.nextInt(0, 10) < 5) {
                                ItemMap itemM = newItemMAP(979, p.id, _bTarget.x, _bTarget.y);
                                if (itemM != null) {
                                    addItemToMap(itemM, p.id, _bTarget.x, _bTarget.y);
                                }
                            }
                        } else if (_bTarget._typeBoss >= (byte) 44 && _bTarget._typeBoss <= (byte) 47) {
                            ItemMap itemM = cNewItemMap(590, p.id, _bTarget.x, _bTarget.y);
                            if (itemM != null) {
                                addItemToMap(itemM, p.id, _bTarget.x, _bTarget.y);
                            }
                        } else if (_bTarget._typeBoss >= (byte) 31 && _bTarget._typeBoss <= (byte) 35) {
                            int itemDT = 17;
                            if (Util.nextInt(0, 4) == 0) {
                                itemDT = 16;
                            }
                            ItemMap itemM = newItemMAP(itemDT, p.id, _bTarget.x, _bTarget.y);
                            if (itemM != null) {
                                addItemToMap(itemM, p.id, _bTarget.x, _bTarget.y);
                            }
                        } else if (_bTarget._typeBoss >= (byte) 26 && _bTarget._typeBoss <= (byte) 28) {//cell rot 3s
                            ItemMap itemM = newItemMAP(16, p.id, _bTarget.x, _bTarget.y);
                            if (itemM != null) {
                                addItemToMap(itemM, p.id, _bTarget.x, _bTarget.y);
                            }
                        } else if (_bTarget._typeBoss == (byte) 30) {//sieu bo hung rot 2s
                            if (Util.nextInt(0, 10) < 3) {
                                int itemDT = 15;
                                if (Util.nextInt(0, 2) == 0) {
                                    itemDT = 14;
                                }
                                ItemMap itemM = newItemMAP(itemDT, p.id, _bTarget.x, _bTarget.y);
                                if (itemM != null) {
                                    addItemToMap(itemM, p.id, _bTarget.x, _bTarget.y);
                                }
                            }
                        } else if (_bTarget._typeBoss == (byte) 48) {//zamasu rot da bao ve
                            if (Util.nextInt(0, 10) <= 7) {
                                ItemMap itemM = newItemMAP(987, p.id, _bTarget.x, _bTarget.y);
                                if (itemM != null) {
                                    addItemToMap(itemM, p.id, _bTarget.x, _bTarget.y);
                                }
                            }
                        } else if (_bTarget._typeBoss == (byte) 49 || _bTarget._typeBoss == (byte) 50) {//billwhis rot ruong
                            ItemMap itemM = newItemMAP(1055, p.id, _bTarget.x, _bTarget.y);
                            if (itemM != null) {
                                addItemToMap(itemM, p.id, _bTarget.x, _bTarget.y);
                            }
                        } else if (_bTarget._typeBoss == (byte) 56 || _bTarget._typeBoss == (byte) 57 || _bTarget._typeBoss == (byte) 58) {//billwhis rot ruong
                            int idItemMap[] = {571, 572, 573, 574, 648, 649, 533, 77, 861};
                            ItemMap itemM = newItemMapBuff(idItemMap[Util.nextInt(0, 10)], p.id, _bTarget.x, _bTarget.y);
                            if (itemM != null) {
                                addItemToMap(itemM, p.id, _bTarget.x, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                            }
                        } else if (_bTarget._typeBoss == (byte) 53 || _bTarget._typeBoss == (byte) 54 || _bTarget._typeBoss == (byte) 55) {//an trom 
                            int idItemMap[] = {571, 572, 573, 574, 648, 649, 533, 77, 861};
                            ItemMap itemM = newItemMapBuff(idItemMap[Util.nextInt(0, 10)], p.id, _bTarget.x, _bTarget.y);
                            if (itemM != null) {
                                addItemToMap(itemM, p.id, _bTarget.x, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                            }
                        }
//                        else {
//        //                if(mob.template.tempId != 0) {
//                            int id = Util.nextInt(17,20);
//
//                            Item itemMap = ItemSell.getItemNotSell(id);
//                            ItemMap item = new ItemMap();
//                            item.playerId = p.id;
//                            item.x = _bTarget.x;
//                            item.y = _bTarget.y;
//                            item.itemMapID = id;
//                            item.itemTemplateID = (short) item.itemMapID;
//                            itemMap.template = ItemTemplate.ItemTemplateID(id);
//                            item.item = itemMap;
//                            itemDrop.add(item);
//                            itemsMap.addAll(itemDrop);
//
//                            //đồng bộ boss chet, cho boss bien mat. add item map //68
//                            try {
//                                m = new Message(68);
//                                m.writer().writeShort(item.itemMapID);
//                                m.writer().writeShort(item.item.template.id);
//                                m.writer().writeShort(_bTarget.x);
//                                m.writer().writeShort(_bTarget.y);
//                                m.writer().writeInt(p.id);
//                                m.writer().flush();
//                                for(Player _pl: players) {
//                                    _pl.session.sendMessage(m);
//                                }
//                                m.cleanup();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        } 
                    }
                    if (_bTarget._typeBoss == 2 && p.havePet == 0) { //boss die la super broly
                        p.detu = _bTarget.detu;
                        leaveDEEEEE(_bTarget.detu);
                        p.havePet = 1;
                        p.isNewPet = true;
                        p.detu.id = (-100000 - p.id);
                        p.statusPet = 0;
                        p.petfucus = 1;
                        pets.add(p.detu);
                        for (byte i = 0; i < players.size(); i++) {
                            if (players.get(i) != null) {
                                loadInfoDeTu(players.get(i).session, p.detu);
                            }
                        }
//                        for(Player _plz: players) {
//                            loadInfoDeTu(_plz.session, p.detu);
//                        }
                    } else {
                        leaveDEEEEE(_bTarget.detu);
                    }
                    //boss chet
                    if (_bTarget._typeBoss < (byte) 44 || _bTarget._typeBoss > (byte) 47) {
//                        for(Player _pp: players) {
//                            sendDieToAnotherPlayer(_pp, _bTarget);
//                        }
                        for (byte i = 0; i < players.size(); i++) {
                            if (players.get(i) != null) {
                                sendDieToAnotherPlayer(players.get(i), _bTarget);
                            }
                        }
                    }
                    //CHECK NHIEM VU SAN BOSS
                    if (p.taskId == (short) 21 || p.taskId == (short) 22 || p.taskId == (short) 23 || p.taskId == (short) 25 || p.taskId == (short) 26 || p.taskId == (short) 27
                            || p.taskId == (short) 28 || p.taskId == (short) 29 || p.taskId == (short) 30 || p.taskId == (byte) 32) {
                        int idBoss = TaskManager.gI().mobTASK0[p.taskId][p.crrTask.index];
                        if ((_bTarget._typeBoss == (byte) (idBoss / 100)) || ((_bTarget._typeBoss == (byte) 41) && p.taskId == (byte) 32 && p.crrTask.index == (byte) 7)) {
                            TaskService.gI().updateCountTask(p);
                        }
                    } else if (p.taskId == (short) 19 && p.crrTask.index == (byte) 1 && _bTarget._typeBoss == (byte) 31) {
                        TaskService.gI().updateCountTask(p);
                        if (p.clan != null) {
                            for (byte mk = 0; mk < p.clan.members.size(); mk++) {
                                Player member = PlayerManger.gI().getPlayerByUserID(p.clan.members.get(mk).id);
                                if (member != null && member.session != null && member.map.id == 59) {
                                    if (member.taskId == (short) 19 && member.crrTask.index == (byte) 1) {
                                        TaskService.gI().updateCountTask(member);
                                    }
                                }
                            }
                        }
                    }
                    //END NHIEM VU SAN BOSS
                    //CHECK NHIEM VU NHAN THOI KHONG
                    if (p.taskId == (short) 31 && p.crrTask.index == (byte) 0 && _bTarget._typeBoss == (byte) 6) {
                        int perNhan = Util.nextInt(0, 5);
                        if (perNhan == 0) {
                            Item itemNhan = ItemSell.getItemNotSell(992);
                            Item _itemNhan = new Item(itemNhan);
                            if (p.addItemToBag(_itemNhan)) {
                                p.sendAddchatYellow("Bạn vừa nhận được nhẫn thời không sai lệch");
                                Service.gI().updateItemBag(p);
                                TaskService.gI().updateCountTask(p);
                            }
                        }
                    }
                    //END CHECK NHIEM VU NHAN THOI KHONG
                    //CHECK SP MABU
                    if (_bTarget._typeBoss >= (byte) 36 && _bTarget._typeBoss <= (byte) 39) {
                        p.pointMabu = (byte) 10;
                        Service.gI().setPowerPoint(p, "TL", (short) 10, (short) 10, (short) 10);
                    }
                    //END CHECK SP MABU
                    if (_bTarget._typeBoss >= (byte) 44 && _bTarget._typeBoss <= 47) {
                        leaveBossYardrat(_bTarget);
                    } else {
                        Timer timerBossLeave = new Timer();
                        TimerTask bossLeave = new TimerTask() {
                            public void run() {
                                leaveBoss(_bTarget); //xoa boss
                            }
                        ;
                        };
                        timerBossLeave.schedule(bossLeave, 5000);
                    }
                    //                MobStartDie(p, damage, mob, fatal, itemDrop);
                } else {
                    dameChar(_bTarget.id, _bTarget.hp, damageDCTT, true);
                    //dame hut mau
                    sendHutHP(p, damageDCTT, false);
                    //dame hut mau
                    //dame hut ki
                    sendHutKI(p, damageDCTT);
                    //dame hut ki
                }

                //send player attack to all player in map
                short _idSKILL = skillPlayerUse.skillId;
                if (p.isMonkey && (p.idSkillselect == 4)) {
                    _idSKILL = 105;
                } else if (p.isMonkey && (p.idSkillselect == 5)) {
                    _idSKILL = 106;
                }
                attachedChar(p.id, _bTarget.id, _idSKILL);
                return;
            }
            //dam va chuong boss
            //vua dung DCTT THI KHONG MISS SKILL
            if (p.noMiss) {
                p.noMiss = false;
                miss = 7;
            }
            if (miss < 8) {
                damage = Util.nextInt((int) (p.getDamFull() * 0.9 * Util.getPercentDouble((int) skillPlayerUse.damage)), (int) (p.getDamFull() * Util.getPercentDouble((int) skillPlayerUse.damage)));
            }
            int kiUse = skillPlayerUse.manaUse;
            if (p.mp >= kiUse && !p.isdie && !_bTarget.isdie && _bTarget.hp > 0 && _bTarget.typePk == 5 && !p.checkPlayerBiKhongChe() && (_timeNOW - skillPlayerUse.lastTimeUseThisSkill > (long) (skillPlayerUse.coolDown)) && (_timeNOW >= p.timeCanSkill)) {
                //CHECK NOI TAI LAZE CO GIAM THOI GIAN HOI CHIEU
                if (p.noiTai.id != 0 && skillPlayerUse.template.id == p.noiTai.idSkill && p.idSkillselect == 11) { //LAZE
                    long _timeDOWN = (long) (Util.getPercentDouble((int) (p.noiTai.param)) * skillPlayerUse.coolDown);
                    skillPlayerUse.lastTimeUseThisSkill = _timeNOW - _timeDOWN;
                    //SEND TIME DOWN NOI TAI
                    Service.gI().sendCoolDownSkill(p, skillPlayerUse.skillId, (int) ((long) skillPlayerUse.coolDown - _timeDOWN));
                } else {
                    skillPlayerUse.lastTimeUseThisSkill = _timeNOW;//gan time hien tai
                    if (skillPlayerUse.template.id == (byte) 1 || skillPlayerUse.template.id == (byte) 3 || skillPlayerUse.template.id == (byte) 5 || skillPlayerUse.template.id == (byte) 11) {
                        p.timeCanSkill = _timeNOW + (long) (500);
                    } else {
                        p.timeCanSkill = _timeNOW + (long) (skillPlayerUse.coolDown);
                    }
                }
                //CHECK KAIOKEN DE TRU 10% HP
                if (skillPlayerUse.template.id == 9 && p.hp > (int) (p.getHpFull() * 0.1)) {
                    p.hp -= (int) (p.getHpFull() * 0.1);
                    postUpdateHP(p);
                }
                p.mp -= kiUse;
                int fantashi = Util.nextInt(0, 100);
                boolean fatal = p.getCritFull() >= fantashi;
                //CHECK NEU BI TROI THI DANH AUTO CRIT
                if (_bTarget.isCharFreez || (p.noiTai.id != 0 && p.noiTai.idSkill == (byte) (-4) && p.hp <= (int) (p.getHpFull() * Util.getPercentDouble((int) (p.noiTai.param))))) { //CHECK NOI TAI crit 
                    fatal = true;
                }
                if (fatal || p.PEMCRIT > 0) {
                    damage = damage * 2;
                    p.PEMCRIT = 0;
                    fatal = true;
                }

                // GIAM DAME SAU KHI BI THOI MIEN
                if (p.downDAME > 0) {
                    damage -= (int) (damage * Util.getPercentDouble((int) (p.downDAME)));
                }
                //CHECK DAME % TANG LEN SAU KHI DUNG SKILL VA CO NOI TAIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
                if (p.upDameAfterNoiTai && p.noiTai.id != 0) {//% DAME DON KE TIEP
                    p.upDameAfterNoiTai = false;
                    damage += (int) (damage * Util.getPercentDouble((int) p.noiTai.param));
                } else if (skillPlayerUse.template.id == p.noiTai.idSkill) { //%DAM CHIEU DAM VA CHUONG
                    damage += (int) (damage * Util.getPercentDouble((int) p.noiTai.param));
                } else if (p.upDameAfterKhi) { //HOA KHI TANG DAME
                    damage += (int) (damage * Util.getPercentDouble((int) p.noiTai.param));
                }
                //CHECK SET KICH HOAT SONGOKU
                if (p.getSetKichHoatFull() == (byte) 3 && skillPlayerUse.template.id == (byte) 1) {
                    damage = damage * 2;
                } else if (p.getSetKichHoatFull() == (byte) 7 && skillPlayerUse.template.id == (byte) 4) { //SET KICH HOAT KAKAROT
                    damage = damage * 2;
                } else if (p.getSetKichHoatFull() == (byte) 5 && skillPlayerUse.template.id == (byte) 17) { //SET KICH HOAT OCTIEU
                    damage = damage * 2;
                }
                //END CHECK SET KICH HOAT SONGOKU
                //CHECK CAI TRANG X3,X4 CHUONG MOI PHUT
//                if((System.currentTimeMillis() >= p.timeX3X4) && p.isChuongX3X4) {
//                    p.isChuongX3X4 = false;
//                }
//                if(p.ItemBody[5] != null && (p.ItemBody[5].template.id == 710 || p.ItemBody[5].template.id == 711) && !p.isChuongX3X4 && (System.currentTimeMillis() >= p.timeX3X4)) {
//                    int perX3X4 = Util.nextInt(0, 10);
//                    if(perX3X4 <= 1) {
//                        p.isChuongX3X4 = true;
//                        p.timeX3X4 = System.currentTimeMillis() + 60000;
//                        if(p.ItemBody[5].template.id == 710) {
//                            damage = damage*3;
//                        } else if (p.ItemBody[5].template.id == 711){
//                            damage = damage*4;
//                        }
//                    }
//                }
                //CHECK TANG TIME GIAP LUYEN TAP
                Service.gI().upTimeGLT(p);
                //BOSS BROLY
                if (_bTarget.isBOSS && (_bTarget._typeBoss == 1 || (_bTarget._typeBoss >= (byte) 44 && _bTarget._typeBoss <= (byte) 47))) {
                    damage = (int) (_bTarget.hpFull * 0.01);
                }
                if (_bTarget.isBOSS && (_bTarget._typeBoss == 53 || _bTarget._typeBoss == 54 || _bTarget._typeBoss == 55)) {
                    damage = 1;
                }
                // dame laze
                if (p.idSkillselect == 11) {
                    damage = (int) (p.mp * Util.getPercentDouble((int) skillPlayerUse.damage));
                    //CHECK SET KICH HOAT LAZE
                    if (p.getSetKichHoatFull() == (byte) 4) {
                        damage = (int) (damage * 1.5);
                    }
                    p.mp = 0;
                    //                drawSkillVip(p, mob, skillPlayerUse.skillId, damage);
//                    Util.log("DAME LAZE: " + damage);
                }
                _bTarget.hp -= damage;
                if (_bTarget.hp <= 0) {
                    _bTarget.isdie = true;
                    _bTarget.isTTNL = false;
                    _bTarget.hp = 0;
                }
                //CHECK NHIEM VU CHILLED
                if (p.taskId == (byte) 32 && p.crrTask.index == (byte) 1 && (_bTarget._typeBoss == (byte) 40 || _bTarget._typeBoss == (byte) 41)) {
                    TaskService.gI().updateCountTask(p);
                }
                if (_bTarget.isdie && _bTarget.typePk == 5) {
                    //set lai type PK khi boss chet
                    _bTarget.typePk = 1;
                    //send dame
                    dameChar(_bTarget.id, _bTarget.hp, damage, fatal);
                    //REMOVE ALL KHONG CHE KHI BOSS CHET
                    _bTarget.removePlayerKhongChe();
                    if (_bTarget._typeBoss != 1 && _bTarget._typeBoss != 2) { //Broly khong roi do
                        ArrayList<ItemMap> itemDrop = new ArrayList<>();
                        if (_bTarget._typeBoss == 3 || _bTarget._typeBoss == 5 || (_bTarget._typeBoss >= (byte) 7 && _bTarget._typeBoss <= (byte) 30 && _bTarget._typeBoss != (byte) 29)) {
                            if (_bTarget._typeBoss == (byte) 7) {
                                Server.gI().mapKUKU = 0;
                                Server.gI().khuKUKU = 0;
                            } else if (_bTarget._typeBoss == (byte) 8) {
                                Server.gI().mapMDD = 0;
                                Server.gI().khuMDD = 0;
                            } else if (_bTarget._typeBoss == (byte) 9) {
                                Server.gI().mapRAMBO = 0;
                                Server.gI().khuRAMBO = 0;
                            } else if (_bTarget._typeBoss == (byte) 14) {
                                Server.gI().mapTDST = 0;
                                Server.gI().khuTDST = 0;
                            }
                            Service.gI().sendThongBaoServer(p.name + " vừa tiêu diệt " + _bTarget.name + " mọi người đều ngưỡng mộ");
                            int perCapHong = Util.nextInt(0, 10);
                            if (perCapHong < 1) {
                                ItemSell CapHong = ItemSell.getItemSell(722, (byte) 1);
                                ItemMap itemROI = new ItemMap();
                                itemROI.playerId = p.id;
                                itemROI.x = _bTarget.x;
                                itemROI.y = _bTarget.y;
//                                itemROI.itemMapID = 722;
//                                itemROI.itemTemplateID = (short)itemROI.itemMapID;
                                itemROI.itemMapID = itemsMap.size();
                                itemROI.itemTemplateID = (short) 722;
                                //                                itemGOD.item.template = ItemTemplate.ItemTemplateID(_ITEMMAPID);
                                //BUILD NEW ITEM + CHI SO CHO DO KICH HOAT
                                Item _ITEMCapHong = new Item(CapHong.item);
                                itemROI.item = _ITEMCapHong;
                                itemDrop.add(itemROI);
                                itemsMap.addAll(itemDrop);

                                //đồng bộ boss chet, cho boss bien mat. add item map //68
                                try {
                                    m = new Message(68);
                                    m.writer().writeShort(itemROI.itemMapID);
                                    m.writer().writeShort(itemROI.item.template.id);
                                    m.writer().writeShort(_bTarget.x);
                                    m.writer().writeShort(_bTarget.y);
                                    m.writer().writeInt(p.id);
                                    m.writer().flush();
                                    for (byte i = 0; i < players.size(); i++) {
                                        if (players.get(i) != null) {
                                            players.get(i).session.sendMessage(m);
                                        }
                                    }
                                    m.cleanup();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else if (_bTarget._typeBoss == 4 || _bTarget._typeBoss == 6) {
                            Service.gI().sendThongBaoServer(p.name + " vừa tiêu diệt " + _bTarget.name + " mọi người đều ngưỡng mộ");
                            ItemMap item = dropItemGOD(p, _bTarget.x, _bTarget.y);

                            //ADD ITEM TO MAP
                            if (item != null) {
                                try {
                                    itemDrop.add(item);
                                    itemsMap.addAll(itemDrop);
                                    m = new Message(68);
                                    m.writer().writeShort(item.itemMapID);
                                    m.writer().writeShort(item.item.template.id);
                                    m.writer().writeShort(_bTarget.x);
                                    m.writer().writeShort(_bTarget.y);
                                    m.writer().writeInt(p.id);
                                    m.writer().flush();
                                    for (byte i = 0; i < players.size(); i++) {
                                        if (players.get(i) != null) {
                                            players.get(i).session.sendMessage(m);
                                        }
                                    }
                                    m.cleanup();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (Util.nextInt(0, 10) < 5) {
                                ItemMap itemM = newItemMAP(979, p.id, _bTarget.x, _bTarget.y);
                                if (itemM != null) {
                                    addItemToMap(itemM, p.id, _bTarget.x, _bTarget.y);
                                }
                            }
                        } else if (_bTarget._typeBoss >= (byte) 44 && _bTarget._typeBoss <= (byte) 47) {
                            ItemMap itemM = cNewItemMap(590, p.id, _bTarget.x, _bTarget.y);
                            if (itemM != null) {
                                addItemToMap(itemM, p.id, _bTarget.x, _bTarget.y);
                            }
                        } else if (_bTarget._typeBoss >= (byte) 31 && _bTarget._typeBoss <= (byte) 35) {
                            int itemDT = 17;
                            if (Util.nextInt(0, 4) == 0) {
                                itemDT = 16;
                            }
                            ItemMap itemM = newItemMAP(itemDT, p.id, _bTarget.x, _bTarget.y);
                            if (itemM != null) {
                                addItemToMap(itemM, p.id, _bTarget.x, _bTarget.y);
                            }
                        } else if (_bTarget._typeBoss >= (byte) 26 && _bTarget._typeBoss <= (byte) 28) {//cell rot 3s
                            ItemMap itemM = newItemMAP(16, p.id, _bTarget.x, _bTarget.y);
                            if (itemM != null) {
                                addItemToMap(itemM, p.id, _bTarget.x, _bTarget.y);
                            }
                        } else if (_bTarget._typeBoss == (byte) 30) {//sieu bo hung rot 2s
                            ItemMap itemM = newItemMAP(15, p.id, _bTarget.x, _bTarget.y);
                            if (itemM != null) {
                                addItemToMap(itemM, p.id, _bTarget.x, _bTarget.y);
                            }
                        } else if (_bTarget._typeBoss == (byte) 48) {//zamasu rot da bao ve
                            if (Util.nextInt(0, 10) <= 7) {
                                ItemMap itemM = newItemMAP(987, p.id, _bTarget.x, _bTarget.y);
                                if (itemM != null) {
                                    addItemToMap(itemM, p.id, _bTarget.x, _bTarget.y);
                                }
                            }
                        } else if (_bTarget._typeBoss == (byte) 49 || _bTarget._typeBoss == (byte) 50) {//billwhis rot ruong
                            ItemMap itemM = newItemMAP(1055, p.id, _bTarget.x, _bTarget.y);
                            if (itemM != null) {
                                addItemToMap(itemM, p.id, _bTarget.x, _bTarget.y);
                            }
                        } else if (_bTarget._typeBoss == (byte) 56 || _bTarget._typeBoss == (byte) 57 || _bTarget._typeBoss == (byte) 58) {//rati
                            int idItemMap[] = {571, 572, 573, 574, 648, 649, 533, 77, 861};
                            ItemMap itemM = newItemMapBuff(idItemMap[Util.nextInt(0, 10)], p.id, _bTarget.x, _bTarget.y);
                            if (itemM != null) {
                                addItemToMap(itemM, p.id, _bTarget.x, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                            }
                        } else if (_bTarget._typeBoss == (byte) 53 || _bTarget._typeBoss == (byte) 54 || _bTarget._typeBoss == (byte) 55) {//billwhis rot ruong
                            int idItemMap[] = {571, 572, 573, 574, 648, 649, 533, 77, 861};
                            ItemMap itemM = newItemMapBuff(idItemMap[Util.nextInt(0, 10)], p.id, _bTarget.x, _bTarget.y);
                            if (itemM != null) {
                                addItemToMap(itemM, p.id, _bTarget.x, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                            }
                        }
//                        else {
                        //                if(mob.template.tempId != 0) {
//                            int perCapHong = Util.nextInt(0,5);
//                            if(perCapHong < 5) {
//                                ItemSell CapHong = ItemSell.getItemSell(722, (byte)1);
//                                ItemMap itemROI = new ItemMap();
//                                itemROI.playerId = p.id;
//                                itemROI.x = _bTarget.x;
//                                itemROI.y = _bTarget.y;
//                                itemROI.itemMapID = 722;
//                                itemROI.itemTemplateID = (short)itemROI.itemMapID;
//    //                                itemGOD.item.template = ItemTemplate.ItemTemplateID(_ITEMMAPID);
//                                //BUILD NEW ITEM + CHI SO CHO DO KICH HOAT
//                                Item _ITEMCapHong = new Item(CapHong.item);
//                                itemROI.item = _ITEMCapHong;
//                                itemDrop.add(itemROI);
//                                itemsMap.addAll(itemDrop);
//
//                                //đồng bộ boss chet, cho boss bien mat. add item map //68
//                                try {
//                                    m = new Message(68);
//                                    m.writer().writeShort(itemROI.itemMapID);
//                                    m.writer().writeShort(itemROI.item.template.id);
//                                    m.writer().writeShort(_bTarget.x);
//                                    m.writer().writeShort(_bTarget.y);
//                                    m.writer().writeInt(p.id);
//                                    m.writer().flush();
//                                    for(Player _pl: players) {
//                                        _pl.session.sendMessage(m);
//                                    }
//                                    m.cleanup();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
                    }
                    if (_bTarget._typeBoss == 2 && p.havePet == 0) { //boss die la super broly
                        p.detu = _bTarget.detu;
                        leaveDEEEEE(_bTarget.detu);
                        p.havePet = 1;
                        p.isNewPet = true;
                        p.detu.id = (-100000 - p.id);
                        p.statusPet = 0;
                        p.petfucus = 1;
                        pets.add(p.detu);
//                        for(Player _plz: players) {
//                            loadInfoDeTu(_plz.session, p.detu);
//                        }
                        for (byte i = 0; i < players.size(); i++) {
                            if (players.get(i) != null) {
                                loadInfoDeTu(players.get(i).session, p.detu);
                            }
                        }
                    } else {
                        leaveDEEEEE(_bTarget.detu);
                    }
                    //boss chet
                    if (_bTarget._typeBoss < (byte) 44 || _bTarget._typeBoss > (byte) 47) {
//                        for(Player _pp: players) {
//                            sendDieToAnotherPlayer(_pp, _bTarget);
//                        }
                        for (byte i = 0; i < players.size(); i++) {
                            if (players.get(i) != null) {
                                sendDieToAnotherPlayer(players.get(i), _bTarget);
                            }
                        }
                    }
                    //CHECK NHIEM VU SAN BOSS
                    if (p.taskId == (short) 21 || p.taskId == (short) 22 || p.taskId == (short) 23 || p.taskId == (short) 25 || p.taskId == (short) 26 || p.taskId == (short) 27
                            || p.taskId == (short) 28 || p.taskId == (short) 29 || p.taskId == (short) 30 || p.taskId == (byte) 32) {
                        int idBoss = TaskManager.gI().mobTASK0[p.taskId][p.crrTask.index];
                        if ((_bTarget._typeBoss == (byte) (idBoss / 100)) || ((_bTarget._typeBoss == (byte) 41) && p.taskId == (byte) 32 && p.crrTask.index == (byte) 7)) {
                            TaskService.gI().updateCountTask(p);
                        }
                    } else if (p.taskId == (short) 19 && p.crrTask.index == (byte) 1 && _bTarget._typeBoss == (byte) 31) {
                        TaskService.gI().updateCountTask(p);
                        if (p.clan != null) {
                            for (byte mk = 0; mk < p.clan.members.size(); mk++) {
                                Player member = PlayerManger.gI().getPlayerByUserID(p.clan.members.get(mk).id);
                                if (member != null && member.session != null && member.map.id == 59) {
                                    if (member.taskId == (short) 19 && member.crrTask.index == (byte) 1) {
                                        TaskService.gI().updateCountTask(member);
                                    }
                                }
                            }
                        }
                    }
                    //END NHIEM VU SAN BOSS
                    //CHECK NHIEM VU NHAN THOI KHONG
                    if (p.taskId == (short) 31 && p.crrTask.index == (byte) 0 && _bTarget._typeBoss == (byte) 6) {
                        int perNhan = Util.nextInt(0, 5);
                        if (perNhan == 0) {
                            Item itemNhan = ItemSell.getItemNotSell(992);
                            Item _itemNhan = new Item(itemNhan);
                            if (p.addItemToBag(_itemNhan)) {
                                p.sendAddchatYellow("Bạn vừa nhận được nhẫn thời không sai lệch");
                                Service.gI().updateItemBag(p);
                                TaskService.gI().updateCountTask(p);
                            }
                        }
                    }
                    //END CHECK NHIEM VU NHAN THOI KHONG
                    //CHECK SP MABU
                    if (_bTarget._typeBoss >= (byte) 36 && _bTarget._typeBoss <= (byte) 39) {
                        p.pointMabu = (byte) 10;
                        Service.gI().setPowerPoint(p, "TL", (short) 10, (short) 10, (short) 10);
                    }
                    //END CHECK SP MABU
                    if (_bTarget._typeBoss >= (byte) 44 && _bTarget._typeBoss <= 47) {
                        leaveBossYardrat(_bTarget);
                    } else {
                        Timer timerBossLeave = new Timer();
                        TimerTask bossLeave = new TimerTask() {
                            public void run() {
                                leaveBoss(_bTarget); //xoa boss
                            }
                        ;
                        };
                        timerBossLeave.schedule(bossLeave, 10000);
                    }
                    //                MobStartDie(p, damage, mob, fatal, itemDrop);
                } else {
                    dameChar(_bTarget.id, _bTarget.hp, damage, fatal);
                    //dame hut mau
                    sendHutHP(p, damage, false);
                    //dame hut mau
                    //dame hut ki
                    sendHutKI(p, damage);
                    //dame hut ki
                }

                //CHIM ATTACK BOSS *****************************************************************************************************
                if (p.chimFollow == (byte) 1 && !_bTarget.isdie && _bTarget.typePk == 5 && p.dameChim > 0) {
                    //DAME CHIM
                    damage = (int) (damage * Util.getPercentDouble(p.dameChim));
                    if (p.getSetKichHoatFull() == (byte) 6) {
                        damage = (int) (damage * 2);
                    }
                    damage = damage > 1 ? damage : 1;
                    _bTarget.hp -= damage;
                    if (_bTarget.hp <= 0) {
                        _bTarget.isdie = true;
                        _bTarget.isTTNL = false;
                        _bTarget.hp = 0;
                    }
                    if (_bTarget.isdie && _bTarget.typePk == 5) {
                        //set lai type PK khi boss chet
                        _bTarget.typePk = 1;
                        chimDameChar(p, _bTarget, damage);
                        //REMOVE ALL KHONG CHE KHI BOSS CHET
                        _bTarget.removePlayerKhongChe();
                        if (_bTarget._typeBoss != 1 && _bTarget._typeBoss != 2) { //Broly khong roi do
                            ArrayList<ItemMap> itemDrop = new ArrayList<>();
                            if (_bTarget._typeBoss == 3 || _bTarget._typeBoss == 5 || (_bTarget._typeBoss >= (byte) 7 && _bTarget._typeBoss <= (byte) 30 && _bTarget._typeBoss != (byte) 29)) {
                                if (_bTarget._typeBoss == (byte) 7) {
                                    Server.gI().mapKUKU = 0;
                                    Server.gI().khuKUKU = 0;
                                } else if (_bTarget._typeBoss == (byte) 8) {
                                    Server.gI().mapMDD = 0;
                                    Server.gI().khuMDD = 0;
                                } else if (_bTarget._typeBoss == (byte) 9) {
                                    Server.gI().mapRAMBO = 0;
                                    Server.gI().khuRAMBO = 0;
                                } else if (_bTarget._typeBoss == (byte) 14) {
                                    Server.gI().mapTDST = 0;
                                    Server.gI().khuTDST = 0;
                                }
                                Service.gI().sendThongBaoServer(p.name + " vừa tiêu diệt " + _bTarget.name + " mọi người đều ngưỡng mộ");
                                int perCapHong = Util.nextInt(0, 10);
                                if (perCapHong < 1) {
                                    ItemSell CapHong = ItemSell.getItemSell(722, (byte) 1);
                                    ItemMap itemROI = new ItemMap();
                                    itemROI.playerId = p.id;
                                    itemROI.x = _bTarget.x;
                                    itemROI.y = _bTarget.y;
//                                    itemROI.itemMapID = 722;
//                                    itemROI.itemTemplateID = (short)itemROI.itemMapID;
                                    itemROI.itemMapID = itemsMap.size();
                                    itemROI.itemTemplateID = (short) 722;
                                    //                                itemGOD.item.template = ItemTemplate.ItemTemplateID(_ITEMMAPID);
                                    //BUILD NEW ITEM + CHI SO CHO DO KICH HOAT
                                    Item _ITEMCapHong = new Item(CapHong.item);
                                    itemROI.item = _ITEMCapHong;
                                    itemDrop.add(itemROI);
                                    itemsMap.addAll(itemDrop);

                                    //đồng bộ boss chet, cho boss bien mat. add item map //68
                                    try {
                                        m = new Message(68);
                                        m.writer().writeShort(itemROI.itemMapID);
                                        m.writer().writeShort(itemROI.item.template.id);
                                        m.writer().writeShort(_bTarget.x);
                                        m.writer().writeShort(_bTarget.y);
                                        m.writer().writeInt(p.id);
                                        m.writer().flush();
//                                        for(Player _pl: players) {
//                                            _pl.session.sendMessage(m);
//                                        }
                                        for (byte i = 0; i < players.size(); i++) {
                                            if (players.get(i) != null) {
                                                players.get(i).session.sendMessage(m);
                                            }
                                        }
                                        m.cleanup();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else if (_bTarget._typeBoss == 4 || _bTarget._typeBoss == 6) {
                                Service.gI().sendThongBaoServer(p.name + " vừa tiêu diệt " + _bTarget.name + " mọi người đều ngưỡng mộ");
                                ItemMap item = dropItemGOD(p, _bTarget.x, _bTarget.y);

                                //ADD ITEM TO MAP
                                if (item != null) {
                                    try {
                                        itemDrop.add(item);
                                        itemsMap.addAll(itemDrop);
                                        m = new Message(68);
                                        m.writer().writeShort(item.itemMapID);
                                        m.writer().writeShort(item.item.template.id);
                                        m.writer().writeShort(_bTarget.x);
                                        m.writer().writeShort(_bTarget.y);
                                        m.writer().writeInt(p.id);
                                        m.writer().flush();
//                                        for(Player _pl: players) {
//                                            _pl.session.sendMessage(m);
//                                        }
                                        for (byte i = 0; i < players.size(); i++) {
                                            if (players.get(i) != null) {
                                                players.get(i).session.sendMessage(m);
                                            }
                                        }
                                        m.cleanup();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (Util.nextInt(0, 10) < 5) {
                                    ItemMap itemM = newItemMAP(979, p.id, _bTarget.x, _bTarget.y);
                                    if (itemM != null) {
                                        addItemToMap(itemM, p.id, _bTarget.x, _bTarget.y);
                                    }
                                }
                            } else if (_bTarget._typeBoss >= (byte) 44 && _bTarget._typeBoss <= (byte) 47) {
                                ItemMap itemM = cNewItemMap(590, p.id, _bTarget.x, _bTarget.y);
                                if (itemM != null) {
                                    addItemToMap(itemM, p.id, _bTarget.x, _bTarget.y);
                                }
                            } else if (_bTarget._typeBoss >= (byte) 31 && _bTarget._typeBoss <= (byte) 35) {
                                int itemDT = 17;
                                if (Util.nextInt(0, 4) == 0) {
                                    itemDT = 16;
                                }
                                ItemMap itemM = newItemMAP(itemDT, p.id, _bTarget.x, _bTarget.y);
                                if (itemM != null) {
                                    addItemToMap(itemM, p.id, _bTarget.x, _bTarget.y);
                                }
                            } else if (_bTarget._typeBoss >= (byte) 26 && _bTarget._typeBoss <= (byte) 28) {//cell rot 3s
                                ItemMap itemM = newItemMAP(16, p.id, _bTarget.x, _bTarget.y);
                                if (itemM != null) {
                                    addItemToMap(itemM, p.id, _bTarget.x, _bTarget.y);
                                }
                            } else if (_bTarget._typeBoss == (byte) 30) {//sieu bo hung rot 2s
                                ItemMap itemM = newItemMAP(15, p.id, _bTarget.x, _bTarget.y);
                                if (itemM != null) {
                                    addItemToMap(itemM, p.id, _bTarget.x, _bTarget.y);
                                }
                            } else if (_bTarget._typeBoss == (byte) 48) {//zamasu rot da bao ve
                                if (Util.nextInt(0, 10) <= 7) {
                                    ItemMap itemM = newItemMAP(987, p.id, _bTarget.x, _bTarget.y);
                                    if (itemM != null) {
                                        addItemToMap(itemM, p.id, _bTarget.x, _bTarget.y);
                                    }
                                }
                            } else if (_bTarget._typeBoss == (byte) 49 || _bTarget._typeBoss == (byte) 50) {//billwhis rot ruong
                                ItemMap itemM = newItemMAP(1055, p.id, _bTarget.x, _bTarget.y);
                                if (itemM != null) {
                                    addItemToMap(itemM, p.id, _bTarget.x, _bTarget.y);
                                }
                            } else if (_bTarget._typeBoss == (byte) 56 || _bTarget._typeBoss == (byte) 57 || _bTarget._typeBoss == (byte) 58) {//billwhis rot ruong
                                int idItemMap[] = {571, 572, 573, 574, 648, 649, 533, 77, 861};
                                ItemMap itemM = newItemMapBuff(idItemMap[Util.nextInt(0, 10)], p.id, _bTarget.x, _bTarget.y);
                                if (itemM != null) {
                                    addItemToMap(itemM, p.id, _bTarget.x, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                }
                            } else if (_bTarget._typeBoss == (byte) 53 || _bTarget._typeBoss == (byte) 54 || _bTarget._typeBoss == (byte) 55) {//billwhis rot ruong
                                int idItemMap[] = {571, 572, 573, 574, 648, 649, 533, 77, 861};
                                ItemMap itemM = newItemMapBuff(idItemMap[Util.nextInt(0, 10)], p.id, _bTarget.x, _bTarget.y);
                                if (itemM != null) {
                                    addItemToMap(itemM, p.id, _bTarget.x, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                    addItemToMap(itemM, p.id, _bTarget.x += 15, _bTarget.y);
                                }
                            }
                        }
                        if (_bTarget._typeBoss == 2 && p.havePet == 0) { //boss die la super broly
                            p.detu = _bTarget.detu;
                            leaveDEEEEE(_bTarget.detu);
                            p.havePet = 1;
                            p.isNewPet = true;
                            p.detu.id = (-100000 - p.id);
                            p.statusPet = 0;
                            p.petfucus = 1;
                            pets.add(p.detu);
//                            for(Player _plz: players) {
//                                loadInfoDeTu(_plz.session, p.detu);
//                            }
                            for (byte i = 0; i < players.size(); i++) {
                                if (players.get(i) != null) {
                                    loadInfoDeTu(players.get(i).session, p.detu);
                                }
                            }
                        } else {
                            leaveDEEEEE(_bTarget.detu);
                        }
                        //boss chet
                        if (_bTarget._typeBoss < (byte) 44 || _bTarget._typeBoss > (byte) 47) {
//                            for(Player _pp: players) {
//                                sendDieToAnotherPlayer(_pp, _bTarget);
//                            }
                            for (byte i = 0; i < players.size(); i++) {
                                if (players.get(i) != null) {
                                    sendDieToAnotherPlayer(players.get(i), _bTarget);
                                }
                            }
                        }
                        //CHECK NHIEM VU SAN BOSS
                        if (p.taskId == (short) 21 || p.taskId == (short) 22 || p.taskId == (short) 23 || p.taskId == (short) 25 || p.taskId == (short) 26 || p.taskId == (short) 27
                                || p.taskId == (short) 28 || p.taskId == (short) 29 || p.taskId == (short) 30 || p.taskId == (byte) 32) {
                            int idBoss = TaskManager.gI().mobTASK0[p.taskId][p.crrTask.index];
                            if ((_bTarget._typeBoss == (byte) (idBoss / 100)) || ((_bTarget._typeBoss == (byte) 41) && p.taskId == (byte) 32 && p.crrTask.index == (byte) 7)) {
                                TaskService.gI().updateCountTask(p);
                            }
                        } else if (p.taskId == (short) 19 && p.crrTask.index == (byte) 1 && _bTarget._typeBoss == (byte) 31) {
                            TaskService.gI().updateCountTask(p);
                            if (p.clan != null) {
                                for (byte mk = 0; mk < p.clan.members.size(); mk++) {
                                    Player member = PlayerManger.gI().getPlayerByUserID(p.clan.members.get(mk).id);
                                    if (member != null && member.session != null && member.map.id == 59) {
                                        if (member.taskId == (short) 19 && member.crrTask.index == (byte) 1) {
                                            TaskService.gI().updateCountTask(member);
                                        }
                                    }
                                }
                            }
                        }
                        //END NHIEM VU SAN BOSS
                        //CHECK NHIEM VU NHAN THOI KHONG
                        if (p.taskId == (short) 31 && p.crrTask.index == (byte) 0 && _bTarget._typeBoss == (byte) 6) {
                            int perNhan = Util.nextInt(0, 5);
                            if (perNhan == 0) {
                                Item itemNhan = ItemSell.getItemNotSell(992);
                                Item _itemNhan = new Item(itemNhan);
                                if (p.addItemToBag(_itemNhan)) {
                                    p.sendAddchatYellow("Bạn vừa nhận được nhẫn thời không sai lệch");
                                    Service.gI().updateItemBag(p);
                                    TaskService.gI().updateCountTask(p);
                                }
                            }
                        }
                        //END CHECK NHIEM VU NHAN THOI KHONG
                        //CHECK SP MABU
                        if (_bTarget._typeBoss >= (byte) 36 && _bTarget._typeBoss <= (byte) 39) {
                            p.pointMabu = (byte) 10;
                            Service.gI().setPowerPoint(p, "TL", (short) 10, (short) 10, (short) 10);
                        }
                        //END CHECK SP MABU
                        if (_bTarget._typeBoss >= (byte) 44 && _bTarget._typeBoss <= 47) {
                            leaveBossYardrat(_bTarget);
                        } else {
                            Timer timerBossLeave = new Timer();
                            TimerTask bossLeave = new TimerTask() {
                                public void run() {
                                    leaveBoss(_bTarget); //xoa boss
                                }
                            ;
                            };
                            timerBossLeave.schedule(bossLeave, 10000);
                        }
                    } else {
                        chimDameChar(p, _bTarget, damage);
                    }
                }
                // *****************************************************************************************************

                //send player attack to all player in map
                short _idSKILL = skillPlayerUse.skillId;
                if (p.isMonkey && (p.idSkillselect == 4)) {
                    _idSKILL = 105;
                } else if (p.isMonkey && (p.idSkillselect == 5)) {
                    _idSKILL = 106;
                }
                attachedChar(p.id, _bTarget.id, _idSKILL);
            }
        }
    }

    /////////// FIGHT CHARRRRRRRRRRRRRRRRR
    public void FightChar(Player p, int charId) throws IOException {
        int damage = 0;
        int damageChim = 0;
        Message m = null;
        Player _bTarget = getPlayerByID(charId);
//        Mob[] arMob = new Mob[10];
//        arMob[0] = mob;
        int miss = Util.nextInt(0, 10);

//        Skill skillUse = p.getSkillById(p.idSkillselect);
//        SkillData skilldata = new SkillData();
//        Skill skillPlayerUse = skilldata.getSkillBySkillTemplate(p.gender, skillUse.skillId, skillUse.point);
        Skill skillPlayerUse = p.getSkillByIDTemplate(p.idSkillselect);
        long _timeNOW = System.currentTimeMillis();
        short xSkill = (short) skillPlayerUse.dx;
        short ySkill = (short) skillPlayerUse.dy;
        if (skillPlayerUse.template.id == 0 || skillPlayerUse.template.id == 9 || skillPlayerUse.template.id == 2 || skillPlayerUse.template.id == 17 || skillPlayerUse.template.id == 4
                || skillPlayerUse.template.id == 1 || skillPlayerUse.template.id == 3 || skillPlayerUse.template.id == 5) {
//            xSkill = (short)(xSkill + 50);
//            ySkill = (short)(ySkill + 20);
            xSkill = (short) (xSkill + 100);
            ySkill = (short) (ySkill + 100);
        }
        //CHECK TAM DANH CUA SKILL
        if (((Math.abs(p.x - _bTarget.x) <= xSkill) && (Math.abs(p.y - _bTarget.y) <= ySkill)) && Service.gI().checkCanAttackChar(p, _bTarget) && !p.isTroi) {
            //use skill troi xayda
            if (p.idSkillselect == 23 && p.mp >= skillPlayerUse.manaUse && !p.isdie && !_bTarget.isdie && !p.isCharFreez && !p.isCharSleep && !p.isCharDCTT && !p.isCharBlind && (_timeNOW - skillPlayerUse.lastTimeUseThisSkill > (long) (skillPlayerUse.coolDown))) {
                skillPlayerUse.lastTimeUseThisSkill = _timeNOW; //gan time hien tai
                //CHECK NOI TAI TROI TANG SAT THUONG DON KE TIEP
                if (p.noiTai.id != 0 && skillPlayerUse.template.id == p.noiTai.idSkill) {
                    p.upDameAfterNoiTai = true;
                }
                p.mp -= skillPlayerUse.manaUse;
                postUpdateKI(p);
                p.isTroi = true;
                _bTarget.isCharFreez = true;
                p.CHARHOLD = _bTarget;
                // send effect troi
                try {
                    m = new Message(-124);
                    m.writer().writeByte(1); //b5
                    m.writer().writeByte(0); //b6
                    m.writer().writeByte(32); //num3
                    m.writer().writeInt(_bTarget.id); //id bi troi
                    m.writer().writeInt(p.id); //id troi
                    m.writer().flush();
                    for (Player pl : players) {
                        pl.session.sendMessage(m);
                    }

                    Timer timerHold = new Timer();
                    TimerTask bossHold = new TimerTask() {
                        public void run() {
                            if (!p.isTroi) {
                                timerHold.cancel();
                            } else {
                                Message m = null;
                                p.isTroi = false;
                                _bTarget.isCharFreez = false;
                                //remove setting troi, va effect tren nguoi mob
                                try {
                                    m = new Message(-124);
                                    m.writer().writeByte(0); //b5
                                    m.writer().writeByte(0); //b6
                                    m.writer().writeByte(32);
                                    m.writer().writeInt(_bTarget.id);
                                    m.writer().flush();
                                    for (Player pl : players) {
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

                                //remove effect troi cua player
                                try {
                                    m = new Message(-124);
                                    m.writer().writeByte(2);
                                    m.writer().writeByte(0);
                                    m.writer().writeInt(p.id);
                                    m.writer().flush();
                                    for (Player pl : players) {
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
                        }
                    ;
                    };
                    timerHold.schedule(bossHold, (skillPlayerUse.damage * 1000));

                    m.cleanup();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            //use thoi mien
            if (p.idSkillselect == 22 && p.mp >= skillPlayerUse.manaUse && !p.isdie && !_bTarget.isdie && !p.isCharFreez && !p.isCharSleep && !p.isCharDCTT && !p.isCharBlind && (_timeNOW - skillPlayerUse.lastTimeUseThisSkill > (long) (skillPlayerUse.coolDown))) {
                skillPlayerUse.lastTimeUseThisSkill = _timeNOW; //gan time hien tai
                //CHECK NOI TAI THOI MIEN TANG SAT THUONG DON KE TIEP
                if (p.noiTai.id != 0 && skillPlayerUse.template.id == p.noiTai.idSkill) {
                    p.upDameAfterNoiTai = true;
                }
                p.mp -= skillPlayerUse.manaUse;
                postUpdateKI(p);
                _bTarget.isCharSleep = true;
                // send effect thoi mien
                try {
                    m = new Message(-124);
                    m.writer().writeByte(1);
                    m.writer().writeByte(0);
                    m.writer().writeByte(41);
                    m.writer().writeInt(_bTarget.id);
                    m.writer().flush();
                    for (Player pl : players) {
                        pl.session.sendMessage(m);
                        Timer timerSleep = new Timer();
                        TimerTask bossSleep = new TimerTask() {
                            public void run() {
                                Message ms = null;
                                _bTarget.isCharSleep = false;
                                if (_bTarget.downDAME == 0) {
                                    _bTarget.downDAME = (byte) (20 + skillPlayerUse.point * 5);
                                    Timer timerDDAME = new Timer();
                                    TimerTask downDDAME = new TimerTask() {
                                        public void run() {
                                            _bTarget.downDAME = 0;
                                        }
                                    ;
                                    };
                                    timerDDAME.schedule(downDDAME, 10000);
                                }
                                //remove setting thoi mien, va effect tren nguoi mob
                                try {
                                    ms = new Message(-124);
                                    ms.writer().writeByte(0);
                                    ms.writer().writeByte(0);
                                    ms.writer().writeByte(41);
                                    ms.writer().writeInt(_bTarget.id);
                                    ms.writer().flush();
                                    pl.session.sendMessage(ms);
                                    ms.cleanup();
                                } catch (Exception var2) {
                                    var2.printStackTrace();
                                } finally {
                                    if (ms != null) {
                                        ms.cleanup();
                                    }
                                }
                            }
                        ;
                        };
                        timerSleep.schedule(bossSleep, (skillPlayerUse.damage * 1000));
                    }
                    m.cleanup();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                setSkillPaint(skillPlayerUse.skillId, p, 8);
                return;
            }
            //        use socola
            //        if(p.idSkillselect == 18) {
            //            int _manaSocola = (int)(skillPlayerUse.manaUse*p.getMpFull());
            //            if(p.mp >= _manaSocola) {
            //                p.mp -= _manaSocola;
            //                postUpdateKI(p);
            //                _bTarget.isCharSocola = true;
            //                // send effect socola
            ////                mob.isSocola = skillPlayerUse.damage;
            //                try {
            //                    m = new Message(-112);
            //                    m.writer().writeByte(1);
            //                    m.writer().writeByte(mobId);
            //                    m.writer().writeShort((short)(Util.nextInt(4127,4133)));
            //                    m.writer().flush();
            //                    for(Player pl: players) {
            //                        pl.session.sendMessage(m);
            //                        ResetSocolaTask socolaTask = new ResetSocolaTask(pl, mob);
            //                        Timer timerSocola = new Timer();
            //                        timerSocola.schedule(socolaTask, 30000);
            //                    }
            //                    m.cleanup();
            //                } catch (Exception e) {
            //                    e.printStackTrace();
            //                }
            //
            //                setSkillPaint(skillPlayerUse.skillId, p, 8);
            //                return;
            //            }
            //        }
            //dich chuyen tuc thoi
            if (p.idSkillselect == 20 && p.mp >= skillPlayerUse.manaUse && !p.isdie && !_bTarget.isdie && !p.isCharFreez && !p.isCharSleep && !p.isCharDCTT && !p.isCharBlind && (_timeNOW - skillPlayerUse.lastTimeUseThisSkill > (long) (skillPlayerUse.coolDown))) {
                skillPlayerUse.lastTimeUseThisSkill = _timeNOW; //gan time hien tai
                //CHECK NOI TAI DICH CHUYEN TUC THOI TANG SAT THUONG DON KE TIEP
                if (p.noiTai.id != 0 && skillPlayerUse.template.id == p.noiTai.idSkill) {
                    p.upDameAfterNoiTai = true;
                }
                // DCTT XONG KHONG MISS SKILL
                p.noMiss = true;
                p.mp -= skillPlayerUse.manaUse;
                p.PEMCRIT = 1;
                postUpdateKI(p);
                _bTarget.isCharDCTT = true;
                try {
                    m = new Message(123);
                    m.writer().writeInt(p.id);
                    m.writer().writeShort(_bTarget.x);
                    m.writer().writeShort(_bTarget.y);
                    m.writer().writeByte(1);
                    m.writer().flush();
//                    for(Player pl: players) {
//                        pl.session.sendMessage(m);
//                    }
                    for (byte i = 0; i < players.size(); i++) {
                        if (players.get(i) != null) {
                            players.get(i).session.sendMessage(m);
                        }
                    }
                    m.cleanup();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // gui hieu ung choang cho client
                try {
                    m = new Message(-124);
                    m.writer().writeByte(1);
                    m.writer().writeByte(0);
                    m.writer().writeByte(40);
                    m.writer().writeInt(_bTarget.id);
                    m.writer().flush();
                    for (Player pl : players) {
                        pl.session.sendMessage(m);

                        Timer dcttTask = new Timer();
                        TimerTask bossDCTT = new TimerTask() {
                            public void run() {
                                Message m = null;
                                _bTarget.isCharDCTT = false;
                                //remove setting troi, va effect tren nguoi mob
                                try {
                                    m = new Message(-124);
                                    m.writer().writeByte(0);
                                    m.writer().writeByte(0);
                                    m.writer().writeByte(40);
                                    m.writer().writeInt(_bTarget.id);
                                    m.writer().flush();
                                    pl.session.sendMessage(m);
                                    m.cleanup();
                                } catch (Exception var2) {
                                    var2.printStackTrace();
                                } finally {
                                    if (m != null) {
                                        m.cleanup();
                                    }
                                }
                            }
                        ;
                        };
                        dcttTask.schedule(bossDCTT, (skillPlayerUse.damage));
                    }
                    m.cleanup();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int dameDCTT = p.getDamFull();
                //CHECK GIAP XEN
                if (_bTarget.useGiapXen) {
                    dameDCTT = (int) (dameDCTT / 2);
                }
                //CHECK KHIEN NANG LUONG CUA DOI THU
                if (_bTarget.isProtect) {
                    dameDCTT = 1;
                }
                // GIAM DAME SAU KHI BI THOI MIEN
                if (p.downDAME > 0) {
                    dameDCTT -= (int) (dameDCTT * Util.getPercentDouble((int) (p.downDAME)));
                }
                //CHECK DAME % TANG LEN SAU KHI DUNG SKILL VA CO NOI TAIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
//                if(p.upDameAfterNoiTai && p.noiTai.id != 0) {//% DAME DON KE TIEP
//                    p.upDameAfterNoiTai = false;
//                    dameDCTT += (int)(dameDCTT*Util.getPercentDouble((int)p.noiTai.param));
//                } else if (skillPlayerUse.template.id == p.noiTai.idSkill) { //%DAM CHIEU DAM VA CHUONG
//                    dameDCTT += (int)(dameDCTT*Util.getPercentDouble((int)p.noiTai.param));
//                } else if (p.upDameAfterKhi) { //HOA KHI TANG DAME
//                    dameDCTT += (int)(dameDCTT*Util.getPercentDouble((int)p.noiTai.param));
//                }
                //CHECK SET KICH HOAT SONGOKU
//                if(p.getSetKichHoatFull() == (byte)3 && skillPlayerUse.template.id == (byte)1) {
//                    dameDCTT = dameDCTT*2;
//                } else if(p.getSetKichHoatFull() == (byte)7 && skillPlayerUse.template.id == (byte)4) { //SET KICH HOAT KAKAROT
//                    dameDCTT = dameDCTT*2;
//                }
                _bTarget.hp -= dameDCTT;
                if (_bTarget.hp <= 0) {
                    _bTarget.isdie = true;
                    _bTarget.hp = 0;
                } else {
                    //PHAN SAT THUONG
                    int damePST = _bTarget.getPercentPST();
                    if (damePST > 0) {
                        damePST = (int) (dameDCTT * Util.getPercentDouble(damePST));
                        p.hp = (p.hp - damePST) < 1 ? 1 : (p.hp - damePST);
                        //send dame
                        dameChar(p.id, p.hp, damePST, false);
                    }
                }
                if (_bTarget.isdie) {
                    dameChar(_bTarget.id, _bTarget.hp, dameDCTT, true);
                    //NEU DEO NGOC RONG SAO DEN THI ROT RA DAT
                    Service.gI().dropDragonBall(_bTarget);
                    //CHECK NEU DANG CON DE TRUNG THI REMOVE DE TRUNG
                    if (_bTarget.chimFollow == (byte) 1 && _bTarget.dameChim > 0) {
                        useDeTrung(_bTarget, (byte) 7);
                        _bTarget.chimFollow = (byte) 0;
                        _bTarget.dameChim = 0;
                        _bTarget.timerDeTrung.cancel();
                        _bTarget.timerDeTrung = null;
                    }
                    for (Player _pll : players) {
                        if (_pll.id == _bTarget.id) {
                            if (_bTarget.isMonkey) {
                                Service.gI().loadCaiTrangTemp(_bTarget);

                                _bTarget.isMonkey = false;
                                //NOI TAI TANG DAME KHI HOA KHI
                                if (_bTarget.upDameAfterKhi && _bTarget.noiTai.id != 0 && _bTarget.noiTai.idSkill == (byte) 13) {
                                    _bTarget.upDameAfterKhi = false;
                                }
                                //NOI TAI TANG DAME KHI HOA KHI
                                Service.gI().loadPoint(_bTarget.session, _bTarget);
                            }
                            sendDieToMe(_bTarget);
                        } else {
                            _pll.sendDefaultTransformToPlayer(_bTarget);
                            sendDieToAnotherPlayer(_pll, _bTarget);
                        }
                    }
                    //CHECK NHIEM VU THACH DAU 10 NGUOI
                    if (p.taskId == (short) 16 && p.crrTask.index == (byte) 0) {
                        TaskService.gI().updateCountTask(p);
                    }
                    //
                    //CHECK SP MABU
                    if (p.map.id >= 114 && p.map.id <= 119) {
                        p.pointMabu = (byte) (p.pointMabu + (byte) 1) > (byte) 10 ? (byte) 10 : (byte) (p.pointMabu + (byte) 1);
                        Service.gI().setPowerPoint(p, "TL", (short) p.pointMabu, (short) 10, (short) 10);
                    }
                    //END CHECK SP MABU
                    if (p.timerDHVT != null) {
                        DaiHoiService.gI().winRoundDHVT(p, _bTarget);
                    }
                } else {
                    dameChar(_bTarget.id, _bTarget.hp, dameDCTT, true);
                    //dame hut mau
                    sendHutHP(p, dameDCTT, false);
                    //dame hut mau
                    //dame hut ki
                    sendHutKI(p, dameDCTT);
                    //dame hut ki
                }

                //send player attack to all player in map
                short _idSKILL = skillPlayerUse.skillId;
                if (p.isMonkey && (p.idSkillselect == 4)) {
                    _idSKILL = 105;
                } else if (p.isMonkey && (p.idSkillselect == 5)) {
                    _idSKILL = 106;
                }
                attachedChar(p.id, _bTarget.id, _idSKILL);
                return;
            }
            //dam va chuong boss
            if (p.idSkillselect != 10) {
                //vua dung DCTT THI KHONG MISS SKILL
                if (p.noMiss) {
                    p.noMiss = false;
                    miss = 7;
                }
                if (miss < 8) {
                    //CHECK SET NE NEU CO
                    miss = Util.nextInt(1, 11);
                    if (miss > _bTarget.getPercentNedon()) {
                        damage = Util.nextInt((int) (p.getDamFull() * 0.9 * Util.getPercentDouble((int) (skillPlayerUse.damage))), (int) (p.getDamFull() * Util.getPercentDouble((int) (skillPlayerUse.damage))));
                    }
                }
                int kiUse = skillPlayerUse.manaUse;
                if (p.mp >= kiUse && !p.isdie && !_bTarget.isdie && !p.isCharFreez && !p.isCharSleep && !p.isCharDCTT && !p.isCharBlind && (_timeNOW - skillPlayerUse.lastTimeUseThisSkill > (long) (skillPlayerUse.coolDown)) && (_timeNOW >= p.timeCanSkill)) {
                    //CHECK NOI TAI LAZE CO GIAM THOI GIAN HOI CHIEU
                    if (p.noiTai.id != 0 && skillPlayerUse.template.id == p.noiTai.idSkill && p.idSkillselect == 11) { //LAZE
                        long _timeDOWN = (long) (Util.getPercentDouble((int) (p.noiTai.param)) * skillPlayerUse.coolDown);
                        skillPlayerUse.lastTimeUseThisSkill = _timeNOW - _timeDOWN;
                        //SEND TIME DOWN NOI TAI
                        Service.gI().sendCoolDownSkill(p, skillPlayerUse.skillId, (int) ((long) skillPlayerUse.coolDown - _timeDOWN));
                    } else {
                        skillPlayerUse.lastTimeUseThisSkill = _timeNOW;//gan time hien tai
                        if (skillPlayerUse.template.id == (byte) 1 || skillPlayerUse.template.id == (byte) 3 || skillPlayerUse.template.id == (byte) 5 || skillPlayerUse.template.id == (byte) 11) {
                            p.timeCanSkill = _timeNOW + (long) (500);
                        } else {
                            p.timeCanSkill = _timeNOW + (long) (skillPlayerUse.coolDown);
                        }
                    }
                    //CHECK KAIOKEN DE TRU 10% HP
                    if (skillPlayerUse.template.id == 9 && p.hp > (int) (p.getHpFull() * 0.1)) {
                        p.hp -= (int) (p.getHpFull() * 0.1);
                        postUpdateHP(p);
                    }
                    p.mp -= kiUse;
                    int fantashi = Util.nextInt(0, 100);
                    boolean fatal = p.getCritFull() >= fantashi;
                    //CHECK NEU BI TROI THI DANH AUTO CRIT
                    if (_bTarget.isCharFreez || (p.noiTai.id != 0 && p.noiTai.idSkill == (byte) (-4) && p.hp <= (int) (p.getHpFull() * Util.getPercentDouble((int) (p.noiTai.param))))) { //CHECK NOI TAI crit 
                        fatal = true;
                    }
                    if (fatal || p.PEMCRIT > 0) {
                        damage = damage * 2;
                        p.PEMCRIT = 0;
                        fatal = true;
                    }
                    //CHECK GIAP XEN
                    if (_bTarget.useGiapXen) {
                        damage = (int) (damage / 2);
                    }
                    if (p.chimFollow == (byte) 1) {
                        damageChim = damage;
                    }
                    //CHECK KHIEN NANG LUONG CUA DOI THU
                    if (_bTarget.isProtect) {
                        damage = 1;
                    }
                    // GIAM DAME SAU KHI BI THOI MIEN
                    if (p.downDAME > 0) {
                        damage -= (int) (damage * Util.getPercentDouble((int) (p.downDAME)));
                        if (p.chimFollow == (byte) 1) {
                            damageChim -= (int) (damageChim * Util.getPercentDouble((int) (p.downDAME)));
                        }
                    }
                    //CHECK DAME % TANG LEN SAU KHI DUNG SKILL VA CO NOI TAIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
                    if (p.upDameAfterNoiTai && p.noiTai.id != 0) {//% DAME DON KE TIEP
                        p.upDameAfterNoiTai = false;
                        damage += (int) (damage * Util.getPercentDouble((int) p.noiTai.param));
                        if (p.chimFollow == (byte) 1) {
                            damageChim += (int) (damageChim * Util.getPercentDouble((int) p.noiTai.param));
                        }
                    } else if (skillPlayerUse.template.id == p.noiTai.idSkill) { //%DAM CHIEU DAM VA CHUONG
                        damage += (int) (damage * Util.getPercentDouble((int) p.noiTai.param));
                        if (p.chimFollow == (byte) 1) {
                            damageChim += (int) (damageChim * Util.getPercentDouble((int) p.noiTai.param));
                        }
                    } else if (p.upDameAfterKhi) { //HOA KHI TANG DAME
                        damage += (int) (damage * Util.getPercentDouble((int) p.noiTai.param));
                        if (p.chimFollow == (byte) 1) {
                            damageChim += (int) (damageChim * Util.getPercentDouble((int) p.noiTai.param));
                        }
                    }
                    //CHECK SET KICH HOAT SONGOKU
                    if (p.getSetKichHoatFull() == (byte) 3 && skillPlayerUse.template.id == (byte) 1) {
                        damage = damage * 2;
                    } else if (p.getSetKichHoatFull() == (byte) 7 && skillPlayerUse.template.id == (byte) 4) { //SET KICH HOAT KAKAROT
                        damage = damage * 2;
                    } else if (p.getSetKichHoatFull() == (byte) 5 && skillPlayerUse.template.id == (byte) 17) { //SET KICH HOAT OC TIÊU
                        damage = damage * 2;
                    }
                    //END CHECK SET KICH HOAT SONGOKU
                    // dame laze
                    if (p.idSkillselect == 11) {
                        damage = (int) (p.mp * Util.getPercentDouble((int) skillPlayerUse.damage));
                        //CHECK SET KICH HOAT LAZE
                        if (p.getSetKichHoatFull() == (byte) 4) {
                            damage = (int) (damage * 1.5);
                        }
                        p.mp = 0;
                        //                drawSkillVip(p, mob, skillPlayerUse.skillId, damage);
                    }
                    _bTarget.hp -= damage;
                    if (_bTarget.hp <= 0) {
                        _bTarget.isdie = true;
                        _bTarget.hp = 0;
                    } else {
                        //PHAN SAT THUONG
                        int damePST = _bTarget.getPercentPST();
                        if (damePST > 0) {
                            damePST = (int) (damage * Util.getPercentDouble(damePST));
                            p.hp = (p.hp - damePST) < 1 ? 1 : (p.hp - damePST);
                            //send dame
                            dameChar(p.id, p.hp, damePST, false);
                        }
                    }
                    if (_bTarget.isdie) {
                        dameChar(_bTarget.id, _bTarget.hp, damage, fatal);
                        //NEU DEO NGOC RONG SAO DEN THI ROT RA DAT
                        Service.gI().dropDragonBall(_bTarget);
                        //CHECK NEU DANG CON DE TRUNG THI REMOVE DE TRUNG
                        if (_bTarget.chimFollow == (byte) 1 && _bTarget.dameChim > 0) {
                            useDeTrung(_bTarget, (byte) 7);
                            _bTarget.chimFollow = (byte) 0;
                            _bTarget.dameChim = 0;
                            _bTarget.timerDeTrung.cancel();
                            _bTarget.timerDeTrung = null;
                        }
                        for (Player _pll : players) {
                            if (_pll.id == _bTarget.id) {
                                if (_bTarget.isMonkey) {
                                    Service.gI().loadCaiTrangTemp(_bTarget);
                                    _bTarget.isMonkey = false;
                                    //NOI TAI TANG DAME KHI HOA KHI
                                    if (_bTarget.upDameAfterKhi && _bTarget.noiTai.id != 0 && _bTarget.noiTai.idSkill == (byte) 13) {
                                        _bTarget.upDameAfterKhi = false;
                                    }
                                    //NOI TAI TANG DAME KHI HOA KHI
                                    Service.gI().loadPoint(_bTarget.session, _bTarget);
                                }
                                sendDieToMe(_bTarget);
                            } else {
                                _pll.sendDefaultTransformToPlayer(_bTarget);
                                sendDieToAnotherPlayer(_pll, _bTarget);
                            }
                        }
                        //CHECK NHIEM VU THACH DAU 10 NGUOI
                        if (p.taskId == (short) 16 && p.crrTask.index == (byte) 0) {
                            TaskService.gI().updateCountTask(p);
                        }
                        //CHECK SP MABU
                        if (p.map.id >= 114 && p.map.id <= 119) {
                            p.pointMabu = (byte) (p.pointMabu + (byte) 1) > (byte) 10 ? (byte) 10 : (byte) (p.pointMabu + (byte) 1);
                            Service.gI().setPowerPoint(p, "TL", (short) p.pointMabu, (short) 10, (short) 10);
                        }
                        if (p.timerDHVT != null) {
                            DaiHoiService.gI().winRoundDHVT(p, _bTarget);
                        }
                    } else {
                        dameChar(_bTarget.id, _bTarget.hp, damage, fatal);
                        //dame hut mau
                        sendHutHP(p, damage, false);
                        //dame hut mau
                        //dame hut ki
                        sendHutKI(p, damage);
                        //dame hut ki
                    }

                    //CHIM ATTACK CHAR *****************************************************************************************************
                    if (p.chimFollow == (byte) 1 && !_bTarget.isdie && p.dameChim > 0) {
                        //DAME CHIM
                        damageChim = (int) (damageChim * Util.getPercentDouble(p.dameChim));
                        if (p.getSetKichHoatFull() == (byte) 6) {
                            damageChim = (int) (damageChim * 2);
                        }
                        damageChim = damageChim > 1 ? damageChim : 1;
                        _bTarget.hp -= damageChim;
                        if (_bTarget.hp <= 0) {
                            _bTarget.isdie = true;
                            _bTarget.hp = 0;
                        }
                        if (_bTarget.isdie) {
                            chimDameChar(p, _bTarget, damageChim);
                            //NEU DEO NGOC RONG SAO DEN THI ROT RA DAT
                            Service.gI().dropDragonBall(_bTarget);
                            //CHECK NEU DANG CON DE TRUNG THI REMOVE DE TRUNG
                            if (_bTarget.chimFollow == (byte) 1 && _bTarget.dameChim > 0) {
                                useDeTrung(_bTarget, (byte) 7);
                                _bTarget.chimFollow = (byte) 0;
                                _bTarget.dameChim = 0;
                                _bTarget.timerDeTrung.cancel();
                                _bTarget.timerDeTrung = null;
                            }
                            for (Player _pll : players) {
                                if (_pll.id == _bTarget.id) {
                                    if (_bTarget.isMonkey) {
                                        Service.gI().loadCaiTrangTemp(_bTarget);
                                        _bTarget.isMonkey = false;
                                        //NOI TAI TANG DAME KHI HOA KHI
                                        if (_bTarget.upDameAfterKhi && _bTarget.noiTai.id != 0 && _bTarget.noiTai.idSkill == (byte) 13) {
                                            _bTarget.upDameAfterKhi = false;
                                        }
                                        //NOI TAI TANG DAME KHI HOA KHI
                                        Service.gI().loadPoint(_bTarget.session, _bTarget);
                                    }
                                    sendDieToMe(_bTarget);
                                } else {
                                    _pll.sendDefaultTransformToPlayer(_bTarget);
                                    sendDieToAnotherPlayer(_pll, _bTarget);
                                }
                            }
                            //CHECK NHIEM VU THACH DAU 10 NGUOI
                            if (p.taskId == (short) 16 && p.crrTask.index == (byte) 0) {
                                TaskService.gI().updateCountTask(p);
                            }
                            //CHECK SP MABU
                            if (p.map.id >= 114 && p.map.id <= 119) {
                                p.pointMabu = (byte) (p.pointMabu + (byte) 1) > (byte) 10 ? (byte) 10 : (byte) (p.pointMabu + (byte) 1);
                                Service.gI().setPowerPoint(p, "TL", (short) p.pointMabu, (short) 10, (short) 10);
                            }
                        } else {
                            chimDameChar(p, _bTarget, damageChim);
                        }
                    }
                    // *****************************************************************************************************

                    //send player attack to all player in map
                    short _idSKILL = skillPlayerUse.skillId;
                    if (p.isMonkey && (p.idSkillselect == 4)) {
                        _idSKILL = 105;
                    } else if (p.isMonkey && (p.idSkillselect == 5)) {
                        _idSKILL = 106;
                    }
                    attachedChar(p.id, _bTarget.id, _idSKILL);
                }
            } else {
                attachedChar(p.id, _bTarget.id, skillPlayerUse.skillId);
            }
        }
    }

    //FIGHT DE TU
    public void FightDetu(Player p, int charId) throws IOException {
        int damage = 0;
        int damageChim = 0;
        Message m = null;
        Detu _bTarget = getDetuByID(charId);
        int miss = Util.nextInt(0, 10);

        Skill skillPlayerUse = p.getSkillByIDTemplate(p.idSkillselect);
        long _timeNOW = System.currentTimeMillis();
        short xSkill = (short) skillPlayerUse.dx;
        short ySkill = (short) skillPlayerUse.dy;
        if (skillPlayerUse.template.id == 0 || skillPlayerUse.template.id == 9 || skillPlayerUse.template.id == 2 || skillPlayerUse.template.id == 17 || skillPlayerUse.template.id == 4
                || skillPlayerUse.template.id == 1 || skillPlayerUse.template.id == 3 || skillPlayerUse.template.id == 5) {
            xSkill = (short) (xSkill + 100);
            ySkill = (short) (ySkill + 100);
        }
        //CHECK TAM DANH CUA SKILL
        if (((Math.abs(p.x - _bTarget.x) <= xSkill) && (Math.abs(p.y - _bTarget.y) <= ySkill)) && Service.gI().checkCanAttackDeTu(p, _bTarget) && !p.isTroi) {
            //use skill troi xayda
            if (p.idSkillselect == 23 && p.mp >= skillPlayerUse.manaUse && !p.isdie && !_bTarget.isdie && !p.isCharFreez && !p.isCharSleep && !p.isCharDCTT && !p.isCharBlind && (_timeNOW - skillPlayerUse.lastTimeUseThisSkill > (long) (skillPlayerUse.coolDown))) {
                skillPlayerUse.lastTimeUseThisSkill = _timeNOW; //gan time hien tai
                //CHECK NOI TAI TROI TANG SAT THUONG DON KE TIEP
                if (p.noiTai.id != 0 && skillPlayerUse.template.id == p.noiTai.idSkill) {
                    p.upDameAfterNoiTai = true;
                }
                p.mp -= skillPlayerUse.manaUse;
                postUpdateKI(p);
                p.isTroi = true;
                _bTarget.isCharFreez = true;
                p.DETUHOLD = _bTarget;
                // send effect troi
                try {
                    m = new Message(-124);
                    m.writer().writeByte(1); //b5
                    m.writer().writeByte(0); //b6
                    m.writer().writeByte(32); //num3
                    m.writer().writeInt(_bTarget.id); //id bi troi
                    m.writer().writeInt(p.id); //id troi
                    m.writer().flush();
                    for (Player pl : players) {
                        pl.session.sendMessage(m);
                    }
                    Timer timerHold = new Timer();
                    TimerTask bossHold = new TimerTask() {
                        public void run() {
                            if (!p.isTroi) {
                                timerHold.cancel();
                            } else {
                                Message m = null;
                                p.isTroi = false;
                                _bTarget.isCharFreez = false;
                                //remove setting troi, va effect tren nguoi mob
                                try {
                                    m = new Message(-124);
                                    m.writer().writeByte(0); //b5
                                    m.writer().writeByte(0); //b6
                                    m.writer().writeByte(32);
                                    m.writer().writeInt(_bTarget.id);
                                    m.writer().flush();
                                    for (Player pl : players) {
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

                                //remove effect troi cua player
                                try {
                                    m = new Message(-124);
                                    m.writer().writeByte(2);
                                    m.writer().writeByte(0);
                                    m.writer().writeInt(p.id);
                                    m.writer().flush();
                                    for (Player pl : players) {
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
                        }
                    ;
                    };
                    timerHold.schedule(bossHold, (skillPlayerUse.damage * 1000));

                    m.cleanup();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            //use thoi mien
            if (p.idSkillselect == 22 && p.mp >= skillPlayerUse.manaUse && !p.isdie && !_bTarget.isdie && !p.isCharFreez && !p.isCharSleep && !p.isCharDCTT && !p.isCharBlind && (_timeNOW - skillPlayerUse.lastTimeUseThisSkill > (long) (skillPlayerUse.coolDown))) {
                skillPlayerUse.lastTimeUseThisSkill = _timeNOW; //gan time hien tai
                //CHECK NOI TAI THOI MIEN TANG SAT THUONG DON KE TIEP
                if (p.noiTai.id != 0 && skillPlayerUse.template.id == p.noiTai.idSkill) {
                    p.upDameAfterNoiTai = true;
                }
                p.mp -= skillPlayerUse.manaUse;
                postUpdateKI(p);
                _bTarget.isCharSleep = true;
                // send effect thoi mien
                try {
                    m = new Message(-124);
                    m.writer().writeByte(1);
                    m.writer().writeByte(0);
                    m.writer().writeByte(41);
                    m.writer().writeInt(_bTarget.id);
                    m.writer().flush();
                    for (Player pl : players) {
                        pl.session.sendMessage(m);
                        Timer timerSleep = new Timer();
                        TimerTask bossSleep = new TimerTask() {
                            public void run() {
                                Message ms = null;
                                _bTarget.isCharSleep = false;
                                if (_bTarget.downDAME == 0) {
                                    _bTarget.downDAME = (byte) (20 + skillPlayerUse.point * 5);
                                    Timer timerDDAME = new Timer();
                                    TimerTask downDDAME = new TimerTask() {
                                        public void run() {
                                            _bTarget.downDAME = 0;
                                        }
                                    ;
                                    };
                                    timerDDAME.schedule(downDDAME, 10000);
                                }
                                //remove setting thoi mien, va effect tren nguoi mob
                                try {
                                    ms = new Message(-124);
                                    ms.writer().writeByte(0);
                                    ms.writer().writeByte(0);
                                    ms.writer().writeByte(41);
                                    ms.writer().writeInt(_bTarget.id);
                                    ms.writer().flush();
                                    pl.session.sendMessage(ms);
                                    ms.cleanup();
                                } catch (Exception var2) {
                                    var2.printStackTrace();
                                } finally {
                                    if (ms != null) {
                                        ms.cleanup();
                                    }
                                }
                            }
                        ;
                        };
                        timerSleep.schedule(bossSleep, (skillPlayerUse.damage * 1000));
                    }
                    m.cleanup();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                setSkillPaint(skillPlayerUse.skillId, p, 8);
                return;
            }
            //        use socola
            //        if(p.idSkillselect == 18) {
            //            int _manaSocola = (int)(skillPlayerUse.manaUse*p.getMpFull());
            //            if(p.mp >= _manaSocola) {
            //                p.mp -= _manaSocola;
            //                postUpdateKI(p);
            //                _bTarget.isCharSocola = true;
            //                // send effect socola
            ////                mob.isSocola = skillPlayerUse.damage;
            //                try {
            //                    m = new Message(-112);
            //                    m.writer().writeByte(1);
            //                    m.writer().writeByte(mobId);
            //                    m.writer().writeShort((short)(Util.nextInt(4127,4133)));
            //                    m.writer().flush();
            //                    for(Player pl: players) {
            //                        pl.session.sendMessage(m);
            //                        ResetSocolaTask socolaTask = new ResetSocolaTask(pl, mob);
            //                        Timer timerSocola = new Timer();
            //                        timerSocola.schedule(socolaTask, 30000);
            //                    }
            //                    m.cleanup();
            //                } catch (Exception e) {
            //                    e.printStackTrace();
            //                }
            //
            //                setSkillPaint(skillPlayerUse.skillId, p, 8);
            //                return;
            //            }
            //        }
            //dich chuyen tuc thoi
            if (p.idSkillselect == 20 && p.mp >= skillPlayerUse.manaUse && !p.isdie && !_bTarget.isdie && !p.isCharFreez && !p.isCharSleep && !p.isCharDCTT && !p.isCharBlind && (_timeNOW - skillPlayerUse.lastTimeUseThisSkill > (long) (skillPlayerUse.coolDown))) {
                skillPlayerUse.lastTimeUseThisSkill = _timeNOW; //gan time hien tai
                //CHECK NOI TAI DICH CHUYEN TUC THOI TANG SAT THUONG DON KE TIEP
                if (p.noiTai.id != 0 && skillPlayerUse.template.id == p.noiTai.idSkill) {
                    p.upDameAfterNoiTai = true;
                }
                // DCTT XONG KHONG MISS SKILL
                p.noMiss = true;
                p.mp -= skillPlayerUse.manaUse;
                p.PEMCRIT = 1;
                postUpdateKI(p);
                _bTarget.isCharDCTT = true;
                try {
                    m = new Message(123);
                    m.writer().writeInt(p.id);
                    m.writer().writeShort(_bTarget.x);
                    m.writer().writeShort(_bTarget.y);
                    m.writer().writeByte(1);
                    m.writer().flush();
//                    for(Player pl: players) {
//                        pl.session.sendMessage(m);
//                    }
                    for (byte i = 0; i < players.size(); i++) {
                        if (players.get(i) != null) {
                            players.get(i).session.sendMessage(m);
                        }
                    }
                    m.cleanup();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // gui hieu ung choang cho client
                try {
                    m = new Message(-124);
                    m.writer().writeByte(1);
                    m.writer().writeByte(0);
                    m.writer().writeByte(40);
                    m.writer().writeInt(_bTarget.id);
                    m.writer().flush();
                    for (Player pl : players) {
                        pl.session.sendMessage(m);

                        Timer dcttTask = new Timer();
                        TimerTask bossDCTT = new TimerTask() {
                            public void run() {
                                Message m = null;
                                _bTarget.isCharDCTT = false;
                                //remove setting troi, va effect tren nguoi mob
                                try {
                                    m = new Message(-124);
                                    m.writer().writeByte(0);
                                    m.writer().writeByte(0);
                                    m.writer().writeByte(40);
                                    m.writer().writeInt(_bTarget.id);
                                    m.writer().flush();
                                    pl.session.sendMessage(m);
                                    m.cleanup();
                                } catch (Exception var2) {
                                    var2.printStackTrace();
                                } finally {
                                    if (m != null) {
                                        m.cleanup();
                                    }
                                }
                            }
                        ;
                        };
                        dcttTask.schedule(bossDCTT, (skillPlayerUse.damage));
                    }
                    m.cleanup();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                int dameDCTT = p.getDamFull();
                //CHECK KHIEN NANG LUONG CUA DOI THU
                if (_bTarget.isProtect) {
                    dameDCTT = 1;
                }
                // GIAM DAME SAU KHI BI THOI MIEN
                if (p.downDAME > 0) {
                    dameDCTT -= (int) (dameDCTT * Util.getPercentDouble((int) (p.downDAME)));
                }
                _bTarget.hp -= dameDCTT;
                if (_bTarget.hp <= 0) {
                    _bTarget.isdie = true;
                    _bTarget.hp = 0;
                }
                if (_bTarget.isdie) {
                    dameChar(_bTarget.id, _bTarget.hp, dameDCTT, true);
                    //SEND TASK HOI SINH DE TU NEU DANH CHET
                    Player suPhu = PlayerManger.gI().getPlayerByDetuID(_bTarget.id);
                    Timer hoiSinhDetu = new Timer();
                    TimerTask hsDetu = new TimerTask() {
                        public void run() {
                            if (_bTarget.isdie) {
                                suPhu.timerHSDe = null;
                                hoiSinhDetu.cancel();
//                                    Player suPhu = PlayerManger.gI().getPlayerByDetuID(_bTarget.id);
                                _bTarget.x = suPhu.x;
                                _bTarget.y = suPhu.y;
                                Service.gI().petLiveFromDead(suPhu);
                                if (suPhu.statusPet == (byte) 1 || suPhu.statusPet == (byte) 2) {
                                    PetAttack(suPhu, _bTarget, suPhu.statusPet);
                                }
                            } else {
                                hoiSinhDetu.cancel();
                            }
                        }
                    ;
                    };
                        hoiSinhDetu.schedule(hsDetu, 60000);
                    suPhu.timerHSDe = hoiSinhDetu;
                    for (Player _pll : players) {
                        if (_pll.id == _bTarget.id) {
                            if (_bTarget.isMonkey) {
                                Service.gI().loadCaiTrangTemp(_bTarget);
                                _bTarget.isMonkey = false;
                                //NOI TAI TANG DAME KHI HOA KHI
                                if (_bTarget.upDameAfterKhi && _bTarget.noiTai.id != 0 && _bTarget.noiTai.idSkill == (byte) 13) {
                                    _bTarget.upDameAfterKhi = false;
                                }
                                //NOI TAI TANG DAME KHI HOA KHI
                                Service.gI().loadPoint(_bTarget.session, _bTarget);
                            }
                            sendDieToMe(_bTarget);
                        } else {
                            _pll.sendDefaultTransformToPlayer(_bTarget);
                            sendDieToAnotherPlayer(_pll, _bTarget);
                        }
                    }
                } else {
                    dameChar(_bTarget.id, _bTarget.hp, dameDCTT, true);
                    //dame hut mau
                    sendHutHP(p, dameDCTT, false);
                    //dame hut mau
                    //dame hut ki
                    sendHutKI(p, dameDCTT);
                    //dame hut ki
                }

                //send player attack to all player in map
                short _idSKILL = skillPlayerUse.skillId;
                if (p.isMonkey && (p.idSkillselect == 4)) {
                    _idSKILL = 105;
                } else if (p.isMonkey && (p.idSkillselect == 5)) {
                    _idSKILL = 106;
                }
                attachedChar(p.id, _bTarget.id, _idSKILL);
                return;
            }
            //dam va chuong boss
            if (p.idSkillselect != 10) {
                //vua dung DCTT THI KHONG MISS SKILL
                if (p.noMiss) {
                    p.noMiss = false;
                    miss = 7;
                }
                if (miss < 8) {
                    //CHECK SET NE NEU CO
                    miss = Util.nextInt(1, 11);
                    if (miss > _bTarget.getPercentNedon()) {
                        damage = Util.nextInt((int) (p.getDamFull() * 0.9 * Util.getPercentDouble((int) (skillPlayerUse.damage))), (int) (p.getDamFull() * Util.getPercentDouble((int) (skillPlayerUse.damage))));
                    }
                }
                int kiUse = skillPlayerUse.manaUse;
                if (p.mp >= kiUse && !p.isdie && !_bTarget.isdie && !p.isCharFreez && !p.isCharSleep && !p.isCharDCTT && !p.isCharBlind && (_timeNOW - skillPlayerUse.lastTimeUseThisSkill > (long) (skillPlayerUse.coolDown)) && (_timeNOW >= p.timeCanSkill)) {
                    //CHECK NOI TAI LAZE CO GIAM THOI GIAN HOI CHIEU
                    if (p.noiTai.id != 0 && skillPlayerUse.template.id == p.noiTai.idSkill && p.idSkillselect == 11) { //LAZE
                        long _timeDOWN = (long) (Util.getPercentDouble((int) (p.noiTai.param)) * skillPlayerUse.coolDown);
                        skillPlayerUse.lastTimeUseThisSkill = _timeNOW - _timeDOWN;
                        //SEND TIME DOWN NOI TAI
                        Service.gI().sendCoolDownSkill(p, skillPlayerUse.skillId, (int) ((long) skillPlayerUse.coolDown - _timeDOWN));
                    } else {
                        skillPlayerUse.lastTimeUseThisSkill = _timeNOW;//gan time hien tai
                        if (skillPlayerUse.template.id == (byte) 1 || skillPlayerUse.template.id == (byte) 3 || skillPlayerUse.template.id == (byte) 5 || skillPlayerUse.template.id == (byte) 11) {
                            p.timeCanSkill = _timeNOW + (long) (500);
                        } else {
                            p.timeCanSkill = _timeNOW + (long) (skillPlayerUse.coolDown);
                        }
                    }
                    //CHECK KAIOKEN DE TRU 10% HP
                    if (skillPlayerUse.template.id == 9 && p.hp > (int) (p.getHpFull() * 0.1)) {
                        p.hp -= (int) (p.getHpFull() * 0.1);
                        postUpdateHP(p);
                    }
                    p.mp -= kiUse;
                    int fantashi = Util.nextInt(0, 100);
                    boolean fatal = p.getCritFull() >= fantashi;
                    //CHECK NEU BI TROI THI DANH AUTO CRIT
                    if (_bTarget.isCharFreez || (p.noiTai.id != 0 && p.noiTai.idSkill == (byte) (-4) && p.hp <= (int) (p.getHpFull() * Util.getPercentDouble((int) (p.noiTai.param))))) { //CHECK NOI TAI crit 
                        fatal = true;
                    }
                    if (fatal || p.PEMCRIT > 0) {
                        damage = damage * 2;
                        p.PEMCRIT = 0;
                        fatal = true;
                    }
                    //CHECK GIAP XEN
                    if (_bTarget.useGiapXen) {
                        damage = (int) (damage / 2);
                    }
                    if (p.chimFollow == (byte) 1 && p.dameChim > 0) {
                        damageChim = damage;
                    }
                    //CHECK KHIEN NANG LUONG CUA DOI THU
                    if (_bTarget.isProtect) {
                        damage = 1;
                    }
                    // GIAM DAME SAU KHI BI THOI MIEN
                    if (p.downDAME > 0) {
                        damage -= (int) (damage * Util.getPercentDouble((int) (p.downDAME)));
                        if (p.chimFollow == (byte) 1 && p.dameChim > 0) {
                            damageChim -= (int) (damageChim * Util.getPercentDouble((int) (p.downDAME)));
                        }
                    }
                    //CHECK DAME % TANG LEN SAU KHI DUNG SKILL VA CO NOI TAIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
                    if (p.upDameAfterNoiTai && p.noiTai.id != 0) {//% DAME DON KE TIEP
                        p.upDameAfterNoiTai = false;
                        damage += (int) (damage * Util.getPercentDouble((int) p.noiTai.param));
                        if (p.chimFollow == (byte) 1 && p.dameChim > 0) {
                            damageChim += (int) (damageChim * Util.getPercentDouble((int) p.noiTai.param));
                        }
                    } else if (skillPlayerUse.template.id == p.noiTai.idSkill) { //%DAM CHIEU DAM VA CHUONG
                        damage += (int) (damage * Util.getPercentDouble((int) p.noiTai.param));
                        if (p.chimFollow == (byte) 1 && p.dameChim > 0) {
                            damageChim += (int) (damageChim * Util.getPercentDouble((int) p.noiTai.param));
                        }
                    } else if (p.upDameAfterKhi) { //HOA KHI TANG DAME
                        damage += (int) (damage * Util.getPercentDouble((int) p.noiTai.param));
                        if (p.chimFollow == (byte) 1 && p.dameChim > 0) {
                            damageChim += (int) (damageChim * Util.getPercentDouble((int) p.noiTai.param));
                        }
                    }
                    //CHECK SET KICH HOAT SONGOKU
                    if (p.getSetKichHoatFull() == (byte) 3 && skillPlayerUse.template.id == (byte) 1) {
                        damage = damage * 2;
                    } else if (p.getSetKichHoatFull() == (byte) 7 && skillPlayerUse.template.id == (byte) 4) { //SET KICH HOAT KAKAROT
                        damage = damage * 2;
                    } else if (p.getSetKichHoatFull() == (byte) 5 && skillPlayerUse.template.id == (byte) 17) { //SET KICH HOAT OC TIEU
                        damage = damage * 2;
                    }
                    //END CHECK SET KICH HOAT SONGOKU
                    //CHECK CAI TRANG X3,X4 CHUONG MOI PHUT
                    if ((System.currentTimeMillis() >= p.timeX3X4) && p.isChuongX3X4) {
                        p.isChuongX3X4 = false;
                    }
                    if (p.ItemBody[5] != null && (p.ItemBody[5].template.id == 710 || p.ItemBody[5].template.id == 711) && !p.isChuongX3X4 && (System.currentTimeMillis() >= p.timeX3X4)) {
                        int perX3X4 = Util.nextInt(0, 10);
                        if (perX3X4 <= 1) {
                            p.isChuongX3X4 = true;
                            p.timeX3X4 = System.currentTimeMillis() + 60000;
                            if (p.ItemBody[5].template.id == 710) {
                                damage = damage * 3;
                            } else if (p.ItemBody[5].template.id == 711) {
                                damage = damage * 4;
                            }
                        }
                    }
                    //CHECK TANG TIME GIAP LUYEN TAP
                    Service.gI().upTimeGLT(p);
                    // dame laze
                    if (p.idSkillselect == 11) {
                        damage = (int) (p.mp * Util.getPercentDouble((int) skillPlayerUse.damage));
                        //CHECK SET KICH HOAT LAZE
                        if (p.getSetKichHoatFull() == (byte) 4) {
                            damage = (int) (damage * 1.5);
                        }
                        p.mp = 0;
                        //                drawSkillVip(p, mob, skillPlayerUse.skillId, damage);
                    }
                    _bTarget.hp -= damage;
                    if (_bTarget.hp <= 0) {
                        _bTarget.isdie = true;
                        _bTarget.hp = 0;
                    } else {
                        //PHAN SAT THUONG
                        int damePST = _bTarget.getPercentPST();
                        if (damePST > 0) {
                            damePST = (int) (damage * Util.getPercentDouble(damePST));
                            p.hp = (p.hp - damePST) < 1 ? 1 : (p.hp - damePST);
                            //send dame
                            dameChar(p.id, p.hp, damePST, false);
                        }
                    }
                    if (_bTarget.isdie) {
                        dameChar(_bTarget.id, _bTarget.hp, damage, fatal);
                        //SEND TASK HOI SINH DE TU NEU DANH CHET
                        Player suPhu = PlayerManger.gI().getPlayerByDetuID(_bTarget.id);
                        Timer hoiSinhDetu = new Timer();
                        TimerTask hsDetu = new TimerTask() {
                            public void run() {
                                if (_bTarget.isdie) {
                                    suPhu.timerHSDe = null;
                                    hoiSinhDetu.cancel();
//                                    Player suPhu = PlayerManger.gI().getPlayerByDetuID(_bTarget.id);
                                    _bTarget.x = suPhu.x;
                                    _bTarget.y = suPhu.y;
                                    Service.gI().petLiveFromDead(suPhu);
                                    if (suPhu.statusPet == (byte) 1 || suPhu.statusPet == (byte) 2) {
                                        PetAttack(suPhu, _bTarget, suPhu.statusPet);
                                    }
                                } else {
                                    hoiSinhDetu.cancel();
                                }
                            }
                        ;
                        };
                        hoiSinhDetu.schedule(hsDetu, 60000);
                        suPhu.timerHSDe = hoiSinhDetu;
                        for (Player _pll : players) {
                            if (_pll.id == _bTarget.id) {
                                if (_bTarget.isMonkey) {
                                    Service.gI().loadCaiTrangTemp(_bTarget);
                                    _bTarget.isMonkey = false;
                                    //NOI TAI TANG DAME KHI HOA KHI
                                    if (_bTarget.upDameAfterKhi && _bTarget.noiTai.id != 0 && _bTarget.noiTai.idSkill == (byte) 13) {
                                        _bTarget.upDameAfterKhi = false;
                                    }
                                    //NOI TAI TANG DAME KHI HOA KHI
                                    Service.gI().loadPoint(_bTarget.session, _bTarget);
                                }
                                sendDieToMe(_bTarget);
                            } else {
                                _pll.sendDefaultTransformToPlayer(_bTarget);
                                sendDieToAnotherPlayer(_pll, _bTarget);
                            }
                        }
                    } else {
                        dameChar(_bTarget.id, _bTarget.hp, damage, fatal);
                        //dame hut mau
                        sendHutHP(p, damage, false);
                        //dame hut mau
                        //dame hut ki
                        sendHutKI(p, damage);
                        //dame hut ki
                    }

                    //CHIM ATTACK DETU *****************************************************************************************************
                    if (p.chimFollow == (byte) 1 && !_bTarget.isdie && p.dameChim > 0) {
                        //DAME CHIM
                        damageChim = (int) (damageChim * Util.getPercentDouble(p.dameChim));
                        if (p.getSetKichHoatFull() == (byte) 6) {
                            damageChim = (int) (damageChim * 2);
                        }
                        damageChim = damageChim > 1 ? damageChim : 1;
                        _bTarget.hp -= damageChim;
                        if (_bTarget.hp <= 0) {
                            _bTarget.isdie = true;
                            _bTarget.hp = 0;
                        }
                        if (_bTarget.isdie) {
                            chimDameChar(p, _bTarget, damageChim);
                            //SEND TASK HOI SINH DE TU NEU DANH CHET
                            Player suPhu = PlayerManger.gI().getPlayerByDetuID(_bTarget.id);
                            Timer hoiSinhDetu = new Timer();
                            TimerTask hsDetu = new TimerTask() {
                                public void run() {
                                    if (_bTarget.isdie) {
                                        suPhu.timerHSDe = null;
                                        hoiSinhDetu.cancel();
//                                        Player suPhu = PlayerManger.gI().getPlayerByDetuID(_bTarget.id);
                                        _bTarget.x = suPhu.x;
                                        _bTarget.y = suPhu.y;
                                        Service.gI().petLiveFromDead(suPhu);
                                        if (suPhu.statusPet == (byte) 1 || suPhu.statusPet == (byte) 2) {
                                            PetAttack(suPhu, _bTarget, suPhu.statusPet);
                                        }
                                    } else {
                                        hoiSinhDetu.cancel();
                                    }
                                }
                            ;
                            };
                            hoiSinhDetu.schedule(hsDetu, 60000);
                            suPhu.timerHSDe = hoiSinhDetu;
                            for (Player _pll : players) {
                                if (_pll.id == _bTarget.id) {
                                    if (_bTarget.isMonkey) {
                                        Service.gI().loadCaiTrangTemp(_bTarget);
                                        _bTarget.isMonkey = false;
                                        //NOI TAI TANG DAME KHI HOA KHI
                                        if (_bTarget.upDameAfterKhi && _bTarget.noiTai.id != 0 && _bTarget.noiTai.idSkill == (byte) 13) {
                                            _bTarget.upDameAfterKhi = false;
                                        }
                                        //NOI TAI TANG DAME KHI HOA KHI
                                        Service.gI().loadPoint(_bTarget.session, _bTarget);
                                    }
                                    sendDieToMe(_bTarget);
                                } else {
                                    _pll.sendDefaultTransformToPlayer(_bTarget);
                                    sendDieToAnotherPlayer(_pll, _bTarget);
                                }
                            }
                        } else {
                            chimDameChar(p, _bTarget, damageChim);
                        }
                    }
                    // *****************************************************************************************************

                    //send player attack to all player in map
                    short _idSKILL = skillPlayerUse.skillId;
                    if (p.isMonkey && (p.idSkillselect == 4)) {
                        _idSKILL = 105;
                    } else if (p.isMonkey && (p.idSkillselect == 5)) {
                        _idSKILL = 106;
                    }
                    attachedChar(p.id, _bTarget.id, _idSKILL);
                }
            } else {
                attachedChar(p.id, _bTarget.id, skillPlayerUse.skillId);
            }
        }
    }

    //hinh anh char attack char
    public void attachedChar(int idChar1, int idChar2, short idSkill) {
        Message m = null;
        try {
            m = new Message(-60);
            m.writer().writeInt(idChar1); //id char dam
            m.writer().writeByte((byte) idSkill); //id skill
            m.writer().writeByte(1); //so luong char bi attack
            m.writer().writeInt(idChar2); //id char bi attack
            m.writer().writeByte(0); //b34
//            for(Player _p: players) {
//                if(_p.id != idChar1) {
//                    _p.session.sendMessage(m);
//                }
//            }
            for (byte i = 0; i < players.size(); i++) {
                if (players.get(i) != null && players.get(i).id != idChar1) {
                    players.get(i).session.sendMessage(m);
                }
            }
            m.writer().flush();
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //tru mau char khi bi danh
    public void dameChar(int idChar1, int hpChar, int damage, boolean isCrit) {
        Message m = null;
        try {
            m = new Message(56);
            m.writer().writeInt(idChar1); //id char bi dam
            m.writer().writeInt(hpChar); //hp con lai
            m.writer().writeInt(damage); //dame
            m.writer().writeBoolean(isCrit); //crit ?
            m.writer().writeByte(-1); // effect bi dam //mac dinh la -1
//            for(Player _p: players) {
////                if(_p.id != idChar1) {
//                    _p.session.sendMessage(m);
////                }
//            }
            for (byte i = 0; i < players.size(); i++) {
                if (players.get(i) != null) {
                    players.get(i).session.sendMessage(m);
                }
            }
            m.writer().flush();
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // xoa boss khi boss chet
    public void leaveBoss(Boss _boss) {
        if (bossMap.contains(_boss)) {
            bossMap.remove(_boss);
            try {
                Message m = new Message(-6);
                m.writer().writeInt(_boss.id);
//                for(Player p: players) {
//                    p.session.sendMessage(m);
//                }
                for (byte i = 0; i < players.size(); i++) {
                    if (players.get(i) != null) {
                        players.get(i).session.sendMessage(m);
                    }
                }
                m.writer().flush();
                m.cleanup();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void exitMap(Player _player, Map _map) {
        Message msg;
        try {
            for (int i = 0; i < _map.getPlayers().size(); i++) {
                Player player = _map.getPlayers().get(i);
                if (_player != player) {
                    msg = new Message(-6);
                    msg.writer().writeInt(_player.id);
                    player.session.sendMessage(msg);
                    msg.cleanup();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playerMove(Player _player) {
        Map map = _player.map;
        Message msg;
        try {
            for (byte i = 0; i < players.size(); i++) {
                if (players.get(i) != null && players.get(i).id != _player.id) {
                    msg = new Message(-7);
                    msg.writer().writeInt(_player.id);
                    msg.writer().writeShort(_player.x);
                    msg.writer().writeShort(_player.y);
                    players.get(i).session.sendMessage(msg);
                    msg.cleanup();
                }
            }
//            for (Player player : players) {
//                if (player != _player) {
//                    msg = new Message(-7);
//                    msg.writer().writeInt(_player.id);
//                    msg.writer().writeShort(_player.x);
//                    msg.writer().writeShort(_player.y);
//                    player.session.sendMessage(msg);
//                    msg.cleanup();
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void detuMove(Detu _detu) {
        Message msg;
        try {
            for (Player player : players) {
                msg = new Message(-7);
                msg.writer().writeInt(_detu.id);
                msg.writer().writeShort(_detu.x);
                msg.writer().writeShort(_detu.y);
                player.session.sendMessage(msg);
                msg.cleanup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chat(Player player, String _text) {
        Message msg;
        try {
            for (Player pl : players) {
                msg = new Message(44);
                msg.writer().writeInt(player.id);
                msg.writer().writeUTF(_text);
                pl.session.sendMessage(msg);
                msg.cleanup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chatTG(Session _session, String _text) {
        Player player = PlayerManger.gI().getPlayerByUserID(_session.userId);
        Map map = player.map;
        Message msg;
        try {
            for (Player pl : players) {
                msg = new Message(92);
                msg.writer().writeInt(player.id);
                msg.writer().writeUTF(_text);
                pl.session.sendMessage(msg);
                msg.cleanup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void MagicTree(Session _session, byte _text) {
//        Player player = PlayerManger.gI().getPlayerByUserID(_session.userId);
//        Map map = player.map;
        Message msg;
        try {
            for (Player pl : players) {
                msg = new Message(-34);
                msg.writer().writeByte(_text);
                pl.session.sendMessage(msg);
                msg.cleanup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void selectUIZone(Player p, Message m) throws IOException {
        byte zoneid = m.reader().readByte();
        if (Server.gI().supportNV && (p.map.id - 1) == Server.gI().mapTDST && zoneid == (byte) (Server.gI().khuTDST) && p.taskId != (short) 22) {
            Service.chatNPC(p, (short) 24, "Không thể thực hiện");
            return;
        }
        if (zoneid >= (byte) 0 && zoneid <= (byte) 14) {
            if (map.area[zoneid].players.size() < 15) {
                //        if (zoneid == id) {
                //            p.sendAddchatYellow("Bạn đã ở khu vực: " + zoneid);
                //            return;
                //        }
                //NEU DEO NGOC RONG SAO DEN THI ROT RA DAT
                if (p.imgNRSD == (byte) 37) {
                    p.imgNRSD = (byte) 0;
                    p.timeWINNRSD = 0;
                    resetBagClan(p);
                    ItemMap itemM = createNewItemMap((p.map.id + 287), -1, p.x, p.y);
                    if (itemM != null) {
                        addItemToMap(itemM, -1, p.x, p.y);
                    }
                }
                //leave suphu va de tu khoi map
                if (p.petfucus == 1) {
                    leaveDetu(p, p.detu);
                }
                if (p.pet2Follow == 1 && p.pet != null) {
                    p.zone.leavePETTT(p.pet);
                }
                leave(p);
                map.area[zoneid].Enter(p);
                m = new Message(21);
                p.session.sendMessage(m);
                m.writer().flush();
                m.cleanup();
            } else {
                Service.chatNPC(p, (short) 24, "Khu vực đã đầy");
            }
        }
    }

    public void openUIZone(Player p) throws IOException {
        if (p == null) {
            return;
        }
        final Message m = new Message(29);
        m.writer().writeByte(this.map.area.length);
        for (byte id = 0; id < this.map.area.length; id++) {
            m.writer().writeByte(id);
            m.writer().writeByte(0);
            m.writer().writeByte(this.map.area[id].getNumplayers());
            m.writer().writeByte(this.map.template.maxplayers);
            m.writer().writeByte(0);
        }
        m.writer().flush();
        p.session.sendMessage(m);
        m.cleanup();
    }

    public byte getNumplayers() {
        return (byte) this.players.size();
    }

    public void LiveFromDead(Player p) {
        Message m = null;
        try {
            m = new Message(-16);
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

    public void GET_PLAYER_MENU(Player p, byte bit) throws IOException {
        Message m = new Message(63);
        m.writer().writeByte(bit);
        p.session.sendMessage(m);
        m.cleanup();
    }

    public void refreshMob(int mobid) {
        try {
//            synchronized (this) {
            int rdSuperMob = Util.nextInt(0, 5); //random sieu quai
            Mob mob = getMob(mobid);
            mob.hp = mob.maxHp = mob.template.maxHp;
            if ((mob.template.tempId >= 13 && mob.template.tempId <= 15) || (mob.template.tempId >= 19 && mob.template.tempId <= 27) || (mob.template.tempId >= 31 && mob.template.tempId <= 69)) {
                if (map.id >= 156 && map.id <= 159) {
                    if (mob.template.tempId == 39) {
                        mob.maxHp = 7250000;
                        mob.hp = 7250000;
                    } else if (mob.template.tempId == 40) {
                        mob.maxHp = 7000000;
                        mob.hp = 7000000;
                    } else if (mob.template.tempId == 43) {
                        mob.maxHp = 10150000;
                        mob.hp = 10150000;
                    } else if (mob.template.tempId == 49) {
                        mob.maxHp = 5750000;
                        mob.hp = 5750000;
                    } else if (mob.template.tempId == 50) {
                        mob.maxHp = 9100000;
                        mob.hp = 9100000;
                    } else if (mob.template.tempId == 66) {
                        mob.maxHp = 11700000;
                        mob.hp = 11700000;
                    } else if (mob.template.tempId == 67) {
                        mob.maxHp = 13050000;
                        mob.hp = 13050000;
                    } else if (mob.template.tempId == 68) {
                        mob.maxHp = 12000000;
                        mob.hp = 12000000;
                    } else if (mob.template.tempId == 69) {
                        mob.maxHp = 13800000;
                        mob.hp = 13800000;
                    }
                }
                if (rdSuperMob < 2 && !isHaveSuperMob) {
                    mob.maxHp = mob.maxHp * 10;
                    mob.hp = mob.maxHp;
                    mob.isboss = true;
                    isHaveSuperMob = true;
                } else {
//                        isHaveSuperMob = false;
                    mob.isboss = false;
                }
            }
            mob.status = 5;
            mob.isDie = false;
            mob.timeRefresh = 0;
            Message m = new Message(-13);
            m.writer().writeByte(mob.tempId);
            m.writer().writeByte((byte) Util.nextInt(0, 3));
            if (rdSuperMob < 2 && mob.isboss) {
                m.writer().writeByte(1);
            } else {
                m.writer().writeByte(0);
            }
            m.writer().writeInt(mob.hp);
            m.writer().flush();
            sendMessage(m);
            m.cleanup();
            //Laplich tu dong danh sau khi hoi sinh
            if (!mob.isboss) {
                scheduleMobAuto(mob);
            }
            //Laplich tu dong danh sau khi hoi sinh
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if ((map.id < 53 || map.id > 62) && map.id != 147 && map.id != 148 && map.id != 149 && map.id != 151 && map.id != 152) {
            synchronized (this) {
                try {
                    for (int i = mobs.size() - 1; i >= 0; i--) {
                        Mob mob = mobs.get(i);
                        if (mob.timeRefresh > 0 && System.currentTimeMillis() >= mob.timeRefresh && mob.isRefresh && mob.template.tempId != 70) {
                            refreshMob(mob.tempId);
                        }
                    }
                } catch (Exception var14) {
                    var14.printStackTrace();
                }

            }
        }
    }

    // lap lich mob auto tan cong
    public void updateMobAuto() {
        int i;
        Mob mob;
        if (mobs.size() > 0) {
            for (i = 0; i < this.mobs.size(); i++) {
                mob = this.mobs.get(i);
                if (mob != null && !mob.isboss && mob.template.tempId != 70) {
                    scheduleMobAuto(mob);
                }
            }
        }
    }

    public void scheduleMobAuto(Mob mob) {
        Timer timerMobAuto = new Timer();
        TimerTask ttMobAuto = new TimerTask() {
            public void run() {
                if (mob.isDie) {
                    timerMobAuto.cancel();
                    if (mob.template.tempId == 70) {
                        Timer hiruregarn = new Timer();
                        TimerTask tthiruregarn = new TimerTask() {
                            public void run() {
                                if (mob.typeHiru < (byte) 2) {
                                    mob.typeHiru += (byte) 1;
                                    refreshMob(mob.tempId);
                                    Message m = null;
                                    try {
                                        m = new Message(101);
                                        if (mob.typeHiru == (byte) 1) {
                                            m.writer().writeByte((byte) 6);
                                            m.writer().writeShort(mob.pointX);
                                            m.writer().writeShort(mob.pointY);
                                        } else if (mob.typeHiru == (byte) 2) {
                                            m.writer().writeByte((byte) 5);
                                        } else if (mob.typeHiru == (byte) 3) {
                                            m.writer().writeByte((byte) 9);
                                        }
                                        m.writer().flush();
                                        for (Player p : players) {
                                            if (p.session != null && p != null) {
                                                p.session.sendMessage(m);
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        if (m != null) {
                                            m.cleanup();
                                        }
                                    }
                                } else {
                                    mob.typeHiru = (byte) 0;
                                }
                                hiruregarn.cancel();
                            }
                        ;
                        };
                            hiruregarn.schedule(tthiruregarn, 10000);
//                        } else {
//                            Message m = null;
//                            try {
//                                m = new Message(-73);
//                                m.writer().writeByte((byte)mob.template.tempId);
//                                m.writer().writeByte((byte)0);
//                                m.writer().flush();
//                                for(Player p: players) {
//                                    if(p.session != null && p != null) {
//                                        p.session.sendMessage(m);
//                                    }
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            } finally {
//                                if (m != null) {
//                                    m.cleanup();
//                                }
//                            }
//                            mob.typeHiru = (byte)0;
//                            Util.log("END HIRU: " + mob.typeHiru);
//                        }
                    }
//                    timerMobAuto.cancel();
                    return;
                }
                if (mob.template.tempId == 70) {
                    initBigBoss(mob);
                } else {
                    Player _charTarget = null;
                    if (mob.pTarget != null && charInMapCanAttack(mob.pTarget.id, mob.pointX, mob.pointY, 200)) {
                        _charTarget = mob.pTarget;
                    } else {
                        _charTarget = getCharNearest(mob.pointX, mob.pointY, 75); //get char gan nhat
                    }
                    if (_charTarget != null) {
                        if (mob.template.tempId != 0 && (!mob.isFreez) && (!mob.isBlind) && (!mob.isDCTT) && (!mob.isSleep)) {
                            loadMobAttached(mob.tempId, _charTarget.id, 0); //mob dame player
                        }
                    }
                }
            }
        ;
        };
        timerMobAuto.schedule(ttMobAuto, 0, 1500);
    }

    public void loadMobAttached(int mobid, int playerid, int kiUse) {
        try {
            Mob mob = this.getMob(mobid);
            if (mob != null) {
                long tFight = System.currentTimeMillis() + 1200L;
                if (mob.isboss) {
                    tFight = System.currentTimeMillis() + 500L;
                }
                mob.timeFight = tFight;
//                        int damage = (int)(mob.template.Level * mob.template.Level / 5);
                int damage = (int) (mob.maxHp * 0.01);
                if (mob.template.tempId == 70) {
                    damage = damage / 40;
                }
                if (mob.isSocola != 0) {
                    damage = (int) (damage * (100 - mob.isSocola) / 100);
                }
                Player player;
                short i;
                int indexBuNhin;
                if (playerid != -1) {
                    player = this.getPlayerByID(playerid);
                    if (player != null) {
                        //CHECK SET NE NEU CO
                        int noMiss = Util.nextInt(1, 11);
                        if (noMiss > player.getPercentNedon()) {
                            //CHECK KHIEN NANG LUONG
                            if (player.isProtect) {
                                damage = 1;
                            }
                            //giap tru dame
                            damage = (int) (damage - player.getDefFull()) > 1 ? (int) (damage - player.getDefFull()) : 1;
                            //CHECK BUA DA TRAU
                            if (player.getBuaDaTrau()) {
                                damage = (int) (damage * 0.5) > 1 ? (int) (damage * 0.5) : 1;
                            }
                            // DAME SIEU QUAI VA CHECK BUA OAI HUNG
                            if (mob.isboss && !player.getBuaOaiHung()) {
                                damage = (int) (player.getHpFull() * 0.1);
                            }
                            //CHECK GIAP XEN
                            if (player.useGiapXen) {
                                damage = (int) (damage / 2);
                            }
                        } else {
                            damage = 0;
                        }
                        //END DAME SIEU QUAI
                        player.hp -= damage;
                        player.mp -= kiUse;

                        //CHECK BUA BAT TU
                        if ((player.hp < 1 && player.getBuaBatTu())) {
                            player.hp = 1;
                        }
                        player.isdie = (player.hp > 0) ? false : true;

                        if (!player.isdie) {
                            //PHAN SAT THUONG
                            int damePST = player.getPercentPST();
                            if (damePST > 0) {
                                damePST = (int) (damage * Util.getPercentDouble(damePST));
                                mob.hp = (mob.hp - damePST) < 1 ? 1 : (mob.hp - damePST);
                                //send dame
                                attachedMob(damePST, mob.tempId, false); //player dame mob
                            }
                        }
                        //NEU DEO NGOC RONG SAO DEN THI ROT RA DAT
                        if (player.isdie) {
                            Service.gI().dropDragonBall(player);
                        }

                        //CHECK NEU DANG CON DE TRUNG THI REMOVE DE TRUNG
                        if (player.isdie && player.chimFollow == (byte) 1 && player.dameChim > 0) {
                            useDeTrung(player, (byte) 7);
                            player.chimFollow = (byte) 0;
                            player.dameChim = 0;
                            player.timerDeTrung.cancel();
                            player.timerDeTrung = null;
                        }
                        boolean isplayermonkey = player.isMonkey;
                        if (player.isdie && player.isMonkey) {
                            Service.gI().loadCaiTrangTemp(player);
                            player.isMonkey = false;
                            //NOI TAI TANG DAME KHI HOA KHI
                            if (player.upDameAfterKhi && player.noiTai.id != 0 && player.noiTai.idSkill == (byte) 13) {
                                player.upDameAfterKhi = false;
                            }
                            //NOI TAI TANG DAME KHI HOA KHI
                            Service.gI().loadPoint(player.session, player);
                        }
                        for (Player p : players) {
                            if (p.id == playerid) {
                                this.MobAtkMeMessage(mob.tempId, p, damage, kiUse, (short) -1, (byte) -1, (byte) -1);
                            } else {
                                if (player.isdie && isplayermonkey) {
                                    p.sendDefaultTransformToPlayer(player);

                                }
                                this.MobAtkAnotherPlayerMessage(mob.tempId, p, player, damage, 0, (short) -1, (byte) -1, (byte) -1);
                            }
                        }
                        isplayermonkey = false;
                    }
                } else {
                    for (i = 0; i < this.players.size(); i++) {
                        if (this.players.get(i) != null) {
                            player = this.players.get(i);
                            if (!player.isdie) {
                                short dx = 80;
                                short dy = 2;
                                if (Math.abs(player.x - mob.pointX) < dx && Math.abs(player.y - mob.pointY) < dy) {
                                    this.MobAtkMessage(mob.tempId, player, damage, damage, (short) -1, (byte) -1, (byte) -1);
                                }

                            }
                        } else {
                            continue;
                        }
                    }
                }
            }
        } catch (Exception var18) {
            var18.printStackTrace();
            return;
        }
    }

    private void MobAtkMessage(int mobid, Player n, int dame, int mpdown, short idskill_atk, byte typeatk, byte typetool) {
        Message m = null;
        try {
            // send mob attack me
            m = new Message(-11);
            m.writer().writeByte(mobid);
            m.writer().writeInt(dame);
            m.writer().writeInt(mpdown);
//            m.writer().writeShort(idskill_atk);
//            m.writer().writeByte(typeatk);
//            m.writer().writeByte(typetool);
            m.writer().flush();
            sendMessage(m);
            m.cleanup();
            // send mob attack another player

            m = new Message(-10);
            m.writer().writeByte(mobid);
            m.writer().writeInt(n.id);
            m.writer().writeInt(n.hp);
            m.writer().writeInt(mpdown);
            m.writer().writeInt(1);
//            m.writer().writeShort(idskill_atk);
//            m.writer().writeByte(typeatk);
//            m.writer().writeByte(typetool);
            m.writer().flush();
            this.sendMyMessage(n, m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }

    }

    // mod attack me message
    private void MobAtkMeMessage(int mobid, Player n, int dame, int mpdown, short idskill_atk, byte typeatk, byte typetool) {
        Message m = null;
        try {
            // send mob attack me
            m = new Message(-11);
            m.writer().writeByte(mobid);
            m.writer().writeInt(dame);
            m.writer().writeInt(mpdown);
//            m.writer().writeShort(idskill_atk);
//            m.writer().writeByte(typeatk);
//            m.writer().writeByte(typetool);
            m.writer().flush();
            n.session.sendMessage(m);
//            sendMessageToMe(m, n);
            m.cleanup();

            if (n.isdie) {
                this.sendDieToMe(n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }

    }

    private void MobAtkAnotherPlayerMessage(int mobid, Player n, Player player, int dame, int mpdown, short idskill_atk, byte typeatk, byte typetool) {
        Message m = null;
        try {
            // send mob attack another player
            m = new Message(-10);
            m.writer().writeByte(mobid);
            m.writer().writeInt(player.id);
            m.writer().writeInt(player.hp);
            m.writer().writeInt(mpdown);
//            m.writer().writeInt(1);
//            m.writer().writeShort(idskill_atk);
//            m.writer().writeByte(typeatk);
//            m.writer().writeByte(typetool);
            m.writer().flush();
            n.session.sendMessage(m);
            m.cleanup();

            if (player.isdie) {
                this.sendDieToAnotherPlayer(n, player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }

    }

    // send player die
//    public void sendDie(Player p)  {
//        Message m = null;
//        try {
//            m = new Message(-11);
//            m.writer().writeByte(c.get().pk);
//            m.writer().writeShort(c.get().x);
//            m.writer().writeShort(c.get().y);
//            m.writer().writeLong(c.get().exp);
//            m.writer().flush();
//            c.p.conn.sendMessage(m);
//            m.cleanup();
//
//            m = new Message(0);
//            m.writer().writeInt(c.get().id);
//            m.writer().writeByte(c.get().pk);
//            m.writer().writeShort(c.get().x);
//            m.writer().writeShort(c.get().y);
//            m.writer().flush();
//            this.sendMyMessage(c.p, m);
//            m.cleanup();
//            m = new Message(-9);
//            m.writer().writeInt(c.get().id);
//            m.writer().writeByte(c.get().pk);
//            m.writer().writeShort(c.get().x);
//            m.writer().writeShort(c.get().y);
//            m.writer().flush();
//            this.sendMyMessage(c.p, m);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if(m != null) {
//                m.cleanup();
//            }
//        }
//    }
    // send me die
    public void sendDieToMe(Player p) {
        Message m = null;
        try {
            m = new Message(-17);
            m.writer().writeByte(p.cPk);
            m.writer().writeShort(p.x);
            m.writer().writeShort(p.y);
            m.writer().writeLong(p.power);
            m.writer().flush();
            sendMessageToMe(m, p);
            m.cleanup();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    // send me die
    public void sendDieToAnotherPlayer(Player psend, Player p) {
        Message m = null;
        try {
            m = new Message(-8);
            m.writer().writeInt(p.id);
            m.writer().writeByte(p.cPk);
            m.writer().writeShort(p.x);
            m.writer().writeShort(p.y);
            m.writer().flush();
            psend.session.sendMessage(m);
            m.cleanup();

//            m = new Message(-15);
//            m.writer().writeInt(p.id);
//            m.writer().writeByte(p.cPk);
//            m.writer().writeShort(p.x);
//            m.writer().writeShort(p.y);
//            m.writer().flush();
//            psend.session.sendMessage(m);
//            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    // return me die
    public void dieReturn(Player p) {
        if (p != null && p.isdie) {
            if (p.zone != null && p.session != null) {
                DieReturn(p);
            }
//            else if(p.c.tdbTileMap != null) {
//                p.c.tdbTileMap.DieReturn(p);
//            }
        }
    }

    // return die
    public void DieReturn(Player p) {
        Message m = null;
        try {
//            if (p.c.isInDun && p.c.dunId != -1) {
//                p.restCave();
//            }
            //leave suphu va de tu khoi map
            if (p.petfucus == 1) {
                leaveDetu(p, p.detu);
            }
            if (p.pet2Follow == 1 && p.pet != null) {
                p.zone.leavePETTT(p.pet);
            }
            leave(p);

            p.isdie = false;

            Map ma;
            int mapid = 21;
            if (p.gender == 1) {
                mapid = 22;
            }
            if (p.gender == 2) {
                mapid = 23;
            }
            goMapTransport(p, mapid);
//            ma = Manager.getMapid(mapid);
////            }
//
//            for (byte j = 0; j < ma.template.wayPoints.length; j++) {
//                WayPoint vg = ma.template.wayPoints[j];
//                if (vg.goMap == mapid) {
//                    p.x = (short) (vg.goX);
//                    p.y = (short) (vg.goY);
//                }
//            }
//            byte errornext = -1;
//            if (errornext == -1) {
//                for (byte j = 0; j < ma.area.length; j++) {
//                    if (ma.area[j].players.size() < ma.template.maxplayers) {
//                        //hinh anh tau bay don
//                        try {
//                            m = new Message(-65);
//                            m.writer().writeInt(p.id);
//                            m.writer().writeByte(1); //0 khong co gi //1 tau binh thuong 2// dich chuyen tuc thoi // 3 tau vutru
//                            m.writer().flush();
//                            for(Player pl: players) {
//                                pl.session.sendMessage(m);
//                            }
//                            m.cleanup();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        
//                        //nhan vat bay tu tren troi xuong
//                        // map clear
//                        try {
//                            m = new Message(-22);
//                            m.writer().flush();
//                            p.session.sendMessage(m);
//                            m.cleanup();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        // send title set -82
//                        Service.gI().tileSet(p.session, mapid);
//                        // map info -24                        
//                        ma.area[j].EnterCapsule(p);
//
            p.hp = p.getHpFull();
            p.mp = p.getMpFull();
            m = new Message(-30);
            m.writer().writeByte(4);
            m.writer().writeLong(p.vang);
            m.writer().writeInt(p.ngoc);
            m.writer().writeInt(p.getHpFull());
            m.writer().writeInt(p.getMpFull());
            m.writer().writeInt(p.ngocKhoa);
            m.writer().flush();
            sendMessageToMe(m, p);
            m.cleanup();

            m = new Message(-16);
            m.writer().flush();
            p.session.sendMessage(m);
            m.cleanup();
//
            return;
//                    }
//                    if (j == ma.area.length - 1) {
//                        errornext = 0;
//                    }
//                }
//            }
//
//            Enter(p);
//            switch (errornext) {
//                case 0:
//                    p.sendAddchatYellow("Bản đồ quá tải.");
//                    return;
//                case 1:
//                    p.sendAddchatYellow("Trang bị thú cưới đã hết hạn. Vui lòng tháo ra để di chuển");
//                    return;
//                case 2:
//                    p.sendAddchatYellow("Cửa " + ma.template.name + " vẫn chưa mở");
//                    return;
//            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    // hoi sinh tai cho
    public static void wakeUpDieReturn(Player p) {
        if (p != null && p.session != null & p.isdie && p.zone != null) {
//            TileMap tileMap = p.c.tileMap;
//            if (!tileMap.map.LangCo() && tileMap.map.dun == null) {
            if (p.ngocKhoa < 1 && p.ngoc < 1) {
                p.sendAddchatYellow("Bạn không đủ ngọc để hồi sinh!");
            } else {
                if (p.ngocKhoa > 0) {
                    p.ngocKhoa -= 1;
                } else {
                    p.ngoc -= 1;
                }
//                    p.isdie = false;
//                    p.hp = p.getHpFull();
//                    p.mp = p.getMpFull();

                p.updateVangNgocMessage();
                p.liveFromDead();
                //HOI SINH O MAP BU THI VE MAP 1
                if (p.map.id >= 114 && p.map.id <= 120) {
                    Map maptele = MainManager.getMapid(114);
                    Controller.getInstance().teleportToMAP(p, maptele);
                }
            }
//            } else {
//                p.conn.sendMessageLog("Bạn không thể hồi sinh tại đây!");
//            }
        }
    }

    // tinh toan vang ngoc, type 0:vang, 1:ngoc, 2:ngockhoa
//    public synchronized int calenderVangNgoc(long x, int type, Player p) {
    public int calenderVangNgoc(long x, int type, Player p) {
        long newData = (long) p.vang + x;
        if (type == 1) {
            newData = (long) p.ngoc + x;
        }
        if (type == 2) {
            newData = (long) p.ngocKhoa + x;
        }

        if (newData > 2000000000L) {
            x = 2000000000;
        } else if (newData < 0L) {
            x = 0;
        }
        return (int) x;
    }

    // namek hoi sinh
    public void hoiSinhNamek(int idhoisinh, Player player) {
        Skill skill = player.getSkillById(7);
//        SkillData skilldata = new SkillData();
//        Skill templateSkillUse = skilldata.getSkillBySkillTemplate(player.gender, skill.skillId, skill.point);
        Skill templateSkillUse = player.getSkillByIDTemplate((short) 7);
        Message m = null;
        Player suPhu = null;
        try {
            int _manaUse = (int) (player.getMpFull() * Util.getPercentDouble(templateSkillUse.manaUse));
            long _TIMENOW = System.currentTimeMillis();
            if (player.mp >= _manaUse && ((_TIMENOW - templateSkillUse.lastTimeUseThisSkill) > (long) templateSkillUse.coolDown)) {
                //CHECK NOI TAI HOI SINH CO GIAM THOI GIAN HOI CHIEU
                if (player.noiTai.id != 0 && templateSkillUse.template.id == player.noiTai.idSkill) {
                    long _timeDOWN = (long) (Util.getPercentDouble((int) (player.noiTai.param)) * templateSkillUse.coolDown);
                    templateSkillUse.lastTimeUseThisSkill = _TIMENOW - _timeDOWN;
                    //SEND TIME DOWN NOI TAI
                    Service.gI().sendCoolDownSkill(player, templateSkillUse.skillId, (int) ((long) templateSkillUse.coolDown - _timeDOWN));
                } else {
                    templateSkillUse.lastTimeUseThisSkill = _TIMENOW;
                }
                if (idhoisinh > 0) {
                    Player _p = this.getPlayerByID(idhoisinh);
                    if (_p.cPk != 8 && _p.typePk == 0 && skill.point > 0) {
                        for (Player p : players) {
                            if (p.id != player.id) {
                                attachedChar(player.id, idhoisinh, (short) 55);
                            }
                            if (Math.abs(player.x - p.x) <= 500) {
                                if (p.hp > 0 && !p.isdie) {
                                    if (p.id == player.id) {
                                        p.hp = (p.hp + (p.getHpFull() * 7 / 10)) > p.getHpFull() ? p.getHpFull() : (p.hp + (p.getHpFull() * 7 / 10));
                                    } else {
                                        p.hp = (p.hp + (p.getHpFull() * (skill.point + 3) / 10)) > p.getHpFull() ? p.getHpFull() : (p.hp + (p.getHpFull() * (skill.point + 3) / 10));
                                        p.mp = (p.mp + (p.getMpFull() * (skill.point + 3) / 10)) > p.getMpFull() ? p.getMpFull() : (p.mp + (p.getMpFull() * (skill.point + 3) / 10));
                                    }
                                    m = new Message(-30);
                                    m.writer().writeByte(4);
                                    m.writer().writeLong(p.vang);
                                    m.writer().writeInt(p.ngoc);
                                    m.writer().writeInt(p.hp);
                                    m.writer().writeInt(p.mp);
                                    m.writer().writeInt(p.ngocKhoa);
                                    m.writer().flush();
                                    p.session.sendMessage(m);
                                    m.cleanup();
                                } else {
                                    p.liveFromDead();
                                }
                                //CHAT CAM ON
                                chat(p, "Cảm ơn " + player.name + " đã trị thương mình!");
                            }
                        }

                        for (Detu pet : pets) {
                            if (Math.abs(player.x - pet.x) <= 500) {
                                if (pet.hp > 0 && !pet.isdie) {

                                    pet.hp = (pet.hp + (pet.getHpFull() * (skill.point + 3) / 10)) > pet.getHpFull() ? pet.getHpFull() : (pet.hp + (pet.getHpFull() * (skill.point + 3) / 10));
                                    pet.mp = (pet.mp + (pet.getMpFull() * (skill.point + 3) / 10)) > pet.getMpFull() ? pet.getMpFull() : (pet.mp + (pet.getMpFull() * (skill.point + 3) / 10));
                                } else {
                                    suPhu = PlayerManger.gI().getPlayerByDetuID(pet.id);
                                    if (suPhu.timerHSDe != null) {
                                        suPhu.timerHSDe.cancel();
                                        suPhu.timerHSDe = null;
                                    }
                                    pet.petLiveFromDead(player);
                                    if (suPhu.statusPet == (byte) 1 || suPhu.statusPet == (byte) 2) {
                                        suPhu.zone.PetAttack(suPhu, pet, suPhu.statusPet);
                                    }
                                }
                                //CHAT CAM ON
                                chat(pet, "Cảm ơn " + player.name + " đã trị thương mình!");
                            }
                        }
                    }
                } else if (idhoisinh <= -100000 && idhoisinh > -200000) {
                    Detu _p = this.getDetuByID(idhoisinh);
                    if (_p.cPk != 8 && _p.typePk == 0 && skill.point > 0) {
                        for (Player p : players) {
                            if (p.id != player.id) {
                                attachedChar(player.id, idhoisinh, (short) 55);
                            }
                            if (Math.abs(player.x - p.x) <= 500) {
                                if (p.hp > 0 && !p.isdie) {
                                    if (p.id == player.id) {
                                        p.hp = (p.hp + (p.getHpFull() * 7 / 10)) > p.getHpFull() ? p.getHpFull() : (p.hp + (p.getHpFull() * 7 / 10));
                                    } else {
                                        p.hp = (p.hp + (p.getHpFull() * (skill.point + 3) / 10)) > p.getHpFull() ? p.getHpFull() : (p.hp + (p.getHpFull() * (skill.point + 3) / 10));
                                        p.mp = (p.mp + (p.getMpFull() * (skill.point + 3) / 10)) > p.getMpFull() ? p.getMpFull() : (p.mp + (p.getMpFull() * (skill.point + 3) / 10));
                                    }
                                    m = new Message(-30);
                                    m.writer().writeByte(4);
                                    m.writer().writeLong(p.vang);
                                    m.writer().writeInt(p.ngoc);
                                    m.writer().writeInt(p.hp);
                                    m.writer().writeInt(p.mp);
                                    m.writer().writeInt(p.ngocKhoa);
                                    m.writer().flush();
                                    p.session.sendMessage(m);
                                    m.cleanup();
                                } else {
                                    p.liveFromDead();
                                }
                                //CHAT CAM ON
                                chat(p, "Cảm ơn " + player.name + " đã trị thương mình!");
                            }
                        }

                        for (Detu pet : pets) {
                            if (Math.abs(player.x - pet.x) <= 500) {
                                if (pet.hp > 0 && !pet.isdie) {

                                    pet.hp = (pet.hp + (pet.getHpFull() * (skill.point + 3) / 10)) > pet.getHpFull() ? pet.getHpFull() : (pet.hp + (pet.getHpFull() * (skill.point + 3) / 10));
                                    pet.mp = (pet.mp + (pet.getMpFull() * (skill.point + 3) / 10)) > pet.getMpFull() ? pet.getMpFull() : (pet.mp + (pet.getMpFull() * (skill.point + 3) / 10));
                                } else {
                                    suPhu = PlayerManger.gI().getPlayerByDetuID(pet.id);
                                    if (suPhu.timerHSDe != null) {
                                        suPhu.timerHSDe.cancel();
                                        suPhu.timerHSDe = null;
                                    }
                                    pet.petLiveFromDead(player);
                                    if (suPhu.statusPet == (byte) 1 || suPhu.statusPet == (byte) 2) {
                                        suPhu.zone.PetAttack(suPhu, pet, suPhu.statusPet);
                                    }
                                }
                                //CHAT CAM ON
                                chat(pet, "Cảm ơn " + player.name + " đã trị thương mình!");
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //gui de lieu ve skill dang dung cho player
    public void setSkillPaint(short idpaint, Player player, int type) {
        Message m = null;
        try {
            for (Player p : players) {
                if (p.id != player.id) {
                    m = new Message(-45);
                    m.writer().writeByte((byte) type);
                    m.writer().writeInt(player.id);
                    m.writer().writeShort(idpaint);
                    m.writer().flush();
                    p.session.sendMessage(m);
                    m.cleanup();
                }
            }
            if (type == 3 && !player.isPet && !player.isBOSS) {
                m = new Message(-45);
                m.writer().writeByte((byte) type);
                m.writer().writeInt(player.id);
                m.writer().writeShort(idpaint);
                m.writer().flush();
                player.session.sendMessage(m);
                m.cleanup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    //skill boom cua xayda
    public void useBoomXayda(Player player) {
//        Skill skillUse = player.getSkillById(14);
//        SkillData skilldata = new SkillData();
//        Skill templateSkillUse = skilldata.getSkillBySkillTemplate(player.gender, skillUse.skillId, skillUse.point);
        Skill templateSkillUse = player.getSkillByIDTemplate((short) 14);
        long _TIMENOW = System.currentTimeMillis();
        if (player.mp >= (int) (player.getMpFull() * 0.5) && ((_TIMENOW - templateSkillUse.lastTimeUseThisSkill) > (long) templateSkillUse.coolDown)) {
            //CHECK NOI TAI TU SAT CO GIAM THOI GIAN HOI CHIEU
            if (player.noiTai.id != 0 && templateSkillUse.template.id == player.noiTai.idSkill) {
                long _timeDOWN = (long) (Util.getPercentDouble((int) (player.noiTai.param)) * templateSkillUse.coolDown);
                templateSkillUse.lastTimeUseThisSkill = _TIMENOW - _timeDOWN;
                //SEND TIME DOWN NOI TAI
                Service.gI().sendCoolDownSkill(player, templateSkillUse.skillId, (int) ((long) templateSkillUse.coolDown - _timeDOWN));
            } else {
                templateSkillUse.lastTimeUseThisSkill = _TIMENOW;
            }
            Message m = null;
            try {
                m = new Message(-45);
                m.writer().writeByte(7);
                m.writer().writeInt(player.id); // id player use    
                m.writer().writeShort(templateSkillUse.skillId); // b91 gui cho co
                m.writer().writeShort(3000); //    seconds
                m.writer().flush();
                for (Player p : players) {
                    if (p.id != player.id) {
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

            DelayUseBomTask boomTask = new DelayUseBomTask(players, mobs, itemsMap, player, templateSkillUse, bossMap, pets);
            Timer timerBoom = new Timer();
            timerBoom.schedule(boomTask, 3000);
        }
    }

    public void useMonkey(Player player) {
        Skill templateSkillUse = player.getSkillByIDTemplate((short) 13);
        long _TIMENOW = System.currentTimeMillis();
        int _manaUse = (int) (player.getMpFull() / 10);
        if (player.mp >= _manaUse && ((_TIMENOW - templateSkillUse.lastTimeUseThisSkill) > (long) templateSkillUse.coolDown)) {
            player.mp -= _manaUse;
            templateSkillUse.lastTimeUseThisSkill = _TIMENOW;
            player.isMonkey = true;
            //NOI TAI TANG DAME KHI HOA KHI
            if (!player.upDameAfterKhi && player.noiTai.id != 0 && player.noiTai.idSkill == (byte) 13) {
                player.upDameAfterKhi = true;
            }
            //NOI TAI TANG DAME KHI HOA KHI
            player.updateBodyMonkey();
//                            Service.gI().loadPoint(_session, player);
        }
    }

    //use qua cau kenh khi
    public void useKenhKhi(Player player) {
//        Skill skillUse = player.getSkillById(10);
//        SkillData skilldata = new SkillData();
//        Skill templateSkillUse = skilldata.getSkillBySkillTemplate(player.gender, skillUse.skillId, skillUse.point);
        Skill templateSkillUse = player.getSkillByIDTemplate((short) 10);
        int _manaUse = (int) (Util.getPercentDouble(templateSkillUse.manaUse) * player.getMpFull());
        long _TIMENOW = System.currentTimeMillis();
        if (player.mp >= _manaUse && !player.checkPlayerBiKhongChe() && ((_TIMENOW - templateSkillUse.lastTimeUseThisSkill) > (long) templateSkillUse.coolDown)) {
            //CHECK NOI TAI KENH KHI CO GIAM THOI GIAN HOI CHIEU
            if (player.noiTai.id != 0 && templateSkillUse.template.id == player.noiTai.idSkill) {
                long _timeDOWN = (long) (Util.getPercentDouble((int) (player.noiTai.param)) * templateSkillUse.coolDown);
                templateSkillUse.lastTimeUseThisSkill = _TIMENOW - _timeDOWN;
                //SEND TIME DOWN NOI TAI
                Service.gI().sendCoolDownSkill(player, templateSkillUse.skillId, (int) ((long) templateSkillUse.coolDown - _timeDOWN));
            } else {
                templateSkillUse.lastTimeUseThisSkill = _TIMENOW;
            }
            player.mp -= _manaUse;
            Message m = null;

            int dameKenhKhi = (int) (player.getDamFull() * Util.getPercentDouble((int) templateSkillUse.damage));
            //CHECK SET KICH HOAT KENH KHI
            if (player.getSetKichHoatFull() == (byte) 2) {
                dameKenhKhi = dameKenhKhi * 2;
            }
            int dameBoom = dameKenhKhi;

            try {
                m = new Message(-45);
                m.writer().writeByte(4);
                m.writer().writeInt(player.id); // id player use  
                m.writer().writeShort(templateSkillUse.skillId);
                m.writer().writeShort(4000); //    seconds
                m.writer().flush();
                for (Player p : players) {
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

            Timer timerKenhKhi = new Timer();
            TimerTask _useKenhKhi = new TimerTask() {
                public void run() {
                    Message m = null;
                    for (Mob mob : mobs) {
                        if (Math.abs(player.x - mob.pointX) < templateSkillUse.dx && Math.abs(player.y - mob.pointY) < templateSkillUse.dy && !mob.isDie) {

                            mob.updateHP(-dameBoom);

                            if (mob.isDie) {
                                ArrayList<ItemMap> itemDrop = new ArrayList<>();
                                //CHECK INIT BOSS MAP KHI GAS
                                Service.gI().initLychee(player);
                                if (mob.template.tempId != 0) {
                                    int idItemNotSell[] = {17, 188, 189, 190, 18, 19, 20, 441, 442, 443, 444, 445, 446, 447, 17, 188, 189, 190, 225, 17, 188, 189, 190, 18, 19, 20, 76, 188, 189, 190, 18, 19, 20};
                                    int percentDrop = Util.nextInt(0, 10);
                                    if (percentDrop < 3) {
                                        int id = Util.nextInt(0, 33);
                                        //                    Item itemMap = ItemSell.getItem(id);
                                        Item itemMap = ItemSell.getItemNotSell(idItemNotSell[id]);
                                        ItemMap item = new ItemMap();
                                        item.playerId = player.id;
                                        item.x = mob.pointX;
                                        item.y = mob.pointY;
//                                            item.itemMapID = idItemNotSell[id];
//                                            item.itemTemplateID = (short) item.itemMapID;
                                        item.itemMapID = itemsMap.size();
                                        item.itemTemplateID = (short) idItemNotSell[id];
                                        itemMap.template = ItemTemplate.ItemTemplateID(idItemNotSell[id]);
                                        item.item = itemMap;
                                        itemDrop.add(item);
                                        itemsMap.addAll(itemDrop);

                                        try {
                                            m = new Message(-12);
                                            m.writer().writeByte(mob.tempId);
                                            m.writer().writeInt(mob.hp);
                                            m.writer().writeBoolean(false);
                                            //                                m.writer().writeByte(itemDrop.size());
                                            m.writer().writeByte(1);
                                            m.writer().writeShort(item.itemMapID);
                                            m.writer().writeShort(item.item.template.id);
                                            m.writer().writeShort(mob.pointX);
                                            m.writer().writeShort(mob.pointY);
                                            m.writer().writeInt(player.id);
                                            m.writer().flush();
                                            for (Player pll : players) {
                                                pll.session.sendMessage(m);
                                            }
                                            m.cleanup();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {

                                        //                                        int id = Util.nextInt(0,10);
                                        //
                                        //                                        Item itemMap = ItemSell.getItem(id);
                                        //                                        ItemMap item = new ItemMap();
                                        //                                        item.playerId = player.id;
                                        //                                        item.x = mob.pointX;
                                        //                                        item.y = mob.pointY;
                                        //                                        item.itemMapID = id;
                                        //                                        item.itemTemplateID = (short) item.itemMapID;
                                        //                                        itemMap.template = ItemTemplate.ItemTemplateID(id);
                                        //                                        item.item = itemMap;
                                        //                                        itemDrop.add(item);
                                        //                                        itemsMap.addAll(itemDrop);
                                        //đồng bộ mob chết
                                        try {
                                            m = new Message(-12);
                                            m.writer().writeByte(mob.tempId);
                                            m.writer().writeInt(mob.hp);
                                            m.writer().writeBoolean(false);
                                            m.writer().writeByte(0); //so luong item
                                            m.writer().flush();
                                            for (Player pll : players) {
                                                pll.session.sendMessage(m);
                                            }
                                            m.cleanup();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    //check TRUNG MABU
                                    if (mob.template.tempId == 70 && mob.typeHiru == (byte) 2) {
                                        int rdMabu = Util.nextInt(0, 5);
                                        if (rdMabu < 1) {
                                            player.hasTrungMabu = true;
                                            player.sendAddchatYellow("Bạn vừa nhận được đệ tử Mabư, quay về nhà gặp Ông Già để thao tác");
                                        }
                                    }
                                } else {
                                    try {
                                        m = new Message(-12);
                                        m.writer().writeByte(mob.tempId);
                                        m.writer().writeInt(mob.hp);
                                        m.writer().writeBoolean(false);
                                        m.writer().writeByte(0); //so luong item
                                        m.writer().flush();
                                        for (Player pll : players) {
                                            pll.session.sendMessage(m);
                                        }
                                        m.cleanup();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                try {
                                    m = new Message(-9);
                                    m.writer().writeByte(mob.tempId);
                                    m.writer().writeInt(mob.hp);
                                    m.writer().writeInt(dameBoom);
                                    m.writer().writeBoolean(false);//flag
                                    //eff boss
                                    //5 khói
                                    m.writer().writeByte(-1);
                                    m.writer().flush();
                                    for (Player pll : players) {
                                        pll.session.sendMessage(m);
                                    }
                                    m.cleanup();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }

                    if (bossMap.size() > 0) {
                        for (int i = 0; i < bossMap.size(); i++) {
                            if (Math.abs(player.x - bossMap.get(i).x) < templateSkillUse.dx && Math.abs(player.y - bossMap.get(i).y) < templateSkillUse.dy && !bossMap.get(i).isdie && Service.gI().checkCanAttackBoss(bossMap.get(i))) {

                                bossMap.get(i).hp -= dameBoom;
                                if (bossMap.get(i).hp <= 0) {
                                    bossMap.get(i).isdie = true;
                                    bossMap.get(i).isTTNL = false;
                                    bossMap.get(i).hp = 0;
                                }
                                if (bossMap.get(i).isdie && bossMap.get(i).typePk == 5) {
                                    bossMap.get(i).typePk = 1;
                                    //send dame
                                    dameChar(bossMap.get(i).id, bossMap.get(i).hp, dameBoom, false);
                                    //REMOVE ALL KHONG CHE KHI BOSS CHET
                                    bossMap.get(i).removePlayerKhongChe();

                                    if (bossMap.get(i)._typeBoss != 1 && bossMap.get(i)._typeBoss != 2) { //Broly khong roi do
                                        ArrayList<ItemMap> itemDrops = new ArrayList<>();

                                        if (bossMap.get(i)._typeBoss == 3 || bossMap.get(i)._typeBoss == 5 || (bossMap.get(i)._typeBoss >= (byte) 7 && bossMap.get(i)._typeBoss <= (byte) 30 && bossMap.get(i)._typeBoss != (byte) 29)) {
                                            if (bossMap.get(i)._typeBoss == (byte) 7) {
                                                Server.gI().mapKUKU = 0;
                                                Server.gI().khuKUKU = 0;
                                            } else if (bossMap.get(i)._typeBoss == (byte) 8) {
                                                Server.gI().mapMDD = 0;
                                                Server.gI().khuMDD = 0;
                                            } else if (bossMap.get(i)._typeBoss == (byte) 9) {
                                                Server.gI().mapRAMBO = 0;
                                                Server.gI().khuRAMBO = 0;
                                            } else if (bossMap.get(i)._typeBoss == (byte) 14) {
                                                Server.gI().mapTDST = 0;
                                                Server.gI().khuTDST = 0;
                                            }
                                            Service.gI().sendThongBaoServer(player.name + " vừa tiêu diệt " + bossMap.get(i).name + " mọi người đều ngưỡng mộ");
                                            int perCapHong = Util.nextInt(0, 10);
                                            if (perCapHong < 1) {
                                                ItemSell CapHong = ItemSell.getItemSell(722, (byte) 1);
                                                ItemMap itemROI = new ItemMap();
                                                itemROI.playerId = player.id;
                                                itemROI.x = bossMap.get(i).x;
                                                itemROI.y = bossMap.get(i).y;
//                                                itemROI.itemMapID = 722;
//                                                itemROI.itemTemplateID = (short)itemROI.itemMapID;
                                                itemROI.itemMapID = itemsMap.size();
                                                itemROI.itemTemplateID = (short) 722;
                                                //                                itemGOD.item.template = ItemTemplate.ItemTemplateID(_ITEMMAPID);
                                                //BUILD NEW ITEM + CHI SO CHO DO KICH HOAT
                                                Item _ITEMCapHong = new Item(CapHong.item);
                                                itemROI.item = _ITEMCapHong;
                                                itemDrops.add(itemROI);
                                                itemsMap.addAll(itemDrops);

                                                //đồng bộ boss chet, cho boss bien mat. add item map //68
                                                try {
                                                    m = new Message(68);
                                                    m.writer().writeShort(itemROI.itemMapID);
                                                    m.writer().writeShort(itemROI.item.template.id);
                                                    m.writer().writeShort(bossMap.get(i).x);
                                                    m.writer().writeShort(bossMap.get(i).y);
                                                    m.writer().writeInt(player.id);
                                                    m.writer().flush();
                                                    for (Player _pl : players) {
                                                        _pl.session.sendMessage(m);
                                                    }
                                                    m.cleanup();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        } else if (bossMap.get(i)._typeBoss == 4 || bossMap.get(i)._typeBoss == 6) {
                                            Service.gI().sendThongBaoServer(player.name + " vừa tiêu diệt " + bossMap.get(i).name + " mọi người đều ngưỡng mộ");
                                            ItemMap item = dropItemGOD(player, bossMap.get(i).x, bossMap.get(i).y);

                                            //ADD ITEM TO MAP
                                            if (item != null) {
                                                try {
                                                    itemDrops.add(item);
                                                    itemsMap.addAll(itemDrops);
                                                    m = new Message(68);
                                                    m.writer().writeShort(item.itemMapID);
                                                    m.writer().writeShort(item.item.template.id);
                                                    m.writer().writeShort(bossMap.get(i).x);
                                                    m.writer().writeShort(bossMap.get(i).y);
                                                    m.writer().writeInt(player.id);
                                                    m.writer().flush();
                                                    for (Player _pl : players) {
                                                        _pl.session.sendMessage(m);
                                                    }
                                                    m.cleanup();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            if (Util.nextInt(0, 10) < 5) {
                                                ItemMap itemM = newItemMAP(979, player.id, bossMap.get(i).x, bossMap.get(i).y);
                                                if (itemM != null) {
                                                    addItemToMap(itemM, player.id, bossMap.get(i).x, bossMap.get(i).y);
                                                }
                                            }
                                        } else if (bossMap.get(i)._typeBoss >= (byte) 31 && bossMap.get(i)._typeBoss <= (byte) 35) {
                                            int itemDT = 17;
                                            if (Util.nextInt(0, 4) == 0) {
                                                itemDT = 16;
                                            }
                                            ItemMap itemM = newItemMAP(itemDT, player.id, bossMap.get(i).x, bossMap.get(i).y);
                                            if (itemM != null) {
                                                addItemToMap(itemM, player.id, bossMap.get(i).x, bossMap.get(i).y);
                                            }
                                        } else if (bossMap.get(i)._typeBoss >= (byte) 26 && bossMap.get(i)._typeBoss <= (byte) 28) {//cell rot 3s
                                            ItemMap itemM = newItemMAP(16, player.id, bossMap.get(i).x, bossMap.get(i).y);
                                            if (itemM != null) {
                                                addItemToMap(itemM, player.id, bossMap.get(i).x, bossMap.get(i).y);
                                            }
                                        } else if (bossMap.get(i)._typeBoss == (byte) 30) {//sieu bo hung rot 2s
                                            ItemMap itemM = newItemMAP(15, player.id, bossMap.get(i).x, bossMap.get(i).y);
                                            if (itemM != null) {
                                                addItemToMap(itemM, player.id, bossMap.get(i).x, bossMap.get(i).y);
                                            }
                                        } else if (bossMap.get(i)._typeBoss == (byte) 48) {//zamasu rot da bao ve
                                            if (Util.nextInt(0, 10) <= 7) {
                                                ItemMap itemM = newItemMAP(987, player.id, bossMap.get(i).x, bossMap.get(i).y);
                                                if (itemM != null) {
                                                    addItemToMap(itemM, player.id, bossMap.get(i).x, bossMap.get(i).y);
                                                }
                                            }
                                        } else if (bossMap.get(i)._typeBoss == (byte) 49 || bossMap.get(i)._typeBoss == (byte) 50) {//billwhis rot ruong
                                            ItemMap itemM = newItemMAP(1055, player.id, bossMap.get(i).x, bossMap.get(i).y);
                                            if (itemM != null) {
                                                addItemToMap(itemM, player.id, bossMap.get(i).x, bossMap.get(i).y);
                                            }
                                        } else if (bossMap.get(i)._typeBoss == (byte) 53 || bossMap.get(i)._typeBoss == (byte) 54) {//billwhis rot ruong
                                            ItemMap itemM = newItemMAP(77, player.id, bossMap.get(i).x, bossMap.get(i).y);
                                            if (itemM != null) {
                                                addItemToMap(itemM, player.id, bossMap.get(i).x, bossMap.get(i).y);
                                            }
                                        }
//                                         else if(bossMap.get(i)._typeBoss >= (byte)44 || bossMap.get(i)._typeBoss <= (byte)47) {
//                                            ItemMap itemM = cNewItemMap(590, player.id, bossMap.get(i).x, bossMap.get(i).y);
//                                            addItemToMap(itemM, player.id, bossMap.get(i).x, bossMap.get(i).y);
//                                        }
//                                        else {
//                                            int id = Util.nextInt(17,20);
//
//            //                                Item itemMap = ItemSell.getItem(id);
//                                            Item itemMap = ItemSell.getItemNotSell(id);
//                                            ItemMap item = new ItemMap();
//                                            item.playerId = player.id;
//                                            item.x = bossMap.get(i).x;
//                                            item.y = bossMap.get(i).y;
//                                            item.itemMapID = id;
//                                            item.itemTemplateID = (short) item.itemMapID;
//                                            itemMap.template = ItemTemplate.ItemTemplateID(id);
//                                            item.item = itemMap;
//                                            itemDrops.add(item);
//                                            itemsMap.addAll(itemDrops);
//                        //                }
//                                            //đồng bộ boss chet, cho boss bien mat. add item map //68
//                                            try {
//                                                m = new Message(68);
//                                                m.writer().writeShort(item.itemMapID);
//                                                m.writer().writeShort(item.item.template.id);
//                                                m.writer().writeShort(bossMap.get(i).x);
//                                                m.writer().writeShort(bossMap.get(i).y);
//                                                m.writer().writeInt(player.id);
//                                                m.writer().flush();
//                                                for(Player _pl: players) {
//                                                    _pl.session.sendMessage(m);
//                                                }
//                                                m.cleanup();
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
                                    }
                                    if (bossMap.get(i)._typeBoss == 2 && player.havePet == 0) { //boss die la super broly
                                        player.detu = bossMap.get(i).detu;
                                        leaveDEEEEE(bossMap.get(i).detu);
                                        player.havePet = 1;
                                        player.isNewPet = true;
                                        player.detu.id = (-100000 - player.id);
                                        player.statusPet = 0;
                                        player.petfucus = 1;
                                        pets.add(player.detu);
                                        for (Player _plz : players) {
                                            loadInfoDeTu(_plz.session, player.detu);
                                        }
                                    } else {
                                        leaveDEEEEE(bossMap.get(i).detu);
                                    }
                                    //boss chet
                                    if (bossMap.get(i)._typeBoss < (byte) 44 || bossMap.get(i)._typeBoss > 47) {
                                        for (Player _pp : players) {
                                            sendDieToAnotherPlayer(_pp, bossMap.get(i));
                                        }
                                    }
                                    //CHECK NHIEM VU SAN BOSS
                                    if (player.taskId == (short) 21 || player.taskId == (short) 22 || player.taskId == (short) 23 || player.taskId == (short) 25 || player.taskId == (short) 26 || player.taskId == (short) 27
                                            || player.taskId == (short) 28 || player.taskId == (short) 29 || player.taskId == (short) 30 || player.taskId == (byte) 32) {
                                        int idBoss = TaskManager.gI().mobTASK0[player.taskId][player.crrTask.index];
                                        if ((bossMap.get(i)._typeBoss == (byte) (idBoss / 100)) || ((bossMap.get(i)._typeBoss == (byte) 41) && player.taskId == (byte) 32 && player.crrTask.index == (byte) 7)) {
                                            TaskService.gI().updateCountTask(player);
                                        }
                                    } else if (player.taskId == (short) 19 && player.crrTask.index == (byte) 1 && bossMap.get(i)._typeBoss == (byte) 31) {
                                        TaskService.gI().updateCountTask(player);
                                        if (player.clan != null) {
                                            for (byte mk = 0; mk < player.clan.members.size(); mk++) {
                                                Player member = PlayerManger.gI().getPlayerByUserID(player.clan.members.get(mk).id);
                                                if (member != null && member.session != null && member.map.id == 59) {
                                                    if (member.taskId == (short) 19 && member.crrTask.index == (byte) 1) {
                                                        TaskService.gI().updateCountTask(member);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    //END NHIEM VU SAN BOSS
                                    //CHECK NHIEM VU NHAN THOI KHONG
                                    if (player.taskId == (short) 31 && player.crrTask.index == (byte) 0 && bossMap.get(i)._typeBoss == (byte) 6) {
                                        int perNhan = Util.nextInt(0, 5);
                                        if (perNhan == 0) {
                                            Item itemNhan = ItemSell.getItemNotSell(992);
                                            Item _itemNhan = new Item(itemNhan);
                                            if (player.addItemToBag(_itemNhan)) {
                                                player.sendAddchatYellow("Bạn vừa nhận được nhẫn thời không sai lệch");
                                                Service.gI().updateItemBag(player);
                                                TaskService.gI().updateCountTask(player);
                                            }
                                        }
                                    }
                                    //END CHECK NHIEM VU NHAN THOI KHONG
                                    //CHECK SP MABU
                                    if (bossMap.get(i)._typeBoss >= (byte) 36 && bossMap.get(i)._typeBoss <= (byte) 39) {
                                        player.pointMabu = (byte) 10;
                                        Service.gI().setPowerPoint(player, "TL", (short) 10, (short) 10, (short) 10);
                                    }
                                    //END CHECK SP MABU
                                    Boss _bossDIE = bossMap.get(i);
                                    if (_bossDIE._typeBoss >= (byte) 44 && _bossDIE._typeBoss <= 47) {
//                                        leaveBoss(_bossDIE);
                                        leaveBossYardrat(_bossDIE);
                                    } else {
                                        Timer timerBossLeave = new Timer();
                                        TimerTask bossLeave = new TimerTask() {
                                            public void run() {
                                                leaveBoss(_bossDIE); //xoa boss
                                            }
                                        ;
                                        };
                                        timerBossLeave.schedule(bossLeave, 5000);
                                    }
//                                    player.zone.leaveBoss(bossMap.get(i)); //xoa boss
//                                    return;
                                    //                MobStartDie(p, damage, mob, fatal, itemDrop);
                                } else {
                                    player.zone.dameChar(bossMap.get(i).id, bossMap.get(i).hp, dameBoom, false);
                                }
                                //ve dap kenh khi cho cac char khac
                                attachedChar(player.id, bossMap.get(i).id, templateSkillUse.skillId);
                            }
                        }
                    }

                    for (Player _p : players) {
                        if (Service.gI().checkCanAttackChar(player, _p) && !_p.isdie) {
//                        if(((_p.cPk != 0 && _p.cPk != player.cPk && player.cPk != 0) || (_p.cPk == 8 && player.cPk != 0) || (_p.cPk != 0 && player.cPk == 8)) && _p.id != player.id && !_p.isdie) {
                            if (Math.abs(player.x - _p.x) < templateSkillUse.dx && Math.abs(player.y - _p.y) < templateSkillUse.dy) {

                                _p.hp -= dameBoom;
                                if (_p.hp <= 0) {
                                    _p.isdie = true;
                                    _p.isTTNL = false;
                                    _p.hp = 0;
                                }
                                if (_p.isdie) {
                                    //NEU DEO NGOC RONG SAO DEN THI ROT RA DAT
                                    Service.gI().dropDragonBall(_p);

                                    //CHECK NEU DANG CON DE TRUNG THI REMOVE DE TRUNG
                                    if (_p.chimFollow == (byte) 1 && _p.dameChim > 0) {
                                        useDeTrung(_p, (byte) 7);
                                        _p.chimFollow = (byte) 0;
                                        _p.dameChim = 0;
                                        _p.timerDeTrung.cancel();
                                        _p.timerDeTrung = null;
                                    }
                                    for (Player _pll : players) {
                                        if (_pll.id == _p.id) {
                                            if (_p.isMonkey) {
                                                Service.gI().loadCaiTrangTemp(_p);
                                                _p.isMonkey = false;
                                                //NOI TAI TANG DAME KHI HOA KHI
                                                if (_p.upDameAfterKhi && _p.noiTai.id != 0 && _p.noiTai.idSkill == (byte) 13) {
                                                    _p.upDameAfterKhi = false;
                                                }
                                                //NOI TAI TANG DAME KHI HOA KHI
                                                Service.gI().loadPoint(_p.session, _p);
                                            }
                                            sendDieToMe(_p);
                                        } else {
                                            _pll.sendDefaultTransformToPlayer(_p);
                                            sendDieToAnotherPlayer(_pll, _p);
                                        }
                                    }
                                } else {
                                    dameChar(_p.id, _p.hp, dameBoom, false);
                                }
                                //ve dap kenh khi cho cac char khac
                                attachedChar(player.id, _p.id, templateSkillUse.skillId);
                            }
                        }
                    }
                    for (Detu _de : pets) {
                        if (Service.gI().checkCanAttackDeTu(player, _de) && !_de.isdie) {
                            if (Math.abs(player.x - _de.x) < templateSkillUse.dx && Math.abs(player.y - _de.y) < templateSkillUse.dy) {
                                _de.hp -= dameBoom;
                                if (_de.hp <= 0) {
                                    _de.isdie = true;
                                    _de.isTTNL = false;
                                    _de.hp = 0;
                                }
                                if (_de.isdie) {
                                    //SEND TASK HOI SINH DE TU NEU DANH CHET
                                    Timer hoiSinhDetu = new Timer();
                                    TimerTask hsDetu = new TimerTask() {
                                        public void run() {
                                            if (_de.isdie) {
                                                player.timerHSDe = null;
                                                hoiSinhDetu.cancel();
                                                Player suPhu = PlayerManger.gI().getPlayerByDetuID(_de.id);
                                                _de.x = suPhu.x;
                                                _de.y = suPhu.y;
                                                Service.gI().petLiveFromDead(suPhu);
                                                if (suPhu.statusPet == (byte) 1 || suPhu.statusPet == (byte) 2) {
                                                    PetAttack(suPhu, _de, suPhu.statusPet);
                                                }
                                            } else {
                                                hoiSinhDetu.cancel();
                                            }
                                        }
                                    ;
                                    };
                                    hoiSinhDetu.schedule(hsDetu, 60000);
                                    player.timerHSDe = hoiSinhDetu;
                                    for (Player _pll : players) {
                                        _pll.sendDefaultTransformToPlayer(_de);
                                        sendDieToAnotherPlayer(_pll, _de);
                                    }
                                } else {
                                    dameChar(_de.id, _de.hp, dameBoom, false);
                                }
                                //ve dap kenh khi cho cac char khac
                                attachedChar(player.id, _de.id, templateSkillUse.skillId);
                            }
                        }
                    }
                }
            ;
            };
            timerKenhKhi.schedule(_useKenhKhi, 5000);
        }
    }

    public void useLaze(Player player) {
        Skill skillUse = player.getSkillById(11);
        SkillData skilldata = new SkillData();
        Skill templateSkillUse = skilldata.getSkillBySkillTemplate(player.gender, skillUse.skillId, skillUse.point);
        Message m = null;
//        long _TIMENOW = System.currentTimeMillis();
//        if(player.mp > 0 && !player.checkPlayerBiKhongChe() && ((_TIMENOW - templateSkillUse.lastTimeUseThisSkill) > (long)templateSkillUse.coolDown)) {
        if (player.mp > 0 && !player.checkPlayerBiKhongChe()) {
//            templateSkillUse.lastTimeUseThisSkill = _TIMENOW;
//            player.mp = 0;
            //send effecct laze
            try {
                m = new Message(-45);
                m.writer().writeByte(4);
                m.writer().writeInt(player.id); // id player use  
                m.writer().writeShort(templateSkillUse.skillId);
                m.writer().writeShort(4000); //    seconds
                m.writer().flush();
                for (Player p : players) {
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
    }

    public void drawSkillVip(Player player, Mob mob, short skillid, int damage) {
        Message m = null;

        try {
            m = new Message(-95);
            m.writer().writeByte(5);
            m.writer().writeInt(player.id);
            m.writer().writeByte((byte) (skillid));
            m.writer().writeInt(mob.tempId);
            m.writer().writeInt(damage);
            m.writer().writeInt(mob.hp);
            m.writer().flush();
            for (Player p : players) {
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

    public void useHuytSao(Player player) {
        Skill skillUse = player.getSkillById(21);
//        SkillData skilldata = new SkillData();
//        Skill templateSkillUse = skilldata.getSkillBySkillTemplate(player.gender, skillUse.skillId, skillUse.point);
        Skill templateSkillUse = player.getSkillByIDTemplate((short) 21);

        setSkillPaint(templateSkillUse.skillId, player, 8);
        long _TIMENOW = System.currentTimeMillis();
        int _manaUse = (int) (Util.getPercentDouble(templateSkillUse.manaUse - 1) * player.getMpFull());
        if (player.mp >= _manaUse && ((_TIMENOW - templateSkillUse.lastTimeUseThisSkill) >= (long) templateSkillUse.coolDown)) {
            //CHECK NOI TAI HUYT SAO CO GIAM THOI GIAN HOI CHIEU
            if (player.noiTai.id != 0 && templateSkillUse.template.id == player.noiTai.idSkill) {
                long _timeDOWN = (long) (Util.getPercentDouble((int) (player.noiTai.param)) * templateSkillUse.coolDown);
                templateSkillUse.lastTimeUseThisSkill = _TIMENOW - _timeDOWN;
                //SEND TIME DOWN NOI TAI
                Service.gI().sendCoolDownSkill(player, templateSkillUse.skillId, (int) ((long) templateSkillUse.coolDown - _timeDOWN));
            } else {
                templateSkillUse.lastTimeUseThisSkill = _TIMENOW;
            }
            player.mp -= _manaUse;
            player.isHuytSao = true;
            Message m = null;
            // send + hp
            for (Player p : players) {
                if (p.id != player.id) {
                    if (p.buffHuytSao == 0 && !p.isHuytSao) {
                        sendUpHpPlayerByHuytSaoMe(p, skillUse.point);
                        //+hp player khac
                        try {
                            m = new Message(-42);
                            m.writer().writeInt(p.hpGoc);
                            m.writer().writeInt(p.mpGoc);
                            m.writer().writeInt(p.damGoc);
                            m.writer().writeInt((int) Math.ceil(p.getHpFull() * (10 + skillUse.point + 3) / 10));// hp full
                            m.writer().writeInt(p.getMpFull());// mp full
                            m.writer().writeInt(p.hp);// hp
                            m.writer().writeInt(p.mp);// mp
                            m.writer().writeByte(p.getSpeed());// speed
                            m.writer().writeByte(20);
                            m.writer().writeByte(20);
                            m.writer().writeByte(1);
                            m.writer().writeInt(p.getDamFull());// dam full
                            m.writer().writeInt(p.getDefFull());// def full
                            m.writer().writeByte(p.getCritFull());// crit full
                            m.writer().writeLong(p.tiemNang);
                            m.writer().writeShort(100);
                            m.writer().writeShort(p.defGoc);
                            m.writer().writeByte(p.critGoc);
                            m.writer().flush();
                            p.session.sendMessage(m);
                            m.cleanup();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        p.buffHuytSao = (byte) skillUse.point;
                    }
                } else {
                    player.isHuytSao = true;
                    player.PEMCRIT = 1;
                    sendUpHpPlayer(player, skillUse.point);
                    Service.gI().loadPoint(player.session, player);
                }

                // send effect huyt sao
                try {
                    m = new Message(-124);
                    m.writer().writeByte(1);
                    m.writer().writeByte(0);
                    m.writer().writeByte(39);
                    m.writer().writeInt(p.id);
                    m.writer().flush();
                    p.session.sendMessage(m);
                    m.cleanup();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // hien thi 30s icon item huyt sao
                showIconItemUse(p, 3781, 30);

                ResetHuytSaoTask huytsaoTask = new ResetHuytSaoTask(p);
                Timer timer = new Timer();
                timer.schedule(huytsaoTask, 30000);
            }
        }
    }

    public void sendUpHpPlayerByHuytSaoMe(Player player, int point) {
        Message m = null;
        try {
            player.hp = player.getHpFull();
            m = new Message(-30);
            m.writer().writeByte(5);
            m.writer().writeInt(player.hp);
            m.writer().flush();
            player.session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //send + hp player
    public void sendUpHpPlayer(Player player, int point) {
        Message m = null;
        try {
            player.hp = player.getHpFull();
            m = new Message(-30);
            m.writer().writeByte(5);
            m.writer().writeInt(player.hp);
//            m.writer().writeInt(hp);
            m.writer().flush();
            player.session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //hien thi icon item duoi thanh hp
    public void showIconItemUse(Player p, int idIcon, int time) {
        Message m = null;
        try {
            m = new Message(-106);
            m.writer().writeShort((short) idIcon);
            m.writer().writeShort((short) time);
            m.writer().flush();
            p.session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //use khien nang luong
    public void useProtect(Player player) {
//        Skill skillUse = player.getSkillById(19);
//        SkillData skilldata = new SkillData();
        Skill templateSkillUse = null;
//        if(player.isPet) {
//            templateSkillUse = skilldata.getSkillBySkillTemplate(player.gender, skillUse.tempSkillId, skillUse.point);
//        } else {
//            templateSkillUse = skilldata.getSkillBySkillTemplate(player.gender, skillUse.skillId, skillUse.point); 
//        }
        templateSkillUse = player.getSkillByIDTemplate((short) 19);
        int _manaUse = (int) (Util.getPercentDouble(templateSkillUse.manaUse) * player.getMpFull());
        long _TIMENOW = System.currentTimeMillis();
        if (player.mp >= _manaUse && ((_TIMENOW - templateSkillUse.lastTimeUseThisSkill) > (long) templateSkillUse.coolDown)) {
            //CHECK NOI TAI KHIEN NANG LUONG CO GIAM THOI GIAN HOI CHIEU
            if (player.noiTai.id != 0 && templateSkillUse.template.id == player.noiTai.idSkill) {
                long _timeDOWN = (long) (Util.getPercentDouble((int) (player.noiTai.param)) * templateSkillUse.coolDown);
                templateSkillUse.lastTimeUseThisSkill = _TIMENOW - _timeDOWN;
                //SEND TIME DOWN NOI TAI
                Service.gI().sendCoolDownSkill(player, templateSkillUse.skillId, (int) ((long) templateSkillUse.coolDown - _timeDOWN));
            } else {
                templateSkillUse.lastTimeUseThisSkill = _TIMENOW;
            }
            player.mp -= _manaUse;
            player.isProtect = true;
            Message m = null;
            try {
                m = new Message(-124);
                m.writer().writeByte(1);
                m.writer().writeByte(0);
                m.writer().writeByte(33);
                m.writer().writeInt(player.id);
                m.writer().flush();
                for (Player pl : players) {
                    pl.session.sendMessage(m);
                    if (player.isPet) {
                        Timer timerPetProtect = new Timer();
                        TimerTask petprotect = new TimerTask() {
                            public void run() {
                                player.isProtect = false;
                                //remove setting khien, va effect tren nguoi
                                //                            try {
                                //                                m = new Message(-124);
                                //                                m.writer().writeByte(2);
                                //                                m.writer().writeByte(0);
                                //                                m.writer().writeByte(33);
                                //                                m.writer().writeInt(player.id);
                                //                                m.writer().flush();
                                //                                this.player.session.sendMessage(m);
                                //                                m.cleanup();
                                //                            } catch (Exception var2) {
                                //                                var2.printStackTrace();
                                //                            } finally {
                                //                                if(m != null) {
                                //                                    m.cleanup();
                                //                                }
                                //                            }

                                //remove effect khien cua player
                                Message msg = null;
                                try {
                                    msg = new Message(-124);
                                    msg.writer().writeByte(2);
                                    msg.writer().writeByte(0);
                                    msg.writer().writeInt(player.id);
                                    msg.writer().flush();
                                    for (Player pll : players) {
                                        pll.session.sendMessage(msg);
                                    }
                                    msg.cleanup();
                                } catch (Exception var2) {
                                    var2.printStackTrace();
                                } finally {
                                    if (msg != null) {
                                        msg.cleanup();
                                    }
                                }
                            }
                        ;
                        };
                        timerPetProtect.schedule(petprotect, (templateSkillUse.damage * 1000));
                    } else {
                        ResetProtectTask protectTask = new ResetProtectTask(pl, player.id);
                        Timer timerProtect = new Timer();
                        timerProtect.schedule(protectTask, (templateSkillUse.damage * 1000));
                    }
                }
                m.cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // hien thi thoi gian va icon item khien nang luong
            if (!player.isPet) {
                showIconItemUse(player, 3784, templateSkillUse.damage);
            }
        }
    }

    public void useProtectPet(Player player) {
        Skill skillUse = player.getSkillById(19);
        SkillData skilldata = new SkillData();
        Skill templateSkillUse = skilldata.getSkillBySkillTemplate(player.gender, skillUse.tempSkillId, skillUse.point);

        int _manaUse = (int) (Util.getPercentDouble(templateSkillUse.manaUse) * player.getMpFull());
//        long _TIMENOW = System.currentTimeMillis();
//        if(player.mp >= _manaUse && ((_TIMENOW - templateSkillUse.lastTimeUseThisSkill) > (long)templateSkillUse.coolDown)) {
        if (player.mp >= _manaUse) {
            //CHECK NOI TAI KHIEN NANG LUONG CO GIAM THOI GIAN HOI CHIEU
//            if(player.noiTai.id != 0 && templateSkillUse.template.id == player.noiTai.idSkill) {
//                long _timeDOWN = (long)(Util.getPercentDouble((int)(player.noiTai.param))*templateSkillUse.coolDown);
//                templateSkillUse.lastTimeUseThisSkill = _TIMENOW - _timeDOWN;
//                //SEND TIME DOWN NOI TAI
//                Service.gI().sendCoolDownSkill(player, templateSkillUse.skillId, (int)((long)templateSkillUse.coolDown - _timeDOWN));
//            } else {
//                templateSkillUse.lastTimeUseThisSkill = _TIMENOW;
//            }
            player.mp -= _manaUse;
            player.isProtect = true;
            Message m = null;
            try {
                m = new Message(-124);
                m.writer().writeByte(1);
                m.writer().writeByte(0);
                m.writer().writeByte(33);
                m.writer().writeInt(player.id);
                m.writer().flush();
                for (Player pl : players) {
                    pl.session.sendMessage(m);
                    if (player.isPet) {
                        Timer timerPetProtect = new Timer();
                        TimerTask petprotect = new TimerTask() {
                            public void run() {
                                player.isProtect = false;

                                //remove effect khien cua player
                                Message msg = null;
                                try {
                                    msg = new Message(-124);
                                    msg.writer().writeByte(2);
                                    msg.writer().writeByte(0);
                                    msg.writer().writeInt(player.id);
                                    msg.writer().flush();
                                    for (Player pll : players) {
                                        pll.session.sendMessage(msg);
                                    }
                                    msg.cleanup();
                                } catch (Exception var2) {
                                    var2.printStackTrace();
                                } finally {
                                    if (msg != null) {
                                        msg.cleanup();
                                    }
                                }
                            }
                        ;
                        };
                        timerPetProtect.schedule(petprotect, (templateSkillUse.damage * 1000));
                    } else {
                        ResetProtectTask protectTask = new ResetProtectTask(pl, player.id);
                        Timer timerProtect = new Timer();
                        timerProtect.schedule(protectTask, (templateSkillUse.damage * 1000));
                    }
                }
                m.cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // hien thi thoi gian va icon item khien nang luong
            if (!player.isPet) {
                showIconItemUse(player, 3784, templateSkillUse.damage);
            }
        }
    }

    public void goMapTransport(Player p, int mapid) {
        if (mapid != 84 && mapid != 85 && mapid != 86 && mapid != 87 && mapid != 88 && mapid != 89 && mapid != 90 && mapid != 91 && mapid != 102 && mapid != 114 && mapid != 115 && mapid != 116 && mapid != 117
                && mapid != 118 && mapid != 119 && mapid != 120 && mapid != 122 && mapid != 123 && mapid != 124 && mapid != 147 && mapid != 148 && mapid != 149 && mapid != 151
                && mapid != 152 && mapid != 131 && mapid != 132 && mapid != 133) {
            if (p.taskId < (short) 8 && ((mapid >= 45 && mapid <= 50) || mapid == 12 || mapid == 18)) {
                p.sendAddchatYellow("Nhiệm vụ chưa hoàn thành không thể tới đây!");
                Service.gI().buyDone(p);
                return;
            } else if (p.taskId < (short) 9 && !MapSoSinh(mapid)) {
                p.sendAddchatYellow("Nhiệm vụ chưa hoàn thành không thể tới đây!");
                Service.gI().buyDone(p);
                return;
            } else if (p.taskId < (short) 20 && mapid >= 63) {
                p.sendAddchatYellow("Nhiệm vụ chưa hoàn thành không thể tới đây!");
                Service.gI().buyDone(p);
                return;
            } else if (((mapid >= 63 && mapid <= 67) || mapid >= 73) && (p.taskId < (short) 21 || (p.taskId == (short) 21 && p.crrTask.index < (byte) 1))) {
                p.sendAddchatYellow("Nhiệm vụ chưa hoàn thành không thể tới đây!");
                Service.gI().buyDone(p);
                return;
            } else if (mapid >= 73 && (p.taskId < (short) 21 || (p.taskId == (short) 21 && p.crrTask.index < (byte) 2))) {
                p.sendAddchatYellow("Nhiệm vụ chưa hoàn thành không thể tới đây!");
                Service.gI().buyDone(p);
                return;
            } else if (mapid >= 78 && (p.taskId < (short) 21 || (p.taskId == (short) 21 && p.crrTask.index < (byte) 3))) {
                p.sendAddchatYellow("Nhiệm vụ chưa hoàn thành không thể tới đây!");
                Service.gI().buyDone(p);
                return;
            } else if (mapid >= 92 && p.taskId < (short) 23) {
                p.sendAddchatYellow("Nhiệm vụ chưa hoàn thành không thể tới đây!");
                Service.gI().buyDone(p);
                return;
            } else if (p.taskId < (short) 26 && mapid >= 97) {
                p.sendAddchatYellow("Nhiệm vụ chưa hoàn thành không thể tới đây!");
                Service.gI().buyDone(p);
                return;
            } else if (p.taskId < (short) 27 && mapid >= 97 && mapid != 104) {
                p.sendAddchatYellow("Nhiệm vụ chưa hoàn thành không thể tới đây!");
                Service.gI().buyDone(p);
                return;
            } else if (p.taskId < (short) 28 && mapid >= 100 && mapid != 104) {
                p.sendAddchatYellow("Nhiệm vụ chưa hoàn thành không thể tới đây!");
                Service.gI().buyDone(p);
                return;
            } else if (p.taskId < (short) 29 && mapid >= 103 && mapid != 104) {
                p.sendAddchatYellow("Nhiệm vụ chưa hoàn thành không thể tới đây!");
                Service.gI().buyDone(p);
                return;
            }
//            if(mapid >= 63 && p.taskId < (short)20) {
//                p.sendAddchatYellow("Nhiệm vụ chưa hoàn thành không thể tới đây!");
//                Service.gI().buyDone(p);
//                return;
//            } else if(((mapid >= 63 && mapid <= 67) || mapid >= 73) && (p.taskId < (short)21 || (p.taskId == (short)21 && p.crrTask.index < (byte)1))) {
//                p.sendAddchatYellow("Nhiệm vụ chưa hoàn thành không thể tới đây!");
//                Service.gI().buyDone(p);
//                return;
//            } else if(mapid >= 73 && (p.taskId < (short)21 || (p.taskId == (short)21 && p.crrTask.index < (byte)2))) {
//                p.sendAddchatYellow("Nhiệm vụ chưa hoàn thành không thể tới đây!");
//                Service.gI().buyDone(p);
//                return;
//            } else if(mapid >= 78 && (p.taskId < (short)21 || (p.taskId == (short)21 && p.crrTask.index < (byte)3))) {
//                p.sendAddchatYellow("Nhiệm vụ chưa hoàn thành không thể tới đây!");
//                Service.gI().buyDone(p);
//                return;
//            } else if(mapid >= 92 && p.taskId < (short)24) {
//                p.sendAddchatYellow("Nhiệm vụ chưa hoàn thành không thể tới đây!");
//                Service.gI().buyDone(p);
//                return;
//            } else if(mapid >= 105 && p.taskId < (short)29) {
//                p.sendAddchatYellow("Nhiệm vụ chưa hoàn thành không thể tới đây!");
//                Service.gI().buyDone(p);
//                return;
//            }
        }
//        else {
        //NEU DEO NGOC RONG SAO DEN THI ROT RA DAT
        Service.gI().dropDragonBall(p);

        Message m = null;
        Map ma = MainManager.getMapid(mapid);
        short _goX = 0;
        short _goY = 10;
        int _rdLocation = 0;
        //        if(mapid == 23) { _goX = 350; } //nha broly lay cay dau
        //        else if (mapid == 21) { _goX = 230; } //nha gohan
        //        else if (mapid == 22) { _goX = 630; } //nha mori
        //        else 
        if (mapid == 21 || mapid == 22 || mapid == 23 || mapid == 24 || mapid == 25 || mapid == 26
                || mapid == 45 || mapid == 47 || mapid == 50 || mapid == 114 || mapid == 154 || mapid == 155) { // || mapid == 84
            if (ma.template.npcs.length > 1) {
                _rdLocation = Util.nextInt(0, (ma.template.npcs.length - 1)); //get index npc random
            }
            _goX = (short) (ma.template.npcs[_rdLocation].cx);
        } else if (mapid == 68) { //THUNG LUNG NAPPA
            _goX = 153;
            _goY = 408;
        } else if (mapid == 19) { //THANH PHO VEGETA
            _goX = 1111;
            _goY = 360;
        } else if (mapid == 79) { //NUI KHI DO
            _goX = 114;
            _goY = 360;
        } else if (mapid == 105) { //CANH DONG TUYET
            _goX = 124;
            _goY = 408;
        } else if (mapid == 48) { //HANH TINH KAIO
            _goX = 371;
            _goY = 240;
        } else if (mapid == 84) { //SIEU THI
            _goX = 699;
            _goY = 336;
        } else if (mapid == 131) { //HANH TINH YARDAT
            _goX = 1227;
            _goY = 456;
        } else if (mapid == 86) { //SAO DEN 2SAO
            _goX = 567;
            _goY = 336;
        } else if (mapid == 87) { //SAO DEN 3SAO
            _goX = 147;
            _goY = 384;
        } else if (mapid == 88) { //SAO DEN 4SAO
            _goX = 284;
            _goY = 336;
        } else if (mapid == 89) { //SAO DEN 5SAO
            _goX = 135;
            _goY = 288;
        } else if (mapid == 90) { //SAO DEN 6SAO
            _goX = 141;
            _goY = 360;
        } else if (mapid == 91) { //SAO DEN 7SAO
            _goX = 1352;
            _goY = 168;
        } else {
            _rdLocation = Util.nextInt(0, (ma.template.arMobid.length - 1)); //get index mob random
            _goX = ma.template.arrMobx[_rdLocation];
            _goY = ma.template.arrMoby[_rdLocation];
        }
        p.x = _goX;
        p.y = _goY;
        byte errornext = -1;
        if (errornext == -1) {
            for (byte j = 0; j < ma.area.length; j++) {
                if (ma.area[j].players.size() < ma.template.maxplayers) {
                    //                    p.map.id = mapid;
                    //HOI HP VA KI NEU CO CHIEN THUYEN TENNIS
                    if (p.isTennis) {
                        p.hp = p.getHpFull();
                        p.mp = p.getMpFull();
                        Service.gI().loadPoint(p.session, p);
                    }
                    //hinh anh tau bay don
                    try {
                        m = new Message(-65);
                        m.writer().writeInt(p.id);
                        if (p.isTennis) {
                            m.writer().writeByte((byte) 3);
                        } else {
                            m.writer().writeByte(1); //0 khong co gi //1 tau binh thuong 2// dich chuyen tuc thoi // 3 tau vutru
                        }
                        m.writer().flush();
                        for (Player pl : players) {
                            pl.session.sendMessage(m);
                        }
                        m.cleanup();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //leave suphu va de tu khoi map
                    if (p.petfucus == 1) {
                        leaveDetu(p, p.detu);
                    }
                    if (p.pet2Follow == 1 && p.pet != null) {
                        p.zone.leavePETTT(p.pet);
                    }
                    leave(p);

                    //tru so luong capsule thuong
                    if (p.indexItemUse != -1 && p.ItemBag[p.indexItemUse].id == 193 && p.ItemBag[p.indexItemUse].quantity > 0) {;
                        //                        p.ItemBag[p.indexItemUse].quantity-=1;
                        p.updateQuantityItemBag(p.indexItemUse, 1);
                        Service.gI().updateItemBag(p);
                    }
                    //nhan vat bay tu tren troi xuong
                    // map clear
                    try {
                        m = new Message(-22);
                        m.writer().flush();
                        p.session.sendMessage(m);
                        m.cleanup();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // send title set -82
//                        Service.gI().tileSet(p.session, mapid);
                    // map info -24
                    ma.area[j].EnterCapsule(p);
                    //                    Service.gI().mapInfo(p.session, p);
                    // -30 info char
                    //                    Service.gI().updateVangNgoc(p);
                    //-42 load point 
                    //                    Service.gI().loadPoint(p.session, p);
                    //                    Service.gI().loadPlayer(p.session, p);
                    return;
                }
                if (j == ma.area.length - 1) {
                    errornext = 0;
                    ma.area[Util.nextInt(0, ma.area.length)].EnterCapsule(p);
                    return;
                }
            }
        }

        EnterCapsule(p);
        switch (errornext) {
            case 0:
                p.sendAddchatYellow("Bản đồ quá tải.");
                return;
            case 1:
                p.sendAddchatYellow("Trang bị thú cưới đã hết hạn. Vui lòng tháo ra để di chuển");
                return;
            case 2:
                p.sendAddchatYellow("Cửa " + ma.template.name + " vẫn chưa mở");
                return;
        }
//        }
    }

    //tdhs td
    public void useTDHS(Player player) {
//        Skill skillUse = player.getSkillById(6);
//        SkillData skilldata = new SkillData();
        Skill templateSkillUse = null;
//        if(player.isPet) {
//            templateSkillUse = skilldata.getSkillBySkillTemplate((byte)0, skillUse.tempSkillId, skillUse.point);
//        } else {
//            templateSkillUse = skilldata.getSkillBySkillTemplate(player.gender, skillUse.skillId, skillUse.point);
//        }
        templateSkillUse = player.getSkillByIDTemplate(player.idSkillselect);
        long _TIMENOW = System.currentTimeMillis();
        int _manaUse = (int) (Util.getPercentDouble(templateSkillUse.manaUse) * player.getMpFull());
        if (player.mp >= _manaUse && !player.checkPlayerBiKhongChe() && ((_TIMENOW - templateSkillUse.lastTimeUseThisSkill) > (long) templateSkillUse.coolDown)) {
            //CHECK NOI TAI TDHS CO GIAM THOI GIAN HOI CHIEU
            if (player.noiTai.id != 0 && templateSkillUse.template.id == player.noiTai.idSkill) {
                long _timeDOWN = (long) (Util.getPercentDouble((int) (player.noiTai.param)) * templateSkillUse.coolDown);
                templateSkillUse.lastTimeUseThisSkill = _TIMENOW - _timeDOWN;
                //SEND TIME DOWN NOI TAI
                Service.gI().sendCoolDownSkill(player, templateSkillUse.skillId, (int) ((long) templateSkillUse.coolDown - _timeDOWN));
            } else {
                templateSkillUse.lastTimeUseThisSkill = _TIMENOW;
            }
            player.mp -= _manaUse;
            Message m = null;
            byte countMob = 0;
            byte countChar = 0;
            int[] indexSize = new int[mobs.size()];
            int[] arrIdChar = new int[15];
//            int[] arrIdBoss = new int[10];
            ArrayList<Integer> arrIdBoss = new ArrayList<>();
            for (Mob mob : mobs) {
                if (Math.abs(player.x - mob.pointX) < templateSkillUse.dx && Math.abs(player.y - mob.pointY) < templateSkillUse.dy) {
                    mob.isBlind = true;
                    indexSize[countMob] = mob.tempId;
                    countMob++;
                }
            }
            //dem so luong boss bi choang
            if (bossMap.size() > 0) {
                for (Boss _boss : bossMap) {
                    if (Math.abs(player.x - _boss.x) < templateSkillUse.dx && Math.abs(player.y - _boss.y) < templateSkillUse.dy) {
                        _boss.isCharBlind = true;
                        arrIdChar[countChar] = _boss.id;
                        arrIdBoss.add(_boss.id);
//                        arrIdBoss[countChar] = _boss.id;
                        countChar++;
                    }
                }
            }
            for (Player _p : players) {
                if (Service.gI().checkCanAttackChar(player, _p) && (_p.ItemBody[5] == null || (_p.ItemBody[5] != null && _p.ItemBody[5].template.id != 463))) {
//                if(((_p.cPk != 0 && _p.cPk != player.cPk && player.cPk != 0) || (_p.cPk == 8 && player.cPk != 0) || (_p.cPk != 0 && player.cPk == 8)) && _p.id != player.id 
//                    && (_p.ItemBody[5] == null || (_p.ItemBody[5] != null && _p.ItemBody[5].template.id != 463 ))) { //id 463, CAI TRANG THO DAI KA CHONG TDHS
                    if (Math.abs(player.x - _p.x) < templateSkillUse.dx && Math.abs(player.y - _p.y) < templateSkillUse.dy) {
                        _p.isCharBlind = true;
                        arrIdChar[countChar] = _p.id;
                        countChar++;
                    }
                }
            }
            ArrayList<Integer> arrIdDetu = new ArrayList<>();
            for (Detu _de : pets) {
                if (Service.gI().checkCanAttackDeTu(player, _de)
                        && (_de.ItemBody[5] == null || (_de.ItemBody[5] != null && _de.ItemBody[5].template.id != 463))) { //id 463, CAI TRANG THO DAI KA CHONG TDHS
                    if (Math.abs(player.x - _de.x) < templateSkillUse.dx && Math.abs(player.y - _de.y) < templateSkillUse.dy) {
                        _de.isCharBlind = true;
                        arrIdChar[countChar] = _de.id;
                        arrIdDetu.add(_de.id);
                        countChar++;
                    }
                }
            }
            short timeCHOANG = (short) (templateSkillUse.damage / 1000);
            if (player.getSetKichHoatFull() == (byte) 1) {
                timeCHOANG = (short) (timeCHOANG * 2);
            }
            try {
                m = new Message(-45);
                m.writer().writeByte(0); //type = 0
                m.writer().writeInt(player.id); // id player use
                m.writer().writeShort(templateSkillUse.skillId); //id skill use
                m.writer().writeByte(countMob); //so luong mob bi choang
                for (int i = 0; i < countMob; i++) {
                    m.writer().writeByte(indexSize[i]); // index mob trong map
                    m.writer().writeByte((byte) timeCHOANG); // thoi gian choang second
                }
                if (countChar == 0) {
                    m.writer().writeByte(0); // so luong nguoi bi choang
                } else {
                    m.writer().writeByte(countChar);
                    for (int j = 0; j < countChar; j++) {
                        m.writer().writeInt(arrIdChar[j]); // id char trong map bi chaong
                        m.writer().writeByte((byte) timeCHOANG); // thoi gian choang second
                    }
                }
                m.writer().flush();
                for (Player p : players) {
                    p.session.sendMessage(m);
                }
//                m.cleanup();
//                for(int i = 0; i < countMob; i++) {
//                    try {
//                        m = new Message(-124);
//                        m.writer().writeByte(1);
//                        m.writer().writeByte(1);
//                        m.writer().writeByte(40);
//                        m.writer().writeByte((byte)(mobs.get(indexSize[i]).tempId));
//                        m.writer().flush();
//                        for(Player p: players) {
//                            p.session.sendMessage(m);
//                        }
//                        m.cleanup();
//                    } catch(Exception e) {}
//                }
                ResetBlindTask blindTask = null;
                int timeResetCHOANG = templateSkillUse.damage;
                //CHECK SET KICH HOAT THENXIN HANG
                if (player.getSetKichHoatFull() == (byte) 1) {
                    timeResetCHOANG = timeResetCHOANG * 2;
                }
                if (countMob > 0 || countChar > 0) {
                    blindTask = new ResetBlindTask(mobs, countMob, indexSize, players, countChar, arrIdChar, arrIdBoss, bossMap, arrIdDetu, pets);
                    Timer timerBlind = new Timer();
                    timerBlind.schedule(blindTask, timeResetCHOANG);
                }
                //            player.session.sendMessage(m);
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

    public void useTDHSPet(Player player) {
        Skill skillUse = player.getSkillById(6);
        SkillData skilldata = new SkillData();
        Skill templateSkillUse = skilldata.getSkillBySkillTemplate((byte) 0, skillUse.tempSkillId, skillUse.point);

        int _manaUse = (int) (Util.getPercentDouble(templateSkillUse.manaUse) * player.getMpFull());
        if (player.mp >= _manaUse && !player.checkPlayerBiKhongChe()) {
            player.mp -= _manaUse;
            Message m = null;
            byte countMob = 0;
            byte countChar = 0;
            int[] indexSize = new int[mobs.size()];
            int[] arrIdChar = new int[15];
//            int[] arrIdBoss = new int[10];
            ArrayList<Integer> arrIdBoss = new ArrayList<>();
            for (Mob mob : mobs) {
                if (Math.abs(player.x - mob.pointX) < templateSkillUse.dx && Math.abs(player.y - mob.pointY) < templateSkillUse.dy) {
                    mob.isBlind = true;
                    indexSize[countMob] = mob.tempId;
                    countMob++;
                }
            }
            //dem so luong boss bi choang
            if (bossMap.size() > 0) {
                for (Boss _boss : bossMap) {
                    if (Math.abs(player.x - _boss.x) < templateSkillUse.dx && Math.abs(player.y - _boss.y) < templateSkillUse.dy) {
                        _boss.isCharBlind = true;
                        arrIdChar[countChar] = _boss.id;
                        arrIdBoss.add(_boss.id);
//                        arrIdBoss[countChar] = _boss.id;
                        countChar++;
                    }
                }
            }
            for (Player _p : players) {
                if (((_p.cPk != 0 && _p.cPk != player.cPk && player.cPk != 0) || (_p.cPk == 8 && player.cPk != 0) || (_p.cPk != 0 && player.cPk == 8)) && _p.id != player.id
                        && (_p.ItemBody[5] == null || (_p.ItemBody[5] != null && _p.ItemBody[5].template.id != 463))) { //id 463, CAI TRANG THO DAI KA CHONG TDHS
                    if (Math.abs(player.x - _p.x) < templateSkillUse.dx && Math.abs(player.y - _p.y) < templateSkillUse.dy) {
                        _p.isCharBlind = true;
                        arrIdChar[countChar] = _p.id;
                        countChar++;
                    }
                }
            }
            ArrayList<Integer> arrIdDetu = new ArrayList<>();
            for (Detu _de : pets) {
                if (_de.id != player.id && Service.gI().checkCanAttackDeTu(player, _de)
                        && (_de.ItemBody[5] == null || (_de.ItemBody[5] != null && _de.ItemBody[5].template.id != 463))) { //id 463, CAI TRANG THO DAI KA CHONG TDHS
                    if (Math.abs(player.x - _de.x) < templateSkillUse.dx && Math.abs(player.y - _de.y) < templateSkillUse.dy) {
                        _de.isCharBlind = true;
                        arrIdChar[countChar] = _de.id;
                        arrIdDetu.add(_de.id);
                        countChar++;
                    }
                }
            }
            short timeCHOANG = (short) (templateSkillUse.damage / 1000);
            if (player.getSetKichHoatFull() == (byte) 1) {
                timeCHOANG = (short) (timeCHOANG * 2);
            }
            try {
                m = new Message(-45);
                m.writer().writeByte(0); //type = 0
                m.writer().writeInt(player.id); // id player use
                m.writer().writeShort(templateSkillUse.skillId); //id skill use
                m.writer().writeByte(countMob); //so luong mob bi choang
                for (int i = 0; i < countMob; i++) {
                    m.writer().writeByte(indexSize[i]); // index mob trong map
                    m.writer().writeByte((byte) timeCHOANG); // thoi gian choang second
                }
                if (countChar == 0) {
                    m.writer().writeByte(0); // so luong nguoi bi choang
                } else {
                    m.writer().writeByte(countChar);
                    for (int j = 0; j < countChar; j++) {
                        m.writer().writeInt(arrIdChar[j]); // id char trong map bi chaong
                        m.writer().writeByte((byte) timeCHOANG); // thoi gian choang second
                    }
                }
                m.writer().flush();
                for (Player p : players) {
                    p.session.sendMessage(m);
                }

                ResetBlindTask blindTask = null;
                int timeResetCHOANG = templateSkillUse.damage;
                //CHECK SET KICH HOAT THENXIN HANG
                if (player.getSetKichHoatFull() == (byte) 1) {
                    timeResetCHOANG = timeResetCHOANG * 2;
                }
                if (countMob > 0 || countChar > 0) {
                    blindTask = new ResetBlindTask(mobs, countMob, indexSize, players, countChar, arrIdChar, arrIdBoss, bossMap, arrIdDetu, pets);
                    Timer timerBlind = new Timer();
                    timerBlind.schedule(blindTask, timeResetCHOANG);
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
    }

    //detrung namek
    public void useDeTrung(Player player, byte type) {
        if (type == (byte) 0) {
            Skill templateSkillUse = player.getSkillByIDTemplate((short) 12);
            if (templateSkillUse != null) {
                int _manaUse = (int) (Util.getPercentDouble(templateSkillUse.manaUse) * player.getMpFull());
                long _TIMENOW = System.currentTimeMillis();
                if (player.mp >= _manaUse && !player.checkPlayerBiKhongChe() && ((_TIMENOW - templateSkillUse.lastTimeUseThisSkill) > (long) templateSkillUse.coolDown)) {
                    //CHECK NOI TAI DE TRUNG CO GIAM THOI GIAN HOI CHIEU
                    if (player.noiTai.id != 0 && templateSkillUse.template.id == player.noiTai.idSkill) {
                        long _timeDOWN = (long) (Util.getPercentDouble((int) (player.noiTai.param)) * templateSkillUse.coolDown);
                        templateSkillUse.lastTimeUseThisSkill = _TIMENOW - _timeDOWN;
                        //SEND TIME DOWN NOI TAI
                        Service.gI().sendCoolDownSkill(player, templateSkillUse.skillId, (int) ((long) templateSkillUse.coolDown - _timeDOWN));
                    } else {
                        templateSkillUse.lastTimeUseThisSkill = _TIMENOW;
                    }
                    player.mp -= _manaUse;
                    player.dameChim = 45 + templateSkillUse.point * 5;
                    Message m = null;
                    try {
                        m = new Message(-95);
                        m.writer().writeByte(type); //type = 0 init de trung
                        m.writer().writeInt(player.id);
                        if (templateSkillUse.point == 1) {
                            m.writer().writeShort((short) 8); //ID MOB
                            //                        initChimNamek(player, 8);
                        } else if (templateSkillUse.point == 2) {
                            m.writer().writeShort((short) 11);
                            //                        initChimNamek(player, 11);
                        } else if (templateSkillUse.point == 3) {
                            m.writer().writeShort((short) 32);
                            //                        initChimNamek(player, 32);
                        } else if (templateSkillUse.point == 4) {
                            m.writer().writeShort((short) 25);
                            //                        initChimNamek(player, 25);
                        } else if (templateSkillUse.point == 5) {
                            m.writer().writeShort((short) 43);
                            //                        initChimNamek(player, 43);
                        } else if (templateSkillUse.point == 6) {
                            m.writer().writeShort((short) 49);
                            //                        initChimNamek(player, 49);
                        } else if (templateSkillUse.point == 7) {
                            m.writer().writeShort((short) 50);
                            //                        initChimNamek(player, 50);
                        }
                        m.writer().writeInt(player.getHpFull()); //HP
                        //
                        player.chimFollow = (byte) 1;
                        m.writer().flush();
                        for (Player p : players) {
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
                    //TIMER RESET CHIM
                    Timer resetChim = new Timer();
                    TimerTask rsChim = new TimerTask() {
                        public void run() {
                            Message m = null;
                            try {
                                m = new Message(-95);
                                m.writer().writeByte((byte) 7);
                                m.writer().writeInt(player.id); //ID CHIM = ID PLAYER
                                m.writer().flush();
                                for (Player p : player.zone.players) {
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
                            player.chimFollow = (byte) 0;
                            player.dameChim = 0;
                            resetChim.cancel();
                            player.timerDeTrung = null;
                        }
                    ;
                    };
                    resetChim.schedule(rsChim, (long) (templateSkillUse.coolDown * 0.3));
                    player.timerDeTrung = resetChim;
                }
            }
        } else if (type == (byte) 7) { //REMOVE CHIM
            Message m = null;
            try {
                m = new Message(-95);
                m.writer().writeByte(type);
                m.writer().writeInt(player.id); //ID CHIM = ID PLAYER
                m.writer().flush();
                for (Player p : players) {
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
    }

    public void useDeTrungForMe(Player me, Player player, byte type) {
        Skill templateSkillUse = player.getSkillByIDTemplate((short) 12);
        if (templateSkillUse != null) {
            Message m = null;
            try {
                m = new Message(-95);
                m.writer().writeByte(type); //type = 0 init de trung
                if (type == (byte) 0) {
                    m.writer().writeInt(player.id);
                    if (templateSkillUse.point == 1) {
                        m.writer().writeShort((short) 8); //ID MOB
                    } else if (templateSkillUse.point == 2) {
                        m.writer().writeShort((short) 11);
                    } else if (templateSkillUse.point == 3) {
                        m.writer().writeShort((short) 32);
                    } else if (templateSkillUse.point == 4) {
                        m.writer().writeShort((short) 25);
                    } else if (templateSkillUse.point == 5) {
                        m.writer().writeShort((short) 43);
                    } else if (templateSkillUse.point == 6) {
                        m.writer().writeShort((short) 49);
                    } else if (templateSkillUse.point == 7) {
                        m.writer().writeShort((short) 50);
                    }
                    m.writer().writeInt(player.getHpFull()); //HP
                    //
                } else if (type == (byte) 7) { //REMOVE CHIM KHOI MAP
                    m.writer().writeInt(player.id); //ID CHIM = ID PLAYER
                }
                m.writer().flush();
                me.session.sendMessage(m);
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

    public void chimAttackMob(Player player, int tempId) {
        Message m = null;
        try {
            m = new Message(-95);
            m.writer().writeByte((byte) 1);
            m.writer().writeInt(player.id); //ID CHIM HAY CHINH LA ID PLAYER
            m.writer().writeByte((byte) tempId); //ID MOB
            m.writer().flush();
            for (Player p : players) {
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

    public void chimDameMob(Player player, Mob mob, int damage) {
        Message m = null;
        try {
            m = new Message(-95);
            m.writer().writeByte((byte) 3);
            m.writer().writeInt(player.id); //ID CHIM HAY CHINH LA ID PLAYER
            m.writer().writeInt(mob.tempId);
            m.writer().writeInt(mob.hp);
            m.writer().writeInt(damage);
            m.writer().flush();
            for (Player p : players) {
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

    public void chimDameChar(Player player, Player target, int damage) {
        Message m = null;
        try {
            m = new Message(-95);
            m.writer().writeByte((byte) 2);
            m.writer().writeInt(player.id); //ID CHIM HAY CHINH LA ID PLAYER
            m.writer().writeInt(target.id);
            m.writer().writeInt(damage);
            m.writer().writeInt(target.hp);
            m.writer().flush();
            for (Player p : players) {
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

    public void useDeTrung2(Player player) {
//        Skill skillUse = player.getSkillById(12);
//        SkillData skilldata = new SkillData();
//        Skill templateSkillUse = skilldata.getSkillBySkillTemplate(player.gender, skillUse.skillId, skillUse.point);
        Skill templateSkillUse = player.getSkillByIDTemplate((short) 12);
        int _manaUse = (int) (Util.getPercentDouble(templateSkillUse.manaUse) * player.getMpFull());
        long _TIMENOW = System.currentTimeMillis();
        if (player.mp >= _manaUse && !player.checkPlayerBiKhongChe() && ((_TIMENOW - templateSkillUse.lastTimeUseThisSkill) > (long) templateSkillUse.coolDown)) {
            //CHECK NOI TAI DE TRUNG CO GIAM THOI GIAN HOI CHIEU
            if (player.noiTai.id != 0 && templateSkillUse.template.id == player.noiTai.idSkill) {
                long _timeDOWN = (long) (Util.getPercentDouble((int) (player.noiTai.param)) * templateSkillUse.coolDown);
                templateSkillUse.lastTimeUseThisSkill = _TIMENOW - _timeDOWN;
                //SEND TIME DOWN NOI TAI
                Service.gI().sendCoolDownSkill(player, templateSkillUse.skillId, (int) ((long) templateSkillUse.coolDown - _timeDOWN));
            } else {
                templateSkillUse.lastTimeUseThisSkill = _TIMENOW;
            }
            player.mp -= _manaUse;
            setSkillPaint(templateSkillUse.skillId, player, 8);

            player.isDeTrung = true;
            Service.gI().loadPoint(player.session, player);
            // hien thi 30s icon de trung
            showIconItemUse(player, 722, (int) (templateSkillUse.coolDown * 0.3 / 1000));

            ResetDeTrungUpTask detrungTask = new ResetDeTrungUpTask(player);
            Timer timer = new Timer();
            timer.schedule(detrungTask, (int) (templateSkillUse.coolDown * 0.3));
        }
    }

    //de tu bao ve, type 1 baove, type 2 tan cong
    public void PetAttack(Player _player, Detu _detu, byte type) {
        ArrayList<Skill> petSkills = new ArrayList<>();
        SkillData skilldata = new SkillData();
//        Skill templateSkillUse = null;
        for (Skill sk : _detu.listSkill) {
            if (sk.skillId != -1) {
                petSkills.add(skilldata.getSkillBySkillTemplate(sk.genderSkill, sk.tempSkillId, sk.point));
            }
        }
        if (_player.detuUpPoint != null) {
            _player.detuUpPoint.cancel();
            _player.detuUpPoint = null;
        }
        Timer timerPetPointUp = new Timer();
        TimerTask petPointUp = new TimerTask() {
            public void run() {
                if (_player.statusPet != 1 && _player.statusPet != 2) {
                    timerPetPointUp.cancel();
//                } else if(_detu.tiemNang > 0) {
                } else if (_detu.tiemNang > 0 && (_detu.hpGoc < _detu.getHpMpLimit() || _detu.mpGoc < _detu.getHpMpLimit() || _detu.damGoc < _detu.getDamLimit())) {
                    pointUpForPet(_detu);
                }
            }
        ;
        };
        timerPetPointUp.schedule(petPointUp, 0, 30000);
        _player.detuUpPoint = timerPetPointUp;
//        _detu.lastTimeUpPoint = System.currentTimeMillis(); //time up point
//        System.out.println("thoi gian INIT: " + _detu.lastTimeUpPoint);
        //task detu bao ve
        if (_player.detuAttack != null) {
            _player.detuAttack.cancel();
            _player.detuAttack = null;
        }
        if (type == 1) {
//            int idrdskill = 0;
//            if(petSkills.size() > 1) {
//                idrdskill = 1;
//            }
//            if(_detu.isKaioken && _detu.mp > 13000) {
//                idrdskill = 2;
//            }
//            Skill petProSkill = petSkills.get(idrdskill);

            Timer timerPetProtect = new Timer();
            TimerTask petPro = new TimerTask() {
                public void run() {
//                    System.out.println("de tu bao ve:");
                    if (!_player.isdie) {
                        if (_detu.isdie) {
                            _detu.isKaioken = false;
                            _detu.isMonkey = false;
                            timerPetProtect.cancel();

                            // creating timertask, timer
                            Timer timerHsPro = new Timer();
                            TimerTask ttPro = new TimerTask() {
                                public void run() {
                                    if (!_detu.isdie) {
                                        timerHsPro.cancel();
                                    } else {
                                        _player.timerHSDe = null;
                                        _detu.x = _player.x;
                                        _detu.y = _player.y;
                                        Service.gI().petLiveFromDead(_player);
                                        PetAttack(_player, _detu, (byte) 1);
                                        timerHsPro.cancel();
                                    }
                                }
                            ;
                            };
                        timerHsPro.schedule(ttPro, 60000);
                            _player.timerHSDe = timerHsPro;
                        } else if (_player.statusPet != 1) {
                            _detu.isKaioken = false;
                            timerPetProtect.cancel();
                        } else if (!_detu.isTTNL) {
                            Mob _mobAttackPro = getMobPetNearest(_detu);
                            int lx = 0;
                            int ly = 0;
                            int idrdskill = 0;
                            //check co trong tam danh cua skill 1 khong
                            if (_mobAttackPro != null) {
                                if ((Math.abs(_detu.x - _mobAttackPro.pointX) <= petSkills.get(0).dx) && (Math.abs(_detu.y - _mobAttackPro.pointY) <= petSkills.get(0).dy)) {
                                    lx = petSkills.get(0).dx;
                                    ly = petSkills.get(0).dy;
                                } else if (petSkills.size() > 1 && (Math.abs(_detu.x - _mobAttackPro.pointX) <= petSkills.get(1).dx) && (Math.abs(_detu.y - _mobAttackPro.pointY) <= petSkills.get(1).dy)) {
                                    lx = petSkills.get(1).dx;
                                    ly = petSkills.get(1).dy;
                                    idrdskill = 1;
                                }
                                if (_detu.isKaioken) {
                                    lx = ly = 500;
                                    idrdskill = 2;
                                }
                                Skill petProSkill = petSkills.get(idrdskill);
                                int damage = 0;

                                //                        if(_detu.isKaioken) {
                                //                            lx = ly = 500;
                                //                        }
                                if ((Math.abs(_detu.x - _mobAttackPro.pointX) <= lx) && (Math.abs(_detu.y - _mobAttackPro.pointY) <= ly)) {
                                    if (petSkills.size() == 1 || _detu.isKaioken) { // neu moi chi co skill 1 thi move de dam, khong thi thoi
                                        //send detu den vi tri mob
                                        _detu.x = _mobAttackPro.pointX;
                                        _detu.y = _mobAttackPro.pointY;
                                        detuMove(_detu);
                                    }
                                    //send de tu attack
                                    if (_detu.mp >= petProSkill.manaUse && _detu.stamina >= (short) 2) {
                                        _detu.mp -= petProSkill.manaUse;
                                        if (_detu.isMabu || _detu.isBerus) {
                                            _detu.stamina = (short) (_detu.stamina - 5) < (short) 0 ? (short) 0 : (short) (_detu.stamina - 5);
                                        } else {
                                            _detu.stamina = (short) (_detu.stamina - 2) < (short) 0 ? (short) 0 : (short) (_detu.stamina - 2);
                                        }
                                        try {
                                            PetSendAttack(_player, _detu, _mobAttackPro, petProSkill);
                                        } catch (Exception e) {
                                        }
                                        //use skill 3
                                        long timeNow = System.currentTimeMillis(); //get time hien tai

                                        if (petSkills.size() >= 3) {
                                            if (_detu.listSkill.get(2).tempSkillId == 9) {
                                                if (_detu.isKaioken) {
                                                    _detu.isKaioken = false;
                                                    _detu.hp -= (int) (_detu.getHpFull() * 0.1);
                                                    _detu.mp -= 9000;
                                                    _detu.updateHpDetu(_player, _detu);

                                                } else if ((timeNow - _detu.listSkill.get(2).lastTimeUseThisSkill) >= 30000) {
                                                    _detu.listSkill.get(2).lastTimeUseThisSkill = timeNow;
                                                    _detu.isKaioken = true;
                                                }
                                            } else {
                                                if ((timeNow - _detu.listSkill.get(2).lastTimeUseThisSkill) >= petSkills.get(2).coolDown) {
                                                    _detu.listSkill.get(2).lastTimeUseThisSkill = timeNow;
                                                    //send use skill
                                                    if (_detu.listSkill.get(2).tempSkillId == 6) { // tdhs
                                                        useTDHSPet(_detu);
                                                    } else if (_detu.listSkill.get(2).tempSkillId == 8) { //ttnl
                                                        _detu.isTTNL = true;
                                                        _detu.petChargeHPMP(_player, _detu, 1);
                                                        // creating timertask, timer
                                                        Timer timerPetTTNL = new Timer();
                                                        TimerTask petTTNL = new TimerTask() {
                                                            public void run() {
                                                                if (_detu.isdie) {
                                                                    _detu.countCharge = 0;
                                                                    _detu.isTTNL = false;
                                                                    timerPetTTNL.cancel();
                                                                }
                                                                if (_detu.countCharge >= 10 || (_detu.hp >= _detu.getHpFull())) {
                                                                    _detu.countCharge = 0;
                                                                    _detu.isTTNL = false;
                                                                    timerPetTTNL.cancel();
                                                                    _detu.petChargeHPMP(_player, _detu, 3);
                                                                } else {
                                                                    _detu.petChargeHPMP(_player, _detu, 2);
                                                                }
                                                            }
                                                        ;
                                                        };
                                                    timerPetTTNL.schedule(petTTNL, 0, 1000);
                                                    }
                                                }
                                            }

                                        }
                                        //use skill 4
                                        if (petSkills.size() == 4) {
                                            if ((timeNow - _detu.listSkill.get(3).lastTimeUseThisSkill) >= petSkills.get(3).coolDown) {
                                                _detu.listSkill.get(3).lastTimeUseThisSkill = timeNow;
                                                //send use skill
                                                if (_detu.listSkill.get(3).tempSkillId == 13) { // transform
                                                    int _mpFullKhi = (int) (_detu.getMpFull() * 0.1);
                                                    if (_detu.mp >= _mpFullKhi) {
                                                        _detu.mp -= _mpFullKhi;
                                                        _detu.isMonkey = true;
                                                        _detu.updateBodyMonkeyPet(_player, _detu, petSkills.get(3));
                                                    }
                                                } else if (_detu.listSkill.get(3).tempSkillId == 19) { //khien nang luong
//                                                int _mpKhien = (int)(_detu.getMpFull()*_detu.listSkill.get(3).manaUse/100);
//                                                if(_detu.mp >= _mpKhien) {
//                                                    _detu.mp -= _mpKhien;
//                                                    _detu.isProtect = true;
                                                    useProtectPet(_detu);
//                                                }
                                                }
                                            }
                                        }
                                    } else {
                                        chat(_detu, "Sư phụ ơi cho con đậu thần!");
                                    }

                                }
                            }
                        }
                    }
                }
            };
//            timerPetProtect.schedule(petPro, 0, (petProSkill.coolDown * 2));
            timerPetProtect.schedule(petPro, 0, 1000);
            //ASIGN TIMER DETU PROTECT
            _player.detuAttack = timerPetProtect;
        } else if (type == 2) {
            // creating timertask, timer de tu tan cong
//            int idrd = 0;
//            if(petSkills.size() > 1) {
//                idrd = Util.nextInt(0, 2);
//            }
//            Skill petUseSkill = petSkills.get(idrd);

            Timer timerPetAttack = new Timer();
            TimerTask petAtt = new TimerTask() {
                public void run() {
                    if (!_player.isdie) {
                        if (_detu.isdie) {
                            _detu.isMonkey = false;
                            timerPetAttack.cancel();

                            // creating timertask, timer
                            Timer timerHs = new Timer();
                            TimerTask tt = new TimerTask() {
                                public void run() {
                                    if (!_detu.isdie) {
                                        timerHs.cancel();
                                    } else {
                                        _player.timerHSDe = null;
                                        Service.gI().SendPetLiveFromDead(_player); //dang tan cong nen hoi sinh tai cho
                                        PetAttack(_player, _detu, (byte) 2);
                                        timerHs.cancel();
                                    }
                                }
                            ;
                            };
                        timerHs.schedule(tt, 60000);
                            _player.timerHSDe = timerHs;
                        } else if (_player.statusPet != 2) {
                            timerPetAttack.cancel();
                        } else if (!_detu.isTTNL) {
                            Mob _mobAttack = getMobPetNearest(_detu);
                            int lx = 0;
                            int ly = 0;
                            int idrdskill = 0;
                            //check co trong tam danh cua skill 1 khong
                            if (petSkills.size() >= 2) {
                                idrdskill = Util.nextInt(0, 2);
                            }
                            if (_detu.isKaioken) {
                                idrdskill = 2;
                            }
                            Skill petUseSkill = petSkills.get(idrdskill);
                            int damage = 0;
                            long timeNow = System.currentTimeMillis(); //get time hien tai
                            if (_mobAttack != null) {
                                if ((Math.abs(_detu.x - _mobAttack.pointX) <= petUseSkill.dx) && (Math.abs(_detu.y - _mobAttack.pointY) <= petUseSkill.dy)) {
                                    //send de tu attack
                                    if (_detu.mp >= petUseSkill.manaUse && _detu.stamina >= (short) 2) {
                                        _detu.mp -= petUseSkill.manaUse;
                                        if (_detu.isMabu || _detu.isBerus) {
                                            _detu.stamina = (short) (_detu.stamina - 5) < (short) 0 ? (short) 0 : (short) (_detu.stamina - 5);
                                        } else {
                                            _detu.stamina = (short) (_detu.stamina - 2) < (short) 0 ? (short) 0 : (short) (_detu.stamina - 2);
                                        }
                                        try {
                                            PetSendAttack(_player, _detu, _mobAttack, petUseSkill);
                                            //use skill 3
                                            if (petSkills.size() >= 3) {
                                                if (_detu.listSkill.get(2).tempSkillId == 9) {
                                                    if (_detu.isKaioken) {
                                                        _detu.isKaioken = false;
                                                        _detu.hp -= (int) (_detu.getHpFull() * 0.1);
                                                        _detu.mp -= 9000;
                                                        _detu.updateHpDetu(_player, _detu);

                                                    } else if ((timeNow - _detu.listSkill.get(2).lastTimeUseThisSkill) >= 30000) {
                                                        _detu.listSkill.get(2).lastTimeUseThisSkill = timeNow;
                                                        _detu.isKaioken = true;
                                                    }
                                                } else {
                                                    if ((timeNow - _detu.listSkill.get(2).lastTimeUseThisSkill) >= petSkills.get(2).coolDown) {
                                                        _detu.listSkill.get(2).lastTimeUseThisSkill = timeNow;
                                                        //send use skill
                                                        if (_detu.listSkill.get(2).tempSkillId == 6) { // tdhs
                                                            useTDHSPet(_detu);
                                                        } else if (_detu.listSkill.get(2).tempSkillId == 8) { //ttnl
                                                            _detu.isTTNL = true;
                                                            _detu.petChargeHPMP(_player, _detu, 1);
                                                            // creating timertask, timer
                                                            Timer timerPetTTNL = new Timer();
                                                            TimerTask petTTNL = new TimerTask() {
                                                                public void run() {
                                                                    if (_detu.isdie) {
                                                                        _detu.countCharge = 0;
                                                                        _detu.isTTNL = false;
                                                                        timerPetTTNL.cancel();
                                                                    }
                                                                    if (_detu.countCharge >= 10 || (_detu.hp >= _detu.getHpFull())) {
                                                                        _detu.countCharge = 0;
                                                                        _detu.isTTNL = false;
                                                                        timerPetTTNL.cancel();
                                                                        _detu.petChargeHPMP(_player, _detu, 3);
                                                                    } else {
                                                                        _detu.petChargeHPMP(_player, _detu, 2);
                                                                    }
                                                                }
                                                            ;
                                                            };
                                                        timerPetTTNL.schedule(petTTNL, 0, 1000);
                                                        }
                                                    }
                                                }

                                            }
                                            //use skill 4
                                            if (petSkills.size() == 4) {
                                                if ((timeNow - _detu.listSkill.get(3).lastTimeUseThisSkill) >= petSkills.get(3).coolDown) {
                                                    _detu.listSkill.get(3).lastTimeUseThisSkill = timeNow;
                                                    //send use skill
                                                    if (_detu.listSkill.get(3).tempSkillId == 13) { // transform
                                                        int _mpFullKhi = (int) (_detu.getMpFull() * 0.1);
                                                        if (_detu.mp >= _mpFullKhi) {
                                                            _detu.mp -= _mpFullKhi;
                                                            _detu.isMonkey = true;
                                                            _detu.updateBodyMonkeyPet(_player, _detu, petSkills.get(3));
                                                        }
                                                    } else if (_detu.listSkill.get(3).tempSkillId == 19) { //khien nang luong
//                                                    int _mpKhien = (int)(_detu.getMpFull()*_detu.listSkill.get(3).manaUse/100);
//                                                    if(_detu.mp >= _mpKhien) {
//                                                        _detu.mp -= _mpKhien;
//                                                        _detu.isProtect = true;
                                                        useProtectPet(_detu);
//                                                    }
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {
                                        }
                                    } else {
                                        chat(_detu, "Sư phụ ơi cho con đậu thần!");
                                    }
                                } else {
                                    //send detu den vi tri mob
                                    _detu.x = _mobAttack.pointX;
                                    _detu.y = _mobAttack.pointY;
                                    detuMove(_detu);

                                    if (_detu.mp >= petUseSkill.manaUse && _detu.stamina >= (short) 2) {
                                        _detu.mp -= petUseSkill.manaUse;
                                        if (_detu.isMabu || _detu.isBerus) {
                                            _detu.stamina = (short) (_detu.stamina - 5) < (short) 0 ? (short) 0 : (short) (_detu.stamina - 5);
                                        } else {
                                            _detu.stamina = (short) (_detu.stamina - 2) < (short) 0 ? (short) 0 : (short) (_detu.stamina - 2);
                                        }
                                        try {
                                            PetSendAttack(_player, _detu, _mobAttack, petUseSkill);
                                            //use skill 3
                                            if (petSkills.size() >= 3) {
                                                if (_detu.listSkill.get(2).tempSkillId == 9) {
                                                    if (_detu.isKaioken) {
                                                        _detu.isKaioken = false;
                                                        _detu.hp -= (int) (_detu.getHpFull() * 0.1);
                                                        _detu.mp -= 9000;
                                                        _detu.updateHpDetu(_player, _detu);

                                                    } else if ((timeNow - _detu.listSkill.get(2).lastTimeUseThisSkill) >= 30000) {
                                                        _detu.listSkill.get(2).lastTimeUseThisSkill = timeNow;
                                                        _detu.isKaioken = true;
                                                    }
                                                } else {
                                                    if ((timeNow - _detu.listSkill.get(2).lastTimeUseThisSkill) >= petSkills.get(2).coolDown) {
                                                        _detu.listSkill.get(2).lastTimeUseThisSkill = timeNow;
                                                        //send use skill
                                                        if (_detu.listSkill.get(2).tempSkillId == 6) { // tdhs
                                                            useTDHSPet(_detu);
                                                        } else if (_detu.listSkill.get(2).tempSkillId == 8) { //ttnl
                                                            _detu.isTTNL = true;
                                                            _detu.petChargeHPMP(_player, _detu, 1);
                                                            // creating timertask, timer
                                                            Timer timerPetTTNL = new Timer();
                                                            TimerTask petTTNL = new TimerTask() {
                                                                public void run() {
                                                                    if (_detu.isdie) {
                                                                        _detu.countCharge = 0;
                                                                        _detu.isTTNL = false;
                                                                        timerPetTTNL.cancel();
                                                                    }
                                                                    if (_detu.countCharge >= 10 || (_detu.hp >= _detu.getHpFull())) {
                                                                        _detu.countCharge = 0;
                                                                        _detu.isTTNL = false;
                                                                        timerPetTTNL.cancel();
                                                                        _detu.petChargeHPMP(_player, _detu, 3);
                                                                    } else {
                                                                        _detu.petChargeHPMP(_player, _detu, 2);
                                                                    }
                                                                }
                                                            ;
                                                            };
                                                        timerPetTTNL.schedule(petTTNL, 0, 1000);
                                                        }
                                                    }
                                                }
                                            }
                                            //use skill 4
                                            if (petSkills.size() == 4) {
                                                if ((timeNow - _detu.listSkill.get(3).lastTimeUseThisSkill) >= petSkills.get(3).coolDown) {
                                                    _detu.listSkill.get(3).lastTimeUseThisSkill = timeNow;
                                                    //send use skill
                                                    if (_detu.listSkill.get(3).tempSkillId == 13) { // transform
                                                        int _mpFullKhi = (int) (_detu.getMpFull() * 0.1);
                                                        if (_detu.mp >= _mpFullKhi) {
                                                            _detu.mp -= _mpFullKhi;
                                                            _detu.isMonkey = true;
                                                            _detu.updateBodyMonkeyPet(_player, _detu, petSkills.get(3));
                                                        }
                                                    } else if (_detu.listSkill.get(3).tempSkillId == 19) { //khien nang luong
//                                                    int _mpKhien = (int)(_detu.getMpFull()*_detu.listSkill.get(3).manaUse/100);
//                                                    if(_detu.mp >= _mpKhien) {
//                                                        _detu.mp -= _mpKhien;
//                                                        _detu.isProtect = true;
                                                        useProtectPet(_detu);
//                                                    }
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {
                                        }
                                    } else {
                                        chat(_detu, "Sư phụ ơi cho con đậu thần!");
                                    }
                                }
                            }
                        }
                    }
                }
            ;
            };
            timerPetAttack.schedule(petAtt, 0, 1000);
            //ASIGN TIMER DETU TANCONG
            _player.detuAttack = timerPetAttack;
        }
    }

    //DETU ATTACK MOBBBBBBBBBBBBBBBBBBBB
    public void PetSendAttack(Player _player, Detu _detu, Mob _mob, Skill _skill) throws IOException {
        int damage = Util.nextInt((int) (_detu.getDamFull() * 0.9 * Util.getPercentDouble((int) _skill.damage)), (int) (_detu.getDamFull() * Util.getPercentDouble((int) _skill.damage)));
        int miss = Util.nextInt(0, 10);
        if (miss > 8) {
            damage = 0;
        }
        int fantashi = Util.nextInt(0, 100);
        boolean fatal = _detu.getCritFull() >= fantashi;
        if (fatal) {
            damage = damage * 2;
        }
        //CHECK SET KICH HOAT SONGOKU CUA DE TU
        if (_detu.getSetKichHoatFull() == (byte) 3 && _skill.template.id == (byte) 1) {
            damage = damage * 2;
        } else if (_detu.getSetKichHoatFull() == (byte) 7 && _skill.template.id == (byte) 4) { //SET KICH HOAT KAKAROT CUA DE TU
            damage = damage * 2;
        }
        //END CHECK SET KICH HOAT SONGOKU CUA DE TU
        //Check bua de tu, neu co thi tang 100% dame cho de tu BUA DE TUs
        if (_player.getBuaDetu()) {
            damage = damage * 2;
        }
        if (damage >= _mob.maxHp && _mob.hp == _mob.maxHp) {
            damage = _mob.maxHp - 1;
        } else if (damage > _mob.hp) {
            damage = _mob.hp;
        } else if (_mob.hp == 1 && damage > 1) {
            damage = 1;
        }

        long upSmTn = (long) (damage);
//        long upSmTn = (long)(damage*20);
        upSmTn = _detu.getPercentUpTnSm(upSmTn); //get tnsm spl tnsm
        if (_detu.cPk > 0 && _detu.cPk < 12 && _detu.cPk != 8) {
            upSmTn = (long) (upSmTn * 1.05);
        } else if (_detu.cPk == 8) {
            upSmTn = (long) (upSmTn * 1.1);
        }
        if (_detu.isMabu || _detu.isBerus) {
            upSmTn = (long) (upSmTn / 3);
        }
        if (_player.ItemBody[5] != null && (_player.ItemBody[5].template.id == 710 || _player.ItemBody[5].template.id == 711)) {
            if (_player.ItemBody[5].template.id == 711) {
                upSmTn = (long) (upSmTn * 1.5);
            } else {
                upSmTn = (long) (upSmTn * 1.35);
            }
        }
        if (_mob.template.tempId == 0) {
            damage = 10;
            upSmTn = 1;
        }
        //MAP NGU HANH SON, TNSM x2
        if (_player.map.id >= 122 && _player.map.id <= 124) {
            upSmTn = upSmTn * 4;
        }
        //MAP THANH DIA GIAM TNSM
        if (_player.map.id >= 156 && _player.map.id <= 159) {
            upSmTn = (long) (upSmTn / 5);
        }
        //MAP KHI GAS GIAM TNSM
        if (_player.map.MapKhiGas()) {
            upSmTn = (long) (upSmTn / 500);
        }
        upSmTn = (long) (upSmTn * 3);
        if (_detu.power > 40000000000L) {
            upSmTn = (long) (upSmTn / 3);
        } else if (_detu.power > 50000000000L) {
            upSmTn = (long) (upSmTn / 4);
        } else if (_detu.power > 60000000000L) {
            upSmTn = (long) (upSmTn / 5);
        } else if (_detu.power > 70000000000L) {
            upSmTn = (long) (upSmTn / 7);
        }
        _mob.updateHP(-damage);
        //UP TNSM
        upSmTn = upSmTn > 0 ? upSmTn : 1;
        if (upSmTn > 0) {
            if (_detu.power < (_detu.getPowerLimit() * 1000000000L)) {
                _detu.tiemNang += upSmTn;
                _detu.power += upSmTn;
                //UP POINT PET
//                openSkillPetAndUpPoint(_detu);
            }
            if (_player.power < (_player.getPowerLimit() * 1000000000L)) {
                _player.tiemNang += (long) (upSmTn / 4);
                _player.power += (long) (upSmTn / 4);
                _player.UpdateSMTN((byte) 2, (long) (upSmTn / 4)); // send cong tnsm cho su phu khi de danh
                if (_player.clan != null) {
                    upTNMemBerClanInMap(_player, (long) (upSmTn / 4));
                }
            }
        }
        if (upSmTn > 0) {
//        if(_detu.tiemNang > 0 && (_detu.hpGoc < _detu.getHpMpLimit() || _detu.mpGoc < _detu.getHpMpLimit() || _detu.damGoc < _detu.getDamLimit())) {
            openSkillPetAndUpPoint(_detu);
        }

        if (_mob.isDie) {
            ArrayList<ItemMap> itemDrop = new ArrayList<>();
            if (_mob.template.tempId != 0) {
                int idItemNotSell[] = {17, 188, 189, 190, 18, 19, 20, 441, 442, 443, 444, 445, 446, 447, 17, 188, 189, 190, 225, 76, 188, 189, 190, 18, 19, 20, 17, 188, 189, 190, 18, 19, 20};
                int percentDrop = Util.nextInt(0, 10);
                int percentDropItemGod = Util.nextInt(0, 5000);
                int perTA = Util.nextInt(0, 1000);
                if (percentDropItemGod < 2 && map.MapCold()) {
                    ItemSell itemGOD = null;
                    int _ITEMMAPID = -1;
                    int idItemGod[] = {555, 556, 561, 562, 563, 557, 558, 561, 564, 565, 559, 560, 561, 566, 567};
                    percentDropItemGod = Util.nextInt(0, 15);
                    itemGOD = ItemSell.getItemSell(idItemGod[percentDropItemGod], (byte) 1);
                    _ITEMMAPID = idItemGod[percentDropItemGod];
//                    if(_detu.gender == 0) {
//                        int idItemGod[] = {555, 556, 561, 562, 563};
//                        percentDropItemGod = Util.nextInt(0, 5);
//                        itemGOD = ItemSell.getItemSell(idItemGod[percentDropItemGod], (byte)1);
//                        _ITEMMAPID = idItemGod[percentDropItemGod];
//                    } else if (_detu.gender == 1) {
//                        int idItemGod[] = {557, 558, 561, 564, 565};
//                        percentDropItemGod = Util.nextInt(0, 5);
//                        itemGOD = ItemSell.getItemSell(idItemGod[percentDropItemGod], (byte)1);
//                        _ITEMMAPID = idItemGod[percentDropItemGod];
//                    } else if (_detu.gender == 2) {
//                        int idItemGod[] = {559, 560, 561, 566, 567};
//                        percentDropItemGod = Util.nextInt(0, 5);
//                        itemGOD = ItemSell.getItemSell(idItemGod[percentDropItemGod], (byte)1);
//                        _ITEMMAPID = idItemGod[percentDropItemGod];
//                    }
                    if (_ITEMMAPID >= 555 && _ITEMMAPID <= 567) {
                        ItemMap itemROI = new ItemMap();
                        itemROI.playerId = _player.id;
                        itemROI.x = _mob.pointX;
                        itemROI.y = _mob.pointY;
//                        itemROI.itemMapID = _ITEMMAPID;
//                        itemROI.itemTemplateID = (short)itemROI.itemMapID;
                        itemROI.itemMapID = itemsMap.size();
                        itemROI.itemTemplateID = (short) _ITEMMAPID;
//                                itemGOD.item.template = ItemTemplate.ItemTemplateID(_ITEMMAPID);
                        //BUILD NEW ITEM + CHI SO CHO DO THAN LINH
                        Item _ITEMGOD = new Item(itemGOD.item);
                        _ITEMGOD.itemOptions.clear();
                        if (_ITEMGOD.template.id == 555 || _ITEMGOD.template.id == 557 || _ITEMGOD.template.id == 559) { //AO THAN
                            _ITEMGOD.itemOptions.add(new ItemOption(47, Util.nextInt(500, 1001)));
                        } else if (_ITEMGOD.template.id == 556) { //QUAN THAN TRAI DAT
                            _ITEMGOD.itemOptions.add(new ItemOption(6, (Util.nextInt(350, 551) * 100)));
                            _ITEMGOD.itemOptions.add(new ItemOption(27, Util.nextInt(9000, 10000)));
                        } else if (_ITEMGOD.template.id == 558) {//QUAN THAN NAMEK
                            _ITEMGOD.itemOptions.add(new ItemOption(6, (Util.nextInt(350, 501) * 100)));
                            _ITEMGOD.itemOptions.add(new ItemOption(27, Util.nextInt(9000, 9500)));
                        } else if (_ITEMGOD.template.id == 560) { //QUAN THAN XAYDA
                            _ITEMGOD.itemOptions.add(new ItemOption(6, (Util.nextInt(350, 601) * 100)));
                            _ITEMGOD.itemOptions.add(new ItemOption(27, Util.nextInt(9500, 10500)));
                        } else if (_ITEMGOD.template.id == 562) { //GANG THAN TD
                            _ITEMGOD.itemOptions.add(new ItemOption(0, (Util.nextInt(300, 571) * 10)));
                        } else if (_ITEMGOD.template.id == 564) { //GANG THAN NAMEK
                            _ITEMGOD.itemOptions.add(new ItemOption(0, (Util.nextInt(300, 551) * 10)));
                        } else if (_ITEMGOD.template.id == 566) { //GANG THAN XAYDA
                            _ITEMGOD.itemOptions.add(new ItemOption(0, (Util.nextInt(300, 601) * 10)));
                        } else if (_ITEMGOD.template.id == 563) { //GIAY THAN TD
                            _ITEMGOD.itemOptions.add(new ItemOption(7, (Util.nextInt(350, 551) * 100)));
                            _ITEMGOD.itemOptions.add(new ItemOption(28, Util.nextInt(9000, 10000)));
                        } else if (_ITEMGOD.template.id == 565) { //GIAY THAN NAMEK
                            _ITEMGOD.itemOptions.add(new ItemOption(7, (Util.nextInt(350, 601) * 100)));
                            _ITEMGOD.itemOptions.add(new ItemOption(28, Util.nextInt(9500, 10500)));
                        } else if (_ITEMGOD.template.id == 567) { //GIAY THAN XAYDA
                            _ITEMGOD.itemOptions.add(new ItemOption(7, (Util.nextInt(350, 501) * 100)));
                            _ITEMGOD.itemOptions.add(new ItemOption(28, Util.nextInt(9000, 9500)));
                        } else { //NHAN THAN LINH
                            _ITEMGOD.itemOptions.add(new ItemOption(14, Util.nextInt(3, 16)));
                        }
                        _ITEMGOD.itemOptions.add(new ItemOption(21, 17));
                        itemROI.item = _ITEMGOD;
                        itemDrop.add(itemROI);
                        itemsMap.addAll(itemDrop);
                    }
                } else if (perTA < 10 && _detu.checkFullSetThan() && map.MapCold()) {
                    int idThucAn[] = {663, 664, 665, 666, 667};
                    perTA = Util.nextInt(0, 5);
                    Item itemMap = ItemSell.getItemNotSell(idThucAn[perTA]);
                    ItemMap item = new ItemMap();
                    item.playerId = _player.id;
                    item.x = _mob.pointX;
                    item.y = _mob.pointY;
                    item.itemMapID = itemsMap.size();
                    item.itemTemplateID = (short) idThucAn[perTA];
                    itemMap.template = ItemTemplate.ItemTemplateID(idThucAn[perTA]);
                    item.item = itemMap;
                    itemDrop.add(item);
                    itemsMap.addAll(itemDrop);
                } else if (perTA < 50 && map.MapFarmThienSu() && _detu.checkSetByLevel((byte) 14)) {
                    int idThienSu[] = {1066, 1067};
                    perTA = Util.nextInt(0, 2);
                    Item itemMap = ItemSell.getItemNotSell(idThienSu[perTA]);
                    ItemMap item = new ItemMap();
                    item.playerId = _player.id;
                    item.x = _mob.pointX;
                    item.y = _mob.pointY;
                    item.itemMapID = itemsMap.size();
                    item.itemTemplateID = (short) idThienSu[perTA];
                    itemMap.template = ItemTemplate.ItemTemplateID(idThienSu[perTA]);
                    item.item = itemMap;
                    itemDrop.add(item);
                    itemsMap.addAll(itemDrop);
                } else if (percentDrop < 3) {
                    int id = Util.nextInt(0, 33);
                    Item itemMap = ItemSell.getItemNotSell(idItemNotSell[id]);
                    ItemMap item = new ItemMap();
                    item.playerId = _player.id;
                    item.x = _mob.pointX;
                    item.y = _mob.pointY;
//                    item.itemMapID = idItemNotSell[id];
//                    item.itemTemplateID = (short) item.itemMapID;
                    item.itemMapID = itemsMap.size();
                    item.itemTemplateID = (short) idItemNotSell[id];
                    itemMap.template = ItemTemplate.ItemTemplateID(idItemNotSell[id]);
                    item.item = itemMap;
                    itemDrop.add(item);
                    itemsMap.addAll(itemDrop);
                }
            }
            //đồng bộ mob chết
            MobStartDie(_player, damage, _mob, fatal, itemDrop);
        } else {
//            if(!_detu.isMonkey) {
            attachedMob(damage, _mob.tempId, fatal); //update hp mob
//            }
            if (_mob.template.tempId != 0 && (!_mob.isFreez) && (!_mob.isBlind) && (!_mob.isDCTT) && (!_mob.isSleep)) {
                loadMobAttachedPet(_mob.tempId, _player.id, damage);
            }
            //dame hut mau
            int dameHutHp = (int) (Util.getPercentDouble(_detu.getPercentHutHp()) * damage);
            _detu.hp += dameHutHp; //hutmau
            if (_detu.hp > _detu.getHpFull()) {
                _detu.hp = _detu.getHpFull();
            }
            _detu.updateHpDetu(_player, _detu);
            //dame hut mau
            //dame hut ki
            int dameHutKi = (int) (Util.getPercentDouble(_detu.getPercentHutKi()) * damage);
            _detu.mp += dameHutKi; //hutmau
            if (_detu.mp > _detu.getMpFull()) {
                _detu.mp = _detu.getMpFull();
            }
            //dame hut ki
        }

        //send detu attack to all player in map
        try {
            Message m = new Message(54);
            m.writer().writeInt(_detu.id);
            m.writer().writeByte(_skill.skillId);
            m.writer().writeByte(_mob.tempId);
            for (Player _p : players) {
                _p.session.sendMessage(m);
            }
            m.writer().flush();
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //get mob gan de tu nhat
    public Mob getMobPetNearest(Detu _detu) {
        Mob _mobNearest = null;
        short minSpace = 500;
        for (Mob mob : mobs) {
            if ((short) (Math.hypot(Math.abs(_detu.x - mob.pointX), Math.abs(_detu.y - mob.pointY))) < minSpace && !mob.isDie) {
//            if (Math.abs(_detu.x - mob.pointX) < minSpace && !mob.isDie) {
                _mobNearest = mob;
//                minSpace = (short)(Math.abs(_detu.x - mob.pointX));
                minSpace = (short) (Math.hypot(Math.abs(_detu.x - mob.pointX), Math.abs(_detu.y - mob.pointY)));
            }
        }
        return _mobNearest;
    }

    //send mob attack de tu
    public void loadMobAttachedPet(int mobid, int playerid, int damage) {
//        synchronized(this) {
        try {
            Mob mob = this.getMob(mobid);
            if (mob != null) {
                long tFight = System.currentTimeMillis() + 1200L;
                if (mob.isboss) {
                    tFight = System.currentTimeMillis() + 500L;
                }
                mob.timeFight = tFight;
//                    damage = (int)(mob.template.Level * mob.template.Level / 5);
                damage = (int) (mob.maxHp * 0.01);
                if (mob.isSocola != 0) {
                    damage = (int) (damage * (100 - mob.isSocola) / 100);
                }
//                    BuNhin buNhin;
                Player player;
                Detu detu;
                short i;
                int indexBuNhin;
                if (playerid != -1) {
                    player = this.getPlayerByID(playerid);
                    if (player != null && player.detu != null) {
                        detu = player.detu;
                        playerid = -1;
                        //CHECK KHIEN NANG LUONG
                        if (detu.isProtect) {
                            damage = 1;
                        }
                        //giap tru dame
                        damage = (int) (damage - detu.getDefFull()) > 1 ? (int) (damage - detu.getDefFull()) : 1;
                        //Check bua de tu, neu co thi giam 50% dame cho de tu
                        if (player.getBuaDetu()) {
                            damage = (int) (damage / 2) > 1 ? (int) (damage / 2) : 1;
                        }
                        //tru dame giap
                        detu.hp = (detu.hp - damage) >= 0 ? (detu.hp - damage) : 0;
                        //                        System.out.println("hp con lali cua de tu: " + detu.hp);
                        detu.isdie = (detu.hp > 0) ? false : true;
                        boolean isplayermonkey = detu.isMonkey;
                        //                        if(detu.isdie && detu.isMonkey) {
                        //                            if(detu.ItemBody[5] != null || detu.NhapThe == 1){
                        //                                Service.gI().LoadCaiTrang(detu, 1, detu.PartHead(), detu.PartHead() + 1, detu.PartHead() + 2);
                        //                            }
                        //                            else{
                        //                                Service.gI().LoadCaiTrang(detu, 1, detu.PartHead(), detu.PartBody(), detu.Leg());
                        //                            }
                        //                            detu.isMonkey = false;
                        ////                            Service.gI().loadPoint(player.session, player);
                        //                        }
                        for (Player p : players) {
                            //                            if(p.id == playerid) {
                            //                                this.MobAtkMeMessage(mob.tempId, p, damage, damage, (short)-1, (byte)-1, (byte)-1);
                            //                            } else {
                            if (detu.isdie && isplayermonkey) {
                                p.sendDefaultTransformToPlayer(detu);

                            }
                            this.MobAtkAnotherPlayerMessage(mob.tempId, p, detu, damage, damage, (short) -1, (byte) -1, (byte) -1);
                            //                            }
                        }
                        isplayermonkey = false;
                    }
                } else {
                    for (i = 0; i < this.players.size(); i++) {
                        if (this.players.get(i) != null) {
                            player = this.players.get(i);
                            if (!player.isdie) {
                                short dx = 80;
                                short dy = 2;
                                if (Math.abs(player.x - mob.pointX) < dx && Math.abs(player.y - mob.pointY) < dy) {
                                    this.MobAtkMessage(mob.tempId, player, damage, damage, (short) -1, (byte) -1, (byte) -1);
                                }

                            }
                        } else {
                            continue;
                        }
                    }
                }
            }
        } catch (Exception var18) {
            var18.printStackTrace();
            return;
        }

//        }
    }

    //UP POINT DETU VA OPEN SKILL DETU
    public void openSkillPetAndUpPoint(Detu _detu) {
        int rdSkill2 = 0;
        if (_detu.power >= 1500000 && _detu.isSoSinh) {
            _detu.isSoSinh = false; //xd, td , nm 303 304 305 part head de tu
            if (_detu.gender == 2) {
                _detu.head = 303;
            } else if (_detu.gender == 1) {
                _detu.head = 305;
            } else {
                _detu.head = 304;
            }
            Service.gI().LoadBodyDetuChange(players, 0, _detu.id, _detu.head, _detu.getDefaultBody(), _detu.getDefaultLeg());
        }
        if (_detu.power >= 150000000L && _detu.listSkill.get(1).skillId == -1) {
            rdSkill2 = Util.nextInt(0, 3);
            if (rdSkill2 == 0) {
                _detu.listSkill.get(1).skillId = 7; //id kame lv1
                _detu.listSkill.get(1).point = 1;
                _detu.listSkill.get(1).genderSkill = (byte) 0;
                _detu.listSkill.get(1).tempSkillId = 1;
            } else if (rdSkill2 == 1) {
                _detu.listSkill.get(1).skillId = 21; //id masenko lv1
                _detu.listSkill.get(1).point = 1;
                _detu.listSkill.get(1).genderSkill = (byte) 1;
                _detu.listSkill.get(1).tempSkillId = 3;
            } else if (rdSkill2 == 2) {
                _detu.listSkill.get(1).skillId = 35; //id atomic lv1
                _detu.listSkill.get(1).point = 1;
                _detu.listSkill.get(1).genderSkill = (byte) 2;
                _detu.listSkill.get(1).tempSkillId = 5;
            } else {
                _detu.listSkill.get(1).skillId = 21; //id masenko lv1
                _detu.listSkill.get(1).point = 1;
                _detu.listSkill.get(1).genderSkill = (byte) 1;
                _detu.listSkill.get(1).tempSkillId = 3;
            }
        }
        if (_detu.power >= 1500000000L && _detu.listSkill.get(2).skillId == -1) {
            rdSkill2 = Util.nextInt(0, 3);
            if (rdSkill2 == 0) {
                _detu.listSkill.get(2).skillId = 42; //id tdhs lv1
                _detu.listSkill.get(2).point = 1;
                _detu.listSkill.get(2).genderSkill = (byte) 0;
                _detu.listSkill.get(2).tempSkillId = 6;
            } else if (rdSkill2 == 1) {
                _detu.listSkill.get(2).skillId = 63; //id kaioken lv1
                _detu.listSkill.get(2).point = 1;
                _detu.listSkill.get(2).genderSkill = (byte) 0;
                _detu.listSkill.get(2).tempSkillId = 9;
            } else if (rdSkill2 == 2) {
                _detu.listSkill.get(2).skillId = 56; //id ttnl lv1
                _detu.listSkill.get(2).point = 1;
                _detu.listSkill.get(2).genderSkill = (byte) 2;
                _detu.listSkill.get(2).tempSkillId = 8;
            } else {
                _detu.listSkill.get(2).skillId = 63; //id kaioken lv1
                _detu.listSkill.get(2).point = 1;
                _detu.listSkill.get(2).genderSkill = (byte) 0;
                _detu.listSkill.get(2).tempSkillId = 9;
            }
        }
        if (_detu.power >= 20000000000L && _detu.listSkill.get(3).skillId == -1) {
            rdSkill2 = Util.nextInt(0, 3);
            if (rdSkill2 == 0) {
                _detu.listSkill.get(3).skillId = 91; //id bien khi lv1
                _detu.listSkill.get(3).point = 1;
                _detu.listSkill.get(3).genderSkill = (byte) 2;
                _detu.listSkill.get(3).tempSkillId = 13;
            } else if (rdSkill2 == 1) {
                _detu.listSkill.get(3).skillId = 121; //id khien nang luong lv1
                _detu.listSkill.get(3).point = 1;
                _detu.listSkill.get(3).genderSkill = (byte) 0;
                _detu.listSkill.get(3).tempSkillId = 19;
            } else {
                _detu.listSkill.get(3).skillId = 121; //id khien nang luong lv1
                _detu.listSkill.get(3).point = 1;
                _detu.listSkill.get(3).genderSkill = (byte) 0;
                _detu.listSkill.get(3).tempSkillId = 19;
            }
        }
    }

    public void pointUpForPet(Detu _detu) {
//        System.out.println("thoi gian hien tai: " + System.currentTimeMillis());
//        if((System.currentTimeMillis() - _detu.lastTimeUpPoint >= 30000)) { //30s up point 1 lan
//            _detu.lastTimeUpPoint = System.currentTimeMillis();
//            System.out.println("Tong tiem nang la: " + _detu.tiemNang);
        if (_detu.damGoc < _detu.getDamLimit()) { //cong suc danh cho de tu
            long tnUpSD = (long) (_detu.tiemNang * 0.3);
            int sdUp = (int) Math.floor(giaiPTBac2((long) 50, (long) (-50 * (1 - 2 * _detu.damGoc)), (long) (0 - tnUpSD)));
            if (sdUp > 0) {
                _detu.tiemNang -= (sdUp * (2 * _detu.damGoc + sdUp - 1) / 2 * 100);
                if ((_detu.damGoc + sdUp) < _detu.getDamLimit()) {
                    _detu.damGoc = (_detu.damGoc + sdUp);
                    _detu.damFull = _detu.damGoc;
                } else if ((_detu.damGoc + sdUp) >= _detu.getDamLimit() && _detu.damGoc < _detu.getDamLimit()) {
                    _detu.damGoc = (_detu.damGoc + sdUp) > _detu.getDamLimit() ? _detu.getDamLimit() : (_detu.damGoc + sdUp);
                    _detu.damFull = _detu.damGoc;
                    long tnUpSDUse = (long) (_detu.getDamLimit() - _detu.damGoc) * (long) (2 * _detu.damGoc + (_detu.getDamLimit() - _detu.damGoc) - 1) / 2 * 100;
//                        _detu.tiemNang += (tnUpSD - tnUpSDUse);
                }
            }
//                else {
//                    _detu.tiemNang += tnUpSD;
//                }
        }
        long tnUpHp = 0;
        long tnUpMp = 0;
        if (_detu.hpGoc > _detu.mpGoc) {
            tnUpHp = (long) (_detu.tiemNang * 0.6);
            tnUpMp = (long) (_detu.tiemNang * 0.4);
        } else {
            tnUpHp = (long) (_detu.tiemNang * 0.4);
            tnUpMp = (long) (_detu.tiemNang * 0.6);
        }
        if (_detu.hpGoc < _detu.getHpMpLimit()) { //cong hp cho de tu
//                int hpUp = (int)(Math.abs( _detu.hpGoc*_detu.hpGoc - Math.sqrt(_detu.hpGoc*_detu.hpGoc - 40*(990-tnUpHp)) ));
            int hpUp = (int) Math.floor(giaiPTBac2((long) 10, (long) _detu.hpGoc, (long) (990 - tnUpHp)));
//                System.out.println("hpUp: " + hpUp);
            if (hpUp > 0) {
                _detu.tiemNang -= (hpUp * (2 * (_detu.hpGoc + 1000) + hpUp * 20 - 20) / 2);
                if ((_detu.hpGoc + hpUp * 20) < _detu.getHpMpLimit()) {
                    _detu.hpGoc = (_detu.hpGoc + hpUp * 20);
                    _detu.hpFull = _detu.hpGoc;
                } else if ((_detu.hpGoc + hpUp * 20) >= _detu.getHpMpLimit() && _detu.hpGoc < _detu.getHpMpLimit()) {
                    _detu.hpGoc = (_detu.hpGoc + hpUp) > _detu.getHpMpLimit() ? _detu.getHpMpLimit() : (_detu.hpGoc + hpUp);
                    _detu.hpFull = _detu.hpGoc;
                    long tnUpHpUse = (long) (_detu.getHpMpLimit() - _detu.hpGoc) * (long) (2 * (_detu.hpGoc + 1000) + (_detu.getHpMpLimit() - _detu.hpGoc) - 20) / 40;
//                        _detu.tiemNang += (tnUpHp - tnUpHpUse);
                }
            }
//                else {
//                    _detu.tiemNang += tnUpHp;
//                }
        }
        if (_detu.mpGoc < _detu.getHpMpLimit()) { //cong mp cho de tu
            int mpUp = (int) Math.floor(giaiPTBac2((long) 10, (long) _detu.mpGoc, (long) (990 - tnUpMp)));
            if (mpUp > 0) {
                _detu.tiemNang -= (mpUp * (2 * (_detu.mpGoc + 1000) + mpUp * 20 - 20) / 2);
                if ((_detu.mpGoc + mpUp * 20) < _detu.getHpMpLimit()) {
                    _detu.mpGoc = (_detu.mpGoc + mpUp * 20);
                    _detu.mpFull = _detu.mpGoc;
                } else if ((_detu.mpGoc + mpUp * 20) >= _detu.getHpMpLimit() && _detu.mpGoc < _detu.getHpMpLimit()) {
                    _detu.mpGoc = (_detu.mpGoc + mpUp) > _detu.getHpMpLimit() ? _detu.getHpMpLimit() : (_detu.mpGoc + mpUp);
                    _detu.mpFull = _detu.mpGoc;
                    long tnUpMpUse = (long) (_detu.getHpMpLimit() - _detu.mpGoc) * (long) (2 * (_detu.mpGoc + 1000) + (_detu.getHpMpLimit() - _detu.mpGoc) - 20) / 40;
//                        _detu.tiemNang += (tnUpMp - tnUpMpUse);
                }
            }
//                else {
//                    _detu.tiemNang += tnUpMp;
//                }
        }
//            _detu.tiemNang = 0;

//            if (type == 3) {
//                tiemNangUse = 2 * (this.defGoc + 5) / 2 * 100000;
//                if ((this.defGoc + point) <= getDefLimit()) {
//                    if (useTiemNang(tiemNangUse)) {
//                        defGoc += point;
//                       defFull = defGoc;
//                    }
//                } else {
//                    Service.gI().serverMessage(this.session, "Vui lòng mở giới hạn sức mạnh");
//                    return;
//                }
    }

    public int giaiPTBac2(long a, long b, long c) {
        // tính delta
        long delta = b * b - 4 * a * c;
        long x1;
        long x2;
        // tính nghiệm
        if (delta > 0) {
            x1 = (long) ((-b + Math.sqrt(delta)) / (2 * a));
            x2 = (long) ((-b - Math.sqrt(delta)) / (2 * a));
//            System.out.println("Phương trình có 2 nghiệm là: "
//                    + "x1 = " + x1 + " và x2 = " + x2);
            return ((int) x1 > (int) x2 ? (int) x1 : (int) x2);
        } else if (delta == 0) {
            x1 = (long) (-b / (2 * a));
//            System.out.println("Phương trình có nghiệm kép: "
//                    + "x1 = x2 = " + x1);
            return (int) x1;
        } else {
            return 0;
        }
    }

    //BOSSSSSSSSSSSSSSSS BOSSSSSSSSSSSSSSSS
    //BOSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
    public Boss getBossByID(int id) {
        for (Boss boss : bossMap) {
            if (id == boss.id) {
                return boss;
            }
        }
        return null;
    }

    public Boss getBossByType(byte type) {
        for (Boss boss : bossMap) {
            if (type == boss._typeBoss) {
                return boss;
            }
        }
        return null;
    }

    public Detu getDetuByID(int id) {
        for (Detu pet : pets) {
            if (id == pet.id) {
                return pet;
            }
        }
        return null;
    }

    public void loadInfoBoss(Boss _boss) {
        Message msg = null;
        try {
            msg = new Message(-5);
            msg.writer().writeInt(_boss.id); //id
            if (_boss.clan != null) {
                msg.writer().writeInt(_boss.clan.id);
            } else {
                msg.writer().writeInt(-1); //id clan
            }
            msg.writer().writeByte(1); //level player viet 1 function get level
            msg.writer().writeBoolean(false);  //co dang vo hinh hay khong
            msg.writer().writeByte(_boss.typePk);  //type pk
            msg.writer().writeByte(_boss.gender); // get nClass teeamplate skill theo gender
            msg.writer().writeByte(_boss.gender); // get gender detu

            msg.writer().writeShort(_boss.BossPartHead()); // part head boss

            msg.writer().writeUTF(_boss.name);  // name de tu
            msg.writer().writeInt(_boss.hp); // hp de tu hien tai
//            msg.writer().writeInt(_boss.hpGoc); // hp full de tu
            msg.writer().writeInt(_boss.hpFull);
            if (_boss._typeBoss == 6) { // LOAD BLACK GOKU
                msg.writer().writeShort(_boss.BossPartHead() - 2);
                msg.writer().writeShort(_boss.BossPartHead() - 1);
            } else if (_boss._typeBoss >= (byte) 44 && _boss._typeBoss <= (byte) 47) {
                msg.writer().writeShort((short) 525);
                msg.writer().writeShort((short) 524);
            } else {
                msg.writer().writeShort(_boss.BossPartHead() + 1);
                msg.writer().writeShort(_boss.BossPartHead() + 2);
            }

            msg.writer().writeByte(8); // bag
            msg.writer().writeByte(-1); //b gui sang khong thay dung
            msg.writer().writeShort(_boss.x); // x 
            msg.writer().writeShort(_boss.y); // y
            msg.writer().writeShort(0); //  eff 5 buff hp
            msg.writer().writeShort(0); // eff 5 buff mp
            msg.writer().writeByte(0); // so luong eff char
//            msg.writer().writeByte(0); // eff template id
//            msg.writer().writeInt(0);  //time start
//            msg.writer().writeInt(0);  //time length
//            msg.writer().writeShort(-1); // param eff
            msg.writer().writeByte(0); // b76
            msg.writer().writeByte(0); //ismonkey
            msg.writer().writeShort(-1); // id can dau van, van bay ,....
            msg.writer().writeByte(0); // cflag
            msg.writer().writeByte(0); // is hop the?
            for (Player _p : players) {
                _p.session.sendMessage(msg);
            }
            msg.cleanup();
        } catch (Exception var2) {
            var2.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
            }
        }
    }

    public void loadBROLY(Boss _broly) {
        haveBROLY = true; //check xem map da co broly hay chua
        Message msg;
        try {
            loadInfoBoss(_broly); //tha broly theo server roi khong can ve cho all player trong map nua
            _broly.zone = this;
            // lap trinh broly tu dong
            Timer timerBroly = new Timer();
            TimerTask timeBroly = new TimerTask() {
                public void run() {
//                    System.out.println("Brolyyyyy");
                    if (_broly.isdie) {
                        timerBroly.cancel();
                        haveBROLY = false;
                        int numberBoss = _broly._numberBoss;
                        if (_broly._typeBoss == 1) {
                            int initSuper = 0;
                            if (_broly.hpFull >= 1500000) {
                                initSuper = 1;
                            }

                            // xoa broly khoi bossmap
//                                    if(bossMap.contains(_broly)) {
//                                        bossMap.remove(_broly);
//                                    }
//                            System.out.println("DIE ROI GOI DEN INIT SUPER");
                            if (initSuper == 1) {
                                Timer timerSPBroly = new Timer();
                                TimerTask ttSuper = new TimerTask() {
                                    public void run() {
//                                        Boss _superBroly = new Boss(1, (byte)2, _player);
//                                        bossMap.add(_superBroly);
//                                        loadBROLY(_player, _superBroly);
//
//                                        Detu _rDetu = new Detu();
//                                        _rDetu.initDetuBroly(_rDetu);
//                                        _rDetu.id = -300000;
//                                        _rDetu.x = (short)(_superBroly.x - 100);
//                                        _rDetu.y = (short)(_superBroly.y - 100);
//                                        _superBroly.detu = _rDetu;
//                                        pets.add(_rDetu);
//                                        for(Player _pz: players) {
//                                            loadInfoDeTu(_pz.session, _rDetu);
//                                        }
                                        int _rdZone = map.getIndexMapNoBroly(); //random khu khong co broly de tha super broly

                                        Boss _superBroly = new Boss(numberBoss, (byte) 2, (short) Util.getToaDoXBROLY(map.template.id), (short) Util.getToaDoYBROLY(map.template.id));
                                        map.area[_rdZone].bossMap.add(_superBroly);
                                        map.area[_rdZone].loadBROLY(_superBroly);

                                        Detu _rDetu = new Detu();
                                        _rDetu.initDetuBroly(_rDetu);
                                        _rDetu.id = -300000;
                                        _rDetu.x = (short) (_superBroly.x - 100);
                                        _rDetu.y = (short) (_superBroly.y - 100);
                                        _superBroly.detu = _rDetu;
                                        map.area[_rdZone].pets.add(_rDetu);
                                        for (Player _pz : map.area[_rdZone].players) {
                                            map.area[_rdZone].loadInfoDeTu(_pz.session, _rDetu);
                                        }
                                        Service.gI().sendThongBaoServer(_superBroly.name + " vừa xuất hiện tại " + map.template.name);
                                    }
                                ;
                                };
                                timerSPBroly.schedule(ttSuper, 60000);
//                                timerSPBroly.schedule(ttSuper, 15000);
                            } else {
                                Timer timerBrolyBack = new Timer();
                                TimerTask ttBrolyBack = new TimerTask() {
                                    public void run() {
                                        Boss _rBroly = new Boss(numberBoss, (byte) 1, (short) Util.getToaDoXBROLY(map.template.id), (short) Util.getToaDoYBROLY(map.template.id));
                                        bossMap.add(_rBroly);
                                        loadBROLY(_rBroly);
                                    }
                                ;
                                };
                                timerBrolyBack.schedule(ttBrolyBack, 300000);
                            }

                        } else if (_broly._typeBoss == 2) { //super broly chet
//                            if(bossMap.contains(_broly)) {
//                                bossMap.remove(_broly);
//                            }
//                            leaveDEEEEE(_broly.detu);
                            Timer timerBrolyBack = new Timer();
                            TimerTask ttBrolyBack = new TimerTask() {
                                public void run() {
                                    int idMap = Util.nextInt(0, 18);
                                    int _rdZone = Server.gI().maps[Server.gI().idMapBroly[idMap]].getIndexMapNoBroly();

                                    Boss _rBroly = new Boss(numberBoss, (byte) 1, (short) Util.getToaDoXBROLY(Server.gI().idMapBroly[idMap]), (short) Util.getToaDoYBROLY(Server.gI().idMapBroly[idMap]));
                                    Server.gI().maps[Server.gI().idMapBroly[idMap]].area[_rdZone].bossMap.add(_rBroly);
                                    Server.gI().maps[Server.gI().idMapBroly[idMap]].area[_rdZone].loadBROLY(_rBroly);
                                }
                            ;
                            };
                            timerBrolyBack.schedule(ttBrolyBack, 600000);
                        }
                        return;
                    } else {
                        Player _charTarget = getCharNearest(_broly.x, _broly.y, 150); //get char gan nhat
                        //                    if(_charTarget != null) {
                        //                        System.out.println("Brolyyyyy get char gan nhat: " + _charTarget.id);
                        //                    }
                        long timeNow = System.currentTimeMillis();
                        if ((timeNow - _broly.lastTimeUseChargeSkill) > 45000 && _broly.hp < _broly.hpFull && !_broly.isTTNL) {
                            _broly.lastTimeUseChargeSkill = timeNow;
                            _broly.isTTNL = true;
                            if (_broly.hp <= (int) (_broly.hpFull * 0.5) && _broly._typeBoss == 1) { //duoi 0.5 thi moi len hp
                                if (_broly.hp <= (int) (_broly.hpFull * 0.1)) {
                                    _broly.hpFull = (int) (_broly.hpFull * 1.6) > 2000000000 ? 2000000000 : (int) (_broly.hpFull * 1.6);
                                } else {
//                                    _broly.hpFull = (int)(_broly.hpFull*2);
                                    _broly.hpFull = (int) (_broly.hpFull * 1.4) > 2000000000 ? 2000000000 : (int) (_broly.hpFull * 1.4);
                                }
                                _broly.damGoc = (int) (_broly.hpFull * 0.1);
                                _broly.damFull = _broly.damGoc;
                            }
                            _broly.bossChargeHPMP(_broly, 1);
                            // creating timertask, timer
                            Timer timerPetTTNL = new Timer();
                            TimerTask petTTNL = new TimerTask() {
                                public void run() {
                                    //                                if(_broly.countCharge >= 10 || (_broly.hp >= _broly.hpFull)) {
                                    if ((_broly.hp >= _broly.hpFull) || _broly.isdie) {
                                        //                                    _broly.countCharge = 0;
                                        _broly.isTTNL = false;
                                        _broly.bossChargeHPMP(_broly, 3);
                                        timerPetTTNL.cancel();
                                    } else {
                                        _broly.bossChargeHPMP(_broly, 2);
                                    }
                                }
                            ;
                            };
                            timerPetTTNL.schedule(petTTNL, 0, 1000);
                        }

                        if (_charTarget != null && !_broly.isTTNL) {
                            //                        int rdAt = Util.nextInt(1,3);
                            //                        for(int i = 0; i < rdAt; i++) {
                            bossAttackChar(_broly, _charTarget);
                            //                        }
                        }
                        if (_charTarget != null) {
                            chat(_broly, "Tránh xa ra đừng để ta nổi giận!");
                        }
                    }
                }
            };
            timerBroly.schedule(timeBroly, 0, 2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //get char gan nhat trong map
    public Player getCharNearest(int xP, int yP, int minSpace) {
        Player _CharNearest = null;
//        short minSpace = 150;
        if(players != null && players.size() > 0){
            for (Player _p : players) {
                if ((int) (Math.hypot(Math.abs(xP - _p.x), Math.abs(yP - _p.y))) < minSpace && !_p.isdie) {
                    _CharNearest = _p;
                    minSpace = (int) (Math.hypot(Math.abs(xP - _p.x), Math.abs(yP - _p.y)));
                }
            }
            if (_CharNearest != null) {
                return _CharNearest;
            } else {
                return null;
            }
        }
        return  _CharNearest;
    }

    //check char in map
    public boolean charInMapCanAttack(int id, int xP, int yP, int minSpace) {
        for (Player _p : players) {
            if (_p != null && _p.id == id) {
                if ((int) (Math.hypot(Math.abs(xP - _p.x), Math.abs(yP - _p.y))) < minSpace && !_p.isdie) {
                    return true;
                }
            }
        }
        return false;
    }

    //RANDOM 1 CHAR TRONG MAP
    public Player getRandomCharInMap() {
        if (players.size() > 0) {
            int _index = Util.nextInt(0, players.size());
            if (players.get(_index) != null) {
                if (_index != -1 && !players.get(_index).isdie) {
                    return players.get(_index);
                }
            }
        }
        return null;
    }

    //HOA DA CHAR TRONG MAP
    public void charToStone(byte count, int space, int xP, int yP, byte typeCC) {//count 0: Hoa da All, 1
        if (players.size() > 0) {
            if (count == (byte) 0) {
                for (Player _p : players) {
                    if (_p != null && _p.session != null) {
                        if ((int) (Math.hypot(Math.abs(xP - _p.x), Math.abs(yP - _p.y))) <= space && !_p.isdie) {
                            //HOA DA CHAR TAI DAY
                            _p.isCharStone = true;
                            if (typeCC == (byte) 0) {
                                _p.sendAddchatYellow("Bạn đã bị hóa đá");
                                Service.gI().LoadCaiTrang(_p, 1, 454, 455, 456);
                                sendHoldBySkill((byte) 1, (byte) 0, (byte) 42, _p.id);
                                Timer timerEndStone = new Timer();
                                TimerTask endStone = new TimerTask() {
                                    public void run() {
                                        if (typeCC == (byte) 0) {
                                            _p.isCharStone = false;
                                            sendHoldBySkill((byte) 0, (byte) 0, (byte) 42, _p.id);
                                        }
                                        Service.gI().loadCaiTrangTemp(_p);
                                    }
                                ;
                                };
                                timerEndStone.schedule(endStone, 5000);
                            } else if (typeCC == (byte) 1) {
                                _p.socolaMabu = (byte) 1;
                                Service.gI().loadPoint(_p.session, _p);
                                _p.sendAddchatYellow("Bạn đã bị nguyền rủa");
                                Service.gI().LoadCaiTrang(_p, 1, 412, 413, 414);
                                if (_p.cPk == (byte) 10) {
                                    _p.cPk = (byte) 11;
                                    _p.detu.cPk = (byte) 11;
                                    Service.gI().changeFlagPK(_p, (byte) 11);
                                    if (_p.petfucus == 1) {
                                        Service.gI().changeFlagPKPet(_p, (byte) 11);
                                    }
                                }
                            }
//                            _p.timerStone = timerEndStone;
                        }
                    }
                }
            } else if (count > (byte) 0) {
                int _index = -1; //index random char
                for (byte i = 0; i < count; i++) {
                    _index = Util.nextInt(0, players.size());
                    if (_index != -1 && players.get(_index) != null && !players.get(_index).isCharStone && !players.get(_index).isdie) {
                        //HOA DA CHAR TAI DAY
                        Player _p = players.get(_index);
                        players.get(_index).isCharStone = true;
                        if (typeCC == (byte) 0) {
                            players.get(_index).sendAddchatYellow("Bạn đã bị hóa đá");
                            Service.gI().LoadCaiTrang(players.get(_index), 1, 454, 455, 456);
                            sendHoldBySkill((byte) 1, (byte) 0, (byte) 42, players.get(_index).id);
                            Timer timerEndStone = new Timer();
                            TimerTask endStone = new TimerTask() {
                                public void run() {
                                    if (typeCC == (byte) 0) {
                                        _p.isCharStone = false;
                                        sendHoldBySkill((byte) 0, (byte) 0, (byte) 42, _p.id);
                                    }
                                    Service.gI().loadCaiTrangTemp(_p);
                                }
                            ;
                            };
                            timerEndStone.schedule(endStone, 5000);
                        } else if (typeCC == (byte) 1) {
                            players.get(_index).socolaMabu = (byte) 1;
                            Service.gI().loadPoint(players.get(_index).session, players.get(_index));
                            players.get(_index).sendAddchatYellow("Bạn đã bị nguyền rủa");
                            Service.gI().LoadCaiTrang(players.get(_index), 1, 412, 413, 414);
                            if (players.get(_index).cPk == (byte) 10) {
                                players.get(_index).cPk = (byte) 11;
                                players.get(_index).detu.cPk = (byte) 11;
                                Service.gI().changeFlagPK(players.get(_index), (byte) 11);
                                if (players.get(_index).petfucus == 1) {
                                    Service.gI().changeFlagPKPet(players.get(_index), (byte) 11);
                                }
                            }
                        }
//                        players.get(_index).timerStone = timerEndStone;
                    }
                }
            }
        }
    }

    public void sendHoldBySkill(byte typead, byte typeTar, byte type, int idChar) {
        Message m = null;
        try {
            m = new Message(-124);
            m.writer().writeByte(typead);
            m.writer().writeByte(typeTar);
            m.writer().writeByte(type);
            m.writer().writeInt(idChar);
            m.writer().flush();
            for (Player p : players) {
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

    public void bossAttackChar(Boss _boss, Player _bTarget) {
        SkillData skilldata = new SkillData();
        int rdskill = Util.nextInt(2, 4); //ran dom dam demon va masenko
//        System.out.println("skill BROLY: " + rdskill);
        if (_boss._typeBoss == (byte) 5 || _boss._typeBoss == (byte) 6 || _boss._typeBoss == (byte) 28 || _boss._typeBoss == (byte) 30 || _boss._typeBoss >= (byte) 48) { //BLACK GOKU
            rdskill = Util.nextInt(0, 2); //random dam dragon va kamejoko
        }
        if ((_boss._typeBoss >= (byte) 15 && _boss._typeBoss <= (byte) 17) || (_boss._typeBoss == (byte) 18) || (_boss._typeBoss == (byte) 19)) { //FIDE
            rdskill = Util.nextInt(4, 6); //random dam galick va atomic
        }
        Skill skill2Boss = null;
        if (_boss._typeBoss == (byte) 5 || _boss._typeBoss == (byte) 6 || _boss._typeBoss == (byte) 28 || _boss._typeBoss == (byte) 30 || _boss._typeBoss >= (byte) 48) {
            skill2Boss = skilldata.getSkillBySkillTemplate((byte) 0, (short) rdskill, 7);
        } else if ((_boss._typeBoss >= (byte) 15 && _boss._typeBoss <= (byte) 17) || (_boss._typeBoss == (byte) 18) || (_boss._typeBoss == (byte) 19)) {
            skill2Boss = skilldata.getSkillBySkillTemplate((byte) 2, (short) rdskill, 7);
        } else {
            skill2Boss = skilldata.getSkillBySkillTemplate((byte) 1, (short) rdskill, 1);
        }
        int damage = _boss.damFull;
        int rdAt = Util.nextInt(1, 4);
        if (_boss._typeBoss == (byte) 29 || (_boss._typeBoss >= (byte) 36 && _boss._typeBoss <= (byte) 39) || (_boss._typeBoss >= (byte) 44 && _boss._typeBoss <= (byte) 47)) {
            rdAt = 1;
        }
        for (int i = 0; i < rdAt; i++) {
            //CHECK SET NE NEU CO
            int noMiss = Util.nextInt(1, 11);
            if (noMiss <= _bTarget.getPercentNedon()) {
//                Util.log("TI LE NE DON: " + _bTarget.getPercentNedon());
                damage = 0;
            } else {
                damage = _boss.damFull;
                if (_boss._typeBoss >= (byte) 44 && _boss._typeBoss <= (byte) 47 && _bTarget.getHpFull() <= 10000) {
                    damage = (int) (_bTarget.getHpFull() / 100);
                }
            }
            if (!_boss.isCharFreez && !_boss.isCharSleep && !_boss.isCharDCTT && !_boss.isCharBlind) {
                if (_boss._typeBoss < (byte) 44 || _boss._typeBoss > (byte) 47) { //BOSS KHAC YARDRAT
                    if (rdskill == 2 || rdskill == 0 || rdskill == 4) { //dich chuyen tuc thoi va dam
                        _boss.x = _bTarget.x;
                        _boss.y = _bTarget.y;
                        bossMove(_boss);
                    }
                }
                int fantashi = Util.nextInt(0, 100);
                boolean fatal = 20 >= fantashi;
                if (fatal) {
                    damage = damage * 2;
                }
                //CHECK GIAP XEN
                if (_bTarget.useGiapXen) {
                    damage = (int) (damage / 2);
                }
                //CHECK KHIEN NANG LUONG THI BOSS DAM -1 hp
                if (_bTarget.isProtect) {
                    damage = 1;
                }
                _bTarget.hp -= damage;
                if (_bTarget.hp <= 0) {
                    _bTarget.isdie = true;
                    _bTarget.hp = 0;
                } else {
                    //PHAN SAT THUONG
                    int damePST = _bTarget.getPercentPST();
                    if (damePST > 0) {
                        damePST = (int) (damage * Util.getPercentDouble(damePST));
                        _boss.hp = (_boss.hp - damePST) < 1 ? 1 : (_boss.hp - damePST);
                        //send dame
                        dameChar(_boss.id, _boss.hp, damePST, false);
                    }
                }
                if (_bTarget.isdie) {
                    for (Player _pll : players) {
                        if (_pll.id == _bTarget.id) {
                            if (_bTarget.isMonkey) {
                                Service.gI().loadCaiTrangTemp(_bTarget);
                                _bTarget.isMonkey = false;
                                //NOI TAI TANG DAME KHI HOA KHI
                                if (_bTarget.upDameAfterKhi && _bTarget.noiTai.id != 0 && _bTarget.noiTai.idSkill == (byte) 13) {
                                    _bTarget.upDameAfterKhi = false;
                                }
                                //NOI TAI TANG DAME KHI HOA KHI
                                Service.gI().loadPoint(_bTarget.session, _bTarget);
                            }
                            sendDieToMe(_bTarget);
                        } else {
                            _pll.sendDefaultTransformToPlayer(_bTarget);
                            sendDieToAnotherPlayer(_pll, _bTarget);
                        }
                    }
                } else {
                    dameChar(_bTarget.id, _bTarget.hp, damage, fatal);

                }

                //send player attack to all player in map
                //            short _idSKILL = skillPlayerUse.skillId;
                //            if(p.isMonkey && (p.idSkillselect == 4)) {
                //                _idSKILL = 105;
                //            } else if(p.isMonkey && (p.idSkillselect == 5)) {
                //                _idSKILL = 106;
                //            }
                attachedChar(_boss.id, _bTarget.id, skill2Boss.skillId);
            }
        }
    }

    public void bossMove(Boss _boss) { //boss dich chuyen tuc thoi
        Message msg;
        try {
            for (Player player : players) {
                msg = new Message(-7);
                msg.writer().writeInt(_boss.id);
                msg.writer().writeShort(_boss.x);
                msg.writer().writeShort(_boss.y);
                player.session.sendMessage(msg);
                msg.cleanup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetTROIKHIMOVE(Player p) {
        Message m = null;
        p.isTroi = false;
        if (p.MOBHOLD != null) {
            p.MOBHOLD.isFreez = false;
            //remove setting troi, va effect tren nguoi mob
            for (Player pl : players) {
                try {
                    m = new Message(-124);
                    m.writer().writeByte(2);
                    m.writer().writeByte(1);
                    m.writer().writeByte(32);
                    m.writer().writeByte(p.MOBHOLD.tempId);
                    m.writer().flush();
                    pl.session.sendMessage(m);
                    m.cleanup();
                } catch (Exception var2) {
                    var2.printStackTrace();
                } finally {
                    if (m != null) {
                        m.cleanup();
                    }
                }

                //remove effect troi cua player
                try {
                    m = new Message(-124);
                    m.writer().writeByte(2);
                    m.writer().writeByte(0);
                    m.writer().writeInt(p.id);
                    m.writer().flush();
                    pl.session.sendMessage(m);
                    m.cleanup();
                } catch (Exception var2) {
                    var2.printStackTrace();
                } finally {
                    if (m != null) {
                        m.cleanup();
                    }
                }
            }
        } else if (p.CHARHOLD != null) {
            p.CHARHOLD.isCharFreez = false;
            //remove setting troi, va effect tren nguoi mob
            for (Player pl : players) {
                try {
                    m = new Message(-124);
                    m.writer().writeByte(0); //b5
                    m.writer().writeByte(0); //b6
                    m.writer().writeByte(32);
                    m.writer().writeInt(p.CHARHOLD.id);
                    m.writer().flush();
                    pl.session.sendMessage(m);
                    m.cleanup();
                } catch (Exception var2) {
                    var2.printStackTrace();
                } finally {
                    if (m != null) {
                        m.cleanup();
                    }
                }

                //remove effect troi cua player
                try {
                    m = new Message(-124);
                    m.writer().writeByte(2);
                    m.writer().writeByte(0);
                    m.writer().writeInt(p.id);
                    m.writer().flush();
                    pl.session.sendMessage(m);
                    m.cleanup();
                } catch (Exception var2) {
                    var2.printStackTrace();
                } finally {
                    if (m != null) {
                        m.cleanup();
                    }
                }
            }
        } else if (p.DETUHOLD != null) {
            p.DETUHOLD.isCharFreez = false;
            //remove setting troi, va effect tren nguoi mob
            for (Player pl : players) {
                try {
                    m = new Message(-124);
                    m.writer().writeByte(0); //b5
                    m.writer().writeByte(0); //b6
                    m.writer().writeByte(32);
                    m.writer().writeInt(p.DETUHOLD.id);
                    m.writer().flush();
                    pl.session.sendMessage(m);
                    m.cleanup();
                } catch (Exception var2) {
                    var2.printStackTrace();
                } finally {
                    if (m != null) {
                        m.cleanup();
                    }
                }

                //remove effect troi cua player
                try {
                    m = new Message(-124);
                    m.writer().writeByte(2);
                    m.writer().writeByte(0);
                    m.writer().writeInt(p.id);
                    m.writer().flush();
                    pl.session.sendMessage(m);
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
    }

    public void charMoveTDLT(Player p) { //player dich chuyen tuc thoi
        Message m;
        try {
            m = new Message(123);
            m.writer().writeInt(p.id);
            m.writer().writeShort(p.x);
            m.writer().writeShort(p.y);
            m.writer().writeByte(1);
            m.writer().flush();
            for (Player pl : players) {
                pl.session.sendMessage(m);
            }
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //LOAD BOSS COOLER
    public void loadBossNoPet(Boss _BOSS) {
        Message msg;
        try {
            loadInfoBoss(_BOSS); //tha broly theo server roi khong can ve cho all player trong map nua
            _BOSS.zone = this;
            if (_BOSS._typeBoss < (byte) 7) {
                Service.gI().sendThongBaoServer("BOSS " + _BOSS.name + " vừa xuất hiện tại " + map.template.name);
            }
            // lap trinh boss tu dong
            long timeTuDong = 2000;
            if ((_BOSS._typeBoss >= (byte) 15 && _BOSS._typeBoss <= (byte) 17) || (_BOSS._typeBoss >= (byte) 18 && _BOSS._typeBoss != (byte) 29 && _BOSS._typeBoss <= (byte) 30)) {
                Service.gI().sendThongBaoServer("BOSS " + _BOSS.name + " vừa xuất hiện tại " + map.template.name );
                timeTuDong = 1000;
            } else if ((_BOSS._typeBoss >= (byte) 31 && _BOSS._typeBoss <= (byte) 41) || _BOSS._typeBoss >= (byte) 48) {
                timeTuDong = 1000;
            }
            Timer timerBOSS = new Timer();
            TimerTask timeBOSS = new TimerTask() {
                public void run() {
                    if (_BOSS.isdie && !Server.gI().openMabu && _BOSS._typeBoss >= (byte) 36 && _BOSS._typeBoss <= (byte) 39) {//BOSS MABU 12h
                        timerBOSS.cancel();
                        bossMap.remove(_BOSS);
                        return;
                    }
                    if (_BOSS.isdie) {
//                        timerBOSS.cancel();
                        if (_BOSS.id > -201000) {
                            short xDIE = _BOSS.x;
                            short yDIE = _BOSS.y;
                            //                        haveBROLY = false;
                            if (_BOSS._typeBoss == 3) { //COOLER 1 DIE
                                Timer timerCoolerBack = new Timer();
                                TimerTask ttCoolerBack = new TimerTask() {
                                    public void run() {
                                        Boss _rCooler = new Boss(101, (byte) 4, xDIE, yDIE);
                                        bossMap.add(_rCooler);
                                        loadBossNoPet(_rCooler);
                                    }
                                ;
                                };
                                timerCoolerBack.schedule(ttCoolerBack, 15000);
                            } else if (_BOSS._typeBoss == 4) { //COOLER 2 DIE
                                Timer timerCoolerXuatHien = new Timer();
                                TimerTask ttCoolerXuatHien = new TimerTask() {
                                    public void run() {
                                        int IDZONE = Util.nextInt(0, map.area.length);
                                        Boss _rCooler = new Boss(101, (byte) 3, (short) 243, (short) 168);

                                        map.area[IDZONE].bossMap.add(_rCooler);
                                        map.area[IDZONE].loadBossNoPet(_rCooler);
                                    }
                                ;
                                };
                                timerCoolerXuatHien.schedule(ttCoolerXuatHien, 1200000);
                            } else if (_BOSS._typeBoss == 5) { // BLACK GOKU DIE
                                Timer timerBLACKBack = new Timer();
                                TimerTask ttBLACKBack = new TimerTask() {
                                    public void run() {
                                        Boss _rBlack = new Boss(102, (byte) 6, xDIE, yDIE);
                                        bossMap.add(_rBlack);
                                        loadBossNoPet(_rBlack);
                                    }
                                ;
                                };
                                timerBLACKBack.schedule(ttBLACKBack, 15000);
                            } else if (_BOSS._typeBoss == 6) { // SUPER BLACK GOKU DIE
                                Timer timerBLACKXuatHien = new Timer();
                                TimerTask ttBLACKXuatHien = new TimerTask() {
                                    public void run() {
                                        int idMap = Util.nextInt(91, 93); //index 91 la map 92, index 92 la map 93
                                        int IDZONE = Util.nextInt(0, map.area.length);
                                        short xBlack = 228;
                                        if (idMap == 91) {
                                            xBlack = 1296;
                                        }
                                        Boss _rBlack = new Boss(102, (byte) 5, xBlack, (short) 360);
                                        Server.gI().maps[idMap].area[IDZONE].bossMap.add(_rBlack);
                                        Server.gI().maps[idMap].area[IDZONE].loadBossNoPet(_rBlack);
                                    }
                                ;
                                };
                                timerBLACKXuatHien.schedule(ttBLACKXuatHien, 1200000);
                            } else if (_BOSS._typeBoss == 7) { //KUKU DIE
                                Timer timerKUKU = new Timer();
                                TimerTask ttKUKU = new TimerTask() {
                                    public void run() {
                                        Service.gI().initKuKu();
                                        timerKUKU.cancel();
                                    }
                                ;
                                };
                                timerKUKU.schedule(ttKUKU, 600000);
                            } else if (_BOSS._typeBoss == (byte) 8) { //MAP DAU DINH DIE
                                Timer timerMAPDINH = new Timer();
                                TimerTask ttMAPDINH = new TimerTask() {
                                    public void run() {
                                        Service.gI().initMAPDAUDINH();
                                        timerMAPDINH.cancel();
                                    }
                                ;
                                };
                                timerMAPDINH.schedule(ttMAPDINH, 600000);
                            } else if (_BOSS._typeBoss == (byte) 9) { //RAMBO DIE
                                Timer timerRAMBO = new Timer();
                                TimerTask ttRAMBO = new TimerTask() {
                                    public void run() {
                                        Service.gI().initRAMBO();
                                        timerRAMBO.cancel();
                                    }
                                ;
                                };
                                timerRAMBO.schedule(ttRAMBO, 600000);
                            } else if (_BOSS._typeBoss == (byte) 10) { //SO 4 DIE
                                //REMOVE SO 3 VA INIT LAI
                                //                            Boss _So3 = getBossByID(-200126);
                                Boss _So3 = getBossByID(-200150);
                                leaveBoss(_So3);
                                _So3 = new Boss(126, (byte) 11, xDIE, yDIE);
                                _So3.typePk = (byte) 5;
                                bossMap.add(_So3);
                                loadBossNoPet(_So3);
                            } else if (_BOSS._typeBoss == (byte) 11) { //SO 3 DIE
                                //REMOVE SO 1 VA INIT LAI
                                //                            Boss _So1 = getBossByID(-200128);
                                Boss _So1 = getBossByID((int) (-200152));
                                leaveBoss(_So1);
                                _So1 = new Boss(128, (byte) 13, xDIE, yDIE);
                                _So1.typePk = (byte) 5;
                                bossMap.add(_So1);
                                loadBossNoPet(_So1);
                            } else if (_BOSS._typeBoss == (byte) 13) { //SO 1 DIE
                                //REMOVE SO 2 VA INIT LAI
                                //                            Boss _So2 = getBossByID(-200127);
                                Boss _So2 = getBossByID((int) (-200151));
                                leaveBoss(_So2);
                                _So2 = new Boss(127, (byte) 12, xDIE, yDIE);
                                _So2.typePk = (byte) 5;
                                bossMap.add(_So2);
                                loadBossNoPet(_So2);
                            } else if (_BOSS._typeBoss == (byte) 12) { //SO 2 DIE
                                //REMOVE TIEU DOI TRUONG VA INIT LAI
                                //                            Boss _So0 = getBossByID(-200129);
                                Boss _So0 = getBossByID((int) (-200153));
                                leaveBoss(_So0);
                                _So0 = new Boss(129, (byte) 14, xDIE, yDIE);
                                _So0.typePk = (byte) 5;
                                bossMap.add(_So0);
                                loadBossNoPet(_So0);
                            } else if (_BOSS._typeBoss == (byte) 14) { //TIEU DOI TRUONG DIE
                                Timer timerTDST = new Timer();
                                TimerTask ttTDST = new TimerTask() {
                                    public void run() {
                                        Service.gI().initTDST();
                                        timerTDST.cancel();
                                    }
                                ;
                                };
                                timerTDST.schedule(ttTDST, 600000);
                            } else if (_BOSS._typeBoss == (byte) 15) { //FIDE 1 DIE
                                Timer timerFide = new Timer();
                                TimerTask ttFide = new TimerTask() {
                                    public void run() {
                                        Boss _rFide = new Boss(131, (byte) 16, xDIE, yDIE);
                                        bossMap.add(_rFide);
                                        loadBossNoPet(_rFide);
                                        timerFide.cancel();
                                    }
                                ;
                                };
                                timerFide.schedule(ttFide, 13000);
                            } else if (_BOSS._typeBoss == (byte) 16) { //FIDE 2 DIE
                                Timer timerFide = new Timer();
                                TimerTask ttFide = new TimerTask() {
                                    public void run() {
                                        Boss _rFide = new Boss(132, (byte) 17, xDIE, yDIE);
                                        bossMap.add(_rFide);
                                        loadBossNoPet(_rFide);
                                        timerFide.cancel();
                                    }
                                ;
                                };
                                timerFide.schedule(ttFide, 13000);
                            } else if (_BOSS._typeBoss == (byte) 17) { //FIDE 3 DIE
                                Timer timerFide = new Timer();
                                TimerTask ttFide = new TimerTask() {
                                    public void run() {
                                        int IDZONE = Util.nextInt(0, Server.gI().maps[79].area.length);
                                        Boss _rFide = new Boss(130, (byte) 15, (short) 224, (short) 192);
                                        Server.gI().maps[79].area[IDZONE].bossMap.add(_rFide);
                                        Server.gI().maps[79].area[IDZONE].loadBossNoPet(_rFide);
                                        //                                    Util.log("INIT _rFide XONG MAP " + Server.gI().maps[79].template.name + ", " + IDZONE);
                                        timerFide.cancel();
                                    }
                                ;
                                };
                                timerFide.schedule(ttFide, 600000);
                            } else if ((_BOSS._typeBoss == (byte) 18 || _BOSS._typeBoss == (byte) 19) && !isHoiSinhA19) {
                                //                        } else if(((_BOSS._typeBoss == (byte)18 && getBossByID(-200136) == null) || (_BOSS._typeBoss == (byte)19 && getBossByID(-200135) == null)) && !isHoiSinhA19) {
                                Boss B19 = getBossByID(-200135);
                                Boss B20 = getBossByID(-200136);
                                if ((_BOSS._typeBoss == (byte) 18 && B20 == null) || (_BOSS._typeBoss == (byte) 18 && B20 != null && B20.isdie)
                                        || (_BOSS._typeBoss == (byte) 19 && B19 == null) || (_BOSS._typeBoss == (byte) 19 && B19 != null && B19.isdie)) {
                                    isHoiSinhA19 = true;
                                    Timer timerA1920 = new Timer();
                                    TimerTask ttA1920 = new TimerTask() {
                                        public void run() {
                                            Service.gI().initAndroid1920();
                                            isHoiSinhA19 = false;
                                            timerA1920.cancel();
                                        }
                                    ;
                                    };
                                    timerA1920.schedule(ttA1920, 600000);
                                }
                            } else if ((_BOSS._typeBoss == (byte) 20 || _BOSS._typeBoss == (byte) 21 || _BOSS._typeBoss == (byte) 22) && !isHoiSinhA15) {
                                //                        } else if(((_BOSS._typeBoss == (byte)20 && getBossByID(-200138) == null && getBossByID(-200139) == null) ||
                                //                            (_BOSS._typeBoss == (byte)21 && getBossByID(-200137) == null && getBossByID(-200139) == null) ||
                                //                            (_BOSS._typeBoss == (byte)22 && getBossByID(-200137) == null && getBossByID(-200138) == null)) && !isHoiSinhA15) {
                                Boss B15 = getBossByID(-200137);
                                Boss B14 = getBossByID(-200138);
                                Boss B13 = getBossByID(-200139);
                                if ((_BOSS._typeBoss == (byte) 20 && B14 == null && B13 == null) || (_BOSS._typeBoss == (byte) 20 && B14 != null && B14.isdie && B13 == null)
                                        || (_BOSS._typeBoss == (byte) 20 && B14 == null && B13 != null && B13.isdie) || (_BOSS._typeBoss == (byte) 20 && B14 != null && B13 != null && B13.isdie && B14.isdie)
                                        || (_BOSS._typeBoss == (byte) 21 && B15 == null && B13 == null) || (_BOSS._typeBoss == (byte) 21 && B15 != null && B15.isdie && B13 == null)
                                        || (_BOSS._typeBoss == (byte) 21 && B15 == null && B13 != null && B13.isdie) || (_BOSS._typeBoss == (byte) 21 && B15 != null && B13 != null && B13.isdie && B15.isdie)
                                        || (_BOSS._typeBoss == (byte) 22 && B14 == null && B15 == null) || (_BOSS._typeBoss == (byte) 22 && B15 != null && B15.isdie && B14 == null)
                                        || (_BOSS._typeBoss == (byte) 22 && B15 == null && B14 != null && B14.isdie) || (_BOSS._typeBoss == (byte) 23 && B15 != null && B14 != null && B14.isdie && B15.isdie)) {
                                    isHoiSinhA15 = true;
                                    Timer timerA1920 = new Timer();
                                    TimerTask ttA1920 = new TimerTask() {
                                        public void run() {
                                            Service.gI().initAndroid15();
                                            isHoiSinhA15 = false;
                                            timerA1920.cancel();
                                        }
                                    ;
                                    };
                                    timerA1920.schedule(ttA1920, 600000);
                                }
                            } else if (_BOSS._typeBoss == (byte) 23) { //POC
                                //REMOVE PIC VA INIT LAI
                                //                            Boss _PIC = getBossByID(-200141);
                                Boss _PIC = getBossByID((int) (-200154));
                                leaveBoss(_PIC);
                                _PIC = new Boss(141, (byte) 24, xDIE, yDIE);
                                _PIC.typePk = (byte) 5;
                                bossMap.add(_PIC);
                                loadBossNoPet(_PIC);
                            } else if (_BOSS._typeBoss == (byte) 24) { //PIC
                                //REMOVE KING KONG VA INIT LAI
                                //                            Boss _KK = getBossByID(-200142);
                                Boss _KK = getBossByID((int) (-200155));
                                leaveBoss(_KK);
                                _KK = new Boss(142, (byte) 25, xDIE, yDIE);
                                _KK.typePk = (byte) 5;
                                bossMap.add(_KK);
                                loadBossNoPet(_KK);
                            } else if (_BOSS._typeBoss == (byte) 25) { //KING KONG
                                Timer timerPicPoc = new Timer();
                                TimerTask ttPicPoc = new TimerTask() {
                                    public void run() {
                                        Service.gI().initPicPoc();
                                        timerPicPoc.cancel();
                                    }
                                ;
                                };
                                timerPicPoc.schedule(ttPicPoc, 600000);
                            } else if (_BOSS._typeBoss == (byte) 26) { // Xen 1 DIE
                                Timer timerXen1 = new Timer();
                                TimerTask ttXen1 = new TimerTask() {
                                    public void run() {
                                        Boss _Xen2 = new Boss(144, (byte) 27, xDIE, yDIE);
                                        bossMap.add(_Xen2);
                                        loadBossNoPet(_Xen2);
                                        timerXen1.cancel();
                                    }
                                ;
                                };
                                timerXen1.schedule(ttXen1, 15000);
                            } else if (_BOSS._typeBoss == (byte) 27) { // Xen 2 DIE
                                Timer timerXen2 = new Timer();
                                TimerTask ttXen2 = new TimerTask() {
                                    public void run() {
                                        Boss _XenHT = new Boss(145, (byte) 28, xDIE, yDIE);
                                        bossMap.add(_XenHT);
                                        loadBossNoPet(_XenHT);
                                        timerXen2.cancel();
                                    }
                                ;
                                };
                                timerXen2.schedule(ttXen2, 15000);
                            } else if (_BOSS._typeBoss == (byte) 28) { // XEN HOAN THIEN DIE
                                if (_BOSS.id == -200146) {
                                    Timer timerSBH = new Timer();
                                    TimerTask ttSBH = new TimerTask() {
                                        public void run() {
                                            Boss _SBH = new Boss(147, (byte) 30, xDIE, yDIE);
                                            bossMap.add(_SBH);
                                            loadBossNoPet(_SBH);
                                            timerSBH.cancel();
                                        }
                                    ;
                                    };
                                    timerSBH.schedule(ttSBH, 15000);
                                } else {
                                    Timer timerXenGinder = new Timer();
                                    TimerTask ttXenGinder = new TimerTask() {
                                        public void run() {
                                            Service.gI().initXenGinder();
                                            timerXenGinder.cancel();
                                        }
                                    ;
                                    };
                                    timerXenGinder.schedule(ttXenGinder, 600000);
                                }
                            } else if (_BOSS._typeBoss == (byte) 30) {
                                Timer timerSBH2 = new Timer();
                                TimerTask ttSBH2 = new TimerTask() {
                                    public void run() {
                                        Service.gI().initXenVoDai();
                                        timerSBH2.cancel();
                                        //                                    Boss _SBH2 = new Boss(147, (byte)30, xDIE, yDIE);
                                        //                                    bossMap.add(_SBH2);
                                        //                                    loadBossNoPet(_SBH2);
                                    }
                                ;
                                };
                                timerSBH2.schedule(ttSBH2, 1200000);
                            } else if (_BOSS._typeBoss == (byte) 36) { //DRABULA
                                int idDIE = Math.abs(_BOSS.id + 200000);
                                Timer timerDra = new Timer();
                                TimerTask ttDra = new TimerTask() {
                                    public void run() {
                                        Boss _Dra = new Boss(idDIE, (byte) 36, xDIE, yDIE);
                                        bossMap.add(_Dra);
                                        loadBossNoPet(_Dra);
                                        timerDra.cancel();
                                    }
                                ;
                                };
                                timerDra.schedule(ttDra, 120000);
                            } else if (_BOSS._typeBoss == (byte) 37) { //BUI BUI
                                int idDIE = Math.abs(_BOSS.id + 200000);
                                Timer timerBui = new Timer();
                                TimerTask ttBui = new TimerTask() {
                                    public void run() {
                                        Boss _Bui = new Boss(idDIE, (byte) 37, xDIE, yDIE);
                                        bossMap.add(_Bui);
                                        loadBossNoPet(_Bui);
                                        timerBui.cancel();
                                    }
                                ;
                                };
                                timerBui.schedule(ttBui, 120000);
                            } else if (_BOSS._typeBoss == (byte) 38) { //YACON
                                int idDIE = Math.abs(_BOSS.id + 200000);
                                Timer timerYcon = new Timer();
                                TimerTask ttYcon = new TimerTask() {
                                    public void run() {
                                        Boss _Ycon = new Boss(idDIE, (byte) 38, xDIE, yDIE);
                                        bossMap.add(_Ycon);
                                        loadBossNoPet(_Ycon);
                                        timerYcon.cancel();
                                    }
                                ;
                                };
                                timerYcon.schedule(ttYcon, 120000);
                            } else if (_BOSS._typeBoss == (byte) 39) { //MABU
                                int idDIE = Math.abs(_BOSS.id + 200000);
                                //                            timeMabu12 = System.currentTimeMillis() + 101000;                            
                                Timer timerMabu = new Timer();
                                TimerTask ttMabu = new TimerTask() {
                                    public void run() {
                                        //                                    if((System.currentTimeMillis() - timeMabu12) >= 1000) {
                                        //                                        Service.gI().setTrungMabuPoint(players, (byte)101);
                                        Boss _Mabu = new Boss(idDIE, (byte) 39, xDIE, yDIE);
                                        bossMap.add(_Mabu);
                                        loadBossNoPet(_Mabu);
                                        timerMabu.cancel();
                                        //                                    } else {
                                        //                                        Service.gI().setTrungMabuPoint(players, (byte)(101 - (int)((System.currentTimeMillis() - timeMabu12)/1000)));
                                        //                                    }
                                    }
                                ;
                                };
    //                            timerMabu.schedule(ttMabu, 0, 1000);
                                timerMabu.schedule(ttMabu, 300000);
                            } else if (_BOSS._typeBoss == (byte) 40) { // CHILLED DIE
                                Timer timerChill = new Timer();
                                TimerTask ttChill = new TimerTask() {
                                    public void run() {
                                        Boss _rChill2 = new Boss(501, (byte) 41, xDIE, yDIE);
                                        bossMap.add(_rChill2);
                                        loadBossNoPet(_rChill2);
                                        timerChill.cancel();
                                    }
                                ;
                                };
                                timerChill.schedule(ttChill, 15000);
                            } else if (_BOSS._typeBoss == (byte) 41) { //CHILLED 2 DIE
                                Timer timerChill2 = new Timer();
                                TimerTask ttChill2 = new TimerTask() {
                                    public void run() {
                                        Service.gI().initChilled();
                                        timerChill2.cancel();
                                    }
                                ;
                                };
                                timerChill2.schedule(ttChill2, 600000);
                            } else if (_BOSS._typeBoss == (byte) 42) { // LYCHEE DIE
                                int lvG = _BOSS.lvGas;
                                int idC = _BOSS.idClan;
                                Timer timerLychee = new Timer();
                                TimerTask ttLychee = new TimerTask() {
                                    public void run() {
                                        Boss _Hita = new Boss(503, (byte) 43, xDIE, yDIE);
                                        _Hita.lvGas = lvG;
                                        _Hita.hpGoc = _Hita.hpGoc * _Hita.lvGas;
                                        _Hita.hpFull = _Hita.hpGoc;
                                        _Hita.hp = _Hita.hpGoc;
                                        _Hita.damFull = _Hita.damFull * _Hita.lvGas;
                                        _Hita.idClan = idC;
                                        bossMap.add(_Hita);
                                        loadBossNoPet(_Hita);
                                        timerLychee.cancel();
                                    }
                                ;
                                };
                                timerLychee.schedule(ttLychee, 15000);
                                //ROT CAI TRANG
                                ItemSell lychee = ItemSell.getItemSell(738, (byte) 1);
                                Item itemMap = new Item(lychee.item);
                                itemMap.itemOptions.clear();
                                int maxP = (int) Math.ceil(36 * lvG / 110);
                                maxP = maxP > 3 && maxP <= 36 ? maxP : 3;
                                itemMap.itemOptions.add(new ItemOption(50, Util.nextInt(1, maxP)));
                                itemMap.itemOptions.add(new ItemOption(77, Util.nextInt(1, maxP)));
                                itemMap.itemOptions.add(new ItemOption(103, Util.nextInt(1, maxP)));
                                itemMap.itemOptions.add(new ItemOption(5, Util.nextInt(1, 16)));
                                itemMap.itemOptions.add(new ItemOption(30, 0)); //KHONG THE GIAO DICH
                                int dayEx = Util.nextInt(1, 8);
                                itemMap.itemOptions.add(new ItemOption(93, dayEx));
                                itemMap.timeHSD = System.currentTimeMillis() + (long) dayEx * 86400000;

                                ItemMap itemM = new ItemMap();
                                itemM.playerId = -1;
                                itemM.x = xDIE;
                                itemM.y = yDIE;
                                //                            itemM.itemMapID = 738;
                                //                            itemM.itemTemplateID = (short) itemM.itemMapID;
                                itemM.itemMapID = itemsMap.size();
                                itemM.itemTemplateID = (short) 738;
                                itemMap.template = ItemTemplate.ItemTemplateID(738);
                                itemM.item = itemMap;

                                addItemToMap(itemM, -1, xDIE, yDIE);

                                itemMap = new Item(lychee.item);
                                itemMap.itemOptions.clear();
                                itemMap.itemOptions.add(new ItemOption(50, Util.nextInt(1, maxP)));
                                itemMap.itemOptions.add(new ItemOption(77, Util.nextInt(1, maxP)));
                                itemMap.itemOptions.add(new ItemOption(103, Util.nextInt(1, maxP)));
                                itemMap.itemOptions.add(new ItemOption(5, Util.nextInt(1, 16)));
                                itemMap.itemOptions.add(new ItemOption(30, 0)); //KHONG THE GIAO DICH
                                dayEx = Util.nextInt(1, 8);
                                itemMap.itemOptions.add(new ItemOption(93, dayEx));
                                itemMap.timeHSD = System.currentTimeMillis() + (long) dayEx * 86400000;
                                itemM = new ItemMap();
                                itemM.playerId = -1;
                                itemM.x = (short) 558;
                                itemM.y = (short) 480;
                                itemM.itemMapID = itemsMap.size();
                                itemM.itemTemplateID = (short) 738;
                                itemMap.template = ItemTemplate.ItemTemplateID(738);
                                itemM.item = itemMap;
                                addItemToMap(itemM, -1, (short) 558, (short) 480);
                                itemMap = new Item(lychee.item);
                                itemMap.itemOptions.clear();
                                itemMap.itemOptions.add(new ItemOption(50, Util.nextInt(1, maxP)));
                                itemMap.itemOptions.add(new ItemOption(77, Util.nextInt(1, maxP)));
                                itemMap.itemOptions.add(new ItemOption(103, Util.nextInt(1, maxP)));
                                itemMap.itemOptions.add(new ItemOption(5, Util.nextInt(1, 16)));
                                itemMap.itemOptions.add(new ItemOption(30, 0)); //KHONG THE GIAO DICH
                                dayEx = Util.nextInt(1, 8);
                                itemMap.itemOptions.add(new ItemOption(93, dayEx));
                                itemMap.timeHSD = System.currentTimeMillis() + (long) dayEx * 86400000;
                                itemM = new ItemMap();
                                itemM.playerId = -1;
                                itemM.x = (short) 768;
                                itemM.y = (short) 480;
                                itemM.itemMapID = itemsMap.size();
                                itemM.itemTemplateID = (short) 738;
                                itemMap.template = ItemTemplate.ItemTemplateID(738);
                                itemM.item = itemMap;
                                addItemToMap(itemM, -1, (short) 768, (short) 480);
                            } else if (_BOSS._typeBoss == (byte) 43) { //END KHI GA
                                //ROT CAI TRANG
                                ItemSell lychee = ItemSell.getItemSell(729, (byte) 1);
                                Item itemMap = new Item(lychee.item);
                                itemMap.itemOptions.clear();
                                int maxP = (int) Math.ceil(41 * _BOSS.lvGas / 110);
                                maxP = maxP > 3 && maxP <= 41 ? maxP : 3;
                                itemMap.itemOptions.add(new ItemOption(50, Util.nextInt(1, maxP)));
                                itemMap.itemOptions.add(new ItemOption(77, Util.nextInt(1, maxP)));
                                itemMap.itemOptions.add(new ItemOption(103, Util.nextInt(1, maxP)));
                                itemMap.itemOptions.add(new ItemOption(5, Util.nextInt(1, 16)));
                                itemMap.itemOptions.add(new ItemOption(30, 0)); //KHONG THE GIAO DICH
                                int dayEx = Util.nextInt(1, 8);
                                itemMap.itemOptions.add(new ItemOption(93, dayEx));
                                itemMap.timeHSD = System.currentTimeMillis() + (long) dayEx * 86400000;

                                ItemMap itemM = new ItemMap();
                                itemM.playerId = -1;
                                itemM.x = xDIE;
                                itemM.y = yDIE;
                                //                            itemM.itemMapID = 729;
                                //                            itemM.itemTemplateID = (short) itemM.itemMapID;
                                itemM.itemMapID = itemsMap.size();
                                itemM.itemTemplateID = (short) 729;
                                itemMap.template = ItemTemplate.ItemTemplateID(729);
                                itemM.item = itemMap;

                                addItemToMap(itemM, -1, xDIE, yDIE);

                                itemMap = new Item(lychee.item);
                                itemMap.itemOptions.clear();
                                itemMap.itemOptions.add(new ItemOption(50, Util.nextInt(1, maxP)));
                                itemMap.itemOptions.add(new ItemOption(77, Util.nextInt(1, maxP)));
                                itemMap.itemOptions.add(new ItemOption(103, Util.nextInt(1, maxP)));
                                itemMap.itemOptions.add(new ItemOption(5, Util.nextInt(1, 16)));
                                itemMap.itemOptions.add(new ItemOption(30, 0)); //KHONG THE GIAO DICH
                                dayEx = Util.nextInt(1, 8);
                                itemMap.itemOptions.add(new ItemOption(93, dayEx));
                                itemMap.timeHSD = System.currentTimeMillis() + (long) dayEx * 86400000;
                                itemM = new ItemMap();
                                itemM.playerId = -1;
                                itemM.x = (short) 558;
                                itemM.y = (short) 480;
                                itemM.itemMapID = itemsMap.size();
                                itemM.itemTemplateID = (short) 738;
                                itemMap.template = ItemTemplate.ItemTemplateID(729);
                                itemM.item = itemMap;
                                addItemToMap(itemM, -1, (short) 558, (short) 480);
                                itemMap = new Item(lychee.item);
                                itemMap.itemOptions.clear();
                                itemMap.itemOptions.add(new ItemOption(50, Util.nextInt(1, maxP)));
                                itemMap.itemOptions.add(new ItemOption(77, Util.nextInt(1, maxP)));
                                itemMap.itemOptions.add(new ItemOption(103, Util.nextInt(1, maxP)));
                                itemMap.itemOptions.add(new ItemOption(5, Util.nextInt(1, 16)));
                                itemMap.itemOptions.add(new ItemOption(30, 0)); //KHONG THE GIAO DICH
                                dayEx = Util.nextInt(1, 8);
                                itemMap.itemOptions.add(new ItemOption(93, dayEx));
                                itemMap.timeHSD = System.currentTimeMillis() + (long) dayEx * 86400000;
                                itemM = new ItemMap();
                                itemM.playerId = -1;
                                itemM.x = (short) 768;
                                itemM.y = (short) 480;
                                itemM.itemMapID = itemsMap.size();
                                itemM.itemTemplateID = (short) 738;
                                itemMap.template = ItemTemplate.ItemTemplateID(729);
                                itemM.item = itemMap;
                                addItemToMap(itemM, -1, (short) 768, (short) 480);

                                //END KHI GA
                                Service.gI().endKhiGas(_BOSS.idClan);
                            } else if (_BOSS._typeBoss == (byte) 48) { //ZAMASU
                                Timer timerZamasu = new Timer();
                                TimerTask ttZamasu = new TimerTask() {
                                    public void run() {
                                        Service.gI().initZamasu();
                                        timerZamasu.cancel();
                                    }
                                ;
                                };
                                timerZamasu.schedule(ttZamasu, 1200000);
                            } else if (_BOSS._typeBoss == (byte) 53 || _BOSS._typeBoss == (byte) 55) { //ZAMASU
                                Timer timerBossNgoc = new Timer();
                                TimerTask ttBossNgoc = new TimerTask() {
                                    public void run() {
                                        Service.gI().initBossNgoc();
                                        timerBossNgoc.cancel();
                                    }
                                ;
                                };
                                timerBossNgoc.schedule(ttBossNgoc, 900000);
                            } else if ((_BOSS._typeBoss == (byte) 49 || _BOSS._typeBoss == (byte) 50) && !isHoiSinhBill) {
                                Boss Bill = getBossByID(-200529);
                                Boss Whis = getBossByID(-200530);
                                if ((_BOSS._typeBoss == (byte) 49 && Whis == null) || (_BOSS._typeBoss == (byte) 49 && Whis != null && Whis.isdie)
                                        || (_BOSS._typeBoss == (byte) 50 && Bill == null) || (_BOSS._typeBoss == (byte) 50 && Bill != null && Bill.isdie)) {
                                    isHoiSinhBill = true;
                                    Timer timerBill = new Timer();
                                    TimerTask ttBill = new TimerTask() {
                                        public void run() {
                                            Service.gI().initBillWhis();
                                            isHoiSinhBill = false;
                                            timerBill.cancel();
                                        }
                                    ;
                                    };
                                    timerBill.schedule(ttBill, 900000);
                                }
                            }
                        }
                        timerBOSS.cancel();
                        return;
                    }
//                    Player _charTarget = getCharNearest(_BOSS.x, _BOSS.y, 2000); //get char gan nhat
                    Player _charTarget = getRandomCharInMap(); //GET CHAR RANDOM TRONG MAP

                    if (_BOSS._typeBoss < (byte) 7 || (_BOSS._typeBoss > (byte) 18 && _BOSS._typeBoss != (byte) 29 && _BOSS._typeBoss < (byte) 36) || (_BOSS._typeBoss >= (byte) 40 && _BOSS._typeBoss <= (byte) 41) || _BOSS._typeBoss >= (byte) 48) {
                        long timeNow = System.currentTimeMillis();
                        if ((timeNow - _BOSS.lastTimeUseChargeSkill) > 45000 && _BOSS.hp < _BOSS.hpFull && !_BOSS.isTTNL && !_BOSS.checkBiKhongChe()) {
                            _BOSS.lastTimeUseChargeSkill = timeNow;
                            _BOSS.isTTNL = true;
                            _BOSS.bossNoPetChargeHPMP(_BOSS, 1);
                            // creating timertask, timer
                            long hpBossNow = (long) _BOSS.hp;
                            Timer timerPetTTNL = new Timer();
                            TimerTask petTTNL = new TimerTask() {
                                public void run() {
                                    //                                if(_broly.countCharge >= 10 || (_broly.hp >= _broly.hpFull)) {
                                    if ((long) _BOSS.hp >= (hpBossNow + (long) ((_BOSS.hpFull) * 0.1)) || _BOSS.hp >= _BOSS.hpFull || _BOSS.checkBiKhongChe() || _BOSS.isdie || _BOSS.countCharge >= 10) {
                                        _BOSS.countCharge = 0;
                                        _BOSS.isTTNL = false;
                                        _BOSS.bossNoPetChargeHPMP(_BOSS, 3);
                                        timerPetTTNL.cancel();
                                    } else {
                                        if (!_BOSS.isdie) {
                                            _BOSS.countCharge++;
                                            _BOSS.bossNoPetChargeHPMP(_BOSS, 2);
                                            chat(_BOSS, "Tái tạo năng lượng đã mất!");
                                        }
                                    }
                                }
                            ;
                            };
                            timerPetTTNL.schedule(petTTNL, 0, 1000);
                        }
                    } else if (_BOSS._typeBoss == (byte) 29) {
                        if (!_BOSS.isBoom) {
                            _BOSS.isBoom = true;
                            Timer timerXenBoom = new Timer();
                            TimerTask xenBoom = new TimerTask() {
                                public void run() {
                                    timerXenBoom.cancel();
                                    Message m = null;
                                    try {
                                        m = new Message(-45);
                                        m.writer().writeByte(7);
                                        m.writer().writeInt(_BOSS.id); // id player use 
                                        m.writer().writeShort((short) 14); // b91 gui cho co
                                        m.writer().writeShort(3000); //    seconds
                                        m.writer().flush();
                                        for (Player p : players) {
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

                                    Timer XenBoom = new Timer();
                                    TimerTask xenKBoom = new TimerTask() {
                                        public void run() {
                                            if (_BOSS.isdie) {
                                                XenBoom.cancel();
                                            } else {
                                                Message ms = null;
                                                try {
                                                    int dameBoom = (int) (_BOSS.hp * Util.getPercentDouble(130));
                                                    ms = new Message(-45);
                                                    ms.writer().writeByte(7);
                                                    ms.writer().writeInt(_BOSS.id); // id player use    
                                                    ms.writer().writeShort((short) 104); // b91 gui cho co
                                                    ms.writer().writeShort(0); //    seconds
                                                    ms.writer().flush();
                                                    for (Player p : players) {
                                                        p.session.sendMessage(ms);
                                                    }
                                                    ms.cleanup();
                                                    //boss chet
                                                    for (Player _pp : players) {
                                                        sendDieToAnotherPlayer(_pp, _BOSS);
                                                    }
                                                    timerBOSS.cancel();
                                                    //END NHIEM VU SAN BOSS
                                                    Timer timerBossLeave = new Timer();
                                                    TimerTask bossLeave = new TimerTask() {
                                                        public void run() {
                                                            leaveBoss(_BOSS); //xoa boss
                                                        }
                                                    ;
                                                    };
                                                timerBossLeave.schedule(bossLeave, 10000);
                                                    for (Player _p : players) {
                                                        if (Math.abs(_BOSS.x - _p.x) < 700 && Math.abs(_BOSS.y - _p.y) < 700) {

                                                            _p.hp -= dameBoom;
                                                            if (_p.hp <= 0) {
                                                                _p.isdie = true;
                                                                _p.isTTNL = false;
                                                                _p.hp = 0;
                                                            }
                                                            if (_p.isdie) {
                                                                for (Player _pll : players) {
                                                                    if (_pll.id == _p.id) {
                                                                        if (_p.isMonkey) {
                                                                            Service.gI().loadCaiTrangTemp(_p);
                                                                            _p.isMonkey = false;
                                                                            //NOI TAI TANG DAME KHI HOA KHI
                                                                            if (_p.upDameAfterKhi && _p.noiTai.id != 0 && _p.noiTai.idSkill == (byte) 13) {
                                                                                _p.upDameAfterKhi = false;
                                                                            }
                                                                            //NOI TAI TANG DAME KHI HOA KHI
                                                                            Service.gI().loadPoint(_p.session, _p);
                                                                        }
                                                                        sendDieToMe(_p);
                                                                    } else {
                                                                        _pll.sendDefaultTransformToPlayer(_p);
                                                                        sendDieToAnotherPlayer(_pll, _p);
                                                                    }
                                                                }
                                                            } else {
                                                                dameChar(_p.id, _p.hp, dameBoom, false);
                                                            }
                                                        }
                                                    }

                                                    for (Detu _pet : pets) {
                                                        if (Math.abs(_BOSS.x - _pet.x) < 700 && Math.abs(_BOSS.y - _pet.y) < 700) {

                                                            _pet.hp -= dameBoom;
                                                            if (_pet.hp <= 0) {
                                                                _pet.isdie = true;
                                                                _pet.isTTNL = false;
                                                                _pet.hp = 0;
                                                            }
                                                            if (_pet.isdie) {
                                                                //SEND TASK HOI SINH DE TU NEU DANH CHET
                                                                Timer hoiSinhDetu = new Timer();
                                                                TimerTask hsDetu = new TimerTask() {
                                                                    public void run() {
                                                                        if (_pet.isdie) {
                                                                            hoiSinhDetu.cancel();
                                                                            Player suPhu = PlayerManger.gI().getPlayerByDetuID(_pet.id);
                                                                            _pet.x = suPhu.x;
                                                                            _pet.y = suPhu.y;
                                                                            Service.gI().petLiveFromDead(suPhu);
                                                                            if (suPhu.statusPet == (byte) 1 || suPhu.statusPet == (byte) 2) {
                                                                                suPhu.zone.PetAttack(suPhu, _pet, suPhu.statusPet);
                                                                            }
                                                                        }
                                                                    }
                                                                ;
                                                                };
                                                            hoiSinhDetu.schedule(hsDetu, 60000);
                                                                for (Player _pll : players) {
                                                                    _pll.sendDefaultTransformToPlayer(_pet);
                                                                    sendDieToAnotherPlayer(_pll, _pet);
                                                                }
                                                            } else {
                                                                dameChar(_pet.id, _pet.hp, dameBoom, false);
                                                            }
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                } finally {
                                                    if (ms != null) {
                                                        ms.cleanup();
                                                    }
                                                }
                                            }
                                        }
                                    };
                                    XenBoom.schedule(xenKBoom, 3000);
                                }
                            };
                            timerXenBoom.schedule(xenBoom, 30000);
                        }
                    } else if (_BOSS._typeBoss == (byte) 36) { //DRABULA HOA DA
                        long timeNow = System.currentTimeMillis();
                        if ((timeNow - _BOSS.lastTimeUseChargeSkill) > 30000 && !_BOSS.isdie && !_BOSS.checkBiKhongChe()) {
                            _BOSS.lastTimeUseChargeSkill = timeNow;
                            charToStone((byte) 2, 500, _BOSS.x, _BOSS.y, (byte) 0);
                        }
                    } else if (_BOSS._typeBoss >= (byte) 37 && _BOSS._typeBoss <= (byte) 39) { //BIEN SOCOLA
                        long timeNow = System.currentTimeMillis();
                        if ((timeNow - _BOSS.lastTimeUseChargeSkill) > 45000 && !_BOSS.isdie && !_BOSS.checkBiKhongChe()) {
                            _BOSS.lastTimeUseChargeSkill = timeNow;
                            charToStone((byte) 2, 1000, _BOSS.x, _BOSS.y, (byte) 1);
                        }
                    }

                    if (_charTarget != null && !_BOSS.isTTNL && !_BOSS.isdie) {
                        bossAttackChar(_BOSS, _charTarget);
                        if (_BOSS._typeBoss == (byte) 28 && _BOSS.callXenCon > 0) {
                            if ((System.currentTimeMillis() - _BOSS.callXenCon) > 60000) {
                                _BOSS.callXenCon = System.currentTimeMillis();
                                if (getBossByID(-200148) == null) {
                                    Boss _XenCon1 = new Boss(148, (byte) 29, (short) 361, (short) 288);
                                    _XenCon1.name = "Xên Con 1";
                                    bossMap.add(_XenCon1);
                                    loadBossNoPet(_XenCon1);
                                }
                                if (getBossByID(-200149) == null) {
                                    Boss _XenCon2 = new Boss(149, (byte) 29, (short) 400, (short) 288);
                                    _XenCon2.name = "Xên Con 2";
                                    bossMap.add(_XenCon2);
                                    loadBossNoPet(_XenCon2);
                                }
                                if (getBossByID(-200150) == null) {
                                    Boss _XenCon3 = new Boss(150, (byte) 29, (short) 340, (short) 288);
                                    _XenCon3.name = "Xên Con 3";
                                    bossMap.add(_XenCon3);
                                    loadBossNoPet(_XenCon3);
                                }
//                                if(getBossByID(-200151) == null) {
//                                    Boss _XenCon4 = new Boss(151, (byte)29, (short)320, (short)288);
//                                    _XenCon4.name = "Xên Con 4";
//                                    bossMap.add(_XenCon4);
//                                    loadBossNoPet(_XenCon4);
//                                }
//                                if(getBossByID(-200152) == null) {
//                                    Boss _XenCon5 = new Boss(152, (byte)29, (short)410, (short)288);
//                                    _XenCon5.name = "Xên Con 5";
//                                    bossMap.add(_XenCon5);
//                                    loadBossNoPet(_XenCon5);
//                                }
//                                if(getBossByID(-200153) == null) {
//                                    Boss _XenCon6 = new Boss(152, (byte)29, (short)410, (short)288);
//                                    _XenCon6.name = "Xên Con 6";
//                                    bossMap.add(_XenCon6);
//                                    loadBossNoPet(_XenCon6);
//                                }
//                                if(getBossByID(-200154) == null) {
//                                    Boss _XenCon7 = new Boss(152, (byte)29, (short)410, (short)288);
//                                    _XenCon7.name = "Xên Con 7";
//                                    bossMap.add(_XenCon7);
//                                    loadBossNoPet(_XenCon7);
//                                }
                            }
                        }
                    }
                    if (_charTarget != null) {
                        if (_BOSS._typeBoss < (byte) 7) {
                            chat(_BOSS, "HAHAHA, chết hết đi!");
                        } else if (_BOSS._typeBoss == (byte) 7 || _BOSS._typeBoss == (byte) 8 || _BOSS._typeBoss == (byte) 9) {
                            chat(_BOSS, "Ngươi đã thấy sợ ta chưa!");
                        } else if (_BOSS._typeBoss >= (byte) 10 && _BOSS._typeBoss <= (byte) 14) {
                            chat(_BOSS, "Ta sẽ mang đầu các ngươi cho ngài Fide!");
                        } else if (_BOSS._typeBoss >= (byte) 15 && _BOSS._typeBoss <= (byte) 17) {
                            chat(_BOSS, "Ta sẽ tiêu diệt hết các ngươi!");
                        } else if (_BOSS._typeBoss == (byte) 18 || _BOSS._typeBoss == (byte) 19) {
                            chat(_BOSS, "HAHA, Ta sẽ tiêu diệt các ngươi và dùng các ngươi chế tạo Rôbốt");
                        } else if (_BOSS._typeBoss == (byte) 20 || _BOSS._typeBoss == (byte) 21 || _BOSS._typeBoss == (byte) 22) {
                            chat(_BOSS, "Bọn ta sẽ giết hết những ai bảo vệ Goku");
                        } else if (_BOSS._typeBoss == (byte) 23 || _BOSS._typeBoss == (byte) 24 || _BOSS._typeBoss == (byte) 25) {
                            chat(_BOSS, "Bọn ta sẽ giết hết các ngươi!");
                        } else if (_BOSS._typeBoss == (byte) 26 || _BOSS._typeBoss == (byte) 27 || _BOSS._typeBoss == (byte) 28) {
                            chat(_BOSS, "Đến đây hết đi, giết các ngươi sẽ giúp ta tiến hóa!");
                        } else if (_BOSS._typeBoss == (byte) 40 || _BOSS._typeBoss == (byte) 41) {
                            chat(_BOSS, "Siêu xayda huyền thoại sao, là gì vậy!");
                        } else if (_BOSS._typeBoss == (byte) 48) {
                            chat(_BOSS, "Ta phải giết chết tên Goku đó");
                        } else if (_BOSS._typeBoss == (byte) 49) {
                            chat(_BOSS, "Tên Super Saiyan God ở đây sao?");
                        } else if (_BOSS._typeBoss == (byte) 50) {
                            chat(_BOSS, "Hành tinh này có đồ ăn gì ngon nhỉ, hôhôhô!");
                        } else if (_BOSS._typeBoss == (byte) 53 || _BOSS._typeBoss == (byte) 54) {
                            chat(_BOSS, "Mạnh mẽ lên, như thế này mà đòi giết ta à ??");
                        }
                    }
                }
            };
            timerBOSS.schedule(timeBOSS, 0, timeTuDong);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //LOAD BOSS MAP YARDRAT
    public void loadBossNoCharge(Boss _BOSS) {
        loadInfoBoss(_BOSS);
        _BOSS.zone = this;
        // lap trinh boss tu dong
        long timeTuDong = 1000;

        Timer timerBOSS = new Timer();
        TimerTask timeBOSS = new TimerTask() {
            public void run() {
                if (_BOSS.isdie) {
                    short xDIE = _BOSS.x;
                    short yDIE = _BOSS.y;
                    int numberBoss = _BOSS._numberBoss;
                    byte typeBoss = _BOSS._typeBoss;
                    Timer timerBOSSBack = new Timer();
                    TimerTask bossBack = new TimerTask() {
                        public void run() {
//                            Boss _BOSSBack = new Boss(numberBoss, typeBoss, xDIE, yDIE);
                            _BOSS.typePk = (byte) 5;
                            _BOSS.isdie = false;
                            _BOSS.hp = _BOSS.hpGoc;
//                            _BOSSBack.setXYStart(numberBoss);
//                            bossMap.add(_BOSSBack);
                            loadBossNoCharge(_BOSS);
                            timerBOSSBack.cancel();
                        }
                    ;
                    };
                    timerBOSSBack.schedule(bossBack, 9000);

                    timerBOSS.cancel();
                    return;
                }
                //CHECK BOSS MOVE
                Player _charTarget = getCharYardrat(_BOSS.x, _BOSS.y, 50, _BOSS.yStart); //get char gan nhat

                if (_charTarget != null && !_BOSS.isdie) {
                    _BOSS.x = _charTarget.x;
                    _BOSS.y = _charTarget.y;
                    bossYardratMove(_BOSS);
                    bossAttackChar(_BOSS, _charTarget);
                } else {
                    _BOSS.x = _BOSS.xStart;
                    _BOSS.y = _BOSS.yStart;
                    if (_BOSS.moveRight == (byte) 1) {
                        _BOSS.moveRight = (byte) 0;
                        _BOSS.x += (short) 50;
                    } else {
                        _BOSS.moveRight = (byte) 1;
                        _BOSS.x -= (short) 50;
                    }
                    bossYardratMove(_BOSS);
                }
//                if(_charTarget != null) {
//                    if(_BOSS._typeBoss < (byte)7) {
//                        chat(_BOSS, "HAHAHA, chết hết đi!");
//                    }
//                }
            }
        };
        timerBOSS.schedule(timeBOSS, 0, timeTuDong);
    }

    public void leaveBossYardrat(Boss _boss) {
        if (bossMap.contains(_boss)) {
//            bossMap.remove(_boss);
            try {
                Message m = new Message(-6);
                m.writer().writeInt(_boss.id);
                for (Player p : players) {
                    p.session.sendMessage(m);
                }
                m.writer().flush();
                m.cleanup();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void bossYardratMove(Boss _boss) {
        Message msg;
        try {
            for (Player player : players) {
                if (player != null && player.session != null) {
                    msg = new Message(-7);
                    msg.writer().writeInt(_boss.id);
                    msg.writer().writeShort(_boss.x);
                    msg.writer().writeShort(_boss.y);
                    player.session.sendMessage(msg);
                    msg.cleanup();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Player getCharYardrat(int xP, int yP, int minSpace, short yStart) {
        Player _CharNearest = null;
//        short minSpace = 150;
        if (players.size() > 0) {
            for (Player _p : players) {
                if ((int) (Math.hypot(Math.abs(xP - _p.x), Math.abs(yP - _p.y))) < minSpace && !_p.isdie) {
                    _CharNearest = _p;
                    minSpace = (int) (Math.hypot(Math.abs(xP - _p.x), Math.abs(yP - _p.y)));
                }
            }
        }
        if (_CharNearest != null) {
            if (_CharNearest.y != yStart) {
                return null;
            } else {
                return _CharNearest;
            }
        } else {
            return null;
        }
    }

    public ItemMap dropItemGOD(Player p, short xDROP, short yDROP) {
        ItemMap itemROI = null;
        int perDrop = Util.nextInt(0, 10);
        if (perDrop < 3) {
            if (Util.nextInt(0, 2) == 0) {
                int percentDropItemGod = -1;
                ItemSell itemGOD = null;
                int _ITEMMAPID = -1;
                int idItemGod[] = {555, 556, 561, 562, 563, 557, 558, 561, 564, 565, 559, 560, 561, 566, 567};
                percentDropItemGod = Util.nextInt(0, 15);
                itemGOD = ItemSell.getItemSell(idItemGod[percentDropItemGod], (byte) 1);
                _ITEMMAPID = idItemGod[percentDropItemGod];
                //        if(p.gender == 0) {
                //            int idItemGod[] = {555, 556, 561, 562, 563};
                //            percentDropItemGod = Util.nextInt(0, 5);
                //            itemGOD = ItemSell.getItemSell(idItemGod[percentDropItemGod], (byte)1);
                //            _ITEMMAPID = idItemGod[percentDropItemGod];
                //        } else if (p.gender == 1) {
                //            int idItemGod[] = {557, 558, 561, 564, 565};
                //            percentDropItemGod = Util.nextInt(0, 5);
                //            itemGOD = ItemSell.getItemSell(idItemGod[percentDropItemGod], (byte)1);
                //            _ITEMMAPID = idItemGod[percentDropItemGod];
                //        } else if (p.gender == 2) {
                //            int idItemGod[] = {559, 560, 561, 566, 567};
                //            percentDropItemGod = Util.nextInt(0, 5);
                //            itemGOD = ItemSell.getItemSell(idItemGod[percentDropItemGod], (byte)1);
                //            _ITEMMAPID = idItemGod[percentDropItemGod];
                //        }
                if (_ITEMMAPID >= 555 && _ITEMMAPID <= 567) {
                    //            ItemMap itemROI = new ItemMap();
                    itemROI = new ItemMap();
                    itemROI.playerId = p.id;
                    itemROI.x = xDROP;
                    itemROI.y = yDROP;
                    //            itemROI.itemMapID = _ITEMMAPID;
                    //            itemROI.itemTemplateID = (short)itemROI.itemMapID;
                    itemROI.itemMapID = itemsMap.size();
                    itemROI.itemTemplateID = (short) _ITEMMAPID;
                    //                                itemGOD.item.template = ItemTemplate.ItemTemplateID(_ITEMMAPID);
                    //BUILD NEW ITEM + CHI SO CHO DO THAN LINH
                    Item _ITEMGOD = new Item(itemGOD.item);
                    _ITEMGOD.itemOptions.clear();
                    if (_ITEMGOD.template.id == 555 || _ITEMGOD.template.id == 557 || _ITEMGOD.template.id == 559) { //AO THAN
                        _ITEMGOD.itemOptions.add(new ItemOption(47, Util.nextInt(500, 1001)));
                    } else if (_ITEMGOD.template.id == 556) { //QUAN THAN TRAI DAT
                        _ITEMGOD.itemOptions.add(new ItemOption(6, (Util.nextInt(350, 551) * 100)));
                        _ITEMGOD.itemOptions.add(new ItemOption(27, Util.nextInt(9000, 10000)));
                    } else if (_ITEMGOD.template.id == 558) {//QUAN THAN NAMEK
                        _ITEMGOD.itemOptions.add(new ItemOption(6, (Util.nextInt(350, 501) * 100)));
                        _ITEMGOD.itemOptions.add(new ItemOption(27, Util.nextInt(9000, 9500)));
                    } else if (_ITEMGOD.template.id == 560) { //QUAN THAN XAYDA
                        _ITEMGOD.itemOptions.add(new ItemOption(6, (Util.nextInt(350, 601) * 100)));
                        _ITEMGOD.itemOptions.add(new ItemOption(27, Util.nextInt(9500, 10500)));
                    } else if (_ITEMGOD.template.id == 562) { //GANG THAN TD
                        _ITEMGOD.itemOptions.add(new ItemOption(0, (Util.nextInt(300, 571) * 10)));
                    } else if (_ITEMGOD.template.id == 564) { //GANG THAN NAMEK
                        _ITEMGOD.itemOptions.add(new ItemOption(0, (Util.nextInt(300, 551) * 10)));
                    } else if (_ITEMGOD.template.id == 566) { //GANG THAN XAYDA
                        _ITEMGOD.itemOptions.add(new ItemOption(0, (Util.nextInt(300, 601) * 10)));
                    } else if (_ITEMGOD.template.id == 563) { //GIAY THAN TD
                        _ITEMGOD.itemOptions.add(new ItemOption(7, (Util.nextInt(350, 551) * 100)));
                        _ITEMGOD.itemOptions.add(new ItemOption(28, Util.nextInt(9000, 10000)));
                    } else if (_ITEMGOD.template.id == 565) { //GIAY THAN NAMEK
                        _ITEMGOD.itemOptions.add(new ItemOption(7, (Util.nextInt(350, 601) * 100)));
                        _ITEMGOD.itemOptions.add(new ItemOption(28, Util.nextInt(9500, 10500)));
                    } else if (_ITEMGOD.template.id == 567) { //GIAY THAN XAYDA
                        _ITEMGOD.itemOptions.add(new ItemOption(7, (Util.nextInt(350, 501) * 100)));
                        _ITEMGOD.itemOptions.add(new ItemOption(28, Util.nextInt(9000, 9500)));
                    } else { //NHAN THAN LINH
                        _ITEMGOD.itemOptions.add(new ItemOption(14, Util.nextInt(3, 16)));
                    }
                    _ITEMGOD.itemOptions.add(new ItemOption(21, 17));
                    itemROI.item = _ITEMGOD;
                    //            itemDrop.add(itemROI);
                    //            itemsMap.addAll(itemDrop);
                    return itemROI;
                }
            } else {
                int idNRRot = Util.nextInt(14, 16);
                Item itemMap = ItemSell.getItemNotSell(idNRRot);
                itemROI = new ItemMap();
                itemROI.playerId = p.id;
                itemROI.x = xDROP;
                itemROI.y = yDROP;
                itemROI.itemMapID = itemsMap.size();
                itemROI.itemTemplateID = (short) idNRRot;
                itemMap.template = ItemTemplate.ItemTemplateID(idNRRot);
                itemROI.item = itemMap;
                if (itemROI != null) {
                    return itemROI;
                }
            }
        }
        return itemROI;
    }

    //ADD ITEM RA MAP, VI DU NGOC RONG SAO DEN ROI RA MAP
    public void addItemToMap(ItemMap itemMap, int idPlayer, short x, short y) {
        Message m = null;
        try {
//            if(!isHasItemInMap(itemMap)) {
            itemsMap.add(itemMap);
//            }
            m = new Message(68);
            m.writer().writeShort(itemMap.itemMapID);
            m.writer().writeShort(itemMap.item.template.id);
            m.writer().writeShort(x);
            m.writer().writeShort(y);
            m.writer().writeInt(idPlayer);
            m.writer().flush();
            for (Player _pl : players) {
                _pl.session.sendMessage(m);
            }
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //CHECK O MAP DA CO ITEM DO HAY CHUA
    public boolean isHasItemInMap(ItemMap item) {
        for (ItemMap itemMap : itemsMap) {
//            if(itemMap.itemMapID == item.itemMapID) {
            if (itemMap.itemTemplateID == item.itemTemplateID) {
                return true;
            }
        }
        return false;
    }

    //ADD NEW ITEMMAP TO ARRAYLIST QUAN LY ITEM ROI O MAP
    public ItemMap createNewItemMap(int idItem, int idPlayer, short x, short y) {
        Item itemMap = ItemSell.getItemNotSell(idItem);
        ItemMap itemM = new ItemMap();
        itemM.playerId = idPlayer;
        itemM.x = x;
        itemM.y = y;
//        itemM.itemMapID = idItem;
//        itemM.itemTemplateID = (short) itemM.itemMapID;
        itemM.itemMapID = this.itemsMap.size();
        itemM.itemTemplateID = (short) idItem;
        itemMap.template = ItemTemplate.ItemTemplateID(idItem);
        itemM.item = itemMap;
        if ((idItem >= 353 && idItem <= 359) || (idItem >= 372 && idItem <= 378)) {
            itemM.timeDrop = System.currentTimeMillis();
        }
        if (itemM != null) {
            return itemM;
        }
        return null;
    }

    public ItemMap newItemMAP(int idItem, int idPlayer, short x, short y) {
        Item itemMap = ItemSell.getItemNotSell(idItem);
        ItemMap itemM = new ItemMap();
        itemM.playerId = idPlayer;
        itemM.x = x;
        itemM.y = y;
        itemM.itemMapID = this.itemsMap.size();
        itemM.itemTemplateID = (short) idItem;
        itemMap.template = ItemTemplate.ItemTemplateID(idItem);
        itemM.item = itemMap;
        if (itemM != null) {
            return itemM;
        }
        return null;
    }

    public ItemMap newItemMapBuff(int idItem, int idPlayer, short x, short y) {
        Item itemMap = ItemBuff.getItem(idItem);
        ItemMap itemM = new ItemMap();
        itemM.playerId = idPlayer;
        itemM.x = x;
        itemM.y = y;
        itemM.itemMapID = this.itemsMap.size();
        itemM.itemTemplateID = (short) idItem;
        itemMap.template = ItemTemplate.ItemTemplateID(idItem);
        itemM.item = itemMap;
        if (itemM != null) {
            return itemM;
        }
        return null;
    }

    public ItemMap cNewItemMap(int idItem, int idPlayer, short x, short y) {
//        int perBiKip = Util.nextInt(0, 3);
//        if(perBiKip == 0) {
        Item itemMap = ItemSell.getItemNotSell(idItem);
        ItemMap itemM = new ItemMap();
        itemM.playerId = idPlayer;
        itemM.x = x;
        itemM.y = y;
        itemM.itemMapID = this.itemsMap.size();
        itemM.itemTemplateID = (short) idItem;
        itemMap.template = ItemTemplate.ItemTemplateID(idItem);
        itemM.item = itemMap;
        if (itemM != null) {
            return itemM;
        }
//        }
        return null;
    }

    public void resetBagClan(Player player) {
        byte imgClan = -1;
        if (player.clan != null) {
            imgClan = player.clan.imgID;
        }
        for (Player _p : players) {
            //UPDATE BAG SAU LUNG
            Service.gI().updateBagNew(_p.session, player.id, imgClan);
            //GET BAG SAU LUNG
            ClanService.gI().getBagBangNew(_p.session, imgClan);
            if (_p.id != player.id) {
                ClanService.gI().loadUpdateInfoMember(_p.session, player);
            }
        }
    }

    //CHECK THANH VIEN BANG O CUNG KHU KHI LAM NHIEM VU
    public boolean checkMemberClanInMap(Player p) {
        byte count = 0;
        for (Player _p : players) {
            if (_p.id != p.id && p.clan != null && _p.clan != null && _p.clan.id == p.clan.id) {
                count++;
                if (count == (byte) 2) {
                    break;
                }
            }
        }
        if (count >= (byte) 2) {
            return true;
        }
        return false;
    }

    //CHECK CO THE GOI RONG NAMEC
    public boolean canCallDragonNamec(Player p) {
        byte count = (byte) 0;
//        for(byte ii = 0; ii < 7; ii++) {
//            Util.log("MAP: " + Server.gI().mapNrNamec[ii]);
//            Util.log("ZONE: " + Server.gI().zoneNrNamec[ii]);
//            Util.log("ID PLAYER: " + Server.gI().idpNrNamec[ii]);
//            Util.log("NAME PLAYER: " + Server.gI().pNrNamec[ii]);
//        }
//        Util.log("CHECK SAME: " + Service.gI().isSameMapNrNamec());
//        Util.log("CHECK SAME: " + Service.gI().isSameZoneNrNamec());
        if (Service.gI().isSameMapNrNamec() && Service.gI().isSameZoneNrNamec()) {
            //CHECK PLAYER IN CLAN HAS NGOC RONG NAMEC
            if (p.clan != null) {
                for (int i = 0; i < Server.gI().idpNrNamec.length; i++) {
                    for (int j = 0; j < p.clan.members.size(); j++) {
//                        Util.log("ID MEMBER IN CLAN: " + p.clan.members.get(j).id);
                        if (Server.gI().idpNrNamec[i] == p.clan.members.get(j).id) {
                            count++;
                        }
                    }
                }
//                Util.log("count: " + count);
                if (count == (byte) 7) {
                    return true;
                }
            }
        }
        return false;
    }

    //UP TNSM THANH VIEN CUNG BANG TRONG CUNG KHU
    public void upTNMemBerClanInMap(Player p, long tnsm) {
        for (Player _p : players) {
            if (_p != null && p != null) {
                if (_p.id != p.id && _p.clan != null && p.clan != null && _p.clan.id == p.clan.id) {
                    byte lv = p.getLevelPower();
                    byte lvMem = _p.getLevelPower();
                    lvMem = (byte) (Math.abs(lvMem - lv));
                    tnsm = tnsm - (long) (tnsm * Util.getPercentDouble((int) (lvMem) * 30));
                    if (tnsm <= (long) 0) {
                        tnsm = (long) 1;
                    }
                    if (_p.gender != (byte) 1) {
                        tnsm = tnsm * 4 / 5 <= 0 ? 1 : tnsm * 4 / 5;
                    }
                    _p.tiemNang += tnsm;
                    _p.UpdateSMTN((byte) 1, tnsm);
                }
            }
        }
    }

    //CHECK QUAI DA DIE HET CHUA TRONG DOANH TRAI DE NEXT MAP
    public boolean chkAllMobDieDT() {
        if (mobs.size() > 0) {
            for (byte i = 0; i < mobs.size(); i++) {
                if (!mobs.get(i).isDie) {
                    return false;
                }
            }
        }
        if (bossMap.size() > 0) {
            for (byte i = 0; i < bossMap.size(); i++) {
                if (!bossMap.get(i).isdie) {
                    return false;
                }
            }
        }
        return true;
    }

    //INIT BIG BOSS AUTO
    public void initBigBoss(Mob bigBoss) {
        if ((System.currentTimeMillis() - bigBoss.delayBoss) >= 5000) {
//            int rd = Util.nextInt(0, 5);
            int rd = Util.nextInt(0, 3);
            byte type = (rd == 4) ? (byte) 8 : (byte) rd;
//            if(type == (byte)3 || type == (byte)8) {
//                Player pRandom = getRandomCharInMap();
//                if(type == (byte)3) {
//                    bigBoss.delayBoss = System.currentTimeMillis();
//                }
//                if(pRandom != null) {
//                    hirudegarnMove(type, pRandom);
//                }
//            } else {
            ArrayList<Integer> listChar = getAllCharSpace(bigBoss.pointX, bigBoss.pointY, 500);
            if (listChar.size() > 0) {
                sendHirudegarnAT(listChar, type, 100000, bigBoss);
            }
//            }
        }
    }

    //BOSS HIRUDEGARN ATTACK
    public void sendHirudegarnAT(ArrayList<Integer> listChar, byte type, int dame, Mob bigBoss) {
        Message m = null;
        try {
            m = new Message(101);
            m.writer().writeByte(type);
            m.writer().writeByte((byte) listChar.size());
            if (listChar.size() > 0) {
                for (byte i = 0; i < (byte) listChar.size(); i++) {
                    m.writer().writeInt(listChar.get(i)); //ID CHAR
                    m.writer().writeInt(1000);
                }
            }
            m.writer().flush();
            for (Player p : players) {
                if (p.session != null && p != null) {
                    p.session.sendMessage(m);
                    if (!p.isdie) {
                        loadMobAttached(bigBoss.tempId, p.id, 0);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    //BOSS HIRUDEGARN MOVE
    public void hirudegarnMove(byte type, Player pRandom) {
        if (pRandom != null) {
            Message m = null;
            try {
                m = new Message(101);
                m.writer().writeByte(type);
                m.writer().writeShort(pRandom.x);
                m.writer().writeShort(pRandom.y);
                m.writer().flush();
                for (Player p : players) {
                    if (p.session != null && p != null) {
                        p.session.sendMessage(m);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (m != null) {
                    m.cleanup();
                }
            }
        }
    }

    //GET ALL PLAYER TRONG TAM DANH
    public ArrayList<Integer> getAllCharSpace(short xP, short yP, int minSpace) {
        ArrayList<Integer> listChar = new ArrayList<>();
        if (players.size() > 0) {
            for (Player _p : players) {
                if ((int) (Math.hypot(Math.abs(xP - _p.x), Math.abs(yP - _p.y))) < minSpace && !_p.isdie) {
                    listChar.add(_p.id);
                }
            }
        }
        return listChar;
    }

    //INIT MOB CHIM
    public void initChimNamek(Player p, int idTemp) {
        Mob m = new Mob(p.id, idTemp, 10);
        m.level = 10;
        m.pointX = (short) (p.x + 50);
        m.pointY = (short) (p.y - 50);
        m.maxHp = p.getHpFull();
        m.hp = m.maxHp;
        m.status = 5;
        mobs.add(m);
    }

    public void close() {
    }
}
