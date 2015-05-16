

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
		
		//current pointer on the code table
		private int currentByte;
		
		//array of opcodes to be executed in the os
		private String[] codeTable;
		
		//static table
		private StaticTable staticTable;
		
		//jump table
		private HashMap<String, Integer> jumpTable;
		
		//maximum possible size of the program
		private final int MAXPROGRAMSIZE = 256;
		
		//heap pointer
		private int heapPointer;
		
		//using this to store values temporarily like a temp register
		private final String TEMPREGISTER = "FF";
	
	
	
//--Constructors--//
	
	/**
	 * Main Constructor
	 * @param ast the ast to generate code from
	 */
	public CodeGenerator(AbstractSyntaxTree ast){
		this.ast = ast;
		this.currentByte = 0;	
		this.codeTable = new String[MAXPROGRAMSIZE];
		for(int i =0; i <codeTable.length; i++){
			codeTable[i] = "00";
		}
		this.staticTable = new StaticTable();
		this.jumpTable = new HashMap<String, Integer>();
		this.heapPointer = 254;
	}
	
	
	/**
	 * Initializes Program code generation and returns an executable image
	 * @return a string array containing the opcodes for the program we wish to run
	 */
	public String[] generateProgramCode(){
		System.out.println("\n\n--->Beginning Code Generation\n");
		
		//generate code for the ast
		generateStatementCode(ast.getRoot());
		
		//add a break to the end of the code.
		addByte("00");
		
		//patch all temporary positions
		backPatchStatic();
		
		//patch all temporary jumps
		backPatchJumps();
		
		//return the program image
		return codeTable;
	}
	
	/**
	 * Fills all the temp locations with their actual positions in memory
	 */
	private void backPatchStatic() {
		for(int i =0; i < codeTable.length; i++){
			
			//have to check every cell for temp locations
			StaticTable.StaticEntry entry = staticTable.getEntryFromTable(codeTable[i]);
			if(entry!= null){
				
				//currentByte pointer will hold the first available address
				int staticPlacement = currentByte + entry.getOffset();
				String hexRepresentation = Integer.toHexString(staticPlacement);
				if(hexRepresentation.length()<2){
					hexRepresentation = "0" + hexRepresentation;
				}
				
				//replace the temporary location with the actual location
				codeTable[i] = hexRepresentation.toUpperCase();
				
				//replace XX bytes
				codeTable[i+1] = "00";
			}
		}
		
	}
	
	/**
	 * 
	 * @param stringToAdd the string we wish to add to the heap.
	 * @return the beginning of 
	 */
	private int addStringToHeap(String stringToAdd){
		//null terminate the string
		addByteToHeap("00");
		
		//add a byte for each character
		for(int i = stringToAdd.length()-1; i>=0; i--){
			
			//format it in hex
			char characterToWrite = stringToAdd.charAt(i);
			String hexRepresentation = String.format("%02x", (int) characterToWrite);
		
			//make sure its length 2
			if(hexRepresentation.length() <2){
				hexRepresentation = "0" + hexRepresentation;
			}
			
			//add the byte to the heap
			addByteToHeap(hexRepresentation.toUpperCase());
		}
		
		//return the address
		return heapPointer + 1;
	}
	
	
	/**
	 * patches temporary jump locations
	 */
	private void backPatchJumps(){
	for(int i =0; i < codeTable.length; i++){
			
			//have to check every cell for temp locations
			Integer location = jumpTable.get(codeTable[i]);
			if(location != null){
				
				//currentByte pointer will hold the first available address
				int placement = location;
				String hexRepresentation = Integer.toHexString(placement);
				if(hexRepresentation.length()<2){
					hexRepresentation = "0" + hexRepresentation;
				}
				
				//replace the temporary location with the actual location
				codeTable[i] = hexRepresentation.toUpperCase();
			}
		}
		
		
	}


	/**
	 * Generates code for each statement. Each Statement will return null, but any expression type things
	 * will return a string (usually reporting back to statement calls)
	 * @param currentAstNode the current node we are visiting on the AST
	 * @return A String if an expr is being evaluated, null otherwise.
	 * 
	 */
	private void generateStatementCode(Node currentAstNode){
	
		switch(currentAstNode.getValue()){
		
			//generate block code
			case("Block"):{
				System.out.println("Generating Code for a block");
				handleBlock(currentAstNode);
			}
			
			break;
		
			//generate variable declaration code
			case("VarDecl"):{
				System.out.println("Generating Code for a Variable Declaration");
				handleVarDecl(currentAstNode);
			}
			
			break;
			
			//generate while statement code
			case("WhileStatement"):{
				System.out.println("Generating Code for a While Statement");
				handleWhileStatement(currentAstNode);
			}
			
			break;
			
			//generate if statement code
			case("IfStatement"):{
				System.out.println("Generating Code for an If Statement");
				handleIfStatement(currentAstNode);
			}
			
			break;
			
			//generate print statement code
			case("PrintStatement") :{
				System.out.println("Generating Code for a Print Statement");
				handlePrintStatement(currentAstNode);
				
				
			}
			
			break;
			
			
			//generate assignment code
			case("AssignmentStatement") :{
				System.out.println("Generating Code for an Assignment Statement");
				handleAssignmentStatement(currentAstNode);
			}
			
			break;
			

			//if we get here, theres some expression to evaluate
			default :{
				
				//generate code for some identifier reference
				if(currentAstNode.getValue().matches("[a-z]")){
					System.out.println("Handling code gen for an identifier");
					handleIdentifier(currentAstNode);
	
				}
				
				//generate code for some digit reference
				if(currentAstNode.getValue().matches("[0-9]")){
					System.out.println("Handling code gen for a digit");
					handleDigit(currentAstNode);
				}
				
				//generate code for some boolean value reference
				if(currentAstNode.getValue().matches("true|false")){
					System.out.println("Handling code gen for a boolean");
					handleBoolVal(currentAstNode);
				}
				
				//generate code for some string reference
				if(currentAstNode.getValue().matches("\"([^\"]*)\"")){
					System.out.println("Handling code gen for a string");
					handleCharList(currentAstNode);
				}
				
				//generate code for an addition (intop) expression
				if(currentAstNode.getValue().matches("\\+")){
					System.out.println("Handling code gen for an integer operation");
					handleAdditionOperation(currentAstNode);
				}
		
					
				//generate code for a boolean not equals expression
				if(currentAstNode.getValue().matches("!=")){
					System.out.println("Handling code gen for a boolean (not equals) expression");
					handleBoolNE(currentAstNode);
				}
		
				//generate code for a boolean equals expression
				if(currentAstNode.getValue().matches("==")){
					System.out.println("Handling code gen for a boolean (equals) expression");
					handleBoolEq(currentAstNode);
				}				
			}
		}
	}


	/**
	 * allocates heap storage for a string and places its reference in the accumulator
	 * @param currentAstNode the node contatining the charlist
	 */
	private void handleCharList(Node currentAstNode) {
		//add the string to the heap
		String value= currentAstNode.getValue().replace("\"", "");
		int sLocation = addStringToHeap(value);
		
		//convert the location of the beginning of the string to hex
		String hexRepresentation = Integer.toHexString(sLocation);
		if(hexRepresentation.length() < 2){
			hexRepresentation = "0" + hexRepresentation;
		}
	
		//load the accumulator with the beggining of the string in memory (in the heap)
		addByte(OpCode.LOADACCWITHCONST.getOpcode());
		addByte(hexRepresentation.toUpperCase());
	}


	/**
	 * Converts the integer to a hex value and places the result in the accumulator
	 * @param currentAstNode
	 * @return
	 */
	private void handleDigit(Node currentAstNode) {
		//load the accumulator 
		addByte(OpCode.LOADACCWITHCONST.getOpcode());
		
		//build the hex representation
		String integerAsHex = Integer.toHexString(Integer.valueOf(currentAstNode.getValue()));
		if(integerAsHex.length() < 2){
			integerAsHex = "0" + integerAsHex;
		}
		//load the byte into the accumulator
		addByte(integerAsHex);
	}

	/**
	 * Handles a boolean equals expression
	 * @param currentAstNode the '==' node 
	 * 
	 */
	private void handleBoolEq(Node currentAstNode) {
		//get the left operand
		Node leftOperand = currentAstNode.getChildren().get(0);
		
		//get the right operand
		Node rightOperand = currentAstNode.getChildren().get(1);
		
		//didnt have time to do nested booleans
		if(rightOperand.getValue().matches("!=|==")){
			iDontLikeNestedBooleans();
		}
		
	///-----LEFT OPERAND ---//
		if(leftOperand.getValue().matches("[a-z]")){
	
			SymbolEntry entry = leftOperand.getSymbolTableData();
			String type = entry.getType();
				
			//print integer
			if(type.matches("int")){
				//get the symbol table entry for the identifier
				SymbolEntry idEntry  = leftOperand.getSymbolTableData();
						
				//get the temp location from the static table
				String location = staticTable.getTempLocationFromVar(leftOperand.getValue()+"@" + idEntry.getScope());
						
				addByte(OpCode.LOADXREGFROMMEM.getOpcode());
				addByte(location);
				addByte("XX");
				
			}
				
			if(type.matches("boolean")){
				//get the symbol table entry for the identifier
				SymbolEntry idEntry  = leftOperand.getSymbolTableData();
					
				//get the temp location from the static table
				String location = staticTable.getTempLocationFromVar(leftOperand.getValue()+"@" + idEntry.getScope());
					
				addByte(OpCode.LOADXREGFROMMEM.getOpcode());
				addByte(location);
				addByte("XX");
			
			}
				
			if(type.matches("string")){
				iDontLikeComparingStrings();
			}
		}
		
		//if true, load the x reg with true
		if(leftOperand.getValue().matches("true")){
			addByte(OpCode.LOADXREGWITHCONST.getOpcode());
			addByte("01");	
		}
			
		//if false, load x reg with false
		if(leftOperand.getValue().matches("false")){
			addByte(OpCode.LOADXREGWITHCONST.getOpcode());
			addByte("00");	
		}
		
		if(leftOperand.getValue().matches("[0-9]")){
			addByte(OpCode.LOADXREGWITHCONST.getOpcode());
			//build the hex representation
			String integerAsHex = Integer.toHexString(Integer.valueOf(leftOperand.getValue()));
			if(integerAsHex.length() < 2){
				integerAsHex = "0" + integerAsHex;
			}
			//load the byte into the accumulator
			addByte(integerAsHex);	
		}
		
		//-----RIGHT OPERAND
		if(rightOperand.getValue().matches("[a-z]")){
			
			SymbolEntry entry = rightOperand.getSymbolTableData();
			String type = entry.getType();
				
			//print integer
			if(type.matches("int")){
				//get the symbol table entry for the identifier
				SymbolEntry idEntry  = rightOperand.getSymbolTableData();
						
				//get the temp location from the static table
				String location = staticTable.getTempLocationFromVar(rightOperand.getValue()+"@" + idEntry.getScope());
						
				addByte(OpCode.COMPAREMEMTOX.getOpcode());
				addByte(location);
				addByte("XX");
				
			}
				
			if(type.matches("boolean")){
				//get the symbol table entry for the identifier
				SymbolEntry idEntry  = rightOperand.getSymbolTableData();
					
				//get the temp location from the static table
				String location = staticTable.getTempLocationFromVar(rightOperand.getValue()+"@" + idEntry.getScope());
					
				addByte(OpCode.COMPAREMEMTOX.getOpcode());
				addByte(location);
				addByte("XX");
			
			}
				
			if(type.matches("string")){
				iDontLikeComparingStrings();
			}
		}
		
		//if true, load the x reg with true
		if(rightOperand.getValue().matches("true")){
			addByte(OpCode.LOADACCWITHCONST.getOpcode());
			addByte("01");	
			addByte(OpCode.STOREACC.getOpcode());
			addByte(TEMPREGISTER);
			addByte("00");
			addByte(OpCode.COMPAREMEMTOX.getOpcode());
			addByte(TEMPREGISTER);
			addByte("00");
		}
			
		//if false, load x reg with false
		if(rightOperand.getValue().matches("false")){
			addByte(OpCode.LOADACCWITHCONST.getOpcode());
			addByte("00");
			addByte(OpCode.STOREACC.getOpcode());
			addByte(TEMPREGISTER);
			addByte("00");
			addByte(OpCode.COMPAREMEMTOX.getOpcode());
			addByte(TEMPREGISTER);
			addByte("00");
		}
		
		if(rightOperand.getValue().matches("[0-9]")){
			addByte(OpCode.LOADACCWITHCONST.getOpcode());
			//build the hex representation
			String integerAsHex = Integer.toHexString(Integer.valueOf(rightOperand.getValue()));
			if(integerAsHex.length() < 2){
				integerAsHex = "0" + integerAsHex;
			}
			//load the byte into the accumulator
			addByte(integerAsHex);
			addByte(OpCode.STOREACC.getOpcode());
			addByte(TEMPREGISTER);
			addByte("00");
			addByte(OpCode.COMPAREMEMTOX.getOpcode());
			addByte(TEMPREGISTER);
			addByte("00");
		}
		
		
	}


	/**
	 * Handles a boolean not equals expression
	 * @param currentAstNode the '!=' node
	 */
	private void handleBoolNE(Node currentAstNode) {
		notWorking();
	}


	/**
	 * Loads the accumulator with a boolean value
	 * 0 for false
	 * 1 for true
	 * @param currentAstNode the boolean value node
	 * 
	 */
	private void handleBoolVal(Node currentAstNode) {
		
		//if true, load the accumulator with 1
		if(currentAstNode.getValue().matches("true")){
			addByte(OpCode.LOADACCWITHCONST.getOpcode());
			addByte("01");	
		}
		
		//if false, load accumulator with 0
		if(currentAstNode.getValue().matches("false")){
			addByte(OpCode.LOADACCWITHCONST.getOpcode());
			addByte("00");	
		}
	}

	/**
	 * Handles intops and nested intops.
	 * @param currentAstNode the '+' node
	 */
	private void handleAdditionOperation(Node currentAstNode) {
		//get the left and right operands
		ArrayList<Node> operands = currentAstNode.getChildren();
		Node leftOperand = operands.get(0);
		Node rightOperand = operands.get(1);
		
		//load acc with whatever the right operand is
		generateStatementCode(rightOperand);
		
		//store the accumulator, going to use FF for a temp register
		addByte(OpCode.STOREACC.getOpcode());
		addByte(TEMPREGISTER);
		addByte("00");
		
		//load the accumulator with the leftmost operand (has to be a digit)
		handleDigit(leftOperand);
		
		//add the contents of the accumulator with the temporary register
		addByte(OpCode.ADDWITHCARRY.getOpcode());
		addByte(TEMPREGISTER);
		addByte("00");
	}

	/**
	 * Handles identifier references
	 * @param currentAstNode the identifier node
	 * @return
	 */
	private void handleIdentifier(Node currentAstNode) {
		//get the symbol table entry for the identifier
		SymbolEntry idEntry  = currentAstNode.getSymbolTableData();
		
		//get the temp location from the static table
		String location = staticTable.getTempLocationFromVar(currentAstNode.getValue()+"@" + idEntry.getScope());
		
		//load the accumulator from memory location of the variable
		addByte(OpCode.LOADACCFROMMEM.getOpcode());
		addByte(location);
		addByte("XX");
	}

	
	/**
	 * Handles assignment statements
	 * @param currentAstNode the '=' node
	 */
	private void handleAssignmentStatement(Node currentAstNode) {
		ArrayList<Node> children = currentAstNode.getChildren();
		
		//get the identifier node
		Node identifier = children.get(0);
		
		//get the symbol table entry for this identifier
		SymbolEntry idEntry = identifier.getSymbolTableData();
		
		//get the temp location pointer from the static table
		String tempLocation = staticTable.getTempLocationFromVar(identifier.getValue() + "@" + idEntry.getScope());
		
		//get the expression we want to evaluate and assign to the variable.
		Node expr = children.get(1);
		generateStatementCode(expr);
		
		//Store the accumulator in memory
		addByte(OpCode.STOREACC.getOpcode());
		addByte(tempLocation);
		addByte("XX");
	}


	/**
	 * Prints integers and strings to the console
	 * @param currentAstNode the print statement node
	 */
	private void handlePrintStatement(Node currentAstNode) {
		Node exprNode = currentAstNode.getChildren().get(0);
		
		
		//attempting to print a identifier
		if(exprNode.getValue().matches("[a-z]")){
			SymbolEntry entry = exprNode.getSymbolTableData();
			String type = entry.getType();
			
			//print integer
			if(type.matches("int")){
				//load y register with the contents of the address for this variable
				String temp = staticTable.getTempLocationFromVar(exprNode.getValue() + "@" + entry.getScope());
				addByte(OpCode.LOADYREGFROMMEM.getOpcode());
				addByte(temp);
				addByte("XX");
				
				//tell system were printing an int and then make system call
				addByte(OpCode.LOADXREGWITHCONST.getOpcode());
				addByte("01");
				addByte("FF");
			}
			
			if(type.matches("boolean")){
				//load y register with the contents of the address for this variable
				String temp = staticTable.getTempLocationFromVar(exprNode.getValue() + "@" + entry.getScope());
				addByte(OpCode.LOADYREGFROMMEM.getOpcode());
				addByte(temp);
				addByte("XX");
				
				//tell system were printing an int and then make system call
				addByte(OpCode.LOADXREGWITHCONST.getOpcode());
				addByte("01");
				addByte("FF");
			}
			
			//print string
			if(type.matches("string")){
				//load y register with the contents of the address for this variable
				String temp = staticTable.getTempLocationFromVar(exprNode.getValue() + "@" + entry.getScope());
				addByte(OpCode.LOADYREGFROMMEM.getOpcode());
				addByte(temp);
				addByte("XX");
				
				//tell system were printing a string and then make system call
				addByte(OpCode.LOADXREGWITHCONST.getOpcode());
				addByte("02");
				addByte("FF");
			}
		}
		
		
		//print a digit
		if(exprNode.getValue().matches("[0-9]")){
			//load the y reg with a constant
			addByte(OpCode.LOADYREGWITHCONST.getOpcode());
			
			//evaluate the digit to be printed
			String exprVal = exprNode.getValue();
			String hexRepresentation = Integer.toHexString(Integer.valueOf(exprVal));
			if(hexRepresentation.length()<2){
				hexRepresentation = "0" + hexRepresentation;
			}
			addByte(hexRepresentation.toUpperCase());
	
			addByte(OpCode.LOADXREGWITHCONST.getOpcode());
			addByte("01");
			addByte("FF");
		}
		
		//print a boolean value
		if(exprNode.getValue().matches("true")){
			//load y register with the contents of the address for this variable
			addByte(OpCode.LOADYREGWITHCONST.getOpcode());
			addByte("01");
			
			//tell system were printing an int and then make system call
			addByte(OpCode.LOADXREGWITHCONST.getOpcode());
			addByte("01");
			addByte("FF");
		}
		
		if(exprNode.getValue().matches("false")){
			//load y register with the contents of the address for this variable
			addByte(OpCode.LOADYREGWITHCONST.getOpcode());
			addByte("00");
			
			//tell system were printing an int and then make system call
			addByte(OpCode.LOADXREGWITHCONST.getOpcode());
			addByte("01");
			addByte("FF");
		}
		
		//print a string
		if(exprNode.getValue().matches("\"([^\"]*)\"")){
			handleCharList(exprNode);
			addByte(OpCode.STOREACC.getOpcode());
			addByte(TEMPREGISTER);
			addByte("00");
			
			//load y from the temp register
			addByte(OpCode.LOADYREGFROMMEM.getOpcode());
			addByte(TEMPREGISTER);
			addByte("00");
			
			//tell system were printing a string and then make system call
			addByte(OpCode.LOADXREGWITHCONST.getOpcode());
			addByte("02");
			addByte("FF");
		
		}
		
		//print an addition statement
		if(exprNode.getValue().matches("\\+")){
			//should perform addition, leaving the value to be printed in the accumulator
			handleAdditionOperation(exprNode);
			//store accumulator in a temp location
			addByte(OpCode.STOREACC.getOpcode());
			addByte(TEMPREGISTER);
			addByte("00");
			addByte(OpCode.LOADYREGFROMMEM.getOpcode());
			addByte(TEMPREGISTER);
			addByte("00");
			
			addByte(OpCode.LOADXREGWITHCONST.getOpcode());
			addByte("01");
			addByte("FF");
	
		}

		
		//print the result of a boolean not equals statement
		if(exprNode.getValue().matches("!=")){
			notWorking();
	
		}

		
		//print the result of a boolean equals statement
		if(exprNode.getValue().matches("==")){
	
		}
	}

	
	/**
	 * Handles if statements
	 * @param currentAstNode the if statement node
	 */
	private void handleIfStatement(Node currentAstNode) {
		//handle the conidtional
		generateStatementCode(currentAstNode.getChildren().get(0));
		//add a temp location for shits and gigs
		String jumpLocation = "J"+jumpTable.size();
		
		//add the branch statement
		addByte(OpCode.BRANCH.getOpcode());
		addByte(jumpLocation);
		
		//get the location before the block nonsense occurs
		int beforeLocation = currentByte;
		generateStatementCode(currentAstNode.getChildren().get(1));
		
		//get the location after all that shit occurs
		int afterLocation = currentByte;
		
		//store that location for the jump table to deal with
		jumpTable.put(jumpLocation, afterLocation - beforeLocation);	
	}

	
	/**
	 * Handles while statements
	 * @param currentAstNode the while statement node
	 */
	private void handleWhileStatement(Node currentAstNode) {
		//store the location just before the while statement so we can branch here 
		int loopLocation= currentByte;
		
		
		//generate code for the branching
		generateStatementCode(currentAstNode.getChildren().get(0));
	
		addByte(OpCode.BRANCH.getOpcode());
		generateStatementCode(currentAstNode.getChildren().get(1));
		
	}


	
	/**
	 * Generates code for variable declarations
	 * @param currentAstNode the vardecl node
	 */
	private void handleVarDecl(Node currentAstNode) {
		ArrayList<Node> children = currentAstNode.getChildren();
		
		//get the type of this identifer
		Node type = children.get(0);
		
		
		//get identifier node
		Node identifier = children.get(1);
		SymbolEntry idEntry = identifier.getSymbolTableData();
		
		switch(type.getValue()){
		
		//its an int
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
			
			//its a string
			case("string"):{
				//load ACC with 00
				addByte(OpCode.LOADACCWITHCONST.getOpcode());
				addByte("00");
				//Store the ACC in temp location, backpatching will happen later.
				addByte(OpCode.STOREACC.getOpcode());
				
				//create new entry in the static table. Will fill in string location in the heap later on
				String temp = staticTable.addEntry(identifier.getValue(), idEntry.getScope(), staticTable.getSize(), "string");
				addByte(temp);
				addByte("XX");
		
			}
			
			break;
			
			
			//its a boolean 
			case("boolean"):{
				//load ACC with 00, all booleans are initialized to 0
				addByte(OpCode.LOADACCWITHCONST.getOpcode());
				addByte("00");
				
				//Store the ACC in temp location, backpatching will happen later.
				addByte(OpCode.STOREACC.getOpcode());
				
				//create new entry in the static table.
				String temp = staticTable.addEntry(identifier.getValue(), idEntry.getScope(), staticTable.getSize(), "boolean");
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
		
		//generate code for each statement node
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
		
		//make sure the code table doesnt go over 256 bytes or heap storage starts overwriting op codes
		if(currentByte>=MAXPROGRAMSIZE || currentByte>= heapPointer){
			codeOverFlow();
		}
		//if there isnt a problem, add the byte to the code table
		else{
			codeTable[currentByte] = byteToAdd;
			currentByte++;
		}
		
	}
	
	/**
	 * Adds a byte to the heap
	 * @param byteToAdd te byte we want to add to the heap
	 */
	private void addByteToHeap(String byteToAdd){
		System.out.println("Adding " + byteToAdd + "to the heap");
		
		//make sure the code table doesnt go over 256 bytes or heap storage starts overwriting op codes
		//raise an error if so
		if(heapPointer <= currentByte || heapPointer <= 0){
			codeOverFlow();
		}
		//if there isnt a problem, add the byte to the heap
		else{
			codeTable[heapPointer] = byteToAdd;
			heapPointer --;
		}
		
	}
	
	
	/**
	 * Error to raise when the program becomes to large to be executed.
	 * Exits gracefully
	 */
	private void codeOverFlow(){
		System.out.println("This program is too large. Compilation failure");
		System.exit(0);
	}
	
	/**
	 * Couldnt get nested booleans working in the time frame.
	 */
	private void iDontLikeNestedBooleans(){
		System.out.println("Sorry, nested booleans are a bitch.");
		System.exit(0);
	}
	
	private void iDontLikeComparingStrings(){
		System.out.println("Sorry, comparing strings is nauseating.");
		System.exit(0);
	}
	
	private void notWorking(){
		System.out.println("Unfortunately boolean not equals isnt working");
		System.exit(0);
		
	}
	
}
