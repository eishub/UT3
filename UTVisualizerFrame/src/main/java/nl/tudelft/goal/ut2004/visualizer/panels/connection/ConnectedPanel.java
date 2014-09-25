package nl.tudelft.goal.ut2004.visualizer.panels.connection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cz.cuni.amis.utils.flag.Flag;
import cz.cuni.amis.utils.flag.FlagListener;

public class ConnectedPanel extends JPanel {

	private final JLabel connectionStatus;
	private final JButton disconnectButton;

	public ConnectedPanel(Flag<URI> uriFlag, final IDisconnect disconnect) {
		this.connectionStatus = new JLabel("Connected to: " + uriFlag.getFlag());
		this.disconnectButton = new JButton("Disconnect");
		
		add(connectionStatus);
		add(disconnectButton);

		uriFlag.addListener(new FlagListener<URI>() {

			@Override
			public void flagChanged(URI changedValue) {
				connectionStatus.setText("Connected to: " + changedValue);
			}
		});

		this.disconnectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				disconnect.disconnect();

			}
		});
	}
}
