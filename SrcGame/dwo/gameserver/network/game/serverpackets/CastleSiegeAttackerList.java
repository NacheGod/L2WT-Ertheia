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

//import java.util.Calendar; //signed time related
//import org.apache.log4j.Logger;

import dwo.gameserver.datatables.sql.ClanTable;
import dwo.gameserver.model.player.formation.clan.L2Clan;
import dwo.gameserver.model.player.formation.clan.L2SiegeClan;
import dwo.gameserver.model.world.residence.castle.Castle;
import dwo.gameserver.model.world.residence.clanhall.type.ClanHallSiegable;

import java.util.Collection;

/**
 * Populates the CastleSiegeEngine Attacker List in the SiegeInfo Window<BR>
 * <BR>
 * packet type id 0xca<BR>
 * format: cddddddd + dSSdddSSd<BR>
 * <BR>
 * c = ca<BR>
 * d = CastleID<BR>
 * d = unknow (0x00)<BR>
 * d = unknow (0x01)<BR>
 * d = unknow (0x00)<BR>
 * d = Number of Attackers Clans?<BR>
 * d = Number of Attackers Clans<BR>
 * { //repeats<BR>
 * d = ClanID<BR>
 * S = ClanName<BR>
 * S = ClanLeaderName<BR>
 * d = ClanCrestID<BR>
 * d = signed time (seconds)<BR>
 * d = AllyID<BR>
 * S = AllyName<BR>
 * S = AllyLeaderName<BR>
 * d = AllyCrestID<BR>
 *
 * @author KenM
 */
public class CastleSiegeAttackerList extends L2GameServerPacket
{
	private Castle _castle;
	private ClanHallSiegable _hall;

	public CastleSiegeAttackerList(Castle castle)
	{
		_castle = castle;
	}

	public CastleSiegeAttackerList(ClanHallSiegable hall)
	{
		_hall = hall;
	}

	@Override
	protected void writeImpl()
	{
		if(_castle != null)
		{
			writeD(_castle.getCastleId());
			writeD(0x00); //0
			writeD(0x01); //1
			writeD(0x00); //0
			int size = _castle.getSiege().getAttackerClans().size();
			if(size > 0)
			{
				L2Clan clan;

				writeD(size);
				writeD(size);
				for(L2SiegeClan siegeclan : _castle.getSiege().getAttackerClans())
				{
					clan = ClanTable.getInstance().getClan(siegeclan.getClanId());
					if(clan == null)
					{
						continue;
					}

					writeD(clan.getClanId());
					writeS(clan.getName());
					writeS(clan.getLeaderName());
					writeD(clan.getCrestId());
					writeD(0x00); //signed time (seconds) (not storated by L2J)
					writeD(clan.getAllyId());
					writeS(clan.getAllyName());
					writeS(""); //AllyLeaderName
					writeD(clan.getAllyCrestId());
				}
			}
			else
			{
				writeD(0x00);
				writeD(0x00);
			}
		}
		else
		{
			writeD(_hall.getId());
			writeD(0x00); //0
			writeD(0x01); //1
			writeD(0x00); //0
			Collection<L2SiegeClan> attackers = _hall.getSiege().getAttackerClans();
			int size = attackers.size();
			if(size > 0)
			{
				writeD(size);
				writeD(size);
				for(L2SiegeClan sClan : attackers)
				{
					L2Clan clan = ClanTable.getInstance().getClan(sClan.getClanId());
					if(clan == null)
					{
						continue;
					}

					writeD(clan.getClanId());
					writeS(clan.getName());
					writeS(clan.getLeaderName());
					writeD(clan.getCrestId());
					writeD(0x00); //signed time (seconds) (not storated by L2J)
					writeD(clan.getAllyId());
					writeS(clan.getAllyName());
					writeS(""); //AllyLeaderName
					writeD(clan.getAllyCrestId());
				}
			}
			else
			{
				writeD(0x00);
				writeD(0x00);
			}
		}
	}
}
