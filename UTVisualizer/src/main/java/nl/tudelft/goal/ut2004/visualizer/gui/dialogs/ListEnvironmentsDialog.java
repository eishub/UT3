package nl.tudelft.goal.ut2004.visualizer.gui.dialogs;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.Collection;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import nl.tudelft.goal.ut2004.visualizer.connection.EnvironmentService;
import nl.tudelft.goal.ut2004.visualizer.controller.ServerController;
import nl.tudelft.goal.ut2004.visualizer.data.EnvironmentData;
import nl.tudelft.goal.ut2004.visualizer.util.CollectionEventAdaptor;
import nl.tudelft.goal.ut2004.visualizer.util.SelectableEnvironment;
import nl.tudelft.goal.ut2004.visualizer.util.WindowPersistenceHelper;


import cz.cuni.amis.utils.collections.ObservableSet;

public class ListEnvironmentsDialog extends JDialog {

	private final JList environmentList;
	/**
	 * Helper class to persist this window.
	 */
	private WindowPersistenceHelper persistenceHelper;
	
	public ListEnvironmentsDialog(Frame parent) {
		super(parent, false);

		setTitle("Environments");
		setLayout(new FlowLayout());
		
		// Setup persistence
		persistenceHelper = new WindowPersistenceHelper(this);
		persistenceHelper.load();
		
		

		ServerController controller = ServerController.getInstance();
		EnvironmentData data = controller.getEnvironmentData();
		ObservableSet<EnvironmentService> clients = data.getEnvironments();
		Collection<SelectableEnvironment> s = SelectableEnvironment
				.fromCollection(clients);
		this.environmentList = new JList(s.toArray());
		this.environmentList
				.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		clients.addCollectionListener(new CollectionEventAdaptor<EnvironmentService>() {

			@Override
			public void postAddEvent(
					Collection<EnvironmentService> alreadyAdded,
					final Collection<EnvironmentService> whereWereAdded) {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						Collection<SelectableEnvironment> s = SelectableEnvironment
								.fromCollection(whereWereAdded);
						environmentList.setModel(new DefaultComboBoxModel(s
								.toArray()));
					}
				});
			}

			@Override
			public void postRemoveEvent(
					Collection<EnvironmentService> alreadyRemoved,
					final Collection<EnvironmentService> whereWereRemoved) {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						Collection<SelectableEnvironment> s = SelectableEnvironment
								.fromCollection(whereWereRemoved);
						environmentList.setModel(new DefaultComboBoxModel(s
								.toArray()));
					}
				});
			}
		});

		add(environmentList);
		
		setSize(400, 225);

	}

}
