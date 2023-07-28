package ca.bc.gov.iamp.bcparis.exception.icbc;


public class ICBCRestException extends RuntimeException {

	private static final long serialVersionUID = -6290556125571489942L;
	
	private static final String BASE_MESSAGE = "Exception to call #ICBC Rest Service#.";
	private String responseContent;

	public ICBCRestException(String secondLineMessage, String responseContent, Throwable cause) {
		super(BASE_MESSAGE + "\n" + secondLineMessage, cause);
		this.responseContent = responseContent;
	}
	
	public String getResponseContent() {
		return responseContent;
	}
	
}
