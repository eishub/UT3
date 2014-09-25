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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import nl.tudelft.goal.ut2004.visualizer.gui.action.AddNativeBotAction;
import nl.tudelft.goal.ut2004.visualizer.gui.widgets.WaypointBox;
import nl.tudelft.goal.ut2004.visualizer.util.WindowPersistenceHelper;

import cz.cuni.amis.pogamut.unreal.communication.worldview.map.IUnrealWaypoint;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.NavPoint;
import cz.cuni.amis.utils.exception.PogamutException;

/**
 * Dialog for adding an Epic Bot to the server.
 * 
 * @author Lennard de Rijk
 * 
 */
public class AddNativeBotDialog extends JDialog {

	/**
	 * Field for the name of the bot.
	 */
	private JTextField nameField;

	/**
	 * {@link JSpinner} for the difficulty level of the bot.
	 */
	private JSpinner levelSpinner;

	/**
	 * {@link JList} which contains a choice between red or blue team.
	 */
	private JComboBox teamList;

	/**
	 * {@link WaypointBox} for choosing the location where the bot should spawn.
	 */
	private WaypointBox location;

	/**
	 * Button which adds a bot when clicked.
	 */
	private JButton addButton;

	/**
	 * Button which closes this dialog when clicked.
	 */
	private JButton closeButton;

	/**
	 * Helper class to persist this window.
	 */
	private WindowPersistenceHelper persistenceHelper;

	/**
	 * Creates a {@link AddNativeBotDialog} which allows the user to spawn an
	 * Epic Bot.
	 * 
	 * @param parent
	 *            Parent Frame, may be null
	 * @param navPoint
	 * @throws PogamutException
	 *             iff the {@link NavPoint} could not be retrieved from the
	 *             server.
	 */
	public AddNativeBotDialog(Frame parent, IUnrealWaypoint navPoint) {
		super(parent, false);

		setTitle("Add Native Bot");
		setLayout(new FlowLayout());

		add(new JLabel("Name"));
		this.nameField = new JTextField();
		this.nameField.setColumns(15);
		add(nameField);

		// Add a spinner from 1 to 7 with increments of 1
		add(new JLabel("Level"));
		SpinnerNumberModel levelModel = new SpinnerNumberModel(4, 1, 7, 1);
		this.levelSpinner = new JSpinner(levelModel);
		add(levelSpinner);

		add(new JLabel("Team"));
		this.teamList = new JComboBox(new String[] { "Other", "Red", "Blue" });
		this.teamList.setSelectedIndex(0);
		add(teamList);

		location = new WaypointBox();
		location.setSelected(navPoint);
		add(location);

		this.addButton = new JButton("Add Bot");
		addButton.addActionListener(new AddNativeBotAction(nameField, location, levelSpinner, teamList));
		add(addButton);

		setSize(400, 125);

		// Setup persistence
		persistenceHelper = new WindowPersistenceHelper(this);
		persistenceHelper.load();
	}
}
