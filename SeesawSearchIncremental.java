//******************************************************************************
//
// File:	SeesawSearchIncremental.java
//
//******************************************************************************

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Class SeesawSearchIncremental is used to perform the Incremental Seesaw
 * Search for solving the MAX-SAT problem.
 * 
 * @author Harshad Paradkar
 *
 */
public class SeesawSearchIncremental {

	/**
	 * This method performs the Incremental Seesaw Search to solve the
	 * MAX-SAT problem for the given parameters.
	 * 
	 * @param parameters	The parameters for the MAX-SAT problem that needs
	 * 						to be solved.
	 * @param maxTries		Number of times to perform the search, after which
	 * 						the Seesaw Search gives up.
	 * @param seed			The seed value, to initialize the PRNG.
	 * @return				An object of type Solution.
	 * @throws Exception	Any exception that might occur during execution.
	 */
	public static Solution solveMAXSAT(MAXSATParameters parameters,
			int maxTries, long seed) throws Exception {

		Random random;

		if(seed == 0) {
			// If the calling method does not want to provide a seed value.
			random = new Random();
		}else {
			// Use the seed value provided by the calling method.
			random = new Random(seed);
		}

		// To store the clauses which need to be added to the solution
		LinkedList<Clause> unexploredClauses = new LinkedList<Clause>();

		for(int i = 0; i < parameters.clauses.length; i++) {
			unexploredClauses.add(parameters.clauses[i]);
		}

		// To hold the clauses that are satisfied
		LinkedList<Clause> trueClauses = new LinkedList<Clause>();

		Clause falseClause = null;

		int noOfIterations = 0, maxSatisfiedClausesCount = 0;
		Clause[] maxSatisfiedClauses = null;

		long startTime = System.currentTimeMillis();

		for(int i = 0; i < maxTries; i++) {

			noOfIterations++;

			int index;
			Clause unExploredClause = null;

			/******************************************************************
			 * 						Optimization phase
			 * 
			 * Keep on adding clauses to the solution. If the last clause 
			 * added was FALSE, break out of Optimization phase, since the 
			 * solution is now in an inconsistent state.
			 *****************************************************************/
			while(true) {

				if(unexploredClauses.size() == 0) {
					break;
				}

				// Randomly generate index of the next clause to pick 
				index = random.nextInt(unexploredClauses.size());

				// Get the randomly picked clause
				unExploredClause = unexploredClauses.remove(index);

				if(unExploredClause.isClauseSatisfied()) {
					// Add the clause to solution if it is TRUE
					trueClauses.add(unExploredClause);
				}else {
					// Mark the FALSE clause, and break
					falseClause = unExploredClause;
					break;
				}
			}// Optimization phase.

			if(trueClauses.size() == parameters.noOfClauses) {
				/* if all the clauses evaluated to true, then no need to go
				 * to the constraining phase. 
				 */
				break;
			}

			/******************************************************************
			 * 						Constraining phase
			 * 
			 * Make all the clauses in the solution evaluate to TRUE.
			 * 
			 * Method 1: Remove the FALSE clause from solution, and place it
			 * back in the list of unexplored clauses, to be picked up by the
			 * Optimization phase.
			 * 
			 * Method 2: Flip a randomly chosen variable from the FALSE clause,
			 * and remove the clauses which might have turned FALSE now from the 
			 * solution, and place them back in the list of unexplored clauses,
			 * to be picked up by the Optimization phase.
			 *****************************************************************/
			Literal[] falseClauseLiterals = falseClause.getLiterals();
			boolean falseClauseSatisfied = false;

			// Generate random value between 0.0 and 1.0(exclusive)
			double sigma = random.nextDouble();
			//sigma  = 0.6;
			if(sigma <= 0.3) {

				/**
				 * Prefer the FALSE clauses.
				 * But only do it less often, as it makes the solution
				 * deviate from the Optimum, but it's required to provide the
				 * necessary randomization.
				 **/

				Variable variable 
				= falseClauseLiterals[random
				                      .nextInt(falseClauseLiterals.length)]
				                    		  .variable;
				// Flip the randomly chosen variable value from FALSE clause
				variable.value = !variable.value;

				// Add the FALSE clause turned TRUE to the solution
				trueClauses.add(falseClause);

				Iterator<Clause> iterator = trueClauses.iterator();

				while(iterator.hasNext()) {

					Clause clause = iterator.next();

					if(clause.isClauseSatisfied()) {
						// if this clause still evaluates to true, check others
						continue;
					}else {
						// Add the TRUE clause turned FALSE back to the list
						// of unexplored clauses
						unexploredClauses.add(clause);
						iterator.remove();
					}
				}


			}else {

				/**
				 * Prefer the TRUE clauses.
				 * Try flipping all variables in the FALSE clauses, so that this
				 * clause is compatible with the rest of the TRUE clauses, even
				 * after flipping the values.
				 * 
				 * If it isn't, put the FALSE clause back into the list of
				 * unexplored clauses.
				 * (Also remove a TRUE clause, because there was some TRUE
				 * clause that wasn't compatible with the FALSE clause. 
				 * Helps the program stay out of local optima.)
				 **/

				LinkedList<Clause> unCompatibleClauses = new LinkedList<Clause>();

				for(int j = 0; j < falseClauseLiterals.length; j++) {

					unCompatibleClauses.clear();

					// get each variable in the false clause, and flip it.
					Variable variable = falseClauseLiterals[j].variable;
					variable.value = !variable.value;

					boolean trueClausesStillSatisfied = true;

					/* After flipping a variable in the false clause, in order 
					 * to make it evaluate to true, check if the other true 
					 * clauses in the intermediate solution are still
					 * evaluating to true.*/
					Iterator<Clause> iterator = trueClauses.iterator();

					while(iterator.hasNext()) {

						Clause clause = iterator.next();

						if(clause.isClauseSatisfied()) {
							// if this clause still evaluates to true,
							// check others
							continue;
						}else {
							/* if this clause is no more true, flip back the
							 * variable in the false clause to it's original
							 * value. Also, set a flag, to continue flipping 
							 * other variables in the false clause.
							 */
							variable.value = !variable.value;
							trueClausesStillSatisfied = false;
							unCompatibleClauses.add(clause);
							break;
						}
					}

					if(trueClausesStillSatisfied) {
						falseClauseSatisfied = true;
						break;
					}
				}

				if(falseClauseSatisfied) {
					trueClauses.add(falseClause);
				}else {
					/*unexploredClauses.add(falseClause);

					Clause unCompatibleClause = unCompatibleClauses
							.remove(random.nextInt(unCompatibleClauses.size()));

					trueClauses.remove(unCompatibleClause);
					unexploredClauses.add(unCompatibleClause);
					unCompatibleClauses.clear();*/

					unexploredClauses.add(falseClause);

					unexploredClauses.add(trueClauses.remove(random.nextInt(trueClauses.size())));
				}
			}// Constraining phase

			// Save this state if a better solution was found
			if(trueClauses.size() > maxSatisfiedClausesCount) {
				maxSatisfiedClausesCount = trueClauses.size();
				maxSatisfiedClauses = Utilities
						.getDeepCopiedClauses(parameters.clauses,
								parameters.noOfVariables);
			}
		}// max-tries

		long endTime = System.currentTimeMillis();

		// Save time elapsed in seconds
		float difference = (endTime - startTime) / 1000.0f;

		Solution solution = new Solution(parameters.noOfVariables);

		solution.setNoOfIterations(noOfIterations);
		solution.setNoOfSatisfiedClauses(maxSatisfiedClausesCount);
		solution.setTime(difference);
		solution.setClauses(maxSatisfiedClauses);

		return solution;
	}
}
