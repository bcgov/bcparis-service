package ca.bc.gov.iamp.bcparis.exception.rest;

public class RestException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public RestException(String message) {
		super(message);
	}
	
	public RestException(String message, Throwable cause) {
		super(message, cause);
	}
}
