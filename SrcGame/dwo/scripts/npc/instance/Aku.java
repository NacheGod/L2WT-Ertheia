package dwo.scripts.npc.instance;

import dwo.gameserver.model.actor.L2Npc;
import dwo.gameserver.model.actor.instance.L2PcInstance;
import dwo.gameserver.model.player.formation.group.L2CommandChannel;
import dwo.gameserver.model.world.quest.Quest;
import dwo.scripts.instances.RB_Tauti;

/**
 * L2GOD Team
 * User: ANZO
 * Date: 03.09.12
 * Time: 14:16
 *
 * NPC для входа в инстанс Экстрим Таути.
 */

public class Aku extends Quest
{
	private static final int AKU = 33671;

	public Aku()
	{
		addAskId(AKU, -1027);
	}

	public static void main(String[] args)
	{
		new Aku();
	}

	@Override
	public String onAsk(L2PcInstance player, L2Npc npc, int ask, int reply)
	{
		switch(reply)
		{
			case 1:
				return "sofa_aku002.htm";
			case 2:
				String message = checkEnterConditions(player);
				if(message != null)
				{
					return message;
				}
				RB_Tauti.getInstance().enterInstance(player, true);
				return null;
			case 3:
				RB_Tauti.getInstance().enterInstance(player, true);
				return null;
		}
		return null;
	}

	/**
	 * Проверка условий для входа в инстанс
	 * @param player Игрок.
	 * @return удоволетворяет-ли группа персонажей дял входа в инстанс
	 */
	private String checkEnterConditions(L2PcInstance player)
	{
		if(player.isGM())
		{
			return null;
		}

		// TODO: Если игрок случайно вышел с инста , то показываем sofa_aku002g.htm чтобы он смог зайти снова

		// TODO: Cемя Адского Пламени сейчас занято, битва с Таути невозможна - sofa_aku002h.htm

		if(player.getParty() == null || player.getParty().getCommandChannel() == null)
		{
			return "sofa_aku002e.htm";
		}

		L2CommandChannel channel = player.getParty().getCommandChannel();

		if(!channel.getLeader().equals(player))
		{
			return "sofa_aku002d.htm";
		}

		if(channel.getMemberCount() > 35 || channel.getMemberCount() < 21)
		{
			return "sofa_aku002c.htm";
		}

		for(L2PcInstance member : channel.getMembers())
		{
			if(member.getLevel() < 97)
			{
				return "sofa_aku002b.htm";
			}
		}

		return null;
	}
}