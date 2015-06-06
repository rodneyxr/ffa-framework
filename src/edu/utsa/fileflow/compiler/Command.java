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
	 * Takes a command and breaks it up so that the arguments can be easily
	 * accessed. The command is also given a command type when tokenize() is
	 * called.
	 * 
	 * @param command
	 *            the command string to be broken up into arguments
	 */
	public Command(String command) {
		this.command = command;
		tokenize();
		type = CommandType.TypeFromString(args.get(0));
	}

	/**
	 * 
	 * @param n
	 *            the nth argument of the command (0 being the name of the
	 *            command)
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

	private void tokenize() {
		String[] argList = command.split("\\s+");
		args = new ArrayList<String>(Arrays.asList(argList));
	}

}
