package ca.bc.gov.iamp.bcparis.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.processor.MessageProcessor;

@RestController
@RequestMapping("/message")
public class MessageApi {

	private final Logger log = LoggerFactory.getLogger(MessageApi.class);

	@Autowired
	private MessageProcessor processor;
	


	@PutMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<Layer7Message> message( @RequestBody Layer7Message message ){
		
		Layer7Message messageResponse = onMessage(message);
		
		return ResponseEntity.ok(messageResponse);
	}
	
	private Layer7Message onMessage(Layer7Message messageContent) {
		
		log.info("Message Received:\n" + messageContent);
		
		return processor.processMessage(messageContent);
	}

}
