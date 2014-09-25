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
package nl.tudelft.goal.visualizer.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import nl.tudelft.goal.unreal.util.vecmathcheck.VecmathCheck;
import nl.tudelft.goal.ut2004.visualizer.controller.ServerController;
import nl.tudelft.goal.ut2004.visualizer.gui.action.PauseResumeAction;
import nl.tudelft.goal.ut2004.visualizer.gui.action.ShowDialogueAction;
import nl.tudelft.goal.ut2004.visualizer.gui.action.ShowServerDependentDialogueAction;
import nl.tudelft.goal.ut2004.visualizer.gui.action.ShowServerEnvironmentDependentDialogueAction;
import nl.tudelft.goal.ut2004.visualizer.gui.dialogs.AddNativeBotDialog;
import nl.tudelft.goal.ut2004.visualizer.gui.dialogs.AddUnrealGoalBotDialog;
import nl.tudelft.goal.ut2004.visualizer.gui.dialogs.ChangeGameSpeedDialog;
import nl.tudelft.goal.ut2004.visualizer.gui.dialogs.ChangeMapDialog;
import nl.tudelft.goal.ut2004.visualizer.gui.dialogs.ListEnvironmentsDialog;
import nl.tudelft.goal.ut2004.visualizer.gui.dialogs.ListPlayerDialog;
import nl.tudelft.goal.ut2004.visualizer.gui.dialogs.ServerConnectionDialog;
import nl.tudelft.goal.ut2004.visualizer.gui.dialogs.SettingsDialog;
import nl.tudelft.goal.visualizer.gui.panels.MapPanel;
import nl.tudelft.goal.ut2004.visualizer.util.WindowPersistenceHelper;
import nl.tudelft.pogamut.base.server.ReconnectingServerDefinition;
import nl.tudelft.pogamut.base.server.ServerDefinition;
import cz.cuni.amis.pogamut.ut2004.server.IUT2004Server;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * This is the main class to start the UnrealVisualizer application. The
 * UnrealVisualizer is started by calling the constructor for this class with an
 * {@link UTServer} instance. All messaging from and to the server will be
 * handled by the application itself.
 * 
 * Agents in Pogamut will be known as Bots inside the GUI.
 * 
 * @author Lennard de Rijk
 * @author M.P. Korstanje
 * 
 */
public class UnrealVisualizerGUI extends JFrame {

	/**
	 * The width of the application window.
	 */
	private static final int WINDOW_WIDTH = 800;
	/**
	 * The height of the application window.
	 */
	private static final int WINDOW_HEIGHT = 600;

	/**
	 * The {@link MapPanel} in use by the {@link UnrealVisualizerGUI}.
	 */
	private final MapPanel mapPanel;
	
	/**
	 * Helper class to persist this window.
	 */
	private WindowPersistenceHelper persistenceHelper;

	public UnrealVisualizerGUI() {
		super();
		
		// Create the controllers
		ServerController.createNewController();
		ServerController controller = ServerController.getInstance();
		ServerDefinition<IUT2004Server> serverDefinition = controller
				.getServerDefinition();
		
		// Set up the main window properties
		setTitle("Unreal Tournament Visualizer for GOAL");
		setResizable(true);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Setup persistence
		persistenceHelper = new WindowPersistenceHelper(this);
		persistenceHelper.load();
		
		// Set up the initial menu bar at the top
		setupMenuBar();

		// Instantiate the MapPanel and add it to the tabbed pane
		mapPanel = new MapPanel();
   
		// Instantiate the mainPanel that contains our tabbed pane
		JPanel mainPanel = new JPanel(new GridLayout(1, 1));
		mainPanel.add(mapPanel);
		add(mainPanel);
	}
        
