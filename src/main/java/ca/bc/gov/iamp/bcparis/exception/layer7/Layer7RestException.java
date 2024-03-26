package ca.bc.gov.iamp.bcparis.exception.layer7;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.iamp.bcparis.exception.icbc.ICBCRestException;

public class Layer7RestException extends RuntimeException {

	private static final long serialVersionUID = 3151843112893690400L;

	private static final String BASE_MESSAGE = "Exception to call #Layer 7 REST service#.";
	private String responseContent;
	private final Logger log = LoggerFactory.getLogger(Layer7RestException.class);
	
	public Layer7RestException(String secondLineMessage, String responseContent, Throwable cause) {
		super(BASE_MESSAGE + "\n" + secondLineMessage, cause);
		this.responseContent = responseContent;
		log.error(BASE_MESSAGE);
	}
	
	public String getResponseContent() {
		return responseContent;
	}
}
 /* changes in the code */