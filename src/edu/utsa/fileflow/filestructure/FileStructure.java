package edu.utsa.fileflow.filestructure;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;
import java.util.Map;
import java.util.StringJoiner;
import java.util.TreeMap;

import edu.utsa.fileflow.utilities.Strings;

public class FileStructure implements Cloneable {

	private TreeMap<String, FileStructure> files;
	private String name;
	private FileStructure parent;

	// TODO: consider UNKNOWN types
	private FileStructureType type;

	public String prefix;

	public FileStructure() {
		this("root", FileStructureType.DIRECTORY);
	}

	private FileStructure(String name, FileStructureType type) {
		this.name = name;
		this.type = type;
		this.prefix = "";
		parent = this;
		if (isDirectory()) {
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
	public FileStructure insertRegularFile(FilePath path) throws FileStructureException, FileFlowWarning {
		FileStructure cp = this;
		String[] tokens = path.tokens();

		for (int i = 0; i < tokens.length - 1; i++) {
			FileStructure next = cp.files.get(tokens[i]);
			if (next != null) {
				if (!next.isDirectory()) {
					throw new FileStructureException(String.format("touch: cannot touch '%s': Not a directory", path));
				}
				cp = next;
			} else {
				throw new FileStructureException(
						String.format("touch: cannot touch '%s': No such file or directory", path));
			}
		}

		// check if the file already exists
		FileStructure next = cp.files.get(tokens[tokens.length - 1]);

		if (next != null) {
			// cp = next;
			// issue a warning that the file already exists
			throw new FileFlowWarning(String.format("touch: '%s': File or directory already exists", path));
		} else if (path.isDirectory()) {
			// if it doesn't exist but is a directory throw an exception
			throw new FileStructureException(
					String.format("touch: setting times of '%s': No such file or directory", path));
		} else {
			cp = cp.insertFile(tokens[tokens.length - 1], FileStructureType.REGULAR_FILE);
		}

		return cp;
	}

	/**
	 * Makes the directory at the path provided. If the path to that directory
	 * does not exist, it will be created.
	 *
	 * @param path
	 *            The file path to make the directory in
	 * @return the file structure that was inserted
	 * @throws FileStructureException
	 */
	public FileStructure insertDirectory(FilePath path) throws FileStructureException {
		if (!isDirectory())
			throw new FileStructureException("cannot insert: not a directory");
		if (fileExists(path)) {
			throw new FileStructureException(String.format("mkdir: cannot create directory '%s': File exists", path));
		}

		FileStructure cp = this; // save the current pointer

		// create the directory path
		for (String token : path.tokens()) {
			if (!cp.isDirectory())
				throw new FileStructureException(
						String.format("mkdir: cannot create directory '%s': Not a directory", path));
			// peek ahead to check if next level exists
			FileStructure next = cp.files.get(token);
			if (next != null) {
				// move to next level if a directory
				if (!next.isDirectory())
					throw new FileStructureException(
							String.format("mkdir: cannot create directory '%s': Not a directory", path));
				cp = next;
			} else {
				// create the next level and move pointer to the next level
				cp = cp.insertFile(token, FileStructureType.DIRECTORY);
			}
		}

		return cp;
	}

	/**
	 * Adds a file structure to the map of files.
	 *
	 * @param fs
	 *            the file structure to add
	 * @return the file structure that was inserted
	 */
	public FileStructure insertFileStructure(FileStructure fs) {
		fs.parent = this;
		if (fs.isDirectory()) {
			fs.files.put("..", this);
		}
		files.put(fs.name, fs);
		return fs;
	}

	/**
	 * Inserts a path into the directory.
	 *
	 * @param path
	 *            The path to insert into the directory
	 * @return the file structure that was inserted
	 * @throws Exception
	 */
	public FileStructure insertForce(FilePath path) throws FileStructureException {
		if (!isDirectory())
			throw new FileStructureException(String.format("cannot touch '%s': Not a directory", path));

		FileStructure cp = this; // save the current pointer
		String[] tokens = path.tokens();
		int dirPathSize = tokens.length;
		if (!path.isDirectory())
			dirPathSize -= 1;

		// create the directory path first
		for (int i = 0; i < dirPathSize; i++) {
			if (!cp.isDirectory())
				throw new FileStructureException(String.format("cannot touch '%s': Not a directory", path));
			// peek ahead to check if next level exists
			FileStructure peek = cp.files.get(tokens[i]);
			if (peek != null) {
				// move to next level if a directory
				if (!peek.isDirectory())
					throw new FileStructureException(String.format("cannot touch '%s': Not a directory", path));
				cp = peek;
			} else {
				// create the next level and move pointer to the next level
				cp = cp.insertFile(tokens[i], FileStructureType.DIRECTORY);
			}
		}

		// insert the last token if the path is a file
		if (!path.isDirectory()) {
			cp = cp.insertFile(tokens[tokens.length - 1], FileStructureType.REGULAR_FILE);
		}

		return cp;
	}

	/**
	 * Copies a file denoted by a file path to another location in the directory
	 * structure. Consider the following cases:
	 * <p/>
	 * -> file1 to file2: If file2 exists it will be overwritten; else it will
	 * be created
	 * <p/>
	 * -> file1 to dir2: file1 will be copied into dir2 overwriting file1 in
	 * dir2 if it exists
	 * <p/>
	 * -> dir1 to dir2: a copy of dir1 will be created in dir2. if dir2/dir1
	 * happens to exist, contents will be merged overwriting the existing
	 * <p/>
	 * -> dir1 to file2: cp: cannot overwrite non-directory 'file2' with
	 * directory 'dir1'
	 * <p/>
	 * -> file1 to non-existing: cp: cannot create regular file 'dir1/file1': No
	 * such file or directory
	 * <p/>
	 * -> dir1 to non-existing: cp: cannot create directory 'dir2/dir1': No such
	 * file or directory
	 *
	 * @param sourcePath
	 *            The path pointing to the source file to copy
	 * @param destinationPath
	 *            The path pointing to the destination location
	 * @return the new file structure that was created
	 * @throws FileStructureException
	 */
	public FileStructure copyFileToPath(FilePath sourcePath, FilePath destinationPath) throws FileStructureException {
		// get the source file and check if it exists
		FileStructure src = getFile(sourcePath);
		if (src == null) {
			throw new FileStructureException(
					String.format("cp: cannot stat '%s': No such file or directory", sourcePath.getPath()));
		}

		// check if the paths point to the same file
		if (sourcePath.equals(destinationPath)) {
			throw new FileStructureException(
					String.format("cp: '%s' and '%s' are the same file", sourcePath.getPath(), sourcePath.getPath()));
		}

		// find the node to insert at
		FileStructure pointer = this;
		String[] tokens = destinationPath.tokens();
		String filename = tokens[tokens.length - 1];

		for (int i = 0; i < tokens.length; i++) {
			// get the next level
			FileStructure next = pointer.files.get(tokens[i]);
			if (next != null) {
				// if the next level exists, move the pointer to it
				pointer = next;
			} else {
				// the next level does not exist
				// if the next level happens to be the last level of the path
				// and is a directory then insert into the current pointer
				if (i == tokens.length - 1 && pointer.isDirectory()) {
					// rename and copy the source to the destination name
					src = src.clone();
					src.name = filename;
					return pointer.insertFileStructure(src);
				} else {
					// there is no path to the destination path in the file
					// structure
					if (src.isDirectory()) {
						throw new FileStructureException(String.format(
								"cp: cannot create directory '%s': No such file or directory", destinationPath.getPath()));
					} else {
						throw new FileStructureException(String.format(
								"cp: cannot create regular file '%s': No such file or directory", destinationPath.getPath()));
					}
				}
			}
		}

		// here pointer is the destination

		if (!src.isDirectory() && !pointer.isDirectory()) {
			// file1 to file2: If file2 exists it will be overwritten; else it
			// will be created
			src = src.clone();
			src.name = filename;
			pointer.parent.insertFileStructure(src);

		} else if (!src.isDirectory() && pointer.isDirectory()) {
			// file1 to dir2: file1 will be copied into dir2 overwriting file1
			// in dir2 if it exists
			pointer.insertFileStructure(src.clone());

		} else if (src.isDirectory() && pointer.isDirectory()) {
			// dir1 to dir2: a copy of dir1 will be created in dir2. if
			// dir2/dir1 happens to exist, contents will be merged overwriting
			// the existing
			if (pointer.files.get(filename) != null) {
				// if destination exists then merge
				src = pointer.merge(src);
				src.name = filename;
			} else {
				src = src.clone();
			}
			pointer.insertFileStructure(src);

		} else if (src.isDirectory() && !pointer.isDirectory()) {
			// dir1 to file2: cp: cannot overwrite non-directory 'file2' with
			// directory 'dir1'
			throw new FileStructureException(String.format(
					"cp: cannot overwrite non-directory '%s' with directory '%s'", destinationPath.getPath(), sourcePath.getPath()));

		}

		return src;
	}

	/**
	 * Merge two files structures together. All files under the invoking file
	 * structure will be overwritten by the files under the file structure that
	 * is passed in. Note that a clone of source will be merged so source will
	 * not be modified.
	 *
	 * @param source
	 *            The file structure to merge into the invoking file structure.
	 *            The files under this will overwrite any files under the
	 *            invoking file structure.
	 * @return a new FileStructure object with both file structures merged
	 */
	public FileStructure merge(FileStructure source) {
		return clone().mergeImpl(source.clone());
	}

	/**
	 * If the file is a directory then the paths of all files under the
	 * directory will be returned. If it is a regular file then its name will be
	 * returned
	 * 
	 * @return an ArrayList of all file paths under this file
	 */
	public ArrayList<FilePath> getAllFilePaths() {
		ArrayList<FilePath> paths = new ArrayList<FilePath>();
		try {
			// call to recursive method that will get all paths under this file
			getAllFilePathsImpl("", paths);
		} catch (InvalidFilePathException e) {
			// this will never happen
			e.printStackTrace();
		}

		Collections.reverse(paths);
		return paths;
	}

	/**
	 * Gives the full path to this file. The file path returned does not include
	 * a preceding file separator.
	 * 
	 * ex: file1 will return 'root/file1' and not '/root/file1'
	 * 
	 * @return the full path to this file
	 */
	public FilePath getAbsolutePath() {
		// create a list to store all levels visited on the way up to root
		ArrayList<String> pathList = new ArrayList<String>();

		// traverse the tree to the root (when the parent is itself)
		FileStructure fs = this;
		pathList.add(fs.name);
		while (fs != fs.parent) {
			fs = fs.parent;
			pathList.add(fs.name);
		}

		// join the path list with the file separator
		StringJoiner sj = new StringJoiner(File.separator);
		ListIterator<String> li = pathList.listIterator(pathList.size());
		while (li.hasPrevious()) {
			sj.add(li.previous());
		}

		// convert the string to a file path and return
		FilePath fp = null;
		try {
			fp = new FilePath(sj.toString(), type);
		} catch (InvalidFilePathException e) {
			// this will never happen
			e.printStackTrace();
		}
		return fp;
	}

	/**
	 * Removes a file from the file structure.
	 *
	 * @param path
	 *            The path to the file to be removed
	 * @return the file that was removed or null if it does not exist
	 */
	public FileStructure removeFile(FilePath path) {
		FileStructure fs = getFile(path);
		if (fs == null)
			return null;
		fs.parent.files.remove(fs.name);
		fs.parent = fs;
		if (fs.isDirectory()) {
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
	public FileStructure getFile(FilePath path) {
		FileStructure cp = this;
		for (String filename : path.tokens()) {
			if (cp.files == null)
				return null;
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
	public boolean fileExists(FilePath path) {
		return getFile(path) != null;
	}

	/**
	 * @return the name with an ending slash if the file is a directory
	 */
	public String displayName() {
		if (isDirectory())
			return prefix + name + File.separator;
		return prefix + name;
	}

	/**
	 * 
	 * @return the type of this file structure
	 */
	public FileStructureType getType() {
		return type;
	}

	/**
	 * @return true if this file is a directory; false otherwise
	 */
	public boolean isDirectory() {
		return type == FileStructureType.DIRECTORY;
	}

	/**
	 * @return true if this file is a regular file; false otherwise
	 */
	public boolean isRegularFile() {
		return type == FileStructureType.REGULAR_FILE;
	}

	/**
	 * Print the file structure.
	 */
	public void print() {
		print(0);
	}

	/**
	 * Performs a deep copy of the file structure.
	 *
	 * @return a clone of the file structure
	 */
	@Override
	public FileStructure clone() {
		FileStructure clone = new FileStructure(name, type);
		clone.prefix = prefix;

		if (!isDirectory()) {
			return clone;
		}
		for (Map.Entry<String, FileStructure> entry : files.entrySet()) {
			FileStructure file = entry.getValue();
			if (file == this || file == parent)
				continue;
			clone.insertFileStructure(file.clone());
		}

		return clone;
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
	 * @param type
	 *            the type that the new file should be
	 * @return the new file that was inserted
	 * @throws Exception
	 *             if the parent is not a directory
	 */
	private FileStructure insertFile(String name, FileStructureType type) throws FileStructureException {
		if (!this.isDirectory())
			throw new FileStructureException("FileStructure: cannot insert: not a directory");

		// create the node to insert
		FileStructure child = new FileStructure(name, type);
		child.prefix = prefix;

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
	 * Merge two files structures together. All files under the invoking file
	 * structure will be overwritten by the files under the file structure that
	 * is passed in. Source will be modified using this method. See
	 * merge(FileStructure source) to avoid making changes to source.
	 *
	 * @param source
	 *            The file structure to merge into the invoking file structure.
	 *            The files under this will overwrite any files under the
	 *            invoking file structure.
	 * @return the original file structure merged with the fs
	 */
	private FileStructure mergeImpl(FileStructure source) {
		// if the source is the same is the destination there is nothing to
		// merge so just return this
		if (source == this) {
			return this;
		}

		// if destination is a file just insert source
		if (!source.isDirectory()) {
			insertFileStructure(source);
			return this;
		} else if (!isDirectory()) {
			source.insertFileStructure(this);
			return source;
		}

		// for all entries in source
		for (Map.Entry<String, FileStructure> entry : source.files.entrySet()) {
			// cache the value
			FileStructure srcFile = entry.getValue();
			// ignore '.' and '..'
			if (srcFile == source || srcFile == source.parent)
				continue;
			// put all files in source and merge directories
			if (srcFile.isDirectory()) {
				// get destination directory
				FileStructure destDir = files.get(srcFile.name);
				// check if it exists
				if (destDir == null) {
					// if it doesn't exist then just insert it
					insertFileStructure(srcFile);
				} else {
					// if it exists then merge the destination into the source
					destDir.mergeImpl(srcFile);
				}
			} else {
				// if its a file just insert since overwriting is acceptable
				insertFileStructure(srcFile);
			}

		}

		return this;
	}

	/**
	 * Recursively traverse the tree and print each node representing a
	 * directory structure.
	 *
	 * @param level
	 */
	private void print(int level) {
		System.out.printf("%s%s\n", Strings.repeat("   ", level), displayName());
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

	/**
	 * 
	 * @param path
	 *            the current path that this recursive method is in
	 * @param paths
	 *            the reference to the list of paths
	 * @return the filename of the current file
	 * @throws InvalidFilePathException
	 */
	private String getAllFilePathsImpl(String path, ArrayList<FilePath> paths) throws InvalidFilePathException {
		if (files == null)
			return joinPath(path, name);

		// iterate over all files in the file structure
		FileStructure file;
		for (Map.Entry<String, FileStructure> entry : files.entrySet()) {
			file = entry.getValue();
			if (file == this || file == parent)
				continue;
			paths.add(new FilePath(file.getAllFilePathsImpl(joinPath(path, name), paths), file.getType()));
		}
		return joinPath(path, name);
	}

	/**
	 * Helper method to concatenate a file path with a filename.
	 * 
	 * @param path
	 *            the path to append the file name to
	 * @param filename
	 *            the file name to append to the path
	 * @return the file path joined with the filename
	 */
	private String joinPath(String path, String filename) {
		if (path == "" || path == null) {
			return filename;
		}
		return String.join(File.separator, path, filename);
	}

}
