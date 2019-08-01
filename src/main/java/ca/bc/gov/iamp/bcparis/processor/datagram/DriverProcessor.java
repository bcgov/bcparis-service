
package ca.bc.gov.iamp.bcparis.processor.datagram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.bc.gov.iamp.bcparis.exception.message.InvalidMessage;
import ca.bc.gov.iamp.bcparis.model.message.Body;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.repository.ICBCRestRepository;
import ca.bc.gov.iamp.bcparis.repository.query.IMSRequest;

@Service
public class DriverProcessor {

	private final Logger log = LoggerFactory.getLogger(DriverProcessor.class);
	
	@Autowired
	private ICBCRestRepository ICBCrepository;
	
	public Layer7Message process(Layer7Message message) {
		log.info("Processing Driver message.");
		
		String imsContent = createIMS(message);
		IMSRequest ims = IMSRequest.builder().imsRequest(imsContent).build();
		
		String icbcResponse = ICBCrepository.requestDetails(ims);
		
		icbcResponse = parseResponse(icbcResponse);
		
		log.info("Driver message processing completed.");
		
		message.getEnvelope().getBody().setMsgFFmt(icbcResponse);
		return message;
	}
	
	public String createIMS(Layer7Message message) {
		
		final String schema = "${transactionName} HC ${fromORI} ${toORI} ${qdCode} ${queryParams}";
		
		final Body body = message.getEnvelope().getBody();
		
		final String TRANSACTION = "DSSMTCPC";
		final String CODE = "QD";
		final String from = body.getCDATAAttribute("FROM");
		final String to = body.getCDATAAttribute("TO");

		String queryParams = getQueryParams(message);
		
		return schema
				.replace("${transactionName}", TRANSACTION)
				.replace("${fromORI}", from)
				.replace("${toORI}", to)
				.replace("${qdCode}", CODE)
				.replace("${queryParams}", queryParams);
	}
	
	private String getQueryParams(Layer7Message message) {
		final Body body = message.getEnvelope().getBody();
		if(body.containAttribute("SNME"))
			return "SNME:" + body.getSNME();
		else if(body.containAttribute("DL"))
			return "DL:" + body.getDL();
		else
			throw new InvalidMessage("Driver message should come with SNME or DL.");
	}
	
	private String parseResponse(String icbcResponse) {
		return icbcResponse;
	}
	
}
