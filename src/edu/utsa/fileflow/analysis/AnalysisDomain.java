package edu.utsa.fileflow.analysis;

public abstract class AnalysisDomain implements Comparable<AnalysisDomain> {

	public abstract AnalysisDomain merge(AnalysisDomain domain);

	public abstract AnalysisDomain top();

	public abstract AnalysisDomain bottom();

	public abstract int compareTo(AnalysisDomain o);
	
	public abstract AnalysisDomain clone();

}
