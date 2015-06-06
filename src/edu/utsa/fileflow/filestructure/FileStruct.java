package edu.utsa.fileflow.filestructure;

import java.util.HashMap;

public class FileStruct {

	private String name;
	private HashMap<String, FileStruct> files;

	/**
	 * This constructor will instantiate a new Directory object.
	 * 
	 * @param name
	 *            - name of the directory
	 * @param level
	 *            - level of the directory relative to 'root'
	 */
	public FileStruct(String name) {
		setName(name);
		files = new HashMap<String, FileStruct>();
	}

	/**
	 * Adds a new directory to the end of the dirList array list.
	 * 
	 * @param newDir
	 */
	public void insert(FileStruct dir) {
		// Main.logger.log("Directory: " + "Adding " + dir.name + " to " + name); // DEBUG
		this.files.put(dir.name, dir);
	}

	/**
	 * Removes a directory from the dirList array list.
	 * 
	 * @param dir
	 */
	public void remove(FileStruct dir) {
		files.remove(dir.name);
	}

	public void insert(String filePath) {
		String[] tokens = filePath.split("/");
		createStructure(tokens, 0);
	}

	public void print(int tabCount) {
		StringBuilder tabs = new StringBuilder("|");
		for (int i = 0; i < tabCount; i++)
			tabs.append("--");
		for (HashMap.Entry<String, FileStruct> dir : files.entrySet()) {
			System.out.println(tabs + "--" + dir.getValue().name);
			dir.getValue().print(tabCount++);
		}
	}

	private void createStructure(String[] tokens, int nextIndex) {
		// Main.logger.log("Directory: " + Arrays.toString(tokens)); // DEBUG
		FileStruct nextDir = files.get(tokens[nextIndex]);
		if (nextDir == null) {
			// create directory
			FileStruct newDir = new FileStruct(tokens[nextIndex]);
			this.insert(newDir);
			if (nextIndex < tokens.length - 1) {
				int newIndex = nextIndex + 1;
				newDir.createStructure(tokens, newIndex);
			}
		} else {
			if (nextIndex < tokens.length - 1) {
				int newIndex = nextIndex + 1;
				nextDir.createStructure(tokens, newIndex);
			}
		}
	}

	/*
	 * Getters and Setters for global class variables
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<String, FileStruct> getDirList() {
		return files;
	}

}
