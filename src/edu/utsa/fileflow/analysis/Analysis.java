package edu.utsa.fileflow.analysis;

import edu.utsa.fileflow.cfg.FlowPointContext;

public interface Analysis<T extends AnalysisDomain<T>> {

	T onBegin(T domain) throws AnalysisException;

	T onFinish(T domain) throws AnalysisException;

	T onBefore(T domain, FlowPointContext context) throws AnalysisException;

	T onAfter(T domain, FlowPointContext context) throws AnalysisException;

	T touch(T domain, FlowPointContext context) throws AnalysisException;

	T mkdir(T domain, FlowPointContext context) throws AnalysisException;

	T copy(T domain, FlowPointContext context) throws AnalysisException;

	T remove(T domain, FlowPointContext context) throws AnalysisException;

	T enterProg(T domain, FlowPointContext context) throws AnalysisException;

	T exitProg(T domain, FlowPointContext context) throws AnalysisException;

	//
	// void enterBlock(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void exitBlock(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void enterStatement(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void exitStatement(AnalysisDomain<T> domain, AnalysisContext context);
	//
	T enterAssignment(T domain, FlowPointContext context) throws AnalysisException;

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
	T enterIfStat(T domain, FlowPointContext context) throws AnalysisException;

	T exitIfStat(T domain, FlowPointContext context) throws AnalysisException;

	T enterElseIfStat(T domain, FlowPointContext context) throws AnalysisException;

	T exitElseIfStat(T domain, FlowPointContext context) throws AnalysisException;

	//
	// void enterElseStat(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void exitElseStat(AnalysisDomain<T> domain, AnalysisContext context);
	//
	T enterWhileStatement(T domain, FlowPointContext context) throws AnalysisException;

	T exitWhileStatement(T domain, FlowPointContext context) throws AnalysisException;
	//
	// void enterCondition(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void exitCondition(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void enterValue(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void exitValue(AnalysisDomain<T> domain, AnalysisContext context);

}
