package ca.bc.gov.iamp.bcparis.api.exception;

import java.util.List;

public class ServiceUnavailableException extends ApiRuntimeException {

	private static final long serialVersionUID = 1L;

	public ServiceUnavailableException(String message) {
		super(message);
	}

	public ServiceUnavailableException(Throwable cause) {
		super(cause);
	}
	public ServiceUnavailableException(String message, Throwable cause, String error) {
		super(message, cause, error);
	}

	public ServiceUnavailableException(String message, Throwable cause, List<String> errors) {
		super(message, cause, errors);
	}

}
