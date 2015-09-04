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
	 * @param path
	 * @throws CompilerException
	 */
	public void insertPath(FilePath path) throws CompilerException {
		boolean pre = precondition.existsInPositive(path);
		boolean $pre = precondition.existsInNegative(path);

		// if the file already exists then issue a warning of possible overwrite
		try {
			postcondition.insertPositive(path);
			postcondition.removeNegative(path);

			// if no exceptions then assume it does not exist
			if (!pre && !$pre) { // unnecessary warnings
				precondition.insertNegative(path);
			}
		} catch (FileStructureException e) {
			throw new CompilerException(e.getMessage());
		} catch (FileFlowWarning e) {
			log.add(e.getMessage());
		}

	}

	public void removePath(FilePath path) throws CompilerException {
		boolean $post = postcondition.existsInNegative(path);
		boolean pre = precondition.existsInPositive(path);
		boolean $pre = precondition.existsInNegative(path);

		// check if it cannot exist
		if ($post) {
			throw new CompilerException(String.format("rm: cannot remove '%s': No such file or directory", path));
		}

		// try to remove the file. null will be returned if it does not exist
		FileStructure removedFile = postcondition.removePositive(path);

		if (removedFile == null) {
			// the file did not exist so try to assume that it exists
			if (pre || $pre) {
				// check if it has not already been assumed
				throw new CompilerException(String.format("rm: cannot remove '%s': No such file or directory", path));
			}

			// assume the removed file exists
			try {
				precondition.positive.insertForce(path);
				// when assuming a file exists when removing, the path to that
				// file should be put into postcondition
				FilePath pathToFile = path.pathToFile();
				// if the path has no parent itself is returned
				if (path != pathToFile) {
					postcondition.positive.insertForce(path.pathToFile());
				}
			} catch (FileStructureException e) {
				e.printStackTrace();
			}
		}

		try {
			postcondition.insertNegative(path);
		} catch (FileStructureException e) {
			// not sure if this could ever happen
			e.printStackTrace();
		}

	}

	public void copyPath(FilePath source, FilePath dest) throws CompilerException {
		boolean post = postcondition.existsInPositive(source);
		boolean $post = postcondition.existsInNegative(source);
		boolean pre = precondition.existsInPositive(source);
		boolean $pre = precondition.existsInNegative(source);

		boolean dpost = postcondition.existsInPositive(dest);
		boolean $dposts = postcondition.existsInNegative(dest);
		boolean dpre = precondition.existsInPositive(dest);
		boolean $dpre = precondition.existsInNegative(dest);

		if (!post) {
			// if the source file does not exist then we must assume it exists
			insertPath(source);
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

		try {
			postcondition.positive.copyFileToPath(source, dest);
			postcondition.removeNegative(dest);
			// if no exceptions then assume it does not exist
			if (!dpre && !$dpre) { // unnecessary warnings
				precondition.insertNegative(dest);
			}
		} catch (FileStructureException e) {
			throw new CompilerException(e.getMessage());
		}
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
