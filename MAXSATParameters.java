//******************************************************************************
//
// File:	MAXSATParameters.java
//
//******************************************************************************

/**
 * Class MAXSATParameters holds the necessary variables required by the
 * Seesaw Search for MAX-SAT program.
 * 
 * @author Harshad Paradkar
 *
 */
public class MAXSATParameters {

	/**
	 * The number of variables in this MAX-SAT problem.
	 */
	public int noOfVariables;
	
	/**
	 * The number of clauses in this MAX-SAT problem.
	 */
	public int noOfClauses;
	
	/**
	 * An array holding the clauses in this MAX-SAT problem.
	 */
	public Clause[] clauses;
}

