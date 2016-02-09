package edu.utsa.fileflow.cfg;

import java.util.ArrayList;
import java.util.Stack;

import org.antlr.v4.runtime.tree.ParseTreeWalker;

import edu.utsa.fileflow.antlr.FileFlowBaseListener;
import edu.utsa.fileflow.antlr.FileFlowParser.AssignmentContext;
import edu.utsa.fileflow.antlr.FileFlowParser.ElseIfStatContext;
import edu.utsa.fileflow.antlr.FileFlowParser.ElseStatContext;
import edu.utsa.fileflow.antlr.FileFlowParser.FunctionCallContext;
import edu.utsa.fileflow.antlr.FileFlowParser.IfStatContext;
import edu.utsa.fileflow.antlr.FileFlowParser.IfStatementContext;
import edu.utsa.fileflow.antlr.FileFlowParser.ProgContext;
import edu.utsa.fileflow.antlr.FileFlowParser.WhileStatementContext;
import edu.utsa.fileflow.utilities.Delegate;

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

	// the control flow graph
	public FlowPoint cfg;

	private FlowPoint last; // the last flow point visited
	private Stack<SwitchBlock> currentSwitchBlocks = new Stack<SwitchBlock>();
	private Stack<SwitchBlock> oldSwitchBlocks = new Stack<SwitchBlock>();
	private Delegate nextDelegate = new Delegate();
	private Delegate exitDelegate = new Delegate();

	public FileFlowListenerImpl() {
	}

	@Override
	public void enterProg(ProgContext ctx) {
		cfg = new FlowPoint(new FlowPointContext("PROG_ENTER", FlowPointContextType.ProgEnter));
		updateLast(cfg);
	}

	@Override
	public void exitProg(ProgContext ctx) {
		updateLast(new FlowPoint(new FlowPointContext("PROG_EXIT", FlowPointContextType.ProgExit)));
	}

	/**
	 * Start IfStatement
	 */
	@Override
	public void enterIfStatement(IfStatementContext ctx) {
		// create and push a new SwitchBlock onto the stack
		currentSwitchBlocks.add(new SwitchBlock());
	}

	@Override
	public void exitIfStatement(IfStatementContext ctx) {
		exitSwitchBlock(FlowPointContextType.IfStatement);
	}

	/**
	 * Start ifStat
	 */
	@Override
	public void enterIfStat(IfStatContext ctx) {
		FlowPoint ifStat = new FlowPoint(new FlowPointContext(ctx.condition(), FlowPointContextType.IfStat));
		updateLast(ifStat);
		currentSwitchBlocks.peek().setLastCondition(ifStat);
	}

	@Override
	public void exitIfStat(IfStatContext ctx) {
		SwitchBlock lastSwitchBlock = currentSwitchBlocks.peek();

		// queue last to point to exit if
		lastSwitchBlock.addBreak(last);

		// last condition -> next
		nextDelegate.add(() -> {
			FlowPoint ifCond = lastSwitchBlock.getLastCondition();
			ifCond.addFlowPoint(last);
		});
	}

	/**
	 * Start elseIfStat
	 */
	@Override
	public void enterElseIfStat(ElseIfStatContext ctx) {
		FlowPoint elseIfStat = new FlowPoint(new FlowPointContext(ctx.condition(), FlowPointContextType.ElseIfStat));
		updateLast(elseIfStat);

		// this needs to be done after updating last
		currentSwitchBlocks.peek().setLastCondition(elseIfStat);
	}

	@Override
	public void exitElseIfStat(ElseIfStatContext ctx) {
		SwitchBlock lastSwitchBlock = currentSwitchBlocks.peek();

		// queue last to point to exit if
		lastSwitchBlock.addBreak(last);

		// last condition -> next
		nextDelegate.add(() -> {
			FlowPoint elseIfCond = lastSwitchBlock.getLastCondition();
			elseIfCond.addFlowPoint(last);
		});
	}

	/**
	 * Start elseStat
	 */
	@Override
	public void enterElseStat(ElseStatContext ctx) {
		FlowPoint elseStat = new FlowPoint(new FlowPointContext("else", FlowPointContextType.ElseStat));
		updateLast(elseStat);

		// this needs to be done after updating last
		currentSwitchBlocks.peek().setLastCondition(elseStat);
	}

	@Override
	public void exitElseStat(ElseStatContext ctx) {
		SwitchBlock lastSwitchBlock = currentSwitchBlocks.peek();

		// queue last to point to exit if
		lastSwitchBlock.addBreak(last);

		// last condition -> next
		nextDelegate.add(() -> {
			FlowPoint elseCond = lastSwitchBlock.getLastCondition();
			elseCond.addFlowPoint(last);
		});
	}

	/**
	 * Start while statements
	 */
	@Override
	public void enterWhileStatement(WhileStatementContext ctx) {
		currentSwitchBlocks.add(new SwitchBlock());
		FlowPoint whileStat = new FlowPoint(new FlowPointContext(ctx.condition(), FlowPointContextType.WhileStatement));
		updateLast(whileStat);
		currentSwitchBlocks.peek().setLastCondition(whileStat);
	}

	@Override
	public void exitWhileStatement(WhileStatementContext ctx) {
		exitSwitchBlock(FlowPointContextType.WhileStatement);
	}

	/**
	 * Start non-conditional statements
	 */
	@Override
	public void enterFunctionCall(FunctionCallContext ctx) {
		updateLast(new FlowPoint(new FlowPointContext(ctx, FlowPointContextType.FunctionCall)));
	}

	@Override
	public void enterAssignment(AssignmentContext ctx) {
		updateLast(new FlowPoint(new FlowPointContext(ctx, FlowPointContextType.Assignment)));
	}

	/**
	 * Makes the necessary modifications to make the correct flow point links
	 * after exiting a conditional scope.
	 * 
	 * @param type
	 *            The context type of the switch block.
	 */
	private void exitSwitchBlock(FlowPointContextType type) {
		// Save lastSwitchBlock in case next statement causes another
		// SwitchBlock to be created. If another SwitchBlock is pushed to the
		// stack then the delegate would be referencing the new SwitchBlock
		// rather than the one current within this scope.
		oldSwitchBlocks.push(currentSwitchBlocks.pop());
		nextDelegate.clear();

		// point all break points to switch block end
		// this code will be called later
		exitDelegate.add(() -> {
			SwitchBlock oldBlock = oldSwitchBlocks.pop();

			ArrayList<FlowPoint> breaks = oldBlock.getBreaks();
			if (breaks.size() > 0) {
				// remove the last break flow point because it will already be
				// pointing to the next flow point
				breaks.remove(breaks.size() - 1);
				// last condition in if block will always be a break point
				// unless it is an elseStat
				if (oldBlock.getLastCondition().getContext().getType() != FlowPointContextType.ElseStat)
					oldBlock.addBreak(oldBlock.getLastCondition());

				for (FlowPoint brk : breaks) {
					brk.addFlowPoint(last);
				}
			}
		});

		if (type == FlowPointContextType.WhileStatement) {
			// add exit while node
			FlowPoint whileStart = oldSwitchBlocks.peek().getLastCondition();
			FlowPoint whileEnd = new FlowPoint(new FlowPointContext("EXIT_WHILE"));
			// link exit while node back to loop start
			// these two lines will treat the condition of the while loop as the
			// exit point
			updateLast(whileEnd);
			updateLast(whileStart);
		}
	}

	/**
	 * Updates class global last variable. If the last was of type If, Else-if
	 * or Else, then it will not point to the new last. This is because IfType
	 * flow points have their own way of connecting flow points.
	 * 
	 * @param newLast
	 *            The last flow point created.
	 */
	private void updateLast(FlowPoint newLast) {
		if (last == null) {
			last = newLast;
			return;
		}

		switch (newLast.getContext().getType()) {
		case ElseIfStat:
		case ElseStat:
			last = newLast;
			break;
		default:
			last = last.addFlowPoint(newLast);
		}

		checkDelegate();
	}

	private void checkDelegate() {
		nextDelegate.run();
		nextDelegate.clear();
		exitDelegate.run();
		exitDelegate.clear();
	}

}
