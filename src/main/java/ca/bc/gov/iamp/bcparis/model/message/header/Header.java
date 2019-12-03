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
public class Header implements Serializable{

	private static final long serialVersionUID = 7760462111504061385L;

	@JsonProperty(value="CPICVer")
	public String cpicVer;
	
	@JsonProperty(value="UDF")
	public String udf;
	
	@JsonProperty(value="Priority")
	public String priority;
	
	@JsonProperty(value="MsgSrvc")
	public MsgSrvc msgSrvc;
	
	@JsonProperty(value="Routing")
	public Routing routing;
	
	@JsonProperty(value="Origin")
	public Origin origin;
	
	@JsonProperty(value="Role")
	public String role;
	
	@JsonProperty(value="AgencyId")
	public String agencyId;
	
	@JsonProperty(value="UserId")
	public String userId;
	
	@JsonProperty(value="DeviceId")
	public String deviceId;
}
