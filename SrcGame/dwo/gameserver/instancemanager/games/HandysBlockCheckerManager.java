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
package dwo.gameserver.instancemanager.games;

import dwo.config.events.ConfigEvents;
import dwo.gameserver.ThreadPoolManager;
import dwo.gameserver.instancemanager.events.EventManager;
import dwo.gameserver.model.actor.L2Character;
import dwo.gameserver.model.actor.instance.L2PcInstance;
import dwo.gameserver.model.items.base.proptypes.ProcessType;
import dwo.gameserver.model.items.itemcontainer.PcInventory;
import dwo.gameserver.model.world.olympiad.OlympiadManager;
import dwo.gameserver.network.game.components.SystemMessageId;
import dwo.gameserver.network.game.serverpackets.L2GameServerPacket;
import dwo.gameserver.network.game.serverpackets.SystemMessage;
import dwo.gameserver.network.game.serverpackets.packet.ex.ExBlockUpSetList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author BiggBoss
 */
public class HandysBlockCheckerManager
{
    /*
      * This class manage the player add/remove, team change and
      * event arena status, as the clearance of the participants
      * list or liberate the arena
      */

	// All the participants and their team classified by arena
	private static final ArenaParticipantsHolder[] _arenaPlayers = new ArenaParticipantsHolder[4];

	// Arena votes to start the game
	private static final Map<Integer, Integer> _arenaVotes = new HashMap<>();

	// Arena Status, True = is being used, otherwise, False
	private static final Map<Integer, Boolean> _arenaStatus = new HashMap<>();

	// Registration request penalty (10 seconds)
	private static List<Integer> _registrationPenalty = new ArrayList<>();

	private HandysBlockCheckerManager()
	{
		// Initialize arena status
		_arenaStatus.put(0, false);
		_arenaStatus.put(1, false);
		_arenaStatus.put(2, false);
		_arenaStatus.put(3, false);
	}

	public static HandysBlockCheckerManager getInstance()
	{
		return SingletonHolder._instance;
	}

	/**
	 * Return the number of event-start votes for the spcified
	 * arena id
	 *
	 * @param arenaId
	 * @return int (number of votes)
	 */
	public int getArenaVotes(int arenaId)
	{
		synchronized(this)
		{
			return _arenaVotes.get(arenaId);
		}
	}

	/**
	 * Add a new vote to start the event for the specified
	 * arena id
	 *
	 * @param arena
	 */
	public void increaseArenaVotes(int arena)
	{
		synchronized(this)
		{
			int newVotes = _arenaVotes.get(arena) + 1;
			ArenaParticipantsHolder holder = _arenaPlayers[arena];

			if(newVotes > holder.getAllPlayers().size() / 2 && !holder.getEvent().isStarted())
			{
				clearArenaVotes(arena);
				if(holder.getBlueTeamSize() == 0 || holder.getRedTeamSize() == 0)
				{
					return;
				}
				if(ConfigEvents.HBCE_FAIR_PLAY)
				{
					holder.checkAndShuffle();
				}
				ThreadPoolManager.getInstance().executeTask(holder.getEvent().new StartEvent());
			}
			else
			{
				_arenaVotes.put(arena, newVotes);
			}
		}
	}

	/**
	 * Will clear the votes queue (of event start) for the
	 * specified arena id
	 *
	 * @param arena
	 */
	public void clearArenaVotes(int arena)
	{
		synchronized(this)
		{
			_arenaVotes.put(arena, 0);
		}
	}

	/**
	 * Returns the players holder
	 *
	 * @param arena
	 * @return ArenaParticipantsHolder
	 */
	public ArenaParticipantsHolder getHolder(int arena)
	{
		return _arenaPlayers[arena];
	}

	/**
	 * Initializes the participants holder
	 */
	public void startUpParticipantsQueue()
	{
		for(int i = 0; i < 4; ++i)
		{
			_arenaPlayers[i] = new ArenaParticipantsHolder(i);
		}
	}

	/**
	 * Add the player to the specified arena (throught the specified
	 * arena manager) and send the needed server ->  client packets
	 *
	 * @param player
	 * @param arenaId
	 */
	public boolean addPlayerToArena(L2PcInstance player, int arenaId)
	{
		ArenaParticipantsHolder holder = _arenaPlayers[arenaId];

		synchronized(holder)
		{
			boolean isRed;

			for(int i = 0; i < 4; i++)
			{
				if(_arenaPlayers[i].getAllPlayers().contains(player))
				{
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.C1_IS_ALREADY_REGISTERED_ON_THE_MATCH_WAITING_LIST).addCharName(player));
					return false;
				}
			}

			if(player.isCursedWeaponEquipped())
			{
				player.sendPacket(SystemMessageId.CANNOT_REGISTER_PROCESSING_CURSED_WEAPON);
				return false;
			}

