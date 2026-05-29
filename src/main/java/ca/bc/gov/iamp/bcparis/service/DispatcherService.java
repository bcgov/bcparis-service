package ca.bc.gov.iamp.bcparis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.bc.gov.iamp.bcparis.exception.message.InvalidMessageType;
import ca.bc.gov.iamp.bcparis.model.MessageType;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.processor.datagram.DriverProcessor;
import ca.bc.gov.iamp.bcparis.processor.datagram.PORProcessor;
import ca.bc.gov.iamp.bcparis.processor.datagram.VehicleProcessor;

@Service
public class DispatcherService {

	private final Logger log = LoggerFactory.getLogger(DispatcherService.class);

	@Autowired
	private DriverProcessor driverProcessor;

	@Autowired
	private VehicleProcessor vehicleProcessor;

	@Autowired
	private PORProcessor PORProcessor;

	public Object dispatch(Layer7Message message) {

		System.out.println(">>> ENTERED DispatcherService.dispatch()");
		log.info("Entered DispatcherService.dispatch()");

		if (message == null) {
			System.out.println(">>> Message is NULL in DispatcherService");
			log.error("Message is null in DispatcherService");
			throw new IllegalArgumentException("Message cannot be null");
		}

		String msgFmt = (message.getEnvelope() != null && message.getEnvelope().getBody() != null)
				? message.getEnvelope().getBody().getMsgFFmt()
				: null;
		if (msgFmt != null) {
			if (msgFmt.contains("DSSMTCPC")) {
				System.out.println(">>> Routing to DriverProcessor based on DSSMTCPC in MsgFFmt");
				log.info("Routing to DriverProcessor based on DSSMTCPC in MsgFFmt");
				return driverProcessor.process(message);
			} else if (msgFmt.contains("DSSMTCPV") || msgFmt.contains("JISTRN2")) {
				System.out.println(">>> Routing to VehicleProcessor based on DSSMTCPV or JISTRN2 in MsgFFmt");
				log.info("Routing to VehicleProcessor based on DSSMTCPV or JISTRN2 in MsgFFmt");
				return vehicleProcessor.process(message);
			}
		}

		MessageType messageType = message.getMessageType();

		System.out.println(">>> MessageType resolved as: " + messageType);
		log.info("MessageType resolved as: {}", messageType);

		switch (messageType) {

			case REPORT: {
				System.out.println(">>> REPORT message type detected. Ignoring.");
				log.info("Report message ignored.");
				return message;
			}

			case DRIVER: {
				System.out.println(">>> Dispatching to DriverProcessor");
				log.info("Message dispatched to Driver processor.");
				return driverProcessor.process(message);
			}

			case VEHICLE: {
				System.out.println(">>> Dispatching to VehicleProcessor");
				log.info("Message dispatched to Vehicle processor.");
				return vehicleProcessor.process(message);
			}

			case POR: {
				System.out.println(">>> Dispatching to PORProcessor");
				log.info("Message dispatched to POR processor.");
				return PORProcessor.process(message);
			}

			default:
				System.out.println(">>> INVALID message type: " + messageType);
				log.error("Invalid Message type: {}", messageType);
				throw new InvalidMessageType("Invalid Message type");
		}
	}
}
