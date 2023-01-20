package nro.task;

import java.util.TimerTask;
import nro.io.Message;
import nro.player.Player;
import nro.map.Mob;

public class ResetDCTTTask extends TimerTask {
    public Player player;
    public Mob mob;

    public ResetDCTTTask(Player p, Mob mob) {
        this.player = p;
        this.mob = mob;
    }
    
    @Override
    public void run() {
        Message m = null;
        this.mob.isDCTT = false;
        //remove setting dctt, va effect tren nguoi mob
        try {
            m = new Message(-124);
            m.writer().writeByte(0);
            m.writer().writeByte(1);
            m.writer().writeByte(40);
            m.writer().writeByte(mob.tempId);
            m.writer().flush();
            this.player.session.sendMessage(m);
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
