/*
 * Copyright (C) 2010 Unreal Visualizer Authors
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
package nl.tudelft.goal.visualizer.gui.panels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import nl.tudelft.goal.ut2004.visualizer.controller.ServerController;
import nl.tudelft.goal.ut2004.visualizer.data.EnvironmentData;
import nl.tudelft.goal.ut2004.visualizer.gui.action.AddInventoryAction;
import nl.tudelft.goal.ut2004.visualizer.gui.action.ChangeTeamAction;
import nl.tudelft.goal.ut2004.visualizer.gui.action.KickAction;
import nl.tudelft.goal.ut2004.visualizer.gui.action.RespawnHereAction;
import nl.tudelft.goal.ut2004.visualizer.gui.action.RespawnRandomAction;
import nl.tudelft.goal.ut2004.visualizer.gui.action.SpawnItemAction;
import nl.tudelft.goal.ut2004.visualizer.gui.dialogs.AddNativeBotDialog;
import nl.tudelft.goal.ut2004.visualizer.gui.dialogs.AddUnrealGoalBotDialog;
import nl.tudelft.goal.ut2004.visualizer.map.PureMapTopPanel;
import nl.tudelft.goal.ut2004.visualizer.services.ISelectionHandler;
import nl.tudelft.goal.ut2004.visualizer.util.UnrealActors;
import nl.tudelft.pogamut.base.server.ServerDefinition;
import cz.cuni.amis.pogamut.unreal.communication.messages.gbinfomessages.IPlayer;
import cz.cuni.amis.pogamut.unreal.communication.worldview.map.IUnrealWaypoint;
import cz.cuni.amis.pogamut.unreal.server.IUnrealServer;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Player;
import nl.tudelft.goal.visualizer.ut3.util.UT3MenuBuilder;

/**
 * This is the main panel in the Visualizer. It contains the map and scoreboard
 * overview for the game.
 *
 * @author Lennard de Rijk
 *
 */
public class MapPanel extends JPanel {

    /**
     * Message displayed if no information is available.
     */
    private static final String NO_INFO_MSG = "No information Available, please check connection";
    private static final String[] teamNames = new String[]{"Red", "Blue"};
    private final PopupMenuSelectionHandler popupMenuSelectionHandler;
    private final PureMapTopPanel mapVisualization;

    /**
     * The {@link JTextArea} which is used as drawing board for the scores.
     */
    // private final JTextArea scoreBoard;
    public MapPanel() {
        super();

        this.setLayout(new BorderLayout());

        popupMenuSelectionHandler = new PopupMenuSelectionHandler();

        // Setup map visualisation panel.
        ServerController controller = ServerController.getInstance();
        ServerDefinition<? extends IUnrealServer> serverDefition = controller.getServerDefinition();
        mapVisualization = new PureMapTopPanel((ServerDefinition<IUnrealServer>) serverDefition);
        mapVisualization.setSelectionHandler(popupMenuSelectionHandler);

        this.add(popupMenuSelectionHandler);
        this.add(mapVisualization);
    }

    private class PopupMenuSelectionHandler extends PopupMenu implements ISelectionHandler {

        @Override
        public void selected(Set<Object> selected, Point at, Component origin) {

            this.removeAll();

            if (selected.isEmpty()) {
                return;
            }

            for (Object object : selected) {
                if (object instanceof IPlayer) {
                    addAgentMenu((IPlayer) object);
                } else if (object instanceof IUnrealWaypoint) {
                    addNavPointMenu((IUnrealWaypoint) object, at);
                }
            }

            show(mapVisualization, at.x, at.y);
        }

