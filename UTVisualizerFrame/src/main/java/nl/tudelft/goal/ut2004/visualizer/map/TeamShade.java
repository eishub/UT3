/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.tudelft.goal.ut2004.visualizer.map;

import java.awt.Color;

/**
 * Generator of various colors that are suitable for representation of agents in
 * the map.
 * 
 * @author Honza
 */
public class TeamShade {

	private Color personalBase;
	private Color personalRed;
	private Color personalBlue;

	private Color redTeam;
	private Color blueTeam;

	private static final double TEAM_COLOR_STRENGHT = 0.25;

	public TeamShade(){
		this(randomColor());
	}
	
	public TeamShade(Color personalColor) {
		this(personalColor, Color.RED, Color.BLUE);
	}

	public TeamShade(Color redteam, Color blueTeam) {
		this(randomColor(), redteam, blueTeam);
	}

	public TeamShade(Color personalColor, Color redTeam, Color blueTeam) {
		this.personalBase = personalColor;
		this.redTeam = redTeam;
		this.blueTeam = blueTeam;

		personalRed = mix(redTeam, personalBase, TEAM_COLOR_STRENGHT);
		personalBlue = mix(blueTeam, personalBase, TEAM_COLOR_STRENGHT);
	}

	public Color getColor(int team) {
		switch (team) {
		case 0:
			return personalRed;
		case 1:
			return personalBlue;
		default:
			return personalBase;
		}
	}

	/**
	 * Create a mixed color from passed color and this color in following
	 * fashion:
	 * 
	 * <pre>
	 * this * (1 - portion) + mixing * portion
	 * </pre>
	 * 
	 * @param mixing
	 *            Color that will be mixed with this one.
	 * @param portion
	 *            how much of mixing color will be used
	 * @return Mixed color, not that this color won't be changed
	 */
	private Color mix(Color a, Color b, double portion) {
		double thisPortion = 1 - portion;
		return new Color(
		// Red
				(int) (thisPortion * a.getRed()) + (int) (portion * b.getRed()),
				// Green
				(int) (thisPortion * a.getGreen()) + (int) (portion * b.getGreen()),
				// Blue
				(int) (thisPortion * a.getBlue()) + (int) (portion * b.getBlue()),
				// Alpha
				(int) (thisPortion * a.getAlpha()) + (int) (portion * b.getAlpha()));
	}

	private static Color randomColor() {
		return new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
	}

	public Color getRedTeam() {
		return redTeam;
	}

	public void setRedTeam(Color redTeam) {
		this.redTeam = redTeam;
		personalRed = mix(redTeam, personalBase, TEAM_COLOR_STRENGHT);
	}

	public Color getBlueTeam() {
		return blueTeam;
	}

	public void setBlueTeam(Color blueTeam) {
		this.blueTeam = blueTeam;
		personalBlue = mix(blueTeam, personalBase, TEAM_COLOR_STRENGHT);
	}

}
