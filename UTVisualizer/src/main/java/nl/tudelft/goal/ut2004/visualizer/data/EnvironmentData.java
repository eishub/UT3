package nl.tudelft.goal.ut2004.visualizer.data;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;

import nl.tudelft.goal.ut2004.visualizer.connection.EnvironmentService;
import nl.tudelft.goal.ut2004.visualizer.connection.VisualizerService;


import cz.cuni.amis.utils.collections.ObservableSet;

public class EnvironmentData {

	public static final String serviceName = "UnrealVisualizerService";
	private final ObservableSet<EnvironmentService> environments;
	private RegistrationHandler handler;

	public EnvironmentData() {
		HashSet<EnvironmentService> set = new HashSet<EnvironmentService>();
		environments = new ObservableSet<EnvironmentService>(set);
	}

	public class RegistrationHandler extends UnicastRemoteObject implements
			VisualizerService, Serializable {

		protected RegistrationHandler() throws RemoteException {
			super();
		}

		@Override
		public void registerEnvironment(EnvironmentService environment)
				throws RemoteException {
			synchronized (environments) {
				// Re add for the benefit of our listeners.
				if (environments.contains(environment)) {
					environments.remove(environment);
				}
				environments.add(environment);
			}

		}

		@Override
		public void unregisterEnvironment(EnvironmentService environment)
				throws RemoteException {
			synchronized (environments) {
				environments.remove(environment);
			}
		}
	}

	/**
	 * List of clients currently registered with the visualizer.
	 * 
	 * NOTE: You must synchronize on this collection if you want to iterate.
	 * 
	 * @return
	 */
	public ObservableSet<EnvironmentService> getEnvironments() {
		return environments;
	}

	public void connect() {
		System.out.println("RMI server started");

		// Create and install a security manager
		// TODO: How hoes this work?
		// if (System.getSecurityManager() == null) {
		// System.setSecurityManager(new RMISecurityManager());
		// System.out.println("Security manager installed.");
		// } else {
		// System.out.println("Security manager already exists.");
		// }

		try { // special exception handler for registry creation
			LocateRegistry.createRegistry(1099);
			System.out.println("Java RMI registry created.");
		} catch (RemoteException e) {
			// do nothing, error means registry already exists
			System.out.println("Java RMI registry already exists.");
		}

		try {
			// Instantiate handler
			handler = new RegistrationHandler();
			// Bind this object instance to the name "RmiServer"
			Naming.rebind(serviceName, handler);
			System.out.println("UnrealVisualizerService bound in registry");
		} catch (Exception e) {
			System.err.println("RMI server exception:" + e);
			e.printStackTrace();
		}
	}

}
