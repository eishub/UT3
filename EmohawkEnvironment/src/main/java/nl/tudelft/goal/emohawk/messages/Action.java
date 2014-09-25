package nl.tudelft.goal.emohawk.messages;

import cz.cuni.amis.utils.exception.PogamutException;

public interface Action {

	void execute() throws PogamutException;

}
