package edu.utsa.fileflow.analysis;

import edu.utsa.fileflow.cfg.FlowPointContext;

public class DummyAnalysis implements Analysis {

	@Override
	public AnalysisDomain enterProg(AnalysisDomain domain, FlowPointContext context) {
		System.out.println("enterProg");
		cast(domain).flag = 1;
		return domain;
	}

	@Override
	public AnalysisDomain exitProg(AnalysisDomain domain, FlowPointContext context) {
		System.out.println("exitProg");
		cast(domain).flag = 1;
		return domain;
	}

	@Override
	public AnalysisDomain touch(AnalysisDomain domain, FlowPointContext context) {
		System.out.println("touch");
		cast(domain).flag = 1;
		// FunctionCallContext ctx = (FunctionCallContext) context.getContext();
		// for (ExpressionContext expr : ctx.expression()) {
		// System.out.println("expr: " + expr.getText());
		// }
		return domain;
	}

	@Override
	public AnalysisDomain mkdir(AnalysisDomain domain, FlowPointContext context) {
		System.out.println("mkdir");
		cast(domain).flag = 1;
		return domain;
	}

	@Override
	public AnalysisDomain copy(AnalysisDomain domain, FlowPointContext context) {
		System.out.println("copy");
		cast(domain).flag = 1;
		return domain;
	}

	@Override
	public AnalysisDomain remove(AnalysisDomain domain, FlowPointContext context) {
		System.out.println("remove");
		cast(domain).flag = 1;
		return domain;
	}

	@Override
	public AnalysisDomain enterWhileStatement(AnalysisDomain domain, FlowPointContext context) {
		System.out.println("while");
		cast(domain).flag = 1;
		return domain;
	}

	private DummyAnalysisDomain cast(AnalysisDomain domain) {
		return (DummyAnalysisDomain) domain;
	}

}
