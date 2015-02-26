import java.util.ArrayList;


public class Parser {
	private ArrayList<Token> tokenStream;
	private ArrayList<String> errorMessages;
	private ArrayList<String> warningMessages;
	
	public Parser(ArrayList<Token> tokenStream){
		this.tokenStream = tokenStream;
		this.errorMessages = new ArrayList<String>();
		this.warningMessages = new ArrayList<String>();
	}
	
	public void parse(){
		try{
		System.out.println("Parsing Source File ");
		parseProgram();
		System.out.println("Parsing Complete");
		} catch(Exception ex){
			System.out.println(ex);
			System.out.println("error in main parse function");
			
		}
	}
	
	private void parseProgram(){
		try {
			System.out.println("Parsing Program");
			parseBlock();
			match("$");	
			System.out.println("Parsing Program Complete");
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing program");
		}
	}
	
	
	private void parseBlock(){
		try {
			System.out.println("Parsing Block");
			match("\\{");
			parseStatementList();
			match("\\}");
			System.out.println("Parsing Block Complete ");
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing block");
		}
	}
	
	private void parseStatementList(){
		try {
			System.out.println("Parsing Statement List");
			parseStatement();
			parseStatementList();	
			System.out.println("Parsing Statement List Complete ");
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing statement list");
		}
	}
	
	
	private void parseStatement(){
		try {
			System.out.println("Parsing STMT ");
			String nextToken = nextToken().getIndicator();
			System.out.println("attempting to parse statement with token" + nextToken);
			switch(nextToken){
				case("PRINTSTATEMENT"):{
					parsePrintStatement();

				}
				
				case("ID"):{
					parseAssignmentStatement();
				}
				
				case("INTTYPE"):{
					parseVariableDeclaration();
					
				}
				
				case("STRINGTYPE"):{
					parseVariableDeclaration();
					
				}
				
				case("BOOLEANTYPE"):{
					parseVariableDeclaration();
					
				}
				
				case("WHILELOOP"):{
					parseWhileStatement();
					
				}
				
				case("IFSTATEMENT"):{
					parseIfStatement();
					
					
				}
				
				case("BLOCKLEFTCURLY"):{
					parseBlock();
				}
				
				default:{
					System.out.println("ERROR PARSING STATEMENT");
				}
			}
			
			System.out.println("Parsing STMT complete ");
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing statemnt ");
		}
	}
	
	private void parsePrintStatement(){
		try {
			System.out.println("Parsing print statement ");
			match("print");
			match("(");
			parseExpression();
			match(")");
			System.out.println("Parsing print statement complete ");
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing print statement");
		}
	}
	
	private void parseAssignmentStatement(){
		try {
			System.out.println("Parsing assignment statement");
			match("[a-z]");
			match("=");
			parseExpression();
			System.out.println("Parsing assignment complete ");
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing assignment statement");
		}
	}
	
	private void parseVariableDeclaration(){
		try {
			System.out.println("Parsing var decl");
			match("int|string|boolean");
			match("[a-z]");
			System.out.println("Parsing var devl complete ");
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing var decl");
		}
	}
	
	private void parseWhileStatement(){
		try {
			System.out.println("Parsing while statement ");
			match("while");
			parseBooleanExpression();
			parseBlock();
			System.out.println("Parsing while statement complete");
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing while statment");
		}
	}
	
	private void parseIfStatement(){
		try {
			System.out.println("Parsing if statement ");
			match("if");
			parseBooleanExpression();
			parseBlock();
			System.out.println("Parsing if statement complete");
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing if statemt");
		}
	}
	
	private void parseExpression(){
		try {
			System.out.println("Parsing EXPR");
			String nextToken = nextToken().getIndicator();
			switch(nextToken){
				case("DIGIT"):{
					parseIntExpression();
				
				}
				
				case("DOUBLEQUOTE"):{
					parseStringExpression();
					
				}
				
				case("LEFTPAREN"):{
					parseBooleanExpression();
					
				}
				
				case("ID"):{
					parseId();
				}
				
				default:{
					//TODO raise error
					System.out.println("ERROR PARSING EXPR");
				}
			}
			
			System.out.println("Parsing EXPR COMPLETE ");
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing expr");
		}
	}
	
	private void parseIntExpression(){
		try {
			System.out.println("Parsing int expr ");
			match("[0-9]");
			if(nextToken().getValue().matches("+")){
				parseIntegerOperation();
				parseExpression();
			}
			else{
				return;
			}
			System.out.println("Parsing int expr complete ");
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing int expr");
		}
	}
	
	private void parseStringExpression(){
		try {
			System.out.println("Parsing String expr ");
			match("\"");
			parseCharlist();
			match("\"");
			System.out.println("Parsing String expr completes");
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing string expr");
		}
	}
	
	private void parseBooleanExpression(){
		try {
			System.out.println("Parsing bool expr");
			String nextToken = nextToken().getValue();
			if(nextToken.matches("false|true")){
				match("false|true");
			}
			else{
			
				match("(");
				parseExpression();
				parseBooleanOperation();
				parseExpression();
				match(")");
			}
			System.out.println("Parsing bool expr complete");
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing bool expr");
		}
	}
	
	private void parseId(){
		try {
			System.out.println("Parsing ID");
			match("[a-z]");
			System.out.println("Parsing id complete");
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing id");
		}
	}
	
	private void parseCharlist(){
		try {
			System.out.println("Parsing charlist ");
			String nextToken = nextToken().getValue();
			if(nextToken.matches("[a-z]") | nextToken.matches("[ ]")){
				parseCharlist();
			}
			else{
				//epcelon production
				
			}
			
			System.out.println("Parsing charlist compelte");
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing charlist");
		}
	}
	
	private void parseBooleanOperation(){
		try {
			System.out.println("Parsing bool op");
			match("==|!=");
			System.out.println("Parsing bool op complete");
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing bool op");
		}
	}
	
	private void parseIntegerOperation(){
		try {
			System.out.println("Parsing int op ");
			match("+");
			System.out.println("Parsing int op complete ");
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error parsing int op");
		}
		
	}
	
	
	private void match(String toMatch){
	
		try {
			System.out.println("Expecting: " + toMatch);

			//if we have another token and match correctly, consume the token.
			if(hasNextToken()){
				String tokenVal = nextToken().getValue();
				System.out.println("Got: " + tokenVal);
				
				if(tokenVal.matches(toMatch)){
					System.out.println("Got a " + toMatch);
					consumeToken();
				}
				//else raise an error or warning.
				else{
					System.out.println("ERROR!");
					//TODO make an error/warning function and call it here.
				}
			}
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error matching ");
		}
	}
	
	private void consumeToken(){
		try {
			System.out.println("removing " + tokenStream.get(0).value);
			tokenStream.remove(0);
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error consuming ");
		}
	}
	
	private Token nextToken(){
		try {
			return tokenStream.get(0);
		} catch (Exception e) {
			 
			System.out.println(e);
			System.out.println("error getting next token ");
		}
		return null;
	}
	
	private boolean hasNextToken(){
		try {
			return !(tokenStream.isEmpty());
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("error checking next token ");
		}
		return false;
	}
}
