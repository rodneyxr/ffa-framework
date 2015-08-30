package edu.utsa.fileflow.filestructure;

@SuppressWarnings("serial")
public class InvalidFilePathException extends Exception {
	public InvalidFilePathException(String message) {
		super("InvalidFilePathException: " + message);
	}
}
