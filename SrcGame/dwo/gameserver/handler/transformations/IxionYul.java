package dwo.gameserver.handler.transformations;

import dwo.gameserver.model.skills.L2Transformation;
import dwo.gameserver.model.skills.SkillTable;

/**
 * L2GOD Team
 * User: ANZO
 * Date: 06.03.12
 * Time: 6:32
 */

public class IxionYul extends L2Transformation
{
	private static final int[] SKILLS = {619};

	public IxionYul()
	{
		// id, colRadius, colHeight
		super(512, 15, 48.15);
	}

	@Override
	public void transformedSkills()
	{
		// Transform Dispel
		getPlayer().addSkill(SkillTable.getInstance().getInfo(619, 1), false);

		getPlayer().setTransformAllowedSkills(SKILLS);
	}

	@Override
	public void removeSkills()
	{
		// Transform Dispel
		getPlayer().removeSkill(SkillTable.getInstance().getInfo(619, 1), false);

		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
}
