package edu.utsa.fileflow.analysis;

import edu.utsa.fileflow.cfg.FlowPoint;

public abstract class AnalysisDomain<T extends AnalysisDomain<T>> implements Comparable<T> {

    /* The first FlowPoint of this domain */
    protected FlowPoint firstFlowPoint;

    /* The last FlowPoint that modified this domain */
    protected FlowPoint lastFlowPoint;

    // TODO: Add documentation
    public FlowPoint getFirstFlowPoint() {
        return firstFlowPoint;
    }

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
