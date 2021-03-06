package dwo.scripts.dynamic_quests;

/**
 * L2GOD Team
 * User: Yorie
 * Date: xx.xx.12
 * Time: xx:xx
 */

public class _00002_OrbisTempleLocalQuest1 extends AbstractOrbisTempleLocalQuest
{
	private _00002_OrbisTempleLocalQuest1(int id)
	{
		super(id);
	}

	public static void main(String[] args)
	{
		_instance = new _00002_OrbisTempleLocalQuest1(201);
	}

	@Override
	public void onCampainStart()
	{
		if(isRaidSpawnScheduled())
		{
			spawnRaid(2);
		}
	}
}
