import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;




public class Tokenizer {
	
	
	
	private boolean isInsideString;
	private RegexPatterns regexes;
	

		
	
	public Tokenizer(){
		this.isInsideString = false;
		 
	}
	
	
	//Main function for Tokenizer
	public void run(String unlexedToken){
		try{
			//Try and Match all of the tokens char by char that are coming in from Lex
			for(int i =0; i < unlexedToken.length(); i++){
				tokenMatch("" + unlexedToken.charAt(i));
			}
		} 
		catch(Exception ex){
			System.out.println(ex);
		}
		

	}
	
	
	public void tokenMatch(String toMatch){
		//new token to be added to the token stream
		Token token = new Token();
		try{
			//If we aren't inside of a string, we aren't concerned with empty strings
			if(!isInsideString && !toMatch.isEmpty()){
				//check for digit token matches
				if(toMatch.matches(RegexPatterns.DIGIT.getPattern())){
					token.value = toMatch ;
					token.type = Token.TokenType.DIGIT.getTokenCode();
				}
			}
			
			
			
			
			//temporary statements for debugging purposes. Simply prints out the tokens
			if(!token.getValue().isEmpty()){
				System.out.println("<value: " + token.getValue() +" type: " + token.type + ">");
			}
		}
		
			
		
		catch(Exception ex){
			System.out.println("error found in token match function");
			System.out.println(ex);
	
		}
	}
}
