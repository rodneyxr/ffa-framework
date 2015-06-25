package edu.utsa.fileflow.filestructure;

public class FilePath {

	private String filePath;

	public FilePath(String filePath) {
		this.filePath = filePath;
	}

	public FilePath getPathToFile() {
		if (filePath.matches("[^/\\\\]+"))
			return new FilePath("");
		return new FilePath(filePath.replaceFirst("[/\\\\][^/\\\\]+[/\\\\]{0,1}$", ""));
	}

	public String[] getTokens() {
		if (filePath.length() == 0)
			return new String[0];
		return filePath.split("[/\\\\]");
	}
	
	public String getFileName() {
		return filePath.replaceFirst("^.*[/\\\\]", "");
	}

	public String getFilePath() {
		return filePath;
	}

	@Override
	public String toString() {
		return filePath;
	}

}
