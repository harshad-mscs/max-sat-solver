//******************************************************************************
//
// File:	MAXSATSolverSmp_3.java
//
//******************************************************************************

import java.util.Random;
import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;

/**
 * Class MAXSATSolverSmp_3 is a parallel version of the Incremental
 * Seesaw Search with restart for MAX-SAT program.
 * 
 * A parallel version of the Incremental Seesaw Search is ran, and the 
 * solution is obtained. This solution will then be again passed to another
 * instance of the parallel Incremental Seesaw Search. This is done for 'M' 
 * number of times, so that each local optimum is optimized further to reach
 * the global optima.
 * 
 * <P>
 * Usage: java pj2 MAXSATSolverSmp_3 inputFilePath max-tries 
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
public class MAXSATSolverSmp_3  extends Task{

	long seed, // The seed value passed to the Seesaw Search function.
	seedOne, seedTwo;

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

		//int initializationSeed = 142382;
		Random random = new Random();

		//int vblInitChoice = 2;

		/*if(vblInitChoice == 1) {
			for(int i = 0; i < parameters.variables.length; i++) {
				parameters.variables[i].value = true;
			}
		}else if(vblInitChoice == 2){
			for(int i = 0; i < parameters.variables.length; i++) {
				parameters.variables[i].value = false;
			}
		}else if(vblInitChoice == 3){
			for(int i = 0; i < parameters.variables.length; i++) {
				parameters.variables[i].value = random.nextBoolean();
			}
		}*/

		N = Integer.parseInt(ar[2]);

		int bestM = 0, bestMaxSatisfiedClauses = 0;
		String bestTime = "";

		long startTime = System.currentTimeMillis();

		// Initialize the Global Reduction Variable with it's Solution.
		reductionVbl = new SolutionVbl(new Solution(parameters.noOfVariables));

		for(int M = 0; M < 150; M++) {

			// Generate 2 random seeds
			seedOne = random.nextLong();
			seedTwo = random.nextLong();

			// Obtain the final seed
			seed = (seedOne / 2 + seedTwo / 2);

			parallelFor(0, N - 1).exec(new Loop(){

				// per thread variables
				Random random;
				SolutionVbl perThreadReductionVbl;

				public void start(){
					random = new Random(seed + rank());
					perThreadReductionVbl = threadLocal(reductionVbl);
				}

				public void run (int step) throws Exception{
					
					// Call the Incremental Seesaw Search function
					Solution solution = SeesawSearchIncremental
							.solveMAXSAT(Utilities.getDeepCopiedParameters(parameters),
									Integer.parseInt(ar[1]),
									random.nextLong());

					// Save the Solution with Maximum Satisfied Clauses
					perThreadReductionVbl.compareNoOfSatisfiedClauses(solution);

					// If all clauses satisfied, then stop.
					if(perThreadReductionVbl.getSolution()
							.getNoOfSatisfiedClauses() == parameters.noOfClauses) {
						stop();
					}
				}
			});

			float intermediateTime = (System.currentTimeMillis() - startTime) / 1000.0f;
			String intermediateTimeString = "";
			if(intermediateTime >= 60.0f) {
				intermediateTimeString = (intermediateTime / 60.0f) + " minute(s).";
			}else {
				intermediateTimeString = (intermediateTime) + " second(s).";
			}

			System.out.println("M: " + (M + 1) + ", Max found: " 
					+ reductionVbl.getSolution().getNoOfSatisfiedClauses() 
					+ ", time: " + intermediateTimeString) ;
			System.out.flush();

			if(bestMaxSatisfiedClauses < reductionVbl.getSolution()
					.getNoOfSatisfiedClauses()) {
				bestMaxSatisfiedClauses = reductionVbl.getSolution()
						.getNoOfSatisfiedClauses();
				bestM = M + 1;
				bestTime = intermediateTimeString;
			}

			// If all clauses satisfied, then do not restart.
			if(reductionVbl.getSolution().getNoOfSatisfiedClauses() 
					== parameters.noOfClauses) {
				break;
			}

			// get the clauses to restart the search from this point
			parameters.clauses = reductionVbl.getSolution().getClauses();
		}

		long endTime = System.currentTimeMillis();
		float difference = (endTime - startTime) / 1000.0f;

		// Print Stats.
		System.out.println("No. of Variables: " + parameters.noOfVariables);
		System.out.println("No. of Clauses: " + parameters.noOfClauses);
		System.out.println("Best Solution found: M = " + bestM + ", MAX-SAT = " 
				+ bestMaxSatisfiedClauses + ", time = " + bestTime);
		
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
		System.err.println ("Usage: java pj2 MAXSATSolverSmp_3 inputFilePath "
				+ "max-tries seesawSearchInstances");
		System.err.println ("<inputFilePath> = The path to the .cnf file.");
		System.err.println ("<max-tries> = The number of iterations, after "
				+ "which Seesaw Search gives up.");
		System.err.println ("<seesawSearchInstances> = The number of instances "
				+ "of Seesaw Search's that need to be performed in parallel.");
		terminate(1);
	}
}
