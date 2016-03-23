package edu.utsa.fileflow.analysis;

public class DummyAnalysisDomain extends AnalysisDomain {

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DummyAnalysisDomain bottom() {
		// TODO Auto-generated method stub
		return null;
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
