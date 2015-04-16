package semanticAnalysis;


import dataStructures.AbstractSyntaxTree;
import dataStructures.SymbolTable;		
import dataStructures.Tree;


			
/**
 * Class that basically just calls the symbol table class, which does all the heavy lifting for semantic analysis
 * @author Andrew
 *
 */
public class SemanticAnalyzer {
	
	
//--Fields--//
	private AbstractSyntaxTree abstractSyntaxTree;
	private SymbolTable symbolTable;	
		
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
	}	
	
	
	/**	
	 * 
	 * @return an AST if semantic analysis is successful, null otherwise.
	 */
	public AbstractSyntaxTree analyze(){
		//if we have no errors generating the symbol table, move on to scope and type checking.
		if(symbolTable.getErrorMessages().isEmpty()){
			
			//print warning messages if any
			if(!symbolTable.getWarningMessages().isEmpty()){
				System.out.println("\n---> WARNINGS");
				symbolTable.printWarningMessages();
			}
			
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
				//print warning messages if any
				if(!symbolTable.getErrorMessages().isEmpty()){
					System.out.println("\n---> WARNINGS");
					symbolTable.printWarningMessages();
				}
				System.out.println("\n---> ERRORS");
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
}
