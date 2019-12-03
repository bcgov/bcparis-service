package ca.bc.gov.iamp.bcparis.processor.datagram;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.bc.gov.iamp.bcparis.exception.por.PORRestException;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.model.message.body.Body;
import ca.bc.gov.iamp.bcparis.model.por.POROutput;
import ca.bc.gov.iamp.bcparis.repository.PORRestRepository;
import ca.bc.gov.iamp.bcparis.service.MessageService;

@Service
public class PORProcessor {

	private final Logger log = LoggerFactory.getLogger(PORProcessor.class);

	@Autowired
	private PORRestRepository PORrepository;
	
	@Autowired
	private MessageService messageService;
	
	public Layer7Message process(Layer7Message message) {
		log.info("Processing POR message.");
		Body body = message.getEnvelope().getBody();
		
		List<String> attributes = body.getSNME();
		
		String snme = body.getAttribute(attributes, "SNME");
		String given1 = body.getAttribute(attributes, "G1");
		String given2 = body.getAttribute(attributes, "G2");
		String given3 = body.getAttribute(attributes, "G3");
		String dob = body.getAttribute(attributes, "DOB");
		
		try {
			POROutput porResult = PORrepository.callPOR(snme, given1, given2, given3, dob);
			
			if("Success".equalsIgnoreCase(porResult.getStatusMsg())) {
				
				final String porContent = extractPORContent(porResult);
				
				String response = buildResponse(message, porContent);
				message.getEnvelope().getBody().setMsgFFmt(response);
				
				log.info("POR message processing completed.");
				log.debug("POR message: " + System.lineSeparator() + response);
				
				return message;
			}else {
				final String m = String.format("Not Expected POR Response. status_msg=%s", porResult.getStatusMsg());
				throw new PORRestException(m, m);
			}
		}catch (PORRestException e) {
			String content = messageService.parseResponseError(e.getResponseContent());
			content = messageService.escape(content);
			content = messageService.buildResponse(body, content);
			body.setMsgFFmt(content);
			throw e;
		}
	}
	
	private String extractPORContent(POROutput porResult) {
		
		StringBuilder sb = new StringBuilder();
		
		porResult.getResult().forEach( result ->{
			sb.append( result.getIndData() )
				.append(System.lineSeparator());
		});
		
		return sb.toString();
	}
	
	private String buildResponse(Layer7Message message, String porResponse) {
		final String NEW_LINE = "\n";
		final String schema = "SEND MT:M" + NEW_LINE +
							  "FMT:Y" + NEW_LINE +
							  "FROM:${from}" + NEW_LINE + 
							  "TO:${to}" + NEW_LINE + 
							  "${TEXT_to_end_of_message}" + NEW_LINE +
							  NEW_LINE +
							  "${por_response}";
		
		final Body body = message.getEnvelope().getBody(); 
		final String from = body.getCDATAAttribute("FROM");
		final String to = body.getCDATAAttribute("TO");	
		final String messageContent = body.cutFromCDATA("TEXT:");
		
		return schema
				.replace("${from}", to)
				.replace("${to}", from)
				.replace("${TEXT_to_end_of_message}", messageContent)
				.replace("${por_response}", porResponse);
	}

}
