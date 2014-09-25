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

import java.util.List;

import cz.cuni.amis.pogamut.base3d.worldview.object.Velocity;
import cz.cuni.amis.pogamut.ut2004.utils.UnrealUtils;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Java2Parameter;
import eis.eis2java.translation.Parameter2Java;
import eis.iilang.Function;
import eis.iilang.Numeral;
import eis.iilang.Parameter;


public class VelocityTranslator implements Java2Parameter<Velocity>, Parameter2Java<Velocity> {

	public static final String VELOCITY_KEYWORD = "velocity";

	@Override
	public Parameter[] translate(Velocity o) throws TranslationException {
		return new Parameter[] { new Function(VELOCITY_KEYWORD,
				new Numeral(UnrealUtils.unrealDegreeToDegree((int) o.getX())),
				new Numeral(UnrealUtils.unrealDegreeToDegree((int) o.getY())),
				new Numeral(UnrealUtils.unrealDegreeToDegree((int) o.getZ())) )
			};
	}

	@Override
	public Class<? extends Velocity> translatesFrom() {
		return Velocity.class;
	}

	@Override
	public Velocity translate(Parameter p) throws TranslationException {

		if (!(p instanceof Function)) {
			String message = String.format("A velocity must be a function, received: %s.", p);
			throw new TranslationException(message);
		}

		Function f = (Function) p;

		// Check function name
		if (!f.getName().equals(VELOCITY_KEYWORD)) {
			String message = String.format("A velocity needs to start with %s, not: %s in %s.", VELOCITY_KEYWORD,
					f.getName(), f);
			throw new TranslationException(message);
		}
		List<Parameter> parameters = f.getParameters();

		// Check number of parameters
		if (parameters.size() != 3) {
			String message = String.format("Expected exactly 3 parameters when parsing %s", f);
			throw new TranslationException(message);
		}

		// Check type of parameters
		for (Parameter parameter : parameters) {
			if (!(parameter instanceof Numeral)) {
				String message = String.format("All arguments of %s should be numerical.", f);
				throw new TranslationException(message);
			}
		}

		// Transform to location.
		double[] c = new double[3];
		for (int i = 0; i < c.length; i++) {
			Parameter parameter = parameters.get(i);
			c[i] = ((Numeral) parameter).getValue().doubleValue();
		}
		return new Velocity(c[0], c[1], c[2]);

	}

	@Override
	public Class<Velocity> translatesTo() {
		return Velocity.class;

	}

}
