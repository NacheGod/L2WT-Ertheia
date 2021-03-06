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
package dwo.gameserver.model.items.base;

import dwo.gameserver.model.actor.L2Character;
import dwo.gameserver.model.holders.CommissionItemHolder;
import dwo.gameserver.model.holders.SkillHolder;
import dwo.gameserver.model.items.base.instance.L2ItemInstance;
import dwo.gameserver.model.items.base.type.L2ArmorType;
import dwo.gameserver.model.skills.base.funcs.Func;
import dwo.gameserver.model.skills.base.funcs.FuncTemplate;
import dwo.gameserver.model.skills.stats.Env;
import dwo.gameserver.model.skills.stats.StatsSet;
import dwo.gameserver.util.StringUtil;
import javolution.util.FastMap;
import org.apache.log4j.Level;

import java.util.ArrayList;

/**
 * This class is dedicated to the management of armors.
 *
 * @version $Revision: 1.2.2.1.2.6 $ $Date: 2005/03/27 15:30:10 $
 */
public class L2Armor extends L2Item
{
	private FastMap<Integer, SkillHolder> _enchantSkill;  // skill that activates when armor is enchanted +4
	// private final String[] _skill;
	private L2ArmorType _type;
	private CommissionItemHolder.CommissionCategoryType _commission_type;

	/**
	 * Constructor for Armor.<BR><BR>
	 * <U><I>Variables filled :</I></U><BR>
	 * <LI>_avoidModifier</LI>
	 * <LI>_pDef & _mDef</LI>
	 * <LI>_mpBonus & _hpBonus</LI>
	 * <LI>enchant4Skill</LI>
	 * @param set : StatsSet designating the set of couples (key,value) caracterizing the armor
	 * @see L2Item constructor
	 */
    public L2Armor(StatsSet set)
    {
        super(set);
        updateItem(set);
    }

    public void updateItem(StatsSet set)
    {
        _type = L2ArmorType.valueOf(set.getString("armor_type", "none").toUpperCase());
        _commission_type = CommissionItemHolder.CommissionCategoryType.valueOf(set.getString("commission_type", "none").toUpperCase());

        long _bodyPart = getBodyPart();
        if(_bodyPart == L2Item.SLOT_NECK || _bodyPart == L2Item.SLOT_HAIR || _bodyPart == L2Item.SLOT_HAIR2 || _bodyPart == L2Item.SLOT_HAIRALL || (_bodyPart & L2Item.SLOT_L_EAR) != 0 || (_bodyPart & L2Item.SLOT_L_FINGER) != 0 || (_bodyPart & L2Item.SLOT_R_BRACELET) != 0 || (_bodyPart & L2Item.SLOT_L_BRACELET) != 0 || (_bodyPart & L2Item.SLOT_BROACH) != 0)
        {
            _type1 = L2Item.TYPE1_WEAPON_RING_EARRING_NECKLACE;
            _type2 = L2Item.TYPE2_ACCESSORY;
        }
        else
        {
            if(_type == L2ArmorType.NONE && getBodyPart() == L2Item.SLOT_L_HAND) // retail define shield as NONE
            {
                _type = L2ArmorType.SHIELD;
            }
            _type1 = L2Item.TYPE1_SHIELD_ARMOR;
            _type2 = L2Item.TYPE2_SHIELD_ARMOR;
        }

        String skill = set.getString("enchant_skill", null);
        if(skill != null)
        {
            _enchantSkill = new FastMap<>();
            String[] split = skill.split(";");
            for(String part : split)
            {
                try
                {
                    String[] enc = part.split(",");
                    int enchant = Integer.parseInt(enc[0]);
                    String[] info = enc[1].split("-");
                    if(info != null && info.length == 2)
                    {
                        int id = Integer.parseInt(info[0]);
                        int level = Integer.parseInt(info[1]);
                        if(id > 0 && level > 0)
                        {
                            _enchantSkill.put(enchant, new SkillHolder(id, level));
                        }
                    }
                }
                catch(Exception nfe)
                {
                    // Incorrect syntax, dont add new skill
                    _log.log(Level.ERROR, StringUtil.concat("> Couldnt parse ", skill, " in armor enchant skills! item ", toString()));
                }
            }
        }
    }

	/**
	 * Returns the type of the armor.
	 * @return L2ArmorType
	 */
	@Override
	public L2ArmorType getItemType()
	{
		return _type;
	}

	/**
	 * @return the type
	 */
	@Override
	public CommissionItemHolder.CommissionCategoryType getItemCommissionType()
	{
		return _commission_type;
	}

	/**
	 * Returns the ID of the item after applying the mask.
	 * @return int : ID of the item
	 */
	@Override
	public int getItemMask()
	{
		return _type.mask();
	}

	/**
	 * Returns array of Func objects containing the list of functions used by the armor
	 * @param instance : L2ItemInstance pointing out the armor
	 * @param player : L2Character pointing out the player
	 * @return Func[] : array of functions
	 */
	@Override
	public Func[] getStatFuncs(L2ItemInstance instance, L2Character player)
	{
		if(_funcTemplates == null || _funcTemplates.length == 0)
		{
			return _emptyFunctionSet;
		}

		ArrayList<Func> funcs = new ArrayList<>(_funcTemplates.length);

		Env env = new Env();
		env.setPlayer(player);
		env.setItem(instance);

		Func f;

		for(FuncTemplate t : _funcTemplates)
		{

			f = t.getFunc(env, instance);
			if(f != null)
			{
				funcs.add(f);
			}
		}

		return funcs.toArray(new Func[funcs.size()]);
	}

	/**
	 * Returns skill that player get when has equiped armor +4  or more
	 * @return
	 */
	public FastMap<Integer, SkillHolder> getEnchantSkills()
	{
		return _enchantSkill;
	}
}