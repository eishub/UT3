/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.tudelft.goal.ut2004.visualizer.map;

import java.awt.Color;
import java.util.Random;

/**
 * Generator of various colors that are suitable for representation of agents in
 * the map. They have to unique, different from each other for user to
 * differentiate and nice to look at.
 * 
 * FIXME: Implement
 * 
 * @author Honza
 */
public class MapColorGenerator {
	private Random random = new Random();

	/**
	 * Create a new color according to specification (see class description)
	 * 
	 * @return unique pretty color
	 */
	private Color getUniqueColor() {
		return new Color(random.nextFloat(), random.nextFloat(),
				random.nextFloat());
	}

	public MapColor getUniqueTeamColor() {
		return new MapColor(getUniqueColor());
	}

	public class MapColor {
		private final Color otherTeam;
		private final Color redTeam;
		private final Color blueTeam;

		private static final double TEAM_COLOR_STRENGHT = 0.25;

		public MapColor(Color baseColor) {
			otherTeam = baseColor;
			redTeam = mix(Color.red, baseColor, TEAM_COLOR_STRENGHT);
			blueTeam = mix(Color.blue, baseColor, TEAM_COLOR_STRENGHT);
		}

		public Color getColor(int team) {
			switch (team) {
			case 0:
				return redTeam;
			case 1:
				return blueTeam;
			default:
				return otherTeam;
			}
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
				(int) (thisPortion * a.getGreen())
						+ (int) (portion * b.getGreen()),
				// Blue
				(int) (thisPortion * a.getBlue())
						+ (int) (portion * b.getBlue()),
				// Alpha
				(int) (thisPortion * a.getAlpha())
						+ (int) (portion * b.getAlpha()));
	}

	/**
	 * This is used to tell the generator that this particular color has been
	 * freed and can be used again. Generator should try to generate colors so
	 * the are as different as possible, so it can use similar shade (or even
	 * same color).
	 * 
	 * @param color
	 *            Color that is no longer needed by map.
	 */
	public void freeColor(Color color) {

	}

}
