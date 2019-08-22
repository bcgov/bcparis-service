package ca.bc.gov.iamp.bcparis.model.message;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import ca.bc.gov.iamp.bcparis.exception.message.InvalidMessageType;
import ca.bc.gov.iamp.bcparis.model.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Layer7Message implements Serializable{

	private static final long serialVersionUID = -2139629085388836915L;
	
	
	@JsonProperty(value="Envelope")
	private Envelope envelope;
	
	@JsonIgnore
	public MessageType getMessageType() {

		if(isReport()) {
			return MessageType.REPORT;
		}
		else {
			final String TO = this.getEnvelope().getBody().getCDATAAttribute("TO");
			switch (TO) {
				case "BC41027": return MessageType.DRIVER;
				case "BC41028": return MessageType.VEHICLE;
				case "BC41029": return MessageType.POR;
				case "BC41127": return MessageType.SATELLITE;
				default:
					throw new InvalidMessageType("Invalid Message type. TO=" + TO);
			}
		}	
	}
	
	@JsonIgnore
	public boolean isReport() {
		return "Report".equalsIgnoreCase(this.getEnvelope().getMqmd().getMessageType());
	}

	@Override
	public String toString() {
		return "Layer7Message [envelope=" + envelope + "]";
	}
	
	

}
