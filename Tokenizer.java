package project;

/**
 * This class implements a tokenizer. A tokenizer is used to
 * split a string (user input) in tokens
 * YOU SHOULD NOT CHANGE THIS FILE
 */
public class Tokenizer {

	// the string to tokenize
	private String buffer;
	// the current index in the string
	private int index;
	// the current token
	private Token current;

	/**
	 * create a tokenizer on the string 'input'
	 */
	public Tokenizer(String input) throws TokenException, LexicalErrorException {
		buffer = input;
		index = 0;
		moveToNextToken();
		current = getNextToken();
	}

	/**
	 * checks if there is more token to read
	 */
	public boolean hasNextToken() {
		return current != null;
	}

	/**
	 * returns the next token to be read
	 */
	public Token peekNextToken() throws TokenException {
		if ( current == null )
			throw new TokenException("no more token");
		return current;
	}

	/**
	 * reads and returns the next token to be read
	 * (i.e. the next token is removed from the tokenizer)
	 */
	public Token readNextToken() throws TokenException, LexicalErrorException {
		if ( current == null )
			throw new TokenException("no more token");
		Token tmp = current;
		current = getNextToken();
		return tmp;
	}

	/////////////////////////////////////////////////////////////

	private void moveToNextToken() {
		char c = 0;
		while ( index < buffer.length() && ( ( c = buffer.charAt(index) ) == ' ' || c == '\t' ) )
			index++;
	}

	private Token getNextToken() throws TokenException, LexicalErrorException {
		if ( index >= buffer.length() )
			return null;
		char c = buffer.charAt(index);
		if ( isLetter(c) )
			return nextIdentifier();
		if ( isDigit(c) )
			return nextNumber();
		if ( isDelimiter(c) )
			return nextDelimiter();
		if ( isOperator(c) )
			return nextOperator();
		if ( c == '"' )
			return nextString();
		if ( c == '=' )
			return nextEqual();
		current = null;
		throw new TokenException("illegal character: " + c);
	}

	private boolean isLetter(char c) {
		return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
	}

	private boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

	private boolean isDelimiter(char c) {
		return c == '(' || c == ')' || c == ',';
	}

	private boolean isOperator(char c) {
		return c == '+' || c =='-' || c == '*' || c == '/' || c == '^';
	}

	private Token nextNumber() throws LexicalErrorException {
		int i = index++;
		int dots = 0;
		char c = 0;
		while ( index < buffer.length() && isDigit(c = buffer.charAt(index)) || c == '.' ) {
			if ( c == '.' )
				dots++;
			if ( dots > 1 ) {
				int j = index;
				current = null;
				throw new LexicalErrorException("malformed number: " + buffer.substring(i, j + 1));
			}
			index++;
		}
		int j = index;
		moveToNextToken();
		return Token.makeNUMBER(Double.parseDouble(buffer.substring(i, j)));
	}

	private Token nextIdentifier() {
		int i = index++;
		char c = 0;
		while ( index < buffer.length() && ( isLetter(c = buffer.charAt(index)) || isDigit(c) ) )
			index++;
		int j = index;
		moveToNextToken();
		return Token.makeIDENTIFIER(buffer.substring(i, j));
	}

	private Token nextString() throws LexicalErrorException {
		int i = ++index;
		while ( index < buffer.length() && buffer.charAt(index) != '"' )
			index++;
		if ( index >= buffer.length() ) {
			int j = index;
			index = buffer.length();
			current = null;
			throw new LexicalErrorException("malformed string: " + buffer.substring(i-1, j));
		}
		int j = index++;
		moveToNextToken();
		return Token.makeSTRING(buffer.substring(i, j));
	}

	private Token nextDelimiter() {
		Token td = null;
		switch ( buffer.charAt(index++) ) {
			case '(' : td = Token.makeOPENPAR(); break;
			case ')' : td = Token.makeCLOSEPAR(); break;
			default :  td = Token.makeCOMMA();
		}
		moveToNextToken();
		return td;
	}

	private Token nextOperator() {
		Token to = null;
		switch ( buffer.charAt(index++) ) {
			case '+' : to = Token.makePLUS(); break;
			case '-' : if ( isBinaryMinus() )
						   to = Token.makeMINUS();
					   else
						   to = Token.makeUNARYMINUS();
					   break;
			case '*' : to = Token.makeTIMES(); break;
			case '/' : to = Token.makeDIVIDE(); break;
			default :  to = Token.makePOWER();
		}
		moveToNextToken();
		return to;
	}

	private boolean isBinaryMinus() {
		try {
			return current != null &&
				   ( current.isIdentifier() || current.isNumber() ||
				     ( current.isDelimiter() && current.getDelimiter().equals(")") )
				   );
		}
		catch ( Exception e ) {
			return false;
		}
	}

	private Token nextEqual() {
		index++;
		moveToNextToken();
		return Token.makeEQUAL();
	}
}
