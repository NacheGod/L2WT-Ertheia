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
package dwo.gameserver.model.world.zone.type;

import dwo.gameserver.GameServerStartup;
import dwo.gameserver.instancemanager.GrandBossManager;
import dwo.gameserver.instancemanager.ZoneManager;
import dwo.gameserver.model.actor.L2Attackable;
import dwo.gameserver.model.actor.L2Character;
import dwo.gameserver.model.actor.L2Npc;
import dwo.gameserver.model.actor.L2Playable;
import dwo.gameserver.model.actor.L2Summon;
import dwo.gameserver.model.actor.instance.L2PcInstance;
import dwo.gameserver.model.player.teleport.TeleportWhereType;
import dwo.gameserver.model.world.zone.AbstractZoneSettings;
import dwo.gameserver.model.world.zone.L2ZoneType;
import dwo.gameserver.util.arrays.L2FastList;
import javolution.util.FastMap;

import java.util.Map;

/**
 * @author DaRkRaGe
 */

public class L2BossZone extends L2ZoneType
{
	private int _timeInvade;

	private int[] _oustLoc = {0, 0, 0};

	public L2BossZone(int id)
	{
		super(id);
		_oustLoc = new int[3];
		AbstractZoneSettings settings = ZoneManager.getSettings(getName());
		if(settings == null)
		{
			settings = new Settings();
		}
		setSettings(settings);
		GrandBossManager.getInstance().addZone(this);
	}

	@Override
	public void setParameter(String name, String value)
	{
		switch(name)
		{
			case "InvadeTime":
				_timeInvade = Integer.parseInt(value);
				break;
			case "oustX":
				_oustLoc[0] = Integer.parseInt(value);
				break;
			case "oustY":
				_oustLoc[1] = Integer.parseInt(value);
				break;
			case "oustZ":
				_oustLoc[2] = Integer.parseInt(value);
				break;
			default:
				super.setParameter(name, value);
				break;
		}
	}

	@Override
	public Settings getSettings()
	{
		return (Settings) super.getSettings();
	}

	/**
	 * Boss zones have special behaviors for player characters.<br>
	 * Players are automatically teleported out when the attempt to enter these zones, except if the time at which they enter the zone is prior to the entry expiration time set for that player.<br>
	 * Entry expiration times are set by any one of the following:<br>
	 * 1) A player logs out while in a zone (Expiration gets set to logoutTime + _timeInvade)<br>
	 * 2) An external source (such as a quest or AI of NPC) set up the player for entry.<br>
	 * There exists one more case in which the player will be allowed to enter.<br>
	 * That is if the server recently rebooted (boot-up time more recent than currentTime - _timeInvade) AND the player was in the zone prior to reboot.
	 */
	@Override
	protected void onEnter(L2Character character)
	{
		if(isEnabled())
		{
			if(character instanceof L2PcInstance)
			{
				L2PcInstance player = (L2PcInstance) character;

				if(player.isGM())
				{
					return;
				}
				// if player has been (previously) cleared by npc/ai for entry and the zone is
				// set to receive players (aka not waiting for boss to respawn)
				if(getSettings().getPlayersAllowed().contains(player.getObjectId()))
				{
					// Get the information about this player's last logout-exit from
					// this zone.
					Long expirationTime = getSettings().getPlayerAllowedReEntryTimes().get(player.getObjectId());

					// with legal entries, do nothing.
					if(expirationTime == null) // legal null expirationTime entries
					{
						long serverStartTime = GameServerStartup.dateTimeServerStarted.getTimeInMillis();
						if(serverStartTime > System.currentTimeMillis() - _timeInvade)
						{
							return;
						}
					}
					else
					{
						// legal non-null logoutTime entries
						getSettings().getPlayerAllowedReEntryTimes().remove(player.getObjectId());
						if(expirationTime > System.currentTimeMillis())
						{
							return;
						}
					}
					getSettings().getPlayersAllowed().remove(getSettings().getPlayersAllowed().indexOf(player.getObjectId()));
				}
				// teleport out all players who attempt "illegal" (re-)entry
				if(_oustLoc[0] != 0 && _oustLoc[1] != 0 && _oustLoc[2] != 0)
				{
					player.teleToLocation(_oustLoc[0], _oustLoc[1], _oustLoc[2]);
				}
				else
				{
					player.teleToLocation(TeleportWhereType.TOWN);
				}
			}
			else if(character instanceof L2Summon)
			{
				L2PcInstance player = ((L2Summon) character).getOwner();
				if(player != null)
				{
					if(getSettings().getPlayersAllowed().contains(player.getObjectId()) || player.isGM())
					{
						return;
					}

					// remove summon and teleport out owner
					// who attempt "illegal" (re-)entry
					if(_oustLoc[0] != 0 && _oustLoc[1] != 0 && _oustLoc[2] != 0)
					{
						player.teleToLocation(_oustLoc[0], _oustLoc[1], _oustLoc[2]);
					}
					else
					{
						player.teleToLocation(TeleportWhereType.TOWN);
					}
				}
				character.getLocationController().decay();
			}
		}
	}

