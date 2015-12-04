package edu.utsa.fileflow.cfg;

/**
 * This class is the is a FlowPoint object that serves as the root of an entire
 * graph.
 * 
 * @author Rodney Rodriguez
 *
 */
public class ControlFlowGraph extends FlowPoint {
	
	private FlowPoint root;
	
	public ControlFlowGraph(FlowPoint root) {
		this.root = root;
	}
	
	public FlowPoint getRoot() {
		return root;
	}
	
}