        @Override
        public Dimension getPreferredSize() {
                return new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);
        }
	

	/**
	 * Setups the {@link JMenuBar} that is shown at the top of the screen.
	 */
	private void setupMenuBar() {
		// This is the main bar that goes on top of the screen
		JMenuBar menuBar = new JMenuBar();
		menuBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		setJMenuBar(menuBar);

		// Level 0 menu for the application itself. Contains for instance Exit
		// entry.
		JMenu applicationMenu = new JMenu("Visualizer");
		{
			// Add settings menu to Visualizer entry
			SettingsDialog settingsDialogue = new SettingsDialog(this);
			String name = "Settings";
			String description = "Change the settings of the visualizer.";
			ShowDialogueAction showSettings = new ShowDialogueAction(settingsDialogue, name, description);
			JMenuItem settings = new JMenuItem(showSettings);
			applicationMenu.add(settings);
			
			// Add Exit menu item to Visualizer entry
			JMenuItem exitVis = new JMenuItem("Exit", KeyEvent.VK_E);
			exitVis.setToolTipText("Exit the Visualizer");
			exitVis.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Close the application
					dispose();
					System.exit(0);
				}
			});
			applicationMenu.add(exitVis);
			
		
		}
		menuBar.add(applicationMenu);

		// Pause resume item
		JMenu server = new JMenu("Server");
		{
			{
				ServerController controller = ServerController.getInstance();
				ServerDefinition<IUT2004Server> serverDefinition = controller
						.getServerDefinition();
				ServerConnectionDialog connectionDialog = new ServerConnectionDialog(
						this, (ReconnectingServerDefinition) serverDefinition);
				String name = "Connection";
				String description = "Connect to an Unreal Tournament Server.";
				ShowDialogueAction action = new ShowDialogueAction(
						connectionDialog, name, description);
				JMenuItem connect = new JMenuItem(action);
				server.add(connect);
			}

			JMenuItem pauseResume = new JMenuItem(new PauseResumeAction());
			server.add(pauseResume);
			{
				ChangeGameSpeedDialog gameSpeedDialog = new ChangeGameSpeedDialog(
						this);
				String name = "Game Speed";
				String description = "Change the speed of the game.";
				ShowServerDependentDialogueAction action = new ShowServerDependentDialogueAction(
						gameSpeedDialog, name, description);
				JMenuItem speed = new JMenuItem(action);
				server.add(speed);
			}

			{
				ChangeMapDialog changeMapDialog = new ChangeMapDialog(this);
				String name = "Change Map";
				String description = "Change the current map.";
				ShowServerDependentDialogueAction action = new ShowServerDependentDialogueAction(
						changeMapDialog, name, description);
				JMenuItem change = new JMenuItem(action);
				server.add(change);
			}
			{
				AddNativeBotDialog addNativeBotDialog = new AddNativeBotDialog(
						this, null);
				String name = "Add Native Bot";
				String description = "Adds a native unreal bot to the game.";
				ShowServerDependentDialogueAction action = new ShowServerDependentDialogueAction(
						addNativeBotDialog, name, description);
				JMenuItem add = new JMenuItem(action);
				server.add(add);
			}
			{
				AddUnrealGoalBotDialog addUnrealGoalBotDialog = new AddUnrealGoalBotDialog(
						this, null);
				String name = "Add UnrealGoal Bot";
				String description = "Adds an UnrealGoal bot to the game.";
				ShowServerEnvironmentDependentDialogueAction action = new ShowServerEnvironmentDependentDialogueAction(
						addUnrealGoalBotDialog, name, description);
				JMenuItem add = new JMenuItem(action);
				server.add(add);
			}
			{
				ListPlayerDialog listPlayerDialog = new ListPlayerDialog(this);
				String name = "List Players";
				String description = "Lists all player in the game.";
				ShowServerDependentDialogueAction action = new ShowServerDependentDialogueAction(
						listPlayerDialog, name, description);
				JMenuItem add = new JMenuItem(action);
				server.add(add);
			}
		}

		menuBar.add(server);

		JMenu environments = new JMenu("Environments");
		{
			String name = "List Environments";
			String description = "List Goal Environments connected to the visualizier.";
			ListEnvironmentsDialog listDialog = new ListEnvironmentsDialog(this);
			ShowDialogueAction action = new ShowDialogueAction(listDialog,
					name, description);
			JMenuItem list = new JMenuItem(action);

			environments.add(list);
		}
		menuBar.add(environments);

	}

	/**
	 * The interface is being closed. We will "try to" dispose all resources in
	 * use.
	 */
	@Override
	public void dispose() {
		super.dispose();

		// The user interface has been disposed, dispose of all other
		// resources.
		ServerController.disposeController();
	}

	/**
	 * Main method for testing purposes only since we need to launch with an
	 * active {@link UTServer} instance
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (!VecmathCheck.check()) {
			JOptionPane.showMessageDialog(null, VecmathCheck.getErrorMessage(),"Version Conflict",JOptionPane.ERROR_MESSAGE);
			
			return;
		}
		
		
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				UnrealVisualizerGUI vizualiser = new UnrealVisualizerGUI();
				vizualiser.setVisible(true);
			}
		});
	}

}
