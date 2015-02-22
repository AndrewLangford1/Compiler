import java.util.ArrayList;

/*
 * Lexer Class
 * Class to break a source file down into a token stream to be passed on to the parser.
 * Handles token building, and lexical errors.
 */

public class Lexer {
	
	
	
	//determines if we are in string mode or not
	private boolean isInsideString;
	
	//tokens to be passed on to parse
	private ArrayList<Token> tokenStream;
	
	// messages that contain lexical errors. Will attempt to be as descriptive as possible.
	private ArrayList<String> errorList;
	
	

		
	
	public Lexer(){
		this.isInsideString = false;
		tokenStream = new ArrayList<Token>();
		errorList = new ArrayList<String>();
		 
	}
	
	
	//Main function for Lex
	public void lex(String unlexedToken, int lineNum){
		try{
			//Try and Match all of the tokens char by char that are coming in from Lex
			//keep matching tokens until empty
			while(!unlexedToken.isEmpty()){
				
				//match a token 
				Token lexedToken = tokenMatch(String.valueOf(unlexedToken.charAt(0)),unlexedToken.substring(0), lineNum);
				
								
				if(!(lexedToken == null)){
					
					//chop found tokens off the head of our unlexed token
					
					//chops multiple chars
					if(lexedToken.value.length()>1){
						unlexedToken = unlexedToken.substring(lexedToken.getValue().length(), unlexedToken.length());
					}
					
					//chops only the head char
					else{
						unlexedToken = unlexedToken.substring(1);
					}
					
					//add the token to the token stream to be sent to parse.
					lexedToken.setLineNum(lineNum);
					tokenStream.add(lexedToken);
					
				}
				
				//we got some whitespace that we want to skip over, chop the whitespace off the unlexed token.
				else{
					unlexedToken = unlexedToken.substring(1);
				}
				
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
	public Token tokenMatch(String toMatch, String restOfToken, int lineNum){
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
				//will enter string mode if this pattern is matched
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
					token = tokenSymbolMatcher(toMatch, restOfToken, lineNum);
					return token;
				}
				
				//skip over whitespace
				if(toMatch.matches(RegexPatterns.RegX.SPACECHAR.getPattern()) | toMatch.matches(RegexPatterns.RegX.ESCAPEDWHITESPACE.getPattern())){
					//System.out.println("found whiteSpace");	
					return null;
				}
			}
			
			
			//String Mode
			if(isInsideString){
				token = stringMode(toMatch, restOfToken);
					
			//Have to handle string errors differently than normal lexical errors
			//only chars or spacechars are allowed. 
				if(token == null){
					buildStringError(toMatch, lineNum);
					return null;
				}
				
				return token;
			}
		}
		catch(Exception ex){
			System.out.println("error found in token match function");
			System.out.println(ex);
		}
		
		
		//if we get here, we know there's a lex error.
		buildInvalidTokenError(toMatch, lineNum);
		
		
		//dont return a token, because we got an error
		return null;
	}

	
	
	
	
