package edu.utsa.fileflow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import edu.utsa.fileflow.analysis.Analyzer;
import edu.utsa.fileflow.analysis.DummyAnalysis;
import edu.utsa.fileflow.analysis.DummyAnalysisDomain;
import edu.utsa.fileflow.cfg.FlowPoint;
import edu.utsa.fileflow.testutils.GraphvizGenerator;
import edu.utsa.fileflow.utilities.FileFlowHelper;

public class Main2 {

	private static final String TEST_SCRIPT = "scripts/test.ffa";

	public static void main(String[] args) throws FileNotFoundException, IOException {
		FlowPoint cfg = FileFlowHelper.generateControlFlowGraphFromFile(new File(TEST_SCRIPT));
		String dot = GraphvizGenerator.generateDOT(cfg);
		GraphvizGenerator.saveDOTToFile(dot, TEST_SCRIPT + ".dot");
		System.out.println("DOT file written to: '" + TEST_SCRIPT + ".dot'");

		Analyzer<DummyAnalysisDomain, DummyAnalysis> analyzer = new Analyzer<>(DummyAnalysisDomain.class,
				DummyAnalysis.class);
		analyzer.analyze(cfg);
	}
}
