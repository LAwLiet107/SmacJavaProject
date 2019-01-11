package project;

/**
 * A class for the tokens returned by a tokenizer.
 * A token is an atomic unit read from the user input.
 * YOU SHOULD NOT CHANGE THIS FILE
 */
public class Token {

	/**
	 * checks if this is the token '='
	 */
	public boolean isEqual() {
		return type == TokenType.EQUAL;
	}

	/**
	 * checks if this is a number token
	 */
	public boolean isNumber() {
		return type == TokenType.NUMBER;
	}

	/**
	 * if this is a number token, returns the
	 * number as a double, throws a TokenException
	 * otherwise
	 */
	public double getNumber() throws TokenException {
		if ( type != TokenType.NUMBER )
			throw new TokenException("token method mismatch: " + type + ".getDouble()");
		return number;
	}

	/**
	 * checks if this is a string token
	 */
	public boolean isString() {
		return type == TokenType.STRING;
	}

	/**
	 * if this is a string token, returns the
	 * string as a String, throws a TokenException
	 * otherwise
	 */
	public String getString() throws TokenException {
		if ( type != TokenType.STRING )
			throw new TokenException("token method mismatch: " + type + ".getString()");
		return string;
	}

	/**
	 * checks if this is an identifier token
	 */
	public boolean isIdentifier() {
		return type == TokenType.IDENTIFIER;
	}

	/**
	 * if this is an identifier token, returns the
	 * identifier as a String, throws a TokenException
	 * otherwise
	 */
	public String getIdentifier() throws TokenException {
		if ( type != TokenType.IDENTIFIER )
			throw new TokenException("token method mismatch: " + type + ".getIdentifier()");
		return string;
	}

	/**
	 * checks if this is a delimiter token
	 */
	public boolean isDelimiter() {
		return type == TokenType.OPENPAR || type == TokenType.CLOSEPAR || type == TokenType.COMMA;
	}

	/**
	 * if this is a delimiter token, returns the
	 * delimiter as a String, throws a TokenException
	 * otherwise
	 */
	public String getDelimiter() throws TokenException {
		switch ( type ) {
		case OPENPAR: return "(";
		case CLOSEPAR: return ")";
		case COMMA: return ",";
		default: throw new TokenException("token method mismatch: " + type + ".getDelimiter()");
		}
	}

	/**
	 * checks if this is an operator token
	 */
	public boolean isOperator() {
		return type == TokenType.PLUS || type == TokenType.MINUS || type == TokenType.UNARYMINUS ||
			   type == TokenType.TIMES || type == TokenType.DIVIDE || type == TokenType.POWER;
	}

	/**
	 * if this is an operator token, returns the
	 * operator as a String, throws a TokenException
	 * otherwise
	 */
	public String getOperator() throws TokenException {
		switch ( type ) {
		case PLUS: return "+";
		case MINUS: return "-";
		case UNARYMINUS: return "~";
		case TIMES: return "*";
		case DIVIDE: return "/";
		case POWER: return "^";
		default: throw new TokenException("token method mismatch: " + type + ".getOperator()");
		}
	}

	/**
	 * returns a string representation
	 * of the token this
	 */
	public String toString() {
		String s = "[";
		switch ( type ) {
		case NUMBER: s += number; break;
		case IDENTIFIER:
		case STRING: s += string; break;
		default: s += type;
		}
		return s + "]";
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// the token types
	private enum TokenType {
		NUMBER, IDENTIFIER, STRING, EQUAL, PLUS, MINUS, UNARYMINUS, TIMES, DIVIDE, POWER, OPENPAR, CLOSEPAR, COMMA
	}

	private TokenType type; // the type of the token
	private double number;  // the double value (when the token is a number)
	private String string;  // the String value (when the token is an identifier or a string)

	// constants defining the operators and delimiters (so we don't
	// reallocate memory each time we make a new operator or delimiter token)
	private static final Token equalToken = new Token(TokenType.EQUAL);
	private static final Token plusToken = new Token(TokenType.PLUS);
	private static final Token minusToken = new Token(TokenType.MINUS);
	private static final Token unaryminusToken = new Token(TokenType.UNARYMINUS);
	private static final Token timesToken = new Token(TokenType.TIMES);
	private static final Token divideToken = new Token(TokenType.DIVIDE);
	private static final Token powerToken = new Token(TokenType.POWER);
	private static final Token openparToken = new Token(TokenType.OPENPAR);
	private static final Token closeparToken = new Token(TokenType.CLOSEPAR);
	private static final Token commaToken = new Token(TokenType.COMMA);

	// private constructor to
	// create an token operator
	private Token(TokenType type) {
		this.type = type;
	}

	// private constructor to make
	// a token number
	private Token(double number) {
		this(TokenType.NUMBER);
		this.number = number;
	}

	// private constructor to make
	// a token string
	private Token(TokenType type, String string) {
		this(type);
		this.string = string;
	}


	///// These methods are used to create tokens
	///// You don't need to use these methods (they
	///// are used by the class Tokenizer)
	public static Token makeNUMBER(double number) {
		return new Token(number);
	}
	public static Token makeIDENTIFIER(String string) {
		return new Token(TokenType.IDENTIFIER, string);
	}
	public static Token makeSTRING(String string) {
		return new Token(TokenType.STRING, string);
	}
	public static Token makeEQUAL() {
		return equalToken;
	}
	public static Token makeOPENPAR() {
		return openparToken;
	}
	public static Token makeCLOSEPAR() {
		return closeparToken;
	}
	public static Token makeCOMMA() {
		return commaToken;
	}
	public static Token makeMINUS() {
		return minusToken;
	}
	public static Token makeUNARYMINUS() {
		return unaryminusToken;
	}
	public static Token makeTIMES() {
		return timesToken;
	}
	public static Token makeDIVIDE() {
		return divideToken;
	}
	public static Token makePOWER() {
		return powerToken;
	}
	public static Token makePLUS() {
		return plusToken;
	}
}
