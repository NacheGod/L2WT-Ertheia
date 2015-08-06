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
package dwo.scripts.ai.group_template;

import dwo.gameserver.ThreadPoolManager;
import dwo.gameserver.instancemanager.HellboundManager;
import dwo.gameserver.model.actor.L2Attackable;
import dwo.gameserver.model.actor.L2Npc;
import dwo.gameserver.model.actor.L2Object;
import dwo.gameserver.model.actor.instance.L2PcInstance;
import dwo.gameserver.model.skills.base.L2Skill;
import dwo.gameserver.model.world.quest.Quest;
import dwo.gameserver.util.Rnd;

/**
 * @author DS
 */
public class Chimeras extends Quest
{
	// NPCs
	private static final int[] NPCS = {
		22349, 22350, 22351, 22352
	};
	private static final int CELTUS = 22353;
	// Locations
	private static final int[][] LOCATIONS = {
		{
			3678, 233418, -3319
		}, {
		2038, 237125, -3363
	}, {
		7222, 240617, -2033
	}, {
		9969, 235570, -1993
	}
	};
	// Items
	private static final int BOTTLE = 2359;
	private static final int DIM_LIFE_FORCE = 9680;
	private static final int LIFE_FORCE = 9681;
	private static final int CONTAINED_LIFE_FORCE = 9682;

	public Chimeras()
	{
		addSkillSeeId(NPCS);
		addSpawnId(CELTUS);
		addSkillSeeId(CELTUS);
	}

	public static void main(String[] args)
	{
		new Chimeras();
	}

	@Override
	public String onSkillSee(L2Npc npc, L2PcInstance caster, L2Skill skill, L2Object[] targets, boolean isPet)
	{
		if(skill.getId() == BOTTLE && !npc.isDead())
		{
			if(targets.length > 0 && targets[0].equals(npc))
			{
				if(npc.getCurrentHp() < npc.getMaxHp() * 0.1)
				{
					if(HellboundManager.getInstance().getLevel() == 7)
					{
						HellboundManager.getInstance().updateTrust(3, true);
					}

					npc.setIsDead(true);
					if(npc.getNpcId() == CELTUS)
					{
						((L2Attackable) npc).dropItem(caster, CONTAINED_LIFE_FORCE, 1);
					}
					else
					{
						if(Rnd.getChance(80))
						{
							((L2Attackable) npc).dropItem(caster, DIM_LIFE_FORCE, 1);
						}
						else if(Rnd.getChance(80))
						{
							((L2Attackable) npc).dropItem(caster, LIFE_FORCE, 1);
						}
					}
					npc.getLocationController().decay();
				}
			}
		}
		return super.onSkillSee(npc, caster, skill, targets, isPet);
	}

	@Override
	public String onSpawn(L2Npc npc)
	{
		if(HellboundManager.getInstance().getLevel() == 7 && !npc.isTeleporting()) // Have random spawn points only in 7 lvl
		{
			int[] spawn = LOCATIONS[Rnd.get(LOCATIONS.length)];
			if(!npc.isInsideRadius(spawn[0], spawn[1], spawn[2], 200, false, false))
			{
				npc.getSpawn().setLocx(spawn[0]);
				npc.getSpawn().setLocy(spawn[1]);
				npc.getSpawn().setLocz(spawn[2]);
				ThreadPoolManager.getInstance().scheduleGeneral(new Teleport(npc, spawn), 100);
			}
		}
		return super.onSpawn(npc);
	}

	private static class Teleport implements Runnable
	{
		private final L2Npc _npc;
		private final int[] _coords;

		public Teleport(L2Npc npc, int[] coords)
		{
			_npc = npc;
			_coords = coords;
		}

		@Override
		public void run()
		{
			_npc.teleToLocation(_coords[0], _coords[1], _coords[2]);
		}
	}
}
