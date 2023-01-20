package nro.item;

import java.util.ArrayList;

public class ItemSell {

    public int id;
    //public byte type;
    public Item item;
    public byte buyType = -1;
    public int buyCoin = 0;
    public int buyCoinLock = 0;
    public int buyGold = 0;
    public boolean isNew;
    public int saleCoinLock = 0;
    public static ArrayList<ItemSell> itemCanSell = new ArrayList<>();
    public static ArrayList<Item> items = new ArrayList<>();
    public static ArrayList<Item> itemsNotSell = new ArrayList<>();

    public static ItemSell getItemSellByID(int id)
    {
        for (ItemSell itemSell : itemCanSell) {
            if(itemSell.id == id)
            {
                return itemSell;
            }
        }
        return null;
    }
     public static Item getItem(int id) {

        for (Item items : items) {
            if(items.template.id == id)
            {
                 return items;
            }
        }
        return null;
    }
    public static Item getItemNotSell(int id) {

        for (Item items : itemsNotSell) {
            if(items.template.id == id)
            {
                 return items;
            }
        }
        return null;
    }
    public static ItemSell getItemSell(int id,byte typeBuy) {

        for (ItemSell itemSell : itemCanSell) {
            if(itemSell.item.template.id == id && itemSell.buyType == typeBuy)
            {
                 return itemSell;
            }
        }
        return null;
    }
}
