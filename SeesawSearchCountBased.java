//******************************************************************************
//
// File:	SeesawSearchCountBased.java
//
//******************************************************************************

import java.util.Random;

/**
 * Class SeesawSearchCountBased is used to perform the Count based Seesaw
 * Search for solving the MAX-SAT problem.
 * 
 * @author Harshad Paradkar
 *
 */
public class SeesawSearchCountBased {

	/**
	 * This method performs the Count based Seesaw Search to solve the
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

		// To hold the maximum satisfied clauses in any particular iteration.
		int temp = 0;

		// Array to hold the truth value counts for each variable.
		VariableCount variableTruthValueCounts[] = null;

		int noOfIterations = 0, maxSatisfiedClausesCount = 0;
		Clause[] maxSatisfiedClauses = null;

		long startTime = System.currentTimeMillis();

		// Start of Count based Seesaw Search
		for(int i = 0; i < maxTries; i++) {

			noOfIterations++;
			temp = 0;
			variableTruthValueCounts = new VariableCount[parameters.noOfVariables + 1];

			/******************************************************************
			 * 						Optimization phase
			 * 
			 * Make every clause evaluate to TRUE. If a clause is not TRUE,
			 * randomly choose a variable in that clause, and flip it.
			 * This will create inconsistencies in the value of variables
			 * across the clauses, but the Optimization phase does not care
			 * about it.
			 * 
			 * Update the count of value for the variables in each clause, to
			 * indicate how many times a variable was TRUE, and FALSE.
			 *****************************************************************/
			for(int j = 0; j < parameters.clauses.length; j++) {

				// Get a clause to make it TRUE, if it's not already.
				Clause clause = parameters.clauses[j];
				Literal[] literals = clause.getLiterals();

				if(!clause.isClauseSatisfied()) {
					// If clause is not satisfied, make it evaluate to TRUE
					int literalIndex = random.nextInt(literals.length);
					Variable variable = literals[literalIndex].variable;
					variable.value = !variable.value;
				}

				// Record the values count of each variable in this clause
				for(int k = 0; k < literals.length; k++) {

					Variable variable = literals[k].variable;

					VariableCount variableCount 
					= variableTruthValueCounts[variable.name];

					if(variableCount == null) {
						variableCount = new VariableCount();
						variableCount.variable = variable;
						variableTruthValueCounts[variable.name] = variableCount;
					}

					if(variable.value) {
						variableCount.trueCount += 1;
					}else {
						variableCount.falseCount += 1;
					}
				}

			}// Optimization phase.

			/******************************************************************
			 * 						Constraining phase
			 * 
			 * Make every variable value consistent across all the clauses by
			 * using a greedy approach.
			 * 
			 * Greedy approach idea: From the optimization phase, if a
			 * particular boolean variable value occurs more number of times 
			 * than the other value, then it must be set to that value.
			 *****************************************************************/
			for(int j = 1; j < variableTruthValueCounts.length; j++) {

				if(variableTruthValueCounts[j].trueCount >= 
						variableTruthValueCounts[j].falseCount) {
					variableTruthValueCounts[j].variable.value = true;
				}else {
					variableTruthValueCounts[j].variable.value = false;
				}
			}// Constraining phase

			// Count the number of satisfied clauses
			for(int j = 0; j < parameters.noOfClauses; j++) {
				Clause clause = parameters.clauses[j];
				if(clause.isClauseSatisfied()) {
					temp++;
				}
			}

			// Save this state if a better solution was found
			if(temp > maxSatisfiedClausesCount) {
				maxSatisfiedClausesCount = temp;
				maxSatisfiedClauses = Utilities
						.getDeepCopiedClauses(parameters.clauses,
								parameters.noOfVariables);
				
				if(temp == parameters.noOfClauses) {
					/* if all the clauses evaluated to true, then no need to
					 * search further.
					 */
					break;
				}
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