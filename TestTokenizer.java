package project;

import java.util.*;

/**
 * This class allows you to test the class Tokenizer.
 */
public class TestTokenizer {

	public static void main(String[] args) throws Exception {
		Scanner console = new Scanner(System.in);

		System.out.print("> ");
		String input = console.nextLine();

		while ( ! input.equals("stop") ) {
			try {
				// builds a new tokenizer with the string input from the user
				Tokenizer tokenizer = new Tokenizer(input);
				// reads and prints all the token from the tokenizer
				while ( tokenizer.hasNextToken() ) {
					Token t = tokenizer.peekNextToken(); // peekNextToken returns the next token
					System.out.print(t + " : type is "); // Using the toString method on Token
					if ( t.isOperator() )
						System.out.println("OPERATOR, operator is " + t.getOperator());
					else if ( t.isDelimiter() )
						System.out.println("DELIMITER, delimiter is " + t.getDelimiter());
					else if ( t.isIdentifier() )
						System.out.println("IDENTIFIER, identifier is " + t.getIdentifier());
					else if ( t.isString() )
						System.out.println("STRING, string is " + t.getString());
					else if ( t.isNumber() )
						System.out.println("NUMBER, number is " + t.getNumber());
					else
						System.out.println("EQUAL");
					tokenizer.readNextToken(); // readNextToken returns the next token
				}							   // and removes it from the tokenizer
				System.out.println();
			}
			catch ( Exception e ) {
				System.out.println("\nERROR: " + e.getMessage());
			}
			System.out.print("> ");
			input = console.nextLine();
		}
	}
}
