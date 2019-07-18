package ca.bc.gov.iamp.bcparis.exception.rest;

public class MessagePostException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public MessagePostException(String message) {
		super(message);
	}
	
	public MessagePostException(String message, Throwable cause) {
		super(message, cause);
	}
}
