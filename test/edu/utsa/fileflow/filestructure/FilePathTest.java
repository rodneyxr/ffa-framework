/**
 * 
 */
package edu.utsa.fileflow.filestructure;

import static org.junit.Assert.*;

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
    public void testFilePathCreation() {
        FilePath filepath;

        // test paths as directories
        for (String path : VALID_FILE_PATHS) {
            filepath = new FilePath(path, true);
            assertEquals("filepath has unexpected value", path, filepath.getFilePath());
        }

        // test paths as directories
        for (String path : VALID_FILE_PATHS) {
            filepath = new FilePath(path, false);
            assertEquals("filepath has unexpected value", path, filepath.getFilePath());
        }

    }

}
