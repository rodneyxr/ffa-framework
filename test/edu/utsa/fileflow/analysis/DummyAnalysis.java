package edu.utsa.fileflow.analysis;

import edu.utsa.fileflow.cfg.FlowPointContext;

public class DummyAnalysis implements Analysis<DummyAnalysisDomain> {

	@Override
	public DummyAnalysisDomain onBegin(DummyAnalysisDomain domain) {
		return domain;
	}

	@Override
	public DummyAnalysisDomain onFinish(DummyAnalysisDomain domain) {
		return domain;
	}

	@Override
	public DummyAnalysisDomain onBefore(DummyAnalysisDomain domain, FlowPointContext context) {
		return domain;
	}

	@Override
	public DummyAnalysisDomain onAfter(DummyAnalysisDomain domain, FlowPointContext context) {
		return domain;
	}

	@Override
	public DummyAnalysisDomain enterProg(DummyAnalysisDomain domain, FlowPointContext context) {
		System.out.println("enterProg");
		domain.flag = 1;
		return domain;
	}

	@Override
	public DummyAnalysisDomain exitProg(DummyAnalysisDomain domain, FlowPointContext context) {
		System.out.println("exitProg");
		domain.flag = 1;
		return domain;
	}

	@Override
	public DummyAnalysisDomain touch(DummyAnalysisDomain domain, FlowPointContext context) {
		System.out.println("touch");
		domain.flag = 1;
		// FunctionCallContext ctx = (FunctionCallContext) context.getContext();
		// for (ExpressionContext expr : ctx.expression()) {
		// System.out.println("expr: " + expr.getText());
		// }
		return domain;
	}

	@Override
	public DummyAnalysisDomain mkdir(DummyAnalysisDomain domain, FlowPointContext context) {
		System.out.println("mkdir");
		domain.flag = 1;
		return domain;
	}

	@Override
	public DummyAnalysisDomain copy(DummyAnalysisDomain domain, FlowPointContext context) {
		System.out.println("copy");
		domain.flag = 1;
		return domain;
	}

	@Override
	public DummyAnalysisDomain remove(DummyAnalysisDomain domain, FlowPointContext context) {
		System.out.println("remove");
		domain.flag = 1;
		return domain;
	}

	@Override
	public DummyAnalysisDomain enterAssignment(DummyAnalysisDomain domain, FlowPointContext context) {
		System.out.println("enter assignment");
		domain.flag = 1;
		return domain;
	}
	
	@Override
	public DummyAnalysisDomain enterIfStat(DummyAnalysisDomain domain, FlowPointContext context) {
		System.out.println("enter IfStat");
		domain.flag = 1;
		return domain;
	}

	@Override
	public DummyAnalysisDomain exitIfStat(DummyAnalysisDomain domain, FlowPointContext context) {
		System.out.println("exit IfStat");
		domain.flag = 1;
		return domain;
	}

	@Override
	public DummyAnalysisDomain enterWhileStatement(DummyAnalysisDomain domain, FlowPointContext context) {
		System.out.println("while");
		domain.flag = 1;
		return domain;
	}

	@Override
	public DummyAnalysisDomain exitWhileStatement(DummyAnalysisDomain domain, FlowPointContext context) {
		System.out.println("exit while");
		domain.flag = 1;
		return domain;
	}

}
