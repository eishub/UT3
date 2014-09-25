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

import org.junit.After;
import org.junit.Before;

/**
 *
 * @author Evers
 */
public class TimeOutEnvironmentTests extends AbstractEnvironmentTests {
        private static long startTime;
        // 10000ms timeout.
        public static final long TIMEOUT = 10000;

        @Before
        public void setUpTest() throws Exception {
                Thread.sleep(150);
                initializeTimeout();
        }
                
        @After
        public void tearDown() {
        }        
        
        /**
         * Sets initializes the timeout function.
         */
        public void initializeTimeout() {
                startTime = System.currentTimeMillis();
        }

        /**
         * Checks if function should timeout.
         *
         * @return True if function should timeout, false otherwise.
         *
         * TODO Use {@link Heatup} here.
         */
        public boolean timeout() {
                return System.currentTimeMillis() - startTime > TIMEOUT ? true : false;
        }
}
