/**
 * Emohawk Bot, an implementation of the environment interface standard that
 * facilitates the connection between GOAL and Emohawk.
 *
 * Copyright (C) 2012 Emohawk Bot authors.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package nl.tudelft.goal.emohawk.translators;

import java.util.Arrays;

import cz.cuni.amis.pogamut.emohawk.agent.module.sensomotoric.EmoticonType;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Java2Parameter;
import eis.eis2java.translation.Parameter2Java;
import eis.eis2java.translation.Translator;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

public class EmoticonTypeTranslator implements Java2Parameter<EmoticonType>, Parameter2Java<EmoticonType> {

	@Override
	public EmoticonType translate(Parameter parameter) throws TranslationException {
		String emoticonTypeString = Translator.getInstance().translate2Java(parameter, String.class);
		emoticonTypeString = emoticonTypeString.toUpperCase();

		try {
			return EmoticonType.valueOf(emoticonTypeString);
		} catch (IllegalArgumentException e) {
			String message = String.format("%s was not a emoticon. Expected one of %s.", emoticonTypeString,
					getValidValues());
			throw new TranslationException(message, e);
		}
	}

	public String getValidValues() {
		EmoticonType[] types = EmoticonType.values();
		String[] values = new String[types.length];
		for (int i = 0; i < values.length; i++) {
			values[i] = types[i].name().toLowerCase();
		}

		return Arrays.toString(values);
	}

	@Override
	public Class<EmoticonType> translatesTo() {
		return EmoticonType.class;
	}

	@Override
	public Parameter[] translate(EmoticonType argument) throws TranslationException {
		return new Parameter[] { new Identifier(argument.name().toLowerCase()) };
	}

	@Override
	public Class<? extends EmoticonType> translatesFrom() {
		return EmoticonType.class;
	}

}