package nl.tudelft.goal.ut2004.visualizer.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cz.cuni.amis.utils.listener.Listeners;

public class ObservableMap<K, V> implements Map<K, V> {

    protected Listeners<MapEventListener> eventListeners = new Listeners<MapEventListener>();

	protected final Map<K, V> map = new HashMap<K, V>();

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsKey(value);
	}

	@Override
	public V get(Object key) {
		return map.get(key);
	}

	@Override
	public V put(K key, V value) {
		Map<K, V> put = Collections.singletonMap(key, value);
		notifyPrePut(put);
		V ret = map.put(key, value);
		notifyPostPut(put);
		return ret;
	}


	@Override
	public V remove(Object key) {

		V value = map.get(key);
		Map<Object, V> rem = Collections.singletonMap(key, value);
		notifyPreRemove(rem);
		value = map.remove(key);
		notifyPostRemove(rem);
		return value;
	}

	

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		notifyPrePut(m);
		map.putAll(m);
		notifyPostPut(m);

	}

	@Override
	public void clear() {
		Map<Object, V> copy = new HashMap<Object, V>(map);
		notifyPreRemove(copy);
		map.clear();
		notifyPostRemove(copy);
	}

	@Override
	public Set<K> keySet() {
		return map.keySet();
	}

	@Override
	public Collection<V> values() {
		return map.values();
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return map.entrySet();
	}
	
	protected synchronized void notifyPostPut(Map<? extends K, ? extends V> m) {
		// TODO Auto-generated method stub

	}

	protected synchronized void notifyPrePut(Map<? extends K, ? extends V> m) {
		// TODO Auto-generated method stub

	}
	protected synchronized void notifyPostRemove(Map<Object, V> rem) {
		// TODO Auto-generated method stub

	}

	protected synchronized void notifyPreRemove(Map<Object, V> rem) {
		// TODO Auto-generated method stub

	}

}
