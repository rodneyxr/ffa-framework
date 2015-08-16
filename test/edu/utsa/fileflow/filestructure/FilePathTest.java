package edu.utsa.fileflow.filestructure;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

/**
 * Created by Rodney Rodriguez on 8/16/15.
 *
 * This test class is used for testing the functionality of the FileStruct class.
 */
public class FilePathTest {

    @Rule
    public ExpectedException expex = ExpectedException.none();

    public final String[] VALID_FILE_PATHS = { "root", "root/", "file1", "./file1", "root/file1", "root/dir1/file1",
        "root/dir1/dir2", "./root/dir1/dir2/file1" };

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testFilePathCreation() throws Exception {
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
