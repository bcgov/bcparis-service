package ca.bc.gov.iamp.bcparis.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.service.MessageService;

@Service
public class MessageValidation {

	private final Logger log = LoggerFactory.getLogger(MessageValidation.class);

	@Autowired
	private MessageService messageService;

	public void validate(Layer7Message message) {
		log.info("Message validation.");
//		MessageType messageType = messageService.getMessageType(message);
//
//		switch (messageType) {
//		case DRIVER:
//			validateDriverOrVehicle(message);
//			break;
//		case VEHICLE:
//			validateDriverOrVehicle(message);
//			break;
//		case REPORT:
//			validateReport(message);
//			break;
//		default:
//			break;
//		}
	}

//	private void validateDriverOrVehicle(Message message) {
//		contains(message, "MSID");
//		contains(message, "FROM");
//	}
//
//	private void validateReport(Message message) {
//		containsTag(message, "MsgType");
//		containsTag(message, "Feedback");
//	}
//
//	private void contains(Message message, String field) {
//		String MSID = message.getCDATAAttribute(field);
//		if (StringUtils.isEmpty(MSID)) {
//			log.error("Message missing field=" + field);
//			throw new InvalidMessageException(message);
//		}
//	}
//
//	private void containsTag(Message message, String field) {
//		String MSID = message.getAttribute(field);
//		if (StringUtils.isEmpty(MSID)) {
//			log.error("Message missing tag=" + field);
//			throw new InvalidMessageException(message);
//		}
//	}

}
