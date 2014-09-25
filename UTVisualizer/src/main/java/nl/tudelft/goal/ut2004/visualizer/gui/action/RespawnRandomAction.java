package nl.tudelft.goal.ut2004.visualizer.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import nl.tudelft.goal.ut2004.visualizer.controller.ServerController;


import cz.cuni.amis.pogamut.unreal.communication.messages.gbinfomessages.IPlayer;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.Respawn;
import cz.cuni.amis.pogamut.ut2004.server.IUT2004Server;

public class RespawnRandomAction extends AbstractAction {
	private final IPlayer player;

	public RespawnRandomAction(IPlayer player) {
		this.player = player;
	
		this.putValue(NAME, "Respawn");

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ServerController controller = ServerController.getInstance();
		IUT2004Server server = controller.getServer();
		assert server != null;

		Respawn respawn = new Respawn(player.getId(), null, null);
		server.getAct().act(respawn);

	}

}
