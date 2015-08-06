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
package dwo.gameserver.instancemanager.clanhall;

import dwo.config.Config;
import dwo.gameserver.engine.databaseengine.FiltredPreparedStatement;
import dwo.gameserver.engine.databaseengine.L2DatabaseFactory;
import dwo.gameserver.engine.databaseengine.ThreadConnection;
import dwo.gameserver.model.actor.L2Character;
import dwo.gameserver.model.actor.instance.L2PcInstance;
import dwo.gameserver.model.player.formation.clan.L2Clan;
import dwo.gameserver.model.skills.stats.StatsSet;
import dwo.gameserver.model.world.residence.clanhall.ClanHallSiegeEngine;
import dwo.gameserver.model.world.residence.clanhall.type.ClanHallSiegable;
import dwo.gameserver.model.world.zone.type.L2ClanHallZone;
import dwo.gameserver.network.game.components.SystemMessageId;
import dwo.gameserver.network.game.serverpackets.SystemMessage;
import dwo.gameserver.util.database.DatabaseUtils;
import javolution.util.FastMap;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.Map;

/**
 * @author BiggBoss
 */
public class ClanHallSiegeManager
{
	private static final Logger _log = LogManager.getLogger(ClanHallSiegeManager.class);
	private static final String SQL_LOAD_HALLS = "SELECT * FROM siegable_clanhall";
	private FastMap<Integer, ClanHallSiegable> _siegableHalls = new FastMap<>();

	private ClanHallSiegeManager()
	{
		_log.log(Level.INFO, "ClanHallSiegeManager: Initializing.");
		loadClanHalls();
	}

	public static ClanHallSiegeManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}

	private void loadClanHalls()
	{
		ThreadConnection con = null;
		FiltredPreparedStatement statement = null;
		ResultSet rs = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SQL_LOAD_HALLS);
			rs = statement.executeQuery();

			_siegableHalls.clear();

			while(rs.next())
			{
				int id = rs.getInt("clanHallId");

				StatsSet set = new StatsSet();

				set.set("id", id);
				set.set("name", rs.getString("name"));
				set.set("ownerId", rs.getInt("ownerId"));
				set.set("desc", rs.getString("desc"));
				set.set("location", rs.getString("location"));
				set.set("nextSiege", rs.getLong("nextSiege"));
				set.set("siegeLenght", rs.getLong("siegeLenght"));
				set.set("scheduleConfig", rs.getString("schedule_config"));
				ClanHallSiegable hall = new ClanHallSiegable(set);
				_siegableHalls.put(id, hall);
				ClanHallManager.addClanHall(hall);
			}
			_log.log(Level.INFO, "ClanHallSiegeManager: Loaded " + _siegableHalls.size() + " conquerable clan halls");

		}
		catch(Exception e)
		{
			_log.log(Level.ERROR, "ClanHallSiegeManager: Could not load siegable clan halls!", e);
		}
		finally
		{
			DatabaseUtils.closeDatabaseCSR(con, statement, rs);
		}
	}

	public FastMap<Integer, ClanHallSiegable> getConquerableHalls()
	{
		return _siegableHalls;
	}

	public ClanHallSiegable getSiegableHall(int clanHall)
	{
		return _siegableHalls.get(clanHall);
	}

	public ClanHallSiegable getNearbyClanHall(L2Character activeChar)
	{
		return getNearbyClanHall(activeChar.getX(), activeChar.getY(), 10000);
	}

	public ClanHallSiegable getNearbyClanHall(int x, int y, int maxDist)
	{
		L2ClanHallZone zone = null;

		for(Map.Entry<Integer, ClanHallSiegable> ch : _siegableHalls.entrySet())
		{
			zone = ch.getValue().getZone();
			if(zone != null && zone.getDistanceToZone(x, y) < maxDist)
			{
				return ch.getValue();
			}
		}
		return null;
	}

	public ClanHallSiegeEngine getSiege(L2Character character)
	{
		ClanHallSiegable hall = getNearbyClanHall(character);
		if(hall == null)
		{
			return null;
		}
		return hall.getSiege();
	}

	public void registerClan(L2Clan clan, ClanHallSiegable hall, L2PcInstance player)
	{
		if(clan.getLevel() < Config.CHS_CLAN_MINLEVEL)
		{
			player.sendMessage("Only clans of level " + Config.CHS_CLAN_MINLEVEL + " or higher may register for a castle siege");
		}
		else if(hall.isWaitingBattle())
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.DEADLINE_FOR_SIEGE_S1_PASSED).addString(hall.getName()));
		}
		else if(hall.isInSiege())
		{
			player.sendPacket(SystemMessageId.NOT_SIEGE_REGISTRATION_TIME2);
		}
		else if(hall.getOwnerId() == clan.getClanId())
		{
			player.sendPacket(SystemMessageId.CLAN_THAT_OWNS_CASTLE_IS_AUTOMATICALLY_REGISTERED_DEFENDING);
		}
		else if(clan.getCastleId() != 0 || clan.getClanhallId() != 0)
		{
			player.sendPacket(SystemMessageId.CLAN_THAT_OWNS_CASTLE_CANNOT_PARTICIPATE_OTHER_SIEGE);
		}
		else if(hall.getSiege().checkIsAttacker(clan))
		{
			player.sendPacket(SystemMessageId.ALREADY_REQUESTED_SIEGE_BATTLE);
		}
		else if(isClanParticipating(clan))
		{
			player.sendPacket(SystemMessageId.APPLICATION_DENIED_BECAUSE_ALREADY_SUBMITTED_A_REQUEST_FOR_ANOTHER_SIEGE_BATTLE);
		}
		else if(hall.getSiege().getAttackers().size() >= Config.CHS_MAX_ATTACKERS)
		{
			player.sendPacket(SystemMessageId.ATTACKER_SIDE_FULL);
		}
		else
		{
			hall.addAttacker(clan);
		}
	}

	public void unRegisterClan(L2Clan clan, ClanHallSiegable hall)
	{
		if(!hall.isRegistering())
		{
			return;
		}
		hall.removeAttacker(clan);
	}

	public boolean isClanParticipating(L2Clan clan)
	{
		for(ClanHallSiegable hall : _siegableHalls.values())
		{
			if(hall.getSiege() != null && hall.getSiege().checkIsAttacker(clan))
			{
				return true;
			}
		}
		return false;
	}

	public void onServerShutDown()
	{
		for(ClanHallSiegable hall : _siegableHalls.values())
		{
			//Rainbow springs has his own attackers table
			if(hall.getId() == 62 || hall.getSiege() == null)
			{
				continue;
			}

			hall.getSiege().saveAttackers();
		}
	}

	private static class SingletonHolder
	{
		private static final ClanHallSiegeManager INSTANCE = new ClanHallSiegeManager();
	}
}