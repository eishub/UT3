/*
 * Copyright (C) 2010 Unreal Visualizer Authors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.tudelft.goal.ut2004.visualizer.util;

/**
 * 
 * Class containing string representations of object types that can be spawned
 * in Unreal Tournament 2004.
 * 
 * @author Lennard de Rijk
 *
 */
public class UnrealActors {

	// FIXME: Unreal has 2 sniper weapons, we are missing the original sniper
	//gun here. Also there should be an airstrike and ion canon painter.
	/**
	 * Collection of Unreal Class names that can be added to the inventory.
	 */
	public final static String[] INVENTORY_TYPES = new String[]{
		"xPickups.HealthPack", "xPickups.MiniHealthPack",
		"xPickups.ShieldPack", "xPickups.SuperHealthPack",
		"xPickups.SuperShieldPack", "xPickups.AdrenalinePickup",
		"xPickups.UDamagePack",
		"xWeapons.ShieldGunPickup",
		"xWeapons.AssaultRiflePickup", "xWeapons.AssaultAmmoPickup",
		"xWeapons.BioRiflePickup", "xWeapons.BioAmmoPickup",
		"xWeapons.ShockRiflePickup", "xWeapons.ShockAmmoPickup",
		"xWeapons.LinkGunPickup", "xWeapons.LinkAmmoPickup",
		"xWeapons.MinigunPickup", "xWeapons.MinigunAmmoPickup",
		"xWeapons.FlakCannonPickup","xWeapons.FlakAmmoPickup",
		"xWeapons.RocketLauncherPickup", "xWeapons.RocketAmmoPickup",
		"xWeapons.SniperRiflePickup", "xWeapons.SniperAmmoPickup",
		"xWeapons.PainterPickup", "xWeapons.RedeemerPickup"};
	
}
