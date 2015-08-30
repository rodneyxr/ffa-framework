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
				/*
				 * FIXME: when assuming a file exists when removing, the path to
				 * that file should be put into postcondition
				 */
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
