package ca.bc.gov.iamp.bcparis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.bc.gov.iamp.bcparis.exception.message.InvalidMessageType;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.model.message.MessageType;
import ca.bc.gov.iamp.bcparis.processor.datagram.DriverProcessor;
import ca.bc.gov.iamp.bcparis.processor.datagram.PORProcessor;
import ca.bc.gov.iamp.bcparis.processor.datagram.SatelliteProcessor;
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
	
	@Autowired
	private SatelliteProcessor satelliteProcessor;
	
	@Autowired
	private ReportService reportService;

	public Layer7Message dispatch(Layer7Message message) {
		
		MessageType messageType = message.getMessageType();
		
		switch (messageType) {
			case REPORT: {
				log.info("Message dispatched to Report service.");
				return reportService.reportdispatcher(message);
			}
			case DRIVER: {
				log.info("Message dispatched to Driver processor.");
				return driverProcessor.process( message );
			}
			case VEHICLE: {
				log.info("Message dispatched to Vehicle processor.");
				return vehicleProcessor.process( message ); 
			}
			case POR: {
				log.info("Message dispatched to POR processor.");
				return PORProcessor.process( message ); 
			}
			case SATELLITE: {
				log.info("Message dispatched to Satellite processor.");
				return satelliteProcessor.process( message ); 
			}
			default: throw new InvalidMessageType("Invalid Message type");
		}
	}
	
}