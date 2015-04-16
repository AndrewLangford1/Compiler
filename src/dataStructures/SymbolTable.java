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
	private void semanticAnalyze(Node currentAstNode){
		
		switch(currentAstNode.getValue()){
			case("Block"):{
				System.out.println("Scope and Type Checking Block");
				handleBlock(currentAstNode);
			}
			
			break;
		
			case("VarDecl"):{
				handleVarDecl(currentAstNode);
			}
			
			break;
			
			case("WhileStatement"):{
				System.out.println("Scope and Type Checking While Statement");
				//pass the while statement's block to this function 
				handleWhileStatement(currentAstNode);
			}
			
			break;
			
			case("IfStatement"):{
				System.out.println("Scope and Type Checking If Statement");
				//pass the if statmenen'ts block to this function
				handleIfStatement(currentAstNode);
			}
			
			break;
			
			case("PrintStatement") :{
				System.out.println("Scope and Type Checking Print Statement");
				handlePrintStatement(currentAstNode);
				
				
			}
			
			break;
			
			case("AssignmentStatement") :{
				System.out.println("Scope and Type Checking Assignment Statement");
				handleAssignmentStatement(currentAstNode);
			}
			
			break;
			
			
			case("+") :{
				System.out.println("Scope and Type Checking Addition  Operation");
				handleAdditionOperation(currentAstNode);
				
				
			}
			
			break;
			
			case("!=") :{
				System.out.println("Scope and Type Checking Boolean != Operation");
				handleBoolOperation(currentAstNode);
				
				
			}
			
			break;
			
			case("==") :{
				System.out.println("Scope and Type Checking == Operation");
				handleBoolOperation(currentAstNode);
				
				
			}
			
			break;
			
			//If we get down here, have to do some matching thanks to my shitty AST design.
			default :{
				
				if(currentAstNode.getValue().matches("[a-z]")){
					handleIdentifier(currentAstNode, currentAstNode.getToken());
				}
				
				if(currentAstNode.getValue().matches("int|bool|string")){
					handleType(currentAstNode);
					
					
				}
				
				if(currentAstNode.getValue().matches("[0-9]")){
					handleDigit(currentAstNode);
					
					
				}
				
				if(currentAstNode.getValue().matches("true|false")){
					handleBoolVal(currentAstNode);
					
					
				}
				
				if(currentAstNode.getValue().matches("\"([^\"]*)\"")){
					handleCharList(currentAstNode);
					
				}
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
		this.getCurrent().setScopeId(scopeCounter);
		
		//iterate over each statement in this block
		for(Node x : blockNode.getChildren()){
			semanticAnalyze(x);
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
	
	private void handleAssignmentStatement(Node assignmentNode){
		//analyze the type
		semanticAnalyze(assignmentNode.getChildren().get(0));
		
		
		//analyze the id
		semanticAnalyze(assignmentNode.getChildren().get(1));

	}
	
	private void handleAdditionOperation(Node additionNode){
		//analyze the left operand
		semanticAnalyze(additionNode.getChildren().get(0));
		
		
		//analyze the right operand
		semanticAnalyze(additionNode.getChildren().get(1));
	}
	
	private void handleBoolOperation(Node boolopNode){
		//analyze the left operand
		semanticAnalyze(boolopNode.getChildren().get(0));
		
		
		//analyze the right operand
		semanticAnalyze(boolopNode.getChildren().get(1));
		
		
	}
	
	private void handleIdentifier(Node idNode, Token idToken){
		handleUndeclaredIdentifiers(idNode.getValue(), idToken);
		
		
	}
	
	private void handleType(Node typeNode){
		
		
	}
	
	private void handleBoolVal(Node boolValNode){
		
		
	}
	
	private void handleDigit(Node digitNode){
		
		
		
	}
	
	private void handleCharList(Node charListNode){
		
		
	}
	
	
	
	/**
	 * handles whether or not an ID is visible in the scope it was called in
	 * @param identifier the char variable of the identifier
	 */
	private void handleUndeclaredIdentifiers(String identifier, Token idToken){
		if(getCurrent().hasSymbolEntry(identifier)){
			//we found the id, so need to do anything
			return;
			
			
		}
		else{
			Node tempCurrent = getCurrent();
			while(tempCurrent.hasParent()){
				tempCurrent = tempCurrent.getParent();
				if(tempCurrent.hasSymbolEntry(identifier)){
					//we found the id, so need to do anything
					return;
				}	
			}
			
			//if we get here, then the Id was not found in any of it's parent's scopes, so it was undeclared
			buildUndeclaredIdentifierError(idToken);
		}
	}
	
	
	private void buildUndeclaredIdentifierError(Token token){
		String error = "ERROR: [Line : " + token.getLineNum() + "] undeclared identifier <" + token.getValue() + ">";
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
