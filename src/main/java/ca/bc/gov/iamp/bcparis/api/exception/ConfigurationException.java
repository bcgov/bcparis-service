package ca.bc.gov.iamp.bcparis.api.exception;

import java.util.List;

public class ConfigurationException extends ApiRuntimeException {

	private static final long serialVersionUID = 1L;

	public ConfigurationException(String message) {
		super(message);
	}

	public ConfigurationException(Throwable cause) {
		super(cause);
	}

	public ConfigurationException(String message, Throwable cause, String error) {
		super(message, cause, error);
	}

	public ConfigurationException(String message, Throwable cause, List<String> errors) {
		super(message, cause, errors);
	}

}
