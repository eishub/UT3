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

import cz.cuni.amis.pogamut.emohawk.agent.module.sensomotoric.Place;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Java2Parameter;
import eis.eis2java.translation.Translator;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;

public class PlaceTranslator implements Java2Parameter<Place> {

	@Override
	public Parameter[] translate(Place o) throws TranslationException {
		return new Parameter[] { new Identifier(o.name().toLowerCase()),
				Translator.getInstance().translate2Parameter(o.getPlaceLocation())[0],
				new Numeral(o.getPlaceRadius()) };
	}

	@Override
	public Class<? extends Place> translatesFrom() {
		return Place.class;
	}

}