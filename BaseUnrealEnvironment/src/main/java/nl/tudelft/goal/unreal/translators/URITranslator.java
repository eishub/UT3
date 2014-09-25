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

import java.net.URI;
import java.net.URISyntaxException;

import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Parameter2Java;
import eis.eis2java.translation.Translator;
import eis.iilang.Parameter;


/**
 * A simple translator that will only translate addresses of the form
 * ut://[host]:[port].
 * 
 * 
 * @author mpkorstanje
 * 
 */
public class URITranslator implements Parameter2Java<URI> {

	@Override
	public URI translate(Parameter parameter) throws TranslationException {
		String uriString = Translator.getInstance().translate2Java(parameter, String.class);

		try {
			return new URI(uriString);
		} catch (URISyntaxException e) {
			String message = String.format("%s was not a URI. Expected a URI of the form ut://[host]:[port].", uriString);
			throw new TranslationException(message, e);
		}

	}

	@Override
	public Class<URI> translatesTo() {
		return URI.class;
	}


}