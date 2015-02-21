import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;




public class Tokenizer {
	
	
	
	private boolean isInsideString;
	private ArrayList<Token> tokenStream;
	private ArrayList<Token> errorList;
	

		
	
	public Tokenizer(){
		this.isInsideString = false;
		tokenStream = new ArrayList<Token>();
		errorList = new ArrayList<Token>();
		 
	}
	
	
	//Main function for Tokenizer
	public void run(String unlexedToken){
		try{
			//Try and Match all of the tokens char by char that are coming in from Lex
			//keep matching tokens until empty
			while(!unlexedToken.isEmpty()){
				
				//match a token 
				Token lexedToken = tokenMatch(String.valueOf(unlexedToken.charAt(0)),unlexedToken.substring(0));
				System.out.println("Finished matching a token");
				
				if(!lexedToken.getValue().isEmpty()){
					System.out.println("Matched a token");
					//chop found tokens off the head of our unlexed token
					if(lexedToken.value.length()>1){
						unlexedToken = unlexedToken.substring(lexedToken.getValue().length()-1, unlexedToken.length()-1);
					}
					else{
						unlexedToken = unlexedToken.substring(1);
					}
					
					
					System.out.println("need to lex the rest of: " + unlexedToken);
					
					
					//add the token to the token stream to be sent to lex.
					tokenStream.add(lexedToken);
					
				}
				else{
					System.out.println("Didn't match a token");
					
					errorList.add(lexedToken);
					unlexedToken = unlexedToken.substring(1);
					
					System.out.println("need to lext the rest of: " + unlexedToken);
				}
				
			}
		} 
		catch(Exception ex){
			System.out.println(ex);
		}
		
		printTokens();
		

	}
	
	//toMatch = char currently trying to lex
	//restOfToken = rest of string trying to lex, to be used for character lookahead
	//currentIndex = current position of toMatch in respect to whole token length
	//tokenLength = entire token length
	public Token tokenMatch(String toMatch, String restOfToken){
		//new token to be added to the token stream
		Token token = new Token();
		try{
			//If we aren't inside of a string:
			if(!isInsideString && !toMatch.isEmpty()){
				
				//TOKEN MATCH HIERARCHY, NOT STRING MODE
				
				//KEYWORDS,IDENTIFIERS
				//Matches reserved words and identifiers. Reserved words take priority over identifiers.
				if(toMatch.matches(RegexPatterns.RegX.SINGLECHAR.getPattern())){
					token = tokenMatchIdentifier(toMatch, restOfToken);
					return token;
				}
				
				//STRINGS
				if(toMatch.matches(RegexPatterns.RegX.DOUBLEQUOTE.getPattern())){
					//enter string mode, create double quote token
					token = beginStringMode(toMatch);
					return token;
				}
				
				//DIGITS
				//check for digit token matches
				if(toMatch.matches(RegexPatterns.RegX.DIGIT.getPattern())){
					token = tokenMatchDigit(toMatch);
					return token;
					
				}
				
				//SYMBOLS
				//Matches Symbols
				if(toMatch.matches(RegexPatterns.RegX.SYMBOL.getPattern())){
					token = tokenSymbolMatcher(toMatch, restOfToken);
					return token;
				}
				
				
				
			}
			else{
				//STRING MODE
				
				
				
				
			}
		}
		catch(Exception ex){
			System.out.println("error found in token match function");
			System.out.println(ex);
		}
		
		
		//if we get here, we know there's a lex error
		return token;
	}

	
	
	
	
