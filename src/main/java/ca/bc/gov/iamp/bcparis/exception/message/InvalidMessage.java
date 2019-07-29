package ca.bc.gov.iamp.bcparis.exception.message;

public class InvalidMessage extends RuntimeException{

	private static final long serialVersionUID = 4101415006761946564L;

	public InvalidMessage(String message) {
		super(message);
	}
	
}
