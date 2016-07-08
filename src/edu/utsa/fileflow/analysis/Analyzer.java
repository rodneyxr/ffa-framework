package edu.utsa.fileflow.analysis;

import java.util.Stack;

import edu.utsa.fileflow.cfg.FlowPoint;
import edu.utsa.fileflow.cfg.FlowPointContext;
import edu.utsa.fileflow.cfg.FlowPointContextType;
import edu.utsa.fileflow.cfg.FlowPointEdge;

public class Analyzer<D extends AnalysisDomain<D>, A extends Analysis<D>> {

	D domain;
	A analysis;

	private D exitDomain;

	public Analyzer(Class<D> d, Class<A> a) {
		try {
			// TODO: create factory interface for these
			domain = d.newInstance();
			analysis = a.newInstance();

			if (domain.bottom() == null) {
				System.err.println(
						"Analysis Error: " + domain.getClass().getSimpleName() + ".bottom() cannot return null.");
				System.exit(1);
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void analyze(FlowPoint cfg) {

		// initialize all nodes to bottom
		cfg.getAllFlowPoints().forEach(fp -> {
			fp.domain = domain.bottom();
		});
		analysis.onBegin(domain);

		// initialize the workset
		Stack<FlowPoint> workset = new Stack<>();

		// add the start node to the workset
		updateAnalysis(domain, cfg);
		workset.add(cfg);

		while (!workset.isEmpty()) {
			FlowPoint flowpoint = workset.pop();

			// add all children to the workset
			for (FlowPointEdge edge : flowpoint.getOutgoingEdgeList()) {
				FlowPoint child = edge.getTarget();
				// for each outgoing edge, compute y (new domain)
				// then check if y is different from the old domain
				// if so, update domain and target to workset
				System.out.printf("(%s.java): %s => %s\n", Analyzer.class.getSimpleName(), flowpoint, child);
				D y = updateAnalysis((D) flowpoint.domain, child);
				if (y.compareTo((D) child.domain) != 0) {
					child.domain = y;
					workset.add(child);
				}
			}
		}

		// this is null because analysis is not reaching prog exit
		if (exitDomain != null) {
			analysis.onFinish((D) exitDomain);
		} else {
			System.err.println("Analysis Warning: Analysis did not reach ProgExit.");
		}
	}

	/**
	 * updateAnalysis is called each iteration of the fixed point algorithm. It
	 * represents a visit to a single flow point in the control flow graph.
	 * 
	 * @param inputDomain
	 *            The domain that should be modified.
	 * @param target
	 *            The target the flow point that will be visited.
	 * 
	 * @return the modified input domain. Depending on the implementation of
	 *         Analysis, a pointer to the same object may be returned; this is
	 *         what the framework does by default.
	 */
	private D updateAnalysis(D inputDomain, FlowPoint target) {
		D result = null;
		FlowPointContext fpctx = target.getContext();
		FlowPointContextType type = fpctx.getType();

		@SuppressWarnings("unchecked")
		D targetDomain = (D) target.domain;
		// merge previous flow point before visiting

		// why is this true sometimes?
		// a node should not loop back to itself
		if (inputDomain == targetDomain) {
			System.err.printf("(%s.java): input == target { %s }\n", this.getClass().getSimpleName(), target);
		}
		targetDomain.merge(inputDomain);

		// call this method before visiting the flow point
		analysis.onBefore(inputDomain, fpctx);

		// visit the node depending on its type
		switch (type) {
		case ProgEnter:
			result = analysis.enterProg(inputDomain, fpctx);
			break;
		case ProgExit:
			result = exitDomain = analysis.exitProg(inputDomain, fpctx);
			break;
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
		case IfStat:
			// TODO: implement exitIfStat
			result = analysis.enterIfStat(inputDomain, fpctx);
			break;
		case Assignment:
			result = analysis.enterAssignment(inputDomain, fpctx);
			break;
		case FlowPoint:
			if (fpctx.getText().equals("EXIT_WHILE")) {
				// TODO: make exitWhile enum
				result = analysis.exitWhileStatement(inputDomain, fpctx);
			}
			break;
		default:
			System.err.println(getClass().getSimpleName() + ".java: Not implemented: " + target);
			System.exit(1);
			break;
		}

		// call this method after visiting the flow point
		analysis.onAfter(inputDomain, fpctx);

		return result;
	}

}
