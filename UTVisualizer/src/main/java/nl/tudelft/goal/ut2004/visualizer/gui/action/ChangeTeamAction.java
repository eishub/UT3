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

import cz.cuni.amis.pogamut.unreal.communication.messages.gbinfomessages.IPlayer;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.ChangeTeam;
import cz.cuni.amis.pogamut.ut2004.server.IUT2004Server;

public class ChangeTeamAction implements ActionListener {

	private final IPlayer player;

	public ChangeTeamAction(IPlayer bot) {
		this.player = bot;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		ServerController controller = ServerController.getInstance();
		IUT2004Server server = controller.getServer();
		assert server != null;

		int team = player.getTeam();
		if (team != 0 || team != 1) {
			team = (int) Math.round(Math.random());
		}

		ChangeTeam change = new ChangeTeam(player.getId(), team);
		server.getAct().act(change);

	}
}
