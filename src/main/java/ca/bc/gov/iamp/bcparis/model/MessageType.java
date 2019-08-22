package ca.bc.gov.iamp.bcparis.model;

public enum MessageType {
	
	POR("BC41029"),
	REPORT(null),
	DRIVER("BC41027"),
	VEHICLE("BC41028"),
	SATELLITE("BC41127");
	
	private final String code;
	
	MessageType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
