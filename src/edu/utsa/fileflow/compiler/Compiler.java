package edu.utsa.fileflow.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.utsa.fileflow.filestructure.Directory;
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
	public void compile() {
		Directory root = new Directory("root", 0);
		// while we have more commands to read
		while (scanner.hasNext()) {
			// parse line to command object
			Command cmd = new Command(scanner.nextLine());

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
		System.out.println(PrintDirectoryTree.printDirectoryTree(root));

	}

	private void handleCopy(Directory dir, Command cmd) {
		// TODO: assert commands are legal
		String arg1 = cmd.getArg(1);
		String[] tokens = arg1.split("/");
		dir.createStructure(tokens, 0);
	}

	private void handleMove(Directory dir, Command cmd) {
		// TODO: assert command is legal
		String arg1 = cmd.getArg(1);
		
	}
	
}
