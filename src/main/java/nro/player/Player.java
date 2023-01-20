package nro.player;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import nro.item.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import nro.clan.Clan;
import nro.clan.ClanManager;
import nro.task.ResetMonkeyTask;
import nro.task.Task;
import nro.task.TaskManager;
import nro.task.TaskService;
import nro.card.RadaCard;
import nro.card.RadaCardService;

import nro.map.Map;
import nro.map.Zone;
import nro.skill.NClass;
import nro.skill.Skill;
import nro.skill.SkillData;
import nro.skill.NoiTai;
import nro.skill.NoiTaiTemplate;
import nro.main.MainManager;
import nro.main.SQLManager;
import nro.main.Server;
import nro.main.Service;
import nro.main.Util;
import nro.io.Message;
import nro.io.Session;
import java.lang.Thread;
import nro.map.Mob;

public class Player {
    public long timeDelayBuyItem;

    public long getTimeDelayBuyItem() {
        return timeDelayBuyItem;
    }

    public void setTimeDelayBuyItem(long timeDelayBuyItem) {
        this.timeDelayBuyItem = timeDelayBuyItem;
    }
    public boolean isLogin = false;
    public Zone zone = null;
    public Session session;
    public int id;
    public Map map;
    public int menuNPCID = -1;
    public int menuID = -1;
    public short x;
    public short y;
    public int[] mapTransport = new int[14];
    public String name;
    public short taskId;
    public byte taskIndex;
    public byte gender;
    public short head;
    public long power;
    public long vang;
    public int ngocKhoa;
    public int ngoc;
    public int hpGoc;
    public int mpGoc;
    public int hp;
    public int mp;
    public int damGoc;
    public short defGoc;
    public byte critGoc;
    public byte critNr;
    public byte typePk = 0;
    public byte limitPower;
    public long tiemNang;
    public long sumTiemNang;
    public NClass nClass;
    public Clan clan;
//    public int clanid; //SERVER 2
    public Skill selectSkill;
    public short idSkillselect = 0;
    public byte[] KSkill = null;
    public byte[] OSkill = null;
    public short CSkill = -1;
    public byte maxluggage = 30;
    public byte maxBox = 30;
    public Item[] ItemBag = null;
    public Item[] ItemBox = null;
    public Item[] ItemBody = null;
    public ArrayList<Player> nearPlayers;
    public int mobAtk = -1;
    public boolean isdie = false;
    public boolean isMonkey = false;
    public boolean isDeTrung = false;
    public boolean isHuytSao = false;
    public boolean isTroi = false;
    public boolean isProtect = false;
    public boolean isBlind = false;
    public byte buffHuytSao = 0;
    public byte PEMCRIT = 0;

    public boolean isCharFreez = false; // check player co bi khong che hay khong troisssss
    public boolean isCharSleep = false; // check player bi thoi mien
    public boolean isCharSocola = false;
    public boolean isCharDCTT = false; //check player co bi choang boi dich chuuyen tuc thoi khong?
    public boolean isCharBlind = false;
    public boolean isCharStone = false; //check player co bi hoa da hay khong?
    public Mob MOBHOLD = null;
    public Player CHARHOLD = null;
    public Detu DETUHOLD = null;
    public byte downDAME = 0; //DUNG SAU KHI HET THOI MIEN THI GIAM DAME

    public boolean isNewPet = false; //dung khi giet super broly;
    public boolean isPet = false;
    public boolean isBOSS = false;
    public boolean isKaioken = false;
    public boolean isTTNL = false;
    public byte cPk = 0;
    public ArrayList<Skill> skill;
    public ArrayList<Skill> listSkill;
//    public ArrayList<Skill> LISTSKILLUSE = new ArrayList<>();
    public ArrayList<Amulet> listAmulet; //danh sach bua`
    public long CSkilldelay = 0;
    public static short[][] infoId = {{281, 361, 351}, {512, 513, 536}, {514, 515, 537}};
    public Detu detu;
    public byte havePet = 0;
    public byte statusPet = 3; //status de tu 0: di theo, 1 bao ve, 2 tan cong, 3 ve nha, 4 hop the
    public byte petfucus = 0;
    public int hpFull;
    public int mpFull;
    public int damFull;
    public byte critFull;
    public short defFull;
    public int NhapThe = 0;
    public int countCharge = 0;
    public int indexItemUse = -1;
    //PET SU KIEN DI THEO
//    public Item itemPet2 = null;
    public Detu pet;
    public boolean isPet2 = false;
    public byte pet2Follow = 0;

    public Item _itemUpStar = null;
    public int _indexUpStar = -1;
    public Item _itemUseEpStar = null;
    public int _indexEpStar = -1;
    public Item _itemDaBaoVe = null;
    public int _indexDaBaoVe = -1;
    public long _timeDapDo = 0;
    public boolean _checkDapDo = true;
    //CHECK USE ITEM
    public boolean _canUSEITEM = true;
    //GIAO DICH
    public ArrayList<Byte> _indexGiaoDich = new ArrayList<>();
    public int _goldGiaoDich = 0;
    public ArrayList<Item> _itemGiaoDich = new ArrayList<>();
    public Player _friendGiaoDich = null;
    public boolean _confirmGiaoDich = false;
    public boolean _isGiaoDich = false;
    //TU DONG LUYEN TAP
    public long timeEndTDLT = 0;
    //TIME BUFF DAU
    public long _TIMEBUFFDAU = 0;
    //CHECK GIAM DAME VA CHI SO MAP COOL
    public boolean isDownPointCold = false;
    public boolean isMapSaoDen = false;
    //TIME OPEN UI
    public long _TIMEOPENUI = 0;
    //NOI TAI
    public NoiTai noiTai = null;
    public byte countMoNoiTai = 0;
    public boolean canOpenNoiTai = true;
    public boolean upDameAfterNoiTai = false;
    public boolean upDameAfterKhi = false; //NOI TAI TANG DAME KHI HOA KHI
    //MAY DO CAPSULE
    public boolean useMayDoCapsule = false;
    public boolean useCuongNo = false;
    public boolean useBoHuyet = false;
    public boolean useBoKhi = false;
    public boolean useGiapXen = false;
    public boolean useThuocMo = false;
    public boolean useThucAn = false;
    //TIMER ITEM BUFFFFFFFFFF
    public Timer timerCSKB = null;
    public Timer timerCN = null;
    public Timer timerBH = null;
    public Timer timerBK = null;
    public Timer timerGX = null;
    public Timer timerTM = null; //thuoc mo
    public Timer timerTA = null;
    public long timeEndCSKB = 0;
    public long timeEndCN = 0;
    public long timeEndBH = 0;
    public long timeEndBK = 0;
    public long timeEndGX = 0;
    public long timeEndTM = 0;
    public long timeEndTA = 0;
    public short secondCSKB = 0;
    public short secondBH = 0;
    public short secondBK = 0;
    public short secondCN = 0;
    public short secondGX = 0;
    public short secondTM = 0;
    public short secondTA = 0;
    public int idTAUse = 0;
    public byte cItemBuff = 0;
    public ArrayList<Integer> idItemBuff = new ArrayList<>();
    //thanhvien
    public int thanhvien;
    public int sotien;
    //sukien
    public int pointSuKien;
    //nhanqua
    public int nhanqua;
    //TIME CHAT THE GIOI
    public long _TIMECHATTG = 0;
    //BANG HOI
    public byte rolePT = (byte) 2; //MAC DINH LA 2, 0 lA PC, 1 LA PHO BANG
    public long _TIMEXINDAU = 0;
    //CAY DAU THAN
    public byte levelTree = 1;
    public long lastTimeTree = 0;
    public byte currentBean = 0;
    public byte maxBean = 5;
    public boolean upMagicTree = false;
    //NHIEM VU
    public Task crrTask = null;
    //SET KICH HOAT
    public long timeX1ENDSKILL = 0;
    //THE LUC
    public short stamina = 0;
    public short maxStamina = 0;
    //QUAY NGOC MAY MAN
    public ArrayList<Item> ItemQuay = new ArrayList<>();
    public boolean openItemQuay = false;
    //QUAY NGOC MAY MAN
    public ArrayList<Item> ItemQuay2 = new ArrayList<>();
    public boolean openItemQuay2 = false;
    //NGOC RONG SAO DEN
    public boolean moveToSaoDen = false;
    public int[] mapSaoDen = new int[7];
    public byte xHPSaoDen = 0;
    public byte imgNRSD = 0;
    public long timeWINNRSD = 0;
    public long[] timeNRSD = new long[7];
    public long[] timeEndNRSD = new long[7];
    public ArrayList<Byte> indexNRSD = new ArrayList<>();
    //FIX COMBO SKILL
    public long timeCanSkill = 0;
    //PORATA 2
    public boolean isPorata2 = false;
    public Item _itemUseEpStar2 = null;
    public int _indexEpStar2 = -1;
    //DE TU MABU
    public boolean isMabu = false;
    public boolean isBerus = false;
    public boolean transfMabu = true;
    public boolean transfBerus = true;
    public boolean hasTrungMabu = false;
    //SKILL DE TRUNG
    public byte chimFollow = 0;
    public int dameChim = 0;
    public Timer timerDeTrung = null;
    //TIMER EFFECT
    public Timer timerEffectChat = null;
    //CONG TIEMNANG
    public boolean canIncreasePoint = true;
    public long timeIncreasePoint = 0;
    //ITEM DEO SAU LUNG
//    public Item itemLung = null;
    //DAI HOI VO THUAT
    public Timer timerDHVT = null;
    public boolean lockPK = false;
    //MABU 12H
    public Timer timerStone = null;
    public byte pointMabu = (byte) 0;
    public byte socolaMabu = (byte) 0;
    //CHIEN THUYEN TENNIS
    public boolean isTennis = false;
    //CAI TRANG x3, x4 DAME CHUONG CO BAN
    public long timeX3X4 = 0;
    public boolean isChuongX3X4 = false;
    public boolean noMiss = false;
    //RADA CARD
    public ArrayList<RadaCard> cards = new ArrayList<>();
    public byte cCardUse;
    public short idAura = (short) (-1);
    public int hpRada;
    public int kiRada;
    public int defRada;
    public int damRada;
    public int sdRada;
    //NGOC RONG NAMEC
    public int nrNamec = 0;
    public byte idNrNamecGo = -1;
    //GIAP LUYEN TAP
    public byte bonusGLT = (byte) 0;
    public Item giapLuyenTap = null;
    public Timer timerGLT = null;
    // TAU NGAM
    public boolean waitTransport = false;
    public Timer timeTauNgam = null;
    // TIME CHUYEN KHU VUC
    public long tSwapZone = 0;
    //TIME HOP THE
    public long tFusion = 0;
    public long tActionDe = 0;
    public Timer timerHSDe = null;
    // BAN BE
    public ArrayList<Friend> friends = new ArrayList<>();

    // TIMER PET
    public Timer detuUpPoint = null;
    public Timer detuAttack = null;
//    public Timer setSession = null;

    public Player() {
        this.ItemBag = null;
        this.ItemBody = null;
        this.ItemBox = null;
        this.nearPlayers = new ArrayList<>();
        this.skill = new ArrayList<>();
        this.listSkill = new ArrayList<>();
        this.listAmulet = new ArrayList<>();
    }

    public Zone getPlace() {
        return zone;
    }

    public Skill getSkill(int id) {
        for (Skill skl : this.skill) {
            if (skl.skillId == id) {
                return skl;
            }
        }
        return null;
    }

    //IS HAS ITEM BUFF
    public boolean isCanUseBuff(int idItem) {
        for (int i = 0; i < idItemBuff.size(); i++) {
            if (idItemBuff.get(i) == idItem) {
                return true;
            }
        }
        return false;
    }

    public boolean hasThucAnBuff(int idItem) {
        for (int i = 0; i < idItemBuff.size(); i++) {
            if (((idItemBuff.get(i) >= 663 && idItemBuff.get(i) <= 667) || idItemBuff.get(i) == 465 || idItemBuff.get(i) == 466 || idItemBuff.get(i) == 472 || idItemBuff.get(i) == 473) && idItemBuff.get(i) != idItem) {
                return true;
            }
        }
        return false;
    }

    public void removeIdBuff(int idItem) {
        for (int i = 0; i < idItemBuff.size(); i++) {
            if (idItemBuff.get(i) == idItem) {
                idItemBuff.remove(i);
            }
        }
    }

    public Skill getSkillById(int id) {
        for (Skill skl : this.listSkill) {
            if (this.isPet) {
                if (skl.tempSkillId == id) {
                    return skl;
                }
            } else {
                if (skl.skillId == id) {
                    return skl;
                }
            }
        }
        return null;
    }

    //CHECK BI KHONG CHE HAY KHONG
    public boolean checkPlayerBiKhongChe() {
        return isCharFreez && isCharSleep && isCharDCTT && isCharBlind && isCharStone;
    }

    //CHECK BI KHONG CHE HAY KHONG
    public boolean checkBiKhongChe() {
        if (isCharFreez || isCharSleep || isCharDCTT || isCharBlind || isCharStone) {
            return true;
        }
        return false;
    }

    //REMOVE ALL KHONG CHE
    public void removePlayerKhongChe() {
        this.isCharFreez = false;
        this.isCharSleep = false;
        this.isCharDCTT = false;
        this.isCharBlind = false;
        this.isCharStone = false;
    }

    public short getDefaultBody() {
        if (this.gender == 0) {
            return 57;
        } else if (this.gender == 1) {
            return 59;
        } else if (this.gender == 2) {
            return 57;
        }
        return -1;
    }

    public short getDefaultLeg() {
        if (this.gender == 0) {
            return 58;
        } else if (this.gender == 1) {
            return 60;
        } else if (this.gender == 2) {
            return 58;
        }
        return -1;
    }

    public short PartHead() {
        if (this.isPet && this.isMabu && this.transfMabu) {
            return 297;
        }
        if (this.isPet && this.isBerus && this.transfBerus) {
            return 508;
        }
        if (NhapThe == 1 && gender == (byte) 1) {
            if (this.ItemBody[5] != null && ((this.ItemBody[5].template.id >= 601 && this.ItemBody[5].template.id <= 603) || (this.ItemBody[5].template.id >= 639 && this.ItemBody[5].template.id <= 641))) {
                for (Item iad : ItemBody[5].entrys) {
                    if (ItemBody[5].id == iad.idTemp) {
                        return (short) iad.headTemp;
                    }
                }
            } else {
                if (this.isPorata2) {
                    return 873;
                } else {
                    return 391;
                }
            }
        } else if (NhapThe == 1 && (gender == (byte) 0 || gender == (byte) 2)) {
            if (this.ItemBody[5] != null && ((this.ItemBody[5].template.id >= 601 && this.ItemBody[5].template.id <= 603) || (this.ItemBody[5].template.id >= 639 && this.ItemBody[5].template.id <= 641))) {
                for (Item iad : ItemBody[5].entrys) {
                    if (ItemBody[5].id == iad.idTemp) {
                        return (short) iad.headTemp;
                    }
                }
            } else {
                if (this.isPorata2 && this.gender == (byte) 0) {
                    return 870;
                } else if (this.isPorata2 && this.gender == (byte) 2) {
                    return 867;
                } else {
                    return 383;
                }
            }
        } else if (this.ItemBody[5] != null && (this.ItemBody[5].template.part != (short) (-1) || (this.ItemBody[5].template.id >= 196 && this.ItemBody[5].template.id <= 210) || (this.ItemBody[5].template.id >= 227 && this.ItemBody[5].template.id <= 229)
                || (this.ItemBody[5].template.id >= 866 && this.ItemBody[5].template.id <= 868) || (this.ItemBody[5].template.id >= 872 && this.ItemBody[5].template.id <= 873))) {
//            return MapAvatarLv0ToPart(this.ItemBody[5].template.id);
            return this.ItemBody[5].template.part;
        } else if (this.ItemBody[5] != null && NhapThe == 0 && (this.ItemBody[5].template.id != 601 && this.ItemBody[5].template.id != 602 && this.ItemBody[5].template.id != 603
                && this.ItemBody[5].template.id != 639 && this.ItemBody[5].template.id != 640 && this.ItemBody[5].template.id != 641)) {
            if (this.id == 1 && this.ItemBody[5].template.id >= 592 && this.ItemBody[5].template.id <= 594) {
                return (short) 126;
            } else {
                for (Item iad : ItemBody[5].entrys) {
                    if (ItemBody[5].id == iad.idTemp) {
                        return (short) iad.headTemp;
                    }
                }
            }
        }
        return head;
    }

    public short iconIDHead() {
        if (NhapThe == 1 && gender == (byte) 1) {
            if (this.ItemBody[5] != null && ((this.ItemBody[5].template.id >= 601 && this.ItemBody[5].template.id <= 603) || (this.ItemBody[5].template.id >= 639 && this.ItemBody[5].template.id <= 641))) {
                return (short) this.ItemBody[5].template.iconID;
            } else {
                if (this.isPorata2) {
                    return 8026;
                } else {
                    return 3901;
                }
            }
        } else if (NhapThe == 1 && (gender == (byte) 0 || gender == (byte) 2)) {
            if (this.ItemBody[5] != null && ((this.ItemBody[5].template.id >= 601 && this.ItemBody[5].template.id <= 603) || (this.ItemBody[5].template.id >= 639 && this.ItemBody[5].template.id <= 641))) {
                return (short) this.ItemBody[5].template.iconID;
            } else {
                if (this.isPorata2 && this.gender == (byte) 0) {
                    return 7994;
                } else if (this.isPorata2 && this.gender == (byte) 2) {
                    return 7961;
                } else {
                    return 3821;
                }
            }
        } else if (this.ItemBody[5] != null && (this.ItemBody[5].template.part != (short) (-1) || (this.ItemBody[5].template.id >= 196 && this.ItemBody[5].template.id <= 210) || (this.ItemBody[5].template.id >= 227 && this.ItemBody[5].template.id <= 229)
                || (this.ItemBody[5].template.id >= 866 && this.ItemBody[5].template.id <= 868) || (this.ItemBody[5].template.id >= 872 && this.ItemBody[5].template.id <= 873))) {
            return (short) this.ItemBody[5].template.iconID;
        } else if (this.ItemBody[5] != null && NhapThe == 0 && (this.ItemBody[5].template.id != 601 && this.ItemBody[5].template.id != 602 && this.ItemBody[5].template.id != 603
                && this.ItemBody[5].template.id != 639 && this.ItemBody[5].template.id != 640 && this.ItemBody[5].template.id != 641)) {
            if (this.id == 1 && this.ItemBody[5].template.id >= 592 && this.ItemBody[5].template.id <= 594) {
                return 1450;
            } else {
                return (short) this.ItemBody[5].template.iconID;
            }
        }
        if (head == 64) {
            return 19;
        } else if (head == 30) {
            return 266;
        } else if (head == 31) {
            return 268;
        } else if (head == 9) {
            return 121;
        } else if (head == 29) {
            return 263;
        } else if (head == 32) {
            return 271;
        } else if (head == 6) {
            return 91;
        } else if (head == 27) {
            return 259;
        } else if (head == 28) {
            return 261;
        }
        return head;
    }

    public short MapAvatarLv0ToPart(int id) {
        short idreturn = 0;
        if (id == 196 || id == 197) {
            idreturn = 103;
        } else if (id == 198) {
            idreturn = 103;
        } else if (id == 199) {
            idreturn = 104;
        } else if (id == 200) {
            idreturn = 105;
        } else if (id == 201) {
            idreturn = 106;
        } else if (id == 202) {
            idreturn = 107;
        } else if (id == 203) {
            idreturn = 108;
        } else if (id == 204) {
            idreturn = 109;
        } else if (id == 205) {
            idreturn = 110;
        } else if (id == 206) {
            idreturn = 112;
        } else if (id == 207) {
            idreturn = 111;
        } else if (id == 208) {
            idreturn = 113;
        } else if (id == 209) {
            idreturn = 0;
        } else if (id == 210) {
            idreturn = 0;
        } else if (id == 227) {
            idreturn = 127;
        } else if (id == 228) {
            idreturn = 128;
        } else if (id == 229) {
            idreturn = 126;
        }
        return idreturn;
    }

