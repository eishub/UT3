package nl.tudelft.goal.ut2004.visualizer.map;

import java.awt.Color;

import nl.tudelft.goal.ut2004.visualizer.timeline.map.IRenderableWorldObject;
import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.unreal.communication.worldview.map.IUnrealWaypoint;

public class RenderableWaypoint implements IRenderableWorldObject {

	private final IUnrealWaypoint waypoint;
	private final Color color;
	private final int glName;

	public RenderableWaypoint(IUnrealWaypoint waypoint, int glName) {
		this.waypoint = waypoint;
		this.glName = glName;
		this.color = Color.BLUE;
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public Object getDataSource() {
		return waypoint;
	}

	@Override
	public int getGLName() {
		return glName;
	}

	@Override
	public Location getLocation() {
		return waypoint.getLocation();
	}

}
