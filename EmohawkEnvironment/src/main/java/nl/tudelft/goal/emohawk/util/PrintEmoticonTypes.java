package nl.tudelft.goal.emohawk.util;

import nl.tudelft.goal.emohawk.translators.EmoticonTypeTranslator;

public class PrintEmoticonTypes {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EmoticonTypeTranslator t = new EmoticonTypeTranslator();
		System.out.println(t.getValidValues());

	}

}
