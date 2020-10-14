	
package ca.bc.gov.iamp.bcparis.message;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ca.bc.gov.iamp.bcparis.model.MessageType;
import ca.bc.gov.iamp.bcparis.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import ca.bc.gov.iamp.bcparis.exception.icbc.ICBCRestException;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.model.message.body.Body;
import ca.bc.gov.iamp.bcparis.repository.ICBCRestRepository;
import ca.bc.gov.iamp.bcparis.repository.query.IMSRequest;

@Service
public class VehicleProcessor implements DatagramProcessor{

	private final Logger log = LoggerFactory.getLogger(VehicleProcessor.class);
	
	@Autowired
	private ICBCRestRepository icbcRepository;
	
	@Autowired
	private MessageService messageService;

	@Override
	public MessageType getType() {
		return MessageType.VEHICLE;
	}

	public Layer7Message process(Layer7Message message) {
		log.info("Processing Vehicle message.");

		List<IMSRequest> requests = createIMSContent(message);
		if (requests.isEmpty()) {
			message.getEnvelope().getBody().setMsgFFmt(messageService.buildErrorResponse(message.getEnvelope().getBody(), "Unable to parse/formatting error"));
			log.warn("Processing Vehicle: Unable to parse/formatting error");
			return message;
		}
		try {

			List<String> responseParsed = requests.parallelStream()
				.map(request -> icbcRepository.requestDetails(message, request))
				.map( icbcResponse -> messageService.parseResponse(icbcResponse))
				.collect(Collectors.toList());

			message.getEnvelope().getBody().setMsgFFmt(messageService.buildResponse(message.getEnvelope().getBody(), String.join("\n\n", responseParsed)));

			log.info("Vehicle message processing completed.");
			return message;
			
		}catch (ICBCRestException e) {
			//TODO: This is for testing and the test should be updated to expect an exception
			String content = messageService.parseResponseError(e.getResponseContent());
			content = messageService.parseResponse(content);
			content = messageService.buildResponse(message.getEnvelope().getBody(), content);
			message.getEnvelope().getBody().setMsgFFmt(content);
			throw e;
		}
	}
	
	private List<IMSRequest> createIMSContent(Layer7Message message) {
		final List<IMSRequest> result = new ArrayList<>();
		
		final String icbcPayload = "${transactionName} HC ${fromORI} ${toORI} ${queryParams}";
		
		final Body body = message.getEnvelope().getBody();
		final String from = body.getCDATAAttribute("FROM");
		final String to = body.getCDATAAttribute("TO");
		
		final List<String> queryParams = messageService.getQueryAttributesList(
				message.getEnvelope().getBody(), 
				Arrays.asList("LIC", "ODN", "TAG", "FLC", "VIN", "REG", "RNS", "RVL"));
		
		for(String query : queryParams) {
			final String transaction = getTransaction(query);
			
			if(query.startsWith("RNS"))
				query = getRNSQuery(query);
			
			// Remove /h /H
			if(query.endsWith("/h") || query.endsWith("/H"))
				query = query.substring(0, query.length() - 2);
			
			// Add date and time
			query += "/" + getLocalTimeNowICBCFormat();
			
			final String imsContent = icbcPayload
					.replace("${transactionName}", transaction)
					.replace("${fromORI}", from)
					.replace("${toORI}", to)
					.replace("${queryParams}", query);
			
			result.add(IMSRequest.builder().imsRequest(imsContent).build());
		}
		
		return result;
	}
	
	/**
	 * @param query
	 * 		RNS:CAMERON/G1:JESSA/G2:/DOB:19890926/RSVP:16
	 * @return
	 * 		RNS:CAMERON/JESSA//1989-09-26/RSVP:16
	 */
	private String getRNSQuery(final String query) {
		final String surname 	= extractFromQuery(query, "RNS:");
		final String g1 		= extractFromQuery(query, "G1:");
		final String g2 		= extractFromQuery(query, "G2:");
		final String dob 		= formatDate(extractFromQuery(query, "DOB:"));
		final String rsvp 		= extractFromQuery(query, "RSVP:");
		
		String rnsQuery = String.format("RNS:%s/%s/%s/%s", surname, g1, g2, dob);
		
		if(!StringUtils.isEmpty(rsvp))
			rnsQuery += "/RSVP:" + rsvp;
		
		return rnsQuery;
	}
	
	private String formatDate(String date) {
		if(!StringUtils.isEmpty(date)) {
			return String.format("%s-%s-%s", date.substring(0, 4), date.substring(4, 6),  date.substring(6, 8));
		}
		return "";
	}
	
	private String extractFromQuery(final String query, final String attribute) {
		final int indexAttr = query.indexOf(attribute);
		if(indexAttr != -1) {
			final int indexEnd = query.indexOf("/", indexAttr);
			final int attrLength = attribute.length();
			if(indexEnd != -1)
				return query.substring(indexAttr + attrLength, indexEnd);
			else
				return query.substring(indexAttr + attrLength);
		}
		return "";
	}
	
	private String getTransaction(String query) {
		return ( query.toUpperCase().startsWith("RVL") || query.toUpperCase().startsWith("RNS"))
			? "JISTRN2" : "JISTRAN";
	}

	/**
	 * Returns Local Time in ICBC format
	 * @return
	 * 		ddMMMyy\HH:mm:ss
	 */
	private String getLocalTimeNowICBCFormat() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMMyy\\HH:mm:ss")).toString();
	}

}
