package project;

/**
 * A class to handle lexical exception
 */
public class LexicalErrorException extends Exception {

	public LexicalErrorException(String msg) {
		super(msg);
	}

	public String toString() {
		return "Lexical error: " + getMessage();
	}
}
