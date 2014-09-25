/*
 * Copyright (C) 2010 Unreal Visualizer Authors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.tudelft.goal.ut2004.visualizer.gui.dialogs;

import java.awt.FlowLayout;
import java.awt.Frame;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import nl.tudelft.goal.ut2004.visualizer.gui.action.PauseResumeAction;
import nl.tudelft.goal.ut2004.visualizer.gui.action.SetSpeedAction;
import nl.tudelft.goal.ut2004.visualizer.util.WindowPersistenceHelper;

/**
 * Dialog for changing speed of the game.
 * 
 * @author M.P. Korstanje
 * 
 */
public class ChangeGameSpeedDialog extends JDialog {

	private JSpinner speedSelection;
	private JLabel setSpeed;
	private JButton pauseResume;
	/**
	 * Helper class to persist this window.
	 */
	private WindowPersistenceHelper persistenceHelper;
	
	public ChangeGameSpeedDialog(Frame parent) {
		super(parent, false);
		setTitle("Change Game Speed");

		this.pauseResume = new JButton(new PauseResumeAction());

		this.speedSelection = new JSpinner(
		// 1 = default speed, 0.1 minimum, 10.0 max. 0.1 step size.
				new SpinnerNumberModel(1, 0.1, 10.0, 0.1));
		this.speedSelection
				.setEditor(new JSpinner.NumberEditor(speedSelection));
		this.setSpeed = new JLabel("Game Speed");

		this.speedSelection
				.addChangeListener(new SetSpeedAction(speedSelection));

		setLayout(new FlowLayout());
		add(pauseResume);
		add(setSpeed);
		add(speedSelection);

		this.setSize(400, 75);
		// Setup persistence
		persistenceHelper = new WindowPersistenceHelper(this);
		persistenceHelper.load();

	}
}
