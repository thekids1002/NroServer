package nro.player;

import nro.main.Util;

public class Boss extends Player {

    public byte _typeBoss = 0; //1: Broly 2: Super broly, 3:Cooler 1, 4:Cooler, 5: Black Goku, 6: Super Black Goku
    public int _numberBoss = 0;
    public boolean isBoom = false;
    public long callXenCon = 0;
    public int lvGas = 1;
    public int idClan = 0;
    //Flag check boss yardrat move
    public byte moveRight = (byte) 1;
    public short xStart = -1;
    public short yStart = -1;

    public long lastTimeUseChargeSkill = 0;
//    public boolean isUpPoint = false;
//    public long lastTimeUpPoint = 0;
//    public ArrayList<Skill> skill;
//    public Detu(Player n) {

    public Boss(int Id, byte type, short xBoss, short yBoss) {
        this.id = -200000 - Id; //ID BOSS -200000 -> -200100 BROLY, -200101 Cooler, -200102 Black, -200110//Kuku, -200115//Mapdaudinh, -200120//RAMBO
        // -200125 so4, -200126 so3, -200127 so2, -200128 so1, -200129 Tieudoitruong, -200130 Fide1, -200131 Fide2, -200132 Fide3
        // -200135 Android19, -200136 Android 20, -200137 Android 15, -200138 Android 14, -200139 Android 13
        // -200140 Poc, -200141 Pic, -200142 KingKong, -200143 Xen 1, -200144 Xen 2, -200145 Xen hoan thien
        // -200146 Xen hoan thien vo dai, -200147 Sieu Bo Hung
        // -200150  //TRUNG UY TRANG
        // -200200 -> -200250 Drabula
        // -200500 -> Chilled, -200501 -> Chilled 2
        // -200502 -> Lychee, -200503 -> Hita
        // -200510 -> -200527 -> Boss up Yardrat
        this._numberBoss = Id;
        this._typeBoss = type;
        this.gender = 3;
        this.power = 1200;
        this.tiemNang = 0;
        this.limitPower = 0;
         if (type == 1) {
            this.name = "Broly " + Id;
            this.head = 291;
            this.hpGoc = Util.nextInt(500, 60000);
            this.mpGoc = 500;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 50;
            this.defGoc = 0;
            this.critGoc = 0;
            this.hpFull = this.hpGoc;
            this.mpFull = 500;
            this.damFull = 50;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        }  else if (type == 2) {
            this.name = "Super Broly " + Id;
            this.head = 294;
            this.hpGoc = Util.nextInt(1000000, 16070777);
            if (this.hpGoc > 14000000) {
                this.hpGoc = 16070777;
            }
            this.mpGoc = 500;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = (int) (this.hpGoc * 0.1);
            this.defGoc = 0;
            this.critGoc = 0;
            this.hpFull = this.hpGoc;
            this.mpFull = 500;
            this.damFull = this.damGoc;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == 3) {
            this.name = "Cooler";
            this.head = 317;
            this.hpGoc = 2000000000;
            this.mpGoc = 2000000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 50000;
            this.defGoc = 10000;
            this.critGoc = 20;
            this.hpFull = 2000000000;
            this.mpFull = 2000000000;
            this.damFull = 50000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == 4) {
            this.name = "Cooler 2";
            this.head = 320;
            this.hpGoc = 2000000000;
            this.mpGoc = 2000000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 100000;
            this.defGoc = 20000;
            this.critGoc = 20;
            this.hpFull = 2000000000;
            this.mpFull = 2000000000;
            this.damFull = 100000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == 5) {
            this.name = "Black Goku";
            this.head = 550;
            this.hpGoc = 2000000000;
            this.mpGoc = 2000000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 50000;
            this.defGoc = 30000;
            this.critGoc = 50;
            this.hpFull = 2000000000;
            this.mpFull = 2000000000;
            this.damFull = 50000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == 6) {
            this.name = "Super Black Goku";
            this.head = 553;
            this.hpGoc = 2000000000;
            this.mpGoc = 2000000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 120000;
            this.defGoc = 30000;
            this.critGoc = 50;
            this.hpFull = 2000000000;
            this.mpFull = 2000000000;
            this.damFull = 120000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == 7) {
            this.name = "Kuku";
            this.head = 159;
            this.hpGoc = 25000000; //700000
            this.mpGoc = 25000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 5000;
            this.defGoc = 1000;
            this.critGoc = 10;
            this.hpFull = 25000000;
            this.mpFull = 25000000;
            this.damFull = 5000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 8) {
            this.name = "Mập Đầu Đinh";
            this.head = 165;
            this.hpGoc = 30000000; //1500000
            this.mpGoc = 30000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 10000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 30000000;
            this.mpFull = 30000000;
            this.damFull = 10000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 9) {
            this.name = "Rambo";
            this.head = 162;
            this.hpGoc = 35000000; //2000000
            this.mpGoc = 35000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 15000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 35000000;
            this.mpFull = 35000000;
            this.damFull = 15000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 10) { //SO 4 TIEU DOI SAT THU
            this.name = "Số 4";
            this.head = 168;
            this.hpGoc = 50000000; //2000000
            this.mpGoc = 50000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 15000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 50000000;
            this.mpFull = 50000000;
            this.damFull = 15000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 11) { //SO 3 TIEU DOI SAT THU
            this.name = "Số 3";
            this.head = 174;
            this.hpGoc = 60000000; //3000000
            this.mpGoc = 60000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 18000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 60000000;
            this.mpFull = 60000000;
            this.damFull = 18000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 1;
            this.isBOSS = true;
        } else if (type == (byte) 12) { //SO 2 TIEU DOI SAT THU
            this.name = "Số 2";
            this.head = 171;
            this.hpGoc = 70000000; //4000000
            this.mpGoc = 70000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 20000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 70000000;
            this.mpFull = 70000000;
            this.damFull = 20000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 1;
            this.isBOSS = true;
        } else if (type == (byte) 13) { //SO 1 TIEU DOI SAT THU
            this.name = "Số 1";
            this.head = 177;
            this.hpGoc = 80000000; //3500000
            this.mpGoc = 80000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 20000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 80000000;
            this.mpFull = 80000000;
            this.damFull = 20000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 1;
            this.isBOSS = true;
        } else if (type == (byte) 14) { //TIEU DOI TRUONG
            this.name = "Tiểu Đội Trưởng";
            this.head = 180;
            this.hpGoc = 100000000; //5000000
            this.mpGoc = 100000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 25000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 100000000;
            this.mpFull = 100000000;
            this.damFull = 25000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 1;
            this.isBOSS = true;
        } else if (type == (byte) 15) { //FIDE DAI CA 1
            this.name = "Fide Đại Ca 1";
            this.head = 183;
            this.hpGoc = 100000000; //2000000
            this.mpGoc = 100000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 30000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 100000000;
            this.mpFull = 100000000;
            this.damFull = 30000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 16) { //FIDE DAI CA 2
            this.name = "Fide Đại Ca 2";
            this.head = 186;
            this.hpGoc = 110000000; //5000000
            this.mpGoc = 110000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 35000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 110000000;
            this.mpFull = 110000000;
            this.damFull = 35000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 17) { //FIDE DAI CA 3
            this.name = "Fide Đại Ca 3";
            this.head = 189;
            this.hpGoc = 120000000; //10000000
            this.mpGoc = 120000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 35000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 120000000;
            this.mpFull = 120000000;
            this.damFull = 35000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 18) { //ANDROID 19
            this.name = "Android 19";
            this.head = 249;
            this.hpGoc = 150000000; //10000000
            this.mpGoc = 150000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 35000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 150000000;
            this.mpFull = 150000000;
            this.damFull = 35000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 19) { //ANDROID 20
            this.name = "Dr.Kôrê";
            this.head = 255;
            this.hpGoc = 170000000; //10000000
            this.mpGoc = 170000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 37000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 170000000;
            this.mpFull = 170000000;
            this.damFull = 37000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 20) { //ANDROID 15
            this.name = "Android 15";
            this.head = 261;
            this.hpGoc = 170000000; //10000000
            this.mpGoc = 170000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 25000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 170000000;
            this.mpFull = 170000000;
            this.damFull = 25000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 21) { //ANDROID 14
            this.name = "Android 14";
            this.head = 246;
            this.hpGoc = 180000000; //10000000
            this.mpGoc = 180000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 25000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 180000000;
            this.mpFull = 180000000;
            this.damFull = 25000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 22) { //ANDROID 13
            this.name = "Android 13";
            this.head = 252;
            this.hpGoc = 200000000; //10000000
            this.mpGoc = 200000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 30000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 200000000;
            this.mpFull = 200000000;
            this.damFull = 30000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 23) { //POC
            this.name = "Poc";
            this.head = 240;
            this.hpGoc = 250000000; //10000000
            this.mpGoc = 250000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 35000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 250000000;
            this.mpFull = 250000000;
            this.damFull = 35000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 24) { //PIC
            this.name = "Pic";
            this.head = 237;
            this.hpGoc = 270000000; //10000000
            this.mpGoc = 270000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 40000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 270000000;
            this.mpFull = 270000000;
            this.damFull = 40000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 1;
            this.isBOSS = true;
        } else if (type == (byte) 25) { //KING KONG
            this.name = "King Kong";
            this.head = 243;
            this.hpGoc = 300000000; //10000000
            this.mpGoc = 300000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 45000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 300000000;
            this.mpFull = 300000000;
            this.damFull = 45000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 1;
            this.isBOSS = true;
        } else if (type == (byte) 26) { //XEN BO HUNG 1
            this.name = "Xên Bọ Hung 1";
            this.head = 228;
            this.hpGoc = 300000000; //10000000
            this.mpGoc = 300000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 45000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 300000000;
            this.mpFull = 300000000;
            this.damFull = 45000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 27) { //XEN BO HUNG 2
            this.name = "Xên Bọ Hung 2";
            this.head = 231;
            this.hpGoc = 320000000; //10000000
            this.mpGoc = 320000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 45000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 320000000;
            this.mpFull = 320000000;
            this.damFull = 45000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 28) { //XEN HOAN THIEN
            this.name = "Xên Hoàn Thiện";
            this.head = 234;
            this.hpGoc = 350000000; //10000000
            this.mpGoc = 350000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 50000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 350000000;
            this.mpFull = 350000000;
            this.damFull = 50000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 29) { //XEN BO HUNG CON
            this.name = "Xên Con";
            this.head = 264;
            this.hpGoc = 5000000; //10000000
            this.mpGoc = 5000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 30000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 5000000;
            this.mpFull = 5000000;
            this.damFull = 30000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
            //INIT NHU PLAYER
//            this.taskId = -1;
//            Skill skill = new Skill();
//            int id = 14;
//            int level = 7;
//            //SET UP SKILL
//            Skill _SKILLTEMP = SkillData.nClasss[2].getSkillTemplate(id).skills[level - 1];
//            skill.template = _SKILLTEMP.template;
//            skill.skillId = _SKILLTEMP.skillId;
//            skill.point = _SKILLTEMP.point;
//            skill.powRequire = _SKILLTEMP.powRequire;
//            skill.coolDown = _SKILLTEMP.coolDown;
//            skill.lastTimeUseThisSkill = 0;
//            skill.dx = _SKILLTEMP.dx;
//            skill.dy = _SKILLTEMP.dy;
//            skill.maxFight = _SKILLTEMP.maxFight;
//            skill.manaUse = _SKILLTEMP.manaUse;
//            skill.options = _SKILLTEMP.options;
//            skill.paintCanNotUseSkill = _SKILLTEMP.paintCanNotUseSkill;
//            skill.damage = _SKILLTEMP.damage;
//            skill.moreInfo = _SKILLTEMP.moreInfo;
//            skill.price = _SKILLTEMP.price;
//            skill.genderSkill = (byte)2;
//            skill.tempSkillId = (short)_SKILLTEMP.template.id;
//
//            this.skill.add(skill);
//            this.vang =0;
//            this.ngoc =0;
//            this.ngocKhoa=0;
//            this.NhapThe =0;
        } else if (type == (byte) 30) { //SIEU BO HUNG
            this.name = "Siêu Bọ Hung";
            this.head = 234;
            this.hpGoc = 2000000000; //10000000
            this.mpGoc = 2000000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 60000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 2000000000;
            this.mpFull = 2000000000;
            this.damFull = 60000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 31) { //TRUNG UY TRANG
            this.name = "Trung úy Trắng";
            this.head = 141;
            this.hpGoc = 500;
            this.mpGoc = 500;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 50;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 500;
            this.mpFull = 500;
            this.damFull = 50;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 32) { //TRUNG UY XANH LO
            this.name = "Trung úy Xanh Lơ";
            this.head = 135;
            this.hpGoc = 500;
            this.mpGoc = 500;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 50;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 500;
            this.mpFull = 500;
            this.damFull = 50;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 33) { //TRUNG UY THEP
            this.name = "Trung úy Thép";
            this.head = 129;
            this.hpGoc = 500;
            this.mpGoc = 500;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 50;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 500;
            this.mpFull = 500;
            this.damFull = 50;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 34) { //NINJA AO TIM
            this.name = "Ninja Áo Tím";
            this.head = 123;
            this.hpGoc = 500;
            this.mpGoc = 500;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 50;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 500;
            this.mpFull = 500;
            this.damFull = 50;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 35) { //ROBOT VE SI
            this.name = "Robốt Vệ Sĩ";
            this.head = 138;
            this.hpGoc = 500;
            this.mpGoc = 500;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 50;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 500;
            this.mpFull = 500;
            this.damFull = 50;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 36) { //DRABULA // -200200 -> -200250
            this.name = "Drabura";
            this.head = 418;
            this.hpGoc = 50000000;
            this.mpGoc = 50000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 20000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 50000000;
            this.mpFull = 50000000;
            this.damFull = 20000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 37) { //BUIBUI
            this.name = "Pui Pui";
            this.head = 451;
            this.hpGoc = 60000000;
            this.mpGoc = 60000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 30000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 60000000;
            this.mpFull = 60000000;
            this.damFull = 30000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 38) { //BUIBUI
            this.name = "Ya Côn";
            this.head = 415;
            this.hpGoc = 100000000;
            this.mpGoc = 100000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 40000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 100000000;
            this.mpFull = 100000000;
            this.damFull = 40000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 39) { //MABU
            this.name = "Ma Bư";
            this.head = 297;
            this.hpGoc = 150000000;
            this.mpGoc = 150000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 50000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 150000000;
            this.mpFull = 150000000;
            this.damFull = 50000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 40) { //CHILLED
            this.name = "Chilled";
            this.head = 1024;
            this.hpGoc = 1000000000;
            this.mpGoc = 1000000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 80000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 1000000000;
            this.mpFull = 1000000000;
            this.damFull = 80000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 41) { //CHILLED
            this.name = "Chilled 2";
            this.head = 1021;
            this.hpGoc = 1500000000;
            this.mpGoc = 1500000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 80000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 1500000000;
            this.mpFull = 1500000000;
            this.damFull = 80000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 42) { //LYCHEE
            this.name = "Dr Lychee";
            this.head = 742;
            this.hpGoc = 11000000;
            this.mpGoc = 11000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 1100;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 11000000;
            this.mpFull = 11000000;
            this.damFull = 1100;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 43) { //Hatchiyack
            this.name = "Hatchiyack";
            this.head = 639;
            this.hpGoc = 13000000;
            this.mpGoc = 13000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 1300;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 13000000;
            this.mpFull = 13000000;
            this.damFull = 1300;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 44) { // Tập sự 510 -> 514
            this.name = "Tập sự";
            this.head = 526;
            this.hpGoc = 350000;
            this.mpGoc = 350000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 3500;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 350000;
            this.mpFull = 350000;
            this.damFull = 3500;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 45) { // Tan binh
            this.name = "Tân binh";
            this.head = 527;
            this.hpGoc = 450000;
            this.mpGoc = 450000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 4500;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 450000;
            this.mpFull = 450000;
            this.damFull = 4500;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 46) { // Chien binh
            this.name = "Chiến binh";
            this.head = 528;
            this.hpGoc = 500000;
            this.mpGoc = 500000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 5000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 500000;
            this.mpFull = 500000;
            this.damFull = 5000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 47) { //Doi truong
            this.name = "Đội trưởng";
            this.head = 529;
            this.hpGoc = 1000000;
            this.mpGoc = 1000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 10000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 1000000;
            this.mpFull = 1000000;
            this.damFull = 10000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 48) { //YAMARSU
            this.name = "Zamasu";
            this.head = 903;
            this.hpGoc = 2000000000;
            this.mpGoc = 2000000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 140000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 2000000000;
            this.mpFull = 2000000000;
            this.damFull = 140000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 49) { //Bill
            this.name = "Bill";
            this.head = 508;
            this.hpGoc = 1000000000;
            this.mpGoc = 1000000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 200000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 1000000000;
            this.mpFull = 1000000000;
            this.damFull = 200000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        } else if (type == (byte) 50) { //Whis
            this.name = "Whis";
            this.head = 838;
            this.hpGoc = 1000000000;
            this.mpGoc = 1000000000;
            this.hp = this.hpGoc;
            this.mp = this.mpGoc;
            this.damGoc = 250000;
            this.defGoc = 2000;
            this.critGoc = 20;
            this.hpFull = 1000000000;
            this.mpFull = 1000000000;
            this.damFull = 250000;
            this.defFull = 0;
            this.critFull = 0;
            this.x = xBoss;
            this.y = yBoss;
            this.typePk = 5;
            this.isBOSS = true;
        }
    }

