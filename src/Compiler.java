
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import semanticAnalysis.SemanticAnalyzer;
import lexnParse.Lexer;
import lexnParse.Parser;
import dataStructures.AbstractSyntaxTree;
import dataStructures.Token;
import dataStructures.Tree;

/**
 * 
 * 
 * @author Andrew
 * 
 * Main Compiler class
 *
 */
public class Compiler {
	private static Scanner scanner;

	
	/**
	 * Main Function
	 * performs compilation
	 * @param args the file arguments
	 */
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
			Tree cst = parseInput(tokenStream);
			
			//kill compilation if there were errors parse
			if(cst == null){
				killCompilation();
			}
			
			
			// TODO semantic analyze CST
			
			semanticAnalyze(cst);
			
			
		
			
		} catch(Exception ex){
			System.out.println("error found in main Compilation Function");
			System.out.println(ex);	
			ex.printStackTrace();
		}
	}
	
	/**
	 *Function to perform lexographic analysis
	 * 
	 * @param sourceFile the file to compile
	 * @return an arrayList of tokens, or null if something went wrong
	 */
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
			System.out.println("Error during Lex process");
			System.out.println(ex);
			ex.printStackTrace();
		}
		//returning null will stop Compilation
		return null;
	}
	
	
	/**
	 * parse the input, and return a CST to perform Semantic Analysis on
	 * @param tokenStream, the token stream generated from lex
	 * @return a cst generated during parse
	 */
	private static Tree parseInput(ArrayList<Token> tokenStream){
		try {
			
			Parser parser = new Parser(tokenStream);
			
			//parse the input
			Tree cst = parser.parse();		
			
			//return the cst if successful
			return cst;
		} catch (Exception e) {
			System.out.println("Error during Parse Phase");
			e.printStackTrace();
		}
		
		
		//return null if something went wrong
		return null;
	}
	
	
	/**
	 * 
	 * takes in a CST, generates an AST, and performs scope and type checking
	 * 
	 * @param cst the concrete syntax tree to be passed on to semantic analysis
	 * @return AbstractSyntaxTree ast or null if something went wrong	
	 */
	private static AbstractSyntaxTree semanticAnalyze(Tree cst){
		try {			
			SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(cst);
			
			semanticAnalyzer.analyze();
					
			return semanticAnalyzer.getAbstractSyntaxTree();
		} catch (Exception e) {
			System.out.println("Error during Semantic Analysis Phase");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/**
	 * Prints exit message and kills the program
	 */
	private static void killCompilation(){
		System.out.println("Stopping Compilation!");
		System.exit(0);
	}
}
