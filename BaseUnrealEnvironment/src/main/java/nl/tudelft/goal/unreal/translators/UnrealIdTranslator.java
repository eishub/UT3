package nl.tudelft.goal.unreal.translators;

import java.util.HashMap;
import java.util.Map;

import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Java2Parameter;
import eis.eis2java.translation.Parameter2Java;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

public class UnrealIdTranslator implements Java2Parameter<UnrealId>, Parameter2Java<UnrealId> {

	protected Map<Identifier, UnrealId> translated = new HashMap<Identifier, UnrealId>();

	@Override
	public Parameter[] translate(UnrealId o) throws TranslationException {

		Identifier id;
		
		// UnrealId can be "None", but we want it to be "none".
		if (o.getStringId().equalsIgnoreCase("none")) {
			id = new Identifier("none");
		} else {
			id = new Identifier(o.getStringId());
		}
		
		translated.put(id, o);
		return new Parameter[] { id };
	}

	@Override
	public Class<? extends UnrealId> translatesFrom() {
		return UnrealId.class;
	}

	@Override
	public UnrealId translate(Parameter parameter) throws TranslationException {
		if (!translated.containsKey(parameter)) {
			String message = String.format("The unrealId must be an id that has been translated before. Recieved %s",
					parameter);
			throw new TranslationException(message);
		}

		return translated.get(parameter);
	}

	@Override
	public Class<UnrealId> translatesTo() {
		return UnrealId.class;

	}

}
