package edu.utsa.fileflow.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Compiler {

	// contains and manages the precondition and postcondition
	private ConditionManager cm;

	/**
	 * Parses the build script and compiles the precondition and postcondition
	 * of the script using a condition manager
	 * 
	 * @param file
	 *            The file to compile the conditions from
	 * @return a ConditionManager object containing the precondition and
	 *         postcondition
	 * @throws CompilerException
	 */
	public ConditionManager compile(File file) throws CompilerException {
		// open the file for reading
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException fnfe) {
			throw new CompilerException(fnfe.getMessage());
		}

		// instantiate the condition manager which contains our precondition and
		// post condition
		cm = new ConditionManager();

		// while we have more commands to read
		while (scanner.hasNext()) {
			// parse line to command object
			String line = scanner.nextLine();

			// handle comments
			line = line.trim();
			if (line.startsWith("#"))
				continue;
			line = line.split("#")[0];
			if (line == null || line.length() == 0)
				continue;

			Command cmd = null;
			try {
				cmd = new Command(line);
			} catch (InvalidCommandException ice) {
				scanner.close();
				throw new CompilerException(ice.getMessage());
			}

			switch (cmd.getType()) {
			case touch:
				// TODO: implement touch
				break;
			case mkdir:
				// TODO: implement mkdir
				break;
			case cp:
				// TODO: implement cp
				break;
			case rm:
				// TODO: implement rm
				break;
			default:
				scanner.close();
				throw new CompilerException("Unknown command: '" + cmd.getArg(0) + "'");
			}
		}

		scanner.close();
		return cm;
	}

}
