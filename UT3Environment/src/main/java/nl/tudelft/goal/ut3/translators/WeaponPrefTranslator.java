package nl.tudelft.goal.ut3.translators;

import cz.cuni.amis.pogamut.ut2004.agent.module.sensor.WeaponPref;
import java.util.LinkedList;

import nl.tudelft.goal.ut3.messages.FireMode;
import cz.cuni.amis.pogamut.ut3.communication.messages.UT3ItemType;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Parameter2Java;
import eis.eis2java.translation.Translator;
import eis.iilang.Function;
import eis.iilang.Parameter;

/**
 * 
 * 
 * Example: weapon(shock_rifle,primary)<br>
 * Example: weapon(bio_rifle,primary)]<br>
 * Example weapon(rocket_launcher,secondary)<br>
 * 
 * @author mpkorstanje
 *
 */
public class WeaponPrefTranslator implements Parameter2Java<WeaponPref> {

	private static final int WEAPON_PARAMETERS = 2;
	private static final String WEAPON_KEYWORD = "weapon";

	@Override
	public WeaponPref translate(Parameter parameter) throws TranslationException {

		if (!(parameter instanceof Function)) {
			String message = String.format("Expected a function but got %s.", parameter);
			throw new TranslationException(message);
		}
		
		Function function = (Function)parameter;
	
		if(!function.getName().equals(WEAPON_KEYWORD)){
			String message = String.format("Expected a function named "+WEAPON_KEYWORD+" but got %s.", parameter);
			throw new TranslationException(message);
		}
		
		LinkedList<Parameter> parameters = function.getParameters();
		if(parameters.size() != WEAPON_PARAMETERS){
			String message = String.format("Expected a function named "+WEAPON_KEYWORD+" with exactly "+WEAPON_PARAMETERS+" arguments but got %s.", parameter);
			throw new TranslationException(message);
		}
		
		UT3ItemType itemType = Translator.getInstance().translate2Java(parameters.getFirst(), UT3ItemType.class);

		
		FireMode fireMode = Translator.getInstance().translate2Java(parameters.getLast(), FireMode.class);

		if(fireMode == FireMode.NONE){
			String message = String.format("Fire mode should be either primary or secondary but got %s.", parameter);
			throw new TranslationException(message);
		}
		
		return new WeaponPref(itemType, fireMode.isPrimary());
		
	}

	@Override
	public Class<WeaponPref> translatesTo() {
		return WeaponPref.class;
	}

}
