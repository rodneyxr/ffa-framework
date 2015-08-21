package edu.utsa.fileflow.filestructure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

		// assert warning is issued when touching a directory that already
		// exists
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
			assertTrue(fp.toString() + " does not exist", root.exists(fp));
		}

		// test the file paths that should not exist
		for (FilePath fp : shouldNotExist) {
			assertFalse(fp.toString() + " exists", root.exists(fp));
		}
	}

	@Test
	public void testRemove() throws Exception {
		FileStructure root = new FileStructure();
		root.mkdir(new FilePath("dir1/dir2/dir3/"));
		root.touch(new FilePath("dir1/dir2/dir3/file1"));
		FilePath pathToRemove = new FilePath("dir1/dir2/dir3");

		// before removal dir3 should exist
		assertTrue(root.exists(pathToRemove));

		// remove and test to ensure dir3 no longer exists
		root.remove(pathToRemove);
		assertFalse(root.exists(pathToRemove));
	}

	@Test
	public void testCopy() throws Exception {
		FileStructure root = new FileStructure();
		FilePath file1 = new FilePath("file1");
		FilePath file2 = new FilePath("file2");

		// this copy should throw an exception because file1 does not exist
		try {
			root.copy(file1, file2);
			fail("copying non-existing file should not have worked");
		} catch (FileStructureException e) {
			assertTrue(e.getMessage().contains("No such file or directory"));
		}

		// touch file1 and assert it exists
		root.touch(file1);
		assertTrue(root.exists(file1));

		// copy file1 to file2 and assert the new file2 exists
		root.copy(file1, file2);
		// FIXME: finish implementing copy method
		 assertTrue(root.exists(file2));
	}

	@Test
	public void testClone() throws Exception {
		// create a file structure and insert some files
		FileStructure f1 = new FileStructure();
		f1.mkdir(new FilePath("dir1/dir2/"));
		f1.mkdir(new FilePath("dir1/dir2/dir3"));
		f1.touch(new FilePath("dir1/dir2/file1"));

		// clone the file structure
		FileStructure clone = f1.clone();

		// assert the files that are in f1 are also in the clone
		assertTrue(f1.exists(new FilePath("dir1/dir2/dir3")));
		assertTrue(clone.exists(new FilePath("dir1/dir2/dir3")));
		
		assertTrue(f1.exists(new FilePath("dir1/dir2/file1")));
		assertTrue(clone.exists(new FilePath("dir1/dir2/file1")));

		// make some changes to f1 and clone
		FilePath onlyInF1 = new FilePath("dir1/dir2/file2");
		f1.touch(onlyInF1);
		
		FilePath onlyInClone = new FilePath("dir1/dir2/dir3/filea");
		clone.touch(onlyInClone);

		// assert that that changes did not affect each other
		assertTrue(f1.exists(onlyInF1));
		assertFalse(clone.exists(onlyInF1));
		
		assertTrue(clone.exists(onlyInClone));
		assertFalse(f1.exists(onlyInClone));
	}

}
