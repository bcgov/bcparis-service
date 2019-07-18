package ca.bc.gov.iamp.bcparis.processor.datagram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;

@Service
public class SatelliteProcessor {

	private final Logger log = LoggerFactory.getLogger(SatelliteProcessor.class);
	
	public Layer7Message process(Layer7Message message) {
		log.debug("Processing Satellite message.");
		return message;
	}
	
}
