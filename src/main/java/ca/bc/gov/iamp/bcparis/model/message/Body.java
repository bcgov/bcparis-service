package ca.bc.gov.iamp.bcparis.model.message;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude="CDATAAttributes")
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
		if(opt.isPresent()) {
			String[] splited = opt.get().split(":");
			return splited.length == 2 ? splited[1] : "";
		}else 
			return "";
	}
	
	public boolean containAttribute(final String attributeName) {
		return msgFFmt.contains(attributeName + ":");
	}
	
	@JsonIgnore
	public List<String> getCDATAAttributes() {
		return CDATAAttributes;
	}
	

	/**
	 * Get the SNME line
	 * @param attributeName
	 * @return
	 */
	public String getSNME() {
		final String START = "SNME:";
		String result = cutFromCDATA(START, "\n");
		return StringUtils.isEmpty(result) ? cutFromCDATA(START, "\\n") : result;
	}
	
	/**
	 * Get the DL line
	 * @param attributeName
	 * @return
	 */
	public String getDL() {
		final String START = "DL:";
		String result = cutFromCDATA(START, "\n");
		return StringUtils.isEmpty(result) ? cutFromCDATA(START, "\\n") : result;
	}
	
	public String cutFromCDATA(final String START, final String END) {
		final int beginIndex = msgFFmt.indexOf(START);
		final int endIndex = msgFFmt.indexOf(END, beginIndex);
		return (beginIndex != -1 && endIndex != -1)
				? msgFFmt.substring(beginIndex + START.length(), endIndex)
				: "";
	}
}
