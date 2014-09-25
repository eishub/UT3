package nl.tudelft.goal.visualizer.gui.panels;

import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

import nl.tudelft.goal.ut2004.visualizer.gui.action.ChangeMapAction;
import nl.tudelft.goal.ut2004.visualizer.gui.action.PauseResumeAction;
import nl.tudelft.goal.ut2004.visualizer.gui.action.SetSpeedAction;
import nl.tudelft.goal.ut2004.visualizer.gui.widgets.MapBox;

public class ServerPanel extends JPanel {

	private final JButton pause;
	private final JSpinner speedSelection;
	private final JLabel setSpeed;
	private final MapBox mapSelection;
	private final JButton changeMap;

	public ServerPanel() {

		// Add buttons
		this.pause = new JButton(new PauseResumeAction());
		this.speedSelection = new JSpinner(
		// 1 = default speed, 0.1 minimum, 5.0 max. 0.1 step size.
				new SpinnerNumberModel(1, 0.1, 5.0, 0.1));
		this.speedSelection
				.setEditor(new JSpinner.NumberEditor(speedSelection));
		this.setSpeed = new JLabel("GameSpeed");

		this.mapSelection = new MapBox();
		this.changeMap = new JButton("Change map");
		this.changeMap.addActionListener(new ChangeMapAction(mapSelection));

		// Add listeners to buttons
		speedSelection.addChangeListener(new SetSpeedAction(speedSelection));
		//getAct().act(new AddBot(botName, location, rotation, skill, type));

		// Add buttons to frame.
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(pause);
		add(setSpeed);
		add(speedSelection);
		add(mapSelection);
		add(changeMap);

		// Handle frame looks
		TitledBorder title = BorderFactory
				.createTitledBorder("Server Controls");
		setBorder(title);

	}
}
