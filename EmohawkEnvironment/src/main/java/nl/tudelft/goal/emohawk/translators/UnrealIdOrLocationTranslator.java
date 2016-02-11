package nl.tudelft.goal.emohawk.translators;

import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Parameter2Java;
import eis.eis2java.translation.Translator;
import eis.iilang.Parameter;
import nl.tudelft.goal.emohawk.messages.UnrealIdOrLocation;

public class UnrealIdOrLocationTranslator implements Parameter2Java<UnrealIdOrLocation> {

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

		String message = String.format(
				"Could not translate to either UnrealId or Location. " + "\nCause 1: %s\nCause 2: %s",
				translationExceptionId, translationExceptionLocation);
		throw new TranslationException(message);

	}

	@Override
	public Class<UnrealIdOrLocation> translatesTo() {
		return UnrealIdOrLocation.class;
	}

}
