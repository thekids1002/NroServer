package nro.item;

import nro.main.Util;

public class ItemOption {
    public int id;
    public int param;
    public boolean isExpires = false;
    public boolean isUpToUp;
    //FOR RADA CARD
    public byte active_card;
    public ItemOptionTemplate optionTemplate;

    public ItemOption() {
    }

    public ItemOption(int tempId, int param) {
        this.id = tempId;
        this.optionTemplate = ItemData.iOptionTemplates[tempId];
        this.param = param;
    }

    public void ItemOptionCard(int tempId, int param, byte active_card) {
        this.id = tempId;
        this.optionTemplate = ItemData.iOptionTemplates[tempId];
        this.param = param;
        this.active_card = active_card;
    }

    public String getOptionString() {
        return Util.replace(this.optionTemplate.name, "#", String.valueOf(this.param));  
    }
}
