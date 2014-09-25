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
package nl.tudelft.goal.ut2004.visualizer.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nl.tudelft.goal.ut2004.visualizer.controller.ServerController;
import nl.tudelft.goal.ut2004.visualizer.gui.widgets.WaypointBox;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.Respawn;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Player;
import cz.cuni.amis.pogamut.ut2004.communication.worldview.map.Waypoint;
import cz.cuni.amis.pogamut.ut2004.server.IUT2004Server;

/**
 * Interface wrapper for the respawn action. Can be used anywhere an action
 * listener is expected.
 * 
 * Assumes a connection exists.
 * 
 * @author M.P. Korstanje
 * 
 */
public class RespawnAction implements ActionListener {

	private final Player player;
	// TODO: Use listeners to set this
	private final WaypointBox navpoints;

	public RespawnAction(Player player, WaypointBox navpoints) {
		this.player = player;
		this.navpoints = navpoints;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ServerController controller = ServerController.getInstance();
		IUT2004Server server = controller.getServer();
		assert server != null;

		Waypoint waypoint = navpoints.getSelected();
		Location location = null;
		if (waypoint != null) {
			location = waypoint.getLocation();
		}

		Respawn respawn = new Respawn(player.getId(), location, null);
		server.getAct().act(respawn);
	}

}
