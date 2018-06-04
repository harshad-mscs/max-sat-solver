//******************************************************************************
//
// File:	Clause.java
//
//******************************************************************************

/**
 * Class Clause is a model for the clause in the MAX-SAT problem.
 * 
 * @author Harshad Paradkar
 */
class Clause{

	int clauseLength;	// The number of literals in this clause

	private Literal[] literals; // An array holding the literals in this clause

	/**
	 * Construct a new clause of the given length.
	 * 
	 * @param clauseLength The number of literals in this clause.
	 */
	public Clause(int clauseLength) {
		this.clauseLength = clauseLength;
		literals = new Literal[clauseLength];
	}

	/**
	 * @return An array of literals, of type Literal, in this clause.
	 */
	public Literal[] getLiterals() {
		return literals;
	}

	/**
	 * This method sets the given array of literals as this clauses' literals.
	 * 
	 * @param ltrls			The given literals to set as this clauses' literals.
	 * @throws Exception	If literal length is not same as clause length.
	 */
	public void setLiterals(Literal[] ltrls) throws Exception {

		if(ltrls.length != clauseLength) {
			throw new Exception("Invalid number of literals provided.");
		}else {
			this.literals = ltrls;
			/*for(int i = 0; i < ltrls.length; i++) {

				if(ltrls[i].variable.name == 0) {
					throw new Exception("Literal name should be non-zero.");
				}

				literals[i] = ltrls[i];
			}*/
		}
	}

	/**
	 * Checks if this clause is satisfied or not, for it's given literals.
	 * 
	 * @return	true: If clause is satisfied; false: otherwise.
	 */
	public boolean isClauseSatisfied() {

		for(int i = 0; i < literals.length; i++) {

			if(literals[i].isNegated) {
				if(!literals[i].variable.value) {
					return true;
				}
			}else if(literals[i].variable.value){
				return true;
			}
		}

		return false;
	}
}
