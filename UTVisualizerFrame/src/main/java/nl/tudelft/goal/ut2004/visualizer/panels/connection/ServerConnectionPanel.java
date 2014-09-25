package nl.tudelft.goal.ut2004.visualizer.panels.connection;

import java.awt.CardLayout;
import java.net.URI;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import nl.tudelft.pogamut.base.server.ReconnectingServerDefinition;
import cz.cuni.amis.utils.flag.FlagListener;

/**
 * Smart widget that handles the connecting to the server.
 * 
 * Shows only buttons applicable to the state of the widget.
 * 
 * For now assumes that the ServerDefinition is a subclass of
 * {@link ReconnectingServerDefinition}.
 * 
 * Possible transitions are:
 * 
 * NotConnected --ConnectButton--> Connecting
 * 
 * Connecting --ServerChanged(!=null)--> Connected Connecting --CancelButton-->
 * NotConnected
 * 
 * Connected --DisconnectButton--> NotConnected Connected
 * --ServerChanged(==null)--> Connecting
 * 
 * Threads interacting are AWT event queue, server connection thread, server
 * thread.
 * 
 * @author M.P. Korstanje
 * 
 */
public class ServerConnectionPanel extends JPanel {

	/**
	 * Generated.
	 */
	private static final long serialVersionUID = -3524243884561574625L;
	private final static String NOT_CONNECTED_PANEL = "notConnected";
	private final static String CONNECTED_PANEL = "connected";
	private final static String CONNECTING_PANEL = "connecting";
	private final CardLayout cardLayout;
	private final ReconnectingServerDefinition serverDefinition;
	private final Listener listener;

	public ServerConnectionPanel(ReconnectingServerDefinition serverDefinition) {
		assert serverDefinition != null;
		this.cardLayout = new CardLayout();
		this.serverDefinition = serverDefinition;
		this.listener = new Listener();

		initPanel();
		addServerListener();
	}


	
	private void initPanel() {
		JPanel notConnected = makeNotConnectedPanel();
		JPanel connecting = makeConnectingPanel();
		JPanel connected = makeConnectedPanel();

		this.setLayout(cardLayout);
		add(notConnected, NOT_CONNECTED_PANEL);
		add(connecting, CONNECTING_PANEL);
		add(connected, CONNECTED_PANEL);

		showPanel(NOT_CONNECTED_PANEL);
	}

	private JPanel makeConnectedPanel() {
		return new ConnectedPanel(serverDefinition.getUriFlag(), new IDisconnect() {

			@Override
			public void disconnect() {
				removeServerListener();
				showPanel(NOT_CONNECTED_PANEL);
				serverDefinition.stopServer();
			}

		});
	}

	private JPanel makeConnectingPanel() {
		return new ConnectingPanel(serverDefinition.getUriFlag(), new IDisconnect() {

			@Override
			public void disconnect() {
				removeServerListener();
				showPanel(NOT_CONNECTED_PANEL);
				serverDefinition.stopServer();
			}

		});
	}

	private JPanel makeNotConnectedPanel() {
		return new NotConnectedPanel(new IConnect() {

			@Override
			public void connect(URI uri) {
				showPanel(CONNECTING_PANEL);
				addServerListener();
				serverDefinition.setUri(uri);
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void addServerListener() {
		serverDefinition.getServerFlag().addListener(listener);
	}

	@SuppressWarnings("unchecked")
	private void removeServerListener() {
		serverDefinition.getServerFlag().removeListener(listener);
	}

	private class Listener implements FlagListener {

		@Override
		public void flagChanged(Object changedValue) {
			// Execute on swing thread.
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					// Check what the status is now.
					Object server = serverDefinition.getServerFlag().getFlag();
					if (server == null) {
						showPanel(CONNECTING_PANEL);
					} else {
						showPanel(CONNECTED_PANEL);
					}
				}
			});

		}
	}

	private void showPanel(String panel) {
		cardLayout.show(this, panel);
	}

}
