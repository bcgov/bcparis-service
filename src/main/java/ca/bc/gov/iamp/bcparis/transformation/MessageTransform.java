package ca.bc.gov.iamp.bcparis.transformation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ca.bc.gov.iamp.bcparis.exception.message.MessageTransformationException;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.util.XMLUtil;

@Service
public class MessageTransform {

	private final Logger log = LoggerFactory.getLogger(MessageTransform.class);
	
	public Layer7Message parse(Layer7Message message) {
		log.info("Message parsing.");

		message = parseCDATA(message);
		
		log.info("Message parsing completed. CDATA=" + message.getEnvelope().getBody().getCDATAAttributes());
		
		return message;
	}
	
	private Layer7Message parseCDATA(Layer7Message message) {
		try{
			String msgFFmt = message.getEnvelope().getBody().getMsgFFmt();
			
			String CDATA = XMLUtil.extractCDATAFromMsgFFmt(msgFFmt);
			
			List<String> attributes = extractCDATAAttributes(CDATA);
			
			message.getEnvelope().getBody().setCDATAAttributes(attributes);
			
			return message;
		}catch (Exception e) {
			throw new MessageTransformationException(e);
		}
	}
	
	private List<String> extractCDATAAttributes(String text){
		final String ATTRIBUTES = "SN:|MT:|MSID:|FROM:|TO:|SUBJ:|TestRNS:|";
		final String TEXT_ATTRIBUTES = "TEXT:|RE:|SNME:|G1:|DOB:|LIC:|RVL";
		
		List<String> attributes = new ArrayList<>();
		
		List<Integer> attributeIndex = new ArrayList<>();
		Matcher matcher = Pattern.compile(ATTRIBUTES + TEXT_ATTRIBUTES).matcher(text);
		
		// Extract the attributes index from the string.
		while(matcher.find())
			attributeIndex.add( matcher.start() );
		
		// Iterate parsing all attributes
		for (int i = 0; i < attributeIndex.size(); i++) {
			Integer start = attributeIndex.get(i);
			Integer end = (i <  attributeIndex.size() -1) ? attributeIndex.get(i+1) : text.length();
	
			String attributeKeyValue = text.substring(start, end);
			if(attributeKeyValue.contains("/"))
				Arrays.asList(attributeKeyValue.split("/")).forEach(attributes::add);
			else {
				attributes.add(attributeKeyValue.trim());
			}
		} 

		return attributes;
	}

}
