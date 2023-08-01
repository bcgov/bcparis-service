package ca.bc.gov.iamp.bcparis.exception.icbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.iamp.bcparis.api.MessageApi;

public class ICBCRestException extends RuntimeException {

	private static final long serialVersionUID = -6290556125571489942L;
	
	private static final String BASE_MESSAGE = " Exception to call #ICBC Rest Service#.";
	private String responseContent;
	private final Logger log = LoggerFactory.getLogger(ICBCRestException.class);

	public ICBCRestException(String secondLineMessage, String responseContent, Throwable cause) {
		super(BASE_MESSAGE + "\n" + secondLineMessage, cause);
		this.responseContent = responseContent;
		log.error(BASE_MESSAGE);
	}
	
	public String getResponseContent() {
		return responseContent;
	}
	
}
