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


import nl.tudelft.goal.unreal.messages.ParameterMap;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Parameter2Java;
import eis.iilang.Identifier;
import eis.iilang.Parameter;
import eis.iilang.ParameterList;

public class ParameterMapTranslator implements Parameter2Java<ParameterMap> {

	// A map entry has a key and value, so a size of 2.
	private static final int ENTRY_SIZE = 2; 

	@Override
	public ParameterMap translate(Parameter parameter) throws TranslationException {
		if (!(parameter instanceof ParameterList)) {
			String message = String.format("%s is not a parameter list.", parameter);
			throw new TranslationException(message);
		}
		
		return translateEntries((ParameterList) parameter);
	}

	private ParameterMap translateEntries(ParameterList parameterList) throws TranslationException {
		ParameterMap map = new ParameterMap();

		for (Parameter entry : parameterList) {
			
			if (!(entry instanceof ParameterList)) {
				String message = String.format("%s is not a parameter list.", entry);
				throw new TranslationException(message);
			}
			
			ParameterList entryList = (ParameterList) entry;
			
			if (entryList.size() != ENTRY_SIZE) {
				String message = String.format("Expected a parameter list with " + ENTRY_SIZE + " elements but received %s.", entryList);
				throw new TranslationException(message);
			}
			
			Parameter key = entryList.get(0);
			Parameter value = entryList.get(1);
			
			if(!(key instanceof Identifier)){
				String message = String.format("Expected an identifier but received %s.", key);
				throw new TranslationException(message);
			}

			map.put((Identifier) key,value);
		}

		return map;
	}

	@Override
	public Class<ParameterMap> translatesTo() {
		return ParameterMap.class;
	}

}