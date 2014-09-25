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
import eis.iilang.Percept;
import nl.tudelft.goal.unreal.messages.UnrealIdOrLocation;
import static nl.tudelft.goal.ut3.environment.AbstractEnvironmentTests.getPercepts;
import static nl.tudelft.goal.ut3.environment.AbstractEnvironmentTests.testBot;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Basic tests to cover weapon changing.
 *
 * @author Evers
 */
public class ChangeWeaponTests extends TimeOutEnvironmentTests {        
        /**
         * Navigate to the weaponlocker to pickup all the weapons required for the test.
         * @throws InterruptedException
         * @throws PerceiveException 
         */
        @BeforeClass
        public static void navigateToPickup() throws InterruptedException, PerceiveException {
                // Clear the weapon prefs, needed because otherwise the bot will change back instantly                
                testBot.navigate(new UnrealIdOrLocation(UnrealId.get("UTWeaponLocker_Content_0")));
                boolean navigating = true;

                float start = System.currentTimeMillis();
                while (navigating && ((System.currentTimeMillis() - start) < TimeOutEnvironmentTests.TIMEOUT)) {
                        for (Percept p : getPercepts()) {
                                if (p.getName().equals("navigation")) {
                                        if (p.getParameters().get(0).equals(new Identifier("reached"))) {
                                                navigating = false;
                                                break;
                                        }
                                }
                        }
                }
                assertTrue(!navigating);
        }
        
        @Before
        public void initTest() 
        {
                testBot.getWeaponPrefs().clearAllPrefs();
        }
        
        /**
         * Changes the current weapon to the impact hammer.
         */
        @Test
        public void changeToImpactHammerTest() throws PerceiveException
        {
                testBot.getWeaponPrefs().addGeneralPref(UT3ItemType.IMPACT_HAMMER, true);
                boolean changed = false;
                while(!changed && !timeout())
                {
                        for (Percept p : getPercepts()) {
                                if (p.getName().equals("currentWeapon")) {
                                        if (p.getParameters().get(0).equals(new Identifier("impact_hammer"))) {
                                                changed = true;
                                                break;
                                        }
                                }
                        }
                }
                assertTrue(changed);
        }

        /**
         * Changes the current weapon to the enforcer.
         */
        @Test
        public void changeToEnforcerTest() throws PerceiveException
        {
                testBot.getWeaponPrefs().addGeneralPref(UT3ItemType.ENFORCER, true);
                boolean changed = false;
                while(!changed && !timeout())
                {
                        for (Percept p : getPercepts()) {
                                if (p.getName().equals("currentWeapon")) {
                                        if (p.getParameters().get(0).equals(new Identifier("enforcer"))) {
                                                changed = true;
                                                break;
                                        }
                                }
                        }
                }
                assertTrue(changed);
        }
        
        /**
         * Changes the current weapon to the biorifle.
         */
        @Test
        public void changeToBioRifleTest() throws PerceiveException
        {
                testBot.getWeaponPrefs().addGeneralPref(UT3ItemType.BIO_RIFLE, true);
                boolean changed = false;
                while(!changed && !timeout())
                {
                        for (Percept p : getPercepts()) {
                                if (p.getName().equals("currentWeapon")) {
                                        if (p.getParameters().get(0).equals(new Identifier("bio_rifle"))) {
                                                changed = true;
                                                break;
                                        }
                                }
                        }
                }
                assertTrue(changed);
        }
        
        /**
         * Changes the current weapon to the linkgun.
         */
        @Test
        public void changeToLinkGunTest() throws PerceiveException
        {
                testBot.getWeaponPrefs().addGeneralPref(UT3ItemType.LINK_GUN, true);
                boolean changed = false;
                while(!changed && !timeout())
                {
                        for (Percept p : getPercepts()) {
                                if (p.getName().equals("currentWeapon")) {
                                        if (p.getParameters().get(0).equals(new Identifier("link_gun"))) {
                                                changed = true;
                                                break;
                                        }
                                }
                        }
                }
                assertTrue(changed);
        }
        
        /**
         * Changes the current weapon to the rocket launcher.
         */
        @Test
        public void changeToRocketLauncherTest() throws PerceiveException
        {
                testBot.getWeaponPrefs().addGeneralPref(UT3ItemType.ROCKET_LAUNCHER, true);
                boolean changed = false;
                while(!changed && !timeout())
                {
                        for (Percept p : getPercepts()) {
                                if (p.getName().equals("currentWeapon")) {
                                        if (p.getParameters().get(0).equals(new Identifier("rocket_launcher"))) {
                                                changed = true;
                                                break;
                                        }
                                }
                        }
                }
                assertTrue(changed);
        }
        
        /**
         * Changes the current weapon to the .
         */
        @Test
        public void changeToFlakCannonTest() throws PerceiveException
        {
                testBot.getWeaponPrefs().addGeneralPref(UT3ItemType.FLAK_CANNON, true);
                boolean changed = false;
                while(!changed && !timeout())
                {
                        for (Percept p : getPercepts()) {
                                if (p.getName().equals("currentWeapon")) {
                                        if (p.getParameters().get(0).equals(new Identifier("flak_cannon"))) {
                                                changed = true;
                                                break;
                                        }
                                }
                        }
                }
                assertTrue(changed);
        }
        
        /**
         * Changes the current weapon to the enforcer.
         */
        @Test
        public void changeToShockRifleTest() throws PerceiveException
        {
                testBot.getWeaponPrefs().addGeneralPref(UT3ItemType.SHOCK_RIFLE, true);
                boolean changed = false;
                while(!changed && !timeout())
                {
                        for (Percept p : getPercepts()) {
                                if (p.getName().equals("currentWeapon")) {
                                        if (p.getParameters().get(0).equals(new Identifier("shock_rifle"))) {
                                                changed = true;
                                                break;
                                        }
                                }
                        }
                }
                assertTrue(changed);
        }
        
        /**
         * Changes the current weapon to the enforcer.
         */
        @Test
        public void changeToStingerTest() throws PerceiveException
        {
                testBot.getWeaponPrefs().addGeneralPref(UT3ItemType.STINGER_MINIGUN, true);
                boolean changed = false;
                while(!changed && !timeout())
                {
                        for (Percept p : getPercepts()) {
                                if (p.getName().equals("currentWeapon")) {
                                        if (p.getParameters().get(0).equals(new Identifier("stinger_minigun"))) {
                                                changed = true;
                                                break;
                                        }
                                }
                        }
                }
                assertTrue(changed);
        }
}