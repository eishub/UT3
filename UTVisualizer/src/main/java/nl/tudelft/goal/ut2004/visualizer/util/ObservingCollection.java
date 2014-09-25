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
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import cz.cuni.amis.utils.collections.ObservableCollection;

/**
 * Decorator to make ObservableCollection properly observable themselves.
 * 
 * @author M.P. Korstanje
 * 
 * @param <T>
 */
public class ObservingCollection<T> extends ObservableCollection<T> implements
		Collection<T> {

	private final Listener listener; 
	private ObservableCollection<T> observed;
	private final ObservableCollection<T> empty = new ObservableCollection<T>(
			new LinkedList<T>());;

	public ObservingCollection() {
		super(new LinkedList<T>());
		this.listener = new Listener();
		observed = new ObservableCollection<T>(new LinkedList<T>());
	}

	public void setObserved(ObservableCollection<T> newCol) {

		// Don't interleave swapping with changes to collection.
		synchronized (this) {
			// Swap
			observed.removeCollectionListener(listener);
			observed = newCol;
			observed.addCollectionListener(listener);

			// Update our observed contents.
			privateClear();
			privateAddAll(newCol);
		}
	}
	
	public void removeObserved() {
		setObserved(empty);
	}

	private void privateAddAll(Collection<T> c) {
		super.addAll(c);
	}

	private void privateClear() {
		super.clear();
	}
	
	private void privateRemoveAll(Collection<T> c) {
		super.removeAll(c);
	}



	// Class used to notify our listeners when col's listeners get notified.
	private class Listener extends CollectionEventAdaptor<T> {

		@Override
		public void postAddEvent(Collection<T> alreadyAdded,
				Collection<T> whereWereAdded) {
			// Don't add until we're done swapping
			synchronized (ObservingCollection.this) {
				// But only if it's for the collection we are observing
				if (whereWereAdded == observed) {
					privateAddAll(alreadyAdded);
				}
			}
		}

		@Override
		public void postRemoveEvent(Collection<T> alreadyRemoved,
				Collection<T> whereWereRemoved) {
			// Don't removed until we're done swapping
			synchronized (ObservingCollection.this) {
				// But only if it's for the collection we are observing
				if (whereWereRemoved == observed) {
					privateRemoveAll(alreadyRemoved);
				}
			}
		}


	}

	@Override
	public boolean add(T e) {
		throw new UnsupportedOperationException("Unmodifyable");
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		throw new UnsupportedOperationException("Unmodifyable");
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("Unmodifyable");
	}

	@Override
	public Iterator<T> iterator() {
		return Collections.unmodifiableCollection(col).iterator();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException("Unmodifyable");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("Unmodifyable");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("Unmodifyable");
	}

}
