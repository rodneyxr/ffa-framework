package edu.utsa.fileflow.analysis;

import edu.utsa.fileflow.cfg.FlowPoint;

public abstract class AnalysisDomain<T extends AnalysisDomain<T>> implements Comparable<T> {

	/* The last FlowPoint that modified this domain */
	protected FlowPoint lastFlowPoint;

	/**
	 * @return the last FlowPoint that modified this domain.
	 */
	public FlowPoint getLastFlowPoint() {
		return lastFlowPoint;
	}

	public abstract T top();

	public abstract T bottom();

	public abstract T merge(T domain);

	public abstract int compareTo(T o);

	public abstract T clone();

}
