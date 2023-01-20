package nro.main;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
//CUSTOM ITEM
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import nro.io.Message;
import nro.io.Session;

import nro.item.Item;
import nro.item.ItemBuff;
import nro.item.ItemOption;
import nro.item.ItemSell;
import nro.item.ItemTemplate;
import nro.shop.Shop;
import nro.shop.TabItemShop;
import nro.map.Map;
import nro.map.MapData;
import nro.map.MapTemplate;
import nro.map.MobTemplate;
import nro.map.Npc;
import nro.map.WayPoint;
import nro.player.Player;
import nro.part.Part;

public class MainManager {

    public int port;
    public String host;
    public static String mysql_part;
    public static String backup_part;
    public static String mysql_host;
    public static int mysql_port;
    public static String mysql_database;
    public static String mysql_user;
    public static String mysql_pass;
    byte vsData;
    byte vsMap;
    byte vsSkill;
    byte vsItem;
    public static ArrayList<Part> parts;
    static Server server = Server.gI();

    public MainManager() {

        this.loadConfigFile();
        this.loadDataBase();
    }
 
    private void loadConfigFile() {
        byte[] ab = GameScr.loadFile("nro.conf").toByteArray();
        if (ab == null) {
            System.out.println("Config file not found!");
            System.exit(0);
        }

        String data = new String(ab);
        HashMap<String, String> configMap = new HashMap();
        StringBuilder sbd = new StringBuilder();
        boolean bo = false;

        for (int i = 0; i <= data.length(); ++i) {
            char es;
            if (i != data.length() && (es = data.charAt(i)) != '\n') {
                if (es == '#') {
                    bo = true;
                }

                if (!bo) {
                    sbd.append(es);
                }
            } else {
                bo = false;
                String sbf = sbd.toString().trim();
                if (sbf != null && !sbf.equals("") && sbf.charAt(0) != '#') {
                    int j = sbf.indexOf(58);
                    if (j > 0) {
                        String key = sbf.substring(0, j).trim();
                        String value = sbf.substring(j + 1).trim();
                        configMap.put(key, value);
                        System.out.println("config: " + key + "-" + value);
                    }
                }
                sbd.setLength(0);
            }
        }

        if (configMap.containsKey("host")) {
            this.host = (String) configMap.get("host");
//            this.host = "103.166.182.237";
        } else {
            this.host = "localhost";
        }

        if (configMap.containsKey("port")) {
            this.port = Integer.parseInt((String) configMap.get("port"));
        } else {
            this.port = 14445;
        }

        if (configMap.containsKey("mysql-host")) {
            this.mysql_host = (String) configMap.get("mysql-host");
//            this.mysql_host = "103.166.182.237";
        } else {
            this.mysql_host = "localhost";
        }

        if (configMap.containsKey("mysql-port")) {
            this.mysql_port = Integer.parseInt((String) configMap.get("mysql-port"));
        } else {
            this.mysql_port = 3306;
        }

        if (configMap.containsKey("mysql-user")) {
            this.mysql_user = (String) configMap.get("mysql-user");
//            this.mysql_user = "foouser4";
        } else {
            this.mysql_user = "root";
        }

        if (configMap.containsKey("mysql-password")) {
            this.mysql_pass = (String) configMap.get("mysql-password");
//            this.mysql_pass = "AAbbcc123abc";
        } else {
            this.mysql_pass = "";
        }

        if (configMap.containsKey("mysql-database")) {
            this.mysql_database = (String) configMap.get("mysql-database");
        } else {
            this.mysql_database = "";
        }

        if (configMap.containsKey("version-Data")) {
//            this.vsData = Byte.parseByte((String) configMap.get("version-Data"));
//            this.vsData = 63; //v219
//            this.vsData = 64; //v221
//            this.vsData = 65; //v222
            this.vsData = 66; //vhalloween
        } else {
            this.vsData = 98;
        }

        if (configMap.containsKey("version-Map")) {
//            this.vsMap = Byte.parseByte((String) configMap.get("version-Map"));
//            this.vsMap = 30; //v221
            this.vsMap = 31; //v222
        } else {
            this.vsMap = 23;
        }

        if (configMap.containsKey("version-Skill")) {
//            this.vsSkill = Byte.parseByte((String) configMap.get("version-Skill"));
            this.vsSkill = 5;
        } else {
            this.vsSkill = 2;
        }

        if (configMap.containsKey("version-Item")) {
//            this.vsItem = Byte.parseByte((String) configMap.get("version-Item"));
//            this.vsItem = 113; //v219
//            this.vsItem = 115; //v221
//            this.vsItem = 116; //v222
            this.vsItem = 119; //vhalloween
        } else {
            this.vsItem = 6;
        }
    }

