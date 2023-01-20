package nro.task;

import java.util.TimerTask;
import nro.io.Message;
import nro.player.Player;
import nro.map.Mob;

import java.util.ArrayList;

public class ResetTroiTask extends TimerTask {
    public ArrayList<Player> players = new ArrayList<>();
    public Player player;
    public Mob mob;
    public int idPlayerHold;

    public ResetTroiTask(ArrayList<Player> players, Mob mob, Player p) {
        this.players = players;
        this.mob = mob;
        this.player = p;
    }
    
    @Override
    public void run() {
        
        if(!this.player.isTroi) {
            this.cancel();
        } else {
            Message m = null;
            this.player.isTroi = false;
            this.mob.isFreez = false;
            //remove setting troi, va effect tren nguoi mob
            try {
                m = new Message(-124);
                m.writer().writeByte(2);
                m.writer().writeByte(1);
                m.writer().writeByte(32);
                m.writer().writeByte(mob.tempId);
                m.writer().flush();
                for(Player p: this.players) {
                    if(p != null) {
                        p.session.sendMessage(m);
                    }
                }
                m.cleanup();
            } catch (Exception var2) {
                var2.printStackTrace();
            } finally {
                if(m != null) {
                    m.cleanup();
                }
            }

            //remove effect troi cua player
            try {
                m = new Message(-124);
                m.writer().writeByte(2);
                m.writer().writeByte(0);
                m.writer().writeInt(this.player.id);
                m.writer().flush();
                for(Player p: this.players) {
                    if(p != null) {
                        p.session.sendMessage(m);
                    }
                }
                m.cleanup();
            } catch (Exception var2) {
                var2.printStackTrace();
            } finally {
                if(m != null) {
                    m.cleanup();
                }
            }
        }
    }
}
