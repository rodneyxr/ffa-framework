package edu.utsa.fileflow.compiler;

public enum CommandType {
	NEW("new"), COPY("cp"), MOVE("mv"), DELETE("rm");
	
	String name;
	
	CommandType(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
