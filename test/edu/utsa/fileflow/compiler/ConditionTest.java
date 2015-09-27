package edu.utsa.fileflow.compiler;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.utsa.fileflow.filestructure.FilePath;
import edu.utsa.fileflow.testutils.ScriptTester;

/**
 * @author Rodney
 *
 */
public class ConditionTest {

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
	public void testMerge() throws Exception {
		FilePath file1 = new FilePath("file1");

		Condition post1 = new Condition();
		post1.insertPositive(file1);
		assertTrue(post1.positive.fileExists(file1));

		Condition post2 = new Condition();
		post2.insertNegative(file1);
		assertTrue(post2.negative.fileExists(file1));

		Condition mergedPost = Condition.merge(post2, post1);
		// FIXME: Condition.MERGE_PRINT hides positive file
		assertTrue(mergedPost.positive.fileExists(file1));
		assertTrue(mergedPost.negative.fileExists(file1));
	}

	@Test // TODO: Implement this test method
	public void testAbstractMerge() throws Exception {
		ConditionManager cm1 = ScriptTester.script("touch a");
		// pre:
		// -a
		// post:
		// +a

		// if (X)
		ConditionManager cm2 = ScriptTester.script("touch b");
		// pre:
		// -b
		// post:
		// +b
		
		// MERGE
		// pre:
		// -a (opt.)
		// -b (opt.)
		// post:
		// +a (opt.)
		// +b (opt.)
		
		// test precondition merge
		Condition pre1 = cm1.getPrecondition();
		Condition pre2 = cm2.getPrecondition();
		Condition merged = Condition.abstractMerge(pre1, pre2);
		merged.print();
		

		// else
//		ConditionManager cm3 = ScriptTester.script("touch c");

	}

}
