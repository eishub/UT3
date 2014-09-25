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
package nl.tudelft.goal.ut2004.visualizer.gui.dialogs;

import java.awt.Frame;
import javax.swing.JDialog;
import nl.tudelft.goal.ut2004.visualizer.panels.connection.ServerConnectionPanel;
import nl.tudelft.goal.ut2004.visualizer.util.WindowPersistenceHelper;
import nl.tudelft.pogamut.base.server.ReconnectingServerDefinition;

/**
 * Dialog to connect to the server.
 * 
 * @author M.P. Korstanje
 * 
 */
public class ServerConnectionDialog extends JDialog {

	/**
	 * Helper class to persist this window.
	 */
	private WindowPersistenceHelper persistenceHelper;
	
	public ServerConnectionDialog(Frame parent,
			ReconnectingServerDefinition serverDefinition) {
		super(parent, false);

		setTitle("Server Connection");

		this.add(new ServerConnectionPanel(serverDefinition));
		
		this.setSize(400, 75);
		// Setup persistence
		persistenceHelper = new WindowPersistenceHelper(this);
		persistenceHelper.load();

	}
}