			if(EventManager.isPlayerParticipant(player) || player.getOlympiadController().isParticipating())
			{
				player.sendMessage("Couldnt register you due other event participation");
				return false;
			}

			if(player.getOlympiadController().isParticipating())
			{
				player.sendMessage("Couldnt register you due other event participation");
				return false;
			}

			if(OlympiadManager.getInstance().isRegistered(player))
			{
				OlympiadManager.getInstance().unRegisterNoble(player);
				player.sendPacket(SystemMessageId.COLISEUM_OLYMPIAD_KRATEIS_APPLICANTS_CANNOT_PARTICIPATE);
			}
		    /*
               if(UnderGroundColiseum.getInstance().isRegisteredPlayer(player))
               {
                   UngerGroundColiseum.getInstance().removeParticipant(player);
                   player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.COLISEUM_OLYMPIAD_KRATEIS_APPLICANTS_CANNOT_PARTICIPATE));
               }
               if(KrateiCubeManager.getInstance().isRegisteredPlayer(player))
               {
                   KrateiCubeManager.getInstance().removeParticipant(player);
                   player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.COLISEUM_OLYMPIAD_KRATEIS_APPLICANTS_CANNOT_PARTICIPATE));
               }
               */

			if(_registrationPenalty.contains(player.getObjectId()))
			{
				player.sendPacket(SystemMessageId.CANNOT_REQUEST_REGISTRATION_10_SECS_AFTER);
				return false;
			}

