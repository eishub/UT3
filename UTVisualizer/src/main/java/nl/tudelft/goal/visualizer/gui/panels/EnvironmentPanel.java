package nl.tudelft.goal.visualizer.gui.panels;

import java.util.Collection;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import nl.tudelft.goal.ut2004.visualizer.connection.EnvironmentService;
import nl.tudelft.goal.ut2004.visualizer.controller.ServerController;
import nl.tudelft.goal.ut2004.visualizer.data.EnvironmentData;
import nl.tudelft.goal.ut2004.visualizer.util.CollectionEventAdaptor;

import cz.cuni.amis.utils.collections.ObservableSet;

public class EnvironmentPanel extends JPanel {

    private JList connected;

    public EnvironmentPanel() {

        EnvironmentData data = ServerController.getInstance()
                .getEnvironmentData();

        data.getEnvironments().addCollectionListener(
                new CollectionEventAdaptor<EnvironmentService>() {
            @Override
            public void postAddEvent(
                    Collection<EnvironmentService> alreadyAdded,
                    Collection<EnvironmentService> whereWereAdded) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        updateText();
                    }
                });

            }

            @Override
            public void postRemoveEvent(
                    Collection<EnvironmentService> alreadyAdded,
                    Collection<EnvironmentService> whereWereRemoved) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        updateText();
                    }
                });
            }
        });

        updateText();
    }

    private void updateText() {
        EnvironmentData environments = ServerController.getInstance()
                .getEnvironmentData();
        ObservableSet<EnvironmentService> clients = environments.getEnvironments();

        removeAll();

        synchronized (clients) {
            connected = new JList(clients.toArray());
        }

        add(connected);

    }
}
