package edu.utsa.fileflow.cfg;

/**
 * 
 * A FlowPoint represents a node in the {@link ControlFlowGraph}. It can have
 * any amount of entry points and should have zero, one or two exit points since
 * there is no switch statements in the FileFlow grammar.
 * 
 * @author Rodney Rodriguez
 *
 */
public class FlowPoint {

	private FlowPointContext fpctx; // holds information from the parser

	private FlowPointEdgeList incoming; // a list of incoming edges
	private FlowPointEdgeList outgoing; // a list of outgoing edges

	public FlowPoint(String text) {
		this(new FlowPointContext(text));
	}

	public FlowPoint(FlowPointContext fpctx) {
		if (fpctx == null) {
			this.fpctx = new FlowPointContext("Flow Point");
		} else {
			this.fpctx = fpctx;
		}
		incoming = new FlowPointEdgeList();
		outgoing = new FlowPointEdgeList();
	}

	/**
	 * Gets the context of this flow point.
	 * 
	 * @return The context of this flow point.
	 */
	public FlowPointContext getContext() {
		return fpctx;
	}

	/**
	 * Gets the list of incoming edges.
	 * 
	 * @return The list of this node's incoming edges.
	 */
	public FlowPointEdgeList getIncomingEdgeList() {
		return incoming;
	}

	/**
	 * Gets the list of outgoing edges.
	 * 
	 * @return The list of this node's outgoing edges.
	 */
	public FlowPointEdgeList getOutgoingEdgeList() {
		return outgoing;
	}

	public FlowPoint addFlowPoint(FlowPoint flowpoint) {
		FlowPointEdge edge = new FlowPointEdge(this, flowpoint);
		outgoing.add(edge);
		flowpoint.incoming.add(edge);
		return flowpoint;
	}

	boolean printed = false;

	public void print() {
		printImpl();
		resetPrint();
	}

	private void printImpl() {
		if (printed == true)
			return;

		// print parent followed by all its children flow points
		System.out.printf("%s => { ", this);
		for (FlowPointEdge edge : getOutgoingEdgeList()) {
			System.out.printf("%s, ", edge.getTarget());
		}
		System.out.println("}");
		printed = true;

		// recursive print for children
		for (FlowPointEdge edge : getOutgoingEdgeList()) {
			edge.getTarget().printImpl();
		}
	}

	private void resetPrint() {
		printed = false;
		for (FlowPointEdge edge : getOutgoingEdgeList()) {
			/**
			 * FIXME: Technically this is correct because it prevents stack
			 * overflows when traversing through a graph with a loop. This is
			 * disabled right now because I know if there is a stack overflow
			 * that there is something wrong with my CFG because there should be
			 * no loops at the moment.
			 */
			// if (printed == false) continue;
			edge.getTarget().resetPrint();
		}
	}

	@Override
	public String toString() {
		return fpctx.getText();
	}

}
