 package nl.tudelft.goal.ut2004.translators;

import cz.cuni.amis.pogamut.ut2004.agent.module.sensor.Game.GameType;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Java2Parameter;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

public class GameTypeTranslator implements Java2Parameter<GameType> {

	@Override
	public Parameter[] translate(GameType o) throws TranslationException {
		return new Parameter[] {new Identifier(o.name().toLowerCase())};
	}

	@Override
	public Class<? extends GameType> translatesFrom() {
		return GameType.class;
	}

}
