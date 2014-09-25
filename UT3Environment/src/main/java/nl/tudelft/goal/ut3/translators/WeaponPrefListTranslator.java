package nl.tudelft.goal.ut3.translators;


import cz.cuni.amis.pogamut.ut2004.agent.module.sensor.WeaponPref;
import nl.tudelft.goal.ut3.messages.WeaponPrefList;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Parameter2Java;
import eis.eis2java.translation.Translator;
import eis.iilang.Parameter;
import eis.iilang.ParameterList;

/**
 * 
 * Example: weapon(shock_rifle,primary)
 * 
 * Example:
 * 
 * [<br>
 * &nbsp; weapon(shock_rifle,primary), weapon(bio_rifle,primary),weapon(rocket_launcher,secondary)<br>
 * ]
 * 
 * Example: weapon(shock_rifle,primary)
 * 
 * @author mpkorstanje
 * 
 */
public class WeaponPrefListTranslator implements Parameter2Java<WeaponPrefList> {

	@Override
	public WeaponPrefList translate(Parameter parameter) throws TranslationException {

		WeaponPrefList weaponList = new WeaponPrefList();
		
		if (!(parameter instanceof ParameterList)) {
			weaponList.add(Translator.getInstance().translate2Java(parameter, WeaponPref.class));
			return weaponList;
		}

		for(Parameter p : (ParameterList) parameter){
			weaponList.add(Translator.getInstance().translate2Java(p, WeaponPref.class));
		}

		return weaponList;
	}


	@Override
	public Class<WeaponPrefList> translatesTo() {
		return WeaponPrefList.class;
	}

}
