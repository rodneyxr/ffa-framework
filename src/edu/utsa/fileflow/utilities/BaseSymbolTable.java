package edu.utsa.fileflow.utilities;

import java.util.HashMap;

import edu.utsa.fileflow.analysis.Mergeable;

public abstract class BaseSymbolTable<T extends Mergeable<T>> extends HashMap<String, T> {
	private static final long serialVersionUID = 1L;

	public BaseSymbolTable<T> merge(BaseSymbolTable<T> other) {
		// if is bottom just return
		if (other.isEmpty())
			return this;

		// add (merge) everything in other table to this table
		other.forEach((k, t2) -> {
			T t1 = this.get(k);

			// if item is only in other table
			if (t1 == null) {
				// just add it to this table
				this.put(k, t2);
			} else {
				// item exists in both, merge the two objects
				t1.merge(t2);
			}
		});
		
		return this;
	}

}