    public short BossPartHead() {
        return head;
    }

    public void setXYStart(int number) {
        switch (number) {
            case 510: {
                this.xStart = (short) 140;
                this.yStart = (short) 456;
                break;
            }
            case 511: {
                this.xStart = (short) 379;
                this.yStart = (short) 456;
                break;
            }
            case 512: {
                this.xStart = (short) 627;
                this.yStart = (short) 456;
                break;
            }
            case 513: {
                this.xStart = (short) 780;
                this.yStart = (short) 456;
                break;
            }
            case 514: {
                this.xStart = (short) 1058;
                this.yStart = (short) 456;
                break;
            }
            case 515: {
                this.xStart = (short) 1220;
                this.yStart = (short) 456;
                break;
            }
            case 516: {
                this.xStart = (short) 186;
                this.yStart = (short) 456;
                break;
            }
            case 517: {
                this.xStart = (short) 374;
                this.yStart = (short) 456;
                break;
            }
            case 518: {
                this.xStart = (short) 631;
                this.yStart = (short) 432;
                break;
            }
            case 519: {
                this.xStart = (short) 785;
                this.yStart = (short) 432;
                break;
            }
            case 520: {
                this.xStart = (short) 1046;
                this.yStart = (short) 456;
                break;
            }
            case 521: {
                this.xStart = (short) 1240;
                this.yStart = (short) 432;
                break;
            }
            case 522: {
                this.xStart = (short) 238;
                this.yStart = (short) 456;
                break;
            }
            case 523: {
                this.xStart = (short) 413;
                this.yStart = (short) 456;
                break;
            }
            case 524: {
                this.xStart = (short) 595;
                this.yStart = (short) 456;
                break;
            }
            case 525: {
                this.xStart = (short) 840;
                this.yStart = (short) 456;
                break;
            }
            case 526: {
                this.xStart = (short) 1050;
                this.yStart = (short) 456;
                break;
            }
            case 527: {
                this.xStart = (short) 1233;
                this.yStart = (short) 456;
                break;
            }
            default: {
                break;
            }
        }
    }
}
