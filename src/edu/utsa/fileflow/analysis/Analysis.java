package edu.utsa.fileflow.analysis;

import edu.utsa.fileflow.cfg.FlowPointContext;

public abstract class Analysis<T extends AnalysisDomain<T>> {

	public T onBegin(T domain) throws AnalysisException {
		return domain;
	}

	public T onFinish(T domain) throws AnalysisException {
		return domain;
	}

	public T onBefore(T domain, FlowPointContext context) throws AnalysisException {
		return domain;
	}

	public T onAfter(T domain, FlowPointContext context) throws AnalysisException {
		return domain;
	}

	public T touch(T domain, FlowPointContext context) throws AnalysisException {
		return domain;
	}

	public T mkdir(T domain, FlowPointContext context) throws AnalysisException {
		return domain;
	}

	public T copy(T domain, FlowPointContext context) throws AnalysisException {
		return domain;
	}

	public T remove(T domain, FlowPointContext context) throws AnalysisException {
		return domain;
	}

	public T enterProg(T domain, FlowPointContext context) throws AnalysisException {
		return domain;
	}

	public T exitProg(T domain, FlowPointContext context) throws AnalysisException {
		return domain;
	}

	public T enterAssignment(T domain, FlowPointContext context) throws AnalysisException {
		return domain;
	}

	public T enterIfStat(T domain, FlowPointContext context) throws AnalysisException {
		return domain;
	}

	public T exitIfStat(T domain, FlowPointContext context) throws AnalysisException {
		return domain;
	}

	public T enterElseIfStat(T domain, FlowPointContext context) throws AnalysisException {
		return domain;
	}

	public T exitElseIfStat(T domain, FlowPointContext context) throws AnalysisException {
		return domain;
	}

	public T enterWhileStatement(T domain, FlowPointContext context) throws AnalysisException {
		return domain;
	}

	public T exitWhileStatement(T domain, FlowPointContext context) throws AnalysisException {
		return domain;
	}

}
