package ca.bc.gov.iamp.bcparis.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.processor.MessageProcessor;
import ca.bc.gov.iamp.bcparis.processor.datagram.SatelliteProcessor;
import ca.bc.gov.iamp.bcparis.repository.Layer7MessageRepository;

@RestController
@RequestMapping("/api/v1/message")
public class MessageApi {

	private final Logger log = LoggerFactory.getLogger(MessageApi.class);

	@Autowired
	private MessageProcessor processor;
	
	@Autowired
	private Layer7MessageRepository repository;
	
	@Autowired
	private SatelliteProcessor satellite;
	
	@PutMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<Layer7Message> message( @RequestBody Layer7Message message ){
		
		Layer7Message messageResponse = onMessage(message);
		
		satellite.checkSatelliteMessage(messageResponse);
		
		return ResponseEntity.ok(messageResponse);
	}
	
	private Layer7Message onMessage(Layer7Message messageContent) {
		
		log.info("Message received");
		log.debug("Message content:\n" + messageContent);
		
		return processor.processMessage(messageContent);
	}
	
	//Only for test purpose
	@PostMapping( path="/test/layer7", consumes=MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<String> testPutMessageLayer7( @RequestBody Layer7Message message ){
		final String response = repository.sendMessage(message);
		return ResponseEntity.ok(response);
	}
	
	//Only for test purpose
	@PostMapping( path="/test/satellite")
	private ResponseEntity<String> testSatelliteVehicle( @RequestParam String uri, @RequestParam String query ){
		satellite.test(uri, query);
		return ResponseEntity.ok("Sent to MQ.");
	}

}
