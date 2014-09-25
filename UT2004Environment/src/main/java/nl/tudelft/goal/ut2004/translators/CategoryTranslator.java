package nl.tudelft.goal.ut2004.translators;

import nl.tudelft.goal.unreal.util.EnvironmentUtil;
import cz.cuni.amis.pogamut.ut2004.communication.messages.ItemType.Category;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Java2Parameter;
import eis.eis2java.translation.Parameter2Java;
import eis.eis2java.translation.Translator;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

public class CategoryTranslator implements Java2Parameter<Category>,
		Parameter2Java<Category> {

	@Override
	public Parameter[] translate(Category o) throws TranslationException {
		return new Parameter[] { new Identifier(o.name().toLowerCase()) };
	}

	@Override
	public Class<? extends Category> translatesFrom() {
		return Category.class;
	}

	@Override
	public Category translate(Parameter parameter) throws TranslationException {
		String itemTypeString = Translator.getInstance().translate2Java(
				parameter, String.class);

		try {
			return Category.valueOf(itemTypeString.toUpperCase());
		} catch (IllegalArgumentException e) {
			String message = String.format(
					"%s was not a ItemType. Expected on off %s.",
					itemTypeString, EnvironmentUtil.listValid(Category.class));
			throw new TranslationException(message, e);
		}
	}

	@Override
	public Class<Category> translatesTo() {
		return Category.class;
	}

}
