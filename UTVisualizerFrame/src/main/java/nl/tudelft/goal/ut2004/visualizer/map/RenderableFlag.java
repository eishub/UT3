package nl.tudelft.goal.ut2004.visualizer.map;

import java.awt.Color;

import nl.tudelft.goal.ut2004.visualizer.timeline.map.IRenderableWorldObject;
import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.FlagInfo;

public class RenderableFlag implements IRenderableWorldObject {

	private final FlagInfo flag;
	private final Color color;
	private final int glName;

	public RenderableFlag(FlagInfo flag, int glName) {
		this.flag = flag;
		this.glName = glName;
		
		switch (flag.getTeam()) {
		case 0:
			this.color = Color.RED;
			break;
		case 1:
			this.color = Color.BLUE;
			break;
		default:
			this.color = Color.ORANGE;
		}
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public Object getDataSource() {
		return flag;
	}

	@Override
	public int getGLName() {
		return glName;
	}

	@Override
	public Location getLocation() {
		return flag.getLocation();
	}



}
