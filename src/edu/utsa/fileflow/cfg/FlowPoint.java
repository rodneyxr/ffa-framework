package edu.utsa.fileflow.cfg;

import org.antlr.v4.runtime.ParserRuleContext;

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

	private FlowPointContext ctx; // holds information from the parser

	private FlowPointEdgeList incoming; // a list of incoming edges
	private FlowPointEdgeList outgoing; // a list of outgoing edges

	public FlowPoint(FlowPointContext ctx) {
		this.ctx = ctx;
		incoming = new FlowPointEdgeList();
		outgoing = new FlowPointEdgeList();
	}

	/**
	 * Gets the context of this flow point.
	 * 
	 * @return The context of this flow point.
	 */
	public FlowPointContext getContext() {
		return ctx;
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

	public void addFlowPoint(FlowPoint flowpoint) {
		FlowPointEdge edge = new FlowPointEdge(this, flowpoint);
		outgoing.add(edge);
		flowpoint.incoming.add(edge);
	}

	public void print() {
		print(0);
	}

	@Override
	public String toString() {
		return ctx.getText();
	}

	protected void print(int level) {
		System.out.printf("Level %d: %s\n", level, this);
		for (FlowPointEdge edge : getOutgoingEdgeList()) {
			edge.getTarget().print(level + 1);
		}
	}

}
