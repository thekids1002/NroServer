package nro.main;

import java.io.IOException;
import java.sql.SQLException;
// võ hoàng kiệt đã comment ở đây
import nro.giftcode.GiftCodeManager;
import nro.io.Message;
import nro.io.Session;
import nro.item.Item;
import nro.item.ItemOption;
import nro.item.ItemSell;
import nro.item.ItemService;
import nro.player.Boss;
import nro.shop.Shop;
import nro.shop.TabItemShop;
import nro.skill.NoiTai;
import nro.skill.NoiTaiTemplate;
import nro.map.Map;
import nro.clan.ClanService;
import nro.clan.Member;
import nro.player.Player;
import nro.player.Detu;
import nro.player.PlayerManger;
import nro.task.TaskService;
import nro.task.TaskManager;
import nro.daihoi.DaiHoiService;
import nro.daihoi.DaiHoiManager;
import nro.item.ItemBuff;

public class Menu {

    Server server = Server.gI();

    public static void doMenuArray(Player p, int idnpc, String chat, String[] menu) {
        Message m = null;
        try {
            m = new Message(32);
            m.writer().writeShort(idnpc);
            m.writer().writeUTF(chat);
            m.writer().writeByte(menu.length);
            for (byte i = 0; i < menu.length; ++i) {
                m.writer().writeUTF(menu[i]);
            }
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

    public static void doMenuArraySay(Player p, short id, String[] menu) {
        Message m = null;
        try {
            m = new Message(38);
            m.writer().writeShort(id);
            for (byte i = 0; i < menu.length; i++) {
                m.writer().writeUTF(menu[i]);
            }
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

    public static void sendWrite(Player p, String title, short type) {
        Message m = null;
        try {
            m = new Message(88);
            m.writer().writeUTF(title);
            m.writer().writeShort(type);
            m.writer().flush();
            p.session.sendMessage(m);
            m.cleanup();
        } catch (IOException var5) {
            var5.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }

    }

    public void textBoxId(Session session, short menuId, String str) {
        Message msg;
        try {
            msg = new Message(88);
            msg.writer().writeInt(menuId);
            msg.writer().writeUTF(str);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendTB(Session session, Player title, String s) {
        Message m = null;
        try {
            m = new Message(94);
            m.writer().writeUTF(title.name);
            m.writer().writeUTF(s);
            m.writer().flush();
            PlayerManger.gI().SendMessageServer(m);
            session.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }

    }

    public void ChatTG(Player p, int avatar, String chat3, byte cmd) {
        Message m = null;
        try {
            m = new Message(-70);
            m.writer().writeShort(avatar);
            m.writer().writeUTF(chat3);
            m.writer().writeByte(cmd);
            m.writer().flush();
            PlayerManger.gI().SendMessageServer(m);
            p.session.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void ChatTG(Player p, short avatar, String str) {
        Message m = null;
        try {
            m = new Message(94);
            m.writer().writeShort(avatar);
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

    public void LuckyRound(Player p, byte type, byte soluong) throws IOException {
        Message m = null;
        try {
            if (type == 0) {
                m = new Message(-127);
                m.writer().writeByte(type);
                short[] arId = new short[]{2280, 2281, 2282, 2283, 2284, 2285, 2286};
                m.writer().writeByte(7);
                for (short i = 0; i < arId.length; i++) {
                    m.writer().writeShort(arId[i]);
                }
                m.writer().writeByte(soluong);
                m.writer().writeInt(10000);
                m.writer().writeShort(0);
                m.writer().flush();
                p.session.sendMessage(m);
            } else if (type == 1) {
                m = new Message(-127);
                m.writer().writeByte(soluong);
                short[] arId = new short[]{2, 3, 4, 5, 6, 7, 8};
                for (short i = 0; i < soluong; i++) {
                    m.writer().writeShort(arId[i]);
                }
                m.writer().flush();
                p.session.sendMessage(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void confirmMenu(Player p, Message m) throws IOException, SQLException, InterruptedException {
        short idNpc = m.reader().readShort();
        byte select = m.reader().readByte();
//        Util.log("ID NPC: " + idNpc);
//        Util.log("SELECT: " + select);
//        Util.log("p.menuID: " + p.menuID);
        switch (p.menuNPCID) {
            case 999: {
                if (p.id == 1 && p.name.equals("admin") || p.name.equals("nhtgame") || p.name.equals("berus") || p.name.equals("coldxayda")) {
                    if (p.menuID == -1) {
                        if (select == 0) {
                            //p.sendAddchatYellow("Chuúc bạn chơi game vui vẻ không quạo!");
                            doMenuArray(p, idNpc, "LIST BOSS", new String[]{"Broly", "Super Broly", "Cooler", "Cooler 2", "Black",
                                "Super Black", "Kuku", "Mập Đinh", "Rambo", "Số 4", "Số 3", "Số 2", "Số 1", "Tiểu Đội\nTrưởng", "Fide 1", "Fide 2", "Fide 3",
                                "Android 19", "Dr.Kôrê", "Android 15", "Android 14", "Android 13", "Poc", "Pic", "KingKong", "Xên 1", "Xên 2", "Xên Hoàn\nThiện",
                                "Xên Con", "Siêu Bọ\nHung", "Trung úy\nTrắng", "Trung úy\nXanh Lơ", "Trung úy\nThép", "Ninja\nÁo Tím", "Robốt\nVệ Sĩ",
                                "Drabura", "Pui Pui", "Ya Côn", "Ma Bư", "Chilled", "Chilled 2", "Dr Lychee", "Hatchiyack", "Tập sự", "Tân binh", "Chiến binh",
                                "Đội trưởng", "Zamasu", "Bill", "Whis", "Sơn Tinh", "Thuỷ Tinh", "Rati", "Fide Vàng", "Ăn Trộm", "Vegito\nQuyền Năng", "Super\nVegito", "Piccolo\nTối Thượng "});
                        } else if (select == 1) {
                            m = null;
                            try {
                                m = new Message(-125);
                                m.writer().writeUTF("Buff Item To Player");
                                m.writer().writeByte((byte) 3);
                                m.writer().writeUTF("Name Player");
                                m.writer().writeByte((byte) 1);
                                m.writer().writeUTF("ID Item");
                                m.writer().writeByte((byte) 0);
                                m.writer().writeUTF("Số lượng");
                                m.writer().writeByte((byte) 0);
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
                        } else if (select == 2) {
                            Service.gI().clientInput(p, "Nhập giftcode", "Giftcode", (byte) 0);
                            //GiftCodeManager.gI().checkInfomationGiftCode(p);
                        }
                        p.menuID = select;
                        break;
                    } else if (p.menuID == 0) {
                        byte typeBoss = (byte) (select + 1);
                        Boss _bossCall = new Boss(Server.gI().idBossCall, typeBoss, p.x, p.y);
                        if (typeBoss == (byte) 2) {
                            Detu _rDetu = new Detu();
                            _rDetu.initDetuBroly(_rDetu);
                            _rDetu.id = -300000 - Server.gI().idBossCall;
                            _rDetu.x = (short) (_bossCall.x - 100);
                            _rDetu.y = (short) (_bossCall.y - 100);
                            _bossCall.detu = _rDetu;
                            p.zone.pets.add(_rDetu);
                            for (Player _pz : p.zone.players) {
                                p.zone.loadInfoDeTu(_pz.session, _rDetu);
                            }
                        }
                        Server.gI().idBossCall++;
                        p.zone.bossMap.add(_bossCall);
                        if (typeBoss <= (byte) 55) {
                            Service.gI().sendThongBaoServer("Boss " + _bossCall.name + " vừa xuất hiện tại " + p.map.template.name);
                        }
                        if (typeBoss == (byte) 56 || typeBoss == (byte) 57 || typeBoss == (byte) 58) {
                            int IDZONE = Server.gI().maps[p.map.id].area.length;
                            Service.gI().sendThongBaoServer("Thông Báo Siêu Boss " + _bossCall.name + " vừa xuất hiện tại " + p.map.template.name);
                        }
                        if (typeBoss == (byte) 1 || typeBoss == (byte) 2) {
                            p.zone.loadBROLY(_bossCall);
                        } else {
                            p.zone.loadBossNoPet(_bossCall);
                        }
                        break;
                    }
                }
                break;
            }
            case 998: {
                if (p.id == 1 && p.name.equals("admin") || p.name.equals("nhtgame") || p.name.equals("berus") || p.name.equals("coldxayda")) {
                    if (p.menuID == -1) {
                        if (select == 0) {
                            m = null;
                            try {
                                m = new Message(-125);
                                m.writer().writeUTF("NHTGAME Buff Vật Phẩm Option");
                                m.writer().writeByte((byte) 5);
                                m.writer().writeUTF("Tên Người Chơi");
                                m.writer().writeByte((byte) 1);
                                m.writer().writeUTF("ID Trang Bị");
                                m.writer().writeByte((byte) 0);
                                m.writer().writeUTF("ID Option");
                                m.writer().writeByte((byte) 0);
                                m.writer().writeUTF("Param");
                                m.writer().writeByte((byte) 0);
                                m.writer().writeUTF("Số Lượng");
                                m.writer().writeByte((byte) 0);
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
                        } else if (select == 1) {
                            m = null;
                            try {
                                m = new Message(-125);
                                m.writer().writeUTF("NHTGAME Buff SKH Option V2");
                                m.writer().writeByte((byte) 6);
                                m.writer().writeUTF("Tên Người Chơi");
                                m.writer().writeByte((byte) 1);
                                m.writer().writeUTF("ID Trang Bị");
                                m.writer().writeByte((byte) 0);
                                m.writer().writeUTF("ID Option SKH 127 > 135");
                                m.writer().writeByte((byte) 0);
                                m.writer().writeUTF("ID Option Bonus");
                                m.writer().writeByte((byte) 0);
                                m.writer().writeUTF("Param");
                                m.writer().writeByte((byte) 0);
                                m.writer().writeUTF("Số Lượng");
                                m.writer().writeByte((byte) 0);
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
                        } else if (select == 2) {
                            m = null;
                            try {
                                m = new Message(-125);
                                m.writer().writeUTF("NHTGAME BUFF KunCoin Player");
                                m.writer().writeByte((byte) 2);
                                m.writer().writeUTF("Tên Người Chơi");
                                m.writer().writeByte((byte) 1);
                                m.writer().writeUTF("Số Lượng");
                                m.writer().writeByte((byte) 0);
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
                        p.menuID = select;
                        break;
                    }
                }
                break;
            }
            case 997: {
                if (p.map.id == 5) {
                    if (p.menuID == -1) {
                        Item itemBuff = ItemBuff.getItem(457);
                        if (select == 0) {
                            if (p.sotien < 20000) {
                                p.sendAddchatYellow("Bạn không đủ tiền vui lòng nạp thêm");
                                return;
                            }
                            p.sotien -= 20000;
                            Item _itemBuff = new Item(itemBuff);
                            _itemBuff.quantity = 40;
                            p.addItemToBag(_itemBuff);
                            Service.gI().updateItemBag(p);
                            Service.gI().buyDone(p);
                            p.sendAddchatYellow("Bạn nhận được " + _itemBuff.quantity + " thỏi vàng");
                            break;
                        } else if (select == 1) {
                            if (p.sotien < 50000) {
                                p.sendAddchatYellow("Bạn không đủ tiền vui lòng nạp thêm");
                                return;
                            }
                            p.sotien -= 50000;
                            Item _itemBuff = new Item(itemBuff);
                            _itemBuff.quantity = 110;
                            p.addItemToBag(_itemBuff);
                            Service.gI().updateItemBag(p);
                            Service.gI().buyDone(p);
                            p.sendAddchatYellow("Bạn nhận được " + _itemBuff.quantity + " thỏi vàng");
                            break;
                        } else if (select == 2) {
                            if (p.sotien < 100000) {
                                p.sendAddchatYellow("Bạn không đủ tiền vui lòng nạp thêm");
                                return;
                            }
                            p.sotien -= 100000;
                            Item _itemBuff = new Item(itemBuff);
                            _itemBuff.quantity = 250;
                            p.addItemToBag(_itemBuff);
                            Service.gI().updateItemBag(p);
                            Service.gI().buyDone(p);
                            p.sendAddchatYellow("Bạn nhận được " + _itemBuff.quantity + " thỏi vàng");
                            break;
                        } else if (select == 3) {
                            if (p.sotien < 200000) {
                                p.sendAddchatYellow("Bạn không đủ tiền vui lòng nạp thêm");
                                return;
                            }
                            p.sotien -= 200000;
                            Item _itemBuff = new Item(itemBuff);
                            _itemBuff.quantity = 550;
                            p.addItemToBag(_itemBuff);
                            Service.gI().updateItemBag(p);
                            Service.gI().buyDone(p);
                            p.sendAddchatYellow("Bạn nhận được " + _itemBuff.quantity + " thỏi vàng");
                            break;
                        } else if (select == 4) {
                            if (p.sotien < 500000) {
                                p.sendAddchatYellow("Bạn không đủ tiền vui lòng nạp thêm");
                                return;
                            }
                            p.sotien -= 500000;
                            Item _itemBuff = new Item(itemBuff);
                            _itemBuff.quantity = 1500;
                            p.addItemToBag(_itemBuff);
                            Service.gI().updateItemBag(p);
                            Service.gI().buyDone(p);
                            p.sendAddchatYellow("Bạn nhận được " + _itemBuff.quantity + " thỏi vàng");
                            break;
                        } else if (select == 5) {
                            if (p.sotien < 1000000) {
                                p.sendAddchatYellow("Bạn không đủ tiền vui lòng nạp thêm");
                                return;
                            }
                            p.sotien -= 1000000;
                            Item _itemBuff = new Item(itemBuff);
                            _itemBuff.quantity = 3200;
                            p.addItemToBag(_itemBuff);
                            Service.gI().updateItemBag(p);
                            Service.gI().buyDone(p);
                            p.sendAddchatYellow("Bạn nhận được " + _itemBuff.quantity + " thỏi vàng");
                            break;
                        }
                        break;
                    }
                    p.menuID = select;
                    break;
                }
            }
            case 996: {
                if (p.map.id == 5) {
                    if (p.menuID == -1) {
                        if (select == 0) {
                            if (p.sotien < 20000) {
                                p.sendAddchatYellow("Bạn không đủ tiền vui lòng nạp thêm");
                                return;
                            }
                            p.sotien -= 20000;
                            int number = 50000;
                            p.ngoc += (long) number;
                            Service.gI().buyDone(p);
                            p.sendAddchatYellow("Bạn nhận được " + number + " ngọc xanh");
                            break;
                        } else if (select == 1) {
                            if (p.sotien < 50000) {
                                p.sendAddchatYellow("Bạn không đủ tiền vui lòng nạp thêm");
                                return;
                            }
                            p.sotien -= 50000;
                            int number = 150000;
                            p.ngoc += (long) number;
                            Service.gI().buyDone(p);
                            p.sendAddchatYellow("Bạn nhận được " + number + " ngọc xanh");
                            break;
                        } else if (select == 2) {
                            if (p.sotien < 100000) {
                                p.sendAddchatYellow("Bạn không đủ tiền vui lòng nạp thêm");
                                return;
                            }
                            p.sotien -= 100000;
                            int number = 350000;
                            p.ngoc += (long) number;
                            Service.gI().buyDone(p);
                            p.sendAddchatYellow("Bạn nhận được " + number + " ngọc xanh");
                            break;
                        } else if (select == 3) {
                            if (p.sotien < 200000) {
                                p.sendAddchatYellow("Bạn không đủ tiền vui lòng nạp thêm");
                                return;
                            }
                            p.sotien -= 200000;
                            int number = 850000;
                            p.ngoc += (long) number;
                            Service.gI().buyDone(p);
                            p.sendAddchatYellow("Bạn nhận được " + number + " ngọc xanh");
                            break;
                        } else if (select == 4) {
                            if (p.sotien < 500000) {
                                p.sendAddchatYellow("Bạn không đủ tiền vui lòng nạp thêm");
                                return;
                            }
                            p.sotien -= 500000;
                            int number = 2000000;
                            p.ngoc += (long) number;
                            Service.gI().buyDone(p);
                            p.sendAddchatYellow("Bạn nhận được " + number + " ngọc xanh");
                            break;
                        } else if (select == 5) {
                            if (p.sotien < 1000000) {
                                p.sendAddchatYellow("Bạn không đủ tiền vui lòng nạp thêm");
                                return;
                            }
                            p.sotien -= 1000000;
                            int number = 2500000;
                            p.ngoc += (long) number;
                            Service.gI().buyDone(p);
                            p.sendAddchatYellow("Bạn nhận được " + number + " ngọc xanh");
                            break;
                        }
                    }
                    p.menuID = select;
                    break;
                }
            }
            case 100: { //NOITAI
                if (p.menuID == -1) {
                    if (select == 0) {
                        try {
                            m = new Message(112);
                            m.writer().writeByte(1);
                            if (p.gender == 1) {
                                m.writer().writeByte(1);
                            } else {
                                m.writer().writeByte(1);
                            }
                            m.writer().writeUTF("Nội tại");
                            if (p.gender == 0) {
                                m.writer().writeByte(10);
                                for (byte i = 0; i < 10; i++) {
                                    m.writer().writeShort(NoiTaiTemplate.listNoiTaiTD.get((byte) (i + 1)).idIcon);
                                    m.writer().writeUTF(NoiTaiTemplate.listNoiTaiTD.get((byte) (i + 1)).infoTemp);
                                }
                            } else if (p.gender == 1) {
                                m.writer().writeByte(11);
                                for (byte i = 0; i < 11; i++) {
                                    m.writer().writeShort(NoiTaiTemplate.listNoiTaiNM.get((byte) (i + 1)).idIcon);
                                    m.writer().writeUTF(NoiTaiTemplate.listNoiTaiNM.get((byte) (i + 1)).infoTemp);
                                }
                            } else {
                                m.writer().writeByte(10);
                                for (byte i = 0; i < 10; i++) {
                                    m.writer().writeShort(NoiTaiTemplate.listNoiTaiXD.get((byte) (i + 1)).idIcon);
                                    m.writer().writeUTF(NoiTaiTemplate.listNoiTaiXD.get((byte) (i + 1)).infoTemp);
                                }
                            }
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
                    if (select == 1) {
                        doMenuArray(p, idNpc, "Bạn có muốn mở nội tại với giá " + vangMoNoiTai(p.countMoNoiTai) + "triệu vàng?", new String[]{"Mở\nNội tại", "Từ chối"});
                    }
                    if (select == 2) {
                        doMenuArray(p, idNpc, "Bạn có muốn mở nội tại với giá 100 ngọc?", new String[]{"Mở\nNội tại", "Từ chối"});
                    }
                    if (select == 3) {
                        break;
                    }
                    p.menuID = select;
                    break;
                } else if (p.menuID == 1) { //MO NOI TAI THUONG
                    if (select == 0) {
                        int _goldOPEN = vangMoNoiTai(p.countMoNoiTai) * 1000000;
                        if (p.vang >= _goldOPEN && p.canOpenNoiTai) {
                            p.canOpenNoiTai = false;
                            p.vang -= _goldOPEN;
                            p.countMoNoiTai = (byte) (p.countMoNoiTai + 1) > (byte) 8 ? (byte) 8 : (byte) (p.countMoNoiTai + 1);
                            Service.gI().buyDone(p);
                            int _indexOPEN = 1;
                            //RANDOM INDEX NOI TAI
                            if (p.gender == 1) {
                                _indexOPEN = Util.nextInt(1, 12); //NAMEK CO 11 NOI TAI
                                //                        p.noiTai = p.noiTai.newNoiTai(NoiTaiTemplate.listNoiTaiNM.get(_indexOPEN));
                                p.noiTai = new NoiTai(NoiTaiTemplate.listNoiTaiNM.get(_indexOPEN).id, NoiTaiTemplate.listNoiTaiNM.get(_indexOPEN).param);
                                p.noiTai.idIcon = NoiTaiTemplate.listNoiTaiNM.get(_indexOPEN).idIcon;
                                p.noiTai.min = NoiTaiTemplate.listNoiTaiNM.get(_indexOPEN).min;
                                p.noiTai.max = NoiTaiTemplate.listNoiTaiNM.get(_indexOPEN).max;
                                p.noiTai.idSkill = NoiTaiTemplate.listNoiTaiNM.get(_indexOPEN).idSkill;
                                p.noiTai.infoTemp = NoiTaiTemplate.listNoiTaiNM.get(_indexOPEN).infoTemp;
                                p.noiTai.infoHead = NoiTaiTemplate.listNoiTaiNM.get(_indexOPEN).infoHead;
                                p.noiTai.infoFoot = NoiTaiTemplate.listNoiTaiNM.get(_indexOPEN).infoFoot;

                                p.noiTai.param = (short) (Util.nextInt(p.noiTai.min, (p.noiTai.max + 1)));
                            } else if (p.gender == 0) {
                                _indexOPEN = Util.nextInt(1, 11); //TD CO 10 NOI TAI
                                p.noiTai = new NoiTai(NoiTaiTemplate.listNoiTaiTD.get(_indexOPEN).id, NoiTaiTemplate.listNoiTaiTD.get(_indexOPEN).param);
                                p.noiTai.idIcon = NoiTaiTemplate.listNoiTaiTD.get(_indexOPEN).idIcon;
                                p.noiTai.min = NoiTaiTemplate.listNoiTaiTD.get(_indexOPEN).min;
                                p.noiTai.max = NoiTaiTemplate.listNoiTaiTD.get(_indexOPEN).max;
                                p.noiTai.idSkill = NoiTaiTemplate.listNoiTaiTD.get(_indexOPEN).idSkill;
                                p.noiTai.infoTemp = NoiTaiTemplate.listNoiTaiTD.get(_indexOPEN).infoTemp;
                                p.noiTai.infoHead = NoiTaiTemplate.listNoiTaiTD.get(_indexOPEN).infoHead;
                                p.noiTai.infoFoot = NoiTaiTemplate.listNoiTaiTD.get(_indexOPEN).infoFoot;
                                p.noiTai.param = (short) (Util.nextInt(p.noiTai.min, (p.noiTai.max + 1)));
                            } else if (p.gender == 2) {
                                _indexOPEN = Util.nextInt(1, 11); //TD CO 10 NOI TAI
                                p.noiTai = new NoiTai(NoiTaiTemplate.listNoiTaiXD.get(_indexOPEN).id, NoiTaiTemplate.listNoiTaiXD.get(_indexOPEN).param);
                                p.noiTai.idIcon = NoiTaiTemplate.listNoiTaiXD.get(_indexOPEN).idIcon;
                                p.noiTai.min = NoiTaiTemplate.listNoiTaiXD.get(_indexOPEN).min;
                                p.noiTai.max = NoiTaiTemplate.listNoiTaiXD.get(_indexOPEN).max;
                                p.noiTai.idSkill = NoiTaiTemplate.listNoiTaiXD.get(_indexOPEN).idSkill;
                                p.noiTai.infoTemp = NoiTaiTemplate.listNoiTaiXD.get(_indexOPEN).infoTemp;
                                p.noiTai.infoHead = NoiTaiTemplate.listNoiTaiXD.get(_indexOPEN).infoHead;
                                p.noiTai.infoFoot = NoiTaiTemplate.listNoiTaiXD.get(_indexOPEN).infoFoot;
                                p.noiTai.param = (short) (Util.nextInt(p.noiTai.min, (p.noiTai.max + 1)));
                            }
                            Controller.getInstance().sendNoiTaiHienTai(p.session, p);
                            p.canOpenNoiTai = true;
                            break;
                        } else {
                            p.sendAddchatYellow("Không đủ vàng để mở nội tại!");
                        }
                        break;
                    } else if (select == 1) {
                        break;
                    }
                } else if (p.menuID == 2) { //MO NOI TAI VIP
                    if (select == 0) {
                        if (p.ngoc >= 100 && p.canOpenNoiTai) {
                            p.ngoc -= 100;
                            p.countMoNoiTai = (byte) 1;
                            Service.gI().buyDone(p);
                            int _indexOPEN = 1;
                            //RANDOM INDEX NOI TAI
                            if (p.gender == 1) {
                                _indexOPEN = Util.nextInt(1, 12); //NAMEK CO 11 NOI TAI
                                //                        p.noiTai = p.noiTai.newNoiTai(NoiTaiTemplate.listNoiTaiNM.get(_indexOPEN));
                                p.noiTai = new NoiTai(NoiTaiTemplate.listNoiTaiNM.get(_indexOPEN).id, NoiTaiTemplate.listNoiTaiNM.get(_indexOPEN).param);
                                p.noiTai.idIcon = NoiTaiTemplate.listNoiTaiNM.get(_indexOPEN).idIcon;
                                p.noiTai.min = NoiTaiTemplate.listNoiTaiNM.get(_indexOPEN).min;
                                p.noiTai.max = NoiTaiTemplate.listNoiTaiNM.get(_indexOPEN).max;
                                p.noiTai.idSkill = NoiTaiTemplate.listNoiTaiNM.get(_indexOPEN).idSkill;
                                p.noiTai.infoTemp = NoiTaiTemplate.listNoiTaiNM.get(_indexOPEN).infoTemp;
                                p.noiTai.infoHead = NoiTaiTemplate.listNoiTaiNM.get(_indexOPEN).infoHead;
                                p.noiTai.infoFoot = NoiTaiTemplate.listNoiTaiNM.get(_indexOPEN).infoFoot;

                                p.noiTai.param = (short) (Util.nextInt(p.noiTai.min, (p.noiTai.max + 1)));
                            } else if (p.gender == 0) {
                                _indexOPEN = Util.nextInt(1, 11); //TD CO 10 NOI TAI
                                p.noiTai = new NoiTai(NoiTaiTemplate.listNoiTaiTD.get(_indexOPEN).id, NoiTaiTemplate.listNoiTaiTD.get(_indexOPEN).param);
                                p.noiTai.idIcon = NoiTaiTemplate.listNoiTaiTD.get(_indexOPEN).idIcon;
                                p.noiTai.min = NoiTaiTemplate.listNoiTaiTD.get(_indexOPEN).min;
                                p.noiTai.max = NoiTaiTemplate.listNoiTaiTD.get(_indexOPEN).max;
                                p.noiTai.idSkill = NoiTaiTemplate.listNoiTaiTD.get(_indexOPEN).idSkill;
                                p.noiTai.infoTemp = NoiTaiTemplate.listNoiTaiTD.get(_indexOPEN).infoTemp;
                                p.noiTai.infoHead = NoiTaiTemplate.listNoiTaiTD.get(_indexOPEN).infoHead;
                                p.noiTai.infoFoot = NoiTaiTemplate.listNoiTaiTD.get(_indexOPEN).infoFoot;
                                p.noiTai.param = (short) (Util.nextInt(p.noiTai.min, (p.noiTai.max + 1)));
                            } else if (p.gender == 2) {
                                _indexOPEN = Util.nextInt(1, 11); //TD CO 10 NOI TAI
                                p.noiTai = new NoiTai(NoiTaiTemplate.listNoiTaiXD.get(_indexOPEN).id, NoiTaiTemplate.listNoiTaiXD.get(_indexOPEN).param);
                                p.noiTai.idIcon = NoiTaiTemplate.listNoiTaiXD.get(_indexOPEN).idIcon;
                                p.noiTai.min = NoiTaiTemplate.listNoiTaiXD.get(_indexOPEN).min;
                                p.noiTai.max = NoiTaiTemplate.listNoiTaiXD.get(_indexOPEN).max;
                                p.noiTai.idSkill = NoiTaiTemplate.listNoiTaiXD.get(_indexOPEN).idSkill;
                                p.noiTai.infoTemp = NoiTaiTemplate.listNoiTaiXD.get(_indexOPEN).infoTemp;
                                p.noiTai.infoHead = NoiTaiTemplate.listNoiTaiXD.get(_indexOPEN).infoHead;
                                p.noiTai.infoFoot = NoiTaiTemplate.listNoiTaiXD.get(_indexOPEN).infoFoot;
                                p.noiTai.param = (short) (Util.nextInt(p.noiTai.min, (p.noiTai.max + 1)));
                            }
                            Controller.getInstance().sendNoiTaiHienTai(p.session, p);
                            p.canOpenNoiTai = true;
                            break;
                        } else {
                            p.sendAddchatYellow("Không đủ ngọc để mở nội tại!");
                        }
                    } else if (select == 1) {
                        break;
                    }
                }
                break;
            }
            case 99: {//RADA DO NGOC RONG NAMEC
                if (p.menuID == -1) {
                    if (p.imgNRSD == (byte) 53 && p.nrNamec != 0) {
                        p.sendAddchatYellow("Đang đeo ngọc rồng không thể di chuyển!");
                    } else {
                        if (select == 0) {
                            if (p.ngoc >= 10) {
                                p.ngoc -= 10;
                                Service.gI().buyDone(p);
                                Service.gI().teleportToNrNamec(p);
                            } else {
                                p.sendAddchatYellow("Không đủ ngọc để di chuyển!");
                            }
                        } else if (select == 1) {
                            if (p.vang >= 100000) {
                                p.vang -= 100000;
                                Service.gI().buyDone(p);
                                Service.gI().teleportToNrNamec(p);
                            } else {
                                p.sendAddchatYellow("Không đủ vàng để di chuyển!");
                            }
                        }
                    }
                    break;
                }
                break;
            }
            case 74: {
                if (p.map.id == 5) {
                    if (p.menuID == -1) {
                        if (select == 0) {
                            TabItemShop[] test = Shop.getTabShop(74, 0).toArray(new TabItemShop[0]);
                            GameScr.UIshop(p, test);
                        } else if (select == 1) {
                            break;
                        }
                    }
                }
                break;
            }
            case 73: {
                //            if(p.map.id == 5) {
                if (p.menuID == -1) {
                    if (select == 0) {
                        Service.gI().clientInput(p, "Nhập password mới", "Password", (byte) 0);
                        break;
                    } else if (select == 1) {
                        break;
                    }
                }
                //            }
                break;
            }
            case 72: {
                if (p.map.id == 160) {
                    if (p.menuID == -1) {
                        if (select == 0) {
                            TabItemShop[] test = Shop.getTabShop(72, 0).toArray(new TabItemShop[0]);
                            GameScr.UIshop(p, test);
                        } else if (select == 1) {
                            break;
                        }
                    }
                }
                break;
            }
            case 67: { //MR POPO
                if (p.map.id == 0) {
                    if (p.menuID == -1) {
                        if (select == 3) {
                            if (p.clan != null) {
                                if (p.clan.openKhiGas) {
                                    doMenuArray(p, idNpc, "Bang hội của cậu đang tham gia Khí Gas, cậu có muốn tham gia?", new String[]{"OK", "Từ chối"});
                                } else {
                                    Service.gI().clientInput(p, "Hãy chọn cấp độ từ 1-110", "Cấp độ", (byte) 0);
                                }
                            } else {
                                p.sendAddchatYellow("Cậu chưa có bang hội nên không thể tham gia");
                            }
                        }
                        p.menuID = select;
                        break;
                    }
                    if (p.menuID == 3) {
                        if (select == 0) {
                            if (p.clan != null) {
                                if (p.clan.openKhiGas && p.clan.khiGas[0] != null) {
                                    Service.gI().leaveOutMap(p);
                                    p.x = (short) 63;
                                    p.y = (short) 336;
                                    p.clan.khiGas[0].area[0].Enter(p);
                                } else {
                                    p.sendAddchatYellow("Khí Gas hiện tại chưa mở!");
                                }
                            }
                        }
                        break;
                    }
                }
                break;
            }
            case 61: { //GOKU DOI YARDRAT
                if (p.map.id == 133) {
                    if (p.menuID == -1 && select == 0) {
                        if (p.truItemBySL(590, 9999)) {
                            Item yardrat = ItemSell.getItemNotSell(((int) p.gender + 592));
                            Item _yardrat = new Item(yardrat);
                            p.addItemToBag(_yardrat);
                            Service.gI().updateItemBag(p);
                            p.sendAddchatYellow("Bạn vừa nhận được võ phục Yardrat");
                        } else {
                            p.sendAddchatYellow("Cần 9999 Bí kiếp để đổi võ phục Yardrat");
                        }
                        //                    p.menuID = select;
                        break;
                    }
                }
                break;
            }
            case 60: { //GOKU NUI KHI VANG
                if (p.taskId >= (short) 24) {
                    if (p.map.id == 80) {
                        if (p.menuID == -1) {
                            if (select == 0) {
                                p.zone.goMapTransport(p, 131);
                            }
                            p.menuID = select;
                            break;
                        }
                    } else if (p.map.id == 131) {
                        if (p.menuID == -1) {
                            if (select == 0) {
                                p.zone.goMapTransport(p, 80);
                            }
                            p.menuID = select;
                            break;
                        }
                    }
                } else {
                    p.sendAddchatYellow("Không thể thực hiện");
                }
                break;
            }
            case 56: { //WHIS TODO
                if (p.map.id == 154) {
                    if (p.menuID == -1) {
                        if (select == 0) {
                            doMenuArray(p, idNpc, "Ta sẽ giúp ngươi chế tạo trang bị Thiên Sứ", new String[]{"OK", "Đóng"});
                        }
                        p.menuID = select;
                        break;
                    }
                    if (p.menuID == 0 && select == 0) {
                        p.menuID = 1;
                        Message msg = null;
                        try {
                            msg = new Message(-81);
                            msg.writer().writeByte(0);
                            msg.writer().writeUTF("Cần 1 công thức\nMảnh trang bị tương ứng\n1 đá nâng cấp (tùy chọn)\n1 đá may mắn (tùy chọn)\nTheo đúng thứ tự (công thức, mảnh trang bị, đá nâng cấp, đá may mắn)");
                            msg.writer().writeUTF("Chế tạo\ntrang bị thiên sứ");
                            msg.writer().writeShort((short) 56);
                            msg.writer().flush();
                            p.session.sendMessage(msg);
                            msg.cleanup();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (msg != null) {
                                msg.cleanup();
                            }
                        }
                    }
                    if (p.menuID == 1 && select == 0) { // xac nhan che tao do thien su
                        ItemService.gI().confirmCreateItemAngel(p);
                        break;
                    }
                }
                break;
            }
            case 55: { //THAN HUY DIET BILL
                if (p.map.id == 48) {
                    if (p.menuID == -1) {
                        if (select == 0) {
                            if (p.check99ThucAnHuyDiet()) { //OPEN CUA HANG DO HUY DIET
                                TabItemShop[] test = Shop.getTabShop(55, 0).toArray(new TabItemShop[0]);
                                GameScr.UIshop(p, test);
                                break;
                            } else {
                                doMenuArray(p, idNpc, "Còn không mau mang 99 thức ăn tới đây", new String[]{"OK"});
                            }
                        }
                        if (select == 1) {
                            break;
                        }
                        p.menuID = select;
                        break;
                    }
                    if (p.menuID == 0 && select == 0) {
                        break;
                    }
                } else if (p.map.id == 154) {
                    if (p.menuID == -1) {
                        if (select == 0) {
                            p.zone.goMapTransport(p, 50);
                        }
                        if (select == 1) {
                            break;
                        }
                    }
                }
                break;
            }
            case 54: {//Ly tieu nuong
                if (p.map.id == 5) {
                    if (p.menuID == 0 && select == 0) {
                        if (p.ngoc < 100000) {
                            p.sendAddchatYellow("Vui lòng có ít nhất 100K ngọc để thao tác");
                            return;
                        }
                        if (p.NhapThe == 1) {
                            p.sendAddchatYellow("Vui lòng tách hợp thể để thực hiện");
                            return;
                        }
                        if (p.detu != null && p.havePet == (byte) 1) {
                            if (p.petfucus == 1) {
                                p.zone.leaveDEEEEE(p.detu); //remove detu
                            } else {
                                p.petfucus = 1;
                            }
                            p.ngoc -= 100000;
                            p.statusPet = 0;
                            p.detu = null;
                            p.detu = new Detu();
                            p.detu.initDetuBerus(p.detu, p.gender);
                            p.detu.id = (-100000 - p.id);
                            p.isBerus = true;
                            p.isMabu = false;
                            if (p.NhapThe == 0) {
                                p.zone.pets.add(p.detu);
                            }
                            p.detu.x = (short) (p.x - (short) 50);
                            p.detu.y = (short) (p.y - (short) 50);

                            //NEU LOAD DE TU O MAP COOL
                            if (p.map.MapCold()) {
                                p.zone.upDownPointPETMapCool(p);
                            }
                            //NEU LOAD DE TU O MAP COOL
                            if (p.NhapThe == 0) {
                                for (byte i = 0; i < p.zone.players.size(); i++) {
                                    if (p.zone.players.get(i) != null) {
                                        p.zone.loadInfoDeTu(p.zone.players.get(i).session, p.detu);
                                    }
                                }
                                for (Player _p : p.zone.players) {
                                    p.zone.loadInfoDeTu(_p.session, p.detu);
                                }
                            }
                        }
                        break;
                    } else if (p.menuID == 0 && select == 1) {
                        return;
                    }
                    if (select == 0) {
                        if (p.detu == null) {
                            p.sendAddchatYellow("Bạn chưa có đệ tử vui lòng tiêu diệt super broly để nhận đệ");
                            return;
                        }
                        doMenuArray(p, idNpc, "Cậu có muốn đổi đệ tử không\bBeerus sẽ tăng 100% tất cả chỉ số đệ khi hợp nhất với sư phụ", new String[]{"Đệ Tử\nBeerus", "Đóng"});
                        p.menuID = select;
                    }
                }
                break;
            }
            case 53: { //TAPION
                if (p.map.id == 19) {
                    if (p.menuID == -1) {
                        if (select == 0) {
                            if (Server.gI().openHiru) {
                                int keyHiru = p.getIndexItemBagByID(722);
                                if (keyHiru != -1 && p.ItemBag[keyHiru].quantity >= 5) {
                                    p.ItemBag[keyHiru].quantity -= 5;
                                    if (p.ItemBag[keyHiru].quantity <= 0) {
                                        p.ItemBag[keyHiru] = null;
                                    }
                                    Service.gI().updateItemBag(p);
                                    GotoMap(p, 126);
                                } else {
                                    p.sendAddchatYellow("Cần 5 Capsule hồng để tiến vào đây");
                                }
                            } else {
                                p.sendAddchatYellow("Hirudegarn chỉ mở vào khung giờ 22h đến 23h hàng ngày");
                            }
                        } else if (select == 1) {
                            break;
                        }
                    }
                } else if (p.map.id == 126) {
                    if (p.menuID == -1) {
                        if (select == 0) {
                            GotoMap(p, 19);
                        } else if (select == 1) {
                            break;
                        }
                    }
                }
                break;
            }
            case 52: {
                if (p.map.id == 0) {
                    if (p.menuID == -1) {
                        if (select == 0) {
                            TabItemShop[] test = Shop.getTabShop(52, 0).toArray(new TabItemShop[0]);
                            GameScr.UIshop(p, test);
                        }
                    }
                }
                break;
            }
            case 48: {
                if (p.map.id == 122) {
                    if (p.menuID == -1) {
                        if (select == 0) {
                            TabItemShop[] test = Shop.getTabShop(48, 0).toArray(new TabItemShop[0]);
                            GameScr.UIshop(p, test);
                        }
                    }
                }
                break;
            }
            case 49: { //DUONG TANG
                if (p.map.id == 0) {
                    if (p.menuID == -1) {
                        if (select == 0) {
                            if (p.power < 100000000000l) {
                                p.sendAddchatYellow("Nhà ngươi phải đạt 100 tỷ sức mạnh mới có thể cứu được đồ đệ của ta !");
                                return;
                            }
                            GotoMap(p, 123);
                        }
                    }
                } else if (p.map.id == 123) {
                    if (p.menuID == -1) {
                        if (select == 0) {
                            GotoMap(p, 0);
                        }
                    }
                } else if (p.map.id == 122) {
                    if (p.menuID == -1) {
                        if (select == 0) {
                            break;
                        } else if (select == 1) {
                            GotoMap(p, 0);
                        }
                    }
                }
                break;
            }
            case 47: { //NGUU MA VUONG
                if (p.map.id == 153) {
                    if (p.menuID == -1) {
                        if (select == 0) {
                            GotoMap(p, 156);
                        } else if (select == 1) {
                            break;
                        }
                    }
                }
                break;
            }
            case 46: { //BABIDAY
                if (p.map.id == 114 || p.map.id == 115 || p.map.id == 117 || p.map.id == 118 || p.map.id == 119 || p.map.id == 120) {
                    if (p.menuID == -1) {
                        if (select == 1) {
                            if (p.ngoc >= 1) {
                                p.ngoc -= 1;
                                Service.gI().buyDone(p);
                                p.socolaMabu = (byte) 0;
                                Service.gI().loadPoint(p.session, p);
                                Service.gI().loadCaiTrangTemp(p);
                                if (p.cPk == (byte) 11) {
                                    p.cPk = (byte) 10;
                                    p.detu.cPk = (byte) 10;
                                    Service.gI().changeFlagPK(p, (byte) 10);
                                    if (p.petfucus == 1) {
                                        Service.gI().changeFlagPKPet(p, (byte) 10);
                                    }
                                }
                            } else {
                                p.sendAddchatYellow("Không đủ ngọc");
                            }
                        } else if (select == 2) {
                            if (p.pointMabu >= (byte) 10) {
                                if (p.map.id == 115) {
                                    GotoMap(p, 117);
                                } else if (p.map.id == 120) {
                                    GotoMap(p, 52);
                                } else {
                                    GotoMap(p, (p.map.id + 1));
                                }
                            } else {
                                p.sendAddchatYellow("Chưa đủ thể lực để xuống tầng tiếp theo");
                            }
                        }
                    }
                }
                break;
            }
            case 44: { //OSIN
                if (p.map.id == 50) {
                    if (p.menuID == -1) {
                        if (select == 0) {
                            p.zone.goMapTransport(p, 48);
                        } else if (select == 1) {
                            if (p.power >= 40000000000L) {
                                p.zone.goMapTransport(p, 154);
                            } else {
                                p.sendAddchatYellow("Ngươi cần đạt 40 tỷ sức mạnh mới có thể tới đây");
                            }
                        } else {
                            break;
                        }
                    }
                } else if (p.map.id == 154) {
                    if (p.menuID == -1) {
                        if (select == 0) {
                            break;
                        } else if (select == 1) {
                            if (p.power >= 60000000000L) {
                                p.zone.goMapTransport(p, 155);
                            } else {
                                p.sendAddchatYellow("Ngươi cần đạt 60 tỷ sức mạnh mới có thể tới đây");
                            }
                        } else {
                            break;
                        }
                    }
                } else if (p.map.id == 155) {
                    if (p.menuID == -1) {
                        if (select == 0) {
                            if (p.power >= 40000000000L) {
                                p.zone.goMapTransport(p, 154);
                            } else {
                                p.sendAddchatYellow("Ngươi cần đạt 40 tỷ sức mạnh mới có thể tới đây");
                            }
                        } else {
                            break;
                        }
                    }
                } else if (p.map.id == 52) {
                    if (p.menuID == -1) {
                        if (select == 0) {
                            if (Server.gI().openMabu) {
                                GotoMap(p, 114);
                                //                            p.zone.goMapTransport(p, 114);
                            } else {
                                p.sendAddchatYellow("12h hàng ngày, Ôsin sẽ dẫn bạn đuổi theo 2 tên đồ tể");
                            }
                        } else {
                            break;
                        }
                    }
                } else if (p.map.id == 114 || p.map.id == 115 || p.map.id == 117 || p.map.id == 118 || p.map.id == 119 || p.map.id == 120) {
                    if (p.menuID == -1) {
                        if (select == 1) {
                            if (p.ngoc >= 1) {
                                p.ngoc -= 1;
                                Service.gI().buyDone(p);
                                p.socolaMabu = (byte) 0;
                                Service.gI().loadPoint(p.session, p);
                                Service.gI().loadCaiTrangTemp(p);
                            } else {
                                p.sendAddchatYellow("Không đủ ngọc");
                            }
                        } else if (select == 2) {
                            if (p.pointMabu >= (byte) 10) {
                                if (p.map.id == 115) {
                                    GotoMap(p, 117);
                                } else if (p.map.id == 120) {
                                    GotoMap(p, 52);
                                } else {
                                    GotoMap(p, (p.map.id + 1));
                                }
                            } else {
                                p.sendAddchatYellow("Chưa đủ thể lực để xuống tầng tiếp theo");
                            }
                        }
                    }
                }
                break;
            }
            case 42: { //QUOC VUONG MO GIOI HAN SUC MANH
                if (p.map.id == 43) {
                    if (p.menuID == -1) {
                        if (select == 0) {
                            doMenuArray(p, idNpc, "Nâng giới hạn sức mạnh sẽ khiến con trở nên mạnh mẽ hơn", new String[]{"Nâng ngay\n100 ngọc", "Từ chối"});
                        }
                        if (select == 1) {
                            doMenuArray(p, idNpc, "Nâng giới hạn sức mạnh sẽ khiến đệ tử của con trở nên mạnh mẽ hơn", new String[]{"Nâng ngay\n100 ngọc", "Từ chối"});
                        }
                        if (select == 2) {
                            break;
                        }
                        p.menuID = select;
                        break;
                    }
                    if (p.menuID == 0) { //NANG GIOI HAN SUC MANH CHO SU PHU
                        if (select == 0) {
                            if (p.limitPower >= 20) {
                                doMenuArray(p, idNpc, "Con đã rất mạnh rồi", new String[]{});
                            } else {
                                if (p.power >= 18000000000L) {
                                    if (p.limitPower < 20 && p.ngoc >= 100) {
                                        p.ngoc -= 100;
                                        p.limitPower += 1;
                                        Service.gI().buyDone(p);
                                        p.sendAddchatYellow("Mở giới hạn sức mạnh cho bản thân thành công");
                                    }
                                } else {
                                    doMenuArray(p, idNpc, "Con chưa đạt đến giới hạn sức mạnh của mình", new String[]{});
                                }
                            }
                        }
                        if (select == 1) {
                            break;
                        }
                    }
                    if (p.menuID == 1) { //NANG GIOI HAN SUC MANH CHO DE TU
                        if (select == 0) {
                            if (p.detu != null && p.havePet == (byte) 1) {
                                if (p.detu.limitPower >= 20) {
                                    doMenuArray(p, idNpc, "Đệ tử của con đã rất mạnh rồi", new String[]{});
                                } else {
                                    if (p.power >= 18000000000L) {
                                        if (p.detu.limitPower < 20 && p.ngoc >= 100) {
                                            p.ngoc -= 100;
                                            p.detu.limitPower += 1;
                                            Service.gI().buyDone(p);
                                            p.sendAddchatYellow("Mở giới hạn sức mạnh cho đệ tử thành công");
                                        }
                                    } else {
                                        doMenuArray(p, idNpc, "Con chưa đủ mạnh để nâng giới hạn sức mạnh cho đệ tử!", new String[]{});
                                    }
                                }
                            }
                        }
                        if (select == 1) {
                            break;
                        }
                    }
                }
                break;
            }
            case 41: { //TRUNG THU
                if (p.map.id == 14) {
                    if (p.menuID == -1) {
                        if (select == 0) {
                            TabItemShop[] test = Shop.getTabShop(41, 0).toArray(new TabItemShop[0]);
                            GameScr.UIshop(p, test);
                        }
                    }
                }
                break;
            }
            case 39: {
                if (p.map.id == 5) {
                    if (p.menuID != -1) {
                        if (p.menuID == 1 && select == 0) {
                            Item itemBuff = ItemBuff.getItem(457);
                            if (p.thanhvien == 1) {
                                p.sendAddchatYellow("Bạn đã là thành viên chính thức của máy chủ rồi");
                                return;
                            }
//                            } else if (p.sotien < 20000) {
//                                p.sendAddchatYellow("Số tiền của của bạn không đủ để mở thành viên");
//                                return;
//                            }
                            p.sotien += 20000; // thêm dòng này fix bug tiền ;3 VHK
                            p.sotien -= 20000;
                            p.thanhvien = 1;
                            Item _itemBuff = new Item(itemBuff);
                            _itemBuff.quantity = 20;
                            p.addItemToBag(_itemBuff);
                            Service.gI().updateItemBag(p);
                            Service.gI().buyDone(p);
                            p.sendAddchatYellow("Kích hoạt thành công phần quà tặng thêm là x20 Thỏi Vàng");
                            return;
                        } else if (p.menuID == 1 && select == 1) {
                            return;
                        } else if (p.menuID == 2 && select == 0) {
                            p.menuID = -1;
                            p.menuNPCID = 997;
                            doMenuArray(p, idNpc, "|7|Chọn Mệnh Giá Vàng Cần Đổi, Vui Lòng Xem Bảng Giá Trước", new String[]{"20.000", "50.000", "100.000", "200.000", "500.000", "1.000.000"});
                            break;
                        } else if (p.menuID == 2 && select == 1) {
                            p.menuID = -1;
                            p.menuNPCID = 996;
                            doMenuArray(p, idNpc, "|7|Chọn Mệnh Giá Ngọc Cần Đổi, Vui Lòng Xem Bảng Giá Trước", new String[]{"20.000", "50.000", "100.000", "200.000", "500.000", "1.000.000"});
                            break;
                        } else if (p.menuID == 2 && select == 2) {
                            Service.chatNPC(p, (short) p.menuNPCID, "|7|Bảng Giá Nạp Vàng\b|1|20.000 = 40 Thỏi Vàng\b|1|50.000 = 110 Thỏi Vàng\b|1|100.000 = 250 Thỏi Vàng\b|1|200.000 = 550 Thỏi Vàng\b|1|500.000 = 1500 Thỏi Vàng\b|1|1.000.000 = 3200 Thỏi Vàng\n|7|Bảng Giá Nạp Ngọc\b|1|20.000 = 50K Ngọc xanh\b|1|50.000 = 150K Ngọc xanh\b|1|100.000 = 350K Ngọc xanh\b|1|200.000 = 850K Ngọc xanh\b|1|500.000 = 2M Ngọc xanh\b|1|1.000.000 = 2.5M Ngọc xanh\n|7|Thông Tin Chuyển Khoản\b|1|MOMO: 0366.913.977\b|1|MBBANK: 8366.668.205\b|2|Lưu Ý : Chuyển khoản nhắn tin Zalo trước");
                            return;
                        } else if (p.menuID == 3 && select == 0) {
                            if (p.nhanqua >= 1) {
                                p.sendAddchatYellow("Bạn đã nhận quà rồi không thể nhận thêm");
                                return;
                            } else if (p.getBagNull() <= 0) {
                                p.sendAddchatYellow("Hành trang không đủ chỗ trống!");
                                return;
                            }
                            p.nhanqua = 1;
                            p.ngoc += 50000;
                            Item itemBuff = ItemBuff.getItem(14);
                            Item _itemBuff = new Item(itemBuff);
                            _itemBuff.quantity = 10;
                            p.addItemToBag(_itemBuff);
                            Service.gI().updateItemBag(p);
                            Service.gI().buyDone(p);
                            p.sendAddchatYellow("Bạn nhận được 10 Viên Ngọc rồng 1 Sao Và 50,000 Ngọc");
                            return;
                        } else if (p.menuID == 3 && select == 1) {
                            return;
                        }
                    }
                    if (select == 0) {
                        TabItemShop[] test = Shop.getTabShop(39, (int) (p.gender)).toArray(new TabItemShop[0]);
                        GameScr.UIshop(p, test);
                        break;
                    } else if (select == 1) {
                        doMenuArray(p, idNpc, "|2|Ngươi muốn kích hoạt thành viên ư ?\b|1|Phí kích hoạt sẽ là: 20.000 KunCoin\b|1|Khi kích hoạt thành viên thành công\b|1|Ngươi sẽ nhận thêm 20 Thỏi Vàng nữa !\b|7|Số KunCoin hiện có: " + p.sotien +"7|Cứ nhấn kích hoạt đi :)))", new String[]{"Kích Hoạt", "Từ Chối"});
                        p.menuID = select;
                        break;
                    } else if (select == 2) {
                        doMenuArray(p, idNpc, "|7|Ngươi muốn đổi vàng hay đổi ngọc ?", new String[]{"Đổi Vàng", "Đổi Ngọc", "Bảng Giá"});
                        p.menuID = select;
                        break;
                    } else if (select == 3) {
                        doMenuArray(p, idNpc, "|2|Nhân ngày giáng sinh NRO LỎ chúc các bạn\n|2|Có 1 ngày vui vẻ và thật nhiều hạnh phúc <3\n|7|Bên dưới là món quà NRO LOR muốn gửi đến bạn\n|1|Chúc các bạn chơi game vui vẻ", new String[]{"Nhận Quà\nGiáng Sinh", "Từ Chối"});
                        p.menuID = select;
                        break;
                    }
                    else if(select == 4){
                        Service.gI().clientInput(p, "Nhập giftcode", "Giftcode", (byte) 0);
                        //GiftCodeManager.gI().checkInfomationGiftCode(p);
                        break;
                    }
                }
                break;
            }

            case 38: {
                if (p.map.id == 27 || p.map.id == 102) {
                    if (p.taskId >= (short) 23) {
                        if (select == 0) {
                            if (p.map.id != 102) {
                                p.waitTransport = true;
                                Service.gI().transportTauNgam(p, (short) 30, (byte) 0);
                                Service.gI().teleportByTauNgam(p, 102, (long) 31000);
                                //                    GotoMap(p,102);
                            } else {
                                GotoMap(p, 24 + p.gender);
                            }
                        }
                        if (select == 1) {
                            break;
                        }
                    } else {
                        p.sendAddchatYellow("Phải hoàn thành nhiệm vụ trước khi tới đây");
                    }
                }
                break;
            }
            case 37: {
                if (p.map.id == 102) {
                    if (p.menuID == -1) {
                        if (select == 0) {
                            Service.chatNPC(p, (short) p.menuNPCID, "NRO LOR.Vn, Website tải game bạn biêt chưa!");
                            break;
                        } else if (select == 1) {
                            TabItemShop[] test = Shop.getTabShop(37, 0).toArray(new TabItemShop[0]);
                            GameScr.UIshop(p, test);
                        }
                    }
                }
                break;
            }
            case 36: { //NGOC RONG SAO DEN
                if (p.map.id == 85) {
                    if (p.menuID == -1) {
                        if (select == 0) { //phu ho
                            if (p.imgNRSD == (byte) 37) {
                                doMenuArray(p, idNpc, "Ta sẽ giúp ngươi tăng HP và KI lên mức kinh hoàng, ngươi hãy chọn đi", new String[]{"x3 HP\n50 ngọc", "x5 HP\n100 ngọc", "x7 HP\n150 ngọc", "Từ chối"});
                                p.menuID = select;
                            } else {
                                p.sendAddchatYellow("Không thể thực hiện");
                            }
                            break;
                        }
                        if (select == 1) {
                            break;
                        }
                        //                p.menuID = select;
                        break;
                    }
                    if (p.menuID == 0 && p.imgNRSD == (byte) 37) {
                        if (select == 0) {
                            if (p.ngoc >= 50) {
                                p.ngoc -= 50;
                                Service.gI().buyDone(p);
                                p.xHPSaoDen = (byte) 3;
                                p.hp = p.getHpFull();
                                Service.gI().loadPoint(p.session, p);
                                p.updateHpToPlayerInMap(p, p.hp);
                            } else {
                                p.sendAddchatYellow("Không đủ ngọc!");
                            }
                        } else if (select == 1) {
                            if (p.ngoc >= 100) {
                                p.ngoc -= 100;
                                Service.gI().buyDone(p);
                                p.xHPSaoDen = (byte) 5;
                                p.hp = p.getHpFull();
                                Service.gI().loadPoint(p.session, p);
                                p.updateHpToPlayerInMap(p, p.hp);
                            } else {
                                p.sendAddchatYellow("Không đủ ngọc!");
                            }
                        } else if (select == 2) {
                            if (p.ngoc >= 150) {
                                p.ngoc -= 150;
                                Service.gI().buyDone(p);
                                p.xHPSaoDen = (byte) 7;
                                p.hp = p.getHpFull();
                                Service.gI().loadPoint(p.session, p);
                                p.updateHpToPlayerInMap(p, p.hp);
                            } else {
                                p.sendAddchatYellow("Không đủ ngọc!");
                            }
                        }
                    }
                }
                break;
            }
            case 35: { //NGOC RONG SAO DEN
                if (p.map.id == 91) {
                    if (p.menuID == -1) {
                        if (select == 0) { //phu ho
                            if (p.imgNRSD == (byte) 37) {
                                doMenuArray(p, idNpc, "Ta sẽ giúp ngươi tăng HP và KI lên mức kinh hoàng, ngươi hãy chọn đi", new String[]{"x3 HP\n50 ngọc", "x5 HP\n100 ngọc", "x7 HP\n150 ngọc", "Từ chối"});
                                p.menuID = select;
                            } else {
                                p.sendAddchatYellow("Không thể thực hiện");
                            }
                            break;
                        }
                        if (select == 1) {
                            break;
                        }
                        //                p.menuID = select;
                        break;
                    }
                    if (p.menuID == 0 && p.imgNRSD == (byte) 37) {
                        if (select == 0) {
                            if (p.ngoc >= 50) {
                                p.ngoc -= 50;
                                Service.gI().buyDone(p);
                                p.xHPSaoDen = (byte) 3;
                                p.hp = p.getHpFull();
                                Service.gI().loadPoint(p.session, p);
                                p.updateHpToPlayerInMap(p, p.hp);
                            } else {
                                p.sendAddchatYellow("Không đủ ngọc!");
                            }
                        } else if (select == 1) {
                            if (p.ngoc >= 100) {
                                p.ngoc -= 100;
                                Service.gI().buyDone(p);
                                p.xHPSaoDen = (byte) 5;
                                p.hp = p.getHpFull();
                                Service.gI().loadPoint(p.session, p);
                                p.updateHpToPlayerInMap(p, p.hp);
                            } else {
                                p.sendAddchatYellow("Không đủ ngọc!");
                            }
                        } else if (select == 2) {
                            if (p.ngoc >= 150) {
                                p.ngoc -= 150;
                                Service.gI().buyDone(p);
                                p.xHPSaoDen = (byte) 7;
                                p.hp = p.getHpFull();
                                Service.gI().loadPoint(p.session, p);
                                p.updateHpToPlayerInMap(p, p.hp);
                            } else {
                                p.sendAddchatYellow("Không đủ ngọc!");
                            }
                        }
                    }
                }
                break;
            }
            case 34: { //NGOC RONG SAO DEN
                if (p.map.id == 90) {
                    if (p.menuID == -1) {
                        if (select == 0) { //phu ho
                            if (p.imgNRSD == (byte) 37) {
                                doMenuArray(p, idNpc, "Ta sẽ giúp ngươi tăng HP và KI lên mức kinh hoàng, ngươi hãy chọn đi", new String[]{"x3 HP\n50 ngọc", "x5 HP\n100 ngọc", "x7 HP\n150 ngọc", "Từ chối"});
                                p.menuID = select;
                            } else {
                                p.sendAddchatYellow("Không thể thực hiện");
                            }
                            break;
                        }
                        if (select == 1) {
                            break;
                        }
                        //                p.menuID = select;
                        break;
                    }
                    if (p.menuID == 0 && p.imgNRSD == (byte) 37) {
                        if (select == 0) {
                            if (p.ngoc >= 50) {
                                p.ngoc -= 50;
                                Service.gI().buyDone(p);
                                p.xHPSaoDen = (byte) 3;
                                p.hp = p.getHpFull();
                                Service.gI().loadPoint(p.session, p);
                                p.updateHpToPlayerInMap(p, p.hp);
                            } else {
                                p.sendAddchatYellow("Không đủ ngọc!");
                            }
                        } else if (select == 1) {
                            if (p.ngoc >= 100) {
                                p.ngoc -= 100;
                                Service.gI().buyDone(p);
                                p.xHPSaoDen = (byte) 5;
                                p.hp = p.getHpFull();
                                Service.gI().loadPoint(p.session, p);
                                p.updateHpToPlayerInMap(p, p.hp);
                            } else {
                                p.sendAddchatYellow("Không đủ ngọc!");
                            }
                        } else if (select == 2) {
                            if (p.ngoc >= 150) {
                                p.ngoc -= 150;
                                Service.gI().buyDone(p);
                                p.xHPSaoDen = (byte) 7;
                                p.hp = p.getHpFull();
                                Service.gI().loadPoint(p.session, p);
                                p.updateHpToPlayerInMap(p, p.hp);
                            } else {
                                p.sendAddchatYellow("Không đủ ngọc!");
                            }
                        }
                    }
                }
                break;
            }
            case 33: { //NGOC RONG SAO DEN
                if (p.map.id == 89) {
                    if (p.menuID == -1) {
                        if (select == 0) { //phu ho
                            if (p.imgNRSD == (byte) 37) {
                                doMenuArray(p, idNpc, "Ta sẽ giúp ngươi tăng HP và KI lên mức kinh hoàng, ngươi hãy chọn đi", new String[]{"x3 HP\n50 ngọc", "x5 HP\n100 ngọc", "x7 HP\n150 ngọc", "Từ chối"});
                                p.menuID = select;
                            } else {
                                p.sendAddchatYellow("Không thể thực hiện");
                            }
                            break;
                        }
                        if (select == 1) {
                            break;
                        }
                        //                p.menuID = select;
                        break;
                    }
                    if (p.menuID == 0 && p.imgNRSD == (byte) 37) {
                        if (select == 0) {
                            if (p.ngoc >= 50) {
                                p.ngoc -= 50;
                                Service.gI().buyDone(p);
                                p.xHPSaoDen = (byte) 3;
                                p.hp = p.getHpFull();
                                Service.gI().loadPoint(p.session, p);
                                p.updateHpToPlayerInMap(p, p.hp);
                            } else {
                                p.sendAddchatYellow("Không đủ ngọc!");
                            }
                        } else if (select == 1) {
                            if (p.ngoc >= 100) {
                                p.ngoc -= 100;
                                Service.gI().buyDone(p);
                                p.xHPSaoDen = (byte) 5;
                                p.hp = p.getHpFull();
                                Service.gI().loadPoint(p.session, p);
                                p.updateHpToPlayerInMap(p, p.hp);
                            } else {
                                p.sendAddchatYellow("Không đủ ngọc!");
                            }
                        } else if (select == 2) {
                            if (p.ngoc >= 150) {
                                p.ngoc -= 150;
                                Service.gI().buyDone(p);
                                p.xHPSaoDen = (byte) 7;
                                p.hp = p.getHpFull();
                                Service.gI().loadPoint(p.session, p);
                                p.updateHpToPlayerInMap(p, p.hp);
                            } else {
                                p.sendAddchatYellow("Không đủ ngọc!");
                            }
                        }
                    }
                }
                break;
            }
            case 32: { //NGOC RONG SAO DEN
                if (p.map.id == 88) {
                    if (p.menuID == -1) {
                        if (select == 0) { //phu ho
                            if (p.imgNRSD == (byte) 37) {
                                doMenuArray(p, idNpc, "Ta sẽ giúp ngươi tăng HP và KI lên mức kinh hoàng, ngươi hãy chọn đi", new String[]{"x3 HP\n50 ngọc", "x5 HP\n100 ngọc", "x7 HP\n150 ngọc", "Từ chối"});
                                p.menuID = select;
                            } else {
                                p.sendAddchatYellow("Không thể thực hiện");
                            }
                            break;
                        }
                        if (select == 1) {
                            break;
                        }
                        //                p.menuID = select;
                        break;
                    }
                    if (p.menuID == 0 && p.imgNRSD == (byte) 37) {
                        if (select == 0) {
                            if (p.ngoc >= 50) {
                                p.ngoc -= 50;
                                Service.gI().buyDone(p);
                                p.xHPSaoDen = (byte) 3;
                                p.hp = p.getHpFull();
                                Service.gI().loadPoint(p.session, p);
                                p.updateHpToPlayerInMap(p, p.hp);
                            } else {
                                p.sendAddchatYellow("Không đủ ngọc!");
                            }
                        } else if (select == 1) {
                            if (p.ngoc >= 100) {
                                p.ngoc -= 100;
                                Service.gI().buyDone(p);
                                p.xHPSaoDen = (byte) 5;
                                p.hp = p.getHpFull();
                                Service.gI().loadPoint(p.session, p);
                                p.updateHpToPlayerInMap(p, p.hp);
                            } else {
                                p.sendAddchatYellow("Không đủ ngọc!");
                            }
                        } else if (select == 2) {
                            if (p.ngoc >= 150) {
                                p.ngoc -= 150;
                                Service.gI().buyDone(p);
                                p.xHPSaoDen = (byte) 7;
                                p.hp = p.getHpFull();
                                Service.gI().loadPoint(p.session, p);
                                p.updateHpToPlayerInMap(p, p.hp);
                            } else {
                                p.sendAddchatYellow("Không đủ ngọc!");
                            }
                        }
                    }
                }
                break;
            }
            case 31: { //NGOC RONG SAO DEN
                if (p.map.id == 87) {
                    if (p.menuID == -1) {
                        if (select == 0) { //phu ho
                            if (p.imgNRSD == (byte) 37) {
                                doMenuArray(p, idNpc, "Ta sẽ giúp ngươi tăng HP và KI lên mức kinh hoàng, ngươi hãy chọn đi", new String[]{"x3 HP\n50 ngọc", "x5 HP\n100 ngọc", "x7 HP\n150 ngọc", "Từ chối"});
                                p.menuID = select;
                            } else {
                                p.sendAddchatYellow("Không thể thực hiện");
                            }
                            break;
                        }
                        if (select == 1) {
                            break;
                        }
                        //                p.menuID = select;
                        break;
                    }
                    if (p.menuID == 0 && p.imgNRSD == (byte) 37) {
                        if (select == 0) {
                            if (p.ngoc >= 50) {
                                p.ngoc -= 50;
                                Service.gI().buyDone(p);
                                p.xHPSaoDen = (byte) 3;
                                p.hp = p.getHpFull();
                                Service.gI().loadPoint(p.session, p);
                                p.updateHpToPlayerInMap(p, p.hp);
                            } else {
                                p.sendAddchatYellow("Không đủ ngọc!");
                            }
                        } else if (select == 1) {
                            if (p.ngoc >= 100) {
                                p.ngoc -= 100;
                                Service.gI().buyDone(p);
                                p.xHPSaoDen = (byte) 5;
                                p.hp = p.getHpFull();
                                Service.gI().loadPoint(p.session, p);
                                p.updateHpToPlayerInMap(p, p.hp);
                            } else {
                                p.sendAddchatYellow("Không đủ ngọc!");
                            }
                        } else if (select == 2) {
                            if (p.ngoc >= 150) {
                                p.ngoc -= 150;
                                Service.gI().buyDone(p);
                                p.xHPSaoDen = (byte) 7;
                                p.hp = p.getHpFull();
                                Service.gI().loadPoint(p.session, p);
                                p.updateHpToPlayerInMap(p, p.hp);
                            } else {
                                p.sendAddchatYellow("Không đủ ngọc!");
                            }
                        }
                    }
                }
                break;
            }
            case 30: { //NGOC RONG SAO DEN
                if (p.map.id == 86) {
                    if (p.menuID == -1) {
                        if (select == 0) { //phu ho
                            if (p.imgNRSD == (byte) 37) {
                                doMenuArray(p, idNpc, "Ta sẽ giúp ngươi tăng HP và KI lên mức kinh hoàng, ngươi hãy chọn đi", new String[]{"x3 HP\n50 ngọc", "x5 HP\n100 ngọc", "x7 HP\n150 ngọc", "Từ chối"});
                                p.menuID = select;
                            } else {
                                p.sendAddchatYellow("Không thể thực hiện");
                            }
                            break;
                        }
                        if (select == 1) {
                            break;
                        }
                        //                p.menuID = select;
                        break;
                    }
                    if (p.menuID == 0 && p.imgNRSD == (byte) 37) {
                        if (select == 0) {
                            if (p.ngoc >= 50) {
                                p.ngoc -= 50;
                                Service.gI().buyDone(p);
                                p.xHPSaoDen = (byte) 3;
                                p.hp = p.getHpFull();
                                Service.gI().loadPoint(p.session, p);
                                p.updateHpToPlayerInMap(p, p.hp);
                            } else {
                                p.sendAddchatYellow("Không đủ ngọc!");
                            }
                        } else if (select == 1) {
                            if (p.ngoc >= 100) {
                                p.ngoc -= 100;
                                Service.gI().buyDone(p);
                                p.xHPSaoDen = (byte) 5;
                                p.hp = p.getHpFull();
                                Service.gI().loadPoint(p.session, p);
                                p.updateHpToPlayerInMap(p, p.hp);
                            } else {
                                p.sendAddchatYellow("Không đủ ngọc!");
                            }
                        } else if (select == 2) {
                            if (p.ngoc >= 150) {
                                p.ngoc -= 150;
                                Service.gI().buyDone(p);
                                p.xHPSaoDen = (byte) 7;
                                p.hp = p.getHpFull();
                                Service.gI().loadPoint(p.session, p);
                                p.updateHpToPlayerInMap(p, p.hp);
                            } else {
                                p.sendAddchatYellow("Không đủ ngọc!");
                            }
                        }
                    }
                }
                break;
            }
            case 29: { //NGOC RONG SAO DEN
                if (p.map.id == 24 || p.map.id == 25 || p.map.id == 26) {
                    if (p.menuID == -1) {
                        if (p.indexNRSD.size() == 0) {
                            if (select == 0) {
                                Service.chatNPC(p, (short) p.menuNPCID, "Mỗi ngày từ 20h đến 21h các hành tinh có Ngọc Rồng Sao Đen sẽ xảy ra 1 cuộc đại chiến. Người nào tìm thấy và giữ được Ngọc Rồng Sao Đen sẽ mang phần thưởng về cho bang của mình trong vòng 1 ngày\nCác phần thưởng như sau\n1 sao đen: +15% sức đánh cho toàn bang\n2 sao đen: +20% HP và KI tối đa cho toàn bang\n3 sao đen: Mỗi giờ 10 hạt đậu thần cấp 10 cho toàn bang\n4 sao đen: Mỗi giờ 1 bùa 1h ngẫu nhiên cho toàn bang\n5 sao đen: Mỗi giờ 3 ngọc nâng cấp ngẫu nhiên cho toàn bang\n6 sao đen: Mỗi giờ 200.000 vàng cho toàn bang\n7 sao đen: Mỗi giờ 2 ngọc cho toàn bang");
                            }
                            if (select == 1) {
                                if (Server.gI().openNRSD) {
                                    Service.gI().openUISaoDen(p);
                                } else {
                                    p.sendAddchatYellow("Ngọc rồng sao đen chỉ mở vào khung giờ 20h đến 21h hàng ngày");
                                }
                            }
                            if (select == 2) {
                                break;
                            }
                            p.menuID = select;
                            break;
                        } else {
                            if (select == 0) {
                                Service.chatNPC(p, (short) p.menuNPCID, "Mỗi ngày từ 20h đến 21h các hành tinh có Ngọc Rồng Sao Đen sẽ xảy ra 1 cuộc đại chiến. Người nào tìm thấy và giữ được Ngọc Rồng Sao Đen sẽ mang phần thưởng về cho bang của mình trong vòng 1 ngày\nCác phần thưởng như sau\n1 sao đen: +15% sức đánh cho toàn bang\n2 sao đen: +20% HP và KI tối đa cho toàn bang\n3 sao đen: Mỗi giờ 10 hạt đậu thần cấp 10 cho toàn bang\n4 sao đen: Mỗi giờ 1 bùa 1h ngẫu nhiên cho toàn bang\n5 sao đen: Mỗi giờ 3 ngọc nâng cấp ngẫu nhiên cho toàn bang\n6 sao đen: Mỗi giờ 200.000 vàng cho toàn bang\n7 sao đen: Mỗi giờ 2 ngọc cho toàn bang");
                            }
                            if (select == 1) {
                                String[] stringNRSD = new String[p.indexNRSD.size()];
                                for (byte i = 0; i < p.indexNRSD.size(); i++) {
                                    if (((System.currentTimeMillis() - p.timeNRSD[p.indexNRSD.get(i)]) >= (long) 3600000) || p.indexNRSD.get(i) <= (byte) 1) {
                                        stringNRSD[i] = "Nhận\nthưởng\n" + (byte) (p.indexNRSD.get(i) + 1) + " sao";
                                    } else {
                                        stringNRSD[i] = (byte) (p.indexNRSD.get(i) + 1) + " sao\n" + (int) (60 - (int) ((System.currentTimeMillis() - p.timeNRSD[p.indexNRSD.get(i)]) / 60000)) + " phút";
                                    }
                                }
                                doMenuArray(p, idNpc, "Ngươi đang có phần thưởng ngọc sao đen, có muốn nhận không?", stringNRSD);
                            }
                            if (select == 2) {
                                if (Server.gI().openNRSD) {
                                    Service.gI().openUISaoDen(p);
                                } else {
                                    p.sendAddchatYellow("Ngọc rồng sao đen chỉ mở vào khung giờ 20h đến 21h hàng ngày");
                                }
                            }
                            if (select == 3) {
                                break;
                            }
                            p.menuID = select;
                            break;
                        }
                    }
                    if (p.indexNRSD.size() == 0) {

                    } else {
                        if (p.menuID == 1) {
                            if (p.indexNRSD.get((byte) select) > (byte) 1) {
                                byte indexNRD = p.indexNRSD.get((byte) select);
                                if ((int) ((System.currentTimeMillis() - p.timeNRSD[indexNRD]) / 60000) > 60) {
                                    if (indexNRD == (byte) 2) {
                                        p.sendAddchatYellow("Cùi quá, chờ nghĩ phần thưởng mới");
                                    } else if (indexNRD == (byte) 3) {
                                        p.sendAddchatYellow("Cùi quá, chờ nghĩ phần thưởng mới");
                                    } else if (indexNRD == (byte) 4) {
                                        p.sendAddchatYellow("Cùi quá, chờ nghĩ phần thưởng mới");
                                    } else if (indexNRD == (byte) 5) {
                                        p.vang = (p.vang + 200000) > 2000000000 ? 2000000000 : (p.vang + 200000);
                                        Service.gI().buyDone(p);
                                    } else if (indexNRD == (byte) 6) {
                                        p.ngoc = (p.ngoc + 2) > 10000000 ? 10000000 : (p.ngoc + 2);
                                        Service.gI().buyDone(p);
                                    }
                                    p.timeNRSD[indexNRD] = System.currentTimeMillis();
                                } else {
                                    p.sendAddchatYellow("Chưa thể nhận, hãy chờ hết thời gian");
                                }
                            } else {
                                p.sendAddchatYellow("Phần thưởng này đã có tác dụng");
                            }
                            break;
                        }
                    }
                }
                break;
            }
            case 27: { //RONG THAN NAMEC
                if (p.menuID == -1) {
                    if (select == 0) {
                        if (p.clan != null) {
                            Player memClan = null;
                            Item _itemDBV = null;
                            Item itemDBV = ItemSell.getItemNotSell(987);
                            for (Member _mem : p.clan.members) {
                                memClan = PlayerManger.gI().getPlayerByUserID(_mem.id);
                                if (memClan != null && memClan.session != null) { //CHECK MEMBER ONLINE MOI DUOC NHAN
                                    _itemDBV = new Item(itemDBV);
                                    _itemDBV.quantity = 10;
                                    memClan.addItemToBag(_itemDBV);
                                    Service.gI().updateItemBag(memClan);
                                    memClan.sendAddchatYellow("Điều ước của ngươi đã trở thành sự thật!");
                                }
                            }
                        }
                    }
                    Service.gI().endEffCallDragon(p);
                    break;
                }
                break;
            }
            case 25: {
                if (p.map.id == 27) {
                    if (select == 0) {
                        if (p.clan == null) {
                            p.sendAddchatYellow("Ngươi chưa có bang hội!");
                        } else {
                            if (p.clan.members.size() < 5) {
                                p.sendAddchatYellow("Bang hội phải có ít nhất 5 thành viên mới có thể tiến hành đi doanh trại Độc Nhãn");
                            } else {
                                if ((System.currentTimeMillis() - p.clan.tcreate) >= (long) 86400000) {
                                    if ((System.currentTimeMillis() - p.clan.topen) >= (long) 86400000 || p.clan.openDoanhTrai) {
                                        boolean chkOpenDT = false;
                                        for (int i = 0; i < p.zone.players.size(); i++) {
                                            if (p.zone.players.get(i).clan != null && p.zone.players.get(i).id != p.id) {
                                                if (p.zone.players.get(i).clan.id == p.clan.id) {
                                                    chkOpenDT = true;
                                                    break;
                                                }
                                            }
                                        }
                                        if (chkOpenDT || p.clan.openDoanhTrai) {
                                            if (Server.gI().cDoanhTrai < Server.gI().maxDoanhTrai) {
                                                GotoMap(p, 53);
                                            } else {
                                                p.sendAddchatYellow("Hiện tại doanh trại Độc Nhãn đang quá tải, vui lòng chờ 30 phút nữa");
                                            }
                                        } else {
                                            p.sendAddchatYellow("Cần ít nhất 2 thành viên để mở doanh trại Độc Nhãn");
                                        }
                                    } else {
                                        p.sendAddchatYellow("Hôm qua các ngươi đã đi doanh trại rồi, hãy chờ đến lượt tiếp theo");
                                    }
                                } else {
                                    p.sendAddchatYellow("Bang hội phải tạo được 1 ngày trở lên mới có thể tiến hành doanh trại Độc Nhãn");
                                }
                            }
                        }
                    }
                    if (select == 1) {
                        break;
                    }
                }
                break;
            }
            case 24: { //RONG THAN TRAI DAT
                if (p.menuID == -1) {
                    if (select == 0) { //DEP TRAI NHAT VU TRU
                        ItemSell avatarVIP = ItemSell.getItemSell(((int) p.gender + 227), (byte) 1);
                        Item _AVATARVIP = new Item(avatarVIP.item);
                        _AVATARVIP.itemOptions.clear();
                        _AVATARVIP.itemOptions.add(new ItemOption(77, Util.nextInt(15, 21)));
                        p.addItemToBag(_AVATARVIP);
                        Service.gI().updateItemBag(p);
                    } else if (select == 1) { // GANG TAY DANG MANG LEN 1 CAP
                        if (p.ItemBody[2] != null && p.ItemBody[2].getParamItemByID(72) < 7) {
                            if (p.ItemBody[2].getParamItemByID(72) == 0) {
                                ItemOption itemOptionNew = new ItemOption(72, 1);
                                p.ItemBody[2].itemOptions.add(itemOptionNew);
                                for (byte i = 0; i < p.ItemBody[2].itemOptions.size(); i++) {
                                    if (p.ItemBody[2].itemOptions.get(i).id == 0) {
                                        p.ItemBody[2].itemOptions.get(i).param = (int) Math.ceil(p.ItemBody[2].itemOptions.get(i).param * 1.1);
                                    }
                                }
                            } else {
                                for (byte i = 0; i < p.ItemBody[2].itemOptions.size(); i++) {
                                    if (p.ItemBody[2].itemOptions.get(i).id == 72) {
                                        p.ItemBody[2].itemOptions.get(i).param += 1;
                                    }
                                    if (p.ItemBody[2].itemOptions.get(i).id == 0) {
                                        p.ItemBody[2].itemOptions.get(i).param = (int) Math.ceil(p.ItemBody[2].itemOptions.get(i).param * 1.1);
                                    }
                                }
                            }
                        }
                        Service.gI().updateItemBody(p);
                        Service.gI().loadPoint(p.session, p);
                        p.LOADCAITRANGTOME();
                    } else if (select == 2) { // DOI SKILL 2, 3 DE TU
                        if (p.havePet == 1 && (p.detu.power >= 1500000000L)) {
                            int rdSkill2 = Util.nextInt(0, 4);
                            if (rdSkill2 == 0) {
                                p.detu.listSkill.get(1).skillId = (short) 7; //id kame lv1
                                p.detu.listSkill.get(1).point = 1;
                                p.detu.listSkill.get(1).genderSkill = (byte) 0;
                                p.detu.listSkill.get(1).tempSkillId = (short) 1;
                            } else if (rdSkill2 == 1) {
                                p.detu.listSkill.get(1).skillId = (short) 21; //id masenko lv1
                                p.detu.listSkill.get(1).point = 1;
                                p.detu.listSkill.get(1).genderSkill = (byte) 1;
                                p.detu.listSkill.get(1).tempSkillId = (short) 3;
                            } else if (rdSkill2 == 2) {
                                p.detu.listSkill.get(1).skillId = (short) 35; //id atomic lv1
                                p.detu.listSkill.get(1).point = 1;
                                p.detu.listSkill.get(1).genderSkill = (byte) 2;
                                p.detu.listSkill.get(1).tempSkillId = (short) 5;
                            } else {
                                p.detu.listSkill.get(1).skillId = (short) 21; //id masenko lv1
                                p.detu.listSkill.get(1).point = 1;
                                p.detu.listSkill.get(1).genderSkill = (byte) 1;
                                p.detu.listSkill.get(1).tempSkillId = (short) 3;
                            }
                            //RANDOM SKILL 3
                            rdSkill2 = Util.nextInt(0, 4);
                            if (rdSkill2 == 0) {
                                p.detu.listSkill.get(2).skillId = (short) 42; //id tdhs lv1
                                p.detu.listSkill.get(2).point = 1;
                                p.detu.listSkill.get(2).genderSkill = (byte) 0;
                                p.detu.listSkill.get(2).tempSkillId = (short) 6;
                            } else if (rdSkill2 == 1) {
                                p.detu.listSkill.get(2).skillId = (short) 63; //id kaioken lv1
                                p.detu.listSkill.get(2).point = 1;
                                p.detu.listSkill.get(2).genderSkill = (byte) 0;
                                p.detu.listSkill.get(2).tempSkillId = (short) 9;
                            } else if (rdSkill2 == 2) {
                                p.detu.listSkill.get(2).skillId = (short) 56; //id ttnl lv1
                                p.detu.listSkill.get(2).point = 1;
                                p.detu.listSkill.get(2).genderSkill = (byte) 2;
                                p.detu.listSkill.get(2).tempSkillId = (short) 8;
                            } else {
                                p.detu.listSkill.get(2).skillId = (short) 63; //id kaioken lv1
                                p.detu.listSkill.get(2).point = 1;
                                p.detu.listSkill.get(2).genderSkill = (byte) 0;
                                p.detu.listSkill.get(2).tempSkillId = (short) 9;
                            }
                        }
                    } else if (select == 3) {
                        //RANDOM SKILL 3
                        if (p.havePet == 1 && (p.detu.power >= 20000000000L)) {
                            int rdSkill2 = Util.nextInt(0, 4);
                            if (rdSkill2 == 0) {
                                p.detu.listSkill.get(2).skillId = (short) 42; //id tdhs lv1
                                p.detu.listSkill.get(2).point = 1;
                                p.detu.listSkill.get(2).genderSkill = (byte) 0;
                                p.detu.listSkill.get(2).tempSkillId = (short) 6;
                            } else if (rdSkill2 == 1) {
                                p.detu.listSkill.get(2).skillId = (short) 63; //id kaioken lv1
                                p.detu.listSkill.get(2).point = 1;
                                p.detu.listSkill.get(2).genderSkill = (byte) 0;
                                p.detu.listSkill.get(2).tempSkillId = (short) 9;
                            } else if (rdSkill2 == 2) {
                                p.detu.listSkill.get(2).skillId = (short) 56; //id ttnl lv1
                                p.detu.listSkill.get(2).point = 1;
                                p.detu.listSkill.get(2).genderSkill = (byte) 2;
                                p.detu.listSkill.get(2).tempSkillId = (short) 8;
                            } else {
                                p.detu.listSkill.get(2).skillId = (short) 63; //id kaioken lv1
                                p.detu.listSkill.get(2).point = 1;
                                p.detu.listSkill.get(2).genderSkill = (byte) 0;
                                p.detu.listSkill.get(2).tempSkillId = (short) 9;
                            }
                            //RANDOM SKILL 4
                            rdSkill2 = Util.nextInt(0, 3);
                            if (rdSkill2 == 0) {
                                p.detu.listSkill.get(3).skillId = (short) 91; //id bien khi lv1
                                p.detu.listSkill.get(3).point = 1;
                                p.detu.listSkill.get(3).genderSkill = (byte) 2;
                                p.detu.listSkill.get(3).tempSkillId = (short) 13;
                            } else if (rdSkill2 == 1) {
                                p.detu.listSkill.get(3).skillId = (short) 121; //id khien nang luong lv1
                                p.detu.listSkill.get(3).point = 1;
                                p.detu.listSkill.get(3).genderSkill = (byte) 0;
                                p.detu.listSkill.get(3).tempSkillId = (short) 19;
                            } else {
                                p.detu.listSkill.get(3).skillId = (short) 121; //id khien nang luong lv1
                                p.detu.listSkill.get(3).point = 1;
                                p.detu.listSkill.get(3).genderSkill = (byte) 0;
                                p.detu.listSkill.get(3).tempSkillId = (short) 19;
                            }
                        }
                    } else if (select == 4) { //GANG TAY DE TU DANG MANG LEN 1 CAP
                        if (p.detu.ItemBody[2] != null && p.detu.ItemBody[2].getParamItemByID(72) < 7) {
                            if (p.detu.ItemBody[2].getParamItemByID(72) == 0) {
                                ItemOption itemOptionNew = new ItemOption(72, 1);
                                p.detu.ItemBody[2].itemOptions.add(itemOptionNew);
                                for (byte i = 0; i < p.detu.ItemBody[2].itemOptions.size(); i++) {
                                    if (p.detu.ItemBody[2].itemOptions.get(i).id == 0) {
                                        p.detu.ItemBody[2].itemOptions.get(i).param = (int) Math.ceil(p.detu.ItemBody[2].itemOptions.get(i).param * 1.1);
                                    }
                                }
                            } else {
                                for (byte i = 0; i < p.detu.ItemBody[2].itemOptions.size(); i++) {
                                    if (p.detu.ItemBody[2].itemOptions.get(i).id == 72) {
                                        p.detu.ItemBody[2].itemOptions.get(i).param += 1;
                                    }
                                    if (p.detu.ItemBody[2].itemOptions.get(i).id == 0) {
                                        p.detu.ItemBody[2].itemOptions.get(i).param = (int) Math.ceil(p.detu.ItemBody[2].itemOptions.get(i).param * 1.1);
                                    }
                                }
                            }
                        }
                        Service.gI().updateItemBody(p.detu);
                        //                    p.LOADCAITRANGTOME();
                    } else if (select == 5) { // MOT CO NGUOI YEU LUA DAO
                                           p.sendAddchatYellow("Có cái đầu bùi nhé con!");
                       // p.tiemNang += (long) 200000000;
                       // p.power += (long) 200000000;
                       // p.UpdateSMTN((byte) 2, (long) 200000000);
                    } else if (select == 6) {
                        p.critNr = (byte) (p.critNr + 2) > (byte) 10 ? (byte) 10 : (byte) (p.critNr + 2);
                        Service.gI().loadPoint(p.session, p);
                    } else if (select == 7) { // MOT CO NGUOI YEU LUA DAO
                        p.ngoc += (long) 5000;
                        Service.gI().buyDone(p);
                        break;
                    }
                    p.sendAddchatYellow("Điều ước của người đã trở thành sự thật!");
                    Service.gI().endEffCallDragon(p);
                    //                Message msg = null;
                    //                try {
                    //                    msg = new Message(-83);
                    //                    msg.writer().writeByte(1);
                    //                    msg.writer().flush();
                    //                    for(Player _p: p.zone.players) {
                    //                        _p.session.sendMessage(msg);
                    //                    }
                    //                    msg.cleanup();
                    //                } catch (Exception e) {
                    //                    e.printStackTrace();
                    //                } finally {
                    //                    if(msg != null) {
                    //                        msg.cleanup();
                    //                    }
                    //                }
                    break;
                }
                break;
            }
            // ba hat mit
            case 21: {
                if (p.map.id == 5) {
                    if (p.menuID == -1) {
                        Message msg = null;
                        if (select == 0) { //ep sao trang bi
                            try {
                                msg = new Message(-81);
                                msg.writer().writeByte(0);
                                msg.writer().writeUTF("Vào hành trang\nChọn trang bị\n(Áo, quần, găng, giày hoặc rada) có ô đặt\nsao pha lê\nChọn loại sao pha lê\nSau đó chọn 'Nâng cấp'");
                                msg.writer().writeUTF("Ta sẽ phù phép\ncho trang bị của ngươi\ntrở nên mạnh mẽ");
                                msg.writer().flush();
                                p.session.sendMessage(msg);
                                msg.cleanup();
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (msg != null) {
                                    msg.cleanup();
                                }
                            }
                        }
                        if (select == 1) { // pha lee hoa trang bi
                            try {
                                msg = new Message(-81);
                                msg.writer().writeByte(0);
                                msg.writer().writeUTF("Vào hành trang\nChọn trang bị\n(Áo, quần, găng, giày hoặc rada)\n Sau đó chọn 'Nâng cấp'");
                                msg.writer().writeUTF("Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị pha lê");
                                msg.writer().flush();
                                p.session.sendMessage(msg);
                                msg.cleanup();
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (msg != null) {
                                    msg.cleanup();
                                }
                            }
                        }
                        p.menuID = select;
                        break;
                    }
                    if (p.menuID == 0 && select == 0) { //xac nhan nang cap
                        Service.gI().sendEpStarItem(p);
                        break;
                    }
                    if (p.menuID == 1 && select == 0) { //xac nhan nang cap
                        Service.gI().sendUpStarItem(p);
                        break;
                    }
                    break;
                } else if (p.map.id == 42 || p.map.id == 43 || p.map.id == 44) {
                    if (p.menuID == -1) {
                        Message msg = null;
                        if (select == 0) { //mua buaf
                            doMenuArray(p, idNpc, "Bùa của ta rất lợi hại. Mua xong có tác dụng ngay nhé, nhớ tranh thủ sử dụng, thoát game phí lắm. Mua càng nhiều thời gian giá càng rẻ!", new String[]{"Bùa Dùng 1\ngiờ", "Bùa Dùng 8\ngiờ", "Bùa Dùng 1\ntháng"});
                        }
                        if (select == 1) { //nang cap trang bi
                            try {
                                msg = new Message(-81);
                                msg.writer().writeByte(0);
                                msg.writer().writeUTF("Vào hành trang\nChọn trang bị\n(Áo, quần, găng, giày hoặc rada)\nChọn loại đá để nâng cấp\nĐá bảo vệ đặt ở vị trí cuối(nếu có)\nSau đó chọn 'Nâng cấp'");
                                msg.writer().writeUTF("Ta sẽ phù phép\ncho trang bị của ngươi\ntrở nên mạnh mẽ");
                                msg.writer().flush();
                                p.session.sendMessage(msg);
                                msg.cleanup();
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (msg != null) {
                                    msg.cleanup();
                                }
                            }
                        }
                        if (select == 2) { //lam phep nhap da
                            try {
                                msg = new Message(-81);
                                msg.writer().writeByte(0);
                                msg.writer().writeUTF("Vào hành trang\nChọn 10 mảnh đá vụn\nChọn một bình nước phép\nSau đó chọn 'Làm phép'");
                                msg.writer().writeUTF("Ta sẽ phù phép\ncho 10 mảnh đá vụn\ntrở thành 1 đá nâng cấp");
                                msg.writer().flush();
                                p.session.sendMessage(msg);
                                msg.cleanup();
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (msg != null) {
                                    msg.cleanup();
                                }
                            }
                        }
                        if (select == 3) { //Nhap ngoc rong
                            try {
                                msg = new Message(-81);
                                msg.writer().writeByte(0);
                                msg.writer().writeUTF("Vào hành trang\nChọn 7 viên ngọc cùng sao\nSau đó chọn 'Làm phép'");
                                msg.writer().writeUTF("Ta sẽ phù phép\ncho 7 viên Ngọc Rồng\nthành 1 viên Ngọc Rồng cấp cao");
                                msg.writer().flush();
                                p.session.sendMessage(msg);
                                msg.cleanup();
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (msg != null) {
                                    msg.cleanup();
                                }
                            }
                        } else if (select == 4) { //NANG CAP BONG TAI PORATA
                            try {
                                msg = new Message(-81);
                                msg.writer().writeByte(0);
                                msg.writer().writeUTF("Vào hành trang\nChọn bông tai Porata\nChọn mảnh bông tai để nâng cấp, số lượng\ṇ̣̣9999 cái\nSau đó chọn 'Nâng cấp'");
                                msg.writer().writeUTF("Ta sẽ phù phép\ncho bông tai Porata của ngươi\nthành cấp 2");
                                msg.writer().flush();
                                p.session.sendMessage(msg);
                                msg.cleanup();
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (msg != null) {
                                    msg.cleanup();
                                }
                            }
                        } else if (select == 5) { //MO CHI SO BONG TAI PORATA2
                            try {
                                msg = new Message(-81);
                                msg.writer().writeByte(0);
                                msg.writer().writeUTF("Vào hành trang\nChọn bông tai Porata\nChọn mảnh hồn bông tai số lượng 99 cái\nvà đá xanh lam để nâng cấp\nSau đó chọn 'Nâng cấp'");
                                msg.writer().writeUTF("Ta sẽ phù phép\ncho bông tai Porata cấp 2 của ngươi\ncó 1 chỉ số ngẫu nhiên");
                                msg.writer().flush();
                                p.session.sendMessage(msg);
                                msg.cleanup();
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (msg != null) {
                                    msg.cleanup();
                                }
                            }
                        }
//                        else if (select == 6) {
//                            try {
//                                msg = new Message(-81);
//                                msg.writer().writeByte(0);
//                                msg.writer().writeUTF("Vào hành trang\nChọn trang bị hủy diệt\nChọn mảnh trang bị thần, số lượng\n99 cái\nSau đó chọn 'Nâng cấp'");
//                                msg.writer().writeUTF("Ta sẽ phù phép\ncho trang bị hủy diệt của ngươi\nthành trang bị thần Heart");
//                                msg.writer().flush();
//                                p.session.sendMessage(msg);
//                                msg.cleanup();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            } finally {
//                                if (msg != null) {
//                                    msg.cleanup();
//                                }
//                            }
//                            //                        doMenuArray(p,idNpc,"Nâng trang bị Thần Heart",new String[]{"Nâng\ntrang bị\nHeart","Mở chỉ số\nHeart"});
//                        }
                        p.menuID = select;
                        break;
                    }
                    if (p.menuID == 0 && select == 0) { //bua 1 gio
                        TabItemShop[] test = Shop.getTabShop(21, 0).toArray(new TabItemShop[0]);
                        GameScr.UIshop(p, test);
                    } else if (p.menuID == 0 && select == 1) { // bua 8 gio
                        TabItemShop[] test = Shop.getTabShop(21, 1).toArray(new TabItemShop[1]);
                        GameScr.UIshop(p, test);
                    } else if (p.menuID == 0 && select == 2) { //bua 1 thang
                        TabItemShop[] test = Shop.getTabShop(21, 2).toArray(new TabItemShop[1]);
                        GameScr.UIshop(p, test);
                    } else if (p.menuID == 1 && select == 0) { //xac nhan nang cap item
                        Service.gI().sendUpLevelItem(p);
                        break;
                    } else if (p.menuID == 2 && select == 0) { //xac nhan ep da
                        Service.gI().sendEpDaVun(p);
                        break;
                    } else if (p.menuID == 3 && select == 0) { //xac nhan ep da
                        Service.gI().sendEpNgocRong(p);
                        break;
                    } else if (p.menuID == 4 && select == 0) { //xac nhan nâng cấp
                        Service.gI().sendUpPorata(p);
                        break;
                    } else if (p.menuID == 5 && select == 0) { //xac nhan mo chi so bong tai
                        Service.gI().sendOpenOptionPorata(p);
                        break;
                    }
                    break;
                }
                //            if(p.menuID != -1)
                //            {
                //                if(p.menuID == 1 && select == 0)
                //                {
                //                    TabItemShop[] test = Shop.getTabShop(21, 0).toArray(new TabItemShop[0]);
                //                    GameScr.UIshop(p, test);
                //                }
                //            }
                //            if(select == 1){
                //                doMenuArray(p,idNpc,"Bùa của ta rất lợi hại. Mua xong có tác dụng ngay nhé, nhớ tranh thủ sử dụng, thoát game phí lắm. Mua càng nhiều thời gian giá càng rẻ!",new String[]{"Bùa\n1 tháng"});
                //                p.menuID = select;
                //            }
                //            break;
            }
            // ghi danh dai hoi vo thuat
            case 23: {
                if (p.map.id == 52) {
                    if (p.menuID == -1) {
                        if (select == 0) {
                            Service.chatNPC(p, (short) p.menuNPCID, "Lịch thi đấu trong ngày\bGiải Nhi đồng: 8,13,18h\bGiải Siêu cấp 1: 9,14,19h\bGiải Siêu cấp 2: 10,15,20h\bGiải Siêu cấp 3: 11,16,21h\bGiải Ngoại hạng: 12,17,22,23h\nGiải thưởng khi thắng mỗi vòng\bGiải Nhi đồng: 2 ngọc\bGiải Siêu cấp 1: 4 ngọc\bGiải Siêu cấp 2: 6 ngọc\bGiải Siêu cấp 3: 8 ngọc\bGiải Ngoại hạng: 10.000 vàng\bVô địch: 5 viên đá nâng cấp\nVui lòng đến đúng giờ để đăng ký thi đấu");
                        }
                        if (select == 1) {
                            if (DaiHoiManager.gI().openDHVT && (System.currentTimeMillis() <= DaiHoiManager.gI().tOpenDHVT)) {
                                String nameDH = DaiHoiManager.gI().nameRoundDHVT();
                                doMenuArray(p, idNpc, "Hiện đang có giải đấu " + nameDH + " bạn có muốn đăng ký không?", new String[]{"Giải\n" + nameDH + "\n(" + DaiHoiManager.gI().costRoundDHVT() + ")", "Từ chối"});
                            } else {
                                Service.chatNPC(p, (short) p.menuNPCID, "Đã hết thời gian đăng ký, hãy quay lại ở giải sau");
                                break;
                            }
                        }
                        p.menuID = select;
                        break;
                    }
                    if (p.menuID == 1) {
                        if (select == 0) {
                            if (DaiHoiService.gI().canRegisDHVT(p.sumTiemNang)) {
                                if (DaiHoiManager.gI().lstIDPlayers.size() < 256) {
                                    if (DaiHoiManager.gI().typeDHVT == (byte) 5 && p.vang >= 10000) {
                                        if (DaiHoiManager.gI().isAssignDHVT(p.id)) {
                                            p.sendAddchatYellow("Bạn đã đăng ký tham gia đại hội võ thuật rồi");
                                        } else {
                                            p.vang -= 10000;
                                            Service.gI().buyDone(p);
                                            Service.chatNPC(p, (short) p.menuNPCID, "Bạn đã đăng ký thành công, nhớ có mặt tại đây trước giờ thi đấu");
                                            //                    DaiHoiManager.gI().lstPlayers.add(p);
                                            DaiHoiManager.gI().lstIDPlayers.add(p.id);
                                        }
                                    } else if (DaiHoiManager.gI().typeDHVT > (byte) 0 && DaiHoiManager.gI().typeDHVT < (byte) 5 && p.ngoc >= (int) (2 * DaiHoiManager.gI().typeDHVT)) {
                                        if (DaiHoiManager.gI().isAssignDHVT(p.id)) {
                                            p.sendAddchatYellow("Bạn đã đăng ký tham gia đại hội võ thuật rồi");
                                        } else {
                                            p.ngoc -= (int) (2 * DaiHoiManager.gI().typeDHVT);
                                            Service.gI().buyDone(p);
                                            Service.chatNPC(p, (short) p.menuNPCID, "Bạn đã đăng ký thành công, nhớ có mặt tại đây trước giờ thi đấu");
                                            //                    DaiHoiManager.gI().lstPlayers.add(p);
                                            DaiHoiManager.gI().lstIDPlayers.add(p.id);
                                        }
                                    } else {
                                        p.sendAddchatYellow("Không đủ vàng ngọc để đăng ký thi đấu");
                                    }
                                } else {
                                    p.sendAddchatYellow("Hiện tại đã đạt tới số lượng người đăng ký tối đa, xin hãy chờ đến giải sau");
                                }

                            } else {
                                p.sendAddchatYellow("Bạn không đủ điều kiện tham gia giải này, hãy quay lại vào giải phù hợp");
                            }
                        }
                        break;
                    }
                }
                break;
            }
            case 20: { //KAIO
                if (p.map.id == 48) {
                    if (p.menuID != -1) {
                        if (p.menuID == 2 && select == 0) { //CON DUONG RAN DOC
                            p.zone.goMapTransport(p, 143);
                            break;
                        } else if (p.menuID == 2 && select == 1) {//HUONG DAN
                            Service.chatNPC(p, (short) p.menuNPCID, "Hiện tại, map chưa được hoàn thiện NRO LOR đang mở để cho các bạn trải nghiệm!");
                            break;
                        }
                    }
                    if (select == 0) {
                        p.zone.goMapTransport(p, 45);
                    }
                    if (select == 1) {
                        p.zone.goMapTransport(p, 50);
                    }
                    if (select == 2) {
                        doMenuArray(p, idNpc, "Con đường rắn độc, là một nơi cực kì nguy hiểm theo như ta thấy thì con chưa đủ trình đâu !", new String[]{"Tham Gia", "Hướng Dẫn"});
                        p.menuID = select;
                    } else if (select == 3) {
                        break;
                    }
                }
                break;
            }
            case 18: { //THAN MEO
                if (p.map.id == 46) {
                    if (p.menuID != -1) {
                    }
                    if (select == 0) {
                        Service.chatNPC(p, (short) p.menuNPCID, "NRO LOR, là tên máy chủ chắc không cần nhắc thì con cũng biết là trang chủ NRO LOR.Vn nhỉ ?");
                        p.menuID = select;
                    }
                }
                break;
            }
            case 19: { //THUONG DE
                if (p.map.id == 45) {
                    if (p.menuID == -1) {
                        if (select == 0) {
                            p.zone.goMapTransport(p, 48);
                        }
                        if (select == 1) { //Open UI QUAY NGOC
                            if (p.ItemQuay.size() > 0) {
                                doMenuArray(p, idNpc, "Bạn có thể chọn từ 1 đến 7 viên ngọc, giá mỗi viên là 4 ngọc\nƯu tiên dùng vé quay trước", new String[]{"Top 100", "Đồng ý", "Vòng quay\nĐặc biệt", "Rương phụ\nĐang có " + p.ItemQuay.size() + "\nmón", "Đóng"});
                            } else {
                                doMenuArray(p, idNpc, "Bạn có thể chọn từ 1 đến 7 viên ngọc, giá mỗi viên là 4 ngọc\nƯu tiên dùng vé quay trước", new String[]{"Top 100", "Đồng ý", "Vòng quay\nĐặc biệt", "Đóng"});
                            }
                        }
                        p.menuID = select;
                        break;
                    }
                    if (p.menuID == 1) {
                        if (select == 0) {
                            break;
                        } else if (select == 1) { //QUAY NGOC THUONG
                            LuckyService.gI().loadUILucky(p);
                        } else if (select == 2) { //QUAY NGOC DAC BIET
                            LuckyService.gI().loadUILucky(p);
                        }
                        if (p.ItemQuay.size() > 0) {
                            if (select == 3) {
                                p.openItemQuay = true;
                                LuckyService.gI().openItemQuay(p);
                            } else if (select == 4) {
                                break;
                            }
                        } else {
                            if (select == 3) {
                                break;
                            }
                        }
                    }
                    break;
                }
                if (p.map.id == 141) {
                    if (p.menuID != -1) {
                    }
                    if (select == 0) {
                        Service.chatNPC(p, (short) p.menuNPCID, "Hiện tại NRO LOR chưa hoàn thiện xong map này vui lòng quay lại sau..!");
                    }
                    if (select == 1) {
                        p.zone.goMapTransport(p, 48);
                    }
                    if (select == 2) {
                        break;
                    }
                }
                break;
            }
            //        case 16:{
            //            if(select == 0){
            //                TabItemShop[] test = Shop.getTabShop(16, p.gender).toArray(new TabItemShop[0]);
            //                GameScr.UIshop(p, test);
            //                break;
            //            }
            //            break;
            //        }
            case 13: {
                if (p.map.id == 5) {
                    if (p.menuID != -1) {
                        if (p.menuID == 0 && select == 0) {
                            if (p.pointSuKien < 10) {
                                p.sendAddchatYellow("Điểm sự kiện của bạn không đủ");
                                return;
                            }
                            p.pointSuKien -= 10;
                            Item Item = ItemBuff.getItem(573);
                            p.addItemToBag(Item);
                            Service.gI().updateItemBag(p);
                            Service.gI().buyDone(p);
                            p.sendAddchatYellow("Bạn vừa đổi thành công 1 Capsule Bạc");
                        } else if (p.menuID == 0 && select == 1) {
                            if (p.pointSuKien < 100) {
                                p.sendAddchatYellow("Điểm sự kiện của bạn không đủ");
                                return;
                            }
                            p.pointSuKien -= 100;
                            Item Item = ItemBuff.getItem(574);
                            p.addItemToBag(Item);
                            Service.gI().updateItemBag(p);
                            Service.gI().buyDone(p);
                            p.sendAddchatYellow("Bạn vừa đổi thành công 1 Capsule Vàng");
                            break;
                        } else if (p.menuID == 0 && select == 2) {
                            if (p.pointSuKien < 100) {
                                p.sendAddchatYellow("Điểm sự kiện của bạn không đủ");
                                return;
                            }
                            p.pointSuKien -= 100;
                            int tyle = Util.nextInt(0, 100);
                            int random = Util.nextInt(15, 21);
                            if (tyle > 1) {
                                Item ItemRanDom = ItemBuff.getItem(random);
                                p.addItemToBag(ItemRanDom);
                                p.sendAddchatYellow("Thử vận may nhận được ngọc rồng " + (random - 13) + " Sao");
                            } else if (tyle <= 1) {
                                Item ItemRanDom2 = ItemBuff.getItem(12);
                                p.addItemToBag(ItemRanDom2);
                                p.sendAddchatYellow("Thử vận may nhận được ngọc rồng 1 Sao");
                            }
                            Service.gI().buyDone(p);
                            Service.gI().updateItemBag(p);
                            break;
                        } else if (p.menuID == 1 && select == 0) {
                            //CHECK NHIEM VU VONG 2 DHVT
                            if (p.taskId == (short) 18 && p.crrTask.index == (byte) 1 && Server.gI().isPassDHVT) {
                                TaskService.gI().updateCountTask(p);
                            }
                            break;
                        }
                        if (p.menuID == 1 && select == 1) {
                            //CHECK NHIEM VU VONG 2 DHVT
                            if (p.taskId == (short) 19 && p.crrTask.index == (byte) 1) {
                                TaskService.gI().updateCountTask(p);
                            }
                            break;
                        }
                        if (p.menuID == 2 && select == 0) { //titan
                            Service.chatNPC(p, (short) 24, HelperDAO.getTopPower());
                            break;
                        } else if (p.menuID == 2 && select == 1) {//ruby
//                            Service.chatNPC(p, (short) 24, HelperDAO.getTopCard());
                            break;
                        } else if (p.menuID == 2 && select == 2) {//tat
                            //                        Item TITAN = ItemSell.getItemNotSell(224);
                            //                        p.addItemToBagx99(TITAN);
                            //                        Service.gI().updateItemBag(p);
                            //                        p.sendAddchatYellow("Nhận thành công đá Thạch Anh Tím");
                            break;
                        } else if (p.menuID == 2 && select == 3) {//saphia
                            //                        Item TITAN = ItemSell.getItemNotSell(221);
                            //                        p.addItemToBagx99(TITAN);
                            //                        Service.gI().updateItemBag(p);
                            //                        p.sendAddchatYellow("Nhận thành công đá Saphia");
                            break;
                        } else if (p.menuID == 2 && select == 4) {//luc bao
                            //                        Item TITAN = ItemSell.getItemNotSell(220);
                            //                        p.addItemToBagx99(TITAN);
                            //                        Service.gI().updateItemBag(p);
                            //                        p.sendAddchatYellow("Nhận thành công đá Lục Bảo");
                            break;
                        } else if (p.menuID == 3 && select == 0) { //GIAI TAN BANG HOI
                            if (Server.gI().isServer == (byte) 1) {
                                ClanService.gI().distoryClan(p);
                            } else {
                                p.sendAddchatYellow("Chỉ thực hiện tại NRO LOR 01");
                            }
                            break;
                        } else if (p.menuID == 3 && select == 1) { //KHU VUC BANG HOI
                            GotoMap(p, 153);
                            break;
                        } else if (p.menuID == 4 && select == 0) { //KHO BAU DUOI BIEN
                            if (p.map.id == 5) {
                                p.zone.goMapTransport(p, 135);
                                break;
                            }
                        } else if (p.menuID == 4 && select == 1) { //HUONG DAN
                            Service.chatNPC(p, (short) p.menuNPCID, "Hiện tại kho báu dưới biển chưa hoàn thiện NRO LOR chỉ mở để cho các bạn trải nghiệm map này!");
                            break;
                        }
                    }
                    if (select == 0) {
                        doMenuArray(p, idNpc, "Theo ta thấy thì con vẫn còn F.A\nSao không đi kiếm gấu mà còn ở nhà chơi game ?\b|1|Thôi được rồi đâu cũng F.A đọc quy tắc sự kiện đi\b|5|Điểm Sự Kiện Đang Có: " + p.pointSuKien + "\n|7|1 Capsule Bạc = 10 điểm sự kiện\n|7|1 Capsule Vàng = 100 điểm sự kiện\n|7|1 lần Thử Vận May Ngọc Rồng = 100 điểm sự kiện\n", new String[]{"Đổi\nCapsule Bạc", "Đổi\nCapsule Vàng", "Thử Vận May\nNgọc Rồng"});
                        p.menuID = select;
                    }
                    if (select == 1) {
                        doMenuArray(p, idNpc, "Chào con, ta rất vui khi gặp con\nCon muốn làm gì nào?", new String[]{"Đại Hội\nVõ Thuật", "Trung\nUý Trắng"});
                        p.menuID = select;
                    }
                    if (select == 2) {
                        doMenuArray(p, idNpc, "Chào con, ta rất vui khi gặp con\nCon muốn làm gì nào?", new String[]{"Sức Mạnh", "Nạp Thẻ"});
                        p.menuID = select;
                    } else if (select == 3) {
                        doMenuArray(p, idNpc, "Chào con, ta rất vui khi gặp con\nCon muốn làm gì nào?", new String[]{"Giải tán\nbang hội", "Khu vực\nbang hội"});
                        p.menuID = select;
                    } else if (select == 4) {
                        doMenuArray(p, idNpc, "Chào con, ta rất vui khi gặp con\nCon muốn làm gì nào?", new String[]{"Kho Báu\nDưới Biển", "Hướng Dẫn"});
                        p.menuID = select;
                    }
                }
                break;
            }

            case 12: {//CUI DI CHUYEN
                if (p.map.id == 19) {
                    if (p.menuID == -1) {
                        if (select == 0) {
                            p.zone.goMapTransport(p, 105);
                            //                        GotoMap(p,109);
                        }
                        if (select == 1) {
                            p.zone.goMapTransport(p, 68);
                            //                        GotoMap(p,68);
                        }
                        if (select == 2) {
                            break;
                        }
                        if (select == 3) {
                            if (p.taskId == (short) 21 && p.crrTask.index == (byte) 0) {
                                doMenuArray(p, idNpc, "Ta vừa thấy tên Kuku, ngươi có muốn tới chỗ hắn không", new String[]{"OK\n100 ngọc", "Từ chối"});
                                p.menuID = select;
                            } else if (p.taskId == (short) 21 && p.crrTask.index == (byte) 1) {
                                doMenuArray(p, idNpc, "Ta vừa thấy tên Mập Đầu Đinh, ngươi có muốn tới chỗ hắn không", new String[]{"OK\n100 ngọc", "Từ chối"});
                                p.menuID = select;
                            } else if (p.taskId == (short) 21 && p.crrTask.index == (byte) 2) {
                                doMenuArray(p, idNpc, "Ta vừa thấy tên Rambo, ngươi có muốn tới chỗ hắn không", new String[]{"OK\n100 ngọc", "Từ chối"});
                                p.menuID = select;
                            }
                            break;
                        }
                    } else if (p.menuID == 3) {
                        if (select == 0 && p.taskId == (short) 21) {
                            if (p.crrTask.index == (byte) 0) {
                                if (Server.gI().mapKUKU != 0) {
                                    if (p.petfucus == 1) {
                                        p.zone.leaveDetu(p, p.detu);
                                    }
                                    if (p.pet2Follow == 1 && p.pet != null) {
                                        p.zone.leavePETTT(p.pet);
                                    }
                                    p.zone.leave(p);
                                    int _rdLocation = Util.nextInt(0, (Server.gI().maps[Server.gI().mapKUKU].template.arMobid.length - 1)); //get index mob random
                                    p.x = Server.gI().maps[Server.gI().mapKUKU].template.arrMobx[_rdLocation];
                                    p.y = Server.gI().maps[Server.gI().mapKUKU].template.arrMoby[_rdLocation];
                                    Server.gI().maps[Server.gI().mapKUKU].area[Server.gI().khuKUKU].EnterCapsule(p);
                                } else {
                                    Service.chatNPC(p, (short) p.menuNPCID, "Kuku chưa xuất hiện");
                                }
                            } else if (p.crrTask.index == (byte) 1) {
                                if (Server.gI().mapMDD != 0) {
                                    if (p.petfucus == 1) {
                                        p.zone.leaveDetu(p, p.detu);
                                    }
                                    if (p.pet2Follow == 1 && p.pet != null) {
                                        p.zone.leavePETTT(p.pet);
                                    }
                                    p.zone.leave(p);
                                    int _rdLocation = Util.nextInt(0, (Server.gI().maps[Server.gI().mapMDD].template.arMobid.length - 1)); //get index mob random
                                    p.x = Server.gI().maps[Server.gI().mapMDD].template.arrMobx[_rdLocation];
                                    p.y = Server.gI().maps[Server.gI().mapMDD].template.arrMoby[_rdLocation];
                                    Server.gI().maps[Server.gI().mapMDD].area[Server.gI().khuMDD].EnterCapsule(p);
                                } else {
                                    Service.chatNPC(p, (short) p.menuNPCID, "Mập Đầu Đinh chưa xuất hiện");
                                }
                            } else if (p.crrTask.index == (byte) 2) {
                                if (Server.gI().mapRAMBO != 0) {
                                    if (p.petfucus == 1) {
                                        p.zone.leaveDetu(p, p.detu);
                                    }
                                    if (p.pet2Follow == 1 && p.pet != null) {
                                        p.zone.leavePETTT(p.pet);
                                    }
                                    p.zone.leave(p);
                                    int _rdLocation = Util.nextInt(0, (Server.gI().maps[Server.gI().mapRAMBO].template.arMobid.length - 1)); //get index mob random
                                    p.x = Server.gI().maps[Server.gI().mapRAMBO].template.arrMobx[_rdLocation];
                                    p.y = Server.gI().maps[Server.gI().mapRAMBO].template.arrMoby[_rdLocation];
                                    Server.gI().maps[Server.gI().mapRAMBO].area[Server.gI().khuRAMBO].EnterCapsule(p);
                                } else {
                                    Service.chatNPC(p, (short) p.menuNPCID, "Rambo chưa xuất hiện");
                                }
                            }
                        }
                        break;
                    }
                } else if (p.map.id == 68) {
                    if (select == 0) {
                        p.zone.goMapTransport(p, 19);
                    }
                    if (select == 1) {
                        break;
                    }
                } else if (p.map.id == 26) {
                    if (select == 0) {
                        p.zone.goMapTransport(p, 24);
                    }
                    if (select == 1) {
                        p.zone.goMapTransport(p, 25);
                    }
                    if (select == 2) {
                        break;
                    }
                }
                break;
            }
            case 11: {
                if (p.map.id == 25) {
                    if (select == 0) {
                        p.zone.goMapTransport(p, 24);
                    }
                    if (select == 1) {
                        p.zone.goMapTransport(p, 26);
                    }
                    if (select == 2) {
                        break;
                    }
                }
                break;
            }
            case 10: {
                if (p.map.id == 24) {
                    if (select == 0) {
                        p.zone.goMapTransport(p, 25);
                    }
                    if (select == 1) {
                        p.zone.goMapTransport(p, 26);
                    }
                    if (select == 2) {
                        break;
                    }
                }
                break;
            }
            case 9: {
                if (p.map.id == 14) {
                    if (select == 0) {
                        TabItemShop[] test = Shop.getTabShop(9, 0).toArray(new TabItemShop[0]);
                        GameScr.UIshop(p, test);
                        break;
                    }
                }
                break;
            }
            case 8: { //DENDE
                if (p.map.id == 7) {
                    if (p.imgNRSD == (byte) 53) {
                        if (select == 1) {
                            if (p.nrNamec == 353) {
                                //                        if(Server.gI().firstNrNamec && p.zone.canCallDragonNamec(p)) {
                                //                            Server.gI().firstNrNamec = false;
                                //                            Server.gI().timeNrNamec = System.currentTimeMillis() + 600000;
                                //                            Service.chatNPC(p, (short)idNpc, "Ngọc bẩn quá, xin chờ em 9 phút nữa để lau bóng ngọc, gọi Rồng mới hiển linh");
                                //                            break;
                                //                        }
                                //                            if(!Server.gI().firstNrNamec) {
                                //                        if((Server.gI().timeNrNamec - System.currentTimeMillis()) > 0) {
                                //                            Service.chatNPC(p, (short)idNpc, "Ngọc bẩn quá, xin chờ em "+ (int)((Server.gI().timeNrNamec - System.currentTimeMillis())/60000) +" phút nữa để lau bóng ngọc, gọi Rồng mới hiển linh");
                                //                            break;
                                //                        } else { //GOI RONG NAMEC
                                if (p.zone.canCallDragonNamec(p)) {
                                    Server.gI().tOpenNrNamec = System.currentTimeMillis() + 86400000;
                                    Server.gI().firstNrNamec = true;
                                    Server.gI().timeNrNamec = 0;

                                    Service.gI().doneDragonNamec(p);
                                    //INIT NGOC RONG HOA THACH
                                    Service.gI().initNgocRongNamec((byte) 1);
                                    //TIMER TASK INIT LAI NGOC RONG NAMEC
                                    Service.gI().reInitNrNamec((long) 86399000);
                                    Service.gI().callDragonNamec(p);
                                    //UI CHON DIEU UOC NGOC RONG NAMEK
                                    p.menuID = -1;
                                    p.menuNPCID = 27;
                                    Menu.doMenuArray(p, 27, "Ta sẽ ban cho cả bang hội các ngươi 1 điều ước, ngươi có 5 phút, hãy\nsuy nghĩ thật kỹ trước khi quyết định", new String[]{"Đá bảo vệ\nx10", "Berry đeo\nlưng"});
                                    break;
                                }
                                //                        }
                                //                            break;
                                //                        } else {
                                //                               Service.chatNPC(p, (short)idNpc, "Ngọc bẩn quá, xin chờ em 9 phút nữa để lau bóng ngọc, gọi Rồng mới hiển linh");
                                //                            break;
                                //                            }
                            } else {
                                p.sendAddchatYellow("Anh phải có viên ngọc rồng Namếc 1 sao");
                            }
                        }
                    } else {
                        if (select == 0) {
                            TabItemShop[] test = Shop.getTabShop(8, 2).toArray(new TabItemShop[0]);
                            GameScr.UIshop(p, test);
                            break;
                        }
                    }
                }
                break;
            }
            case 7: {
                if (p.map.id == 0) {
                    if (select == 0) {
                        TabItemShop[] test = Shop.getTabShop(7, 1).toArray(new TabItemShop[0]);
                        GameScr.UIshop(p, test);
                        break;
                    }
                }
                break;
            }
            case 4: {
                if (p.map.id == 21 || p.map.id == 22 || p.map.id == 23) {
                    if (p.upMagicTree) { //NANG CAP DAU THAN
                        if (p.menuID == -1) {
                            if (select == 0) {
                                doMenuArray(p, idNpc, "Bạn có chắc chắn muốn nâng cấp nhanh?", new String[]{"OK", "Từ chối"});
                            }
                            if (select == 1) {
                                doMenuArray(p, idNpc, "Bạn có chắc chắn muốn hủy nâng cấp?", new String[]{"OK", "Từ chối"});
                            }
                            p.menuID = select;
                            break;
                        }
                        if (p.menuID == 0 && select == 0) {
                            if (p.ngoc >= ngocUpMagicTree(p.levelTree) && p.levelTree < (byte) 10) {
                                p.ngoc -= ngocUpMagicTree(p.levelTree);
                                Service.gI().buyDone(p);
                                p.levelTree = (byte) (p.levelTree + 1) > (byte) 10 ? (byte) 10 : (byte) (p.levelTree + 1);
                                p.upMagicTree = false;
                                p.maxBean = (byte) (p.maxBean + 2) > (byte) 23 ? (byte) 23 : (byte) (p.maxBean + 2);
                                p.lastTimeTree = System.currentTimeMillis();
                                Service.gI().MagicTree(p, (byte) 0);
                            } else {
                                p.sendAddchatYellow("Không đủ ngọc để nâng cấp đậu thần");
                            }
                            break;
                        }
                        if (p.menuID == 1 && select == 0) {
                            p.vang += goldUpMagicTree(p.levelTree) / 2;
                            Service.gI().buyDone(p);
                            p.upMagicTree = false;
                            p.lastTimeTree = System.currentTimeMillis();
                            Service.gI().MagicTree(p, (byte) 0);
                            p.sendAddchatYellow("Đã hủy nâng cấp đậu thần");
                            break;
                        }
                    } else {
                        if (p.menuID == -1) {
                            if (select == 0) {
                                int countDAUHT = 0;
                                //COUNT DAU THAN TRONG HANH TRANG
                                for (byte i = 0; i < p.ItemBag.length; i++) {
                                    if (p.ItemBag[i] != null && p.ItemBag[i].template.type == (byte) 6) {
                                        countDAUHT += p.ItemBag[i].quantity;
                                    }
                                }
                                int maxDauTheo = 20;
                                if (p.gender == (byte) 1) {
                                    maxDauTheo = 30;
                                }
                                if (countDAUHT < maxDauTheo) {
                                    byte countThu = (byte) (maxDauTheo - countDAUHT) > p.maxBean ? p.maxBean : (byte) (maxDauTheo - countDAUHT);
                                    p.currentBean -= countThu;
                                    p.lastTimeTree = System.currentTimeMillis() - (long) (p.currentBean * p.levelTree * 60000);
                                    int itemDAUTHAN = 13;
                                    if (p.levelTree == (byte) 2) {
                                        itemDAUTHAN = 60;
                                    } else if (p.levelTree == (byte) 3) {
                                        itemDAUTHAN = 61;
                                    } else if (p.levelTree == (byte) 4) {
                                        itemDAUTHAN = 62;
                                    } else if (p.levelTree == (byte) 5) {
                                        itemDAUTHAN = 63;
                                    } else if (p.levelTree == (byte) 6) {
                                        itemDAUTHAN = 64;
                                    } else if (p.levelTree == (byte) 7) {
                                        itemDAUTHAN = 65;
                                    } else if (p.levelTree == (byte) 8) {
                                        itemDAUTHAN = 352;
                                    } else if (p.levelTree == (byte) 9) {
                                        itemDAUTHAN = 523;
                                    } else if (p.levelTree == (byte) 10) {
                                        itemDAUTHAN = 595;
                                    }
                                    byte indexDAU = p.getIndexBagid(itemDAUTHAN);
                                    if (indexDAU != -1) {
                                        p.ItemBag[indexDAU].quantity += (int) countThu;
                                    } else {
                                        byte indexNOT = p.getIndexBagNotItem();
                                        if (p.getBagNull() == 0) {
                                            p.sendAddchatYellow("Hành trang không đủ chỗ trống!");
                                            return;
                                        } else {
                                            ItemSell dauThan = ItemSell.getItemSell(itemDAUTHAN, (byte) 1);
                                            Item dauThuHoach = new Item(dauThan.item);
                                            dauThuHoach.quantity = (int) countThu;
                                            p.ItemBag[indexNOT] = dauThuHoach;
                                        }
                                    }
                                    Service.gI().updateItemBag(p);
                                    p.sendAddchatYellow("Bạn vừa thu hoạch thành công " + countThu + " viên đậu thần cấp " + p.levelTree);
                                } else {
                                    p.sendAddchatYellow("Số đậu trong hành trang đã đầy");
                                }
                                Service.gI().MagicTree(p, (byte) 0);

                                p.getIndexItemBagByType((byte) 6); //GET DAU THAN TRONG HANH TRANG

                            }
                            if (p.levelTree >= (byte) 10) {
                                if (select == 1) { //THU DAU NHANH
                                    if (p.ngoc >= ngocThuDauThan(p.levelTree)) {
                                        p.ngoc -= ngocThuDauThan(p.levelTree);
                                        Service.gI().buyDone(p);
                                        p.currentBean = p.maxBean;
                                        p.lastTimeTree = 0;
                                        Service.gI().MagicTree(p, (byte) 0);
                                    } else {
                                        p.sendAddchatYellow("Không đủ ngọc để nâng cấp đậu thần");
                                    }
                                }
                            } else {
                                if (select == 1) {
                                    doMenuArray(p, idNpc, "Bạn có chắc chắn muốn nâng cấp đậu thần?", new String[]{"OK", "Từ chối"});
                                }
                                if (select == 2) { //THU DAU NHANH
                                    if (p.ngoc >= ngocThuDauThan(p.levelTree)) {
                                        p.ngoc -= ngocThuDauThan(p.levelTree);
                                        Service.gI().buyDone(p);
                                        p.currentBean = p.maxBean;
                                        p.lastTimeTree = 0;
                                        Service.gI().MagicTree(p, (byte) 0);
                                    } else {
                                        p.sendAddchatYellow("Không đủ ngọc để nâng cấp đậu thần");
                                    }
                                }
                            }
                            p.menuID = select;
                            break;
                        }
                        if (p.levelTree < (byte) 10) {
                            if (p.menuID == 1) {
                                if (select == 0) { //NANG CAP DAU THAN
                                    if (p.vang >= goldUpMagicTree(p.levelTree) && p.levelTree < (byte) 10) {
                                        p.vang -= goldUpMagicTree(p.levelTree);
                                        Service.gI().buyDone(p);
                                        //                        p.levelTree += (byte)1;
                                        p.upMagicTree = true;
                                        p.lastTimeTree = System.currentTimeMillis() + (long) (timeUpMagicTree(p.levelTree) * 1000);
                                        Service.gI().MagicTree(p, (byte) 0);
                                    } else {
                                        p.sendAddchatYellow("Không đủ vàng để nâng cấp đậu thần");
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
                break;
            }
            case 2: {
                if (p.map.id == 22) {
                    if (p.menuID == -1) {
                        if (select == 0) {
                            Service.chatNPC(p, (short) p.menuNPCID, "Một cậu bé có đuôi khỉ được tìm thấy bởi một ông lão sống một mình trong rừng, ông đặt tên là Son Goku và xem đứa bé như là cháu của mình. Một ngày nọ Goku tình cờ gặp một cô gái tên là Bulma trên đường đi bắt cá về, Goku và Bulma đã cùng nhau truy tìm bảy viên ngọc rồng. Các viên ngọc rồng này chứa đựng một bí mật có thể triệu hồi một con rồng và ban điều ước cho ai sở hữu chúng. Trên cuộc hành trình dài đi tìm những viên ngọc rồng, họ gặp những người bạn (Yamcha, Krillin,Yajirobe, Thiên, Giáo tử, Oolong,...) và những đấu sĩ huyền thoại cũng như nhiều ác quỷ. Họ trải qua những khó khăn và học hỏi các chiêu thức võ thuật đặc biệt để tham gia thi đấu trong đại hội võ thuật thế giới được tổ chức hằng năm. Ngoài các sự kiện đại hội võ thuật, Goku và các bạn còn phải đối phó với các thế lực độc ác như Đại vương Pilaf, Quân đoàn khăn đỏ của Độc nhãn tướng quân, Đại ma vương Picollo và những đứa con của hắn. Chiến binh người Saiya: Radiz, Hoàng tử Saiya Vegeta cùng tên cận vệ Nappa. Rồi họ đi đến Namek, gặp rồng thần của Namek; chạm trán Frieza, khi trở về Trái Đất đụng độ Nhóm android sát thủ (các Android 16, 17, 18,19, 20) và sau đó là quái vật từ tương lai Cell, Kẻ thù từ vũ trụ Majin Buu, thần hủy diệt Beerus, các đối thủ từ các vũ trụ song song, Đối thủ mạnh nhất với Goku là Jiren (đến từ vũ trụ 11).");
                            break;
                        }
//                        if (select == 1) {
//                            long _vang = ((long) p.vang + 500000000) > 2000000000L ? 2000000000L : ((long) p.vang + 500000000);
//                            p.vang = _vang;
//                            Service.gI().buyDone(p);
//                            p.sendAddchatYellow("Nhận thành công 500 triệu vàng");
//                            break;
//                        }
//                        if (select == 2) {
//                            if (p.ngoc < 10000000) {
//                                p.ngoc = p.ngoc + 100000;
//                                Service.gI().buyDone(p);
//                                p.sendAddchatYellow("Nhận thành công 100k ngọc");
//                            } else {
//                                p.sendAddchatYellow("Vui lòng sử dụng hết ngọc");
//                            }
//                            break;
//                        }
                        if (p.hasTrungMabu && select == 1) {
                            doMenuArray(p, idNpc, "Con có muốn nhận đệ tử Mabư, sẽ thay thế đệ tử hiện tại nếu có", new String[]{"OK", "Xóa đệ\nMabư", "Đóng"});
                            p.menuID = select;
                            break;
                        }
                    }
                    if (p.hasTrungMabu) {
                        if (p.menuID == 1) {
                            if (select == 0) {
                                p.statusPet = 0;
                                p.detu = null;
                                p.detu = new Detu();
                                p.detu.initDetuMabu(p.detu, p.gender);
                                p.detu.id = (-100000 - p.id);
                                p.hasTrungMabu = false;
                            } else if (select == 1) {
                                p.hasTrungMabu = false;
                            } else {
                                break;
                            }
                        }
                    }
                }
                break;
            }
            case 1: {
                if (p.map.id == 23) {
                    if (p.menuID == -1) {
                        if (select == 0) {
                            Service.chatNPC(p, (short) p.menuNPCID, "Một cậu bé có đuôi khỉ được tìm thấy bởi một ông lão sống một mình trong rừng, ông đặt tên là Son Goku và xem đứa bé như là cháu của mình. Một ngày nọ Goku tình cờ gặp một cô gái tên là Bulma trên đường đi bắt cá về, Goku và Bulma đã cùng nhau truy tìm bảy viên ngọc rồng. Các viên ngọc rồng này chứa đựng một bí mật có thể triệu hồi một con rồng và ban điều ước cho ai sở hữu chúng. Trên cuộc hành trình dài đi tìm những viên ngọc rồng, họ gặp những người bạn (Yamcha, Krillin,Yajirobe, Thiên, Giáo tử, Oolong,...) và những đấu sĩ huyền thoại cũng như nhiều ác quỷ. Họ trải qua những khó khăn và học hỏi các chiêu thức võ thuật đặc biệt để tham gia thi đấu trong đại hội võ thuật thế giới được tổ chức hằng năm. Ngoài các sự kiện đại hội võ thuật, Goku và các bạn còn phải đối phó với các thế lực độc ác như Đại vương Pilaf, Quân đoàn khăn đỏ của Độc nhãn tướng quân, Đại ma vương Picollo và những đứa con của hắn. Chiến binh người Saiya: Radiz, Hoàng tử Saiya Vegeta cùng tên cận vệ Nappa. Rồi họ đi đến Namek, gặp rồng thần của Namek; chạm trán Frieza, khi trở về Trái Đất đụng độ Nhóm android sát thủ (các Android 16, 17, 18,19, 20) và sau đó là quái vật từ tương lai Cell, Kẻ thù từ vũ trụ Majin Buu, thần hủy diệt Beerus, các đối thủ từ các vũ trụ song song, Đối thủ mạnh nhất với Goku là Jiren (đến từ vũ trụ 11).");
                            break;
                        }
//                        if (select == 1) {
//                            long _vang = ((long) p.vang + 500000000) > 2000000000L ? 2000000000L : ((long) p.vang + 500000000);
//                            p.vang = _vang;
//                            Service.gI().buyDone(p);
//                            p.sendAddchatYellow("Nhận thành công 500 triệu vàng");
//                            break;
//                        }
//                        if (select == 2) {
//                            if (p.ngoc < 10000000) {
//                                p.ngoc = p.ngoc + 100000;
//                                Service.gI().buyDone(p);
//                                p.sendAddchatYellow("Nhận thành công 100k ngọc");
//                            } else {
//                                p.sendAddchatYellow("Vui lòng sử dụng hết ngọc");
//                            }
//                            break;
//                        }
                        if (p.hasTrungMabu && select == 1) {
                            doMenuArray(p, idNpc, "Con có muốn nhận đệ tử Mabư, sẽ thay thế đệ tử hiện tại nếu có", new String[]{"OK", "Xóa đệ\nMabư", "Đóng"});
                            p.menuID = select;
                            break;
                        }
                    }
                    if (p.hasTrungMabu) {
                        if (p.menuID == 1) {
                            if (select == 0) {
                                p.statusPet = 0;
                                p.detu = null;
                                p.detu = new Detu();
                                p.detu.initDetuMabu(p.detu, p.gender);
                                p.detu.id = (-100000 - p.id);
                                p.hasTrungMabu = false;
                            } else if (select == 1) {
                                p.hasTrungMabu = false;
                            } else {
                                break;
                            }
                        }
                    }
                }
                break;
            }
            case 0: {
                if (p.map.id == 21) {
                    if (p.menuID == -1) {
                        if (select == 0) {
                            Service.chatNPC(p, (short) p.menuNPCID, "Một cậu bé có đuôi khỉ được tìm thấy bởi một ông lão sống một mình trong rừng, ông đặt tên là Son Goku và xem đứa bé như là cháu của mình. Một ngày nọ Goku tình cờ gặp một cô gái tên là Bulma trên đường đi bắt cá về, Goku và Bulma đã cùng nhau truy tìm bảy viên ngọc rồng. Các viên ngọc rồng này chứa đựng một bí mật có thể triệu hồi một con rồng và ban điều ước cho ai sở hữu chúng. Trên cuộc hành trình dài đi tìm những viên ngọc rồng, họ gặp những người bạn (Yamcha, Krillin,Yajirobe, Thiên, Giáo tử, Oolong,...) và những đấu sĩ huyền thoại cũng như nhiều ác quỷ. Họ trải qua những khó khăn và học hỏi các chiêu thức võ thuật đặc biệt để tham gia thi đấu trong đại hội võ thuật thế giới được tổ chức hằng năm. Ngoài các sự kiện đại hội võ thuật, Goku và các bạn còn phải đối phó với các thế lực độc ác như Đại vương Pilaf, Quân đoàn khăn đỏ của Độc nhãn tướng quân, Đại ma vương Picollo và những đứa con của hắn. Chiến binh người Saiya: Radiz, Hoàng tử Saiya Vegeta cùng tên cận vệ Nappa. Rồi họ đi đến Namek, gặp rồng thần của Namek; chạm trán Frieza, khi trở về Trái Đất đụng độ Nhóm android sát thủ (các Android 16, 17, 18,19, 20) và sau đó là quái vật từ tương lai Cell, Kẻ thù từ vũ trụ Majin Buu, thần hủy diệt Beerus, các đối thủ từ các vũ trụ song song, Đối thủ mạnh nhất với Goku là Jiren (đến từ vũ trụ 11).");
                            //Service.gI().clientInput(p, "Nhập Mã Quà Tặng", "Mã Quà Tặng", (byte) 0);
                            break;
                        }
//                        if (select == 1) {
//                            long _vang = ((long) p.vang + 500000000) > 2000000000L ? 2000000000L : ((long) p.vang + 500000000);
//                            p.vang = _vang;
//                            Service.gI().buyDone(p);
//                            p.sendAddchatYellow("Nhận thành công 500 triệu vàng");
//                            break;
//                        }
//                        if (select == 2) {
//                            if (p.ngoc < 10000000) {
//                                p.ngoc = p.ngoc + 100000;
//                                Service.gI().buyDone(p);
//                                p.sendAddchatYellow("Nhận thành công 100k ngọc");
//                            } else {
//                                p.sendAddchatYellow("Vui lòng sử dụng hết ngọc");
//                            }
//                            break;
//                        }
                        if (p.hasTrungMabu && select == 1) {
                            doMenuArray(p, idNpc, "Con có muốn nhận đệ tử Mabư, sẽ thay thế đệ tử hiện tại nếu có", new String[]{"OK", "Xóa đệ\nMabư", "Đóng"});
                            p.menuID = select;
                            break;
                        }
                    }
                    if (p.hasTrungMabu) {
                        if (p.menuID == 1) {
                            if (select == 0) {
                                p.statusPet = 0;
                                p.detu = null;
                                p.detu = new Detu();
                                p.detu.initDetuMabu(p.detu, p.gender);
                                p.detu.id = (-100000 - p.id);
                                p.hasTrungMabu = false;
                            } else if (select == 1) {
                                p.hasTrungMabu = false;
                            } else {
                                break;
                            }
                        }
                    }
                }
                break;
            }
            default: {
                Service.gI().sendTB(p.session, 0, "Chức Năng Đang Được Cập Nhật " + idNpc, 0);
                break;
            }
        }
        m.cleanup();
    }

    public void GotoMap(Player p, int id) {
        Map maptele = MainManager.getMapid(id);
        Controller.getInstance().teleportToMAP(p, maptele);
    }

    public void menuHandler(Player p, Message m) throws IOException, SQLException, InterruptedException {
        byte idNPC = m.reader().readByte();// ID NPC
        byte menuID = m.reader().readByte();// Lớp nút 1
        byte select = m.reader().readByte();// Lớp nút 2
//         System.out.println("menuID: "+ p.menuID);
//         System.out.println("menuNPCID: "+ p.menuNPCID);
//         System.out.println("select: "+ select);
        int tl;
        switch (p.menuNPCID) {

            case 13:
                if (p.menuID == 1) {
                    if (select == 0) {
                        p.openBox();
                    }
                }
                break;

            default: {
                Service.gI().sendTB(p.session, 0, "Chức Năng Đang Được Cập Nhật " + idNPC, 0);
                break;
            }

            //   Service.getInstance().serverMessage(p.session,"ID NPC " + b1);
        }
        m.cleanup();
    }

    public void openUINpc(Player p, Message m) throws IOException {
        short idnpc = m.reader().readShort();//idnpc
        int avatar;
        m.cleanup();
        p.menuID = -1;
        p.menuNPCID = idnpc;
        avatar = NpcAvatar(p, idnpc);
        m = new Message(33);

        if (p.menuNPCID == 74 && p.map.id == 5) {
            doMenuArray(p, idnpc, "Chào mừng đến với cửa hàng của ta", new String[]{"OK", "Từ chối"});
            return;
        } else if (p.menuNPCID == 73) {
            doMenuArray(p, idnpc, "Đổi password", new String[]{"OK", "Từ chối"});
            return;
        } else if (p.menuNPCID == 72 && p.map.id == 160) {
            doMenuArray(p, idnpc, "Chào mừng đến với cửa hàng của ta, sử dụng 10 huy chương đồng và 1000 ngọc để đổi những món đồ giá trị với chỉ số ngẫu nhiên lên tới 30% (Thu thập huy chương đồng bằng cách tiêu diệt Black Goku hoặc Cooler)", new String[]{"OK", "Từ chối"});
            return;
        } else if (p.menuNPCID == 71 && p.map.id == 161) {
            if (p.taskId == (short) 31) {
                if (p.crrTask.index == (byte) 5 && p.crrTask.subtasks[p.crrTask.index] == (byte) p.menuNPCID) {
                    TaskService.gI().updateCountTask(p);
                    Service.chatNPC(p, (short) p.menuNPCID, "Cậu sẽ đưa tôi về chỗ Bardock thật sao?");
                    return;
                }
            } else if (p.taskId == (short) 32) {
                if (p.crrTask.index == (byte) 2 && p.crrTask.subtasks[p.crrTask.index] == (byte) p.menuNPCID) {
                    Item thuocMo = ItemSell.getItemNotSell(1016);
                    Item _thuocMo = new Item(thuocMo);
                    p.addItemToBag(_thuocMo);
                    Service.gI().updateItemBag(p);
                    p.sendAddchatYellow("Bạn nhận được Thuốc mỡ Ipana");
                    TaskService.gI().updateCountTask(p);
                    Service.chatNPC(p, (short) p.menuNPCID, "Cậu đã chạm trán tên đó rồi sao?");
                    return;
                }
            }
            Service.chatNPC(p, (short) p.menuNPCID, "Đi đu đưa đi...");
            return;
        } else if (p.menuNPCID == 70 && p.map.id == 160) { //BARDOCK
            if (p.taskId == (short) 31) {
                if (p.crrTask.index == (byte) 2 && p.crrTask.subtasks[p.crrTask.index] == (byte) p.menuNPCID) {
                    TaskService.gI().updateCountTask(p);
                    Service.chatNPC(p, (short) p.menuNPCID, "Tôi tên là Bardock, người Xayda\bHành tinh của tôi vừa bị Fide phá hủy\bKhông biết tại sao tôi thoát chết...\bvà xuất hiện tại nơi này nữa\bTôi đang bị thương, cậu hãy giúp tôi hạ đám lính ngoài kia");
                    return;
                } else if (p.crrTask.index == (byte) 4 && p.crrTask.subtasks[p.crrTask.index] == (byte) p.menuNPCID) {
                    TaskService.gI().updateCountTask(p);
                    Service.chatNPC(p, (short) p.menuNPCID, "Cảm ơn cậu\bGiờ nhờ cậu đi tìm nhóc Berry về giúp tôi\bcó thể cậu nhóc loanh quanh ở Bờ Rừng Nguyên Thủy");
                    return;
                } else if (p.crrTask.index == (byte) 6 && p.crrTask.subtasks[p.crrTask.index] == (byte) p.menuNPCID) {
                    TaskService.gI().updateCountTask(p);
                    Service.chatNPC(p, (short) p.menuNPCID, "Mơn cậu lần nữa\bHiện tại trong hang không còn gì để ăn\bCậu hãy giúp tôi tìm một ít lương thực");
                    return;
                } else if (p.crrTask.index == (byte) 7 && p.crrTask.subtasks[p.crrTask.index] == (byte) p.menuNPCID) {
                    for (byte i = 0; i < p.ItemBag.length; i++) {
                        if (p.ItemBag[i] != null && p.ItemBag[i].template.id == 993 && (p.ItemBag[i].quantity == 99)) {
                            p.ItemBag[i] = null;
                            TaskService.gI().updateCountTask(p);
                            Service.gI().updateItemBag(p);
                            Service.chatNPC(p, (short) p.menuNPCID, "Mơn cậu thêm lần nữa\bVới số lương thực này tôi sẽ sớm bình phục\bNgoài kia bọn lính đang ức hiếp cư dân hành tinh này\bMong cậu có thể ra sức lần nữa để cứu họ");
                            return;
                        }
                    }
                    Service.chatNPC(p, (short) p.menuNPCID, "Cậu thu thập 99 giỏ thức ăn để dự trữ");
                    return;
                } else if (p.crrTask.subtasks[(byte) (p.crrTask.countSub - (byte) 1)] == (byte) p.menuNPCID && p.crrTask.index == (byte) (p.crrTask.countSub - (byte) 1)) {
                    if (p.power < (p.getPowerLimit() * 1000000000L)) {
                        p.tiemNang += p.crrTask.bonus;
                        p.power += p.crrTask.bonus;
                    }
                    p.UpdateSMTN((byte) 2, p.crrTask.bonus);
                    p.sendAddchatYellow("Bạn vừa được thưởng " + Util.powerToString(p.crrTask.bonus) + " sức mạnh");
                    Service.chatNPC(p, (short) p.menuNPCID, TaskManager.gI().textChatTASK[p.taskId]);
                    TaskService.gI().setupNextNewTask(p, (byte) (p.taskId + (byte) 1));
                    return;
                }
            }
            Service.chatNPC(p, (short) p.menuNPCID, "Sáng ra suối, tối vào hang...");
            return;
        } else if (p.menuNPCID == 67 && p.map.id == 0) { //MR POPO
            doMenuArray(p, idnpc, "Thượng đế vừa phát hiện 1 loại khí đang âm thầm\nhủy diệt mọi mầm sống trên Trái Đất,\nnó được gọi là Destron Gas.\nTa sẽ đưa các cậu đến nơi ấy, các cậu sẵn sàng chưa?", new String[]{"Thông tin\nChi tiết", "Top 100\nBang hội", "Thành tích\nBang", "OK", "Từ chối"});
            return;
        } else if (p.menuNPCID == 61) {//DOI YARDART
            doMenuArray(p, idnpc, "Hãy cố gắng tập luyện\nThu thập 9.999 bí kiếp để đổi trang phục Yardrat nhé", new String[]{"Nhận thưởng", "OK"});
            return;
        } else if (p.menuNPCID == 60 && (p.map.id == 80 || p.map.id == 131)) { //GOKU NUI KHI VANG
            doMenuArray(p, idnpc, "Ta mới hạ Fide, nhưng nó đã kịp đào 1 cái lỗ\nHành tinh này sắp nổ tinh rồi\nMau lượn thôi", new String[]{"Chuẩn"});
            return;
        } else if (p.menuNPCID == 56) {
            if (p.map.id == 154) {
                doMenuArray(p, idnpc, "Thử đánh với ta xem nào.\nNgươi còn 1 lượt nữa cơ mà.", new String[]{"Nói chuyện", "Top 100", "[LV:1]"});
            }
            return;
        } else if (p.menuNPCID == 55) { //THAN HUY DIET BILL
            if (p.map.id == 48) {
                doMenuArray(p, idnpc, "Không muốn hành tinh này bị hủy diệt thì mang 99 phần đồ ăn tới đây,\nta sẽ cho một món đồ Hủy Diệt.\nPhục vụ tốt ta có thể cho trang bị mạnh mẽ hơn đến 15%", new String[]{"Đồng ý", "Từ chối"});
            } else if (p.map.id == 154) {
                doMenuArray(p, idnpc, "...", new String[]{"Về\nThánh Địa\nKaio", "Từ chối"});
            }
            return;
        } else if (p.menuNPCID == 54) { //TAPION
            if (p.map.id == 5) {
                doMenuArray(p, idnpc, "Chị sắp đi cướp ngân hàng em có muốn đi cùng không ?", new String[]{"Đổi Đệ Tử", "Từ chối"});
            }
            return;
        } else if (p.menuNPCID == 53) { //TAPION
            if (p.map.id == 19) {
                doMenuArray(p, idnpc, "Ác quỷ truyền thuyết Hirudegarn\nđã thoát khỏi phong ấn ngàn năm\nHãy giúp tôi chế ngự nó", new String[]{"OK", "Từ chối"});
            } else if (p.map.id == 126) {
                doMenuArray(p, idnpc, "Ngươi muốn bỏ chạy sao?", new String[]{"OK", "Từ chối"});
            }
            return;

        } else if (p.menuNPCID == 52) { //HUNG VUONG
            if (p.map.id == 0) {
                doMenuArray(p, idnpc, "Tạm thời ta sẽ ở đây bán cải trang cho các cậu", new String[]{"Cửa hàng"});
            }
            return;
        } else if (p.menuNPCID == 48) { //NGO KHONG NGU HANH SON
            if (p.map.id == 122) {
                doMenuArray(p, idnpc, "Hãy giải phong ấn cho ta, ta sẽ tặng ngươi một món quà", new String[]{"Cửa hàng"});
            }
            return;
        } else if (p.menuNPCID == 49) { //DUONG TANG
            if (p.map.id == 0) {
                doMenuArray(p, idnpc, "A mi phò phò, thí chủ hãy giúp giải cứu đồ đệ của bần tăng đang bị\nphong ấn tại ngũ hành sơn.", new String[]{"Đồng ý", "Từ chối", "Nhận thưởng"});
            } else if (p.map.id == 123) {
                doMenuArray(p, idnpc, "Thí chủ muốn trở về sao ?", new String[]{"Đồng ý", "Từ chối"});
            } else if (p.map.id == 122) {
                doMenuArray(p, idnpc, "A mi phò phò, thí chủ hãy thu thập bùa 'giải khai phong ấn', mỗi chữ 10 cái.", new String[]{"Giải\nPhong ấn", "Về\nLàng Aru", "Top\nHoa quả"});
            }
            return;
        } else if (p.menuNPCID == 47 && p.map.id == 153) { //NGUU MA VUONG
            doMenuArray(p, idnpc, "Ngươi đang muốn tìm mảnh vỡ và mảnh hồn bông tai Porata trong\ntruyền thuyết, ta sẽ đưa ngươi đến đó ?", new String[]{"OK", "Đóng"});
            return;
        } else if (p.menuNPCID == 46) { //BABIDAY
            if (p.map.id == 114 || p.map.id == 115 || p.map.id == 117 || p.map.id == 118 || p.map.id == 119 || p.map.id == 120) {
                if (p.cPk == (byte) 11) {
                    doMenuArray(p, idnpc, "Bọn Kaio do con nhóc Ôsin cầm đầu đã có mặt tại đây...Hãy chuẩn bị 'Tiếp\nkhách' nhé!", new String[]{"Hướng\ndẫn\nthêm", "Giải trừ\nphép thuật\n1 ngọc", "Xuống\nTầng dưới", "Về nhà"});
                } else {
                    doMenuArray(p, idnpc, "Haha...", new String[]{"OK"});
                }
            }
            return;
        } else if (p.menuNPCID == 44) { //OSSIN
            if (p.taskId == (short) 30) {
                if (p.crrTask.index == (byte) 0 && p.crrTask.subtasks[p.crrTask.index] == (byte) p.menuNPCID) {
                    TaskService.gI().updateCountTask(p);
                    Service.chatNPC(p, (short) p.menuNPCID, "Cậu hãy chuẩn bị để đi cùng chúng tôi");
                    return;
                } else if (p.crrTask.subtasks[(byte) (p.crrTask.countSub - (byte) 1)] == (byte) p.menuNPCID && p.crrTask.index == (byte) (p.crrTask.countSub - (byte) 1)) {
                    if (p.power < (p.getPowerLimit() * 1000000000L)) {
                        p.tiemNang += p.crrTask.bonus;
                        p.power += p.crrTask.bonus;
                    }
                    p.UpdateSMTN((byte) 2, p.crrTask.bonus);
                    p.sendAddchatYellow("Bạn vừa được thưởng " + Util.powerToString(p.crrTask.bonus) + " sức mạnh");
                    Service.chatNPC(p, (short) p.menuNPCID, TaskManager.gI().textChatTASK[p.taskId]);
                    TaskService.gI().setupNextNewTask(p, (byte) (p.taskId + (byte) 1));
                    return;
                }
            }
            if (p.map.id == 50) {
                doMenuArray(p, idnpc, "Ta có thể giúp gì ngươi?", new String[]{"Đến Kaio", "Đến\nhành tinh\nBill", "Từ chối"});
            } else if (p.map.id == 154) {
                doMenuArray(p, idnpc, "Ta có thể giúp gì ngươi?", new String[]{"Cửa hàng", "Đến\nhành tinh\nNgục tù", "Từ chối"});
            } else if (p.map.id == 155) {
                doMenuArray(p, idnpc, "Ta có thể giúp gì ngươi?", new String[]{"Đến\nhành tinh\nBill", "Từ chối"});
            } else if (p.map.id == 52) {
                doMenuArray(p, idnpc, "Bây giờ tôi sẽ bí mật...\nđuổi theo 2 tên đồ tể...\nQuý vị nào muốn đi theo thì xin mời!", new String[]{"OK", "Từ chối"});
            } else if (p.map.id == 114 || p.map.id == 115 || p.map.id == 117 || p.map.id == 118 || p.map.id == 119 || p.map.id == 120) {
                if (p.cPk == (byte) 10) {
                    doMenuArray(p, idnpc, "Đừng vội xem thường Babiđây, ngay đến cha hắn là thần ma đạo sĩ\nBibiđây khi còn sống cũng phải sợ hắn đấy!", new String[]{"Hướng\ndẫn\nthêm", "Giải trừ\nphép thuật\n1 ngọc", "Xuống\nTầng dưới", "Về nhà"});
                } else {
                    doMenuArray(p, idnpc, "Haha...", new String[]{"OK"});
                }
            }
            return;
        } else if (p.menuNPCID == 41) { //TRUNG THU
            if (p.map.id == 14) {
                doMenuArray(p, idnpc, "Trung thu đến rồi, xin chào!", new String[]{"Cửa hàng"});
            }
            return;
        } else if ((p.menuNPCID <= 36 && p.menuNPCID >= 30) && (p.map.id >= 85 && p.map.id <= 91)) { //NGOC RONG SAO DEN
            doMenuArray(p, idnpc, "Ta có thể giúp gì cho người?", new String[]{"Phù hộ", "Từ chối"});
            return;
        } else if (p.menuNPCID == 29 && (p.map.id == 24 || p.map.id == 25 || p.map.id == 26)) { //NGOC RONG SAO DEN
            if (p.indexNRSD.size() > 0) {
                doMenuArray(p, idnpc, "Đường đến ngọc rồng sao đen đã mở, ngươi có muốn tham gia không?", new String[]{"Hướng\ndẫn\nthêm", "Nhận thưởng", "Tham gia", "Từ chối"});
            } else {
                doMenuArray(p, idnpc, "Đường đến ngọc rồng sao đen đã mở, ngươi có muốn tham gia không?", new String[]{"Hướng\ndẫn\nthêm", "Tham gia", "Từ chối"});
            }
            return;
        } else if (p.menuNPCID == 23 && p.map.id == 52) {  //GHI DANH DAI HOI VO THUAT
            //Đã hết hạn đăng ký thi đấu, xin vui lòng chờ đến giải sau vào lúc 15h
            if (DaiHoiManager.gI().openDHVT) {
                String nameDH = DaiHoiManager.gI().nameRoundDHVT();
                doMenuArray(p, idnpc, "Chào mừng bạn đến với đại hội võ thuật\bGiải " + nameDH + " đang có " + DaiHoiManager.gI().lstIDPlayers.size() + " người đăng ký thi đấu", new String[]{"Thông tin\bChi tiết", "Đăng ký", "Giải\nSiêu Hạng", "Đại Hội\nVõ Thuật\nLần thứ\n23"});
            } else {
                doMenuArray(p, idnpc, "Đã hết hạn đăng ký thi đấu, xin vui lòng chờ đến giải sau", new String[]{"Thông tin\bChi tiết", "OK", "Giải\nSiêu Hạng", "Đại Hội\nVõ Thuật\nLần thứ\n23"});
            }
            return;
        } else if (p.menuNPCID == 21) {
            if (p.map.id == 5) {
                doMenuArray(p, idnpc, "Ngươi tìm ta có việc gì?", new String[]{"Ép sao trang\nbị", "Pha lê hóa\ntrang bị", "Chuyển hóa Trang\nbị", "Võ đài Sinh\nTử"});
            } else if (p.map.id == 42 || p.map.id == 43 || p.map.id == 44) {
                doMenuArray(p, idnpc, "Ngươi tìm ta có việc gì?", new String[]{"Cửa hàng\nBùa", "Nâng cấp Vật\n phẩm", "Làm phép\nNhập đá", "Nhập\nNgọc Rồng", "Nâng cấp\nBông tai\nPorata", "Mở chỉ số\nBông tai\nPorata cấp 2"});
            }
            return;
        } else if (p.menuNPCID == 20 && p.map.id == 48) {  //THAN KAIO
            doMenuArray(p, idnpc, "Con muốn quay về sao?", new String[]{"Về Thần Điện", "Thánh Địa\nKaio", "Con Đường\nRắn Độc", "Từ Chối"});
            return;
        } else if (p.menuNPCID == 18 && p.map.id == 46) {  //THAN MEO
            doMenuArray(p, idnpc, "Tải Game Tại NRO LOR.Vn?", new String[]{"Nói Chuyện", "Từ Chối"});
            return;
        } else if (p.menuNPCID == 19 && p.map.id == 141) {
            doMenuArray(p, idnpc, "Giỏi lắm tiêu diệt được đám quái vật đó rồi à, Ta còn nhiệm vụ khác cho con đây !", new String[]{"Đến Chỗ\nCađíc", "Về\nThánh Địa\nKaio", "Từ Chối"});
            return;
        } else if (p.menuNPCID == 19 && p.map.id == 45) {
            doMenuArray(p, idnpc, "Con đã mạnh hơn ta, ta sẽ chỉ đường cho con đến Kaio để gặp thần Vũ\nTrụ Phương Bắc\nNgài là thần cai quản vũ trụ này, hãy theo ngài ấy học võ công", new String[]{"Đến Kaio", "Quay ngọc\nMay mắn"});
            return;
        } else if (p.menuNPCID == 10 && p.map.id == 24) {
            doMenuArray(p, idnpc, "Tàu Vũ Trụ của tôi có thể đưa cậu đến hành tinh khác trong 3 giây. Cậu muốn đi đâu", new String[]{"Đến\nNamếc", "Đến\nXayda", "Từ Chối"});
            return;
        } else if (p.menuNPCID == 11 && p.map.id == 25) {
            doMenuArray(p, idnpc, "Tàu Vũ Trụ Namếc tuy cũ nhưng tốc độ không hề kém bất kỳ loại tầu nào khác. Cậu muốn đi đâu?", new String[]{"Đến\nTrái Đất", "Đến\nXayda", "Từ Chối"});
            return;
        } else if (p.menuNPCID == 12) {
            if (p.map.id == 19) {
                if (p.taskId == (short) 21 && p.crrTask.index == (byte) 0) {
                    doMenuArray(p, idnpc, "Đội quân Fide đang ở Thung Lũng Nappa, ta sẽ đưa ngươi đến đó", new String[]{"Đến\nCold", "Đến\nNappa", "Từ Chối", "Đến chỗ\nKuku"});
                } else if (p.taskId == (short) 21 && p.crrTask.index == (byte) 1) {
                    doMenuArray(p, idnpc, "Đội quân Fide đang ở Thung Lũng Nappa, ta sẽ đưa ngươi đến đó", new String[]{"Đến\nCold", "Đến\nNappa", "Từ Chối", "Đến chỗ\nMập Đầu Đinh"});
                } else if (p.taskId == (short) 21 && p.crrTask.index == (byte) 2) {
                    doMenuArray(p, idnpc, "Đội quân Fide đang ở Thung Lũng Nappa, ta sẽ đưa ngươi đến đó", new String[]{"Đến\nCold", "Đến\nNappa", "Từ Chối", "Đến chỗ\nRambo"});
                } else {
                    doMenuArray(p, idnpc, "Đội quân Fide đang ở Thung Lũng Nappa, ta sẽ đưa ngươi đến đó", new String[]{"Đến\nCold", "Đến\nNappa", "Từ Chối"});
                }
            } else if (p.map.id == 68) {
                doMenuArray(p, idnpc, "Ngươi muốn bỏ chạy ư?", new String[]{"Đồng ý", "Từ Chối"});
            } else if (p.map.id == 26) {
                doMenuArray(p, idnpc, "Tàu vũ trụ Xayda sử dụng công nghệ mới nhất, có thể đưa ngươi đi bất kỳ đâu, chỉ cần trả tiền là được", new String[]{"Đến\nTrái Đất", "Đến\nNamếc", "Từ Chối"});
            }
            return;
        } else if (p.menuNPCID == 42 && p.map.id == 43) { //Quoc vuong mo gioi han suc manh
            doMenuArray(p, idnpc, "Con muốn nâng giới hạn sức mạnh cho bản thân hay đệ tử?", new String[]{"Bản thân", "Đệ tử", "Từ Chối"});
            return;
        } else if (p.menuNPCID == 38 && (p.map.id == 27 || p.map.id == 102)) { //TRUNKS SANG TUONG LAI
            if (p.taskId >= (short) 23) {
                if (p.taskId == (short) 24 && p.crrTask.index == (byte) 1 && p.crrTask.subtasks[p.crrTask.index] == (byte) p.menuNPCID) {
                    TaskService.gI().updateCountTask(p);
                    Service.chatNPC(p, (short) p.menuNPCID, "Cháu đến từ tương lai và cháu cần các chú giúp. Đây là thuốc trợ tim cho Quy Lão, không lâu nữa Quy Lão sẽ bị bệnh.");
                    return;
                }
                if (p.map.id != 102) {
                    doMenuArray(p, idnpc, "Chào chú cháu có thể giúp gì?", new String[]{"Đi đến Tương lai", "Từ Chối"});
                    return;
                } else {
                    doMenuArray(p, idnpc, "Chào chú cháu có thể giúp gì?", new String[]{"Quay về\nQuá khứ", "Từ Chối"});
                    return;
                }
            } else {
                p.sendAddchatYellow("Phải hoàn thành nhiệm vụ trước khi tới đây");
                Service.gI().buyDone(p);
            }
            return;
        } else if (p.menuNPCID == 37 && p.map.id == 102) { //BUNMA TUONG LAI
            if (p.taskId == (short) 24 && p.crrTask.index == (byte) 3 && p.crrTask.subtasks[p.crrTask.index] == (byte) p.menuNPCID) {
                TaskService.gI().updateCountTask(p);
                Service.chatNPC(p, (short) p.menuNPCID, "Cảm ơn cậu đã đến đây, chúng tôi đang gặp rắc rối lớn. Hãy giúp chúng tôi tiêu diệt lũ Xên gần đây");
                return;
            } else {
                if (p.crrTask.subtasks[(byte) (p.crrTask.countSub - (byte) 1)] == (byte) p.menuNPCID && p.crrTask.index == (byte) (p.crrTask.countSub - (byte) 1)) {
                    if (p.power < (p.getPowerLimit() * 1000000000L)) {
                        p.tiemNang += p.crrTask.bonus;
                        p.power += p.crrTask.bonus;
                    }
                    p.UpdateSMTN((byte) 2, p.crrTask.bonus);
                    p.sendAddchatYellow("Bạn vừa được thưởng " + Util.powerToString(p.crrTask.bonus) + " sức mạnh");
                    Service.chatNPC(p, (short) p.menuNPCID, TaskManager.gI().textChatTASK[p.taskId]);
                    TaskService.gI().setupNextNewTask(p, (byte) (p.taskId + (byte) 1));
                    return;
                }
            }
            doMenuArray(p, idnpc, "Cảm ơn bạn đã đến đây để giúp chúng tôi", new String[]{"Kể Chuyện", "Cửa hàng"});
            return;
        } else if (p.menuNPCID == 16 && (p.map.id == 24 || p.map.id == 25 || p.map.id == 26)) {
            TabItemShop[] test = Shop.getTabShop(16, p.gender).toArray(new TabItemShop[0]);
            GameScr.UIshop(p, test);
//                doMenuArray(p,idnpc,Text.get(0, 1),new String[]{"Cửa Hàng"});
            return;
        } else if (p.menuNPCID == 25 && p.map.id == 27) {
            if (p.clan != null) {
                if (p.clan.openDoanhTrai) {
                    doMenuArray(p, idnpc, "Doanh trại Độc Nhãn đang được mở,\nngươi có chắc chắn muốn vào trại Độc Nhãn (còn " + (30 - (int) ((System.currentTimeMillis() - p.clan.topen) / 60000)) + " phút)", new String[]{"OK", "Từ Chối"});
                } else {
                    doMenuArray(p, idnpc, "Ngươi có chắc chắn muốn vào trại Độc Nhãn", new String[]{"OK", "Từ Chối"});
                }
            } else {
                doMenuArray(p, idnpc, "Vui lòng gia nhập bang hội", new String[]{"OK", "Từ Chối"});
            }
            return;
        } else if (p.menuNPCID == 1 || p.menuNPCID == 0 || p.menuNPCID == 2) {
            if ((p.menuNPCID == 0 && p.map.id == 21) || (p.menuNPCID == 1 && p.map.id == 23) || (p.menuNPCID == 2 && p.map.id == 22)) {
                if (p.crrTask.subtasks[(byte) (p.crrTask.countSub - (byte) 1)] == (byte) p.menuNPCID && p.crrTask.index == (byte) (p.crrTask.countSub - (byte) 1)) {
                    if (p.power < (p.getPowerLimit() * 1000000000L)) {
                        p.tiemNang += p.crrTask.bonus;
                        p.power += p.crrTask.bonus;
                    }
                    p.UpdateSMTN((byte) 2, p.crrTask.bonus);
                    long _vang = ((long) p.vang + 700000000) > 2000000000L ? 2000000000L : ((long) p.vang + 700000000);
                    p.vang = _vang;
                    p.ngoc += 17000;
                    p.ngocKhoa += 34000;
                    Service.gI().buyDone(p);
                    p.sendAddchatYellow("Bạn vừa được thưởng " + Util.powerToString(p.crrTask.bonus) + " sức mạnh và ngọc, ngọc khoá, vàng");
                    Service.chatNPC(p, (short) p.menuNPCID, TaskManager.gI().textChatTASK[p.taskId]);
                    if (p.taskId == (short) 4) {
                        TaskService.gI().setupNextNewTask(p, (byte) 7);
                    } else {
                        TaskService.gI().setupNextNewTask(p, (byte) (p.taskId + (byte) 1));
                    }
                    return;
                } else if (p.taskId == (short) 8 && p.crrTask.index == (byte) 2) {
                    TaskService.gI().updateCountTask(p);
                    Service.chatNPC(p, (short) p.menuNPCID, "Con đã nghe về rồng thần chưa, thứ có thể thực hiện được điều ước. Gần đây có tháp Karin, con hãy đến đó xem xét tình hình");
                    return;
                } else if (p.taskId == (short) 24 && p.crrTask.index == (byte) 0) {
                    TaskService.gI().updateCountTask(p);
                    Service.chatNPC(p, (short) p.menuNPCID, "OK, ta biết rồi. Bây giờ con hãy đi tìm vị khách lạ đó");
                    return;
                }
                if (p.hasTrungMabu) {
                    doMenuArray(p, idnpc, Text.get(0, 0), new String[]{"Kể chuyện", "Đệ Tử\nMabư"});
                } else {
                    doMenuArray(p, idnpc, Text.get(0, 0), new String[]{"Kể chuyện"});
                }
            }
            return;
        } else if (p.menuNPCID == 39 && p.map.id == 5) {
            doMenuArray(p, idnpc, Text.get(0, 1), new String[]{"Cửa Hàng", "Chức Năng", "Đổi Tiền", "Nhận Quà\nGiáng Sinh", "Giftcode"});
            return;
        } else if (p.menuNPCID == 9 && p.map.id == 14) {
            if (p.gender == 2) {
                if (p.crrTask.subtasks[p.crrTask.index] == (byte) p.menuNPCID && p.crrTask.index == (byte) 2 && p.taskId == (short) 7) {
                    Service.chatNPC(p, (short) p.menuNPCID, "Cảm ơn ngươi đã cứu ta. Ta sẽ sẵn sàng phục vụ nếu ngươi cần mua vật dụng");
                    TaskService.gI().updateCountTask(p);
                    return;
                }
                doMenuArray(p, idnpc, Text.get(0, 1), new String[]{"Cửa Hàng"});
            } else {
                Service.gI().sendTB(p.session, 0, "Ta chỉ bán đồ cho hành tinh Xayda", 0);
            }
            return;
        } else if (p.menuNPCID == 7 && p.map.id == 0) {
            if (p.gender == 0) {
                if (p.crrTask.subtasks[p.crrTask.index] == (byte) p.menuNPCID && p.crrTask.index == (byte) 2 && p.taskId == (short) 7) {
                    Service.chatNPC(p, (short) p.menuNPCID, "Cảm ơn ngươi đã cứu ta. Ta sẽ sẵn sàng phục vụ nếu ngươi cần mua vật dụng");
                    TaskService.gI().updateCountTask(p);
                    return;
                }
                doMenuArray(p, idnpc, Text.get(0, 1), new String[]{"Cửa Hàng"});
            } else {
                Service.gI().sendTB(p.session, 0, "Ta chỉ bán đồ cho hành tinh Trái đất", 0);
            }
            return;
        } else if (p.menuNPCID == 8 && p.map.id == 7) {
            if (p.imgNRSD == (byte) 53) {
                if (p.map.id == 7) {
                    doMenuArray(p, idnpc, "Ồ, ngọc rồng namếc, bạn thật là may mắn\nnếu tìm đủ 7 viên sẽ được Rồng Thiêng Namếc ban cho điều ước", new String[]{"Hướng\ndẫn\nGọi Rồng", "Gọi rồng", "Từ chối"});
                }
            } else {
                if (p.gender == 1) {
                    if (p.crrTask.subtasks[p.crrTask.index] == (byte) p.menuNPCID && p.crrTask.index == (byte) 2 && p.taskId == (short) 7) {
                        Service.chatNPC(p, (short) p.menuNPCID, "Cảm ơn ngươi đã cứu ta. Ta sẽ sẵn sàng phục vụ nếu ngươi cần mua vật dụng");
                        TaskService.gI().updateCountTask(p);
                        return;
                    }
                    doMenuArray(p, idnpc, Text.get(0, 1), new String[]{"Cửa Hàng"});

                } else {
                    Service.gI().sendTB(p.session, 0, "Ta chỉ bán đồ cho hành tinh Namếc", 0);
                }
            }
            return;
        } else if (p.menuNPCID == 13 && p.map.id == 5) { //QUY LAO
            if (p.taskId == (short) 12) {
//                if(p.crrTask.index == (byte)(p.crrTask.countSub - (byte)1) && p.crrTask.subtasks[(byte)(p.crrTask.countSub - (byte)1)] == (byte)p.menuNPCID && p.clan != null
//                && p.clan.members.size() >= 5 && p.gender == (byte)0) {
                if (p.crrTask.index == (byte) (p.crrTask.countSub - (byte) 1) && p.crrTask.subtasks[(byte) (p.crrTask.countSub - (byte) 1)] == (byte) p.menuNPCID && p.gender == (byte) 0) {
                    if (p.power < (p.getPowerLimit() * 1000000000L)) {
                        p.tiemNang += p.crrTask.bonus;
                        p.power += p.crrTask.bonus;
                    }
                    p.UpdateSMTN((byte) 2, p.crrTask.bonus);
                    p.sendAddchatYellow("Bạn vừa được thưởng " + Util.powerToString(p.crrTask.bonus) + " sức mạnh");
                    Service.chatNPC(p, (short) p.menuNPCID, TaskManager.gI().textChatTASK[p.taskId]);
                    TaskService.gI().setupNextNewTask(p, (byte) (p.taskId + (byte) 1));
                    return;
                }
            } else if (p.taskId == (short) 24) {
                if (p.crrTask.index == (byte) 2 && p.crrTask.subtasks[p.crrTask.index] == (byte) p.menuNPCID) {
                    TaskService.gI().updateCountTask(p);
                    Service.chatNPC(p, (short) p.menuNPCID, "Ta không biết chuyện gì sắp xảy ra nhưng cảm ơn con");
                    return;
                }
            } else {
                if (((p.crrTask.subtasks[(byte) (p.crrTask.countSub - (byte) 1)] == (byte) p.menuNPCID && p.crrTask.index == (byte) (p.crrTask.countSub - (byte) 1))
                        || (p.taskId == (short) 9 && p.crrTask.index == (byte) 0)) && p.gender == (byte) 0) {
                    if (p.power < (p.getPowerLimit() * 1000000000L)) {
                        p.tiemNang += p.crrTask.bonus;
                        p.power += p.crrTask.bonus;
                    }
                    p.UpdateSMTN((byte) 2, p.crrTask.bonus);
                    p.sendAddchatYellow("Bạn vừa được thưởng " + Util.powerToString(p.crrTask.bonus) + " sức mạnh");
                    Service.chatNPC(p, (short) p.menuNPCID, TaskManager.gI().textChatTASK[p.taskId]);
                    if (p.taskId == (short) 9) {
                        TaskService.gI().setupNextNewTask(p, (byte) 12);
                    } else if (p.taskId == (short) 17) {
                        TaskService.gI().setupNextNewTask(p, (byte) 20);
                    } else {
                        TaskService.gI().setupNextNewTask(p, (byte) (p.taskId + (byte) 1));
                    }
                    return;
                }
            }
//            doMenuArray(p,idnpc,"Chào con, ta rất vui khi gặp con\nCon muốn làm gì nào?",new String[]{"Nói chuyện","Tính năng","Đá nâng cấp","Bang hội"});
            doMenuArray(p, idnpc, "Chào con, ta rất vui khi gặp con\nCon muốn làm gì nào?", new String[]{"Sự Kiện\nChristmas", "Nhiệm Vụ", "Xếp Hạng", "Bang hội", "Kho Báu\nDưới Biển"});
            return;
        } else if (p.menuNPCID == 14 && p.map.id == 13) { //TRUONG LAO GURU
            if (p.taskId == (short) 12) {
//                if(p.crrTask.index == (byte)(p.crrTask.countSub - (byte)1) && p.crrTask.subtasks[(byte)(p.crrTask.countSub - (byte)1)] == (byte)p.menuNPCID && p.clan != null
//                && p.clan.members.size() >= 5 && p.gender == (byte)1) {
                if (p.crrTask.index == (byte) (p.crrTask.countSub - (byte) 1) && p.crrTask.subtasks[(byte) (p.crrTask.countSub - (byte) 1)] == (byte) p.menuNPCID && p.gender == (byte) 1) {
                    if (p.power < (p.getPowerLimit() * 1000000000L)) {
                        p.tiemNang += p.crrTask.bonus;
                        p.power += p.crrTask.bonus;
                    }
                    p.UpdateSMTN((byte) 2, p.crrTask.bonus);
                    p.sendAddchatYellow("Bạn vừa được thưởng " + Util.powerToString(p.crrTask.bonus) + " sức mạnh");
                    Service.chatNPC(p, (short) p.menuNPCID, TaskManager.gI().textChatTASK[p.taskId]);
                    TaskService.gI().setupNextNewTask(p, (byte) (p.taskId + (byte) 1));
                    return;
                }
            } else {
                if (((p.crrTask.subtasks[(byte) (p.crrTask.countSub - (byte) 1)] == (byte) p.menuNPCID && p.crrTask.index == (byte) (p.crrTask.countSub - (byte) 1))
                        || (p.taskId == (short) 9 && p.crrTask.index == (byte) 0)) && p.gender == (byte) 1) {
                    if (p.power < (p.getPowerLimit() * 1000000000L)) {
                        p.tiemNang += p.crrTask.bonus;
                        p.power += p.crrTask.bonus;
                    }
                    p.UpdateSMTN((byte) 2, p.crrTask.bonus);
                    p.sendAddchatYellow("Bạn vừa được thưởng " + Util.powerToString(p.crrTask.bonus) + " sức mạnh");
                    Service.chatNPC(p, (short) p.menuNPCID, TaskManager.gI().textChatTASK[p.taskId]);
                    if (p.taskId == (short) 9) {
                        TaskService.gI().setupNextNewTask(p, (byte) 12);
                    } else if (p.taskId == (short) 17) {
                        TaskService.gI().setupNextNewTask(p, (byte) 20);
                    } else {
                        TaskService.gI().setupNextNewTask(p, (byte) (p.taskId + (byte) 1));
                    }
                    return;
                }
            }
            Service.gI().sendTB(p.session, 0, "Chức Năng Đang Được Cập Nhật " + idnpc, 0);
            return;
        } else if (p.menuNPCID == 15 && p.map.id == 20) { //VUA VEGETA
            if (p.taskId == (short) 12) {
//                if(p.crrTask.index == (byte)(p.crrTask.countSub - (byte)1) && p.crrTask.subtasks[(byte)(p.crrTask.countSub - (byte)1)] == (byte)p.menuNPCID && p.clan != null
//                && p.clan.members.size() >= 5 && p.gender == (byte)2) {
                if (p.crrTask.index == (byte) (p.crrTask.countSub - (byte) 1) && p.crrTask.subtasks[(byte) (p.crrTask.countSub - (byte) 1)] == (byte) p.menuNPCID && p.gender == (byte) 2) {
                    if (p.power < (p.getPowerLimit() * 1000000000L)) {
                        p.tiemNang += p.crrTask.bonus;
                        p.power += p.crrTask.bonus;
                    }
                    p.UpdateSMTN((byte) 2, p.crrTask.bonus);
                    p.sendAddchatYellow("Bạn vừa được thưởng " + Util.powerToString(p.crrTask.bonus) + " sức mạnh");
                    Service.chatNPC(p, (short) p.menuNPCID, TaskManager.gI().textChatTASK[p.taskId]);
                    TaskService.gI().setupNextNewTask(p, (byte) (p.taskId + (byte) 1));
                    return;
                }
            } else {
                if (((p.crrTask.subtasks[(byte) (p.crrTask.countSub - (byte) 1)] == (byte) p.menuNPCID && p.crrTask.index == (byte) (p.crrTask.countSub - (byte) 1))
                        || (p.taskId == (short) 9 && p.crrTask.index == (byte) 0)) && p.gender == (byte) 2) {
                    if (p.power < (p.getPowerLimit() * 1000000000L)) {
                        p.tiemNang += p.crrTask.bonus;
                        p.power += p.crrTask.bonus;
                    }
                    p.UpdateSMTN((byte) 2, p.crrTask.bonus);
                    p.sendAddchatYellow("Bạn vừa được thưởng " + Util.powerToString(p.crrTask.bonus) + " sức mạnh");
                    Service.chatNPC(p, (short) p.menuNPCID, TaskManager.gI().textChatTASK[p.taskId]);
                    if (p.taskId == (short) 9) {
                        TaskService.gI().setupNextNewTask(p, (byte) 12);
                    } else if (p.taskId == (short) 17) {
                        TaskService.gI().setupNextNewTask(p, (byte) 20);
                    } else {
                        TaskService.gI().setupNextNewTask(p, (byte) (p.taskId + (byte) 1));
                    }
                    return;
                }
            }
            Service.gI().sendTB(p.session, 0, "Chức Năng Đang Được Cập Nhật " + idnpc, 0);
            return;
        } else if (p.menuNPCID == 18 && p.map.id == 46) { //THAN MEO
            if (p.taskId == (short) 29 && p.crrTask.index == (byte) 0 && p.crrTask.subtasks[p.crrTask.index] == (byte) p.menuNPCID) {
                if (p.damGoc >= 10000) {
                    TaskService.gI().updateCountTask(p);
                    Service.chatNPC(p, (short) p.menuNPCID, "Con hãy tiếp tục đến tương lai và thu thập Capsule kì bí");
                    return;
                } else {
                    Service.gI().buyDone(p);
                    Service.chatNPC(p, (short) p.menuNPCID, "Con hãy nâng sức đánh gốc lên 10K rồi quay lại gặp ta");
                    return;
                }
            }
            return;
        } else if (p.menuNPCID == 4 && (p.map.id == 21 || p.map.id == 22 || p.map.id == 23)) {
            if (p.upMagicTree) {
                doMenuArray(p, idnpc, "Cây đậu thần", new String[]{"Nâng cấp\nnhanh " + ngocUpMagicTree(p.levelTree) + "\nngọc", "Hủy nâng\ncấp hồi " + Util.powerToString((long) (goldUpMagicTree(p.levelTree) / 2)) + "\nvàng"});
            } else {
                if (p.levelTree >= (byte) 10) {
                    if (p.currentBean < p.maxBean) {
                        doMenuArray(p, idnpc, "Cây đậu thần", new String[]{"Thu hoạch", "Thu đậu nhanh\n" + ngocThuDauThan(p.levelTree) + " ngọc"});
                    } else {
                        doMenuArray(p, idnpc, "Cây đậu thần", new String[]{"Thu hoạch"});
                    }
                } else {
                    if (p.currentBean < p.maxBean) {
                        doMenuArray(p, idnpc, "Cây đậu thần", new String[]{"Thu hoạch", "Nâng cấp\n" + timeStringUpMagicTree(p.levelTree) + " " + Util.powerToString((long) goldUpMagicTree(p.levelTree)) + "\nvàng", "Thu đậu nhanh\n" + ngocThuDauThan(p.levelTree) + " ngọc"});
                    } else {
                        doMenuArray(p, idnpc, "Cây đậu thần", new String[]{"Thu hoạch", "Nâng cấp\n" + timeStringUpMagicTree(p.levelTree) + " " + Util.powerToString((long) goldUpMagicTree(p.levelTree)) + "\nvàng"});
                    }
                }
            }
            return;
        } else if (p.menuNPCID == 3) {
            p.openBox();
            return;
        } else {
            Service.gI().sendTB(p.session, 0, "Chức Năng Đang Được Cập Nhật " + idnpc, 0);
        }
        m.writer().flush();
        p.session.sendMessage(m);
        m.cleanup();
    }

    public int NpcAvatar(Player p, int npcID) {

        for (int i = 0; i < p.getPlace().map.template.npcs.length; i++) {
            if (p.getPlace().map.template.npcs[i].tempId == npcID) {
                return p.getPlace().map.template.npcs[i].avartar;
            }

        }
        return -1;
    }

    public int vangMoNoiTai(byte count) {
        if (count == 0) {
            return 0;
        } else if (count == 1) {
            return 10;
        } else if (count == 2) {
            return 20;
        } else if (count == 3) {
            return 40;
        } else if (count == 4) {
            return 80;
        } else if (count == 5) {
            return 160;
        } else if (count == 6) {
            return 320;
        } else if (count == 7) {
            return 640;
        } else {
            return 1280;
        }
//        return 1280;
    }

    //vang nang cap dau than
    public int goldUpMagicTree(byte level) {
        if (level == (byte) 1) {
            return 5000;
        } else if (level == (byte) 2) {
            return 10000;
        } else if (level == (byte) 3) {
            return 100000;
        } else if (level == (byte) 4) {
            return 1000000;
        } else if (level == (byte) 5) {
            return 10000000;
        } else if (level == (byte) 6) {
            return 20000000;
        } else if (level == (byte) 7) {
            return 50000000;
        } else if (level == (byte) 8) {
            return 100000000;
        } else if (level == (byte) 9) {
            return 300000000;
        }
        return 300000000;
    }

    //ngoc nang cap NHANH CAY DAU THAN
    public int ngocUpMagicTree(byte level) {
        if (level == (byte) 1) {
            return 10;
        } else if (level == (byte) 2) {
            return 100;
        } else if (level == (byte) 3) {
            return 1000;
        } else if (level == (byte) 4) {
            return 3000;
        } else if (level == (byte) 5) {
            return 5000;
        } else if (level == (byte) 6) {
            return 7000;
        } else if (level == (byte) 7) {
            return 8000;
        } else if (level == (byte) 8) {
            return 9000;
        } else if (level == (byte) 9) {
            return 10000;
        }
        return 10000;
    }

    //NGOC THU HOACH NHANH DAU THAN
    public int ngocThuDauThan(byte level) {
        if (level == (byte) 1) {
            return 2;
        } else if (level == (byte) 2) {
            return 5;
        } else if (level == (byte) 3) {
            return 8;
        } else if (level == (byte) 4) {
            return 11;
        } else if (level == (byte) 5) {
            return 14;
        } else if (level == (byte) 6) {
            return 17;
        } else if (level == (byte) 7) {
            return 20;
        } else if (level == (byte) 8) {
            return 23;
        } else if (level == (byte) 9) {
            return 26;
        }
        return 26;
    }

    //TIME NANG CAP DAU THAN
    public int timeUpMagicTree(byte level) {
        if (level == (byte) 1) {
            return 600;
        } else if (level == (byte) 2) {
            return 6000;
        } else if (level == (byte) 3) {
            return 58920;
        } else if (level == (byte) 4) {
            return 597600;
        } else if (level == (byte) 5) {
            return 1202400;
        } else if (level == (byte) 6) {
            return 2592000;
        } else if (level == (byte) 7) {
            return 4752000;
        } else if (level == (byte) 8) {
            return 5961600;
        } else if (level == (byte) 9) {
            return 8640000;
        }
        return 8640000;
    }

    //TIME STRING UP DAU THAN
    public String timeStringUpMagicTree(byte level) {
        if (level == (byte) 1) {
            return "10m";
        } else if (level == (byte) 2) {
            return "1h40m";
        } else if (level == (byte) 3) {
            return "16h22m";
        } else if (level == (byte) 4) {
            return "6d22h";
        } else if (level == (byte) 5) {
            return "13d22h";
        } else if (level == (byte) 6) {
            return "30d";
        } else if (level == (byte) 7) {
            return "55d";
        } else if (level == (byte) 8) {
            return "69d";
        } else if (level == (byte) 9) {
            return "100d";
        }
        return "100d";
    }
}
