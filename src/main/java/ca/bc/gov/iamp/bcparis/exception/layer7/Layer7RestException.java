package ca.bc.gov.iamp.bcparis.exception.layer7;

public class Layer7RestException extends RuntimeException {

	private static final long serialVersionUID = 3151843112893690400L;

	private static final String BASE_MESSAGE = " Exception to call #Layer 7 REST service#";
	private String responseContent;
	
	public Layer7RestException(String secondLineMessage, String responseContent, Throwable cause) {
		super(BASE_MESSAGE + "\n" + secondLineMessage, cause);
		this.responseContent = responseContent;
	}
	
	public String getResponseContent() {
		return responseContent;
	}
}
