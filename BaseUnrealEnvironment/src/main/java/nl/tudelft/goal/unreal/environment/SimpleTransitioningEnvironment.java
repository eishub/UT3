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

import java.util.Map;

import eis.eis2java.environment.AbstractEnvironment;
import eis.exceptions.ManagementException;
import eis.iilang.EnvironmentState;
import eis.iilang.Parameter;

/**
 * Convenience implementation to simplify dealing with EIS state transitions.
 * This environment hides all transitions from its subclasses and provides a
 * better transition model.
 * 
 * It guarantees that initialize, connect, pause, start and kill are be called in
 * this order. The arrow indicates the direction, the brackets indicate these
 * calls are optional.
 * 
 * initalizeEnvironment() ---> connectEnvironment() ---> [pauseEnvironment
 * [<---> startEnvironment] --->] killEnvironment() ---> initalizeEnvironment()
 * --->
 * 
 * @author mpkorstanje
 * 
 */
public abstract class SimpleTransitioningEnvironment extends AbstractEnvironment {

	/**
	 * Generated serialVersionUID.
	 */
	private static final long serialVersionUID = -8671600742206011276L;


	@Override
	public final synchronized void init(Map<String, Parameter> parameters) throws ManagementException {
		super.init(parameters);

		// Delegate actual initialization to abstract method.
		initializeEnvironment(parameters);

		// Connect to environment
		connectEnvironment();

		// Transition to paused because the EIS spec requires this.
		setState(EnvironmentState.PAUSED);

		// Transition to running because UT can't be started paused.
		setState(EnvironmentState.RUNNING);
		
		// Connect agents once environment state is set to running. 
		// This allows agents to start from the get go.
		connectAgents();
	}
	
	protected abstract void initializeEnvironment(Map<String, Parameter> parameters) throws ManagementException;

	
	protected abstract void connectEnvironment() throws ManagementException;


	protected abstract void connectAgents() throws ManagementException;


	@Override
	public final synchronized void kill() throws ManagementException {

		// Delegate kill to abstract method.
		killEnvironment();
		// Transition complete, set the state.
		super.kill();

	}

	protected abstract void killEnvironment() throws ManagementException;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final synchronized void pause() throws ManagementException {
		// Delegate kill to abstract method.
		pauseEvironment();
		// Transition complete, set the state.
		super.pause();
	}

	protected abstract void pauseEvironment() throws ManagementException;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final synchronized void start() throws ManagementException {
		// Delegate start to abstract method.
		startEnvironment();
		// Transition complete, set the state.
		super.start();
	}

	protected abstract void startEnvironment() throws ManagementException;
}
