package edu.utsa.fileflow;

import java.io.File;
import java.io.FileNotFoundException;

import edu.utsa.fileflow.compiler.Compiler;

public class Main {

	public static void main(String[] args) {
		System.out.println("Hello, World! Welcome to File Flow Analysis!");
		File file = new File("test.script");
		Compiler compile;

		try {
			compile = new Compiler(file);
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			return;
		}
		
		compile.compile();
		
	}
	
}
