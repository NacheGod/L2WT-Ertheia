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
package dwo.gameserver.network.game.serverpackets.packet.party;

import dwo.gameserver.model.actor.instance.L2PcInstance;
import dwo.gameserver.network.game.serverpackets.L2GameServerPacket;

public class PartySmallWindowUpdate extends L2GameServerPacket
{
	private L2PcInstance _member;

	public PartySmallWindowUpdate(L2PcInstance member)
	{
		_member = member;
	}

	@Override
	protected void writeImpl()
	{
		writeD(_member.getObjectId());
		writeS(_member.getName());
		writeD((int) _member.getCurrentCp()); //c4
		writeD(_member.getMaxCp()); //c4
		writeD((int) _member.getCurrentHp());
		writeD(_member.getMaxVisibleHp());
		writeD((int) _member.getCurrentMp());
		writeD(_member.getMaxMp());
		writeC(_member.getLevel());
		writeH(_member.getClassId().getId());
		writeD(_member.getVitalityDataForCurrentClassIndex().getVitalityPoints());     //GOD Vitality
	}
}