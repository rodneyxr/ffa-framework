package edu.utsa.fileflow.filestructure;

import java.util.HashMap;

import edu.utsa.fileflow.utilities.PrintDirectoryTree;

public class FileStruct {

	private String name;
	private FileStruct parent;
	private final HashMap<String, FileStruct> files;

	/**
	 * This constructor will instantiate the new FileStruct object.
	 * 
	 * @param name
	 *            name of the directory
	 */
	public FileStruct(String name) {
		this.name = name;
		this.parent = null;
		this.files = new HashMap<String, FileStruct>();
	}

	/**
	 * Adds a file structure to the map of files.
	 * 
	 * @param fs
	 *            the file structure to add
	 */
	public FileStruct insert(FileStruct fs) {
		fs.parent = this;
		return files.put(fs.name, fs);
	}

	/**
	 * Inserts the filePath into the file structure. If the directories do not exist, then it will create them.
	 * 
	 * @param filePath
	 *            the file path to insert into the file structure
	 */
	public FileStruct insert(FilePath filePath) {
		String[] tokens = filePath.getTokens();
		FileStruct next = this;
		for (String token : tokens) {
			next.insert(new FileStruct(token));
			next = next.files.get(token);
		}
		return next;
	}
	
	public FileStruct insert(FileStruct fs, FilePath filePath) {
		FileStruct nodeToInsertAt = insert(filePath.getPathToFile());
		fs.name = filePath.getFileName();
		return nodeToInsertAt.insert(fs);
	}

	/**
	 * Removes a FileStruct from its children
	 *
	 * @param fs
	 *            FileStruct to remove
	 */
	private FileStruct remove(FileStruct fs) {
		return files.remove(fs.name);
	}

	/**
	 * 
	 * @param filePath
	 *            path to the file to be removed
	 * @return the FileStruct that was removed
	 */
	public FileStruct remove(FilePath filePath) {
		FileStruct fileToRemove = getFileStruct(filePath);
		if (fileToRemove == null) {
			return null;
		}
		// FIXME: test if parent is null
		return fileToRemove.parent.remove(fileToRemove);
	}

	/**
	 * 
	 * @param filePath
	 *            path to the target FileStruct to be returned
	 * @return the FileStruct specified by the filePath
	 */
	public FileStruct getFileStruct(FilePath filePath) {
		String[] tokens = filePath.getTokens();
		FileStruct next = this;
		for (String token : tokens) {
			next = next.files.get(token);
			if (next == null)
				return null;
		}
		return next;
	}

	/**
	 * Checks if a file exists given the path to that file
	 * 
	 * @param filePath
	 * @return true if the file exists
	 */
	public boolean pathExists(FilePath filePath) {
		return getFileStruct(filePath) != null;
	}

	/*
	 * Getters and Setters for global class variables
	 */
	public String getName() {
		return name;
	}

	public HashMap<String, FileStruct> getFiles() {
		return files;
	}

	@Override
	public FileStruct clone() {
		FileStruct clone = new FileStruct(name);
		for (HashMap.Entry<String, FileStruct> entry : files.entrySet()) {
			clone.insert(entry.getValue().clone());
		}
		return clone;
	}
	
	@Override
	public String toString() {
		return PrintDirectoryTree.printDirectoryTree(this);
	}

}
