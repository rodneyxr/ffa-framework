package edu.utsa.fileflow.compiler;

public class ConditionManager {

	private final Precondition precondition;
	private final Postcondition postcondition;

	public ConditionManager() {
		precondition = new Precondition();
		postcondition = new Postcondition();
	}

	public Precondition getPrecondition() {
		return precondition;
	}

	public Postcondition getPostcondition() {
		return postcondition;
	}

	public void print() {
		// print the precondition
		System.out.println("-------------");
		System.out.println("Precondition");
		System.out.println("-------------");
		precondition.print();

		System.out.println();

		// print the postcondition
		System.out.println("--------------");
		System.out.println("Postcondition");
		System.out.println("--------------");
		postcondition.print();
	}

}
