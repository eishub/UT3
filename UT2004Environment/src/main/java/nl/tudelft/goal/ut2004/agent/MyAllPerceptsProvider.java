package nl.tudelft.goal.ut2004.agent;

import eis.eis2java.util.AllPerceptsProvider;

/**
 * class to extend the {@link AllPerceptsProvider} with a ready notification
 * mechanism. This allows us to register the entity only after the percepts come
 * available.
 * 
 * @author W.Pasman jan15
 *
 */
public interface MyAllPerceptsProvider extends AllPerceptsProvider {

	/**
	 * Sets the listener for the AllPerceptsProvider. Simple method, to keep the
	 * implementation simple as there will be 1 listener only.
	 * 
	 * @param listener
	 *            the listener that needs to be notified when percepts are ready
	 *            for use.
	 */
	public void setPerceptsReadyListener(PerceptsReadyListener listener);
}
