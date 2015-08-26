package edu.utsa.fileflow;

import java.io.File;

import edu.utsa.fileflow.compiler.Compiler;
import edu.utsa.fileflow.compiler.CompilerException;
import edu.utsa.fileflow.compiler.Condition;
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
		Condition preCondition = null;
		try {
			preCondition = compiler.compile(file);
		} catch (CompilerException ce) {
			System.err.println(ce.getMessage());
			System.exit(-1);
		}

		// print the precondition
		System.out.println("-------------");
		System.out.println("Precondition");
		System.out.println("-------------");
		System.out.println(preCondition);

		System.out.println();

		// print the postcondition
		System.out.println("--------------");
		System.out.println("Postcondition");
		System.out.println("--------------");
		System.out.println(compiler.getPostCondition());

	}

}
