import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;




public class Tokenizer {
	
	private boolean isInsideString;
	private HashMap<String,String> regexMap;
	
	//
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
	
	
	
	
	public Tokenizer(){
		this.isInsideString = false;
		this.regexMap = RegexPatterns.REGEXES;
		
		
		
	}
	
	
	public void tokenMatch(String toMatch){		
		
		System.out.println(toMatch);
				
/*
		//string to hold temporary substring found by match
		String token = new String();
		
		//string to hold the max length token
		String ret = new String();
		
		//String to hold longest matched regex key
		String tokenMatcher = new String();
		
		
		//holds length of current longest match
		int max = 0;

		//iterate over all patterns to find max
		for(String key : regexMap.keySet()){
			
			//grab regex
			String thePattern = regexMap.get(key);
			
			//declare our pattern
			Pattern pattern = Pattern.compile(thePattern);
			
			//check to see if pattern matches current string
			Matcher matcher = pattern.matcher(toMatch);
			
			//if we find a match, see if it is the current longest match
			if(matcher.find()){
				token = matcher.group();
				if(token.length() > max){
					tokenMatcher = key;
					max = token.length();
					ret = token;
				}
			}
		}
*/
	}
	
	public void checkTokenType(String token){
		int identifier = isIdentifier(token);
		int digit = isDigit(token);
		int literal = isLiteral(token);
	
	
		
		
	}
	
	public int isIdentifier(String token){
		if(!isInsideString){
			if(token.matches(regexMap.get("singleChar"))){
				return TokenType.IDENTIFIER.tokenCode;
			}	
		}
		return 0;
	}
	
	public int isDigit(String token){
		if(!isInsideString){
			if(token.matches(regexMap.get("digit"))){
				return TokenType.DIGIT.tokenCode;
			}
		}
		return 0;
	}
	
	public int isLiteral(String token){
		if(isInsideString){
			//keep grabbing characters from inside the string
			//aka grab next token until you find the end of the string dictated by an end double quote
			if(token.matches(regexMap.get("stringInit"))){
				isInsideString = false;
				return TokenType.LITERAL.tokenCode;
			}
		}
		if(!isInsideString){
			//if not inside string, check to see if this token is a double quote
			if(token.matches(regexMap.get("stringInit"))){
				isInsideString = true;
				return TokenType.LITERAL.tokenCode;
			}
		}
		
		return 0;
	}
	
	
		
	
}
