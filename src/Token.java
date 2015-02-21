
public class Token {
	
	
	//fields
	
	//value of the token
	public String value;
	
	//type of token: see enum TokenType
	public int type;
	
	//Regex code (see RegexPatterns) that matches this token
	public int regexCode;
	
	public String regexName;
	

	//list of possible token types
	public enum TokenType{
		IDENTIFIER(1), 
		STRING(2), 
		DIGIT(3), 
		RESERVED(4),
		SYMBOL(5),
		ERROR(6);
		
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
	
	public Token(String value, int regexCode, int type, String regexName){
		this.value = value;
		this.regexCode = regexCode;
		this.type = type;
		this.regexName = regexName;
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

	public String getRegexName() {
		return regexName;
	}

	public void setRegexName(String regexName) {
		this.regexName = regexName;
	}

	@Override
	public String toString() {
		return "Token [value=" + value + ", type=" + type + ", regexCode="
				+ regexCode + ", regexName=" + regexName + "]";
	}

	
	


}
