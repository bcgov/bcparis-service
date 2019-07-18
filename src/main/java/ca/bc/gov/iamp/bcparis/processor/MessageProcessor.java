package ca.bc.gov.iamp.bcparis.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.service.DispatcherService;
import ca.bc.gov.iamp.bcparis.transformation.MessageTransform;
import ca.bc.gov.iamp.bcparis.validation.MessageValidation;

@Service
public class MessageProcessor {

	private final Logger log = LoggerFactory.getLogger(MessageProcessor.class);
	
	@Autowired
	private MessageTransform transform;
	
	@Autowired
	private MessageValidation validation;
	
	@Autowired
	private DispatcherService dispatcher;

	public Layer7Message processMessage(Layer7Message message){
		log.info("Start Message processing.");
		
		message = transform.parse(message);
		
		validation.validate(message);
		
		log.info("Finished Message processing.");
		
		return dispatcher.dispatch(message);

	}
	
}
