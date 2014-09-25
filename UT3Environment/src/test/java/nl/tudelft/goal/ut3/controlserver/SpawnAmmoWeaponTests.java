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
package nl.tudelft.goal.ut3.controlserver;

import static nl.tudelft.goal.ut3.controlserver.AbstractControlTests.utServer;
import cz.cuni.amis.pogamut.ut3.communication.messages.UT3ItemType;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * First we add ammo to the bot and after that we add the weapon,
 *  ammo should be added to the weapon.
 * @author Evers
 */
public class SpawnAmmoWeaponTests extends AbstractControlTests {
        /**
         * Add 8 shock cores and the shock rifle, ammo should be 28.
         */
        @Test
        public void addShockCoreAndShockRifle() throws InterruptedException
        {
                assertTrue(testBot.getWeaponry().getWeapon(UT3ItemType.SHOCK_RIFLE) == null);   // bot doesn't posses weapon already
                assertEquals(0, testBot.getWeaponry().getAmmo(UT3ItemType.SHOCK_RIFLE));
                utServer.addInventory(testBot.getInfo().getId(), UT3ItemType.SHOCK_RIFLE_AMMO.getName());
                Thread.sleep(500);
                assertEquals(8, testBot.getWeaponry().getAmmo(UT3ItemType.SHOCK_RIFLE));
                utServer.addInventory(testBot.getInfo().getId(), UT3ItemType.SHOCK_RIFLE.getName());
                Thread.sleep(500);
                assertEquals(28, testBot.getWeaponry().getAmmo(UT3ItemType.SHOCK_RIFLE));
        }
}
