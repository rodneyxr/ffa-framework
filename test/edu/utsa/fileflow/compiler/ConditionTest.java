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

	@Test
	public void testAbstractMerge() throws Exception {
		FilePath a = new FilePath("a");
		FilePath b = new FilePath("b");
		FilePath c = new FilePath("c");
		ConditionManager init = ScriptTester.script("touch a");
		// pre:
		// -a
		// post:
		// +a
		assertTrue(init.getPrecondition().existsInNegative(a));
		assertTrue(init.getPostcondition().existsInPositive(a));

		// if (X) <- branch here so we consider all possible paths and merge
		// branch1 -> pre: -a -b | post: +a +b
		ConditionManager branch1 = ScriptTester.script("touch a\ntouch b");
		assertTrue(branch1.getPrecondition().existsInNegative(a));
		assertTrue(branch1.getPrecondition().existsInNegative(b));
		assertTrue(branch1.getPostcondition().existsInPositive(a));
		assertTrue(branch1.getPostcondition().existsInPositive(b));
		// else
		// branch2 -> pre: -a -c | post: +a +c
		ConditionManager branch2 = ScriptTester.script("touch a\ntouch c");
		assertTrue(branch2.getPrecondition().existsInNegative(a));
		assertTrue(branch2.getPrecondition().existsInNegative(c));
		assertTrue(branch2.getPostcondition().existsInPositive(a));
		assertTrue(branch2.getPostcondition().existsInPositive(c));

		// merge branch1 and branch2
		Condition mergedPre = Condition.abstractMerge(branch1.getPrecondition(), branch2.getPrecondition());
		Condition mergedPost = Condition.abstractMerge(branch1.getPostcondition(), branch2.getPostcondition());

		// DEBUG
		// System.out.println("Precondition");
		// mergedPre.print();
		// System.out.println("\nPostcondition");
		// mergedPost.print();

		// pre:
		// -a
		// -b (opt.)
		// -c (opt.)
		assertTrue(mergedPre.existsInNegative(a) && !mergedPre.negative.getFile(a).isOptional());
		assertTrue(mergedPre.existsInNegative(b) && mergedPre.negative.getFile(b).isOptional());
		assertTrue(mergedPre.existsInNegative(c) && mergedPre.negative.getFile(c).isOptional());

		// post:
		// +a
		// +b (opt.)
		// +c (opt.)
		assertTrue(mergedPost.existsInPositive(a) && !mergedPost.positive.getFile(a).isOptional());
		assertTrue(mergedPost.existsInPositive(b) && mergedPost.positive.getFile(b).isOptional());
		assertTrue(mergedPost.existsInPositive(c) && mergedPost.positive.getFile(c).isOptional());
	}

}
