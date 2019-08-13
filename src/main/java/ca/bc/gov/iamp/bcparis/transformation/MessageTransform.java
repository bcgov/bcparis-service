package ca.bc.gov.iamp.bcparis.transformation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ca.bc.gov.iamp.bcparis.exception.message.MessageTransformException;
import ca.bc.gov.iamp.bcparis.model.message.Body;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.util.XMLUtil;

@Service
public class MessageTransform {

	private final Logger log = LoggerFactory.getLogger(MessageTransform.class);

	public Layer7Message parse(Layer7Message message) {
		log.info("Message parsing.");

		message = removeCDATA(message);
		message = parseCDATA(message);

		log.info("Message parsing completed. CDATA=" + message.getEnvelope().getBody().getCDATAAttributes());

		return message;
	}

	/**
	 * Remove the CDATA tags
	 * 
	 * @param msgFFmt
	 *            Example: <![CDATA[\rSN:M00001-0001 MT:MUF
	 *            MSID:BRKR-190703-20:40:43\nFROM:BC41127\nTO:BC41028]]>
	 * @return Example: SN:M00001-0001 MT:MUF
	 *         MSID:BRKR-190703-20:40:43\nFROM:BC41127\nTO:BC41028
	 */
	private Layer7Message removeCDATA(Layer7Message message) {
		Body body = message.getEnvelope().getBody();
		String msgFFmt = XMLUtil.extractCDATAFromMsgFFmt(body.getMsgFFmt());
		body.setMsgFFmt(msgFFmt);
		return message;
	}

	private Layer7Message parseCDATA(Layer7Message message) {
		try {
			String msgFFmt = message.getEnvelope().getBody().getMsgFFmt();

			List<String> attributes = extractCDATAAttributes(msgFFmt);

			message.getEnvelope().getBody().setCDATAAttributes(attributes);

			return message;
		} catch (Exception e) {
			throw new MessageTransformException(e);
		}
	}

	public static List<String> extractCDATAAttributes(String text) {

		final String ATTRIBUTES = "SN:|MT:|MSID:|FROM:|TO:|SUBJ:|" + "TEXT:|RE:|" + "TestRNS:|" // Satellite test
				+ "G1:|G2:|G3:|DOB:|DL:|" // POR
				+ "SNME:|DL:|" // Driver
				+ "LIC:|ODN:|TAG:|FLC:|VIN:|REG:|RNS:|RVL:|" // Vehicle
				+ "\\n|\\\\n|\n\n|\\\\n\\\\n"; // New line and New line escaped (backslash)

		List<String> attributes = new ArrayList<>();

		List<Integer> attributeIndex = new ArrayList<>();
		Matcher matcher = Pattern.compile(ATTRIBUTES).matcher(text);

		// Extract the attributes index from the string.
		while (matcher.find())
			attributeIndex.add(matcher.start());

		// Iterate parsing all attributes
		for (int i = 0; i < attributeIndex.size(); i++) {
			Integer start = attributeIndex.get(i);
			Integer end = (i < attributeIndex.size() - 1) ? attributeIndex.get(i + 1) : text.length();

			String attributeKeyValue = text.substring(start, end);
			if (attributeKeyValue.contains("/"))
				Arrays.asList(attributeKeyValue.split("/")).forEach(attributes::add);
			else {
				attributes.add(attributeKeyValue.trim());
			}
		}

		return attributes;
	}

}
