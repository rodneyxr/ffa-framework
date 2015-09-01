package edu.utsa.fileflow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
		File file = new File("scripts/test.ffa");
		try {
			System.setIn(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}

		Compiler compiler = new Compiler();
		ConditionManager conditionManager = null;
		try {
			conditionManager = compiler.compile(System.in);
		} catch (CompilerException ce) {
			System.err.println(ce.getMessage());
			System.exit(-1);
		}

		conditionManager.print();
	}

}