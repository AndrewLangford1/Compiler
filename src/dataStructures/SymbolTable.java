package dataStructures;

import java.util.ArrayList;
import java.util.HashMap;
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
	
	private HashMap<Integer, Node> selfAsHash;
	
	private ArrayList<String> errorMessages;
	
	

//--Constructors--//	
	public SymbolTable(AbstractSyntaxTree ast){
		super();
		this.ast = ast;
		this.scopeCounter = 0;
		this.errorMessages = new ArrayList<String>();
		this.selfAsHash = new HashMap<Integer, Node>();
	}
	
//--Methods --//
	
	public void init(){
		//generate the symbol table
		semanticAnalyze(ast.getRoot());
		selfToHash();
		
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
	private String semanticAnalyze(Node currentAstNode){
		
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
				handleWhileStatement(currentAstNode);
			}
			
			break;
			
			case("IfStatement"):{
				//pass the if statmenen'ts block to this function
				handleIfStatement(currentAstNode);
			}
			
			break;
			
			case("PrintStatement") :{

				handlePrintStatement(currentAstNode);
				
				
			}
			
			break;
			
			case("AssignmentStatement") :{

				handleAssignmentStatement(currentAstNode);
			}
			
			break;
			
			
			/**
			 * If we get down here, have to do some matching thanks to my shitty AST design.
			 * this default case needs alot of work
			 */
			default :{
				
				if(currentAstNode.getValue().matches("[a-z]")){
					return handleIdentifier(currentAstNode, currentAstNode.getToken());
	
				}
				
				if(currentAstNode.getValue().matches("[0-9]")){
					return "int";
				}
				
				if(currentAstNode.getValue().matches("true|false")){
					return "boolean";
					
					
				}
				
				if(currentAstNode.getValue().matches("\"([^\"]*)\"")){
					return "string";
				}
				
				if(currentAstNode.getValue().matches("\\+")){
					return handleAdditionOperation(currentAstNode);
				}
		
			
				if(currentAstNode.getValue().matches("!=")){
					return handleBoolOperation(currentAstNode);
				}
		
	
				if(currentAstNode.getValue().matches("==")){
					return handleBoolOperation(currentAstNode);
				}
				
				return null;
			}
		}
		return null;
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
		this.getCurrent().setScopeId(scopeCounter);
		
		//iterate over each statement in this block
		for(Node x : blockNode.getChildren()){
			semanticAnalyze(x);
		}
		
		//when finished checking statements in this block, return to parent block.
		returnToParent();
	}
	
	
	/**
	 * puts id's in the symbol table
	 * @param varDeclNode
	 */
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
			
			symbol.setLineDeclared(idNode.getToken().getLineNum());
			
			//ad the symbol entry to the table
			getCurrent().addSymbolEntry(idVal, symbol);
			
			System.out.println("Added <" + idVal +"> of type " + symbol.getType() + " to scope " + this.scopeCounter);
		}
		
		//otherwise, raise an already declared error and move on.
		else{
			Token idToken = idNode.getToken();
			
			//build and add the error message
			String alreadyDeclared = "ERROR: [Line " + idToken.getLineNum() + "] variable <" + idToken.getValue() + "> already declared for this scope ";
			this.errorMessages.add(alreadyDeclared);			
		}
	}
	
	/**
	 * checks the comparison and then analyzes the blocks statements
	 */
	private void handleWhileStatement(Node whileNode){
		//analyze condition
		semanticAnalyze(whileNode.getChildren().get(0));
		
		//analyze block of statement
		semanticAnalyze(whileNode.getChildren().get(1));
	}
	
	private void handleIfStatement(Node ifNode){
		//analyze condition
		semanticAnalyze(ifNode.getChildren().get(0));
				
		//analyze block of statement
		semanticAnalyze(ifNode.getChildren().get(1));
	}
	
	private void handlePrintStatement(Node printNode){
		//analyze the expr
		semanticAnalyze(printNode.getChildren().get(0));
	}
	
	
	/**
	 * handles assignment statement scope/type checking
	 * @param assignmentNode
	 */
	private void handleAssignmentStatement(Node assignmentNode){
		//analyze the id
		String idType = semanticAnalyze(assignmentNode.getChildren().get(0));
		
		
		//analyze the Expr
		 String exprType = semanticAnalyze(assignmentNode.getChildren().get(1));
		 
		 if(exprType != null){
			 
			 //if the types don't match, raise an error
			 if(!idType.matches(exprType)){
				 assignmentError(idType, exprType, assignmentNode.getToken().getLineNum()); 
			 }
			 
			 //if the types match, update the symbol table entry
			 else{
				Node idNode =  assignmentNode.getChildren().get(0);
				SymbolEntry entry = getIdentifierData(idNode.getValue());
				entry.setInitialized(true);
			 }
		 }	
	}
	
	private String handleAdditionOperation(Node additionNode){
		//analyze the left operand
		String leftNode = semanticAnalyze(additionNode.getChildren().get(0));
	
		//analyze the right operand
		String exprNode = semanticAnalyze(additionNode.getChildren().get(1));
		
		if(exprNode != null){
			//see if the data types match
			if(!leftNode.matches(exprNode)){
				intTopError(leftNode, exprNode, additionNode.getToken().getLineNum());
				
			}
			
		}
		
		return leftNode;
		
	}
	
	private String handleBoolOperation(Node boolopNode){
		//analyze the left operand
		String leftOperand = semanticAnalyze(boolopNode.getChildren().get(0));
		
		
		//analyze the right operand
		String rightOperand = semanticAnalyze(boolopNode.getChildren().get(1));
		
		if(rightOperand != null){
			//see if the data types match
			if(!leftOperand.matches(rightOperand)){
				this.booleanOpError(leftOperand, rightOperand, boolopNode.getToken().getLineNum());
			}	
		}
		
		return "boolean";
	}
	
	private String handleIdentifier(Node idNode, Token idToken){
		String idType =  handleUndeclaredIdentifiers(idNode.getValue(), idToken);
		return idType;
		
	}
	

	
	/**
	 * Checks to see if the ID is declared, and returns its type
	 * @param identifier
	 * @param idToken
	 * @return the type of the node.
	 */
	private String handleUndeclaredIdentifiers(String identifier, Token idToken){
		if(getCurrent().hasSymbolEntry(identifier)){
			//we found the id, so return its type
			SymbolEntry entry = getCurrent().getSymbolEntry(identifier);
			return entry.getType();
		}
		else{
			Node tempCurrent = getCurrent();
			while(tempCurrent.hasParent()){
				tempCurrent = tempCurrent.getParent();
				if(tempCurrent.hasSymbolEntry(identifier)){
					SymbolEntry entry = getCurrent().getSymbolEntry(identifier);
					return entry.getType();
				}	
			}
			
			//if we get here, then the Id was not found in any of it's parent's scopes, so it was undeclared
			buildUndeclaredIdentifierError(idToken);
			return null;
		}
	}
	
	
	/**
	 * 
	 * @param identifier
	 * @return the symbol entry associated with this identifier, or null if it wasnt found
	 */
	private SymbolEntry getIdentifierData(String identifier){
		if(getCurrent().hasSymbolEntry(identifier)){
			//we found the id, so need to do anything
			return getCurrent().getSymbolEntry(identifier);
			
			
		}
		else{
			Node tempCurrent = getCurrent();
			while(tempCurrent.hasParent()){
				tempCurrent = tempCurrent.getParent();
				if(tempCurrent.hasSymbolEntry(identifier)){
					//we found the id, so need to do anything
					return tempCurrent.getSymbolEntry(identifier);
				}	
			}
			return null;
		}
	}
	
	
	private void buildUndeclaredIdentifierError(Token token){
		String error = "ERROR: [Line : " + token.getLineNum() + "] undeclared identifier <" + token.getValue() + ">";
		this.errorMessages.add(error);
	}
	
	
	private void intTopError(String expected, String received, int lineNum){
		String error = "ERROR: [Line : " + lineNum + "] Expected a " + expected + " type, but received a " + received + " type. \n You cannot add these....";
		this.errorMessages.add(error);

	}
	
	private void booleanOpError(String expected, String received, int lineNum){
		String error = "ERROR: [Line : " + lineNum + "] Cannot compare " + expected + " with " + received;
		this.errorMessages.add(error);
	}
	
	private void assignmentError(String expected, String received, int lineNum){
		String error = "ERROR: [Line : " + lineNum + "] " + "Cannot assign " + received + " to id of type " + expected;
		this.errorMessages.add(error);
	}
	
		
	
	/**
	 * print the error messages.
	 */
	public void printErrorMessages(){
		for(String s : errorMessages){
			System.out.println(s);
		}
	}
	
	/**
	 * Turns the Tree Structure into a HashTable
	 * 
	 */
	private void selfToHash(){
		//make a blank hash
		HashMap<Integer, Node> symbolTableAsHash = new HashMap<Integer, Node>();
		
		//build the hash
		addHashEntry(this.getRoot(), symbolTableAsHash);
		
		this.selfAsHash = symbolTableAsHash;
	}
	
	/**
	 * Adds each hash entry to the symbol table hash
	 * @param currentSymbolTableNode current node in the symol table tree
	 * @param hash the HashMap we are currently building
	 */
	
	private void addHashEntry(Node currentSymbolTableNode, HashMap<Integer, Node> hash){
		//creates the hashtable entry
		int key = currentSymbolTableNode.getScopeId();
		Node value = currentSymbolTableNode;	
		hash.put(key, value);
		
		//recurse
		if(currentSymbolTableNode.getChildren().isEmpty()){
			return;
		}
		else{
			for(Node x : currentSymbolTableNode.getChildren()){
				addHashEntry(x, hash);
			}
		}
	}
	
	
	/**
	 * Simply calls printHashTable
	 */
	public void printAsHash(){
		printHashTable(this.selfAsHash);
	}
	
	
	
	/**
	 * Prints the symbol table out as a hashtable
	 * 
	 * @param hashTable the hashtable to print 
	 */
	private void printHashTable(HashMap<Integer, Node> hashTable){
		for(Integer x : hashTable.keySet()){
			Node toPrint = hashTable.get(x);
			for(String y : toPrint.getEntryData().keySet()){

				System.out.println("identifier    => " + y);
				toPrint.getEntryData().get(y).print();
				System.out.println("\n");
			}
		}
		
	}
	
	
	
	/**
	 * @return the selfAsHash
	 */
	public HashMap<Integer, Node> getSelfAsHash() {
		return selfAsHash;
	}
	
	public Node getSymbolNode(Integer key){
		return selfAsHash.get(key);
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
			
			System.out.println(toPrint+ node.getValue() + " " );
	
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
}
