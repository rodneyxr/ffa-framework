package edu.utsa.fileflow.compiler;

public enum CommandType {
	NEW("new"), COPY("cp"), MOVE("mv"), DELETE("rm");

	String name;

	CommandType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static CommandType TypeFromString(String name) {
		for (CommandType type : CommandType.values()) {
			if (type.getName().equals(name))
				return type;
		}
		return null;
	}
}
