package edu.utsa.fileflow.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Compiler {
	private Scanner scanner;

	/**
	 * initializes the parser object
	 * @param script - file name of the script to open for reading
	 */
	public Compiler(String script) {
		try {
			this.scanner = new Scanner(new File(script));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public void compile() {
		int line = 0;
		while (scanner.hasNext()) {
			Command cmd = new Command(scanner.nextLine());
			System.out.printf("cmd %d: %d\n", line, cmd.getSize());
			line++;
		}
	}
	
}
