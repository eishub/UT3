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

import java.awt.FlowLayout;
import java.awt.Frame;
import javax.swing.JButton;
import javax.swing.JDialog;
import nl.tudelft.goal.ut2004.visualizer.gui.action.ChangeMapAction;
import nl.tudelft.goal.ut2004.visualizer.gui.widgets.MapBox;
import nl.tudelft.goal.ut2004.visualizer.util.WindowPersistenceHelper;

/**
 * Dialog for changing speed of the game.
 * 
 * @author M.P. Korstanje
 * 
 */
public class ChangeMapDialog extends JDialog {


	private MapBox mapSelection;
	private JButton changeMap;
	/**
	 * Helper class to persist this window.
	 */
	private WindowPersistenceHelper persistenceHelper;
	
	public ChangeMapDialog(Frame parent) {
		super(parent, false);

		setTitle("Change Map");
		
		this.mapSelection = new MapBox();
		this.changeMap = new JButton("Change map");
		this.changeMap.addActionListener(new ChangeMapAction(mapSelection));

		setLayout(new FlowLayout());
		add(mapSelection);
		add(changeMap);
		
		this.setSize(400, 75);
		// Setup persistence
		persistenceHelper = new WindowPersistenceHelper(this);
		persistenceHelper.load();

	}
}
