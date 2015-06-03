package edu.utsa.fileflow.filestructure;

import java.util.ArrayList;

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
		dirList.add(dir);
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
