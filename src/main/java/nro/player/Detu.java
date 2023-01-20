package nro.player;

import java.util.ArrayList;
import nro.item.Item;
import nro.skill.Skill;
import nro.main.Util;

public class Detu extends Player{
    public boolean isUpPoint = false;
    public long lastTimeUpPoint = 0;
    public boolean isSoSinh = false;
    public short headpet2;
    public short bodypet2;
    public short legpet2;
    public ArrayList<Skill> skill;
    public Detu() {}

    public void initDetuBroly(Detu _detu) {
        int rdGender = Util.nextInt(0, 3);
        int rdHP = Util.nextInt(0, 51);
        int rdHPorKI = Util.nextInt(0, 5); //0 la hp > ki, 1: hp < ki, 2: hp = ki
        _detu.name = "Đệ tử";
        if(rdGender == 0) {
            _detu.head = 285; //282 de ss xd, 285 sstd, 288 ssnm
        } else if(rdGender == 1) {
            _detu.head = 288;
        } else if(rdGender == 2) {
            _detu.head = 282;
        }
        
        _detu.gender = (byte)rdGender;
        _detu.power = 1200;
        if(rdHPorKI == 4) {
            _detu.hpGoc = 2000;
            _detu.mpGoc = 2000;
        } else if(rdHPorKI == 0 || rdHPorKI == 1) {
            _detu.hpGoc = (2000 + rdHP*20);
            _detu.mpGoc = (2000 - rdHP*20);
        } else if(rdHPorKI == 2 || rdHPorKI == 3){
            _detu.hpGoc = (2000 - rdHP*20);
            _detu.mpGoc = (2000 + rdHP*20);
        } else {
            _detu.hpGoc = (2000 - rdHP*20);
            _detu.mpGoc = (2000 + rdHP*20);
        }
        _detu.hp = _detu.hpGoc;
        _detu.mp = _detu.mpGoc;
        _detu.damGoc = Util.nextInt(25, 70);
        _detu.defGoc = (short)Util.nextInt(70, 500);
        _detu.critGoc = (byte)Util.nextInt(1, 7);
        _detu.tiemNang = 0;
        _detu.limitPower = 0;
        _detu.hpFull = _detu.hpGoc;
        _detu.mpFull = _detu.mpGoc;
        _detu.damFull = _detu.damGoc;
        _detu.defFull = _detu.defGoc;
        _detu.critFull = _detu.critGoc;
        _detu.isPet = true;
        _detu.isSoSinh = true;
        _detu.typePk = 0;

        int rdSkill = Util.nextInt(0, 3);
        if(rdSkill == 0) {
            Skill Skillpet1 = new Skill();
            Skillpet1.skillId = (short)(0);
            Skillpet1.point = 1;
            Skillpet1.genderSkill = (byte)0;
            Skillpet1.tempSkillId = (short)0;
            Skillpet1.lastTimeUseThisSkill = 0;
            _detu.listSkill.add(Skillpet1);
        } else if(rdSkill == 1) {
            Skill Skillpet2 = new Skill();
            Skillpet2.skillId = (short)(14);
            Skillpet2.point = 1;
            Skillpet2.genderSkill = (byte)(1);
            Skillpet2.tempSkillId = (short)(2);
            Skillpet2.lastTimeUseThisSkill = 0;
            _detu.listSkill.add(Skillpet2);
        } else {
            Skill Skillpet3 = new Skill();
            Skillpet3.skillId = (short)(28);
            Skillpet3.point = 1;
            Skillpet3.genderSkill = (byte)(2);
            Skillpet3.tempSkillId = (short)(4);
            Skillpet3.lastTimeUseThisSkill = 0;
            _detu.listSkill.add(Skillpet3);
        }
        for(int i = 0; i < 3; i++) {
            Skill Skillpet = new Skill();
            Skillpet.skillId = (short)(-1);
            Skillpet.point = 1;
            Skillpet.genderSkill = (byte)(-1);
            Skillpet.tempSkillId = (short)(-1);
            Skillpet.lastTimeUseThisSkill = 0;
            _detu.listSkill.add(Skillpet);
        }

        _detu.ItemBody = new Item[7];
    }

