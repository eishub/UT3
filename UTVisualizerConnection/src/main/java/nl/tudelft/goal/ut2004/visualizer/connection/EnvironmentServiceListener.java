package nl.tudelft.goal.ut2004.visualizer.connection;

import eis.exceptions.ManagementException;

public interface EnvironmentServiceListener {

	void addBot(AddBotCommand parameters) throws ManagementException ;

}