	//Creates a digit token from the provided String
	public Token tokenMatchDigit(String toMatch){
		//build token object
		Token tempToken = buildToken(toMatch,RegexPatterns.RegX.DIGIT.getCode(), Token.TokenType.DIGIT.getTokenCode(), 
											RegexPatterns.RegX.DIGIT.getName());	
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
			
			//if we have more than 1 character left in the unlexed token, lookahead to see if we can match a reserved word
			if(restOfToken.length()>1){
				
				//lookahead buffer
				String lookAhead = toMatch;

				//holds string value of matched token.
				String matched = new String();
				
				//temp next char string;
				String nextChar;
						
				//denotes the code for best regex match (for reserved word matches only)
				int bestRegexMatch = 0;
				
				//will hold the regex object that matches a reserved word
				RegexPatterns.RegX regexMatch = null;
				
				
					//lookahead to see if we can match a reserved word, instead of returning IDs
				for(int i =1; i< restOfToken.length(); i++){				
					//get the next character on the token
					nextChar = String.valueOf(restOfToken.charAt(i));	
				
					//if the next character is [a-z], then add this character onto the lookahead string
					if(nextChar.matches(RegexPatterns.RegX.SINGLECHAR.getPattern())){
						
						lookAhead+=nextChar;
							
						//see if this string matches a reserved word
						regexMatch= longestMatchedReserved(lookAhead);
							
						//if the string matches a reserved word, get the code of that regex.
						//we know that after every iteration of the for loop that the lookahead is longer than the previous attempted match.
						//therefore, the longest match is stored in bestRegexMatch after each iteration.
						if(regexMatch != null){
							matched = lookAhead;
							bestRegexMatch = regexMatch.getCode();
						}
					}
						
					//if we get a non [a-z] character, stop looking ahead.
					else{
						break;
					}
				}
			
				//if we have a match for a reserved word, return that token instead of an ID
				if(bestRegexMatch>0 && regexMatch != null){
					tempToken = buildToken(matched, bestRegexMatch, 
					   		Token.TokenType.RESERVED.getTokenCode(), regexMatch.getName());
				}
				
				//TODO clean this up. shouldnt need two else clauses to create a simple ID token.
				else{
					//regex match is just a char
					//return an identifier
					//only return the single char to denote an ID
					tempToken = buildToken(toMatch, RegexPatterns.RegX.SINGLECHAR.getCode(), 
										   		Token.TokenType.IDENTIFIER.getTokenCode(), "ID");
					
				}
			}
			else{
				//regex match is just a char
				//return an identifier
				//only return the single char to denote an ID
				tempToken = buildToken(toMatch, RegexPatterns.RegX.SINGLECHAR.getCode(), 
									   		Token.TokenType.IDENTIFIER.getTokenCode(), "ID");
				
			}
		} 
		catch(Exception ex){
			System.out.println(ex);
			System.out.println("error found in identifier function");
		}
		
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
	
	
	public Token tokenSymbolMatcher(String toMatch, String restOfToken, int lineNum){
		Token tempToken = new Token();
		try{
			
			//get the specific regex object that matches this char
			RegexPatterns.RegX regexMatch = longestSymbolMatch(toMatch);
			
			//if the rest of the token is empty or has just the character we're trying to match in it, return that symbol. No need to lookahead
			if(restOfToken.length() <=1){
				
				tempToken = buildToken(toMatch, regexMatch.getCode(), Token.TokenType.SYMBOL.getTokenCode(),regexMatch.getName());
				
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
							
							//build boolean equals token
							tempToken = buildToken(matched, RegexPatterns.RegX.BOOLEANEQUALS.getCode(), 
									Token.TokenType.SYMBOL.getTokenCode(),RegexPatterns.RegX.BOOLEANEQUALS.getName());
						}
						
						//if its not boolean equals, just return assignment
						else{
							tempToken = buildToken(toMatch, regexMatch.getCode(), Token.TokenType.SYMBOL.getTokenCode(),regexMatch.getName());
						}
						
						break;
					}	
				
					
					//check for boolean not equals
					case "!":{
						if(nextChar.matches(RegexPatterns.RegX.ASSIGNMENT.getPattern())){
							matched = toMatch + nextChar;
							
							//build boolean not equals token
							tempToken = buildToken(matched, RegexPatterns.RegX.BOOLEANNOTEQUALS.getCode(), 
														Token.TokenType.SYMBOL.getTokenCode(),RegexPatterns.RegX.BOOLEANNOTEQUALS.getName());
						}
						//anything else after a ! sign is an error
						else{
							this.buildInvalidTokenError(toMatch, lineNum);	
						}
						
						break;
					}
					
					//nextChar wasnt a symbol we're interested in, so return the single symbol toMatch
					default:{
						tempToken = buildToken(toMatch, regexMatch.getCode(), Token.TokenType.SYMBOL.getTokenCode(),regexMatch.getName());
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
		
		//if we get here, we didnt match a symbol.
		return null;
	}
	
	
	//Creates a double quote token and initializes string mode.
	public Token beginStringMode(String toMatch){
		//grab regex for double quote
		RegexPatterns.RegX quoteRegex = RegexPatterns.RegX.DOUBLEQUOTE;
		
		//build the double quote token
		Token tempToken = buildToken(toMatch, quoteRegex.getCode(), Token.TokenType.STRING.getTokenCode(),quoteRegex.getName());
		
		//start string mode
		this.isInsideString = true;
		
		return tempToken;
	}
	
	
	//Builds tokens for valid string characters
	//Chars, charlists, and spacechars allowed only.
	//returns a token object if a valid token is found
	//returns null if no valid token can be found
	public Token stringMode(String toMatch, String restOfToken){
		Token tempToken = new Token();
		
		try{
			
			
			//Only attempting to match quotes, single chars, or any version of whitespace. Anything else is a lex error
			
			//These are the regexes we are interested in matching.
			RegexPatterns.RegX quoteReg = RegexPatterns.RegX.DOUBLEQUOTE;
			RegexPatterns.RegX singleChar = RegexPatterns.RegX.SINGLECHAR;
			RegexPatterns.RegX spaceChar = RegexPatterns.RegX.SPACECHAR;
			RegexPatterns.RegX escapedWhiteSpace = RegexPatterns.RegX.ESCAPEDWHITESPACE;
			
			
			//if the next character is a double quote, kill string mode and return a double quote token.
			if(toMatch.matches(quoteReg.getPattern())){
				this.isInsideString = false;
				tempToken = buildToken(toMatch, quoteReg.getCode(), Token.TokenType.STRING.getTokenCode(),quoteReg.getName());
				return tempToken;
			}
			
			
			//if the next character isn't a double quote, check for valid strings (chars, or space characters)
			else{
			
				//match a single character
				if(toMatch.matches(singleChar.getPattern())){
					tempToken = buildToken(toMatch, singleChar.getCode(), Token.TokenType.STRING.getTokenCode(),singleChar.getName());
					return tempToken;
				}
				
				
				//match for whitespace characters.
				if(toMatch.matches(spaceChar.getPattern()) | toMatch.matches(escapedWhiteSpace.getPattern())){
					tempToken = buildToken(toMatch, spaceChar.getCode(), Token.TokenType.WHITESPACE.getTokenCode(), spaceChar.getName());
					return tempToken;
				}
			}
		} 
		catch(Exception ex){
			System.out.println("Error in String Mode");
			System.out.println(ex);
		}
		
		//if we get here, there was no valid string match.
		return null;
	}
	
	
	//builds a simple error message to be added to the error list
	public void buildInvalidTokenError(String toMatch, int lineNum){
		String errorMessage = "ERROR: [Line " + lineNum + "] ";
		errorMessage += "Invalid token: " + "'"+  toMatch + "'";
		errorList.add(errorMessage);
	}
	
	//builds a simple string error message to be added to the error list
	public void buildStringError(String invalidToken, int lineNum){
		String errorMessage = "ERROR: [Line " + lineNum + "] Invalid token found inside String. \n";
		errorMessage +=  "Found: " + "'" +invalidToken + "' , expecting char or space";
		errorList.add(errorMessage);
	}
	
	
	
	
	
	//constructs a new token and returns it. Calls Token constructor
	public Token buildToken(String value, int regexCode, int type, String indicator){
		Token ret = new Token(value, regexCode, type, indicator);
		return ret;
	}
		
	
	//Prints out tokens, debugging only
	public void printTokens(){
		System.out.println("TokenStream: ");
		for(Token x: tokenStream){
			System.out.println(x.toString());
		}
	}
	
	public void printErrors(){
		System.out.println(this.errorList.size() + " error(s):");
		//print errors
		for(String error : errorList){
			System.out.println(error);
		}
	}
	
/////auto generated////
	public ArrayList<Token> getTokenStream() {
		return tokenStream;
	}





	public ArrayList<String> getErrorList() {
		return errorList;
	}
	
}
