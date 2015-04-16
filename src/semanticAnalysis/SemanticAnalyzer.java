package semanticAnalysis;

import java.util.ArrayList;

import dataStructures.AbstractSyntaxTree;
import dataStructures.Node;
import dataStructures.SymbolTable;
import dataStructures.Token;
import dataStructures.Tree;

public class SemanticAnalyzer {
	
	
//--Fields--//
	private AbstractSyntaxTree abstractSyntaxTree;
	private SymbolTable symbolTable;	
	private ArrayList<String> errorMessages;
		
//-Constructors--//
		
	/**
	 * Default constructor
	 * @param concreteSyntaxTree the cst generated during parse
	 * 
	 */
	public SemanticAnalyzer(Tree concreteSyntaxTree){
		System.out.println("\n---> Beginning Semantic Analysis");
		this.abstractSyntaxTree = new AbstractSyntaxTree(concreteSyntaxTree);
		this.symbolTable = generateSymbolTable();
		this.errorMessages = new ArrayList<String>();
	}
	
	
	/**
	 * 
	 * @return an AST if semantic analysis is successful, null otherwise.
	 */
	public AbstractSyntaxTree analyze(){
			
		//if we have no errors generating the symbol table, move on to scope and type checking.
		if(symbolTable.getErrorMessages().isEmpty()){
			
			scopeCheck();
			
			//if scope and type checking succeeds, print the data we found and return an AST
			if(errorMessages.isEmpty()){
			
				//print as a Tree
				System.out.println("\n---> Symbol Table Tree");
				symbolTable.printAsTree();
				
				//print as a HashTable
				System.out.println("\n---> Symbol Table Hash");
				symbolTable.printAsHash();
			
				//perform scope and type checking
	
				System.out.println("\n---> Semantic Analysis Successful");
				//successful, so print the AST
				System.out.println("\n---> Abstract SyntaxTree");
				abstractSyntaxTree.print();
				
				return abstractSyntaxTree;
			}
			
			//scope and type checking failed, so print the error messages and return null to kill compilation.
			else{
				printErrorMessages();
				System.out.println("Semantic Analysis failed.....");
				return null;
			}
		}
		
		//there was an error generating the symbol table, print error messages and return null to kill compilation.
		else{
			symbolTable.printErrorMessages();
			System.out.println("Semantic Analysis failed.....");
			return null;
		}
	}	
		
	
	/**
	 * Iterates over the AST and builds a SymbolTable
	 * 
	 */
	private SymbolTable generateSymbolTable(){
		System.out.println("Generating Symbol Table...");
		
		//create a blank symbol table
		SymbolTable symbolTable = new SymbolTable(abstractSyntaxTree);
		
		//build the symbol table
		symbolTable.init();
		
		return symbolTable;
	}
	
	
	/**
	 * Scope checks the entire program
	 */
	private void scopeCheck(){
		int currentScope = 0;
		scopeCheckStatements(this.abstractSyntaxTree.getRoot(), currentScope);
	}
	
	
	/**
	 * handles scope checking for statement lists.
	 * 
	 * @param currentAstNode the current node visiting on the AST	
	 * @param currentScope current scope marker
	 */
	private void scopeCheckStatements(Node currentAstNode, int currentScope){
		switch(currentAstNode.getValue()){
			case("Block"):{
				scopeHandleBlock(currentAstNode, currentScope);
			}
			
			break;
			
			case("WhileStatement"):{

	
			}
			
			break;
			
			case("IfStatement"):{
				
			}
			
			break;
			
			case("AssignmentStatement") : {

			}	
			
			break;
			
			case("PrintStatement") :{
	
			}
			
			break;
			
			case("+") :{
				
				
				
			}
			
			break;
			
			case("!=") :{
				
				
				
			}
			
			break;
			
			case("==") :{
				
				
				
			}
			
			default :{
				//do nothing
				
			}
		}
	}
	
	
	/**
	 * handles scope checking for a block.
	 * 
	 * @param blockNode the block node to evaluate
	 * @param currentScope current scope marker
	 */
	private void scopeHandleBlock(Node blockNode, int currentScope){
		//increment the scope because were entering a new block
		currentScope += 1;
		
		//iterate over each statement in this block
		for(Node x : blockNode.getChildren()){
			scopeCheckStatements(x, currentScope);
		}
		
		//decrement the scope because we finished all statements in this block
		currentScope --;
		
	}
	
	private void handleWhileStatement(){
		
		
		
		
		
		
	}
	
	private void handleIfStatement(Node ifNode, int currentScope){

		
	}
	
	private void handleAssignmentStatement(Node assignmentNode, int currentScope){
		
	}
	
	private void handlePrintStatement(int currentScope){
		
		
	}
		
		
	
	/**
	 * 
	 * @param scope the current scope node
	 * @param id the placeholder of the id
	 * @return true if the id is present in the scope, false otherwise
	 */
	private boolean scopeHasId(Node scope, String id){
		if(scope.hasSymbolEntry(id)){
			return true;
		}
		else{
			return false;
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
}
