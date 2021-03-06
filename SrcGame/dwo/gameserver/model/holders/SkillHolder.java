/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package dwo.gameserver.model.holders;

import dwo.gameserver.model.skills.SkillTable;
import dwo.gameserver.model.skills.base.L2Skill;

/**
 *
 * @author BiggBoss
 * Simple class for storing skill id/level
 *
 */
public class SkillHolder
{
	private final int _skillId;
	private final int _skillLvl;

	public SkillHolder(int skillId, int skillLvl)
	{
		_skillId = skillId;
		_skillLvl = skillLvl;
	}

	public SkillHolder(L2Skill skill)
	{
		_skillId = skill.getId();
		_skillLvl = skill.getLevel();
	}

	public int getSkillId()
	{
		return _skillId;
	}

	public int getSkillLvl()
	{
		return _skillLvl;
	}

	public L2Skill getSkill()
	{
		return SkillTable.getInstance().getInfo(_skillId, _skillLvl);
	}
}