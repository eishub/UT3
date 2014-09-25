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
import eis.iilang.Numeral;
import eis.iilang.Percept;
import java.util.ArrayList;
import nl.tudelft.goal.unreal.messages.UnrealIdOrLocation;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for almost all the UT3 action percepts.
 * 
 * @author Evers
 */
public class ActionPerceptTests extends AbstractEnvironmentTests {
        
        // TODO: Fragged percept is hard to test because we actually need to load two agents in the world and let
        //              on of them finish the other, instead we could spawn the bot with a rocket-launcher and kill himself
        // Navigation tests are in NavigationTest.java
        
        /**
         * Check the path percept.
         */
        @Test
        public void pathTest() throws InterruptedException, PerceiveException {
                // Calculate path to the udamage pickup
                nl.tudelft.goal.unreal.messages.Percept p = testBot.path(new UnrealIdOrLocation(testBot.getInfo().getLocation()), new UnrealIdOrLocation(UnrealId.get("UTPickupFactory_UDamage_0")));
                // We can only test the EndId and the list of locations because start-position are different every time
                assertTrue(p.size() == 4);
                assertEquals(UnrealId.get("UTPickupFactory_UDamage_0"), p.get(1));
                ArrayList<UnrealId> unrealIds = (ArrayList<UnrealId>) p.get(3);
                assertEquals(UnrealId.get("UTPickupFactory_UDamage_0"), unrealIds.get(unrealIds.size()-1));
        }
        
        /**
         * Check the logic percept.
         * @throws PerceiveException 
         */
        @Test
        public void logicTest() throws PerceiveException {
                // Check if there has been one logic iteration
                for(Percept p : startupPercepts) {
                        if(p.getName().equals("logic")) {
                                assertTrue(p.getParameters().size() == 1);
                                assertFalse(new Numeral(0).equals(p.getParameters().get(0)));
                        }                        
                }
        }
}