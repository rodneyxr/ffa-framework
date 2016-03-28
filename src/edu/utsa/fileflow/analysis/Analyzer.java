package edu.utsa.fileflow.analysis;

import java.util.PriorityQueue;

import edu.utsa.fileflow.cfg.FlowPoint;
import edu.utsa.fileflow.cfg.FlowPointContext;
import edu.utsa.fileflow.cfg.FlowPointContextType;
import edu.utsa.fileflow.cfg.FlowPointEdge;

public class Analyzer {

	public static void analyze(FlowPoint cfg, Analysis analysis) {
		// initialize the workset
		PriorityQueue<FlowPoint> workset = new PriorityQueue<>();

		// add the start node to the workset
		updateAnalysis(cfg, new DummyAnalysisDomain(), analysis);
		workset.add(cfg);

		while (!workset.isEmpty()) {
			FlowPoint flowpoint = workset.poll();

			// add all children to the workset
			for (FlowPointEdge edge : flowpoint.getOutgoingEdgeList()) {
				FlowPoint child = edge.getTarget();
				// for each outgoing edge, compute y (new domain)
				// then check if y is different from the old domain
				// if so, update domain and target to workset
				AnalysisDomain y = updateAnalysis(child, flowpoint.domain, analysis);
				if (y.compareTo(child.domain) != 0) {
					child.domain = y;
					workset.add(child);
				}
			}

		}
	}

	private static AnalysisDomain updateAnalysis(FlowPoint target, AnalysisDomain inputDomain, Analysis analysis) {
		AnalysisDomain result = null;
		FlowPointContext fpctx = target.getContext();
		FlowPointContextType type = fpctx.getType();

		// FIXME: find an alternative to this
		if (target.domain == null) {
			target.domain = new DummyAnalysisDomain();
		}

		switch (type) {
		case ProgEnter:
			result = analysis.enterProg(inputDomain, fpctx);
			break;
		case ProgExit:
			result = analysis.exitProg(inputDomain, fpctx);
		case FunctionCall:
			if (fpctx.getText().startsWith("touch")) {
				result = analysis.touch(inputDomain, fpctx);
			} else if (fpctx.getText().startsWith("mkdir")) {
				result = analysis.mkdir(inputDomain, fpctx);
			} else if (fpctx.getText().startsWith("rm")) {
				result = analysis.remove(inputDomain, fpctx);
			} else if (fpctx.getText().startsWith("copy")) {
				result = analysis.copy(inputDomain, fpctx);
			}
			break;
		case WhileStatement:
			result = analysis.enterWhileStatement(inputDomain, fpctx);
			break;
		default:
			System.err.println("Not implemented: " + target);
			break;
		}
		return result;
	}

}
