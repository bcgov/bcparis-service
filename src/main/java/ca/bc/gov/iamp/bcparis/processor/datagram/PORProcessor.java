package ca.bc.gov.iamp.bcparis.processor.datagram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.bc.gov.iamp.bcparis.exception.por.PORRestException;
import ca.bc.gov.iamp.bcparis.model.message.Body;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.model.por.POROutput;
import ca.bc.gov.iamp.bcparis.repository.PORRestRepository;
import ca.bc.gov.iamp.bcparis.util.XMLUtil;

@Service
public class PORProcessor {

	private final Logger log = LoggerFactory.getLogger(PORProcessor.class);

	@Autowired
	private PORRestRepository PORrepository;
	
	
	public Layer7Message process(Layer7Message message) {
		log.info("Processing POR message.");
		Body body = message.getEnvelope().getBody();
		
		String surname = body.getCDATAAttribute("SNME");
		String given1 = body.getCDATAAttribute("G1");
		String given2 = body.getCDATAAttribute("G2");
		String given3 = body.getCDATAAttribute("G3");
		String dob = body.getCDATAAttribute("DOB");
		
		POROutput porResult = PORrepository.callPOR(surname, given1, given2, given3, dob);
		
		if("Success".equalsIgnoreCase(porResult.getStatusMsg())) {
			
			final String porContent = extractPORContent(porResult);
			
			String layer7ContenteResponse = buildResponse(message, porContent);
			message.getEnvelope().getBody().setMsgFFmt(layer7ContenteResponse);
			message.getEnvelope().getBody().swapFromAndTo();
			
			log.info("POR message processing completed.");
			
			return message;
		}else {
			 throw new PORRestException("Not Expected POR Response. status_msg=" + porResult.getStatusMsg());
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
	
	private String buildResponse(Layer7Message message, String PORContent) {
		final String msgFFmt = message.getEnvelope().getBody().getMsgFFmt();
		final boolean contaisCDATATAG = XMLUtil.containsCDATA(message.getEnvelope().getBody().getMsgFFmt());
		
		return contaisCDATATAG 
				? XMLUtil.CDATA_BEGIN + XMLUtil.extractCDATAFromMsgFFmt(msgFFmt) + System.lineSeparator() + PORContent + XMLUtil.CDATA_END
				: XMLUtil.extractCDATAFromMsgFFmt(msgFFmt) + System.lineSeparator() + PORContent;
	}
}
