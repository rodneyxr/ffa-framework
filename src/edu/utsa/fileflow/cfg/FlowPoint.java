package edu.utsa.fileflow.cfg;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import edu.utsa.fileflow.analysis.AnalysisDomain;

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

	private static final AtomicInteger ID_GENERATOR = new AtomicInteger();

	private int id; // unique ID to be used when creating Graphviz DOT file
	private boolean printed = false; // used for graph traversal

	private FlowPointContext fpctx; // holds information from the parser

	private FlowPointEdgeList incoming; // a list of incoming edges
	private FlowPointEdgeList outgoing; // a list of outgoing edges

	// Analysis variables
	private AnalysisDomain<?> domain;

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
		id = ID_GENERATOR.getAndIncrement();
	}

	public AnalysisDomain<?> getDomain() {
		return domain;
	}

	public void setDomain(AnalysisDomain<?> domain) {
		this.domain = domain;
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

	/**
	 * Gets the unique ID of this flow point object.
	 * 
	 * @return The ID of this flow point.
	 */
	public int getID() {
		return id;
	}

	/**
	 * Get all flow points under this flow point. The list returned will also
	 * include itself.
	 * 
	 * @return A list of all flow points.
	 */
	public ArrayList<FlowPoint> getAllFlowPoints() {
		ArrayList<FlowPoint> children = new ArrayList<FlowPoint>();
		getAllFlowPointsImpl(children);
		resetPrint();
		return children;
	}

	/**
	 * Implementation of getAllFlowPoints(). This private method is used to hide
	 * the parameter that is used by the recursion.
	 * 
	 * @param children
	 *            The list that holds all flow points.
	 * @return The list that holds all flow points.
	 */
	private ArrayList<FlowPoint> getAllFlowPointsImpl(ArrayList<FlowPoint> children) {
		if (printed)
			return children;

		children.add(this);
		printed = true;
		for (FlowPointEdge edge : getOutgoingEdgeList()) {
			edge.getTarget().getAllFlowPointsImpl(children);
		}

		return children;
	}

	/**
	 * Prints a text representation of this flow point and its children
	 * recursively.
	 */
	public void print() {
		printImpl();
		resetPrint();
	}

	/*
	 * Implementation of print().
	 */
	private void printImpl() {
		if (printed)
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

	/*
	 * Sets the boolean printed to false for all flow points under this instance
	 * of flow point.
	 */
	private void resetPrint() {
		printed = false;
		for (FlowPointEdge edge : getOutgoingEdgeList()) {
			FlowPoint target = edge.getTarget();
			if (target.printed)
				target.resetPrint();
		}
	}

	@Override
	public String toString() {
		return fpctx.getText();
	}

}