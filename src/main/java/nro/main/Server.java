package nro.main;

import java.io.ByteArrayOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import nro.giftcode.GiftCodeManager;
import nro.io.Session;
import nro.clan.ClanManager;
import nro.task.TaskManager;
import nro.card.RadaCardManager;
import nro.item.ItemData;
import nro.map.Map;
import nro.map.MapTemplate;
import nro.player.Boss;
import nro.skill.SkillData;
import nro.skill.NoiTaiTemplate;

import java.util.Timer;
import java.util.TimerTask;
import nro.daihoi.DaiHoiService;


public class Server {
    private static Server instance;
    public static Object LOCK_MYSQL = new Object();
    public static boolean isDebug = false;
    public static MainManager manager;
    public Menu menu;
    public static Map[] maps;
    public static ByteArrayOutputStream[] cache = new ByteArrayOutputStream[7];
    public static SaveData runTime = new SaveData();
    public int idMapBroly[] = {5, 6, 10, 13, 19, 20, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38};
    public long zoneTimeEndNRSD = 0;
    public boolean openNRSD = false;
    public boolean openHiru = false;
    public boolean openMabu = false;
    //NGOC RONG NAMEC
    public int mapNrNamec[] = {-1,-1,-1,-1,-1,-1,-1};
    public String nameNrNamec[] = {"","","","","","",""};
    public byte zoneNrNamec[] = {-1,-1,-1,-1,-1,-1,-1};
    public String pNrNamec[] = {"","","","","","",""};
    public int idpNrNamec[] = {-1,-1,-1,-1,-1,-1,-1};
    public long timeNrNamec = 0;
    public boolean firstNrNamec = true;
    public long tOpenNrNamec = 0;
    //
    public short cDoanhTrai = 0;
    public short cKhiGas = 0;
    public short maxDoanhTrai = 200;
    public short maxKhiGas = 200;
    public boolean isPassDHVT = true;
    public boolean isCTG = false;
    public int idBossCall = 1000;
    //
    public byte isServer = (byte)1;
;

    public int mapKUKU = 0;
    public int khuKUKU = 0;
    public int mapMDD = 0;
    public int khuMDD = 0;
    public int mapRAMBO = 0;
    public int khuRAMBO = 0;

    public int mapTDST = 0;
    public int khuTDST = 0;
    public boolean supportNV = false;

    public void init() {
        ItemData.loadDataItem();
        menu = new Menu();
        manager = new MainManager();

        ClanManager.gI().init();
        SkillData.createSkill();
        TaskManager.gI().init();
        RadaCardManager.gI().init();
        GiftCodeManager.gI().init();

        cache[0] = GameScr.loadFile("res/cache/vhalloween/NRdata");
        cache[1] = GameScr.loadFile("res/cache/vhalloween/NRmap");
        cache[2] = GameScr.loadFile("res/cache/vhalloween/NRskill");
        cache[3] = GameScr.loadFile("res/cache/vhalloween/NRitem0");
        cache[4] = GameScr.loadFile("res/cache/vhalloween/NRitem1");
        cache[5] = GameScr.loadFile("res/cache/vhalloween/NRitem2");
        cache[6] = GameScr.loadFile("res/cache/vhalloween/NRitem100");
        this.maps = new Map[MapTemplate.arrTemplate.length];
        short i;

        for (i = 0; i < this.maps.length; ++i) {
            this.maps[i] = new Map(MapTemplate.arrTemplate[i]);
            this.maps[i].start();
        }
        
        NoiTaiTemplate.initNoiTai();
        Util.log("GET LIST NOI TAI XONG");
        //START AUTO SAVE
        runTime.start();
    }

    public static Server gI() {
        if (instance == null) {
            instance = new Server();
            instance.init();
        }
        return instance;
    }
    
    public static void main(String[] args) {
        Server.gI().run();
    }

