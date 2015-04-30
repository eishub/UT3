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
package nl.tudelft.goal.unreal.messages;

import java.util.logging.Level;

/**
 * List of valid parameter keys that can be used to initialize the environment.
 * 
 * 
 * @author M.P. Korstanje
 * 
 */
public enum ConfigurationKey implements Key {

	/**
	 * List of bots
	 * 
	 */
	BOTS("bots"),

	/**
	 * List of native bots (not connected with GOAL)
	 */
	NATIVE_BOTS("nativebots"),
	/**
	 * Address of the server.
	 * 
	 * Should be of the form protocol//host:port
	 */

	BOT_SERVER("botServer"),
	/**
	 * Address of the server.
	 * 
	 * Should be of the form protocol//host:port
	 */
	CONTROL_SERVER("controlServer"),

	/**
	 * Log level used. Controls how many messages are displayed on the console.
	 * 
	 * Valid log levels are any from {@link Level}.
	 */
	LOGLEVEL("logLevel"),

	/**
	 * Address for the visualizer service.
	 * 
	 */
	VISUALIZER_SERVER("visualizer");

	// Human readable (camelCase) form of the enum.
	private String key;

	private ConfigurationKey(String name) {
		this.key = name;
	}

	@Override
	public String getKey() {

		return key;
	}
}