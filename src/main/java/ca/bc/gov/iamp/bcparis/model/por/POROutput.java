package ca.bc.gov.iamp.bcparis.model.por;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POROutput implements Serializable {

	private static final long serialVersionUID = 945391756494355778L;
	
	@JsonProperty(value="result_set")
	private List<PORResult> result;
	
	@JsonProperty(value="status_msg")
	private String statusMsg;

	@Override
	public String toString() {
		return "POROutput [result=" + result + "]";
	}
	
}
