package nl.tudelft.goal.ut2004.translators;

import java.util.Arrays;
import java.util.Set;

import cz.cuni.amis.pogamut.ut2004.communication.messages.ItemType;
import cz.cuni.amis.pogamut.ut2004.communication.messages.UT2004ItemType;
import cz.cuni.amis.pogamut.ut2004.communication.messages.ItemType.Category;
import cz.cuni.amis.pogamut.ut2004.communication.messages.UT2004ItemType.UT2004Group;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Java2Parameter;
import eis.eis2java.translation.Parameter2Java;
import eis.eis2java.translation.Translator;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

public class UT2004ItemTypeTranslator implements
		Java2Parameter<UT2004ItemType>, Parameter2Java<UT2004ItemType> {

	@Override
	public Parameter[] translate(UT2004ItemType o) throws TranslationException {
		return new Parameter[] { new Identifier(o.getGroup().name()
				.toLowerCase()) };
	}

	@Override
	public Class<? extends UT2004ItemType> translatesFrom() {
		return UT2004ItemType.class;
	}

	@Override
	public UT2004ItemType translate(Parameter parameter)
			throws TranslationException {
		String itemTypeString = Translator.getInstance().translate2Java(
				parameter, String.class);

		try {
			UT2004Group itemGroup = UT2004Group.valueOf(UT2004Group.class,
					itemTypeString.toUpperCase());
			Set<ItemType> items = UT2004ItemType.GROUPS.get(itemGroup);
			for (ItemType item : items) {
				if (item.getCategory() == Category.WEAPON) {
					return (UT2004ItemType) item;
				}
			}

			String message = String.format(
					"%s was not a ItemType in the weapon category.",
					itemTypeString);
			throw new TranslationException(message);
		} catch (IllegalArgumentException e) {
			String message = String.format(
					"%s was not a ItemType. Expected on off %s.",
					itemTypeString, Arrays.toString(UT2004Group.values()));
			throw new TranslationException(message, e);
		}
	}

	@Override
	public Class<UT2004ItemType> translatesTo() {
		return UT2004ItemType.class;
	}

}
