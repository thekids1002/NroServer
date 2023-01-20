package nro.main;

import nro.io.Message;
import nro.player.Player;
import nro.item.Item;
import nro.item.ItemOption;
import nro.item.ItemSell;

public class LuckyService {
    private static LuckyService instance;

    public static LuckyService gI() {
        if (instance == null) {
            instance = new LuckyService();
        }
        return instance;
    }

    public void loadUILucky(Player p) {
        Message m = null;
        try {
            m = new Message(-127);
            m.writer().writeByte((byte)0);
            m.writer().writeByte((byte)7);
            m.writer().writeShort((short)419);
            m.writer().writeShort((short)420);
            m.writer().writeShort((short)421);
            m.writer().writeShort((short)422);
            m.writer().writeShort((short)423);
            m.writer().writeShort((short)424);
            m.writer().writeShort((short)425);
            m.writer().writeByte((byte)1);
            m.writer().writeInt(4);
            m.writer().writeShort((short)821);
            p.session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }

    public void resultLucky(Player p, byte type, byte count) {
        if(type == (byte)2) {
            int idItemNotSell[] = {18,19,20, 441,442,443,444,445,446,447, 190, 225, 220,221,222,223,224,995,996,997,998,999,1000,1001,1007,1013,1021,1022,1023,1028, 954,955,
            733,734,735,743,744,746,795,849,897,920, 1008,967,944,943,942,936,919,918,917,916,910,909,908,893,892,
            828,829,830,831,832,833,834,835,836,837,838,839,840,841,842,859,956};
            int idImg[] = {423,424,425, 3887,3888,3889,3890,3891,3892,3893, 930, 1421, 1420,1419,1416,1417,1418};
            int id = -1;
            Message m = null;
            try {
                m = new Message(-127);
                m.writer().writeByte((byte)1);
                m.writer().writeByte(count);
                for(byte i = 0; i < count; i++) {
//                    id = Util.nextInt(0, 32);
//                    id = Util.nextInt(0, 57);
                    id = Util.nextInt(0, 74);
                    Item itemQuay = ItemSell.getItemNotSell(idItemNotSell[id]);
                    Item _item = new Item(itemQuay);
                    if(idItemNotSell[id] == 190) {
                        _item.quantity = Util.nextInt(1, 51)*1000;
                        _item.itemOptions.add(new ItemOption(171, _item.quantity/1000));
                    }
                    if(itemHasTime(idItemNotSell[id])) {
                        int day = Util.nextInt(1, 31);
                        _item.itemOptions.add(new ItemOption(93, day));
                        _item.timeHSD = System.currentTimeMillis() + (long)day*86400000;
                    }
                    p.ItemQuay.add(_item);
                    m.writer().writeShort((short)(_item.template.iconID));
                }
                p.session.sendMessage(m);
                m.cleanup();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(m != null) {
                    m.cleanup();
                }
            }
        }
    }

    public boolean itemHasTime(int id) {
        int hasTime[] = {995,996,997,998,999,1000,1001,1007,1013,1021,1022,1023,1028, 954,955,
            1008,967,944,943,942,936,919,918,917,916,910,909,908,893,892};
        for(int i = 0; i < hasTime.length; i++) {
            if(hasTime[i] == id) {
                return true;
            }
        }
        return false;
    }

    public void openItemQuay(Player p) {
        Message m = null;
        try {
            m = new Message(-44);
            m.writer().writeByte((byte)4);
            m.writer().writeByte((byte)1);      
            for (int i = 0; i < 1; i++) {
//                TabItemShop tabItemShop = tabs[i];   
                m.writer().writeUTF("Vật phẩm");
                m.writer().writeByte((byte)(p.ItemQuay.size()));
                for (int j = 0; j < p.ItemQuay.size(); j++) {
                    m.writer().writeShort(p.ItemQuay.get(j).template.id);
                    m.writer().writeUTF(p.ItemQuay.get(j).template.name);

                    m.writer().writeByte((byte)(p.ItemQuay.get(j).itemOptions.size()));
                    
                    for (ItemOption itemOption : p.ItemQuay.get(j).itemOptions) {
                        m.writer().writeByte(itemOption.id);
                        m.writer().writeShort((short)itemOption.param);
                    }
                    //hiển thị new item
                    m.writer().writeByte((byte)0);
                    //xử lý preview cải trang
                    boolean isCT = (p.ItemQuay.get(j).template.type == 5) && p.ItemQuay.get(j).template.checkIsCaiTrang();
//                    m.writer().writeByte((isCT ? 1 : 0));
                    m.writer().writeByte((byte)0);
//                    if (isCT) {
//                         for(Item iad : itemSell.item.entrys){
//                            if(itemSell.item.id == iad.idTemp){
//                                m.writer().writeShort(iad.headTemp);
//                                m.writer().writeShort(iad.bodyTemp);
//                                m.writer().writeShort(iad.legTemp);
//                                m.writer().writeShort(-1);
//                            }
//                        }
//                    }
                }
                m.writer().flush();
                p.session.sendMessage(m);
                m.cleanup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
