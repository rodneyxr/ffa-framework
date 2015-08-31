package edu.utsa.fileflow.compiler;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.utsa.fileflow.filestructure.FilePath;

public class ScriptsTest {

	private Compiler compiler;

	@Before
	public void setUp() throws Exception {
		compiler = new Compiler();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		ConditionManager cm = script(""
				+ "touch file1\n"
				+ "touch file2\n");
		
		Precondition pre = cm.getPrecondition();
		Postcondition post = cm.getPostcondition();
		
		assertTrue(pre.negative.fileExists(new FilePath("file1")));
		assertTrue(pre.negative.fileExists(new FilePath("file2")));
		assertTrue(post.positive.fileExists(new FilePath("file1")));
		assertTrue(post.positive.fileExists(new FilePath("file2")));

	}

	private ConditionManager script(String data) throws CompilerException {
		return compiler.compile(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)));
	}

}
