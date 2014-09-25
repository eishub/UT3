package nl.tudelft.goal.ut2004.messages;

public enum FlagState {

	HOME, HELD, DROPPED;

	public static FlagState valueOfIgnoreCase(String state) {
		return valueOf(state.toUpperCase());
	}

}
