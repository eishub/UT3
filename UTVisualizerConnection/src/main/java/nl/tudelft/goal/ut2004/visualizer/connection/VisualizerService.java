package nl.tudelft.goal.ut2004.visualizer.connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VisualizerService extends Remote {

	public void registerEnvironment(EnvironmentService environment)
			throws RemoteException;

	public void unregisterEnvironment(EnvironmentService environment)
			throws RemoteException;

}
