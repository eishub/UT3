 package nl.tudelft.goal.ut3.translators;

import nl.tudelft.goal.ut3.messages.SelectorList;
import nl.tudelft.goal.ut3.selector.ContextSelector;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Parameter2Java;
import eis.eis2java.translation.Translator;
import eis.iilang.Parameter;
import eis.iilang.ParameterList;

public class SelectorListTranslator implements Parameter2Java<SelectorList> {

	@Override
	public SelectorList translate(Parameter parameter) throws TranslationException {

		SelectorList list = new SelectorList();

		// Syntactic sugar, single items don't need a list.
		if (!(parameter instanceof ParameterList)) {
			list.add(Translator.getInstance().translate2Java(parameter, ContextSelector.class));
		} else {
			ParameterList parameterList = (ParameterList) parameter;
			for (Parameter p : parameterList) {
				list.add(Translator.getInstance().translate2Java(p, ContextSelector.class));
			}
		}
		return list;
	}

	@Override
	public Class<SelectorList> translatesTo() {
		return SelectorList.class;
	}

}
