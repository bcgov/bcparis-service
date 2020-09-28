package ca.bc.gov.iamp.bcparis.api;

import ca.bc.gov.iamp.bcparis.repository.Layer7MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.processor.MessageProcessor;
import ca.bc.gov.iamp.bcparis.util.RequestContext;

/**
 * The message API Controller accepts request to search drivers, vehicles or por and return information.
 *
 * There is a global exception handler ca.bc.gov.iamp.api.exception.handling.CustomRestExceptionHandler that handles any exception thrown.
 * There is a local exception handler ca.bc.gov.iamp.bcparis.api.exception.ExceptionHandlerController that handles known ICBC, layer7 or POR exception (an email is send in this case)
 *
 */
@RestController
@RequestMapping("/api/v1/message")
public class MessageController {

	private final Logger log = LoggerFactory.getLogger(MessageController.class);

	private final MessageProcessor processor;

	private final Layer7MessageRepository layer7Repository;

	public MessageController(MessageProcessor processor, Layer7MessageRepository layer7Repository) {
		this.processor = processor;
		this.layer7Repository = layer7Repository;
	}

	@PutMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> message( @RequestBody Layer7Message message ) {
		log.info("Message received");
		return ResponseEntity.ok(processor.processMessage(message));
	}

	@PostMapping( path="test/layer7", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> testPutMessageLayer7( @RequestBody Layer7Message message ){
		final String response = layer7Repository.sendMessage(message);
		return ResponseEntity.ok(response);
	}

}
