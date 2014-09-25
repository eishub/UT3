package nl.tudelft.goal.unreal.util;

import java.util.Collection;

/**
 * Selects from a collection a single element.

 * @author mpkorstanje
 *
 * @param <T> type of the collection.
 */

public interface Selector<T>  {
	
	public T select(Collection<? extends T> c);

}
