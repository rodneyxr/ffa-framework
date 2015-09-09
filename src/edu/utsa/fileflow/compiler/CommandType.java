package edu.utsa.fileflow.compiler;

import java.util.HashMap;

public enum CommandType {
	touch("touch"), mkdir("mkdir"), cp("cp"), rm("rm"), mv("mv");

	// this HashMap will provide a fast lookup time for the typeFromString() method
	private static final HashMap<String, CommandType> CMD_MAP;
	static {
		CMD_MAP = new HashMap<String, CommandType>();
		for (CommandType type : CommandType.values())
			CMD_MAP.put(type.name, type);
	}

	private String name;

	CommandType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static CommandType typeFromString(String name) throws InvalidCommandException {
		CommandType type = CMD_MAP.get(name);
		if (type == null)
			throw new InvalidCommandException("Unknown command '" + name + "'");
		return type;
	}
}
