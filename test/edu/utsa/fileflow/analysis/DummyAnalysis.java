package edu.utsa.fileflow.analysis;

import edu.utsa.fileflow.cfg.FlowPointContext;

public class DummyAnalysis extends BaseAnalysis<DummyAnalysisDomain> {

	@Override
	public DummyAnalysisDomain onBefore(DummyAnalysisDomain domain, FlowPointContext context) {
		domain.flag = 1;
		return domain;
	}

}
