package nl.tudelft.goal.ut2004.visualizer.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nl.tudelft.goal.ut2004.visualizer.controller.ServerController;


import cz.cuni.amis.pogamut.unreal.communication.worldview.map.IUnrealWaypoint;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.SpawnActor;
import cz.cuni.amis.pogamut.ut2004.server.IUT2004Server;

public class SpawnItemAction implements ActionListener {

	private final IUnrealWaypoint navPoint;
	private final String item;

	public SpawnItemAction(IUnrealWaypoint navPoint, String item) {
		this.navPoint = navPoint;
		this.item = item;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ServerController controller = ServerController.getInstance();
		IUT2004Server server = controller.getServer();
		assert server != null;

		SpawnActor spawnActor = new SpawnActor(navPoint.getLocation(), null,
				item);
		server.getAct().act(spawnActor);
	}

}
