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
		boolean post = postcondition.existsInPositive(path);
		boolean $post = postcondition.existsInNegative(path);
		boolean pre = precondition.existsInPositive(path);
		boolean $pre = precondition.existsInNegative(path);

		// first check if the file cannot exist
		if (pre && !post) {
			// if it cannot exist then throw an exception
			throw new CompilerException(String.format("File '%s' does not exist", path));
		}

		// if the file already exists then issue a warning of possible overwrite
		try {
			postcondition.insertPositive(path);
			postcondition.removeNegative(path);
			// if no exceptions then assume it does not exist
			if (!$pre) // unnecessary warnings
				precondition.insertNegative(path);
		} catch (FileStructureException e) {
			throw new CompilerException(e.getMessage());
		} catch (FileFlowWarning e) {
			log.add(e.getMessage());
		}

	}

	public void removePath(FilePath path) throws CompilerException {
		// boolean post = postcondition.existsInPositive(path);
		boolean $post = postcondition.existsInNegative(path);
		boolean pre = precondition.existsInPositive(path);
		boolean $pre = precondition.existsInNegative(path);

		FileStructure removedFile = postcondition.removePositive(path);

		if (removedFile == null) {
			// the file did not exist so try to assume that it exists
			// TODO: assume the removed file exists
		}

		try {
			postcondition.insertNegative(path);
		} catch (FileStructureException e) {
			e.printStackTrace();
		} catch (FileFlowWarning e) {
			// ignore warning
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
