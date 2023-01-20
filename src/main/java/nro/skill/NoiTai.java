package nro.skill;

import org.json.simple.JSONObject;

public class NoiTai {
    public byte id; //ID NOI TAI
    public short param; // CHI SO NOI TAI
    public int min = 0; //CHI SO MIN
    public int max = 0; //CHI SO MAX
    public byte idSkill = -1; //ID TEMPLATE SKILL
    public short idIcon; //ID ANH CUA NOI TAI
    public String infoTemp; //MO TA NOI TAI
    public String infoHead;
    public String infoFoot;
//    public NoiTaiTemplate template;

    public NoiTai(byte _id, short _param) {
        this.id = _id;
        this.param = _param;
    }

    public void newNoiTai(NoiTai _NOITAI) {
        this.id = _NOITAI.id;
        this.param = 0;
        this.min = _NOITAI.min;
        this.max = _NOITAI.max;
        this.idIcon = _NOITAI.idIcon;
        this.infoTemp = _NOITAI.infoTemp;
        this.infoHead = _NOITAI.infoHead;
        this.infoFoot = _NOITAI.infoFoot;
    }

    public JSONObject ObjectNoiTai() {
        JSONObject put = new JSONObject();
        put.put((Object)"id", (Object)this.id);
        put.put((Object)"param", (Object)this.param);
        return put;
    }
}
