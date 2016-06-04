package edu.utsa.fileflow.analysis;

import edu.utsa.fileflow.cfg.FlowPointContext;

public abstract class BaseAnalysis<T extends AnalysisDomain<T>> implements Analysis<T> {

	@Override
	public T onBegin(T domain) {
		return domain;
	}

	@Override
	public T onFinish(T domain) {
		return domain;
	}

	@Override
	public T onBefore(T domain, FlowPointContext context) {
		return domain;
	}

	@Override
	public T onAfter(T domain, FlowPointContext context) {
		return domain;
	}

	@Override
	public T touch(T domain, FlowPointContext context) {
		return domain;
	}

	@Override
	public T mkdir(T domain, FlowPointContext context) {
		return domain;
	}

	@Override
	public T copy(T domain, FlowPointContext context) {
		return domain;
	}

	@Override
	public T remove(T domain, FlowPointContext context) {
		return domain;
	}

	@Override
	public T enterProg(T domain, FlowPointContext context) {
		return domain;
	}

	@Override
	public T exitProg(T domain, FlowPointContext context) {
		return domain;
	}

	@Override
	public T enterAssignment(T domain, FlowPointContext context) {
		return domain;
	}

	@Override
	public T enterIfStat(T domain, FlowPointContext context) {
		return domain;
	}

	@Override
	public T exitIfStat(T domain, FlowPointContext context) {
		return domain;
	}

	@Override
	public T enterWhileStatement(T domain, FlowPointContext context) {
		return domain;
	}

	@Override
	public T exitWhileStatement(T domain, FlowPointContext context) {
		return domain;
	}

}
