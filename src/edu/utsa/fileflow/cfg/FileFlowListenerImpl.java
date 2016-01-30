package edu.utsa.fileflow.cfg;

import java.util.ArrayList;
import java.util.Stack;

import org.antlr.v4.runtime.tree.ParseTreeWalker;

import edu.utsa.fileflow.antlr.FileFlowBaseListener;
import edu.utsa.fileflow.antlr.FileFlowParser.AssignmentContext;
import edu.utsa.fileflow.antlr.FileFlowParser.ElseIfStatContext;
import edu.utsa.fileflow.antlr.FileFlowParser.FunctionCallContext;
import edu.utsa.fileflow.antlr.FileFlowParser.IfStatContext;
import edu.utsa.fileflow.antlr.FileFlowParser.IfStatementContext;
import edu.utsa.fileflow.antlr.FileFlowParser.ProgContext;
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

	// the last flow point visited
	private FlowPoint last;
	private Stack<SwitchBlock> currentSwitchBlocks = new Stack<SwitchBlock>();
	private Stack<SwitchBlock> oldSwitchBlocks = new Stack<SwitchBlock>();
	private Delegate delegate = new Delegate();

	public FileFlowListenerImpl() {
	}

	@Override
	public void enterProg(ProgContext ctx) {
		cfg = new FlowPoint("CFG_ENTER");
		updateLast(cfg);
	}

	@Override
	public void exitProg(ProgContext ctx) {
		updateLast(new FlowPoint("CFG_EXIT"));
	}

	@Override
	public void enterIfStatement(IfStatementContext ctx) {
		// create and push a new SwitchBlock onto the stack
		currentSwitchBlocks.add(new SwitchBlock());
	}

	@Override
	public void exitIfStatement(IfStatementContext ctx) {
		// Save lastSwitchBlock in case next statement causes another
		// SwitchBlock to be created. If another SwitchBlock is pushed to the
		// stack then the delegate would be referencing the new SwitchBlock
		// rather than the one current within this scope.
		oldSwitchBlocks.push(currentSwitchBlocks.pop());

		// point all break points to switch block end
		delegate.add(() -> {
			ArrayList<FlowPoint> breaks = oldSwitchBlocks.pop().getBreaks();
			if (breaks.size() > 0) {
				// remove the last break flow point because it will already be
				// pointing to the next flow point
				breaks.remove(breaks.size() - 1);

				for (FlowPoint brk : breaks) {
					brk.addFlowPoint(last);
				}
			}
		});
	}

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
		delegate.add(() -> {
			FlowPoint ifCond = lastSwitchBlock.getLastCondition();
			ifCond.addFlowPoint(last);
		});
	}

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
		delegate.add(() -> {
			FlowPoint elseIfCond = lastSwitchBlock.getLastCondition();
			elseIfCond.addFlowPoint(last);
		});
	}

	@Override
	public void enterFunctionCall(FunctionCallContext ctx) {
		updateLast(new FlowPoint(new FlowPointContext(ctx, FlowPointContextType.FunctionCall)));
	}

	@Override
	public void enterAssignment(AssignmentContext ctx) {
		updateLast(new FlowPoint(new FlowPointContext(ctx, FlowPointContextType.Assignment)));
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

		// switch (last.getContext().getType()) {
		// case IfStat:
		// case ElseIfStat:
		// case ElseStatement:
		// last = newLast;
		// break;
		// default:
		// last = last.addFlowPoint(newLast);
		// }

//		FlowPointContextType newLastType = newLast.getContext().getType();
		switch (newLast.getContext().getType()) {
		case ElseIfStat:
			last = newLast;
			break;
		default:
			last = last.addFlowPoint(newLast);
		}
		// if (newLastType != FlowPointContextType.ElseIfStat) {
		// last = last.addFlowPoint(newLast);
		// } else {
		// last = newLast;
		// }

		checkDelegate();
	}

	private void checkDelegate() {
		delegate.run();
		delegate.clear();
	}

}
