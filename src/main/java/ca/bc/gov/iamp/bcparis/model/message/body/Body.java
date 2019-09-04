package ca.bc.gov.iamp.bcparis.model.message.body;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
@ToString(exclude="cdataAttributes")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Body implements Serializable{

	private static final long serialVersionUID = 4946604044695860019L;
	
	@JsonProperty(value="MsgFFmt")
	private String msgFFmt;
	
	@JsonIgnore
	private List<String> cdataAttributes;
	
	/**
	 * Return CDATA attribute value
	 * 
	 * @param attributeName the name of the attribute.
	 * @return the attribute value
	 */
	@JsonIgnore
	public String getCDATAAttribute(final String attributeName) {
		return getAttribute(cdataAttributes, attributeName);
	}
	
	/**
	 * Return the attribute value
	 * @param list
	 * 	Example:  [SNME:WISKIN, G1:TOMAS, G2:GEORGE, G3:ALPHONSE, DOB:20050505]
	 * @param attributeName
	 * 	Example: CNAME
	 * @return
	 * 	Example: WISKIN
	 */
	@JsonIgnore
	public String getAttribute(final List<String> list, final String attributeName) {
		final String ATTR_WITH_DELIMITER  = attributeName.toUpperCase() + ":";
		Optional<String> opt = list.stream().filter( attr->attr.startsWith(ATTR_WITH_DELIMITER)  ).findFirst();
		return opt.isPresent() ? opt.get().substring(opt.get().indexOf(":")+1) : "";
	}
	
	@JsonIgnore
	public List<String> getAttributeList(final String attributeName) {
		final String ATTR_WITH_DELIMITER  = attributeName.toUpperCase() + ":";
		Stream<String> opt = cdataAttributes.stream().filter( attr->attr.startsWith(ATTR_WITH_DELIMITER)  );
		return opt.collect(Collectors.toList()); 
	}
	
	/**
	 * Extract the SNME line
	 * @return
	 * Example: SNME:WISKIN/G1:TOMAS/G2:GEORGE/G3:ALPHONSE/DOB:20050505
	 */
	@JsonIgnore
	public List<String> getSNME() {
		String snmeLine = "SNME:" + getCDATAAttribute("SNME");
		return parseForwardSlash(snmeLine);
	}
	
	/**
	 * Parse a line containing many attributes
	 * @param line
	 * 	Example: SNME:WISKIN/G1:TOMAS/G2:GEORGE/G3:ALPHONSE/DOB:20050505
	 * @return
	 * 	Example: [SNME:WISKIN, G1:TOMAS, G2:GEORGE, G3:ALPHONSE, DOB:20050505]
	 */
	private List<String> parseForwardSlash(String line) {
		return Arrays.asList(line.split("/"));
	}
	
	public boolean containAttribute(final String attributeName) {
		return msgFFmt.contains(attributeName + ":");
	}
	
	@JsonIgnore
	public List<String> getCDATAAttributes() {
		return cdataAttributes;
	}
	
	/**
	 * Get the DL line
	 * @param attributeName
	 * @return
	 */
	@JsonIgnore
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
	
	public String cutFromCDATA(final String START) {
		final int beginIndex = msgFFmt.indexOf(START);
		return (beginIndex != -1) ? msgFFmt.substring(beginIndex) : "";
	}
}
