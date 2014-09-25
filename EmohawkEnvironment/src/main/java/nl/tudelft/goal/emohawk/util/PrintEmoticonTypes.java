package nl.tudelft.goal.emohawk.util;

import nl.tudelft.goal.emohawk.translators.EmoticonTypeTranslator;
import cz.cuni.amis.pogamut.emohawk.agent.module.sensomotoric.EmoticonType;

public class PrintEmoticonTypes {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EmoticonTypeTranslator t = new EmoticonTypeTranslator();
		System.out.println(t.getValidValues());

	}

}
