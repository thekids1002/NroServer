package nro.map;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import nro.player.Player;
import nro.main.GameScr;
import nro.main.Util;

public final class Map {

    public int id;
    public byte planetId;
    public byte tileId;
    public byte bgId;
    public byte bgType;
    public byte type;
    public String name;
    public ArrayList<Player> players;
    public ArrayList<WayPoint> wayPoints;
    public ArrayList<Npc> npcs;
    public ArrayList<Mob> mobs;
    public ArrayList<ItemMap> items;
    public MapTemplate template;
    public Zone[] area;
    private boolean runing;
    private Object LOCK;
    private Thread threadUpdate;

    public boolean MapCold() {
        return id == 109 || id == 108 || id == 107 || id == 110 || id == 106 || id == 105 || id == 158 || id == 159;
    }

    public boolean MapSaoDen() {
        return (id >= 85 && id <= 91);
    }

    public boolean MapThanhDia() {
        return id == 156 || id == 157 || id == 158 || id == 159;
    }

    public boolean MapThanhDia1() {
        return id == 156 || id == 157;
    }

    public boolean MapThanhDia2() {
        return id == 158 || id == 159;
    }

    public boolean MapCell() {
        return id == 92 || id == 93 || id == 94 || id == 96 || id == 97 || id == 98 || id == 99 || id == 100;
    }

    public boolean MapSetKichHoat() {
        return id == 1 || id == 2 || id == 3 || id == 8 || id == 9 || id == 11 || id == 15 || id == 16 || id == 17;
    }

    public boolean MapNrNamec() {
        return id == 8 || id == 9 || id == 10 || id == 11 || id == 12 || id == 13 || id == 31 || id == 32 || id == 33 || id == 34;
    }

    public boolean MapDropNrNamec() {
        return id == 7 || id == 8 || id == 9 || id == 10 || id == 11 || id == 12 || id == 13 || id == 31 || id == 32 || id == 33 || id == 34 || id == 43 || id == 25;
    }

    public boolean MapKhiGas() {
        return id == 146 || id == 147 || id == 148 || id == 149 || id == 151 || id == 152 || id == 155;
    }

    public boolean MapFarmThienSu() {
        return id == 155 || (id >= 160 && id <= 163);
    }

    public boolean MapHome() {
        return id == 21 || id == 22 || id == 23 || id == 39 || id == 40 || id == 41;
    }

    public boolean MapTest() {
        return id == 5;
    }

    private class RunPlace implements Runnable {

        public RunPlace() {
        }

        ;
        public void run() {
            long l1;
            long l2;
            while (Map.this.runing) {
                try {
                    l1 = System.currentTimeMillis();
                    Map.this.update();
                    l2 = System.currentTimeMillis() - l1;
                    Thread.sleep(Math.abs(500L - l2));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return;
        }
    }

    public Map(MapTemplate mapTemplate) {
        this.LOCK = new Object();
        this.id = mapTemplate.id;
        this.template = mapTemplate;
        this.area = new Zone[mapTemplate.numarea];
        for (byte i = 0; i < this.template.numarea; ++i) {
            this.area[i] = new Zone(this, i);
        }
        this.players = new ArrayList<>();
        this.wayPoints = new ArrayList<>();
        this.npcs = new ArrayList<>();
        this.mobs = new ArrayList<>();
        this.items = new ArrayList<>();
        this.initMob();
        this.threadUpdate = new Thread(new RunPlace());
    }

    public void initMob() {
        for (byte j = 0; j < area.length; j++) {
            area[j].mobs.clear();
            int k = 0;
            for (short i = 0; i < this.template.arMobid.length; i++) {
                Mob m = new Mob(k, this.template.arMobid[i], 10);
                m.level = this.template.arrMoblevel[i];
                m.pointX = this.template.arrMobx[i];
                m.pointY = this.template.arrMoby[i];
                m.maxHp = this.template.arrMaxhp[i];
                m.hp = m.maxHp;
                m.status = 5;
                if (m.status == 3) {
                    if (j % 5 == 0) {
                        m.hp = m.maxHp *= 200;
                    } else {
                        m.status = 0;
                    }
                } else if (m.status == 2) {
                    m.hp = m.maxHp *= 100;
                } else if (m.status == 1) {
                    m.hp = m.maxHp *= 10;
                }
                area[j].mobs.add(m);
                k++;
            }
        }
    }

    public int getId() {
        return id;
    }

    //random khu khong co BROLY
    public int getIndexMapNoBroly() {
        ArrayList<Integer> indexMap = new ArrayList<>();
        for (int i = 0; i < area.length; i++) {
            if (!area[i].haveBROLY) {
                indexMap.add(i);
            }
        }
        int rdIndex = Util.nextInt(0, indexMap.size());
        return indexMap.get(rdIndex);
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getPlanetId() {
        return planetId;
    }

    public void setPlanetId(byte planetId) {
        this.planetId = planetId;
    }

    public byte getBgId() {
        return bgId;
    }

    public void setBgId(byte bgId) {
        this.bgId = bgId;
    }

    public byte getBgType() {
        return bgType;
    }

    public void setBgType(byte bgType) {
        this.bgType = bgType;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public ArrayList<WayPoint> getWayPoints() {
        return wayPoints;
    }

    public ArrayList<Npc> getNpcs() {
        return npcs;
    }

    public void setNpcs(ArrayList<Npc> npcs) {
        this.npcs = npcs;
    }

    public ArrayList<Mob> getMobs() {
        return mobs;
    }

    public void setMobs(ArrayList<Mob> mobs) {
        this.mobs = mobs;
    }

    public ArrayList<ItemMap> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemMap> items) {
        this.items = items;
    }

    public void loadMapFromResource() {
        ByteArrayInputStream bai = null;
        DataInputStream dis = null;
        try {
            byte[] ab = GameScr.loadFile("res/map/" + this.id).toByteArray();
            bai = new ByteArrayInputStream(ab);
            dis = new DataInputStream(bai);
            this.template.tmw = this.ushort((short) dis.read());
            this.template.tmh = this.ushort((short) dis.read());
            this.template.maps = new char[dis.available()];
            int i;
            for (i = 0; i < this.template.tmw * this.template.tmh; i++) {
                this.template.maps[i] = (char) dis.readByte();
            }
            this.template.types = new int[this.template.maps.length];
            if (dis != null) {
                dis.close();
            }
            if (bai != null) {
                bai.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void update() {
        byte i;
        for (i = 0; i < this.area.length; ++i) {
            if (this.area[i] != null) {
                this.area[i].update();
                this.area[i].updateItemMap();
            }
        }
    }

    public void start() {
        if (this.runing) {
            this.close();
        }
        this.runing = true;
        this.threadUpdate.start();
    }

    public int ushort(short s) {
        return s & 0xFFFF;
    }

    public void close() {
        this.runing = false;
        byte i;
        for (i = 0; i < this.area.length; ++i) {
            if (this.area[i] != null) {
                this.area[i].close();
                this.area[i] = null;
            }
        }
        this.threadUpdate = null;
        this.template = null;
        this.LOCK = null;
    }
}
