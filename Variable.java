//******************************************************************************
//
// File:	Variable.java
//
//******************************************************************************

/**
 * Class Variable acts as a model for holding the info about a boolean 
 * variable.
 * 
 * @author Harshad Paradkar
 *
 */
public class Variable {

	/**
	 * An integer valued name, using which this variable can be identified.
	 */
	public int name;

	/**
	 * The boolean value of this variable.
	 */
	public boolean value;

	/**
	 * Construct a variable with the given name. (Default value will be false)
	 * 
	 * @param name	The integer valued name of this variable to be constructed.
	 */
	public Variable(int name) {
		this.name = name;
	}

	/**
	 * Construct a variable with the given name, and value.
	 * 
	 * @param name	The integer valued name of this variable to be constructed.
	 * @param value	The boolean value of this variable to be constructed.
	 */
	public Variable(int name, boolean value) {
		this.name = name;
		this.value = value;
	}
}