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
package nl.tudelft.goal.ut2004.visualizer.controller;


import nl.tudelft.goal.ut2004.visualizer.data.EnvironmentData;
import nl.tudelft.goal.ut2004.visualizer.data.GameData;
import nl.tudelft.goal.ut2004.visualizer.data.GameStatusData;
import nl.tudelft.pogamut.base.server.ReconnectingServerDefinition;
import nl.tudelft.pogamut.base.server.ServerDefinition;
import nl.tudelft.pogamut.ut2004.server.UTServerDefinition;
import cz.cuni.amis.pogamut.ut2004.server.IUT2004Server;
import cz.cuni.amis.utils.flag.FlagListener;

/**
 * 
 * s all the gathering of data for the basic functionality of the visualizer. It stores for instance information on each player in {@link GameData}. It is also
 * the access for the application to get access to subscribe to {@link AbstractData} and retrieve the {@link UTServer} if that is necessary.
 * 
 * @author Lennard de Rijk
 * @author M.P. Korstanje
 * 
 */
public class ServerController {

	/**
	 * The {@link UTServer} this {@link ServerController} controls.
	 */
	private final ServerDefinition<IUT2004Server> serverDefinition;

	/**
	 * The collection of data we have on the players in the server.
	 */
	private final GameData gameData;

	/**
	 * The {@link AbstractData} that contains the latest {@link GameStatus}.
	 */
	private final GameStatusData gameStatus;

	private final EnvironmentData environmentData;

	private ServerController() {

		UTServerDefinition utServerDefinition = new UTServerDefinition();
		this.serverDefinition = new ReconnectingServerDefinition<IUT2004Server>(
				utServerDefinition);

		this.gameData = new GameData();
		this.gameStatus = new GameStatusData();
		this.environmentData = new EnvironmentData();
		this.environmentData.connect();
		// Hook up the listeners to the server
		addServerListener();

		// Init data modules.
		init();
	}

	/**
	 * Listener for the server status.
	 * 
	 * Synchronized as events happen in another thread.
	 * 
	 */
	private void addServerListener() {
		this.serverDefinition.getServerFlag().addListener(
				new FlagListener<IUT2004Server>() {
					@Override
					public synchronized void flagChanged(IUT2004Server server) {
						// Notify data components that server has changed.
						gameData.serverChanged(server);
						gameStatus.serverChanged(server);
					}
				});
	}

	/**
	 * Initializes the ServerController with data that is already present in the UTServer.
	 */
	private void init() {
		// Notify data components that server is already in place.
		IUT2004Server server = serverDefinition.getServerFlag().getFlag();
		if (server != null) {
			gameData.serverChanged(server);
			gameStatus.serverChanged(server);
		}
	}

	/**
	 * @return The collection of data we have on all the players
	 */
	public GameData getGameData() {
		return gameData;
	}

	/**
	 * @return the {@link GameStatusData} instance that keeps track of the GameStatus
	 */
	public GameStatusData getGameStatus() {
		return gameStatus;
	}

	/**
	 * @return the {@link WorldServerDefinition} this controllers holds.
	 */
	public ServerDefinition<IUT2004Server> getServerDefinition() {
		return serverDefinition;
	}

	/**
	 * Returns the {@link IUT2004Server} managed by this controller {@link WorldServerDefinition}.
	 * 
	 * Or null if no server is running.
	 * 
	 * @return null or the server.
	 * 
	 * 
	 */
	public IUT2004Server getServer() {
		return serverDefinition.getServerFlag().getFlag();
	}

	public EnvironmentData getEnvironmentData() {
		return environmentData;
	}

	/**
	 * Disposes the {@link ServerController} instance.
	 */
	private void dispose() {
		this.serverDefinition.stopServer();
	}

	/**
	 * The singleton instance of the ServerController
	 */
	private static ServerController singleton;

	/**
	 * Creates an new {@link ServerController}, disposes the old one iff exists.
	 * 
	 * @param server
	 *            The UTServer used by the {@link ServerController}
	 */
	public static void createNewController() {
		disposeController();
		singleton = new ServerController();
	}

	/**
	 * Disposes the current {@link ServerController} if it exists.
	 */
	public static void disposeController() {
		if (singleton != null) {
			singleton.dispose();
			singleton = null;
		}
	}

	/**
	 * 
	 * @return the singleton instance of the {@link ServerController}
	 */
	public static ServerController getInstance() {
		return singleton;
	}

}
