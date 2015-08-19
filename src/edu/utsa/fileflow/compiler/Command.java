package edu.utsa.fileflow.compiler;

import java.util.ArrayList;
import java.util.Arrays;

public class Command {

	// justifies the type of command
	private CommandType type;

	// the original command string given to the constructor
	private String command;

	// the arguments of the command. args.get(0) being the name of the command
	private ArrayList<String> args;

	/**
	 * Takes a command and breaks it up so that the arguments can be easily accessed.
	 * 
	 * @param command
	 *            the command string to be broken up into arguments
	 */
	public Command(String command) throws InvalidCommandException {
		setCommand(command);
		tokenize();
		type = CommandType.typeFromString(args.get(0));
	}

	/**
	 * 
	 * @param n
	 *            the nth argument of the command (0 being the name of the command)
	 * @return the nth argument
	 */
	public String getArg(int n) {
		return args.get(n);
	}

	public String getCommand() {
		return command;
	}

	public int getSize() {
		return args.size();
	}
	
	public CommandType getType() {
		return type;
	}

	@Override
	public String toString() {
		return command;
	}

	private void setCommand(String command) throws InvalidCommandException {
		if (command == null)
			throw new InvalidCommandException("null command string");
		this.command = command.trim();
	}


	private void tokenize() throws InvalidCommandException {
		String[] argList = command.split("\\s+");
		if (argList.length == 0)
			throw new InvalidCommandException("empty command");
		args = new ArrayList<String>(Arrays.asList(argList));
	}
	
}
