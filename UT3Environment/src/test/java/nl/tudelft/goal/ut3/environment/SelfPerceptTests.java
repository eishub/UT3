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
import eis.iilang.Function;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Percept;
import nl.tudelft.goal.unreal.messages.UnrealIdOrLocation;
import static nl.tudelft.goal.ut3.environment.AbstractEnvironmentTests.testBot;
import nl.tudelft.goal.ut3.messages.FireMode;
import nl.tudelft.goal.ut3.messages.SelectorList;
import nl.tudelft.goal.ut3.selector.WorldObject;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for all the UT3 self percepts.
 * 
 * @author Evers
 */
public class SelfPerceptTests extends AbstractEnvironmentTests {

        /**
         * Check the self-percept.
         */
        @Test
        public void selfTest() {
                for(Percept p : startupPercepts) {
                        if(p.getName().equals("self")) {
                                assertTrue(p.getParameters().size() == 3);
                                // traverse all parameters
                               assertEquals(new Identifier(BOT_NAME), p.getParameters().get(1));
                               assertEquals(new Identifier(BOT_TEAM.name().toLowerCase()), p.getParameters().get(2));
                        }
                }
        }
        
        /**
         * Check the orientation percept of the bot while not moving.
         *  The percepts are wrong if the location equals position 0.0,0.0,0.0.
         */
        @Test
        public void orientationStandStillTest() {
                 for(Percept p : startupPercepts) {
                        if(p.getName().equals("orientation")) {
                                // traverse all parameters
                                assertTrue(p.getParameters().size() == 3);
                                Function noneLocation = new Function("location", new Numeral(0.0), new Numeral(0.0), new Numeral(0.0));
                                assertFalse(p.getParameters().get(0).equals(noneLocation));
                        }
                }                                               
        }
        
        // TODO(Optional): Create dynamic test for velocity, let the bot run to the flag and measure a velocity > 0
        
        /**
         * Check the status percept.
         */
        @Test
        public void statusTest() {
                  for(Percept p : startupPercepts) {
                        if(p.getName().equals("status")) {
                                // The OLD UT2004 environment got 4 parameters (Health, Armor, Adrenaline and Combo)
                                //  but the new environment does not have Combo and adrenaline so only Health and Armor are sent and adrenaline = 0 and combo = null.
                                assertTrue(p.getParameters().size() == 4);
                                assertEquals(new Numeral(100), p.getParameters().get(0));
                                assertEquals(new Numeral(0), p.getParameters().get(1));
                        }
                }                 
        }
        
        /**
         * Check the score percept.
         */
        @Test
        public void scoreTest() {
                
                // Score must be 0,0,0
                for(Percept p : startupPercepts) {
                        if(p.getName().equals("score")) {
                                assertTrue(p.getParameters().size() == 3);
                                assertEquals(new Numeral(0), p.getParameters().get(0));
                                assertEquals(new Numeral(0), p.getParameters().get(1));
                                assertEquals(new Numeral(0), p.getParameters().get(2));
                        }
                }   
        }
        
        /**
         * Check the currentWeapon percept.
         */
        @Test
        public void currentWeaponTest() {
                for(Percept p : startupPercepts) {
                        if(p.getName().equals("currentWeapon")) {
                                assertTrue(p.getParameters().size() == 2);
                                assertEquals(new Identifier("enforcer"), p.getParameters().get(0));
                                assertEquals(new Identifier(FireMode.NONE.name().toLowerCase()), p.getParameters().get(1));
                        }
                }
        }
        
        /**
         * Check the weapon percept.
         */
        @Test
        public void weaponTest() {
                boolean enforcer = false, impact_hammer = false;
                
                for(Percept p : startupPercepts) {
                        if(p.getName().equals("weapon")) {
                                assertTrue(p.getParameters().size() == 3);      // WeaponType, Ammo, AltAmmo
                                
                                // Enforcer
                                if(p.getParameters().get(0).equals(new Identifier("enforcer"))) {
                                        enforcer = true;
                                        assertEquals(new Numeral(50), p.getParameters().get(1));
                                        assertEquals(new Numeral(50), p.getParameters().get(2));
                                }
                                // Impact hammer, no ammo
                                else if(p.getParameters().get(0).equals(new Identifier("impact_hammer"))) {
                                        impact_hammer = true;
                                        assertEquals(new Numeral(0), p.getParameters().get(1));
                                        assertEquals(new Numeral(0), p.getParameters().get(2));                                        
                                }
                                else {
                                        // We got some different weapons, not particulary wrong but strange
                                        
                                }
                        }
                }      
                assertTrue(enforcer);
                assertTrue(impact_hammer);
        }
        
        /**
         * Test ammo and health while shooting itself.
         * @throws InterruptedException 
         */
        @Test
        public void testSuicideAttempt() throws InterruptedException
        {
                testBot.getWeaponPrefs().clearAllPrefs();
                testBot.getWeaponPrefs().addGeneralPref(UT3ItemType.ROCKET_LAUNCHER, true);
                testBot.navigate(new UnrealIdOrLocation(UnrealId.get("UTWeaponLocker_Content_0")));
                Thread.sleep(3000);
                
                SelectorList selectorList = new SelectorList();
                selectorList.add(new WorldObject(UnrealId.get("UTWeaponLocker_Content_0")));
                testBot.shoot(selectorList);      // near suicide attempt
                
                Thread.sleep(3000);
                assertTrue(testBot.getInfo().getHealth() < 100);
                assertEquals(17, testBot.getWeaponry().getAmmo(UT3ItemType.ROCKET_LAUNCHER));
        }
}