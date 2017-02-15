package edu.utsa.fileflow.analysis;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import edu.utsa.fileflow.cfg.FlowPoint;
import edu.utsa.fileflow.utilities.GraphvizGenerator;
import edu.utsa.fileflow.utilities.FileFlowHelper;

/**
 * 
 * @author Rodney Rodriguez
 *
 */
public class AnalysisTest {

	private static final String TEST_SCRIPT = "scripts/tests/test.ffa";

	@Test
	public void testTraversal() throws Exception {
		FlowPoint cfg = FileFlowHelper.generateControlFlowGraphFromFile(new File(TEST_SCRIPT));
		DummyAnalysisDomain domain = new DummyAnalysisDomain();
		DummyAnalysis analysis = new DummyAnalysis();
		Analyzer<DummyAnalysisDomain, DummyAnalysis> analyzer = new Analyzer<>(domain, analysis);
		analyzer.analyze(cfg);
	}

	@Test
	public void testDotFile() throws Exception {
		FlowPoint cfg = FileFlowHelper.generateControlFlowGraphFromFile(new File(TEST_SCRIPT));

		// generate DOT file before analysis
		String dot1 = GraphvizGenerator.generateDOT(cfg);

		// perform dummy analysis
		DummyAnalysisDomain domain = new DummyAnalysisDomain();
		DummyAnalysis analysis = new DummyAnalysis();
		Analyzer<DummyAnalysisDomain, DummyAnalysis> analyzer = new Analyzer<>(domain, analysis);
		analyzer.analyze(cfg);

		// generate DOT file after analysis
		String dot2 = GraphvizGenerator.generateDOT(cfg);

		// DOT files should be identical after analysis
		assertEquals("CFG should not be modified by analysis.", dot1, dot2);
	}

}
