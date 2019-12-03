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
public class Origin implements Serializable{

	private static final long serialVersionUID = -9218577737879956898L;

	@JsonProperty(value="QName")
	public String qname;
	
	@JsonProperty(value="QMgrName")
	public String qMgrName;
	
}
