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

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;

import nl.tudelft.goal.ut2004.visualizer.timeline.map.FlagRenderer;
import nl.tudelft.goal.ut2004.visualizer.timeline.map.MapRenderer;
import nl.tudelft.goal.ut2004.visualizer.timeline.map.PlayerRenderer;
import nl.tudelft.goal.ut2004.visualizer.timeline.map.WaypointRenderer;
import nl.tudelft.goal.ut2004.visualizer.util.WindowPersistenceHelper;

/**
 * Dialog to connect to the server.
 * 
 * @author M.P. Korstanje
 * 
 */
public class SettingsDialog extends JDialog {

	/**
	 * Helper class to persist this window.
	 */
	private WindowPersistenceHelper persistenceHelper;

	public SettingsDialog(Frame parent) {
		super(parent, false);

		setTitle("Visualizer Settings");

		this.setSize(200, 200);
		// Setup persistence
		persistenceHelper = new WindowPersistenceHelper(this);
		persistenceHelper.load();

		this.setLayout(new FlowLayout());

		JButton selectHighColor = new JButton("Change Map High Color");
		selectHighColor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Color current = MapRenderer.getHighColor();
				Color selected = JColorChooser.showDialog(SettingsDialog.this, "Pick a color", current);
				MapRenderer.setHighColor(selected);
			}
		});
		add(selectHighColor);

		JButton selectLowColor = new JButton("Change Map Low Color");
		selectLowColor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Color current = MapRenderer.getLowColor();
				Color selected = JColorChooser.showDialog(SettingsDialog.this, "Pick a color", current);
				if (selected == null)
					return;
				MapRenderer.setLowColor(selected);
			}
		});
		add(selectLowColor);

		JButton selectNavpointColor = new JButton("Change Navpoint Color");
		selectNavpointColor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Color current = WaypointRenderer.getColor();
				Color selected = JColorChooser.showDialog(SettingsDialog.this, "Pick a color", current);
				if (selected == null)
					return;
				WaypointRenderer.setColor(selected);
			}
		});
		
		new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		};
		add(selectNavpointColor);

		JButton selectRedTeamColor = new JButton("Change Red Team Color");
		selectRedTeamColor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				{
					Color current = FlagRenderer.getRedFlagColor();
					Color selected = JColorChooser.showDialog(SettingsDialog.this, "Pick a color", current);
					if (selected == null)
						return;
					
					
					FlagRenderer.setRedFlagColor(selected);
					PlayerRenderer.setRedTeamColor(selected);
				}

			}
		});
		add(selectRedTeamColor);

		JButton selectBlueTeamColor = new JButton("Change Blue Team Color");
		selectBlueTeamColor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				{
					Color current = FlagRenderer.getBlueFlagColor();
					Color selected = JColorChooser.showDialog(SettingsDialog.this, "Pick a color", current);
					if (selected == null)
						return;
					FlagRenderer.setBlueFlagColor(selected);
					PlayerRenderer.setBlueTeamColor(selected);

				}
			}
		});
		add(selectBlueTeamColor);
	}

}
