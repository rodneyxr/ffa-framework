package edu.utsa.fileflow.utilities;

import java.util.HashMap;

public abstract class BaseSymbolTable<T> {

	protected final HashMap<String, T> table = new HashMap<>();

	public BaseSymbolTable() {
	}

	public T get(String variable) {
		return table.get(variable);
	}

}
