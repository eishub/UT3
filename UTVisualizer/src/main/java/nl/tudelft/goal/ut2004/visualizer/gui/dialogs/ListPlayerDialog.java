package nl.tudelft.goal.ut2004.visualizer.gui.dialogs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import nl.tudelft.goal.ut2004.visualizer.controller.ServerController;
import nl.tudelft.goal.ut2004.visualizer.gui.action.KickAction;
import nl.tudelft.goal.ut2004.visualizer.gui.action.RespawnRandomAction;
import nl.tudelft.goal.ut2004.visualizer.util.CollectionEventAdaptor;
import nl.tudelft.goal.ut2004.visualizer.util.WindowPersistenceHelper;

import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.Player;
import cz.cuni.amis.utils.collections.ObservableCollection;

public class ListPlayerDialog extends JDialog {

	/**
	 * Helper class to persist this window.
	 */
	private WindowPersistenceHelper persistenceHelper;
	
	public ListPlayerDialog(Frame parent) {
		super(parent, false);

		setTitle("Players");
		add(new PlayerDisplayList());
		this.setSize(300, 500);
		// Setup persistence
		persistenceHelper = new WindowPersistenceHelper(this);
		persistenceHelper.load();
	}

	private class PlayerDisplayList extends JPanel {

		private HashMap<UnrealId, PlayerDisplay> displays = new HashMap<UnrealId, ListPlayerDialog.PlayerDisplay>();
		private Component verticalGlue = new Box.Filler(new Dimension(), new Dimension(), new Dimension(0, Integer.MAX_VALUE));
		
		public PlayerDisplayList() {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS) );
			add(verticalGlue);

			ObservableCollection<Player> players = ServerController.getInstance().getGameData().getPlayers();

			updateList(players);
			validate();
			
			players.addCollectionListener(new CollectionEventAdaptor<Player>() {

				@Override
				public void postAddEvent(Collection<Player> alreadyAdded, Collection<Player> whereWereAdded) {
					updateList(whereWereAdded);

				}

				@Override
				public void postRemoveEvent(Collection<Player> alreadyAdded, Collection<Player> whereWereRemoved) {
					updateList(whereWereRemoved);
				}	
			});
		}

		private void updateList(Collection<Player> players) {
			remove(verticalGlue);
			
			// Add new elements
			for(Player player : players){
				PlayerDisplay display = displays.get(player.getId());
				if(display == null){
					display = new PlayerDisplay(player);
					displays.put(player.getId(), display);
					add(display);
				} 
			}

			// Remove those that are no longer present
			// We first check which players are still in the display list 
			// but not in the current group using the unrealIds of the players.
			// Not ideal but fastest solution there is.
			Set<UnrealId> ids = new HashSet<UnrealId>(displays.keySet());
			for(Player player : players){
				ids.remove(player.getId());
			}
			for(UnrealId id : ids){
				PlayerDisplay display = displays.remove(id);
				remove(display);
			}
			
			add(verticalGlue);
			
			validate();
			repaint();
		}
	}

	private class PlayerDisplay extends JPanel {

		private Player player;

		public PlayerDisplay(Player player) {
			this.player = player;

			this.setLayout(new FlowLayout(FlowLayout.LEFT));
			this.add(new JButton(new KickAction(player)));
			this.add(new JButton(new RespawnRandomAction(player)));
			this.add(new JLabel(player.getName()));
		}
	}
}