    public short PartBody() {
        if (NhapThe == 1 && (gender == (byte) 0 || gender == (byte) 2 || gender == (byte) 1)) {
            return (short) (PartHead() + 1);
        }
        if (this.ItemBody[5] != null && this.ItemBody[5].template.level == 1 && this.ItemBody[5].template.part == (short) (-1) && NhapThe == 0 && (this.ItemBody[5].template.id != 601 && this.ItemBody[5].template.id != 602 && this.ItemBody[5].template.id != 603
                && this.ItemBody[5].template.id != 639 && this.ItemBody[5].template.id != 640 && this.ItemBody[5].template.id != 641)) {
            if (this.ItemBody[5].template.id == 545 || this.ItemBody[5].template.id == 546) {
                return (short) 458;
            } else if (this.ItemBody[5].template.id == 857 || this.ItemBody[5].template.id == 858) {
                return (short) 829;
            } else {
                return (short) (PartHead() + 1);
            }
        }
        if (this.isPet && this.isMabu && this.transfMabu) {
            return 298;
        }
        if (this.isPet && this.isBerus && this.transfBerus) {
            return 509;
        }
        if (this.ItemBody[0] == null) {
            return getDefaultBody();
        }
        return ItemTemplate.ItemTemplateID(this.ItemBody[0].id).part;
    }

    public short Leg() {
        if (NhapThe == 1 && (gender == (byte) 0 || gender == (byte) 2 || gender == (byte) 1)) {
            return (short) (PartHead() + 2);
        }
        if (this.ItemBody[5] != null && this.ItemBody[5].template.level == 1 && this.ItemBody[5].template.part == (short) (-1) && NhapThe == 0 && (this.ItemBody[5].template.id != 601 && this.ItemBody[5].template.id != 602 && this.ItemBody[5].template.id != 603
                && this.ItemBody[5].template.id != 639 && this.ItemBody[5].template.id != 640 && this.ItemBody[5].template.id != 641)) {
            if (this.ItemBody[5].template.id == 545 || this.ItemBody[5].template.id == 546) {
                return (short) 459;
            } else if (this.ItemBody[5].template.id == 857 || this.ItemBody[5].template.id == 858) {
                return (short) 830;
            } else {
                return (short) (PartHead() + 2);
            }
        }
        if (this.isPet && this.isMabu && this.transfMabu) {
            return 299;
        }
        if (this.isPet && this.isBerus && this.transfBerus) {
            return 510;
        }
        if (this.ItemBody[1] == null) {
            return getDefaultLeg();
        }
        return ItemTemplate.ItemTemplateID(this.ItemBody[1].id).part;
    }

    public int getMount() {
        for (Item item : ItemBag) {
            if (item != null && item.id != -1) {
                if (item.template.type == 23 || item.template.type == 24) {
                    return item.template.id;
                }
            }
        }
        return -1;
    }

    public int getHpFull() {
        int hp = hpFull;
        Skill skl = null;
        if (ItemBody[1] != null) {
            int hpTam = ItemBody[1].getParamItemByID(6);
            if (hpTam > 0) {
                hp += hpTam;
            } else {
                hp += ItemBody[1].getParamItemByID(22) * 1000;
            }
            hp += (int) (hp * Util.getPercentDouble(ItemBody[1].getParamItemByID(77))); //rp nr7s
        }
        if (ItemBody[0] != null) {
            hp += (int) (hp * Util.getPercentDouble(ItemBody[0].getParamItemByID(77))); //rp nr7s
        }
        if (ItemBody[2] != null) {
            hp += (int) (hp * Util.getPercentDouble(ItemBody[2].getParamItemByID(77))); //rp nr7s
        }
        if (ItemBody[3] != null) {
            hp += (int) (hp * Util.getPercentDouble(ItemBody[3].getParamItemByID(77))); //rp nr7s
        }
        if (ItemBody[4] != null) {
            hp += (int) (hp * Util.getPercentDouble(ItemBody[4].getParamItemByID(77))); //rp nr7s
        }
        if (ItemBody[5] != null) {
            if ((ItemBody[5].template.id >= 601 && ItemBody[5].template.id <= 603) || (ItemBody[5].template.id >= 639 && ItemBody[5].template.id <= 641)) { //CAI TRANG HOP THE
                if (this.NhapThe == 1) {
                    hp += (int) (hp * Util.getPercentDouble(ItemBody[5].getParamItemByID(77)));
                }
            } else {
                hp += (int) (hp * Util.getPercentDouble(ItemBody[5].getParamItemByID(77)));
            }
        }
        if (ItemBody[6] != null) {
            hp += (int) (hp * Util.getPercentDouble(ItemBody[6].getParamItemByID(77))); //rp nr7s
        }
        //CHECK SET KICH HOAT NAPPA
        if (this.getSetKichHoatFull() == (byte) 9) {
            hp = (int) (hp * 1.8);
        }
        if (this.timeEndNRSD[(byte) 1] > System.currentTimeMillis()) {
            hp = (int) (hp * 1.2);
        }
        //OPTION PORATA 2
        if (this.NhapThe == 1 && this.isPorata2) {
            int iPRT = getIndexItemBagByID(921);
            if (iPRT != -1) {
                hp += (int) (hp * Util.getPercentDouble(ItemBag[iPRT].getParamItemByID(77)));
            }
        }
        if (!this.isPet && ItemBody[8] != null) {
            hp += (int) (hp * Util.getPercentDouble(ItemBody[8].getParamItemByID(77)));
        }
        //pet
        if (this.pet2Follow == 1 && this.pet != null && this.ItemBody[7] != null) {
            hp += (int) (hp * Util.getPercentDouble(ItemBody[7].getParamItemByID(77)));
        }
        //RADA CARD
        hp += hpRada;
        //HP BO HUYET
        if (this.useBoHuyet) {
            hp = hp * 2;
        }
        if (this.isMonkey) {
            skl = this.getSkillById(13);
            hp = (int) Math.ceil(hp * Util.getPercentDouble((10 + skl.point + 3) * 10));
        }
        if (this.NhapThe == 1 && (this.detu.isBerus == false && this.detu.isMabu == false)) {
            hp += this.detu.isMabu ? (int) (this.detu.getHpFull() * 1) : this.detu.getHpFull();
        }
        if (this.NhapThe == 1 && this.detu.isMabu) {
            hp += this.detu.isMabu ? (int) (this.detu.getHpFull() * 1.2) : this.detu.getHpFull();
        }
        if (this.NhapThe == 1 && this.detu.isBerus) {
            hp += this.detu.isBerus ? (int) (this.detu.getHpFull() * 2) : this.detu.getHpFull();
        }
        //XHP NGOC RONG SAO DEN
        if (this.xHPSaoDen > (byte) 0 && (map.id >= 85 && map.id <= 91)) {
            hp = (int) (hp * (int) (this.xHPSaoDen));
        }

        if (this.isHuytSao) {
            skl = this.getSkillById(21);
            hp = (int) Math.ceil(hp * Util.getPercentDouble((10 + skl.point + 3) * 10));
        } else if (this.buffHuytSao > 0) {
            hp = (int) Math.ceil(hp * Util.getPercentDouble((10 + (int) this.buffHuytSao + 3) * 10));
        }

        //GIAM 50%hp map Cool
        if (this.isDownPointCold) {
            hp = (int) (hp / 2);
        }
        return hp;
    }

    public int getMpFull() {
        int mp = mpFull;
        if (ItemBody[3] != null) {
            int mpTam = ItemBody[3].getParamItemByID(7);
            if (mpTam > 0) {
                mp += mpTam;
            } else {
                mp += ItemBody[3].getParamItemByID(23) * 1000;
            }
            mp += (int) (mp * Util.getPercentDouble(ItemBody[3].getParamItemByID(103))); //rp nr6s
        }
        if (ItemBody[0] != null) {
            mp += (int) (mp * Util.getPercentDouble(ItemBody[0].getParamItemByID(103))); //rp nr6s
        }
        if (ItemBody[1] != null) {
            mp += (int) (mp * Util.getPercentDouble(ItemBody[1].getParamItemByID(103))); //rp nr6s
        }
        if (ItemBody[2] != null) {
            mp += (int) (mp * Util.getPercentDouble(ItemBody[2].getParamItemByID(103))); //rp nr6s
        }
        if (ItemBody[4] != null) {
            mp += (int) (mp * Util.getPercentDouble(ItemBody[4].getParamItemByID(103))); //rp nr6s
        }
        if (ItemBody[5] != null) {
            if ((ItemBody[5].template.id >= 601 && ItemBody[5].template.id <= 603) || (ItemBody[5].template.id >= 639 && ItemBody[5].template.id <= 641)) { //CAI TRANG HOP THE
                if (this.NhapThe == 1) {
                    mp += (int) (mp * Util.getPercentDouble(ItemBody[5].getParamItemByID(103))); //caitrang
                }
            } else {
                mp += (int) (mp * Util.getPercentDouble(ItemBody[5].getParamItemByID(103))); //caitrang
            }
        }
        if (ItemBody[6] != null) {
            mp += (int) (mp * Util.getPercentDouble(ItemBody[6].getParamItemByID(103))); //rp nr6s
        }
        if (this.timeEndNRSD[(byte) 1] > System.currentTimeMillis()) {
            mp = (int) (mp * 1.2);
        }
        //OPTION PORATA 2
        if (this.NhapThe == 1 && this.isPorata2) {
            int iPRT = getIndexItemBagByID(921);
            if (iPRT != -1) {
                mp += (int) (mp * Util.getPercentDouble(ItemBag[iPRT].getParamItemByID(103)));
            }
        }
        if (!this.isPet && ItemBody[8] != null) {
            mp += (int) (mp * Util.getPercentDouble(ItemBody[8].getParamItemByID(103)));
        }
        //pet
        if (this.pet2Follow == 1 && this.pet != null && this.ItemBody[7] != null) {
            mp += (int) (mp * Util.getPercentDouble(ItemBody[7].getParamItemByID(103)));
        }
        //RADA CARD
        mp += kiRada;
        //HP BO KHI
        if (this.useBoKhi) {
            mp = mp * 2;
        }
        if (this.NhapThe == 1 && (this.detu.isBerus == false && this.detu.isMabu == false)) {
            mp += this.detu.isMabu ? (int) (this.detu.getMpFull() * 1) : this.detu.getMpFull();
        }
        if (this.NhapThe == 1 && this.detu.isMabu) {
            mp += this.detu.isMabu ? (int) (this.detu.getMpFull() * 1.2) : this.detu.getMpFull();
        }
        if (this.NhapThe == 1 && this.detu.isBerus) {
            mp += this.detu.isBerus ? (int) (this.detu.getMpFull() * 2) : this.detu.getMpFull();
        }
        return mp;
    }

