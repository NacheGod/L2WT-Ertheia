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
package dwo.gameserver.network.game.clientpackets.packet.enchant.item;

import dwo.gameserver.datatables.xml.EnchantItemData;
import dwo.gameserver.model.actor.instance.L2PcInstance;
import dwo.gameserver.model.items.EnchantItem;
import dwo.gameserver.model.items.base.instance.L2ItemInstance;
import dwo.gameserver.network.game.clientpackets.L2GameClientPacket;
import dwo.gameserver.network.game.components.SystemMessageId;
import dwo.gameserver.network.game.serverpackets.packet.enchant.item.ExPutEnchantSupportItemResult;

/**
 *
 * @author KenM
 */
public class RequestExTryToPutEnchantSupportItem extends L2GameClientPacket
{

	private int _supportObjectId;
	private int _enchantObjectId;

	@Override
	protected void readImpl()
	{
		_supportObjectId = readD();
		_enchantObjectId = readD();
	}

	@Override
	protected void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if(activeChar != null)
		{
			if(activeChar.isEnchanting())
			{
				L2ItemInstance item = activeChar.getInventory().getItemByObjectId(_enchantObjectId);
				L2ItemInstance support = activeChar.getInventory().getItemByObjectId(_supportObjectId);

				if(item == null || support == null)
				{
					return;
				}

				EnchantItem supportTemplate = EnchantItemData.getInstance().getSupportItem(support);

				if(supportTemplate == null || !supportTemplate.isValid(item))
				{
					// message may be custom
					activeChar.sendPacket(SystemMessageId.INAPPROPRIATE_ENCHANT_CONDITION);
					activeChar.setActiveEnchantSupportItem(null);
					activeChar.sendPacket(new ExPutEnchantSupportItemResult(0));
					return;
				}
				activeChar.setActiveEnchantSupportItem(support);
				activeChar.sendPacket(new ExPutEnchantSupportItemResult(_supportObjectId));
			}
		}
	}

	@Override
	public String getType()
	{
		return "[C] D0:50 RequestExTryToPutEnchantSupportItem";
	}
}
