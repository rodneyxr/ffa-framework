package edu.utsa.fileflow.cfg;

import java.util.ArrayList;

public class SwitchBlock {

	// list of flow points that should exit the switch block
	private ArrayList<FlowPoint> breakPoints = new ArrayList<FlowPoint>();
	FlowPoint last;

	// the last condition flow point visited
	private FlowPoint lastCondition;

	public SwitchBlock() {
	}

	/**
	 * Adds a flow point that represents a statement that could break out of the
	 * conditional block.
	 * 
	 * @param breakNode
	 *            The flow point that could break the block.
	 */
	public void addBreak(FlowPoint breakNode) {
		breakPoints.add(breakNode);
	}

	/**
	 * Gets all the break points in the block.
	 * 
	 * @return A list of break points in the block.
	 */
	public ArrayList<FlowPoint> getBreaks() {
		return breakPoints;
	}

	/**
	 * Gets the last conditional statement in the block.
	 * 
	 * @return The last conditional statement in the block.
	 */
	public FlowPoint getLastCondition() {
		return lastCondition;
	}

	/**
	 * Sets the last conditional statement in the block.
	 * 
	 * @param lastCondition
	 *            The last conditional statement in the block.
	 */
	public void setLastCondition(FlowPoint lastCondition) {
		this.lastCondition = lastCondition;
	}

}
