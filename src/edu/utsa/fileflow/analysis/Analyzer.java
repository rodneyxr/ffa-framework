package edu.utsa.fileflow.analysis;

import edu.utsa.fileflow.TestAnalysisDomainImpl;
import edu.utsa.fileflow.cfg.FlowPoint;
import edu.utsa.fileflow.cfg.FlowPointContext;
import edu.utsa.fileflow.cfg.FlowPointContextType;
import edu.utsa.fileflow.cfg.FlowPointEdge;

public class Analyzer {

	public static void analyze(FlowPoint cfg, Analysis analysis) {
		AnalysisDomain<?> domain = new TestAnalysisDomainImpl();

		FlowPointContext fpctx = cfg.getContext();
		FlowPointContextType type = fpctx.getType();

		// perform analysis on root
		updateAnalysis(type, domain, analysis, fpctx);

		// recursive analysis on CFG
		// TODO: handle loops
		analyzeRec(cfg, analysis, domain);
	}

	private static void analyzeRec(FlowPoint fp, Analysis analysis, AnalysisDomain<?> domain) {
		for (FlowPointEdge edge : fp.getOutgoingEdgeList()) {
			FlowPointContext fpctx = edge.getTarget().getContext();
			FlowPointContextType type = fpctx.getType();
			updateAnalysis(type, domain, analysis, fpctx);
		}

		// recursive analysis for children
		for (FlowPointEdge edge : fp.getOutgoingEdgeList()) {
			analyzeRec(edge.getTarget(), analysis, domain);
		}
	}

	private static AnalysisDomain<?> updateAnalysis(FlowPointContextType type, final AnalysisDomain<?> domain,
			Analysis analysis, FlowPointContext fpctx) {
		AnalysisDomain<?> result = null;
		switch (type) {
		case ProgEnter:
			result = analysis.enterProg(domain, fpctx);
			break;
		case FunctionCall:
			if (fpctx.getText().startsWith("touch")) {
				result = analysis.touch(domain, fpctx);
			} else if (fpctx.getText().startsWith("mkdir")) {
				result = analysis.mkdir(domain, fpctx);
			} else if (fpctx.getText().startsWith("rm")) {
				result = analysis.remove(domain, fpctx);
			} else if (fpctx.getText().startsWith("copy")) {
				result = analysis.copy(domain, fpctx);
			}
			break;
		default:
			break;
		}
		return result;
	}

}
