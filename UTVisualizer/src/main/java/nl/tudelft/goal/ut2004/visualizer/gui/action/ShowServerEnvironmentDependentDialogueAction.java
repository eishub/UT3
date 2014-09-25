package nl.tudelft.goal.ut2004.visualizer.gui.action;

import java.util.Collection;

import javax.swing.JDialog;

import nl.tudelft.goal.ut2004.visualizer.connection.EnvironmentService;
import nl.tudelft.goal.ut2004.visualizer.controller.ServerController;
import nl.tudelft.goal.ut2004.visualizer.util.CollectionEventAdaptor;

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
public class ShowServerEnvironmentDependentDialogueAction extends
		ShowDialogueAction {

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
	public ShowServerEnvironmentDependentDialogueAction(final JDialog dialog,
			String name, String description) {
		super(dialog, name, description);

		ServerController controller = ServerController.getInstance();
		controller.getServerDefinition().getServerFlag()
				.addListener(new ServerListener());
		controller.getEnvironmentData().getEnvironments()
				.addCollectionListener(new CollectionListener());

		setEnabled(controller.getServer() != null);
	}

	private boolean environmentsAvailable = false;
	private boolean serverAvailable = false;

	private void checkStatus() {
		if (environmentsAvailable && serverAvailable) {
			setEnabled(true);
		} else {
			setEnabled(false);
			dialog.setVisible(false);
		}
	}

	private class CollectionListener extends
			CollectionEventAdaptor<EnvironmentService> {
		@Override
		public void postAddEvent(Collection<EnvironmentService> alreadyAdded,
				Collection<EnvironmentService> whereWereAdded) {
			environmentsAvailable = !whereWereAdded.isEmpty();
			checkStatus() ;
		}

		@Override
		public void postRemoveEvent(
				Collection<EnvironmentService> alreadyRemoved,
				Collection<EnvironmentService> whereWereRemoved) {
			environmentsAvailable = !whereWereRemoved.isEmpty();
			checkStatus() ;
		}
	}

	private class ServerListener implements FlagListener<IUT2004Server> {
		@Override
		public void flagChanged(IUT2004Server changedValue) {
			serverAvailable = changedValue != null;
			checkStatus() ;
		}
	}
}
