package nl.tudelft.goal.ut2004.visualizer.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import nl.tudelft.goal.ut2004.util.Team;
import nl.tudelft.goal.ut2004.visualizer.connection.AddBotCommand;
import nl.tudelft.goal.ut2004.visualizer.connection.EnvironmentService;
import nl.tudelft.goal.ut2004.visualizer.gui.widgets.WaypointBox;
import nl.tudelft.goal.ut2004.visualizer.util.SelectableEnvironment;

import eis.exceptions.ManagementException;

public class AddUnrealGoalBotAction implements ActionListener {

	private final JTextField botName;
	private final nl.tudelft.goal.ut2004.visualizer.gui.widgets.WaypointBox location;
	private final JSpinner skill;
	private final JComboBox teamList;
	private final JComboBox environmentList;

	public AddUnrealGoalBotAction(JTextField botName, WaypointBox location,
			JSpinner skill, JComboBox teamList, JComboBox environmentList) {
		this.botName = botName;
		this.location = location;
		this.skill = skill;
		this.teamList = teamList;
		this.environmentList = environmentList;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		SelectableEnvironment se = (SelectableEnvironment) environmentList
				.getSelectedItem();
		final EnvironmentService environment = se.getItem();

		final AddBotCommand addBot = new AddBotCommand();
		addBot.setBotName(botName.getText());
		addBot.setSkill((Integer) skill.getValue());
		addBot.setTeam((Team) teamList.getSelectedItem());

		//TODO: Add thread task to list and add to environment panel.
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					environment.addBot(addBot);
				} catch (RemoteException e1) {
					e1.printStackTrace();

					JOptionPane
							.showMessageDialog(
									null,
									"We could not connect to the Unreal Goal environment. A stack strace has been printed to your console.",
									"An Error has Occured",
									JOptionPane.ERROR_MESSAGE);

				} catch (ManagementException e2) {
					e2.printStackTrace();
					JOptionPane
							.showMessageDialog(
									null,
									"The environment was unable to add a bot. A stack strace has been printed to your console.",
									"An Error has Occured",
									JOptionPane.ERROR_MESSAGE);
				}
			}
		}).start();
	}

}
