public class RegexMatchers{

//This class is simply a namespace for regular expression strings
//they match strings in the Final Project Grammar.
// for most current grammar definitions, see http://www.labouseur.com/courses/compilers/grammar.pdf

/////////////////TYPES/////////////////////////
	
	//String type
	public static String STR = "string";
	
	//integer type
	public static String INT = "int";
	
	//boolean type
	public static String BOOL = "boolean";
	
	
	//Char
	public static String CHARACTER = "[a-z]";
	public static String CHARLIST = "\"[a-z]* + | *\"";
	
	//Digits
	public static String DIGIT = "[0-9]{1}";
	
	//boolean
	public static String BOOLVAL = "true|false";
	
	//String declaration
	public static String STRINGDEC = "\".*\"";
	
	public static String STRINGINIT = "\"";


//////////////RESERVED /////////////////////////
	
	//Program
	//denotes end of program
	//ex: Block $
	public static String PROGRAM = "[$]";
	
	//Block {}
	//denotes new scope
	public static String LEFTCURL = "[{]";
	public static String RIGHTCURL = "[}]";
	
	
	//Parentheses
	//used in print statement, boolean expressions
	public static String LEFTPAREN = "[(]";
	public static String RIGHTPAREN = "[)]";
		
	//Print Statement
	public static String PRNTSTMT = "print";
	
	//LOOPS
	//while statement
	public static String WHILESTMT = "while";
	
	//BRANCHING
	//if statement
	public static String IFSTMT = "if";
	
/////////////WHITESPACE///////////////////
	
	//tabs or spaces
	public static String WHITESPACE = " +|\\t";
	
	//newline characters like carriage return or newline
	public static String CRETURN = "\\r";
	public static String NLINE = "\\n";
	
	
////////////OPERATORS////////////////////
	
	//BOOLEAN OPERATORS
	public static String BOOLEQ = "==";
	public static String BOOLNOEQ = "!=";
	
	//INTEGER OPERATORS
	public static String INTOP = "[+]";
	
	//Assignment
	public static String ASSIGNMENT = "=";
	
	
///////////// holds array of all patterns defined in the file.
	public static String[] PATTERNS = {ASSIGNMENT,
										BOOL,
										BOOLEQ,
										BOOLNOEQ,
										BOOLVAL,
										CHARACTER,
										CRETURN,
										DIGIT,
										IFSTMT,
										INT,
										INTOP,
										LEFTCURL,
										LEFTPAREN,
										NLINE,
										PRNTSTMT,
										PROGRAM,		
										RIGHTCURL,
										RIGHTPAREN,
										STR,
										STRINGDEC,
										WHILESTMT,
										STRINGINIT,
										WHITESPACE};
	
	
}
