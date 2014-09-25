package nl.tudelft.goal.ut2004.visualizer.services;

import java.awt.Component;
import java.awt.Point;
import java.util.Set;

public interface ISelectionHandler {

	void selected(Set<Object> selected, Point at, Component origin);
}
