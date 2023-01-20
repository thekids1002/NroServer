package nro.task;

import java.util.TimerTask;
import nro.io.Message;
import nro.player.Player;
import nro.map.Mob;

public class ResetSocolaTask extends TimerTask {
    public Player player;
    public Mob mob;

    public ResetSocolaTask(Player p, Mob mob) {
        this.player = p;
        this.mob = mob;
    }
    
    @Override
    public void run() {
        Message m = null;
        this.mob.isSocola = 0;
        //remove setting socola, va effect tren nguoi mob
        try {
            m = new Message(-112);
            m.writer().writeByte(0);
            m.writer().writeByte(this.mob.tempId);
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