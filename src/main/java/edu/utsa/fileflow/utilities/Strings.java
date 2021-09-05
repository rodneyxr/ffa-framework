package edu.utsa.fileflow.utilities;

public class Strings {

	public static String repeat(final char c, final int repeat) {
		return new String(new char[repeat]).replace('\0', c);
	}
	
	public static String repeat(final String s, final int repeat) {
		return new String(new char[repeat]).replace("\0", s);
	}
	
}
