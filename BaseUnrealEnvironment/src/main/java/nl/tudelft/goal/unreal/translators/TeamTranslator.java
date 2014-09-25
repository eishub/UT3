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


import nl.tudelft.goal.ut2004.util.Team;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Java2Parameter;
import eis.eis2java.translation.Parameter2Java;
import eis.eis2java.translation.Translator;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

public class TeamTranslator implements Parameter2Java<Team>, Java2Parameter<Team> {

	@Override
	public Team translate(Parameter parameter) throws TranslationException {
		String teamString = Translator.getInstance().translate2Java(parameter, String.class);

		try {
			return Team.valueOfIgnoresCase(teamString);
		} catch (IllegalArgumentException e) {
			String message = String.format("%s was not a Team. Expected on off %s.", teamString, Team.values());
			throw new TranslationException(message, e);
		}
	}
	
	@Override
	public Parameter[] translate(Team o) throws TranslationException {
		return new Parameter[]{ new Identifier(o.name().toLowerCase()) };
	}

	@Override
	public Class<? extends Team> translatesFrom() {
		return Team.class;
	}

	@Override
	public Class<Team> translatesTo() {
		return Team.class;
	}

}