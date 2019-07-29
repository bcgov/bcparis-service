package ca.bc.gov.iamp.bcparis.exception.layer7;

public class Layer7RestException extends RuntimeException {

	private static final long serialVersionUID = 3151843112893690400L;

	public Layer7RestException(String message, Throwable cause) {
		super(message, cause);
	}
}
