package CodeGen;

import java.util.ArrayList;
import java.util.HashMap;

import dataStructures.AbstractSyntaxTree;
import dataStructures.Node;
import dataStructures.StaticTable;


/**
 * Class to generate program code
 * @author Andrew Langford
 *
 */
public class CodeGenerator {
		
	
	
	/**
	 * 
	 * Enum to represent 6502a opcodes
	 * @author Andrew Langford
	 *
	 */
	private enum OpCode{
		LOADACCWITHCONST("A9"), //Load the accumulator with a constant
		
		LOADACCFROMMEM("AD"), //Load the accumulator from memory
		
		STOREACC("8D"), //Store the accumulator in memory
		
		ADDWITHCARRY("6D"), //Add with carry 6D ADC ADC $0010 6D 10 00. Adds contents of an address to
		 						//the contents of the accumulator and keeps the result in the accumulator
		
		LOADXREGWITHCONST("A2"), //Load the X register with a constant
		
		LOADXREGFROMMEM("AE"), //Load the X register from memory
		
		LOADYREGWITHCONST("A0"), //Load the Y register with a constant
		
		LOADYREGFROMMEM("AC"), //Load the Y register from memory
		
		NOOP("EA"), //No Operation 
		
		BREAK("00"), //Break (which is really a system call)
		
		COMPAREMEMTOX("EC"), //Compare a byte in memory to the X reg.  Sets the Z (zero) flag if equal
		
		BRANCH("D0"), //Branch n bytes if Z flag = 0
		
		INCREMENT("EE"), //Increment the value of a byte
		
		SYSTEMCALL("FF"); //System Call
		
		private String opCode;
		
		private OpCode(String opCode){
			this.opCode = opCode;
		}
		
		public String getOpcode(){
			return this.opCode;
		}
	}
	
	
//--Fields--//
	
		//the AST to generate code from
		private AbstractSyntaxTree ast;
		
		private int scopeCounter;
		
		private int currentByte;
		
		private int[] codeTable;
		
		private StaticTable staticTable;
		
		private HashMap<String, Integer> jumpTable;
	
	
	
//--Constructors--//
	
	/**
	 * Main Constructor
	 * @param ast the ast to generate code from
	 */
	public CodeGenerator(AbstractSyntaxTree ast){
		this.ast = ast;
		this.scopeCounter = 0;
		this.currentByte = 0;	
		this.codeTable = new int[256];
		this.staticTable = new StaticTable();
		this.jumpTable = new HashMap<String, Integer>();
	}
	
	
	/**
	 * Initializes Program code generation
	 */
	public void generateProgramCode(){
		generateStatementCode(ast.getRoot());
	}
	
	/**
	 * Generates the symbol table by iterating over an AST
	 * @param currentAstNode the current node we are visiting on the AST
	 * 
	 */
	private void generateStatementCode(Node currentAstNode){
		
		
		switch(currentAstNode.getValue()){
			case("Block"):{
				System.out.println("Generating Code for a block");
				handleBlock(currentAstNode);
			}
			
			break;
		
			case("VarDecl"):{
				System.out.println("Generating Code for a Variable Declaration");
				handleVarDecl(currentAstNode);
			}
			
			break;
			
			case("WhileStatement"):{
				System.out.println("Generating Code for a While Statement");
				//pass the while statement's block to this function 
				handleWhileStatement(currentAstNode);
			}
			
			break;
			
			case("IfStatement"):{
				System.out.println("Generating Code for an If Statement");
				//pass the if statmenen'ts block to this function
				handleIfStatement(currentAstNode);
			}
			
			break;
			
			case("PrintStatement") :{
				System.out.println("Generating Code for a Print Statement");
				handlePrintStatement(currentAstNode);
				
				
			}
			
			break;
			
			case("AssignmentStatement") :{
				System.out.println("Generating Code for an Assignment Statement");
				handleAssignmentStatement(currentAstNode);
			}
			
			break;
			

			default :{
				
			}
		}
	}


	private void handleAssignmentStatement(Node currentAstNode) {
		// TODO Auto-generated method stub
		
	}


	private void handlePrintStatement(Node currentAstNode) {
		
		
	}


	private void handleIfStatement(Node currentAstNode) {
		// TODO Auto-generated method stub
		
	}


	private void handleWhileStatement(Node currentAstNode) {
		// TODO Auto-generated method stub
		
	}


	private void handleVarDecl(Node currentAstNode) {
		ArrayList<Node> children = currentAstNode.getChildren();
		Node type = children.get(0);
		Node identifier = children.get(1);
		switch(type.getValue()){
			case("int"):{
				
			}
			
			break;
			
			case("string"):{
				
				
			}
			
			case("boolean"):{
				
				
			}
			
			break;
			
			default:{
				
				
			}
		}
		
	}


	
	/**
	 * Generates code for each statement in the block.
	 * @param currentAstNode The block node
	 */
	private void handleBlock(Node currentAstNode) {
		
		scopeCounter += 1;
		
		for(Node node : currentAstNode.getChildren())
			this.generateStatementCode(node);
		
		System.out.println("Finished Generating code for this block");
	}
	
}
