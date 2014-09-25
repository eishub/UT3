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
package nl.tudelft.goal.ut2004.visualizer.events;

import nl.tudelft.goal.ut2004.visualizer.data.AbstractData;

/**
 * Interface for classes who want to subscribe to {@link AbstractData}.
 * 
 * @author Lennard de Rijk
 *
 */
public interface DataUpdateListener {

	/**
	 * Called when {@link AbstractData} has been updated for all objects
	 * which have subscribed to that {@link AbstractData} object.
	 * 
	 * @param data The {@link AbstractData} instance that has been updated
	 * @param type The {@link DataUpdateType} that has taken place
	 */
	public void update(AbstractData data);
}
