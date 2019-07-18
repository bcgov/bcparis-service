package ca.bc.gov.iamp.bcparis.api.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Error implements Serializable{

	private static final long serialVersionUID = -2955804452006849998L;
	private String details;
	
	public Error(String details) {
		this.details = details;
	}

}
