package edu.utsa.fileflow.compiler;

@SuppressWarnings("serial")
public class CompilerException extends Exception {
	public CompilerException(String message) {
		super("CompilerException: " + message);
	}
}
