package nl.tudelft.goal.unreal.util;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

	/**
	 * Add a set of environment variables to the environment vars. Overwrites
	 * previous values if already existing. You probably should not use null key
	 * and values. Dirty hack from http://stackoverflow.com/questions
	 * /318239/how-do-i-set-environment-variables-from-java. Modified that code,
	 * it did not add but clear values on OSX. This does not change anything in
	 * your OS, it just tells java to use some value for that variable.
	 */
	public static void setEnv(Map<String, String> newenv) {
		// copy before starting to mess with it.
		Map<String, String> origEnv = new HashMap<String, String>(
				System.getenv());

		try {
			Class<?> processEnvironmentClass = Class
					.forName("java.lang.ProcessEnvironment");
			Field theEnvironmentField = processEnvironmentClass
					.getDeclaredField("theEnvironment");
			theEnvironmentField.setAccessible(true);
			Map<String, String> env = (Map<String, String>) theEnvironmentField
					.get(null);
			env.putAll(newenv);
			Field theCaseInsensitiveEnvironmentField = processEnvironmentClass
					.getDeclaredField("theCaseInsensitiveEnvironment");
			theCaseInsensitiveEnvironmentField.setAccessible(true);
			Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField
					.get(null);
			cienv.putAll(newenv);
		} catch (NoSuchFieldException e) {
			try {
				Class[] classes = Collections.class.getDeclaredClasses();
				Map<String, String> env = System.getenv();
				for (Class cl : classes) {
					if ("java.util.Collections$UnmodifiableMap".equals(cl
							.getName())) {
						Field field = cl.getDeclaredField("m");
						field.setAccessible(true);
						Object obj = field.get(env);
						Map<String, String> map = (Map<String, String>) obj;
						/**
						 * we must clear original first, as it has
						 * ProcessEnvironment?$Variable instead of String
						 * objects under the hood.
						 */
						map.clear();
						map.putAll(origEnv);
						map.putAll(newenv);
					}
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
