package edu.utsa.fileflow.cfg;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import edu.utsa.fileflow.antlr.FileFlowBaseListener;
import edu.utsa.fileflow.antlr.FileFlowParser.AssignmentContext;
import edu.utsa.fileflow.antlr.FileFlowParser.FunctionCallContext;

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
	public FlowPoint next;

	public FileFlowListenerImpl() {
		next = cfg = new ControlFlowGraph();
	}
	
	@Override
	public void enterAssignment(AssignmentContext ctx) {
		next(ctx, FlowPointContextType.Assignment);
	}

	@Override
	public void enterFunctionCall(FunctionCallContext ctx) {
		next(ctx, FlowPointContextType.FunctionCall);
	}
	
	private void next(ParserRuleContext ctx, FlowPointContextType type) {
		FlowPoint flowpoint = new FlowPoint(new FlowPointContext(ctx, type));
		next.addFlowPoint(flowpoint);
		next = flowpoint;
	}

}
