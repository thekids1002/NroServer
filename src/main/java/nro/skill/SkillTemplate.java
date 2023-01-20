package nro.skill;

import org.json.simple.JSONObject;

public class SkillTemplate {

    public boolean isBuffToPlayer() {
        return this.type == 2;
    }

    public boolean isUseAlone() {
        return this.type == 3;
    }

    public boolean isAttackSkill() {
        return this.type == 1;
    }

    public byte id;

    public int classId;

    public String name;

    public int maxPoint;

    public int manaUseType;

    public int type;

    public int iconId;

    public String[] description;

    public Skill[] skills;

    public String damInfo;
    public static JSONObject ObjectSkill(Skill skill, long lastTimeUse) {
        JSONObject put = new JSONObject();
        put.put((Object)"id", (Object)skill.skillId);
        put.put((Object)"point", (Object)skill.point);
        put.put((Object)"lastuse", (Object)lastTimeUse);
        return put;
    }

    public static JSONObject ObjectSkillPet(Skill skill) {
        JSONObject put = new JSONObject();
        put.put((Object)"id", (Object)skill.skillId);
        put.put((Object)"point", (Object)skill.point);
        put.put((Object)"gender", (Object)skill.genderSkill);
        put.put((Object)"tempid", (Object)skill.tempSkillId);
        return put;
    }
}
