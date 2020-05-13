package ca.bc.gov.iamp.bcparis.api.exception;

import java.util.List;

public class ObjectNotFoundException extends ApiRuntimeException {

	private static final long serialVersionUID = 1L;

	public ObjectNotFoundException(String message) {
		super(message);
	}

	public ObjectNotFoundException(Throwable cause) {
		super(cause);
	}

	public ObjectNotFoundException(String message, Throwable cause, String error) {
		super(message, cause, error);
	}

	public ObjectNotFoundException(String message, Throwable cause, List<String> errors) {
		super(message, cause, errors);
	}

}
