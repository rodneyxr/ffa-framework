package edu.utsa.fileflow.compiler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.utsa.fileflow.filestructure.ConditionTree;
import edu.utsa.fileflow.filestructure.FilePath;
import edu.utsa.fileflow.filestructure.FileStructureException;

/**
 * @author Rodney Rodriguez
 *
 */
public class ConditionTreeTest {

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
	public void testPrint() throws FileStructureException {
		ConditionTree root = new ConditionTree();
		root.insert(new FilePath("dir1/dir2/file1"), false);
		root.insert(new FilePath("dir1/dir2/file2"), true);
		root.insert(new FilePath("dir1/dir2/file3"), true);
		root.insert(new FilePath("dira/dirb/filea"), true);
		root.insert(new FilePath("dira/dirb/fileb"), false);
		root.print();
	}

}
