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
import eis.iilang.Percept;
import nl.tudelft.goal.unreal.messages.UnrealIdOrLocation;
import static nl.tudelft.goal.ut3.environment.AbstractEnvironmentTests.testBot;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Evers
 */
public class InitialAmmoTests extends TimeOutEnvironmentTests {
        /**
         * Walk over the normal pickups in a straight line.
         */
        @BeforeClass
        public static void navigateToPickup() throws InterruptedException, PerceiveException {
                // Clear the weapon prefs, needed because otherwise the bot will change back instantly 
                testBot.navigate(new UnrealIdOrLocation(UnrealId.get("UTPickupFactory_UDamage_0")));
                Thread.sleep(6000);
                testBot.navigate(new UnrealIdOrLocation(UnrealId.get("UTWeaponPickupFactory_9")));
                Thread.sleep(6000);
        }
        
        @Test
        public void impactHammerAmmoTest() {
                assertEquals(0, testBot.getWeaponry().getAmmo(UT3ItemType.IMPACT_HAMMER));
        }
        
        @Test
        public void bioRifleAmmoTest() throws PerceiveException {
                assertTrue(testBot.getWeaponry().getWeapon(UT3ItemType.BIO_RIFLE) != null);
                assertEquals(25, testBot.getWeaponry().getAmmo(UT3ItemType.BIO_RIFLE));
        }
        
        @Test
        public void shockRifleAmmoTest() throws PerceiveException {
                assertTrue(testBot.getWeaponry().getWeapon(UT3ItemType.SHOCK_RIFLE) != null);
                assertEquals(20, testBot.getWeaponry().getAmmo(UT3ItemType.SHOCK_RIFLE));
        }
        
        @Test
        public void stingerMinigunAmmoTest() throws PerceiveException {
                assertTrue(testBot.getWeaponry().getWeapon(UT3ItemType.STINGER_MINIGUN) != null);
                assertEquals(100, testBot.getWeaponry().getAmmo(UT3ItemType.STINGER_MINIGUN));
        }
        
        @Test
        public void rocketLauncherAmmoTest() throws PerceiveException {
                assertTrue(testBot.getWeaponry().getWeapon(UT3ItemType.ROCKET_LAUNCHER) != null);
                assertEquals(9, testBot.getWeaponry().getAmmo(UT3ItemType.ROCKET_LAUNCHER));
        }
        
        @Test
        public void linkGunAmmoTest() throws PerceiveException {
                assertTrue(testBot.getWeaponry().getWeapon(UT3ItemType.LINK_GUN) != null);
                assertEquals(50, testBot.getWeaponry().getAmmo(UT3ItemType.LINK_GUN));
        }
}
