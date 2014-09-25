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
package nl.tudelft.goal.ut2004.visualizer.util;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JList;

import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.NavPoint;
import cz.cuni.amis.pogamut.ut2004.communication.worldview.map.Waypoint;

/**
 * Wrapper around {@link NavPoint} to make it more human readable in {@link JList}.
 * 
 * @author Lennard de Rijk
 * 
 */
public class SelectableWayPoint {

	/**
	 * The {@link NavPoint} wrapped in this object.
	 */
	private final Waypoint navPoint;

	/**
	 * 
	 * @param navPoint
	 *            The {@link NavPoint} to wrap in this object.
	 */
	public SelectableWayPoint(Waypoint navPoint) {
		this.navPoint = navPoint;

	}

	/**
	 * @return the navPoint wrapped in this class.
	 */
	public Waypoint getWaypoint() {
		return navPoint;
	}

	/**
	 * Represents a {@link NavPoint} in a human readable form.
	 */
	@Override
	public String toString() {
		String ret = navPoint.getID();

		return ret;
	}

	/**
	 * Transforms a {@link Collection} of {@link NavPoint} into an Array of {@link SelectableWayPoint}.
	 * 
	 * @param navCollection
	 *            The {@link Collection} of {@link NavPoint} to transform
	 * @return An Array of {@link SelectableWayPoint}
	 */
	public static Collection<SelectableWayPoint> getFromWaypointCollection(
			Collection<Waypoint> navCollection) {
		Collection<SelectableWayPoint> selectablePoints = new ArrayList<SelectableWayPoint>(navCollection.size());

		for (Waypoint navPoint : navCollection) {
			selectablePoints.add(new SelectableWayPoint(navPoint));
		}

		return selectablePoints;
	}

}
