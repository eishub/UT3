package nl.tudelft.goal.ut3.messages;

public enum FlagState {

	HOME, HELD, DROPPED;

	public static FlagState valueOfIgnoreCase(String state) {
		return valueOf(state.toUpperCase());
	}

}