    public void ChatBunma() {
        try {
            while (true) {
                Calendar calendar = Calendar.getInstance();
                int sec = calendar.get(13);
                if ((sec % 20 == 0 || sec == 0)) {
                    for (int i = 0; i < PlayerManger.gI().conns.size(); i++) {
                        if (PlayerManger.gI().conns.get(i) != null && PlayerManger.gI().conns.get(i).player != null) {
                            Player player = PlayerManger.gI().conns.get(i).player;
                            if (player != null && player.map == map) {
                                player.zone.chat(player, "Wwow, sexy quá !");
                            }
                        }
                    }
                }
                Thread.sleep(1000L);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getDamFull() {
        int dam = damFull;
        if (ItemBody[2] != null) {
            dam += ItemBody[2].getParamItemByID(0);
            dam += (int) (dam * Util.getPercentDouble(ItemBody[2].getParamItemByID(50))); //rp nr3s
        }
        if (ItemBody[0] != null) {
            dam += (int) (dam * Util.getPercentDouble(ItemBody[0].getParamItemByID(50))); //rp nr3s
        }
        if (ItemBody[1] != null) {
            dam += (int) (dam * Util.getPercentDouble(ItemBody[1].getParamItemByID(50))); //rp nr3s
        }
        if (ItemBody[3] != null) {
            dam += (int) (dam * Util.getPercentDouble(ItemBody[3].getParamItemByID(50))); //rp nr3s
        }
        if (ItemBody[4] != null) {
            dam += (int) (dam * Util.getPercentDouble(ItemBody[4].getParamItemByID(50))); //rp nr3s
        }
        if (ItemBody[5] != null) {
            if ((ItemBody[5].template.id >= 601 && ItemBody[5].template.id <= 603) || (ItemBody[5].template.id >= 639 && ItemBody[5].template.id <= 641)) { //CAI TRANG HOP THE
                if (this.NhapThe == 1) {
                    dam += (int) (dam * Util.getPercentDouble(ItemBody[5].getParamItemByID(50)));
                }
            } else {
                dam += (int) (dam * Util.getPercentDouble(ItemBody[5].getParamItemByID(50)));
                if (ItemBody[5].template.id == 860 || ItemBody[5].template.id == 584) {
                    dam += (int) (dam * Util.getPercentDouble(ItemBody[5].getParamItemByID(117)));
                }
            }
        }
        if (ItemBody[6] != null) {
            if (ItemBody[6].id == 531 || ItemBody[6].id == 536) {
                dam = (int) (dam * 0.7);
            } else if (ItemBody[6].id == 530 || ItemBody[6].id == 535) {
                dam = (int) (dam * 0.8);
            } else {
                dam = (int) (dam * 0.9);
            }
            dam += (int) (dam * Util.getPercentDouble(ItemBody[6].getParamItemByID(50))); //rp nr3s
        } else {
            if (!this.isPet) {//KHONG PHAI LA PET
                if (bonusGLT == (byte) 3) {
                    dam = (int) (dam * 1.3);
                } else if (bonusGLT == (byte) 2) {
                    dam = (int) (dam * 1.2);
                } else if (bonusGLT == (byte) 1) {
                    dam = (int) (dam * 1.1);
                }
            }
        }
        if (this.timeEndNRSD[(byte) 0] > System.currentTimeMillis()) {
            dam = (int) (dam * 1.15);
        }
        //OPTION PORATA 2
        if (this.NhapThe == 1 && this.isPorata2) {
            int iPRT = getIndexItemBagByID(921);
            if (iPRT != -1) {
                dam += (int) (dam * Util.getPercentDouble(ItemBag[iPRT].getParamItemByID(50))); //rp nr3s
            }
        }
        if (!this.isPet && ItemBody[8] != null) {
            dam += (int) (dam * Util.getPercentDouble(ItemBody[8].getParamItemByID(50)));
        }
        //RADA CARD
        dam += damRada;
        if (sdRada > 0) {
            dam += (int) (dam * Util.getPercentDouble(sdRada));
        }
        //DAME CUONG NO
        if (this.useCuongNo) {
            dam = dam * 2;
        }
        if (this.useThucAn) {
            if ((this.idTAUse >= 663 && this.idTAUse <= 667) || (this.idTAUse == 465)) {
                dam = (int) (dam * 1.1);
            } else if (this.idTAUse == 466) {
                dam = (int) (dam * 1.15);
            } else if (this.idTAUse == 472) {
                dam = (int) (dam * 1.2);
            } else if (this.idTAUse == 473) {
                dam = (int) (dam * 1.25);
            }
        }
        if (this.useThuocMo) {
            dam = (int) (dam * 1.1);
        }
        if (this.pet2Follow == 1 && this.pet != null && this.ItemBody[7] != null) {
            dam += (int) (dam * Util.getPercentDouble(ItemBody[7].getParamItemByID(50)));
        }
        if (this.isMonkey) {
            Skill skl = this.getSkillById(13);
            dam = (int) Math.ceil(dam * Util.getPercentDouble((100 + skl.point + 8)));
        }
//        if(this.isDeTrung) {
//            Skill skl = this.getSkillById(12);
//            dam = (int)Math.ceil(dam*Util.getPercentDouble((100 + (skl.point + 9)*5)));
//            if(this.getSetKichHoatFull() == (byte)6) {
//                dam = (int)Math.ceil(dam*Util.getPercentDouble((100 + (skl.point + 9)*5)));
//            }
//        }
        if (this.NhapThe == 1 && (this.detu.isBerus == false && this.detu.isMabu == false)) {
            dam += this.detu.isMabu ? (int) (this.detu.getDamFull() * 1) : this.detu.getDamFull();
        }
        if (this.NhapThe == 1 && this.detu.isMabu) {
            dam += this.detu.isMabu ? (int) (this.detu.getDamFull() * 1.2) : this.detu.getDamFull();
        }
        if (this.NhapThe == 1 && this.detu.isBerus) {
            dam += this.detu.isBerus ? (int) (this.detu.getDamFull() * 2) : this.detu.getDamFull();
        }
        //CHECK MAP COOL
        if (this.isDownPointCold) {
            dam = (int) (dam / 2);
        }
        //CHECK BI NGUYEN RUA MAP MABU 12H
        if (this.socolaMabu == (byte) 1) {
            dam = (int) (dam * 0.85);
        }
        return dam;
    }

    public short getDefFull() {
        short def = defFull;
        if (ItemBody[0] != null) { //set ao
            def += ItemBody[0].getParamItemByID(47);
            def = (short) (def * (100 + ItemBody[0].getParamItemByID(94)) / 100); //rp nr2s
        }
        if (ItemBody[1] != null) { //set quan
            def = (short) (def * (100 + ItemBody[1].getParamItemByID(94)) / 100); //rp nr2s
        }
        if (ItemBody[2] != null) { //set gang
            def = (short) (def * (100 + ItemBody[2].getParamItemByID(94)) / 100); //rp nr2s
        }
        if (ItemBody[3] != null) { //set giay
            def = (short) (def * (100 + ItemBody[3].getParamItemByID(94)) / 100); //rp nr2s
        }
        if (ItemBody[4] != null) { //set rada
            def = (short) (def * (100 + ItemBody[4].getParamItemByID(94)) / 100); //rp nr2s
        }
        if (ItemBody[5] != null) { //CAI TRANG
            def = (short) (def * (100 + ItemBody[5].getParamItemByID(94)) / 100); //% giap cua cai trang
        }
        if (ItemBody[6] != null) { //giap luyen tap
            def = (short) (def * (100 + ItemBody[6].getParamItemByID(94)) / 100); //rp nr2s
        }
        //RADA CARD
        def += defRada;
        //OPTION PORATA 2
        if (this.NhapThe == 1 && this.isPorata2) {
            int iPRT = getIndexItemBagByID(921);
            if (iPRT != -1) {
                def += (int) (def * Util.getPercentDouble(ItemBag[iPRT].getParamItemByID(94))); //rp nr3s
            }
        }
        if (!this.isPet && ItemBody[8] != null) {
            def += (int) (def * Util.getPercentDouble(ItemBody[8].getParamItemByID(94)));
        }
        return def;
    }

    public byte getCritFull() {
        byte crit = critFull;
        crit += critNr;
        if (ItemBody[4] != null) {
            crit += ItemBody[4].getParamItemByID(14);
        }
        if (ItemBody[5] != null) {
            crit += ItemBody[5].getParamItemByID(14);
        }
        //OPTION PORATA 2
        if (this.NhapThe == 1 && this.isPorata2) {
            int iPRT = getIndexItemBagByID(921);
            if (iPRT != -1) {
                crit += ItemBag[iPRT].getParamItemByID(14); //rp nr3s
            }
        }
        if (!this.isPet && ItemBody[8] != null) {
            crit += ItemBody[8].getParamItemByID(14);
        }
        if (this.pet2Follow == 1 && this.pet != null && this.ItemBody[7] != null) {
            crit += ItemBody[7].getParamItemByID(14);
        }
        if (this.isMonkey) {
            crit = 100;
        }
        return crit;
    }

    public byte getSpeed() {
        if (this.isMonkey) {
            return 9;
        }
        return 7;
    }

    public void updateVangNgocHPMP() {
        Message m;
        try {
            m = new Message(-30);
            m.writer().writeByte(4);
            m.writer().writeLong(this.vang);
            m.writer().writeInt(this.ngoc);
            m.writer().writeByte(this.hp);
            m.writer().writeByte(this.mp);
            m.writer().writeInt(this.ngocKhoa);
            this.session.sendMessage(m);
            m.cleanup();
        } catch (Exception e) {
        }

    }

    public void updateItemBag() {
        Message msg;
        try {
            Item[] itemsBody = this.ItemBag;
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
                    msg.writer().writeByte(item.itemOptions.size());
                    for (ItemOption itemOption : item.itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }

            }
            this.session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateItemBox() {
        Message msg;
        try {
            Item[] itemsBody = this.ItemBox;
            msg = new Message(-35);
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
                    msg.writer().writeByte(item.itemOptions.size());
                    for (ItemOption itemOption : item.itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }

            }
            this.session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte getBoxNull() {
        byte num = 0;
        for (byte i = 0; i < this.ItemBox.length; ++i) {
            if (this.ItemBox[i] == null) {
                num++;
            }
        }
        return num;
    }

    public byte getBagNull() {
        byte num = 0;
        for (byte i = 0; i < this.ItemBag.length; ++i) {
            if (this.ItemBag[i] == null) {
                num++;
            }
        }
        return num;
    }

    public Item getIndexBag(int index) {
        if (index < this.ItemBag.length && index >= 0) {
            return this.ItemBag[index];
        }
        return null;
    }

    public Item getIndexBox(int index) {
        if (index < this.ItemBox.length && index >= 0) {
            return this.ItemBox[index];
        }
        return null;
    }

    protected Item getItemIdBag(int id) {
        for (int i = 0; i < this.ItemBag.length; ++i) {
            final Item item = this.ItemBag[i];
            if (item != null && item.id == id) {
                return item;
            }
        }
        return null;
    }

    // get index item bag theo id item
    public int getIndexItemBagByID(int id) {
        for (int i = 0; i < this.ItemBag.length; i++) {
            if (this.ItemBag[i] != null && this.ItemBag[i].template.id == id) {
                return i;
            }
        }
        return -1;
    }

    //get index item cung loai trong bag
    public byte getIndexBagid(int id) {
        byte i;
        Item item;
        for (i = 0; i < this.ItemBag.length; ++i) {
            item = this.ItemBag[i];
            if (item != null && item.id == id) {
                if ((item.id == 590 || (item.id >= 1066 && item.id <= 1070)) && item.quantity < 9999) {
                    return i;
                } else if (item.quantity < 99) {
                    return i;
                }
            }
        }
        return -1;
    }

    public byte getIndexBoxid(int id) {
        for (byte i = 0; i < this.ItemBox.length; ++i) {
            Item item = this.ItemBox[i];
            if (item != null && item.id == id) {
                if ((item.id == 590 || (item.id >= 1066 && item.id <= 1070)) && item.quantity < 9999) {
                    return i;
                } else if (item.quantity < 99) {
                    return i;
                }
            }
        }
        return -1;
    }

    protected int getIndexBagItem(int id) {
        for (int i = 0; i < this.ItemBag.length; ++i) {
            final Item item = this.ItemBag[i];
            if (item != null && item.id == id) {
                return i;
            }
        }
        return -1;
    }

    public byte getIndexBagNotItem() {
        byte i;
        Item item;
        for (i = 0; i < this.ItemBag.length; ++i) {
            item = this.ItemBag[i];
            if (item == null) {
                return i;
            }
        }

        return -1;
    }

    protected byte getIndexBoxNotItem() {
        for (byte i = 0; i < this.ItemBox.length; ++i) {
            final Item item = this.ItemBox[i];
            if (item == null) {
                return i;
            }
        }
        return -1;
    }

    protected byte getIndexBody() {
        for (byte i = 0; i < this.ItemBody.length; ++i) {
            final Item item = this.ItemBody[i];
            if (item == null) {
                return i;
            }
        }
        return -1;
    }

    //GET SO O CON TRONG TRONG BAG
    public byte getAvailableBag() {
        byte num = 0;
        for (int i = 0; i < this.ItemBag.length; ++i) {
            if (this.ItemBag[i] == null) {
                ++num;
            }
        }
        return num;
    }

    //LAY INDEX DO TRONG RUONG THEO TYPE ITEM
    public byte getIndexItemBoxByType(byte type) {
        for (byte i = 0; i < this.ItemBox.length; i++) {
            if (this.ItemBox[i] != null && this.ItemBox[i].template.type == type) {
                return i;
            }
        }
        return -1;
    }

    //LAY INDEX DO TRONG RUONG THEO TYPE ITEM
    public byte getIndexItemBagByType(byte type) {
        for (byte i = 0; i < this.ItemBag.length; i++) {
            if (this.ItemBag[i] != null && this.ItemBag[i].template.type == type) {
                return i;
            }
        }
        return -1;
    }

    //ADD ITEM TO BAG CUA PLAYER
    public Boolean addItemToBag(Item item) {
//        System.out.println("ID ITEM BUY : " + item.id);
//        System.out.println("QUANTITY ITEM BUY : " + item.quantity);
//        System.out.println("QUANTITY ITEM BUY : " + item.quantityTemp);
        try {
            byte index = this.getIndexBagid(item.id); //index item cung loai
            if (item.template.id == 521) { //TDLT
                if (index != -1) {
                    for (int i = 0; i < ItemBag[index].itemOptions.size(); i++) {
                        if (ItemBag[index].itemOptions.get(i).id == 1) {
                            ItemBag[index].itemOptions.get(i).param = (ItemBag[index].itemOptions.get(i).param + 20) > 2000 ? 2000 : (ItemBag[index].itemOptions.get(i).param + 20);
                        }
                    }
                } else {
                    index = this.getIndexBagNotItem();
                    if (getBagNull() <= 0) {
                        this.sendAddchatYellow("Hành trang không đủ chỗ trống!");
                        return false;
                    } else {
                        Item _item = new Item(item);
                        _item.quantity += (item.quantity - 1);
                        this.ItemBag[index] = _item;
                        return true;
                    }
                }
                return true;
            } else if (item.template.id == 590 || (item.template.id >= 1066 && item.template.id <= 1070)) { //BI KIEP
                if (index != -1) {
                    int countLeft = this.ItemBag[index].quantity - 9998;
                    if (countLeft > 0) {
                        byte index2 = this.getIndexBagNotItem();
                        if (getBagNull() <= 0) {
                            this.sendAddchatYellow("Hành trang không đủ chỗ trống!");
                            return false;
                        } else {
                            if (this.ItemBag[index].quantity < 9999) {
                                this.ItemBag[index].quantity = (this.ItemBag[index].quantity + item.quantity) > 9999 ? 9999 : (this.ItemBag[index].quantity + item.quantity);
                            }
                            Item _item = new Item(item);
                            _item.quantity += (countLeft - 1);
                            this.ItemBag[index2] = _item;
                            return true;
                        }
                    } else {
                        if (this.ItemBag[index].quantity < 9999) {
                            this.ItemBag[index].quantity = (this.ItemBag[index].quantity + item.quantity) > 9999 ? 9999 : (this.ItemBag[index].quantity + item.quantity);
                        }
                    }
                    return true;
                } else {
                    index = this.getIndexBagNotItem();
                    if (getBagNull() <= 0) {
                        this.sendAddchatYellow("Hành trang không đủ chỗ trống!");
                        return false;
                    } else {
                        Item _item = new Item(item);
                        this.ItemBag[index] = _item;
                        return true;
                    }
                }
            } else if (item.template.id == 933) { //MANH VO BONG TAI
                if (index != -1) {
                    for (int i = 0; i < ItemBag[index].itemOptions.size(); i++) {
                        if (ItemBag[index].itemOptions.get(i).id == 31) {
                            if (item.isDrop == (byte) 1) {
                                ItemBag[index].itemOptions.get(i).param = (ItemBag[index].itemOptions.get(i).param + 1) <= 9999 ? (ItemBag[index].itemOptions.get(i).param + 1) : 9999;
                            } else {
                                ItemBag[index].itemOptions.get(i).param = (ItemBag[index].itemOptions.get(i).param + 10) <= 9999 ? (ItemBag[index].itemOptions.get(i).param + 10) : 9999;
                            }
                        }
                    }
                } else {
                    index = this.getIndexBagNotItem();
                    if (getBagNull() <= 0) {
                        this.sendAddchatYellow("Hành trang không đủ chỗ trống!");
                        return false;
                    } else {
                        Item _item = new Item(item);
                        this.ItemBag[index] = _item;
                        return true;
                    }
                }
                return true;
            } else if (item.template.id >= 650 && item.template.id <= 662) { //ITEM HUY DIET
                index = this.getIndexBagNotItem();
                byte indexThucAn = getIndex99ThucAnHuyDiet();
                if (indexThucAn != -1 && this.ItemBag[indexThucAn].quantity >= 99) {
                    if (getBagNull() <= 0) {
                        this.sendAddchatYellow("Hành trang không đủ chỗ trống!");
                        return false;
                    } else {
                        //TRU THUC AN DI
                        this.ItemBag[indexThucAn] = null;
                        int bonusHuyDiet = Util.nextInt(0, 16);
                        if (item.template.type == (byte) 0) { //AO HUY DIET
                            Item _item = new Item(item);
                            _item.quantity += (item.quantity - 1);
                            if (bonusHuyDiet > 0) {
                                for (byte ix = 0; ix < _item.itemOptions.size(); ix++) {
                                    if (_item.itemOptions.get(ix).id == 47) {
                                        _item.itemOptions.get(ix).param += (int) (_item.itemOptions.get(ix).param * bonusHuyDiet / 100);
                                    }
                                }
                            }
                            this.ItemBag[index] = _item;
                        } else if (item.template.type == (byte) 1) { //QUAN HUY DIET
                            Item _item = new Item(item);
                            _item.quantity += (item.quantity - 1);
                            if (bonusHuyDiet > 0) {
                                for (byte ix = 0; ix < _item.itemOptions.size(); ix++) {
                                    if (_item.itemOptions.get(ix).id == 22) {
                                        _item.itemOptions.get(ix).param += (int) (_item.itemOptions.get(ix).param * bonusHuyDiet / 100);
                                    }
                                    if (_item.itemOptions.get(ix).id == 27) {
                                        _item.itemOptions.get(ix).param += (int) (_item.itemOptions.get(ix).param * bonusHuyDiet / 100);
                                    }
                                }
                            }
                            this.ItemBag[index] = _item;
                        } else if (item.template.type == (byte) 2) { //GANG HUY DIET
                            Item _item = new Item(item);
                            _item.quantity += (item.quantity - 1);
                            if (bonusHuyDiet > 0) {
                                for (byte ix = 0; ix < _item.itemOptions.size(); ix++) {
                                    if (_item.itemOptions.get(ix).id == 0) {
                                        _item.itemOptions.get(ix).param += (int) (_item.itemOptions.get(ix).param * bonusHuyDiet / 100);
                                    }
                                }
                            }
                            this.ItemBag[index] = _item;
                        } else if (item.template.type == (byte) 3) { //GIAY HUY DIET
                            Item _item = new Item(item);
                            _item.quantity += (item.quantity - 1);
                            if (bonusHuyDiet > 0) {
                                for (byte ix = 0; ix < _item.itemOptions.size(); ix++) {
                                    if (_item.itemOptions.get(ix).id == 23) {
                                        _item.itemOptions.get(ix).param += (int) (_item.itemOptions.get(ix).param * bonusHuyDiet / 100);
                                    }
                                    if (_item.itemOptions.get(ix).id == 28) {
                                        _item.itemOptions.get(ix).param += (int) (_item.itemOptions.get(ix).param * bonusHuyDiet / 100);
                                    }
                                }
                            }
                            this.ItemBag[index] = _item;
                        } else if (item.template.type == (byte) 4) { //NHAN HUY DIET
                            Item _item = new Item(item);
                            _item.quantity += (item.quantity - 1);
                            if (bonusHuyDiet > 0) {
                                for (byte ix = 0; ix < _item.itemOptions.size(); ix++) {
                                    if (_item.itemOptions.get(ix).id == 14) {
                                        _item.itemOptions.get(ix).param += (int) (_item.itemOptions.get(ix).param * bonusHuyDiet / 100);
                                    }
                                }
                            }
                            this.ItemBag[index] = _item;
                        }
                    }
                } else {
                    this.sendAddchatYellow("Không đủ thức ăn để đổi đồ!");
                    return false;
                }
                return true;
            } else if (item.isItemBackground()) {
                index = this.getIndexBagNotItem();
                byte indexMedal = getIndexItemByIdAndQuantiy(979, 10); //id = 979: huy chuong dong
                if (indexMedal != -1 && this.ItemBag[indexMedal].quantity >= 10) {
                    if (getBagNull() <= 0) {
                        this.sendAddchatYellow("Hành trang không đủ chỗ trống!");
                        return false;
                    } else {
                        //TRU HUY CHUONG DI
                        this.ItemBag[indexMedal].quantity -= 10;
                        if (this.ItemBag[indexMedal].quantity <= 0) {
                            this.ItemBag[indexMedal] = null;
                        }

                        Item _item = new Item(item);
                        _item.quantity += (item.quantity - 1);
                        _item.itemOptions.clear();
                        //TODO:
                        _item.itemOptions.add(new ItemOption(50, Util.nextInt(10, 26)));
                        _item.itemOptions.add(new ItemOption(77, Util.nextInt(10, 21)));
                        _item.itemOptions.add(new ItemOption(103, Util.nextInt(10, 21)));
                        _item.itemOptions.add(new ItemOption(14, Util.nextInt(1, 11)));
                        int day = Util.nextInt(1, 8);
                        _item.itemOptions.add(new ItemOption(93, day));
                        _item.timeHSD = System.currentTimeMillis() + (long) day * 86400000;
                        this.ItemBag[index] = _item;
                    }
                } else {
                    this.sendAddchatYellow("Không đủ huy chương đồng để đổi đồ!");
                    return false;
                }
                return true;
            }
            if (index != -1 && (item.id == 595 || item.template.type == 27 || item.template.type == 12 || item.template.type == 29 || item.template.type == 30 || item.template.type == 31 || item.template.type == 14
                    || item.template.type == 15 || item.template.type == 16 || item.template.type == 8 || item.template.type == 33 || item.template.type == 25)) { //12 nr
                int countLeft = this.ItemBag[index].quantity + item.quantity - 99;
                if (countLeft > 0) {
                    byte index2 = this.getIndexBagNotItem();
                    if (getBagNull() <= 0) {
                        this.sendAddchatYellow("Hành trang không đủ chỗ trống!");
                        return false;
                    } else {
                        if (this.ItemBag[index].quantity < 99) {
                            this.ItemBag[index].quantity = (this.ItemBag[index].quantity + item.quantity) > 99 ? 99 : (this.ItemBag[index].quantity + item.quantity);
                        }
                        Item _item = new Item(item);
                        _item.quantity += (countLeft - 1);
                        this.ItemBag[index2] = _item;
                        return true;
                    }
                } else {
                    if (this.ItemBag[index].quantity < 99) {
                        this.ItemBag[index].quantity = (this.ItemBag[index].quantity + item.quantity) > 99 ? 99 : (this.ItemBag[index].quantity + item.quantity);
                    }
                }
                return true;
            } else {
                index = this.getIndexBagNotItem();
                if (getBagNull() <= 0) {
                    this.sendAddchatYellow("Hành trang không đủ chỗ trống!");
                    return false;
                } else {
                    Item _item = new Item(item);
                    _item.quantity += (item.quantity - 1);
                    this.ItemBag[index] = _item;
                    return true;
                }
            }
        } catch (Exception var6) {
            var6.printStackTrace();
            return false;
        }

    }

    public void itemBagToBox(int index) {
        Item item = getIndexBag(index);
        if (item != null) {
            byte indexBox = getIndexBoxid(item.id);

            if (item.template.id == 590 || (item.template.id >= 1066 && item.template.id <= 1070)) { //BI KIEP
                if (indexBox != -1) {
                    int countLeft = this.ItemBox[indexBox].quantity - 9998;
                    if (countLeft > 0) {
                        byte indexBox2 = this.getIndexBoxNotItem();
                        if (getBoxNull() == 0) {
                            this.sendAddchatYellow("Rương đồ không đủ chỗ trống!");
                            return;
                        } else {
                            removeItemBag(index);
                            if (this.ItemBox[indexBox].quantity < 9999) {
                                this.ItemBox[indexBox].quantity = (this.ItemBox[indexBox].quantity + item.quantity) > 9999 ? 9999 : (this.ItemBox[indexBox].quantity + item.quantity);
                            }
                            Item _item = new Item(item);
                            _item.quantity += (countLeft - 1);
                            this.ItemBox[indexBox2] = _item;
                        }
                    } else {
                        removeItemBag(index);
                        if (this.ItemBox[indexBox].quantity < 9999) {
                            this.ItemBox[indexBox].quantity = (this.ItemBox[indexBox].quantity + item.quantity) > 9999 ? 9999 : (this.ItemBox[indexBox].quantity + item.quantity);
                        }
                    }
                } else {
                    indexBox = this.getIndexBoxNotItem();
                    if (getBoxNull() == 0) {
                        this.sendAddchatYellow("Rương đồ không đủ chỗ trống!");
                        return;
                    } else {
                        removeItemBag(index);
                        Item _item = new Item(item);
                        this.ItemBox[indexBox] = _item;
                    }
                }
            } else if (item.template.id == 933) { //MANH VO BONG TAI
                if (indexBox != -1) {
                    removeItemBag(index);
                    for (int i = 0; i < ItemBox[indexBox].itemOptions.size(); i++) {
                        if (ItemBox[indexBox].itemOptions.get(i).id == 31) {
                            ItemBox[indexBox].itemOptions.get(i).param = (ItemBox[indexBox].itemOptions.get(i).param + item.getParamItemByID(31)) <= 9999 ? (ItemBox[indexBox].itemOptions.get(i).param + item.getParamItemByID(31)) : 9999;
                        }
                    }
                } else {
                    indexBox = this.getIndexBoxNotItem();
                    if (getBoxNull() == 0) {
                        this.sendAddchatYellow("Rương đồ không đủ chỗ trống!");
                        return;
                    } else {
                        removeItemBag(index);
                        Item _item = new Item(item);
                        this.ItemBox[indexBox] = _item;
                    }
                }
            } else if (indexBox != -1 && (item.template.type == 27 || item.template.type == 6 || item.template.type == 12 || item.template.type == 14 || item.template.type == 15
                    || item.template.type == 16 || item.template.type == 22 || item.template.type == 23 || item.template.type == 24 || item.template.type == 25
                    || item.template.type == 29 || item.template.type == 30 || item.template.type == 31 || item.template.type == 33)) {
                int countLeft = ItemBox[indexBox].quantity + item.quantity - 99;

                if (countLeft > 0) {
                    byte indexBox2 = getIndexBoxNotItem();
                    if (getBoxNull() <= 0) {
                        this.sendAddchatYellow("Rương đồ không đủ chỗ trống");
                        return;
                    } else {
                        if (ItemBox[indexBox].quantity < 99) {
                            ItemBox[indexBox].quantity = (ItemBox[indexBox].quantity + item.quantity) > 99 ? 99 : (ItemBox[indexBox].quantity + item.quantity);
                        }
                        Item _item = new Item(item);
                        _item.quantity += (countLeft - 1);
                        this.ItemBox[indexBox2] = _item;
                    }
                } else {
                    if (ItemBox[indexBox].quantity < 99) {
                        ItemBox[indexBox].quantity = (ItemBox[indexBox].quantity + item.quantity) > 99 ? 99 : (ItemBox[indexBox].quantity + item.quantity);
                    }
                }
                removeItemBag(index);
//                Item item2 = ItemBox[indexBox];
//                item2.quantity += item.quantity;
            } else {
                if (getBoxNull() <= 0) {
                    this.sendAddchatYellow("Rương đồ không đủ chỗ trống");
                    return;
                }
                indexBox = getIndexBoxNotItem();
                removeItemBag(index);

                Item _item = new Item(item);
                _item.quantity += (item.quantity - 1);
                this.ItemBox[indexBox] = _item;
            }
            try {
                sortBox();
                sortBag();
            } catch (Exception e) {
            }
            Service.gI().updateItemBox(this);
            Service.gI().updateItemBag(this);
            //CHECK CANCEL TIMER GLT;
            if (item.template.type == (byte) 32 && giapLuyenTap != null) {
                giapLuyenTap.useNow = false;
                bonusGLT = (byte) 0;
                giapLuyenTap = null;
                //CANCEL TIMER GIAP LUYEN TAP
                Service.gI().loadPoint(this.session, this);
                timerGLT.cancel();
                timerGLT = null;
            }
//            item.quantity = item.quantityTemp;
        }
    }

    public void itemBoxToBag(int index) {
//        Item item = getIndexBox(index);
//        if(item != null)
//        {
//            byte indexBag = getIndexBagid(item.id);  
//            if ( indexBag != -1 && (item.id == 595 || item.id ==194)) {
//                removeItemBox(index);
//                Item item2 = ItemBag[indexBag];
//                item2.quantity += item.quantity;
//            } else {
//                if (getBagNull() <= 0) {
//                    this.sendAddchatYellow("Hành trang không đủ chỗ trống");
//                    return;
//                }
//                indexBag = getIndexBagNotItem();
//                removeItemBox(index);
//                ItemBag[indexBag] = item;
//            }
//            Service.gI().updateItemBag(this);
//            Service.gI().updateItemBox(this);
//        }
        Item item = getIndexBox(index);
        if (item != null) {
            byte indexBag = getIndexBagid(item.id);
            if (indexBag != -1 && (item.template.type == 27 || item.template.type == 6 || item.template.type == 12 || item.template.type == 14 || item.template.type == 15
                    || item.template.type == 16 || item.template.type == 22 || item.template.type == 23 || item.template.type == 24 || item.template.type == 25
                    || item.template.type == 29 || item.template.type == 30 || item.template.type == 31 || item.template.type == 33)) {
                int countLeft = ItemBag[indexBag].quantity + item.quantity - 99;
                if (countLeft > 0) {
                    byte indexBag2 = getIndexBagNotItem();
                    if (getBagNull() <= 0) {
                        this.sendAddchatYellow("Hành trang không đủ chỗ trống");
                        return;
                    } else {
                        if (ItemBag[indexBag].quantity < 99) {
                            ItemBag[indexBag].quantity = (ItemBag[indexBag].quantity + item.quantity) > 99 ? 99 : (ItemBag[indexBag].quantity + item.quantity);
                        }
                        Item _item = new Item(item);
                        _item.quantity += (countLeft - 1);
                        this.ItemBag[indexBag2] = _item;
                    }
                } else {
                    if (ItemBag[indexBag].quantity < 99) {
                        ItemBag[indexBag].quantity = (ItemBag[indexBag].quantity + item.quantity) > 99 ? 99 : (ItemBag[indexBag].quantity + item.quantity);
                    }
                }
                removeItemBox(index);
            } else {
                if (getBagNull() <= 0) {
                    this.sendAddchatYellow("Hành trang không đủ chỗ trống");
                    return;
                }
                indexBag = getIndexBagNotItem();
                removeItemBox(index);

                Item _item = new Item(item);
                _item.quantity += (item.quantity - 1);
                this.ItemBag[indexBag] = _item;
            }
            try {
                sortBox();
                sortBag();
            } catch (Exception e) {
            }
            Service.gI().updateItemBox(this);
            Service.gI().updateItemBag(this);
        }
    }

    public void useItemBody(Item item, short indexItemBag) {
        int index = -1;
        if (item != null && item.id != -1) {
            if (item.template.gender == this.gender || item.template.gender == 3) {
//            if (item.template.gender == this.gender || item.template.type == 5 || item.template.gender == 3) {
                if (item.template.level == 13 && this.power < 17000000000L) {
                    Service.gI().serverMessage(this.session, "Sức mạnh không đủ yêu cầu");
                    return;
                } else if (((item.template.id >= 650 && item.template.id <= 662) || (item.template.id >= 1048 && item.template.id <= 1062)) && this.power < item.template.strRequire * 1000L) {
                    Service.gI().serverMessage(this.session, "Sức mạnh không đủ yêu cầu");
                    return;
                }
                if (item.template.strRequire <= this.power) {
                    if (item.template.type >= 0 && item.template.type <= 10) {
                        index = item.template.type;
                    } else if (item.template.type == 11) {
                        index = 8;
                    } else if (item.template.type == 23 || item.template.type == 24) {
                        index = 9;
                    } else if (item.template.type == 32) {
                        index = 6;
                    }
                } else {
                    Service.gI().serverMessage(this.session, "Sức mạnh không đủ yêu cầu");
                }
            } else {
                Service.gI().serverMessage(this.session, "Sai hành tinh");
            }
        }
        if (index != -1) {
            if (this.ItemBody[index] != null) {
                this.ItemBag[indexItemBag] = this.ItemBody[index];
            } else {
                this.ItemBag[indexItemBag] = null;
            }
            this.ItemBody[index] = item;
            //NEU THAO CAI TRANG O MAP COOL

            //TANG CHI SO CHO ITEM SAU LUNG
            if (item.template.type == 11 && (this.ItemBody[index] != null || this.ItemBody[index].id != item.id)) {
                if (this.imgNRSD != (byte) 53 && this.imgNRSD != (byte) 37) {
                    Service.gI().setItemBagNew(this, item.id);
                }
            }
            //effchat marron
            int caitrang = this.ItemBody[index].template.id;
            if (this.ItemBody[index] != null && this.ItemBody[index].template.id == caitrang) {
                if (caitrang == 1103) {
                    ItemService.gI().sendEffectChat(this);
                } else if (caitrang == 455) {
                    ItemService.gI().sendEffectChatCaiTrangODo(this);
                } else if (caitrang >= 875 && caitrang <= 877 || caitrang == 883) {
                    ItemService.gI().sendEffectChatCaiTrangDep(this);
                }
            } else {
                if (this.timerEffectChat != null) {
                    this.timerEffectChat.cancel();
                    this.timerEffectChat = null;
                }
            }
            if (map.MapCold() && item.template.type == 5) {
                this.zone.upDownPointMapCool(this);
            }
        }
    }

    public void itemBodyToBag(int index) {
        if (getBagNull() <= 0) {
            sendAddchatYellow("Hành trang không đủ chỗ trống");
            return;
        }
        Item _item = this.ItemBody[index];
        if (_item != null) {
            byte indexBag = getIndexBagNotItem();
            //GIAM CHI SO CHO ITEM SAU LUNG
            if (_item.template.type == 11 && (this.ItemBody[index] != null || this.ItemBody[index].id == _item.id)) {
                if (this.imgNRSD != (byte) 53 && this.imgNRSD != (byte) 37) {
                    zone.resetBagClan(this);
                }
            }
            //end effchat marron
            if (_item.template.id == 1103) {
                if (this.timerEffectChat != null) {
                    this.timerEffectChat.cancel();
                    this.timerEffectChat = null;
                }
            }
            //REMOVE PET 2 NEU CO
            if (index == 7 && this.ItemBody[index] != null && this.ItemBody[index].id == _item.id) {
                this.pet2Follow = (byte) 0;
                zone.leavePETTT(this.pet);
                this.pet = null;
            }
            removeItemBody(index);
            this.ItemBag[indexBag] = _item;

            Service.gI().updateItemBody(this);
            Service.gI().updateItemBag(this);
            //CHECK INIT TIMER GLT;
            if (_item.template.type == (byte) 32) {
                int cMinute = _item.getParamItemByID(9);
                if (cMinute > 0) {
                    if (_item.id == 531 || _item.id == 536) {
                        bonusGLT = (byte) 3;
                    } else if (_item.id == 531 || _item.id == 536) {
                        bonusGLT = (byte) 2;
                    } else {
                        bonusGLT = (byte) 1;
                    }
                    _item.useNow = true;
                    giapLuyenTap = _item;
                    //TIMER GIAP LUYEN TAP
                    Service.gI().timeGiapLuyenTap(this, (byte) 0);
                }
            }
            Service.gI().loadPoint(session, this);
            //send HP to all player in map
            updateHpToPlayerInMap(this, this.hp);

            LOADCAITRANGTOME();
            //NEU THAO CAI TRANG O MAP COOL
            if (map.MapCold() && _item.template.type == 5) {
                this.zone.upDownPointMapCool(this);
            }
            //CHECK TIME CUA KHI NEU THAO SET KICH HOAT
            if (System.currentTimeMillis() >= this.timeX1ENDSKILL && this.getSetKichHoatFull() != (byte) 8 && this.isMonkey) {
                ResetMonkeyTask monkeyTask = new ResetMonkeyTask(this);
                Timer timer = new Timer();
                timer.schedule(monkeyTask, 0);
            }
        }
    }

    public void LOADCAITRANGTOME() {
        if (!this.isMonkey) {
//            System.out.println("ismonkey body to bag: " + this.isMonkey);
            Service.gI().loadCaiTrangTemp(this);
        } else {
            Message m = null;
            Skill skl = this.getSkillById(13);
            try {
                m = new Message(-90);
                m.writer().writeByte(0);
                m.writer().writeInt(this.id);
                //part head monkey
                if (skl.point == 1) {
                    m.writer().writeShort(192);
                } else if (skl.point == 2) {
                    m.writer().writeShort(195);
                } else if (skl.point == 3) {
                    m.writer().writeShort(196);
                } else if (skl.point == 4) {
                    m.writer().writeShort(199);
                } else if (skl.point == 5) {
                    m.writer().writeShort(197);
                } else if (skl.point == 6) {
                    m.writer().writeShort(200);
                } else if (skl.point == 7) {
                    m.writer().writeShort(198);
                }
                // part body monkey
                m.writer().writeShort(193);
                // part leg monkey
                m.writer().writeShort(194);
                // level mokey
                m.writer().writeByte(skl.point);
                m.writer().flush();
                this.session.sendMessage(m);
                m.cleanup();
            } catch (Exception e) {

            }
        }
    }

    public void itemBagToBody(byte index) throws IOException {
        Item item = this.ItemBag[index];
        if (item != null) {
            useItemBody(item, index);
            sortBag();
            Service.gI().updateItemBag(this);
            Service.gI().updateItemBody(this);
            //CHECK CANCEL TIMER GLT;
            if (item.template.type == (byte) 32 && giapLuyenTap != null) {
                giapLuyenTap.useNow = false;
                bonusGLT = (byte) 0;
                giapLuyenTap = null;
                //CANCEL TIMER GIAP LUYEN TAP
                Service.gI().loadPoint(this.session, this);
                timerGLT.cancel();
                timerGLT = null;
            }
            Service.gI().loadPoint(session, this);

            if (!this.isMonkey) {
                Service.gI().loadCaiTrangTemp(this);
            } else {
                Message m = null;
                Skill skl = this.getSkillById(13);
                try {
                    m = new Message(-90);
                    m.writer().writeByte(0);
                    m.writer().writeInt(this.id);
                    //part head monkey
                    if (skl.point == 1) {
                        m.writer().writeShort(192);
                    } else if (skl.point == 2) {
                        m.writer().writeShort(195);
                    } else if (skl.point == 3) {
                        m.writer().writeShort(196);
                    } else if (skl.point == 4) {
                        m.writer().writeShort(199);
                    } else if (skl.point == 5) {
                        m.writer().writeShort(197);
                    } else if (skl.point == 6) {
                        m.writer().writeShort(200);
                    } else if (skl.point == 7) {
                        m.writer().writeShort(198);
                    }
                    // part body monkey
                    m.writer().writeShort(193);
                    // part leg monkey
                    m.writer().writeShort(194);
                    // level mokey
                    m.writer().writeByte(skl.point);
                    m.writer().flush();
                    this.session.sendMessage(m);
                    m.cleanup();
                } catch (Exception e) {

                }
            }
            //CHECK TIME CUA KHI NEU THAO SET KICH HOAT
            if (System.currentTimeMillis() >= this.timeX1ENDSKILL && this.getSetKichHoatFull() != (byte) 8 && this.isMonkey) {
                ResetMonkeyTask monkeyTask = new ResetMonkeyTask(this);
                Timer timer = new Timer();
                timer.schedule(monkeyTask, 0);
            }
            //
        }
    }

    //ITEM TO PET
    public void itemBagToBodyPet(byte index) throws IOException {
        if (this.detu.power >= 1500000) {
            Item item = this.ItemBag[index];
            if (item.template.type != (byte) 32 && item.template.type != (byte) 11) {
                useItemBodyPet(item, index);
                Service.gI().updateItemBag(this);
                //CHECK CANCEL TIMER GLT;
                if (item.template.type == (byte) 32 && giapLuyenTap != null) {
                    giapLuyenTap.useNow = false;
                    bonusGLT = (byte) 0;
                    giapLuyenTap = null;
                    //CANCEL TIMER GIAP LUYEN TAP
                    Service.gI().loadPoint(this.session, this);
                    timerGLT.cancel();
                    timerGLT = null;
                }
                //        this.detu.head = this.detu.PartHead();
                Service.gI().LoadDeTu(this, (byte) 2); //load lai bang UI detu
                if (!this.detu.isMonkey) {
                    if ((item.template.type == (byte) 5 && item.template.checkIsCaiTrang()) || item.template.type != (byte) 5) {
                        for (Player p : this.zone.players) {
                            p.sendDefaultTransformToPlayer(this.detu);
                        }
                    }
                }
            } else {
                sendAddchatYellow("Không thể thực hiện");
            }
        } else {
            sendAddchatYellow("Sức mạnh để tử đạt 1tr5 mới có thể mặc đồ!");
        }
    }

    public void removeItemBodyPet(byte index) {
        this.detu.ItemBody[index] = null;
    }

    public void itemBodyToBagPet(byte index) {
        if (getBagNull() <= 0) {
            sendAddchatYellow("Hành trang không đủ chỗ trống");
            return;
        }
        Item _item = this.detu.ItemBody[index];
        if (_item != null) {
            byte indexBag = getIndexBagNotItem();
            removeItemBodyPet(index);
            this.ItemBag[indexBag] = _item;
            Service.gI().updateItemBag(this);
//            this.detu.head = this.detu.PartHead();
            //NEU THAO CAI TRANG DETU O MAP COOL
            if (map.MapCold() && _item.template.type == 5) {
                this.zone.upDownPointPETMapCool(this);
            }
            Service.gI().LoadDeTu(this, (byte) 2);
            if (!this.detu.isMonkey) {
                for (Player p : this.zone.players) {
                    p.sendDefaultTransformToPlayer(this.detu);
                }
            }
            //CHECK TIME CUA KHI NEU THAO SET KICH HOAT
            if (System.currentTimeMillis() >= this.detu.timeX1ENDSKILL && this.detu.getSetKichHoatFull() != (byte) 8 && this.detu.isMonkey) {
                Timer timerPetKhi = new Timer();
                Player _player = this;
                TimerTask petKhi = new TimerTask() {
                    public void run() {
                        if (petfucus == 1) {
                            Message m = null;
                            try {
//                                System.out.println("pet het khi");
                                detu.isMonkey = false;
                                detu.hp = detu.hp > detu.getHpFull() ? detu.getHpFull() : detu.hp;
                                updateHpDetu(_player, detu);
                                //send default part to all player
                                for (Player p : zone.players) {
                                    p.sendDefaultTransformToPlayer(detu);
                                }

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
                timerPetKhi.schedule(petKhi, 0);
            }
        }
    }

    public void useItemBodyPet(Item item, byte indexItemBag) {
        byte index = -1;
        if (item != null && item.id != -1) {
            if (item.template.type == 4 && !chkRadaSetKichHoat(item)) {
                sendAddchatYellow("Hành tinh đệ tử không phù hợp");
                return;
            }
            if (item.template.gender == this.detu.gender || item.template.gender == 3) {
//            if (item.template.gender == this.detu.gender || item.template.type == 5 || item.template.gender == 3) {
                if (item.template.level == 13 && this.detu.power < 17000000000L) {
                    Service.gI().serverMessage(this.session, "Sức mạnh đệ tử không đủ yêu cầu");
                    return;
                } else if (((item.template.id >= 650 && item.template.id <= 662) || (item.template.id >= 1048 && item.template.id <= 1062)) && this.detu.power < item.template.strRequire * 1000L) {
                    Service.gI().serverMessage(this.session, "Sức mạnh đệ tử không đủ yêu cầu");
                    return;
                }
                if (item.template.strRequire <= this.detu.power) {
                    if (item.template.type >= 0 && item.template.type <= 11) {
                        index = item.template.type;
                    }
                    if (item.template.type == 32) {
                        index = 6;
                    }
                } else {
                    sendAddchatYellow("Sức mạnh đệ tử không đủ yêu cầu");
                }
            } else {
                sendAddchatYellow("Hành tinh đệ tử không phù hợp");
            }
        }
        if (index != -1) {
            if (this.detu.ItemBody[index] != null) {
                this.ItemBag[indexItemBag] = this.detu.ItemBody[index];
            } else {
                this.ItemBag[indexItemBag] = null;
            }
            this.detu.ItemBody[index] = item;
            //NEU THAO CAI TRANG DETU O MAP COOL
            if (map.MapCold() && item.template.type == 5) {
                this.zone.upDownPointPETMapCool(this);
            }
            //CHECK TIME CUA KHI NEU THAO SET KICH HOAT
            if (System.currentTimeMillis() >= this.detu.timeX1ENDSKILL && this.detu.getSetKichHoatFull() != (byte) 8 && this.detu.isMonkey) {
                Timer timerPetKhi = new Timer();
                Player _player = this;
                TimerTask petKhi = new TimerTask() {
                    public void run() {
                        if (petfucus == 1) {
                            Message m = null;
                            try {
//                                System.out.println("pet het khi");
                                detu.isMonkey = false;
                                detu.hp = detu.hp > detu.getHpFull() ? detu.getHpFull() : detu.hp;
                                updateHpDetu(_player, detu);
                                //send default part to all player
                                for (Player p : zone.players) {
                                    p.sendDefaultTransformToPlayer(detu);
                                }

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
                timerPetKhi.schedule(petKhi, 0);
            }
        }
    }

    //tru so luong item;
    public void updateQuantityItemBag(int index, int count) {
        Message msg;
        if (this.ItemBag[index].quantity >= count) {
            this.ItemBag[index].quantity -= count;
            if (this.ItemBag[index].quantity <= 0) {
                this.ItemBag[index] = null;
                try {
                    msg = new Message(-36);
                    msg.writer().writeByte(2);
                    msg.writer().writeByte((byte) index);
                    msg.writer().writeInt(0);
                    this.session.sendMessage(msg);
                    msg.cleanup();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    msg = new Message(-36);
                    msg.writer().writeByte(2);
                    msg.writer().writeByte((byte) index);
                    msg.writer().writeInt(this.ItemBag[index].quantity);
                    this.session.sendMessage(msg);
                    msg.cleanup();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateQuantityItemBag2(byte index, int count) {
        Message msg;
        if (this.ItemBag[index].quantity >= count) {
            this.ItemBag[index].quantity -= count;
            if (this.ItemBag[index].quantity == 0) {
                this.ItemBag[index] = null;
                try {
                    msg = new Message(-36);
                    msg.writer().writeByte(2);
                    msg.writer().writeByte(index);
                    msg.writer().writeInt(0);
                    this.session.sendMessage(msg);
                    msg.cleanup();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    msg = new Message(-36);
                    msg.writer().writeByte(2);
                    msg.writer().writeByte(index);
                    msg.writer().writeInt((byte) this.ItemBag[index].quantity);
                    this.session.sendMessage(msg);
                    msg.cleanup();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // END ITEM TO PET
    public void removeItemBag(int index) {
        this.ItemBag[index] = null;
    }

    public void loadBox() throws IOException {
        Message m;
        m = new Message(-35);
        m.writer().writeByte(0);
        m.writer().writeByte(this.ItemBox.length);
        for (int i = 0; i < this.ItemBox.length; i++) {
            m.writer().writeShort(this.ItemBox[i].id);
            m.writer().writeInt(this.ItemBox[i].quantity);
            m.writer().writeUTF(this.ItemBox[i].getInfo());
            m.writer().writeUTF(this.ItemBox[i].getContent());
            m.writer().writeByte(this.ItemBox[i].itemOptions.size());
            for (int j = 0; j < this.ItemBox[i].itemOptions.size(); j++) {
                m.writer().writeByte(0);
                m.writer().writeShort(0);
            }
        }
        this.session.sendMessage(m);
        m.cleanup();
    }

    public void UpdateSMTN(byte type, long amount) {
        Message msg;
        try {
            msg = new Message(-3);
            msg.writer().writeByte(type);
            msg.writer().writeInt((int) (amount > Integer.MAX_VALUE ? Integer.MAX_VALUE : amount));
            this.session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void openBox() throws IOException {
        Message m;
        m = new Message(-35);
        m.writer().writeByte(1);
        this.session.sendMessage(m);
        m.cleanup();
    }

//    public synchronized void removeItemBag(byte index, int quantity) {
//        Item item = getIndexBag(index);
//        try {
//            item.quantity -= quantity;
//            Message m = new Message(69);
//            m.writer().writeByte(index);
//            m.writer().writeShort(quantity);
//            m.writer().flush();
//            this.session.sendMessage(m);
//            m.cleanup();
//            if (item.quantity <= 0) {
//                this.ItemBag[index] = null;
//            }
//        } catch (IOException iOException) {
//        }
//    }
    public void removeItemBody(int index) {
        this.ItemBody[index] = null;
    }
//    

    public void removeItemBox(int index) {
        this.ItemBox[index] = null;
    }

    public void increasePoint(byte type, short point) {
        if (this.canIncreasePoint && (System.currentTimeMillis() - this.timeIncreasePoint) > 500) {
            this.canIncreasePoint = false;
            this.timeIncreasePoint = System.currentTimeMillis();
            if (point <= 0) {
                this.canIncreasePoint = true;
                return;
            }
            long tiemNangUse = 0;
            if (type == 0) {
                if (point > (short) 100) {
                    point = (short) 100;
                }
                int pointHp = (int) point * 20;
                tiemNangUse = (int) point * (2 * (this.hpGoc + 1000) + pointHp - 20) / 2;
                if ((this.hpGoc + pointHp) <= getHpMpLimit()) {
                    if (this.tiemNang >= tiemNangUse) {
                        this.tiemNang -= tiemNangUse;
                        if (this.taskId == (short) 3 && this.crrTask.index == (byte) 0) {
                            TaskService.gI().updateCountTask(this);
                        }
                        this.sumTiemNang += tiemNangUse;
                        hpGoc += pointHp;
                        hpFull = hpGoc;
                    } else {
                        Service.gI().serverMessage(this.session, "Bạn không đủ tiềm năng");
                        this.canIncreasePoint = true;
                        return;
                    }
//                    if (useTiemNang(tiemNangUse)) {
//                        this.sumTiemNang += tiemNangUse;
//                        hpGoc += pointHp;
//                        hpFull = hpGoc ;
//                    }
                } else {
                    Service.gI().serverMessage(this.session, "Vui lòng mở giới hạn sức mạnh");
                    this.canIncreasePoint = true;
                    return;
                }
            }
            if (type == 1) {
                if (point > (short) 100) {
                    point = (short) 100;
                }
                int pointMp = (int) point * 20;
                tiemNangUse = (int) point * (2 * (this.mpGoc + 1000) + pointMp - 20) / 2;
                if ((this.mpGoc + pointMp) <= getHpMpLimit()) {
                    if (this.tiemNang >= tiemNangUse) {
                        this.tiemNang -= tiemNangUse;
                        if (this.taskId == (short) 3 && this.crrTask.index == (byte) 0) {
                            TaskService.gI().updateCountTask(this);
                        }
                        this.sumTiemNang += tiemNangUse;
                        mpGoc += pointMp;
                        mpFull = mpGoc;
                    } else {
                        Service.gI().serverMessage(this.session, "Bạn không đủ tiềm năng");
                        this.canIncreasePoint = true;
                        return;
                    }
//                    if (useTiemNang(tiemNangUse)) {
//                        this.sumTiemNang += tiemNangUse;
//                        mpGoc += pointMp;
//                        mpFull = mpGoc;
//                    }
                } else {
                    Service.gI().serverMessage(this.session, "Vui lòng mở giới hạn sức mạnh");
                    this.canIncreasePoint = true;
                    return;
                }
            }
            if (type == 2) {
                if (point > (short) 100) {
                    point = (short) 100;
                }
                tiemNangUse = (int) point * (2 * this.damGoc + (int) point - 1) / 2 * 100;
                if ((this.damGoc + (int) point) <= getDamLimit()) {
                    if (this.tiemNang >= tiemNangUse) {
                        this.tiemNang -= tiemNangUse;
                        if (this.taskId == (short) 3 && this.crrTask.index == (byte) 0) {
                            TaskService.gI().updateCountTask(this);
                        }
                        this.sumTiemNang += tiemNangUse;
                        damGoc += (int) point;
                        damFull = damGoc;
                    } else {
                        Service.gI().serverMessage(this.session, "Bạn không đủ tiềm năng");
                        this.canIncreasePoint = true;
                        return;
                    }
//                    if (useTiemNang(tiemNangUse)) {
//                        this.sumTiemNang += tiemNangUse;
//                        damGoc += point;
//                        damFull = damGoc;
//                    }
                } else {
                    Service.gI().serverMessage(this.session, "Vui lòng mở giới hạn sức mạnh");
                    this.canIncreasePoint = true;
                    return;
                }
            }
            if (type == 3) {
                point = (short) 1;
                tiemNangUse = 2 * (this.defGoc + 5) / 2 * 100000;
                if ((this.defGoc + (int) point) <= getDefLimit()) {
                    if (this.tiemNang >= tiemNangUse) {
                        this.tiemNang -= tiemNangUse;
                        if (this.taskId == (short) 3 && this.crrTask.index == (byte) 0) {
                            TaskService.gI().updateCountTask(this);
                        }
                        this.sumTiemNang += tiemNangUse;
                        defGoc += (int) point;
                        defFull = defGoc;
                    } else {
                        Service.gI().serverMessage(this.session, "Bạn không đủ tiềm năng");
                        this.canIncreasePoint = true;
                        return;
                    }
//                    if (useTiemNang(tiemNangUse)) {
//                        this.sumTiemNang += tiemNangUse;
//                        defGoc += point;
//                       defFull = defGoc;
//                    }
                } else {
                    Service.gI().serverMessage(this.session, "Vui lòng mở giới hạn sức mạnh");
                    this.canIncreasePoint = true;
                    return;
                }
            }
            if (type == 4) {
                point = (short) 1;
                tiemNangUse = 50000000L;
                for (int i = 0; i < this.critGoc; i++) {
                    tiemNangUse *= 5L;
                }
                if ((this.critGoc + (int) point) <= getCrifLimit()) {
                    if (this.tiemNang >= tiemNangUse) {
                        this.tiemNang -= tiemNangUse;
                        if (this.taskId == (short) 3 && this.crrTask.index == (byte) 0) {
                            TaskService.gI().updateCountTask(this);
                        }
                        this.sumTiemNang += tiemNangUse;
                        critGoc += (int) point;
                        critFull = critGoc;
                    } else {
                        Service.gI().serverMessage(this.session, "Bạn không đủ tiềm năng");
                        this.canIncreasePoint = true;
                        return;
                    }
//                    if (useTiemNang(tiemNangUse)) {
//                        this.sumTiemNang += tiemNangUse;
//                        critGoc += point;
//                        critFull = critGoc;
//                    }
                } else {
                    Service.gI().serverMessage(this.session, "Vui lòng mở giới hạn sức mạnh");
                    this.canIncreasePoint = true;
                    return;
                }
            }
            this.canIncreasePoint = true;
            Service.gI().loadPoint(this.session, this);
        } else {
            Service.gI().loadPoint(this.session, this);
        }
    }

    public boolean useTiemNang(long tiemNang) {
        if (this.tiemNang < tiemNang) {
            Service.gI().serverMessage(this.session, "Bạn không đủ tiềm năng");
            return false;
        }
        if (this.tiemNang >= tiemNang) {
            this.tiemNang -= tiemNang;
            if (this.taskId == (short) 3 && this.crrTask.index == (byte) 0) {
                TaskService.gI().updateCountTask(this);
            }
            return true;
        }
        return false;
    }

    public int getPowerLimit() {
        if (limitPower == 0) {
            return 18;
        }
        if (limitPower == 1) {
            return 20;
        }
        if (limitPower == 2) {
            return 30;
        }
        if (limitPower == 3) {
            return 40;
        }
        if (limitPower == 4) {
            return 50;
        }
        if (limitPower == 5) {
            return 60;
        }
        if (limitPower == 6) {
            return 70;
        }
        if (limitPower == 7) {
            return 80;
        }
        if (limitPower == 8) {
            return 90;
        }
        if (limitPower == 9) {
            return 100;
        }
        if (limitPower == 10) {
            return 110;
        }
        if (limitPower == 11) {
            return 110;
        }
        if (limitPower == 12) {
            return 110;
        }
        if (limitPower == 13) {
            return 110;
        }
        if (limitPower == 14) {
            return 110;
        }
        if (limitPower == 15) {
            return 110;
        }
        if (limitPower == 16) {
            return 110;
        }
        if (limitPower == 17) {
            return 110;
        }
        if (limitPower == 18) {
            return 110;
        }
        if (limitPower == 19) {
            return 110;
        }
        if (limitPower == 20) {
            return 110;
        }
        if (limitPower == 22) {
            return 110;
        }
        if (limitPower == 23) {
            return 110;
        }
        if (limitPower == 24) {
            return 110;
        }
        if (limitPower == 25) {
            return 110;
        }
        if (limitPower == 26) {
            return 110;
        }
        if (limitPower == 27) {
            return 110;
        }
        if (limitPower == 28) {
            return 110;
        }
        return 0;
    }

    public int getHpMpLimit() {
        if (limitPower == 0) {
            return 220000;
        }
        if (limitPower == 1) {
            return 240000;
        }
        if (limitPower == 2) {
            return 300000;
        }
        if (limitPower == 3) {
            return 350000;
        }
        if (limitPower == 4) {
            return 400000;
        }
        if (limitPower == 5) {
            return 450000;
        }
        if (limitPower == 6) {
            return 500000;
        }
        if (limitPower == 7) {
            return 550000;
        }
        if (limitPower == 8) {
            return 600000;
        }
        if (limitPower == 9) {
            return 650000;
        }
        if (limitPower == 10) {
            return 700000;
        }
        if (limitPower == 11) {
            return 700000;
        }
        if (limitPower == 12) {
            return 700000;
        }
        if (limitPower == 13) {
            return 700000;
        }
        if (limitPower == 14) {
            return 700000;
        }
        if (limitPower == 15) {
            return 700000;
        }
        if (limitPower == 16) {
            return 700000;
        }
        if (limitPower == 17) {
            return 700000;
        }
        if (limitPower == 18) {
            return 700000;
        }
        if (limitPower == 19) {
            return 700000;
        }
        if (limitPower == 20) {
            return 700000;
        }
        if (limitPower == 21) {
            return 700000;
        }
        if (limitPower == 22) {
            return 700000;
        }
        if (limitPower == 23) {
            return 700000;
        }
        if (limitPower == 24) {
            return 700000;
        }
        if (limitPower == 25) {
            return 700000;
        }
        if (limitPower == 26) {
            return 700000;
        }
        if (limitPower == 27) {
            return 700000;
        }
        if (limitPower == 28) {
            return 700000;
        }
        return 0;
    }

    public int getDamLimit() {
        if (limitPower == 0) {
            return 11000;
        }
        if (limitPower == 1) {
            return 12000;
        }
        if (limitPower == 2) {
            return 15000;
        }
        if (limitPower == 3) {
            return 18000;
        }
        if (limitPower == 4) {
            return 20000;
        }
        if (limitPower == 5) {
            return 22000;
        }
        if (limitPower == 6) {
            return 24000;
        }
        if (limitPower == 7) {
            return 25000;
        }
        if (limitPower == 8) {
            return 26000;
        }
        if (limitPower == 9) {
            return 27000;
        }
        if (limitPower == 10) {
            return 30000;
        }
        if (limitPower == 11) {
            return 30000;
        }
        if (limitPower == 12) {
            return 30000;
        }
        if (limitPower == 13) {
            return 30000;
        }
        if (limitPower == 14) {
            return 30000;
        }
        if (limitPower == 15) {
            return 30000;
        }
        if (limitPower == 16) {
            return 30000;
        }
        if (limitPower == 17) {
            return 30000;
        }
        if (limitPower == 18) {
            return 30000;
        }
        if (limitPower == 19) {
            return 30000;
        }
        if (limitPower == 20) {
            return 30000;
        }
        if (limitPower == 21) {
            return 30000;
        }
        if (limitPower == 22) {
            return 30000;
        }
        if (limitPower == 23) {
            return 30000;
        }
        if (limitPower == 24) {
            return 30000;
        }
        if (limitPower == 25) {
            return 30000;
        }
        if (limitPower == 26) {
            return 30000;
        }
        if (limitPower == 27) {
            return 30000;
        }
        if (limitPower == 28) {
            return 30000;
        }
        return 0;
    }

    public short getDefLimit() {
        if (limitPower == 0) {
            return 550;
        }
        if (limitPower == 1) {
            return 600;
        }
        if (limitPower == 2) {
            return 700;
        }
        if (limitPower == 3) {
            return 800;
        }
        if (limitPower == 4) {
            return 900;
        }
        if (limitPower == 5) {
            return 1000;
        }
        if (limitPower == 6) {
            return 1100;
        }
        if (limitPower == 7) {
            return 1200;
        }
        if (limitPower == 8) {
            return 1300;
        }
        if (limitPower == 9) {
            return 1400;
        }
        if (limitPower == 10) {
            return 1500;
        }
        if (limitPower == 11) {
            return 1500;
        }
        if (limitPower == 12) {
            return 1500;
        }
        if (limitPower == 13) {
            return 1500;
        }
        if (limitPower == 14) {
            return 1500;
        }
        if (limitPower == 15) {
            return 1500;
        }
        if (limitPower == 16) {
            return 1500;
        }
        if (limitPower == 17) {
            return 1500;
        }
        if (limitPower == 18) {
            return 1500;
        }
        if (limitPower == 19) {
            return 1500;
        }
        if (limitPower == 20) {
            return 1500;
        }
        if (limitPower == 21) {
            return 1500;
        }
        if (limitPower == 22) {
            return 1500;
        }
        if (limitPower == 23) {
            return 1500;
        }
        if (limitPower == 24) {
            return 1500;
        }
        if (limitPower == 25) {
            return 1500;
        }
        if (limitPower == 26) {
            return 1500;
        }
        if (limitPower == 27) {
            return 1500;
        }
        if (limitPower == 28) {
            return 1500;
        }

        return 0;
    }

    public byte getCrifLimit() {
        if (limitPower == 0) {
            return 5;
        }
        if (limitPower == 1) {
            return 6;
        }
        if (limitPower == 2) {
            return 7;
        }
        if (limitPower == 3) {
            return 8;
        }
        if (limitPower == 4) {
            return 9;
        }
        if (limitPower == 5) {
            return 10;
        }
        if (limitPower == 6) {
            return 10;
        }
        if (limitPower == 7) {
            return 10;
        }
        if (limitPower == 8) {
            return 10;
        }
        if (limitPower == 9) {
            return 10;
        }
        if (limitPower == 10) {
            return 10;
        }
        if (limitPower == 11) {
            return 10;
        }
        if (limitPower == 12) {
            return 10;
        }
        if (limitPower == 13) {
            return 10;
        }
        if (limitPower == 14) {
            return 10;
        }
        if (limitPower == 15) {
            return 10;
        }
        if (limitPower == 16) {
            return 10;
        }
        if (limitPower == 17) {
            return 10;
        }
        if (limitPower == 18) {
            return 10;
        }
        if (limitPower == 19) {
            return 10;
        }
        if (limitPower == 20) {
            return 10;
        }
        if (limitPower == 21) {
            return 10;
        }
        if (limitPower == 22) {
            return 10;
        }
        if (limitPower == 23) {
            return 10;
        }
        if (limitPower == 24) {
            return 10;
        }
        if (limitPower == 25) {
            return 10;
        }
        if (limitPower == 26) {
            return 10;
        }
        if (limitPower == 27) {
            return 10;
        }
        if (limitPower == 28) {
            return 10;
        }
        return 0;
    }

    public void move(short _toX, short _toY) {
        if (_toX != this.x) {
            this.x = _toX;
        }
        if (_toY != this.y) {
            this.y = _toY;
        }
        this.zone.playerMove(this);
    }

    public void update() {
        this.hp = this.getHpFull();
        this.mp = this.getMpFull();
        Service.gI().loadPoint(this.session, this);
    }

    public void sortBag() throws IOException {
        try {
            int i;
            for (i = 0; i < ItemBag.length; i = (i + 1)) {
                if (ItemBag[i] != null && !(ItemBag[i]).isExpires && (ItemTemplate.ItemTemplateID(ItemBag[i].id)).isUpToUp) {
                    for (int j = (i + 1); j < ItemBag.length; j = (j + 1)) {
                        if (ItemBag[j] != null && !(ItemBag[i]).isExpires && (ItemBag[j]).id == (ItemBag[i]).id) {
                            (ItemBag[i]).quantity += (ItemBag[j]).quantity;
                            ItemBag[j] = null;
                        }
                    }
                }
            }

            for (i = 0; i < ItemBag.length; i = (i + 1)) {
                if (ItemBag[i] == null) {
                    for (int j = (i + 1); j < ItemBag.length; j = j + 1) {
                        if (ItemBag[j] != null) {
                            ItemBag[i] = ItemBag[j];
                            ItemBag[j] = null;
                            break;
                        }
                    }
                }
            }
        } catch (Exception exception) {
        }
        final Message m = new Message(-30);
        m.writer().writeByte(18);
        m.writer().flush();
        this.session.sendMessage(m);
        m.cleanup();
    }

    public void sortBox() throws IOException {
        for (byte i = 0; i < this.ItemBox.length; ++i) {
            if (this.ItemBox[i] != null && !this.ItemBox[i].isExpires && ItemTemplate.ItemTemplateID(this.ItemBox[i].id).isUpToUp) {
                for (byte j = (byte) (i + 1); j < this.ItemBox.length; ++j) {
                    if (this.ItemBox[j] != null && !this.ItemBox[i].isExpires && this.ItemBox[j].id == this.ItemBox[i].id) {
                        final Item item = this.ItemBox[i];
                        item.quantity += this.ItemBox[j].quantity;
                        this.ItemBox[j] = null;
                    }
                }
            }
        }
        for (byte i = 0; i < this.ItemBox.length; ++i) {
            if (this.ItemBox[i] == null) {
                for (byte j = (byte) (i + 1); j < this.ItemBox.length; ++j) {
                    if (this.ItemBox[j] != null) {
                        this.ItemBox[i] = this.ItemBox[j];
                        this.ItemBox[j] = null;
                        break;
                    }
                }
            }
        }
        final Message m = new Message(-30);
        m.writer().writeByte(19);
        m.writer().flush();
        this.session.sendMessage(m);
        m.cleanup();
    }

    //chim thông báo
    public void sendAddchatYellow(String str) {
        try {
            Message m = new Message(-25);
            m.writer().writeUTF(str);
            m.writer().flush();
            session.sendMessage(m);
            m.cleanup();
        } catch (IOException ex) {
        }
    }

    public void requestItem(int typeUI) throws IOException {
        Message m = new Message(23);
        m.writer().writeByte(typeUI);
        m.writer().flush();
        session.sendMessage(m);
        m.cleanup();
    }

    // cap nhat thong tin hp,mp, vang, ngoc ben client
    public void updateVangNgocMessage() {
        Message m = null;
        try {
            if (this.session != null) {
                m = new Message(-30);
                m.writer().writeByte(4);
                m.writer().writeLong(this.vang);
                m.writer().writeInt(this.ngoc);
                m.writer().writeInt(this.getHpFull());
                m.writer().writeInt(this.getMpFull());
                m.writer().writeInt(this.ngocKhoa);
                m.writer().flush();
                this.session.sendMessage(m);
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    //cap nhat lai hp, mp, hay money hoac tat ca
    public void updateHpMpMoney(long vang, int ngoc, int hp, int mp, int ngocKhoa, Player p) {
        Message m = null;
        try {
            if (this.session != null) {
                m = new Message(-30);
                m.writer().writeByte(4);
                m.writer().writeLong(vang);
                m.writer().writeInt(ngoc);
                m.writer().writeInt(hp);
                m.writer().writeInt(mp);
                m.writer().writeInt(ngocKhoa);
                m.writer().flush();
                p.session.sendMessage(m);
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    // hoi sinh
    public void liveFromDead() {
        Message m = null;
        try {
            this.hp = this.getHpFull();
            this.mp = this.getMpFull();
            this.isdie = false;
            if (this.session != null) {
                m = new Message(-16);
                m.writer().flush();
                this.session.sendMessage(m);
                m.cleanup();
            }

            m = new Message(84);
            m.writer().writeInt(this.id);
            m.writer().writeShort(this.x);
            m.writer().writeShort(this.y);
            m.writer().flush();
            this.getPlace().sendMyMessage(this, m);
            m.cleanup();
        } catch (Exception var2) {
            var2.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    // hoi sinh
    public void petLiveFromDead(Player player) {
        Message m = null;
        try {
            this.hp = this.getHpFull();
            this.mp = this.getMpFull();
            this.isdie = false;
            if (this.session != null) {
                m = new Message(-16);
                m.writer().flush();
                this.session.sendMessage(m);
                m.cleanup();
            }

            m = new Message(84);
            m.writer().writeInt(this.id);
            m.writer().writeShort(this.x);
            m.writer().writeShort(this.y);
            m.writer().flush();
            for (Player _p : player.zone.players) {
                _p.session.sendMessage(m);
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

    public void updateBodyMonkey() {

        Message m = null;
        Skill skl = this.getSkillById(13);

        try {
            m = new Message(-45);
            m.writer().writeByte(6);
            m.writer().writeInt(this.id);
            m.writer().writeShort(91);
            m.writer().flush();
            for (Player pl : this.getPlace().players) {
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
        Player _player = this;

        Timer timerBienKhi = new Timer();
        TimerTask _useBienKhi = new TimerTask() {
            public void run() {
                Message m = null;
                try {
                    m = new Message(-90);
                    m.writer().writeByte(0);
                    m.writer().writeInt(_player.id);
                    //part head monkey
                    if (skl.point == 1) {
                        m.writer().writeShort(192);
                    } else if (skl.point == 2) {
                        m.writer().writeShort(195);
                    } else if (skl.point == 3) {
                        m.writer().writeShort(196);
                    } else if (skl.point == 4) {
                        m.writer().writeShort(199);
                    } else if (skl.point == 5) {
                        m.writer().writeShort(197);
                    } else if (skl.point == 6) {
                        m.writer().writeShort(200);
                    } else if (skl.point == 7) {
                        m.writer().writeShort(198);
                    }
                    // part body monkey
                    m.writer().writeShort(193);
                    // part leg monkey
                    m.writer().writeShort(194);
                    // level mokey
                    m.writer().writeByte(1);
                    m.writer().flush();
                    _player.session.sendMessage(m);
                    m.cleanup();

                    // send bien khi cho toan map
                    _player.sendMonkeyTransformToAll(skl);

                    int timeKHI = 150000;
                    //CHECK SET KICH HOAT x5 HOA KHI
                    if (_player.getSetKichHoatFull() == (byte) 8) {
                        timeKHI = timeKHI * 5;
                    }
                    //task reset monkey
                    ResetMonkeyTask monkeyTask = new ResetMonkeyTask(_player);
                    Timer timer = new Timer();
                    timer.schedule(monkeyTask, timeKHI);
                } catch (Exception var2) {
                    var2.printStackTrace();
                } finally {
                    if (m != null) {
                        m.cleanup();
                    }
                }
                Service.gI().loadPoint(_player.session, _player);
                //TIME DUNG DE CHECK NEU DANG MAC SETKH MA THAO RA THI END KHI
                _player.timeX1ENDSKILL = (long) (System.currentTimeMillis() + (long) 150000);
            }
        ;
        };
        timerBienKhi.schedule(_useBienKhi, 3000);
    }

    public void updateBodyMonkeyPet(Player _player, Detu _detu, Skill skl) {
        Message m = null;
//        Skill skl = getSkillById(13);
        try {
            m = new Message(-45);
            m.writer().writeByte(6);
            m.writer().writeInt(_detu.id);
            m.writer().writeShort(91);
            m.writer().flush();
            for (Player pl : _player.getPlace().players) {
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

        Timer timerBienKhi = new Timer();
        TimerTask _useBienKhi = new TimerTask() {
            public void run() {
                Message m = null;
                try {
                    m = new Message(-90);
                    m.writer().writeByte(0);
                    m.writer().writeInt(_detu.id);
                    //part head monkey
                    if (skl.point == 1) {
                        m.writer().writeShort(192);
                    } else if (skl.point == 2) {
                        m.writer().writeShort(195);
                    } else if (skl.point == 3) {
                        m.writer().writeShort(196);
                    } else if (skl.point == 4) {
                        m.writer().writeShort(199);
                    } else if (skl.point == 5) {
                        m.writer().writeShort(197);
                    } else if (skl.point == 6) {
                        m.writer().writeShort(200);
                    } else if (skl.point == 7) {
                        m.writer().writeShort(198);
                    }
                    // part body monkey
                    m.writer().writeShort(193);
                    // part leg monkey
                    m.writer().writeShort(194);
                    // level mokey
                    m.writer().writeByte(1);
                    m.writer().flush();
                    for (Player p : _player.zone.players) {
                        p.session.sendMessage(m);
                    }
                    m.cleanup();

                    //update hp
                    _detu.hp = _detu.getHpFull();
                    updateHpDetu(_player, _detu);
                    //task reset monkey
                    int timeKHI = 150000;
                    //CHECK SET KICH HOAT x5 HOA KHI
                    if (_detu.getSetKichHoatFull() == (byte) 8) {
                        timeKHI = timeKHI * 5;
                    }

                    Timer timerPetKhi = new Timer();
                    TimerTask petKhi = new TimerTask() {
                        public void run() {
                            if (_player.petfucus == 1) {
                                Message m = null;
                                try {
                                    //                                System.out.println("pet het khi");
                                    _detu.isMonkey = false;
                                    _detu.hp = _detu.hp > _detu.getHpFull() ? _detu.getHpFull() : _detu.hp;
                                    updateHpDetu(_player, _detu);
                                    //send default part to all player
                                    for (Player p : _player.zone.players) {
                                        p.sendDefaultTransformToPlayer(_detu);
                                    }

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
                    timerPetKhi.schedule(petKhi, timeKHI);
                    //TIME DUNG DE CHECK NEU DANG MAC SETKH MA THAO RA THI END KHI
                    _detu.timeX1ENDSKILL = (long) (System.currentTimeMillis() + (long) 150000);
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
        timerBienKhi.schedule(_useBienKhi, 3000);
    }

    public void updateHpDetu(Player _player, Detu _detu) {
        Message m = null;
        try {
            m = new Message(-30); //subcommand
            m.writer().writeByte(9); //update hp thoi
            m.writer().writeInt(_detu.id); // id 
            m.writer().writeInt(_detu.hp); //hp
            m.writer().writeInt(_detu.getHpFull());//hp full
            m.writer().flush();
            for (Player p : _player.zone.players) {
                p.session.sendMessage(m);
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

    public void updateHpToPlayerInMap(Player _player, int hp) {
        Message m = null;
        try {
            m = new Message(-30); //subcommand
            m.writer().writeByte(9); //update hp thoi
            m.writer().writeInt(_player.id); // id 
            m.writer().writeInt(hp); //hp
            m.writer().writeInt(_player.getHpFull());//hp full
            m.writer().flush();
            for (Player p : _player.zone.players) {
                if (p.id != _player.id) {
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

    public void sendHPTangToMe() {
        Message ms = null;
        try {
            ms = new Message(-30);
            ms.writer().writeByte(5);
            ms.writer().writeInt(this.hp);
            ms.writer().flush();
            this.session.sendMessage(ms);
            ms.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void sendUpdateBodyPet(Player _player, Detu _detu, short _head, short _body, short _leg) {
//        Message m = null;
//        try {
//            m = new Message(-90);
//            m.writer().writeByte(0);
//            m.writer().writeInt(_detu.id);
//            //part head
//            m.writer().writeShort(_head);
//            // part body
//            m.writer().writeShort(_body);
//            // part leg
//            m.writer().writeShort(_leg);
//            // level mokey
//            m.writer().writeByte(1);
//            m.writer().flush();
//            
//            m.cleanup();
//        } catch (Exception var2) {
//            var2.printStackTrace();
//        } finally {
//            if(m != null) {
//                m.cleanup();
//            }
//        }
//    }
    // send part bien khi cho cac nguoi choi trong map
    public void sendMonkeyTransformToAll(Skill skl) {
        Message m = null;
        try {
            for (Player p : this.zone.players) {
                if (p.id != this.id) {
                    m = new Message(-90);
                    m.writer().writeByte(0);
                    m.writer().writeInt(this.id);
                    //part head monkey
                    if (skl.point == 1) {
                        m.writer().writeShort(192);
                    } else if (skl.point == 2) {
                        m.writer().writeShort(195);
                    } else if (skl.point == 3) {
                        m.writer().writeShort(196);
                    } else if (skl.point == 4) {
                        m.writer().writeShort(199);
                    } else if (skl.point == 5) {
                        m.writer().writeShort(197);
                    } else if (skl.point == 6) {
                        m.writer().writeShort(200);
                    } else if (skl.point == 7) {
                        m.writer().writeShort(198);
                    }
                    // part body monkey
                    m.writer().writeShort(193);
                    // part leg monkey
                    m.writer().writeShort(194);
                    // level mokey
                    m.writer().writeByte(1);
                    m.writer().flush();
                    p.session.sendMessage(m);
                    m.cleanup();
                }
            }

        } catch (Exception var2) {
            var2.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    //tai tao nang luong
    public void chargeHPMP(int step) {
        if (!this.isdie) {
            Skill skillUse = this.getSkillById(8);
            SkillData skilldata = new SkillData();
            short skillID = 0;
            if (this.isPet) {
                skillID = skilldata.getSkillIDBySkillTemplateID(this.gender, skillUse.tempSkillId, skillUse.point);
            } else {
                skillID = skilldata.getSkillIDBySkillTemplateID(this.gender, skillUse.skillId, skillUse.point);
            }
            if (this.checkPlayerBiKhongChe()) {
                this.countCharge = 0;
                this.zone.setSkillPaint(skillID, this, 3);
                return;
            }
            if (step == 1) {
                this.zone.setSkillPaint(skillID, this, 1);
            } else if (step == 2) {
                if (this.countCharge < 10 && (this.hp < this.getHpFull() || this.mp < this.getMpFull())) {
                    this.hp += (int) (this.getHpFull() / 10);
                    this.mp += (int) (this.getMpFull() / 10);
                    this.hp = this.hp > this.getHpFull() ? this.getHpFull() : this.hp;
                    this.mp = this.mp > this.getMpFull() ? this.getMpFull() : this.mp;
                    updateHpMpMoney(this.vang, this.ngoc, this.hp, this.mp, this.ngocKhoa, this);
//                    this.zone.setSkillPaint(skillID, this, 6);
                    this.countCharge++;
                } else {
                    this.countCharge = 0;
                    this.zone.setSkillPaint(skillID, this, 3);
                }
            } else {
                this.countCharge = 0;
                this.zone.setSkillPaint(skillID, this, 3);
            }
        } else {
            this.sendAddchatYellow("Không thể thực hiện");
        }
    }

    //tai tao nang luong
    public void petChargeHPMP(Player _player, Detu _detu, int step) {
        if (!_detu.isdie) {
            Skill skillUse = _detu.getSkillById(8);
            SkillData skilldata = new SkillData();
            short skillID = 0;
            skillID = skilldata.getSkillIDBySkillTemplateID(_detu.gender, skillUse.tempSkillId, skillUse.point);

            if (step == 1) {
                _player.zone.setSkillPaint(skillID, _detu, 1);
            } else if (step == 2) {
                if (_detu.countCharge < 10 && (_detu.hp < _detu.getHpFull() || _detu.mp < _detu.getMpFull())) {
                    _detu.hp += (int) (_detu.getHpFull() / 10);
                    _detu.mp += (int) (_detu.getMpFull() / 10);
                    _detu.hp = _detu.hp > _detu.getHpFull() ? _detu.getHpFull() : _detu.hp;
                    _detu.mp = _detu.mp > _detu.getMpFull() ? _detu.getMpFull() : _detu.mp;
                    _detu.updateHpDetu(_player, _detu);
//                    _player.zone.setSkillPaint(skillID, _detu, 6);
                    _detu.countCharge++;
                } else {
                    _detu.countCharge = 0;
                    _player.zone.setSkillPaint(skillID, _detu, 3);
                }
            } else {
                _detu.countCharge = 0;
                _player.zone.setSkillPaint(skillID, _detu, 3);
            }
        } else {
            _detu.sendAddchatYellow("Không thể thực hiện");
        }
    }

    public void bossChargeHPMP(Boss _boss, int step) {
        if (!_boss.isdie) {
            SkillData skilldata = new SkillData();
            short skillID = skilldata.getSkillIDBySkillTemplateID((byte) 2, (short) 8, 7);
            if (step == 1) {
                _boss.zone.setSkillPaint(skillID, _boss, 1);
            } else if (step == 2) {
//                if(_boss.countCharge < 10 && (_boss.hp < _boss.hpFull)) {
                if ((_boss.hp < _boss.hpFull)) {
                    _boss.hp += (int) (_boss.hpFull / 10);
                    _boss.hp = _boss.hp > _boss.hpFull ? _boss.hpFull : _boss.hp;

                    Message m = null;
                    try {
                        m = new Message(-30); //subcommand
                        m.writer().writeByte(9); //update hp thoi
                        m.writer().writeInt(_boss.id); // id 
                        m.writer().writeInt(_boss.hp); //hp
                        m.writer().writeInt(_boss.hpFull);//hp full
                        m.writer().flush();
                        for (Player p : _boss.zone.players) {
                            p.session.sendMessage(m);
                        }
                        m.cleanup();
                    } catch (Exception var2) {
                        var2.printStackTrace();
                    } finally {
                        if (m != null) {
                            m.cleanup();
                        }
                    }
//                    this.zone.setSkillPaint(skillID, this, 6);
//                    _boss.countCharge ++;
                } else {
//                    _boss.countCharge = 0;
                    _boss.zone.setSkillPaint(skillID, _boss, 3);
                }
            } else {
//                _boss.countCharge = 0;
                _boss.zone.setSkillPaint(skillID, _boss, 3);
            }
        } else {
            _boss.isTTNL = false;
//            _boss.sendAddchatYellow("Không thể thực hiện");
        }
    }

    public void bossNoPetChargeHPMP(Boss _boss, int step) {
        if (!_boss.isdie) {
            SkillData skilldata = new SkillData();
            short skillID = skilldata.getSkillIDBySkillTemplateID((byte) 2, (short) 8, 7);
            if (step == 1) {
                _boss.zone.setSkillPaint(skillID, _boss, 1);
            } else if (step == 2) {
                if ((_boss.hp < _boss.hpFull)) {
//                    _boss.hp += (int)(_boss.hpFull / 50);
//                    long hplate = (long)(_boss.hp + (long)(_boss.hpFull / 50)) > 2000000000L ? 2000000000L : (long)(_boss.hp + (long)(_boss.hpFull / 50));
                    long hplate = (long) (_boss.hp + (long) (_boss.hpFull / 50)) > _boss.hpFull ? _boss.hpFull : (long) (_boss.hp + (long) (_boss.hpFull / 50));
//                    _boss.hp = _boss.hp > _boss.hpFull ? _boss.hpFull : _boss.hp;
                    _boss.hp = (int) hplate;

                    Message m = null;
                    try {
                        m = new Message(-30); //subcommand
                        m.writer().writeByte(9); //update hp thoi
                        m.writer().writeInt(_boss.id); // id 
                        m.writer().writeInt(_boss.hp); //hp
                        m.writer().writeInt(_boss.hpFull);//hp full
                        m.writer().flush();
                        for (Player p : _boss.zone.players) {
                            p.session.sendMessage(m);
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
                    _boss.isTTNL = false;
                    _boss.zone.setSkillPaint(skillID, _boss, 3);
                }
            } else {
                _boss.isTTNL = false;
                _boss.zone.setSkillPaint(skillID, _boss, 3);
            }
        } else {
            _boss.isTTNL = false;
        }
    }

    //send part default cho cac nguoi choi khac trong map
    public void sendDefaultTransformToPlayer(Player p) {
        Message m = null;
        try {
            m = new Message(-90);
            m.writer().writeByte(0);
            m.writer().writeInt(p.id);
            //part head monkey
            m.writer().writeShort(p.PartHead());

            String bodyLeg = Service.gI().writePartBodyLeg(p);
            String[] arrOfStr = bodyLeg.split(",", 2);
            m.writer().writeShort(Short.parseShort(arrOfStr[0]));
            m.writer().writeShort(Short.parseShort(arrOfStr[1]));
//            if((p.ItemBody[5] != null && p.ItemBody[5].template.level != 0) || p.NhapThe == 1){     
//                if(p.NhapThe == 1) {
//                    m.writer().writeShort(p.PartHead() + 1);
//                    m.writer().writeShort(p.PartHead() + 2);
//                } else if(p.ItemBody[5] != null && p.ItemBody[5].template.id == 604) {//CAI TRANG VIP
//                    m.writer().writeShort(472);
//                    m.writer().writeShort(473);
//                } else if(p.ItemBody[5] != null && p.ItemBody[5].template.id == 605) {//CAI TRANG VIP
//                    m.writer().writeShort(476);
//                    m.writer().writeShort(477);
//                } else if(p.ItemBody[5] != null && p.ItemBody[5].template.id == 606) {//CAI TRANG VIP
//                    m.writer().writeShort(474);
//                    m.writer().writeShort(475);
//                } else {
//                    m.writer().writeShort(p.PartHead() + 1);
//                    m.writer().writeShort(p.PartHead() + 2);
//                }
//            } else {
//                m.writer().writeShort(p.PartBody());
//                m.writer().writeShort(p.Leg());
//            }
            // level mokey
            m.writer().writeByte(0);
            m.writer().flush();
            this.session.sendMessage(m);
            m.cleanup();

        } catch (Exception var2) {
            var2.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    //CHI SO EP SAO PHA LE
    public int getPercentHutHp() {
        int _percent = 0;
        if (ItemBody[0] != null) {
            _percent += ItemBody[0].getParamItemByID(95); //spl hut mau
        }
        if (ItemBody[1] != null) {
            _percent += ItemBody[1].getParamItemByID(95); //spl hut mau
        }
        if (ItemBody[2] != null) {
            _percent += ItemBody[2].getParamItemByID(95); //spl hut mau
        }
        if (ItemBody[3] != null) {
            _percent += ItemBody[3].getParamItemByID(95); //spl hut mau
        }
        if (ItemBody[4] != null) {
            _percent += ItemBody[4].getParamItemByID(95); //spl hut mau
        }
        if (ItemBody[4] != null) {
            _percent += ItemBody[4].getParamItemByID(95); //spl hut mau
        }
        if (ItemBody[5] != null && ItemBody[5].template.id == 448) {
            _percent += 50;  //cai trang dracula
        }
        if (ItemBody[6] != null) {
            _percent += ItemBody[6].getParamItemByID(95); //spl hut mau
        }
        return _percent;
    }

    public int getPercentHutKi() {
        int _percent = 0;
        if (ItemBody[0] != null) {
            _percent += ItemBody[0].getParamItemByID(96); //spl hut mau
        }
        if (ItemBody[1] != null) {
            _percent += ItemBody[1].getParamItemByID(96); //spl hut mau
        }
        if (ItemBody[2] != null) {
            _percent += ItemBody[2].getParamItemByID(96); //spl hut mau
        }
        if (ItemBody[3] != null) {
            _percent += ItemBody[3].getParamItemByID(96); //spl hut mau
        }
        if (ItemBody[4] != null) {
            _percent += ItemBody[4].getParamItemByID(96); //spl hut mau
        }
        if (ItemBody[6] != null) {
            _percent += ItemBody[6].getParamItemByID(96); //spl hut mau
        }
        return _percent;
    }

    public long getPercentUpTnSm(long _tnsm) {
        if (ItemBody[0] != null) {
            _tnsm += (long) (_tnsm * ItemBody[0].getParamItemByID(101) / 100); //spl tnsm
        }
        if (ItemBody[1] != null) {
            _tnsm += (long) (_tnsm * ItemBody[1].getParamItemByID(101) / 100); //spl tnsm
        }
        if (ItemBody[2] != null) {
            _tnsm += (long) (_tnsm * ItemBody[2].getParamItemByID(101) / 100); //spl tnsm
        }
        if (ItemBody[3] != null) {
            _tnsm += (long) (_tnsm * ItemBody[3].getParamItemByID(101) / 100); //spl tnsm
        }
        if (ItemBody[4] != null) {
            _tnsm += (long) (_tnsm * ItemBody[4].getParamItemByID(101) / 100); //spl tnsm
        }
        if (ItemBody[5] != null) {
            _tnsm += (long) (_tnsm * ItemBody[5].getParamItemByID(101) / 100); //spl tnsm
        }
        if (ItemBody[6] != null) {
            _tnsm += (long) (_tnsm * ItemBody[6].getParamItemByID(101) / 100); //spl tnsm
        }
        return _tnsm;
    }

    //GET % ne don
    public int getPercentNedon() {
        int _percent = 0;
        if (ItemBody[0] != null) {
            _percent += ItemBody[0].getParamItemByID(108); //spl pst
        }
        if (ItemBody[1] != null) {
            _percent += ItemBody[1].getParamItemByID(108); //spl pst
        }
        if (ItemBody[2] != null) {
            _percent += ItemBody[2].getParamItemByID(108); //spl pst
        }
        if (ItemBody[3] != null) {
            _percent += ItemBody[3].getParamItemByID(108); //spl pst
        }
        if (ItemBody[4] != null) {
            _percent += ItemBody[4].getParamItemByID(108); //spl pst
        }
        if (ItemBody[6] != null) {
            _percent += ItemBody[6].getParamItemByID(108); //spl pst
        }
        if (ItemBody[5] != null) {
            _percent += ItemBody[5].getParamItemByID(108); //cai trang
        }
        if (_percent > 90) {
            _percent = 90;
        }
        return (int) (_percent / 10);
    }

    //GET % phan sat thuong
    public int getPercentPST() {
        int _percent = 0;
        if (ItemBody[0] != null) {
            _percent += ItemBody[0].getParamItemByID(97); //spl pst
        }
        if (ItemBody[1] != null) {
            _percent += ItemBody[1].getParamItemByID(97); //spl pst
        }
        if (ItemBody[2] != null) {
            _percent += ItemBody[2].getParamItemByID(97); //spl pst
        }
        if (ItemBody[3] != null) {
            _percent += ItemBody[3].getParamItemByID(97); //spl pst
        }
        if (ItemBody[4] != null) {
            _percent += ItemBody[4].getParamItemByID(97); //spl pst
        }
        if (ItemBody[6] != null) {
            _percent += ItemBody[6].getParamItemByID(97); //spl pst
        }
        return _percent;
    }

    //GET % phan sat thuong
    public int getPercentGold() {
        int _percent = 0;
        if (ItemBody[0] != null) {
            _percent += ItemBody[0].getParamItemByID(100); //spl pst
        }
        if (ItemBody[1] != null) {
            _percent += ItemBody[1].getParamItemByID(100); //spl pst
        }
        if (ItemBody[2] != null) {
            _percent += ItemBody[2].getParamItemByID(100); //spl pst
        }
        if (ItemBody[3] != null) {
            _percent += ItemBody[3].getParamItemByID(100); //spl pst
        }
        if (ItemBody[4] != null) {
            _percent += ItemBody[4].getParamItemByID(100); //spl pst
        }
        if (ItemBody[5] != null && ItemBody[5].template.id == 451) {
            _percent += 20;  //cai trang vua quy satan
        }
        if (ItemBody[6] != null) {
            _percent += ItemBody[6].getParamItemByID(100); //spl pst
        }
        return _percent;
    }

    // GET DAME CAI TRANG
    public int dameUpByCaiTrang(int damage) {
        if (ItemBody[5] != null && (ItemBody[5].template.id >= 550 && ItemBody[5].template.id <= 552)) {
            damage += (int) (damage * Util.getPercentDouble(ItemBody[5].getParamItemByID(19)));
        }
        return damage;
    }

    // END CHI SO EP SAO PHA LE
    // GET BUA 
    public int getBuaTNSM() {
        int x = 1;
        if (listAmulet.get(0).timeEnd > System.currentTimeMillis()) {
            x = x * 2;
        }
        if (listAmulet.get(8).timeEnd > System.currentTimeMillis()) {
            x = x * 3;
        }
        if (listAmulet.get(9).timeEnd > System.currentTimeMillis()) {
            x = x * 4;
        }
        return x;
    }

    public boolean getBuaDetu() {
        if (listAmulet.get(7).timeEnd > System.currentTimeMillis()) {
            return true;
        }
        return false;
    }

    public boolean getBuaManhMe() {
        if (listAmulet.get(1).timeEnd > System.currentTimeMillis()) {
            return true;
        }
        return false;
    }

    public boolean getBuaDaTrau() {
        if (listAmulet.get(2).timeEnd > System.currentTimeMillis()) {
            return true;
        }
        return false;
    }

    public boolean getBuaOaiHung() {
        if (listAmulet.get(3).timeEnd > System.currentTimeMillis()) {
            return true;
        }
        return false;
    }

    public boolean getBuaBatTu() {
        if (listAmulet.get(4).timeEnd > System.currentTimeMillis()) {
            return true;
        }
        return false;
    }

    public boolean getBuaDeoDai() {
        if (listAmulet.get(5).timeEnd > System.currentTimeMillis()) {
            return true;
        }
        return false;
    }

    public boolean getBuaThuHut() {
        if (listAmulet.get(6).timeEnd > System.currentTimeMillis()) {
            return true;
        }
        return false;
    }

    // END GET BUA
    public byte getLevelPower() {
        if (this.power >= 300010000000L) {
            return (byte) 28;
        } else if (this.power >= 280010000000L) {
            return (byte) 27;
        } else if (this.power >= 260010000000L) {
            return (byte) 26;
        } else if (this.power >= 230010000000L) {
            return (byte) 25;
        } else if (this.power >= 200010000000L) {
            return (byte) 24;
        } else if (this.power >= 180010000000L) {
            return (byte) 23;
        } else if (this.power >= 160010000000L) {
            return (byte) 22;
        } else if (this.power >= 130010000000L) {
            return (byte) 21;
        } else if (this.power >= 100010000000L) {
            return (byte) 20;
        } else if (this.power >= 81000000000L) {
            return (byte) 19;
        } else if (this.power >= 70010000000L) {
            return (byte) 18;
        } else if (this.power >= 60010000000L) {
            return (byte) 17;
        } else if (this.power >= 50010000000L) {
            return (byte) 16;
        } else if (this.power >= 40000000000L) {
            return (byte) 15; //khi tim
        } else if (this.power >= 10000000000L) {
            return (byte) 14; //khi xanh
        } else if (this.power >= 5000000000L) {
            return (byte) 13;
        } else if (this.power >= 1500000000) {
            return (byte) 12;
        } else if (this.power >= 150000000) {
            return (byte) 11;
        } else if (this.power >= 15000000) {
            return (byte) 10;
        } else if (this.power >= 1500000) {
            return (byte) 9;
        } else if (this.power >= 700000) {
            return (byte) 8;
        } else if (this.power >= 340000) {
            return (byte) 7;
        } else if (this.power >= 170000) {
            return (byte) 6;
        } else if (this.power >= 90000) {
            return (byte) 5;
        } else if (this.power >= 40000) {
            return (byte) 4;
        } else if (this.power >= 15000) {
            return (byte) 3;
        } else if (this.power >= 3000) {
            return (byte) 2;
        } else if (this.power >= 1000) {
            return (byte) 1;
        }
        return (byte) 1;
    }

    public String getStringLevel() {
        String percent = Util.strPercentPower(this.power);
        if (this.power >= 300010000000L) {
            return "Thông Đại Thiên Sứ Cấp 4 + " + percent;
        } else if (this.power >= 280010000000L) {
            return "Thông Đại Thiên Sứ Cấp 3 + " + percent;
        } else if (this.power >= 230010000000L) {
            return "Thông Đại Thiên Sứ Cấp 2 + " + percent;
        } else if (this.power >= 180010000000L) {
            return "Thông Đại Thiên Sứ Cấp 1 + " + percent;
        } else if (this.power >= 130010000000L) {
            return "Giới Vương Thần Cấp 4 + " + percent;
        } else if (this.power >= 100010000000L) {
            return "Giới Vương Thần Cấp 3 + " + percent;
        } else if (this.power >= 80000000000L) {
            return "Giới Vương Thần Cấp 2 + " + percent;
        } else if (this.power >= 70000000000L) {
            return "Giới Vương Thần Cấp 1 + " + percent;
        } else if (this.power >= 60000000000L) {
            return "Thần Hủy Diệt Cấp 2 + " + percent;
        } else if (this.power >= 50000000000L) {
            return "Thần Hủy Diệt Cấp 1 + " + percent;
        } else if (this.power >= 40000000000L) {
            if (this.gender == (byte) 0) {
                return "Thần Trái Đất Cấp 3 + " + percent;
            } else if (this.gender == (byte) 1) {
                return "Thần Namếc Cấp 3 + " + percent;
            } else if (this.gender == (byte) 2) {
                return "Thần Xayda Cấp 3 + " + percent;
            }
        } else if (this.power >= 10000000000L) {
            if (this.gender == (byte) 0) {
                return "Thần Trái Đất Cấp 2 + " + percent;
            } else if (this.gender == (byte) 1) {
                return "Thần Namếc Cấp 2 + " + percent;
            } else if (this.gender == (byte) 2) {
                return "Thần Xayda Cấp 2 + " + percent;
            }
        } else if (this.power >= 5000000000L) {
            if (this.gender == (byte) 0) {
                return "Thần Trái Đất Cấp 1 + " + percent;
            } else if (this.gender == (byte) 1) {
                return "Thần Namếc Cấp 1 + " + percent;
            } else if (this.gender == (byte) 2) {
                return "Thần Xayda Cấp 1 + " + percent;
            }
        } else if (this.power >= 1500000000L) {
            if (this.gender == (byte) 0) {
                return "Siêu Nhân Cấp 4 + " + percent;
            } else if (this.gender == (byte) 1) {
                return "Siêu Namếc Cấp 4 + " + percent;
            } else if (this.gender == (byte) 2) {
                return "Siêu Xayda Cấp 4 + " + percent;
            }
        } else if (this.power >= 150000000) {
            if (this.gender == (byte) 0) {
                return "Siêu Nhân Cấp 3 + " + percent;
            } else if (this.gender == (byte) 1) {
                return "Siêu Namếc Cấp 3 + " + percent;
            } else if (this.gender == (byte) 2) {
                return "Siêu Xayda Cấp 3 + " + percent;
            }
        } else if (this.power >= 15000000) {
            if (this.gender == (byte) 0) {
                return "Siêu Nhân Cấp 2 + " + percent;
            } else if (this.gender == (byte) 1) {
                return "Siêu Namếc Cấp 2 + " + percent;
            } else if (this.gender == (byte) 2) {
                return "Siêu Xayda Cấp 2 + " + percent;
            }
        } else if (this.power >= 1500000) {
            if (this.gender == (byte) 0) {
                return "Siêu Nhân Cấp 1 + " + percent;
            } else if (this.gender == (byte) 1) {
                return "Siêu Namếc Cấp 1 + " + percent;
            } else if (this.gender == (byte) 2) {
                return "Siêu Xayda Cấp 1 + " + percent;
            }
        }
        return "Sơ Sinh + " + percent;
    }

    //GET SKILL BY TEMPLATE IDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD
    public Skill getSkillByIDTemplate(short tempid) {
        for (Skill _SKILL : skill) {
            if (_SKILL.tempSkillId == tempid) {
                return _SKILL;
            }
        }
        return null;
    }

    //CHECK SET KICH HOAT
    public byte getSetKichHoatFull() {
        if (this.ItemBody[0] != null && this.ItemBody[1] != null && this.ItemBody[2] != null && this.ItemBody[3] != null && this.ItemBody[4] != null) {
            int idOption = 0;
            int count = 0;
            //GET LOAI SET KICH HOAT
            for (byte i = 0; i < this.ItemBody[0].itemOptions.size(); i++) {
                if (this.ItemBody[0].itemOptions.get(i).id == 127 || this.ItemBody[0].itemOptions.get(i).id == 128 || this.ItemBody[0].itemOptions.get(i).id == 129
                        || this.ItemBody[0].itemOptions.get(i).id == 130 || this.ItemBody[0].itemOptions.get(i).id == 131 || this.ItemBody[0].itemOptions.get(i).id == 132
                        || this.ItemBody[0].itemOptions.get(i).id == 133 || this.ItemBody[0].itemOptions.get(i).id == 134 || this.ItemBody[0].itemOptions.get(i).id == 135) {
                    idOption = this.ItemBody[0].itemOptions.get(i).id;
                    count++;
                    break;
                }
            }
            if (count == 0) {
                return 0;
            }
            for (byte i = 1; i < 5; i++) {
                for (byte j = 0; j < this.ItemBody[i].itemOptions.size(); j++) {
                    if (this.ItemBody[i].itemOptions.get(j).id == idOption) {
                        count++;
                        break;
                    }
                }
            }
            if (count == 5) {
                if (this.gender == 0) {
                    if (idOption == 127) {
                        return (byte) 1;
                    } else if (idOption == 128) {
                        return (byte) 2;
                    } else if (idOption == 129) {
                        return (byte) 3;
                    }
                } else if (this.gender == 1) {
                    if (idOption == 130) {
                        return (byte) 4;
                    } else if (idOption == 131) {
                        return (byte) 5;
                    } else if (idOption == 132) {
                        return (byte) 6;
                    }
                } else if (this.gender == 2) {
                    if (idOption == 133) {
                        return (byte) 7;
                    } else if (idOption == 134) {
                        return (byte) 8;
                    } else if (idOption == 135) {
                        return (byte) 9;
                    }
                }
            }
        }
        return (byte) 0;
    }

    //GET CARD BY ID
    public RadaCard cardByIdCard(short id) {
        for (RadaCard card : cards) {
            if (card.id == id) {
                return card;
            }
        }
        return null;
    }

    public boolean chkRadaSetKichHoat(Item item) {
        boolean hasTypeKichHoat = false;
        for (byte i = 0; i < item.itemOptions.size(); i++) {
            if ((item.itemOptions.get(i).id == 127 || item.itemOptions.get(i).id == 128 || item.itemOptions.get(i).id == 129
                    || item.itemOptions.get(i).id == 130 || item.itemOptions.get(i).id == 131 || item.itemOptions.get(i).id == 132
                    || item.itemOptions.get(i).id == 133 || item.itemOptions.get(i).id == 134 || item.itemOptions.get(i).id == 135) && !hasTypeKichHoat) {
                hasTypeKichHoat = true;
            }
            if ((item.itemOptions.get(i).id == 127 || item.itemOptions.get(i).id == 128 || item.itemOptions.get(i).id == 129) && this.detu.gender == (byte) 0) {
                return true;
            } else if ((item.itemOptions.get(i).id == 130 || item.itemOptions.get(i).id == 131 || item.itemOptions.get(i).id == 132) && this.detu.gender == (byte) 1) {
                return true;
            } else if ((item.itemOptions.get(i).id == 133 || item.itemOptions.get(i).id == 134 || item.itemOptions.get(i).id == 135) && this.detu.gender == (byte) 2) {
                return true;
            }
        }
        if (hasTypeKichHoat) {
            return false;
        } else {
            return true;
        }
    }
    //END CHECK SET KICH HOAT

    public static Player setup(int account_id) {
        try {
            //synchronized (Server.LOCK_MYSQL) {
//                ResultSet rs = SQLManager.stat.executeQuery("SELECT * FROM `player` WHERE `account_id`LIKE'" + account_id + "';");
            ResultSet rs = SQLManager.stat.executeQuery("SELECT * FROM `player` WHERE `account_id`=" + account_id + ";");
            if (rs != null && rs.first()) {
                Player player = new Player();
                player.id = rs.getInt("account_id");
                player.taskId = rs.getByte("task_id");
                player.name = rs.getString("name");
                player.head = rs.getShort("head");
                player.gender = rs.getByte("gender");
                player.nClass = SkillData.nClasss[player.gender];
                player.power = rs.getLong("power");
                player.vang = rs.getLong("vang");
                player.ngoc = rs.getInt("luong");
                player.ngocKhoa = rs.getInt("luong_khoa");
                player.thanhvien = rs.getInt("thanhvien");
                player.sotien = rs.getInt("sotien");
                player.pointSuKien = rs.getInt("sukien");
                player.nhanqua = rs.getInt("nhanqua");
                player.x = rs.getShort("where_x");
                player.y = rs.getShort("where_y");
//                    player.clanid = rs.getInt("clan_id"); //SERVER 2
                if (rs.getInt("clan_id") != -1) {
                    player.clan = ClanManager.gI().getClanById(rs.getInt("clan_id"));
                }
//                    Map map = Manager.getMapid(rs.getByte("where_id"));
                Map map = MainManager.getMapid(rs.getInt("where_id"));
                map.getPlayers().add(player);
                player.map = map;
                player.hpGoc = rs.getInt("hp_goc");
                player.mpGoc = rs.getInt("mp_goc");
                player.hp = player.hpGoc;
                player.mp = player.mpGoc;
                player.damGoc = rs.getInt("dame_goc");
                player.defGoc = rs.getShort("def_goc");
                player.critGoc = rs.getByte("crit_goc");
                player.critNr = rs.getByte("crit_nr");
                player.tiemNang = rs.getLong("tiem_nang");
                player.limitPower = rs.getByte("limit_power");
                player.hpFull = rs.getInt("hp_goc");
                player.mpFull = rs.getInt("mp_goc");
                player.damFull = rs.getInt("dame_goc");
                player.defFull = rs.getShort("def_goc");
                player.NhapThe = rs.getInt("nhapthe");
                player.petfucus = (byte) 0;
                player.critFull = rs.getByte("crit_goc");
                player.rolePT = rs.getByte("role_pt");
                player.isPorata2 = rs.getByte("nhapthe2") == 1 ? true : false;
                player.hasTrungMabu = rs.getByte("hasmabu") == 1 ? true : false;
                player.isTennis = rs.getByte("istennis") == 1 ? true : false;
                player.tSwapZone = System.currentTimeMillis() + 30000;
                //SUM TOTAL TIEM NANG
                player.sumTiemNang = 0;
                if (player.hpGoc > 100) {
                    player.sumTiemNang += ((long) player.hpGoc - (long) 100) / (long) 20 * ((long) 2 * ((long) 100 + (long) 1000) + ((long) player.hpGoc - (long) 100) - (long) 20) / (long) 2;
                }
                if (player.mpGoc > 100) {
                    player.sumTiemNang += ((long) player.mpGoc - (long) 100) / (long) 20 * ((long) 2 * ((long) 100 + (long) 1000) + ((long) player.mpGoc - (long) 100) - (long) 20) / (long) 2;
                }
                if (player.damGoc > 15) {
                    player.sumTiemNang += ((long) player.damGoc - (long) 100) * ((long) 30 + ((long) player.damGoc - (long) 100) - (long) 1) / (long) 2 * (long) 100;
                }
                if (player.defGoc > 0) {
                    for (int i = 0; i < player.defGoc; i++) {
                        player.sumTiemNang += ((long) i + (long) 5) * (long) 100000;
                    }
                }
//                    if(player.critGoc > 0) {
//                        for(int i = 1; i <= player.critGoc; i++) {
//                            long TNCRIT = 50000000L;
//                            for(int j = 1; j <= player.critGoc; j++) {
//                                TNCRIT *= 5L;
//                            }
//                            player.sumTiemNang += TNCRIT;
//                        }
//                    }
//                    Util.log("TIEM NANG: " + player.sumTiemNang);
                JSONArray jar = (JSONArray) JSONValue.parse(rs.getString("skill"));
                JSONObject job;
                int index;
                if (jar != null) {
                    job = null;
                    for (index = 0; index < jar.size(); index++) {
                        Skill skill = new Skill();

                        Skill Skill = new Skill();
                        job = (JSONObject) JSONValue.parse(jar.get(index).toString());
                        int id = Integer.parseInt(job.get("id").toString());
                        int level = Integer.parseInt(job.get("point").toString());
                        long lastuse = Long.parseLong(job.get("lastuse").toString());
                        //SET UP SKILL
                        Skill _SKILLTEMP = player.nClass.getSkillTemplate(id).skills[level - 1];
                        skill.template = _SKILLTEMP.template;
                        skill.skillId = _SKILLTEMP.skillId;
                        skill.point = _SKILLTEMP.point;
                        skill.powRequire = _SKILLTEMP.powRequire;
                        skill.coolDown = _SKILLTEMP.coolDown;
                        skill.lastTimeUseThisSkill = lastuse;
                        skill.dx = _SKILLTEMP.dx;
                        skill.dy = _SKILLTEMP.dy;
                        skill.maxFight = _SKILLTEMP.maxFight;
                        skill.manaUse = _SKILLTEMP.manaUse;
                        skill.options = _SKILLTEMP.options;
                        skill.paintCanNotUseSkill = _SKILLTEMP.paintCanNotUseSkill;
                        skill.damage = _SKILLTEMP.damage;
                        skill.moreInfo = _SKILLTEMP.moreInfo;
                        skill.price = _SKILLTEMP.price;
                        skill.genderSkill = (byte) player.gender;
                        skill.tempSkillId = (short) _SKILLTEMP.template.id;

                        player.skill.add(skill);
                        //END SET UP SKILL
                        Skill.skillId = (short) id;
                        Skill.point = level;
                        player.listSkill.add(Skill);
                        job.clear();
                    }
//                        for(Skill ss: player.skill) {
//                            Util.log("SKILL: " + ss.template.name + ", " + ss.lastTimeUseThisSkill + ", " + ss.coolDown);
//                        }
                }

                player.maxluggage = rs.getByte("maxluggage");
                player.maxBox = rs.getByte("maxbox");
                JSONObject job2 = null;
                player.ItemBag = new Item[player.maxluggage];
                jar = (JSONArray) JSONValue.parse(rs.getString("itembag"));
                int j;
                if (jar != null) {
                    for (j = 0; j < jar.size(); ++j) {
                        if (j >= player.maxluggage) {
                            break;
                        }
//                        for (j = 0; j < player.maxluggage; j++) {
                        job2 = (JSONObject) jar.get(j);
                        index = Byte.parseByte(job2.get("index").toString());
                        player.ItemBag[index] = ItemTemplate.parseItem(jar.get(j).toString());
                        job2.clear();
                    }
                }

                player.ItemBox = new Item[player.maxBox];
                jar = (JSONArray) JSONValue.parse(rs.getString("itembox"));
                if (jar != null) {
                    for (j = 0; j < jar.size(); ++j) {
                        if (j >= player.maxBox) {
                            break;
                        }
                        job2 = (JSONObject) jar.get(j);
                        index = Byte.parseByte(job2.get("index").toString());
                        player.ItemBox[index] = ItemTemplate.parseItem(jar.get(j).toString());
                        job2.clear();
                    }
                }

                player.ItemBody = new Item[10];
                jar = (JSONArray) JSONValue.parse(rs.getString("itembody"));
                if (jar != null) {
                    for (j = 0; j < jar.size(); ++j) {
                        if (j >= 10) {
                            break;
                        }
                        job2 = (JSONObject) jar.get(j);
                        index = Byte.parseByte(job2.get("index").toString());
                        player.ItemBody[index] = ItemTemplate.parseItem(jar.get(j).toString());
                        job2.clear();
                    }
                }

                jar = (JSONArray) JSONValue.parse(rs.getString("amulet"));
                if (jar != null) {
                    for (j = 0; j < jar.size(); ++j) {
                        Amulet _amulet = new Amulet();
                        job2 = (JSONObject) jar.get(j);
                        _amulet.id = Integer.parseInt(job2.get("id").toString());
                        _amulet.timeEnd = Long.parseLong(job2.get("point").toString());
                        player.listAmulet.add(_amulet);
                        job2.clear();
                    }
                }
                //THONG TIN DAU THAN
                jar = (JSONArray) JSONValue.parse(rs.getString("bean"));
                if (jar != null) {
                    for (j = 0; j < jar.size(); ++j) {
                        job2 = (JSONObject) jar.get(j);
                        player.levelTree = Byte.parseByte(job2.get("level").toString());
                        player.lastTimeTree = Long.parseLong(job2.get("time").toString());
                        player.maxBean = Byte.parseByte(job2.get("max").toString());
                        if (player.lastTimeTree > System.currentTimeMillis()) {
                            player.upMagicTree = true;
                        }
                        job2.clear();
                    }
                }
                //THONG TIN TASK
                jar = (JSONArray) JSONValue.parse(rs.getString("task"));
                if (jar != null) {
                    job2 = (JSONObject) jar.get(0);
                    Task _task = new Task();
                    _task.setupTask(TaskManager.gI().getTasks().get(player.taskId), player.gender);
                    _task.index = Byte.parseByte(job2.get("index").toString());
                    _task.count = Short.parseShort(job2.get("count").toString());
                    player.crrTask = _task;
                    job2.clear();
                }
                //THONG TIN NGOC RONG SAO DEN
                jar = (JSONArray) JSONValue.parse(rs.getString("starblack"));
                if (jar != null) {
                    for (j = 0; j < jar.size(); ++j) {
                        job2 = (JSONObject) jar.get(j);
                        player.timeNRSD[j] = Long.parseLong(job2.get("time").toString());
                        player.timeEndNRSD[j] = Long.parseLong(job2.get("timeend").toString());
                        if (System.currentTimeMillis() < player.timeEndNRSD[j]) {
                            player.indexNRSD.add((byte) j);
                        }
                        job2.clear();
                    }
                }
                //THONG TIN ITEM DANG DUNG NEU CO
                jar = (JSONArray) JSONValue.parse(rs.getString("itemuse"));
                if (jar != null) {
                    for (j = 0; j < jar.size(); ++j) {
                        job2 = (JSONObject) jar.get(j);
                        int idITEM = Integer.parseInt(job2.get("id").toString());
                        Service.gI().itemBuffLogin(player, idITEM, Long.parseLong(job2.get("time").toString()));
                        job2.clear();
                    }
                }
                //THONG TIN BAN BE
                jar = (JSONArray) JSONValue.parse(rs.getString("friends"));
                if (jar != null) {
                    for (j = 0; j < jar.size(); ++j) {
                        job2 = (JSONObject) jar.get(j);
                        int idFriend = Integer.parseInt(job2.get("id").toString());
                        boolean isOn = PlayerManger.gI().getPlayerByUserID(idFriend) != null ? true : false;
                        Friend _friend = new Friend.Builder(idFriend)
                                .withHead(Short.parseShort(job2.get("head").toString()))
                                .withHeadICON(Short.parseShort(job2.get("headICON").toString()))
                                .withBody(Short.parseShort(job2.get("body").toString()))
                                .withLeg(Short.parseShort(job2.get("leg").toString()))
                                .withBag(Byte.parseByte(job2.get("bag").toString()))
                                .withName(job2.get("name").toString())
                                .isOnline(isOn)
                                .withPower(job2.get("strPower").toString())
                                .build();
                        player.friends.add(_friend);
                        job2.clear();
                    }
                }
                //THONG TIN RADA CARD
                jar = (JSONArray) JSONValue.parse(rs.getString("card"));
                RadaCard card = null;
                player.cCardUse = (byte) 0;
                player.hpRada = 0;
                player.kiRada = 0;
                player.defRada = 0;
                player.damRada = 0;
                player.sdRada = 0;
                if (jar != null) {
                    for (j = 0; j < jar.size(); ++j) {
                        job2 = (JSONObject) jar.get(j);
                        card = new RadaCard();
                        card.buildCard(Short.parseShort(job2.get("id").toString()), Byte.parseByte(job2.get("amount").toString()), Byte.parseByte(job2.get("level").toString()), Byte.parseByte(job2.get("use").toString()));
                        if (card.set_use == (byte) 1 && player.cCardUse <= (byte) 5) {
                            player.cCardUse = (byte) (player.cCardUse + 1) > (byte) 5 ? (byte) 5 : (byte) (player.cCardUse + 1);
                            RadaCardService.gI().updateBuffRada(player, card);
                        }
                        if (card.id == (short) 956 && card.level >= (byte) 1 && card.set_use == (byte) 1) {
                            player.idAura = (short) 0;
                        }
                        player.cards.add(card);
                        job2.clear();
                    }
                }
                //GET NOI TAI TU DATABASE
                jar = (JSONArray) JSONValue.parse(rs.getString("noitai"));
                if (jar != null && jar.size() > 0) {
                    job2 = (JSONObject) jar.get(0);
                    player.noiTai = new NoiTai(Byte.parseByte(job2.get("id").toString()), Short.parseShort(job2.get("param").toString()));
                    if (player.gender == 0) {
                        player.noiTai.idIcon = NoiTaiTemplate.listNoiTaiTD.get(player.noiTai.id).idIcon;
                        player.noiTai.min = NoiTaiTemplate.listNoiTaiTD.get(player.noiTai.id).min;
                        player.noiTai.max = NoiTaiTemplate.listNoiTaiTD.get(player.noiTai.id).max;
                        player.noiTai.idSkill = NoiTaiTemplate.listNoiTaiTD.get(player.noiTai.id).idSkill;
                        player.noiTai.infoTemp = NoiTaiTemplate.listNoiTaiTD.get(player.noiTai.id).infoTemp;
                        player.noiTai.infoHead = NoiTaiTemplate.listNoiTaiTD.get(player.noiTai.id).infoHead;
                        player.noiTai.infoFoot = NoiTaiTemplate.listNoiTaiTD.get(player.noiTai.id).infoFoot;
                    } else if (player.gender == 1) {
                        player.noiTai.idIcon = NoiTaiTemplate.listNoiTaiNM.get(player.noiTai.id).idIcon;
                        player.noiTai.min = NoiTaiTemplate.listNoiTaiNM.get(player.noiTai.id).min;
                        player.noiTai.max = NoiTaiTemplate.listNoiTaiNM.get(player.noiTai.id).max;
                        player.noiTai.idSkill = NoiTaiTemplate.listNoiTaiNM.get(player.noiTai.id).idSkill;
                        player.noiTai.infoTemp = NoiTaiTemplate.listNoiTaiNM.get(player.noiTai.id).infoTemp;
                        player.noiTai.infoHead = NoiTaiTemplate.listNoiTaiNM.get(player.noiTai.id).infoHead;
                        player.noiTai.infoFoot = NoiTaiTemplate.listNoiTaiNM.get(player.noiTai.id).infoFoot;
                    } else {
                        player.noiTai.idIcon = NoiTaiTemplate.listNoiTaiXD.get(player.noiTai.id).idIcon;
                        player.noiTai.min = NoiTaiTemplate.listNoiTaiXD.get(player.noiTai.id).min;
                        player.noiTai.max = NoiTaiTemplate.listNoiTaiXD.get(player.noiTai.id).max;
                        player.noiTai.idSkill = NoiTaiTemplate.listNoiTaiXD.get(player.noiTai.id).idSkill;
                        player.noiTai.infoTemp = NoiTaiTemplate.listNoiTaiXD.get(player.noiTai.id).infoTemp;
                        player.noiTai.infoHead = NoiTaiTemplate.listNoiTaiXD.get(player.noiTai.id).infoHead;
                        player.noiTai.infoFoot = NoiTaiTemplate.listNoiTaiXD.get(player.noiTai.id).infoFoot;
                    }
                    job2.clear();
                } else {
                    player.noiTai = new NoiTai((byte) 0, (short) 0);
                    player.noiTai.idIcon = (short) 5223;
                    player.noiTai.infoTemp = "Chưa có nội tại";
                    player.noiTai.infoHead = "Chưa có nội tại";
                    player.noiTai.infoFoot = "Chưa có nội tại";
                }
                player.countMoNoiTai = (byte) (rs.getInt("count_noitai"));

                rs = SQLManager.stat.executeQuery("SELECT * FROM `pet` WHERE `account_id`=" + account_id + ";");
                if (rs != null && rs.first()) {
                    player.havePet = 1;
                    player.detu = new Detu();
//                        player.detu.id = (-player.id - 100000);
                    player.detu.name = rs.getString("name");
                    player.detu.head = rs.getShort("head");
                    player.detu.gender = rs.getByte("gender");
                    player.detu.power = rs.getLong("power");
                    player.detu.hpGoc = rs.getInt("hp_goc");
                    player.detu.mpGoc = rs.getInt("mp_goc");
                    player.detu.hp = player.detu.hpGoc;
                    player.detu.mp = player.detu.mpGoc;
                    player.detu.damGoc = rs.getInt("dame_goc");
                    player.detu.defGoc = rs.getShort("def_goc");
                    player.detu.critGoc = rs.getByte("crit_goc");
                    player.detu.tiemNang = rs.getLong("tiem_nang");
                    player.detu.limitPower = rs.getByte("limit_power");
                    player.detu.hpFull = rs.getInt("hp_goc");
                    player.detu.mpFull = rs.getInt("mp_goc");
                    player.detu.damFull = rs.getInt("dame_goc");
                    player.detu.defFull = rs.getShort("def_goc");
                    player.detu.critFull = rs.getByte("crit_goc");
                    player.detu.stamina = rs.getShort("stamina");
//                        player.detu.maxStamina = (short)1000;
                    player.detu.isPet = true;
                    if (player.detu.power < 1500000) {
                        player.detu.isSoSinh = true;
                    }
                    if (rs.getByte("ismabu") == 1) {
                        player.detu.isMabu = true;
                    }
                    if (rs.getByte("isberus") == 1) {
                        player.detu.isBerus = true;
                    }
                    //CHECK DE TU IS DIE
                    if (rs.getByte("isdie") == 1) {
                        player.detu.isdie = true;
                        player.detu.hp = 0;
                    }
                    jar = (JSONArray) JSONValue.parse(rs.getString("skill"));
//                        JSONObject job;
                    int indexPet;
                    if (jar != null) {
                        job = null;
                        SkillData skilldata = new SkillData();
                        Skill templateSkilldetu = null;
                        for (indexPet = 0; indexPet < jar.size(); indexPet++) {
//                                Skill skillpet = new Skill();
                            Skill Skillpet = new Skill();
                            job = (JSONObject) JSONValue.parse(jar.get(indexPet).toString());
                            int id = Integer.parseInt(job.get("id").toString());
                            int level = Integer.parseInt(job.get("point").toString());
                            int gender = Integer.parseInt(job.get("gender").toString());
                            int template = Integer.parseInt(job.get("tempid").toString());
//                                skillpet = player.nClass.getSkillTemplate(id).skills[level - 1];
//                                player.detu.skill.add(skillpet);

                            Skillpet.skillId = (short) id;
                            Skillpet.point = level;
                            Skillpet.genderSkill = (byte) gender;
                            Skillpet.tempSkillId = (short) template;
                            //LAY TIME HIEN TAI KHONG CHO OUT RA VAO LAI DE HOA KHI LUON
                            Skillpet.lastTimeUseThisSkill = System.currentTimeMillis();
                            player.detu.listSkill.add(Skillpet);

                            job.clear();
                        }
                    }

                    player.detu.ItemBody = new Item[7];
                    jar = (JSONArray) JSONValue.parse(rs.getString("itembody"));
                    if (jar != null) {
                        for (j = 0; j < jar.size(); ++j) {
                            job2 = (JSONObject) jar.get(j);
                            indexPet = Byte.parseByte(job2.get("index").toString());
                            player.detu.ItemBody[indexPet] = ItemTemplate.parseItem(jar.get(j).toString());
                            job2.clear();
                        }
                    }
                } else {
                    player.detu = null;
                }
                rs.close();
                return player;
            } else {
                return null;
            }
            //}
        } catch (Exception var23) {
            var23.printStackTrace();
            return null;
        }
    }

    //SAU SE XOA, TAM THOI DUNG DE CHO AE DA NANG CAP
    public Boolean addItemToBagx99(Item item) {
        try {
            byte index = this.getIndexBagNotItem();

            if (getBagNull() <= 0) {
                this.sendAddchatYellow("Hành trang không đủ chỗ trống!");
                return false;
            } else {
                Item _item = new Item(item);
                _item.quantity += 98;
                this.ItemBag[index] = _item;
                return true;
            }
        } catch (Exception var6) {
            var6.printStackTrace();
            return false;
        }

    }

    //CHECK CAI TRANG CHONG CAI LANH
    public boolean checkCTCold() {
        if (ItemBody[5] != null && (ItemBody[5].template.id == 450 || ItemBody[5].template.id == 630 || ItemBody[5].template.id == 631 || ItemBody[5].template.id == 632
                || ItemBody[5].template.id == 878 || ItemBody[5].template.id == 879 || ItemBody[5].template.id >= 386 && ItemBody[5].template.id <= 394)) {
            return true;
        }
        return false;
    }

    //
    public boolean check99ThucAnHuyDiet() {
        for (Item item : this.ItemBag) {
            if (item != null && item.template.id >= 663 && item.template.id <= 667 && item.quantity == 99) {
                return true;
            }
        }
        return false;
    }

    //GET INDEX THUC AN x99
    public byte getIndex99ThucAnHuyDiet() {
        for (byte i = 0; i < this.ItemBag.length; i++) {
            if (this.ItemBag[i] != null && this.ItemBag[i].template.id >= 663 && this.ItemBag[i].template.id <= 667 && this.ItemBag[i].quantity == 99) {
                return i;
            }
        }
        return -1;
    }

    //GET INDEX ITEM THEO ID ITEM VA SO LUONG CAN CHECK
    public byte getIndexItemByIdAndQuantiy(int idItem, int quantity) {
        for (byte i = 0; i < this.ItemBag.length; i++) {
            if (this.ItemBag[i] != null && this.ItemBag[i].template.id == idItem && this.ItemBag[i].quantity >= quantity) {
                return i;
            }
        }
        return -1;
    }

    //CHECK VA TRU SO LUONG ITEM THEO ID ITEM
    public boolean truItemBySL(int idItem, int quantity) {
        for (byte i = 0; i < this.ItemBag.length; i++) {
            if (this.ItemBag[i] != null && this.ItemBag[i].template.id == idItem && this.ItemBag[i].quantity >= quantity) {
                this.ItemBag[i].quantity -= quantity;
                if (this.ItemBag[i].quantity <= 0) {
                    this.ItemBag[i] = null;
                }
                return true;
            }
        }
        return false;
    }

    //CHECK FULL SET THAN LINH
    public boolean checkFullSetThan() {
        if (this.ItemBody[0] != null && this.ItemBody[1] != null && this.ItemBody[2] != null && this.ItemBody[3] != null && this.ItemBody[4] != null) {
            if (this.ItemBody[0].template.level == (byte) 13 && this.ItemBody[1].template.level == (byte) 13 && this.ItemBody[2].template.level == (byte) 13
                    && this.ItemBody[3].template.level == (byte) 13 && this.ItemBody[4].template.level == (byte) 13) {
                return true;
            }
        }
        return false;
    }

    //CHECK FULL SET THEO LEVEL ITEM
    public boolean checkSetByLevel(byte level) {
        if (this.ItemBody[0] != null && this.ItemBody[1] != null && this.ItemBody[2] != null && this.ItemBody[3] != null && this.ItemBody[4] != null) {
            if (this.ItemBody[0].template.level == level && this.ItemBody[1].template.level == level && this.ItemBody[2].template.level == level
                    && this.ItemBody[3].template.level == level && this.ItemBody[4].template.level == level) {
                return true;
            }
        }
        return false;
    }

    //CHECK GIAP LUYEN TAP TRONG HANH TRANG
    public boolean checkThaoGLT() {
        for (Item item : ItemBag) {
            if (item != null && item.id != -1) {
                if (item.template.type == (byte) 32 && item.getParamItemByID(9) > 0) {
                    if (item.id == 531 || item.id == 536) {
                        bonusGLT = (byte) 3;
                    } else if (item.id == 530 || item.id == 535) {
                        bonusGLT = (byte) 2;
                    } else {
                        bonusGLT = (byte) 1;
                    }
                    return true;
                }
            }
        }
        bonusGLT = (byte) 0;
        return false;
    }

    //CHECK DA CO BAN BE
    public boolean isHasFriend(int idFriend) {
        for (byte i = 0; i < this.friends.size(); i++) {
            if (this.friends.get(i).id == idFriend) {
                return true;
            }
        }
        return false;
    }

    //GET OBJ DAU THAN
    public JSONObject ObjectBean() {
        JSONObject put = new JSONObject();
        put.put((Object) "level", (Object) this.levelTree);
        put.put((Object) "time", (Object) this.lastTimeTree);
        put.put((Object) "max", (Object) this.maxBean);
        return put;
    }

    //GET OBJ TASK
    public JSONObject ObjectTask() {
        JSONObject put = new JSONObject();
        put.put((Object) "index", (Object) this.crrTask.index);
        put.put((Object) "count", (Object) this.crrTask.count);
        return put;
    }

    //GET OBJ NGOC RONG SAO DEN
    public JSONObject ObjectNRSD(byte index) {
        JSONObject put = new JSONObject();
        put.put((Object) "index", (Object) index);
        put.put((Object) "time", (Object) this.timeNRSD[index]);
        put.put((Object) "timeend", (Object) this.timeEndNRSD[index]);
        return put;
    }

    //GET OBJ RADA CARD
    public JSONObject ObjectCard(RadaCard card) {
        JSONObject put = new JSONObject();
        put.put((Object) "id", (Object) card.id);
        put.put((Object) "amount", (Object) card.amount);
        put.put((Object) "level", (Object) card.level);
        put.put((Object) "use", (Object) card.set_use);
//        put.put((Object)"unlock", (Object)card.unlock);
        return put;
    }

    public JSONObject ObjectItemUse(int id, long time) {
        JSONObject put = new JSONObject();
        put.put((Object) "id", (Object) id);
        put.put((Object) "time", (Object) time);
        return put;
    }

    public JSONObject ObjectFriend(Friend friend) {
        JSONObject put = new JSONObject();
        put.put((Object) "id", (Object) friend.id);
        put.put((Object) "head", (Object) friend.head);
        put.put((Object) "headICON", (Object) friend.headICON);
        put.put((Object) "body", (Object) friend.body);
        put.put((Object) "leg", (Object) friend.leg);
        put.put((Object) "bag", (Object) friend.bag);
        put.put((Object) "name", (Object) friend.name);
        put.put((Object) "isOnline", (Object) friend.isOnline);
        put.put((Object) "strPower", (Object) friend.strPower);
        return put;
    }
}
