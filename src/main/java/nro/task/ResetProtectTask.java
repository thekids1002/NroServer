package nro.task;

import java.util.TimerTask;
import nro.io.Message;
import nro.player.Player;

public class ResetProtectTask extends TimerTask {
    public Player player;
    public int idPlayerProtect;

    public ResetProtectTask(Player p, int id) {
        this.player = p;
        this.idPlayerProtect = id;
    }
    
    @Override
    public void run() {
        Message m = null;
        this.player.isProtect = false;
        //remove setting khien, va effect tren nguoi
        try {
            m = new Message(-124);
            m.writer().writeByte(2);
            m.writer().writeByte(0);
            m.writer().writeByte(33);
            m.writer().writeInt(player.id);
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

        //remove effect khien cua player
        try {
            m = new Message(-124);
            m.writer().writeByte(2);
            m.writer().writeByte(0);
            m.writer().writeInt(this.idPlayerProtect);
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
