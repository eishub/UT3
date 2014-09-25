package nl.tudelft.goal.ut2004.visualizer.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nl.tudelft.goal.ut2004.visualizer.controller.ServerController;
import cz.cuni.amis.pogamut.unreal.communication.worldview.map.IUnrealWaypoint;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.Respawn;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Player;
import cz.cuni.amis.pogamut.ut2004.server.IUT2004Server;

public class RespawnHereAction implements ActionListener {
	private final Player player;
	private final IUnrealWaypoint navpoint;

	public RespawnHereAction(Player player, IUnrealWaypoint navPoint) {
		this.player = player;
		this.navpoint = navPoint;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ServerController controller = ServerController.getInstance();
		IUT2004Server server = controller.getServer();
		assert server != null;

		Respawn respawn = new Respawn(player.getId(), navpoint.getLocation(),null);
		server.getAct().act(respawn);
	}
}
