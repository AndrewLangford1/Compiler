package dataStructures;


public class Token {
	
	
	//fields
	
	//value of the token
	private String value;
	
	//type of token: see enum TokenType
	private int type;
	
	//Regex code (see RegexPatterns) that matches this token
	private int regexCode;
	
	private String indicator;
	
	private int lineNum;

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
	
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the regexCode
	 */
	public int getRegexCode() {
		return regexCode;
	}

	/**
	 * @param regexCode the regexCode to set
	 */
	public void setRegexCode(int regexCode) {
		this.regexCode = regexCode;
	}

	/**
	 * @return the indicator
	 */
	public String getIndicator() {
		return indicator;
	}

	/**
	 * @param indicator the indicator to set
	 */
	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}

	/**
	 * @return the lineNum
	 */
	public int getLineNum() {
		return lineNum;
	}

	/**
	 * @param lineNum the lineNum to set
	 */
	public void setLineNum(int lineNum) {
		this.lineNum = lineNum;
	}
	
	@Override
	public String toString() {
		return "<" + value + " , " + indicator+ ">";
	}
	
}
