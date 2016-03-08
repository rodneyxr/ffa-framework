package edu.utsa.fileflow;

import edu.utsa.fileflow.analysis.Analysis;
import edu.utsa.fileflow.analysis.AnalysisDomain;
import edu.utsa.fileflow.antlr.FileFlowParser.ExpressionContext;
import edu.utsa.fileflow.antlr.FileFlowParser.FunctionCallContext;
import edu.utsa.fileflow.cfg.FlowPointContext;

public class TestAnalysisImpl implements Analysis {

	@Override
	public AnalysisDomain<?> enterProg(AnalysisDomain<?> domain, FlowPointContext context) {
		System.out.println("enterProg");
		return domain;
	}

	@Override
	public AnalysisDomain<?> touch(AnalysisDomain<?> domain, FlowPointContext context) {
		System.out.println("touch");
		FunctionCallContext ctx = (FunctionCallContext) context.getContext();
		for (ExpressionContext expr : ctx.expression()) {
			System.out.println(expr.getText());
		}
		return domain;
	}

	@Override
	public AnalysisDomain<?> mkdir(AnalysisDomain<?> domain, FlowPointContext context) {
		System.out.println("mkdir");
		return domain;
	}

	@Override
	public AnalysisDomain<?> copy(AnalysisDomain<?> domain, FlowPointContext context) {
		System.out.println("copy");
		return domain;
	}

	@Override
	public AnalysisDomain<?> remove(AnalysisDomain<?> domain, FlowPointContext context) {
		System.out.println("remove");
		return domain;
	}

}
