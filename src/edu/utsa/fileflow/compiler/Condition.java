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

	public FileStructure removePositive(FilePath path) {
		return positive.removeFile(path);
	}

	public FileStructure getPositiveRoot() {
		return positive;
	}

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
	 * @throws FileFlowWarning
	 */
	public FileStructure insertNegative(FilePath path) throws FileStructureException, FileFlowWarning {
		if (path.isDir())
			return negative.insertDirectory(path);
		return negative.insertRegularFile(path);
	}

	public FileStructure removeNegative(FilePath path) {
		return negative.removeFile(path);
	}

	public FileStructure getNegativeRoot() {
		return negative;
	}

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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Existing\n");
		sb.append(positive);
		sb.append("\n\nNon-existing\n");
		sb.append(negative);
		return sb.toString();
	}

}
