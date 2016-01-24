package edu.utsa.fileflow.cfg;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import edu.utsa.fileflow.antlr.FileFlowBaseListener;
import edu.utsa.fileflow.antlr.FileFlowParser.AssignmentContext;
import edu.utsa.fileflow.antlr.FileFlowParser.FunctionCallContext;
import edu.utsa.fileflow.antlr.FileFlowParser.IfStatContext;
import edu.utsa.fileflow.antlr.FileFlowParser.ProgContext;

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

	public ControlFlowGraph cfg;
	public ScopeStack scopes;
	public FlowPoint last;

	public FileFlowListenerImpl() {
		scopes = new ScopeStack();
		cfg = new ControlFlowGraph();
		last = cfg.getEnter();
		scopes.push(cfg);
	}

	// TODO: handle enter statement
	// TODO: handle push and pop

	@Override
	public void enterProg(ProgContext ctx) {
		// create new scope to push onto the stack
		Scope newScope = new Scope(new FlowPointContext("Enter Prog"), new FlowPointContext("Exit Prog"));
		scopes.push(newScope);

		// point last to the start of the scope
		last.addFlowPoint(newScope.getEnter());

		// update the last flow point
		last = newScope.getEnter();
	}

	@Override
	public void exitProg(ProgContext ctx) {
		// last -> exit
		last.addFlowPoint(scopes.peek().getExit());

		// last = exit
		last = scopes.peek().getExit();

		// pop scope stack
		scopes.pop();

		// again to finish the CFG
		last = last.addFlowPoint(scopes.pop().getExit());
	}

	@Override
	public void enterIfStat(IfStatContext ctx) {
		Scope newScope = new Scope(new FlowPointContext(ctx.condition(), FlowPointContextType.IfStat),
				new FlowPointContext("Exit IfStat"));
		scopes.push(newScope);

		last = last.addFlowPoint(newScope.getEnter());
	}

	@Override
	public void exitIfStat(IfStatContext ctx) {
		// if the ifStat is false we skip this scope
		scopes.peek().getEnter().addFlowPoint(scopes.peek().getExit());
		last = last.addFlowPoint(scopes.pop().getExit());
	}

	@Override
	public void enterAssignment(AssignmentContext ctx) {
		setLast(ctx, FlowPointContextType.Assignment);
	}

	@Override
	public void enterFunctionCall(FunctionCallContext ctx) {
		setLast(ctx, FlowPointContextType.FunctionCall);
	}

	private void setLast(ParserRuleContext ctx, FlowPointContextType type) {
		FlowPoint flowpoint = new FlowPoint(new FlowPointContext(ctx, type));
		last.addFlowPoint(flowpoint);
		last = flowpoint;
	}

}