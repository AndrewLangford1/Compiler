import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class Tokenizer {
	
	
	public Tokenizer(){
	}
	
	
	public String tokenMatch(String toMatch){
		//string to hold tempory substring found by match
		String temp = new String();
		
		//holds length of current longest match
		int max = 0;
		
		//returnable string
		String ret = new String();
		
		//iterate over all patterns to find max
		for(String x : RegexMatchers.PATTERNS){
			
			//declare our pattern
			Pattern pattern = Pattern.compile(x);
			
			//check to see if pattern matches current string
			Matcher matcher = pattern.matcher(toMatch);
			
			//if we find a match, see if it is the current longest match
			if(matcher.find()){
				temp = matcher.group();
				if(temp.length() > max){
					max = temp.length();
				}
			}
		}
		
		return ret;
	}
	

	
	
	
}
