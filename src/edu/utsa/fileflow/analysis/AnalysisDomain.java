package edu.utsa.fileflow.analysis;

public abstract class AnalysisDomain<T extends AnalysisDomain<T>> implements Comparable<T> {

	public abstract T merge(T domain);

	public abstract T top();

	public abstract T bottom();

	public abstract int compareTo(T o);

	public abstract T clone();

}
