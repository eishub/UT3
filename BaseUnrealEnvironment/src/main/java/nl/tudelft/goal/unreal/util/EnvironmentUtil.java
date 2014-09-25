package nl.tudelft.goal.unreal.util;

import nl.tudelft.goal.unreal.messages.Key;
import cz.cuni.amis.pogamut.base.agent.IAgentId;

public class EnvironmentUtil {

	/**
	 * Simplifies a given string representation of an AgentID by removing the
	 * UUID making it human readable.
	 * 
	 * By the specs of the agentID this should still result in a unique but
	 * readable ID.
	 * 
	 * @param iAgentId
	 * @return the agentID without the UUID.
	 */
	public static String simplefyID(IAgentId agentID) {
		String token = agentID.getToken();
		int index = token.lastIndexOf('/');
		// If the expected separator could not be found, use the whole string.
		if (index < 0) {
			return token;
		}

		return token.substring(0, index);
	}

	/**
	 * Returns a comma separated string of enum constants. When the enum
	 * implements the {@link Key} interface the human readable version will be
	 * used.
	 * 
	 * @param type
	 * @return
	 */
	public static <E extends Enum<E>> String listValid(Class<E> type) {

		String ret = "";

		E[] keys = type.getEnumConstants();
		for (int i = 0; i < keys.length; i++) {

			if (keys[i] instanceof Key) {
				ret += ((Key) keys[i]).getKey();
			} else {
				ret += keys[i].name().toLowerCase();
			}

			if (i < keys.length - 1)
				ret += ", ";
		}
		return ret;
	}

}
