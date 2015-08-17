package edu.utsa.fileflow.filestructure;

import java.io.File;
import java.io.IOException;

/**
 * This class will represent a file path within a file system. Separators must
 * be a forward slash '/'. Any file path that ends with a separator is a
 * directory.
 * 
 * @author Rodney Rodriguez
 *
 */
public class FilePath {

	private String path;
	private boolean isdir;

	public FilePath(String path) {
		// check for an ending separator to determine if it is a slash
		this(path, path.matches(".*[/\\\\]\\s*$"));
	}
	
	public FilePath(String path, boolean isdir) {
		this.path = clean(path);
		this.isdir = isdir;
	}

	/**
	 * Splits the file path into separate strings representing each level.
	 * 
	 * @return an array of strings representing file names
	 */
	public String[] tokens() {
		String regex = File.separator;
		if (regex.equals("\\")) {
			regex = "\\\\";
		}
		return path.split(regex);
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
	 * @throws IOException
	 */
	private String clean(String path) {
		return new File(path).getPath();
	}

	public String toString() {
		return path;
	}

}
