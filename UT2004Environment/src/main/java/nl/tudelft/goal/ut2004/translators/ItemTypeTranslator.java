package nl.tudelft.goal.ut2004.translators;

import cz.cuni.amis.pogamut.ut2004.communication.messages.ItemType;
import cz.cuni.amis.pogamut.ut2004.communication.messages.UT2004ItemType;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Java2Parameter;
import eis.eis2java.translation.Parameter2Java;
import eis.eis2java.translation.Translator;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

public class ItemTypeTranslator implements Java2Parameter<ItemType>,
		Parameter2Java<ItemType> {

	@Override
	public Parameter[] translate(ItemType o) throws TranslationException {
		return new Parameter[] { new Identifier(o.getGroup().name()
				.toLowerCase()) };
	}

	@Override
	public Class<? extends ItemType> translatesFrom() {
		return ItemType.class;
	}

	@Override
	public ItemType translate(Parameter parameter) throws TranslationException {
		return Translator.getInstance().translate2Java(parameter,
				UT2004ItemType.class);
	}

	@Override
	public Class<ItemType> translatesTo() {
		return ItemType.class;
	}

}