	@Override
	protected void onExit(L2Character character)
	{
		if(isEnabled())
		{
			if(character instanceof L2PcInstance)
			{
				L2PcInstance player = (L2PcInstance) character;
				if(player.isGM())
				{
					return;
				}
				// if the player just got disconnected/logged out, store the dc
				// time so that
				// decisions can be made later about allowing or not the player
				// to log into the zone
				if(!player.isOnline() && getSettings().getPlayersAllowed().contains(player.getObjectId()))
				{
					// mark the time that the player left the zone
					getSettings().getPlayerAllowedReEntryTimes().put(player.getObjectId(), System.currentTimeMillis() + _timeInvade);
				}
				else
				{
					if(getSettings().getPlayersAllowed().contains(player.getObjectId()))
					{
						getSettings().getPlayersAllowed().remove(getSettings().getPlayersAllowed().indexOf(player.getObjectId()));
					}
					getSettings().getPlayerAllowedReEntryTimes().remove(player.getObjectId());
				}
			}
			if(character instanceof L2Playable)
			{
				if(getCharacters() != null && !getCharacters().isEmpty())
				{
					getSettings().getRaidList().clear();
					int count = 0;
					for(L2Character obj : getCharactersInside())
					{
						if(obj == null)
						{
							continue;
						}
						if(obj instanceof L2Playable)
						{
							count++;
						}
						else if(obj instanceof L2Attackable && obj.isRaid())
						{
							getSettings().getRaidList().add(obj);
						}
					}
					// if inside zone isnt any player, force all boss instance return to its spawn points
					if(count == 0 && !getSettings().getRaidList().isEmpty())
					{
						for(L2Character a_raidList : getSettings().getRaidList())
						{
							L2Attackable raid = (L2Attackable) a_raidList;
							if(raid == null || raid.getSpawn() == null || raid.isDead())
							{
								continue;
							}
							if(!raid.isInsideRadius(raid.getSpawn().getLocx(), raid.getSpawn().getLocy(), 150, false))
							{
								raid.returnHome();
							}
						}
					}
				}
			}
		}
		if(character instanceof L2Attackable && character.isRaid() && !character.isDead())
		{
			((L2Attackable) character).returnHome();
		}
	}

	@Override
	public void onDieInside(L2Character character)
	{
	}

	@Override
	public void onReviveInside(L2Character character)
	{
	}

	public void setZoneEnabled(boolean flag)
	{
		if(isEnabled() != flag)
		{
			oustAllPlayers();
		}

		setEnabled(flag);
	}

	public int getTimeInvade()
	{
		return _timeInvade;
	}

	public L2FastList<Integer> getAllowedPlayers()
	{
		return getSettings().getPlayersAllowed();
	}

	public void setAllowedPlayers(L2FastList<Integer> players)
	{
		if(players != null)
		{
			getSettings().getPlayersAllowed().clear();
			getSettings().getPlayersAllowed().addAll(players);
		}
	}

	public boolean isPlayerAllowed(L2PcInstance player)
	{
		if(player.isGM())
		{
			return true;
		}
		else if(getSettings().getPlayersAllowed().contains(player.getObjectId()))
		{
			return true;
		}
		else
		{
			if(_oustLoc[0] != 0 && _oustLoc[1] != 0 && _oustLoc[2] != 0)
			{
				player.teleToLocation(_oustLoc[0], _oustLoc[1], _oustLoc[2]);
			}
			else
			{
				player.teleToLocation(TeleportWhereType.TOWN);
			}
			return false;
		}
	}

	/**
	 * Some GrandBosses send all players in zone to a specific part of the zone,
	 * rather than just removing them all. If this is the case, this command should
	 * be used. If this is no the case, then use oustAllPlayers().
	 *
	 * @param x
	 * @param y
	 * @param z
	 */

