package ca.bc.gov.iamp.bcparis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ca.bc.gov.iamp.bcparis.model.NotificationType;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;

@Service
public class ReportService {
	
	private final Logger log = LoggerFactory.getLogger(ReportService.class);

	public Layer7Message reportdispatcher(Layer7Message message) {

		log.debug("Processing Report message.");
		
		String msgtype = message.getEnvelope().getMqmd().getMessageType();
		String feedback = message.getEnvelope().getMqmd().getFeedback();
		
		NotificationType notificationType = NotificationType.GetNotificationType(feedback);

		log.debug("Report feedback=" + feedback);
		
		if (!msgtype.isEmpty()) {

			switch (notificationType) {
			
				case MQFB_PAN:
					log.info("PAN notification dispatched to Report processor.");
					log.info("Processing MQFB_PAN notification. Just continue.");
					break;
					
				case MQFB_NAN:
					log.info("NAN notification dispatched to Report processor.");
					log.info("Processing MQFB_NAN notification.");
					break;
					
				case MQFB_EXPIRATION:
					log.info("MQFB_EXPIRATION  notification dispatched to Report processor.");
					log.info("Processing MQFB_EXPIRATION notification.");
					break;
					
				case MQFB_NONE:
					log.info("PAN  notification dispatched to Report processor.");
					log.info("Processing MQFB_NONE notification.");
					break;
					
				case DEFAULT:
					log.info("PAN  notification dispatched to Report processor.");
					log.info("Processing DEFAULT notification.");
			}

		}
		return message;
	}

	
}