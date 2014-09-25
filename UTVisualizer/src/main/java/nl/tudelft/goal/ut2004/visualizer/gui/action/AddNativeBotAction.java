package nl.tudelft.goal.ut2004.visualizer.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import nl.tudelft.goal.ut2004.visualizer.controller.ServerController;
import nl.tudelft.goal.ut2004.visualizer.gui.widgets.WaypointBox;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.AddBot;
import cz.cuni.amis.pogamut.ut2004.communication.worldview.map.Waypoint;
import cz.cuni.amis.pogamut.ut2004.server.IUT2004Server;

public class AddNativeBotAction implements ActionListener {

	private final JTextField botName;
	private final nl.tudelft.goal.ut2004.visualizer.gui.widgets.WaypointBox location;
	private final JSpinner skill;
	private final JComboBox team;

	public AddNativeBotAction(JTextField botName, WaypointBox location,
			JSpinner skill, JComboBox team) {
		this.botName = botName;
		this.location = location;
		this.skill = skill;
		this.team = team;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		ServerController controller = ServerController.getInstance();
		IUT2004Server server = controller.getServer();
		assert server != null;

		// getAct().act(new AddBot(botName, location, rotation, skill, type));

		Waypoint waypoint = location.getSelected();
		Location location = null;
		if (waypoint != null) {
			location = waypoint.getLocation();
		}

		String botName = this.botName.getText();
		Integer skill = (Integer) this.skill.getValue();

		String selectedTeam = (String) team.getSelectedItem();
		
		Integer team = null;
		if (selectedTeam.equals("Red")) {
			team = 0;
		} else if (selectedTeam.equals("Blue")) {
			team = 1;
		}

		AddBot addBot = new AddBot(botName, location, null, skill, team, null);
		server.getAct().act(addBot);
	}

}
