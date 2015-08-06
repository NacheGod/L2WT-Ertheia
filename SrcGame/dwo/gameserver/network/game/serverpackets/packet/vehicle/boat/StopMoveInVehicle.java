package dwo.gameserver.network.game.serverpackets.packet.vehicle.boat;

import dwo.gameserver.model.actor.instance.L2PcInstance;
import dwo.gameserver.network.game.serverpackets.L2GameServerPacket;
import dwo.gameserver.util.geometry.Point3D;

/**
 * @author Maktakien
 */

public class StopMoveInVehicle extends L2GameServerPacket
{
	private int _charObjId;
	private int _boatId;
	private Point3D _pos;
	private int _heading;

	public StopMoveInVehicle(L2PcInstance player, int boatId)
	{
		_charObjId = player.getObjectId();
		_boatId = boatId;
		_pos = player.getInVehiclePosition();
		_heading = player.getHeading();
	}

	@Override
	protected void writeImpl()
	{
		writeD(_charObjId);
		writeD(_boatId);
		writeD(_pos.getX());
		writeD(_pos.getY());
		writeD(_pos.getZ());
		writeD(_heading);
	}
}
