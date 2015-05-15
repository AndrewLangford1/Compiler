
import java.util.ArrayList;




/*
 * Class Parser
 * parses input text and generates a CST to be passed on to Semantic Analysis
 * 
 * 
 */

public class Parser {
	private ArrayList<Token> tokenStream;
	
	//the concrete syntax tree
	private Tree cst;
	
	public Parser(ArrayList<Token> tokenStream){
		this.tokenStream = tokenStream;
		cst = new Tree();
		
	}
	
	
	//main parser function
	//Recursive descent parser
	//@return a concrete syntax tree or null if there was errors
	public Tree parse(){
		try{
			System.out.println("\n---> Parsing");
			
			//parse the program
			boolean parseSuccess = parseProgram();
			
			//if parse succeeds, return the CST it generated during the parse
			if(parseSuccess){
				
				System.out.println("\n---> Concrete Syntax Tree");
				
				//print the CST 
				cst.print();
				
				
				//return the cst
				return cst;
			}
			
		} catch(Exception ex){
			System.out.println(ex);
			System.out.println("error in main parse function");
		}
		
		//if parse fails or there was an error, return null
		return null;
	}
	
	private boolean parseProgram(){
		try {
			
			//set the root node of the cst
			cst.addBranchNode("Program");
			
			parseBlock();
			
			//match EOF
			System.out.println("Expecting <$>");
			match("[$]");
			if(hasNextToken()){
				this.codeAfterEOF();
				return false;
			}
			else{
				parseSuccess();
				return true;
			}
		
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing program");
		}
		
		return false;
	}
	
	
	private void parseBlock(){
		try {
			
		System.out.println("Parsing Block..");
		
		
		//add a branch node
		cst.addBranchNode("Block");
		
		
		//match opening brace
			System.out.println("Expecting <{>");
			match("\\{");
			
		//begin parsing statements, if any
			parseStatementList();
	
			System.out.println("Expecting <}>");
			
			//match closing brace
			match("\\}");
			
			
			System.out.println("Ending Block");
			
			//Return to parent node of the current branch node
			cst.returnToParent();
			
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing block");
		}
		
	
	}
	
	
	//This function runs checks against the following sets for Statement list. If a starting set of Statement is found, it parses a statement.
	//If none are found, its probably the end of a block. Or an error.
	private void parseStatementList(){
			try {
			
				
				if(hasNextToken()){
					
					//add a branch node to the cst
					cst.addBranchNode("StatementList");
					
						//grab the next tokens type
					 String nextToken = nextToken().getIndicator();
					 
					 //basically just passes control onto parse statement if any elements of Statements starting set are found
					 switch(nextToken){
						 case("PRINTSTATEMENT"):{
							 parseStatement();
			
						 }
						 break;
			
						 case("ID"):{
							 parseStatement();
						 }
						 break;
			
						 case("INTTYPE"):{
							 parseStatement();
			
						 }
						 break;
			
						 case("STRINGTYPE"):{
							 parseStatement();
			
						 }
						 break;
			
						 case("BOOLEANTYPE"):{
							 parseStatement();
			
						 }
						 
						 break;
			
						 case("WHILELOOP"):{
							 parseStatement();
			
						 }
						 
						 break;
			
						 case("IFSTATEMENT"):{
							 parseStatement();
			
						 }
						 
						 break;
			
						 case("BLOCKLEFTCURLY"):{
							 parseStatement();
						 }
						 
						 break;
			
						 //if nothing is found, return to parse block
						 default:{
							
							 //TODO check if this causes error
							//Return to parent node of the current branch node
							cst.returnToParent();
							return;
						 }
					 }
					 
					 
					 parseStatementList();
					 
					//Return to parent node of the current branch node
					 cst.returnToParent();
				}
				else{
					//ran out of tokens prematurely. kill parse.
					prematureEndOfFile();
					
					
				}
			 

		 } catch (Exception e) {
			 System.out.println(e);
		 }
	}
	

	
	
	
	//Parses single statement, pretty self explanatory
	private void parseStatement(){
		System.out.println("Parsing Statement..");
		try {
			//add a branch node to the cst
			cst.addBranchNode("Statement");
			if(hasNextToken()){
				String nextToken = nextToken().getIndicator();
				switch(nextToken){
					case("PRINTSTATEMENT"):{
						System.out.println("Parsing Print Statement..");
						parsePrintStatement();
					}
					
					break;
					
					case("ID"):{
						System.out.println("Parsing Assignment Statement..");
						parseAssignmentStatement();
					}
					
					break;
					
					case("INTTYPE"):{
						System.out.println("Parsing VarDecl..");
						parseVariableDeclaration();
						
					}
					
					break;
					
					case("STRINGTYPE"):{
						System.out.println("Parsing VarDecl..");
						parseVariableDeclaration();
						
					}
					
					break;
					
					case("BOOLEANTYPE"):{
						System.out.println("Parsing VarDecl..");
						parseVariableDeclaration();
						
					}
					
					break;
					
					case("WHILELOOP"):{
						System.out.println("Parsing While Statement..");
						parseWhileStatement();
						
					}
					
					break;
					
					case("IFSTATEMENT"):{
						System.out.println("Parsing If Statement..");
						parseIfStatement();
					}
					
					break;
					
					case("BLOCKLEFTCURLY"):{
						parseBlock();
					}
					
					break;
					
					default:{
						
						
						
					}
				}
				System.out.println("Ending Statement");
				
				//Return to parent node of the current branch node
				cst.returnToParent();
			}
			else{
				//ran out of tokens prematurely. kill parse.
				prematureEndOfFile();
				
			}
			
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing statement ");
		}
	}
	
	
//Parses print statements ie print(Expr)
	private void parsePrintStatement(){
		try {
			//add a branch node to the cst
			cst.addBranchNode("PrintStatement");
			
			System.out.println("Expecting <print>");
			match("print");
			
			System.out.println("Expecting <(> ");
			//match left paren
			match("\\(");
			
			//parse expression inside print statement
			parseExpression();
			
			System.out.println("Expecting <)>");
			//match right paren
			match("\\)");
			
			
			//Return to parent node of the current branch node
			cst.returnToParent();
			
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing print statement");
		}
	}
	
//Parse Assignments
	private void parseAssignmentStatement(){
		try {
			
			
			//add a branch node to the cst
			cst.addBranchNode("AssignmentStatement");
			
			//match
			parseId();
			
			System.out.println("Expecting <=>");
			//match assignment character
			match("=");
			
			//parse the expression this id is being assigned to
			parseExpression();
			
			
			//Return to parent node of the current branch node
			cst.returnToParent();
		
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing assignment statement");
		}
	}
	
	
	//VARDECL
	private void parseVariableDeclaration(){
		try {
			
			
			//add a branch node to the cst
			cst.addBranchNode("VarDecl");
			
			System.out.println("Expecting <type>");
		
			
			//add a branch node to the cst
			cst.addBranchNode("type");
			
			//match a type for this variable
			match("int|string|boolean");
			
			//Return to parent node of the current branch node
			cst.returnToParent();
			
			//parse the ID for this vardecl
			parseId();
			
			//Return to parent node of the current branch node
			cst.returnToParent();
			
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing var decl");
		}
	}
	
//Parse WHILE BLOCKS
	private void parseWhileStatement(){
		try {
			
			//add a branch node to the cst
			cst.addBranchNode("WhileStatement");
			
			System.out.println("Expecting <while>");
			//match while keyword
			match("while");
			
			//parse boolean expr
			parseBooleanExpression();
			
			//parse body of the while loop
			parseBlock();
			
			//Return to parent node of the current branch node
			cst.returnToParent();
			
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing while statment");
		}
	}
	
//IF STATEMENT
	private void parseIfStatement(){
		try {
			
			//add a branch node to the cst
			cst.addBranchNode("IfStatement");
			
			
			System.out.println("Expecting <if>");
			
			
			//match if keyword
			match("if");
			
			//parse boolean expr
			parseBooleanExpression();
			
			//parse body of the if statememt
			parseBlock();
			
			
			//Return to parent node of the current branch node
			cst.returnToParent();
			
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing if statement");
		}
	}
	
//Parses expressions
	private void parseExpression(){
		try {
			
			//add a branch node to the cst
			cst.addBranchNode("Expr");
			
			
			if(hasNextToken()){
				//grab current token
					String nextToken = nextToken().getIndicator();
					
					//figure out what type of expression it is
					switch(nextToken){
					
					//if digit, we (hope) it is an integer expression
						case("DIGIT"):{
							System.out.println("Parsing Integer Expression");
							parseIntExpression();
						
						} 
						
						break;
						
						//if a double quote, we hope it is a string expr
						case("DOUBLEQUOTE"):{
							System.out.println("Parsing String Expression");
							parseStringExpression();
							
						}
						
						break;
						
						
						//either of these could be a boolean expr
						case("LEFTPAREN"):{
							System.out.println("Parsing Boolean Expression");
							parseBooleanExpression();
							
						}
						break;
						
						case("BOOLEANVALUE"):{
							System.out.println("Parsing Boolean Expression");
							parseBooleanExpression();
						}
					
						
						break;
						
						//could be just an ID by itself
						case("ID"):{
							parseId();
						}
						
						break;
						
						default:{
							invalidExpression(nextToken());
					
						}
					}
					
					
					//Return to parent node of the current branch node
					cst.returnToParent();
			}
			else{
				//ran out of tokens prematurely. kill parse.
				prematureEndOfFile();
				
				
			}
			
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing expr");
		}
	}
	
	
	//parse an integer expression
	private void parseIntExpression(){
		try {
			
			//add a branch node to the cst
			cst.addBranchNode("IntExpr");
			
			
		
			System.out.println("Expecting <digit>");
			
			//add a branch node to the cst
			cst.addBranchNode("digit");
			
			
			//match a digit
			match("[0-9]");
			
			
			//Return to parent node of the current branch node
			cst.returnToParent();
			
			if(hasNextToken()){
			
				//parse integer operation
				if(nextToken().getValue().matches("\\+")){
					parseIntegerOperation();
					parseExpression();
				}
				else{
					
					//Return to parent node of the current branch node
					cst.returnToParent();
					
					return;
				}
				
				
				//Return to parent node of the current branch node
				cst.returnToParent();
			}
			else{
				//ran out of tokens prematurely. kill parse.
				prematureEndOfFile();
				
			}
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing int expr");
		}
	}
	
	
	//parse a string expression
	private void parseStringExpression(){
		try {
			
			//add a branch node to the cst
			cst.addBranchNode("StringExpr");
			
			System.out.println("Expecting <quote>");
		//match double quotes
			match("\"");
			
			//parse charlist inside of the string
			parseCharlist();
			
			System.out.println("Expecting <quote>");
			//match end double quotes
			match("\"");
			
			
			//Return to parent node of the current branch node
			cst.returnToParent();
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing string expr");
		}
	}
	
	
	//parse a boolean expression
	private void parseBooleanExpression(){
		try {
			
			//add a branch node to the cst
			cst.addBranchNode("BooleanExpr");
			
			if(hasNextToken()){
				//grab next token
				String nextToken = nextToken().getValue();
				
				System.out.println("matching" + nextToken);
				//match boolean values
				
				
				if(nextToken.matches("false|true")){
					cst.addBranchNode("boolval");
						match("false|true");
					cst.returnToParent();
				}
				else{
				//match (Expr boolop Expr)
					System.out.println("Expecting <(>");
					match("\\(");
					parseExpression();
					parseBooleanOperation();
					parseExpression();
					System.out.println("Expecting <)>");
					match("\\)");
				}
				
				//Return to parent node of the current branch node
				cst.returnToParent();
			}	
			else{
				//ran out of tokens prematurely. kill parse.
				prematureEndOfFile();
				
			}
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing bool expr");
		}
	}
	
	
	//parses a single id
	private void parseId(){
		try {
			
			//add a branch node to the cst
			cst.addBranchNode("Id");
			
			
			//add a branch node to the cst
			cst.addBranchNode("char");
			
			
			System.out.println("Expecting <id>");
			
			
			//single chars denote ids hopefully
			match("[a-z]");
			
			//Return to parent node of the current branch node
			cst.returnToParent();
			
			
			//Return to parent node of the current branch node
			cst.returnToParent();
			
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing id");
		}
	}
	
	
	//parses charlists which are contained inside strings
	private void parseCharlist(){
		try {
			
			
			//add a branch node to the cst
			cst.addBranchNode("CharList");
			
			if(hasNextToken()){
				//grab next token
				String nextToken = nextToken().getValue();
				
				//only chars and spaces allowed inside charlists
				if(nextToken.matches("[a-z]|[ ]")){
					
					System.out.println("Expecting <char> or <space>");
					
					//add a branch node to the cst
					cst.addBranchNode("char");
					
					//match char or whitespace
					match("[a-z]|[ ]");
					
					
					//Return to parent node of the current branch node
					cst.returnToParent();
					
					//continue parsing the charlist....
					parseCharlist();
				}
			
				//until we match an end quote.
				if(nextToken.matches("\"")){
					
					//Return to parent node of the current branch node
						cst.returnToParent();
						
						return;
				}
				else{
					//epcelon production
					//Return to parent node of the current branch node
					cst.returnToParent();
					return;
				}
			}
			else{
				//ran out of tokens prematurely. kill parse.
				prematureEndOfFile();
				
			}
				
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing charlist");
		}
	}
	
	
//parse a boolean operation
	private void parseBooleanOperation(){
		try {
			//add a branch node to the cst
			cst.addBranchNode("boolop");
			
			//match either boolop equals? or doesnt equal?
			System.out.println("Expecting <booleanOp>");
			match("==|!=");
			
			cst.returnToParent();
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing bool op");
		}
	}
	
