package edu.utsa.fileflow.cfg;

import java.util.ArrayList;

public class SwitchBlock {

	// list of flow points that should exit the switch block
	private ArrayList<FlowPoint> breakPoints = new ArrayList<FlowPoint>();

	// the last condition flow point visited
	private FlowPoint lastCondition;

	public SwitchBlock() {
	}

	public void addBreak(FlowPoint breakNode) {
		breakPoints.add(breakNode);
	}

	public ArrayList<FlowPoint> getBreaks() {
		return breakPoints;
	}

	public FlowPoint getLastCondition() {
		return lastCondition;
	}

	public void setLastCondition(FlowPoint lastCondition) {
		this.lastCondition = lastCondition;
	}

}
