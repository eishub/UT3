package nl.tudelft.goal.unreal.messages;

import java.util.ArrayList;

/**
 * Simple wrapper to describe a percept as a list of of Objects. These
 * objects are then translated by EIS2Java.
 * 
 * @author mpkorstanje
 * 
 */
public class Percept extends ArrayList<Object> {

	/**
	 * Serial version UID is date.
	 */
	private static final long serialVersionUID = 201205071333L;

	public Percept(Object... arguments) {
		for (Object o : arguments) {
			add(o);
		}
	}

}
