package nl.tudelft.goal.ut2004.agent;

/**
 * Callback when percepts of {@link MyAllPerceptsProvider} are ready to be used.
 * 
 * @author W.Pasman jan15
 *
 */
public interface PerceptsReadyListener {
	/**
	 * Called when the percepts provider is ready to deliver 1st percept.
	 */
	void notifyPerceptsReady();

}
