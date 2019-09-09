package ca.bc.gov.iamp.bcparis.exception.por;


public class PORRestException extends RuntimeException{

	private static final long serialVersionUID = 217462322973509320L;

	public PORRestException(String message, Throwable cause) {
		super(message, cause);
	}

	public PORRestException(String message) {
		super(message);
	}

}
