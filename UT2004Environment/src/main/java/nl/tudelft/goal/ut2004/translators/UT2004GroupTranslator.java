package nl.tudelft.goal.ut2004.translators;

import nl.tudelft.goal.unreal.util.EnvironmentUtil;
import cz.cuni.amis.pogamut.ut2004.communication.messages.UT2004ItemType.UT2004Group;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Java2Parameter;
import eis.eis2java.translation.Parameter2Java;
import eis.eis2java.translation.Translator;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

public class UT2004GroupTranslator implements Java2Parameter<UT2004Group>,
		Parameter2Java<UT2004Group> {

	@Override
	public Parameter[] translate(UT2004Group o) throws TranslationException {
		return new Parameter[] { new Identifier(o.name().toLowerCase()) };
	}

	@Override
	public Class<? extends UT2004Group> translatesFrom() {
		return UT2004Group.class;
	}

	@Override
	public UT2004Group translate(Parameter parameter)
			throws TranslationException {
		String itemTypeString = Translator.getInstance().translate2Java(
				parameter, String.class);

		try {
			return UT2004Group.valueOf(itemTypeString.toUpperCase());
		} catch (IllegalArgumentException e) {
			String message = String.format(
					"%s was not a ItemType. Expected on off %s.",
					itemTypeString,
					EnvironmentUtil.listValid(UT2004Group.class));
			throw new TranslationException(message, e);
		}
	}

	@Override
	public Class<UT2004Group> translatesTo() {
		return UT2004Group.class;
	}

}
