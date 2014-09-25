package nl.tudelft.goal.ut2004.visualizer.connection.client;

import java.rmi.RemoteException;

import nl.tudelft.goal.ut2004.visualizer.connection.EnvironmentService;
import nl.tudelft.goal.ut2004.visualizer.connection.VisualizerService;
import cz.cuni.amis.utils.flag.Flag;

/**
 * Wrapper class that represents the {@link VisualizerService} to the
 * {@link UnrealGoalEnvironment}.
 * 
 * @author M.P. Korstanje
 * 
 */
public class RemoteVisualizer {

	private final Flag<Boolean> connectedFlag = new Flag<Boolean>();
	private VisualizerService visualizer;
	private EnvironmentService environment;

	protected void setVisualizerService(VisualizerService visualizer) {
		this.visualizer = visualizer;
		setConnected(true);
	}

	public void setEnvironment(EnvironmentService client) {
		try {
			this.environment = client;
			this.visualizer.registerEnvironment(client);
		} catch (RemoteException e) {
			// Connection failed? Update status.
			disconnect();
			e.printStackTrace();
		}
	}

	private void setConnected(boolean connected) {
		connectedFlag.setFlag(connected);
	}

	public Flag<Boolean> getConnectedFlag() {
		return connectedFlag;
	}

	public void disconnect() {
		try {
			if (visualizer != null)
				visualizer.unregisterEnvironment(environment);
		} catch (RemoteException e) {
			// Ignore, we are disconnected now.
			// e.printStackTrace();
		}

		environment = null;

		setConnected(false);
	}

}
