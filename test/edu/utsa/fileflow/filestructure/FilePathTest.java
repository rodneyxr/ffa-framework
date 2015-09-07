/**
 * 
 */
package edu.utsa.fileflow.filestructure;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author Rodney
 *
 */
public class FilePathTest {

	@Rule
	public ExpectedException expex = ExpectedException.none();

	public final String[] VALID_FILE_PATHS = { "root", "root/", "file1", "./file1", "root/file1", "root/dir1/file1",
			"root/dir1/dir2", "./root/dir1/dir2/file1" };

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
	public void testFilePathCreation() throws Exception {
		FilePath filepath;

		// test paths as directories
		for (String path : VALID_FILE_PATHS) {
			filepath = new FilePath(path, true);
			assertEquals("filepath has unexpected value", true, filepath.isDir());
		}

		// test paths as files
		for (String path : VALID_FILE_PATHS) {
			filepath = new FilePath(path, false);
			assertEquals("filepath has unexpected value", false, filepath.isDir());
		}

	}

	@Test
	public void testPathToFile() throws Exception {
		// test a path with multiple levels
		FilePath dir1_dir2_file1 = new FilePath("dir1/dir2/file1");
		FilePath dir1_dir2 = new FilePath("dir1/dir2/");
		assertEquals(dir1_dir2.toString(), dir1_dir2_file1.pathToFile().toString());

		// test a path with only one level
		FilePath file1 = new FilePath("file1");
		assertEquals(file1, file1.pathToFile());
	}
	
	@Test
	public void testConcat() throws Exception {
		FilePath root_dir1 = new FilePath("dir1/dir2");
		FilePath file1 = new FilePath("file1");
		assertEquals(FilePath.concat(root_dir1, file1), new FilePath("dir1/dir2/file1"));
		
		FilePath root = new FilePath("root");
		assertEquals(FilePath.concat(root, file1), new FilePath("root/file1"));
	}

}
