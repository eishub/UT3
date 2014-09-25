/**
 * BaseUnrealEnvironment, an implementation of the environment interface standard that 
 * facilitates the connection between GOAL and the UT2004 engine. 
 * 
 * Copyright (C) 2012 BaseUnrealEnvironment authors.
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
package nl.tudelft.goal.unreal.translators;

import java.util.logging.Level;

import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Parameter2Java;
import eis.eis2java.translation.Translator;
import eis.iilang.Parameter;

public class LevelTranslator implements Parameter2Java<Level> {

	@Override
	public Level translate(Parameter parameter) throws TranslationException {
		String levelString = Translator.getInstance().translate2Java(parameter, String.class);
		try {
			return Level.parse(levelString);
		} catch (IllegalArgumentException e) {
			String message = String.format("%s was not a valid log level.", levelString);
			throw new TranslationException(message);
		}
	}

	@Override
	public Class<Level> translatesTo() {
		return Level.class;
	}

}