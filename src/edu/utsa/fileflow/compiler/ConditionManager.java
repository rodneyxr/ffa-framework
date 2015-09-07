package edu.utsa.fileflow.compiler;

import java.util.ArrayList;

import edu.utsa.fileflow.filestructure.FileFlowWarning;
import edu.utsa.fileflow.filestructure.FilePath;
import edu.utsa.fileflow.filestructure.FileStructure;
import edu.utsa.fileflow.filestructure.FileStructureException;

public class ConditionManager {

	private final Precondition precondition;
	private final Postcondition postcondition;
	private final ArrayList<String> log;

	public ConditionManager() {
		precondition = new Precondition();
		postcondition = new Postcondition();
		log = new ArrayList<String>();
	}

	public Precondition getPrecondition() {
		return precondition;
	}

	public Postcondition getPostcondition() {
		return postcondition;
	}

	/**
	 * Inserts a path to the post condition. If the file exists it will be
	 * overwritten. If the file does not exist it will be
	 * 
	 * FIXME: when inserting a file to a path that doesn't exist the path to
	 * that file should be marked as existing
	 * 
	 * @param path
	 * @throws CompilerException
	 */
	public void insertPath(FilePath path) throws CompilerException {
		boolean pre = precondition.existsInPositive(path);
		boolean $pre = precondition.existsInNegative(path);

		// only do this for regular files because if it is a directory then
		// mkdir will make the path to that directory
		if (!path.isDir()) {
			FilePath pathToFile = path.pathToFile();
			if (pathToFile != path)
				if (assume(pathToFile) == null) {
					throw new CompilerException(
							String.format("touch: cannot touch '%s': No such file or directory", path));
				}
		}

		// if the file already exists then issue a warning of possible overwrite
		try {
			postcondition.insertPositive(path);
			postcondition.removeNegative(path);

			// if no exceptions then assume it does not exist
			if (!pre && !$pre) { // unnecessary warnings
				precondition.insertNegativeForce(path);
			}
		} catch (FileStructureException e) {
			throw new CompilerException(e.getMessage());
		} catch (FileFlowWarning e) {
			log.add(e.getMessage());
		}

	}

	/**
	 * Removes a file from the file structure and makes the necessary changes to
	 * the precondition and postconditions.
	 * 
	 * @param path
	 *            the path to the file to remove
	 * @throws CompilerException
	 *             if the file could does not exist and could not be assumed to
	 *             exist
	 */
	public void removePath(FilePath path) throws CompilerException {
		// first assume that the file exists
		FileStructure fileToRemove = assume(path);
		if (fileToRemove != null) {
			// if it does exist then correct the file path to match isDirectory
			path.setDir(fileToRemove.isDirectory());
			// remove the file
			postcondition.removePositive(path);
			try {
				// it should not exist so add it as a negative file
				postcondition.insertNegativeForce(path);
			} catch (FileStructureException e) {
				// not sure if this could ever happen
				e.printStackTrace();
			}
		} else {
			throw new CompilerException(String.format("rm: cannot remove '%s': No such file or directory", path));
		}

	}

	public void copyPath(FilePath source, FilePath dest) throws CompilerException {
		boolean dpre = precondition.existsInPositive(dest);
		boolean $dpre = precondition.existsInNegative(dest);

		// if the source file does not exist then we must assume it exists
		if (assume(source) == null) {
			throw new CompilerException(String.format("cp: cannot stat '%s': No such file or directory", source));
		}

		// sourceFile will exist here
		FileStructure sourceFile = postcondition.positive.getFile(source);
		sourceFile = sourceFile.clone();

		FilePath pathToDest = dest.pathToFile();

		if (dest != pathToDest) {
			if (!postcondition.existsInPositive(dest.pathToFile())) {
				insertPath(pathToDest);
			}
		}

		// assume all sub-directories and files do not exist if dest is a
		// directory
		for (FilePath path : sourceFile.getAllFilePaths()) {
			if (!assumeNot(FilePath.concat(dest, path))) {
				throw new CompilerException(String.format("cp: cannot stat '%s': No such file or directory", path));
			}
		}

		try {
			postcondition.positive.copyFileToPath(source, dest);
			postcondition.removeNegative(dest);
			// if no exceptions then assume it does not exist
			if (!dpre && !$dpre) { // unnecessary warnings
				precondition.insertNegativeForce(dest);
			}
		} catch (FileStructureException e) {
			throw new CompilerException(e.getMessage());
		}

	}

	/**
	 * Assumes a file exists and make the necessary modifications to both the
	 * precondition and postcondition.
	 * 
	 * @param path
	 *            the path to the file to assume exists
	 * @return the FileStructure if the file was assumed successfully; null
	 *         otherwise.
	 */
	public FileStructure assume(FilePath path) {
		boolean pre = precondition.existsInPositive(path);
		boolean $pre = precondition.existsInNegative(path);
		FileStructure file = postcondition.positive.getFile(path);

		if (file == null) {
			// the file did not exist so try to assume that it exists
			if (pre || $pre) {
				// check if it has not already been assumed
				return null;
			}

			// assume the file exists
			try {
				precondition.insertPositiveForce(path);
				file = postcondition.insertPositiveForce(path);
			} catch (FileStructureException e) {
				// this should never occur
				e.printStackTrace();
				return null;
			}
		}
		return file;
	}

	/**
	 * Assumes a file exists and make the necessary modifications to both the
	 * precondition and postcondition.
	 * 
	 * @param path
	 *            the path to the file to assume exists
	 * @return true if the file was assumed successfully; false otherwise.
	 */
	public boolean assumeNot(FilePath path) {
		boolean pre = precondition.existsInPositive(path);
		boolean $pre = precondition.existsInNegative(path);
		FileStructure file = postcondition.positive.getFile(path);

		if (file == null) {
			// the file did not exist so try to assume that it exists
			if (pre || $pre) {
				// check if it has not already been assumed
				return false;
			}

			// assume the file does not exist
			try {
				precondition.insertNegativeForce(path);
				// TODO: possibly enable this
				// postcondition.negative.insertForce(path);
			} catch (FileStructureException e) {
				// this should never occur
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @return the log for this condition manager
	 */
	public ArrayList<String> getLog() {
		return log;
	}

	public void print() {
		// print the precondition
		System.out.println("-------------");
		System.out.println("Precondition");
		System.out.println("-------------");
		precondition.print();

		System.out.println();

		// print the postcondition
		System.out.println("--------------");
		System.out.println("Postcondition");
		System.out.println("--------------");
		postcondition.print();

		System.out.println();
		System.out.println("*** Log ***");
		if (log.isEmpty()) {
			System.out.println("NO MESSAGES");
		} else {
			for (String message : log) {
				System.out.println(message);
			}
		}
	}

}
