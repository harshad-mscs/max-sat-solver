//******************************************************************************
//
// File:	Utilities.java
//
//******************************************************************************

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;

/**
 * Class Utilities provides the necessary functionality during the execution
 * of the Seesaw Search programs.
 * 
 * @author Harshad Paradkar
 *
 */
public class Utilities {

	/**
	 * Validates the input provided to the sequential version of the 
	 * Seesaw Search program.
	 * 
	 * @param args	The command line arguments to be validated.
	 * @return		true: If input is valid; false: otherwise.
	 */
	public static boolean inputInValidForSeq(String[] args) {

		try {
			if(args.length != 2) {
				return true;
			}

			if(args[0].equals("")) {
				return true;
			}

			if(Integer.parseInt(args[1]) < 1) {
				return true;
			}

			return false;
		}catch(Exception e) {
			return false;
		}
	}

	/**
	 * Validates the input provided to the parallel version of the 
	 * Seesaw Search program.
	 * 
	 * @param args	The command line arguments to be validated.
	 * @return		true: If input is valid; false: otherwise.
	 */
	public static boolean inputInValidForSmp(String[] args) {

		try {
			if(args.length != 3) {
				return true;
			}

			if(args[0].equals("")) {
				return true;
			}

			if(Integer.parseInt(args[1]) < 1) {
				return true;
			}

			if(Integer.parseInt(args[2]) < 1) {
				return true;
			}

			return false;
		}catch(Exception e) {
			return false;
		}
	}

	/**
	 * Reads the input file, and provides the necessary parameters for the
	 * Seesaw Search program.
	 * 
	 * @param filePath		The path to the input file to be read.
	 * @return				Parameters for the Seesaw Search for MAX-SAT
	 * 						program.
	 * @throws Exception	Any exception that might occur during execution.
	 */
	public static MAXSATParameters getParameters(String filePath) 
			throws Exception{

		int clausesEncountered = 0;
		BufferedReader br = null;

		try {
			File file = new File(filePath);

			br = new BufferedReader(new FileReader(file));

			String line;
			int noOfVariables = 0, noOfClauses = 0;
			Variable[] variables = null;
			Clause[] clauses = null;

			while ((line = br.readLine()) != null) {
				line = line .trim();

				if(line.length() > 0 
						&& (line.charAt(0) == 'p' || line.charAt(0) == 'P')) {

					// read the parameters (variable, and clause count)
					
					// trim any extra spaces, if any
					String[] parts = line.split("\\s+");

					noOfVariables = Integer.parseInt(parts[2]);
					variables = new Variable[noOfVariables + 1];

					for(int i = 0; i <= noOfVariables; i++) {
						variables[i] = new Variable(i);
					}

					noOfClauses = Integer.parseInt(parts[3]);
					clauses = new Clause[noOfClauses];

				}else if(line.length() > 0
						&& (line.charAt(0) != 'c' || line.charAt(0) != 'C') 
						&& clausesEncountered < noOfClauses) {

					// read the CNF expression, ignoring the comments
					
					String[] clausesOnALine = line.split("\\s+0\\s*");

					for(int i = 0; i < clausesOnALine.length; i++) {

						String[] stringLiterals = clausesOnALine[i].split("\\s+");

						Clause clause = new Clause(stringLiterals.length);

						Literal[] literals = new Literal[clause.clauseLength];

						for(int j = 0 ; j < stringLiterals.length; j++) {

							int integerLiteral = Integer.parseInt(stringLiterals[j]);

							if(integerLiteral == 0) {
								throw new Exception("Something went wrong"
										+ " in interpreting the clauses.");
							}else {
								if(integerLiteral < 0) {
									literals[j]
											= new Literal(variables[-1 * integerLiteral], true);
								}else {
									literals[j] 
											= new Literal(variables[integerLiteral], false);
								}
							}
						}

						clause.setLiterals(literals);
						clauses[clausesEncountered] = clause;
						clausesEncountered++;
					}
				}
			}

			MAXSATParameters parameters = new MAXSATParameters();

			parameters.noOfVariables = noOfVariables;
			parameters.noOfClauses = noOfClauses;
			parameters.clauses = clauses;

			return parameters;
		}catch(Exception e) {
			System.err.println(e.getMessage());
			return null;
		}finally {
			if(br != null) {
				br.close();
			}
		}
	}

