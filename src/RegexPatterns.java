
//This enum class is simply a namespace for regular expression strings
//they match strings in the Final Project Grammar.
// for most current grammar definitions, see http://www.labouseur.com/courses/compilers/grammar.pdf
		public enum RegexPatterns{
			
			
			//Program
			//denotes end of program
			//ex: Block $
			ENDOFPROGRAM("[$]"),
			
			//Block {}
			//denotes new scope
			BLOCKLEFTCURLY("[{]"),
			BLOCKRIGHTCURLY("[}]"),
			
			//Print Statement
			PRINTSTMT("print"),
			
			//Assignment
			ASSIGNMENT("="),
			
			//LOOPS
			//while statement
			WHILESTMT("while"),
			
			//BRANCHING
			//if statement
			IFSTATEMENT("if"),
			
			//double quote, denotes beginning or end of strings.
			DOUBLEQUOTE("\""),
			
			//CHARLIST
			CHARLIST("(\"[a-z]*\")|(\"\")|(\"[a-z]*\\s*\")|(\"\\s*\")|(\"\\s*[a-z]*\")|(\"\\s*[a-z]*\\s*\")"),
			
			//Parentheses
			//used in print statement, boolean expressions
			LEFTPAREN("[(]"),
			RIGHTPAREN("[)]"),
			
			//TYPES
			STRINGTYPE("string"),
			BOOLEANTYPE("boolean"),
			INTTYPE("int"),
			
			//CHAR/ID
			SINGLECHAR("[a-z]" ),
			
	
	
			/////////////WHITESPACE///////////////////
			
			//tabs or spaces
			WHITESPACE("[ ]+(?=([^\"]*\"[^\"]*\")*[^\"]*$)"),
	
			//newline characters like carriage return or newline
			CARRIAGERETURN("\\r"),
			NEWLINE("\\n"),
	
		
			
			//DIGIT
			DIGIT("[0-9]{1}"),
			
	
			//BOOLEAN OPERATORS
			BOOLEANEQUALS("=="),
			BOOLEANNOTEQUALS("!="),
			
			//BOOLEAN VALUES
			BOOLEANVALUE("true|false"),
				
		
	
			//INTEGER OPERATORS
			ADDITION("[+]");
			
			
		public String pattern;
	
		private RegexPatterns(String pattern){
			this.pattern = pattern;
		}
	
		public String getPattern(){
			return this.pattern;
		}
	}

