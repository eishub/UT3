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

import java.util.LinkedList;

import nl.tudelft.goal.ut2004.visualizer.util.ObservingCollection;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.MapList;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Player;
import cz.cuni.amis.pogamut.ut2004.communication.worldview.map.Waypoint;
import cz.cuni.amis.pogamut.ut2004.server.IUT2004Server;
import cz.cuni.amis.utils.collections.ObservableCollection;

/**
 * Class that keeps track of the players that are currently in the server.
 * 
 * @author Lennard de Rijk
 * @author M.P. Korstanje
 * 
 */
public class GameData extends AbstractData {

	private final ObservingCollection<Player> players;
	private final ObservableCollection<Waypoint> waypoints;
	private final ObservableCollection<MapList> maps;
	
    /** Most rescent message containing info about the player's score. */
	//Map<UnrealId, PlayerScore> lastPlayerScore = null;

	/** Most rescent message containing info about the player team's score. */
	//Map<Integer, TeamScore> lastTeamScore = null;
	

	public GameData() {
		players = new ObservingCollection<Player>();
		waypoints = new ObservableCollection<Waypoint>(new LinkedList<Waypoint>());
		maps = new ObservableCollection<MapList>(new LinkedList<MapList>());
		
	}

	@Override
	public void serverChanged(IUT2004Server server) {
		players.removeObserved();
		waypoints.clear();
		maps.clear();
//		lastPlayerScore.clear();
//		lastTeamScore.clear();
		
		if (server != null) {
			players.setObserved(server.getPlayers());
			waypoints.addAll(server.getMap().vertexSet());
			maps.addAll(server.getAvailableMaps());
			
		}
	}

	public ObservableCollection<MapList> getAvailableMaps() {
		return maps;
	}

	public ObservableCollection<Player> getPlayers() {
		return players;
	}

	public ObservableCollection<Waypoint> getWaypoints() {
		return waypoints;
	}


	
	
//	/**
//	 * PlayerScore listener.
//	 */
//	private class PlayerScoreListener implements IWorldEventListener<PlayerScore>
//	{
//		@Override
//		public void notify(PlayerScore event)
//		{
//			synchronized(lastPlayerScore) {
//				lastPlayerScore.put(event.getId(), event);
//			}
//		}
//
//		/**
//		 * Constructor. Registers itself on the given WorldView object.
//		 * @param worldView WorldView object to listent to.
//		 */
//		public PlayerScoreListener(IWorldView worldView)
//		{
//			worldView.addEventListener(PlayerScore.class, this);
//		}
//	}
//
//	/** PlayerScore listener */
//	private PlayerScoreListener playerScoreListener;
//
//	/*========================================================================*/
//	
//	/**
//	 * TeamScore listener.
//	 */
//	private class TeamScoreListener implements IWorldObjectEventListener<TeamScore, WorldObjectUpdatedEvent<TeamScore>>
//	{
//		/**
//		 * Constructor. Registers itself on the given WorldView object.
//		 * @param worldView WorldView object to listent to.
//		 */
//		public TeamScoreListener(IWorldView worldView)
//		{
//			worldView.addObjectListener(TeamScore.class, WorldObjectUpdatedEvent.class, this);
//		}
//
//		@Override
//		public void notify(WorldObjectUpdatedEvent<TeamScore> event) {
//			synchronized(lastTeamScore) {
//				lastTeamScore.put(event.getObject().getTeam(), event.getObject());
//			}
//		}
//	}
//
//	/** TeamScore listener */
//	private TeamScoreListener teamScoreListener;

}