	public void movePlayersTo(int x, int y, int z)
	{
		if(_characterList.isEmpty())
		{
			return;
		}

		getCharactersInside().stream().filter(character -> character instanceof L2PcInstance).forEach(character -> {
			L2PcInstance player = (L2PcInstance) character;
			if(player.isOnline())
			{
				player.teleToLocation(x, y, z);
			}
		});
	}

	/**
	 * Occasionally, all players need to be sent out of the zone (for example,
	 * if the players are just running around without fighting for too long, or
	 * if all players die, etc). This call sends all online players to town and
	 * marks offline players to be teleported (by clearing their relog
	 * expiration times) when they log back in (no real need for off-line
	 * teleport).
	 */
	public void oustAllPlayers()
	{
		if(_characterList.isEmpty())
		{
			return;
		}

		getCharactersInside().stream().filter(character -> character instanceof L2PcInstance).forEach(character -> {
			L2PcInstance player = (L2PcInstance) character;
			if(player.isOnline())
			{
				if(_oustLoc[0] != 0 && _oustLoc[1] != 0 && _oustLoc[2] != 0)
				{
					player.teleToLocation(_oustLoc[0], _oustLoc[1], _oustLoc[2]);
				}
				else
				{
					player.teleToLocation(TeleportWhereType.TOWN);
				}
			}
		});
		getSettings().getPlayerAllowedReEntryTimes().clear();
		getSettings().getPlayersAllowed().clear();
	}

	/**
	 * This function is to be used by external sources, such as quests and AI in order to allow a player for entry into the zone for some time. Naturally if the player does not enter within the allowed time, he/she will be teleported out again...
	 * @param player reference to the player we wish to allow
	 * @param durationInSec amount of time in seconds during which entry is valid.
	 */
	public void allowPlayerEntry(L2PcInstance player, int durationInSec)
	{
		if(!player.isGM())
		{
			if(!getSettings().getPlayersAllowed().contains(player.getObjectId()))
			{
				getSettings().getPlayersAllowed().add(player.getObjectId());
			}
			getSettings().getPlayerAllowedReEntryTimes().put(player.getObjectId(), System.currentTimeMillis() + durationInSec * 1000);
		}
	}

	public void removePlayer(L2PcInstance player)
	{
		if(!player.isGM())
		{
			getSettings().getPlayersAllowed().remove(Integer.valueOf(player.getObjectId()));
			getSettings().getPlayerAllowedReEntryTimes().remove(player.getObjectId());
		}
	}

	public void updateKnownList(L2Npc npc)
	{
		if(_characterList == null || _characterList.isEmpty())
		{
			return;
		}

		Map<Integer, L2PcInstance> npcKnownPlayers = npc.getKnownList().getKnownPlayers();
		for(L2Character character : getCharactersInside())
		{
			if(character == null)
			{
				continue;
			}
			if(character instanceof L2PcInstance)
			{
				L2PcInstance player = (L2PcInstance) character;
				if(player.isOnline())
				{
					npcKnownPlayers.put(player.getObjectId(), player);
				}
			}
		}
	}

	private class Settings extends AbstractZoneSettings
	{
		// track the times that players got disconnected. Players are allowed
		// to log back into the zone as long as their log-out was within _timeInvade time...
		// <player objectId, expiration time in milliseconds>
		private final FastMap<Integer, Long> _playerAllowedReEntryTimes;

		// track the players admitted to the zone who should be allowed back in
		// after reboot/server downtime (outside of their control), within 30 of server restart
		private final L2FastList<Integer> _playersAllowed;

		private final L2FastList<L2Character> _raidList;

		public Settings()
		{
			_playerAllowedReEntryTimes = new FastMap<>();
			_playersAllowed = new L2FastList<>();
			_raidList = new L2FastList<>();
		}

		public FastMap<Integer, Long> getPlayerAllowedReEntryTimes()
		{
			return _playerAllowedReEntryTimes;
		}

		public L2FastList<Integer> getPlayersAllowed()
		{
			return _playersAllowed;
		}

		public L2FastList<L2Character> getRaidList()
		{
			return _raidList;
		}

		@Override
		public void clear()
		{
			_playerAllowedReEntryTimes.clear();
			_playersAllowed.clear();
			_raidList.clear();
		}
	}
}
