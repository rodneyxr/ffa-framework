package edu.utsa.fileflow.analysis;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import edu.utsa.fileflow.cfg.FlowPoint;
import edu.utsa.fileflow.testutils.GraphvizGenerator;
import edu.utsa.fileflow.utilities.FileFlowHelper;

/**
 * 
 * @author Rodney Rodriguez
 *
 */
public class AnalysisTest {

	private static final String TEST_SCRIPT = "scripts/test.ffa";

	@Rule
	public ExpectedException expectException = ExpectedException.none();

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
	public void testTraversal() throws Exception {
		FlowPoint cfg = FileFlowHelper.generateControlFlowGraphFromScript(new File(TEST_SCRIPT));
		Analyzer<DummyAnalysisDomain, DummyAnalysis> analyzer = new Analyzer<>(DummyAnalysisDomain.class,
				DummyAnalysis.class);
		analyzer.analyze(cfg);
	}

	@Test
	public void testDotFile() throws Exception {
		FlowPoint cfg = FileFlowHelper.generateControlFlowGraphFromScript(new File(TEST_SCRIPT));

		// generate DOT file before analysis
		String dot1 = GraphvizGenerator.generateDOT(cfg);

		// perform prefix analysis
		Analyzer<DummyAnalysisDomain, DummyAnalysis> analyzer = new Analyzer<>(DummyAnalysisDomain.class,
				DummyAnalysis.class);
		analyzer.analyze(cfg);

		// generate DOT file after analysis
		String dot2 = GraphvizGenerator.generateDOT(cfg);

		// DOT files should be identical after analysis
		assertEquals("CFG should not be modified by analysis.", dot1, dot2);
	}

}
