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
		match()
		
		
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
