package ca.bc.gov.iamp.bcparis.exception.por;


public class PORRestException extends RuntimeException{

	private static final long serialVersionUID = 217462322973509320L;

	private static final String BASE_MESSAGE = "Exception to call #POR Rest service #";
	private String responseContent;
	
	public PORRestException(String secondLineMessage, String responseContent, Throwable cause) {
		super(BASE_MESSAGE + "\n" + secondLineMessage, cause);
		this.responseContent = responseContent;
	}
	
	public PORRestException(String secondLineMessage, String responseContent) {
		super(BASE_MESSAGE + "\n" + secondLineMessage);
		this.responseContent = responseContent;
	}
	
	public String getResponseContent() {
		return responseContent;
	}

}
