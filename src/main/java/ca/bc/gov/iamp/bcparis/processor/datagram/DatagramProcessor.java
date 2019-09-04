package ca.bc.gov.iamp.bcparis.processor.datagram;

import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;

public interface DatagramProcessor {

	public Layer7Message process(Layer7Message message);
	
}
