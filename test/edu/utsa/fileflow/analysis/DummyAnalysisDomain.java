package edu.utsa.fileflow.analysis;

public class DummyAnalysisDomain extends AnalysisDomain {

	private static final DummyAnalysisDomain TOP = new DummyAnalysisDomain();
	private static final DummyAnalysisDomain BOTTOM = new DummyAnalysisDomain();
	static {
		TOP.flag = 1;
		BOTTOM.flag = 0;
	}

	int flag = 0;

	@Override
	public DummyAnalysisDomain merge(AnalysisDomain d) {
		DummyAnalysisDomain domain = (DummyAnalysisDomain) d;
		if (flag != domain.flag) {
			flag = 1;
		}
		return this;
	}

	@Override
	public DummyAnalysisDomain top() {
		return TOP.clone();
	}

	@Override
	public DummyAnalysisDomain bottom() {
		return BOTTOM.clone();
	}

	@Override
	public int compareTo(AnalysisDomain o) {
		DummyAnalysisDomain domain = (DummyAnalysisDomain) o;
		if (flag < domain.flag)
			return -1;
		if (flag > domain.flag)
			return 1;
		return 0;
	}

	@Override
	public String toString() {
		return Integer.toString(flag);
	}

	@Override
	public DummyAnalysisDomain clone() {
		DummyAnalysisDomain domain = new DummyAnalysisDomain();
		domain.flag = flag;
		return domain;
	}

}
