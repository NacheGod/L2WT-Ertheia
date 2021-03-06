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
package dwo.config.mods;

import dwo.config.Config;
import dwo.config.ConfigProperties;
import org.apache.log4j.Level;

/**
 * @author L0ngh0rn
 */
public class ConfigGraciaSeeds extends Config
{
	private static final String path = L2JS_GRACIA_SEEDS_CONFIG;

	public static void loadConfig()
	{
		_log.log(Level.INFO, "Loading: " + path);
		try
		{
			ConfigProperties properties = new ConfigProperties(path);
			SOD_TIAT_KILL_COUNT = getInt(properties, "TiatKillCountForNextState", 10);
			SOD_STAGE_2_LENGTH = getLong(properties, "Stage2Length", 720) * 60000;
			SOI_EKIMUS_KILL_COUNT = getInt(properties, "TiatKillCountForNextState", 1);
			SOI_STAGE_2_LENGTH = getLong(properties, "Stage2Length", 1440) * 60000;
		}
		catch(Exception e)
		{
			throw new Error("Failed to Load " + path + " File.", e);
		}
	}
}
