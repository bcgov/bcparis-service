package ca.bc.gov.iamp.bcparis.api.exception;

import java.util.List;

public class BadRequestException extends ApiRuntimeException {

	private static final long serialVersionUID = 1L;

	public BadRequestException(String message) {
		super(message);
	}

	public BadRequestException(Throwable cause) {
		super(cause);
	}
	public BadRequestException(String message, Throwable cause, String error) {
		super(message, cause, error);
	}

	public BadRequestException(String message, Throwable cause, List<String> errors) {
		super(message, cause, errors);
	}

}
