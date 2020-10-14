

package ca.bc.gov.iamp.bcparis.message;

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

import ca.bc.gov.iamp.bcparis.exception.icbc.ICBCRestException;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.model.message.body.Body;
import ca.bc.gov.iamp.bcparis.repository.ICBCRestRepository;
import ca.bc.gov.iamp.bcparis.repository.query.IMSRequest;

@Service
public class DriverProcessor implements DatagramProcessor{

	private final Logger log = LoggerFactory.getLogger(DriverProcessor.class);
	
	@Autowired
	private ICBCRestRepository icbcRepository;
	
	@Autowired
	private MessageService messageService;

	@Override
	public MessageType getType() {
		return MessageType.DRIVER;
	}

	public Layer7Message process(Layer7Message message) {
		log.info("Processing Driver message.");

		List<IMSRequest> requests = createIMSContent(message);
		if (requests.isEmpty()) {
			message.getEnvelope().getBody().setMsgFFmt(messageService.buildErrorResponse(message.getEnvelope().getBody(), "Unable to parse/formatting error"));
			log.warn("Processing Driver: Unable to parse/formatting error");
			return message;
		}
		try {
			
			List<String> responseParsed = requests.parallelStream()
					.map(request -> icbcRepository.requestDetails(message, request))
					.map( icbcResponse -> messageService.parseResponse(icbcResponse))
					.collect(Collectors.toList());
			message.getEnvelope().getBody().setMsgFFmt(messageService.buildResponse(message.getEnvelope().getBody(), String.join("\n\n", responseParsed)));
				
			log.info("Driver message processing completed.");
			return message;
				
		}catch (ICBCRestException e) {
			String content = messageService.parseResponseError(e.getResponseContent());
			content =  messageService.parseResponse(content);
			content = messageService.buildResponse(message.getEnvelope().getBody(), content);
			message.getEnvelope().getBody().setMsgFFmt(content);
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

	
}
