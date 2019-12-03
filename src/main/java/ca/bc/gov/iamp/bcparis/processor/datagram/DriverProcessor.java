

package ca.bc.gov.iamp.bcparis.processor.datagram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.bc.gov.iamp.bcparis.exception.icbc.ICBCRestException;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.model.message.body.Body;
import ca.bc.gov.iamp.bcparis.repository.ICBCRestRepository;
import ca.bc.gov.iamp.bcparis.repository.query.IMSRequest;
import ca.bc.gov.iamp.bcparis.service.MessageService;

@Service
public class DriverProcessor implements DatagramProcessor{

	private final Logger log = LoggerFactory.getLogger(DriverProcessor.class);
	
	@Autowired
	private ICBCRestRepository icbcRepository;
	
	@Autowired
	private MessageService messageService;
	
	public Layer7Message process(Layer7Message message) {
		log.info("Processing Driver message.");
		
		Body body = message.getEnvelope().getBody();
		List<IMSRequest> requests = createIMSContent(message);
		
		try {
			
			List<String> responseParsed = requests.parallelStream()
					.map(request -> icbcRepository.requestDetails(message, request))
					.map( icbcResponse -> parseDriverResponse(icbcResponse))
					.collect(Collectors.toList());
				
			final String response = String.join("\n\n", responseParsed); 
			final String msgFFmt = messageService.buildResponse(body, response);
			body.setMsgFFmt(msgFFmt);
				
			log.info("Driver message processing completed.");
			return message;
				
		}catch (ICBCRestException e) {
			String content = messageService.parseResponseError(e.getResponseContent());
			content = parseDriverResponse(content);
			content = messageService.buildResponse(body, content);
			body.setMsgFFmt(content);
			throw e;
		}
	}
	
	private List<IMSRequest> createIMSContent(Layer7Message message) {
		final List<IMSRequest> result = new ArrayList<>();
		
		final String icbcPayload = "${transactionName} HC ${fromORI} ${toORI} ${qdCode} ${queryParams}";
		
		final String transaction = "DSSMTCPC";
		final String code = "QD";
		
		final Body body = message.getEnvelope().getBody();
		final String from = body.getCDATAAttribute("FROM");
		final String to = body.getCDATAAttribute("TO");
		
		final List<String> queryParams = messageService.getQueryAttributesList(
				message.getEnvelope().getBody(), Arrays.asList("SNME", "DL"));

		for(String query : queryParams) {
			final String imsContent = icbcPayload
					.replace("${transactionName}", transaction)
					.replace("${fromORI}", from)
					.replace("${toORI}", to)
					.replace("${qdCode}", code)
					.replace("${queryParams}", query);
			
			result.add(IMSRequest.builder().imsRequest(imsContent).build());
		}
		
		return result;
	}

	private String parseDriverResponse(String icbcResponse) {
		final String NEW_LINE = "\n";
		icbcResponse = icbcResponse
				.replaceAll("\\]\"", NEW_LINE)		// ]” are converted to newline
				.replaceAll("\\]\\\\\"", NEW_LINE) 	// ]/” are converted to newline
				.replaceAll("[^\\x00-\\x7F]+", "");
		return messageService.escape(icbcResponse);
	}
	
}
