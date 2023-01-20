package nro.giftcode;

import java.util.ArrayList;
import java.util.HashMap;

public class GiftCode {

    String code;
    int Soluong;
    public HashMap<Integer, Integer> detail = new HashMap<>();
    public ArrayList<Integer> listIdPlayer = new ArrayList<>();

    public boolean isUsedGiftCode(int idPlayer) {
        return listIdPlayer.contains(idPlayer);
    }

    public void addPlayerUsed(int idPlayer) {
        listIdPlayer.add(idPlayer);
    }
}
