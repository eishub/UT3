package nl.tudelft.goal.ut2004.visualizer.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nl.tudelft.goal.ut2004.visualizer.controller.ServerController;


import cz.cuni.amis.pogamut.unreal.communication.messages.gbinfomessages.IPlayer;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.AddInventory;
import cz.cuni.amis.pogamut.ut2004.server.IUT2004Server;

public class AddInventoryAction implements ActionListener {
	private final IPlayer player;
	private final String item;

	public AddInventoryAction(IPlayer player, String item) {
		this.player = player;
		this.item = item;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ServerController controller = ServerController.getInstance();
		IUT2004Server server = controller.getServer();
		assert server != null;

		AddInventory addInventory = new AddInventory(player.getId(), item);

		server.getAct().act(addInventory);
	}

}
