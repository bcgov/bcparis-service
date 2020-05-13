package ca.bc.gov.iamp.bcparis.api.exception;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

@Getter
public class ApiRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private List<String> errors;

	public ApiRuntimeException() {
		super();
	}

	public ApiRuntimeException(String message) {
		super(message);
	}

	public ApiRuntimeException(String message, String error) {
		this(message, null, error);
	}

	public ApiRuntimeException(String message, List<String> errors) {
		this(message, null, errors);
	}

	public ApiRuntimeException(Throwable cause) {
		super(cause);
	}

	public ApiRuntimeException(Throwable cause, String error) {
		this(null, cause, error);
	}

	public ApiRuntimeException(Throwable cause, List<String> errors) {
		this(null, cause, errors);
	}

	public ApiRuntimeException(String message, Throwable cause, String error) {
		this(message, cause, Arrays.asList(error));
	}

	public ApiRuntimeException(String message, Throwable cause, List<String> errors) {
		super(message, cause);
		this.errors = errors;
	}
}
