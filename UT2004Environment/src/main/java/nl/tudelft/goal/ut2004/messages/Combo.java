package nl.tudelft.goal.ut2004.messages;

public enum Combo {

		BERSERK("xGame.ComboBerserk"), 
		BOOSTER("xGame.ComboDefensive"), 
		INVISIBILITY("xGame.ComboInvis"), 
		SPEED("xGame.ComboSpeed");
	
	private String utName;
	
	private Combo(String utName){
		this.utName = utName;
	}
	
	@Override
	public String toString(){
		return utName;
	}

	public static Combo valueOfIgnoresCase(String comboString) {
		return Combo.valueOf(comboString.toUpperCase());
	}
	
	public static Combo parseCombo(String comboString){
		for(Combo c : Combo.values()){
			if(c.utName.equals(comboString)){
				return c;
			}
		}
		
		return null;
	}
}
