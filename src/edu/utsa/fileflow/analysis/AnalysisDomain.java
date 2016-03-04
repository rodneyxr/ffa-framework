package edu.utsa.fileflow.analysis;

public abstract class AnalysisDomain<T> implements Comparable<T> {
	
	public abstract void merge();
	public abstract T top();
	public abstract T bottom();
	public abstract int compareTo(T o);

}