	private void parseIntegerOperation(){
		try {
			//add a branch node to the cst
			cst.addBranchNode("intop");
			
			//match the integer operation
			System.out.println("Expecting <+>");
			match("\\+");
			cst.returnToParent();
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing int op");
		}
		
	}
	
	
//matches the current token against the provided regex.
	private void match(String toMatch){
	
		try {
			//if we have another token and match correctly, consume the token.
			if(hasNextToken()){
				
				Token nextToken = nextToken();
				
				//get value of the current token
				
				String tokenVal = nextToken.getValue();
				
				
				if(tokenVal.matches(toMatch)){
					System.out.println("Got " + tokenVal);
					
					//any matches are leaf nodes for the cst
					cst.addLeafNode(nextToken.toString(), nextToken);
					
					//consume the token
					consumeToken();
				}
				//else raise an error or warning.
				else{
					unexpectedTokenError();
				}
			}
			else{
				//ran out of tokens prematurely. kill parse.
				if(toMatch == "[$]"){
					System.out.println("Warning: Forgot <$, EOF> Insert this token!");
				}
				else{
					prematureEndOfFile();
				}
			}
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error matching ");
		}
	}
	
	
	
	

	
//consumes a token
	private void consumeToken(){
		try {
			tokenStream.remove(0);
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error consuming ");
		}
	}
	
	
//returns next token
	private Token nextToken(){
		try {
			if(hasNextToken()){
				return tokenStream.get(0);
			}
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error getting next token ");
		}
		return null;
	}
	
//returns true if there is another token in the token stream. returns false otherwise.
	private boolean hasNextToken(){
		try {
			return !(tokenStream.isEmpty());
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("error checking next token ");
		}
		return false;
	}
	
