import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;




public class Tokenizer {
	
	
	
	private boolean isInsideString;
	

		
	
	public Tokenizer(){
		this.isInsideString = false;
		 
	}
	
	
	//Main function for Tokenizer
	public void run(String unlexedToken){
		try{
			//Try and Match all of the tokens char by char that are coming in from Lex
			for(int i =0; i < unlexedToken.length(); i++){
				tokenMatch(String.valueOf(unlexedToken.charAt(i)),unlexedToken.substring(i), i, unlexedToken.length());
			}
		} 
		catch(Exception ex){
			System.out.println(ex);
		}
		

	}
	
	//toMatch = char currently trying to lex
	//restOfToken = rest of string trying to lex, to be used for character lookahead
	//currentIndex = current position of toMatch in respect to whole token length
	//tokenLength = entire token length
	public void tokenMatch(String toMatch, String restOfToken, int currentIndex, int tokenLength){
		//new token to be added to the token stream
		Token token = new Token();
		System.out.println("The rest:  " + restOfToken);
		try{
			//If we aren't inside of a string, we aren't concerned with empty strings
			if(!isInsideString && !toMatch.isEmpty()){
				
				
				//check for digit token matches
				if(toMatch.matches(RegexPatterns.Regs.DIGIT.getPattern())){
					token.value = toMatch ;
					token.type = Token.TokenType.DIGIT.getTokenCode();
				}
				
				if(toMatch.matches(RegexPatterns.Regs.SINGLECHAR.getPattern())){
				}
				
			}
			
			
			//temporary statements for debugging purposes. Simply prints out the tokens
			if(!token.getValue().isEmpty()){
				//System.out.println("<value: " + token.getValue() +" type: " + token.type + ">");
			}
		}
		
	
		catch(Exception ex){
			System.out.println("error found in token match function");
			System.out.println(ex);
	
		}
	}

	//this function will match ID types, but also match reserved words
	public void matchIdentifier(String toMatch, String restOfToken, int currentIndex, int tokenLength){
		System.out.println("Lexing: " + restOfToken + "\n Currently at char: " + toMatch);
		
		//lookahead buffer
		String lookAhead = toMatch;
		
		//temp next char string;
		String nextChar;
		
		int tempCode;
		int bestRegexMatch = 0;
		
		//lookahead to see if we can match a reserved word, instead of returning IDs
		for(int i =1; i<= restOfToken.length(); i++){
			
			//get the next character on the token
			nextChar = String.valueOf(restOfToken.charAt(i));
			
			//if the next character is [a-z], then add this character onto the lookahead string
			if(nextChar.matches(RegexPatterns.Regs.SINGLECHAR.getPattern())){
				lookAhead+=nextChar;
				
				//see if this string matches a reserved word
				tempCode = reservedMatcher(lookAhead);
				
				//if the string matches a reserved word, get the code of that regex.
				//we know that after every iteration of the for loop that the lookahead is longer than the previous attempted match.
				//therefore, the longest match is stored in bestRegexMatch after each iteration.
				if(tempCode > 0){
					bestRegexMatch = tempCode;
				}
			}
			
			//if we get a non [a-z] character, stop looking ahead.
			else{
				break;
			}
		}
		
		//if we have a match for a reserved word, return that token instead of an ID
		if(bestRegexMatch>0){
		}
	}
	
	
	//Takes in a String to be matched against the Regexes that match reserved words in the grammar
	//Returns code of the Regex that matched the string
	public int reservedMatcher(String toMatch){
		
		//Note: Reserved words have to be unique. Therefore, the match found here is the best one. 
		for(RegexPatterns.Regs x: RegexPatterns.RESERVED){
			if(toMatch.matches(x.getPattern())){
				return x.getCode();
			}
		}
		return 0;
	}
}
