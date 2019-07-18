package ca.bc.gov.iamp.bcparis.model.por;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PORResult implements Serializable {

	private static final long serialVersionUID = 8046973615510061644L;
	
	@JsonProperty(value="ind_data")
	private String indData;

	@Override
	public String toString() {
		return "PORResult [indData=" + indData + "]";
	}
	
}
