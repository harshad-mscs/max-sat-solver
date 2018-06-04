//******************************************************************************
//
// File:	VariableCount.java
//
//******************************************************************************

/**
 * Class VariableCount acts as a model for holding the info about a boolean 
 * variable, and the count of it's boolean values occurring during the
 * Count based Seesaw Search.
 * 
 * @author Harshad Paradkar
 *
 */
public class VariableCount {
	
	/**
	 * A reference to the variable for which boolean value count needs to ne
	 * stored.
	 */
	public Variable variable;
	
	/**
	 * The number of times this variables was TRUE.
	 */
	public int trueCount;
	
	/**
	 * The number of times this variable was FALSE.
	 */
	public int falseCount;
}