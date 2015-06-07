package edu.utsa.fileflow.compiler;

@SuppressWarnings("serial")
public class InvalidCommandException extends Exception {
	public InvalidCommandException(String message) {
		super("InvalidCommandException: " + message);
	}
}
