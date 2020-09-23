package ca.bc.gov.iamp.bcparis.api;

import ca.bc.gov.iamp.bcparis.exception.message.InvalidMessageType;
import ca.bc.gov.iamp.bcparis.repository.Layer7MessageRepository;
import ca.bc.gov.iamp.bcparis.message.DatagramProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.util.RequestContext;

import java.util.List;
import java.util.Optional;

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

	@Autowired
	private RequestContext context;

	@Autowired
	private List<DatagramProcessor> datagramProcessorList;

	@Autowired
	private Layer7MessageRepository layer7Repository;

	@PutMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<Layer7Message> message( @RequestBody Layer7Message message ){

		log.info("Message received");

		context.setRequestObject(message);
		Layer7Message response = null;

		Optional<DatagramProcessor> processor = datagramProcessorList.stream().filter(x -> x.getType() == message.getMessageType()).findFirst();

		if(processor.isPresent()) {
			response =  processor.get().process(message);
		} else {
			throw new InvalidMessageType("Invalid Message type");
		}

		return ResponseEntity.ok(response);
	}


	@PostMapping( path="test/layer7", consumes=MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<String> testPutMessageLayer7( @RequestBody Layer7Message message ){
		final String response = layer7Repository.sendMessage(message);
		return ResponseEntity.ok(response);
	}

}
