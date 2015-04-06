package utilities;
public class RegexPatterns{
//This enum class is simply a namespace for regular expression strings
//they match strings in the Final Project Grammar.
// for most current grammar definitions, see http://www.labouseur.com/courses/compilers/grammar.pdf
		
	
	
	//Collections of Regexes
		public static RegX[] RESERVED = {RegX.PRINTSTMT, RegX.WHILESTMT, RegX.IFSTATEMENT, RegX.INTTYPE,
									RegX.BOOLEANTYPE, RegX.STRINGTYPE, RegX.BOOLEANVALUE};
		
		public static RegX[] SYMBOLS = {RegX.BLOCKLEFTCURLY, RegX.BLOCKRIGHTCURLY, RegX.LEFTPAREN,
											RegX.RIGHTPAREN, RegX.ENDOFPROGRAM, RegX.BOOLEANEQUALS, RegX.BOOLEANNOTEQUALS, RegX.ASSIGNMENT,
											RegX.ADDITION
		};
		
		public static RegX[] VALIDSTRINGS = {RegX.SINGLECHAR, RegX.NEWLINE, RegX.TAB, RegX.CARRIAGERETURN, RegX.SPACECHAR };
		
		
		

		public enum RegX{
			
			
			//Program
			//denotes end of program
			//ex: Block $
			ENDOFPROGRAM("[$]", 1, "EOF"),
			
			//Block {}
			//denotes new scope
			BLOCKLEFTCURLY("[{]", 2, "BLOCKLEFTCURLY"),
			BLOCKRIGHTCURLY("[}]", 3, "BLOCKRIGHTCURLY"),
			
			//Print Statement
			PRINTSTMT("print", 4, "PRINTSTATEMENT"),
			 
			//Assignment
			ASSIGNMENT("=", 5, "ASSIGNMENT"),
			
			//LOOPS
			//while statement
			WHILESTMT("while", 6, "WHILELOOP"),
			
			//BRANCHING
			//if statement
			IFSTATEMENT("if", 7, "IFSTATEMENT"),
			
			//double quote, denotes beginning or end of strings.
			DOUBLEQUOTE("\"", 8, "DOUBLEQUOTE"),
			
			//CHARLIST
			CHARLIST("(\"[a-z]*\")|(\"\")|(\"[a-z]*\\s*\")|(\"\\s*\")|(\"\\s*[a-z]*\")|(\"\\s*[a-z]*\\s*\")",9, "CHARLIST" ),
			
			//Parentheses
			//used in print statement, boolean expressions
			LEFTPAREN("[(]", 10, "LEFTPAREN"),
			RIGHTPAREN("[)]", 11, "RIGHTPAREN"),
			
			//TYPES
			STRINGTYPE("string", 12, "STRINGTYPE"),
			BOOLEANTYPE("boolean", 13, "BOOLEANTYPE"),
			INTTYPE("int", 14, "INTTYPE"),
			
			//CHAR/ID
			SINGLECHAR("[a-z]", 15, "CHAR" ),
			
	
	
			/////////////WHITESPACE///////////////////
			
			//tabs or spaces
			ESCAPEDWHITESPACE("[\\t]|[\\r]|[\\n]", 16, "ESCAPEDWHITESPACE"),
	
			//newline characters like carriage return or newline
			CARRIAGERETURN("\\r", 17, "CARRIAGEERETURN"),
			NEWLINE("\\n", 18, "NEWLINE"),
			TAB("\\t", 19, "TAB"),
			SPACECHAR("[ ]", 20, "SPACECHAR"),
			ESCAPESEQUENCE("[\\]", 21, "ESCAPESEQUENCE"),
			
	
		
			
			//DIGIT
			DIGIT("[0-9]{1}", 22, "DIGIT"),
			
	
			//BOOLEAN OPERATORS
			BOOLEANEQUALS("==", 23, "BOOLEANEQUALS"),
			BOOLEANNOTEQUALS("!=", 24, "BOOLEANNOTEQUALS"),
			
			//BOOLEAN VALUES
			BOOLEANVALUE("true|false", 25, "BOOLEANVALUE"),
				
		
	
			//INTEGER OPERATORS
			ADDITION("[+]", 26, "ADDITION"),
			
			SYMBOL("[$]|[{]|[}]|[(]|[)]|[=]|[!]|[+]", 27, "SYMBOL"),
			
			
			
			//Misc used in parser
			TYPEMATCH("int|boolean|string", 28, "TYPE");
			
			
			
		private String pattern;
		private int code;
		private String name;
	
		private RegX(String pattern, int code, String name){
			this.pattern = pattern;
			this.code = code;
			this.name = name;
		}
	
		public String getPattern(){
			return this.pattern;
		}
		
		public int getCode(){
			return this.code;
		}
		
		public String getName(){
			
			return this.name;
		}

	}
		
	
}
		

