package dwo.gameserver.handler.transformations;

import dwo.gameserver.model.skills.L2Transformation;
import dwo.gameserver.model.skills.SkillTable;

public class Rabbit extends L2Transformation
{
	private static final int[] Skills = {619, 5491, 629, 630};

	public Rabbit()
	{
		// id, colRadius, colHeight
		super(105, 5, 4.5);
	}

	@Override
	public void transformedSkills()
	{
		// Transfrom Dispel
		getPlayer().addSkill(SkillTable.getInstance().getInfo(619, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().addSkill(SkillTable.getInstance().getInfo(5491, 1), false);
		getPlayer().addSkill(SkillTable.getInstance().getInfo(629, 1), false);
		getPlayer().addSkill(SkillTable.getInstance().getInfo(630, 1), false);
		getPlayer().setTransformAllowedSkills(Skills);
	}

	@Override
	public void removeSkills()
	{
		// Transfrom Dispel
		getPlayer().removeSkill(SkillTable.getInstance().getInfo(619, 1), false);
		// Decrease Bow/Crossbow Attack Speed
		getPlayer().removeSkill(SkillTable.getInstance().getInfo(5491, 1), false);
		getPlayer().removeSkill(SkillTable.getInstance().getInfo(629, 1), false);
		getPlayer().removeSkill(SkillTable.getInstance().getInfo(630, 1), false);
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
}
