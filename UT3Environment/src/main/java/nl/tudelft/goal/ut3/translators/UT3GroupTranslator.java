package nl.tudelft.goal.ut3.translators;

import nl.tudelft.goal.unreal.util.EnvironmentUtil;
import cz.cuni.amis.pogamut.ut2004.communication.messages.UT2004ItemType.UT2004Group;
import cz.cuni.amis.pogamut.ut3.communication.messages.UT3ItemType.UT3Group;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Java2Parameter;
import eis.eis2java.translation.Parameter2Java;
import eis.eis2java.translation.Translator;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

public class UT3GroupTranslator implements Java2Parameter<UT3Group>,
		Parameter2Java<UT3Group> {

	@Override
	public Parameter[] translate(UT3Group o) throws TranslationException {
		return new Parameter[] { new Identifier(o.name().toLowerCase()) };
	}

	@Override
	public Class<? extends UT3Group> translatesFrom() {
		return UT3Group.class;
	}

	@Override
	public UT3Group translate(Parameter parameter) throws TranslationException {
		String itemTypeString = Translator.getInstance().translate2Java(
				parameter, String.class);

		try {
			return UT3Group.valueOf(itemTypeString.toUpperCase());
		} catch (IllegalArgumentException e) {
			String message = String.format(
					"%s was not a ItemType. Expected on off %s.",
					itemTypeString,
					EnvironmentUtil.listValid(UT2004Group.class));
			throw new TranslationException(message, e);
		}
	}

	@Override
	public Class<UT3Group> translatesTo() {
		return UT3Group.class;
	}

}
