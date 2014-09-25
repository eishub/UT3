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

import java.util.Collection;

import cz.cuni.amis.utils.collections.CollectionEventListener;

/**
 * Adaptor for the CollectionEventListener. Limits the number of methods that need to be implemented.
 * 
 * @author M.P. Korstanje
 * 
 * @param <T>
 */
public abstract class CollectionEventAdaptor<T> implements
		CollectionEventListener<T> { 

	@Override
	public void postAddEvent(Collection<T> alreadyAdded,
			Collection<T> whereWereAdded) {
	}

	@Override
	public void postRemoveEvent(Collection<T> alreadyRemoved,
			Collection<T> whereWereRemoved) {
	}

	@Override
	public void preAddEvent(Collection<T> toBeAdded, Collection<T> whereToAdd) {

	}

	@Override
	public void preRemoveEvent(Collection<T> toBeRemoved,
			Collection<T> whereToRemove) {

	}

}
