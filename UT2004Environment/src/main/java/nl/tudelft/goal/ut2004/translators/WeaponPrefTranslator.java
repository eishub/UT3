package nl.tudelft.goal.ut2004.translators;

import java.util.LinkedList;

import nl.tudelft.goal.ut2004.messages.FireMode;
import cz.cuni.amis.pogamut.ut2004.agent.module.sensor.WeaponPref;
import cz.cuni.amis.pogamut.ut2004.communication.messages.ItemType;
import cz.cuni.amis.pogamut.ut2004.communication.messages.ItemType.Category;
import cz.cuni.amis.pogamut.ut2004.communication.messages.UT2004ItemType.UT2004Group;
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
		
		UT2004Group group = Translator.getInstance().translate2Java(parameters.getFirst(), UT2004Group.class);

		ItemType itemType = getWeapon(group);
		
		FireMode fireMode = Translator.getInstance().translate2Java(parameters.getLast(), FireMode.class);

		if(fireMode == FireMode.NONE){
			String message = String.format("Fire mode should be either primary or secondary but got %s.", parameter);
			throw new TranslationException(message);
		}
		
		return new WeaponPref(itemType, fireMode.isPrimary());
		
	}

	private ItemType getWeapon(UT2004Group group) throws TranslationException {
		for(ItemType item : group.getTypes()){
			if(item.getCategory() == Category.WEAPON){
				return item;
			}
		}
		
		String message = String.format("%s was not a ItemType in the weapon category.", group);
		throw new TranslationException(message);
	}

	@Override
	public Class<WeaponPref> translatesTo() {
		return WeaponPref.class;
	}

}
