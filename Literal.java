//******************************************************************************
//
// File:	Literal.java
//
//******************************************************************************

/**
 * Class Literal acts as a model for the literals in a clause holding boolean
 * variables.
 * 
 * @author Harshad Paradkar
 *
 */
public class Literal {

	/**
	 * The variable in this literal.
	 */
	public Variable variable;
	
	/**
	 * To indicate if this literal is a negation of it's variable.
	 */
	public boolean isNegated; 
			
	/**
	 * Construct a new Literal of the given variable.
	 * 
	 * @param variable	The variable of this literal.
	 * @param isNegated	If true: Variable is negated; false: otherwise.
	 */
	public Literal(Variable variable, boolean isNegated) {
		this.variable = variable;
		this.isNegated = isNegated;
	}
	
	/**
	 * @return true: If literal's value is TRUE; false: otherwise.
	 */
	public boolean getValue() {
		
		if(isNegated) {
			return !(variable.value);
		}else {
			return variable.value;
		}
	}
}