    //load item database
    public void loadDataBase() {
        SQLManager.create(this.mysql_host, this.mysql_port, this.mysql_database, this.mysql_user, this.mysql_pass);
//        SQLManager.create("localhost", this.mysql_port, this.mysql_database, "root", "AAbbcc!!@@123abc");
        int i;
        try {
            ResultSet res;
            JSONArray Option;
            i = 0;
            for (res = SQLManager.stat.executeQuery("SELECT * FROM `mob`;"); res.next(); ++i) {
                MobTemplate md = new MobTemplate();
                md.tempId = Integer.parseInt(res.getString("id"));
                md.name = res.getString("name");
                md.level = Byte.parseByte(res.getString("level"));
                md.Level = Integer.parseInt(res.getString("level"));
                md.maxHp = Integer.parseInt(res.getString("hp"));
                md.rangeMove = Byte.parseByte(res.getString("rangeMove"));
                md.speed = Byte.parseByte(res.getString("speed"));
                MobTemplate.entrys.add(md);
            }
            res.close();
            //load MAP
            i = 0;
            byte j;
            res = SQLManager.stat.executeQuery("SELECT * FROM `map`;");
            if (res.last()) {
                MapTemplate.arrTemplate = new MapTemplate[res.getRow()];
                res.beforeFirst();
            }

            while (res.next()) {
                MapTemplate mapTemplate = new MapTemplate();
                mapTemplate.id = res.getInt("id");
                mapTemplate.name = res.getString("name");
                mapTemplate.type = res.getByte("type");
                mapTemplate.planetId = res.getByte("planet_id");
                mapTemplate.tileId = res.getByte("tile_id");
                mapTemplate.bgId = res.getByte("bg_id");
                mapTemplate.bgType = res.getByte("bg_type");
                mapTemplate.maxplayers = res.getByte("maxplayer");
                mapTemplate.numarea = res.getByte("numzone");
                mapTemplate.wayPoints = MapData.loadListWayPoint(mapTemplate.id).toArray(new WayPoint[0]);
                JSONArray jar2;
                Option = (JSONArray) JSONValue.parse(res.getString("Mob"));
                mapTemplate.arMobid = new short[Option.size()];
                mapTemplate.arrMoblevel = new int[Option.size()];
                mapTemplate.arrMaxhp = new int[Option.size()];
                mapTemplate.arrMobx = new short[Option.size()];
                mapTemplate.arrMoby = new short[Option.size()];
                short l;
                for (l = 0; l < Option.size(); ++l) {
                    jar2 = (JSONArray) Option.get(l);
                    mapTemplate.arMobid[l] = Short.parseShort(jar2.get(0).toString());
                    mapTemplate.arrMoblevel[l] = Integer.parseInt(jar2.get(1).toString());
                    mapTemplate.arrMaxhp[l] = Integer.parseInt(jar2.get(2).toString());
                    mapTemplate.arrMobx[l] = Short.parseShort(jar2.get(3).toString());
                    mapTemplate.arrMoby[l] = Short.parseShort(jar2.get(4).toString());
                }
                Option = (JSONArray) JSONValue.parse(res.getString("Npc"));
                mapTemplate.npcs = new Npc[Option.size()];
                for (j = 0; j < Option.size(); ++j) {
                    mapTemplate.npcs[j] = new Npc();
                    jar2 = (JSONArray) JSONValue.parse(Option.get(j).toString());
                    Npc npc = mapTemplate.npcs[j];
                    npc.status = Byte.parseByte(jar2.get(0).toString());
                    npc.cx = Short.parseShort(jar2.get(1).toString());
                    npc.cy = Short.parseShort(jar2.get(2).toString());
                    npc.tempId = Integer.parseInt(jar2.get(3).toString());
                    npc.avartar = Integer.parseInt(jar2.get(4).toString());
                }
                MapTemplate.arrTemplate[i] = mapTemplate;
                i++;
            }

            res.close();
            i = 0;
            JSONObject job;
            for (res = SQLManager.stat.executeQuery("SELECT * FROM `item`;"); res.next(); ++i) {
                
                ItemTemplate item = new ItemTemplate();
                item.id = Short.parseShort(res.getString("id"));
                item.type = Byte.parseByte(res.getString("type"));
                item.gender = Byte.parseByte(res.getString("gender"));
                item.name = res.getString("name");
                item.description = res.getString("description");
                item.level = Byte.parseByte(res.getString("level"));
                item.strRequire = Integer.parseInt(res.getString("strRequire"));
                item.iconID = Integer.parseInt(res.getString("IconID"));
                item.part = Short.parseShort(res.getString("part"));
                item.type = Byte.parseByte(res.getString("type"));
                item.isUpToUp = Boolean.parseBoolean(res.getString("isUpToUp"));
                Option = (JSONArray) JSONValue.parse(res.getString("ItemOption"));
                if (Option.size() > 0) {
                    for (int k = 0; k < Option.size(); ++k) {
                        job = (JSONObject) Option.get(k);
                        item.itemoption.add(new ItemOption(Integer.parseInt(job.get("id").toString()), Integer.parseInt(job.get("param").toString())));
                    }
                } else {
                    item.itemoption.add(new ItemOption(73, 0));
                }

                ItemTemplate.entrys.add(item);
            }
            
            res.close();

            //load itemShell
            i = 0;
            res = SQLManager.stat.executeQuery("SELECT * FROM `ItemSell`;");
            while (res.next()) {
                ItemSell sell = new ItemSell();
                sell.id = Integer.parseInt(res.getString("item_id"));
                sell.buyCoin = Integer.parseInt(res.getString("buyCoin"));
                sell.buyGold = Integer.parseInt(res.getString("buyGold"));
                sell.buyType = Byte.parseByte(res.getString("buyType"));
                sell.isNew = res.getBoolean("isNew");
                Item item = new Item();
                item.id = sell.id;
                item.template = ItemTemplate.ItemTemplateID(item.id);
                item.quantity = Integer.parseInt(res.getString("quantity"));
                item.quantityTemp = Integer.parseInt(res.getString("quantity"));
                item.isExpires = Boolean.parseBoolean(res.getString("isExpires"));
                Option = (JSONArray) JSONValue.parse(res.getString("optionItem"));
                if (Option.size() > 0) {
                    for (int l = 0; l < Option.size(); l++) {
                        JSONObject job2 = (JSONObject) Option.get(l);
                        item.itemOptions.add(new ItemOption(Integer.parseInt(job2.get("id").toString()), Integer.parseInt(job2.get("param").toString())));
                    }
                }
                else
                {
                     item.itemOptions.add(new ItemOption(73, 0));
                }
                sell.item = item;
                ItemSell.items.add(item);
                ItemSell.itemCanSell.add(sell);
                i++;
            }
            res.close();

            //load itemShell
            i = 0;
            res = SQLManager.stat.executeQuery("SELECT * FROM `itemnotsell`;");
            while (res.next()) {
                //Add ngoc rong vao item not sell (14->20 la ngocrongm 441->965 la saophale) 516 la socola
                //380 : CAPSULE KIBI 381->385 la ITEM BUFF: CUONG NO, BO HUYET, BO KHI,.....
                //663->667: THUC AN DOI DO HUY DIET
                //74 DUI GA NUONG, 73 DUI GA NHIEM VU , 78 DUA BE NHIEM VU //85 TRUYEN DOREMON
//                int idItemNotSell[] = {14,15,16,17,18,19,20, 380,381,382,383,384,385, 441,442,443,444,445,446,447, 76,188,189,190, 225, 220,221,222,223,224 ,516, 663,664,665,666,667, 74,73,78,85,};
    //            for(i = 0; i < idItemNotSell.length; i++) {
                    Item _item = new Item();
//                    _item.id = idItemNotSell[i];
                    _item.id = Integer.parseInt(res.getString("item_id"));
                    _item.template = ItemTemplate.ItemTemplateID(_item.id);
                    _item.quantity = 1;
                    _item.quantityTemp = 99;
                    _item.isExpires = true;
                    Option = (JSONArray) JSONValue.parse(res.getString("optionItem"));
                    if (Option.size() > 0) {
                        for (int l = 0; l < Option.size(); l++) {
                            JSONObject job2 = (JSONObject) Option.get(l);
                            _item.itemOptions.add(new ItemOption(Integer.parseInt(job2.get("id").toString()), Integer.parseInt(job2.get("param").toString())));
                        }
                    }
                    else
                    {
                         _item.itemOptions.add(new ItemOption(73, 0));
                    }
    //                _item.itemOptions.add(new ItemOption(73, 0));
                    ItemSell.itemsNotSell.add(_item);
    //            }
                i++;
            }
            res.close();

                        //load nhtgamebuff
            i = 0;
            res = SQLManager.stat.executeQuery("SELECT * FROM `nhtgamebuff`;");
            while (res.next()) {
                Item _item = new Item();
                _item.id = Integer.parseInt(res.getString("item_id"));
                _item.template = ItemTemplate.ItemTemplateID(_item.id);
                _item.quantity = 1;
                _item.quantityTemp = 99;
                _item.isExpires = true;
                Option = (JSONArray) JSONValue.parse(res.getString("optionItem"));
                if (Option.size() > 0) {
                    for (int l = 0; l < Option.size(); l++) {
                        JSONObject job2 = (JSONObject) Option.get(l);
                        _item.itemOptions.add(new ItemOption(Integer.parseInt(job2.get("id").toString()), Integer.parseInt(job2.get("param").toString())));
                    }
                } else {
                    _item.itemOptions.add(new ItemOption(73, 0));
                }
                ItemBuff.items.add(_item);
                i++;
            }
            res.close();

            
            
            //load Shops
            i = 0;
            res = SQLManager.stat.executeQuery("SELECT * FROM `shop`;");
            while (res.next()) {
                Shop shop = new Shop();
                shop.npcID = Integer.parseInt(res.getString("npcID"));
                shop.idTabShop = Integer.parseInt(res.getString("idTabShop"));
                JSONArray tabs = (JSONArray) JSONValue.parse(res.getString("itemSell"));
                for (int k = 0; k < tabs.size(); k++) {
                    TabItemShop tabItemShop = new TabItemShop();
                    JSONObject tabItem = (JSONObject) JSONValue.parse(tabs.get(k).toString());
                    tabItemShop.tabName = tabItem.get("tabName").toString();
                    JSONArray items = (JSONArray) JSONValue.parse(tabItem.get("items").toString());
                    for (int l = 0; l < items.size(); l++) {
                        int itemSellID = Integer.parseInt(items.get(l).toString());
                        tabItemShop.itemsSell.add(ItemSell.getItemSellByID(itemSellID));
                    }
                    shop.tabShops.add(tabItemShop);
                }
                Shop.shops.add(shop);
                i++;
            }
            res.close();
            i = 0;
            res = SQLManager.stat.executeQuery("SELECT * FROM `itemtemp`;");
            while (res.next()) {
                Item item2 = new Item();
                item2.idTemp = res.getInt("id");
                item2.headTemp = res.getShort("head");
                item2.bodyTemp = res.getShort("body");
                item2.legTemp = res.getShort("leg");
                item2.entrys.add(item2);  
                i++;
            }
            res.close();
        } catch (Exception var14) {
            var14.printStackTrace();
            System.exit(0);
        }
        SQLManager.close();
        SQLManager.create(this.mysql_host, this.mysql_port, this.mysql_database, this.mysql_user, this.mysql_pass);
    }

