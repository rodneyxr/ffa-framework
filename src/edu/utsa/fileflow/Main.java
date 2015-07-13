package edu.utsa.fileflow;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import edu.utsa.fileflow.compiler.Compiler;
import edu.utsa.fileflow.compiler.CompilerException;
import edu.utsa.fileflow.filestructure.FileStruct;
import edu.utsa.fileflow.utilities.LuckyLogger;

public class Main {

	public static final String version = "1.0.0";
	public static final boolean DEBUG = true;
	public static final LuckyLogger logger = new LuckyLogger("DEBUG");
	private static Scanner input = new Scanner(System.in);
	
	public static void main(String[] args) {
		logger.setEnabled(DEBUG);
		
		System.out.println("File Flow Analysis Version "+version+"\nWritten by Rodney Rodriguez and Steven Petroff \nfor The University of Texas at San Antonio");
		System.out.println("\nMenu:");
		System.out.println(" 1. To select a file to load type \"load\" or \"1\"");
		System.out.println(" 2. To use stdin to define a new script type \"stdin\" or \"2\"");
		System.out.println(" 3. To review the command dictionary type \"help\" or \"3\"");
		System.out.println("To quit the program type \"quit or q\"");
		String cmd = input.nextLine();
		
		while(!cmd.toLowerCase().equals("q") && !cmd.toLowerCase().equals("quit")){
			if(cmd.equals("1") || cmd.equals("load")){
				System.out.println("Please specify the path of the file: ");
				String filepath = input.nextLine();
				loadFile(filepath);
			}
			
			if(cmd.equals("2") || cmd.equals("stdin")){
				option_stdin();
			}
			if(cmd.equals("3") || cmd.equals("help")){
				printCommandDictionary();
				
			}
			
			cmd = input.nextLine();
		}
		
	}
	
	private static void option_stdin() {
		File tempFile = new File("tempfile.txt");
		FileWriter fw = null;
		if(!tempFile.exists()){
			try {
				tempFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			fw = new FileWriter(tempFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Please enter the commands you wish to enter one by one, with the last one being \"end\"");
		System.out.println(" or to review the command dictionary, type \"help\"");
		String cmd = input.nextLine();
		while(!cmd.toLowerCase().equals("end")){
			try {
				fw.write(cmd+"\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			cmd = input.nextLine();
		}
		try {
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		loadFile("tempfile.txt");
				
	}

	private static void printCommandDictionary() {
		System.out.println("\nAll commands are in the following format:\n\tcommand arg1 arg2");
		System.out.println("Create a file:\t\t new filename");
		System.out.println("Copy a file or dir:\t cp source destination");
		System.out.println("Move a file or dir:\t mv source destination");
		System.out.println("Delete a file or dir:\t rm source\n");
	}

	private static void loadFile(String fp){
		// create a file for the script to compile
		File file = new File(fp);
		Compiler compiler = new Compiler();

		FileStruct preCondition = null;
		try {
			preCondition = compiler.compile(file);
		} catch (CompilerException ce) {
			System.err.println(ce.getMessage());
			System.exit(-1);
		}

		System.out.println("Pre-condition Filestructure");
		System.out.println(preCondition);

		System.out.println("\nPost-condition Filestructure");
		System.out.println(compiler.getPost());
	}

}
