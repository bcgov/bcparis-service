package ca.bc.gov.iamp.bcparis.api.exception;

import java.util.List;

public class ServiceInternalException extends ApiRuntimeException {

	private static final long serialVersionUID = 1L;

	public ServiceInternalException(String message) {
		super(message);
	}

	public ServiceInternalException(Throwable cause) {
		super(cause);
	}
	public ServiceInternalException(String message, Throwable cause, String error) {
		super(message, cause, error);
	}

	public ServiceInternalException(String message, Throwable cause, List<String> errors) {
		super(message, cause, errors);
	}

}
