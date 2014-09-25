package nl.tudelft.goal.ut2004.agent;

import nl.tudelft.goal.ut3.translators.ItemTypeTranslator;
import nl.tudelft.goal.ut3.translators.UT3ItemTypeTranslator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cz.cuni.amis.pogamut.ut2004.communication.messages.ItemType;
import eis.eis2java.exception.NoTranslatorException;
import eis.eis2java.exception.TranslationException;
import eis.eis2java.translation.Translator;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

public class ItemTypeTranslatorTests {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testWeaponTranslator() throws NoTranslatorException, TranslationException {
		
		Translator t = Translator.getInstance();
		
		UT3ItemTypeTranslator ut3ItemTypeTranslator = new UT3ItemTypeTranslator();
		t.registerParameter2JavaTranslator(ut3ItemTypeTranslator);
		ItemTypeTranslator itemTypeTranslatorTest = new ItemTypeTranslator();
		t.registerParameter2JavaTranslator(itemTypeTranslatorTest);
		
		Parameter p = new Identifier("shock_rifle");
		ItemType ut3ItemType = t.translate2Java(p, ItemType.class);		
	}

}