	//Creates a digit token from the provided String
	public Token tokenMatchDigit(String toMatch){
		
		//build token object
		Token tempToken = new Token();
		tempToken.value = toMatch ;
		tempToken.type = Token.TokenType.DIGIT.getTokenCode();
		tempToken.regexCode = RegexPatterns.RegX.DIGIT.getCode();
		tempToken.regexName = RegexPatterns.RegX.DIGIT.getName();
		
		return tempToken;
	}
	
	
	/*
	 * Param:
	 * toMatch: the current character we are attempting to token match
	 * restOfToken = the input token from lex we haven't lexed yet.
	 * 
	 */
	//this function will match ID types, but also match reserved words
	public Token tokenMatchIdentifier(String toMatch, String restOfToken){
		
		Token tempToken = new Token();
		
		try{
			System.out.println("Reserved/Identifier Lexing: " + restOfToken + "\nCurrently at char: " + toMatch);
			
			
			//if we have more than 1 character left in the unlexed token, lookahead to see if we can match a reserved word
			if(restOfToken.length()>1){
				//lookahead buffer
				String lookAhead = toMatch;
				System.out.println("lookahead: " + lookAhead);
				String matched = new String();
				
				//temp next char string;
				String nextChar;
						
				//denotes the code for best regex match (for reserved word matches only)
				int bestRegexMatch = 0;
				
				//will hold the regex object that matches a reserved word
				RegexPatterns.RegX regexMatch = null;
				
				
					//lookahead to see if we can match a reserved word, instead of returning IDs
				for(int i =1; i< restOfToken.length(); i++){
					System.out.println("index: " + i +"\nRestOfToken: " +restOfToken + "\nLengthToken: " + restOfToken.length());
						
					//get the next character on the token
					nextChar = String.valueOf(restOfToken.charAt(i));
					System.out.println("Next Char: " + nextChar);
						
					//if the next character is [a-z], then add this character onto the lookahead string
					if(nextChar.matches(RegexPatterns.RegX.SINGLECHAR.getPattern())){
						lookAhead+=nextChar;
							
						System.out.println("lookahead: " + lookAhead);
							
						//see if this string matches a reserved word
						regexMatch= longestMatchedReserved(lookAhead);
							
						//if the string matches a reserved word, get the code of that regex.
						//we know that after every iteration of the for loop that the lookahead is longer than the previous attempted match.
						//therefore, the longest match is stored in bestRegexMatch after each iteration.
						if(regexMatch != null){
							matched = lookAhead;
							System.out.println("matched: " + matched);
							bestRegexMatch = regexMatch.getCode();
							System.out.println("bestRegexMatch: " + bestRegexMatch);
						}
					}
						
					//if we get a non [a-z] character, stop looking ahead.
					else{
						break;
					}
				}
			
				//if we have a match for a reserved word, return that token instead of an ID
				if(bestRegexMatch>0 && regexMatch != null){
					//set the regex match for this token
					tempToken.setRegexCode(bestRegexMatch);
					
					//this token is of reserved type
					tempToken.setType(Token.TokenType.RESERVED.getTokenCode());
					
					//set the value of this token
					tempToken.setValue(matched);
					
					tempToken.setRegexName(regexMatch.getName());
				}
			}
			else{
				//regex match is just a char
				tempToken.setRegexCode(RegexPatterns.RegX.SINGLECHAR.getCode());
				
				//return an identifier
				tempToken.setType(Token.TokenType.IDENTIFIER.getTokenCode());
				
				//only return the single char to denote an ID
				tempToken.setValue(toMatch);
				
				tempToken.setRegexName("ID");
			}
		} 
		catch(Exception ex){
			System.out.println(ex);
			System.out.println("error found in identifier function");
		}
		
		System.out.println("Temp Token: " + tempToken.regexCode + " " + tempToken.type + " " + tempToken.value);
		
		return tempToken;
	}
	
	
	//Takes in a String to be matched against the Regexes that match reserved words in the grammar
	//Returns Regex that matched the string
	public RegexPatterns.RegX longestMatchedReserved(String toMatch){
		
		//Note: Reserved words have to be unique. Therefore, the match found here is the best one. 
		for(RegexPatterns.RegX x: RegexPatterns.RESERVED){
			if(toMatch.matches(x.getPattern())){
				//return the code of the Regex Match
				return x;
			}
		}
		
		//if we return null, we know this token is an identifier and not a reserved word
		return null;
	}
	
	
	public Token tokenSymbolMatcher(String toMatch, String restOfToken){
		Token tempToken = new Token();
		try{
			
			//get the specific regex object that matches this char
			RegexPatterns.RegX regexMatch = longestSymbolMatch(toMatch);
			
			//if the rest of the token is empty or has just the character we're trying to match in it, return that symbol. No need to lookahead
			if(restOfToken.length() <=1){
				tempToken.setValue(toMatch);
				tempToken.setRegexCode(regexMatch.getCode());
				tempToken.setType(Token.TokenType.SYMBOL.getTokenCode());
				tempToken.setRegexName(regexMatch.getName());
				
			}
			
			//else if theres more characters, check to see if we get == or !=
			else{
				//string matched will a lookahead buffer
				String matched;
				//holds next char in input token sequence
				String nextChar = String.valueOf(restOfToken.charAt(1));
				
				switch(toMatch){
				
				//check for boolean equals
					case "=":{
						if(nextChar.matches(RegexPatterns.RegX.ASSIGNMENT.getPattern())){
							matched = toMatch + nextChar;
							tempToken.setValue(matched);
							tempToken.setRegexCode(RegexPatterns.RegX.BOOLEANEQUALS.getCode());
							tempToken.setType(Token.TokenType.SYMBOL.getTokenCode());
							tempToken.setRegexName(RegexPatterns.RegX.BOOLEANEQUALS.getName());
						}
						
						break;
					}	
				
					
					//check for boolean not equals
					case "!":{
						if(nextChar.matches(RegexPatterns.RegX.ASSIGNMENT.getPattern())){
							matched = toMatch + nextChar;
							tempToken.setValue(matched);
							tempToken.setRegexCode(RegexPatterns.RegX.BOOLEANNOTEQUALS.getCode());
							tempToken.setType(Token.TokenType.SYMBOL.getTokenCode());
							tempToken.setRegexName(RegexPatterns.RegX.BOOLEANNOTEQUALS.getName());
						}
						
						break;
					}
					
					//nextChar wasnt a symbol we're interested in, so return the single symbol toMatch
					default:{
						tempToken.setValue(toMatch);
						tempToken.setRegexCode(regexMatch.getCode());
						tempToken.setType(Token.TokenType.SYMBOL.getTokenCode());
						tempToken.setRegexName(regexMatch.getName());
					}
				}
			}
		}
		
		
		catch(Exception ex){
			System.out.println("Error in symbol Matcher");
			System.out.println(ex);	
		}
		
		return tempToken;
	}
	
	
	//Returns a # greater than 0 if the given string matches a symbol
	//returns 0 if the given string doesn't match a symbol
	public RegexPatterns.RegX longestSymbolMatch(String toMatch){
		for(RegexPatterns.RegX x : RegexPatterns.SYMBOLS){
			if(toMatch.matches(x.getPattern())){
				return x;
			}
		}
		return null;
	}
	
	
	//Creates a double quote token and initializes string mode.
	public Token beginStringMode(String toMatch){
		//grab regex for double quote
		RegexPatterns.RegX quoteRegex = RegexPatterns.RegX.DOUBLEQUOTE;
		
		//build the double quote token
		Token tempToken = new Token();
		tempToken.setValue(toMatch);
		tempToken.setRegexCode(quoteRegex.getCode());
		tempToken.setRegexName(quoteRegex.getName());
		tempToken.setType(Token.TokenType.STRING.getTokenCode());
		
		//start string mode
		this.isInsideString = true;
		
		return tempToken;
	}
	
	public Token stringMode(String toMatch, String restOfToken){
		Token tempToken = new Token();
		RegexPatterns.RegX quoteReg = RegexPatterns.RegX.DOUBLEQUOTE;
		
		//if the next character is a double quote, kill string mode and return a double quote token.
		if(toMatch.matches(quoteReg.getPattern())){
			this.isInsideString = false;
			tempToken.setValue(toMatch);
			tempToken.setRegexName(quoteReg.getName());
			tempToken.setType(Token.TokenType.STRING.getTokenCode());
			tempToken.setRegexCode(quoteReg.getCode());
			return tempToken;
		}
		else{
			return tempToken;
			
			
			
			
			
			
			
		}
	}
		
	
	//Prints out tokens, debugging only
	public void printTokens(){
		for(Token x: tokenStream){
			System.out.println(x.toString());
		}
	}
}
