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
import eis.exceptions.PerceiveException;
import eis.iilang.Function;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Percept;
import nl.tudelft.goal.unreal.messages.UnrealIdOrLocation;
import static nl.tudelft.goal.ut3.environment.AbstractEnvironmentTests.startupPercepts;
import static nl.tudelft.goal.ut3.environment.AbstractEnvironmentTests.testBot;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Evers
 */
public class MapPerceptTests extends AbstractEnvironmentTests {

        /**
         * Check the navPoint percept.
         */
        @Test
        public void navPointTest() {
                boolean bFoundUdamage = false;
                for (Percept p : startupPercepts) {
                        if (p.getName().equals("navPoint")) {
                                assertTrue(p.getParameters().size() == 3);
                                if (p.getParameters().get(0).equals(new Identifier("UTPickupFactory_UDamage_0"))) {
                                        bFoundUdamage = true;

                                        // Check location 
                                        Function udamageLocation = new Function("location", new Numeral(0.72), new Numeral(1.17), new Numeral(172.0));
                                        assertTrue(udamageLocation.equals(p.getParameters().get(1)));
                                }
                        }
                }
                assertTrue(bFoundUdamage);
        }
        
        @Test
        public void pickupBioRifleTest() {
                boolean bioRifleFound = false;
                
                for (Percept p : startupPercepts) {
                        if (p.getName().equals("pickup") && p.getParameters().size() == 3) {
                                 if (p.getParameters().get(2).equals(new Identifier("bio_rifle"))) {
                                        bioRifleFound = true;
                                }                       
                        }
                }
                assertTrue(bioRifleFound);
        }
        
        @Test
        public void pickupShockRifleTest() {
                boolean shockRifleFound = false;
                
                for (Percept p : startupPercepts) {
                        if (p.getName().equals("pickup") && p.getParameters().size() == 3) {
                                 if (p.getParameters().get(2).equals(new Identifier("shock_rifle"))) {
                                        shockRifleFound = true;
                                }                       
                        }
                }
                assertTrue(shockRifleFound);
        }
        
        @Test
        public void pickupLinkGunTest() {
                boolean linkGunFound = false;
                
                for (Percept p : startupPercepts) {
                        if (p.getName().equals("pickup") && p.getParameters().size() == 3) {
                                 if (p.getParameters().get(2).equals(new Identifier("link_gun"))) {
                                        linkGunFound = true;
                                }                       
                        }
                }
                assertTrue(linkGunFound);
        }
        
        @Test       
        public void pickupStingerMinigunTest() {
                boolean stingerFound = false;
                
                for (Percept p : startupPercepts) {
                        if (p.getName().equals("pickup") && p.getParameters().size() == 3) {
                                 if (p.getParameters().get(2).equals(new Identifier("stinger_minigun"))) {
                                        stingerFound = true;
                                }                       
                        }
                }
                assertTrue(stingerFound);
        }
        
        
        @Test       
        public void pickupFlakCannonTest() {
                boolean flakCannonFound = false;
                
                for (Percept p : startupPercepts) {
                        if (p.getName().equals("pickup") && p.getParameters().size() == 3) {
                                 if (p.getParameters().get(2).equals(new Identifier("flak_cannon"))) {
                                        flakCannonFound = true;
                                }                       
                        }
                }
                assertTrue(flakCannonFound);
        }
        
        @Test       
        public void pickupRocketLauncherTest() {
                boolean rocketLauncherFound = false;
                
                for (Percept p : startupPercepts) {
                        if (p.getName().equals("pickup") && p.getParameters().size() == 3) {
                                 if (p.getParameters().get(2).equals(new Identifier("rocket_launcher"))) {
                                        rocketLauncherFound = true;
                                }                       
                        }
                }
                assertTrue(rocketLauncherFound);
        }
        
        @Test       
        public void pickupSniperRifleTest() {
                boolean sniperRifleFound = false;
                
                for (Percept p : startupPercepts) {
                        if (p.getName().equals("pickup") && p.getParameters().size() == 3) {
                                 if (p.getParameters().get(2).equals(new Identifier("sniper_rifle"))) {
                                        sniperRifleFound = true;
                                }                       
                        }
                }
                assertTrue(sniperRifleFound);
        }
        
        @Test     
        public void pickupRedeemerTest() {
                boolean redeemerFound = false;
                
                for (Percept p : startupPercepts) {
                        if (p.getName().equals("pickup") && p.getParameters().size() == 3) {
                                 if (p.getParameters().get(2).equals(new Identifier("redeemer"))) {
                                        redeemerFound = true;
                                }                       
                        }
                }
                assertTrue(redeemerFound);
        }

        /**
         * Test the pickup percepts for deployable weapons.
         */
        @Test
        public void pickupDeployablesTest() {
                boolean slowVolumeFound = false;

                for (Percept p : startupPercepts) {
                        /**
                         * TEST ALL THE DEPLOYABLES ON THIS MAP *
                         */
                        // TODO: Rest of the deployables
                        if (p.getName().equals("pickup")) {
                                if (p.getParameters().size() == 3 && p.getParameters().get(2).equals(new Identifier("slow_volume"))) {
                                        slowVolumeFound = true;
                                        assertEquals(new Identifier("deployable"), p.getParameters().get(1));
                                }
                        }
                }
                assertTrue(slowVolumeFound);
        }

        /**
         * Test if the blue flag is on the blue UnrealID and the red flag is on
         * the red UnrealID.
         */
        @Test
        public void flagBasePercept() {
                for (Percept p : startupPercepts) {
                        if (p.getName().equals("base")) {
                                assertTrue(p.getParameters().size() == 2);
                                if (p.getParameters().get(0).equals(new Identifier("blue"))) {
                                        assertEquals(new Identifier("UTCTFBlueFlagBase_0"), p.getParameters().get(1));
                                }
                                if (p.getParameters().get(0).equals(new Identifier("red"))) {
                                        assertEquals(new Identifier("UTCTFRedFlagBase_0"), p.getParameters().get(1));
                                }
                        }
                }
        }
}