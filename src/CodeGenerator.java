

import java.util.ArrayList;
import java.util.HashMap;




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
		
		private String[] codeTable;
		
		private StaticTable staticTable;
		
		private HashMap<String, Integer> jumpTable;
		
		private final int MAXPROGRAMSIZE = 255;
	
	
	
//--Constructors--//
	
	/**
	 * Main Constructor
	 * @param ast the ast to generate code from
	 */
	public CodeGenerator(AbstractSyntaxTree ast){
		this.ast = ast;
		this.scopeCounter = 0;
		this.currentByte = 0;	
		this.codeTable = new String[255];
		for(int i =0; i <codeTable.length; i++){
			codeTable[i] = "00";
		}
		this.staticTable = new StaticTable();
		this.jumpTable = new HashMap<String, Integer>();
	}
	
	
	/**
	 * Initializes Program code generation and returns an executable image
	 * @return a string array containing the opcodes for the program we wish to run
	 */
	public String[] generateProgramCode(){
		generateStatementCode(ast.getRoot());
		return codeTable;
	}
	
	/**
	 * Generates code for each statement. Each Statement will return null, but any expression type things
	 * will return a string (usually reporting back to statement calls)
	 * @param currentAstNode the current node we are visiting on the AST
	 * @return A String if an expr is being evaluated, null otherwise.
	 * 
	 */
	private String generateStatementCode(Node currentAstNode){
		
		
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
				
				if(currentAstNode.getValue().matches("[a-z]")){
					System.out.println("Handling code gen for an identifier");
					return handleIdentifier(currentAstNode, currentAstNode.getToken());
	
				}
				
				if(currentAstNode.getValue().matches("[0-9]")){
					System.out.println("Handling code gen for a digit");
					return handleDigit(currentAstNode);
				}
				
				if(currentAstNode.getValue().matches("true|false")){
					System.out.println("Handling code gen for a boolean");
					return handleBoolVal(currentAstNode);
				}
				
				if(currentAstNode.getValue().matches("\"([^\"]*)\"")){
					System.out.println("Handling code gen for a string");
					return handleCharList(currentAstNode);
				}
				
				if(currentAstNode.getValue().matches("\\+")){
					System.out.println("Handling code gen for an integer operation");
					return handleAdditionOperation(currentAstNode);
				}
		
			
				if(currentAstNode.getValue().matches("!=")){
					System.out.println("Handling code gen for a boolean (not equals) expression");
					return handleBoolNE(currentAstNode);
				}
		
	
				if(currentAstNode.getValue().matches("==")){
					System.out.println("Handling code gen for a boolean (equals) expression");
					return handleBoolEq(currentAstNode);
				}				
			}
		}
		
		return null;
	}


	private String handleCharList(Node currentAstNode) {
		// TODO Auto-generated method stub
		return null;
	}


	private String handleDigit(Node currentAstNode) {
		addByte(OpCode.LOADACCWITHCONST.getOpcode());
		String integerAsHex = Integer.toHexString(Integer.valueOf(currentAstNode.getValue()));
		if(integerAsHex.length() < 2){
			integerAsHex = "0" + integerAsHex;
		}
		addByte(integerAsHex);
		return null;
	}


	private String handleBoolEq(Node currentAstNode) {
		// TODO Auto-generated method stub
		return null;
	}


	private String handleBoolNE(Node currentAstNode) {
		// TODO Auto-generated method stub
		return null;
	}


	private String handleBoolVal(Node currentAstNode) {
		if(currentAstNode.getValue().matches("true")){
			addByte(OpCode.LOADACCWITHCONST.getOpcode());
			addByte("01");	
		}
		
		if(currentAstNode.getValue().matches("false")){
			addByte(OpCode.LOADACCWITHCONST.getOpcode());
			addByte("00");	
		}
		return null;
	}


	private String handleAdditionOperation(Node currentAstNode) {
		ArrayList<Node> operands = currentAstNode.getChildren();
		/**
		 * this should load the accumulator with whatever the right operand contains
		 */
		generateStatementCode(operands.get(1));
		
	
		addByte(OpCode.ADDWITHCARRY.getOpcode());
		return null;
	}


	private String handleIdentifier(Node currentAstNode, Token token) {
		SymbolEntry idEntry  = currentAstNode.getSymbolTableData();
		String location = staticTable.getTempLocationFromVar(currentAstNode.getValue()+"@" + idEntry.getScope());
		addByte(OpCode.LOADACCFROMMEM.getOpcode());
		addByte(location);
		addByte("XX");
		return null;
	}


	private void handleAssignmentStatement(Node currentAstNode) {
		ArrayList<Node> children = currentAstNode.getChildren();
		Node identifier = children.get(0);
		SymbolEntry idEntry = identifier.getSymbolTableData();
		//get the temp location pointer from the static table
		String tempLocation = staticTable.getTempLocationFromVar(identifier.getValue() + "@" + idEntry.getScope());
		//get the entry from the temp location
		StaticTable.StaticEntry tableEntry = staticTable.getEntryFromTable(tempLocation);
		
		//get the expression we want to evaluate and assign to the variable.
		Node expr = children.get(1);
		generateStatementCode(expr);
		
		//Store the accumulator in memory
		addByte(OpCode.STOREACC.getOpcode());
		addByte(tempLocation);
		addByte("XX");
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
		SymbolEntry idEntry = identifier.getSymbolTableData();
		switch(type.getValue()){
			case("int"):{
				//load ACC with 00, all integers are initialized to 0
				addByte(OpCode.LOADACCWITHCONST.getOpcode());
				addByte("00");
				
				//Store the ACC in temp location, backpatching will happen later.
				addByte(OpCode.STOREACC.getOpcode());
				
				//create new entry in the static table.
				String temp = staticTable.addEntry(identifier.getValue(), idEntry.getScope(), staticTable.getSize(), "int");
				addByte(temp);
				addByte("XX");
			}
				
			break;
			
			case("string"):{
				//create new entry in the static table. Will fill in string value in the heap later on
				staticTable.addEntry(identifier.getValue(), idEntry.getScope(), staticTable.getSize(), "string");
		
			}
			
			case("boolean"):{
				//load ACC with 00, all integers are initialized to 0
				addByte(OpCode.LOADACCWITHCONST.getOpcode());
				addByte("00");
				
				//Store the ACC in temp location, backpatching will happen later.
				addByte(OpCode.STOREACC.getOpcode());
				
				//create new entry in the static table.
				String temp = staticTable.addEntry(identifier.getValue(), scopeCounter, staticTable.getSize(), "boolean");
				addByte(temp);
				addByte("XX");
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
	
	
	
	/**
	 * Add an operation to the code table, if we still have memory. Else, raise an error.
	 * @param operation
	 */
	private void addByte(String byteToAdd){
		System.out.println("Adding " + byteToAdd + " to the target file" );
		
		if(currentByte> MAXPROGRAMSIZE){
			codeOverFlow();
		}
		else{
			codeTable[currentByte] = byteToAdd;
			currentByte++;
		}
		
	}
	
	
	/**
	 * Error to raise when the program becomes to large to be executed.
	 */
	private void codeOverFlow(){
		System.out.println("This program is too large. Compilation failure");
		System.exit(0);
	}
	
}
