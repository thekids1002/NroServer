package nro.main;

import io.LogHistory;
import nro.constant.Constant;
import nro.giftcode.GiftCode;
import nro.giftcode.GiftCodeManager;
import nro.io.Message;
import nro.io.Session;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import nro.clan.Clan;
import nro.clan.ClanManager;
import nro.clan.ClanService;
import nro.clan.ClanMessage;
import nro.item.*;
import nro.map.Map;
import nro.map.Mob;
import nro.map.Npc;
import nro.map.WayPoint;
import nro.map.ItemMap;
import nro.player.Player;
import nro.player.Detu;
import nro.player.PlayerDAO;
import nro.player.PlayerManger;
import nro.player.Boss;
import nro.player.UseSkill;
import nro.shop.Shop;
import nro.shop.TabItemShop;
import nro.task.TaskService;
import nro.daihoi.DaiHoiService;
import nro.card.RadaCardService;
import nro.player.FriendService;

public class Controller {

    private static Controller instance;
    Server server = Server.gI();
    public LogHistory LogHistory = new LogHistory(getClass());

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public void onMessage(Session _session, Message m) {
        try {
            Player player = PlayerManger.gI().getPlayerByUserID(_session.userId);
            byte cmd = m.command;
//            if(cmd != -7 && cmd != 29 && cmd != -67 && cmd != -62) {
            //    Util.log("Tinh Nang CMD: " + cmd);
//            }
            switch (cmd) {
                case -127:
                    byte typequay = m.reader().readByte();
                    if (typequay == (byte) 1) { //TIEP TUC QUAY
                        LuckyService.gI().loadUILucky(player);
                    }
                    if (typequay == (byte) 2) {
                        byte soluong = m.reader().readByte();
                        if (player.ngoc >= (int) soluong * 4) {
                            if ((player.ItemQuay.size() + (int) soluong) > 100) {
                                player.sendAddchatYellow("Rương phụ đã đầy!");
                                LuckyService.gI().loadUILucky(player);
                            } else {
                                player.ngoc -= (int) soluong * 4;
                                Service.gI().buyDone(player);
                                LuckyService.gI().resultLucky(player, typequay, soluong);
                            }
                        } else {
                            player.sendAddchatYellow("Không đủ ngọc để quay!");
                        }
                    }
//                    server.menu.LuckyRound(player,typequay,soluong);
                    break;
                case -125: //CLIENT INPUT
                    byte sizeInput = m.reader().readByte();
                    if (player.Role != 0 && player.name.equals("admin") && player.menuNPCID == 999 && player.menuID == 1) {
                        if (sizeInput < 3 || sizeInput > 3) {
                            player.sendAddchatYellow("Chưa nhập đủ các trường");
                            break;
                        }
                        String namePlayerBuff = m.reader().readUTF();
                        String strIdBuff = m.reader().readUTF();
                        String strQuantityBuff = m.reader().readUTF();
                        if (!Util.isNumericInt(strIdBuff) || !Util.isNumericInt(strQuantityBuff)) {
                            player.sendAddchatYellow("ID Item và số lượng phải là số");
                            break;
                        }
                        int idItemBuff = Integer.parseInt(strIdBuff);
                        int quantityItemBuff = Integer.parseInt(strQuantityBuff);
                        Player pBuffItem = PlayerManger.gI().getPlayerByName(namePlayerBuff);
                        if (pBuffItem != null && pBuffItem.session != null) {
                            String txtBuff = "Buff to player: " + pBuffItem.name + "\b";
                            if (idItemBuff == -1) {
                                pBuffItem.vang = Math.min(pBuffItem.vang + (long) quantityItemBuff, Constant.MAX_MONEY);
                                txtBuff += quantityItemBuff + " vàng\b";
                            } else if (idItemBuff == -2) {
                                pBuffItem.ngoc = Math.min(pBuffItem.ngoc + quantityItemBuff, Constant.MAX_GEM);
                                txtBuff += quantityItemBuff + " ngọc\b";
                            } else if (idItemBuff == -3) {
                                pBuffItem.ngocKhoa = Math.min(pBuffItem.ngocKhoa + quantityItemBuff, Constant.MAX_RUBY);
                                txtBuff += quantityItemBuff + " ngọc khóa\b";
                            } else {
                                Item itemBuffTemplate = ItemSell.getItemNotSell(idItemBuff);
                                if (itemBuffTemplate != null) {
                                    Item itemBuff = new Item(itemBuffTemplate);
                                    itemBuff.quantity = quantityItemBuff;
                                    pBuffItem.addItemToBag(itemBuff);
                                    txtBuff += "x" + quantityItemBuff + " " + itemBuff.template.name + "\b";
                                }
                            }
                            Service.gI().updateItemBag(pBuffItem);
                            Service.chatNPC(player, (short) 24, txtBuff);
                            if (player.id != pBuffItem.id) {
                                Service.chatNPC(pBuffItem, (short) 24, txtBuff);
                            }
                        } else {
                            player.sendAddchatYellow("Player không online");
                        }
                        break;
                    }
                    String textLevel = m.reader().readUTF();
                    if (player.menuID == 3 && player.menuNPCID == 67) {//MR POPO
                        if (Util.isNumericInt(textLevel)) {
//                            player.sendAddchatYellow("Khí gas hiện tại chưa mở");
                            if (Server.gI().cKhiGas < Server.gI().maxKhiGas) {
                                if (player.clan != null && player.clan.cOpenGas > (byte) 0) {
                                    int level = Integer.parseInt(textLevel);

                                    if (!player.clan.openKhiGas) {
                                        Service.gI().initKhiGa(player.clan, level);

                                        Service.gI().leaveOutMap(player);
                                        player.x = (short) 63;
                                        player.y = (short) 336;
                                        player.clan.khiGas[0].area[0].Enter(player);
                                    }
                                } else {
                                    player.sendAddchatYellow("Số lượt tham gia Khí Gas hôm nay đã hết");
                                }
                            } else {
                                player.sendAddchatYellow("Hiện tại Khí Gas đang quá tải, vui lòng chờ 30p nữa");
                            }
                        } else {
                            player.sendAddchatYellow("Lựa chọn cấp độ từ 1-110");
                        }
                    } else if (player.menuNPCID == 73) {
                        PlayerDAO.changePassword(player.id, textLevel);
                        player.sendAddchatYellow("Đổi mật khẩu thành công");
                    } else if (player.menuNPCID >= 0 && player.menuNPCID <= 2) {
                        GiftCode giftcode = GiftCodeManager.gI().checkUseGiftCode(player.id, textLevel);
                        if (giftcode != null) {
                            ItemService.gI().addItemGiftCodeToPlayer(player, giftcode);
                        } else {
                            player.sendAddchatYellow("Bạn đã nhận GiftCode này rồi hoặc GiftCode không tồn tại");
                        }
                    }
                    break;
                case -113:
                    UseSkill.useSkill(player, m);
                    break;
                case -84:
                    Service.gI().CHAGE_MOD_BODY(player);
                    break;
                //status de tu
                case -108:
                    if (player.detu != null && (System.currentTimeMillis() - player.tActionDe) >= 5000) {
                        player.tActionDe = System.currentTimeMillis();
                        byte statuspet = m.reader().readByte();
                        if (player.NhapThe == 0 && player.statusPet != statuspet && !player.detu.isdie) {
                            player.statusPet = statuspet;
                            if (statuspet == 0) { // di theo
                                player.petfucus = 1;
                                Service.gI().LoadDeTuFollow(player, 1);
                                player.zone.chat(player.detu, "OK, con sẽ đi theo sư phụ!");
                            } else if (statuspet == 1) { // bao ve
                                if (player.petfucus == 0) {
                                    player.petfucus = 1;
                                    Service.gI().LoadDeTuFollow(player, 1);
                                }
                                player.zone.PetAttack(player, player.detu, statuspet);
                                player.zone.chat(player.detu, "OK, con sẽ bảo vệ sư phụ!");
                            } else if (statuspet == 2) { // tan cong
                                if (player.petfucus == 0) {
                                    player.petfucus = 1;
                                    Service.gI().LoadDeTuFollow(player, 1);
                                }
                                player.zone.PetAttack(player, player.detu, statuspet);
                                player.zone.chat(player.detu, "OK, sư phụ cứ để con lo!");
                            } else if (statuspet == 3) { //de tu ve nha
                                player.zone.chat(player.detu, "Bai bai sư phụ!");
                                player.petfucus = 0;
                                player.zone.leaveDetu(player, player.detu); // send remove detu to all
                            } else if (statuspet == 4) {
                                player.sendAddchatYellow("Chỉ có thể thực hiện bằng bông tai!");
                            } else if (statuspet == 5) {
                                player.sendAddchatYellow("Chức năng đang được xây dựng!");
                            }
                        } else {
                            player.sendAddchatYellow("Không thể thực hiện!");
                        }
                    } else {
                        player.sendAddchatYellow("Thực hiện quá nhanh!");
                    }
                    break;
                case -107:
                    if (!player.lockPK) {
                        Service.gI().LoadDeTu(player, (byte) 2);
                    } else {
                        player.sendAddchatYellow("Không thể thực hiện");
                    }
//                    Service.gI().sendMessage(_session, -107, "detu");
                    break;

                //flag
                case -105:
                    if (player.waitTransport) {
                        player.waitTransport = false;
                        Map maptele = MainManager.getMapid(102);
                        Controller.getInstance().teleportToMAP(player, maptele);
                        player.timeTauNgam.cancel();
                        player.timeTauNgam = null;
                    }
                    break;
                case -103:
                    if (!player.lockPK) {
                        byte actionflag = m.reader().readByte();
                        if (actionflag == 0) {
                            Service.gI().drawFlagPK(player); // mo giao dien doi co
                        } else if (actionflag == 1) {
                            byte flagtype = m.reader().readByte();
                            if ((player.map.id >= 85 && player.map.id <= 91 && (flagtype >= (byte) 10 || flagtype == (byte) 0)) || player.imgNRSD == (byte) 37 || (player.map.id >= 114 && player.map.id <= 120)) {
                                player.sendAddchatYellow("Không thể thực hiện!");
                            } else {
                                player.cPk = flagtype;
                                player.detu.cPk = flagtype;
                                Service.gI().changeFlagPK(player, flagtype);
                                if (player.petfucus == 1) {
                                    Service.gI().changeFlagPKPet(player, flagtype);
                                }
                            }
                        }
                    } else {
                        player.sendAddchatYellow("Không thể thực hiện");
                    }
                    break;
                case -101:
                    Service.gI().serverMessage(_session, "Đăng Nhập Đê!");
//                    login2(_session);
                    break;
                //su dung capsule transport
                case -91:
                    byte idTransport = m.reader().readByte();
                    if (player.moveToSaoDen) {
                        player.zone.goMapTransport(player, player.mapSaoDen[idTransport]);
                    } else {
                        player.zone.goMapTransport(player, player.mapTransport[idTransport]);
                    }

                    break;
                case -87:
//                    Manager.sendData(player);
                    MainManager.sendDatav2(_session);
                    break;
                case -59:
                    player.sendAddchatYellow("Tạm khóa chức năng thách đấu");
                    break;
                case -86: //giao dich
                    byte gdaction = m.reader().readByte(); //action giao dich == 1 || == 0 thi gd voi player
                    int gdIdChar = -1;
                    Player gdChar = null;
                    if (gdaction == 0) { //moi giao dich
                        gdIdChar = m.reader().readInt(); //ID Char duoc moi giao dich
                        gdChar = PlayerManger.gI().getPlayerByUserID(gdIdChar);
                        if (gdChar != null && gdChar.session != null) {
                            try { //SEND Loi moi giao dich
                                m = new Message(-86);
                                m.writer().writeByte(0);
                                m.writer().writeInt(player.id);
                                m.writer().flush();
                                gdChar.session.sendMessage(m);
                                m.cleanup();
                            } catch (Exception var2) {
                                var2.printStackTrace();
                            } finally {
                                if (m != null) {
                                    m.cleanup();
                                }
                            }
                        }
                    } else if (gdaction == 1) { //chap nhan giao dich
                        gdIdChar = m.reader().readInt(); //ID Char giao dich
                        gdChar = PlayerManger.gI().getPlayerByUserID(gdIdChar);
                        player._friendGiaoDich = gdChar;
                        gdChar._friendGiaoDich = player;
                        //SET STATUS GIAO DICH
                        player._isGiaoDich = true;
                        gdChar._isGiaoDich = true;
                        try { //SEND OPEN UI GIAO DICH
                            m = new Message(-86);
                            m.writer().writeByte(1);
                            m.writer().writeInt(gdChar.id);
                            m.writer().flush();
                            player.session.sendMessage(m);
                            m.cleanup();

                            m = new Message(-86);
                            m.writer().writeByte(1);
                            m.writer().writeInt(player.id);
                            m.writer().flush();
                            gdChar.session.sendMessage(m);
                            m.cleanup();
                        } catch (Exception var2) {
                            var2.printStackTrace();
                        } finally {
                            if (m != null) {
                                m.cleanup();
                            }
                        }
                    } else if (gdaction == 2) { //add do vao giao dich
                        byte _indexGD = m.reader().readByte();
                        int _amountGD = m.reader().readInt();
                        if (_indexGD == -1) {
                            if (player.vang < _amountGD) {
                                _amountGD = (int) player.vang;
                            }
                            player.vang = (player.vang - _amountGD) >= 0 ? (player.vang - _amountGD) : 0;
                            player._goldGiaoDich = _amountGD > 2000000000 ? 2000000000 : _amountGD;
                            Service.gI().buyDone(player);
                        } else {
                            if ((player.ItemBag[_indexGD].template.type == 0 || player.ItemBag[_indexGD].template.type == 1 || player.ItemBag[_indexGD].template.type == 2
                                    || player.ItemBag[_indexGD].template.type == 3 || player.ItemBag[_indexGD].template.type == 4 || player.ItemBag[_indexGD].template.id == 722) && player.ItemBag[_indexGD].chkItemCanGD()) { //ITEM CO BAN
                                //                            ItemSell _itemTemp =  ItemSell.getItemSellByID(player.ItemBag[_indexGD].template.id);
                                Item _itemByChar = player.ItemBag[_indexGD];
                                Item _itemGD = new Item();

                                _itemGD.id = _itemByChar.id;
                                _itemGD.quantity = _amountGD == 0 ? 1 : _amountGD;
                                _itemGD.template = ItemTemplate.ItemTemplateID(_itemGD.id);
                                for (ItemOption _option : _itemByChar.itemOptions) {
                                    _itemGD.itemOptions.add(new ItemOption(_option.id, _option.param));
                                }
                                player._itemGiaoDich.add(_itemGD);
                                player._indexGiaoDich.add(_indexGD);
                                //XOA ITEM TRONG HANH TRANG PLAYER
                                player.ItemBag[_indexGD].quantity -= _itemGD.quantity;
                                if (player.ItemBag[_indexGD].quantity <= 0) {
                                    player.ItemBag[_indexGD] = null;
                                }
                                Service.gI().updateItemBag(player);
//                                player.sortBag();
                            } else if (player.ItemBag[_indexGD].template.type == 14 || (player.ItemBag[_indexGD].template.type == 29 && player.ItemBag[_indexGD].template.id < 579) || player.ItemBag[_indexGD].template.type == 30
                                    || player.ItemBag[_indexGD].template.type == 12 || player.ItemBag[_indexGD].template.id == 380) { //ITEM KHONG BAN
                                Item _itemTempNotSell = ItemSell.getItemNotSell(player.ItemBag[_indexGD].template.id);
                                Item _itemGD = new Item(_itemTempNotSell);
                                _itemGD.quantity = _amountGD == 0 ? 1 : _amountGD;
                                player._itemGiaoDich.add(_itemGD);
                                player._indexGiaoDich.add(_indexGD);
                                //XOA ITEM TRONG HANH TRANG PLAYER
                                player.ItemBag[_indexGD].quantity -= _itemGD.quantity;
                                if (player.ItemBag[_indexGD].quantity <= 0) {
                                    player.ItemBag[_indexGD] = null;
                                }
                                Service.gI().updateItemBag(player);
//                                player.sortBag();
                            } else if (_indexGD == -1) { //VANG GIAO DICH

                            } else { //REMOVE KHOI GIAO DICH
                                try {
                                    m = new Message(-86);
                                    m.writer().writeByte(2);
                                    m.writer().writeByte(_indexGD);
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
                        }

                    } else if (gdaction == 4) { //
//                        Util.log("ACTION ACTION 4: " + m.reader().readByte());
                    } else if (gdaction == 5) { //KHOA GIAO DICH
                        try {
                            m = new Message(-86);
                            m.writer().writeByte(6); //type
                            m.writer().writeInt(player._goldGiaoDich); //VANG GIAO DICH
                            m.writer().writeByte(player._itemGiaoDich.size()); //size item giao dich
                            for (Item _itgd : player._itemGiaoDich) {
                                m.writer().writeShort(_itgd.id);
                                m.writer().writeInt(_itgd.quantity);
                                m.writer().writeByte(_itgd.itemOptions.size()); //So luogn OPTION CUA ITEM
                                for (ItemOption _optgd : _itgd.itemOptions) {
                                    m.writer().writeByte(_optgd.id);
                                    m.writer().writeShort(_optgd.param);
                                }
                            }
                            m.writer().flush();
                            player._friendGiaoDich.session.sendMessage(m);
                            m.cleanup();
                        } catch (Exception var2) {
                            var2.printStackTrace();
                        } finally {
                            if (m != null) {
                                m.cleanup();
                            }
                        }
                    } else if (gdaction == 7) { //XAC NHAN GIAO DICH
                        byte _boxMeNull = player.getAvailableBag();
                        if (_boxMeNull < player._friendGiaoDich._itemGiaoDich.size()) {
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
                            player._isGiaoDich = false;
                            player._friendGiaoDich._isGiaoDich = false;

                            player._friendGiaoDich.sendAddchatYellow("Giao dịch đã bị hủy");
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
                            return;
                        }
                        player._confirmGiaoDich = true;
                        if (!player._friendGiaoDich._confirmGiaoDich) {
                            player.sendAddchatYellow("Đang đợi người chơi còn lại xác nhận");
                        } else if (player._confirmGiaoDich && player._friendGiaoDich._confirmGiaoDich) {
                            byte _boxFriendNull = player._friendGiaoDich.getAvailableBag();
                            //XOA ITEM CA 2 BEN
//                            //XOA ITEM CUA BAN THAN
//                            for(int i = 0; i < player._indexGiaoDich.size(); i++) {
//                                player.ItemBag[player._indexGiaoDich.get(i)].quantity -= player._itemGiaoDich.get(i).quantity;
//                                if(player.ItemBag[player._indexGiaoDich.get(i)].quantity == 0) {
//                                    player.ItemBag[player._indexGiaoDich.get(i)] = null;
//                                }
//                            }
//                            player.sortBag();
//                            for(int i = 0; i < player._friendGiaoDich._indexGiaoDich.size(); i++) {
//                                //XOA ITEM FRIEND
//                                player._friendGiaoDich.ItemBag[player._friendGiaoDich._indexGiaoDich.get(i)].quantity -= player._friendGiaoDich._itemGiaoDich.get(i).quantity;
//                                if(player._friendGiaoDich.ItemBag[player._friendGiaoDich._indexGiaoDich.get(i)].quantity == 0) {
//                                    player._friendGiaoDich.ItemBag[player._friendGiaoDich._indexGiaoDich.get(i)] = null;
//                                }
//                            }
//                            player._friendGiaoDich.sortBag();
                            String logItem1 = "list";
                            String logItem2 = "list";
                            //ADD ITEM TO ME
                            for (int i = 0; i < player._friendGiaoDich._indexGiaoDich.size(); i++) {
                                byte indexME = player.getIndexBagNotItem();
//                                Item _item = new Item(item);
//                                _item.quantity += (item.quantity - 1);
                                if (indexME != (byte) (-1)) {
                                    logItem1 += ";id:" + player._friendGiaoDich._itemGiaoDich.get(i).id + "," + player._friendGiaoDich._itemGiaoDich.get(i).template.name + ",quantity:" + player._friendGiaoDich._itemGiaoDich.get(i).quantity;
                                    player.ItemBag[indexME] = player._friendGiaoDich._itemGiaoDich.get(i);
                                }
                            }
                            //ADD ITEM TO FRIEND
                            for (int i = 0; i < player._indexGiaoDich.size(); i++) {
                                byte indexFRIEND = player._friendGiaoDich.getIndexBagNotItem();
//                                Item _item = new Item(item);
//                                _item.quantity += (item.quantity - 1);
                                if (indexFRIEND != (byte) (-1)) {
                                    logItem2 += ";id:" + player._itemGiaoDich.get(i).id + "," + player._itemGiaoDich.get(i).template.name + ",quantity:" + player._itemGiaoDich.get(i).quantity;
                                    player._friendGiaoDich.ItemBag[indexFRIEND] = player._itemGiaoDich.get(i);
                                }
                            }
                            if (player._goldGiaoDich > 0) {
                                long _VANG = ((long) player._friendGiaoDich.vang + (long) player._goldGiaoDich) > 2000000000L ? 2000000000L : ((long) player._friendGiaoDich.vang + (long) player._goldGiaoDich);
                                player._friendGiaoDich.vang = _VANG;
                            }
                            if (player._friendGiaoDich._goldGiaoDich > 0) {
                                long _VANG2 = ((long) player.vang + (long) player._friendGiaoDich._goldGiaoDich) > 2000000000L ? 2000000000L : ((long) player.vang + (long) player._friendGiaoDich._goldGiaoDich);
                                player.vang = _VANG2;
                            }
                            //UPDATE BAG CHO TOI
                            Service.gI().updateItemBag(player);
                            Service.gI().buyDone(player);
                            //UPDATE BAG CHO FRIEND
                            Service.gI().updateItemBag(player._friendGiaoDich);
                            Service.gI().buyDone(player._friendGiaoDich);

                            //SEND THONG BAO GIAO DICH THANH CONG
                            player.sendAddchatYellow("Giao dịch thành công");
                            player._friendGiaoDich.sendAddchatYellow("Giao dịch thành công");

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

                            //GIAO DICH THANH CONG UPDATE LEN DB
                            Connection conn = DataSource.getConnection();
                            PlayerDAO.updateDBAuto(player, conn);
                            PlayerDAO.updateDBAuto(player._friendGiaoDich, conn);
                            PlayerDAO.logTradeDB(player.id, player.name, player._friendGiaoDich.id, player._friendGiaoDich.name, logItem2, logItem1, conn);
                            conn.close();
                        }
                    } else if (gdaction == 3 && player._friendGiaoDich != null && player._friendGiaoDich._friendGiaoDich != null) { //HUY GIAO DICH
                        //SEND TAT UI GIAO DICH
                        //ADD ITEM TO ME
                        for (int i = 0; i < player._indexGiaoDich.size(); i++) {
                            byte indexME = player.getIndexBagNotItem();
//                                Item _item = new Item(item);
//                                _item.quantity += (item.quantity - 1);
                            if (indexME != (byte) (-1)) {
                                player.ItemBag[indexME] = player._itemGiaoDich.get(i);
                            }
                        }
                        //ADD ITEM TO FRIEND
                        for (int i = 0; i < player._friendGiaoDich._indexGiaoDich.size(); i++) {
                            byte indexFRIEND = player._friendGiaoDich.getIndexBagNotItem();
//                                Item _item = new Item(item);
//                                _item.quantity += (item.quantity - 1);
                            if (indexFRIEND != (byte) (-1)) {
                                player._friendGiaoDich.ItemBag[indexFRIEND] = player._friendGiaoDich._itemGiaoDich.get(i);
                            }
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
                    break;
                case -81: //ba hat mit
                    byte actionn = m.reader().readByte(); //action 1 nang cap
                    byte size = m.reader().readByte();
                    byte indexUI = m.reader().readByte();
                    byte indexUI2 = -1;
                    byte indexUI3 = -1;
                    if (player.menuNPCID == 56 && player.map.id == 154) { //whis dap do thien su
                        if (actionn == 1 && player.menuID == 1) {
                            ItemService.gI().createItemAngel(player, m, size, indexUI);
                        }
                    } else {
                        if (player.map.id == 5) {
                            if (actionn == 1 && player.menuID == 1) { //pha le hoa trang bi
                                //                            Util.log("action, size, indexUI: " + actionn + ',' + size + ',' + indexUI);
                                Service.gI().upgradeStarItem(player, indexUI);
                            } else if (actionn == 1 && player.menuID == 0) {//ep sao trang bi
                                indexUI2 = m.reader().readByte();
                                if (indexUI2 == -1) {
                                    Service.gI().serverMessage(_session, "Không nghịch ngu!");
                                } else {
                                    Service.gI().EpStarItem(player, indexUI, indexUI2);
                                }
                            }
                        } else {
                            if (actionn == 1 && player.menuID == 1) { //nang cap trang bi
                                indexUI2 = m.reader().readByte();
                                if (size == (byte) 3) {
                                    indexUI3 = m.reader().readByte();
                                }
                                if (indexUI2 == -1) {
                                    Service.gI().serverMessage(_session, "Không nghịch ngu!");
                                } else {
                                    Service.gI().upgradeLevelItem(player, indexUI, indexUI2, indexUI3);
                                }
                            } else if (actionn == 1 && player.menuID == 2) {//ep manh da vun
                                indexUI2 = m.reader().readByte();
                                if (indexUI2 == -1) {
                                    Service.gI().serverMessage(_session, "Không nghịch ngu!");
                                } else {
                                    Service.gI().EpStoneBreak(player, indexUI, indexUI2);
                                }
                            } else if (actionn == 1 && player.menuID == 3) { //nhap ngoc rong
                                Service.gI().EpDragonBall(player, indexUI);
                            } else if (actionn == 1 && player.menuID == 4) { //nang cap bt porata
                                indexUI2 = m.reader().readByte();
                                if (indexUI2 == -1) {
                                    Service.gI().serverMessage(_session, "Không nghịch ngu!");
                                } else {
                                    Service.gI().upgradePorata(player, indexUI, indexUI2);
                                }
                            } else if (actionn == 1 && player.menuID == 5) { //mo chi so porata cap 2
                                indexUI2 = m.reader().readByte();
                                indexUI3 = m.reader().readByte();
                                if (indexUI2 == -1 || indexUI3 == -1) {
                                    Service.gI().serverMessage(_session, "Không nghịch ngu!");
                                } else {
                                    Service.gI().openOptionPorata(player, indexUI, indexUI2, indexUI3);
                                }
                            } else if (actionn == 1 && player.menuID == 7) { // NANG CAP ITEM HEART
                                indexUI2 = m.reader().readByte();
                                if (indexUI2 == -1) {
                                    Service.gI().serverMessage(_session, "Không nghịch ngu!");
                                } else {
                                    Service.gI().upgradeItemHeart(player, indexUI, indexUI2);
                                }
                            }
                        }
                    }
//                    Service.gI().upgrade(player, actionn, indexUI);
                    break;
                case -80:
                    byte typeFriend = m.reader().readByte();
                    if (typeFriend == (byte) 0) { //get list friends
                        FriendService.gI().listFriend(player, (byte) 0);
                    } else if (typeFriend == (byte) 1) { //add new friend
                        int idFriend = m.reader().readInt();
                        if (idFriend != player.id) {
                            FriendService.gI().addFriend(player, idFriend, (byte) 1);
                        }
                    } else if (typeFriend == (byte) 2) { //delete friend
                        int idFriend = m.reader().readInt();
                        FriendService.gI().deleteFriend(player, idFriend, (byte) 2);
                    }
//                    Service.gI().sendMessage(_session, -80, "1630679754715_-80_r");
                    break;
                case -74:
                    byte type = m.reader().readByte();
                    if (type == 1) {
                        Service.gI().sizeImageSource(_session);
                    } else if (type == 2) {
                        Service.gI().imageSource(_session);
                    }
                    break;
                case -72: //CHAT RIENG GIUA CAC PLAYER
                    int idChatPri = m.reader().readInt();
                    String textChatPri = m.reader().readUTF();
                    if (textChatPri.length() > 40) {
                        textChatPri = textChatPri.substring(0, 40);
                    }
                    Player pTake = PlayerManger.gI().getPlayerByUserID(idChatPri);
                    if (pTake != null && pTake.session != null) {
                        Service.gI().ChatPrivate(pTake, player, textChatPri);
                    }
                    break;
                case -71:
                    if (Server.gI().isCTG) {
                        if ((System.currentTimeMillis() >= player._TIMECHATTG)) {
                            if (player.id >= 1) {
                                player._TIMECHATTG = System.currentTimeMillis() + 30000L;
                            }
                            boolean isCanChat = false;
                            String chat2 = m.reader().readUTF();
                            if (chat2.length() > 40) {
                                chat2 = chat2.substring(0, 40);
                            }
                            if (player.ngocKhoa >= 5) {
                                player.ngocKhoa = player.ngocKhoa - 5;
                                isCanChat = true;
                            } else if (player.ngoc >= 5) {
                                player.ngoc = player.ngoc - 5;
                                isCanChat = true;
                            } else {
                                isCanChat = true;
                            }
                            if (isCanChat) {
                                Service.gI().ChatGolbaL(player, chat2);
                                Service.gI().buyDone(player);
                            }
                        } else {
                            player.sendAddchatYellow("Vui lòng đợi " + (player._TIMECHATTG - System.currentTimeMillis()) / 1000 + " giây nữa!");
                        }
                    } else {
                        player.sendAddchatYellow("Không thể thực hiện");
                    }
                    break;
                // chat the gioi
                case -70:
                    String chat3 = m.reader().readUTF();
                    server.menu.ChatTG(player, player.PartHead(), chat3, (byte) 1);
                    break;
                case -77:
                case -67:
//                    if(player != null && player.isLogin && _session.zoomLevel != 2) {
////                        Util.log("Tinh 2: " + cmd);
//                        Service.gI().updateItemBody(player);
//                        Service.gI().updateItemBag(player);
//                        Service.gI().updateItemBox(player);
//                        player.isLogin = false;
//                    }
                    GameScr.reciveImage(player, m);
                    break;
                case -66:
                    int effId = m.reader().readShort();
                    if (effId != 470) {
//                        if(_session.zoomLevel == 2) {
//                            if(player.map.id < 153) {
                        Service.gI().effData(_session, effId);
//                            }
//                        }
                    }
                    break;
                case -63:
                    // id image logo clan
                    Service.gI().sendMessage(_session, -63, "1630679755147_-63_r");
                    break;
                // hoi sinh cua namek va player attack player
                case -62:
                    if (player.isLogin && player != null) {
                        Service.gI().updateItemBody(player);
                        Service.gI().updateItemBag(player);
                        Service.gI().updateItemBox(player);
                        Service.gI().loadCaiTrangTemp(player);
                        player.isLogin = false;
                    }
//                    ClanService.gI().clanIconImage(_session);
//                    Util.log("CLAN IMAGE");
                    break;
                case -60:
//                    byte idkynang = m.reader().readByte();
                    int idChar = m.reader().readInt();
                    if (player.idSkillselect == 7) { //hoi sinh
                        player.getPlace().hoiSinhNamek(idChar, player);
                    } else { //attack player
                        if (idChar <= -300000) {
                            break;
                        }
                        if (idChar <= -200000) { //acttack boss
                            player.getPlace().FightBoss(player, idChar);
                        } else if (idChar <= -100000 && idChar > -200000) {
                            player.getPlace().FightDetu(player, idChar);
                        } else {
                            player.getPlace().FightChar(player, idChar);
                        }
                    }
                    break;
                case -57: //MOI VAO BANG
                    byte actionInvite = m.reader().readByte();
                    if (Server.gI().isServer == (byte) 1) {
                        if (actionInvite == (byte) 0) { //GUI LOI MOI VAO BANG
                            int playerIDInvite = m.reader().readInt();
                            //GET PLAYER SEND TO INVITE
                            Player playerInvited = PlayerManger.gI().getPlayerByUserID(playerIDInvite);
                            //SEND INVITE TO PLAYER
                            String strInviteSend = player.name + " mời bạn gia nhập bang hội";
                            ClanService.gI().inviteClanSend(playerInvited.session, strInviteSend, player.clan.id, player.clan.id);

                        } else if (actionInvite == (byte) 1) { //DONG Y LOI MOI VAO BANG
                            int clanIDInvite = m.reader().readInt();
                            int codeInvite = m.reader().readInt();
                            //JOIN TO CLAN
                            if (player.clan == null) {
                                ClanService.gI().addPlayerToClan(player, clanIDInvite);
                            }

                        } else if (actionInvite == (byte) 2) {
                            int clanIDInvite = m.reader().readInt();
                            int codeInvite = m.reader().readInt();
                        }
                    } else {
                        player.sendAddchatYellow("Chỉ thực hiện ở NOAH 1");
                    }
                    break;
                //PHONG PHO BANG, CHU BANG, DUOI KHOI BANG
                case -56:
                    int id56 = m.reader().readInt();
                    int role56 = m.reader().readByte();
                    if (Server.gI().isServer == (byte) 1) {
                        if (role56 == (byte) (-1)) { //LOAI THANH VIEN
                            ClanService.gI().loaiThanhVien(id56, player);
                        } else if (role56 == (byte) 0 || role56 == (byte) 1 || role56 == (byte) 2) { //0 CHU BANG, 1 PHO BANG, 2 CAT CHUC
                            ClanService.gI().changeRolePhoBang(id56, player, (byte) role56);
                        }
                    } else {
                        player.sendAddchatYellow("Chỉ thực hiện ở NOAH 1");
                    }
                    break;
                // LEAVE CLAN
                case -55:
                    if (Server.gI().isServer == (byte) 1) {
                        if (player.clan.leaderID != player.id) {
                            ClanService.gI().leaveThanhVien(player);
                        } else {
                            player.sendAddchatYellow("Bang chủ không thể rời bang");
                        }
                    } else {
                        player.sendAddchatYellow("Chỉ thực hiện ở NOAH 1");
                    }
                    break;
                case -54: //CHO DAU THAN
                    int idMessDau = m.reader().readInt();
                    if (Server.gI().isServer == (byte) 1) {
                        byte indexDau = player.getIndexItemBoxByType((byte) 6); //TIM DAU THAN
                        if (indexDau != -1) {
                            //MESSAGE XIN DAU

                            ClanMessage MessXinDau = player.clan.messages.get((byte) (idMessDau - 1));
                            if (MessXinDau.recieve < 5 && player.ItemBox[indexDau].quantity >= 1) {
                                //GET PLAYER XIN DAU
                                Player playerNhanDau = PlayerManger.gI().getPlayerByUserID(MessXinDau.playerId);
                                if (playerNhanDau != null) {
                                    //UPDATE LAI MESSAGE XIN DAU
                                    MessXinDau.recieve += 1;
                                    //TAO MOI ITEM DAU THAN
                                    ItemSell itemDAUTHAN = ItemSell.getItemSell(player.ItemBox[indexDau].id, (byte) 1);
                                    playerNhanDau.addItemToBag(itemDAUTHAN.item);
                                    Service.gI().updateItemBag(playerNhanDau);
                                    playerNhanDau.sendAddchatYellow("Bạn vừa nhận được " + itemDAUTHAN.item.template.name);
                                    //TRU DAU CUA NGUOI CHO DAU
                                    player.ItemBox[indexDau].quantity -= 1;
                                    if (player.ItemBox[indexDau].quantity == 0) {
                                        player.ItemBox[indexDau] = null;
                                    }
                                    Service.gI().updateItemBox(player);

                                    //SEND CHAT TO CLAN
                                    ClanService.gI().sendMessageToClan(player.clan);
                                    //REMOVE MESSAGE XIN DAU NEU DU 5 VIEN
                                    if (MessXinDau.recieve >= 5) {
                                        player.clan.messages.remove((byte) (idMessDau - 1));
                                    }
                                }
                            }
                        } else {
                            player.sendAddchatYellow("Đã hết đậu thần trong rương, hãy về thu hoạch thêm");
                        }
                    } else {
                        player.sendAddchatYellow("Chỉ thực hiện ở NOAH 1");
                    }
                    break;
                case -51: //XIN VAO BANG, CHAT BANG
                    byte typeChat = m.reader().readByte();
                    if (Server.gI().isServer == (byte) 1) {
                        if (typeChat == 0) {
                            String tchat = m.reader().readUTF();
                            tchat = tchat.toLowerCase();
                            if (tchat.length() > 40) {
                                tchat = tchat.substring(0, 40);
                            }
                            int id = (int) (player.clan.messages.size() + 1);
                            ClanMessage clanMess = new ClanMessage(player, id, (byte) 0, tchat, (byte) 0); //PLAYER, IDMESS, TYPEMEss, TExtMess, COLORMESS
                            player.clan.messages.add(clanMess);
                            if (player.clan.messages.size() > 10) {
                                player.clan.messages.remove(0); //REMOVE MESSAGE DAU TIEN;
                            }
                            //SEND CHAT TO CLAN
                            ClanService.gI().sendMessageToClan(player.clan);
                        } else if (typeChat == 2) {
                            int idClanX = m.reader().readInt();
                            //GET CLAN JOIN
                            Clan clanJoin = ClanManager.gI().getClanById(idClanX);

                            ClanService.gI().checkHasMessageClan(clanJoin, player.id);
                            int idx = (int) (clanJoin.messages.size() + 1);
                            ClanMessage clanMess = new ClanMessage(player, idx, (byte) 2, "Xin vào bang hội", (byte) 0); //PLAYER, IDMESS, TYPEMEss, TExtMess, COLORMESS
                            clanMess.playerName = player.name + " (" + Util.powerToString(player.power) + ")";
                            clanJoin.messages.add(clanMess);
                            if (clanJoin.messages.size() > 10) {
                                clanJoin.messages.remove(0); //REMOVE MESSAGE DAU TIEN;
                            }
                            //SEND CHAT TO CLAN
                            ClanService.gI().sendMessageToClan(clanJoin);
                        } else if (typeChat == 1) { //XIN DAU THAN
                            if ((System.currentTimeMillis() - player._TIMEXINDAU) > 300000) {
                                player._TIMEXINDAU = System.currentTimeMillis();
                                int idx = (int) (player.clan.messages.size() + 1);
                                ClanMessage clanMess = new ClanMessage(player, idx, (byte) 1, "Xin đậu thần", (byte) 0); //PLAYER, IDMESS, TYPEMEss, TExtMess, COLORMESS
                                player.clan.messages.add(clanMess);
                                if (player.clan.messages.size() > 10) {
                                    player.clan.messages.remove(0); //REMOVE MESSAGE DAU TIEN;
                                }
                                //SEND CHAT TO CLAN
                                ClanService.gI().sendMessageToClan(player.clan);
                            } else {
                                player.sendAddchatYellow("Chờ hết 5p đi bạn ơi");
                            }
                        }
                    } else {
                        player.sendAddchatYellow("Chỉ thực hiện ở NOAH 1");
                    }
                    break;
                case -50:
                    int clanId = m.reader().readInt();
                    ClanService.gI().clanMember(_session, clanId);
                    break;
                case -49: //CHAP NHAN HOAC TU CHOI VAO BANG
                    int idAccept = m.reader().readInt();
                    byte actionAccept = m.reader().readByte();
                    if (Server.gI().isServer == (byte) 1) {
                        if (player.clan != null && player.id == player.clan.leaderID) {
                            if (actionAccept == (byte) 0) { //CHAP NHAN VAO BANG
                                //JOIN TO CLAN
                                ClanMessage messXin = player.clan.messages.get((int) (idAccept - 1));
                                Player _playerAccept = PlayerManger.gI().getPlayerByUserID(messXin.playerId);
                                if (_playerAccept != null) {
                                    if (_playerAccept.clan == null) {
                                        messXin.text = "Chấp nhận vào bang";
                                        messXin.type = (byte) 0;
                                        ClanService.gI().addPlayerToClan(_playerAccept, player.clan.id);
                                    }
                                } else {
                                    player.sendAddchatYellow("Người chơi hiện tại đang offline");
                                }
                            } else if (actionAccept == (byte) 1) { //TU CHOI VAO BANG
                                //JOIN TO CLAN
                                ClanMessage messXin = player.clan.messages.get((int) (idAccept - 1));
                                messXin.text = "Từ chối vào bang";
                                messXin.color = (byte) 2;
                                messXin.type = (byte) 0;
                                //SEND CHAT TO CLAN
                                ClanService.gI().sendMessageToClan(player.clan);
                            }
                        } else {
                            player.sendAddchatYellow("Chức năng chỉ dành cho chủ bang");
                        }
                    } else {
                        player.sendAddchatYellow("Chỉ thực hiện ở NOAH 1");
                    }
                    break;
                case -47:
                    if (player.clan == null) {
                        String clanName = m.reader().readUTF();
//                        ClanService.gI().clanIconImageInfo(_session);
//                        ClanService.gI().clanIconImage(_session);
                        ClanService.gI().searchClan(_session, clanName);
                    } else {
                        ClanService.gI().clanInfo(_session, player);
                    }
                    break;
                case -46:
                    byte action = m.reader().readByte();
                    if (Server.gI().isServer == (byte) 1) {
                        if (action == 4) {
                            if (player.clan != null && player.id == player.clan.leaderID) {
                                byte idIMGBag = m.reader().readByte(); //idIMGBag NEW
                                String SLOGAN = m.reader().readUTF(); //SLOGAN
                                if (idIMGBag != player.clan.imgID) {
                                    ClanService.gI().updateBagClan(player, idIMGBag); //UPDATE BIEU TUONG BANG

                                    //UPDATE VANG NGOC
                                    Service.gI().updateVangNgoc(player);
                                    for (Player _p : player.zone.players) {
                                        //UPDATE BAG SAU LUNG
                                        Service.gI().updateBagNew(_p.session, player.id, idIMGBag);
                                        //GET BAG SAU LUNG
                                        ClanService.gI().getBagBangNew(_p.session, idIMGBag);
                                    }
                                } else if (SLOGAN != "" && SLOGAN != player.clan.slogan) {
                                    Service.gI().updateSloganClan(_session, SLOGAN);
                                }
                            } else {
                                player.sendAddchatYellow("Chức năng chỉ dành cho chủ bang");
                            }
                        } else if (action == 3) { //UPDATE BIEU TUONG BANG
                            if (player.clan != null) {
                                ClanService.gI().clanIconImageInfo(_session, (byte) 3);
                                ClanService.gI().clanIconImage(_session);
                            }
                        } else if (action == 1) { //TAO BANG
                            if (player.clan == null) {
                                ClanService.gI().clanIconImageInfo(_session, (byte) 1);
                                ClanService.gI().clanIconImage(_session);
                            }
                        } else if (action == 2) { //SEND TAO BANG
                            if (player.clan == null) {
                                byte idBag = m.reader().readByte();
                                if (ClanService.gI().createNewClan(idBag, m.reader().readUTF(), player)) {
//                                ClanService.gI().createNewClan(idBag, m.reader().readUTF(), player);
                                    //SEND PT MOI
                                    ClanService.gI().clanInfo(_session, player);

                                    //UPDATE VANG NGOC
                                    Service.gI().updateVangNgoc(player);

                                    for (Player _p : player.zone.players) {
                                        //UPDATE BAG SAU LUNG
                                        Service.gI().updateBagNew(_p.session, player.id, idBag);
                                        //GET BAG SAU LUNG
                                        ClanService.gI().getBagBangNew(_p.session, idBag);
                                    }
                                    //CHECK NHIEM VU GIA NHAP BANG HOI
                                    if (player.taskId == (short) 12 && player.crrTask.index == (byte) 0) {
                                        TaskService.gI().updateCountTask(player);
                                    }
                                }
                                //UPDATE CLAN
                                //                        ClanService.gI().updateClanType0(_session, player.clan.members.get(0));
                            }
                        }
                    } else {
                        player.sendAddchatYellow("Chỉ thực hiện ở NOAH 1");
                    }
                    break;
                // su dung skill dac biet
                case -45:
                    byte kynang = m.reader().readByte();

                    switch (kynang) {
                        //bien khi cua xayda
                        case 6:
                            player.zone.useMonkey(player);
//                            Skill templateSkillUse = player.getSkillByIDTemplate((short)13);
//                            long _TIMENOW = System.currentTimeMillis();
//                            int _manaUse = (int)(player.getMpFull()/10);
//                            if(player.mp >= _manaUse && ((_TIMENOW - templateSkillUse.lastTimeUseThisSkill) > (long)templateSkillUse.coolDown)) {
//                                player.mp -= _manaUse;
//                                templateSkillUse.lastTimeUseThisSkill = _TIMENOW;
//                                player.isMonkey = true;
//                                //NOI TAI TANG DAME KHI HOA KHI
//                                if(!player.upDameAfterKhi && player.noiTai.id != 0 && player.noiTai.idSkill == (byte)13) {
//                                    player.upDameAfterKhi = true;
//                                }
//                                //NOI TAI TANG DAME KHI HOA KHI
//                                player.updateBodyMonkey();
//    //                            Service.gI().loadPoint(_session, player);
//                            }
                            break;
                        //thai duong ha san
                        case 0:
                            player.zone.useTDHS(player);
                            break;
                        //khoi tao tai tao nang luong xayda
                        case 1:
                            player.chargeHPMP(1);
                            break;
                        //uphpmp tai tao nang luong
                        case 2:
                            player.chargeHPMP(2);
                            break;
                        //ket thuc tai tao nang luong
                        case 3:
                            player.chargeHPMP(3);
                            break;
                        case 4:
                            if (player.gender == 1) {
                                player.zone.useLaze(player); //done -mp
                            }
                            if (player.gender == 0) { // done -mp
                                player.zone.useKenhKhi(player);
                            }
                            break;
                        case 7: //done -mp
                            if (player.gender == (byte) 2) {
                                player.zone.useBoomXayda(player);
                            }
                            break;
                        case 10: //done -mp
                            if (player.gender == (byte) 2) {
                                player.zone.useHuytSao(player);
                            }
                            break;
                        case 9: //done -mp
                            player.zone.useProtect(player);
                            break;
                        case 8: //done -mp
                            if (player.idSkillselect == 12 && player.gender == 1) {
//                                player.zone.useDeTrung2(player); // de trung tam thoi, cong chi so vao player
                                player.zone.useDeTrung(player, (byte) 0);
//                                player.zone.useDeTrung(player); de trung chinh
                            }
                            break;
                    }
                    break;
                case -43:
                    if (!player.lockPK) {
                        try {
                            short idItem = 0;
                            byte typeUse = m.reader().readByte();
                            byte where = m.reader().readByte();
                            byte index = m.reader().readByte();
                            if (index == -1) {
                                idItem = m.reader().readShort();
                            }
                            UseItemHandler(_session, player, typeUse, where, index, idItem);
//                            System.out.println(" Type USe, WHERE, INDEX, ID ITEM: " + typeUse + ", " + where + ", " + index + ", " + idItem);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        player.sendAddchatYellow("Không thể thực hiện");
                    }
                    break;

                case -41:
                    //UPDATE_CAPTION
                    byte _ggender = m.reader().readByte();
//                    Service.gI().sendMessage(_session, -41, "1630679754812_-41_r");
                    sendCaptionLevel(_session, _ggender);
                    break;
                case -40:
                    if (!player.lockPK) {
                        ReadMessage.gI().getItem(_session, m);
                    } else {
                        player.sendAddchatYellow("Không thể thực hiện");
                    }
                    break;
                case -39:
                    //finishLoadMap
                    player.zone.loadPlayers(player);
                    break;
                case -32:
                    int bgId = m.reader().readShort();
//                    Util.log("-32 load ID: " + bgId);
//                    if(_session.zoomLevel == 2) {
//                        if(player.map.id < 153) {
                    Service.gI().bgTemp(_session, bgId);
//                        }
//                    }
                    break;
                case -30:
                    messageSubCommand(_session, m);
                    break;

                case -29:
                    messageNotLogin(_session, m);
                    break;
                case -28:
                    messageNotMap(_session, m);
                    break;
                case -111:
//                    short shortImgs2 = m.reader().readShort();
//                    Util.log("TOTAL IMGSRC2: " + shortImgs2);
//                    if(!_session.imgSrc2 && shortImgs2 == (short)0) {
//                        _session.imgSrc2 = true;
//                        Service.gI().sendMessage(_session, -111, "20220809234716450_-111_r");
//                    }
//                    for(short i = 0; i < shortImgs2; i++) {
//                        Util.log("UTF+byte: " + m.reader().readUTF());
//                    }
//                    Service.gI().sendMessage(_session, -111, "1630679748814_-111_r"); //-111
                    break;
                case -27:
                    _session.sendSessionKey();
//                    Service.gI().sendMessage(_session, -111, "1630679748814_-111_r"); //-111 ////////////////-111 PHIA O DAY
                    Service.gI().sendMessage(_session, -111, "20220809234716450_-111_r");
                    Service.gI().versionImageSource(_session); //-74
//                    Service.gI().sendMessage(_session, -29, "1630679748828_-29_2_r"); // -29
                    m = new Message(-29);
                    m.writer().writeByte(2);
                    m.writer().writeUTF("Chibi Dragon:103.161.174.21:14445:0");
                    m.writer().writeByte(1);
                    m.writer().flush();
                    _session.sendMessage(m);
                    m.cleanup();
                    break;
                case -33:
                case -23:
                    WayPoint[] wp = player.map.template.wayPoints;
                    if (wp != null) {
                        Map mapold = player.map;
                        for (int i = 0; i < wp.length; i++) {
                            Map maptele = MainManager.getMapid(wp[i].goMap);
                            player.map = maptele;
                        }
                        player.zone.VGo(player, m, mapold);
                    } else {
                        Service.gI().serverMessage(_session, "Không thể vào map");
                        Service.gI().resetPoint(_session, player.x - 50, player.y);
                    }
                    break;
                case -20:
                    try {
                    short itemMapId = m.reader().readShort();
                    if (itemMapId == (short) 74) {
                        player.update();
                        Message ms = new Message(-20);
                        ms.writer().writeShort(itemMapId);
                        ms.writer().writeUTF("Bạn vừa ăn Đùi Gà Nướng");
                        player.session.sendMessage(ms);
                        ms.cleanup();
                        return;
                    } else if (itemMapId == (short) 78) {
                        Message ms = new Message(-20);
                        ms.writer().writeShort(itemMapId);
                        ms.writer().writeUTF("Wow, một đứa bé thật dễ thương!");
                        player.session.sendMessage(ms);
                        ms.cleanup();
                        if (player.taskId == (short) 3 && player.crrTask.index == (byte) 1) {//NHIEM VU SAO BANG
                            TaskService.gI().updateCountTask(player);
                        }
                        return;
                    }
                    ItemMap itemPick = player.zone.getItemMapByID(itemMapId);
                    if (itemPick != null) {
                        if (itemPick.itemTemplateID >= (short) 372 && itemPick.itemTemplateID <= (short) 378) {
                            if ((System.currentTimeMillis() - itemPick.timeDrop) >= 10000) {
                                if (player.zone.timePickNRSD != 0 && Server.gI().openNRSD) {
                                    if (System.currentTimeMillis() >= player.zone.timePickNRSD) {
                                        player.imgNRSD = (byte) 37;
                                        player.timeWINNRSD = System.currentTimeMillis() + (long) 300000;
                                        for (Player _p : player.zone.players) {
                                            //UPDATE BAG SAU LUNG
                                            Service.gI().updateBagNew(_p.session, player.id, (byte) 37);
                                            //GET BAG SAU LUNG
                                            ClanService.gI().getBagBangNew(_p.session, (byte) 37);
                                            if (_p.id != player.id) {
                                                ClanService.gI().loadUpdateInfoMember(_p.session, player);
                                            }
                                        }
                                        // NHAT NGOC RONG THI DOI SANG CO DEN
                                        player.cPk = (byte) 8;
                                        player.detu.cPk = (byte) 8;
                                        Service.gI().changeFlagPK(player, (byte) 8);
                                        if (player.petfucus == 1) {
                                            Service.gI().changeFlagPKPet(player, (byte) 8);
                                        }
                                        //END NHAT NGOC RONG THI DOI SANG CO DEN
                                        player.sendAddchatYellow("Bạn nhận được Ngọc rồng " + (itemPick.itemTemplateID - (short) 371) + " sao đen");
                                        player.zone.removeItemDrop(player, (int) itemMapId);

                                        //TASK WIN NGOC RONG SAO DEN
                                        Timer timerWinNRSD = new Timer();
                                        TimerTask winNRSD = new TimerTask() {
                                            public void run() {
                                                if (player.imgNRSD == (byte) 0) {
                                                    player.timeWINNRSD = 0;
                                                    timerWinNRSD.cancel();
                                                } else {
                                                    if (System.currentTimeMillis() >= player.timeWINNRSD) {
                                                        boolean hasNRSD = false;
                                                        for (byte i = 0; i < player.indexNRSD.size(); i++) {
                                                            if (player.indexNRSD.get(i) == (byte) (itemPick.itemTemplateID - (short) 372) && (System.currentTimeMillis() + (long) 2000000) < player.timeEndNRSD[i]) {
                                                                hasNRSD = true;
                                                                break;
                                                            }
                                                        }
                                                        if (!hasNRSD) {
                                                            player.zone.timePickNRSD = 0;
                                                            player.sendAddchatYellow("Chúc mừng bạn đã dành được Ngọc rồng " + (itemPick.itemTemplateID - (short) 371) + " sao đen cho bang");
                                                            //                                                player.imgNRSD = (byte)0;
                                                            //                                                player.timeWINNRSD = 0;
                                                            //SETUP PHAN THUONG CHO CA CLAN
                                                            player.timeNRSD[(itemPick.itemTemplateID - (short) 372)] = System.currentTimeMillis() - (long) 3600000;
                                                            player.timeEndNRSD[(itemPick.itemTemplateID - (short) 372)] = System.currentTimeMillis() + (long) (86400000);
                                                            player.indexNRSD.add((byte) (itemPick.itemTemplateID - (short) 372));
                                                            Service.gI().loadPoint(player.session, player);
                                                            if (player.clan != null) {
                                                                ClanService.gI().setupNRSDClan(player, (byte) (itemPick.itemTemplateID - (short) 372));
                                                            }
                                                            //ALL PLAYER VE TRAM TAU VU TRU
                                                            byte size = (byte) player.zone.players.size();
                                                            for (byte i = 0; i < size; i++) {
                                                                if (player.zone.players.get(0).id != player.id) {
                                                                    player.zone.players.get(0).sendAddchatYellow("Trò chơi tìm ngọc đã kết thúc. Hẹn gặp lại bạn vào 20h tối mai");
                                                                    player.zone.players.get(0).zone.goMapTransport(player.zone.players.get(0), (int) player.zone.players.get(0).gender + 24);
                                                                }
                                                            }
                                                            player.zone.goMapTransport(player, (int) player.gender + 24);
                                                            timerWinNRSD.cancel();
                                                        } else {
                                                            player.sendAddchatYellow("Bang hội của bạn đã có Ngọc rồng " + (itemPick.itemTemplateID - (short) 371) + " sao đen");
                                                            //NEU DEO NGOC RONG SAO DEN THI ROT RA DAT
                                                            if (player.imgNRSD == (byte) 37) {
                                                                player.imgNRSD = (byte) 0;
                                                                player.timeWINNRSD = 0;
                                                                player.zone.resetBagClan(player);
                                                                ItemMap itemM = player.zone.createNewItemMap((player.map.id + 287), -1, player.x, player.y);
                                                                player.zone.addItemToMap(itemM, -1, player.x, player.y);
                                                            }
                                                            timerWinNRSD.cancel();
                                                        }
                                                    } else {
                                                        player.sendAddchatYellow("Cố giữ ngọc thêm " + (int) ((player.timeWINNRSD - System.currentTimeMillis()) / 1000) + " giây nữa sẽ thắng");
                                                    }
                                                }
                                            }
                                        ;
                                        };
                                            timerWinNRSD.schedule(winNRSD, 0, 10000);
                                    } else {
                                        player.sendAddchatYellow("Chưa thể nhặt lúc này, hãy đợi " + (int) ((player.zone.timePickNRSD - System.currentTimeMillis()) / 1000) + " giây nữa");
                                    }
                                } else {
                                    player.sendAddchatYellow("Trò chơi tìm ngọc đã kết thúc. Hẹn gặp lại bạn vào 20h tối mai");
                                }
                            } else {
                                player.sendAddchatYellow("Chưa thể nhặt ngọc rồng lúc này");
                            }
                        } else if (itemPick.itemTemplateID >= (short) 353 && itemPick.itemTemplateID <= (short) 359) { //PICK NGOC RONG NAMEC
                            if ((System.currentTimeMillis() - itemPick.timeDrop) >= 10000) {
                                if (System.currentTimeMillis() >= Server.gI().tOpenNrNamec) {
                                    if (player.imgNRSD != (byte) 53) {
                                        player.imgNRSD = (byte) 53;
                                        for (Player _p : player.zone.players) {
                                            //UPDATE BAG SAU LUNG
                                            Service.gI().updateBagNew(_p.session, player.id, (byte) 53);
                                            //GET BAG SAU LUNG
                                            ClanService.gI().getBagBangNew(_p.session, (byte) 53);
                                            if (_p.id != player.id) {
                                                ClanService.gI().loadUpdateInfoMember(_p.session, player);
                                            }
                                        }
                                        player.nrNamec = (int) itemPick.itemTemplateID;

                                        Server.gI().mapNrNamec[(int) (itemPick.itemTemplateID - 353)] = player.map.id;
                                        Server.gI().nameNrNamec[(int) (itemPick.itemTemplateID - 353)] = player.map.template.name;
                                        Server.gI().zoneNrNamec[(int) (itemPick.itemTemplateID - 353)] = player.zone.id;
                                        Server.gI().pNrNamec[(int) (itemPick.itemTemplateID - 353)] = player.name;
                                        Server.gI().idpNrNamec[(int) (itemPick.itemTemplateID - 353)] = player.id;

                                        player.sendAddchatYellow("Bạn nhận được Ngọc rồng " + (itemPick.itemTemplateID - (short) 352) + " sao");
                                        player.zone.removeItemDrop(player, (int) itemMapId);
                                        //UPDATE TYKE PK
                                        player.typePk = (byte) 5;
                                        Service.gI().setTypePK(player, (byte) 5);
                                    } else {
                                        player.sendAddchatYellow("Không thể nhặt lúc này");
                                    }
                                } else {
                                    player.sendAddchatYellow("Chỉ là cục đá thôi, nhặt làm gì?");
                                }
                            } else {
                                player.sendAddchatYellow("Chưa thể nhặt ngọc rồng lúc này");
                            }
                        } else if (itemPick.itemTemplateID == (short) 362) { //PICK HOA THACH NGOC RONG NAMEC
                            player.sendAddchatYellow("Chỉ là cục đá thôi, nhặt làm gì?");
                        } else {
                            player.zone.PickItemDrop(player, itemMapId);
                        }
                    }
//                        if(itemMapId >= (short)372 && itemMapId <= (short)378) { //PICK NGOC RONG SAO DEN
//                            ItemMap NrBLACK = player.zone.getItemMapByID(itemMapId);
//                            if(NrBLACK != null && (System.currentTimeMillis() - NrBLACK.timeDrop) >= 10000) {
//                                if(player.zone.timePickNRSD != 0 && Server.gI().openNRSD) {
//                                    if(System.currentTimeMillis() >= player.zone.timePickNRSD) {
//                                        player.imgNRSD = (byte)37;
//                                        player.timeWINNRSD = System.currentTimeMillis() + (long)300000;
//                                        for(Player _p: player.zone.players) {
//                                            //UPDATE BAG SAU LUNG
//                                            Service.gI().updateBagNew(_p.session, player.id, (byte)37);
//                                            //GET BAG SAU LUNG
//                                            ClanService.gI().getBagBangNew(_p.session, (byte)37);
//                                            if(_p.id != player.id) {
//                                                ClanService.gI().loadUpdateInfoMember(_p.session, player);
//                                            }
//                                        }
//                                        // NHAT NGOC RONG THI DOI SANG CO DEN
//                                        player.cPk = (byte)8;
//                                        player.detu.cPk = (byte)8;
//                                        Service.gI().changeFlagPK(player, (byte)8);
//                                        if(player.petfucus == 1) {
//                                            Service.gI().changeFlagPKPet(player, (byte)8);
//                                        }
//                                        //END NHAT NGOC RONG THI DOI SANG CO DEN
//                                        player.sendAddchatYellow("Bạn nhận được Ngọc rồng " + (itemMapId-(short)371) +" sao đen");
//                                        player.zone.removeItemDrop(player, (int)itemMapId);
//
//                                        //TASK WIN NGOC RONG SAO DEN
//                                        Timer timerWinNRSD = new Timer();
//                                        TimerTask winNRSD = new TimerTask() {
//                                            public void run()
//                                            {
//                                                if(player.imgNRSD == (byte)0) {
//                                                    player.timeWINNRSD = 0;
//                                                    timerWinNRSD.cancel();
//                                                } else {
//                                                    if(System.currentTimeMillis() >= player.timeWINNRSD) {
//                                                        boolean hasNRSD = false;
//                                                        for(byte i = 0; i < player.indexNRSD.size(); i++) {
//                                                            if(player.indexNRSD.get(i) == (byte)(itemMapId-(short)372) && (System.currentTimeMillis() + (long)2000000) < player.timeEndNRSD[i]) {
//                                                                hasNRSD = true;
//                                                                break;
//                                                            }
//                                                        }
//                                                        if(!hasNRSD) {
//                                                            player.zone.timePickNRSD = 0;
//                                                            player.sendAddchatYellow("Chúc mừng bạn đã dành được Ngọc rồng " + (itemMapId-(short)371) +" sao đen cho bang");
//            //                                                player.imgNRSD = (byte)0;
//            //                                                player.timeWINNRSD = 0;
//                                                            //SETUP PHAN THUONG CHO CA CLAN
//                                                            player.timeNRSD[(itemMapId-(short)372)] = System.currentTimeMillis() - (long)3600000;
//                                                            player.timeEndNRSD[(itemMapId-(short)372)] = System.currentTimeMillis() + (long)(86400000);
//                                                            player.indexNRSD.add((byte)(itemMapId-(short)372));
//                                                            Service.gI().loadPoint(player.session, player);
//                                                            if(player.clan != null) {
//                                                                ClanService.gI().setupNRSDClan(player, (byte)(itemMapId-(short)372));
//                                                            }
//                                                            //ALL PLAYER VE TRAM TAU VU TRU
//                                                            byte size = (byte)player.zone.players.size();
//                                                            for(byte i = 0; i < size; i++) {
//                                                                if(player.zone.players.get(0).id != player.id) {
//                                                                    player.zone.players.get(0).sendAddchatYellow("Trò chơi tìm ngọc đã kết thúc. Hẹn gặp lại bạn vào 20h tối mai");
//                                                                    player.zone.players.get(0).zone.goMapTransport(player.zone.players.get(0), (int)player.zone.players.get(0).gender + 24);
//                                                                }
//                                                            }
//                                                            player.zone.goMapTransport(player, (int)player.gender + 24);
//                                                            timerWinNRSD.cancel();
//                                                        } else {
//                                                            player.sendAddchatYellow("Bang hội của bạn đã có Ngọc rồng " + (itemMapId-(short)371) +" sao đen");
//                                                            //NEU DEO NGOC RONG SAO DEN THI ROT RA DAT
//                                                            if(player.imgNRSD == (byte)37) {
//                                                                player.imgNRSD = (byte)0;
//                                                                player.timeWINNRSD = 0;
//                                                                player.zone.resetBagClan(player);
//                                                                ItemMap itemM = player.zone.createNewItemMap((player.map.id + 287), -1, player.x, player.y);
//                                                                player.zone.addItemToMap(itemM, -1, player.x, player.y);
//                                                            }
//                                                            timerWinNRSD.cancel();
//                                                        }
//                                                    } else {
//                                                        player.sendAddchatYellow("Cố giữ ngọc thêm " + (int)((player.timeWINNRSD - System.currentTimeMillis())/1000) +" giây nữa sẽ thắng");
//                                                    }
//                                                }
//                                            };
//                                        };
//                                        timerWinNRSD.schedule(winNRSD, 0, 10000);
//                                    } else {
//                                        player.sendAddchatYellow("Chưa thể nhặt lúc này, hãy đợi " + (int)((player.zone.timePickNRSD-System.currentTimeMillis())/1000) + " giây nữa");
//                                    }
//                                } else {
//                                    player.sendAddchatYellow("Trò chơi tìm ngọc đã kết thúc. Hẹn gặp lại bạn vào 20h tối mai");
//                                }
//                            } else {
//                                player.sendAddchatYellow("Chưa thể nhặt ngọc rồng lúc này");
//                            }
//                        } else if(itemMapId >= (short)353 && itemMapId <= (short)359) { //PICK NGOC RONG NAMEC
//                            ItemMap NrNAMEC = player.zone.getItemMapByID(itemMapId);
//                            if(NrNAMEC != null && (System.currentTimeMillis() - NrNAMEC.timeDrop) >= 10000) {
//                                if(System.currentTimeMillis() >= Server.gI().tOpenNrNamec) {
//                                    if(player.imgNRSD != (byte)53) {
//                                        player.imgNRSD = (byte)53;
//                                        for(Player _p: player.zone.players) {
//                                            //UPDATE BAG SAU LUNG
//                                            Service.gI().updateBagNew(_p.session, player.id, (byte)53);
//                                            //GET BAG SAU LUNG
//                                            ClanService.gI().getBagBangNew(_p.session, (byte)53);
//                                            if(_p.id != player.id) {
//                                                ClanService.gI().loadUpdateInfoMember(_p.session, player);
//                                            }
//                                        }
//                                        player.nrNamec = (int)itemMapId;
//
//                                        Server.gI().mapNrNamec[(int)(itemMapId - 353)] = player.map.id;
//                                        Server.gI().nameNrNamec[(int)(itemMapId - 353)] = player.map.template.name;
//                                        Server.gI().zoneNrNamec[(int)(itemMapId - 353)] = player.zone.id;
//                                        Server.gI().pNrNamec[(int)(itemMapId - 353)] = player.name;
//                                        Server.gI().idpNrNamec[(int)(itemMapId - 353)] = player.id;
//
//                                        player.sendAddchatYellow("Bạn nhận được Ngọc rồng " + (itemMapId-(short)352) +" sao");
//                                        player.zone.removeItemDrop(player, (int)itemMapId);
//                                        //UPDATE TYKE PK
//                                        player.typePk = (byte)5;
//                                        Service.gI().setTypePK(player, (byte)5);
//                                    } else {
//                                        player.sendAddchatYellow("Không thể nhặt lúc này");
//                                    }
//                                } else {
//                                    player.sendAddchatYellow("Chỉ là cục đá thôi, nhặt làm gì?");
//                                }
//                            } else {
//                                player.sendAddchatYellow("Chưa thể nhặt ngọc rồng lúc này");
//                            }
//                        } else if(itemMapId == (short)362) { //PICK HOA THACH NGOC RONG NAMEC
//                            player.sendAddchatYellow("Chỉ là cục đá thôi, nhặt làm gì?");
//                        } else {
//                            player.zone.PickItemDrop(player, itemMapId);
//                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
                //Wakeup Die Return
                case -16: {
                    player.getPlace().wakeUpDieReturn(player);
                    break;
                }
                //Die Return
                case -15: {
                    player.getPlace().dieReturn(player);
                    break;
                }
                case -7:
                    byte b = m.reader().readByte();
                    short xPre = player.x;
                    try {
                        player.x = m.reader().readShort();
                        player.y = m.reader().readShort();
                    } catch (Exception e) {
                    }
                    player.zone.playerMove(player);
                    if (player.havePet == 1 && (player.statusPet == 0 || player.statusPet == 1) && !player.detu.isdie && !player.detu.checkBiKhongChe()) {
                        player.detu.x = (short) (xPre > player.x ? (player.x + 30) : (player.x - 30));
                        player.detu.y = player.y;
                        player.zone.detuMove(player.detu);
                    }
                    if (player.pet2Follow == (byte) 1 && player.pet != null) {
                        player.pet.x = (short) (xPre > player.x ? (player.x + 60) : (player.x - 60));
                        player.pet.y = player.y;
                        player.zone.detuMove(player.pet);
                    }
                    if (player.isTroi == true) {
                        player.zone.resetTROIKHIMOVE(player);
                    }
                    //CHECK TREN VO DAI
                    if (player.y == (short) 336 && (player.x >= (short) 611 || player.x <= (short) 157) && player.lockPK && player._friendGiaoDich != null) {
                        player.lockPK = false;
                        DaiHoiService.gI().winRoundDHVT(player._friendGiaoDich, player);
                    }
                    break;
                case 6:
                    try {
                    byte typeBuy = m.reader().readByte();
                    short itemID = m.reader().readShort();
                    boolean isCanBuy = false;

                    if (player.openItemQuay) {

                        if (typeBuy == (byte) 0) { // NHAN 1 DO THOI
                            //                            Item _itemBuy = new Item(itemBuy.item);
                            if (player.ItemQuay.get(itemID).template.id == 190) {
                                player.sendAddchatYellow("Bạn vừa nhận được " + player.ItemQuay.get(itemID).quantity / 1000 + "K vàng");
                                player.vang = ((long) (player.vang + player.ItemQuay.get(itemID).quantity) > 2000000000L ? 2000000000L : (long) (player.vang + player.ItemQuay.get(itemID).quantity));
                                player.ItemQuay.remove(itemID);
                                Service.gI().buyDone(player);
                            } else {
                                boolean isBuySuccess = player.addItemToBag(player.ItemQuay.get(itemID));
                                if (isBuySuccess) {
                                    player.sendAddchatYellow("Bạn vừa nhận được " + player.ItemQuay.get(itemID).template.name);
                                    player.ItemQuay.remove(itemID);
                                    Service.gI().updateItemBag(player);
                                } else {
                                    Service.gI().updateItemBag(player);
                                }
                            }
                        } else if (typeBuy == (byte) 1) { //XOA BO DO
                            player.ItemQuay.remove(itemID);
                        } else if (typeBuy == (byte) 2) { //NHAN ALL DO
                            for (byte i = 0; i < player.ItemQuay.size(); i++) {
                                if (player.ItemQuay.get(i).template.id == 190) {
                                    player.sendAddchatYellow("Bạn vừa nhận được " + player.ItemQuay.get(i).quantity / 1000 + "K vàng");
                                    player.vang = ((long) (player.vang + player.ItemQuay.get(i).quantity) > 2000000000L ? 2000000000L : (long) (player.vang + player.ItemQuay.get(i).quantity));
                                } else {
                                    boolean isBuySuccess = player.addItemToBag(player.ItemQuay.get(i));
                                    if (isBuySuccess) {
                                        player.buyitemdelay = System.currentTimeMillis() + 1000L;
                                        player.sendAddchatYellow("Bạn vừa nhận được " + player.ItemQuay.get(i).template.name);
                                    }
                                }
                            }
                            Service.gI().updateItemBag(player);
                            Service.gI().buyDone(player);
                            player.ItemQuay.clear();
                        }
                        LuckyService.gI().openItemQuay(player);
                    } else {
                        ItemSell itemBuy = ItemSell.getItemSell(itemID, typeBuy);
                        if ((itemBuy.item.template.id >= 213 && itemBuy.item.template.id <= 219) || itemBuy.item.template.id == 522 || itemBuy.item.template.id == 671 || itemBuy.item.template.id == 672) {

                            if (player.ngocKhoa >= itemBuy.buyCoin) {
                                player.ngocKhoa -= itemBuy.buyCoin;
                                player.buyitemdelay = System.currentTimeMillis() + 1000L;
                                isCanBuy = true;
                            } else if (player.ngoc >= itemBuy.buyCoin) {
                                player.ngoc -= itemBuy.buyCoin;
                                player.buyitemdelay = System.currentTimeMillis() + 1000L;
                                isCanBuy = true;
                            } else {
                                player.sendAddchatYellow("Bạn không đủ ngọc để mua vật phẩm");
                                isCanBuy = false;
                            }
                            if (isCanBuy) {
                                player.buyitemdelay = System.currentTimeMillis() + 1000L;
                                if (itemID == 213) {
                                    if (player.listAmulet.get(0).timeEnd > System.currentTimeMillis()) {
                                        player.listAmulet.get(0).timeEnd += 43200 * 60000L;
                                    } else {
                                        player.listAmulet.get(0).timeEnd += (long) (System.currentTimeMillis() + 43200 * 60000L);
                                    }
                                } else if (itemID == 214) {
                                    if (player.listAmulet.get(1).timeEnd > System.currentTimeMillis()) {
                                        player.listAmulet.get(1).timeEnd += 43200 * 60000L;
                                    } else {
                                        player.listAmulet.get(1).timeEnd += (long) (System.currentTimeMillis() + 43200 * 60000L);
                                    }
                                } else if (itemID == 215) {
                                    if (player.listAmulet.get(2).timeEnd > System.currentTimeMillis()) {
                                        player.listAmulet.get(2).timeEnd += 43200 * 60000L;
                                    } else {
                                        player.listAmulet.get(2).timeEnd += (long) (System.currentTimeMillis() + 43200 * 60000L);
                                    }
                                } else if (itemID == 216) {
                                    if (player.listAmulet.get(3).timeEnd > System.currentTimeMillis()) {
                                        player.listAmulet.get(3).timeEnd += 43200 * 60000L;
                                    } else {
                                        player.listAmulet.get(3).timeEnd += (long) (System.currentTimeMillis() + 43200 * 60000L);
                                    }
                                } else if (itemID == 217) {
                                    if (player.listAmulet.get(4).timeEnd > System.currentTimeMillis()) {
                                        player.listAmulet.get(4).timeEnd += 43200 * 60000L;
                                    } else {
                                        player.listAmulet.get(4).timeEnd += (long) (System.currentTimeMillis() + 43200 * 60000L);
                                    }
                                } else if (itemID == 218) {
                                    if (player.listAmulet.get(5).timeEnd > System.currentTimeMillis()) {
                                        player.listAmulet.get(5).timeEnd += 43200 * 60000L;
                                    } else {
                                        player.listAmulet.get(5).timeEnd += (long) (System.currentTimeMillis() + 43200 * 60000L);
                                    }
                                } else if (itemID == 219) {
                                    if (player.listAmulet.get(6).timeEnd > System.currentTimeMillis()) {
                                        player.listAmulet.get(6).timeEnd += 43200 * 60000L;
                                    } else {
                                        player.listAmulet.get(6).timeEnd += (long) (System.currentTimeMillis() + 43200 * 60000L);
                                    }
                                } else if (itemID == 522) {
                                    if (player.listAmulet.get(7).timeEnd > System.currentTimeMillis()) {
                                        player.listAmulet.get(7).timeEnd += 43200 * 60000L;
                                    } else {
                                        player.listAmulet.get(7).timeEnd += (long) (System.currentTimeMillis() + 43200 * 60000L);
                                    }
                                } else if (itemID == 671) {
                                    if (player.listAmulet.get(8).timeEnd > System.currentTimeMillis()) {
                                        player.listAmulet.get(8).timeEnd += 43200 * 60000L;
                                    } else {
                                        player.listAmulet.get(8).timeEnd += (long) (System.currentTimeMillis() + 43200 * 60000L);
                                    }
                                } else if (itemID == 672) {
                                    if (player.listAmulet.get(9).timeEnd > System.currentTimeMillis()) {
                                        player.listAmulet.get(9).timeEnd += 43200 * 60000L;
                                    } else {
                                        player.listAmulet.get(9).timeEnd += (long) (System.currentTimeMillis() + 43200 * 60000L);
                                    }
                                }
                                player.sendAddchatYellow("Mua thành công " + itemBuy.item.template.name);
                                TabItemShop[] test = Shop.getTabShop(21, 0).toArray(new TabItemShop[0]);
                                GameScr.UIshop(player, test);
                            }
                            //                            Service.gI().updateItemBag(player);
                            Service.gI().buyDone(player);
                            return;
                        } else if (itemBuy.item.template.id == 517) { //MUA MO RONG HANH TRANG
                            if (player.maxluggage < (byte) 80) {
                                int _ngocMoHT = (int) (player.maxluggage - (byte) 20) < (byte) 10 ? ((int) (player.maxluggage - (byte) 20) + 1) * 100 : 1000;
                                if (player.ngoc >= _ngocMoHT) {
                                    player.ngoc -= _ngocMoHT;
                                    player.maxluggage = (byte) (player.maxluggage + 1);
                                    player.ItemBag = Arrays.copyOf(player.ItemBag, (player.ItemBag.length + 1));
                                    Service.gI().updateItemBag(player);
                                    player.buyitemdelay = System.currentTimeMillis() + 1000L;
                                    player.sendAddchatYellow("Mở rộng hành trang thành công");
                                    Service.gI().buyDone(player);
                                    TabItemShop[] test = Shop.getTabShop(39, 2).toArray(new TabItemShop[0]);
                                    GameScr.UIshop(player, test);
                                } else {
                                    player.sendAddchatYellow("Không đủ ngọc để mở rộng hành trang");
                                    Service.gI().buyDone(player);
                                }
                            } else {
                                player.sendAddchatYellow("Số ô hành trang đã đạt mức tối đa");
                                Service.gI().buyDone(player);
                            }
                            return;
                        } else if (itemBuy.item.template.id == 518) { //MUA MO RONG RUONG DO
                            if (player.maxBox < (byte) 80) {
                                int _vangMoRD = (int) (player.maxBox - (byte) 20) < (byte) 10 ? ((int) (player.maxBox - (byte) 20) + 1) * 50000000 : 500000000;
                                if (player.vang >= _vangMoRD) {
                                    player.vang -= _vangMoRD;
                                    player.maxBox = (byte) (player.maxBox + 1);
                                    player.ItemBox = Arrays.copyOf(player.ItemBox, (player.ItemBox.length + 1));
                                    Service.gI().updateItemBox(player);
                                    player.sendAddchatYellow("Mở rộng rương đồ thành công");
                                    Service.gI().buyDone(player);
                                    TabItemShop[] test = Shop.getTabShop(39, 2).toArray(new TabItemShop[0]);
                                    GameScr.UIshop(player, test);
                                    player.buyitemdelay = System.currentTimeMillis() + 1000L;
                                } else {
                                    player.sendAddchatYellow("Không đủ vàng để mở rộng rương đồ");
                                    Service.gI().buyDone(player);
                                }
                            } else {
                                player.sendAddchatYellow("Số ô rương đồ đã đạt mức tối đa");
                                Service.gI().buyDone(player);
                            }
                            return;
                        } else if (itemBuy.item.template.id == 453) { //MUA CHIEN THUYEN TENNIS
                            if (player.ngoc >= itemBuy.buyCoin) {
                                player.ngoc -= itemBuy.buyCoin;
                                player.sendAddchatYellow("Mua thành công chiến thuyền Tennis");
                                player.isTennis = true;
                                Service.gI().buyDone(player);
                            } else {
                                player.sendAddchatYellow("Bạn không đủ ngọc để mua vật phẩm");
                                Service.gI().buyDone(player);
                            }
                            return;
                        }
                        if (itemBuy == null || itemBuy.canbuy == 0) {
                            player.sendAddchatYellow("Item " + itemID + " chưa được mở bán");
                            return;
                        }
                        if (player.buyitemdelay > System.currentTimeMillis()) {
                            player.sendAddchatYellow("Bạn thao tác quá nhanh");
                            return;
                        }
                        if (typeBuy != itemBuy.buyType) {
                            if (typeBuy == 0) {
                                player.sendAddchatYellow("Item không bán bằng vàng");
                            }
                            if (typeBuy == 1) {
                                player.sendAddchatYellow("Item không bán bằng ngọc");
                            }
                            return;
                        }

                        if (itemBuy.buyType == 0) {
                            if (player.vang >= itemBuy.buyGold) {
                                player.vang -= itemBuy.buyGold;
                                isCanBuy = true;
                            } else {
                                player.sendAddchatYellow("Bạn không đủ vàng để mua vật phẩm");
                                isCanBuy = false;
                            }
                        } else if (itemBuy.buyType == 1) {
                            if (player.ngocKhoa >= itemBuy.buyCoin) {
                                player.ngocKhoa -= itemBuy.buyCoin;
                                if(itemBuy.item.template.id == 457){
                                    player.buyitemdelay = System.currentTimeMillis() + 30000L;
                                }
                                else{
                                    player.buyitemdelay = System.currentTimeMillis() + 1000L;
                                }
                                isCanBuy = true;
                            } else if (player.ngoc >= itemBuy.buyCoin) {
                                player.ngoc -= itemBuy.buyCoin;
                                if(itemBuy.item.template.id == 457){
                                    player.buyitemdelay = System.currentTimeMillis() + 30000L;
                                }
                                else{
                                    player.buyitemdelay = System.currentTimeMillis() + 1000L;
                                }
                                isCanBuy = true;
                            } else {
                                player.sendAddchatYellow("Bạn không đủ ngọc để mua vật phẩm");
                                isCanBuy = false;
                            }
                        } else {
                            player.sendAddchatYellow("lỗi");
                            isCanBuy = false;
                        }
                        //                        if(player.getBagNull() == 0){
                        //                            player.sendAddchatYellow("Hành trang không đủ chỗ trống");
                        //                            isCanBuy = false;
                        //                        }
                        if (isCanBuy) {
                            //                            Item _itemBuy = new Item(itemBuy.item);
                            boolean isBuySuccess = player.addItemToBag(itemBuy.item);
                            if (isBuySuccess) {
                                Service.gI().updateItemBag(player);
                                Service.gI().buyDone(player);
                                player.sendAddchatYellow("Mua thành công " + itemBuy.item.template.name);
                                if(itemBuy.item.template.id == 457){
                                    player.buyitemdelay = System.currentTimeMillis() + 30000L;
                                }
                                else{
                                    player.buyitemdelay = System.currentTimeMillis() + 1000L;
                                }
                            } else {
                                Service.gI().updateItemBag(player);
                                Service.gI().buyDone(player);
                            }
                        } else {
                            Service.gI().updateItemBag(player);
                            Service.gI().buyDone(player);
                        }

                    }
                } catch (Exception e) {
                    player.sendAddchatYellow("Mua vật phẩm không thành công");
                    e.printStackTrace();
                }
                break;
                case 7:
                    byte actionBuy = m.reader().readByte();
                    byte typeBuy = m.reader().readByte();
                    short indexBuy = m.reader().readShort();
                    Service.gI().SellItem(player, actionBuy, typeBuy, indexBuy);
                    break;
                case 11:
                    byte modId = m.reader().readByte();
                    Service.gI().requestModTemplate(_session, modId);
                    break;
                case 18: //TELE TO PLAYER: DICH CHUYEN
                    int idPTele = m.reader().readInt();
                    Player pTele = PlayerManger.gI().getPlayerByUserID(idPTele);
                    if (pTele != null && pTele.session != null) {
                        if (player.ItemBody[5] != null && ((player.ItemBody[5].template.id >= 592 && player.ItemBody[5].template.id <= 594) || (player.ItemBody[5].template.id == 905)
                                || (player.ItemBody[5].template.id == 907) || (player.ItemBody[5].template.id == 911))) {
                            teleToPlayer(player, pTele);
                        } else {
                            player.sendAddchatYellow("Yêu cầu có khả năng dịch chuyển tức thời!");
                        }
                    } else {
                        player.sendAddchatYellow("Người chơi hiện tại không online");
                    }
                    break;
                case 21:
                    //Chon khu vuc
                    if (player != null) {
                        if ((player.map.id >= 53 && player.map.id <= 62) || (player.map.id >= 114 && player.map.id <= 120) || player.map.id == 51 || (player.map.id >= 147 && player.map.id <= 152 && player.map.id != 150)) {
                            player.sendAddchatYellow("Không thể đổi khu vực tại đây");
                        } else {
                            if (System.currentTimeMillis() >= player.tSwapZone) {
                                if (player.id > 3) {
                                    player.tSwapZone = System.currentTimeMillis() + 15000;
                                }
                                player.zone.selectUIZone(player, m);
                            } else {
                                Service.chatNPC(player, (short) 24, "Chưa thể chuyển khu vực lúc này vui lòng chờ " + (int) ((player.tSwapZone - System.currentTimeMillis()) / 1000) + " giây nữa");
                            }
                        }
                    }
                    break;

                case 22:
                    //Xử lý menu có option 
                    try {
                    server.menu.menuHandler(player, m);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
                case 29:
                    if ((player.map.id >= 53 && player.map.id <= 62) || (player.map.id >= 114 && player.map.id <= 120) || player.map.id == 51 || (player.map.id >= 147 && player.map.id <= 152 && player.map.id != 150)) {
                        player.sendAddchatYellow("Không thể đổi khu vực tại đây");
                    } else {
                        try {
                            if (player != null) {
                                player.zone.openUIZone(player);
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    break;
                case 32:
                    try {
                    server.menu.confirmMenu(player, m);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

                case 33:
                    try {
                    server.menu.openUINpc(player, m);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
                case 34:
                    short selectSkill = m.reader().readShort();
                    player.selectSkill = player.getSkill(selectSkill);
                    player.idSkillselect = selectSkill;
                    break;
                case 35:
                    break;
                // chat
                case 44:
                    String text = m.reader().readUTF();
                    if (server.isDebug) {
                        if (text.contains("m ")) {
                            int mapId = Integer.parseInt(text.replace("m ", ""));

                            Map maptele = MainManager.getMapid(mapId);
                            teleportToMAP(player, maptele);
                        } else if (text.contains("smtn ")) {
                            int amount = Integer.parseInt(text.replace("smtn ", ""));
                            player.UpdateSMTN((byte) 2, amount);
                        } else if (text.equals("die")) {
                            player.getPlace().LiveFromDead(player);
                        } else if (text.equals("check")) {
                            player.sendAddchatYellow("MAP " + player.x + " " + player.y);
                        } else if (text.contains("u ")) {
                            short u = Short.parseShort(text.replace("u ", ""));
                            player.y += u;
                            player.zone.playerMove(player);
                        } else if (text.contains("id ")) {
                            int idimg = Integer.parseInt(text.replace("id ", ""));
                            Service.gI().LoadDeTuFollow(player, idimg);
                        }
                    } else {
                        if (text.contains("ten con la ") && player.havePet == 1) { //doi ten de tu
                            String namePet = text.replace("ten con la ", "");
                            namePet = namePet.replace(" ", "");
                            namePet = namePet.toLowerCase();
                            namePet = Util.validateString(namePet);
                            if (namePet.length() > 10) {
                                namePet = namePet.substring(0, 10);
                            }
                            if (namePet == "") {
                                namePet = "Đệ tử";
                            }
                            int indexItem = player.getIndexItemBagByID(400);//get index doi ten de tu;
                            if (player.ItemBag[indexItem].quantity > 0 && indexItem != -1) {
                                player.updateQuantityItemBag(indexItem, 1);
//                                player.removeItemBag(indexItem);
                                Service.gI().updateItemBag(player);
                                player.detu.name = namePet;
                                if (player.petfucus == 1) {
                                    player.zone.leaveDEEEEE(player.detu);
                                    player.statusPet = 0;
                                    player.zone.pets.add(player.detu);
                                    //NEU LOAD DE TU O MAP COOL
                                    if (player.map.MapCold()) {
                                        player.zone.upDownPointPETMapCool(player);
                                    }
                                    //NEU LOAD DE TU O MAP COOL
                                    for (Player _p : player.zone.players) {
                                        player.zone.loadInfoDeTu(_p.session, player.detu);
                                    }
                                }
                            }
                            player.zone.chat(player, text);
                            break;
                        }
                        //lenh chat status dt
                        if (text.contains("di theo") && player.havePet == 1 && player.NhapThe == 0 || text.contains("follow") && player.NhapThe == 0 && player.havePet == 1) {
                            player.petfucus = 1;
                            player.statusPet = 0;
                            Service.gI().LoadDeTuFollow(player, 1);
                            player.zone.chat(player.detu, "OK, con sẽ đi theo sư phụ!");

                            break;
                        }
                        if (text.contains("tan cong") && player.havePet == 1 && player.NhapThe == 0 || text.contains("attack") && player.havePet == 1 && player.NhapThe == 0) {
                            if (player.petfucus == 0) {
                                player.petfucus = 1;
                                Service.gI().LoadDeTuFollow(player, 1);
                            }
                            player.statusPet = 2;
                            player.zone.PetAttack(player, player.detu, player.statusPet);
                            player.zone.chat(player.detu, "OK, sư phụ cứ để con lo!");

                            break;
                        }
                        if (text.contains("bao ve") && player.havePet == 1 && player.NhapThe == 0 || text.contains("protect") && player.havePet == 1 && player.NhapThe == 0) {
                            if (player.petfucus == 0) {
                                player.petfucus = 1;
                                Service.gI().LoadDeTuFollow(player, 1);
                            }
                            player.statusPet = 1;
                            player.zone.PetAttack(player, player.detu, player.statusPet);
                            player.zone.chat(player.detu, "OK, con sẽ bảo vệ sư phụ!");

                            break;
                        }
                        if (text.equals("bien hinh") && player.detu != null && player.detu.isMabu) {
                            player.detu.transfMabu = !player.detu.transfMabu;
                            if (player.petfucus == 1) {
                                for (Player p : player.zone.players) {
                                    p.sendDefaultTransformToPlayer(player.detu);
                                }
                            }
                        }
                        if (player.Role != 0) {
                            if (player.name.equals("admin") && text.equals("admin")) {
                                player.menuID = -1;
                                player.menuNPCID = 999;
                                Menu.doMenuArray(player, 24, "MENU ADMIN", new String[]{"Call Boss", "Buff Item", "Check GiftCode"});
                                break;
                            }
                            if (text.contains("map ")) {
                                int mapIdx = Integer.parseInt(text.replace("map ", ""));

                                Map maptelex = MainManager.getMapid(mapIdx);
                                teleportToMAP(player, maptelex);
                            } else if (text.contains("toado")) {
                                player.zone.chat(player, "x: " + player.x + ",y: " + player.y);
                                break;
                            } else if (text.contains("wcache")) {
                                ItemData.reWriteCacheData();
                                break;
                            } else if (text.contains("kuku")) {
                                Service.gI().initKuKu();
                                break;
                            } else if (text.contains("onl")) {
                                player.sendAddchatYellow("Số người chơi đang online là: " + PlayerManger.gI().getPlayers().size());
                                break;
                            } else if (text.contains("btri")) {
                                ArrayList<Session> sessBaoTri = new ArrayList<>();

                                for (Player pbaotri : PlayerManger.gI().getPlayers()) {
                                    if (pbaotri.id != 1) {
                                        sessBaoTri.add(pbaotri.session);
                                    }
                                }
                                for (Session sbaotri : sessBaoTri) {
                                    PlayerManger.gI().kick(sbaotri);

                                }
                                break;
                            } else if (text.contains("item ")) {
                                int idItem = Integer.parseInt(text.replace("item ", ""));
                                Item itemBuff = ItemSell.getItemNotSell(idItem);
                                Item _itemBuff = new Item(itemBuff);
                                if (_itemBuff.template.id == 987) {
                                    _itemBuff.quantity = 99;
                                }
                                player.addItemToBag(_itemBuff);
                                Service.gI().updateItemBag(player);
                                break;
                            } else if (text.contains("eff ")) {
                                byte effe = Byte.parseByte(text.replace("eff ", ""));
                                //                            Util.log("effe: " + effe);
//                                Service.gI().sendEffectServer((byte)(-1), (byte)2, (byte)60, player.x, (short)(player.y+250), (short)1, player);
                                try {
                                    Message meff = new Message(-9);
                                    meff.writer().writeByte(0);
                                    meff.writer().writeInt(100);
                                    meff.writer().writeInt(0);
                                    meff.writer().writeBoolean(true);//flag
                                    //eff boss
                                    //5 khói
                                    meff.writer().writeByte(effe);
                                    meff.writer().flush();
                                    player.session.sendMessage(meff);
                                } catch (Exception e) {
                                }
                                break;
                            } else if (text.contains("dhvt")) {
                                Server.gI().isPassDHVT = !Server.gI().isPassDHVT;
                                if (Server.gI().isPassDHVT) {
                                    player.sendAddchatYellow("Đã mở skip DHVT");
                                } else {
                                    player.sendAddchatYellow("Đã đóng skip DHVT");
                                }
                                break;
                            } else if (text.contains("ctg")) {
                                Server.gI().isCTG = !Server.gI().isCTG;
                                if (Server.gI().isCTG) {
                                    player.sendAddchatYellow("Đã mở chat thế giới");
                                } else {
                                    player.sendAddchatYellow("Đã đóng chat thế giới");
                                }
                                break;
                            } else if (text.contains("bboss")) {
                                byte typeBoss = Byte.parseByte(text.replace("bboss", ""));
                                Boss _bossCall = new Boss(Server.gI().idBossCall, typeBoss, player.x, player.y);
                                if (typeBoss == (byte) 2) {
                                    Detu _rDetu = new Detu();
                                    _rDetu.initDetuBroly(_rDetu);
                                    _rDetu.id = -300000 - Server.gI().idBossCall;
                                    _rDetu.x = (short) (_bossCall.x - 100);
                                    _rDetu.y = (short) (_bossCall.y - 100);
                                    _bossCall.detu = _rDetu;
                                    player.zone.pets.add(_rDetu);
                                    for (Player _pz : player.zone.players) {
                                        player.zone.loadInfoDeTu(_pz.session, _rDetu);
                                    }
                                }
                                Server.gI().idBossCall++;

                                player.zone.bossMap.add(_bossCall);

                                if (typeBoss == (byte) 1 || typeBoss == (byte) 2) {
                                    player.zone.loadBROLY(_bossCall);
                                } else {
                                    player.zone.loadBossNoPet(_bossCall);
                                }
                                break;
                            } else if (text.contains("upclandb")) {
                                ClanService.gI().updateDataClanToDB();
                                player.sendAddchatYellow("Update data Clan to Database success");
                                for (int i = 0; i < PlayerManger.gI().conns.size(); i++) {
                                    if (PlayerManger.gI().conns.get(i) != null && PlayerManger.gI().conns.get(i).player != null) {
                                        Player playerSave = PlayerManger.gI().conns.get(i).player;
                                        if (playerSave != null) {
                                            PlayerDAO.updateDBAuto(playerSave, DataSource.connSaveData);
                                        }
                                    }
                                }
                                player.sendAddchatYellow("Update data Player to Database success");
                                break;
                            }
                        }
                        player.zone.chat(player, text);
                    }
                    break;
                case -34:
                    byte typeTree = m.reader().readByte();
                    if (typeTree == (byte) 2 && player.upMagicTree && player.lastTimeTree <= System.currentTimeMillis()) {
                        player.levelTree += (byte) 1;
                        player.upMagicTree = false;
                        player.maxBean += (byte) 2;
                        player.lastTimeTree = System.currentTimeMillis();
                    }
//                    if(typeTree == (byte)0) {
                    Service.gI().MagicTree(player, (byte) 0);
//                    }
                    break;
                //tấn công quái  

                case 54:
                    player.getPlace().FightMob(player, m);
                    break;
                //lay image item bay moi
                case 66:
                    String newMount = m.reader().readUTF();
//                    Util.log("item 66: " + newMount);
                    Service.gI().sendNewMount(_session, newMount);
                    break;
                // nhap
                case 88:
                    Draw.Draw(player, m);
                    break;
                // thong tin nguoi choi khac khi an hien len
                case -79:
                    int playerid = m.reader().readInt();
                    Player player2 = PlayerManger.gI().getPlayerByUserID(playerid);
                    Service.gI().PlayerMenu(player.session, player2);
                    break;
                // NOI TAI
                case 112:
                    if (!player.lockPK) {
                        byte _typeNoiTai = m.reader().readByte();
                        //                    Util.log("TYPE NOI TAI: " + _typeNoiTai);
                        if (player.power >= 10000000000L) {
                            if (_typeNoiTai == 0) { //MO UI NOI TAI
                                player.menuID = -1;
                                player.menuNPCID = 100;
                                Menu.doMenuArray(player, 24, "Nội tại là một kĩ năng bị động hỗ trợ đặc biệt\nBạn có muốn mở hoặc thay đổi không ?", new String[]{"Xem tất cả \nNội tại", "Mở nội tại", "Mở nội tại\nVIP", "Từ chối"});
                            }
                        } else {
                            player.sendAddchatYellow("Cần 10 Tỷ sức mạnh để mở nội tại");
                        }
                    } else {
                        player.sendAddchatYellow("Không thể thực hiện");
                    }
//                    try{
//                        server.menu.openUINpc(player, m);
//                    }catch (Exception e) {
//                        e.printStackTrace();
//                    }
                    break;
                case 126:
                    String androidpack = m.reader().readUTF();
//                    Util.log("ANDROID PACK: " + androidpack);
                    break;
                //RADA CARD
                case 127:
                    if (!player.lockPK) {
                        byte typerada = m.reader().readByte();
//                        Util.log("typerada: " + typerada);
                        short idrada = -1;
//                        if(m.reader().available() > 0) {
//                            short idrada = m.reader().readShort();
//                            Util.log("idrada: " + idrada);
//                        }
                        if (typerada == (byte) 0) {
                            RadaCardService.gI().readRadaCard(player);
                        } else if (typerada == (byte) 1) {
                            idrada = m.reader().readShort();
//                            Util.log("idrada: " + idrada);
                            RadaCardService.gI().setUseCard(player, idrada);
                        }
                    } else {
                        player.sendAddchatYellow("Không thể thực hiện");
                    }
                    break;
                default:
//                    Util.log("Tinh Nang CMD chua mo: " + cmd);
                    break;
            }
        } catch (Exception e) {
        }
        if (m != null) {
            m.cleanup();
        }
    }

    public void UseItemHandler(Session s, Player p, byte typeUse, byte where, byte index, short idtemp) {
        try {
            if (where == 0) {
                if (typeUse == 0) {
                } else if (typeUse == 1) {
                    Service.gI().DropDoneBody(p, typeUse, "Bạn có chắc chắn muốn vứt bỏ (mất luôn)\n" + p.ItemBody[index].template.name + " ?", index, where);
//                    Service.gI().DropDone(p,typeUse,"Bạn có chắc chắn muốn hủy bỏ (mất luôn)\n" + p.ItemBag[index].template.name +" ?",index);
                }
                if (typeUse == 2) {
                    p.removeItemBody(index);
                    Service.gI().updateItemBody(p);
                    Service.gI().loadCaiTrangTemp(p);
                }
            } else if (where == 1) {
                if (typeUse == 0) {
                    if (index == -1 && !p.isdie) { // use dau than
                        for (int i = 0; i < p.ItemBag.length; i++) {
                            if (p.ItemBag[i] != null && p.ItemBag[i].template.id == idtemp) {
                                long _TIMENOW = System.currentTimeMillis();
                                if (p.ItemBag[i].quantity > 0 && ((_TIMENOW - p._TIMEBUFFDAU) > 10000)) {
                                    p._TIMEBUFFDAU = _TIMENOW;
                                    int hpbuff = p.ItemBag[i].getParamItemByID(48);
                                    if (hpbuff == 0) {
                                        hpbuff = p.ItemBag[i].getParamItemByID(2) * 1000;
                                    }
                                    p.hp = (p.hp + hpbuff) > p.getHpFull() ? p.getHpFull() : (p.hp + hpbuff);
                                    p.mp = (p.mp + hpbuff) > p.getMpFull() ? p.getMpFull() : (p.mp + hpbuff);

                                    if (p.havePet == 1 && !p.detu.isdie) {
                                        p.detu.hp = (p.detu.hp + hpbuff) > p.detu.getHpFull() ? p.detu.getHpFull() : (p.detu.hp + hpbuff);
                                        p.detu.mp = (p.detu.mp + hpbuff) > p.detu.getMpFull() ? p.detu.getMpFull() : (p.detu.mp + hpbuff);
                                        p.detu.stamina = (short) (p.detu.stamina + (short) (p.ItemBag[i].template.level) * 100) > (short) 1000 ? (short) 1000 : (short) (p.detu.stamina + (short) (p.ItemBag[i].template.level) * 100);
                                        p.updateHpDetu(p, p.detu);
                                        if (p.petfucus == 1 && p.statusPet != 3 && p.statusPet != 4) {
                                            p.zone.chat(p.detu, "Con cám ơn sư phụ");
                                        }
                                    }
                                    p.updateQuantityItemBag(i, 1);
//                                    p.ItemBag[i].quantity-=1;
                                    Service.gI().loadPoint(p.session, p);
//                                    Service.gI().updateItemBag(p);
                                }
                            }
                        }
                        return;
                    }
                    Service.gI().UseItem(p, index, idtemp);
                } else if (typeUse == 1) {
                    if (p.ItemBag[index] != null) {
                        Service.gI().DropDone(p, typeUse, "Bạn có chắc chắn muốn vứt bỏ (mất luôn)\n" + p.ItemBag[index].template.name + " ?", index);
                    }
                }
                if (typeUse == 2) {
                    p.removeItemBag((int) index);
                    Service.gI().updateItemBag(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void teleportToMAP(Player p, Map map) {
        byte index = -1;
        ArrayList<Byte> listZone = new ArrayList<>();
        for (byte j = 0; j < map.area.length; j++) {
            if (map.area[j].players.size() < map.template.maxplayers) {
                listZone.add(j);
                if (index == (byte) (-1)) {
                    index = j;
                }
//                break;
            }
        }
        if (index != (byte) (-1)) {
            //leave suphu va de tu khoi map
            if (p.petfucus == 1) {
                p.zone.leaveDetu(p, p.detu);
            }
            if (p.pet2Follow == 1 && p.pet != null) {
                p.zone.leavePETTT(p.pet);
            }
            p.zone.leave(p);
            if (map.id == 53) {
                //INIT DOANH TRAI LUC MO
                if (!p.clan.openDoanhTrai) {
//                if(p.petfucus == 1) {
//                    p.zone.leaveDetu(p, p.detu);
//                }
//                p.zone.leave(p);
                    p.x = (short) 102;
                    p.y = (short) 432;
                    //INIT MAP DOANH TRAI MOI
                    Service.gI().initMapDoanhTrai(p.clan, 53, (byte) 0);

                    //INIT HP, DAME MOB
                    if (p.clan.doanhTrai[0].area[0].mobs.size() > 0) {
                        for (Mob _mob : p.clan.doanhTrai[0].area[0].mobs) {
                            _mob.maxHp = p.getDamFull() * 6;
                            _mob.hp = _mob.maxHp;
                        }
                    }

                    p.clan.doanhTrai[0].area[0].Enter(p);
//                p.clan.doanhTrai.get(0).area[0].Enter(p);
                } else if (p.clan.openDoanhTrai && p.clan.doanhTrai[0] != null) { //VAO DOANH TRAI KHI DA MO
//                if(p.petfucus == 1) {
//                    p.zone.leaveDetu(p, p.detu);
//                }
//                p.zone.leave(p);
                    p.x = (short) 102;
                    p.y = (short) 432;

                    //INIT HP, DAME MOB
                    int hpMobNew = p.getDamFull() * 6;
                    if (p.clan.doanhTrai[0].area[0].mobs.size() > 0) {
                        if (hpMobNew > p.clan.doanhTrai[0].area[0].mobs.get(0).maxHp) {
                            for (Mob _mob : p.clan.doanhTrai[0].area[0].mobs) {
                                _mob.maxHp = hpMobNew;
                                _mob.hp = _mob.maxHp;
                            }
                        }
                    }

                    p.clan.doanhTrai[0].area[0].Enter(p);

                }
            } else if (map.id == 153) { //KHU VUC BANG HOI
//            p.zone.leave(p);
//            map.getPlayers().add(p);
                p.map = map;
                p.x = (short) 74;
                p.y = (short) 432;
                p.map.area[index].Enter(p);
            } else if (map.id == 156) { //TAY THANH DIA
//            p.zone.leave(p);
//            map.getPlayers().add(p);
                p.map = map;
                p.x = (short) 884;
                p.y = (short) 360;
                p.map.area[index].Enter(p);
            } else if (map.id == 160) { //KHU HANG DONG
//            p.zone.leave(p);
//            map.getPlayers().add(p);
                p.map = map;
                p.x = (short) 712;
                p.y = (short) 168;
                p.map.area[index].Enter(p);
            } else if (map.id == 114 || map.id == 115 || map.id == 117 || map.id == 118 || map.id == 119 || map.id == 120) { //MABU 12h
//            p.zone.leave(p);
//            map.getPlayers().add(p);
                byte indexR = (byte) Util.nextInt(0, listZone.size());
                p.map = map;
                p.x = (short) 352;
                p.y = (short) 336;
                p.map.area[indexR].Enter(p);
                p.pointMabu = (byte) 0;
                Service.gI().setPowerPoint(p, "TL", (short) 0, (short) 10, (short) 10);
//            if(map.id == 120) {
//                Service.gI().setTrungMabuPoint(p);
//            }
            } else if (map.id == 123) { //NGU HANH SON
//            p.zone.leave(p);
//            map.getPlayers().add(p);
                p.map = map;
                p.x = (short) 109;
                p.y = (short) 384;
                p.map.area[index].Enter(p);
            } else {
//            p.zone.leave(p);
//            map.getPlayers().add(p);
                p.map = map;
                for (WayPoint ww : p.map.template.wayPoints) {
                    for (Npc n : p.map.template.npcs) {
                        if (n != null) {
                            p.x = (short) n.cx;
                            p.y = (short) n.cy;
                        } else {
                            p.x = (short) (ww.maxX + ww.goX);
                            p.y = ww.maxY;
                        }
                    }
                }
                p.map.area[index].Enter(p);
            }
        } else {
            //leave suphu va de tu khoi map
            if (p.petfucus == 1) {
                p.zone.leaveDetu(p, p.detu);
            }
            if (p.pet2Follow == 1 && p.pet != null) {
                p.zone.leavePETTT(p.pet);
            }
            p.zone.leave(p);

            p.map.area[Util.nextInt(0, map.area.length)].Enter(p);
//            p.sendAddchatYellow("Bản đồ quá tải");
        }
    }

    public void teleToPlayer(Player p, Player pGoto) {
        if ((pGoto.map.id >= 84 && pGoto.map.id <= 91) || (pGoto.map.id >= 114 && pGoto.map.id <= 120) || pGoto.map.id == 147 || pGoto.map.id == 148 || pGoto.map.id == 149
                || pGoto.map.id == 151 || pGoto.map.id == 152 || (pGoto.map.id >= 53 && pGoto.map.id <= 62) || (pGoto.map.id >= 21 && pGoto.map.id <= 23)) {
            p.sendAddchatYellow("Không thể thực hiện");
        } else {
            if (Service.gI().checkTaskGoMap(p, pGoto.map.id)) {
                if (pGoto.zone.players.size() < pGoto.map.template.maxplayers) {
                    //leave suphu va de tu khoi map
                    if (p.petfucus == 1) {
                        p.zone.leaveDetu(p, p.detu);
                    }
                    if (p.pet2Follow == 1 && p.pet != null) {
                        p.zone.leavePETTT(p.pet);
                    }
                    p.zone.leave(p);

                    p.map = pGoto.map;
                    p.x = pGoto.x;
                    p.y = pGoto.y;
                    pGoto.zone.Enter(p);
                } else {
                    p.sendAddchatYellow("Bản đồ quá tải");
                }
            } else {
                p.sendAddchatYellow("Vui lòng hoàn thành nhiệm vụ trước khi tới đây!");
            }
        }
    }

    public void messageNotLogin(Session session, Message msg) {
        if (msg != null) {
            try {
                byte cmd = msg.reader().readByte();
                //  Util.log("messageNotLogin TYPE: " + cmd);
                switch (cmd) {
                    case 0:
                        login(session, msg);
                        break;
                    case 2:
                        session.setClientType(msg);
                        break;
                    default:
                        //  Util.log("messageNotLogin: " + cmd);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void messageNotMap(Session _session, Message _msg) {
        if (_msg != null) {
            try {
                Player player = PlayerManger.gI().getPlayerByUserID(_session.userId);
                byte cmd = _msg.reader().readByte();
                //  Util.log("messageNotMap TYPE: " + cmd);
                switch (cmd) {
                    case 2:
                        createChar(_session, _msg);
                        break;
                    // send data map
                    case 6:
//                        Manager.sendMap(player);
                        MainManager.sendMapv2(_session);
                        break;
                    // send data skill
                    case 7:
//                        Manager.sendSkill(player);
                        MainManager.sendSkillv2(_session);
                        break;
                    // send data item
                    case 8:
//                        Manager.sendItem(player);
                        MainManager.sendItemv2(_session);
                        break;
                    case 10:
//                        player.zone.AddItemGa();
                        Service.gI().mapTemp(_session, player.map.getId());
                        break;
                    case 13:
                        player.update();
                        break;
                    default:
                        //        Util.log("messageNotMap: " + cmd);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void messageSubCommand(Session _session, Message _msg) {
        if (_msg != null) {
            try {
                Player player = PlayerManger.gI().getPlayerByUserID(_session.userId);
                byte command = _msg.reader().readByte();
                switch (command) {
                    case 5:

                    case 16:
                        if (!player.lockPK) {
                            byte type = _msg.reader().readByte();
                            short point = _msg.reader().readShort();
                            player.increasePoint(type, point);
                        } else {
                            player.sendAddchatYellow("Không thể thực hiện");
                        }
                        break;
                    case 63:
                        Service.gI().GET_PLAYER_MENU(player);
                        break;
                    default:
                        Util.log("messageSubCommand: " + command);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void Send_ThongBao(Session session) {
        try {
            Message msg;
            msg = new Message(-70);
            msg.writer().writeShort(6101);
            msg.writer().writeUTF("Nếu Lỗi mất hành trang hãy thoát ra vào lại hoặc xoá dữ liệu!!!!");
            msg.writer().writeByte(0);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {

        }
    }

    public void login(Session session, Message msg) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String user = msg.reader().readUTF();
            String pass = msg.reader().readUTF();
            msg.reader().readUTF();
            msg.reader().readByte();
            Service.gI().updateVersion(session); // -28 _4
            // -77 SMALLIMAGE_VERSION
//            Service.gI().sendMessage(session, -77, "20220731121832313_-77_r"); //v219
//            Service.gI().sendMessage(session, -77, "v222_-77_r"); //v222
            Service.gI().sendMessage(session, -77, "vhalloween_-77_r"); //vhalloween
            // -93 BGITEM_VERSION
            Service.gI().sendMessage(session, -93, "20220809231949044_-93_r");
//            Service.gI().updateVersion(session); // -28 _4
            MainManager.sendDatav2(session); //-87 send data

//            conn = DataSource.connLogin;
            conn = DataSource.getConnectionLogin();
            pstmt = conn.prepareStatement("select * from account where username=? and password=? limit 1");
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                byte havechar = rs.getByte("havechar");
                byte isOn = rs.getByte("isOn");
                if (isOn == Server.gI().isServer || isOn == 0) {
                    //                session.nhanvat = rs.getString("nhanvat").toLowerCase();
                    session.taikhoan = rs.getString("username").toLowerCase();
                    session.matkhau = rs.getString("password").toLowerCase();
                    session.userId = rs.getInt("id");
                    //                if(havechar == 1) {
                    Player p = PlayerManger.gI().getPlayerByUserID(session.userId);
                    if (rs.getBoolean("ban")) {
                        Service.gI().serverMessage(session, "Tài khoản đã bị khóa vui lòng liên hệ với ADMIN để biết thêm chi tiết");
                    } else if (!Util.CheckString(user, "^[a-zA-Z0-9]+$") || !Util.CheckString(pass, "^[a-zA-Z0-9]+$")) {
                        Service.gI().serverMessage(session, "Vui Lòng Đăng Kí Trệ Trang Chủ http://nrolau.xyz!");
                    }// else if (session.userId != 1 &&  session.userId != 3 &&  session.userId != 2 &&  session.userId != 5) {
                       // Service.gI().serverMessage(session, "máy chủ đang bảo trì");
                   // } 
else if (p != null) {
                        //                        this.logout(p.session);
                        PlayerManger.gI().kick(p.session);

                        Service.gI().serverMessage(session, "Bạn đang đăng nhập trên thiết bị khác");
                    } else {
                        pstmt = conn.prepareStatement("select * from player where account_id=? limit 1");
                        pstmt.setInt(1, session.userId);
                        msg.cleanup();
                        rs = pstmt.executeQuery();
                        if (rs.first()) {
                            this.LogHistory.log1(String.format("User: %s - Pass: %s - UserId: %s - IP: %s Login successful", new Object[]{session.taikhoan, session.matkhau, session.userId, session.getIP()}));
                            this.LogHistory.log(String.format("User: %s - Pass: %s - UserId: %s Login successful", new Object[]{session.taikhoan, session.matkhau, session.userId}));
                            pstmt = conn.prepareStatement("UPDATE ACCOUNT SET isOn=? WHERE id=?");
                            conn.setAutoCommit(false);
                            pstmt.setInt(1, (int) Server.gI().isServer);
                            pstmt.setInt(2, session.userId);
                            if (pstmt.executeUpdate() == 1) {
                            }
                            conn.commit();

                            Player player = Player.setup(session.userId);
                            PlayerManger.gI().getPlayers().add(player);
                            player.session = session;

                            //add them update version
                            Service.gI().updateVersion(session);

                            Service.gI().itemBg(session, 0); ///-31
                            sendInfo(session);
                            //SEND ITEM USE DANG SU DUNG DO
                            Service.gI().sendItemBuff(player);
                            //SEND COOLDOWN SKILL
                            Service.gI().coolDownAllSkill(player);
                            //SEND THONG TIN TAO BANG
                            ClanService.gI().clanIconImageInfo(session, (byte) 1);
                            ClanService.gI().clanIconImage(session);
                            //INIT ITEM SAU LUNG
                            if (player.clan != null) {
                                ClanService.gI().sendMessageClanToMe(player);
                                ClanService.gI().clanInfo(session, player);
                                Service.gI().updateBagNew(session, player.id, player.clan.imgID);
                                //GET BAG SAU LUNG
                                ClanService.gI().getBagBangNew(session, player.clan.imgID);
                            } else {
                                //UPDATE BAG SAU LUNG
                                Service.gI().updateBagNew(session, player.id, (byte) (-1));
                                //GET BAG SAU LUNG
                                ClanService.gI().getBagBangNew(session, (byte) (-1));
                            }
                            if (player.ItemBody[8] != null) {
                                if (player.imgNRSD != (byte) 53 && player.imgNRSD != (byte) 37) {
                                    Service.gI().setItemBagNew(player, player.ItemBody[8].id);
                                }
                            }
                            if (player.ItemBody[7] != null) {
                                Service.gI().initPet2(player, player.ItemBody[7].id);
                            }
                            //effchat marron
                            if (player.ItemBody[5] != null && player.ItemBody[5].template.id == 1103) {
                                ItemService.gI().sendEffectChat(player);
                            }
                            if (player.detu != null && player.detu.isdie) {
                                Service.gI().petComebackLogin(player);
                            }
                            if (player.idAura == (short) 0) {
//                                    RadaCardService.gI().sendAuraCard(player, (short)1);
                                RadaCardService.gI().sendAuraCard(player, (short) 0);
                            }
                            player.sendAddchatYellow("Nếu Lỗi mất hành trang hãy thoát ra vào lại hoặc xoá dữ liệu!");
                            this.Send_ThongBao(session);
                            PlayerManger.gI().put(session);
                            PlayerManger.gI().put(player);
                            session.player = player;
                            Service.gI().loadCaiTrangTemp(player);
                            Service.gI().LoadDeTu(player, player.havePet);
                            player.isLogin = true;
                        } else {
                            //                            Service.gI().itemBg(session, 0);
                            //                            sendInfoCreate(session);
                            //                            Service.gI().sendMessage(session, -28, "1630679754226_-28_4_r");
                            Service.gI().sendMessage(session, -31, "1631370772604_-31_r");
                            Service.gI().sendMessage(session, -82, "1631370772610_-82_r");
                            try {
                                Message m = new Message(-89);
                                m.writer().writeByte(1);
                                m.writer().flush();
                                session.sendMessage(m);
                                m.cleanup();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Service.gI().sendMessage(session, -28, "1630679754226_-28_4_r");
                            //                            Manager.sendMapv2(session);
                            //                            Manager.sendSkillv2(session);
                            //                            Manager.sendItemv2(session);
                            Service.gI().sendMessage(session, 2, "1631370772901_2_r");
                        }
                    }
                } else {
                    Service.gI().serverMessage(session, "Bạn đang đăng nhập Server khác");
                }
            } else {
                Service.gI().serverMessage(session, "Tài khoản mật khẩu không chính xác !");
            }
            if (!DataSource.flagLogin) {
                conn.close();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    synchronized public void createChar(Session session, Message msg) {
//        Connection conn = DBService.gI().getConnection();

        PreparedStatement pstmt = null;
        //String CREATE_NHANVAT = "UPDATE account SET nhanvat==? WHERE id=?)";
        Service.gI().updateVersion(session); // -28 _4
        // -77 SMALLIMAGE_VERSION
//        Service.gI().sendMessage(session, -77, "20220731121832313_-77_r"); //v219
//        Service.gI().sendMessage(session, -77, "v222_-77_r"); //v222
        Service.gI().sendMessage(session, -77, "vhalloween_-77_r"); //vhalloween
        // -93 BGITEM_VERSION
//        Service.gI().sendMessage(session, -93, "1630679752231_-93_r");
        Service.gI().sendMessage(session, -93, "20220809231949044_-93_r");
        MainManager.sendDatav2(session); //-87 send data
        try {
//            Connection conn = DataSource.connLogin;
            Connection conn = DataSource.getConnectionCreate();
            String name = msg.reader().readUTF();
            name = name.toLowerCase();
            name = Util.validateString(name);
            if (name != "") {
                int gender = msg.reader().readByte();
                int head = msg.reader().readByte();
                if (gender > 2 || gender < 0) {
                    session.player.sendAddchatYellow("Hành tinh lựa chòn không hợp lệ !");
                }
                pstmt = conn.prepareStatement("SELECT * FROM `player` WHERE name=?");
                pstmt.setString(1, name);
                ResultSet rs = pstmt.executeQuery();
                if (!rs.first()) {
                    if (PlayerDAO.create(session, name, (int) gender, (int) head)) {
                        session.player = Player.setup(session.userId);
                        PlayerManger.gI().getPlayers().add(session.player);
                        session.player.session = session;
                        Service.gI().updateVersion(session);
                        Service.gI().itemBg(session, 0);
                        sendInfo(session);
                        session.player.sendAddchatYellow("Chào mừng " + session.player.name + " đến với thế giới ngọc rồng");
                        PlayerManger.gI().put(session);
                        PlayerManger.gI().put(session.player);
                        //                    session.player = session.player;

                    }
                } else {
                    Service.gI().serverMessage(session, "Tên đã tồn tại");
                }
            } else {
                Service.gI().serverMessage(session, "Tên không hợp lệ");
            }
            if (!DataSource.flagCreate) {
                conn.close();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

    }

    // STRING LEVEL CUA TUNG HANH TINH
    public void sendCaptionLevel(Session session, byte gender) {
        Message m = null;
        try {
            m = new Message(-41);
//            m.writer().writeByte(15); //SO LUONG CAPTION
            m.writer().writeByte(20);
            m.writer().writeUTF("Tân Thủ");
            m.writer().writeUTF("Tập Sự Sơ Cấp");
            m.writer().writeUTF("Tập Sự Trung Cấp");
            m.writer().writeUTF("Tập Sự Cao Cấp");
            m.writer().writeUTF("Tân Binh");
            m.writer().writeUTF("Chiến Binh");
            m.writer().writeUTF("Chiến Binh Cao Cấp");
            m.writer().writeUTF("Vệ Binh Hoàng Gia");
            if (gender == 0) {
                m.writer().writeUTF("Siêu Nhân Cấp 1");
                m.writer().writeUTF("Siêu Nhân Cấp 2");
                m.writer().writeUTF("Siêu Nhân Cấp 3");
                m.writer().writeUTF("Siêu Nhân Cấp 4");
                m.writer().writeUTF("Thần Trái Đất Cấp 1");
                m.writer().writeUTF("Thần Trái Đất Cấp 2");
                m.writer().writeUTF("Thần Trái Đất Cấp 3");

            } else if (gender == 1) {
                m.writer().writeUTF("Siêu Namếc Cấp 1");
                m.writer().writeUTF("Siêu Namếc Cấp 2");
                m.writer().writeUTF("Siêu Namếc Cấp 3");
                m.writer().writeUTF("Siêu Namếc Cấp 4");
                m.writer().writeUTF("Thần Namếc Cấp 1");
                m.writer().writeUTF("Thần Namếc Cấp 2");
                m.writer().writeUTF("Thần Namếc Cấp 3");
            } else {
                m.writer().writeUTF("Siêu Xayda Cấp 1");
                m.writer().writeUTF("Siêu Xayda Cấp 2");
                m.writer().writeUTF("Siêu Xayda Cấp 3");
                m.writer().writeUTF("Siêu Xayda Cấp 4");
                m.writer().writeUTF("Thần Xayda Cấp 1");
                m.writer().writeUTF("Thần Xayda Cấp 2");
                m.writer().writeUTF("Thần Xayda Cấp 3");
            }
            m.writer().writeUTF("Thần Hủy Diệt Cấp 1");
            m.writer().writeUTF("Thần Hủy Diệt Cấp 2");
            m.writer().writeUTF("Giới Vương Thần Cấp 1");
            m.writer().writeUTF("Giới Vương Thần Cấp 2");
            m.writer().writeUTF("Giới Vương Thần Cấp 3");
            session.sendMessage(m);
            m.cleanup();
        } catch (Exception var2) {
            var2.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void login2(Session session) {
        String user = "User" + Util.nextInt(2222222, 8888888);
//        Connection conn = DBService.gI().getConnection();

        try {
            Connection conn = DataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO account(username,password,nhanvat) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user);
            ps.setString(2, "");
            ps.setBoolean(3, true);
            if (ps.executeUpdate() == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.first()) {
                    Service.gI().login2(session, user);

                }
            } else {
                Service.gI().serverMessage(session, "Có lỗi vui lòng thử lại");
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("lỗi server.Controller.login2()");
        }
    }

    public void sendInfo(Session session) {
        Player player = PlayerManger.gI().getPlayerByUserID(session.userId);
        //send tileset
        Service.gI().tileSetReal(session); //-82

        sendNoiTaiHienTai(session, player); //112 noi tai

        Service.gI().loadPoint(session, player); //-42 load point
        //SEND NHIEM VU HIEN TAI
        TaskService.gI().loadCurrentTask(session, player.crrTask); //40 load task
        Service.gI().clearMap(session); //-22 clear map
        Service.gI().loadPlayer(session, player); //LOAD THONG TIN CHAR -30
        if (player.clan != null) {

        }
        Service.gI().sendMessage(session, -69, "1630679754701_-69_r");
        // -68 STAMINA
        Service.gI().sendMessage(session, -68, "1630679754708_-68_r");
        // -80 FRIEND
        Service.gI().sendMessage(session, -80, "1630679754715_-80_r");
        // -97 UPDATE_ACTIVEPOINT
        Service.gI().sendMessage(session, -97, "1630679754722_-97_r");
        // -107 PET_INFO
//        Service.gI().sendMessage(session, -107, "1630679754733_-107_r");
        // -119 THELUC
        Service.gI().sendMessage(session, -119, "1630679754740_-119_r");
        // -113 CHANGE_ONSKILL
//        Service.gI().sendMessage(session, -113, "1630679754747_-113_r");
        sendSkillDefault(session, player);
        // 50 GAME_INFO
        Service.gI().sendMessage(session, 50, "1630679754755_50_r");

        for (Map map : server.maps) {
            if (map.id != player.map.id) {
                continue;
            }
            for (int i = 0; i < map.area.length; i++) {
                if (map.area[i].players.size() < map.template.maxplayers) {
                    map.area[i].Enter(player);
                    return;
                }
            }
        }
    }

    public void sendInfoCreate(Session session) {
//        Player player = PlayerManger.gI().getPlayerByUserID(session.userId);
//        Service.gI().tileSet(session, player.map.id);
//        Service.gI().sendMessage(session, 112, "1630679754607_112_r");
        Message m = null;
        try {
            m = new Message(112);
            m.writer().writeByte(0); //TYPE
            m.writer().writeShort((short) 5223);
            m.writer().writeUTF("Chưa có nội tại");
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
//        Service.gI().loadPoint(session, player);
        Service.gI().sendMessage(session, 40, "1630679754622_40_r");
        Service.gI().clearMap(session);
//        Service.gI().loadPlayer(session, player);
        Service.gI().sendMessage(session, -69, "1630679754701_-69_r");
        // -68 STAMINA
        Service.gI().sendMessage(session, -68, "1630679754708_-68_r");
        // -80 FRIEND
        Service.gI().sendMessage(session, -80, "1630679754715_-80_r");
        // -97 UPDATE_ACTIVEPOINT
        Service.gI().sendMessage(session, -97, "1630679754722_-97_r");
        // -107 PET_INFO
//        Service.gI().sendMessage(session, -107, "1630679754733_-107_r");
        // -119 THELUC
        Service.gI().sendMessage(session, -119, "1630679754740_-119_r");
        // -113 CHANGE_ONSKILL
//        Service.gI().sendMessage(session, -113, "1630679754747_-113_r");
//        sendSkillDefault(session, player);
        // 50 GAME_INFO
        Service.gI().sendMessage(session, 50, "1630679754755_50_r");

//        for (Map map : server.maps) {
//            if (map.id != player.map.id) {
//                continue;
//            }
//            for (int i = 0; i < map.area.length; i++) {
//                if (map.area[i].players.size() < map.template.maxplayers) {
//                    map.area[i].Enter(player);
//                    return;
//                }
//            }
//        }
    }

    //SEND DEFAULT SKILL LUC DAU DANG NHAP VAO GAME
    public void sendSkillDefault(Session session, Player player) {
        int num = player.listSkill.size();
        num = num > 10 ? 10 : num;
        Message m = null;
        try {
            m = new Message(-113);
            if (num > 10) {
                for (int i = 0; i < 10; i++) {
                    m.writer().writeByte(player.listSkill.get(i).skillId);
                }
            } else {
                for (int i = 0; i < num; i++) {
                    m.writer().writeByte(player.listSkill.get(i).skillId);
                }
                for (int i = num; i < 10; i++) {
                    m.writer().writeByte(-1);
                }
            }
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

    //SEND NOI TAI HIEN TAI
    public void sendNoiTaiHienTai(Session session, Player player) {
        Message m = null;
        try {
            m = new Message(112);
            m.writer().writeByte(0); //TYPE
            if (player.noiTai.id == 0) {
                m.writer().writeShort((short) 5223);
                m.writer().writeUTF("Chưa có nội tại");
            } else {
                m.writer().writeShort(player.noiTai.idIcon);
                m.writer().writeUTF(player.noiTai.infoHead + player.noiTai.param + player.noiTai.infoFoot);
            }
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

    public void logout(Session session) {
        if (session.player != null) {
            RadaCardService.gI().setCardLogout(session.player, (short) 956);
            if (session.player.havePet == 1 && session.player.statusPet != 3) {
                if (session.player.detu.isMonkey) {
                    session.player.detu.isMonkey = false;
                }
                session.player.statusPet = 3;
                session.player.petfucus = 0;
                session.player.zone.leaveDetu(session.player, session.player.detu);
            }
            if (session.player.pet2Follow == 1 && session.player.pet != null) {
                session.player.zone.leavePETTT(session.player.pet);
            }
            //CHECK NEU SU DUNG MAYDO CAPSULE MA OUT GAME
            if (session.player.useMayDoCapsule) {
                session.player.useMayDoCapsule = false;
            }
            //CHECK NEU DANG SU DUNG TU DONG LUYEN TAP THI TRA LAI TIME
            if (session.player.timeEndTDLT > 0) {
                int _timeConLai = (int) ((session.player.timeEndTDLT - System.currentTimeMillis()) / 60000);
//                session.player.timeEndTDLT = 0;
                //GET TDLT TRONG BAG
                int iTDLT = session.player.getIndexItemBagByID(521);
                if (iTDLT != -1) {
                    //UPDATE LAI TIME
                    for (byte i = 0; i < session.player.ItemBag[iTDLT].itemOptions.size(); i++) {
                        if (session.player.ItemBag[iTDLT].itemOptions.get(i).id == 1) {
                            session.player.ItemBag[iTDLT].itemOptions.get(i).param = _timeConLai;
                        }
                    }
                }

//                Service.gI().updateItemBag(p);
            }
            //NEU DANG TROI DO THI HUY TROI
            if (session.player.isTroi == true) {
                session.player.zone.resetTROIKHIMOVE(session.player);
            }
            Map map = session.player.map;
            if (!session.player.isdie) {
                if ((map.template.id >= 85 && map.template.id <= 91) || (map.template.id >= 53 && map.template.id <= 62) || map.template.id == 126) {
                    session.player.map = MainManager.getMapid((int) (session.player.gender + 24));
                    session.player.x = (short) 124;
                    session.player.y = (short) 336;
                } else if (map.template.id >= 114 && map.template.id <= 120) { //MABU 12h thi ve DHVT
                    session.player.map = MainManager.getMapid(52);
                    session.player.x = (short) 191;
                    session.player.y = (short) 336;
                } else if (map.template.id >= 147 && map.template.id <= 152 && map.template.id != 150) {
                    session.player.map = MainManager.getMapid(0);
                    session.player.x = (short) 530;
                    session.player.y = (short) 432;
                }
                //NEU DEO NGOC RONG SAO DEN THI ROT RA DAT
                Service.gI().dropDragonBall(session.player);
            } else {
                session.player.map = MainManager.getMapid((int) ((int) session.player.gender + 21));
                int _rdLocation = Util.nextInt(0, (session.player.map.template.npcs.length - 1)); //get index npc random
                session.player.x = (short) (session.player.map.template.npcs[_rdLocation].cx);
                session.player.y = (short) 10;
            }
            PlayerDAO.updateDB(session.player);
            //CHECK NEU CO TIMER TASK THI HUY ALL
            if (session.player.timerCSKB != null) {
                session.player.timerCSKB.cancel();
                session.player.timerCSKB = null;
            }
            if (session.player.timerCN != null) {
                session.player.timerCN.cancel();
                session.player.timerCN = null;
            }
            if (session.player.timerBH != null) {
                session.player.timerBH.cancel();
                session.player.timerBH = null;
            }
            if (session.player.timerBK != null) {
                session.player.timerBK.cancel();
                session.player.timerBK = null;
            }
            if (session.player.timerGX != null) {
                session.player.timerGX.cancel();
                session.player.timerGX = null;
            }
            if (session.player.timerDeTrung != null) {
                session.player.timerDeTrung.cancel();
                session.player.timerDeTrung = null;
            }
            if (session.player.timerDHVT != null) {
                session.player.timerDHVT.cancel();
                session.player.timerDHVT = null;
                session.player._friendGiaoDich = null;
            }
            if (session.player.timerTM != null) {
                session.player.timerTM.cancel();
                session.player.timerTM = null;
            }
            if (session.player.timerTA != null) {
                session.player.timerTA.cancel();
                session.player.timerTA = null;
            }
            if (session.player.timerStone != null) {
                session.player.timerStone.cancel();
                session.player.timerStone = null;
            }
            if (session.player.timerGLT != null) {
                session.player.timerGLT.cancel();
                session.player.timerGLT = null;
            }
            if (session.player.timeTauNgam != null) {
                session.player.timeTauNgam.cancel();
                session.player.timeTauNgam = null;
            }
            if (session.player.timerHSDe != null) {
                session.player.timerHSDe.cancel();
                session.player.timerHSDe = null;
            }
            if (session.player.havePet == (byte) 1 && session.player.detu != null) {
                if (session.player.detuUpPoint != null) {
                    session.player.detuUpPoint.cancel();
                    session.player.detuUpPoint = null;
                }
                if (session.player.detuAttack != null) {
                    session.player.detuAttack.cancel();
                    session.player.detuAttack = null;
                }
            }
            if (session.player.timerEffectChat != null) {
                session.player.timerEffectChat.cancel();
                session.player.timerEffectChat = null;
            }
//            session.player.timer.cancel();
            session.player.zone.leave(session.player); // send remove player to all
            //CHECK NEU DANG CON DE TRUNG THI REMOVE DE TRUNG
            if (session.player.chimFollow == (byte) 1) {
                session.player.chimFollow = (byte) 0;
                session.player.dameChim = 0;
//                session.player.timerDeTrung.cancel();
//                session.player.timerDeTrung = null;
            }
//            if(session.player.petfucus == 1) {
//                session.player.petfucus = 0;
//                session.player.zone.leaveDetu(session.player, session.player.detu); // send remove detu to all
//            }
//            session.player.zone.exitMap(session.player, map);
//            session.player.zone.players.remove(session.player);
            session.player.map = null;
//            if(session.player.setSession != null) {
//                session.player.setSession.cancel();
//                session.player.setSession = null;
//            }
            PlayerManger.gI().getPlayers().remove(session.player);
        }

//        Player player = PlayerManger.gI().getPlayerByUserID(session.userId);
//        if (player != null) {
//            if(player.havePet == 1 && player.statusPet != 3) {
//                player.statusPet = 3;
//            }
//            Map map = player.map;
//            PlayerDAO.updateDB(player);
//            session.player.zone.exitMap(player,map);
//            session.player.zone.players.remove(player);
//            session.player.map = null;
//            PlayerManger.gI().getPlayers().remove(player);
//        }
    }

}
