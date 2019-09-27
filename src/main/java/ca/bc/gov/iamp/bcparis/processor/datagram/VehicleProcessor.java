	
package ca.bc.gov.iamp.bcparis.processor.datagram;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.model.message.body.Body;
import ca.bc.gov.iamp.bcparis.repository.ICBCRestRepository;
import ca.bc.gov.iamp.bcparis.repository.query.IMSRequest;
import ca.bc.gov.iamp.bcparis.service.MessageService;

@Service
public class VehicleProcessor implements DatagramProcessor{

	private final Logger log = LoggerFactory.getLogger(VehicleProcessor.class);
	
	@Autowired
	private ICBCRestRepository icbcRepository;
	
	@Autowired
	private MessageService messageService;
	
	public Layer7Message process(Layer7Message message) {
		log.info("Processing Vehicle message.");
		
		List<IMSRequest> requests = createIMSContent(message);
		
		List<String> responseParsed = requests.parallelStream()
			.map(request -> icbcRepository.requestDetails(request))
			.map( icbcResponse -> {
				icbcResponse = parseResponse(icbcResponse);
				return messageService.buildResponse(message.getEnvelope().getBody(), icbcResponse);
			})
			.collect(Collectors.toList());
		
		final String msgFFmt = String.join("\n\n", responseParsed); 
		message.getEnvelope().getBody().setMsgFFmt(msgFFmt);
		
		log.info("Vehicle message processing completed.");
		
		return message;
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
			
			if(query.toUpperCase().startsWith("VIN"))
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
	
	private String getTransaction(String query) {
		return ( query.toUpperCase().startsWith("RVL") || query.toUpperCase().startsWith("RNS"))
			? "JISTRN2" : "JISTRAN";
	}
	
	private String parseResponse(String icbcResponse) {
		final String NEW_LINE = "\n";
		return icbcResponse
				.replaceAll("\\$\"", NEW_LINE)	// $” are converted to newline
				.replaceAll("\\$\\\\\"", NEW_LINE)	// $\” are converted to newline
				.replaceAll("[^\\x00-\\x7F]+", "");
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
