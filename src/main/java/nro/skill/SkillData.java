package nro.skill;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import nro.main.FileIO;

public class SkillData {

    public static NClass[] nClasss;

    private static SkillOptionTemplate[] sOptionTemplates;

    public static void createSkill() {
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(FileIO.readFile("data/NRskill_v5"));
            DataInputStream dis = new DataInputStream(is);
            dis.readByte();
            dis.readByte();
            sOptionTemplates = new SkillOptionTemplate[(int) dis.readByte()];
            for (int i = 0; i < sOptionTemplates.length; i++) {
                sOptionTemplates[i] = new SkillOptionTemplate();
                sOptionTemplates[i].id = i;
                sOptionTemplates[i].name = dis.readUTF();
            }
            nClasss = new NClass[dis.readByte()];
            for (int j = 0; j < nClasss.length; j++) {
                nClasss[j] = new NClass();
                nClasss[j].classId = j;
                nClasss[j].name = dis.readUTF();
                nClasss[j].skillTemplates = new SkillTemplate[(int) dis.readByte()];
                for (int k = 0; k < nClasss[j].skillTemplates.length; k++) {
                    nClasss[j].skillTemplates[k] = new SkillTemplate();
                    nClasss[j].skillTemplates[k].id = dis.readByte();
                    nClasss[j].skillTemplates[k].name = dis.readUTF();
                    nClasss[j].skillTemplates[k].maxPoint = (int) dis.readByte();
                    nClasss[j].skillTemplates[k].manaUseType = (int) dis.readByte();
                    nClasss[j].skillTemplates[k].type = (int) dis.readByte();
                    nClasss[j].skillTemplates[k].iconId = (int) dis.readShort();
                    nClasss[j].skillTemplates[k].damInfo = dis.readUTF();
                    /*nClasss[j].skillTemplates[k].description = */dis.readUTF();
                    nClasss[j].skillTemplates[k].skills = new Skill[(int) dis.readByte()];
                    for (int l = 0; l < nClasss[j].skillTemplates[k].skills.length; l++) {
                        if(nClasss[j].skillTemplates[k].id == 9) {
                            if(l == 6) {
                                nClasss[j].skillTemplates[k].skills[0] = new Skill();
                                nClasss[j].skillTemplates[k].skills[0].skillId = dis.readShort();
                                nClasss[j].skillTemplates[k].skills[0].template = nClasss[j].skillTemplates[k];
                                nClasss[j].skillTemplates[k].skills[0].point = (int) dis.readByte();
                                nClasss[j].skillTemplates[k].skills[0].powRequire = dis.readLong();
                                nClasss[j].skillTemplates[k].skills[0].manaUse = (int) dis.readShort();
                                nClasss[j].skillTemplates[k].skills[0].coolDown = dis.readInt();
                                nClasss[j].skillTemplates[k].skills[0].dx = (int) dis.readShort();
                                nClasss[j].skillTemplates[k].skills[0].dy = (int) dis.readShort();
                                nClasss[j].skillTemplates[k].skills[0].maxFight = (int) dis.readByte();
                                nClasss[j].skillTemplates[k].skills[0].damage = dis.readShort();
                                nClasss[j].skillTemplates[k].skills[0].price = dis.readShort();
                                nClasss[j].skillTemplates[k].skills[0].moreInfo = dis.readUTF();
                            } else {
                                nClasss[j].skillTemplates[k].skills[l+1] = new Skill();
                                nClasss[j].skillTemplates[k].skills[l+1].skillId = dis.readShort();
                                nClasss[j].skillTemplates[k].skills[l+1].template = nClasss[j].skillTemplates[k];
                                nClasss[j].skillTemplates[k].skills[l+1].point = (int) dis.readByte();
                                nClasss[j].skillTemplates[k].skills[l+1].powRequire = dis.readLong();
                                nClasss[j].skillTemplates[k].skills[l+1].manaUse = (int) dis.readShort();
                                nClasss[j].skillTemplates[k].skills[l+1].coolDown = dis.readInt();
                                nClasss[j].skillTemplates[k].skills[l+1].dx = (int) dis.readShort();
                                nClasss[j].skillTemplates[k].skills[l+1].dy = (int) dis.readShort();
                                nClasss[j].skillTemplates[k].skills[l+1].maxFight = (int) dis.readByte();
                                nClasss[j].skillTemplates[k].skills[l+1].damage = dis.readShort();
                                nClasss[j].skillTemplates[k].skills[l+1].price = dis.readShort();
                                nClasss[j].skillTemplates[k].skills[l+1].moreInfo = dis.readUTF();
                            }
                        } else {
                            nClasss[j].skillTemplates[k].skills[l] = new Skill();
                            nClasss[j].skillTemplates[k].skills[l].skillId = dis.readShort();
                            nClasss[j].skillTemplates[k].skills[l].template = nClasss[j].skillTemplates[k];
                            nClasss[j].skillTemplates[k].skills[l].point = (int) dis.readByte();
                            nClasss[j].skillTemplates[k].skills[l].powRequire = dis.readLong();
                            nClasss[j].skillTemplates[k].skills[l].manaUse = (int) dis.readShort();
                            nClasss[j].skillTemplates[k].skills[l].coolDown = dis.readInt();
                            nClasss[j].skillTemplates[k].skills[l].dx = (int) dis.readShort();
                            nClasss[j].skillTemplates[k].skills[l].dy = (int) dis.readShort();
                            nClasss[j].skillTemplates[k].skills[l].maxFight = (int) dis.readByte();
                            nClasss[j].skillTemplates[k].skills[l].damage = dis.readShort();
                            nClasss[j].skillTemplates[k].skills[l].price = dis.readShort();
                            nClasss[j].skillTemplates[k].skills[l].moreInfo = dis.readUTF();
                        }
                        //Skills.add(nClasss[j].skillTemplates[k].skills[l]);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Util.log("finish createSkill");
    }

    public short getSkillIDBySkillTemplateID(byte gender, short tempid, int point) {
        for(SkillTemplate tempSkill: nClasss[gender].skillTemplates) {
            if(tempSkill.id == (byte)tempid) {
                for(Skill skill: tempSkill.skills) {
                    if(skill.point == (byte)point) {
                        return skill.skillId;
                    }
                }
            }
        }
        return -1;
    }

    public Skill getSkillBySkillTemplate(byte gender, short tempid, int point) {
        for(SkillTemplate tempSkill: nClasss[gender].skillTemplates) {
            if(tempSkill.id == (byte)tempid) {
                for(Skill skill: tempSkill.skills) {
                    if(skill.point == (byte)point) {
                        return skill;
                    }
                }
            }
        }
        return null;
    }
}
