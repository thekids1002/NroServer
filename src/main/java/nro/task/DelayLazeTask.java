package nro.task;

import java.util.TimerTask;
import nro.io.Message;
import nro.player.Player;
import nro.skill.Skill;
import java.util.ArrayList;

public class DelayLazeTask extends TimerTask {
    public ArrayList<Player> players = new ArrayList<>();
    public Player player;
    public Skill skill;

    public DelayLazeTask(ArrayList<Player> listPlayer, Player iplayer, Skill iskill) {
        this.players = listPlayer;
        this.player = iplayer;
        this.skill = iskill;
    }

    @Override
    public void run() {
        Message m = null;
        try {
            m = new Message(-45);
            m.writer().writeByte(4);
            m.writer().writeInt(player.id); // id player use  
            m.writer().writeShort(skill.skillId);
            m.writer().writeShort(3); //    seconds
            m.writer().flush();
            for(Player p: players) {
                p.session.sendMessage(m);
            }
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(m != null) {
                m.cleanup();
            }
        }
    }
}
