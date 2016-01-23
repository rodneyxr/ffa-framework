package edu.utsa.fileflow.cfg;

import org.antlr.v4.runtime.ParserRuleContext;

public class FlowPointContext {

	private ParserRuleContext ctx;
	private FlowPointContextType type;
	private String text;

	public FlowPointContext(String text) {
		this.ctx = null;
		this.text = text;
		this.type = FlowPointContextType.FlowPoint;
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
		if (type == FlowPointContextType.FlowPoint)
			return text;
		return ctx.getText();
	}

}
