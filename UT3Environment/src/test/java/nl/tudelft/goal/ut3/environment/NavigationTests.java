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
import eis.iilang.Identifier;
import eis.iilang.Percept;
import nl.tudelft.goal.unreal.messages.UnrealIdOrLocation;
import static nl.tudelft.goal.ut3.environment.AbstractEnvironmentTests.testBot;
import static nl.tudelft.goal.ut3.environment.AbstractEnvironmentTests.getPercepts;
import static nl.tudelft.goal.ut3.environment.AbstractEnvironmentTests.testBot;
import org.junit.Test;

import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Evers
 */
public class NavigationTests extends TimeOutEnvironmentTests {
        @Before
        public void beforeTest() throws Exception {
                super.setUpTest();
                testBot.respawn();
        }
        
        /**
         * Very basic navigation test. Runs to the udamage pickup.
         */
        @Test
        public void simpleNavigationTest() throws InterruptedException, PerceiveException {
                testBot.navigate(new UnrealIdOrLocation(UnrealId.get("UTPickupFactory_UDamage_0")));
                boolean navigating = false;

                while (!navigating && !timeout()) {
                        for (Percept p : getPercepts()) {
                                if (p.getName().equals("navigation")) {
                                        if (p.getParameters().get(0).equals(new Identifier("navigating"))) {
                                                assertTrue(p.getParameters().size() == 2);

                                                assertEquals(new Identifier("navigating"), p.getParameters().get(0));
                                                assertEquals(new Identifier("UTPickupFactory_UDamage_0"), p.getParameters().get(1));
                                                navigating = true;
                                        }
                                }
                        }
                }
                assertTrue(navigating);
        }
        
        /**
         * Reached test
         */
        @Test
        public void testReachedNavpoint() throws InterruptedException, PerceiveException {
                testBot.navigate(new UnrealIdOrLocation(UnrealId.get("UTPickupFactory_UDamage_0")));
                boolean reached = false;

                while (!reached && !timeout()) {
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
}