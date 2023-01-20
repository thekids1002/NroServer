package nro.task;

import java.util.TimerTask;
import nro.io.Message;
import nro.player.Player;
import nro.map.Mob;

public class ResetSleepTask extends TimerTask {
    public Player player;
    public Mob mob;

    public ResetSleepTask(Player p, Mob mob) {
        this.player = p;
        this.mob = mob;
    }
    
    @Override
    public void run() {
        Message m = null;
        this.mob.isSleep = false;
        //remove setting thoi mien, va effect tren nguoi mob
        try {
            m = new Message(-124);
            m.writer().writeByte(0);
            m.writer().writeByte(1);
            m.writer().writeByte(41);
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