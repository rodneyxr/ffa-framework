package edu.utsa.fileflow.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.utsa.fileflow.filestructure.FileStruct;
import edu.utsa.fileflow.utilities.PrintDirectoryTree;

public class Compiler {
	private Scanner scanner;

	/**
	 * Initializes the parser object by creating a scanner to read the script.
	 * 
	 * @param file
	 *            the script to compile
	 */
	public Compiler(File file) throws FileNotFoundException {
		this.scanner = new Scanner(file);
	}

	/**
	 * Parses the test script and will return a Directory Structure Object
	 */
	// TODO: add support for comments
	public FileStruct compile() {
		FileStruct root = new FileStruct("root");
		// while we have more commands to read
		while (scanner.hasNext()) {
			// parse line to command object
			String line = scanner.nextLine();
			if (line.startsWith("#")) continue;
			Command cmd = new Command((line.split("#"))[0]);
			
			switch (cmd.getType()) {
			case COPY:
				handleCopy(root, cmd);
				break;
			case DELETE:
				break;
			case MOVE:
				handleMove(root, cmd);
				break;
			case NEW:
				break;
			default:
				break;
			}
		}
		
		return root;
	}

	private void handleCopy(FileStruct fs, Command cmd) {
		// TODO: assert commands are legal
		String arg1 = cmd.getArg(1);
		fs.insert(arg1);
	}

	private void handleMove(FileStruct fs, Command cmd) {
		// TODO: assert command is legal
		String arg1 = cmd.getArg(1);

	}

}
