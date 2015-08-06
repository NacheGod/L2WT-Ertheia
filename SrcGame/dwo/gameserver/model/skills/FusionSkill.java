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

package dwo.gameserver.model.skills;

import dwo.gameserver.ThreadPoolManager;
import dwo.gameserver.engine.geodataengine.GeoEngine;
import dwo.gameserver.model.actor.L2Character;
import dwo.gameserver.model.skills.base.L2Skill;
import dwo.gameserver.model.skills.effects.L2Effect;
import dwo.gameserver.util.Util;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.concurrent.Future;

/**
 * @author kombat, Forsaiken
 */
public class FusionSkill
{
	protected static final Logger _log = LogManager.getLogger(FusionSkill.class);

	protected int _skillCastRange;
	protected int _fusionId;
	protected int _fusionLevel;
	protected L2Character _caster;
	protected L2Character _target;
	protected Future<?> _geoCheckTask;

	public FusionSkill()
	{
	}

	public FusionSkill(L2Character caster, L2Character target, L2Skill skill)
	{
		_skillCastRange = skill.getCastRange();
		_caster = caster;
		_target = target;
		_fusionId = skill.getTriggeredId();
		_fusionLevel = skill.getTriggeredLevel();

		L2Effect effect = _target.getFirstEffect(_fusionId);
		if(effect != null)
		{
			effect.increaseEffect();
		}
		else
		{
			L2Skill force = SkillTable.getInstance().getInfo(_fusionId, _fusionLevel);
			if(force != null)
			{
				force.getEffects(_caster, _target, null);
			}
			else
			{
				_log.log(Level.WARN, "Triggered skill [" + _fusionId + ';' + _fusionLevel + "] not found!");
			}
		}
		_geoCheckTask = ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new GeoCheckTask(), 1000, 1000);
	}

	public L2Character getCaster()
	{
		return _caster;
	}

	public L2Character getTarget()
	{
		return _target;
	}

	public void onCastAbort()
	{
		_caster.setFusionSkill(null);
		L2Effect effect = _target.getFirstEffect(_fusionId);
		if(effect != null)
		{
			effect.decreaseForce();
		}

		_geoCheckTask.cancel(true);
	}

	public class GeoCheckTask implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				if(!Util.checkIfInRange(_skillCastRange, _caster, _target, true))
				{
					_caster.abortCast();
				}

				if(!GeoEngine.getInstance().canSeeTarget(_caster, _target))
				{
					_caster.abortCast();
				}
			}
			catch(Exception e)
			{
				// ignore
			}
		}
	}
}