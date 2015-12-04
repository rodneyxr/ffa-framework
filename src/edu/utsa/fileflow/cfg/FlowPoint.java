package edu.utsa.fileflow.cfg;

import java.util.Vector;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * 
 * A FlowPoint represents a node in the {@link ControlFlowGraph}. It can have any
 * amount of entry points and zero, one or two exit points.
 * 
 * @author Rodney Rodriguez
 *
 */
public class FlowPoint {

	public Vector<ParserRuleContext> flowpoints;

	public FlowPoint() {
		flowpoints = new Vector<ParserRuleContext>();
	}

	public FlowPoint(Vector<ParserRuleContext> block) {
		flowpoints = block;
	}

	public void add(ParserRuleContext ctx) {
		flowpoints.add(ctx);
	}

	public void printBlock() {
		System.out.println("\nBlock:");
		for (ParserRuleContext ctx : flowpoints) {
			System.out.println(ctx.getText());
		}
		System.out.println("===============================\n");
	}

}
