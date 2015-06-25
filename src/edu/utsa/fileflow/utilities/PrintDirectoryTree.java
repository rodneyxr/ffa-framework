package edu.utsa.fileflow.utilities;

import java.util.HashMap;

import edu.utsa.fileflow.filestructure.FileStruct;

public class PrintDirectoryTree {

	/**
	 * Pretty print the directory tree and its file names.
	 * 
	 * @param folder
	 *            must be a folder.
	 * @return
	 */
	public static String printDirectoryTree(FileStruct root) {
		StringBuilder sb = new StringBuilder();
		printDirectoryTree(root, 0, sb);
		return sb.toString();
	}

	private static void printDirectoryTree(FileStruct dir, int indent, StringBuilder sb) {
		sb.append(getIndentString(indent));
		sb.append("+--");
		sb.append(dir.getName());
		sb.append("");
		sb.append("\n");
		for (HashMap.Entry<String, FileStruct> entry : dir.getFiles().entrySet()) {
			printDirectoryTree(entry.getValue(), indent + 1, sb);
		}

	}

	private static String getIndentString(int indent) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < indent; i++) {
			sb.append("|  ");
		}
		return sb.toString();
	}
}