    public void initDetuMabu(Detu _detu, byte gender) {
        int rdHP = Util.nextInt(0, 51);
        int rdHPorKI = Util.nextInt(0, 5); //0 la hp > ki, 1: hp < ki, 2: hp = ki
        _detu.name = "Mabư";
        _detu.head = 297;
        
        _detu.gender = gender;
        _detu.power = 1500000;
        if(rdHPorKI == 4) {
            _detu.hpGoc = 2000;
            _detu.mpGoc = 2000;
        } else if(rdHPorKI == 0 || rdHPorKI == 1) {
            _detu.hpGoc = (2000 + rdHP*20);
            _detu.mpGoc = (2000 - rdHP*20);
        } else if(rdHPorKI == 2 || rdHPorKI == 3){
            _detu.hpGoc = (2000 - rdHP*20);
            _detu.mpGoc = (2000 + rdHP*20);
        } else {
            _detu.hpGoc = (2000 - rdHP*20);
            _detu.mpGoc = (2000 + rdHP*20);
        }
        _detu.hp = _detu.hpGoc;
        _detu.mp = _detu.mpGoc;
        _detu.damGoc = Util.nextInt(25, 70);
        _detu.defGoc = (short)Util.nextInt(70, 500);
        _detu.critGoc = (byte)Util.nextInt(1, 7);
        _detu.tiemNang = 0;
        _detu.limitPower = 0;
        _detu.hpFull = _detu.hpGoc;
        _detu.mpFull = _detu.mpGoc;
        _detu.damFull = _detu.damGoc;
        _detu.defFull = _detu.defGoc;
        _detu.critFull = _detu.critGoc;
        _detu.isPet = true;
        _detu.isSoSinh = false;
        _detu.typePk = 0;
        _detu.isMabu = true;
        int rdSkill = Util.nextInt(0, 3);
        if(rdSkill == 0) {
            Skill Skillpet1 = new Skill();
            Skillpet1.skillId = (short)(0);
            Skillpet1.point = 1;
            Skillpet1.genderSkill = (byte)0;
            Skillpet1.tempSkillId = (short)0;
            Skillpet1.lastTimeUseThisSkill = 0;
            _detu.listSkill.add(Skillpet1);
        } else if(rdSkill == 1) {
            Skill Skillpet2 = new Skill();
            Skillpet2.skillId = (short)(14);
            Skillpet2.point = 1;
            Skillpet2.genderSkill = (byte)(1);
            Skillpet2.tempSkillId = (short)(2);
            Skillpet2.lastTimeUseThisSkill = 0;
            _detu.listSkill.add(Skillpet2);
        } else {
            Skill Skillpet3 = new Skill();
            Skillpet3.skillId = (short)(28);
            Skillpet3.point = 1;
            Skillpet3.genderSkill = (byte)(2);
            Skillpet3.tempSkillId = (short)(4);
            Skillpet3.lastTimeUseThisSkill = 0;
            _detu.listSkill.add(Skillpet3);
        }
        for(int i = 0; i < 3; i++) {
            Skill Skillpet = new Skill();
            Skillpet.skillId = (short)(-1);
            Skillpet.point = 1;
            Skillpet.genderSkill = (byte)(-1);
            Skillpet.tempSkillId = (short)(-1);
            Skillpet.lastTimeUseThisSkill = 0;
            _detu.listSkill.add(Skillpet);
        }

        _detu.ItemBody = new Item[7];
    }

    public void initPet(short head, short body, short leg) {
        this.name = "";
        this.headpet2 = head;
        this.bodypet2 = body;
        this.legpet2 = leg;
        this.gender = (byte)3;
        this.power = 1200;
        this.hpGoc = 5000000;
        this.mpGoc = 5000000;
        this.hp = 5000000;
        this.mp = 5000000;
        this.damGoc = 1;
        this.defGoc = (short)1;
        this.critGoc = (byte)0;
        this.tiemNang = 0;
        this.limitPower = (byte)0;
        this.hpFull = 5000000;
        this.mpFull = 5000000;
        this.damFull = 0;
        this.defFull = (short)1;
        this.critFull = (byte)0;
        this.stamina = (short)0;
//                        player.detu.maxStamina = (short)1000;
        this.isPet2 = true;
    }
}
