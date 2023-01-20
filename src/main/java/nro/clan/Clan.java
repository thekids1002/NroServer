package nro.clan;

import java.util.ArrayList;
import nro.map.Map;
import java.util.Timer;

public class Clan {

    public int id;

    public String name;

    public String slogan;

    public byte imgID;

    public byte level;

    public int clanPoint;

    public long powerPoint;

    public int leaderID;
    public String leaderName;

    public byte currMember;

    public byte maxMember;

    public long time;
    public long tcreate;
    public long topen;

    public ArrayList<Member> members;
    public ArrayList<ClanMessage> messages; 
    //LIST DOANH TRAI
    public Map[] doanhTrai = new Map[10];
//    public ArrayList<Map> doanhTrai = new ArrayList<>();
    public boolean openDoanhTrai = false;
    //LIST MAP KHI GA
    public Map[] khiGas = new Map[5];
    public boolean openKhiGas = false;
    public int lvGas = 1;
    public long topenGas = 0;
    public Timer timerGas = null;
    public byte cOpenGas = (byte)3;

    public boolean flagDestroy = false;
    public boolean flagCreate = false;

    public Clan() {
        this.members = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.level = (byte)1;
        this.clanPoint = 0;
        this.powerPoint = 0;
        this.currMember = (byte)1;
        this.maxMember = 10;
        this.time = System.currentTimeMillis();
    }

    public Member getMemberByID(int id) {
        for(Member mem: members) {
            if(mem.id == id) {
                return mem;
            }
        }
        return null;
    }

    public String removeMemberByID(int id) {
        String name = "a";
        for(Member mem: members) {
            if(mem.id == id) {
                name = mem.name;
                members.remove(mem);
                return name;
            }
        }
        return name;
    }

}
