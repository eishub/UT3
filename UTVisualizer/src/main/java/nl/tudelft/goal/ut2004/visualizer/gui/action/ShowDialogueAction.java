package nl.tudelft.goal.ut2004.visualizer.gui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;

public class ShowDialogueAction extends AbstractAction {

	protected final JDialog dialog;

	public ShowDialogueAction(JDialog dialog, String name, String description) {
		this.dialog = dialog;

		putValue(Action.SHORT_DESCRIPTION, description);
		putValue(Action.NAME, name);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		dialog.setVisible(true);
	}

}
