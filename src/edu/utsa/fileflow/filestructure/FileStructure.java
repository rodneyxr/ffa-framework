package edu.utsa.fileflow.filestructure;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import edu.utsa.fileflow.utilities.Strings;

public class FileStructure {

	private TreeMap<String, FileStructure> files;
	private String name;
	private FileStructure parent;
	private boolean isdir;

	public FileStructure() {
		this("root", true);
	}

	private FileStructure(String name, boolean isdir) {
		this.name = name;
		this.isdir = isdir;
		parent = this;
		if (isdir) {
			files = new TreeMap<String, FileStructure>();
			files.put(".", this);
			files.put("..", parent);
		} else {
			files = null;
		}
	}

	/**
	 * Traverse through the path and touch the file at the end of the path. The
	 * path to the file must exist.
	 * 
	 * @param path
	 *            The path to the file to be touched
	 * @return the file structure that was created
	 * @throws FileStructureException
	 * @throws FileFlowWarning
	 */
	public FileStructure touch(FilePath path) throws FileStructureException, FileFlowWarning {
		FileStructure cp = this;
		String[] tokens = path.tokens();

		for (int i = 0; i < tokens.length - 1; i++) {
			FileStructure next = cp.files.get(tokens[i]);
			if (next != null) {
				if (!next.isdir) {
					throw new FileStructureException(String.format("touch: cannot touch ‘%s’: Not a directory", path));
				}
				cp = next;
			} else {
				throw new FileStructureException(
						String.format("touch: cannot touch ‘%s’: No such file or directory", path));
			}
		}

		// check if the file already exists
		FileStructure next = cp.files.get(tokens[tokens.length - 1]);

		if (next != null) {
			// cp = next;
			// issue a warning that the file already exists
			throw new FileFlowWarning(String.format("touch: ‘%s’: File or directory already exists", path));
		} else if (path.isDir()) {
			// if it doesn't exist but is a directory throw an exception
			throw new FileStructureException(
					String.format("touch: setting times of ‘%s’: No such file or directory", path));
		} else {
			cp = cp.insert(tokens[tokens.length - 1], false);
		}

		return cp;
	}

	/**
	 * Makes the directory at the path provided. If the path to that directory
	 * does not exist, it will be created.
	 *
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public FileStructure mkdir(FilePath path) throws FileStructureException {
		if (!isdir)
			throw new FileStructureException("cannot insert: not a directory");
		if (exists(path)) {
			throw new FileStructureException(String.format("mkdir: cannot create directory ‘path’: File exists", path));
		}

		FileStructure cp = this; // save the current pointer

		// create the directory path
		for (String token : path.tokens()) {
			if (!cp.isdir)
				throw new FileStructureException(String.format("mkdir: cannot create directory ‘%s’: Not a directory", path));
			// peek ahead to check if next level exists
			FileStructure next = cp.files.get(token);
			if (next != null) {
				// move to next level if a directory
				if (!next.isdir)
					throw new FileStructureException(String.format("mkdir: cannot create directory ‘%s’: Not a directory", path));
				cp = next;
			} else {
				// create the next level and move pointer to the next level
				cp = cp.insert(token, true);
			}
		}

		return cp;
	}

	/**
	 * Removes a file from the file structure.
	 * 
	 * @param path
	 *            The path to the file to be removed
	 * @return the file that was removed or null if it does not exist
	 */
	public FileStructure remove(FilePath path) {
		FileStructure fs = get(path);
		if (fs == null)
			return null;
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
	 * @param path
	 *            the path to the file to be returned
	 * @return the file that the path points to or null if it does not exist
	 */
	public FileStructure get(FilePath path) {
		FileStructure cp = this;
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
	 * @param path
	 *            The path to check if it exists
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
			return name + File.separator;
		return name;
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
	 * @param name
	 *            the name of the file to insert
	 * @param isdir
	 *            true if the new file should be a directory; false otherwise
	 * @return the new file that was inserted
	 * @throws Exception
	 *             if the parent is not a directory
	 */
	private FileStructure insert(String name, boolean isdir) throws FileStructureException {
		if (!this.isdir)
			throw new FileStructureException("FileStructure: cannot insert: not a directory");

		// create the node to insert
		FileStructure child = new FileStructure(name, isdir);
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
		FileStructure file;
		for (Map.Entry<String, FileStructure> entry : files.entrySet()) {
			file = entry.getValue();
			if (file == this || file == parent)
				continue;
			file.print(level + 1);
		}
	}

	// /**
	// * Inserts a path into the directory.
	// *
	// * @param path
	// * @return
	// * @throws Exception
	// */
	// public FileStructure insert(FilePath path) throws FileStructureException
	// {
	// if (!isdir)
	// throw new FileStructureException("cannot insert: not a directory");
	//
	// FileStructure cp = this; // save the current pointer
	// String[] tokens = path.tokens();
	// int dirPathSize = tokens.length;
	// if (!path.isDir())
	// dirPathSize -= 1;
	//
	// // create the directory path first
	// for (int i = 0; i < dirPathSize; i++) {
	// if (!cp.isdir)
	// throw new FileStructureException("cannot insert: not a directory");
	// // peek ahead to check if next level exists
	// FileStructure peek = cp.files.get(tokens[i]);
	// if (peek != null) {
	// // move to next level if a directory
	// if (!peek.isdir)
	// throw new FileStructureException("cannot insert: not a directory");
	// cp = peek;
	// } else {
	// // create the next level and move pointer to the next level
	// cp = cp.insert(tokens[i], true);
	// }
	// }
	//
	// // insert the last token if the path is a file
	// if (!path.isDir()) {
	// cp = cp.insert(tokens[tokens.length - 1], false);
	// }
	//
	// return cp;
	// }

}
