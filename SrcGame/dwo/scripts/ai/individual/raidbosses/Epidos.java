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
package dwo.scripts.ai.individual.raidbosses;

import dwo.gameserver.model.actor.L2Npc;
import dwo.gameserver.model.actor.ai.CtrlIntention;
import dwo.gameserver.model.actor.instance.L2MonsterInstance;
import dwo.gameserver.model.actor.instance.L2PcInstance;
import dwo.gameserver.model.world.quest.Quest;
import dwo.gameserver.util.MinionList;
import dwo.gameserver.util.Rnd;
import gnu.trove.map.hash.TIntDoubleHashMap;

import java.util.Arrays;

/**
 * Manages minion's spawn, idle despawn and Teleportation Cube spawn.
 * @author GKR
 */
public class Epidos extends Quest
{
	private static final int[] EPIDOSES = {
		25609, 25610, 25611, 25612
	};
	private static final int[] MINIONS = {
		25605, 25606, 25607, 25608
	};
	private static final int[] MINIONS_COUNT = {
		3, 6, 11
	};

	private final TIntDoubleHashMap _lastHp = new TIntDoubleHashMap();

	public Epidos()
	{
		addKillId(EPIDOSES);
		addSpawnId(EPIDOSES);
	}

	public static void main(String[] args)
	{
		new Epidos();
	}

	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if(event.equalsIgnoreCase("check_minions"))
		{
			if(Rnd.get(1000) > 250 && _lastHp.containsKey(npc.getObjectId()))
			{
				int hpDecreasePercent = (int) ((_lastHp.get(npc.getObjectId()) - npc.getCurrentHp()) * 100 / npc.getMaxHp());
				int minionsCount = 0;
				int spawnedMinions = ((L2MonsterInstance) npc).getMinionList().countSpawnedMinions();

				if(hpDecreasePercent > 5 && hpDecreasePercent <= 15 && spawnedMinions <= 9)
				{
					minionsCount = MINIONS_COUNT[0];
				}
				else if((hpDecreasePercent > 1 && hpDecreasePercent <= 5 || hpDecreasePercent > 15 && hpDecreasePercent <= 30) && spawnedMinions <= 6)
				{
					minionsCount = MINIONS_COUNT[1];
				}
				else if(spawnedMinions == 0)
				{
					minionsCount = MINIONS_COUNT[2];
				}

				for(int i = 0; i < minionsCount; i++)
				{
					MinionList.spawnMinion((L2MonsterInstance) npc, MINIONS[Arrays.binarySearch(EPIDOSES, npc.getNpcId())]);
				}

				_lastHp.put(npc.getObjectId(), npc.getCurrentHp());
			}

			startQuestTimer("check_minions", 10000, npc, null);
		}
		else if(event.equalsIgnoreCase("check_idle"))
		{
			if(npc.getAI().getIntention() == CtrlIntention.AI_INTENTION_ACTIVE)
			{
				npc.getLocationController().delete();
			}
			else
			{
				startQuestTimer("check_idle", 600000, npc, null);
			}
		}
		return null;
	}

	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet)
	{
		if(npc.isInsideRadius(-45474, 247450, -13994, 2000, true, false))
		{
			addSpawn(32376, -45482, 246277, -14184, 0, false, 0, false);
		}

		_lastHp.remove(npc.getObjectId());
		return super.onKill(npc, killer, isPet);
	}

	@Override
	public String onSpawn(L2Npc npc)
	{
		startQuestTimer("check_minions", 10000, npc, null);
		startQuestTimer("check_idle", 600000, npc, null);
		_lastHp.put(npc.getObjectId(), npc.getMaxHp());

		return super.onSpawn(npc);
	}
}
