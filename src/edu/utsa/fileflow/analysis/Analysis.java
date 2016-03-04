package edu.utsa.fileflow.analysis;

public interface Analysis<T> {

	void touch(AnalysisDomain<T> domain, AnalysisContext context);

	void mkdir(AnalysisDomain<T> domain, AnalysisContext context);

	void copy(AnalysisDomain<T> domain, AnalysisContext context);

	void remove(AnalysisDomain<T> domain, AnalysisContext context);

	// void enterProg(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void exitProg(AnalysisDomain<T> domain, AnalysisContext context);
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
	// void enterWhileStatement(AnalysisDomain<T> domain, AnalysisContext
	// context);
	//
	// void exitWhileStatement(AnalysisDomain<T> domain, AnalysisContext
	// context);
	//
	// void enterCondition(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void exitCondition(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void enterValue(AnalysisDomain<T> domain, AnalysisContext context);
	//
	// void exitValue(AnalysisDomain<T> domain, AnalysisContext context);

}
