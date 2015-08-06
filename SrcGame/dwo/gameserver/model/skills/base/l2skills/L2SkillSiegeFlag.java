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
package dwo.gameserver.model.skills.base.l2skills;

import dwo.config.Config;
import dwo.gameserver.datatables.xml.NpcTable;
import dwo.gameserver.engine.databaseengine.idfactory.IdFactory;
import dwo.gameserver.instancemanager.castle.CastleManager;
import dwo.gameserver.instancemanager.castle.CastleSiegeManager;
import dwo.gameserver.instancemanager.clanhall.ClanHallSiegeManager;
import dwo.gameserver.instancemanager.fort.FortManager;
import dwo.gameserver.instancemanager.fort.FortSiegeManager;
import dwo.gameserver.model.actor.L2Character;
import dwo.gameserver.model.actor.L2Object;
import dwo.gameserver.model.actor.instance.L2PcInstance;
import dwo.gameserver.model.actor.instance.L2SiegeFlagInstance;
import dwo.gameserver.model.skills.base.L2Skill;
import dwo.gameserver.model.skills.stats.StatsSet;
import dwo.gameserver.model.world.residence.castle.Castle;
import dwo.gameserver.model.world.residence.clanhall.type.ClanHallSiegable;
import dwo.gameserver.model.world.residence.fort.Fort;
import dwo.gameserver.network.game.components.SystemMessageId;
import org.apache.log4j.Level;

public class L2SkillSiegeFlag extends L2Skill
{
	private final boolean _isAdvanced;
	private final boolean _isOutpost;

	public L2SkillSiegeFlag(StatsSet set)
	{
		super(set);
		_isAdvanced = set.getBool("isAdvanced", false);
		_isOutpost = set.getBool("isOutpost", false);
	}

	/**
	 * Return true if character clan place a flag<BR><BR>
	 *
	 * @param activeChar The L2Character of the character placing the flag
	 * @param isCheckOnly if false, it will send a notification to the player telling him
	 * why it failed
	 */
	public static boolean checkIfOkToPlaceFlag(L2Character activeChar, boolean isCheckOnly, boolean isOutPost)
	{
		if(isOutPost)
		{
			return false;
		}
		Castle castle = CastleManager.getInstance().getCastle(activeChar);
		Fort fort = FortManager.getInstance().getFort(activeChar);
		ClanHallSiegable hall = ClanHallSiegeManager.getInstance().getNearbyClanHall(activeChar);

		if(castle == null && fort == null && hall == null)
		{
			return false;
		}
		if(castle != null)
		{
			return checkIfOkToPlaceFlag(activeChar, castle, isCheckOnly);
		}
		if(fort != null)
		{
			return checkIfOkToPlaceFlag(activeChar, fort, isCheckOnly);
		}
		return checkIfOkToPlaceFlag(activeChar, hall, isCheckOnly);
	}

	public static boolean checkIfOkToPlaceFlag(L2Character activeChar, ClanHallSiegable hall, boolean isCheckOnly)
	{
		if(!(activeChar instanceof L2PcInstance))
		{
			return false;
		}

		String text = "";
		L2PcInstance player = (L2PcInstance) activeChar;
		int hallId = hall.getId();

		if(hallId <= 0)
		{
			text = "You must be on Siegable clan hall ground to place a flag.";
		}
		else if(!hall.isInSiege())
		{
			text = "You can only place a flag during a siege.";
		}
		else if(player.getClan() == null || !player.isClanLeader())
		{
			text = "You must be a clan leader to place a flag.";
		}
		else if(!hall.isRegistered(player.getClan()))
		{
			text = "You must be an attacker to place a flag.";
		}
		else if(hall.getSiege().getAttackerClan(player.getClan()).getNumFlags() > Config.CHS_MAX_FLAGS_PER_CLAN)
		{
			text = "You have already placed the maximum number of flags possible.";
		}
		else if(!player.isInsideZone(L2Character.ZONE_HQ))
		{
			player.sendPacket(SystemMessageId.NOT_SET_UP_BASE_HERE);
		}
		else if(!hall.getSiege().canPlantFlag())
		{
			text = "You cannot place a flag on this siege.";
		}
		else
		{
			return true;
		}

		if(!isCheckOnly)
		{
			player.sendMessage(text);
		}
		return false;
	}

	/**
	 *
	 * @param activeChar
	 * @param castle
	 * @param isCheckOnly
	 * @return
	 */
	public static boolean checkIfOkToPlaceFlag(L2Character activeChar, Castle castle, boolean isCheckOnly)
	{
		if(!(activeChar instanceof L2PcInstance))
		{
			return false;
		}

		String text = "";
		L2PcInstance player = (L2PcInstance) activeChar;

		if(castle == null || castle.getCastleId() <= 0)
		{
			text = "You must be on castle ground to place a flag.";
		}
		else if(!castle.getSiege().isInProgress())
		{
			text = "You can only place a flag during a siege.";
		}
		else if(castle.getSiege().getAttackerClan(player.getClan()) == null)
		{
			text = "You must be an attacker to place a flag.";
		}
		else if(!player.isClanLeader())
		{
			text = "You must be a clan leader to place a flag.";
		}
		else if(castle.getSiege().getAttackerClan(player.getClan()).getNumFlags() >= CastleSiegeManager.getInstance().getFlagMaxCount())
		{
			text = "You have already placed the maximum number of flags possible.";
		}
		else if(!player.isInsideZone(L2Character.ZONE_HQ))
		{
			player.sendPacket(SystemMessageId.NOT_SET_UP_BASE_HERE);
		}
		else
		{
			return true;
		}

		if(!isCheckOnly)
		{
			player.sendMessage(text);
		}
		return false;
	}

