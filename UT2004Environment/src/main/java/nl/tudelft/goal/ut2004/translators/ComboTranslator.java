 package nl.tudelft.goal.ut2004.translators;

import java.util.Arrays;

import nl.tudelft.goal.ut2004.messages.Combo;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Parameter2Java;
import eis.eis2java.translation.Translator;
import eis.iilang.Parameter;

public class ComboTranslator implements Parameter2Java<Combo> {

	@Override
	public Combo translate(Parameter parameter) throws TranslationException {
		String comboString = Translator.getInstance().translate2Java(parameter, String.class);

		try {
			return Combo.valueOfIgnoresCase(comboString);
		} catch (IllegalArgumentException e) {
			String message = String.format("%s was not a Combo. Expected on off %s.", comboString, Arrays.toString(Combo.values()));
			throw new TranslationException(message, e);
		}
	}

	@Override
	public Class<Combo> translatesTo() {
		return Combo.class;
	}

}
