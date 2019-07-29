package ca.bc.gov.iamp.bcparis.model.message;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Envelope implements Serializable{

	private static final long serialVersionUID = 3135973817402771496L;
	
	@JsonProperty(value="Body")
	private Body body;
	
	@JsonProperty(value="MQMD")
	private MQMD mqmd;

}
