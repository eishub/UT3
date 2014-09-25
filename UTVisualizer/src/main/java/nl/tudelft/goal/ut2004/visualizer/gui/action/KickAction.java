package nl.tudelft.goal.ut2004.visualizer.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import nl.tudelft.goal.ut2004.visualizer.controller.ServerController;

import cz.cuni.amis.pogamut.unreal.communication.messages.gbinfomessages.IPlayer;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.Kick;
import cz.cuni.amis.pogamut.ut2004.server.IUT2004Server;

public class KickAction extends AbstractAction  {
	private final IPlayer player;

	public KickAction(IPlayer bot) {
		this.player = bot;
		
		this.putValue(NAME, "Kick");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ServerController controller = ServerController.getInstance();
		IUT2004Server server = controller.getServer();
		assert server != null;

		Kick kick = new Kick(player.getId());
		server.getAct().act(kick);
	}
}
