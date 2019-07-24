package ca.bc.gov.iamp.bcparis.model.message;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude="CDATAAttributes")
public class Body implements Serializable{

	private static final long serialVersionUID = 4946604044695860019L;
	
	@JsonProperty(value="MsgFFmt")
	private String msgFFmt;
	
	@JsonIgnore
	private List<String> CDATAAttributes;
	
	/**
	 * Return CDATA attribute value
	 * 
	 * @param attributeName the name of the attribute.
	 * @return the attribute value
	 */
	public String getCDATAAttribute(final String attributeName) {
		final String ATTR_WITH_DELIMITER  = attributeName.toUpperCase() + ":";
		Optional<String> opt = CDATAAttributes.stream().filter( attr->attr.startsWith(ATTR_WITH_DELIMITER)  ).findFirst();
		return opt.isPresent() ? opt.get().split(":")[1] : "";
	}
	
	@JsonIgnore
	public List<String> getCDATAAttributes() {
		return CDATAAttributes;
	}
	

	/**
	 * Get the full SNME line
	 * @param attributeName
	 * @return
	 */
	public String getSNME() {
		final String START = "SNME:";
		final String END = "\n";
		
		return null;
	}
}
