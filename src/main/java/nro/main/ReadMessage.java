package nro.main;

import nro.io.Message;
import nro.io.Session;
import nro.player.Player;
import nro.player.PlayerManger;

public class ReadMessage {

    private static ReadMessage instance;

    public static ReadMessage gI() {
        if (instance == null) {
            instance = new ReadMessage();
        }
        return instance;
    }

    //get use item
    public void getItem(Session session, Message msg) {
        Player player = PlayerManger.gI().getPlayerByUserID(session.userId);
        try {
            byte type = msg.reader().readByte();
            byte index = msg.reader().readByte();
//            System.out.println("type use item: " + type);
//            System.out.println("index item: " + index);
            switch (type){
                case 0:
                    player.itemBoxToBag(index);
                    break;
                case 1:
                    player.itemBagToBox(index);
                    break;
                case 4:
                    player.itemBagToBody(index);
                    break;
                case 5:
                    player.itemBodyToBag(index);
                    break;
                case 6: //mac do cho de tu
                    if(player != null && player.detu != null) {
                        player.itemBagToBodyPet(index);
                    }
                    break;
                case 7: //thao do cua de tu
                    if(player != null && player.detu != null) {
                        player.itemBodyToBagPet(index);
                    }
                    break;
                case 31:
                    if(player != null && player.detu != null) {
                        Service.gI().statusDetu(player);
                    }
                    break;
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
