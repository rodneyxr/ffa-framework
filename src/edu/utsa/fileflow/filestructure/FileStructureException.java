package edu.utsa.fileflow.filestructure;

@SuppressWarnings("serial")
public class FileStructureException extends Exception {
	public FileStructureException(String message) {
		super("FileException: " + message);
	}
}
