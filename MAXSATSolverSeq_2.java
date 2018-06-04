//******************************************************************************
//
// File:	MAXSATSolverSeq_2.java
//
//******************************************************************************

import java.util.Random;

/**
 * Class MAXSATSolverSeq_2 is a sequential version of the Incremental
 * Seesaw Search for MAX-SAT program.
 * 
 * <P>
 * Usage: java pj2 MAXSATSolverSeq_2 inputFilePath max-tries
 * <BR><TT><I><inputFilePath></I></TT> = The path to the .cnf file.
 * <BR><TT><I><max-tries></I></TT> = The number of iterations, after which
 * the  Seesaw Search gives up.
 * 
 * </P>
 * @author Harshad Paradkar
 *
 */
public class MAXSATSolverSeq_2 {

	/**
	 * Main program.
	 * 
	 * @param ar			An array of command line arguments.
	 * @throws Exception	Any exception that might occur during the execution.
	 */
	public static void main(String[] ar) throws Exception{

		// Validate the command line arguments.
		if(Utilities.inputInValidForSeq(ar)) {
			usage();
		}

		// get the parameters after reading the input file.
		MAXSATParameters parameters = Utilities.getParameters(ar[0]);

		if(parameters == null) {
			System.err.println("Unable to read input file.");
			throw new IllegalArgumentException("Unable to read input file.");
		}

		int initializationSeed = 1423821;
		Random random = new Random();

		// Call the Incremental Seesaw Search function
		Solution solution = SeesawSearchIncremental
				.solveMAXSAT(parameters, Integer.parseInt(ar[1]),
						random.nextLong());

		// Print Stats.
		System.out.println("No. of Variables: " + parameters.noOfVariables);
		System.out.println("No. of Clauses: " + parameters.noOfClauses);
		System.out.println("MAX-SAT: " + solution.getNoOfSatisfiedClauses());
		
		float time = solution.getTime();

		if(time >= 60.0f) {
			System.out.println("Run time: " + time / 60.0f + " minute(s).");
		}else {
			System.out.println("Run time: " + time + " second(s).");
		}
	}
	
	/**
	 * Method to notify the user when invalid input arguments to the program
	 * have been provided.
	 */
	private static void usage(){
		System.err.println("Invalid arguments!");
		System.err.println ("Usage: java pj2 MAXSATSolverSeq_2 inputFilePath "
				+ "max-tries");
		System.err.println ("<inputFilePath> = The path to the .cnf file.");
		System.err.println ("<max-tries> = The number of iterations, after "
				+ "which Seesaw Search gives up.");
		throw new IllegalArgumentException();
	}
}
