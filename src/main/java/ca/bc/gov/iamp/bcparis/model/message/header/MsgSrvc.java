package ca.bc.gov.iamp.bcparis.model.message.header;

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
public class MsgSrvc implements Serializable{

	private static final long serialVersionUID = -515913362892074269L;
	
	@JsonProperty(value="msgActn")
	public String msgActn;
	
}
