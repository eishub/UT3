package nl.tudelft.goal.unreal.translators;

import nl.tudelft.goal.unreal.messages.BotParametersKey;
import nl.tudelft.goal.unreal.util.EnvironmentUtil;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Parameter2Java;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

public class BotParametersKeyTranslator implements Parameter2Java<BotParametersKey> {

	@Override
	public BotParametersKey translate(Parameter p) throws TranslationException {

		if (!(p instanceof Identifier)) {
			String message = String.format("%s is not a identifier.", p);
			throw new TranslationException(message);
		}

		String id = ((Identifier) p).getValue();

		for (BotParametersKey k : BotParametersKey.values()) {
			if (k.getKey().equals(id)) {
				return k;
			}
		}
		
		String message = "%s is not a valid key. Valid keys are: %s.";
		message = String.format(message, id, EnvironmentUtil.listValid(BotParametersKey.class));
		
		throw new TranslationException(message);

	}

	@Override
	public Class<BotParametersKey> translatesTo() {
		return BotParametersKey.class;
	}

}
