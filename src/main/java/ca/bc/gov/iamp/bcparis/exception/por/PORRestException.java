package ca.bc.gov.iamp.bcparis.exception.por;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.iamp.bcparis.exception.layer7.Layer7RestException;

public class PORRestException extends RuntimeException{

	private static final long serialVersionUID = 217462322973509320L;

	private static final String BASE_MESSAGE = "Exception to call #POR Rest service #";
	private String responseContent;
	private final Logger log = LoggerFactory.getLogger(PORRestException.class);
	
	public PORRestException(String secondLineMessage, String responseContent, Throwable cause) {
		super(BASE_MESSAGE + "\n" + secondLineMessage, cause);
		this.responseContent = responseContent;
		log.error(BASE_MESSAGE);
	}
	
	public PORRestException(String secondLineMessage, String responseContent) {
		super(BASE_MESSAGE + "\n" + secondLineMessage);
		this.responseContent = responseContent;
	}
	
	public String getResponseContent() {
		return responseContent;
	}

}
/* changes in the code */