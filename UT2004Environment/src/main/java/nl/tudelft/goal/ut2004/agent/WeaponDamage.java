package nl.tudelft.goal.ut2004.agent;

import java.util.HashMap;

import cz.cuni.amis.pogamut.ut2004.communication.messages.ItemType;
import cz.cuni.amis.pogamut.ut2004.communication.messages.UT2004ItemType;


public class WeaponDamage {
	
	private static HashMap<String, ItemType> damageToItem = new HashMap<String, ItemType>();
	
	static {
		add(UT2004ItemType.SNIPER_RIFLE, "XWeapons.DamTypeSniperHeadShot", "XWeapons.DamTypeSniperShot");
		add(UT2004ItemType.ROCKET_LAUNCHER, "XWeapons.DamTypeRocket", "XWeapons.DamTypeRocketHoming");
		add(UT2004ItemType.FLAK_CANNON, "XWeapons.DamTypeFlakShell", "XWeapons.DamTypeFlakChunk");
		add(UT2004ItemType.MINIGUN, "XWeapons.DamTypeMinigunAlt", "XWeapons.DamTypeMinigunBullet");
		add(UT2004ItemType.LINK_GUN, "XWeapons.DamTypeLinkShaft","XWeapons.DamTypeLinkPlasma");
		add(UT2004ItemType.SHOCK_RIFLE, "XWeapons.DamTypeShockCombo", "XWeapons.DamTypeShockBall", "XWeapons.DamTypeShockBeam");
		add(UT2004ItemType.BIO_RIFLE, "XWeapons.DamTypeBioGlob");
		add(UT2004ItemType.ASSAULT_RIFLE, "XWeapons.DamTypeAssaultBullet", "XWeapons.DamTypeAssaultGrenade");
		add(UT2004ItemType.SHIELD_GUN, "XWeapons.DamTypeShieldImpact");
	}
	
	
	private static void  add(ItemType weapon, String... damage){
		for(String d: damage){
			damageToItem.put(d, weapon);
		}
	}

	public static ItemType weaponForDamage(String uDamage){
		return damageToItem.get(uDamage);
	}
	
}
