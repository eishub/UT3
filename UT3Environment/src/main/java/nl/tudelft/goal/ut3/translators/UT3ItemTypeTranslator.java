package nl.tudelft.goal.ut3.translators;

import cz.cuni.amis.pogamut.ut2004.communication.messages.ItemType;
import cz.cuni.amis.pogamut.ut2004.communication.messages.ItemType.Category;
import java.util.Arrays;
import java.util.Set;

import cz.cuni.amis.pogamut.ut3.communication.messages.UT3ItemType;
import cz.cuni.amis.pogamut.ut3.communication.messages.UT3ItemType.UT3Group;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Java2Parameter;
import eis.eis2java.translation.Parameter2Java;
import eis.eis2java.translation.Translator;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

public class UT3ItemTypeTranslator implements Java2Parameter<UT3ItemType>,
		Parameter2Java<UT3ItemType> {

	@Override
	public Parameter[] translate(UT3ItemType o) throws TranslationException {
		return new Parameter[] { new Identifier(o.getGroup().name()
				.toLowerCase()) };
	}

	@Override
	public Class<? extends UT3ItemType> translatesFrom() {
		return UT3ItemType.class;
	}

	@Override
	public UT3ItemType translate(Parameter parameter)
			throws TranslationException {
		String itemTypeString = Translator.getInstance().translate2Java(
				parameter, String.class);

		try {
			UT3Group itemGroup = UT3Group.valueOf(UT3Group.class,
					itemTypeString.toUpperCase());
			Set<ItemType> items = UT3ItemType.GROUPS.get(itemGroup);
			for (ItemType item : items) {
				if (item.getCategory() == Category.WEAPON) {
					return (UT3ItemType) item;
				}
			}

			String message = String.format(
					"%s was not a ItemType in the weapon category.",
					itemTypeString);
			throw new TranslationException(message);
		} catch (IllegalArgumentException e) {
			String message = String.format(
					"%s was not a ItemType. Expected on off %s.",
					itemTypeString, Arrays.toString(UT3Group.values()));
			throw new TranslationException(message, e);
		}
	}

	@Override
	public Class<UT3ItemType> translatesTo() {
		return UT3ItemType.class;
	}

}
