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
public class MessageApi {

	private final Logger log = LoggerFactory.getLogger(MessageApi.class);

	@Autowired
	private MessageProcessor processor;

	@Autowired
	private RequestContext context;

	@PutMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<Object> message( @RequestBody Layer7Message message ){

		log.info("Message received");

		context.setRequestObject(message);
		Object response = null;

		response = processor.processMessage(message);

		return ResponseEntity.ok(response);
	}

}
