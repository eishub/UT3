 package nl.tudelft.goal.ut3.translators;

import cz.cuni.amis.pogamut.ut2004.agent.navigation.NavigationState;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Java2Parameter;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

public class NavigationStateTranslator implements Java2Parameter<NavigationState> {

	@Override
	public Parameter[] translate(NavigationState o) throws TranslationException {

		Identifier state;
		switch (o) {
		case NAVIGATING:
			state = new Identifier("navigating");
			break;
		case STUCK:
			state = new Identifier("stuck");
			break;
		case PATH_COMPUTATION_FAILED:
			state = new Identifier("noPath");
			break;
		case TARGET_REACHED:
			state = new Identifier("reached");
			break;
		case STOPPED:
			state = new Identifier("waiting");
			break;
		default:
			// If this happens, states have been added to NavigationState enums.
			// Java doesn't support this sort to checking for enums.
			throw new TranslationException("Encountered unknown state: " + o);
		}

		return new Parameter[] { state };
	}

	@Override
	public Class<? extends NavigationState> translatesFrom() {
		return NavigationState.class;
	}

}
