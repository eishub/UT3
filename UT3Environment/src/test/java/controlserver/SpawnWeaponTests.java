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
package controlserver;

import static controlserver.AbstractControlTests.utServer;
import cz.cuni.amis.pogamut.ut3.communication.messages.UT3ItemType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the control connection by adding all the weapons to a bot through the control connection.
 * @author Evers
 */
public class SpawnWeaponTests extends AbstractControlTests {
        
         /**
         * Give the bot another enforcer, this will double the ammo
         * @throws InterruptedException 
         */
        @Test
        public void addInventoryEnforcer() throws InterruptedException
        {
                assertTrue(testBot.getWeaponry().getWeapon(UT3ItemType.ENFORCER) != null);   // bot does have this weapon
                utServer.addInventory(testBot.getInfo().getId(), UT3ItemType.ENFORCER.getName());
                Thread.sleep(500);
                assertTrue(testBot.getWeaponry().getWeapon(UT3ItemType.ENFORCER) != null);
                assertEquals(100, testBot.getWeaponry().getAmmo(UT3ItemType.ENFORCER));
        }
        
        /**
         * Give the bot a shock rifle and check the ammo.
         * @throws InterruptedException 
         */
        @Test
        public void addInventoryShockRifle() throws InterruptedException
        {
                assertTrue(testBot.getWeaponry().getWeapon(UT3ItemType.SHOCK_RIFLE) == null);   // bot doesn't posses weapon already
                utServer.addInventory(testBot.getInfo().getId(), UT3ItemType.SHOCK_RIFLE.getName());
                Thread.sleep(500);
                assertTrue(testBot.getWeaponry().getWeapon(UT3ItemType.SHOCK_RIFLE) != null);
                assertEquals(20, testBot.getWeaponry().getAmmo(UT3ItemType.SHOCK_RIFLE));
        }
}
