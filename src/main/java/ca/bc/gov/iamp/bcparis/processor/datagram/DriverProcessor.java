
package ca.bc.gov.iamp.bcparis.processor.datagram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.repository.ICBCRestRepository;
import ca.bc.gov.iamp.bcparis.repository.IMSRequest;

@Service
public class DriverProcessor {

	private final Logger log = LoggerFactory.getLogger(DriverProcessor.class);
	
	@Autowired
	private ICBCRestRepository ICBCrepository;
	
	private final String IMS_RESQUEST_BODY = "DSSMTCPC HC BC41127 BC41027 QD SNME:SMITH/G1:JANE/G2:MARY/DOB:19000101/SEX:F";
	
	public Layer7Message process(Layer7Message message) {
		log.info("Processing Driver message.");
		
		IMSRequest ims = IMSRequest.builder().imsRequest(IMS_RESQUEST_BODY).build();
		
		String icbcResponse = ICBCrepository.requestDetails(ims);
		
		log.info("Driver message processing completed.");
		
		return message;
	}
	
	
	
	
}
