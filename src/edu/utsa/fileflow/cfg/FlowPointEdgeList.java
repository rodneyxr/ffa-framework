package edu.utsa.fileflow.cfg;

import java.util.ArrayList;

public class FlowPointEdgeList extends ArrayList<FlowPointEdge> {
	private static final long serialVersionUID = 1L;

	public FlowPointEdgeList() {
		super();
	}

	/**
	 * Returns the edge for the given index.
	 * 
	 * @param index
	 *            The index of the requested edge.
	 * @return The edge at the given index.
	 */
	public FlowPointEdge getEdge(int index) {
		return get(index);
	}
}
