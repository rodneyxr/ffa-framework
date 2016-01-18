package edu.utsa.fileflow.cfg;

/**
 * This class is the is a FlowPoint object that serves as the root of an entire
 * graph.
 * 
 * @author Rodney Rodriguez
 *
 */
public class ControlFlowGraph extends FlowPoint {

	public ControlFlowGraph() {
		super(null);
	}

	@Override
	public void print() {
		for (FlowPointEdge edge : getOutgoingEdgeList()) {
			edge.getTarget().print(1);
		}
	}

//	@Override
//	protected void print(int level) {
//		System.out.printf("Level %d: %s\n", level, getContext().getText());
//	}

}
