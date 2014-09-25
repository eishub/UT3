 package nl.tudelft.goal.unreal.translators;

import nl.tudelft.goal.unreal.messages.None;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Java2Parameter;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

public class NoneTranslator implements Java2Parameter<None> {

	@Override
	public Parameter[] translate(None o) throws TranslationException {
		return new Parameter[]{ new Identifier(o.id())};
	}

	@Override
	public Class<? extends None> translatesFrom() {
		return None.class;
	}

}
