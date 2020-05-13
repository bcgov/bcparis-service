package ca.bc.gov.iamp.bcparis.api.exception;

import java.util.List;

public class InputValidationException extends ApiRuntimeException {

	private static final long serialVersionUID = 1L;

	public InputValidationException(String message) {
		super(message);
	}

	public InputValidationException(Throwable cause) {
		super(cause);
	}
	public InputValidationException(String message, Throwable cause, String error) {
		super(message, cause, error);
	}

	public InputValidationException(String message, Throwable cause, List<String> errors) {
		super(message, cause, errors);
	}

}
