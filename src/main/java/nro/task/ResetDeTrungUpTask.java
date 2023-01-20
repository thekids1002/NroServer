package nro.task;

import java.util.TimerTask;
import nro.io.Message;
import nro.main.Service;
import nro.player.Player;

public class ResetDeTrungUpTask extends TimerTask {
    public Player player;

    public ResetDeTrungUpTask(Player p) {
        this.player = p;
    }
    
    @Override
    public void run() {
        if(this.player != null) {
            Message m = null;
            this.player.isDeTrung = false;
            Service.gI().loadPoint(this.player.session, this.player);
        }
    }
}
