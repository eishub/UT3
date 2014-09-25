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
package nl.tudelft.goal.visualizer.ut3.util;

import cz.cuni.amis.pogamut.unreal.communication.messages.gbinfomessages.IPlayer;
import cz.cuni.amis.pogamut.unreal.communication.worldview.map.IUnrealWaypoint;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.Point;
import nl.tudelft.goal.ut2004.visualizer.gui.action.AddInventoryAction;
import nl.tudelft.goal.ut2004.visualizer.gui.action.SpawnItemAction;

/**
 *
 * @author Michiel Hegemans
 */
public class UT3MenuBuilder {
    /**
     * Creates an UT3 specific menu for the visualizer.
     * 
     * @param navPoint  navPoint where to perform the action.
     * @param at        Position where to show menu.
     * 
     * @return          A menu to control UT3 bots.
     */
    public static Menu buildUT3NavPointMenu(final IUnrealWaypoint navPoint, final Point at) {
        Menu UT3Menu = buildUT3Menu();
        UT3Menu.add(buildUTObjectMenu(navPoint, "Weapons", Unreal3Actors.WEAPON_TYPES));
        UT3Menu.add(buildUTObjectMenu(navPoint, "Deployables", Unreal3Actors.DEPLOYABLE_TYPES));
        UT3Menu.add(buildUTObjectMenu(navPoint, "Ammo", Unreal3Actors.AMMO_TYPES));
        UT3Menu.add(buildUTObjectMenu(navPoint, "Pickups", Unreal3Actors.PICKUP_TYPES));        
        // TODO: Not yet implemented.
        // UT3Menu.add(buildUTObjectMenu(navPoint, "Deployed", Unreal3Actors.DEPLOYED_TYPES));        
        UT3Menu.add(buildUTObjectMenu(navPoint, "Vehicles", Unreal3Actors.VEHICLE_TYPES));               
        
        return UT3Menu;
    }
    
    /**
     * Creates and agent menu that allows user to add items to bot.
     * @param bot Target bot.
     * @return Menu with options to add.
     */
    public static Menu buildUT3AgentMenu(final IPlayer bot) {
        Menu UT3Menu = buildUT3Menu();
        
        UT3Menu.add(buildUTObjectInventoryMenu(bot, "Weapons", Unreal3Actors.WEAPON_TYPES));
        UT3Menu.add(buildUTObjectInventoryMenu(bot, "Deployables", Unreal3Actors.DEPLOYABLE_TYPES));
        UT3Menu.add(buildUTObjectInventoryMenu(bot, "Ammo", Unreal3Actors.AMMO_TYPES));
        UT3Menu.add(buildUTObjectInventoryMenu(bot, "Pickups", Unreal3Actors.PICKUP_TYPES));
        
        return UT3Menu;
    }
    
    /**
     * Creates UT3 menu item.
     * @return Empty menu with name UT3.
     */
    private static Menu buildUT3Menu() {
        return new Menu("UT3");
    }
    
    private static Menu buildUTObjectInventoryMenu(final IPlayer bot, String menuName, String[] UTObjects) {
        Menu addUTObject = new Menu(menuName);
        
        for (String id : UTObjects) {
            MenuItem UTObject = new MenuItem(id);
            UTObject.addActionListener(new AddInventoryAction(bot, id));
            addUTObject.add(UTObject);
        }
        
        return addUTObject;
    }
    
    /**
     * Creates a menu for specific UT object types.
     * 
     * @param navPoint  Location where to spawn item.
     * @param menuName  Name of the menu.
     * @param UTObjects List of object types.
     * 
     * @return A list of items that can spawn on the navPoint.
     */
    private static Menu buildUTObjectMenu(final IUnrealWaypoint navPoint, String menuName, String[] UTObjects) {
        Menu spawnUTObject = new Menu(menuName);
        
        for (String id : UTObjects) {
            MenuItem UTObject = new MenuItem(id);
            UTObject.addActionListener(new SpawnItemAction(navPoint, id));
            spawnUTObject.add(UTObject);
        }
        
        return spawnUTObject;
    }
}
