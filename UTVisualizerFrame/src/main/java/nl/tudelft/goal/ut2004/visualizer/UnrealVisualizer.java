package nl.tudelft.goal.ut2004.visualizer;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import nl.tudelft.goal.ut2004.visualizer.map.PureMapTopPanel;
import nl.tudelft.goal.ut2004.visualizer.panels.connection.ServerConnectionPanel;
import nl.tudelft.pogamut.base.server.ReconnectingServerDefinition;
import nl.tudelft.pogamut.base.server.ServerDefinition;
import nl.tudelft.pogamut.unreal.server.UnrealServerDefinition;
import nl.tudelft.pogamut.ut2004.server.UTServerDefinition;
import cz.cuni.amis.pogamut.unreal.server.IUnrealServer;

public class UnrealVisualizer extends JFrame {

	private ReconnectingServerDefinition<IUnrealServer> server;

	@SuppressWarnings("unchecked")
	private UnrealVisualizer() {

		UnrealServerDefinition<? extends IUnrealServer> server = new UTServerDefinition();

		this.server = new ReconnectingServerDefinition<IUnrealServer>(
				(ServerDefinition<IUnrealServer>) server);

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.add(new ServerConnectionPanel(this.server));
		tabbedPane.add(new PureMapTopPanel(this.server));

		add(tabbedPane);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UnrealVisualizer visualizer = new UnrealVisualizer();
				visualizer.setTitle("UnrealVisualizer");
				visualizer.setSize(500, 500);
				visualizer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				visualizer.setVisible(true);
			}
		});

	}

}
