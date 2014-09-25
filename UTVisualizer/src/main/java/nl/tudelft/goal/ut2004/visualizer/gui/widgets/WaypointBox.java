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
package nl.tudelft.goal.ut2004.visualizer.gui.widgets;

import java.util.Collection;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import nl.tudelft.goal.ut2004.visualizer.controller.ServerController;
import nl.tudelft.goal.ut2004.visualizer.data.GameData;
import nl.tudelft.goal.ut2004.visualizer.util.CollectionEventAdaptor;
import nl.tudelft.goal.ut2004.visualizer.util.SelectableWayPoint;

import cz.cuni.amis.pogamut.ut2004.communication.worldview.map.Waypoint;
import cz.cuni.amis.utils.collections.ObservableCollection;

/**
 * Smart Navpoint selection box. Can only be used when there is a connection.
 * 
 * @author M.P. Korstanje
 * 
 */
public class WaypointBox extends JPanel {

	private final SuggestionField suggestionField;
	private final Listener listener;
	private final ObservableCollection<Waypoint> waypoints;
	private final SuggestionModel suggestionModel;

	public WaypointBox() {

		this.listener = new Listener();
		this.suggestionModel = new SuggestionModel();
		this.suggestionField = new SuggestionField(suggestionModel);
		this.add(suggestionField);

		ServerController controller = ServerController.getInstance();
		GameData data = controller.getGameData();
		waypoints = data.getWaypoints();
		waypoints.addCollectionListener(listener);
		addAll(waypoints);

	}

	/**
	 * Returns the way point selected by this combo box.
	 * 
	 * @return the selected waypoint or null if none.
	 */
	public Waypoint getSelected() {
		SelectableWayPoint sw = (SelectableWayPoint) suggestionModel
				.getSelectedItem();
		if (sw != null) {
			return sw.getWaypoint();
		}

		return null;
	}

	public void setSelected(Object item) {
		if (item instanceof Waypoint) {
			item = new SelectableWayPoint((Waypoint) item);
		}
		suggestionField.setSelectedItem(item);
	}

	/**
	 * Adds all ways points in a representable form to the list model.
	 * 
	 * @param waypoints
	 */
	private void addAll(Collection<Waypoint> waypoints) {
		synchronized (waypoints) {
			for (Waypoint w : waypoints) {
				suggestionModel.addElement(new SelectableWayPoint(w));
			}
		}
	}

	private void removeAll(Collection<Waypoint> waypoints) {
		synchronized (waypoints) {
			for (Waypoint w : waypoints) {
				suggestionModel.removeElement(new SelectableWayPoint(w));
			}
		}
	}

	/**
	 * Called when this panel will no longer be used. Useful for clean up.
	 * 
	 * TODO: Currently only exists to accommodate the {@link WaypointBox} which
	 * has no meaningfull way to detect when it is diposed and should remove
	 * it's listeners. The {@link ObservableCollection} should use weak
	 * references.
	 * 
	 */
	public void dispose() {
		waypoints.removeCollectionListener(listener);
	}

	private class Listener extends CollectionEventAdaptor<Waypoint> {

		@Override
		public void postRemoveEvent(final Collection<Waypoint> alreadyRemoved,
				Collection<Waypoint> whereWereRemoved) {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					removeAll(alreadyRemoved);
				}

			});
		}

		@Override
		public void postAddEvent(final Collection<Waypoint> alreadyAdded,
				Collection<Waypoint> whereWereAdded) {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					addAll(alreadyAdded);
				}
			});
		}

	}
}
