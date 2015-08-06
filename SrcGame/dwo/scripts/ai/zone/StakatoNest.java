package dwo.scripts.ai.zone;

import dwo.gameserver.model.actor.L2Attackable;
import dwo.gameserver.model.actor.L2Npc;
import dwo.gameserver.model.actor.L2Object;
import dwo.gameserver.model.actor.ai.CtrlIntention;
import dwo.gameserver.model.actor.instance.L2MonsterInstance;
import dwo.gameserver.model.actor.instance.L2PcInstance;
import dwo.gameserver.model.items.base.proptypes.ProcessType;
import dwo.gameserver.model.skills.SkillTable;
import dwo.gameserver.model.skills.base.L2Skill;
import dwo.gameserver.model.world.quest.Quest;
import dwo.gameserver.network.game.serverpackets.MagicSkillUse;
import dwo.gameserver.util.Broadcast;
import dwo.gameserver.util.Rnd;
import dwo.gameserver.util.Util;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

public class StakatoNest extends Quest
{
	// List of all mobs just for register
	private static final int[] _stakato_mobs = {
		18793, 18794, 18795, 18796, 18797, 18798, 22617, 22618, 22619, 22620, 22621, 22622, 22623, 22624, 22625, 22626,
		22627, 22628, 22629, 22630, 22631, 22632, 22633, 25667
	};
	// Coocons
	private static final int[] _cocoons = {18793, 18794, 18795, 18796, 18797, 18798};

	// Cannibalistic Stakato Leader
	private static final int _stakato_leader = 22625;

	// Spike Stakato Nurse
	private static final int _stakato_nurse = 22630;
	// Spike Stakato Nurse (Changed)
	private static final int _stakato_nurse_2 = 22631;
	// Spiked Stakato Baby
	private static final int _stakato_baby = 22632;
	// Spiked Stakato Captain
	private static final int _stakato_captain = 22629;

	// Female Spiked Stakato
	private static final int _stakato_female = 22620;
	// Male Spiked Stakato
	private static final int _stakato_male = 22621;
	// Male Spiked Stakato (Changed)
	private static final int _stakato_male_2 = 22622;
	// Spiked Stakato Guard
	private static final int _stakato_guard = 22619;

	// Cannibalistic Stakato Chief
	private static final int _stakato_chief = 25667;
	// Growth Accelerator
	private static final int _growth_accelerator = 2905;
	// Small Stakato Cocoon
	private static final int _small_cocoon = 14833;
	// Large Stakato Cocoon
	private static final int _large_cocoon = 14834;

	public StakatoNest()
	{
		registerMobs(_stakato_mobs);
	}

	public static void main(String[] args)
	{
		new StakatoNest();
	}

	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		L2MonsterInstance _mob = (L2MonsterInstance) npc;

