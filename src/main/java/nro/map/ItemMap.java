package nro.map;

import nro.item.Item;

public class ItemMap {
    public Item item;
    public int playerId;
    public int itemMapID;
    public short itemTemplateID;
    public int x;
    public int y;
    public short rO;
    public long removedelay = 30000L + System.currentTimeMillis();
    public long timeDrop = 0;
}
