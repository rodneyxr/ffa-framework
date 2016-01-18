package edu.utsa.fileflow.cfg;

import org.antlr.v4.runtime.ParserRuleContext;

public class FlowPointContext {

	private ParserRuleContext ctx;
	private FlowPointContextType type;

	public FlowPointContext(ParserRuleContext ctx) {
		this(ctx, FlowPointContextType.FlowPoint);
	}

	public FlowPointContext(ParserRuleContext ctx, FlowPointContextType type) {
		this.ctx = ctx;
		this.type = type;
	}

	public ParserRuleContext getContext() {
		return ctx;
	}

	public FlowPointContextType getType() {
		return type;
	}
	
	public String getText() {
		return ctx.getText();
	}

}
