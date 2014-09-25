package nl.tudelft.goal.ut2004.agent;

import cz.cuni.amis.pogamut.base.communication.command.IAct;
import cz.cuni.amis.pogamut.base.component.bus.IComponentBus;
import cz.cuni.amis.pogamut.base.utils.logging.IAgentLogger;
import cz.cuni.amis.pogamut.ut2004.agent.params.UT2004AgentParameters;
import cz.cuni.amis.pogamut.ut2004.communication.worldview.UT2004WorldView;
import cz.cuni.amis.pogamut.ut2004.observer.impl.UT2004Observer;

public class TestBot extends UT2004Observer {

	public TestBot(UT2004AgentParameters params, IComponentBus bus, IAgentLogger agentLogger, UT2004WorldView worldView, IAct act) {
		super(params, bus, agentLogger, worldView, act);
		// TODO Auto-generated constructor stub
	}

}
