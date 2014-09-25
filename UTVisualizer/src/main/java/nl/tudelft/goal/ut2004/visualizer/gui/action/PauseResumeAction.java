package nl.tudelft.goal.ut2004.visualizer.gui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import nl.tudelft.goal.ut2004.visualizer.controller.ServerController;

import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.Pause;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.GameInfo;
import cz.cuni.amis.pogamut.ut2004.server.IUT2004Server;
import cz.cuni.amis.utils.flag.FlagListener;

public class PauseResumeAction extends AbstractAction {

	public PauseResumeAction() {
		putValue(Action.SHORT_DESCRIPTION, "Pause or resume the game");

		ServerController controller = ServerController.getInstance();
		controller.getServerDefinition().getServerFlag()
				.addListener(new FlagListener<IUT2004Server>() {

					@Override
					public void flagChanged(IUT2004Server changedValue) {
						if (changedValue == null) {
							setEnabled(false);
						} else {
							setEnabled(true);
							updateStatus();

						}
					}
				});

		updateStatus();
	}

	private void updateStatus() {

		ServerController controller = ServerController.getInstance();
		IUT2004Server server = controller.getServer();

		if (server == null) {
			putValue(Action.NAME, "Pause");
			setEnabled(false);
			return;
		}

		setEnabled(true);
		GameInfo info = server.getWorldView().getSingle(GameInfo.class);

		if (info.isGamePaused()) {
			putValue(Action.NAME, "Resume");
		} else {
			putValue(Action.NAME, "Pause");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ServerController controller = ServerController.getInstance();
		IUT2004Server server = controller.getServer();

		assert server != null;

		GameInfo info = server.getWorldView().getSingle(GameInfo.class);

		Pause pauseResume;

		if (info.isGamePaused()) {
			pauseResume = new Pause(false, false);
			putValue(Action.NAME, "Pause");
		} else {
			pauseResume = new Pause(true, true);
			putValue(Action.NAME, "Resume");
		}

		server.getAct().act(pauseResume);
	}

}
