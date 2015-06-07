package edu.utsa.fileflow.filestructure;

import java.util.HashMap;

public class FileStruct {

	private String name;
	private final HashMap<String, FileStruct> files;

	/**
	 * This constructor will instantiate a new Directory object.
	 * 
	 * @param name
	 *            - name of the directory
	 * @param level
	 *            - level of the directory relative to 'root'
	 */
	public FileStruct(String name) {
		this.name = name;
		files = new HashMap<String, FileStruct>();
	}

	/**
	 * Adds a file structure to the map of files.
	 * 
	 * @param fs
	 *            the file structure to add
	 */
	public void insert(FileStruct fs) {
		// Main.logger.log("Directory: " + "Adding " + dir.name + " to " + name); // DEBUG
		this.files.put(fs.name, fs);
	}

	public void insert(String filePath) {
		String[] tokens = filePath.split("/");
		createStructure(tokens, 0);
	}

	/**
	 * Removes a directory from the dirList array list.
	 * 
	 * @param dir
	 */
	public void remove(FileStruct fs) {
		files.remove(fs.name);
	}

	private void createStructure(String[] tokens, int nextIndex) {
		// Main.logger.log("Directory: " + Arrays.toString(tokens)); // DEBUG
		FileStruct nextFS = files.get(tokens[nextIndex]);
		if (nextFS == null) {
			// create directory
			FileStruct newFS = new FileStruct(tokens[nextIndex]);
			this.insert(newFS);
			if (nextIndex < tokens.length - 1) {
				int newIndex = nextIndex + 1;
				newFS.createStructure(tokens, newIndex);
			}
		} else {
			if (nextIndex < tokens.length - 1) {
				int newIndex = nextIndex + 1;
				nextFS.createStructure(tokens, newIndex);
			}
		}
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

}
