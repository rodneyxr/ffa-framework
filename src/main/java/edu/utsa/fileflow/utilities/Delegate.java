package edu.utsa.fileflow.utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * This class will hold objects that implement the Runnable interface. It is to
 * be used add a variable amount of Runnable objects and run them at a later
 * time.
 * 
 * @author Rodney Rodriguez
 *
 */
public class Delegate {

	// List of runnable objects to be executed when run is invoked
	private List<Runnable> runnables;

	/**
	 * Initializes an empty list of runnables.
	 */
	public Delegate() {
		this(null);
	}

	/**
	 * Initializes the list of runnables and sets the value passed in as the
	 * only Runnable in the delegate.
	 * 
	 * @param runnable
	 *            The Runnable to be set.
	 */
	public Delegate(Runnable runnable) {
		runnables = new ArrayList<Runnable>();
		add(runnable);
	}

	/**
	 * Add a runnable to the list. This method does nothing if the value passed
	 * in is null.
	 * 
	 * @param runnable
	 *            Runnable object that overrides execute.
	 */
	public void add(Runnable runnable) {
		if (runnable != null) {
			runnables.add(runnable);
		}
	}

	/**
	 * Clears the current list of runnables and adds the value passed in. This
	 * method will result in the value passed in being the only Runnable in this
	 * delegate.
	 * 
	 * @param runnable
	 *            Runnable object that overrides execute.
	 */
	public void set(Runnable runnable) {
		clear();
		add(runnable);
	}

	/**
	 * Removes all runnables from the delegate.
	 */
	public void clear() {
		runnables.clear();
	}

	/**
	 * Invokes run for each Runnable in this instance of delegate.
	 */
	public void run() {
		for (Runnable runnable : runnables)
			runnable.run();
	}
}
