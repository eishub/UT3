package nl.tudelft.goal.ut2004.visualizer.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;

import nl.tudelft.goal.ut2004.visualizer.controller.ServerController;
import nl.tudelft.goal.ut2004.visualizer.gui.widgets.MapBox;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.ChangeMap;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.MapList;
import cz.cuni.amis.pogamut.ut2004.server.IUT2004Server;
import cz.cuni.amis.utils.flag.FlagListener;

public class ChangeMapAction extends AbstractAction {

	private final MapBox mapSelection;

	public ChangeMapAction(MapBox box) {
		this.mapSelection = box;

		putValue(Action.SHORT_DESCRIPTION, "Change the current map");
		putValue(Action.NAME, "Change map");
		ServerController controller = ServerController.getInstance();
		controller.getServerDefinition().getServerFlag()
				.addListener(new FlagListener<IUT2004Server>() {

					@Override
					public void flagChanged(IUT2004Server changedValue) {
						if (changedValue == null) {
							setEnabled(false);
						} else {
							setEnabled(true);

						}
					}
				});

		IUT2004Server server = controller.getServer();
		if (server == null) {
			setEnabled(false);
			return;
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		ServerController controller = ServerController.getInstance();
		IUT2004Server server = controller.getServer();
		assert server != null;

		// Sending command directly rather then using the provided method.
		// provided method restarts server which conflicts with the restarting
		// server definition.
		// server.setGameMap((String) mapSelection.getSelecteditem());
		MapList selected = mapSelection.getSelected();
		if (selected != null)
			server.getAct().act(new ChangeMap().setMapName(selected.getName()));
	}

}
