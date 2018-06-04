//******************************************************************************
//
// File:	Solution.java
//
//******************************************************************************

/**
 * Class Solution acts as a model to store the various parameters of the 
 * output from the Seesaw Search for solving the MAX-SAT problem.
 * 
 * @author Harshad Paradkar
 *
 */
public class Solution {

	private float time; // Time taken for execution of Seesaw Search

	private int noOfIterations; // Iterations to reach the returned solution

	private int noOfSatisfiedClauses; // Satisfied clauses count
	
	private int noOfVariables; // Variable count
	
	private Clause[] clauses; // Array holding clauses in solution

	/**
	 * Construct a Solution for the problem with the specified number of 
	 * variables.
	 * 
	 * @param noOfVariables	Variable count for this Solution.
	 */
	public Solution(int noOfVariables) {

		if(noOfVariables < 1) {
			throw new IllegalArgumentException ("No of variables should be"
					+ "non-zero positive integer.");
		}else {
			this.noOfVariables = noOfVariables;
		}
	}
	
	/**
	 * Make a deep copy of the passed solution into this solution.
	 * 
	 * @param solution	The solution to be deep copied into this.
	 * @return			The solution in which the deep copy was performed.
	 */
	public Solution copy(Solution solution){

		this.noOfIterations = solution.noOfIterations;
		this.noOfSatisfiedClauses = solution.noOfSatisfiedClauses;
		this.time = solution.time;
		
		try {
			if(solution.clauses != null) {
				this.clauses = Utilities.getDeepCopiedClauses(solution.clauses, 
						noOfVariables);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return this;
	}

	/**
	 * @param solution
	 * @return
	 */
	public Solution update(Solution solution){

		this.noOfIterations += solution.noOfIterations;
		this.noOfSatisfiedClauses = solution.noOfSatisfiedClauses;
		this.time += solution.time;
		
		try {
			if(solution.clauses != null) {
				this.clauses = Utilities.getDeepCopiedClauses(solution.clauses, 
						noOfVariables);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return this;
	}
	
	/**
	 * Create a deep copied clone of this solution, and return it.
	 */
	public Object clone(){
		Solution solution = new Solution(this.noOfVariables);
		solution.copy (this);
		return solution;
	}

	/**
	 * @return	The clauses in the solution.
	 */
	public Clause[] getClauses(){
		return clauses;
	}
	
	/**
	 * @return	The time taken for this instance of the search to provide
	 * 			this solution.
	 */
	public float getTime() {
		return time;
	}
	
	/**
	 * @return	The Satisfied clauses count in this solution.
	 */
	public int getNoOfSatisfiedClauses() {
		return noOfSatisfiedClauses;
	}
	
	/**
	 * @return	The value of max-tries to reach this solution.
	 */
	public int getNoOfIterations() {
		return noOfIterations;
	}
	
	/**
	 * @param clauses	Set the clauses in this solution to the given clauses.
	 */
	public void setClauses(Clause[] clauses) {
		
		this.clauses = clauses;
	}
	
	/**
	 * @param time	Set the time in this solution to the given time.
	 */
	public void setTime(float time) {
		this.time = time;
	}
	
	/**
	 * @param noOfSatisfiedClauses	Set the number of satisfied clauses in this 
	 * 								solution to the given number of satisfied
	 * 								clauses.
	 */
	public void setNoOfSatisfiedClauses(int noOfSatisfiedClauses) {
		this.noOfSatisfiedClauses = noOfSatisfiedClauses;
	}
	
	/**
	 * @param noOfIterations	Set the number of iterations in this solution 
	 * 							to the given number of iterations.
	 */
	public void setNoOfIterations(int noOfIterations) {
		this.noOfIterations = noOfIterations;
	}
}