package dataStructures;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;


/**
 * 
 * @author Andrew
 *
 *
 *Class to handle the construction, structure, and access of the Symbol Table.
 */

public class SymbolTable extends Tree{

	
//--Fields--//
	private AbstractSyntaxTree ast;
	
	private int scopeCounter;
	
	private ArrayList<String> errorMessages;
	
	

//--Constructors--//	
	public SymbolTable(AbstractSyntaxTree ast){
		super();
		this.ast = ast;
		this.scopeCounter = 0;
		this.errorMessages = new ArrayList<String>();	
	}
	
//--Methods --//
	
	public void init(){
		//generate the symbol table
		generateSymbolTable(ast.getRoot());
	}
	
	
	
	/**
	 * @return the errorMessages
	 */
	public ArrayList<String> getErrorMessages() {
		return errorMessages;
	}

	/**
	 * Generates the symbol table by iterating over an AST
	 * @param currentAstNode the current node we are visiting on the AST
	 * 
	 */
	private void generateSymbolTable(Node currentAstNode){
		
		switch(currentAstNode.getValue()){
			case("Block"):{
				handleBlock(currentAstNode);
			}
			
			break;
		
			case("VarDecl"):{
				handleVarDecl(currentAstNode);
			}
			
			break;
			
			case("WhileStatement"):{
				
				//pass the while statement's block to this function 
				generateSymbolTable(currentAstNode.getChildren().get(1));
			}
			
			break;
			
			case("IfStatement"):{
				//pass the if statmenen'ts block to this function
				generateSymbolTable(currentAstNode.getChildren().get(1));
			}
			
			break;
			
			default :{
				//do nothing
				
			}
		}
	}
	
	/**
	 * initializes a new scope node for this block.
	 * 
	 * @param blockNode the block to parse
	 */
	
	private void handleBlock(Node blockNode){
		//increase the scope by one.
		scopeCounter += 1;
		System.out.println("Entering Scope " + scopeCounter);

		//add the node
		this.addBranchNode("Scope " + scopeCounter);
		
		//iterate over each statement in this block
		for(Node x : blockNode.getChildren()){
			generateSymbolTable(x);
		}
		
		//when finished checking statements in this block, return to parent block.
		returnToParent();
	}
	
	private void handleVarDecl(Node varDeclNode){
		System.out.println("Handling Variable Declaration");
		
		//grab the var decl children
		ArrayList<Node> children = varDeclNode.getChildren();
				
		//second node is the identifier itself
		Node idNode = children.get(1);
		
		//get the value of the identifier
		String idVal = idNode.getToken().getValue();
		
		//want to check if the the identifier is already declared in this scope.
		boolean isAlreadyDeclared = getCurrent().hasSymbolEntry(idVal);
		
		//if its not declared in this scope, go ahead and add it to the symbol table
		if(!isAlreadyDeclared){
			
			//build a symbol 
			SymbolEntry symbol = new SymbolEntry();
			
			//set the scope for this variable
			symbol.setScope(scopeCounter);
			
			//set declared to true
			symbol.setDeclared(true);
			
			//first node in a vardecl node is the data type for an identifier
			Node typeNode = children.get(0);

			symbol.setType(typeNode.getToken().getValue());
			
			//ad the symbol entry to the table
			getCurrent().addSymbolEntry(idVal, symbol);
			
			System.out.println("Added [" + idVal +"] of type " + symbol.getType() + " to scope " + this.scopeCounter);
		}
		
		//otherwise, raise an already declared error and move on.
		else{
			Token idToken = idNode.getToken();
			
			//build and add the error message
			String alreadyDeclared = "ERROR: [Line " + idToken.getLineNum() + "] variable [" + idToken.getValue() + "] already declared";
			this.errorMessages.add(alreadyDeclared);			
		}
	}
	
	
	
	/**
	 * Prints the symbol table out as a tree.
	 */
	public void printAsTree(){
		printLevel(this.getRoot(), 0);
	}
	
	/**
	 * prints the tree out in a nested tree fashion.
	 * @param node the node to start at
	 * @param level the level of the tree
	 */
	private void printLevel(Node node, int level){
		try {
			String toPrint = "";
			
			//set level of nesting
			for(int i =0; i<level; i++){
				toPrint += "  ";
			}
			
			//print the scope out
			System.out.println(toPrint+ node.getValue());
	
			//print out the variables declared in this scope
			for(Entry<String, SymbolEntry> entry  : node.getEntryData().entrySet()){
				System.out.println(toPrint+"  " + entry.getValue().getType() + " " +  entry.getKey());
			}
		
			
			//recurse
			if(node.getChildren().isEmpty()){
				return;
			}
			else{
				level += 1;
				for(Node x : node.getChildren()){
					this.printLevel(x, level);
				}
			}
		} catch (Exception e) {
			System.out.println("Error printing tree at level " + level + " at node " + node.getValue());
			e.printStackTrace();
		}
	}
	
	public void returnToParent(){
		super.returnToParent();
	}
}
