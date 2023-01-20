package nro.task;

import java.util.TimerTask;
import nro.io.Message;
import nro.main.Service;
import nro.player.Player;

public class ResetMonkeyTask extends TimerTask {
    public Player player;

    public ResetMonkeyTask(Player p) {
        this.player = p;
    }
    
    @Override
    public void run() {
        if(this.player.isdie || !this.player.isMonkey) {
            this.cancel();
            this.player.upDameAfterKhi = false;
            return;
        }
        if(this.player.isMonkey) {
            Message m = null;
            try {
                Service.gI().loadCaiTrangTemp(this.player);

                for (Player p : this.player.zone.players) {
                    if(p.id != this.player.id) {
                        p.sendDefaultTransformToPlayer(this.player);
                    }
                }

                this.player.isMonkey = false;
                //NOI TAI TANG DAME KHI HOA KHI
                if(this.player.upDameAfterKhi && this.player.noiTai.id != 0 && this.player.noiTai.idSkill == (byte)13) {
                    this.player.upDameAfterKhi = false;
                }
                //NOI TAI TANG DAME KHI HOA KHI
                Service.gI().loadPoint(this.player.session, this.player);

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
