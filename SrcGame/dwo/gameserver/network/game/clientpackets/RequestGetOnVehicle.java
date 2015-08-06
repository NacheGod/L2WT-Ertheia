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
package dwo.gameserver.network.game.clientpackets;

import dwo.gameserver.instancemanager.vehicle.BoatManager;
import dwo.gameserver.model.actor.L2Character;
import dwo.gameserver.model.actor.instance.L2BoatInstance;
import dwo.gameserver.model.actor.instance.L2PcInstance;
import dwo.gameserver.network.game.serverpackets.packet.vehicle.boat.GetOnVehicle;
import dwo.gameserver.util.geometry.Point3D;

public class RequestGetOnVehicle extends L2GameClientPacket
{
	private int _boatId;
	private Point3D _pos;

	@Override
	protected void readImpl()
	{
		int x;
		int y;
		int z;
		_boatId = readD();
		x = readD();
		y = readD();
		z = readD();
		_pos = new Point3D(x, y, z);
	}

	@Override
	protected void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if(activeChar == null)
		{
			return;
		}

		L2BoatInstance boat;
		if(activeChar.isInBoat())
		{
			boat = activeChar.getBoat();
			if(boat.getObjectId() != _boatId)
			{
				activeChar.sendActionFailed();
				return;
			}
		}
		else
		{
			boat = BoatManager.getInstance().getBoat(_boatId);
			if(boat == null || boat.isMoving() || !activeChar.isInsideRadius(boat, 1000, true, false))
			{
				activeChar.sendActionFailed();
				return;
			}
		}

		activeChar.setInVehiclePosition(_pos);
		activeChar.setVehicle(boat);
		activeChar.broadcastPacket(new GetOnVehicle(activeChar.getObjectId(), boat.getObjectId(), _pos));
		activeChar.setInsideZone(L2Character.ZONE_PEACE, true);
		activeChar.setXYZ(boat.getX(), boat.getY(), boat.getZ());
		activeChar.revalidateZone(true);
	}

	@Override
	public String getType()
	{
		return "[C] 5C GetOnVehicle";
	}
}