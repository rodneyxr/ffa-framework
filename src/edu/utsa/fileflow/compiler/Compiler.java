package edu.utsa.fileflow.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.utsa.fileflow.filestructure.FileStruct;

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
	public FileStruct compile() {
		FileStruct root = new FileStruct("root");
		// while we have more commands to read
		while (scanner.hasNext()) {
			// parse line to command object
			String line = scanner.nextLine();
			
			// skip line if it is a comment
			line = line.trim();
			if (line.startsWith("#")) continue;
			line = line.split("#")[0];
			if (line == null || line.length() == 0) continue;
			
			Command cmd = null;
			try {
				cmd = new Command(line);
			} catch (InvalidCommandException ice) {
				System.err.println(ice.getMessage());
				continue;
			}
			
			switch (cmd.getType()) {
			case COPY:
				handleCopy(root, cmd);
				break;
			case DELETE:
				handleDelete(root, cmd);
				break;
			case MOVE:
				handleMove(root, cmd);
				break;
			case NEW:
				break;
			default:
				System.err.println("Compiler: Unknown command '"+ cmd.getArg(0) +"'");
				break;
			}
		}
		
		return root;
	}

	private void handleCopy(FileStruct fs, Command cmd) {
		// TODO: assert commands are legal
		String arg1 = cmd.getArg(1);
		// first argument must exist, so we add it to precondition file structure
		fs.insert(arg1);
	}

	private void handleMove(FileStruct fs, Command cmd) {
		// TODO: assert command is legal
		String arg1 = cmd.getArg(1);
		// first argument must exist, so we add it to precondition file structure
		fs.insert(arg1);
	}

	private void handleDelete(FileStruct fs, Command cmd) {
		// TODO: assert command is legal
		String arg1 = cmd.getArg(1);
		// first argument must exist, so we add it to precondition file structure
		fs.insert(arg1);
	}

}
