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
import java.util.Collection;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import nl.tudelft.goal.ut2004.util.Team;
import nl.tudelft.goal.ut2004.visualizer.connection.EnvironmentService;
import nl.tudelft.goal.ut2004.visualizer.controller.ServerController;
import nl.tudelft.goal.ut2004.visualizer.data.EnvironmentData;
import nl.tudelft.goal.ut2004.visualizer.gui.action.AddUnrealGoalBotAction;
import nl.tudelft.goal.ut2004.visualizer.gui.widgets.WaypointBox;
import nl.tudelft.goal.ut2004.visualizer.util.CollectionEventAdaptor;
import nl.tudelft.goal.ut2004.visualizer.util.SelectableEnvironment;
import nl.tudelft.goal.ut2004.visualizer.util.WindowPersistenceHelper;
import cz.cuni.amis.pogamut.unreal.communication.worldview.map.IUnrealWaypoint;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.NavPoint;
import cz.cuni.amis.utils.collections.ObservableSet;
import cz.cuni.amis.utils.exception.PogamutException;

/**
 * Dialog for adding an Epic Bot to the server.
 * 
 * @author Lennard de Rijk
 * 
 */
public class AddUnrealGoalBotDialog extends JDialog {

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

	private JComboBox environmentList;

	/**
	 * Helper class to persist this window.
	 */
	private WindowPersistenceHelper persistenceHelper;
	
	/**
	 * Creates a {@link AddUnrealGoalBotDialog} which allows the user to spawn
	 * an Epic Bot.
	 * 
	 * @param parent
	 *            Parent Frame, may be null
	 * @param navPoint
	 * @throws PogamutException
	 *             iff the {@link NavPoint} could not be retrieved from the
	 *             server.
	 */
	public AddUnrealGoalBotDialog(Frame parent, IUnrealWaypoint navPoint) {
		super(parent, false);

		setTitle("Add UnrealGoal bot");
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
		this.teamList = new JComboBox(Team.values());
		this.teamList.setSelectedIndex(0);
		add(teamList);

		location = new WaypointBox();
		if (navPoint != null)
			location.setSelected(navPoint);
		add(location);

		ServerController controller = ServerController.getInstance();
		EnvironmentData data = controller.getEnvironmentData();
		ObservableSet<EnvironmentService> clients = data.getEnvironments();
		Collection<SelectableEnvironment> s = SelectableEnvironment
				.fromCollection(clients);
		this.environmentList = new JComboBox(s.toArray());
		clients.addCollectionListener(new CollectionEventAdaptor<EnvironmentService>() {

			@Override
			public void postAddEvent(
					Collection<EnvironmentService> alreadyAdded,
					final Collection<EnvironmentService> whereWereAdded) {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						Collection<SelectableEnvironment> s = SelectableEnvironment
								.fromCollection(whereWereAdded);
						environmentList.setModel(new DefaultComboBoxModel(s
								.toArray()));
					}
				});
			}

			@Override
			public void postRemoveEvent(
					Collection<EnvironmentService> alreadyRemoved,
					final Collection<EnvironmentService> whereWereRemoved) {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						Collection<SelectableEnvironment> s = SelectableEnvironment
								.fromCollection(whereWereRemoved);
						environmentList.setModel(new DefaultComboBoxModel(s
								.toArray()));
					}
				});
			}
		});

		add(environmentList);

		this.addButton = new JButton("Add Bot");
		addButton.addActionListener(new AddUnrealGoalBotAction(nameField,
				location, levelSpinner, teamList, environmentList));
		add(addButton);

		setSize(400, 225);
		// Setup persistence
		persistenceHelper = new WindowPersistenceHelper(this);
		persistenceHelper.load();
	}
}