		if(_mob.getNpcId() == _stakato_leader && Rnd.get(1000) < 100 && _mob.getCurrentHp() < _mob.getMaxHp() * 0.3)
		{
			L2MonsterInstance _follower = checkMinion(npc);

			if(_follower != null)
			{
				double _hp = _follower.getCurrentHp();

				if(_hp > _follower.getMaxHp() * 0.3)
				{
					_mob.abortAttack();
					_mob.abortCast();
					_mob.setHeading(Util.calculateHeadingFrom(_mob, _follower));
					_mob.doCast(SkillTable.getInstance().getInfo(4484, 1));
					_mob.setCurrentHp(_mob.getCurrentHp() + _hp);
					_follower.doDie(_follower);
					_follower.getLocationController().delete();
				}
			}
		}
		return super.onAttack(npc, attacker, damage, isPet);
	}

	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if(npc == null || player == null)
		{
			return null;
		}
		if(npc.isDead())
		{
			return null;
		}

		if(event.equalsIgnoreCase("nurse_change"))
		{
			npc.getSpawn().decreaseCount(npc);
			npc.getLocationController().delete();
			L2Npc _spawned = addSpawn(_stakato_nurse_2, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), false, 0, true);
			attackPlayer(player, _spawned);
		}
		else if(event.equalsIgnoreCase("male_change"))
		{
			npc.getSpawn().decreaseCount(npc);
			npc.getLocationController().delete();
			L2Npc _spawned = addSpawn(_stakato_male_2, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), false, 0, true);
			attackPlayer(player, _spawned);
		}
		return null;
	}

	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet)
	{
		L2MonsterInstance _minion = checkMinion(npc);

		if(npc.getNpcId() == _stakato_nurse && _minion != null)
		{
			Broadcast.toSelfAndKnownPlayers(npc, new MagicSkillUse(npc, 2046, 1, 1000, 0));
			for(int i = 0; i < 3; i++)
			{
				L2Npc _spawned = addSpawn(_stakato_captain, _minion, true);
				attackPlayer(killer, _spawned);
			}
		}
		else if(npc.getNpcId() == _stakato_baby)
		{
			L2MonsterInstance _leader = ((L2MonsterInstance) npc).getLeader();
			if(_leader != null && !_leader.isDead())
			{
				startQuestTimer("nurse_change", 5000, _leader, killer);
			}
		}
		else if(npc.getNpcId() == _stakato_male && _minion != null)
		{
			Broadcast.toSelfAndKnownPlayers(npc, new MagicSkillUse(npc, 2046, 1, 1000, 0));
			for(int i = 0; i < 3; i++)
			{
				L2Npc _spawned = addSpawn(_stakato_guard, _minion, true);
				attackPlayer(killer, _spawned);
			}
		}
		else if(npc.getNpcId() == _stakato_female)
		{
			L2MonsterInstance _leader = ((L2MonsterInstance) npc).getLeader();
			if(_leader != null && !_leader.isDead())
			{
				startQuestTimer("male_change", 5000, _leader, killer);
			}
		}
		else if(npc.getNpcId() == _stakato_chief)
		{
			if(killer.isInParty())
			{
				List<L2PcInstance> party = killer.getParty().getMembers();
				for(L2PcInstance member : party)
				{
					giveCocoon(member, npc);
				}
			}
			else
			{
				giveCocoon(killer, npc);
			}
		}
		return super.onKill(npc, killer, isPet);
	}

	@Override
	public String onSkillSee(L2Npc npc, L2PcInstance caster, L2Skill skill, L2Object[] targets, boolean isPet)
	{
		if(ArrayUtils.contains(_cocoons, npc.getNpcId()) && ArrayUtils.contains(targets, npc) && skill.getId() == _growth_accelerator)
		{
			npc.doDie(caster);
			L2Npc _spawned = addSpawn(_stakato_chief, npc.getX(), npc.getY(), npc.getZ(), Util.calculateHeadingFrom(npc, caster), false, 0, true);
			attackPlayer(caster, _spawned);
		}
		return super.onSkillSee(npc, caster, skill, targets, isPet);
	}

	private L2MonsterInstance checkMinion(L2Npc npc)
	{
		L2MonsterInstance mob = (L2MonsterInstance) npc;
		if(mob.hasMinions())
		{
			List<L2MonsterInstance> _minion = mob.getMinionList().getSpawnedMinions();
			if(_minion != null && !_minion.isEmpty() && _minion.get(0) != null && !_minion.get(0).isDead())
			{
				return _minion.get(0);
			}
		}
		return null;
	}

	private void attackPlayer(L2PcInstance player, L2Npc npc)
	{
		if(npc != null && player != null)
		{
			npc.setIsRunning(true);
			((L2Attackable) npc).addDamageHate(player, 0, 999);
			npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, player);
		}
	}

	private void giveCocoon(L2PcInstance player, L2Npc npc)
	{
		if(Rnd.getChance(20))
		{
			player.addItem(ProcessType.QUEST, _large_cocoon, 1, npc, true);
		}
		else
		{
			player.addItem(ProcessType.QUEST, _small_cocoon, 1, npc, true);
		}
	}
}