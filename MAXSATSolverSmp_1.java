//******************************************************************************
//
// File:	MAXSATSolverSmp_1.java
//
//******************************************************************************

import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;
import edu.rit.util.Random;

/**
 * Class MAXSATSolverSmp_1 is a parallel version of the Count based
 * Seesaw Search for MAX-SAT program.
 * 
 * <P>
 * Usage: java pj2 MAXSATSolverSmp_1 inputFilePath max-tries 
 * seesawSearchInstances
 * <BR><TT><I><inputFilePath></I></TT> = The path to the .cnf file.
 * <BR><TT><I><max-tries></I></TT> = The number of iterations, after which
 * the  Seesaw Search gives up.
 * <BR><TT><I>seesawSearchInstances></I></TT> = The number of instances of 
 * Seesaw Search's that need to be performed in parallel.
 * 
 * </P>
 * @author Harshad Paradkar
 *
 */
public class MAXSATSolverSmp_1 extends Task{

	// The seed value passed to the Seesaw Search function.
	long seed;

	// The number of instances of Seesaw Search to run in parallel.
	int N;

	// Global reduction variable.
	SolutionVbl reductionVbl;

	/**
	 * Main program.
	 * 
	 * @param ar			An array of command line arguments.
	 * @throws Exception	Any exception that might occur during the execution.
	 */
	public void main(final String ar[])throws Exception{

		// Validate the command line arguments.
		if(Utilities.inputInValidForSmp(ar)) {
			usage();
		}

		// get the parameters after reading the input file.
		MAXSATParameters parameters = Utilities.getParameters(ar[0]);

		if(parameters == null) {
			System.err.println("Unable to read input file.");
			throw new IllegalArgumentException("Unable to read input file.");
		}

		int initializationSeed = 142382;
		Random random = new Random(initializationSeed);

		seed = random.nextLong();

		N = Integer.parseInt(ar[2]);

		// Initialize the Global Reduction Variable with it's Solution.
		reductionVbl = new SolutionVbl(new Solution(parameters.noOfVariables));

		long startTime = System.currentTimeMillis();
		
		parallelFor(0, N - 1).exec(new Loop(){

			// per thread variables
			Random random;
			SolutionVbl perThreadReductionVbl;

			public void start(){
				random = new Random(seed + rank());
				perThreadReductionVbl = threadLocal(reductionVbl);
			}

			public void run (int step) throws Exception{
				
				// Call the Count Based Seesaw Search function
				Solution solution = SeesawSearchCountBased
						.solveMAXSAT(Utilities.getDeepCopiedParameters(parameters),
								Integer.parseInt(ar[1]),
								random.nextLong());

				// Save the Solution with Maximum Satisfied Clauses
				perThreadReductionVbl.compareNoOfSatisfiedClauses(solution);

				// If all clauses satisfied, then stop.
				if(solution.getNoOfSatisfiedClauses() 
						== parameters.noOfClauses) {
					stop();
				}
			}
		});

		long endTime = System.currentTimeMillis();
		float difference = (endTime - startTime) / 1000.0f;
		
		// Print Stats.
		System.out.println("No. of Variables: " + parameters.noOfVariables);
		System.out.println("No. of Clauses: " + parameters.noOfClauses);
		System.out.println("MAX-SAT: " + reductionVbl.getSolution()
		.getNoOfSatisfiedClauses());
		
		if(difference >= 60.0f) {
			System.out.println("Total run time: " + difference / 60.0f + " minute(s).");
		}else {
			System.out.println("Total run time: " + difference + " second(s).");
		}
	}

	/**
	 * Method to notify the user when invalid input arguments to the program
	 * have been provided.
	 */
	private static void usage(){
		System.err.println("Invalid arguments!");
		System.err.println ("Usage: java pj2 MAXSATSolverSmp_1 inputFilePath "
				+ "max-tries seesawSearchInstances");
		System.err.println ("<inputFilePath> = The path to the .cnf file.");
		System.err.println ("<max-tries> = The number of iterations, after "
				+ "which Seesaw Search gives up.");
		System.err.println ("<seesawSearchInstances> = The number of instances "
				+ "of Seesaw Search's that need to be performed in parallel.");
		terminate(1);
	}
}
