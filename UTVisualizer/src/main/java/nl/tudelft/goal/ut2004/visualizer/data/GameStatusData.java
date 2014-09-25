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
package nl.tudelft.goal.ut2004.visualizer.data;

import java.util.Collection;
import java.util.Map;

import cz.cuni.amis.pogamut.base.communication.worldview.IWorldView;
import cz.cuni.amis.pogamut.base.communication.worldview.object.WorldObjectId;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.FlagInfo;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.GameInfo;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.TeamScore;
import cz.cuni.amis.pogamut.ut2004.server.IUT2004Server;

/**
 * 
 * {@link AbstractData} object that keeps track of the objects that refer to the status of the game. More precisely stores the latest {@link GameStatus} and
 * {@link FlagInfo} objects.
 * 
 * @author Lennard de Rijk
 * 
 */
public class GameStatusData extends AbstractData {

	/**
	 * Total number of teams we can store {@link FlagInfo} for.
	 */
	private static final int NUMBER_OF_TEAMS = 2;

	/**
	 * Last {@link GameStatus} we have received
	 */
	private GameInfo gameInfo;

	/**
	 * Contains for each team the last {@link FlagInfo} object we have received
	 */
	private Collection<FlagInfo> flagInfoCollection;

	private Collection<TeamScore> teamScoreCollection;

	public GameStatusData() {
		super();
	}

	/**
	 * Listener for the server.
	 * 
	 * May be called from another thread.
	 */
	@Override
	public synchronized void serverChanged(IUT2004Server server) {
		if (server != null) {
			updateGameInfo(server.getWorldView());
			updateFlagInfo(server.getWorldView());
			updateTeamScore(server.getWorldView());
		} else {
			clearGameInfo();
			clearFlagInfo();
			clearTeamScore();
		}

		notifyListeners();
	}

	private void clearTeamScore() {
		teamScoreCollection = null;
	}

	private void updateTeamScore(IWorldView view) {
		Class<TeamScore> teamScoreClass = TeamScore.class;
		teamScoreCollection = view.getAll(teamScoreClass).values();
	}

	/**
	 * Clears the {@link FlagInfo} from the game status.
	 */
	private void clearFlagInfo() {
		flagInfoCollection = null;
	}

	/**
	 * Clears the {@link GameInfo} from the game status.
	 */
	private void clearGameInfo() {
		gameInfo = null;
	}

	/**
	 * Updates the current game status.
	 * 
	 * @param server
	 *            the latest {@link GameStatus} object
	 */
	private void updateGameInfo(IWorldView view) {
		this.gameInfo = view.getSingle(GameInfo.class);
	}

	/**
	 * Updates the current information about a flag.
	 * 
	 * @param flagInfo
	 *            the {@link FlagInfo} object to store
	 */
	private void updateFlagInfo(IWorldView view) {
		Map<WorldObjectId, FlagInfo> flags = view.getAll(FlagInfo.class);
		flagInfoCollection = flags.values();
	}

	/**
	 * 
	 * @return The most recent {@link GameStatus}
	 */
	public synchronized GameInfo getGameInfo() {
		return gameInfo;
	}

	/**
	 * @return the flagInfoCollection
	 */
	public synchronized Collection<FlagInfo> getFlagInfoCollection() {
		return flagInfoCollection;
	}

	/**
	 * 
	 * @return the teamScoreCollection
	 */
	public synchronized Collection<TeamScore> getTeamScoreCollection() {
		return teamScoreCollection;
	}

}
