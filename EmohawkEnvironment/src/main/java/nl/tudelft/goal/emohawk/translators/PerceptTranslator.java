package nl.tudelft.goal.emohawk.translators;

import java.util.ArrayList;

import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Java2Parameter;
import eis.eis2java.translation.Translator;
import eis.iilang.Parameter;
import eis.iilang.ParameterList;
import nl.tudelft.goal.emohawk.messages.None;
import nl.tudelft.goal.emohawk.messages.Percept;

/**
 * Translates percepts of objects to arrays such that they can be reasonably be
 * expected to match. When objects translate to multiple elements they are
 * considered to be a {@link ParameterList}. When they translate to an empty
 * list they're ignored. Single element lists are collapsed.
 *
 * @author mpkorstanje
 *
 */
public class PerceptTranslator implements Java2Parameter<Percept> {

	@Override
	public Parameter[] translate(Percept percept) throws TranslationException {
		ArrayList<Object> translated = new ArrayList<Object>(percept.size());
		for (Object elements : percept) {

			// Null elements are considered "none".
			if (elements == null) {
				elements = new None();
			}

			Parameter[] t = Translator.getInstance().translate2Parameter(elements);

			// Collapse single and empty arrays.
			if (t.length == 0) {
				continue;
			} else if (t.length == 1) {
				translated.add(t[0]);
			} else {
				translated.add(new ParameterList(t));
			}
		}
		return translated.toArray(new Parameter[translated.size()]);
	}

	@Override
	public Class<? extends Percept> translatesFrom() {
		return Percept.class;
	}

}
