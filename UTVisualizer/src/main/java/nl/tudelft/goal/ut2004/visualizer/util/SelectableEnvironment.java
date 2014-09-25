package nl.tudelft.goal.ut2004.visualizer.util;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

import nl.tudelft.goal.ut2004.visualizer.connection.EnvironmentService;

public class SelectableEnvironment {

	private final EnvironmentService environment;

	public SelectableEnvironment(EnvironmentService environment) {
		this.environment = environment;
	}

	public EnvironmentService getItem() {
		return environment;
	}
	
	/**
	 * Simplifies a given string representation of an AgentID by removing the
	 * UUID making it human readable.
	 * 
	 * By the specs of the agentID this should still result in a unique but
	 * readable ID.
	 * 
	 * @param id
	 * @return the agentID without the UUID.
	 */
	private String simplefyID(String id) {
		//TODO: Duplicate code as in Unreal Environment
		int index = id.lastIndexOf('/');
		// If the expected separator could not be found, use the whole string.
		if (index < 0) {
			index = id.length();
			//log.severe("Could not find UUID seperator in Agent ID: " + id);
		}
		return id.substring(0, index);
	}
	
	@Override
	public String toString() {
		try {
			return simplefyID(environment.getAgentId().getToken());
		} catch (RemoteException e) {
			return "Unconnected Environment";
		}
	}

	public static Collection<SelectableEnvironment> fromCollection(
			Collection<? extends EnvironmentService> environments) {

		Collection<SelectableEnvironment> ret = new ArrayList<SelectableEnvironment>(
				environments.size());

		for (EnvironmentService env : environments) {
			ret.add(new SelectableEnvironment(env));
		}

		return ret;
	}

}
