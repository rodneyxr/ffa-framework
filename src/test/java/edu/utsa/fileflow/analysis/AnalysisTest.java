package edu.utsa.fileflow.analysis;

import edu.utsa.fileflow.cfg.FlowPoint;
import edu.utsa.fileflow.utilities.FileFlowHelper;
import edu.utsa.fileflow.utilities.GraphvizGenerator;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Rodney Rodriguez
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
        assertEquals(dot1, dot2, "CFG should not be modified by analysis.");
    }

}
