package nl.tudelft.goal.ut2004.visualizer.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nl.tudelft.goal.ut2004.visualizer.controller.ServerController;

import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.SetGameSpeed;
import cz.cuni.amis.pogamut.ut2004.server.IUT2004Server;

public class SetSpeedAction implements ActionListener, ChangeListener {

	private final JSpinner speedSelection;

	public SetSpeedAction(JSpinner speedSelection) {
		this.speedSelection = speedSelection;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		setSpeed();
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		setSpeed();
	}

	private void setSpeed() {
		ServerController controller = ServerController.getInstance();
		IUT2004Server server = controller.getServer();
		assert server != null;

		double speed = (Double) speedSelection.getValue();

		// Limiting speed to 10. Documentation of indicates
		// SetGameSpeed indicates this is reasonable.
		speed = Math.max(0.1, Math.min(10, speed));

		SetGameSpeed setSpeed = new SetGameSpeed(speed);
		server.getAct().act(setSpeed);
	}

}
