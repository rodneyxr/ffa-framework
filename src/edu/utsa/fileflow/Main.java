package edu.utsa.fileflow;

import java.io.File;

import edu.utsa.fileflow.compiler.Compiler;
import edu.utsa.fileflow.compiler.CompilerException;
import edu.utsa.fileflow.compiler.ConditionManager;
import edu.utsa.fileflow.utilities.LuckyLogger;

/**
 * File Flow Analysis
 * 
 * @author Rodney Rodriguez
 *
 */

public class Main {
	public static final boolean DEBUG = true;
	public static final LuckyLogger logger = new LuckyLogger("DEBUG");

	public static void main(String[] args) {
		logger.setEnabled(DEBUG);

		// create a file for the script to compile
		File file = new File("scripts/test.script");
		Compiler compiler = new Compiler();
		ConditionManager conditionManager = null;
		try {
			conditionManager = compiler.compile(file);
		} catch (CompilerException ce) {
			System.err.println(ce.getMessage());
			System.exit(-1);
		}

		conditionManager.print();
	}

}
