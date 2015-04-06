
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import lexnParse.Lexer;
import lexnParse.Parser;
import dataStructures.Token;
import dataStructures.SyntaxTree;







	

public class Compiler {
	private static Scanner scanner;

	
	//takes in a source file, and passes them on to the tokenizer.
	public static void main(String[] args){
		
		
		try { 
			//source file to be translated to target
			//for execution in command prompt
			File source = new File(args[0]);
			
			System.out.println("---> Begin Compilation:  " + source.getPath());
			
			
			//lex the source file, grab the token stream from lex
			ArrayList<Token> tokenStream = lexographicAnalyze(source);
			
			
			//kill compilation if there were errors in lex
			if(tokenStream == null){
				killCompilation();
			}
			
			//parse the source file, grab the CST from parse
			SyntaxTree cst = parseInput(tokenStream);
			
			//kill compilation if there were errors parse
			if(cst == null){
				killCompilation();
			}
			
			
			// TODO semantic analyze CST
			
			
		
			
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
			
			
			System.out.println("\n---> Lexing");
			
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
			}
					
			else{
				//print the token stream out, for shits and gigs
				System.out.println("\n---> Lex Success! ");
				
				lexer.printTokens();
				
				//return the tokens
				return tokenStream;
			}
			
		}
		
		catch(Exception ex){
			System.out.println(ex);
		}
		//returning null will stop Compilation
		return null;
	}
	
	
	/*
	 * parse the input, and return a CST to perform Semantic Analysis on
	 * @param tokenStream, the token stream generated from lex
	 * @return a cst generated during parse
	 */
	private static SyntaxTree parseInput(ArrayList<Token> tokenStream){
		System.out.println("\n---> Parsing");
		
		Parser parser = new Parser(tokenStream);
		
		//parse the input
		SyntaxTree cst = parser.parse();
		
		
		System.out.println("\n---> Concrete Syntax Tree");
		//print the CST 
		cst.print(cst.getRoot(), 0);
		
		return cst;
	}
	
	private static void killCompilation(){
		System.out.println("Stopping Compilation!");
		System.exit(0);
	}
}
