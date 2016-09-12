package edu.utsa.fileflow.analysis;

import edu.utsa.fileflow.cfg.FlowPointContext;

public interface Analysis<T extends AnalysisDomain<T>> {

	T onBegin(T domain);

	T onFinish(T domain);

	T onBefore(T domain, FlowPointContext context);

	T onAfter(T domain, FlowPointContext context);

	T touch(T domain, FlowPointContext context);

	T mkdir(T domain, FlowPointContext context);

	T copy(T domain, FlowPointContext context);

	T remove(T domain, FlowPointContext context);

	T enterProg(T domain, FlowPointContext context);

	T exitProg(T domain, FlowPointContext context);

	//
	// void enterBlock(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void exitBlock(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void enterStatement(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void exitStatement(AnalysisDomain<T> domain, AnalysisContext context);
	//
	T enterAssignment(T domain, FlowPointContext context);

	// AnalysisDomain exitAssignment(T domain, FlowPointContext context);
	//
	// void enterFunctionCall(AnalysisDomain<T> domain, AnalysisContext
	// context);
	//
	// void exitFunctionCall(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void enterExpression(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void exitExpression(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void enterIfStatement(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void exitIfStatement(AnalysisDomain<T> domain, AnalysisContext context);
	//
	T enterIfStat(T domain, FlowPointContext context);

	T exitIfStat(T domain, FlowPointContext context);

	T enterElseIfStat(T domain, FlowPointContext context);

	T exitElseIfStat(T domain, FlowPointContext context);

	//
	// void enterElseStat(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void exitElseStat(AnalysisDomain<T> domain, AnalysisContext context);
	//
	T enterWhileStatement(T domain, FlowPointContext context);

	T exitWhileStatement(T domain, FlowPointContext context);
	//
	// void enterCondition(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void exitCondition(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void enterValue(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void exitValue(AnalysisDomain<T> domain, AnalysisContext context);

}
