package edu.utsa.fileflow.cfg;

/**
 * This class is the is a FlowPoint object that serves as the root of an entire
 * graph.
 * 
 * @author Rodney Rodriguez
 *
 */
public class ControlFlowGraph extends Scope {

	public ControlFlowGraph() {
		super(new FlowPointContext("START CFG"), new FlowPointContext("END CFG"));
	}

	public void print() {
		getEnter().print();
	}

}
