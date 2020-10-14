package ca.bc.gov.iamp.bcparis.message;

import ca.bc.gov.iamp.bcparis.model.MessageType;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;

public interface DatagramProcessor {

	MessageType getType();

	Layer7Message process(Layer7Message message);
	
}
