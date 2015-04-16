/**
 * 
 */
package dataStructures;

/**
 * @author Andrew
 *
 *
 *Class Symbol Entry
 *
 *Basically just used as a placeholder for all entries in the Symbol Table
 */



public class SymbolEntry {
	
//--Fields--//
	private int scope;
	private boolean isDeclared;
	private int lineDeclared;
	private String type;
	private boolean isUsed;
	private boolean isInitialized;
	
	
	
	
//--Constructors --//
	public SymbolEntry(){
		this.scope = 0 ;
		this.isDeclared =false;
		this.lineDeclared = 0;
		this.type = null;
		this.isUsed = false;
		this.isInitialized = false;
		
	}
	
	public SymbolEntry(int scope, boolean isDeclared, int lineDeclared, String type, boolean isUsed, boolean isInitialized){
		this.scope = scope;
		this.isDeclared = isDeclared;
		this.lineDeclared = lineDeclared;
		this.type = type;
		this.isUsed = isUsed;
		this.isInitialized = isInitialized;
	}
	
//--Methods--//

	/**
	 * @return the scope
	 */
	public int getScope() {
		return scope;
	}

	/**
	 * @param scope the scope to set
	 */
	public void setScope(int scope) {
		this.scope = scope;
	}

	/**
	 * @return the isDeclared
	 */
	public boolean isDeclared() {
		return isDeclared;
	}

	/**
	 * @param isDeclared the isDeclared to set
	 */
	public void setDeclared(boolean isDeclared) {
		this.isDeclared = isDeclared;
	}

	/**
	 * @return the lineDeclared
	 */
	public int getLineDeclared() {
		return lineDeclared;
	}

	/**
	 * @param lineDeclared the lineDeclared to set
	 */
	public void setLineDeclared(int lineDeclared) {
		this.lineDeclared = lineDeclared;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the isUsed
	 */
	public boolean isUsed() {
		return isUsed;
	}

	/**
	 * @param isUsed the isUsed to set
	 */
	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}

	/**
	 * @return the isInitialized
	 */
	public boolean isInitialized() {
		return isInitialized;
	}

	/**
	 * @param isInitialized the isInitialized to set
	 */
	public void setInitialized(boolean isInitialized) {
		this.isInitialized = isInitialized;
	}

	/**
	 * Basically just attempts to print out the entry in a nice manner.
	 * 
	 */
	public void print(){
		System.out.println("Scope         => " + this.scope);
		System.out.println("Type  	      => " + this.type);
		System.out.println("isDeclared    => " + this.isDeclared);
		System.out.println("lineDeclared  => " + this.lineDeclared);
		System.out.println("isUsed        => " + this.isUsed);
		System.out.println("isInitialized => " + this.isInitialized);
	}
}
