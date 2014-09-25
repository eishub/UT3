package nl.tudelft.goal.ut2004.visualizer.connection;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import cz.cuni.amis.pogamut.base.agent.IAgentId;
import eis.exceptions.ManagementException;

/**
 * Acts as a client for the visualizer. Mediates the interaction between the
 * actual client and the visualizer by providing listeners.
 * 
 * @author M.P. Korstanje
 * @param <VisualizerClientListener>
 * 
 */
public final class EnvironmentServiceMediator extends UnicastRemoteObject
		implements EnvironmentService {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 201210291347L;
	
	private final IAgentId id;
	private EnvironmentServiceListener listener;

	public EnvironmentServiceMediator(IAgentId id) throws RemoteException {
		this.id = id;
	}

	public void setListener(EnvironmentServiceListener listener) {
		this.listener = listener;
	}

	public void removeListener() {
		listener = null;
	}

	@Override
	public void addBot(AddBotCommand parameters) throws RemoteException,
			ManagementException {
		listener.addBot(parameters);
	}

	@Override
	public IAgentId getAgentId() throws RemoteException {
		return id;
	}

	public String toString(){
		String name = id.getToken();
		return name;
	}
}
