package edu.utsa.fileflow.compiler;

import java.io.InputStream;
import java.util.Scanner;

import edu.utsa.fileflow.filestructure.FilePath;
import edu.utsa.fileflow.filestructure.InvalidFilePathException;

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
	public ConditionManager compile(InputStream inputStream) throws CompilerException {
		// open the file for reading
		Scanner scanner = new Scanner(inputStream);

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
				cp(cmd);
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
			throw new CompilerException(
					String.format("'%s': Command '%s' expects one argument", cmd, cmd.getType().getName()));
		}

		// create the file to insert
		FilePath file1 = createFilePath(cmd.getArg(1), false);
		cm.insertPath(file1);
	}

	private void mkdir(Command cmd) throws CompilerException {
		if (cmd.getSize() != 2) {
			throw new CompilerException(
					String.format("'%s': Command '%s' expects one argument", cmd, cmd.getType().getName()));
		}

		// create the directory to insert
		FilePath directory = createFilePath(cmd.getArg(1), true);
		cm.insertPath(directory);
	}

	private void rm(Command cmd) throws CompilerException {
		if (cmd.getSize() != 2) {
			throw new CompilerException(
					String.format("'%s': Command '%s' expects one argument", cmd, cmd.getType().getName()));
		}

		// instantiate the file to remove
		FilePath file = createFilePath(cmd.getArg(1));
		cm.removePath(file);
	}

	// TODO: implement this
	private void cp(Command cmd) throws CompilerException {
		if (cmd.getSize() != 3) {
			throw new CompilerException(
					String.format("'%s': Command '%s' expects two arguments", cmd, cmd.getType().getName()));
		}

		// instantiate the source file
		FilePath source = createFilePath(cmd.getArg(1));
		
		// instantiate the destination file
		FilePath dest = createFilePath(cmd.getArg(2));
		
		cm.copyPath(source, dest);
		
	}

	/**
	 * Wrapper method for instantiating new file paths. This is to avoid a mess
	 * of repetitive exception handling in compiler methods that require a file
	 * path to be constructed.
	 * 
	 * @param path
	 *            a string representing the file path to create
	 * @param isdir
	 *            true if the file path points to a directory; false if it
	 *            points to a regular file
	 * @return the new file path
	 * @throws CompilerException
	 *             if the file path is invalid
	 */
	private FilePath createFilePath(String path, boolean isdir) throws CompilerException {
		try {
			return new FilePath(path, isdir);
		} catch (InvalidFilePathException e) {
			throw new CompilerException(e.getMessage());
		}
	}

	/**
	 * Wrapper method for instantiating new file paths. This is to avoid a mess
	 * of repetitive exception handling in compiler methods that require a file
	 * path to be constructed.
	 * 
	 * @param path
	 *            a string representing the file path to create
	 * @return the new file path
	 * @throws CompilerException
	 *             if the file path is invalid
	 */
	private FilePath createFilePath(String path) throws CompilerException {
		try {
			return new FilePath(path);
		} catch (InvalidFilePathException e) {
			throw new CompilerException(e.getMessage());
		}
	}

}