        private void addNavPointMenu(final IUnrealWaypoint navPoint, final Point at) {
            Menu m = new Menu(navPoint.getID());
            {

                Menu respawn = getRespawnHereMenu(navPoint);
                {

                    if (respawn != null) {
                        m.add(respawn);
                    }
                }
                Menu addBot = new Menu("Add Bot");
                {
                    MenuItem nativeBot = getAddNativeMenu(navPoint, at);
                    addBot.add(nativeBot);
                    MenuItem unrealGoal = getAddUnrealGoalMenu(navPoint, at);
                    if (unrealGoal != null) {
                        addBot.add(unrealGoal);
                    }
                    m.add(addBot);
                }
                m.add(UT3MenuBuilder.buildUT3NavPointMenu(navPoint, at));
                Menu UT2004Menu = new Menu("UT2004");
                UT2004Menu.add(getSpawnItemMenu(navPoint));
                m.add(UT2004Menu);                
            }
            this.add(m);
        }

        private Menu getSpawnItemMenu(final IUnrealWaypoint navPoint) {
            Menu spawnItem = new Menu("Spawn Item");

            for (String id : UnrealActors.INVENTORY_TYPES) {
                MenuItem item = new MenuItem(id);
                item.addActionListener(new SpawnItemAction(navPoint, id));
                spawnItem.add(item);
            }
            return spawnItem;
        }

        private MenuItem getAddUnrealGoalMenu(final IUnrealWaypoint navPoint, final Point at) {

            ServerController controller = ServerController.getInstance();
            EnvironmentData data = controller.getEnvironmentData();

            if (data.getEnvironments().isEmpty()) {
                return null;
            }

            MenuItem unrealGoal = new MenuItem("UnrealGoal");

            unrealGoal.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    AddUnrealGoalBotDialog d = new AddUnrealGoalBotDialog(null, navPoint);
                    d.setLocationRelativeTo(MapPanel.this);
                    d.setLocation(at);
                    d.setVisible(true);

                }
            });
            return unrealGoal;
        }

        private MenuItem getAddNativeMenu(final IUnrealWaypoint navPoint, final Point at) {
            MenuItem nativeBot = new MenuItem("Native");
            nativeBot.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    AddNativeBotDialog d = new AddNativeBotDialog(null, navPoint);
                    d.setLocationRelativeTo(MapPanel.this);
                    d.setLocation(at);
                    d.setVisible(true);
                }
            });
            return nativeBot;
        }

        private Menu getRespawnHereMenu(final IUnrealWaypoint navPoint) {
            ServerController controller = ServerController.getInstance();
            Collection<Player> players = controller.getGameData().getPlayers();

            if (players.isEmpty()) {
                return null;
            }

            Menu respawn = new Menu("Respawn (here)");

            for (Player p : players) {
                MenuItem player = new MenuItem(p.getName());
                player.addActionListener(new RespawnHereAction(p, navPoint));
                respawn.add(player);
            }

            return respawn;

        }

        private void addAgentMenu(IPlayer bot) {

            Menu m = new Menu(bot.getName());
            {
                MenuItem kick = new MenuItem("Kick");
                kick.addActionListener(new KickAction(bot));

                MenuItem change = new MenuItem("Change Team");
                change.addActionListener(new ChangeTeamAction(bot));

                MenuItem respawn = new MenuItem("Respawn (random)");
                respawn.addActionListener(new RespawnRandomAction(bot));
                
                m.add(UT3MenuBuilder.buildUT3AgentMenu(bot));   
            
                // UT3 is superious, in looks, and in code. We reflect this in the code here as well.
                Menu UT2004Menu = new Menu("UT2004");
                Menu inventoryUT2004 = getAddInventoryUT2004Menu(bot);
                UT2004Menu.add(inventoryUT2004);
                m.add(UT2004Menu);
                
                m.add(kick);
                m.add(change);
                m.add(respawn);
            }
            
            this.add(m);
        }

        private Menu getAddInventoryUT2004Menu(IPlayer bot) {
            Menu spawnItem = new Menu("Add Inventory");

            for (String id : UnrealActors.INVENTORY_TYPES) {
                MenuItem item = new MenuItem(id);
                item.addActionListener(new AddInventoryAction(bot, id));
                spawnItem.add(item);
            }
            return spawnItem;
        }
    }
}
