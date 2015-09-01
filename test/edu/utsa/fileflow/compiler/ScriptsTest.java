package edu.utsa.fileflow.compiler;

import org.junit.Test;

public class ScriptsTest {

	@Test
	public void testElementalFunctions() throws Exception {
		ScriptTester.testScript("scripts/tests/elemental_functions_test.ffa");
	}

}
