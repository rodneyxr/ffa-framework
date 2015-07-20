package edu.utsa.fileflow.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.utsa.fileflow.Main;
import edu.utsa.fileflow.filestructure.FilePath;
import edu.utsa.fileflow.filestructure.FileStruct;

public class Compiler {

	// existing precondition file structure
	private FileStruct pre;
	// cannot exist precondition file structure
	private FileStruct $pre;
	// postcondition file structure (what the structure will look like after)
	private FileStruct post;

	/**
	 * Parses the test script and will return a Directory Structure Object
	 * 
	 * @param file
	 * @return Precondition file structure
	 * @throws CompilerException
	 */
	public FileStruct compile(File file) throws CompilerException {
		// open the file for reading
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException fnfe) {
			throw new CompilerException(fnfe.getMessage());
		}

		// instantiate the precondition file structure
		pre = new FileStruct("root");
		$pre = new FileStruct("root");

		// instantiate the postcondition file structure
		// current structure while we execute commands
		post = new FileStruct("root");

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
			case COPY:
				handleCopy(cmd);
				break;
			case DELETE:
				handleDelete(cmd);
				break;
			case MOVE:
				handleMove(cmd);
				break;
			case NEW:
				handleNew(cmd);
				break;
			default:
				scanner.close();
				throw new CompilerException("Unknown command: '" + cmd.getArg(0) + "'");
			}
		} 

		scanner.close();
		return pre;
	}

	/**
	 * Assumes that a file path exists in the precondition. In the special case that it does not exist in either the
	 * precondition or the postcondition the file path will be added to both because we will assume that path exists
	 * before the script was executed.
	 * 
	 * @param filePath
	 * @return true if we CAN assume that filePath existed before we ran the script
	 */
	private boolean assume(FilePath filePath/*, boolean exists*/) {
		boolean inPre = pre.pathExists(filePath);
		boolean $inPre = $pre.pathExists(filePath);
		boolean inPost = post.pathExists(filePath);
		
		if (inPre) {
			// if path in precondition but not in the post then it was deleted/moved at some point
			// because it was already used as a precondition and was added to the postcondition at
			// the same time. So if it is no longer in the post condition then it is doesn't not exist.
			if (!inPost) {
				Main.logger.log("%s does not exist", filePath);
				return false;// throw new CompilerException(String.format("%s does not exist", filePath));
			} // if path exists in both then it is valid
		} else {
			// if path does not exist in the precondition but exists in post
			// do nothing because is not a precondition but still a valid argument
			// else if it does not exists in either then it becomes a precondition
			if (!inPost) {
				if ($inPre) {
					Main.logger.log("%s cannot be assumed", filePath);
					return false;
				}
				pre.insert(filePath);
				post.insert(filePath);
			}
		}
		
		return true;
	}

	private void handleCopy(Command cmd) throws CompilerException {
		// assert command is legal
		if (cmd.getSize() != 3) {
			throw new CompilerException(String.format("'%s': Command '%s' expects exactly 2 arguments", cmd, cmd.getType().getName()));
		}

		// handle the first argument
		FilePath arg1 = new FilePath(cmd.getArg(1));
		if (!assume(arg1)) {
			throw new CompilerException(String.format("'%s': arg1 does not exist", cmd));
		}

		// the second argument should not exist, so if it does throw an exception
		FilePath arg2 = new FilePath(cmd.getArg(2));
		// in order for the second argument to be valid the path to the file must exist but not the file itself
		if (!assume(arg2.getPathToFile())) {
			throw new CompilerException(String.format("'%s': path to arg2 does not exist", cmd));
		}

		if (post.pathExists(arg2)) {
			throw new CompilerException(String.format("'%s': File or directory already exists in post-condition.\n%s", cmd, post));
		}

		// insert clone of the path to the new path
		post.insert(post.getFileStruct(arg1).clone(), arg2);
	}

	private void handleMove(Command cmd) throws CompilerException {
		// assert command is legal
		if (cmd.getSize() != 3) {
			throw new CompilerException(String.format("'%s': Command '%s' expects exactly 2 arguments", cmd, cmd.getType().getName()));
		}

		// handle the first argument
		FilePath arg1 = new FilePath(cmd.getArg(1));
		if (!assume(arg1)) {
			throw new CompilerException(String.format("'%s': arg1 does not exist", cmd));
		}

		// the second argument should not exist, so if it does throw an exception
		FilePath arg2 = new FilePath(cmd.getArg(2));
		// in order for the second argument to be valid the path to the file must exist but not the file itself
		if (!assume(arg2.getPathToFile())) {
			throw new CompilerException(String.format("'%s': path to arg2 does not exist", cmd));
		}

		if (post.pathExists(arg2)) {
			throw new CompilerException(String.format("'%s': File or directory already exists in post-condition.\n%s", cmd, post));
		}

		// insert clone of arg1 to the new path
		post.insert(post.getFileStruct(arg1).clone(), arg2);
		// delete arg1 from file structure
		post.remove(arg1);

	}

	private void handleDelete(Command cmd) throws CompilerException {
		// assert command is legal
		if (cmd.getSize() != 2) {
			throw new CompilerException(String.format("'%s': Command '%s' expects only 1 argument", cmd, cmd.getType().getName()));
		}

		// first argument must exist so add it to precondition file structure if it isn't in post
		FilePath arg1 = new FilePath(cmd.getArg(1));

		// if it exists in post then it doesn't need to be in precondition
		// because it was already created by another command
		if (!assume(arg1)) {
			throw new CompilerException(String.format("'%s': File does not exist", cmd));
		}
		
		post.remove(arg1);
	}

	private void handleNew(Command cmd) throws CompilerException {
		// assert command is legal
		if (cmd.getSize() != 2) {
			throw new CompilerException(String.format("'%s': Command '%s' expects only 1 argument", cmd, cmd.getType().getName()));
		}

		// arg1 should not exist in either pre or post
		FilePath arg1 = new FilePath(cmd.getArg(1));
		if (post.pathExists(arg1)) {
			throw new CompilerException(String.format("'%s': File or directory already exists in post-condition.\n%s", cmd, post));
		}

		// assert it doesn't exist in precondition
		if (!$pre.pathExists(arg1) && !pre.pathExists(arg1)) {
			$pre.insert(arg1);
		}
		
		post.insert(arg1);
	}

	public FileStruct getPost() {
		return post;
	}
	
	public FileStruct getAntiPre() {
		return $pre;
	}

}
