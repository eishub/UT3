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
import junit.framework.Assert;
import nl.tudelft.goal.unreal.messages.UnrealIdOrLocation;
import static nl.tudelft.goal.ut3.environment.AbstractEnvironmentTests.getPercepts;
import static nl.tudelft.goal.ut3.environment.AbstractEnvironmentTests.testBot;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Evers
 */
public class StopTests extends TimeOutEnvironmentTests {

        @After
        public void tearDown() {
        }

        @Test
        public void simpleNavigationStopTest() throws InterruptedException, PerceiveException {
                testBot.navigate(new UnrealIdOrLocation(UnrealId.get("UTPickupFactory_UDamage_0")));
                Thread.sleep(300);
                testBot.stop();
                boolean stopped = false;

                while (!stopped && !timeout()) {
                        for (Percept p : getPercepts()) {
                                if (p.getName().equals("navigation")) {
                                        if (p.getParameters().get(0).equals(new Identifier("waiting"))) {
                                                assertEquals(new Identifier("none"), p.getParameters().get(1));
                                                stopped = true;
                                        }
                                }
                        }
                }
                assertTrue(stopped);
        }

        /**
         * Call stop from a state in which the bot is navigating(reached,_).
         *
         * @throws InterruptedException
         * @throws PerceiveException
         */
        @Test
        public void stopFromReachedTest() throws InterruptedException, PerceiveException {
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

                testBot.stop();
                Thread.sleep(1000);

                boolean stopped = false;
                for (Percept p : getPercepts()) {
                        if (p.getName().equals("navigation")) {
                                assertTrue(p.getParameters().size() == 2);
                                assertEquals(new Identifier("waiting"), p.getParameters().get(0));
                                assertEquals(new Identifier("none"), p.getParameters().get(1));
                                stopped = true;
                        }
                }
                assertTrue(stopped);
        }
        /**
         * Call stop from a state where the bot is in navigating(nopath,none).
         *
         * @throws InterruptedException
         * @throws PerceiveException
         */
        @Test
        public void stopFromNoPathTest() throws InterruptedException, PerceiveException {
                testBot.navigate(new UnrealIdOrLocation(UnrealId.get("PathNode_21")));
                boolean noPath = false;

                while (!noPath && !timeout()) {
                        for (Percept p : getPercepts()) {
                                if (p.getName().equals("navigation")) {
                                        if (p.getParameters().get(0).equals(new Identifier("noPath"))) {
                                                noPath = true;
                                                // Exit loop.

                                                break;
                                        }
                                }
                        }
                }

                assertTrue(noPath);
                testBot.stop();
                Thread.sleep(1000);

                boolean stopped = false;
                for (Percept p : getPercepts()) {
                        if (p.getName().equals("navigation")) {
                                assertTrue(p.getParameters().size() == 2);

                                assertEquals(new Identifier("waiting"), p.getParameters().get(0));
                                assertEquals(new Identifier("none"), p.getParameters().get(1));
                                stopped = true;
                        }
                }
                assertTrue(stopped);
        }

        /**
         * Stop action performed after a stuck event.
         *
         * @throws InterruptedException
         * @throws PerceiveException
         */
        @Test
        public void stopFromStuckTest() throws InterruptedException, PerceiveException {
                testBot.navigate(new UnrealIdOrLocation(UnrealId.get("PathNode_22")));
                boolean stuck = false;

                while (!stuck && !timeout()) {
                        for (Percept p : getPercepts()) {
                                if (p.getName().equals("navigation")) {
                                        if (p.getParameters().get(0).equals(new Identifier("stuck"))) {
                                                stuck = true;
                                        }
                                }
                        }
                }

                assertTrue(stuck);
                testBot.stop();
                Thread.sleep(1000);

                boolean stopped = false;
                for (Percept p : getPercepts()) {
                        if (p.getName().equals("navigation")) {
                                assertTrue(p.getParameters().size() == 2);

                                assertEquals(new Identifier("waiting"), p.getParameters().get(0));
                                assertEquals(new Identifier("none"), p.getParameters().get(1));
                                stopped = true;
                        }
                }
                assertTrue(stopped);
        }
}