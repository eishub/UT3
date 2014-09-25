package nl.tudelft.goal.ut2004.translators;

import nl.tudelft.goal.ut2004.messages.UnrealIdOrLocation;
import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Java2Parameter;
import eis.eis2java.translation.Parameter2Java;
import eis.eis2java.translation.Translator;
import eis.iilang.Parameter;
 
public class UnrealIdOrLocationTranslator implements Parameter2Java<UnrealIdOrLocation>, Java2Parameter<UnrealIdOrLocation> {

	@Override
	public UnrealIdOrLocation translate(Parameter parameter) throws TranslationException {

		TranslationException translationExceptionId;
		try {
			UnrealId id = Translator.getInstance().translate2Java(parameter, UnrealId.class);
			return new UnrealIdOrLocation(id);
		} catch (TranslationException e) {
			translationExceptionId = e;
		}

		TranslationException translationExceptionLocation;
		try {
			Location location = Translator.getInstance().translate2Java(parameter, Location.class);
			return new UnrealIdOrLocation(location);
		} catch (TranslationException e) {
			translationExceptionLocation = e;
		}

		String message = String.format("Could not translate to either UnrealId or Location. "
				+ "\nCause 1: %s\nCause 2: %s", translationExceptionId, translationExceptionLocation);
		throw new TranslationException(message);

	}

	@Override
	public Class<UnrealIdOrLocation> translatesTo() {
		return UnrealIdOrLocation.class;
	}

	@Override
	public Parameter[] translate(UnrealIdOrLocation idOrLocation) throws TranslationException {
		if(idOrLocation.isLocation()){
			return Translator.getInstance().translate2Parameter(idOrLocation.getLocation());
		} else {
			return Translator.getInstance().translate2Parameter(idOrLocation.getId());
		}
	}

	@Override
	public Class<? extends UnrealIdOrLocation> translatesFrom() {
		return UnrealIdOrLocation.class;
	}

}
