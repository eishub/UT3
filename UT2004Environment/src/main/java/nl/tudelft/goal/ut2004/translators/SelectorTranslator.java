 package nl.tudelft.goal.ut2004.translators;


import nl.tudelft.goal.ut2004.selector.ALocation;
import nl.tudelft.goal.ut2004.selector.WorldObject;
import nl.tudelft.goal.ut2004.selector.ContextSelector;
import nl.tudelft.goal.ut2004.selector.EnemyFlagCarrier;
import nl.tudelft.goal.ut2004.selector.FriendlyFlagCarrier;
import nl.tudelft.goal.ut2004.selector.NearestEnemy;
import nl.tudelft.goal.ut2004.selector.NearestFriendly;
import nl.tudelft.goal.ut2004.selector.NearestFriendlyWithUT2004LinkGun;
import nl.tudelft.goal.ut2004.selector.None;
import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Parameter2Java;
import eis.eis2java.translation.Translator;
import eis.iilang.Function;
import eis.iilang.Parameter;

public class SelectorTranslator implements Parameter2Java<ContextSelector> {

	@Override
	public ContextSelector translate(Parameter parameter) throws TranslationException {
	
		if(parameter instanceof Function){
			Location location = Translator.getInstance().translate2Java(parameter, Location.class);
			return new  ALocation(location);
		}
		
		String selectorString = Translator.getInstance().translate2Java(parameter, String.class);

		if (selectorString.equals("nearestEnemy")) {
			return new NearestEnemy();
		} else if (selectorString.equals("nearestFriendly")) {
			return new NearestFriendly();
		} else if (selectorString.equals("nearestFriendlyWithLinkGun")){
			return new NearestFriendlyWithUT2004LinkGun();
		} else if (selectorString.equals("enemyFlagCarrier")) {
			return new EnemyFlagCarrier();
		} else if (selectorString.equals("friendlyFlagCarrier")) {
			return new FriendlyFlagCarrier();
		} else if (selectorString.equals("none")){
			return new None();
		}
		
		UnrealId id = Translator.getInstance().translate2Java(parameter, UnrealId.class);
		
		return new WorldObject(id);
	}

	@Override
	public Class<ContextSelector> translatesTo() {
		return ContextSelector.class;
	}

}
