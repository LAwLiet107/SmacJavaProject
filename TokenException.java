package project;

/**
 * A class to handle Token exceptions. Token exceptions
 * are thrown in case of Token method misuse. Consequently
 * those exception should NEVER be thrown in your final
 * version of SMAC because they are "internal" errors
 */
public class TokenException extends Exception {

	public TokenException(String msg) {
		super(msg);
	}
}
