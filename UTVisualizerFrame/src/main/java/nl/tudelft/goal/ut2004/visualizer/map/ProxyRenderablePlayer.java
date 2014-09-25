package nl.tudelft.goal.ut2004.visualizer.map;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import cz.cuni.amis.introspection.Folder;
import cz.cuni.amis.pogamut.base.agent.IAgentId;
import cz.cuni.amis.pogamut.base.agent.state.level0.IAgentState;
import cz.cuni.amis.pogamut.base.communication.command.IAct;
import cz.cuni.amis.pogamut.base.communication.worldview.IWorldView;
import cz.cuni.amis.pogamut.base.component.bus.IComponentBus;
import cz.cuni.amis.pogamut.base.component.exception.ComponentCantPauseException;
import cz.cuni.amis.pogamut.base.component.exception.ComponentCantResumeException;
import cz.cuni.amis.pogamut.base.component.exception.ComponentCantStartException;
import cz.cuni.amis.pogamut.base.component.exception.ComponentCantStopException;
import cz.cuni.amis.pogamut.base.utils.logging.IAgentLogger;
import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.base3d.worldview.object.Rotation;
import cz.cuni.amis.pogamut.base3d.worldview.object.Velocity;
import cz.cuni.amis.pogamut.unreal.communication.messages.gbinfomessages.IPlayer;
import cz.cuni.amis.utils.exception.PogamutException;
import cz.cuni.amis.utils.flag.ImmutableFlag;
import nl.tudelft.goal.ut2004.visualizer.map.MapColorGenerator.MapColor;
import nl.tudelft.goal.ut2004.visualizer.timeline.map.IRenderableUTAgent;

public class ProxyRenderablePlayer implements IRenderableUTAgent {
	private final int glName;
	private final IPlayer agent;
	private final MapColor color;

	public ProxyRenderablePlayer(IPlayer agent, MapColor color, int glName) {
		this.glName = glName;
		this.agent = agent;
		this.color = color;
	}

	@Override
	public void respawn() throws PogamutException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public IAct getAct() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public IWorldView getWorldView() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public String getName() {
		return agent.getName();
	}

	@Override
	public IAgentId getComponentId() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public IAgentLogger getLogger() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public ImmutableFlag<IAgentState> getState() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public Folder getIntrospection() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void start() throws ComponentCantStartException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void startPaused() throws ComponentCantStartException {
		throw new UnsupportedOperationException("Not yet implemented");

	}

	@Override
	public void pause() throws ComponentCantPauseException {
		throw new UnsupportedOperationException("Not yet implemented");

	}

	@Override
	public void resume() throws ComponentCantResumeException {
		throw new UnsupportedOperationException("Not yet implemented");

	}

	@Override
	public void stop() throws ComponentCantStopException {
		throw new UnsupportedOperationException("Not yet implemented");

	}

	@Override
	public void kill() {
		throw new UnsupportedOperationException("Not yet implemented");

	}

	@Override
	public IComponentBus getEventBus() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public Location getLocation() {
		return agent.getLocation();
	}

	@Override
	public Velocity getVelocity() {
		return agent.getVelocity();
	}

	@Override
	public Rotation getRotation() {
		return agent.getRotation();
	}

	@Override
	public Color getColor() {
		return color.getColor(agent.getTeam());
	}


	@Override
	public List<String> getAssociatedInfo() {
		return new ArrayList<String>(0);
	}

	@Override
	public Object getDataSource() {
		return agent;
	}

	@Override
	public int getGLName() {
		return glName;
	}

}
