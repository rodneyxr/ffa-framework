package edu.utsa.fileflow.testutils;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import edu.utsa.fileflow.compiler.Compiler;
import edu.utsa.fileflow.compiler.CompilerException;
import edu.utsa.fileflow.compiler.ConditionManager;
import edu.utsa.fileflow.filestructure.FilePath;

/**
 * A helper class to automatically test and assert scripts
 * 
 * @author Rodney Rodriguez
 *
 */
public class ScriptTester {

	private static final Pattern KW_TEST = Pattern.compile("TEST");
	private static final Pattern KW_EXPECTS = Pattern.compile("EXPECTS");
	private static final Pattern KW_PRE = Pattern.compile("pre:");
	private static final Pattern KW_POST = Pattern.compile("post:");
	private static final Pattern KW_WARN = Pattern.compile("warn:");
	private static final Pattern KW_ERROR = Pattern.compile("error:");

	private static final Pattern SYM_BRACE_OPEN = Pattern.compile("\\{");
	private static final Pattern SYM_BRACE_CLOSE = Pattern.compile("\\}");

	private static Scanner scanner;
	private static String token;
	private static String caseName;

	public static void testScript(String file) throws Exception {
		// setup the input stream to read the test script
		scanner = new Scanner(new FileInputStream(file));

		while (scanner.hasNext()) {
			token = "";

			token = next();
			if (token == null) {
				// empty test script
				scanner.close();
				return;
			}
			expect(KW_TEST);

			// get test case name
			token = next();
			caseName = token;

			token = next();
			expect(SYM_BRACE_OPEN);
			scanner.nextLine();

			// SCRIPT STARTS HERE
			StringBuilder script = new StringBuilder();
			while (!scanner.hasNext(SYM_BRACE_CLOSE)) {
				token = scanner.nextLine();
				script.append(token).append('\n');
			}

			String errorMessage = null;
			ConditionManager cm = null;
			try {
				cm = script(script.toString());
			} catch (Exception e) {
				errorMessage = e.getMessage();
			}

			token = next();
			expect(SYM_BRACE_CLOSE);
			// SCRIPT ENDS HERE

			token = next();
			expect(KW_EXPECTS);

			token = next();
			expect(SYM_BRACE_OPEN);
			// EXPECTS STARTS HERE

			// expect: pre, post, warn, error
			ArrayList<String> pre = generateExpectList(KW_PRE, KW_POST);
			ArrayList<String> post = generateExpectList(KW_POST, KW_WARN);
			ArrayList<String> warn = generateExpectList(KW_WARN, KW_ERROR);
			ArrayList<String> error = generateExpectList(KW_ERROR, SYM_BRACE_CLOSE);
			// EXPECTS ENDS HERE

			// assert precondition
			for (String s : pre) {
				assertTrue(String.format("%s: an error occurred", caseName),
						cm != null && cm.getPrecondition() != null);
				char sign = s.charAt(0);
				boolean negate = false;

				if (sign == '!') {
					s = s.substring(1);
					negate = true;
					sign = s.charAt(0);
				}

				switch (sign) {
				case '+':
					if (negate) {
						assertTrue(String.format("%s: pre: '%s' should not be present", caseName, s),
								!cm.getPrecondition().existsInPositive(new FilePath(s.substring(1))));
					} else {
						assertTrue(String.format("%s: pre: '%s' not found", caseName, s),
								cm.getPrecondition().existsInPositive(new FilePath(s.substring(1))));
					}
					break;
				case '-':
					if (negate) {
						assertTrue(String.format("%s: pre: '%s' should not be present", caseName, s),
								!cm.getPrecondition().existsInNegative(new FilePath(s.substring(1))));
					} else {
						assertTrue(String.format("%s: pre: '%s' not found", caseName, s),
								cm.getPrecondition().existsInNegative(new FilePath(s.substring(1))));
					}
					break;
				default:
					throw new Exception(String.format("%s: '%s' must begin with a '+' or '-'", caseName, s));
				}
			}

			// assert postcondition
			for (String s : post) {
				assertTrue(String.format("%s: an error occurred", caseName),
						cm != null && cm.getPostcondition() != null);
				char sign = s.charAt(0);
				boolean negate = false;

				if (sign == '!') {
					s = s.substring(1);
					negate = true;
					sign = s.charAt(0);
				}

				switch (sign) {
				case '+':
					if (negate) {
						assertTrue(String.format("%s: post: '%s' should not be present", caseName, s),
								!cm.getPostcondition().existsInPositive(new FilePath(s.substring(1))));
					} else {
						assertTrue(String.format("%s: post: '%s' not found", caseName, s),
								cm.getPostcondition().existsInPositive(new FilePath(s.substring(1))));
					}
					break;
				case '-':
					if (negate) {
						assertTrue(String.format("%s: post: '%s' should not be present", caseName, s),
								!cm.getPostcondition().existsInNegative(new FilePath(s.substring(1))));
					} else {
						assertTrue(String.format("%s: post: '%s' not found", caseName, s),
								cm.getPostcondition().existsInNegative(new FilePath(s.substring(1))));
					}
					break;
				default:
					throw new Exception(String.format("%s: '%s' must begin with a '+' or '-'", caseName, s));
				}
			}

			// assert warnings are found in the log
			for (String s : warn) {
				assertTrue(String.format("%s: an error occurred", caseName),
						cm != null && cm.getPostcondition() != null);
				s = s.toLowerCase();
				boolean contains = false;
				for (String l : cm.getLog()) {
					l = l.toLowerCase();
					if (l.contains(s)) {
						contains = true;
					}
				}
				assertTrue(String.format("%s: '%s' not found in log", caseName, s), contains);
			}

			for (String s : error) {
				s = s.toLowerCase();
				boolean contains = false;
				if (errorMessage != null && errorMessage.toLowerCase().contains(s)) {
					contains = true;
				}
				assertTrue(String.format("%s: '%s' not found in log", caseName, s), contains);
			}

			// end of test case
			token = next();
			expect(SYM_BRACE_CLOSE);
		}

		scanner.close();

	}

	public static ConditionManager script(String data) throws CompilerException {
		return new Compiler().compile(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)));
	}

	/**
	 * 
	 * @return the next token skipping any comments
	 */
	private static String next() {
		// skip comments
		while (scanner.hasNext("#.*")) {
			scanner.nextLine();
		}
		if (scanner.hasNext())
			return scanner.next();
		return null;
	}

	/**
	 * Generate a list for pre, post, warn, and error assertions
	 * 
	 * @param expect
	 *            what assertion list to make
	 * @param next
	 *            what token to stop at
	 * @return an list of the lines under an assertion
	 */
	private static ArrayList<String> generateExpectList(Pattern expect, Pattern next) {
		ArrayList<String> list = new ArrayList<String>();
		if (scanner.hasNext(expect)) {
			scanner.next();
		} else {
			return list;
		}

		while (!scanner.hasNext(next) && !scanner.hasNext(SYM_BRACE_CLOSE)) {
			token = scanner.nextLine();
			token = token.replaceFirst("#.*$", "");
			token = token.trim();
			if (token.length() > 0)
				list.add(token);
		}

		return list;
	}

	/**
	 * Asserts that a pattern was matched last by the scanner
	 * 
	 * @param pattern
	 *            the pattern to match
	 * @throws Exception
	 *             if the pattern does not match the last token scanned
	 */
	private static void expect(Pattern pattern) throws Exception {
		if (!pattern.matcher(token).matches()) {
			throw new Exception(String.format("Expected '%s' but got '%s'", pattern, token));
		}
	}
}
