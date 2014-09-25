package nl.tudelft.goal.ut2004.visualizer.map;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import nl.tudelft.goal.ut2004.visualizer.services.ISelectionHandler;
import nl.tudelft.pogamut.base.server.ServerDefinition;
import cz.cuni.amis.pogamut.unreal.server.IUnrealServer;
import cz.cuni.amis.utils.flag.FlagListener;

/**
 * This panel handles connecting to an unreal server and rendering it.
 * 
 * @author M.P. Korstanje
 */
public class PureMapTopPanel extends JPanel {

	/**
	 * Default generated.
	 */
	private static final long serialVersionUID = -448000130214012550L;
	protected final ServerDefinition<IUnrealServer> serverDefinition;
	protected PureMapGLPanel mapPanel;
	private ISelectionHandler selectionHandler;

	// protected InstanceContent lookupContent;

	public PureMapTopPanel(ServerDefinition<IUnrealServer> serverDefinition) {
		this.serverDefinition = serverDefinition;
		this.setLayout(new BorderLayout());

		// Trigger server changed sync with serverDefintion.
		handleServerChanged();

		// Associate our lookup with this TC
		// lookupContent = new InstanceContent();
		// associateLookup(new AbstractLookup(lookupContent));

		// set proper name
		handleVisibility();
		handleServerName();
		handleServer();
	}

	/**
	 * The map updates if it's shown, it doesn't when it's not.
	 * 
	 */
	private void handleVisibility() {
		this.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentShown(ComponentEvent e) {
				if (mapPanel != null) {
					mapPanel.startDisplayLoop();
				}
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				if (mapPanel != null) {
					mapPanel.stopDisplayLoop();
				}
			}
		});
	}

	private void handleServerName() {
		setPanelName();
		serverDefinition.getServerNameFlag().addListener(
				new FlagListener<String>() {

					@Override
					public void flagChanged(String changedValue) {
						SwingUtilities.invokeLater(new Runnable() {

							@Override
							public void run() {
								setPanelName();
							}
						});
					}
				});

	}

	protected void setPanelName() {
		String map = "no map";
		if (serverDefinition.getServerFlag().getFlag() != null) {
			map = serverDefinition.getServerFlag().getFlag().getMapName();
		}

		this.setName(serverDefinition.getServerName() + " [" + map + "]");
		// setDisplayName(serverDef.getServerName() + " [" + map + "]");
	}

	private void handleServer() {
		serverDefinition.getServerFlag().addListener(
				new FlagListener<IUnrealServer>() {

					/**
					 * When server changes, get message
					 * 
					 * @param changedValue
					 */
					@SuppressWarnings("unchecked")
					@Override
					public void flagChanged(IUnrealServer server) {
						SwingUtilities.invokeLater(new Runnable() {

							@Override
							public void run() {
								handleServerChanged();
							}

						});
					}
				});

	}

	/**
	 * Clears the panel and updates the map panel.
	 * 
	 */
	private void handleServerChanged() {
		removeAll();
		setPanelName();
		if (serverDefinition.getServerFlag().getFlag() != null) {
			setUpMapPanel();
		} else {
			add(new JLabel("Not connected."), BorderLayout.PAGE_START);
		}
		revalidate();
		repaint();
	}

	@SuppressWarnings("unchecked")
	protected void setUpMapPanel() {

		// Clear up any previous panels.
		if (mapPanel != null) {
			remove(mapPanel);
			mapPanel.destroy();
		}

		// Create map panel
		IUnrealServer server = serverDefinition.getServerFlag().getFlag();
		mapPanel = new PureMapGLPanel(server.getMap(), server);
		mapPanel.setSelectionHandler(selectionHandler);

		// Don't show unless visible.
		if (isVisible()) {
			mapPanel.startDisplayLoop();
		}

		// add map panel to the TC
		add(mapPanel);
	}

	public void setSelectionHandler(ISelectionHandler selectionHandler) {
		this.selectionHandler = selectionHandler;
		if (mapPanel != null) {
			mapPanel.setSelectionHandler(selectionHandler);
		}
	}

}
