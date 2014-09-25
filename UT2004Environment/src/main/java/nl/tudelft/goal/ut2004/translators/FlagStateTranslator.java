 package nl.tudelft.goal.ut2004.translators;
import nl.tudelft.goal.ut2004.messages.FlagState;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Java2Parameter;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

/**
 * Example: held<br>
 * Example: home<br>
 * Example: dropped<br>
 * 
 * @author mpkorstanje
 *
 */
public class FlagStateTranslator implements Java2Parameter<FlagState> {

	@Override
	public Parameter[] translate(FlagState o) throws TranslationException {
		return new Parameter[] {new Identifier(o.name().toLowerCase())};
	}

	@Override
	public Class<? extends FlagState> translatesFrom() {
		return FlagState.class;
	}


	

}
