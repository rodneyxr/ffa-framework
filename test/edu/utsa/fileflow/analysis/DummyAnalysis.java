package edu.utsa.fileflow.analysis;

import edu.utsa.fileflow.cfg.FlowPointContext;

public class DummyAnalysis implements Analysis<DummyAnalysisDomain> {

	@Override
	public AnalysisDomain onBefore(DummyAnalysisDomain domain, FlowPointContext context) {
		return domain;
	}

	@Override
	public AnalysisDomain onAfter(DummyAnalysisDomain domain, FlowPointContext context) {
		return domain;
	}

	@Override
	public AnalysisDomain enterProg(DummyAnalysisDomain domain, FlowPointContext context) {
		System.out.println("enterProg");
		domain.flag = 1;
		return domain;
	}

	@Override
	public AnalysisDomain exitProg(DummyAnalysisDomain domain, FlowPointContext context) {
		System.out.println("exitProg");
		domain.flag = 1;
		return domain;
	}

	@Override
	public AnalysisDomain touch(DummyAnalysisDomain domain, FlowPointContext context) {
		System.out.println("touch");
		domain.flag = 1;
		// FunctionCallContext ctx = (FunctionCallContext) context.getContext();
		// for (ExpressionContext expr : ctx.expression()) {
		// System.out.println("expr: " + expr.getText());
		// }
		return domain;
	}

	@Override
	public AnalysisDomain mkdir(DummyAnalysisDomain domain, FlowPointContext context) {
		System.out.println("mkdir");
		domain.flag = 1;
		return domain;
	}

	@Override
	public AnalysisDomain copy(DummyAnalysisDomain domain, FlowPointContext context) {
		System.out.println("copy");
		domain.flag = 1;
		return domain;
	}

	@Override
	public AnalysisDomain remove(DummyAnalysisDomain domain, FlowPointContext context) {
		System.out.println("remove");
		domain.flag = 1;
		return domain;
	}

	@Override
	public AnalysisDomain enterWhileStatement(DummyAnalysisDomain domain, FlowPointContext context) {
		System.out.println("while");
		domain.flag = 1;
		return domain;
	}

	@Override
	public AnalysisDomain exitWhileStatement(DummyAnalysisDomain domain, FlowPointContext context) {
		System.out.println("exit while");
		domain.flag = 1;
		return domain;
	}

}
