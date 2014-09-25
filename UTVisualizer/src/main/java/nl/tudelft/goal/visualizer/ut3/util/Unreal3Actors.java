/*
 * Copyright (C) 2013 AMIS research group, Faculty of Mathematics and Physics, Charles University in Prague, Czech Republic
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
package nl.tudelft.goal.visualizer.ut3.util;

/**
 *
 * @author Michiel
 */
public class Unreal3Actors {
    // FIXME: Unreal has 2 sniper weapons, we are missing the original sniper
    //gun here. Also there should be an airstrike and ion canon painter.

    /**
     * Collection of Unreal Class names that can be added to the inventory.
     */
    public final static String[] WEAPON_TYPES = new String[]{        
        "UTGame.UTWeap_LinkGun",        
        "UTGameContent.UTWeap_Avril_Content",        
        "UTGameContent.UTWeap_BioRifle_Content",
        "UTGame.UTWeap_Enforcer",
        "UTGame.UTWeap_FlakCannon",                                
        "UTGameContent.UTWeap_Redeemer_Content",
        "UTGame.UTWeap_RocketLauncher",
        "UTGame.UTWeap_ShockRifle",
        "UTGame.UTWeap_SniperRifle",
        "UTGame.UTWeap_Stinger"
     };
    
    public final static String[] DEPLOYABLE_TYPES = new String[] {
        "UTGameContent.UTDeployableEMPMine",
        "UTGameContent.UTDeployableEnergyShield",
        "UTGameContent.UTDeployableLinkGenerator",
        "UTGameContent.UTDeployableShapedCharge",
        "UTGameContent.UTDeployableSlowVolume",
        "UTGameContent.UTDeployableSpiderMineTrap",
        "UTGame.UTDeployableXRayVolumeBase",
        "UT3Gold.UTDeployableXRayVolume"
    };
    
    public final static String[] AMMO_TYPES = new String[] {
        "GameBotsUT3.GBAmmo_LinkGun",
        "GameBotsUT3.GBAmmo_AVRiL",        
        "GameBotsUT3.GBAmmo_BioRifle_Content",
        "GameBotsUT3.GBAmmo_Enforcer",
        "GameBotsUT3.GBAmmo_FlakCannon",                
        "GameBotsUT3.GBAmmo_RocketLauncher",
        "GameBotsUT3.GBAmmo_ShockRifle",
        "GameBotsUT3.GBAmmo_SniperRifle",
        "GameBotsUT3.GBAmmo_Stinger"
    };
    
    public final static String[] PICKUP_TYPES = new String[] {
        "GameBotsUT3.GBArmorPickup_Helmet",
        "GameBotsUT3.GBArmorPickup_ShieldBelt",
        "GameBotsUT3.GBArmorPickup_Thighpads",
        "GameBotsUT3.GBArmorPickup_Vest",        
        "GameBotsUT3.GBPickupFactory_HealthVial",
        "GameBotsUT3.GBPickupFactory_MediumHealth",
        "GameBotsUT3.GBPickupFactory_SuperHealth_Spawnable",
        "UTGameContent.UTBerserk",
        "UTGameContent.UTInvisibility",
        "UTGameContent.UTInvulnerability",
        "UTGameContent.UTJumpBoots",        
        "UTGameContent.UTUDamage"
    };
    
    /*
    public final static String[] DEPLOYED_TYPES = new String[] {
        "UTGameContent.UTEnergyShield"
    };    
    */
    
    public final static String[] VEHICLE_TYPES = new String[] {
        "UTGameContent.UTVehicle_Cicada_Content",
        "UTGameContent.UTVehicle_Fury_Content",
        "UTGameContent.UTVehicle_Raptor_Content",
        "UTGameContent.UTVehicle_Manta_Content",
        "UTGameContent.UTVehicle_Viper_Content",
        "UTGameContent.UTVehicle_NightShade_Content",        
        "UT3Gold.UTVehicle_StealthbenderGold_Content",
        "UTGameContent.UTVehicle_Leviathan_Content",
        "UTGameContent.UTVehicle_SPMA_Content",
        "UTGameContent.UTVehicle_Eradicator_Content",
        "UTGameContent.UTVehicle_Goliath_Content",
        "UTGameContent.UTVehicle_HellBender_Content",
        "UTGameContent.UTVehicle_Nemesis",
        "UTGameContent.UTVehicle_Paladin",
        "UTGameContent.UTVehicle_Scorpion_Content",
        "UTGameContent.UTVehicle_DarkWalker_Content",
        "UTGameContent.UTVehicle_Scavenger_Content",
    };
}
            