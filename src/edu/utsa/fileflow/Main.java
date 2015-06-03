package edu.utsa.fileflow;

import edu.utsa.fileflow.compiler.Compiler;

public class Main {

	public static void main(String[] args) {
		System.out.println("Hello, World! Welcome to File Flow Analysis!");
		Compiler compile = new Compiler("test.script");
		compile.compile();
		
	}
	
}
