package dwo.gameserver.handler.transformations;

import dwo.gameserver.model.skills.L2Transformation;
import dwo.gameserver.model.skills.SkillTable;

public class LilimKnightNormal extends L2Transformation
{
	private static final int[] Skills = {619, 5491, 568, 569, 570, 571};

	public LilimKnightNormal()
	{
		// id, colRadius, colHeight
		super(208, 12, 25.5);
	}

	@Override
	public void transformedSkills()
	{
		// Attack Buster
		getPlayer().addSkill(SkillTable.getInstance().getInfo(568, 3), false);
		// Attack Storm
		getPlayer().addSkill(SkillTable.getInstance().getInfo(569, 3), false);
		// Attack Rage
		getPlayer().addSkill(SkillTable.getInstance().getInfo(570, 3), false);
		// Poison Dust
		getPlayer().addSkill(SkillTable.getInstance().getInfo(571, 3), false);
		// Transfrom Dispel
		getPlayer().addSkill(SkillTable.getInstance().getInfo(619, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(SkillTable.getInstance().getInfo(5491, 1), false);
		getPlayer().setTransformAllowedSkills(Skills);
	}

	@Override
	public void removeSkills()
	{
		// Attack Buster
		getPlayer().removeSkill(SkillTable.getInstance().getInfo(568, 3), false);
		// Attack Storm
		getPlayer().removeSkill(SkillTable.getInstance().getInfo(569, 3), false);
		// Attack Rage
		getPlayer().removeSkill(SkillTable.getInstance().getInfo(570, 3), false);
		// Poison Dust
		getPlayer().removeSkill(SkillTable.getInstance().getInfo(571, 3), false);
		// Transfrom Dispel
		getPlayer().removeSkill(SkillTable.getInstance().getInfo(619, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(SkillTable.getInstance().getInfo(5491, 1), false);
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
}
