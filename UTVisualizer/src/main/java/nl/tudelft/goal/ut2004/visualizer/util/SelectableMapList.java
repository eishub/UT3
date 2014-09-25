package nl.tudelft.goal.ut2004.visualizer.util;

import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.MapList;

public class SelectableMapList {
	private final MapList map;

	public SelectableMapList(MapList map) {
		this.map = map;
	}

	public String toString() {
		return map.getName();
	}

	public MapList getMapList() {
		return map;
	}
}
