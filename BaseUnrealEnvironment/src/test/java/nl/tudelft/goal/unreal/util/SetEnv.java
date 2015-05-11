package nl.tudelft.goal.unreal.util;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SetEnv {

	/**
	 * Check that new vars appear in env and Check that existing vars do not
	 * disappear after adding new one. We test these two at once, because this
	 * change will persist for the whole unit test (they all run in one JVM)
	 */
	@Test
	public void addEnvVar() {
		final String var = "test";
		final String value = "testvalue";

		int oldsize = System.getenv().size();

		Map<String, String> vars = new HashMap<String, String>();
		vars.put(var, value);
		EnvironmentUtil.setEnv(vars);

		assertEquals(value, System.getenv(var));
		assertEquals(oldsize + 1, System.getenv().size());
	}

}