package dwo.gameserver.handler.transformations;

import dwo.gameserver.model.skills.L2Transformation;
import dwo.gameserver.model.skills.SkillTable;

public class DragonBomberWeak extends L2Transformation
{
	private static final int[] Skills = {619, 5491, 580, 581, 582, 583};

	public DragonBomberWeak()
	{
		// id, colRadius, colHeight
		super(218, 16, 24);
	}

	@Override
	public void transformedSkills()
	{
		// Death BlowDamage
		getPlayer().addSkill(SkillTable.getInstance().getInfo(580, 2), false);
		// Sand Cloud
		getPlayer().addSkill(SkillTable.getInstance().getInfo(581, 2), false);
		// Scope Bleed
		getPlayer().addSkill(SkillTable.getInstance().getInfo(582, 2), false);
		// Assimilation
		getPlayer().addSkill(SkillTable.getInstance().getInfo(583, 2), false);
		// Transfrom Dispel
		getPlayer().addSkill(SkillTable.getInstance().getInfo(619, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(SkillTable.getInstance().getInfo(5491, 1), false);
		getPlayer().setTransformAllowedSkills(Skills);
	}

	@Override
	public void removeSkills()
	{
		// Death BlowDamage
		getPlayer().removeSkill(SkillTable.getInstance().getInfo(580, 2), false);
		// Sand Cloud
		getPlayer().removeSkill(SkillTable.getInstance().getInfo(581, 2), false);
		// Scope Bleed
		getPlayer().removeSkill(SkillTable.getInstance().getInfo(582, 2), false);
		// Assimilation
		getPlayer().removeSkill(SkillTable.getInstance().getInfo(583, 2), false, false);
		// Transfrom Dispel
		getPlayer().removeSkill(SkillTable.getInstance().getInfo(619, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(SkillTable.getInstance().getInfo(5491, 1), false);
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
}
