package edu.utsa.fileflow;

import java.io.File;

import edu.utsa.fileflow.compiler.Compiler;
import edu.utsa.fileflow.compiler.CompilerException;
import edu.utsa.fileflow.filestructure.FileStruct;
import edu.utsa.fileflow.utilities.LuckyLogger;
import edu.utsa.fileflow.utilities.PrintDirectoryTree;

public class Main {

	public static final boolean DEBUG = true;
	public static final LuckyLogger logger = new LuckyLogger("DEBUG");

	public static void main(String[] args) {
		logger.setEnabled(DEBUG);
		Main.logger.log("Hello, World! Welcome to File Flow Analysis!");

		// create a file for the script to compile
		File file = new File("test.script");
		Compiler compiler = new Compiler();

		FileStruct preCondition = null;
		try {
			preCondition = compiler.compile(file);
		} catch (CompilerException ce) {
			System.err.println(ce.getMessage());
			System.exit(-1);
		}

		System.out.println(PrintDirectoryTree.printDirectoryTree(preCondition));

	}

}
