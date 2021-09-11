package edu.utsa.fileflow.analysis;

public interface Mergeable<T> {
	T merge(T other);
}
