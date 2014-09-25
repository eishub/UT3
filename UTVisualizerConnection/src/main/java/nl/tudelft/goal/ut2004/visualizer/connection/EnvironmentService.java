package nl.tudelft.goal.ut2004.visualizer.connection;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import cz.cuni.amis.pogamut.base.agent.IAgentId;
import eis.exceptions.ManagementException;

public interface EnvironmentService extends Remote, Serializable {

	public void addBot(AddBotCommand parameters) throws RemoteException,
			ManagementException;

	public IAgentId getAgentId() throws RemoteException;
}
