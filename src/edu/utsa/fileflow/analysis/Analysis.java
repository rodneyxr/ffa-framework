package edu.utsa.fileflow.analysis;

import edu.utsa.fileflow.cfg.FlowPointContext;

public interface Analysis<T extends AnalysisDomain> {

	AnalysisDomain touch(T domain, FlowPointContext context);

	AnalysisDomain mkdir(T domain, FlowPointContext context);

	AnalysisDomain copy(T domain, FlowPointContext context);

	AnalysisDomain remove(T domain, FlowPointContext context);

	AnalysisDomain enterProg(T domain, FlowPointContext context);

	AnalysisDomain exitProg(T domain, FlowPointContext context);

	//
	// void enterBlock(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void exitBlock(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void enterStatement(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void exitStatement(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void enterAssignment(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void exitAssignment(AnalysisDomain<T> domain, AnalysisContext context);
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
	// void enterIfStat(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void exitIfStat(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void enterElseIfStat(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void exitElseIfStat(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void enterElseStat(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void exitElseStat(AnalysisDomain<T> domain, AnalysisContext context);
	//
	AnalysisDomain enterWhileStatement(T domain, FlowPointContext context);

	AnalysisDomain exitWhileStatement(T domain, FlowPointContext context);
	//
	// void enterCondition(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void exitCondition(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void enterValue(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void exitValue(AnalysisDomain<T> domain, AnalysisContext context);

}
