package edu.utsa.fileflow.filestructure;

public class FileName {

	private String fileName;
	private int fileLevel;
	private Directory parentDir;
	private FileType fileType;
	
	public FileName(String fileName, int fileLevel, Directory parentDir, FileType fileType){
		setFileName(fileName);
		setFileLevel(fileLevel);
		setParentDir(parentDir);
		setFileType(fileType);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getFileLevel() {
		return fileLevel;
	}

	public void setFileLevel(int fileLevel) {
		this.fileLevel = fileLevel;
	}

	public Directory getParentDir() {
		return parentDir;
	}

	public void setParentDir(Directory parentDir) {
		this.parentDir = parentDir;
	}

	public FileType getFileType() {
		return fileType;
	}

	public void setFileType(FileType fileType) {
		this.fileType = fileType;
	}
}
