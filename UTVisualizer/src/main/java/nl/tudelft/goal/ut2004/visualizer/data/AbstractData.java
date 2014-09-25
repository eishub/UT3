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

import java.util.ArrayList;

import nl.tudelft.goal.ut2004.visualizer.events.DataUpdateListener;

import cz.cuni.amis.pogamut.ut2004.server.IUT2004Server;

/**
 * Abstract class for data that is gathered from the UnrealServer.
 * 
 * @author Lennard de Rijk
 * @author M.P. Korstanje
 * 
 */
public abstract class AbstractData {

	private final ArrayList<DataUpdateListener> listeners;

	public AbstractData() {
		listeners = new ArrayList<DataUpdateListener>();
	}

	/**
	 * Called when ever the server is updated.
	 * 
	 * @param server
	 *            the new server or null if the connection was lost.
	 */
	public abstract void serverChanged(IUT2004Server server);

	/**
	 * Add the specified {@link DataUpdateListener} to the listeners for this {@link AbstractData} object.
	 * 
	 * @param listener
	 *            The listener that needs to be subscribed
	 */
	public void subscribe(DataUpdateListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes the specified {@link DataUpdateListener} from the listeners to this {@link AbstractData} object.
	 * 
	 * @param listener
	 *            The listener that needs to be removed.
	 */
	public void unsubscribe(DataUpdateListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Notifies all the listeners that this {@link AbstractData} has been updated.
	 * 
	 * @param type
	 *            The Type of {@link DataUpdateEvent}.
	 */
	public void notifyListeners() {
		for (DataUpdateListener listener : listeners) {
			listener.update(this);
		}
	}

}
