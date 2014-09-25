/**
 * BaseUnrealEnvironment, an implementation of the environment interface standard that 
 * facilitates the connection between GOAL and the UT2004 engine. 
 * 
 * Copyright (C) 2012 BaseUnrealEnvironment authors.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package nl.tudelft.goal.ut2004.util;


/**
 * Enumerates the available teams.
 * 
 * @author mpkorstanje
 * 
 */
public enum Team {

	RED(0), BLUE(1), GREEN(2), GOLD(3), OTHER(255);

	private final int team;

	private Team(int team) {
		this.team = team;
	}

	public int id() {
		return team;
	}

	public static Team valueOfIgnoresCase(String teamString) {
		return valueOf(teamString.toUpperCase());
	}
	
	public static Team valueOf(int id) {
		for (Team team : values()) {
			if (id == team.id()) {
				return team;
			}
		}
		return OTHER;
	}
}
