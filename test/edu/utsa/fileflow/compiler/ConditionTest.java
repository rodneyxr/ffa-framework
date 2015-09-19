package edu.utsa.fileflow.compiler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.utsa.fileflow.filestructure.FilePath;

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
		System.out.println("Post1:");
		post1.print();
		
		System.out.println();
		
		Condition post2 = new Condition();
		post2.insertNegative(file1);
		System.out.println("Post2:");
		post2.print();
		
		System.out.println();
		
		Condition mergedPost = Condition.merge(post2, post1);
		System.out.println("Merged Post:");
		mergedPost.print();
	}

}
