 package nl.tudelft.goal.ut2004.translators;

import java.util.Arrays;

import nl.tudelft.goal.ut2004.messages.FireMode;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Java2Parameter;
import eis.eis2java.translation.Parameter2Java;
import eis.eis2java.translation.Translator;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
/**
 * Example: primary<br>
 * Example: secondary
 * 
 * 
 * @author mpkorstanje
 *
 */
public class FireModeTranslator implements Parameter2Java<FireMode>, Java2Parameter<FireMode> {

	@Override
	public FireMode translate(Parameter parameter) throws TranslationException {
		String string = Translator.getInstance().translate2Java(parameter, String.class);

		try {
			return FireMode.valueOfIgnoreCase(string);
		} catch (IllegalArgumentException e) {
			String message = String.format("%s was not a fire mode. Expected on off %s.", string, Arrays.toString(FireMode.values()));
			throw new TranslationException(message, e);
		}
	}

	@Override
	public Class<FireMode> translatesTo() {
		return FireMode.class;
	}

	@Override
	public Parameter[] translate(FireMode o) throws TranslationException {
		return new Parameter[]{new Identifier(o.name().toLowerCase())};
	}

	@Override
	public Class<? extends FireMode> translatesFrom() {
		return FireMode.class;
	}

}
