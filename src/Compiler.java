import java.io.File;
import java.util.Scanner;


/*
 * Lexer Class
 * Class to break a source file down into a token stream to be passed on to the parser.
 * Handles source file input and line number tracking.
 */
public class Compiler {
	private static Scanner scanner;

	
	//takes in a source file, and passes them on to the tokenizer.
	public static void main(String[] args	){
		
		
		
//////////LEXOGRAPHIC ANALYZER///////////////////////////////		
		try {
			//source file to be translated to target
			//for execution in command prompt
			File source = new File(args[0]);
		
			scanner = new Scanner(source);
		
			

			Lexer lex= new Lexer();
				
			
			//iterate over source file
			while(scanner.hasNextLine()){
				lex.run(scanner.nextLine());
			}
			
			scanner.close();
		} catch(Exception ex){
			System.out.println("error found in main lex function");
			System.out.println(ex);	
		}
	}

}
