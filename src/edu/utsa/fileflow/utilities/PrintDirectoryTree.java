package edu.utsa.fileflow.utilities;

import edu.utsa.fileflow.filestructure.Directory;

public class PrintDirectoryTree {

	/**
	 * Pretty print the directory tree and its file names.
	 * 
	 * @param folder
	 *            must be a folder.
	 * @return
	 */
	public static String printDirectoryTree(Directory root) {

	    int indent = 0;
	    StringBuilder sb = new StringBuilder();
	    printDirectoryTree(root, indent, sb);
	    return sb.toString();
	}

	private static void printDirectoryTree(Directory dir, int indent,
	        StringBuilder sb) {

	    sb.append(getIndentString(indent));
	    sb.append("+--");
	    sb.append(dir.getDirName());
	    sb.append("");
	    sb.append("\n");
	    for (Directory d : dir.getDirList()) {
//	        if (file.isDirectory()) {
	            printDirectoryTree(d, indent + 1, sb);
//	        } else {
//	            printFile(file, indent + 1, sb);
//	        }
	    }

	}

//	private static void printFile(Directory file, int indent, StringBuilder sb) {
//	    sb.append(getIndentString(indent));
//	    sb.append("+--");
//	    sb.append(file.getName());
//	    sb.append("\n");
//	}

	private static String getIndentString(int indent) {
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < indent; i++) {
	        sb.append("|  ");
	    }
	    return sb.toString();
	}
}
