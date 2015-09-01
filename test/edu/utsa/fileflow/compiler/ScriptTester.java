package edu.utsa.fileflow.compiler;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ScriptTester {

	private static final Pattern KW_TEST = Pattern.compile("TEST");
	private static final Pattern KW_EXPECTS = Pattern.compile("EXPECTS");
	private static final Pattern KW_PRE = Pattern.compile("pre:");
	private static final Pattern KW_POST = Pattern.compile("post:");
	private static final Pattern KW_WARN = Pattern.compile("warn:");
	private static final Pattern KW_ERROR = Pattern.compile("error:");

	private static final Pattern SYM_BRACE_OPEN = Pattern.compile("\\{");
	private static final Pattern SYM_BRACE_CLOSE = Pattern.compile("\\}");

	Compiler compiler;
	Scanner scanner;
	String token;

	@Before
	public void setUp() throws Exception {
		compiler = new Compiler();
		token = "";
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		// setup the input stream to read the test script
		scanner = new Scanner(new FileInputStream("scripts/test.script"));

		token = next();
		if (token == null) {
			// empty test script
			scanner.close();
			return;
		}
		expect(KW_TEST);

		// get test case name
		token = next();
		String caseName = token;
		System.out.println(caseName);

		token = next();
		expect(SYM_BRACE_OPEN);
		scanner.nextLine();

		// SCRIPT STARTS HERE
		StringBuilder script = new StringBuilder();
		while (!scanner.hasNext(SYM_BRACE_CLOSE)) {
			token = scanner.nextLine();
			script.append(token).append('\n');
		}
		System.out.println(script.toString());
		token = next();
		expect(SYM_BRACE_CLOSE);
		// SCRIPT ENDS HERE

		// EXPECTS STARTS HERE
		token = next();
		expect(KW_EXPECTS);

		// expect: pre, post, warn, error
		// read pre
		generateExpectList(KW_POST);
		generateExpectList(KW_WARN);
		generateExpectList(KW_ERROR);
		generateExpectList(SYM_BRACE_CLOSE);
		// EXPECTS ENDS HERE
		
		scanner.close();
	}

	private String next() {
		// skip comments
		while (scanner.hasNext("#.*")) {
			scanner.nextLine();
		}
		return scanner.next();
	}

	private ArrayList<String> generateExpectList(Pattern next) {
		ArrayList<String> list = new ArrayList<String>();

		while (!scanner.hasNext(next) && !scanner.hasNext(SYM_BRACE_CLOSE)) {
			token = scanner.nextLine();
			token = token.replaceFirst("#.*$", "");
			list.add(token.trim());
		}

		return list;
	}

	private ConditionManager script(String data) throws CompilerException {
		return compiler.compile(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)));
	}

	public void expect(Pattern pattern) throws Exception {
		if (!pattern.matcher(token).matches()) {
			throw new Exception(String.format("Expected '%s' but got '%s'", pattern, token));
		}
	}
}
