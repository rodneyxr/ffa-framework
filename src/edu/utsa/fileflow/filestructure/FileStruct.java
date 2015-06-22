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
	
	public void remove(String filePath) {
		String[] tokens = filePath.split("/");
		if (tokens.length == 1) {
			remove(files.get(tokens[0]));
			return;
		}
		FileStruct next = this, prev;
		for (int i=0, size= tokens.length; i<size;i++) {
			String token = tokens[i];
			prev = next;
			next = next.files.get(token);
			if (next.files.get(tokens[i+1]) == null){
				prev.remove(next.name);
			}
		}
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

	public boolean pathExists(String filePath) {
		String[] tokens = filePath.split("/");
		FileStruct next = this;
		for (String token : tokens) {
			next = next.files.get(token);
			if (next == null)
				return false;
		}
		return true;
	}
	
	public boolean pathExists(String[] filePath) {
		FileStruct next = this;
		for (int i=0, size = filePath.length-1; i < size;i++) {
			next = next.files.get(filePath[i]);
			if (next == null)
				return false;
		}
		return true;
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
