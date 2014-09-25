 package nl.tudelft.goal.ut3.translators;

import cz.cuni.amis.pogamut.ut2004.communication.messages.ItemType.Group;
import cz.cuni.amis.pogamut.ut3.communication.messages.UT3ItemType.UT3Group;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Java2Parameter;
import eis.eis2java.translation.Parameter2Java;
import eis.eis2java.translation.Translator;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

public class GroupTranslator implements Java2Parameter<Group>, Parameter2Java<Group> {

	@Override
	public Parameter[] translate(Group o) throws TranslationException {
		return new Parameter[] { new Identifier(o.name().toLowerCase()) };
	}

	@Override
	public Class<? extends Group> translatesFrom() {
		return Group.class;
	}

	@Override
	public Group translate(Parameter parameter) throws TranslationException {
		return Translator.getInstance().translate2Java(parameter, UT3Group.class);
	}

	@Override
	public Class<Group> translatesTo() {
		return Group.class;
	}

}
