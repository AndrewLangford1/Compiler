import java.util.ArrayList;


public class Parser {
	private ArrayList<Token> tokenStream;

	
	public Parser(ArrayList<Token> tokenStream){
		this.tokenStream = tokenStream;
	}
	
	
	//main parser function
	public void parse(){
		try{
		parseProgram();
		} catch(Exception ex){
			System.out.println(ex);
			System.out.println("error in main parse function");
			
		}
	}
	
	private void parseProgram(){
		try {
			parseBlock();
			
			//match EOF
			System.out.println("Expecting <$>");
			match("[$]");
			System.out.println("Ending Program");
			System.out.println("Parse Success!");
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing program");
		}
	}
	
	
	private void parseBlock(){
		try {
			
			
		System.out.println("Parsing Block..");
		//match opening brace
			System.out.println("Expecting <{>");
			match("\\{");
			
		//begin parsing statements, if any
			parseStatementList();
		//match closing brace
			System.out.println("Expecting <}>");
			match("\\}");
			
			
			System.out.println("Ending Block");
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
							return;
						 }
					 }
					 
					 
					 parseStatementList();
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
			}
			else{
				//ran out of tokens prematurely. kill parse.
				prematureEndOfFile();
				
			}
			
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing statemnt ");
		}
	}
	
	
//Parses print statements ie print(Expr)
	private void parsePrintStatement(){
		try {
			
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
			
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing print statement");
		}
	}
	
//Parse Assignments
	private void parseAssignmentStatement(){
		try {
			
			//match
			parseId();
			
			System.out.println("Expecting <=>");
			//match assignment character
			match("=");
			
			//parse the expression this id is being assigned to
			parseExpression();
		
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing assignment statement");
		}
	}
	
	
	//VARDECL
	private void parseVariableDeclaration(){
		try {
			
			
			System.out.println("Expecting <type>");
			//match a type for this variable
			match("int|string|boolean");
			
			//parse the ID for this vardecl
			parseId();
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing var decl");
		}
	}
	
//Parse WHILE BLOCKS
	private void parseWhileStatement(){
		try {
			
			System.out.println("Expecting <while>");
			//match while keyword
			match("while");
			
			//parse boolean expr
			parseBooleanExpression();
			
			//parse body of the while loop
			parseBlock();
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing while statment");
		}
	}
	
//IF STATEMENT
	private void parseIfStatement(){
		try {
			
			System.out.println("Expecting <if>");
			//match if keyword
			match("if");
			
			//parse boolean expr
			parseBooleanExpression();
			
			//parse body of the if statememt
			parseBlock();
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing if statemt");
		}
	}
	
//Parses expressions
	private void parseExpression(){
		try {
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
		//match a digit
			System.out.println("Expecting <digit>");
			match("[0-9]");
			if(hasNextToken()){
			
				//parse integer operation
				if(nextToken().getValue().matches("\\+")){
					parseIntegerOperation();
					parseExpression();
				}
				else{
					return;
				}
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
			
			System.out.println("Expecting <quote>");
		//match double quotes
			match("\"");
			
			//parse charlist inside of the string
			parseCharlist();
			
			System.out.println("Expecting <quote>");
			//match end double quotes
			match("\"");
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing string expr");
		}
	}
	
	
	//parse a boolean expression
	private void parseBooleanExpression(){
		try {
			
			if(hasNextToken()){
				//grab next token
				String nextToken = nextToken().getValue();
				
				System.out.println("matching" + nextToken);
				//match boolean values
				
				if(nextToken.matches("false|true")){
					match("false|true");
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
			
			//single chars denote ids hopefully
			System.out.println("Expecting <id>");
			match("[a-z]");
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing id");
		}
	}
	
	
	//parses charlists which are contained inside strings
	private void parseCharlist(){
		try {
			
			if(hasNextToken()){
				//grab next token
				String nextToken = nextToken().getValue();
				
				//only chars and spaces allowed inside charlists
				if(nextToken.matches("[a-z]|[ ]")){
					
					System.out.println("Expecting <char> or <space>");
					//match char or whitespace
					match("[a-z]|[ ]");
					
					//continue parsing the charlist....
					parseCharlist();
				}
			
				//until we match an end quote.
				if(nextToken.matches("\"")){
						return;
				}
				else{
					//epcelon production
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
			
			//match either boolop equals? or doesnt equal?
			System.out.println("Expecting <booleanOp>");
			match("==|!=");
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing bool op");
		}
	}
	
	private void parseIntegerOperation(){
		try {
			
			//match the integer operation
			System.out.println("Expecting <+>");
			match("\\+");
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
				
				//get value of the current token
				String tokenVal = nextToken().getValue();
				
				
				if(tokenVal.matches(toMatch)){
					System.out.println("Got " + tokenVal);
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
//TODO this function should pass consumed token to a CST and a symbol table.
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
}
