//******************************************************************************
//
// File:	SolutionVbl.java
//
//******************************************************************************

import edu.rit.pj2.Vbl;

/**
 * Class SolutionVbl acts as the reduction variable for reducing the 
 * Seesaw Search solutions obtained from each thread, to the best one.
 * 
 * Best solution is the one which gives maximum number of satisfied clauses.
 * 
 * @author Harshad Paradkar
 *
 */
public class SolutionVbl implements Vbl{

	private Solution solution;

	/**
	 * Construct a reduction variable with the given solution.
	 * @param solution	The solution which will be reduced.
	 */
	public SolutionVbl(Solution solution) {
		this.solution = solution;
	}

	public Object clone(){

		SolutionVbl solutionVbl = new SolutionVbl(this.solution);
		if (this.solution != null)
			solutionVbl.solution = (Solution) this.solution.clone();
		return solutionVbl;
	}

	@Override
	public void set(Vbl vbl) {
		this.solution.copy (((SolutionVbl)vbl).solution);
	}

	/**
	 * Compares the number of satisfied clauses in this solution, and the 
	 * given solution, and deep copies the given solution into this 
	 * solution if the given solution provides more number of satisfied
	 * clauses that in this solution.
	 * 
	 * @param solution	The solution to be compared with this solution for
	 * 					optimality.
	 */
	public void compareNoOfSatisfiedClauses(Solution solution) {

		if(solution.getNoOfSatisfiedClauses() >
		this.solution.getNoOfSatisfiedClauses()) {
			this.solution.copy(solution);
		}
	}

	/**
	 * @return	The solution from this reduction variable.
	 */
	public Solution getSolution() {
		return solution;
	}

	@Override
	public void reduce(Vbl vbl) {

		if(((SolutionVbl)vbl).solution.getNoOfSatisfiedClauses() >
		this.solution.getNoOfSatisfiedClauses()) {
			this.solution.copy(((SolutionVbl)vbl).solution);
		}
	}
}