package nl.tudelft.goal.unreal.messages;

import java.util.logging.Level;

import nl.tudelft.goal.ut2004.util.Skin;

public enum BotParametersKey implements Key {

	/**
	 * Name of the bot
	 */
	NAME("name"),
	/**
	 * Weather or not the bot aims ahead of the target.
	 * 
	 * Either "true" of "false".
	 */
	LEADTARGET("leadTarget"),

	/**
	 * Log level used. Controls how many messages are displayed on the console.
	 * 
	 * Valid log levels are any from {@link Level}.
	 */
	LOGLEVEL("logLevel"),
	/**
	 * Skill of the bot between 0 (poor) and 7 (good).
	 * 
	 * Controls how well the bot aims.
	 */
	SKILL("skill"),
	/**
	 * Skin used by the bot.
	 * 
	 * Any one of {@link Skin} will do.
	 */
	SKIN("skin"),
	/**
	 * Team of the bot.
	 * 
	 * Either 0 (red) or 1 (blue).
	 * 
	 */
	TEAM("team"),
	/**
	 * Start location for the bot.
	 */
	STARTLOCATION("startLocation"), 
	/**
	 * Start rotation for the bot.
	 */
	STARTROTATION("startRotation");


	// Human readable (camelCase) form of the enum.
	private String key;

	private BotParametersKey(String name) {
		this.key = name;
	}

	/**
	 * 
	 * @return a human readable name.
	 */
	@Override
	public String toString() {
		return key;
	}

	/**
	 * 
	 * @return a list of valid values as a string.
	 */
	private static String listValid() {
		String ret = "";

		ConfigurationKey[] keys = ConfigurationKey.values();
		for (int i = 0; i < keys.length; i++) {
			ret += keys[i].toString();

			if (i < keys.length - 1)
				ret += ", ";
		}
		return ret;
	}

	/**
	 * Returns the enum with the value of the string. matches.
	 * 
	 * @param value
	 * @return an ParameterKey.
	 * @throws IllegalArgumentException
	 *             if the provided value was not a valid parameter key.
	 */
	public static BotParametersKey parseKey(String value) throws IllegalArgumentException {
		assert value != null;

		for (BotParametersKey key : BotParametersKey.values()) {
			if (key.key.equalsIgnoreCase(value)) {
				return key;
			}
		}

		String message = "%s is not a valid parameter key. Valid keys are: %s.";
		message = String.format(message, value, listValid());
		throw new IllegalArgumentException(message);
	}

	public String getKey() {
		return key;
	}
}
