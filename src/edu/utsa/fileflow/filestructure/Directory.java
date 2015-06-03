package edu.utsa.fileflow.filestructure;

import java.util.ArrayList;

import edu.utsa.fileflow.compiler.Command;

public class Directory {

	private String dirName;
	private int dirLevel;
	private ArrayList<Directory> dirList;
	private ArrayList<FileName> fileList;
	
	/**
	 * This constructor will instantiate a new Directory object.
	 * @param name - name of the directory 
	 * @param level - level of the directory relative to 'root'
	 */
	public Directory(String name, int level){
		setDirName(name);
		setDirLevel(level);
		dirList = new ArrayList<Directory>();
		fileList = new ArrayList<FileName>();
	}

	/**
	 * Adds a new directory to the end of the dirList array list.
	 * @param newDir
	 */
	public void addDirectoryToList(Directory dir){
		System.out.println("Adding "+dir.dirName+" to "+dirName);
		this.dirList.add(dir);
	}

	/**
	 * Removes a directory from the dirList array list.
	 * @param dir
	 */
	public void removeDirectoryFromList(Directory dir){
		dirList.remove(dir);
	}
	
	/**
	 * Adds a file to the end of the fileList array list.
	 * @param file
	 */
	public void addFileToList(FileName file){
		fileList.add(file);
	}
	
	/**
	 * Removes a file from the fileList array list.
	 * @param file
	 */
	public void removeFileFromList(FileName file){
		fileList.remove(file);
	}
	
	
	public void createStructure(String[] tokens, int nextIndex) {
		
		if(!checkIfDirectoryExists(tokens[nextIndex])){
			//create directory
			Directory newDir = new Directory(tokens[nextIndex],0);
			this.addDirectoryToList(newDir);
			if(nextIndex < tokens.length-1){
				int newIndex = nextIndex+1;
				newDir.createStructure(tokens, newIndex);
			}
		}
		
	}
	
	public void printDirectories(int tabCount){
		String tabs = "";
		for(int i = 0; i < tabCount; i++)
			tabs+="\t";
		
		System.out.println(dirName);
		
		for(Directory dir : dirList){
			System.out.print(dir.dirName);
			dir.printDirectories(tabCount++);
		}
		System.out.print("\n");
	}
	
	private boolean checkIfDirectoryExists(String dirname){
		for(Directory dir : dirList){
			if(dir.dirName.equals(dirname))
				return true;
		}
		return false;
	}
	
	private boolean checkIfFileExists(String filename){
		for(FileName file : fileList){
			if(file.getFileName().equals(filename))
				return true;
		}
		return false;
	}

	/*
	 *  Getters and Setters for global class variables
	 */
	public String getDirName() {
		return dirName;
	}

	public void setDirName(String dirName) {
		this.dirName = dirName;
	}

	public int getDirLevel() {
		return dirLevel;
	}

	public void setDirLevel(int dirLevel) {
		this.dirLevel = dirLevel;
	}

	public ArrayList<Directory> getDirList() {
		return dirList;
	}

	public void setDirList(ArrayList<Directory> dirList) {
		this.dirList = dirList;
	}

	public ArrayList<FileName> getFileList() {
		return fileList;
	}

	public void setFileList(ArrayList<FileName> fileList) {
		this.fileList = fileList;
	}


}