	//bad token error. print bad token and kill parsing.
	private void unexpectedTokenError(){
		System.out.println("[Line: " + nextToken().getLineNum() + "]" + "ERROR: Unexpected token " + nextToken().getValue());
		System.out.println("Parse Fail....");
		System.exit(1);
	}
	
	//ran out of tokens prematurely. kill parsing
	private void prematureEndOfFile(){
		System.out.println("ERROR: Premature end of File. Missing End Curly Brace");
		System.out.println("Parse Fail....");
		System.exit(1);
	}
	
	private void invalidExpression(Token token){
		System.out.println("ERROR: Invalid Expression");
		System.out.println("[Line: " + token.getLineNum() + "]" + "Unexpected token <" + token.getValue() + ">");
		System.out.println("Parse Fail....");
		System.exit(1);
	}
	
	
	//prints a warning if code was found after EOF
	private void codeAfterEOF(){
		System.out.println("WARNING: Found tokens after end of file. Removing these tokens");
		while(hasNextToken()){
			System.out.println("Removing: " + nextToken().toString());
			tokenStream.remove(0);
		}
		parseSuccess();
	}
	
	//prints out friendly messages if parsing was successful
	private void parseSuccess(){
		System.out.println("Ending Program");
		System.out.println("Parse Success!");
	}
	
}
