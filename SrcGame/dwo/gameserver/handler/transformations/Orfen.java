package dwo.gameserver.handler.transformations;

import dwo.gameserver.model.skills.L2Transformation;
import dwo.gameserver.model.skills.SkillTable;

/**
 * God World Team
 * User: Yukio
 * Date: 29.12.12
 * Time: 23:50
 */
public class Orfen extends L2Transformation
{
	private static final int[] SKILLS = {9210};

	public Orfen()
	{
		// id, colRadius, colHeight
		super(142, 15, 25);
	}

	@Override
	public void transformedSkills()
	{
		getPlayer().addSkill(SkillTable.getInstance().getInfo(9210, 1), false);
		getPlayer().setTransformAllowedSkills(SKILLS);
	}

	@Override
	public void removeSkills()
	{
		getPlayer().removeSkill(SkillTable.getInstance().getInfo(9210, 1), false);
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
}
