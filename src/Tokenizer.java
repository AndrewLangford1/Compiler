import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;




public class Tokenizer {
	
	
	
	private boolean isInsideString;
	private ArrayList<String> unmatchedTokens;
	private RegexPatterns regexes;
	
	//Used to check class of tokens
	private enum TokenType{
		IDENTIFIER(1), 
		LITERAL(2), 
		DIGIT(3), 
		RESERVED(4);
		
		private final int tokenCode;
		
		private TokenType(int tokenCode){
			this.tokenCode = tokenCode;
		}
		
		public int getTokenCode(){
			return this.tokenCode;
		}
	}
		
	
	public Tokenizer(ArrayList<String> unmatchedTokens){
		this.isInsideString = false;
		this.unmatchedTokens = unmatchedTokens;
		 
	}
	
	public void run(){
		for(String x : unmatchedTokens){
	
		}
		

	}
	
	
	public void tokenMatch(String toMatch){	
		if(isInsideString){

			
		}
		
		
	}	
}
