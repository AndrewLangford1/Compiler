import java.util.HashMap;

public class RegexMatchers{

//This class is simply a namespace for regular expression strings
//they match strings in the Final Project Grammar.
// for most current grammar definitions, see http://www.labouseur.com/courses/compilers/grammar.pdf

/////////////////TYPES/////////////////////////
	public static  HashMap<String,String> REGEXES;

	static{
		 REGEXES= new HashMap<String,String>();
		
		//TYPES
		REGEXES.put("stringtype", "string" );
		REGEXES.put("booleantype", "boolean");
		REGEXES.put("inttype", "int");
		
		//CHAR
		REGEXES.put("singlechar","[a-z]" );
		
		//CHARLIST
		//REGEXES.put("charlist", "\"(?<=\")(?:\\.|[^\"\\])*(?=\")\"");
		
		REGEXES.put("quote", "");
		
		//SINGLE DIGIT
		REGEXES.put("digit", "[0-9]{1}");
		
		//BOOLEAN VALUES
		REGEXES.put("boolValue", "true|false");
		
		//A double quote for string opening/closing
		REGEXES.put("stringInit", "\"");
		
		
		//Program
		//denotes end of program
		//ex: Block $
		REGEXES.put("programEnd", "[$]");
		
		//Block {}
		//denotes new scope
		REGEXES.put("leftcurl", "[{]");
		REGEXES.put("rightcurl" ,"[}]");
		
		//Parentheses
		//used in print statement, boolean expressions
		REGEXES.put("leftparen","[(]");
		REGEXES.put("rightparen" , "[)]");
		
		//Print Statement
		REGEXES.put("printstatement", "print");
		
		//LOOPS
		//while statement
		REGEXES.put("whilestmt", "while");
		
		//BRANCHING
		//if statement
		REGEXES.put("ifstmt", "if");
		/////////////WHITESPACE///////////////////
		
		//tabs or spaces
		REGEXES.put("whitespace", "[ ]+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

		//newline characters like carriage return or newline
		REGEXES.put("carriagereturn","\\r");
		REGEXES.put("newline", "\\n");


		////////////OPERATORS////////////////////

		//BOOLEAN OPERATORS
		REGEXES.put("booleaneq","==");
		REGEXES.put("boolnoeq",  "!=");

		//INTEGER OPERATORS
		REGEXES.put("intop", "[+]");
		
		//Assignment
		REGEXES.put("assignment","=");
		
	}
}
