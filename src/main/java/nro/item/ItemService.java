package nro.item;

import nro.constant.Constant;
import nro.giftcode.GiftCode;
import nro.io.Message;
import nro.main.Service;
import nro.main.Util;
import nro.player.Player;

import java.util.ArrayList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class ItemService {

    private static ItemService instance;

    public static ItemService gI() {
        if (instance == null) {
            instance = new ItemService();
        }
        return instance;
    }

    public void createItemAngel(Player p, Message m, byte size, byte index) {
        try {
            byte index3 = -1;
            byte index4 = -1;
            if (index == (byte) (-1) || size < (byte) 2 || size > (byte) 4) {
                Service.gI().serverMessage(p.session, "Không thể thực hiện");
                return;
            }
            Item item = p.ItemBag[index];
            if (item == null || !isFormulaByGender(item.template.id, p.gender)) {
                Service.gI().serverMessage(p.session, "Không thể thực hiện");
                return;
            }
            if (item.quantity < 1) {
                Service.gI().serverMessage(p.session, "Cần 1 công thức");
                return;
            }
            Item item3 = null;
            Item item4 = null;

            byte index2 = m.reader().readByte();
            if (index2 == (byte) (-1)) {
                Service.gI().serverMessage(p.session, "Không thể thực hiện");
                return;
            }
            Item item2 = p.ItemBag[index2];
            if (item2 == null || !isPieceItemAngle(item2.template.id)) {
                Service.gI().serverMessage(p.session, "Không thể thực hiện");
                return;
            }
            if (item2.quantity < 999) {
                Service.gI().serverMessage(p.session, "Cần 999 mảnh trang bị");
                return;
            }

            if (size >= (byte) 3) {
                index3 = m.reader().readByte();
                if (index3 == (byte) (-1)) {
                    Service.gI().serverMessage(p.session, "Không thể thực hiện");
                    return;
                }
                item3 = p.ItemBag[index3];
                if (item3 == null || !isStoneUpgradeAngle(item3.template.id)) {
                    Service.gI().serverMessage(p.session, "Không thể thực hiện");
                    return;
                }
            }
            if (size == (byte) 4) {
                index4 = m.reader().readByte();
                if (index4 == (byte) (-1)) {
                    Service.gI().serverMessage(p.session, "Không thể thực hiện");
                    return;
                }
                item4 = p.ItemBag[index4];
                if (item4 == null || !isStoneLuckyAngle(item4.template.id)) {
                    Service.gI().serverMessage(p.session, "Không thể thực hiện");
                    return;
                }
            }
//            Util.log("SIZE: " + size + ", " + index + ", " + index2 + ", " + index3 + ", " + index4);
            String info = nameItemAngleTarget(item2.template.id, p.gender);
            info += "\b|2|Mảnh ghép " + item2.quantity + "/999";
            int percentSuccess = 35;
            if (item3 != null) {
                percentSuccess += (item3.template.id - 1073) * 10;
                info += "\b|2|Đá nâng cấp cấp " + (item3.template.id - 1073) + " (+" + (item3.template.id - 1073) + "0% tỉ lệ thành công)";
            }
            if (item4 != null) {
                info += "\b|2|Đá may mắn cấp " + (item4.template.id - 1078) + " (+" + (item4.template.id - 1078) + "0% tỉ lệ tối đa các chỉ số)";
            }
            info += "\b|2|Tỉ lệ thành công: " + percentSuccess + "%";
            info += "\b|2|Phí nâng cấp: 200 triệu vàng";

            //save item update
            p._itemUpStar = item;
            p._itemUseEpStar = item2;
            p._itemUseEpStar2 = item3;
            p._itemDaBaoVe = item4;

            p._indexUpStar = index;
            p._indexEpStar = index2;
            p._indexEpStar2 = index3;
            p._indexDaBaoVe = index4;

            m = new Message(32);
            m.writer().writeShort((short) 56);
            m.writer().writeUTF(info);
            if (p.vang >= 200000000L) {
                m.writer().writeByte(2);
                m.writer().writeUTF("Nâng cấp");
                m.writer().writeUTF("Từ chối");
            } else {
                m.writer().writeByte(1);
                m.writer().writeUTF("Cần 200 Tr\nvàng");
            }

            m.writer().flush();
            p.session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void confirmCreateItemAngel(Player p) {
        if (p.getBagNull() <= 0) {
            p.sendAddchatYellow("Hành trang không đủ chỗ trống!");
            return;
        }
        Item formulaAngle = p._itemUpStar;
        Item pieceAngle = p._itemUseEpStar;
        Item stoneUpgrade = p._itemUseEpStar2;
        Item stoneLucky = p._itemDaBaoVe;

        if (formulaAngle == null || pieceAngle == null || !isFormulaByGender(formulaAngle.template.id, p.gender) || !isPieceItemAngle(pieceAngle.template.id)) {
            return;
        }
        if ((System.currentTimeMillis() - p._timeDapDo) >= 1000 && p._checkDapDo) {
            p._timeDapDo = System.currentTimeMillis();
            p._checkDapDo = false;
            int perSuccess = 35;
            int perLucky = 20;
            if (stoneUpgrade != null && p._indexEpStar2 != -1 && isStoneUpgradeAngle(stoneUpgrade.template.id)) {
                perSuccess += (stoneUpgrade.template.id - 1073) * 10;
            }
            if (stoneLucky != null && p._indexDaBaoVe != -1 && isStoneLuckyAngle(stoneLucky.template.id)) {
                perLucky += perLucky * (stoneLucky.template.id - 1078) * 10 / 100;
            }
            if (p.ItemBag[p._indexUpStar].quantity >= 1 && p.ItemBag[p._indexEpStar].quantity >= 999 && p.vang >= 200000000L) {
                int idAngle = getIdItemAngleCreate(pieceAngle.template.id, p.gender);
                p.vang -= 200000000L;
                //tru so luong cong thuc
                p.ItemBag[p._indexUpStar].quantity -= 1;
                if (p.ItemBag[p._indexUpStar].quantity <= 0) {
                    p.ItemBag[p._indexUpStar] = null;
                }
                //tru so luong manh trang bi
                p.ItemBag[p._indexEpStar].quantity -= 999;
                if (p.ItemBag[p._indexEpStar].quantity <= 0) {
                    p.ItemBag[p._indexEpStar] = null;
                }
                //tru so luong da nang cap neu co
                if (stoneUpgrade != null && p.ItemBag[p._indexEpStar2].quantity >= 1) {
                    p.ItemBag[p._indexEpStar2].quantity -= 1;
                    if (p.ItemBag[p._indexEpStar2].quantity <= 0) {
                        p.ItemBag[p._indexEpStar2] = null;
                    }
                } else {
                    return;
                }
                //tru so luong da may man neu co
                if (stoneLucky != null && p.ItemBag[p._indexDaBaoVe].quantity >= 1) {
                    p.ItemBag[p._indexDaBaoVe].quantity -= 1;
                    if (p.ItemBag[p._indexDaBaoVe].quantity <= 0) {
                        p.ItemBag[p._indexDaBaoVe] = null;
                    }
                } else {
                    return;
                }
                Service.gI().updateVangNgoc(p);

                if (Util.nextInt(0, 100) < perSuccess) {
                    Item itemAngleTemp = ItemSell.getItemNotSell(idAngle);
                    if (itemAngleTemp == null) {
                        return;
                    }
                    perSuccess = Util.nextInt(0, 50); //luc nay la percent bonus 0 -> 20%
                    if (perSuccess == 49) {
                        perSuccess = 20;
                    } else if (perSuccess == 48 || perSuccess == 47) {
                        perSuccess = 19;
                    } else if (perSuccess == 46 || perSuccess == 45) {
                        perSuccess = 18;
                    } else if (perSuccess == 44 || perSuccess == 43) {
                        perSuccess = 17;
                    } else if (perSuccess == 42 || perSuccess == 41) {
                        perSuccess = 16;
                    } else if (perSuccess == 40 || perSuccess == 39) {
                        perSuccess = 15;
                    } else if (perSuccess == 38 || perSuccess == 37) {
                        perSuccess = 14;
                    } else if (perSuccess == 36 || perSuccess == 35) {
                        perSuccess = 13;
                    } else if (perSuccess == 34 || perSuccess == 33) {
                        perSuccess = 12;
                    } else if (perSuccess == 32 || perSuccess == 31) {
                        perSuccess = 11;
                    } else if (perSuccess == 30 || perSuccess == 29) {
                        perSuccess = 10;
                    } else if (perSuccess <= 28 && perSuccess >= 26) {
                        perSuccess = 9;
                    } else if (perSuccess <= 25 && perSuccess >= 23) {
                        perSuccess = 8;
                    } else if (perSuccess <= 22 && perSuccess >= 20) {
                        perSuccess = 7;
                    } else if (perSuccess <= 19 && perSuccess >= 17) {
                        perSuccess = 6;
                    } else if (perSuccess <= 16 && perSuccess >= 14) {
                        perSuccess = 5;
                    } else if (perSuccess <= 13 && perSuccess >= 11) {
                        perSuccess = 4;
                    } else if (perSuccess <= 10 && perSuccess >= 8) {
                        perSuccess = 3;
                    } else if (perSuccess <= 7 && perSuccess >= 5) {
                        perSuccess = 2;
                    } else if (perSuccess <= 4 && perSuccess >= 2) {
                        perSuccess = 1;
                    } else if (perSuccess <= 1) {
                        perSuccess = 0;
                    }
                    Item itemAngle = new Item(itemAngleTemp);
                    perSuccess += 10;
//                    Util.log("PERSUCCESS: " + perSuccess);
                    if (perSuccess > 0) {
                        for (byte i = 0; i < itemAngle.itemOptions.size(); i++) {
                            if (itemAngle.itemOptions.get(i).id != 21 && itemAngle.itemOptions.get(i).id != 30) {
                                itemAngle.itemOptions.get(i).param += (itemAngle.itemOptions.get(i).param * perSuccess / 100);
                            }
                        }
                    }
                    //option bonus
                    perSuccess = Util.nextInt(0, 100);
//                    Util.log("perLucky: " + perLucky);
                    if (perSuccess <= perLucky) {
                        if (perSuccess >= (perLucky - 3)) {
                            perLucky = 3;
                        } else if (perSuccess <= (perLucky - 4) && perSuccess >= (perLucky - 10)) {
                            perLucky = 2;
                        } else {
                            perLucky = 1;
                        }
                        itemAngle.itemOptions.add(new ItemOption(41, perLucky));
                        ArrayList<Integer> listOptionBonus = new ArrayList<>();
                        listOptionBonus.add(42);
                        listOptionBonus.add(43);
                        listOptionBonus.add(44);
                        listOptionBonus.add(45);
                        listOptionBonus.add(46);
                        listOptionBonus.add(197);
                        listOptionBonus.add(198);
                        listOptionBonus.add(199);
                        listOptionBonus.add(200);
                        listOptionBonus.add(201);
                        listOptionBonus.add(202);
                        listOptionBonus.add(203);
                        listOptionBonus.add(204);
                        for (int i = 0; i < perLucky; i++) {
                            perSuccess = Util.nextInt(0, listOptionBonus.size());
                            itemAngle.itemOptions.add(new ItemOption(listOptionBonus.get(perSuccess), Util.nextInt(1, 6)));
                            listOptionBonus.remove(perSuccess);
                        }
                    }
                    p.addItemToBag(itemAngle);

                    Service.gI().sendUpStarSuccess(p);
                } else {
                    Service.gI().sendUpStarError(p);
                }
                Service.gI().updateItemBag(p);
            }
            p._checkDapDo = true;
        }
        //RESET ALL
        Service.gI().resetItemDapDo(p);
    }

    private boolean isFormulaByGender(int id, byte gender) {
        return id == ((int) gender + 1071) || id == ((int) gender + 1084);
    }

    private boolean isPieceItemAngle(int id) {
        return id >= 1066 && id <= 1070;
    }

    private boolean isStoneUpgradeAngle(int id) {
        return id >= 1074 && id <= 1078;
    }

    private boolean isStoneLuckyAngle(int id) {
        return id >= 1079 && id <= 1083;
    }

    private String nameItemAngleTarget(int id, byte gender) {
        String info = "|1|";
        if (id == 1066) {
            info += "Chế tạo Áo Thiên Sứ";
        } else if (id == 1067) {
            info += "Chế tạo Quần Thiên Sứ";
        } else if (id == 1068) {
            info += "Chế tạo Giày Thiên Sứ";
        } else if (id == 1069) {
            info += "Chế tạo Nhẫn Thiên Sứ";
        } else if (id == 1070) {
            info += "Chế tạo Găng Tay Thiên Sứ";
        }

        if (gender == (byte) 0) {
            info += " Trái Đất";
        } else if (gender == (byte) 1) {
            info += " Namếc";
        } else if (gender == (byte) 2) {
            info += " Xayda";
        }

        return info;
    }

    private int getIdItemAngleCreate(int idPiece, byte gender) {
        int idAngle = -1;
        if (idPiece == 1066) {
            idAngle = 1048 + (int) gender;
        } else if (idPiece == 1067) {
            idAngle = 1051 + (int) gender;
        } else if (idPiece == 1070) {
            idAngle = 1054 + (int) gender;
        } else if (idPiece == 1068) {
            idAngle = 1057 + (int) gender;
        } else if (idPiece == 1069) {
            idAngle = 1060 + (int) gender;
        }
        return idAngle;
    }

    //for giftcode
    public void addItemGiftCodeToPlayer(Player p, GiftCode giftcode) {
        Set<Integer> keySet = giftcode.detail.keySet();
        String textGift = "Bạn vừa nhận được:\b";
        for (Integer key : keySet) {
            int idItem = key;
            int quantity = giftcode.detail.get(key);
            if (idItem == -1) {
                p.vang = Math.min(p.vang + (long) quantity, Constant.MAX_MONEY);
                textGift += quantity + " vàng\b";
            } else if (idItem == -2) {
                p.ngoc = Math.min(p.ngoc + quantity, Constant.MAX_GEM);
                textGift += quantity + " ngọc\b";
            } else if (idItem == -3) {
                p.ngocKhoa = Math.min(p.ngocKhoa + quantity, Constant.MAX_RUBY);
                textGift += quantity + " ngọc khóa\b";
            } else {
                Item itemGiftTemplate = ItemSell.getItemNotSell(idItem);
                if (itemGiftTemplate != null) {
                    Item itemGift = new Item(itemGiftTemplate);
                    itemGift.quantity = quantity;
                    p.addItemToBag(itemGift);
                    textGift += "x" + quantity + " " + itemGift.template.name + "\b";
                }
            }
        }
        Service.gI().updateItemBag(p);
        Service.chatNPC(p, (short) 24, textGift);
    }

    //effect chat
    public void sendEffectChat(Player p) {
        Timer timeEffectChat = new Timer();
        TimerTask sendEffect = new TimerTask() {
            public void run() {
                boolean check = false;
//                System.out.println("ZONE SIZE: " + p.zone.players.size());
                for (Player playerNear : p.zone.players) {
                    if (playerNear.id == p.id) {
                        check = true;
                        break;
                    }
                }
                if (!check) {
                    timeEffectChat.cancel();
                }
                for (Player playerNear : p.zone.players) {
//                    System.out.println("ZONE id: " + playerNear.id);
                    if (playerNear.id != p.id && Math.abs(p.x - playerNear.x) <= 200) {
                        playerNear.zone.chat(playerNear, "Wwow, Em ấy thật đẹp!");
                    }
                }
            }
        ;
        };
        timeEffectChat.schedule(sendEffect, 0, 5000);
        p.timerEffectChat = timeEffectChat;
    }

    public void sendEffectChatCaiTrangODo(Player p) {
        Timer timeEffectChat = new Timer();
        TimerTask sendEffect = new TimerTask() {
            public void run() {
                boolean check = false;
//                System.out.println("ZONE SIZE: " + p.zone.players.size());
                for (Player playerNear : p.zone.players) {
                    if (playerNear.id == p.id) {
                        check = true;
                        break;
                    }
                }
                if (!check) {
                    timeEffectChat.cancel();
                }
                for (Player playerNear : p.zone.players) {
//                    System.out.println("ZONE id: " + playerNear.id);
                    if (playerNear.id != p.id && Math.abs(p.x - playerNear.x) <= 200) {
                        int random = Util.nextInt(0, 3);
                        if (random == 0) {
                            playerNear.zone.chat(playerNear, "Hôi quá, tránh xa ta ra!");
                        } else if (random == 1) {
                            playerNear.zone.chat(playerNear, "Biến đi");
                        } else if (random == 2) {
                            playerNear.zone.chat(playerNear, "Mùi gì hôi quá ?");
                        }
                    }
                }
            }
        ;
        };
        timeEffectChat.schedule(sendEffect, 0, 5000);
        p.timerEffectChat = timeEffectChat;
    }

    public void sendEffectChatCaiTrangDep(Player p) {
        Timer timeEffectChat = new Timer();
        TimerTask sendEffect = new TimerTask() {
            public void run() {
                boolean check = false;
//                System.out.println("ZONE SIZE: " + p.zone.players.size());
                for (Player playerNear : p.zone.players) {
                    if (playerNear.id == p.id) {
                        check = true;
                        break;
                    }
                }
                if (!check) {
                    timeEffectChat.cancel();
                }
                for (Player playerNear : p.zone.players) {
//                    System.out.println("ZONE id: " + playerNear.id);
                    if (playerNear.id != p.id && Math.abs(p.x - playerNear.x) <= 200) {
                        int random = Util.nextInt(0, 3);
                        if (random == 0) {
                            playerNear.zone.chat(playerNear, "Cool Ngầu quá!");
                        } else if (random == 1) {
                            playerNear.zone.chat(playerNear, "Anh ấy thật đẹp trai");
                        } else if (random == 2) {
                            playerNear.zone.chat(playerNear, "Wwow, anh ấy đẹp trai quá");
                        }
                    }
                }
            }
        ;
        };
        timeEffectChat.schedule(sendEffect, 0, 5000);
        p.timerEffectChat = timeEffectChat;
    }
}
