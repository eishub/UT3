package nl.tudelft.goal.ut3.messages;

public enum FireMode {

	PRIMARY(1), SECONDARY(2), THIRD(3), NONE(0);

	private int mode;
	private FireMode(int mode){
		this.mode = mode;
	}
	public boolean isPrimary() {
		return this == PRIMARY;
	}
	
	public int id(){
		return mode;
	}

	public static FireMode valueOfIgnoreCase(String firemode) {
		return valueOf(firemode.toUpperCase());
	}

	public static Object valueOf(Boolean primaryShooting, Boolean secondaryShooting) {
		if (primaryShooting != null && primaryShooting)
			return PRIMARY;
		if (secondaryShooting != null && secondaryShooting)
			return SECONDARY;
		return NONE;
	}

	public static Object valueOf(int mode) {
		for(FireMode m : FireMode.values()){
			if(m.mode == mode){
				return m;
			}
		}
		
		throw new IllegalArgumentException();
	}

}
