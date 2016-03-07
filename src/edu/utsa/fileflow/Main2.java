package edu.utsa.fileflow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import edu.utsa.fileflow.analysis.Analyzer;
import edu.utsa.fileflow.cfg.FlowPoint;
import edu.utsa.fileflow.utilities.FileFlowHelper;

public class Main2 {

	static final String TEST_SCRIPT = "scripts/test.ffa";

	public static void main(String... args) throws FileNotFoundException, IOException {
		FlowPoint cfg = FileFlowHelper.generateControlFlowGraphFromScript(new File(TEST_SCRIPT));
		TestAnalysisImpl test = new TestAnalysisImpl();
		Analyzer.analyze(cfg, test);
	}

}
