package edu.utsa.fileflow.cfg;

/**
 * This class represents an edge in a {@link ControlFlowGraph}. This edge
 * should serve as a directed edge to connect two {@link FlowPoint} objects.
 * 
 * @author Rodney Rodriguez
 *
 */
public class FlowPointEdge {

	private FlowPoint source; // the source node
	private FlowPoint target; // the target node

	/**
	 * Creates an edge, settings the source and target nodes.
	 * 
	 * @param source
	 *            The source node.
	 * @param target
	 *            The target node.
	 */
	public FlowPointEdge(FlowPoint source, FlowPoint target) {
		this.source = source;
		this.target = target;
	}

	/**
	 * Gets the source node.
	 * 
	 * @return The source node.
	 */
	public FlowPoint getSource() {
		return source;
	}

	/**
	 * Sets the source node.
	 * 
	 * @param source
	 *            The source node.
	 */
	public void setSource(FlowPoint source) {
		this.source = source;
	}

	/**
	 * Gets the target node.
	 * 
	 * @return The target node.
	 */
	public FlowPoint getTarget() {
		return target;
	}

	/**
	 * Sets the target node.
	 * 
	 * @param target
	 *            The target node.
	 */
	public void setTarget(FlowPoint target) {
		this.target = target;
	}

}
