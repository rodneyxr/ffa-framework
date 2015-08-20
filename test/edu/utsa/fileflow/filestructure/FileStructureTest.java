package edu.utsa.fileflow.filestructure;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author Rodney Rodriguez
 *
 */
public class FileStructureTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();

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
	public void testTouch() throws Exception {
		FileStructure root = new FileStructure();

		// assert exception is thrown when touching non-existing directory
		exception.expect(FileStructureException.class);
		root.touch(new FilePath("dir1/"));

		// assert warning is issued when touching a file that already exists
		exception.expect(FileFlowWarning.class);
		root.touch(new FilePath("file1"));
		root.touch(new FilePath("file1"));

		// assert warning is issued when touching a directory that already exists
		exception.expect(FileFlowWarning.class);
		root.mkdir(new FilePath("dir1/"));
		root.touch(new FilePath("dir1/"));

		assertEquals(true, root.exists(new FilePath("file1")));
		assertEquals(false, root.exists(new FilePath("dir1/")));
	}
	
	@Test
	public void testMkdir() throws Exception {
		FileStructure root = new FileStructure();
		root.mkdir(new FilePath("dir1/dir2/"));
		
		assertEquals(true, root.exists(new FilePath("dir1/")));
		assertEquals(true, root.exists(new FilePath("dir1/dir2/")));
	}

	@Test
	public void testExists() throws Exception {
		FileStructure root = new FileStructure();
		root.mkdir(new FilePath("dir1/dir2/dir3/"));
		root.touch(new FilePath("dir1/dir2/dir3/file1"));

		FilePath[] shouldExist = new FilePath[] { new FilePath("dir1/dir2/dir3/file1"), new FilePath("dir1/dir2/dir3/"),
				new FilePath("dir1/dir2/"), new FilePath("dir1/"), new FilePath("/"), new FilePath(".."),
				new FilePath("."), };

		FilePath[] shouldNotExist = new FilePath[] { new FilePath("dir1/dir2/dir3/doesnotexist"),
				new FilePath("dir1/dir2/fakedir/"), new FilePath("dirfake/"), new FilePath("fakefile"),
				new FilePath(""), };

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
	public void testRemove() throws Exception {
		FileStructure root = new FileStructure();
		root.mkdir(new FilePath("dir1/dir2/dir3/"));
		root.touch(new FilePath("dir1/dir2/dir3/file1"));
		FilePath pathToRemove = new FilePath("dir1/dir2/dir3");

		// before removal dir3 should exist
		assertEquals(true, root.exists(pathToRemove));

		// remove and test to ensure dir3 no longer exists
		root.remove(pathToRemove);
		assertEquals(false, root.exists(pathToRemove));
	}

}
