package nro.map;

import nro.player.Player;

public class Mob {

    public int tempId;
    public int level;
    public int hp;
    public int maxHp;
    public short pointX;
    public short pointY;
    public long xpup;
    public byte status;
    public boolean isDie;
    public boolean isRefresh = true; // hoi sinh quai
    public boolean isboss = false;
    public boolean isFreez = false; // check bi troi khong
    public boolean isBlind = false; // check bi stun khong
    public boolean isDCTT = false; // check dich chuyen tuc thoi
    public boolean isSleep = false; // thoi mien
    public short isSocola = 0; //socola
    public long timeRefresh;
    public long timeFight;
    public MobTemplate template;
    public Player pTarget;
    //BIGBOSS HIRUREGARN
    public long delayBoss = 0;
    public byte typeHiru = 0;
    public Mob(int id,int idtemplate, int level) {
        this.tempId = id;
        this.template = MobTemplate.entrys.get(idtemplate);
        this.level = level;
        this.hp = maxHp = template.maxHp;
        this.xpup = 10000;
        this.isDie = false;
        this.isRefresh = true;
        this.level = level;
        this.xpup = 100000L;
        this.isDie = false;
        this.timeFight = -1L;
        
    }
    public static MobTemplate getMob(int id) {
        for (MobTemplate mob : MobTemplate.entrys) {
            if (mob.tempId == id) {
                return mob;
            }
        }
        return null;
    }
    public void updateHP(int num) {
        hp += num;
        if (hp <= 0) {            
            hp = 0;
            status = 0;
            isDie = true;
            //reset char target
            pTarget = null;
            if (isRefresh) {
                timeRefresh = System.currentTimeMillis()+ 11000;
            }
        }
    }
    public void update() {

    }
}
