package edu.utsa.fileflow.compiler;

import edu.utsa.fileflow.filestructure.FileStructureException;
import edu.utsa.fileflow.filestructure.FilePath;
import edu.utsa.fileflow.filestructure.FileStructure;

public class Condition {

	// File Structure that can exist
	private FileStructure existingFileStruct;

	// This one cannot exist
	private FileStructure nonexistingFileStruct;

	/**
	 * Instantiates the two file structures representing existing and non-existing conditions
	 */
	public Condition() {
		existingFileStruct = new FileStructure();
		nonexistingFileStruct = new FileStructure();
	}

	/**
	 * Inserts a file path into the this condition under the appropriate file structure.
	 * @param filePath The file path to insert.
	 * @param existing True if the file path should be inserted into the existing file structure. False otherwise.
	 * @throws Exception 
	 */
	public void insert(FilePath filePath, boolean existing) throws FileStructureException {
		if (existing) {
			existingFileStruct.insert(filePath);
		} else {
			nonexistingFileStruct.insert(filePath);
		}
	}

	/**
	 * Removes a file path from the condition. 
	 * @param filePath The file path to be removed.
	 * @param existing True if the file path should be removed from the existing file structure. False otherwise.
	 */
	public void remove(FilePath filePath, boolean existing) {
		if (existing) {
			existingFileStruct.remove(filePath);
		} else {
			nonexistingFileStruct.remove(filePath);
		}
	}

	/**
	 * Returns the existing or non-existing file structure.
	 * @param existing True if the existing file structure should be returned. False otherwise.
	 * @return The file structure corresponding to the boolean parameter. 
	 */
	public FileStructure getFileStruct(boolean existing) {
		if (existing)
			return existingFileStruct;
		return nonexistingFileStruct;
	}

	/**
	 * 
	 * @param filePath The file path to check if it exists.
	 * @return True if the file path exists in the existing file structure.
	 */
	public boolean exists(FilePath filePath) {
		return existingFileStruct.exists(filePath);
	}

	/**
	 * 
	 * @param filePath The file path to check if it can exist.
	 * @return True if the file path exists in the non-existing file structure.
	 */
	public boolean canExist(FilePath filePath) {
		return !nonexistingFileStruct.exists(filePath);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Existing\n");
		sb.append(existingFileStruct);
		sb.append("\nNon-existing\n");
		sb.append(nonexistingFileStruct);
		return sb.toString();
	}

}
