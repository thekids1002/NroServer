package nro.card;

import nro.item.ItemOption;
import java.util.ArrayList;

public class RadaCard {
    public short id;
    public short idIcon;
    public byte rank;
    public byte amount;
    public byte max_amount;
    public byte is_cardmob;
    public short temp_mob;

    public short head;
    public short body;
    public short leg;
    public short bag;

    public String name;
    public String info;
    public byte level;
    public byte set_use;

    public ArrayList<ItemOption> itemOptions = new ArrayList<>();

    public RadaCard template;

    public void buildCard(short id, byte amount, byte level, byte use) {
        this.id = id;
        this.amount = amount;
        this.level = level;
        this.set_use = use;
        this.template = RadaCardManager.gI().getCardById(id);
    }
}
