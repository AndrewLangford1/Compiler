import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;



public class Compiler {
	private static Scanner scanner;

	
	//takes in a source file, and passes them on to the tokenizer.
	public static void main(String[] args){
		
		
		try {
			//source file to be translated to target
			//for execution in command prompt
			File source = new File(args[0]);
			
			System.out.println("Begin Compilation:  " + source.getPath());
			
			ArrayList<Token> tokenStream = lexographicAnalyze(source);
			
			
			//move on to parse if there weren't any errors
			if(!(tokenStream == null)){
				parseInput(tokenStream);
			
			}
			//kill compilation if there were errors in lex
			else{
				return;
			}
			
			
			
		
		} catch(Exception ex){
			System.out.println("error found in main lex function");
			System.out.println(ex);	
		}
	}
	
	
	//Function to perform lexographic analysis
	//Returns a list of tokens if lex was successful
	//returns null if there were errors found during lexing.
	private static ArrayList<Token> lexographicAnalyze(File sourceFile){
		try{
		
			//using scanner to read files
			scanner = new Scanner(sourceFile);
			
			//Lex object will scan input and build token stream
			Lexer lexer= new Lexer();
			
			//keep track of line numbers
			int lineCount = 0;
			
			
			System.out.println("Lexing.....");
			
			//iterate over source file
			while(scanner.hasNextLine()){
				lineCount ++;
				lexer.lex(scanner.nextLine(), lineCount);
			}
			
			//stop reading input;
			scanner.close();
			
			//Grab errors, if there are any
			ArrayList<String> errors = lexer.getErrorList();
			
			//Grab Token stream from lex
			ArrayList<Token> tokenStream = lexer.getTokenStream();
			
			//if we have errors, kill compilation and print them out to the console.
			if(!errors.isEmpty()){
				lexer.printErrors();
				//returning null will stop Compilation
				return null;
			}
					
			else{
				//print the token stream out, for shits and gigs
				System.out.println("Lex Success! ");
				
				lexer.printTokens();
				
				//return the tokens
				return tokenStream;
			}
			
		}
		
		catch(Exception ex){
			System.out.println(ex);
		}
		return null;
	}
	
	
	/*
	 * 
	 */
	private static void parseInput(ArrayList<Token> tokenStream){
		System.out.println("Moving on to parse.....");
		Parser parser = new Parser(tokenStream);
		parser.parse();
	}
}
