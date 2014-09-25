package nl.tudelft.goal.ut3.visualizer.connection.client;

import java.net.URI;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import nl.tudelft.goal.ut2004.visualizer.connection.VisualizerService;
import nl.tudelft.pogamut.base.server.ServerDefinition;
import cz.cuni.amis.utils.exception.PogamutException;
import cz.cuni.amis.utils.flag.FlagListener;

public class VisualizerServiceDefinition extends ServerDefinition<RemoteVisualizer> {

	/**
	 * Serial version UID. Date of last modification.
	 */
	private static final long serialVersionUID = 201212041520L;

	public static final String serviceName = "UnrealVisualizerService";

	private final FlagListener<Boolean> connectionListener = new FlagListener<Boolean>() {

		@Override
		public void flagChanged(Boolean connected) {
			if (!connected)
				startServer(); 
		}
	};

	private RemoteVisualizer visualizer;

	/**
	 * Starts a connection to the Visualizer server. Throws a Pogamut exception if the connection fails.
	 * 
	 */
	@Override
	public void startServer() {

		try {
			URI uri = getUri();

			assert uri.getHost() != null;
			assert uri.getPort() != -1;

			Registry registry = LocateRegistry.getRegistry(uri.getHost(), uri.getPort());
			VisualizerService service = (VisualizerService) registry.lookup(serviceName);
			visualizer = new RemoteVisualizer();
			visualizer.setVisualizerService(service);
			visualizer.getConnectedFlag().addListener(connectionListener);
			setNewServer(visualizer);

		} catch (RemoteException e) {
			throw new PogamutException("Problem marshalling remote visualizer.", e);
		} catch (NotBoundException e) {
			throw new PogamutException("UnrealVisualizer is not running", e);
		}
	}

	@Override
	public void stopServer() {
		if (visualizer != null) {
			visualizer.getConnectedFlag().removeListener(connectionListener);
			visualizer.disconnect();
			visualizer = null;
			setNewServer(null);
		}
	}

}
