package edu.utsa.fileflow.analyzer;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * 
 * @author Rodney Rodriguez
 *
 */
public class AnalyzerTest {

	@Rule
	public ExpectedException expectException = ExpectedException.none();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParentMergeSequence() throws Exception {
		for (int i = 1; i < 10; i++) {
			System.out.printf("%d %d\n", i, i+1);
		}
	}

}
