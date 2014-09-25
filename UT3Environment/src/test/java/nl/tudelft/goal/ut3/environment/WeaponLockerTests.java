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
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Evers
 */
public class WeaponLockerTests extends TimeOutEnvironmentTests {

        /**
         * Navigate to the weaponlocker to pickup all the weapons required for
         * the test.
         *
         * @throws InterruptedException
         * @throws PerceiveException
         */
        @BeforeClass
        public static void navigateToPickup() throws InterruptedException, PerceiveException {
                // Clear the weapon prefs, needed because otherwise the bot will change back instantly                
                testBot.navigate(new UnrealIdOrLocation(UnrealId.get("UTWeaponLocker_Content_0")));
                boolean reached = false;

                float start = System.currentTimeMillis();
                while (!reached && ((System.currentTimeMillis() - start) < TimeOutEnvironmentTests.TIMEOUT)) {
                        for (Percept p : getPercepts()) {
                                if (p.getName().equals("navigation")) {
                                        if (p.getParameters().get(0).equals(new Identifier("reached"))) {
                                                reached = true;
                                        }
                                }
                        }
                }
                assertTrue(reached);
        }

        @Test
        public void impactHammerAmmoTest() throws PerceiveException {
                for (Percept p : startupPercepts) {
                        if (p.getName().equals("weapon")) {
                                if (p.getParameters().get(0).equals(new Identifier("impact_hammer"))) {
                                        assertTrue(p.getParameters().size() == 3);
                                        assertTrue(p.getParameters().get(1).equals(new Numeral(0)));
                                }
                        }
                }
        }

        @Test
        public void enforcerAmmoTest() throws PerceiveException {
                for (Percept p : startupPercepts) {
                        if (p.getName().equals("weapon")) {
                                if (p.getParameters().get(0).equals(new Identifier("enforcer"))) {
                                        assertTrue(p.getParameters().size() == 3);
                                        assertTrue(p.getParameters().get(1).equals(new Numeral(50)));
                                }
                        }
                }
        }
        
        @Test
        public void bioRifleAmmoTest() throws PerceiveException {
                assertTrue(testBot.getWeaponry().getWeapon(UT3ItemType.BIO_RIFLE) != null);
                assertEquals(50, testBot.getWeaponry().getAmmo(UT3ItemType.BIO_RIFLE));
        }
        
        @Test
        public void shockRifleAmmoTest() throws PerceiveException {
                assertTrue(testBot.getWeaponry().getWeapon(UT3ItemType.SHOCK_RIFLE) != null);
                assertEquals(30, testBot.getWeaponry().getAmmo(UT3ItemType.SHOCK_RIFLE));
        }
        
        @Test
        public void stingerMinigunAmmoTest() throws PerceiveException {
                assertTrue(testBot.getWeaponry().getWeapon(UT3ItemType.STINGER_MINIGUN) != null);
                assertEquals(150, testBot.getWeaponry().getAmmo(UT3ItemType.STINGER_MINIGUN));
        }
        
        @Test
        public void rocketLauncherAmmoTest() throws PerceiveException {
                assertTrue(testBot.getWeaponry().getWeapon(UT3ItemType.ROCKET_LAUNCHER) != null);
                assertEquals(18, testBot.getWeaponry().getAmmo(UT3ItemType.ROCKET_LAUNCHER));
        }
        
        @Test
        public void linkGunAmmoTest() throws PerceiveException {
                assertTrue(testBot.getWeaponry().getWeapon(UT3ItemType.LINK_GUN) != null);
                assertEquals(100, testBot.getWeaponry().getAmmo(UT3ItemType.LINK_GUN));
        }
}
