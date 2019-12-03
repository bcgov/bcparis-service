package ca.bc.gov.iamp.bcparis.exception.message;

public class MessageTransformException extends RuntimeException{

	private static final long serialVersionUID = -206132037125650949L;

	public MessageTransformException(Throwable cause) {
		super(cause);
	}

	public MessageTransformException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
