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
	private FileStructureType type;

	public FilePath(String path) throws InvalidFilePathException {
		// check for an ending separator to determine if it is a slash
		this(path, path.matches(REGEX_ENDING_PATH) ? FileStructureType.DIRECTORY : FileStructureType.UNKNOWN);
	}

	public FilePath(String path, FileStructureType type) throws InvalidFilePathException {
		this.path = clean(path);
		this.type = type;
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
			return new FilePath(path.replaceFirst(REGEX_REPLACE_FIRST, ""), FileStructureType.DIRECTORY);
		} catch (InvalidFilePathException e) {
			// this will never happen
			// return null to silence compiler error
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Concatenate two file paths together.
	 * 
	 * @param fp1
	 *            preceding file path
	 * @param fp2
	 *            following file path
	 * @return a new file path with the two paths combined
	 */
	public static FilePath concat(FilePath fp1, FilePath fp2) {
		String path = fp1.path + SEPARATOR + fp2.path;
		FilePath fpNew = null;
		try {
			fpNew = new FilePath(path, fp2.getType());
		} catch (InvalidFilePathException e) {
			// this will never happen
			e.printStackTrace();
		}
		return fpNew;
	}

	/**
	 * 
	 * @return true if the path points to a directory
	 */
	public boolean isDirectory() {
		return type == FileStructureType.DIRECTORY;
	}

	/**
	 * 
	 * @return the type that this file path represents
	 */
	public FileStructureType getType() {
		return type;
	}

	/**
	 * Sets the type that this file path should represent
	 * 
	 * @param type
	 *            the type that this file path should represent
	 */
	public void setType(FileStructureType type) {
		this.type = type;
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
	
	/**
	 * 
	 * @return a string representation of the file path
	 */
	public String getPath() {
		return path;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof FilePath))
			return false;
		return ((FilePath) o).path.equals(path);
	}

	@Override
	public String toString() {
		if (isDirectory()) {
			return path + File.separator;
		} else if (type == FileStructureType.UNKNOWN) {
			return path + "?";
		}
		return path;
	}

}
