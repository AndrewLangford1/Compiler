public class RegexPatterns{
//This enum class is simply a namespace for regular expression strings
//they match strings in the Final Project Grammar.
// for most current grammar definitions, see http://www.labouseur.com/courses/compilers/grammar.pdf
		
	
		public static Regs[] RESERVED = {Regs.PRINTSTMT, Regs.WHILESTMT, Regs.IFSTATEMENT, Regs.INTTYPE,
									Regs.BOOLEANTYPE, Regs.STRINGTYPE, Regs.BOOLEANVALUE};
		
		public static Regs[] SYMBOLS = {Regs.BLOCKLEFTCURLY, Regs.BLOCKRIGHTCURLY, Regs.DOUBLEQUOTE, Regs.LEFTPAREN,
											Regs.RIGHTPAREN, Regs.ENDOFPROGRAM, Regs.BOOLEANEQUALS, Regs.BOOLEANNOTEQUALS, Regs.ASSIGNMENT,
											Regs.ADDITION
		};
	
		public enum Regs{
			
			
			//Program
			//denotes end of program
			//ex: Block $
			ENDOFPROGRAM("[$]", 1),
			
			//Block {}
			//denotes new scope
			BLOCKLEFTCURLY("[{]", 2),
			BLOCKRIGHTCURLY("[}]", 3),
			
			//Print Statement
			PRINTSTMT("print", 4),
			
			//Assignment
			ASSIGNMENT("=", 5),
			
			//LOOPS
			//while statement
			WHILESTMT("while", 6),
			
			//BRANCHING
			//if statement
			IFSTATEMENT("if", 7),
			
			//double quote, denotes beginning or end of strings.
			DOUBLEQUOTE("\"", 8),
			
			//CHARLIST
			CHARLIST("(\"[a-z]*\")|(\"\")|(\"[a-z]*\\s*\")|(\"\\s*\")|(\"\\s*[a-z]*\")|(\"\\s*[a-z]*\\s*\")",9 ),
			
			//Parentheses
			//used in print statement, boolean expressions
			LEFTPAREN("[(]", 10),
			RIGHTPAREN("[)]", 11),
			
			//TYPES
			STRINGTYPE("string", 12),
			BOOLEANTYPE("boolean", 13),
			INTTYPE("int", 14),
			
			//CHAR/ID
			SINGLECHAR("[a-z]", 15 ),
			
	
	
			/////////////WHITESPACE///////////////////
			
			//tabs or spaces
			WHITESPACE("[ ]+(?=([^\"]*\"[^\"]*\")*[^\"]*$)", 16),
	
			//newline characters like carriage return or newline
			CARRIAGERETURN("\\r", 17),
			NEWLINE("\\n", 18),
	
		
			
			//DIGIT
			DIGIT("[0-9]{1}", 19),
			
	
			//BOOLEAN OPERATORS
			BOOLEANEQUALS("==", 20),
			BOOLEANNOTEQUALS("!=", 21),
			
			//BOOLEAN VALUES
			BOOLEANVALUE("true|false", 22),
				
		
	
			//INTEGER OPERATORS
			ADDITION("[+]", 23),
			
			SYMBOL("[$]|[{]|[}]|[(]|[)]|[=]|[\"]|[!]|[+]", 24);
			
			
		private String pattern;
		private int code;
	
		private Regs(String pattern, int code){
			this.pattern = pattern;
			this.code = code;
		}
	
		public String getPattern(){
			return this.pattern;
		}
		
		public int getCode(){
			return this.code;
		}

	}
		
	
}
		

