package nro.task;

import java.util.ArrayList;
import java.util.TimerTask;
import nro.io.Message;
import nro.player.Player;
import nro.player.Detu;
import nro.player.Boss;
import nro.map.Mob;

public class ResetBlindTask extends TimerTask {
    public ArrayList<Mob> mobs;
    public ArrayList<Player> players;
    public ArrayList<Boss> bosses;
    public ArrayList<Detu> detus;
    public int countMob = 0;
    public int countChar = 0;
    public int countBoss = 0;
    public int[] arrayMob;
    public int[] arrayChar;
    public ArrayList<Integer> arrayBoss;
    public ArrayList<Integer> arrayDetu;

    public ResetBlindTask(ArrayList<Mob> mobss, int cMob, int[] arrMob, ArrayList<Player> playerss, int cChar, int[] arrChar, ArrayList<Integer> arrBoss, ArrayList<Boss> bossess, ArrayList<Integer> arrDetu, ArrayList<Detu> petss) {
        //int cBoss, int[] arrBoss, ArrayList<Boss> bossess
        this.mobs = mobss;
        this.countMob = cMob;
        this.arrayMob = arrMob;
        this.players = playerss;
        this.countChar = cChar;
        this.arrayChar = arrChar;
        //Boss
        this.bosses = bossess;
//        this.countBoss = cBoss;
        this.arrayBoss = arrBoss;
        this.detus = petss;
        this.arrayDetu = arrDetu;
    }
    
    @Override
    public void run() {
        Message m = null;
        if(this.countMob > 0) {
            for(int i = 0; i < countMob; i++) {
                this.mobs.get(this.arrayMob[i]).isBlind = false;
            }
        }

        if(this.countChar > 0) {
            for(int i = 0; i < countChar; i++) {
                for(Player _p: this.players) {
                    if(_p.id == this.arrayChar[i]) {
                        _p.isCharBlind = false;
                    }
                }
            }
        }

        if(this.arrayBoss.size() > 0) {
            for(int i = 0; i < this.arrayBoss.size(); i++) {
                for(Boss _boss: this.bosses) {
                    if(_boss.id == this.arrayBoss.get(i)) {
                        _boss.isCharBlind = false;
                    }
                }
            }
        }
        
        if(this.arrayDetu.size() > 0) {
            for(int i = 0; i < this.arrayDetu.size(); i++) {
                for(Detu _pet: this.detus) {
                    if(_pet.id == this.arrayDetu.get(i)) {
                        _pet.isCharBlind = false;
                    }
                }
            }
        }

    }
}
