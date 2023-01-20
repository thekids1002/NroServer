package nro.task;

import java.util.TimerTask;
import nro.io.Message;
import nro.main.Service;
import nro.player.Player;

public class ResetHuytSaoTask extends TimerTask {
    public Player player;

    public ResetHuytSaoTask(Player p) {
        this.player = p;
    }
    
    @Override
    public void run() {
        Message m = null;
        this.player.isHuytSao = false;
        this.player.buffHuytSao = 0;
        this.player.hp = this.player.hp > this.player.getHpFull() ? this.player.getHpFull() : this.player.hp;
        try {
            m = new Message(-124);
            m.writer().writeByte(0);
            m.writer().writeByte(0);
            m.writer().writeByte(39);
            m.writer().writeInt(this.player.id);
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

        Service.gI().loadPoint(this.player.session, this.player);
    }
}
