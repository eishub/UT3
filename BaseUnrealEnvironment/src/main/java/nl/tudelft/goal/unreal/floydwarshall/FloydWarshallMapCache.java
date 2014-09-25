package nl.tudelft.goal.unreal.floydwarshall;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.NavPoint;
import cz.cuni.amis.pogamut.ut2004.communication.translator.shared.events.MapPointListObtained;

/**
 * Cache for {@link FloydWarshallMap}s. Because FloydWarshallMaps consume a fair
 * amount of memory it is better to share this map between bots. As long as a
 * bot has a reference to a SharedFloydWarshallMap that map will remain in
 * memory.
 * 
 * A maps identity is based on the lexographically smallest unrealID and the
 * badEdgeflag. As such the FloydWarshallMapCache can handle different maps at
 * the same time.
 * 
 * @author mpkorstanje
 * 
 */
public final class FloydWarshallMapCache {

	/**
	 * Singleton instance of this cache.
	 */
	private static FloydWarshallMapCache instance;

	/**
	 * Cache for {@link FloydWarshallMap}s.
	 */
	private final Map<MapKey, WeakReference<FloydWarshallMap>> cache = new HashMap<MapKey, WeakReference<FloydWarshallMap>>();

	/**
	 * A map can be uniquely identified by its badedge flag and a navpoint id
	 * (which includes the map name). To make identification consistent we use
	 * the smallest id.
	 * 
	 * @author mpkorstanje
	 * 
	 */
	private static final class MapKey {

		@Override
		public String toString() {
			return "MapKey [id=" + id + ", badEdgeFlag=" + badEdgeFlag + "]";
		}

		/**
		 * The smallest navpoint id.
		 */
		private final String id;

		/**
		 * The prohibeted edges in this map.
		 */
		private final int badEdgeFlag;

		public MapKey(Map<UnrealId, NavPoint> map, int badEdgeFlag) {
			this.id = getSmallest(map.keySet());
			this.badEdgeFlag = badEdgeFlag;
		}

		private String getSmallest(Set<UnrealId> set) {

			if (set.isEmpty()) {
				return null;
			}

			String smallestString = set.iterator().next().getStringId();
			for (UnrealId candidate : set) {
				String candidateString = candidate.getStringId();
				if (candidateString.compareTo(smallestString) < 0) {
					smallestString = candidateString;
				}

			}

			return smallestString;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + badEdgeFlag;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof MapKey))
				return false;
			MapKey other = (MapKey) obj;
			if (badEdgeFlag != other.badEdgeFlag)
				return false;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}

	}

	/**
	 * Private constructor for singleton.
	 */
	private FloydWarshallMapCache() {

	}

	public synchronized FloydWarshallMap createMap(MapPointListObtained event, int badEdgeFlag, Logger log) {
		MapKey key = new MapKey(event.getNavPoints(), badEdgeFlag);
		FloydWarshallMap sharedMap = null;

		// Check if map has been cached.
		if (cache.containsKey(key)) {
			sharedMap = cache.get(key).get();
		}

		// Even if entry exist, weak reference might still be null.
		if (sharedMap != null) {
			log.info("Map exists for " + key);	
		} else {
			log.info("Creating new map for " + key);
			sharedMap = new FloydWarshallMap(badEdgeFlag, log);
			sharedMap.performFloydWarshall(event);
			cache.put(key, new WeakReference<FloydWarshallMap>(sharedMap));
		} 
		
		return sharedMap;
	}

	public synchronized static FloydWarshallMapCache getInstance() {
		if (instance == null) {
			instance = new FloydWarshallMapCache();
		}
		return instance;
	}

}
