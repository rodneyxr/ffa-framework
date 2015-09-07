package edu.utsa.fileflow.filestructure;

import java.io.File;

/**
 * This class will represent a file path within a file system. Separators must
 * be a forward slash '/'. Any file path that ends with a separator is a
 * directory.
 * 
 * @author Rodney Rodriguez
 *
 */
public class FilePath {

	private static final String REGEX_ENDING_PATH = ".*[/\\\\]\\s*$";
	private static final String SEPARATOR;

	static {
		String regex = File.separator;
		if (regex.equals("\\")) {
			regex = "\\\\";
		}
		SEPARATOR = regex;
	}

	private static final String REGEX_REPLACE_FIRST = String.format("[%s][^%s]+[%s]{0,1}$", SEPARATOR, SEPARATOR,
			SEPARATOR);

	private String path;
	private boolean isdir;

	public FilePath(String path) throws InvalidFilePathException {
		// check for an ending separator to determine if it is a slash
		this(path, path.matches(REGEX_ENDING_PATH));
	}

	public FilePath(String path, boolean isdir) throws InvalidFilePathException {
		this.path = clean(path);
		this.isdir = isdir;
	}

	/**
	 * Splits the file path into separate strings representing each level.
	 * 
	 * @return an array of strings representing file names
	 */
	public String[] tokens() {
		return path.split(SEPARATOR);
	}

	/**
	 * 
	 * @return A FilePath representing the parent directory of this file. In the
	 *         case where a file path is only one level and has no parent itself
	 *         will be returned.
	 */
	public FilePath pathToFile() {
		if (path.matches("[^" + SEPARATOR + "]+"))
			return this;
		try {
			return new FilePath(path.replaceFirst(REGEX_REPLACE_FIRST, ""), true);
		} catch (InvalidFilePathException e) {
			// this will never happen
			// return null to silence compiler error
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @return true if the path points to a directory
	 */
	public boolean isDir() {
		return isdir;
	}

	/**
	 * Strips spaces from both sides of the path.
	 * 
	 * @return a cleaned file path
	 * @throws InvalidFilePathException
	 *             if the path is null or has no base name
	 */
	private String clean(String path) throws InvalidFilePathException {
		if (path == null)
			throw new InvalidFilePathException("No file path was provided");
		path = new File(path).getPath();
		if (path.equals("")) {
			throw new InvalidFilePathException("No file path was provided");
		}
		return path;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof FilePath))
			return false;
		return ((FilePath) o).toString().equals(toString());
	}

	@Override
	public String toString() {
		if (isdir) {
			return path + File.separator;
		}
		return path;
	}

}
