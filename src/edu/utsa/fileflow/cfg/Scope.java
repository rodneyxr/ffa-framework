package edu.utsa.fileflow.cfg;

/**
 * A scope in our context will refer to a block of code. We will keep track of a
 * stack of scopes as we traverse the AST. Each scope contains an enter and exit
 * flow point.
 * 
 * @author Rodney Rodriguez
 *
 */
public class Scope {

	private FlowPoint enter;
	private FlowPoint exit;

	public Scope(FlowPointContext enterfpctx) {
		this(enterfpctx, null);
	}
	
	public Scope(FlowPointContext enterfpctx, FlowPointContext exitfpctx) {
		this.enter = new FlowPoint(enterfpctx);
		this.exit = new FlowPoint(exitfpctx);
	}

	/**
	 * Gets the enter flow point of the scope.
	 * 
	 * @return The enter flow point of the scope.
	 */
	public FlowPoint getEnter() {
		return enter;
	}

	/**
	 * Gets the exit flow point of the scope.
	 * 
	 * @return The exit flow point of the scope.
	 */
	public FlowPoint getExit() {
		return exit;
	}

}
