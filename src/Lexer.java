import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.*;


/*
 * Lexer Class
 * Class to break a source file down into a token stream to be passed on to the parser.
 * Handles source file input and line number tracking.
 */
public class Lexer {
	private static Scanner scanner;

	
	//takes in a source file, and passes them on to the tokenizer.
	public static void main(String[] args	){
		try {
			//source file to be translated to target
			//for execution in command prompt
			File source = new File(args[0]);
		
			scanner = new Scanner(source);
			
			ArrayList<String> tokens = new ArrayList<String>();
	
			//iterate over source file
			while(scanner.hasNext()){
				tokens.add(scanner.next());			
			}
			
			Tokenizer tokenizer = new Tokenizer(tokens);
			
			tokenizer.run();
			
			scanner.close();
		} catch(Exception ex){
			System.out.println(ex);	
		}
	}

}
