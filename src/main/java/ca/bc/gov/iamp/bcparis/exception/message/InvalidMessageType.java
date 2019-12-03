package ca.bc.gov.iamp.bcparis.exception.message;

public class InvalidMessageType extends RuntimeException{

	private static final long serialVersionUID = 4101415006761946564L;

	public InvalidMessageType(String message) {
		super(message);
	}
	
}
