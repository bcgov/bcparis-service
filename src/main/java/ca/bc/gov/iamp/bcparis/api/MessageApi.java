package ca.bc.gov.iamp.bcparis.api;

import ca.bc.gov.iamp.bcparis.exception.message.InvalidMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.processor.MessageProcessor;
import ca.bc.gov.iamp.bcparis.util.RequestContext;

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
		try {
			response = processor.processMessage(message);
		} catch (InvalidMessage e) {
			String respMessage =  String.format("%s %s",message.getEnvelope().getBody().getMsgFFmt(),"Unable to parse/formatting error");

			message.getEnvelope().getBody().setMsgFFmt(respMessage);
			response = message;
		}

		
		return ResponseEntity.ok(response);
	}

}