	/**
	 *
	 * @param activeChar
	 * @param fort
	 * @param isCheckOnly
	 * @return
	 */
	public static boolean checkIfOkToPlaceFlag(L2Character activeChar, Fort fort, boolean isCheckOnly)
	{
		if(!(activeChar instanceof L2PcInstance))
		{
			return false;
		}

		String text = "";
		L2PcInstance player = (L2PcInstance) activeChar;

		if(fort == null || fort.getFortId() <= 0)
		{
			text = "You must be on fort ground to place a flag.";
		}
		else if(!fort.getSiege().isInProgress())
		{
			text = "You can only place a flag during a siege.";
		}
		else if(fort.getSiege().getAttackerClan(player.getClan()) == null)
		{
			text = "You must be an attacker to place a flag.";
		}
		else if(!player.isClanLeader())
		{
			text = "You must be a clan leader to place a flag.";
		}
		else if(fort.getSiege().getAttackerClan(player.getClan()).getNumFlags() >= FortSiegeManager.getInstance().getFlagMaxCount())
		{
			text = "You have already placed the maximum number of flags possible.";
		}
		else if(!player.isInsideZone(L2Character.ZONE_HQ))
		{
			player.sendPacket(SystemMessageId.NOT_SET_UP_BASE_HERE);
		}
		else
		{
			return true;
		}

		if(!isCheckOnly)
		{
			player.sendMessage(text);
		}
		return false;
	}

	/**
	 * Return true if character clan place a flag<BR><BR>
	 *
	 * @param activeChar The L2Character of the character placing the flag
	 * @param isCheckOnly if false, it will send a notification to the player telling him
	 * why it failed
	 */
	public static boolean checkIfOkToPlaceHQ(L2Character activeChar, boolean isCheckOnly, boolean isOutPost)
	{
		Castle castle = CastleManager.getInstance().getCastle(activeChar);
		Fort fort = FortManager.getInstance().getFort(activeChar);

		if(castle == null && fort == null)
		{
			return false;
		}

		String text = "";
		L2PcInstance player = (L2PcInstance) activeChar;

		if(fort != null && fort.getFortId() == 0 || castle != null && castle.getCastleId() == 0)
		{
			text = "You must be on fort or castle ground to construct an outpost or flag.";
		}
		else if(fort != null && !fort.getZone().isSiegeActive() || castle != null && !castle.getZone().isSiegeActive())
		{
			text = "You can only construct an outpost or flag on siege field.";
		}
		else if(!player.isClanLeader())
		{
			text = "You must be a clan leader to construct an outpost or flag.";
		}
		else if(!player.isInsideZone(L2Character.ZONE_HQ))
		{
			player.sendPacket(SystemMessageId.NOT_SET_UP_BASE_HERE);
		}
		else
		{
			return true;
		}

		if(!isCheckOnly)
		{
			player.sendMessage(text);
		}
		return false;
	}

	@Override
	public void useSkill(L2Character activeChar, L2Object[] targets)
	{
		if(!(activeChar instanceof L2PcInstance))
		{
			return;
		}

		L2PcInstance player = (L2PcInstance) activeChar;

		if(player.getClan() == null || player.getClan().getLeaderId() != player.getObjectId())
		{
			return;
		}

		if(!checkIfOkToPlaceFlag(player, true, _isOutpost))
		{
			return;
		}

		// Fortress/Castle/ClanHall siege
		try
		{
			// Spawn a new flag
			L2SiegeFlagInstance flag = new L2SiegeFlagInstance(player, IdFactory.getInstance().getNextId(), NpcTable.getInstance().getTemplate(35062), _isAdvanced, false);
			flag.setTitle(player.getClan().getName());
			flag.setCurrentHpMp(flag.getMaxHp(), flag.getMaxMp());
			flag.setHeading(player.getHeading());
			flag.getLocationController().spawn(player.getX(), player.getY(), player.getZ() + 50);
			Castle castle = CastleManager.getInstance().getCastle(activeChar);
			Fort fort = FortManager.getInstance().getFort(activeChar);
			ClanHallSiegable hall = ClanHallSiegeManager.getInstance().getNearbyClanHall(activeChar);
			if(castle != null)
			{
				castle.getSiege().getFlag(player.getClan()).add(flag);
			}
			else if(fort != null)
			{
				fort.getSiege().getFlag(player.getClan()).add(flag);
			}
			else
			{
				hall.getSiege().getFlag(player.getClan()).add(flag);
			}

		}
		catch(Exception e)
		{
			player.sendMessage("Error placing flag:" + e);
			_log.log(Level.WARN, "Error placing flag: " + e.getMessage(), e);
		}
	}
}
