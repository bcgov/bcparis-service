package ca.bc.gov.iamp.bcparis.exception.rest;

public class ResponseCodeNotExpected extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public ResponseCodeNotExpected(String message) {
		super(message);
	}
	
}
