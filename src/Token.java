
public class Token {
	
	
	//fields
	
	//value of the token
	public String value;
	
	//type of token: see enum TokenType
	public int type;
	
	//Regex code (see RegexPatterns) that matches this token
	public int regexCode;
	
	public String indicator;
	
	public int lineNum;

	//list of possible token types
	public enum TokenType{
		IDENTIFIER(1), 
		STRING(2), 
		DIGIT(3), 
		RESERVED(4),
		SYMBOL(5),
		ERROR(6),
		WHITESPACE(7);
		
		private final int tokenCode;
		
		private TokenType(int tokenCode){
			this.tokenCode = tokenCode;
		}
		
		public int getTokenCode(){
			return this.tokenCode;
		}
	}
	
	//CONSTRUCTORS
	public Token(){
		this.value = "";
		
	}
	
	public Token(String value){
		this.value = value;
	}
	
	public Token(String value, int regexCode, int type, String indicator){
		this.value = value;
		this.regexCode = regexCode;
		this.type = type;
		this.indicator = indicator;
	}
	
	
	//INSTANCE METHODS
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}
	
	public int getRegexCode() {
		return regexCode;
	}

	public void setRegexCode(int regexCode) {
		this.regexCode = regexCode;
	}

	public String getIndicator() {
		return indicator;
	}

	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}
	
	public int getLineNum() {
		return lineNum;
	}

	public void setLineNum(int lineNum) {
		this.lineNum = lineNum;
	}
	

	@Override
	public String toString() {
		return "<" + value + " , " + indicator+ ">";
	}

	
	


}
