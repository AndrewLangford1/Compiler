import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;




public class Tokenizer {
	
	
	public Tokenizer(){
	}
	
	
	public void tokenMatch(String toMatch){		
		//pull regex table
		HashMap<String,String> regexMap = RegexMatchers.REGEXES;
		
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
		System.out.println("TokenString: " + ret + "  Type: " + tokenMatcher);
	
		String[] restOfString = toMatch.split(tokenMatcher);
	}
	
}
