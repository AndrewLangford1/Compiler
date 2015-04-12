package semanticAnalysis;

import dataStructures.AbstractSyntaxTree;
import dataStructures.SymbolTable;
import dataStructures.Tree;

public class SemanticAnalyzer {
	
	
//--Fields--//
	private AbstractSyntaxTree abstractSyntaxTree;
	private SymbolTable symbolTable;
		
//-Constructors--//
	
	/*
	 * Default constructor
	 * @params concreteSyntaxTree the cst generated during parse
	 * 
	 */
	public SemanticAnalyzer(Tree concreteSyntaxTree){
		System.out.println("\n---> Beginning Semantic Analysis");
		this.abstractSyntaxTree = new AbstractSyntaxTree(concreteSyntaxTree);
		this.symbolTable = generateSymbolTable();
	}
	
	public void analyze(){
	
		
		//print the symbol table
		System.out.println("\n---> Symbol Table ");
		symbolTable.printAsTree();
		
		//perform scope and type checking
		
		
		//print the AST
		System.out.println("\n---> Abstract SyntaxTree");
		abstractSyntaxTree.print();
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
	
	private void scopeCheck(){
		
		
	}
	
	/**
	 * @return the abstractSyntaxTree
	 */
	public AbstractSyntaxTree getAbstractSyntaxTree() {
		return abstractSyntaxTree;
	}
	
	

}
