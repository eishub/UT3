/*
 * Copyright (C) 2014 AMIS research group, Faculty of Mathematics and Physics, Charles University in Prague, Czech Republic
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
package nl.tudelft.goal.ut3.environment;

import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;
import cz.cuni.amis.pogamut.ut3.communication.messages.UT3ItemType;
import eis.exceptions.PerceiveException;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Percept;
import nl.tudelft.goal.unreal.messages.UnrealIdOrLocation;
import static nl.tudelft.goal.ut3.environment.AbstractEnvironmentTests.getPercepts;
import static nl.tudelft.goal.ut3.environment.AbstractEnvironmentTests.testBot;
import nl.tudelft.goal.ut3.messages.SelectorList;
import nl.tudelft.goal.ut3.selector.WorldObject;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Evers
 */
public class AmmoTests extends TimeOutEnvironmentTests {
        /**
         * Start shooting with the enforcer and check the ammo.
         * @throws InterruptedException
         * @throws PerceiveException 
         */
        @Test
        public void shootEnforcerTest() throws InterruptedException, PerceiveException {
                testBot.getWeaponPrefs().clearAllPrefs();
                testBot.getWeaponPrefs().addGeneralPref(UT3ItemType.ENFORCER, true);
                SelectorList selectorList = new SelectorList();
                selectorList.add(new WorldObject(UnrealId.get("UTCTFRedFlagBase_0")));
                testBot.shoot(selectorList);
                
                boolean weaponFired = false;
                while(!weaponFired && !timeout())
                {
                        for (Percept p : getPercepts()) {
                                if (p.getName().equals("weapon")) {
                                        if (p.getParameters().get(0).equals(new Identifier("enforcer"))) {
                                                assertFalse(p.getParameters().get(1).equals(new Numeral(50)));
                                                weaponFired = true;
                                        }
                                }
                        }
                }
                assertTrue(weaponFired);
        }
        
        /**
         * Shoot with the rocket launcher and check the ammo
         * @throws InterruptedException 
         */
        @Test
        public void shootRocketLauncher() throws InterruptedException {
                testBot.getWeaponPrefs().clearAllPrefs();
                testBot.getWeaponPrefs().addGeneralPref(UT3ItemType.ROCKET_LAUNCHER, true);
                testBot.navigate(new UnrealIdOrLocation(UnrealId.get("UTWeaponLocker_Content_0")));
                SelectorList selectorList = new SelectorList();
                selectorList.add(new WorldObject(UnrealId.get("UTCTFRedFlagBase_0")));
                Thread.sleep(3000);                

                testBot.shoot(selectorList);
                
                Thread.sleep(300);
                assertTrue(testBot.getWeaponry().getWeapon(UT3ItemType.ROCKET_LAUNCHER) != null);
                assertFalse(testBot.getWeaponry().getWeapon(UT3ItemType.ROCKET_LAUNCHER).getPrimaryAmmo() == 18);
        }
        
        /**
         * Shoot with the bio-rifle and check the ammo.
         * @throws InterruptedException 
         */
        @Test
        public void shootBioRifle() throws InterruptedException {
                testBot.getWeaponPrefs().clearAllPrefs();
                testBot.getWeaponPrefs().addGeneralPref(UT3ItemType.BIO_RIFLE, true);
                testBot.navigate(new UnrealIdOrLocation(UnrealId.get("UTWeaponLocker_Content_0")));
                SelectorList selectorList = new SelectorList();
                selectorList.add(new WorldObject(UnrealId.get("UTCTFRedFlagBase_0")));
                Thread.sleep(3000);
                
                testBot.shoot(selectorList);      // near suicide attempt
                
                Thread.sleep(300);
                assertTrue(testBot.getWeaponry().getWeapon(UT3ItemType.BIO_RIFLE) != null);
                assertFalse(testBot.getWeaponry().getWeapon(UT3ItemType.BIO_RIFLE).getPrimaryAmmo() == 25);
        }

        /**
         * Shoot with the shock-rifle and check the ammo.
         * @throws InterruptedException 
         */
        @Test
        public void shootShockRifle() throws InterruptedException {
                testBot.getWeaponPrefs().clearAllPrefs();
                testBot.getWeaponPrefs().addGeneralPref(UT3ItemType.SHOCK_RIFLE, true);
                testBot.navigate(new UnrealIdOrLocation(UnrealId.get("UTWeaponLocker_Content_0")));
                Thread.sleep(3000);
                
                SelectorList selectorList = new SelectorList();
                selectorList.add(new WorldObject(UnrealId.get("UTCTFRedFlagBase_0")));
                testBot.shoot(selectorList);      // near suicide attempt
                
                Thread.sleep(300);
                assertTrue(testBot.getWeaponry().getWeapon(UT3ItemType.SHOCK_RIFLE) != null);
                assertFalse(testBot.getWeaponry().getWeapon(UT3ItemType.SHOCK_RIFLE).getPrimaryAmmo() == 18);
        }
        
        /**
         * Shoot with the stinger minigun and check the ammo.
         * @throws InterruptedException 
         */
        @Test
        public void shootStinger() throws InterruptedException {
                testBot.getWeaponPrefs().clearAllPrefs();
                testBot.getWeaponPrefs().addGeneralPref(UT3ItemType.STINGER_MINIGUN, true);
                testBot.navigate(new UnrealIdOrLocation(UnrealId.get("UTWeaponLocker_Content_0")));
                Thread.sleep(3000);
                
                SelectorList selectorList = new SelectorList();
                selectorList.add(new WorldObject(UnrealId.get("UTCTFRedFlagBase_0")));
                testBot.shoot(selectorList);      // near suicide attempt
                
                Thread.sleep(300);
                assertTrue(testBot.getWeaponry().getWeapon(UT3ItemType.STINGER_MINIGUN) != null);
                assertFalse(testBot.getWeaponry().getWeapon(UT3ItemType.STINGER_MINIGUN).getPrimaryAmmo() == 100);
        }
        
        /**
         * Shoot with the linkgun and check the ammo.
         * @throws InterruptedException 
         */
        @Test
        public void shootLinkGun() throws InterruptedException {
                testBot.getWeaponPrefs().clearAllPrefs();
                testBot.getWeaponPrefs().addGeneralPref(UT3ItemType.LINK_GUN, true);
                testBot.navigate(new UnrealIdOrLocation(UnrealId.get("UTWeaponLocker_Content_0")));
                Thread.sleep(3000);
                
                SelectorList selectorList = new SelectorList();
                selectorList.add(new WorldObject(UnrealId.get("UTCTFRedFlagBase_0")));
                testBot.shoot(selectorList);      // near suicide attempt
                
                Thread.sleep(300);
                assertTrue(testBot.getWeaponry().getWeapon(UT3ItemType.LINK_GUN) != null);
                assertFalse(testBot.getWeaponry().getWeapon(UT3ItemType.LINK_GUN).getPrimaryAmmo() == 50);
        }
}
