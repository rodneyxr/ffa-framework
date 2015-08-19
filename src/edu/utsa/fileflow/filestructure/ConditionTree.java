package edu.utsa.fileflow.filestructure;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import edu.utsa.fileflow.utilities.Strings;

public class ConditionTree {

	private TreeMap<String, ConditionTree> files;
	private String name;
	private ConditionTree parent;
	private boolean isdir;
	
	// true if this node is existing; false if non-existing
	private boolean positive;

	public ConditionTree() {
		this("root", true, true);
	}

	private ConditionTree(String name, boolean isdir, boolean positive) {
		this.name = name;
		this.isdir = isdir;
		this.positive = positive;
		parent = this;
		if (isdir) {
			files = new TreeMap<String, ConditionTree>();
			files.put(".", this);
			files.put("..", parent);
		} else {
			files = null;
		}
	}

	/**
	 * Inserts a path into the directory.
	 * 
	 * TODO: check for inserting existing paths into non-existing paths
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public ConditionTree insert(FilePath path, boolean positive) throws FileStructureException {
		if (!isdir)
			throw new FileStructureException("cannot insert: not a directory");

		ConditionTree cp = this; // save the current pointer
		String[] tokens = path.tokens();
		int dirPathSize = tokens.length;
		if (!path.isDir())
			dirPathSize -= 1;

		// create the directory path first
		for (int i = 0; i < dirPathSize; i++) {
			if (!cp.isdir)
				throw new FileStructureException("cannot insert: not a directory");
			// peek ahead to check if next level exists
			ConditionTree peek = cp.files.get(tokens[i]);
			if (peek != null) {
				// move to next level if a directory
				if (!peek.isdir)
					throw new FileStructureException("cannot insert: not a directory");
				cp = peek;
			} else {
				// create the next level and move pointer to the next level
				cp = cp.insert(tokens[i], true, positive);
			}
		}

		// insert the last token if the path is a file
		if (!path.isDir()) {
			cp = cp.insert(tokens[tokens.length - 1], false, positive);
		}

		return cp;
	}
	
	/**
	 * Removes a file from the file structure.
	 * 
	 * @param path The path to the file to be removed
	 * @return the file that was removed or null if it does not exist
	 */
	public ConditionTree remove(FilePath path) {
		ConditionTree fs = get(path);
		if (fs == null) return null;
		fs.parent.files.remove(fs.name);
		fs.parent = fs;
		if (fs.isdir) {
			fs.files.put("..", fs);
		}
		
		return fs;
	}
	
	/**
	 * Gets the file at the path provided.
	 * 
	 * @param path the path to the file to be returned
	 * @return the file that the path points to or null if it does not exist
	 */
	public ConditionTree get(FilePath path) {
		ConditionTree cp = this;
		for (String filename : path.tokens()) {
			cp = cp.files.get(filename);
			if (cp == null)
				return null;
		}
		return cp;
	}
	
	/**
	 * Tells whether a path exists in the file structure.
	 * 
	 * @param path The path to check if it exists
	 * @return true if the path exists
	 */
	public boolean exists(FilePath path) {
		return get(path) != null;
	}
	
	/**
	 * 
	 * @return the name with an ending slash if the file is a directory
	 */
	public String displayName() {
		if (isdir)
			return (positive ? "+" : "-") + name + File.separator;
		return (positive ? "+" : "-") + name;
	}

	/**
	 * Print the file structure.
	 */
	public void print() {
		print(0);
	}

	@Override
	public String toString() {
		return displayName();
	}

	/**
	 * Inserts a file into the current directory.
	 * 
	 * @param name the name of the file to insert
	 * @param isdir true if the new file should be a directory; false otherwise
	 * @return the new file that was inserted
	 * @throws Exception if the parent is not a directory
	 */
	private ConditionTree insert(String name, boolean isdir, boolean positive) throws FileStructureException {
		if (!this.isdir)
			throw new FileStructureException("FileStructure: cannot insert: not a directory");
		
		// create the node to insert
		ConditionTree child = new ConditionTree(name, isdir, positive);
		// set the parent
		child.parent = this;
		if (child.files != null) {
			child.files.put("..", child.parent);
		}
		// insert the node
		files.put(name, child);
		return child;
	}

	/**
	 * Recursively traverse the tree and print each node representing a
	 * directory structure.
	 * 
	 * @param level
	 */
	private void print(int level) {
		System.out.printf("%s%s\n", Strings.repeat('\t', level), displayName());
		if (files == null)
			return;

		// iterate over all files in the file structure
		ConditionTree file;
		for (Map.Entry<String, ConditionTree> entry : files.entrySet()) {
			file = entry.getValue();
			if (file == this || file == parent)
				continue;
			file.print(level + 1);
		}
	}

}
