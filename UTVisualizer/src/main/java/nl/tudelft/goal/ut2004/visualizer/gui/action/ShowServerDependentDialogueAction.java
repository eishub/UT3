package nl.tudelft.goal.ut2004.visualizer.gui.action;

import javax.swing.JDialog;

import nl.tudelft.goal.ut2004.visualizer.controller.ServerController;
import cz.cuni.amis.pogamut.ut2004.server.IUT2004Server;
import cz.cuni.amis.utils.flag.FlagListener;

/**
 * Action that is active or inactive depending on the state of the connection to
 * the server. When the action is active it will launch a dialoque. This
 * dialoque will be hidden when the connection to the server is lost.
 * 
 * @author mpkorstanje
 * 
 */
public class ShowServerDependentDialogueAction extends ShowDialogueAction {

	/**
	 * Constructs the action.
	 * 
	 * @param dialog
	 *            to show when the action is used.
	 * @param name
	 *            this action.
	 * @param description
	 *            to show when a user hovers on this action.
	 */
	public ShowServerDependentDialogueAction(final JDialog dialog, String name,
			String description) {
		super(dialog, name, description);

		ServerController controller = ServerController.getInstance();
		controller.getServerDefinition().getServerFlag()
				.addListener(new FlagListener<IUT2004Server>() {

					@Override
					public void flagChanged(IUT2004Server changedValue) {
						if (changedValue == null) {
							setEnabled(false);
							dialog.setVisible(false);
						} else {
							setEnabled(true);
						}
					}
				});

		setEnabled(controller.getServer() != null);
	}

}
