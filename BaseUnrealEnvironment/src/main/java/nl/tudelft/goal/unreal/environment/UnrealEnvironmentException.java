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
package nl.tudelft.goal.unreal.environment;


public class UnrealEnvironmentException extends Exception {

	public UnrealEnvironmentException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	public UnrealEnvironmentException(Throwable e) {
		super(e);
	}

	public UnrealEnvironmentException(String message) {
		super(message);
	}

	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 8336479361070728436L;

}
