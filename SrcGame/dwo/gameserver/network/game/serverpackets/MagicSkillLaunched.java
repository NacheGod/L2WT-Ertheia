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
package dwo.gameserver.network.game.serverpackets;

import dwo.gameserver.model.actor.L2Character;
import dwo.gameserver.model.actor.L2Object;

public class MagicSkillLaunched extends L2GameServerPacket
{
	private int _charObjId;
	private int _skillId;
	private int _skillLevel;
	private int _numberOfTargets;
	private L2Object[] _targets;
	private int _singleTargetId;

	public MagicSkillLaunched(L2Character cha, int skillId, int skillLevel, L2Object[] targets)
	{
		_charObjId = cha.getObjectId();
		_skillId = skillId;
		_skillLevel = skillLevel;

		if(targets != null)
		{
			_numberOfTargets = targets.length;
			_targets = targets;
		}
		else
		{
			_numberOfTargets = 1;
			_targets = new L2Object[]{cha};
		}
		_singleTargetId = 0;
	}

	public MagicSkillLaunched(L2Character cha, int skillId, int skillLevel)
	{
		_charObjId = cha.getObjectId();
		_skillId = skillId;
		_skillLevel = skillLevel;
		_numberOfTargets = 1;
		_singleTargetId = cha.getTargetId();
	}

	@Override
	protected void writeImpl()
	{
		writeD(0x00);
		writeD(_charObjId);
		writeD(_skillId);
		writeD(_skillLevel);
		writeD(_numberOfTargets); // also failed or not?
		if(_singleTargetId != 0 || _numberOfTargets == 0)
		{
			writeD(_singleTargetId);
		}
		else
		{
			for(L2Object target : _targets)
			{
				writeD(target.getObjectId());
			}
		}
	}
}
