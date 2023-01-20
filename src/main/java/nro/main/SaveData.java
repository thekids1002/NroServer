package nro.main;

import java.util.Calendar;

import nro.player.Player;
import nro.player.PlayerDAO;
import nro.player.PlayerManger;

public class SaveData extends Thread{
     public static SaveData gI() {
        if (instance == null) {
            instance = new SaveData();
        }
        return instance;
    }
    private static SaveData instance;
    @Override
    public void run(){
        try{
            while (true) {
                Calendar calendar = Calendar.getInstance();
                int sec = calendar.get(13);
//                if((sec == 30 || sec == 0)) {
                if((sec == 45)) {
//                    Connection conn = DataSource.getConnection();
                    for (int i=0; i< PlayerManger.gI().conns.size(); i++) {
                        if(PlayerManger.gI().conns.get(i) != null && PlayerManger.gI().conns.get(i).player != null) {
                            Player player = PlayerManger.gI().conns.get(i).player;
                            if(player!=null){
                                PlayerDAO.updateDBAuto(player, DataSource.connSaveData);
                            }
                        }
                    }
//                    conn.close();
                }
                Thread.sleep(1000L);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
