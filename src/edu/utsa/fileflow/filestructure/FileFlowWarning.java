package edu.utsa.fileflow.filestructure;

@SuppressWarnings("serial")
public class FileFlowWarning extends Exception {
	public FileFlowWarning(String message) {
		super("Warning: " + message);
	}
}
