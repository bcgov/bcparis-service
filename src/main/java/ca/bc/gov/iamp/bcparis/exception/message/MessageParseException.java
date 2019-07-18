package ca.bc.gov.iamp.bcparis.exception.message;

public class MessageParseException extends RuntimeException{

	private static final long serialVersionUID = 6419439448489467890L;

	public MessageParseException() {
		super();
	}

	public MessageParseException(String message, Throwable cause) {
		super(message, cause);
	}

}
