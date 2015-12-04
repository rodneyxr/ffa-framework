package edu.utsa.fileflow.compiler;

import java.util.Vector;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import edu.utsa.fileflow.antlr.FileFlowBaseListener;
import edu.utsa.fileflow.antlr.FileFlowParser.AssignmentContext;
import edu.utsa.fileflow.antlr.FileFlowParser.ElseIfStatContext;
import edu.utsa.fileflow.antlr.FileFlowParser.ElseStatContext;
import edu.utsa.fileflow.antlr.FileFlowParser.FunctionCallContext;
import edu.utsa.fileflow.antlr.FileFlowParser.IfStatContext;
import edu.utsa.fileflow.antlr.FileFlowParser.ProgContext;
import edu.utsa.fileflow.cfg.FlowPoint;

/**
 * This class will be responsible for handling entry and exit points when
 * traversing through ANTLR's parse tree. Methods within
 * {@link FileFlowBaseListener} can be overridden to be used as hooks for when
 * the {@link ParseTreeWalker} visits or leaves a node.
 * 
 * @author Rodney Rodriguez
 *
 */
public class FileFlowListenerImpl extends FileFlowBaseListener {

	private Vector<FlowPoint> flowPoints;
	private FlowPoint flowpoint;
	private boolean consecutive;

	public FileFlowListenerImpl() {
		flowPoints = new Vector<FlowPoint>();
		flowpoint = new FlowPoint();
		consecutive = true;
	}

	public Vector<FlowPoint> getFlowPoints() {
		return flowPoints;
	}

	@Override
	public void enterAssignment(AssignmentContext ctx) {
		printctx("Enter assignment:", ctx);
		flowpoint.add(ctx);
	}

	@Override
	public void enterFunctionCall(FunctionCallContext ctx) {
		printctx("Enter functionCall: ", ctx);
		flowpoint.add(ctx);
	}

	// @Override
	// public void enterIfStatement(IfStatementContext ctx) {
	// consecutive(ctx);
	// for (ParseTree t : ctx.children) {
	// System.out.println(t.getText());
	// }
	// printctx("Enter IfStatement:", ctx);
	// // System.out.println("Parent: " + ctx.parent.getText());
	// }

	@Override
	public void enterIfStat(IfStatContext ctx) {
		consecutive(ctx);
		printctx("Enter IfStat: ", ctx);
	}

	@Override
	public void enterElseIfStat(ElseIfStatContext ctx) {
		consecutive(ctx);
		printctx("Enter ElseIfStat: ", ctx);
	}

	@Override
	public void enterElseStat(ElseStatContext ctx) {
		consecutive(ctx);
		printctx("Enter ElseStat: ", ctx);
	}

	@Override
	public void exitProg(ProgContext ctx) {
		consecutive(ctx);
	}

	private void printctx(String msg, ParserRuleContext ctx) {
		System.out.print(msg);
		System.out.println(ctx.getText());
	}

	private void consecutive(ParserRuleContext ctx) {
		consecutive = false;
		flowPoints.add(flowpoint);
		flowpoint = new FlowPoint();
		System.out.println("Last: " + flowPoints.lastElement().flowpoints.lastElement().getText());
	}

}
