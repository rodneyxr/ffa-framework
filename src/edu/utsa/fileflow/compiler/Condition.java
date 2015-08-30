package edu.utsa.fileflow.compiler;

import edu.utsa.fileflow.filestructure.FileFlowWarning;
import edu.utsa.fileflow.filestructure.FilePath;
import edu.utsa.fileflow.filestructure.FileStructure;
import edu.utsa.fileflow.filestructure.FileStructureException;

public abstract class Condition {

	// existing file structure
	protected FileStructure positive;

	// non-existing file structure
	protected FileStructure negative;

	/**
	 * Instantiates the two file structures representing existing and
	 * non-existing conditions
	 */
	public Condition() {
		positive = new FileStructure();
		negative = new FileStructure();
	}

	/**
	 * Inserts an existing file into this condition.
	 * 
	 * @param path
	 *            The path to insert the positive file at
	 * @return the file that was inserted
	 * @throws FileStructureException
	 * @throws FileFlowWarning
	 */
	public FileStructure insertPositive(FilePath path) throws FileStructureException, FileFlowWarning {
		if (path.isDir())
			return positive.insertDirectory(path);
		return positive.insertRegularFile(path);
	}

	/**
	 * Removes the file at the path provided from the positive file structure.
	 * 
	 * @param path
	 *            the path to the file to remove
	 * @return the file that was removed or null if it does not exist
	 */
	public FileStructure removePositive(FilePath path) {
		return positive.removeFile(path);
	}

	/**
	 * 
	 * @return the positive file structure
	 */
	public FileStructure getPositiveRoot() {
		return positive;
	}

	/**
	 * Tells if a file exists under the positive file structure at the path
	 * provided.
	 * 
	 * @param path
	 *            the path to the file to determine its existence
	 * @return true if the file exists; false otherwise
	 */
	public boolean existsInPositive(FilePath path) {
		return positive.fileExists(path);
	}

	/**
	 * Inserts a non-existing file into this condition.
	 * 
	 * @param path
	 *            The path to insert the negative file at
	 * @return the file that was inserted
	 * @throws FileStructureException
	 */
	public FileStructure insertNegative(FilePath path) throws FileStructureException {
//		if (path.isDir())
//			return negative.insertDirectory(path);
//		return negative.insertRegularFile(path);
		return negative.insertForce(path);
	}

	/**
	 * Removes the file at the path provided from the negative file structure.
	 * 
	 * @param path
	 *            the path to the file to remove
	 * @return the file that was removed or null if it does not exist
	 */
	public FileStructure removeNegative(FilePath path) {
		return negative.removeFile(path);
	}

	/**
	 * 
	 * @return the negative file structure
	 */
	public FileStructure getNegativeRoot() {
		return negative;
	}

	/**
	 * Tells if a file exists under the negative file structure at the path
	 * provided.
	 * 
	 * @param path
	 *            the path to the file to determine its existence
	 * @return true if the file exists; false otherwise
	 */
	public boolean existsInNegative(FilePath path) {
		return negative.fileExists(path);
	}

	public void print() {
		System.out.println("Existing");
		positive.print();
		System.out.println();
		System.out.println("Non-existing");
		negative.print();
	}
	
}
