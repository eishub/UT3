package nl.tudelft.goal.ut2004.visualizer.util;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.prefs.Preferences;

/**
 * Helper to persist windows. Will store the last known size and position of a
 * given window. When used the first time the settings of the window will be
 * used as defaults.
 */
public class WindowPersistenceHelper {

	private static final String WINDOW_Y = "WINDOW_Y";
	private static final String WINDOW_X = "WINDOW_X";
	private static final String WINDOW_HEIGHT = "WINDOW_HEIGHT";
	private static final String WINDOW_WIDTH = "WINDOW_WIDTH";

	private final Window window;
	private final Preferences preferences;
	private final String className;

	public WindowPersistenceHelper(Window component) {
		this.window = component;
		this.preferences = Preferences.userNodeForPackage(component.getClass());
		this.className = component.getClass().getSimpleName();

		// Save on change.
		window.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				save();

			}

			@Override
			public void componentMoved(ComponentEvent e) {
				save();
			}

		});
	}

	public void load() {

		int width = preferences.getInt(className + WINDOW_WIDTH, window.getWidth());
		int height = preferences.getInt(className + WINDOW_HEIGHT, window.getHeight());
		window.setSize(width, height);

		int x = preferences.getInt(className + WINDOW_X, window.getX());
		int y = preferences.getInt(className + WINDOW_Y, window.getY());
		window.setLocation(x, y);

	}

	public void save() {
		Dimension size = window.getSize();
		preferences.putInt(className + WINDOW_WIDTH, window.getWidth());
		preferences.putInt(className + WINDOW_HEIGHT, window.getHeight());

		Point position = window.getLocation();
		preferences.putInt(className + WINDOW_X, window.getX());
		preferences.putInt(className + WINDOW_Y, window.getY());

	}

}