			if(holder.getBlueTeamSize() < holder.getRedTeamSize())
			{
				holder.addPlayer(player, 1);
				isRed = false;
			}
			else
			{
				holder.addPlayer(player, 0);
				isRed = true;
			}
			holder.broadCastPacketToTeam(new ExBlockUpSetList(player, isRed, false));
			return true;
		}
	}

	/**
	 * Will remove the specified player from the specified
	 * team and arena and will send the needed packet to all
	 * his team mates / enemy team mates
	 *
	 * @param player
	 * @param arenaId
	 */
	public void removePlayer(L2PcInstance player, int arenaId, int team)
	{
		ArenaParticipantsHolder holder = _arenaPlayers[arenaId];
		synchronized(holder)
		{
			boolean isRed = team == 0;

			holder.removePlayer(player, team);
			holder.broadCastPacketToTeam(new ExBlockUpSetList(player, isRed, true));

			// End event if theres an empty team
			int teamSize = isRed ? holder.getRedTeamSize() : holder.getBlueTeamSize();
			if(teamSize == 0)
			{
				holder.getEvent().endEventAbnormally();
			}

			Integer objId = player.getObjectId();
			if(!_registrationPenalty.contains(objId))
			{
				_registrationPenalty.add(objId);
			}
			schedulePenaltyRemoval(objId);
		}
	}

	/**
	 * Will change the player from one team to other (if possible)
	 * and will send the needed packets
	 *
	 * @param player
	 * @param arena
	 * @param team
	 */
	public void changePlayerToTeam(L2PcInstance player, int arena, int team)
	{
		ArenaParticipantsHolder holder = _arenaPlayers[arena];

		synchronized(holder)
		{
			boolean isFromRed = holder._redPlayers.contains(player);

			if(!isFromRed && holder.getRedTeamSize() == 6)
			{
				player.sendMessage("The team is full");
				return;
			}

			int futureTeam = isFromRed ? 1 : 0;
			holder.addPlayer(player, futureTeam);

			if(isFromRed)
			{
				holder.removePlayer(player, 0);
			}
			else
			{
				holder.removePlayer(player, 1);
			}
			holder.broadCastPacketToTeam(new ExBlockUpSetList(player, isFromRed));
		}
	}

	/**
	 * Will erase all participants from the specified holder
	 *
	 * @param arenaId
	 */
	public void clearPaticipantQueueByArenaId(int arenaId)
	{
		synchronized(this)
		{
			_arenaPlayers[arenaId].clearPlayers();
		}
	}

	/**
	 * Returns true if arena is holding an event at this momment
	 *
	 * @param arenaId
	 * @return boolean
	 */
	public boolean arenaIsBeingUsed(int arenaId)
	{
		if(arenaId < 0 || arenaId > 3)
		{
			return false;
		}
		return _arenaStatus.get(arenaId);
	}

	/**
	 * Set the specified arena as being used
	 *
	 * @param arenaId
	 */
	public void setArenaBeingUsed(int arenaId)
	{
		_arenaStatus.put(arenaId, true);
	}

	/**
	 * Set as free the specified arena for future
	 * events
	 *
	 * @param arenaId
	 */
	public void setArenaFree(int arenaId)
	{
		_arenaStatus.put(arenaId, false);
	}

	/**
	 * Called when played logs out while participating
	 * in Block Checker Event
	 *
	 * @param player L2PcInstance
	 */
	public void onDisconnect(L2PcInstance player)
	{
		int arena = player.getEventController().getHandysBlockCheckerEventArena();
		int team = getHolder(arena).getPlayerTeam(player);
		getInstance().removePlayer(player, arena, team);
		if(player.getTeam() > 0)
		{
			player.stopAllEffects();
			// Remove team aura
			player.setTeam(0);

			// Remove the event items
			PcInventory inv = player.getInventory();

			if(inv.getItemByItemId(13787) != null)
			{
				long count = inv.getInventoryItemCount(13787, 0);
				inv.destroyItemByItemId(ProcessType.EVENT, 13787, count, player, player);
			}
			if(inv.getItemByItemId(13788) != null)
			{
				long count = inv.getInventoryItemCount(13788, 0);
				inv.destroyItemByItemId(ProcessType.EVENT, 13788, count, player, player);
			}
			player.setInsideZone(L2Character.ZONE_PVP, false);
			// Teleport Back
			player.teleToLocation(-57478, -60367, -2370);
		}
	}

	private void schedulePenaltyRemoval(int objId)
	{
		ThreadPoolManager.getInstance().scheduleGeneral(new PenaltyRemove(objId), 10000);
	}

	private static class SingletonHolder
	{
		private static HandysBlockCheckerManager _instance = new HandysBlockCheckerManager();
	}

	public class ArenaParticipantsHolder
	{
		int _arena;
		List<L2PcInstance> _redPlayers;
		List<L2PcInstance> _bluePlayers;
		HandysBlockCheckerEngine _engine;

		public ArenaParticipantsHolder(int arena)
		{
			_arena = arena;
			_redPlayers = new ArrayList<>(6);
			_bluePlayers = new ArrayList<>(6);
			_engine = new HandysBlockCheckerEngine(this, _arena);
		}

		public List<L2PcInstance> getRedPlayers()
		{
			return _redPlayers;
		}

		public List<L2PcInstance> getBluePlayers()
		{
			return _bluePlayers;
		}

		public List<L2PcInstance> getAllPlayers()
		{
			List<L2PcInstance> all = new ArrayList<>(12);
			all.addAll(_redPlayers);
			all.addAll(_bluePlayers);
			return all;
		}

		public void addPlayer(L2PcInstance player, int team)
		{
			if(team == 0)
			{
				_redPlayers.add(player);
			}
			else
			{
				_bluePlayers.add(player);
			}
		}

		public void removePlayer(L2PcInstance player, int team)
		{
			if(team == 0)
			{
				_redPlayers.remove(player);
			}
			else
			{
				_bluePlayers.remove(player);
			}
		}

		public int getPlayerTeam(L2PcInstance player)
		{
			if(_redPlayers.contains(player))
			{
				return 0;
			}
			else
			{
				return _bluePlayers.contains(player) ? 1 : -1;
			}
		}

		public int getRedTeamSize()
		{
			return _redPlayers.size();
		}

		public int getBlueTeamSize()
		{
			return _bluePlayers.size();
		}

		public void broadCastPacketToTeam(L2GameServerPacket packet)
		{
			for(L2PcInstance p : _redPlayers)
			{
				p.sendPacket(packet);
			}
			for(L2PcInstance p : _bluePlayers)
			{
				p.sendPacket(packet);
			}
		}

		public void clearPlayers()
		{
			_redPlayers.clear();
			_bluePlayers.clear();
		}

		public HandysBlockCheckerEngine getEvent()
		{
			return _engine;
		}

		public void updateEvent()
		{
			_engine.updatePlayersOnStart(this);
		}

		private void checkAndShuffle()
		{
			int redSize = _redPlayers.size();
			int blueSize = _bluePlayers.size();
			if(redSize > blueSize + 1)
			{
				broadCastPacketToTeam(SystemMessage.getSystemMessage(SystemMessageId.TEAM_ADJUSTED_BECAUSE_WRONG_POPULATION_RATIO));
				int needed = redSize - (blueSize + 1);
				for(int i = 0; i < needed + 1; i++)
				{
					L2PcInstance plr = _redPlayers.get(i);
					if(plr == null)
					{
						continue;
					}
					changePlayerToTeam(plr, _arena, 1);
				}
			}
			else if(blueSize > redSize + 1)
			{
				broadCastPacketToTeam(SystemMessage.getSystemMessage(SystemMessageId.TEAM_ADJUSTED_BECAUSE_WRONG_POPULATION_RATIO));
				int needed = blueSize - (redSize + 1);
				for(int i = 0; i < needed + 1; i++)
				{
					L2PcInstance plr = _bluePlayers.get(i);
					if(plr == null)
					{
						continue;
					}
					changePlayerToTeam(plr, _arena, 0);
				}
			}
		}
	}

	private class PenaltyRemove implements Runnable
	{
		private final Integer objectId;

		public PenaltyRemove(Integer id)
		{
			objectId = id;
		}

		@Override
		public void run()
		{
			_registrationPenalty.remove(objectId);
		}
	}
}