    public void run() {
        ServerSocket listenSocket = null;
        try {
            Util.log("Start server...");
            listenSocket = new ServerSocket(Server.manager.port);
            int idBROLY = 1;
            for(int i = 0; i < idMapBroly.length; i++) {
                int xBroly = Util.getToaDoXBROLY(idMapBroly[i]);
                int yBroly = Util.getToaDoYBROLY(idMapBroly[i]);
                for(int j = 0; j < 2; j++) {
                    Boss _sBroly = new Boss(idBROLY, (byte)1, (short)xBroly, (short)yBroly);
                    idBROLY++;
                    int _rdZone = maps[idMapBroly[i]].getIndexMapNoBroly();
                    maps[idMapBroly[i]].area[_rdZone].bossMap.add(_sBroly);
                    maps[idMapBroly[i]].area[_rdZone].loadBROLY(_sBroly);
//                    Util.log("MAP NAME: " + maps[idMapBroly[i]].template.name + ", khu: " + _rdZone);
                }
            }
            Util.log("INIT BROLY XONG");
            int _rdZoneCooler = Util.nextInt(0, 5);
            Boss _cooler = new Boss(101, (byte)3, (short)243, (short)168);
            maps[107].area[_rdZoneCooler].bossMap.add(_cooler);
            maps[107].area[_rdZoneCooler].loadBossNoPet(_cooler);
            Util.log("INIT COOLER XONG " + maps[107].template.name + " KHU " + _rdZoneCooler);
            
            Timer timerBLACKXuatHien = new Timer();
            TimerTask ttBLACKXuatHien = new TimerTask() {
                public void run()
                {
                    int idMap = Util.nextInt(91, 93); //index 91 la map 92, index 92 la map 93
                    int IDZONE = Util.nextInt(0, maps[idMap].area.length);
                    short xBlack = 228;
                    if(idMap == 91) {
                        xBlack = 1296;
                    }
                    Boss _rBlack = new Boss(102, (byte)5, xBlack, (short)360);
                    maps[idMap].area[IDZONE].bossMap.add(_rBlack);
                    maps[idMap].area[IDZONE].loadBossNoPet(_rBlack);
                    Util.log("INIT BLACK XONG KHU " + IDZONE);
                    timerBLACKXuatHien.cancel();
                };
            };
            timerBLACKXuatHien.schedule(ttBLACKXuatHien, 30000);

            Timer timerKUKUX = new Timer();
            TimerTask ttKUKU = new TimerTask() {
                public void run()
                {
                    int idMap = 68;
                    short xKu = (short)758;
                    short yKu = (short)408;
                    idMap = Util.nextInt(68, 73);
                    int IDZONE = Util.nextInt(0, maps[idMap].area.length);
                    if(idMap == 69) {
                        xKu = (short)808;
                        yKu = (short)384;
                    } else if(idMap == 70) {
                        xKu = (short)301;
                        yKu = (short)360;
                    } else if(idMap == 71) {
                        xKu = (short)282;
                        yKu = (short)168;
                    } else if(idMap == 72) {
                        xKu = (short)1017;
                        yKu = (short)312;
                    }
                    Boss _rKuku = new Boss(110, (byte)7, xKu, yKu);
                    maps[idMap].area[IDZONE].bossMap.add(_rKuku);
                    maps[idMap].area[IDZONE].loadBossNoPet(_rKuku);
                    //
                    mapKUKU = idMap;
                    khuKUKU = IDZONE;
                    Util.log("INIT KUKU XONG MAP " + maps[idMap].template.name + ", " + IDZONE);

                    idMap = 64;
                    xKu = (short)794;
                    yKu = (short)312;
                    idMap = Util.nextInt(63, 68);
                    IDZONE = Util.nextInt(0, maps[idMap].area.length);
                    if(idMap == 65) {
                        xKu = (short)1246;
                        yKu = (short)240;
                    } else if(idMap == 63) {
                        xKu = (short)695;
                        yKu = (short)144;
                    } else if(idMap == 66) {
                        xKu = (short)993;
                        yKu = (short)360;
                    } else if(idMap == 67) {
                        xKu = (short)972;
                        yKu = (short)720;
                    } 
                    Boss _rMapDinh = new Boss(115, (byte)8, xKu, yKu);
                    maps[idMap].area[IDZONE].bossMap.add(_rMapDinh);
                    maps[idMap].area[IDZONE].loadBossNoPet(_rMapDinh);
                    mapMDD = idMap;
                    khuMDD = IDZONE;
                    Util.log("INIT _rMapDinh XONG MAP " + maps[idMap].template.name + ", " + IDZONE);

                    idMap = 73;
                    xKu = (short)324;
                    yKu = (short)168;
                    idMap = Util.nextInt(73, 77);
                    IDZONE = Util.nextInt(0, maps[idMap].area.length);
                    if(idMap == 74) {
                        xKu = (short)532;
                        yKu = (short)336;
                    } else if(idMap == 75) {
                        xKu = (short)515;
                        yKu = (short)216;
                    } else if(idMap == 76) {
                        xKu = (short)845;
                        yKu = (short)240;
                    } else if(idMap == 77) {
                        xKu = (short)701;
                        yKu = (short)288;
                    } 
                    Boss _rRambo = new Boss(120, (byte)9, xKu, yKu);
                    maps[idMap].area[IDZONE].bossMap.add(_rRambo);
                    maps[idMap].area[IDZONE].loadBossNoPet(_rRambo);
                    mapRAMBO = idMap;
                    khuRAMBO = IDZONE;
                    Util.log("INIT _rRambo XONG MAP " + maps[idMap].template.name + ", " + IDZONE);
                    timerKUKUX.cancel();
                };
            };
            timerKUKUX.schedule(ttKUKU, 60000);

            Timer timerTDST = new Timer();
            TimerTask ttTDST = new TimerTask() {
                public void run()
                {
                    int idMap = Util.nextInt(0, 3);
                    short xSo4 = (short)1392;
                    short ySo4 = (short)240;
                    short xSo3 = (short)1361;
//                    short ySo3 = (short)240;
                    short xSo2 = (short)1330;
//                    short ySo2 = (short)240;
                    short xSo1 = (short)1300;
//                    short ySo1 = (short)240;
                    short xSo0 = (short)1270;
//                    short ySo0 = (short)240;
                    if(idMap == 0) {
                        idMap = 81;
                    } else if(idMap == 1) {
                        idMap = 82;
                        xSo4 = (short)1446;
                        xSo3 = (short)1416;
                        xSo2 = (short)1386;
                        xSo1 = (short)1356;
                        xSo0 = (short)1326;
                        ySo4 = (short)336;
                    } else {
                        idMap = 78;
                        xSo4 = (short)150;
                        xSo3 = (short)180;
                        xSo2 = (short)210;
                        xSo1 = (short)240;
                        xSo0 = (short)270;
                        ySo4 = (short)360;
                    }
                    int IDZONE = Util.nextInt(0, maps[idMap].area.length);
                    Boss _So4 = new Boss(125, (byte)10, xSo4, ySo4);
                    Boss _So3 = new Boss(150, (byte)11, xSo3, ySo4);
                    Boss _So2 = new Boss(151, (byte)12, xSo2, ySo4);
                    Boss _So1 = new Boss(152, (byte)13, xSo1, ySo4);
                    Boss _So0 = new Boss(153, (byte)14, xSo0, ySo4);
                    
                    maps[idMap].area[IDZONE].bossMap.add(_So4);
                    maps[idMap].area[IDZONE].loadBossNoPet(_So4);
                    Service.gI().sendThongBaoServer("BOSS " + _So4.name + " vừa xuất hiện tại " + maps[idMap].template.name);
                    
                    maps[idMap].area[IDZONE].loadInfoBoss(_So3);
                    maps[idMap].area[IDZONE].bossMap.add(_So3);
                    Service.gI().sendThongBaoServer("BOSS " + _So3.name + " vừa xuất hiện tại " + maps[idMap].template.name);
                    maps[idMap].area[IDZONE].loadInfoBoss(_So2);
                    maps[idMap].area[IDZONE].bossMap.add(_So2);
                    Service.gI().sendThongBaoServer("BOSS " + _So2.name + " vừa xuất hiện tại " + maps[idMap].template.name);
                    maps[idMap].area[IDZONE].loadInfoBoss(_So1);
                    maps[idMap].area[IDZONE].bossMap.add(_So1);
                    Service.gI().sendThongBaoServer("BOSS " + _So1.name + " vừa xuất hiện tại " + maps[idMap].template.name);
                    maps[idMap].area[IDZONE].loadInfoBoss(_So0);
                    maps[idMap].area[IDZONE].bossMap.add(_So0);
                    Service.gI().sendThongBaoServer("BOSS " + _So0.name + " vừa xuất hiện tại " + maps[idMap].template.name);
                    mapTDST = idMap;
                    khuTDST = IDZONE;
                    Util.log("INIT TDST XONG MAP " + maps[idMap].template.name + ", " + IDZONE);
                };
            };
            timerTDST.schedule(ttTDST, 10000);

            Timer timerFIDE = new Timer();
            TimerTask ttFIDE = new TimerTask() {
                public void run()
                {
                    int IDZONE = Util.nextInt(0, maps[79].area.length);
                    Boss _rFide = new Boss(130, (byte)15, (short)224, (short)192);
                    maps[79].area[IDZONE].bossMap.add(_rFide);
                    maps[79].area[IDZONE].loadBossNoPet(_rFide);
                    Util.log("INIT _rFide XONG MAP " + maps[79].template.name + ", " + IDZONE);
                    //INIT ANDROID 1920
                    Service.gI().initAndroid1920();
                    //INIT ANDROID 151413
                    Service.gI().initAndroid15();
                    //INIT PICPOC
                    Service.gI().initPicPoc();
                    Service.gI().initXenGinder();
                    Service.gI().initXenVoDai();
                    //INIT CHILLED
                    Service.gI().initChilled();
                    Service.gI().initZamasu();
//                    Service.gI().initBillWhis();
                    timerFIDE.cancel();
                };
            };
            timerFIDE.schedule(ttFIDE, 15000);

            Service.gI().initNgocRongSaoDen();
            Service.gI().initMabu12h();
            Service.gI().initHirudegarn();
            DaiHoiService.gI().initDaiHoiVoThuat();
            Service.gI().initNgocRongNamec((byte)0);
            Service.gI().initMapYardrat();
            //INIT SUPPORT NHIEM VU
            Service.gI().supportTDST();

            Util.log("INIT MOB AUTO");
            for(Map _map: maps) {
                if(_map.area[0].mobs.size() > 0 && _map.id != 0 && _map.id != 7 && _map.id != 14 && (_map.id < 53 || _map.id > 62)) {
                    for(int i = 0; i < _map.area.length; i++) {
                        _map.area[i].updateMobAuto();
                    }
                }
            }
            Util.log("INIT MOB AUTO XONG");

            while (true) {
                Socket sc = listenSocket.accept();
                Util.log("Session connect: " + sc.getPort());
                new Session(sc);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
