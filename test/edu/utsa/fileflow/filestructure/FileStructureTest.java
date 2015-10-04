package edu.utsa.fileflow.filestructure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

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
		root.insertRegularFile(new FilePath("dir1/"));

		// assert warning is issued when touching a file that already exists
		exception.expect(FileFlowWarning.class);
		root.insertRegularFile(new FilePath("file1"));
		root.insertRegularFile(new FilePath("file1"));

		// assert warning is issued when touching a directory that already
		// exists
		exception.expect(FileFlowWarning.class);
		root.insertDirectory(new FilePath("dir1/"));
		root.insertRegularFile(new FilePath("dir1/"));

		assertEquals(true, root.fileExists(new FilePath("file1")));
		assertEquals(false, root.fileExists(new FilePath("dir1/")));
	}

	@Test
	public void testMkdir() throws Exception {
		FileStructure root = new FileStructure();
		root.insertDirectory(new FilePath("dir1/dir2/"));

		assertEquals(true, root.fileExists(new FilePath("dir1/")));
		assertEquals(true, root.fileExists(new FilePath("dir1/dir2/")));
	}

	@Test
	public void testExists() throws Exception {
		FileStructure root = new FileStructure();
		root.insertDirectory(new FilePath("dir1/dir2/dir3/"));
		root.insertRegularFile(new FilePath("dir1/dir2/dir3/file1"));

		FilePath[] shouldExist = new FilePath[] { new FilePath("dir1/dir2/dir3/file1"), new FilePath("dir1/dir2/dir3/"),
				new FilePath("dir1/dir2/"), new FilePath("dir1/"), new FilePath("/"), new FilePath(".."),
				new FilePath("."), };

		FilePath[] shouldNotExist = new FilePath[] { new FilePath("dir1/dir2/dir3/doesnotexist"),
				new FilePath("dir1/dir2/fakedir/"), new FilePath("dirfake/"), new FilePath("fakefile"), };

		// test the file paths that should exist
		for (FilePath fp : shouldExist) {
			assertTrue(fp.toString() + " does not exist", root.fileExists(fp));
		}

		// test the file paths that should not exist
		for (FilePath fp : shouldNotExist) {
			assertFalse(fp.toString() + " exists", root.fileExists(fp));
		}
	}

	@Test
	public void testRemove() throws Exception {
		FileStructure root = new FileStructure();
		root.insertDirectory(new FilePath("dir1/dir2/dir3/"));
		root.insertRegularFile(new FilePath("dir1/dir2/dir3/file1"));
		FilePath pathToRemove = new FilePath("dir1/dir2/dir3");

		// before removal dir3 should exist
		assertTrue(root.fileExists(pathToRemove));

		// remove and test to ensure dir3 no longer exists
		root.removeFile(pathToRemove);
		assertFalse(root.fileExists(pathToRemove));
	}

	@Test
	public void testCopy() throws Exception {
		FileStructure root = new FileStructure();
		FilePath file1 = new FilePath("file1");
		FilePath file2 = new FilePath("file2");

		// this copy should throw an exception because file1 does not exist
		try {
			root.copyFileToPath(file1, file2);
			fail("copying non-existing file should not have worked");
		} catch (FileStructureException e) {
			assertTrue(e.getMessage().contains("No such file or directory"));
		}

		// touch file1 and assert it exists
		root.insertRegularFile(file1);
		assertTrue(root.fileExists(file1));

		// copy file1 to file2 and assert the new file2 exists
		root.copyFileToPath(file1, file2);
		assertTrue(root.fileExists(file2));

		// make a directory to test copying a file into
		// test file to directory
		FilePath dir1 = new FilePath("dir1/");
		root.insertDirectory(dir1);
		root.copyFileToPath(file1, dir1);
		assertTrue(root.fileExists(new FilePath("dir1/file1")));

		// test directory to directory
		root = new FileStructure();
		FilePath dir2 = new FilePath("dir2/");
		FilePath dir1_file1 = new FilePath("dir1/file1");
		root.insertDirectory(dir1);
		root.insertDirectory(dir2);
		root.insertRegularFile(dir1_file1);
		root.copyFileToPath(dir1, dir2);
		assertTrue(root.fileExists(new FilePath("dir2/dir1/file1")));

		// test directory to existing directory
		root.insertRegularFile(new FilePath("dir1/file2"));
		root.copyFileToPath(dir1, dir2);
		assertTrue(root.fileExists(new FilePath("dir2/dir1/file1")));

		// test directory to file (should throw an exception)
		root = new FileStructure();
		root.insertRegularFile(file1);
		root.insertDirectory(dir1);
		exception.expect(FileStructureException.class);
		root.copyFileToPath(dir1, file1);

	}

	@Test
	public void testClone() throws Exception {
		// create a file structure and insert some files
		FileStructure f1 = new FileStructure();
		f1.insertDirectory(new FilePath("dir1/dir2/"));
		f1.insertDirectory(new FilePath("dir1/dir2/dir3"));
		f1.insertRegularFile(new FilePath("dir1/dir2/file1"));

		// clone the file structure
		FileStructure clone = f1.clone();

		// assert the files that are in f1 are also in the clone
		assertTrue(f1.fileExists(new FilePath("dir1/dir2/dir3")));
		assertTrue(clone.fileExists(new FilePath("dir1/dir2/dir3")));

		assertTrue(f1.fileExists(new FilePath("dir1/dir2/file1")));
		assertTrue(clone.fileExists(new FilePath("dir1/dir2/file1")));

		// make some changes to f1 and clone
		FilePath onlyInF1 = new FilePath("dir1/dir2/file2");
		f1.insertRegularFile(onlyInF1);

		FilePath onlyInClone = new FilePath("dir1/dir2/dir3/filea");
		clone.insertRegularFile(onlyInClone);

		// assert that that changes did not affect each other
		assertTrue(f1.fileExists(onlyInF1));
		assertFalse(clone.fileExists(onlyInF1));

		assertTrue(clone.fileExists(onlyInClone));
		assertFalse(f1.fileExists(onlyInClone));

	}

	@Test
	public void testMerge() throws Exception {
		// create some file paths to use for testing
		FilePath dir1_dir2 = new FilePath("dir1/dir2/");
		FilePath dira_dirb = new FilePath("dira/dirb/");
		FilePath dir1_dir2_file1 = new FilePath("dir1/dir2/file1");
		FilePath dir1_dir2_file2 = new FilePath("dir1/dir2/file2");
		FilePath dira_dirb_filea1 = new FilePath("dira/dirb/filea1");

		// create the destination root. sourceRoot will be merged into this
		FileStructure destinationRoot = new FileStructure();
		destinationRoot.insertDirectory(dir1_dir2);
		destinationRoot.insertRegularFile(dir1_dir2_file2);

		// create the source root to merge into destinationRoot
		FileStructure sourceRoot = new FileStructure();
		sourceRoot.insertDirectory(dir1_dir2);
		sourceRoot.insertRegularFile(dir1_dir2_file1);
		sourceRoot.insertDirectory(dira_dirb);

		// do the merge
		destinationRoot = destinationRoot.merge(sourceRoot);

		// assert files from both file structures are in destination
		assertTrue(destinationRoot.fileExists(dir1_dir2_file1));
		assertTrue(destinationRoot.fileExists(dir1_dir2_file2));
		assertTrue(destinationRoot.fileExists(dira_dirb));

		// modify the destination
		destinationRoot.insertRegularFile(dira_dirb_filea1);

		// assert that modifying destination after a merge doesn't affect the
		// source file structure
		assertFalse(sourceRoot.fileExists(dira_dirb_filea1));

		// test merging a file that is not a directory
		FileStructure root = new FileStructure();
		FilePath file1 = new FilePath("file1");
		root.insertRegularFile(file1);
		FileStructure file1Structure = root.getFile(file1);
		destinationRoot = destinationRoot.merge(file1Structure);
		assertTrue(destinationRoot.fileExists(file1));

		// test merging a directory with a file
		FileStructure file = root.getFile(file1);
		assertFalse(file.isDirectory());
		file = file.merge(destinationRoot);
		assertTrue(file.fileExists(file1) && file.fileExists(dir1_dir2_file1));
	}

	@Test
	public void testGetAllFilePaths() throws Exception {
		FileStructure root = new FileStructure();
		root.insertDirectory(new FilePath("dir1"));
		root.insertRegularFile(new FilePath("dir1/file1"));
		root.insertDirectory(new FilePath("dir1/dir2/"));

		ArrayList<FilePath> paths = root.getFile(new FilePath("dir1")).getAllFilePaths();
		assertTrue(paths.contains(new FilePath("dir1/dir2/")));
		assertTrue(paths.contains(new FilePath("dir1/file1")));

		root = new FileStructure();
		paths = root.getAllFilePaths();
		assertTrue(paths.isEmpty());
	}

	@Test
	public void testGetAbsolutePath() throws Exception {
		FileStructure root = new FileStructure();
		root.insertForce(new FilePath("dir1/dir2/dir3/file1"));
		FileStructure file1 = root.getFile(new FilePath("dir1/dir2/dir3/file1"));
		assertEquals(file1.getAbsolutePath(), new FilePath("root/dir1/dir2/dir3/file1"));

		assertEquals(root.getAbsolutePath(), new FilePath("root/"));
	}

	@Test
	public void testAbstractMerge() throws Exception {
		FileStructure fs1 = new FileStructure();
		FileStructure fs2 = new FileStructure();

		FilePath a = new FilePath("a");
		FilePath b = new FilePath("b");
		FilePath c = new FilePath("c");
		FilePath z = new FilePath("z");

		fs1.insertRegularFile(a);
		fs1.insertRegularFile(b);
		fs1.insertRegularFile(z);

		fs2.insertRegularFile(a);
		fs2.insertRegularFile(c);

		// merge fs1 and fs2, any differences should be marked optional
		FileStructure merged = FileStructure.abstractMerge(fs1, fs2);
		
		assertFalse(merged.getFile(a).isOptional());
		assertTrue(merged.getFile(b).isOptional());
		assertTrue(merged.getFile(c).isOptional());
		assertTrue(merged.getFile(z).isOptional());

		// ensure two identical optional nodes stay optional because abstract
		// merge should not see them as different, but it should still mark them
		// different so that the file will be optional
		merged = FileStructure.abstractMerge(merged, fs2);

		assertFalse(merged.getFile(a).isOptional());
		assertTrue(merged.getFile(b).isOptional());
		assertTrue(merged.getFile(c).isOptional());
		assertTrue(merged.getFile(z).isOptional());
	}
	
	@Test
	public void testAbstractMergeSimple() throws Exception {
		FileStructure fs1 = new FileStructure();
		FileStructure fs2 = new FileStructure();

		FilePath a = new FilePath("a");
		FilePath b = new FilePath("b");
		
		fs1.insertRegularFile(a);
		fs2.insertRegularFile(b);
		
		FileStructure merged = FileStructure.abstractMerge(fs1, fs2);
		
		assertTrue(merged.getFile(a).isOptional());
		assertTrue(merged.getFile(b).isOptional());
	}

}