	/**
	 * Prints the clauses along with the literal values.
	 * 
	 * @param clauses	An array, holding clauses to be printed to the standard 
	 * 					output.
	 */
	public static void printClauses(Clause[] clauses) {


		for(int i = 0; i < clauses.length; i++) {

			Literal[] literals = clauses[i].getLiterals();
			System.out.print("[ ");
			for(int j = 0; j < 3; j++) {
				if(literals[j].isNegated) {
					System.out.print("-" + literals[j].variable.name + " ");
				}else {
					System.out.print(literals[j].variable.name + " ");
				}
			}
			System.out.print("]");

			System.out.print(" [ ");
			for(int j = 0; j < 3; j++) {
				if(literals[j].isNegated) {
					System.out.print(!literals[j].variable.value + " ");
				}else {
					System.out.print(literals[j].variable.value + " ");
				}
			}
			System.out.print("]");

			if(clauses[i].isClauseSatisfied()) {
				System.out.println(" = T");
			}else {
				System.out.println(" = F");
			}
		}
	}

	/**
	 * Prints the clauses along with the literal values.
	 * 
	 * @param clauses	An list, holding clauses to be printed to the standard 
	 * 					output.
	 */
	public static void printClauses(LinkedList<Clause> clauses) {


		for(int i = 0; i < clauses.size(); i++) {

			Literal[] literals = clauses.get(i).getLiterals();
			System.out.print("[ ");
			for(int j = 0; j < 3; j++) {
				if(literals[j].isNegated) {
					System.out.print("-" + literals[j].variable.name + " ");
				}else {
					System.out.print(literals[j].variable.name + " ");
				}
			}
			System.out.print("]");

			System.out.print(" [ ");
			for(int j = 0; j < 3; j++) {
				if(literals[j].isNegated) {
					System.out.print(!literals[j].variable.value + " ");
				}else {
					System.out.print(literals[j].variable.value + " ");
				}
			}
			System.out.print("]");

			if(clauses.get(i).isClauseSatisfied()) {
				System.out.println(" = T");
			}else {
				System.out.println(" = F");
			}
		}
	}

	/**
	 * Deep copies the given MAX-SAT parameters into a new object.
	 * 
	 * @param parameters	The parameters to be deep copied.
	 * @return				The deep copied parameters.
	 * @throws Exception	Any exception that might occur during the execution.
	 */
	public static MAXSATParameters getDeepCopiedParameters(MAXSATParameters parameters) throws Exception {
		
		MAXSATParameters deepCopiedParameters = new MAXSATParameters();
		
		deepCopiedParameters.noOfClauses = parameters.noOfClauses;
		deepCopiedParameters.noOfVariables = parameters.noOfVariables;
		
		deepCopiedParameters.clauses = getDeepCopiedClauses(parameters.clauses, parameters.noOfVariables);
		
		return deepCopiedParameters;
	}

	/**
	 * Extracts the variables from the clauses into an array.
	 * 
	 * @param clauses		The clauses from which variables need to be 
	 * 						extracted.
	 * @param noOfVariables	The number of distinct variables in the clauses.
	 * @return				Array of deep copied variables from the clauses.
	 */
	public static Variable[] extractVariables(Clause[] clauses, int noOfVariables) {
		
		Variable[] variables = new Variable[noOfVariables + 1];
		
		for(int i = 0; i < clauses.length; i++) {
			Literal[] literals = clauses[i].getLiterals();
			
			for(int j = 0; j < literals.length; j++) {
				Variable variable = literals[j].variable;
				variables[variable.name] = new Variable(variable.name, variable.value);
			}
		}
		
		return variables;
	}
	
	/**
	 * Deep copies the given array of clauses into another array of clauses.
	 * 
	 * @param clauses		The clauses that need to be deep copied.
	 * @param noOfVariables	The number of distinct variables in the clauses.
	 * @return				Deep copied array of clauses.
	 * @throws Exception	Any exception that might occur during the execution.
	 */
	public static Clause[] getDeepCopiedClauses(Clause[] clauses, int noOfVariables) throws Exception{

		Variable[] variables = extractVariables(clauses, noOfVariables);
		
		Clause[] deepCopiedClauses = new Clause[clauses.length];

		for(int i = 0; i < clauses.length; i++) {

			Clause clause = clauses[i];

			Literal[] literals = clause.getLiterals();
			Literal[] deepCopiedLiterals = new Literal[literals.length];

			for(int j = 0; j < deepCopiedLiterals.length; j++) {

				deepCopiedLiterals[j] = new Literal(variables[literals[j].variable.name],
						literals[j].isNegated);
			}

			Clause deepCopiedClause = new Clause(clause.clauseLength);
			deepCopiedClause.setLiterals(deepCopiedLiterals);

			deepCopiedClauses[i] = deepCopiedClause;
		}

		return deepCopiedClauses;
	}	
}