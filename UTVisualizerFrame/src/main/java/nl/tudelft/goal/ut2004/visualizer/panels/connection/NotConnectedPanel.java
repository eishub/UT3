package nl.tudelft.goal.ut2004.visualizer.panels.connection;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NotConnectedPanel extends JPanel {

	private static final String SERVER_ADDRESS_KEY = "SERVER_ADDRESS";
	private static final String LOCAL_UT_SERVER = "ut://127.0.0.1:3001";
	private static Preferences preferences = Preferences.userNodeForPackage(NotConnectedPanel.class);
	private final JTextField serverAddressField;
	private final JLabel notice;
	private IConnect serverConnectionPanel;
	private final JButton connectButton;

	public NotConnectedPanel(IConnect panel) {
		this.serverAddressField = createServerAddressField();
		this.connectButton = createConnectButton();
		this.notice = createNotice();
		this.serverConnectionPanel = panel;

		add(serverAddressField);
		add(connectButton);
		add(notice);
	}
	

	private JButton createConnectButton() {
		JButton connectButton = new JButton("Connect");
		connectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tryConnectToServerAddress();
			}
		});

		return connectButton;
	}

	private JTextField createServerAddressField() {
		JTextField text = new JTextField(20);
		String serverAddress = preferences.get(SERVER_ADDRESS_KEY, LOCAL_UT_SERVER);
		text.setText(serverAddress);

		text.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					tryConnectToServerAddress();
				}
			}

		});

		return text;
	}

	private JLabel createNotice() {
		JLabel notice = new JLabel();
		notice.setForeground(Color.BLUE);
		return notice;
	}

	private void tryConnectToServerAddress() {

		String serverAddress = serverAddressField.getText();
		URI serverURI;
		try {
			serverURI = new URI(serverAddress);
		} catch (URISyntaxException e) {
			notice.setText("A valid address has the form ut://<address>:<port>");
			return;
		}

		if (serverURI.getPort() == -1 || serverURI.getHost() == null) {
			notice.setText("A valid address has the form ut://<address>:<port>");
			return;
		}

		notice.setText("");
		preferences.put(SERVER_ADDRESS_KEY, serverAddress);
		serverConnectionPanel.connect(serverURI);
	}


}
