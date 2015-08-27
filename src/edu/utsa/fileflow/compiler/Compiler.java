package edu.utsa.fileflow.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.utsa.fileflow.Main;
import edu.utsa.fileflow.filestructure.FileFlowWarning;
import edu.utsa.fileflow.filestructure.FilePath;
import edu.utsa.fileflow.filestructure.FileStructureException;

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
				touch(cmd);
				break;
			case mkdir:
				mkdir(cmd);
				break;
			case cp:
				// TODO: implement cp
				break;
			case rm:
				rm(cmd);
				break;
			default:
				scanner.close();
				throw new CompilerException("Unknown command: '" + cmd.getArg(0) + "'");
			}
		}

		scanner.close();
		return cm;
	}

	private void touch(Command cmd) throws CompilerException {
		if (cmd.getSize() != 2) {
			throw new CompilerException(String.format("'%s': Command '%s' expects only 1 argument", cmd, cmd.getType().getName()));
		}

		// create the file to insert
		FilePath file1 = new FilePath(cmd.getArg(1), false);
		cm.insertPath(file1);
	}
	
	private void mkdir(Command cmd) throws CompilerException {
		if (cmd.getSize() != 2) {
			throw new CompilerException(String.format("'%s': Command '%s' expects only 1 argument", cmd, cmd.getType().getName()));
		}
		
		// create the directory to insert
		FilePath directory = new FilePath(cmd.getArg(1), true);
		cm.insertPath(directory);
	}

	private void rm(Command cmd) throws CompilerException {
		if (cmd.getSize() != 2) {
			throw new CompilerException(String.format("'%s': Command '%s' expects only 1 argument", cmd, cmd.getType().getName()));
		}
		
		// instantiate the file to remove
		FilePath file = new FilePath(cmd.getArg(1));
		cm.removePath(file);
	}
	
}
