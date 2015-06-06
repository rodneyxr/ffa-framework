package edu.utsa.fileflow.utilities;

/**
 * Helper class to enable easy logging to STDOUT
 * 
 * @author Rodney
 *
 */
public class LuckyLogger {

	private String name;
	private boolean enabled;

	public LuckyLogger(String name) {
		this(name, true);
	}

	public LuckyLogger(String name, boolean enabled) {
		this.name = name;
		this.enabled = enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void log(String msg) {
		if (enabled)
			System.out.println(name + ": " + msg);
	}

	public void log(Object object) {
		log(object.toString());
	}

	public void log(String format, Object... args) {
		log(String.format(format, args));
	}

	public void log(short s) {
		log(Short.toString(s));
	}

	public void log(int i) {
		log(Integer.toString(i));
	}

	public void log(long l) {
		log(Long.toString(l));
	}

	public void log(float f) {
		log(Float.toString(f));
	}

	public void log(double d) {
		log(Double.toString(d));
	}

	public void log(char c) {
		log(Character.toString(c));
	}

	public void log(byte b) {
		log(Byte.toString(b));
	}

	public void log(boolean b) {
		log(Boolean.toString(b));
	}

	public String toString() {
		return String.format("LuckyLogger: { name = '%s', enabled = %b }", name, enabled);
	}
}
