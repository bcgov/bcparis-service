package ca.bc.gov.iamp.bcparis.api.exception.handling;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ApiError {

	private int statusCode;
	private HttpStatus status;
	private String message;
	private List<String> errors;

	public ApiError(HttpStatus status, String message, String error) {
		this(status, message, Arrays.asList(error));
	}

	public ApiError(HttpStatus status, String message, List<String> errors) {
		super();
		this.status = status;
		this.statusCode = status.value();
		this.message = message;
		this.errors = errors;
	}
}
