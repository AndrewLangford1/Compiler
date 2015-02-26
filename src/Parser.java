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

	}
	
	private void parseProgram(){
		parseBlock();
		match("$");	
	}
	
	private void parseBlock(){
		match("{");
		parseStatementList();
		match("}");
	}
	
	private void parseStatementList(){
		parseStatement();
		parseStatementList();
	}
	
	
	private void parseStatement(){
		String nextToken = nextToken().getIndicator();
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
		}
	}
	
	private void parsePrintStatement(){
		match("print");
		match("(");
		parseExpression();
		match(")");
		
	}
	
	private void parseAssignmentStatement(){
		match("[a-z]");
		match("=");
		parseExpression();
	}
	
	private void parseVariableDeclaration(){
		match("int|string|boolean");
		match("[a-z]");
	}
	
	private void parseWhileStatement(){
		match("while");
		parseBooleanExpression();
		parseBlock();
	}
	
	private void parseIfStatement(){
		match("if");
		parseBooleanExpression();
		parseBlock();
	}
	
	private void parseExpression(){
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
			}
		}
	}
	
	private void parseIntExpression(){
		match("[0-9]");
		if(nextToken().getValue().matches("+")){
			parseIntegerOperation();
			parseExpression();
		}
		else{
			return;
		}
	}
	
	private void parseStringExpression(){
		match("\"");
		parseCharlist();
		match("\"");
	}
	
	private void parseBooleanExpression(){
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
	}
	
	private void parseId(){
		match("[a-z]");
	}
	
	private void parseCharlist(){
		String nextToken = nextToken().getValue();
		if(nextToken.matches("[a-z]") | nextToken.matches("[ ]")){
			parseCharlist();
		}
		else{
			//epcelon production
			
		}
	}
	
	private void parseBooleanOperation(){
		match("==|!=");
	}
	
	private void parseIntegerOperation(){
		match("+");
		
	}
	
	
	
	
	
	private void match(String toMatch){
		//if we have another token andmatch correctly, consume the token.
		if(hasNextToken() && nextToken().getValue().matches(toMatch)){
			consumeToken();
		}
		
		//else raise an error or warning.
		else{
			//TODO make an error/warning function and call it here.
			
			
		}
		
	}
	
	private void consumeToken(){
		tokenStream.remove(0);
	}
	
	private Token nextToken(){
		return tokenStream.get(0);
	}
	
	private boolean hasNextToken(){
		return !(tokenStream.isEmpty());	
	}
}
