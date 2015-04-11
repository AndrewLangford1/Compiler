package semanticAnalysis;

import dataStructures.AbstractSyntaxTree;
import dataStructures.SyntaxTree;

public class SemanticAnalyzer {
	
	
//--Fields--//
	private AbstractSyntaxTree abstractSyntaxTree;
	
//-Constructors--//
	
	/*
	 * Default constructor
	 * @params concreteSyntaxTree the cst generated during parse
	 * 
	 */
	public SemanticAnalyzer(SyntaxTree concreteSyntaxTree){
		this.abstractSyntaxTree = new AbstractSyntaxTree(concreteSyntaxTree);
		
	}

	/**
	 * @return the abstractSyntaxTree
	 */
	public AbstractSyntaxTree getAbstractSyntaxTree() {
		return abstractSyntaxTree;
	}
	
	private void generateSymbolTable(){
		
		
		
		
		
		
	}
	
	private void scopeCheck(){
		
		
	}
	
	

}
