package nl.tudelft.goal.ut3.server;

import com.google.inject.AbstractModule;

import cz.cuni.amis.pogamut.ut2004.agent.params.UT2004AgentParameters;
import cz.cuni.amis.pogamut.ut2004.factory.guice.remoteagent.UT2004ServerModule;
import cz.cuni.amis.pogamut.ut2004.server.IUT2004Server;

public class EnvironmentControllerServerModule<PARAMS extends UT2004AgentParameters> extends UT2004ServerModule<PARAMS> {
	
	@Override
	protected void configureModules() {
		super.configureModules();
		addModule(new AbstractModule() {

			@Override
			protected void configure() {
				bind(IUT2004Server.class).to(EnvironmentControllerServer.class);
			}
			
		});
	}
	
}
