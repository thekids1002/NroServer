package nro.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import nro.io.Message;
import nro.io.Session;
import nro.item.Item;
import nro.item.Amulet;
import nro.item.ItemOption;
import nro.item.ItemSell;
import nro.shop.TabItemShop;
import nro.map.MobTemplate;
import nro.player.Player;

public class GameScr {
     public static void SendFile(Session session, int cmd, String url) throws IOException {
        byte[] ab = GameScr.loadFile(url).toByteArray();
        Message msg = new Message(cmd);
        msg.writer().write(ab);
        msg.writer().flush();
        session.sendMessage(msg);
        msg.cleanup();
    }
     public static void reciveImage(Player p, Message m) throws IOException {
        int id = m.reader().readInt();
        m.cleanup();
        byte[] icon = FileIO.readFile("res/icon/x" + p.session.zoomLevel + "/" + id + ".png");
        if (icon.length == 0) {
            return;
        } 
        m = new Message(-67);
        m.writer().writeInt(id);
        m.writer().writeInt(icon.length);
        m.writer().write(icon);
        m.writer().flush();
        p.session.sendMessage(m);
        m.cleanup();
    }
    public static void reciveImageSmall(Player p, Message m) throws IOException {
        int id = m.reader().readInt();
        m.cleanup();
        byte[] icon = FileIO.readFile("res/icon/x" + p.session.zoomLevel + "/" + id + ".png");
        if (icon.length == 0) {
            return;
        } 
        m = new Message(-77);
        m.writer().writeInt(id);
        m.writer().writeInt(icon.length);
        m.writer().flush();
        p.session.sendMessage(m);
        m.cleanup();
    }    
    public static ByteArrayOutputStream loadFile(String url) {
        FileInputStream openFileInput;
        try {
            openFileInput = new FileInputStream(url);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[1024];
            while (true) {
                int read = openFileInput.read(bArr);
                if (read == -1) {
                    break;
                }
                byteArrayOutputStream.write(bArr, 0, read);
            }
            openFileInput.close();
            return byteArrayOutputStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void UIshop(Player p, TabItemShop[] tabs) throws IOException {
        p.openItemQuay = false;
        Message m = null;
        try {
            m = new Message(-44);
            m.writer().writeByte(0);
            m.writer().writeByte(tabs.length);      
            for (int i = 0; i < tabs.length; i++) {
                TabItemShop tabItemShop = tabs[i];   
                m.writer().writeUTF(tabItemShop.tabName.replace("$", "\n"));
                m.writer().writeByte(tabItemShop.itemsSell.size());
                for (int j = 0; j < tabItemShop.itemsSell.size(); j++) {
                    ItemSell itemSell = tabItemShop.itemsSell.get(j);
                    if (itemSell == null) {
                        m.writer().writeShort(-1);
                        continue;
                    }
                    m.writer().writeShort(itemSell.item.template.id);
                    if(itemSell.item.template.id == 518) {
                        if((byte)(p.maxBox - (byte)20) < (byte)10) {
                            m.writer().writeInt(((int)(p.maxBox - (byte)20) + 1)*50000000);
                        } else {
                            m.writer().writeInt(500000000);
                        }
                    } else {
                        m.writer().writeInt(itemSell.buyGold);
                    }
                    if(itemSell.item.template.id == 517) {
                        if((byte)(p.maxluggage - (byte)20) < (byte)10) {
                            m.writer().writeInt(((int)(p.maxluggage - (byte)20) + 1)*100);
                        } else {
                            m.writer().writeInt(1000);
                        }
                    } else {
                        m.writer().writeInt(itemSell.buyCoin);
                    }

                    m.writer().writeByte(itemSell.item.itemOptions.size());
                    if((itemSell.item.template.id >= 213 && itemSell.item.template.id <= 219) || itemSell.item.template.id == 522 || itemSell.item.template.id == 671 || itemSell.item.template.id == 672) { //Bua tri tue    
                        byte _idOption = 65;
                        Amulet _amulet = null;
                        int timeLeft = 0;
                        if(itemSell.item.template.id == 213) {
                            _amulet = p.listAmulet.get(0);
                        } else if(itemSell.item.template.id == 214) {
                            _amulet = p.listAmulet.get(1);
                        } else if(itemSell.item.template.id == 215) {
                            _amulet = p.listAmulet.get(2);
                        } else if(itemSell.item.template.id == 216) {
                            _amulet = p.listAmulet.get(3);
                        } else if(itemSell.item.template.id == 217) {
                            _amulet = p.listAmulet.get(4);
                        } else if(itemSell.item.template.id == 218) {
                            _amulet = p.listAmulet.get(5);
                        } else if(itemSell.item.template.id == 219) {
                            _amulet = p.listAmulet.get(6);
                        } else if(itemSell.item.template.id == 522) {
                            _amulet = p.listAmulet.get(7);
                        } else if(itemSell.item.template.id == 671) {
                            _amulet = p.listAmulet.get(8);
                        } else if(itemSell.item.template.id == 672) {
                            _amulet = p.listAmulet.get(9);
                        }
                        if(_amulet.timeEnd - System.currentTimeMillis() > 60000) {
                            timeLeft = (int)((_amulet.timeEnd - System.currentTimeMillis())/60000);
                            if(timeLeft > 1440) {
                                _idOption = 63;
                                timeLeft = (short)(timeLeft/1440);
                            }
                        }
                        m.writer().writeByte(_idOption);
                        m.writer().writeShort(timeLeft);
                    } else {
                        for (ItemOption itemOption : itemSell.item.itemOptions) {
                            m.writer().writeByte(itemOption.id);
                            m.writer().writeShort((short)itemOption.param);
                        }
                    }
                    //hiển thị new item
                    m.writer().writeByte((itemSell.isNew ? 1 : 0));
                    //xử lý preview cải trang
                    boolean isCT = (itemSell.item.template.type == 5) && itemSell.item.template.checkIsCaiTrang();
                    m.writer().writeByte((isCT ? 1 : 0));
                    if (isCT) {
                         for(Item iad : itemSell.item.entrys){
                            if(itemSell.item.id == iad.idTemp){
                                m.writer().writeShort(iad.headTemp);
                                m.writer().writeShort(iad.bodyTemp);
                                m.writer().writeShort(iad.legTemp);
                                m.writer().writeShort(-1);
                            }
                        }
                    }
                }
                m.writer().flush();
                p.session.sendMessage(m);
                m.cleanup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void reciveImageMOB(Player p, Message m) {
        try {
            int id = m.reader().readUnsignedByte();
            MobTemplate mob = MobTemplate.getMob(id);
            if (mob == null) {
                return;
            }
            int zoomLv = p.session.zoomLevel;
            if (zoomLv < 1 || zoomLv > 4) {
                zoomLv = 1;
            }
            ByteArrayOutputStream a = loadFile("Img/mob/x" + zoomLv + "/" + id);
            if (a != null) {
                a.flush();
                byte[] ab = a.toByteArray();
                m = new Message(-28);
                m.writer().write(ab);
                m.writer().flush();
                p.session.sendMessage(m);
            }
            a.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }
    public static void saveFile(String url, byte[] data) {
        try {
            File f = new File(url);
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(url);
            fos.write(data);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
