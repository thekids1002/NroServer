package nro.skill;

public class Skill {
    
	public SkillTemplate template;

	public short skillId;

	public int point;

	public long powRequire;

	public int coolDown;

	public long lastTimeUseThisSkill;

	public int dx;

	public int dy;

	public int maxFight;

	public int manaUse;

	public SkillOption[] options;

	public boolean paintCanNotUseSkill;

	public short damage;

	public String moreInfo;

	public short price;
        
        public byte genderSkill;
        public short tempSkillId;

        public void newSkill(Skill _Skill) {
            this.template = _Skill.template;
            this.skillId = _Skill.skillId;
            this.point = _Skill.point;
            this.powRequire = _Skill.powRequire;
            this.coolDown = _Skill.coolDown;
            this.lastTimeUseThisSkill = 0;
            this.dx = _Skill.dx;
            this.dy = _Skill.dy;
            this.maxFight = _Skill.maxFight;
            this.manaUse = _Skill.manaUse;
            this.options = _Skill.options;
            this.paintCanNotUseSkill = _Skill.paintCanNotUseSkill;
            this.damage = _Skill.damage;
            this.moreInfo = _Skill.moreInfo;
            this.price = _Skill.price;
//            this.genderSkill = ;
            this.tempSkillId = _Skill.template.id;
        }
}
