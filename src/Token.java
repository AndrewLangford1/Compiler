
public class Token {
	
	
	//fields
	public String value;
	public int type;
	
	
	//list of possible token types
	public enum TokenType{
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
	
	//CONSTRUCTORS
	public Token(){
		this.value = "";
		
	}
	
	public Token(String value){
		this.value = value;
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

}
