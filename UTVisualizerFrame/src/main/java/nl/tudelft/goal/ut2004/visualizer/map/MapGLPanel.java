package nl.tudelft.goal.ut2004.visualizer.map;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;

import nl.tudelft.goal.ut2004.visualizer.timeline.map.EnvironmentRenderer;
import nl.tudelft.goal.ut2004.visualizer.timeline.map.CollectionRenderer;
import nl.tudelft.goal.ut2004.visualizer.timeline.map.MapController;
import nl.tudelft.goal.ut2004.visualizer.timeline.map.MapRenderer;
import nl.tudelft.goal.ut2004.visualizer.timeline.map.MapViewpoint;
import cz.cuni.amis.pogamut.unreal.communication.messages.gbinfomessages.IPlayer;
import cz.cuni.amis.pogamut.unreal.communication.worldview.map.IUnrealMap;

/**
 * This is a GLJPanel that displays UT2004Map. it is a base class, so it takes
 * care of some things, while others are left for derived class to do.
 * <p>
 * What it does: * Can render passed map * Can render all stuff in
 * agentRenderers * Takes care of user interaction for map viewpoint * Selection
 * of object
 * <p>
 * What it doesn't do: * automatically display map, someone else has to take
 * care of that (adding/removing) from agentRenderers * fill in agentRenderers,
 * derived class has to do that.
 * 
 * In lookup are selected objects
 * 
 * @author Honza
 */
public abstract class MapGLPanel extends GLJPanel implements
		MapViewpoint.ViewpointListener {

	protected MapViewpoint mapViewpoint;
	protected MapController mapController;

	protected MapRenderer mapRenderer;
	protected CollectionRenderer<IPlayer> agentRenderes;
	protected CollectionRenderer<Object> objectRenderes;

	protected EnvironmentRenderer environmentRenderer;

	// iterator used to assign unique names for gl rendering, this enabling
	// selection of objects
	protected int lastGLName = 1;

	private IUnrealMap map;

	/**
	 * Create a panel for
	 * 
	 * @param caps
	 * @param map
	 * @param log
	 */
	protected MapGLPanel(GLCapabilities caps, IUnrealMap map) {
		super(caps);

		// if (Beans.isDesignTime()) {
		// Beans.setDesignTime(false);
		// }

		this.map = map;

		// Stuff for controlling viewpoint in map
		mapViewpoint = new MapViewpoint();
		mapController = new MapController(this, mapViewpoint, map.getBox());
		mapController.registerListeners();

		// Create renderers
		mapRenderer = new MapRenderer(map, lastGLName++);
		agentRenderes = new CollectionRenderer<IPlayer>();
		objectRenderes = new CollectionRenderer<Object>();
		environmentRenderer = new EnvironmentRenderer(mapViewpoint,
				agentRenderes, objectRenderes, mapRenderer);

		// Add listener so this level is rendered
		this.addGLEventListener(environmentRenderer);

		// Listen for changes in viewpoint
		mapViewpoint.addViewpointListener(this);

		// Set initial position of view + thanks to listener display
		mapViewpoint.setFromViewedBox(map.getBox());

	}

	/**
	 * Create a pane showing passed map
	 * 
	 * @param map
	 *            Map this pane is supposed to show
	 */
	protected MapGLPanel(IUnrealMap map, Logger log) {
		this(getCapabilities(), map);
	}

	/**
	 * I require HW acceleration and double buffering.
	 * 
	 * @return Set of required capabilities
	 */
	private static GLCapabilities getCapabilities() {
		GLCapabilities caps = new GLCapabilities(GLProfile.getDefault());
		caps.setHardwareAccelerated(true);
		caps.setDoubleBuffered(true);
		return caps;
	}

	/**
	 * When viewpoint is changed, render the map (call display()).
	 * 
	 * @param viewpoint
	 */
	@Override
	public synchronized void onChangedViewpoint(MapViewpoint viewpoint) {
		display();
	}

	/**
	 * Get agents at point p in the scene.
	 * 
	 * @param p
	 *            in window coordiates system, [0,0] is left top
	 * @return List of renderable agents that are at the passed point
	 */
	public synchronized Set<Object> getObjectsAt(Point p) {
		environmentRenderer.setSelectPoint(p);
		display();
		int[] list = environmentRenderer.getSelectedObjects();

		Set<Object> selection = new HashSet<Object>();

		// find that miserable renderer in agents
		selection.addAll(agentRenderes.getObjectsByGLName(list));
		
		// find that miserable renderer in world objects
		selection.addAll(objectRenderes.getObjectsByGLName(list));

		return selection;
	}

	/**
	 * Remove listeners and basically clean up this map. Any call to this object
	 * after this method should invoke exception (it doesn't but I can always
	 * hope in NullPointerException).
	 */
	public synchronized void destroy() {
		this.removeGLEventListener(environmentRenderer);
		this.mapViewpoint.removeViewpointListener(this);

		mapRenderer.destroy();
		agentRenderes.destroy();
		objectRenderes.destroy();
		
		mapRenderer = null;
		environmentRenderer = null;
		agentRenderes = null;
		objectRenderes = null;

		mapController = null;
		mapViewpoint = null;
	}

	protected IUnrealMap getMap() {
		return map;
	}
}
