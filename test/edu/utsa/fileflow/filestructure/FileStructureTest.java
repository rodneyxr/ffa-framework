package edu.utsa.fileflow.filestructure;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rodney Rodriguez
 *
 */
public class FileStructureTest {

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
	public void testExists() throws FileStructureException {
		FileStructure root = new FileStructure();
		root.insert(new FilePath("dir1/dir2/dir3/file1"));
		
		FilePath[] shouldExist = new FilePath[]{
				new FilePath("dir1/dir2/dir3/file1"),
				new FilePath("dir1/dir2/dir3/"),
				new FilePath("dir1/dir2/"),
				new FilePath("dir1/"),
				new FilePath("/"),
				new FilePath(".."),
				new FilePath("."),
		};
		
		FilePath[] shouldNotExist = new FilePath[]{
				new FilePath("dir1/dir2/dir3/doesnotexist"),
				new FilePath("dir1/dir2/fakedir/"),
				new FilePath("dirfake/"),
				new FilePath("fakefile"),
				new FilePath(""),
		};
		
		// test the file paths that should exist
		for (FilePath fp : shouldExist) {
			assertEquals(fp.toString() + " does not exist", true, root.exists(fp));
		}
		
		// test the file paths that should not exist
		for (FilePath fp : shouldNotExist) {
			assertEquals(fp.toString() + " exists", false, root.exists(fp));
		}
	}
	
	@Test
	public void testRemove() throws FileStructureException {
		FileStructure root = new FileStructure();
		root.insert(new FilePath("dir1/dir2/dir3/file1"));
		FilePath pathToRemove = new FilePath("dir1/dir2/dir3");
		
		// before removal dir3 should exist
		assertEquals(true, root.exists(pathToRemove));
		
		// remove and test to ensure dir3 no longer exists
		root.remove(pathToRemove);
		assertEquals(false, root.exists(pathToRemove));
	}

}
