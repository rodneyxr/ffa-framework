package edu.utsa.fileflow.analysis;

import edu.utsa.fileflow.TestAnalysisDomainImpl;
import edu.utsa.fileflow.cfg.FlowPoint;
import edu.utsa.fileflow.cfg.FlowPointContext;
import edu.utsa.fileflow.cfg.FlowPointContextType;
import edu.utsa.fileflow.cfg.FlowPointEdge;

public class Analyzer {

	public static void analyze(FlowPoint cfg, Analysis analysis) {
		AnalysisDomain<?> domain = new TestAnalysisDomainImpl();

		// TODO: traverse CFG and update analysis domain along the way
		FlowPointContext fpctx = cfg.getContext();
		FlowPointContextType type = fpctx.getType();
		updateAnalysis(type, domain, analysis, fpctx);

		FlowPoint c = cfg;
		for (FlowPointEdge edge : c.getOutgoingEdgeList()) {
			FlowPoint fp = edge.getTarget();
			fpctx = fp.getContext();
			type = fpctx.getType();
			updateAnalysis(type, domain, analysis, fpctx);
		}

	}

	private static void updateAnalysis(FlowPointContextType type, final AnalysisDomain<?> domain, Analysis analysis,
			FlowPointContext fpctx) {
		switch (type) {
		case ProgEnter:
			analysis.enterProg(domain, fpctx);
			break;
		case FunctionCall:
			if (fpctx.getText().startsWith("touch")) {
				analysis.touch(domain, fpctx);
			} else if (fpctx.getText().startsWith("mkdir")) {
				analysis.mkdir(domain, fpctx);
			} else if (fpctx.getText().startsWith("rm")) {
				analysis.remove(domain, fpctx);
			} else if (fpctx.getText().startsWith("copy")) {
				analysis.copy(domain, fpctx);
			}
			break;
		default:
			break;
		}
	}

}