    public static void sendData(Player p) {
        Message m = null;
        try {
            m = new Message(-87);
            m.writer().write(Server.cache[0].toByteArray());
            m.writer().flush();
            p.session.sendMessage(m);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public static void sendDatav2(Session session) {
        Message m = null;
        try {
            m = new Message(-87);
            m.writer().write(Server.cache[0].toByteArray());
            m.writer().flush();
            session.sendMessage(m);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public static void sendMap(Player p) {
        Message m = null;
        try {
            m = new Message(-28);
            m.writer().write(Server.cache[1].toByteArray());
            m.writer().flush();
            p.session.sendMessage(m);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public static void sendMapv2(Session session) {
        Message m = null;
        try {
            m = new Message(-28);
            m.writer().writeByte((byte)6);
            m.writer().write(Server.cache[1].toByteArray());
            m.writer().flush();
            session.sendMessage(m);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public static void sendSkill(Player p) {
        Message m = null;
        try {
            m = new Message(-28);
            m.writer().write(Server.cache[2].toByteArray());
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

    public static void sendSkillv2(Session session) {
        Message m = null;
        try {
            m = new Message(-28);
            m.writer().writeByte((byte)7);
            m.writer().write(Server.cache[2].toByteArray());
            m.writer().flush();
            session.sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public static void sendItem(Player p) {
        Message m = null;
        try {
            m = new Message(-28);
            m.writer().write(Server.cache[3].toByteArray());
//            p.session.doSendMessage(m);
            p.session.sendMessage(m);
            m.cleanup();

            m = new Message(-28);
            m.writer().write(Server.cache[4].toByteArray());
//            p.session.doSendMessage(m);
            p.session.sendMessage(m);
            m.cleanup();

            m = new Message(-28);
            m.writer().write(Server.cache[5].toByteArray());
//            p.session.doSendMessage(m);
            p.session.sendMessage(m);
            m.cleanup();
            
            m = new Message(-28);
            m.writer().write(Server.cache[6].toByteArray());
//            p.session.doSendMessage(m);
            p.session.sendMessage(m);
            m.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public static void sendItemv2(Session session) {
        Message m = null;
        try {
            m = new Message(-28);
            m.writer().writeByte((byte)8);
            m.writer().write(Server.cache[3].toByteArray());
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-28);
            m.writer().writeByte((byte)8);
            m.writer().write(Server.cache[4].toByteArray());
            session.sendMessage(m);
            m.cleanup();

            m = new Message(-28);
            m.writer().writeByte((byte)8);
//            m.writer().write(Server.cache[5].toByteArray());
            //CUSTOM ITEM
            ByteArrayInputStream is = new ByteArrayInputStream(FileIO.readFile("res/cache/vhalloween/NRitem2"));
            DataInputStream dis = new DataInputStream(is);
            m.writer().writeByte(dis.readByte()); //verItem
            m.writer().writeByte(dis.readByte()); //type item: 2
            m.writer().writeShort(dis.readShort()); //start item
            short endItem = dis.readShort();
            m.writer().writeShort((short)1145); //enditem v222
//            m.writer().writeShort((short)1075); //enditem v221

            for(short i = (short)800; i < (short)1118; i++) {
                m.writer().writeByte(dis.readByte()); //type item
                m.writer().writeByte(dis.readByte()); //gender item
                m.writer().writeUTF(dis.readUTF()); //name item
                m.writer().writeUTF(dis.readUTF()); //description item
                m.writer().writeByte(dis.readByte()); //level item
                m.writer().writeInt(dis.readInt()); //strRequire item
                m.writer().writeShort(dis.readShort()); //iconID item
                m.writer().writeShort(dis.readShort()); //part item
                m.writer().writeBoolean(dis.readBoolean()); //isUptoUp item
            }
            m.writer().writeByte((byte)5); //type item
            m.writer().writeByte((byte)3); //gender item
            m.writer().writeUTF("Cải trang Black Goku"); //name item
            m.writer().writeUTF("Cải trang thành Black Goku"); //description item
            m.writer().writeByte((byte)1); //level item
            m.writer().writeInt((int)15000000); //strRequire item
            m.writer().writeShort((short)5141); //iconID item
            m.writer().writeShort((short)(-1)); //part item
            m.writer().writeBoolean(false); //isUptoUp item

            m.writer().writeByte((byte)5); //type item
            m.writer().writeByte((byte)3); //gender item
            m.writer().writeUTF("Cải trang Hóa Đá"); //name item
            m.writer().writeUTF("Cải trang thành Tượng Đá"); //description item
            m.writer().writeByte((byte)1); //level item
            m.writer().writeInt((int)15000000); //strRequire item
            m.writer().writeShort((short)4392); //iconID item
            m.writer().writeShort((short)(-1)); //part item
            m.writer().writeBoolean(false); //isUptoUp item

            m.writer().writeByte((byte)5); //type item
            m.writer().writeByte((byte)3); //gender item
            m.writer().writeUTF("Cải trang Bill"); //name item
            m.writer().writeUTF("Cải trang thành Thần Hủy Diệt Bill"); //description item
            m.writer().writeByte((byte)1); //level item
            m.writer().writeInt((int)15000000); //strRequire item
            m.writer().writeShort((short)4847); //iconID item
            m.writer().writeShort((short)(-1)); //part item
            m.writer().writeBoolean(false); //isUptoUp item

            m.writer().writeByte((byte)5); //type item
            m.writer().writeByte((byte)3); //gender item
            m.writer().writeUTF("Cải trang Champa"); //name item
            m.writer().writeUTF("Cải trang thành Thần Hủy Diệt Champa"); //description item
            m.writer().writeByte((byte)1); //level item
            m.writer().writeInt((int)15000000); //strRequire item
            m.writer().writeShort((short)4879); //iconID item
            m.writer().writeShort((short)(-1)); //part item
            m.writer().writeBoolean(false); //isUptoUp item

            m.writer().writeByte((byte)5); //type item
            m.writer().writeByte((byte)3); //gender item
            m.writer().writeUTF("Cải trang Whis"); //name item
            m.writer().writeUTF("Cải trang thành Thiên Sứ Whis"); //description item
            m.writer().writeByte((byte)1); //level item
            m.writer().writeInt((int)15000000); //strRequire item
            m.writer().writeShort((short)7679); //iconID item
            m.writer().writeShort((short)(-1)); //part item
            m.writer().writeBoolean(false); //isUptoUp item

            m.writer().writeByte((byte)5); //type item
            m.writer().writeByte((byte)3); //gender item
            m.writer().writeUTF("Cải trang Cadic"); //name item
            m.writer().writeUTF("Cải trang thành Cadic"); //description item
            m.writer().writeByte((byte)1); //level item
            m.writer().writeInt((int)15000000); //strRequire item
            m.writer().writeShort((short)6027); //iconID item
            m.writer().writeShort((short)(-1)); //part item
            m.writer().writeBoolean(false); //isUptoUp item

            m.writer().writeByte((byte)5); //type item
            m.writer().writeByte((byte)3); //gender item
            m.writer().writeUTF("Cải trang Nappa"); //name item
            m.writer().writeUTF("Cải trang thành Nappa"); //description item
            m.writer().writeByte((byte)1); //level item
            m.writer().writeInt((int)15000000); //strRequire item
            m.writer().writeShort((short)6058); //iconID item
            m.writer().writeShort((short)(-1)); //part item
            m.writer().writeBoolean(false); //isUptoUp item

            m.writer().writeByte((byte)27); //type item
            m.writer().writeByte((byte)3); //gender item
            m.writer().writeUTF("Rương cải trang may mắn HuyMe"); //name item
            m.writer().writeUTF("Giấu bên trong nhiều vật phẩm quý giá"); //description item
            m.writer().writeByte((byte)1); //level item
            m.writer().writeInt((int)1500000); //strRequire item
            m.writer().writeShort((short)5007); //iconID item
            m.writer().writeShort((short)(-1)); //part item
            m.writer().writeBoolean(false); //isUptoUp item

            //SET HEART
            m.writer().writeByte((byte)0); //type item
            m.writer().writeByte((byte)0); //gender item
            m.writer().writeUTF("Áo Heart Trái Đất"); //name item
            m.writer().writeUTF("Trang bị Thần Heart"); //description item
            m.writer().writeByte((byte)13); //level item
            m.writer().writeInt((int)1500000); //strRequire item
            m.writer().writeShort((short)6528); //iconID item
            m.writer().writeShort((short)589); //part item
            m.writer().writeBoolean(false); //isUptoUp item

            m.writer().writeByte((byte)1); //type item
            m.writer().writeByte((byte)0); //gender item
            m.writer().writeUTF("Quần Heart Trái Đất"); //name item
            m.writer().writeUTF("Trang bị Thần Heart"); //description item
            m.writer().writeByte((byte)13); //level item
            m.writer().writeInt((int)1500000); //strRequire item
            m.writer().writeShort((short)6529); //iconID item
            m.writer().writeShort((short)590); //part item
            m.writer().writeBoolean(false); //isUptoUp item

            m.writer().writeByte((byte)0); //type item
            m.writer().writeByte((byte)1); //gender item
            m.writer().writeUTF("Áo Heart Namếc"); //name item
            m.writer().writeUTF("Trang bị Thần Heart"); //description item
            m.writer().writeByte((byte)13); //level item
            m.writer().writeInt((int)1500000); //strRequire item
            m.writer().writeShort((short)6533); //iconID item
            m.writer().writeShort((short)583); //part item
            m.writer().writeBoolean(false); //isUptoUp item

            m.writer().writeByte((byte)1); //type item
            m.writer().writeByte((byte)1); //gender item
            m.writer().writeUTF("Quần Heart Namếc"); //name item
            m.writer().writeUTF("Trang bị Thần Heart"); //description item
            m.writer().writeByte((byte)13); //level item
            m.writer().writeInt((int)1500000); //strRequire item
            m.writer().writeShort((short)6534); //iconID item
            m.writer().writeShort((short)584); //part item
            m.writer().writeBoolean(false); //isUptoUp item

            m.writer().writeByte((byte)0); //type item
            m.writer().writeByte((byte)2); //gender item
            m.writer().writeUTF("Áo Heart Xayda"); //name item
            m.writer().writeUTF("Trang bị Thần Heart"); //description item
            m.writer().writeByte((byte)13); //level item
            m.writer().writeInt((int)1500000); //strRequire item
            m.writer().writeShort((short)6537); //iconID item
            m.writer().writeShort((short)586); //part item
            m.writer().writeBoolean(false); //isUptoUp item

            m.writer().writeByte((byte)1); //type item
            m.writer().writeByte((byte)2); //gender item
            m.writer().writeUTF("Quần Heart Xayda"); //name item
            m.writer().writeUTF("Trang bị Thần Heart"); //description item
            m.writer().writeByte((byte)13); //level item
            m.writer().writeInt((int)1500000); //strRequire item
            m.writer().writeShort((short)6538); //iconID item
            m.writer().writeShort((short)587); //part item
            m.writer().writeBoolean(false); //isUptoUp item

            m.writer().writeByte((byte)4); //type item
            m.writer().writeByte((byte)3); //gender item
            m.writer().writeUTF("Nhẫn Heart"); //name item
            m.writer().writeUTF("Trang bị Thần Heart"); //description item
            m.writer().writeByte((byte)13); //level item
            m.writer().writeInt((int)1500000); //strRequire item
            m.writer().writeShort((short)6532); //iconID item
            m.writer().writeShort((short)(-1)); //part item
            m.writer().writeBoolean(false); //isUptoUp item

            m.writer().writeByte((byte)2); //type item
            m.writer().writeByte((byte)0); //gender item
            m.writer().writeUTF("Găng Heart Trái Đất"); //name item
            m.writer().writeUTF("Trang bị Thần Heart"); //description item
            m.writer().writeByte((byte)13); //level item
            m.writer().writeInt((int)1500000); //strRequire item
            m.writer().writeShort((short)6530); //iconID item
            m.writer().writeShort((short)(-1)); //part item
            m.writer().writeBoolean(false); //isUptoUp item

            m.writer().writeByte((byte)3); //type item
            m.writer().writeByte((byte)0); //gender item
            m.writer().writeUTF("Giầy Heart Trái Đất"); //name item
            m.writer().writeUTF("Trang bị Thần Heart"); //description item
            m.writer().writeByte((byte)13); //level item
            m.writer().writeInt((int)1500000); //strRequire item
            m.writer().writeShort((short)6531); //iconID item
            m.writer().writeShort((short)(-1)); //part item
            m.writer().writeBoolean(false); //isUptoUp item

            m.writer().writeByte((byte)2); //type item
            m.writer().writeByte((byte)1); //gender item
            m.writer().writeUTF("Găng Heart Namếc"); //name item
            m.writer().writeUTF("Trang bị Thần Heart"); //description item
            m.writer().writeByte((byte)13); //level item
            m.writer().writeInt((int)1500000); //strRequire item
            m.writer().writeShort((short)6535); //iconID item
            m.writer().writeShort((short)(-1)); //part item
            m.writer().writeBoolean(false); //isUptoUp item

            m.writer().writeByte((byte)3); //type item
            m.writer().writeByte((byte)1); //gender item
            m.writer().writeUTF("Giầy Heart Namếc"); //name item
            m.writer().writeUTF("Trang bị Thần Heart"); //description item
            m.writer().writeByte((byte)13); //level item
            m.writer().writeInt((int)1500000); //strRequire item
            m.writer().writeShort((short)6536); //iconID item
            m.writer().writeShort((short)(-1)); //part item
            m.writer().writeBoolean(false); //isUptoUp item

            m.writer().writeByte((byte)2); //type item
            m.writer().writeByte((byte)2); //gender item
            m.writer().writeUTF("Găng Heart Xayda"); //name item
            m.writer().writeUTF("Trang bị Thần Heart"); //description item
            m.writer().writeByte((byte)13); //level item
            m.writer().writeInt((int)1500000); //strRequire item
            m.writer().writeShort((short)6539); //iconID item
            m.writer().writeShort((short)(-1)); //part item
            m.writer().writeBoolean(false); //isUptoUp item

            m.writer().writeByte((byte)3); //type item
            m.writer().writeByte((byte)2); //gender item
            m.writer().writeUTF("Giầy Heart Xayda"); //name item
            m.writer().writeUTF("Trang bị Thần Heart"); //description item
            m.writer().writeByte((byte)13); //level item
            m.writer().writeInt((int)1500000); //strRequire item
            m.writer().writeShort((short)6540); //iconID item
            m.writer().writeShort((short)(-1)); //part item
            m.writer().writeBoolean(false); //isUptoUp item

            // ITEM TICKET
            m.writer().writeByte((byte)27); //type item
            m.writer().writeByte((byte)3); //gender item
            m.writer().writeUTF("Mảnh Áo Thần"); //name item
            m.writer().writeUTF("Mảnh ghép Áo Thần Heart"); //description item
            m.writer().writeByte((byte)1); //level item
            m.writer().writeInt((int)1500000); //strRequire item
            m.writer().writeShort((short)6523); //iconID item
            m.writer().writeShort((short)(-1)); //part item
            m.writer().writeBoolean(false); //isUptoUp item

            m.writer().writeByte((byte)27); //type item
            m.writer().writeByte((byte)3); //gender item
            m.writer().writeUTF("Mảnh Quần Thần"); //name item
            m.writer().writeUTF("Mảnh ghép Quần Thần Heart"); //description item
            m.writer().writeByte((byte)1); //level item
            m.writer().writeInt((int)1500000); //strRequire item
            m.writer().writeShort((short)6524); //iconID item
            m.writer().writeShort((short)(-1)); //part item
            m.writer().writeBoolean(false); //isUptoUp item

            m.writer().writeByte((byte)27); //type item
            m.writer().writeByte((byte)3); //gender item
            m.writer().writeUTF("Mảnh Găng Thần"); //name item
            m.writer().writeUTF("Mảnh ghép Găng Thần Heart"); //description item
            m.writer().writeByte((byte)1); //level item
            m.writer().writeInt((int)1500000); //strRequire item
            m.writer().writeShort((short)6525); //iconID item
            m.writer().writeShort((short)(-1)); //part item
            m.writer().writeBoolean(false); //isUptoUp item

            m.writer().writeByte((byte)27); //type item
            m.writer().writeByte((byte)3); //gender item
            m.writer().writeUTF("Mảnh Giày Thần"); //name item
            m.writer().writeUTF("Mảnh ghép Giày Thần Heart"); //description item
            m.writer().writeByte((byte)1); //level item
            m.writer().writeInt((int)1500000); //strRequire item
            m.writer().writeShort((short)6526); //iconID item
            m.writer().writeShort((short)(-1)); //part item
            m.writer().writeBoolean(false); //isUptoUp item

            m.writer().writeByte((byte)27); //type item
            m.writer().writeByte((byte)3); //gender item
            m.writer().writeUTF("Mảnh Nhẫn Thần"); //name item
            m.writer().writeUTF("Mảnh ghép Nhẫn Thần Heart"); //description item
            m.writer().writeByte((byte)1); //level item
            m.writer().writeInt((int)1500000); //strRequire item
            m.writer().writeShort((short)6527); //iconID item
            m.writer().writeShort((short)(-1)); //part item
            m.writer().writeBoolean(false); //isUptoUp item
            
            m.writer().writeByte((byte)27); //type item
            m.writer().writeByte((byte)3); //gender item
            m.writer().writeUTF("Bùa trang bị kích hoạt"); //name item
            m.writer().writeUTF("Vật phẩm dùng để mở trang bị kích hoạt"); //description item
            m.writer().writeByte((byte)1); //level item
            m.writer().writeInt((int)1500000); //strRequire item
            m.writer().writeShort((short)6847); //iconID item
            m.writer().writeShort((short)(-1)); //part item
            m.writer().writeBoolean(false); //isUptoUp item
            //END CUSTOM ITEM
            session.sendMessage(m);
            m.cleanup();
            
            m = new Message(-28);
            m.writer().writeByte((byte)8);
            m.writer().write(Server.cache[6].toByteArray());
            session.sendMessage(m);
            m.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public static Map getMapid(int id) {
//        synchronized (server.maps) {

            for (Map map : server.maps) {
                if (map != null && map.template.id == id) {
                    return map;
                }
            }
            return null;

//            for (short i = 0; i < server.maps.length; i++) {
//                Map map = server.maps[i];
//                if (map != null && map.template.id == id) {
//                    return map;
//                }
//            }
//        }
    }

    public static void reciveImageMOB(Player player, Message m) {
        try {
            if (player != null && player.session != null && m != null && m.reader().available() > 0) {
                GameScr.reciveImageMOB(player, m);
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
