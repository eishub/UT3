/*
 * Copyright (C) 2013 AMIS research group, Faculty of Mathematics and Physics, Charles University in Prague, Czech Republic
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.tudelft.goal.ut2004.translators;

import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Parameter2Java;
import eis.eis2java.translation.Translator;
import eis.iilang.Parameter;
import java.util.Arrays;
import nl.tudelft.goal.ut2004.messages.Scope;

/**
 *
 * @author Michiel
 */
public class ScopeTranslator implements Parameter2Java<Scope> {

    @Override
    public Scope translate(Parameter parameter) throws TranslationException {
        String scopeString = Translator.getInstance().translate2Java(parameter, String.class);
        
        try {
            return Scope.valueOfIgnoreCase(scopeString);
        } catch (IllegalArgumentException e) {
            String message = String.format("%s is not a valid scope. Excepted one off %", scopeString, Arrays.toString(Scope.values()));
            throw new TranslationException(message, e);
        }
    }

    @Override
    public Class<Scope> translatesTo() {
        return Scope.class;
    }
    
}
