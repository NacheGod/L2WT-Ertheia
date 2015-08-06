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

import dwo.gameserver.engine.databaseengine.FiltredPreparedStatement;
import dwo.gameserver.engine.databaseengine.L2DatabaseFactory;
import dwo.gameserver.engine.databaseengine.ThreadConnection;
import dwo.gameserver.model.actor.instance.L2PcInstance;
import dwo.gameserver.util.database.DatabaseUtils;
import org.apache.log4j.Level;

import java.sql.SQLException;

/**
 * @author Plim
 */
public class RequestPetitionFeedback extends L2GameClientPacket
{
	private static final String INSERT_FEEDBACK = "INSERT INTO petition_feedback VALUES (?,?,?,?,?)";

	//cdds
	//private int _unknown;
	private int _rate; // 4=VeryGood, 3=Good, 2=Fair, 1=Poor, 0=VeryPoor
	private String _message;

	@Override
	protected void readImpl()
	{
		//_unknown = 
		readD(); // unknown
		_rate = readD();
		_message = readS();
	}

	@Override
	protected void runImpl()
	{
		L2PcInstance player = getClient().getActiveChar();

		if(player == null || player.getLastPetitionGmName() == null)
		{
			return;
		}

		if(_rate > 4 || _rate < 0) // Ilegal vote
		{
			return;
		}

		ThreadConnection con = null;
		FiltredPreparedStatement statement = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(INSERT_FEEDBACK);
			statement.setString(1, player.getName());
			statement.setString(2, player.getLastPetitionGmName());
			statement.setInt(3, _rate);
			statement.setString(4, _message);
			statement.setLong(5, System.currentTimeMillis());

			statement.execute();
		}
		catch(SQLException e)
		{
			_log.log(Level.ERROR, "Error while saving petition feedback");
		}
		finally
		{
			DatabaseUtils.closeDatabaseCS(con, statement);
			player.setLastPetitionGmName(null);
		}
	}

	@Override
	public String getType()
	{
		return "[C] C9 RequestPetitionFeedback";
	}
}
