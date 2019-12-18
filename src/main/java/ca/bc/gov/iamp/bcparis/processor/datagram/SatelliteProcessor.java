package ca.bc.gov.iamp.bcparis.processor.datagram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.bc.gov.iamp.bcparis.api.exception.ExceptionHandlerController;
import ca.bc.gov.iamp.bcparis.model.MessageType;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.service.SatelliteService;

@Service
public class SatelliteProcessor {

	@Autowired
	private SatelliteService service;
	
	@Autowired
	private ExceptionHandlerController exceptionHandler;
	
	final String NEW_LINE = "\n";
	private final Logger log = LoggerFactory.getLogger(SatelliteProcessor.class);
	
	private final String SCHEMA = "SEND MT:M" + NEW_LINE +
			  "FMT:Y" + NEW_LINE +
			  "FROM:{FROM}" + NEW_LINE + 
			  "TO:{TO}" + NEW_LINE + 
			  "TEXT:{TEXT}" + NEW_LINE +
			  NEW_LINE +
			  "{QUERY_MESSAGE}" + 
			  NEW_LINE + NEW_LINE + 
			  "{DATE_TIME}{DATE_TIME}";

	public String process(Layer7Message message) {
		log.debug("Processing Satellite message.");
		
		calculateRoundTripTime(message);
		
		return "OK";
	}
	
	private void calculateRoundTripTime(Layer7Message messageResponse) {
		final String text = messageResponse.getEnvelope().getBody().getCDATAAttribute("TEXT");
		if(text.startsWith("BCPARIS Diagnostic Test")) {
			String date = text.substring(text.indexOf("qwe") + 3);
			log.info("Satellite Round Trip Execution: " + service.calculateExecutionTime(date));
		}
	}
	
	public void sendSatelliteMessages() {
		try {
			
			log.info("Sending Satellite message.");
			
			sendVehicleMessages();
			sendDriverMessages();
			sendPorMessages();
			
			log.info("Satellite messages sent.");
			
		}catch (Exception e) {
			exceptionHandler.satelliteException(e);
		}
	}
	
	private void sendVehicleMessages() {
		final String VEHICLE_TO_URI = MessageType.VEHICLE.getCode();
		
		//BC41028 JISTRAN Single Lic
		service.sendMessage(SCHEMA, VEHICLE_TO_URI, "LIC:PCG829/H");
	}
	
	private void sendDriverMessages() {
		final String DRIVER_TO_URI = MessageType.DRIVER.getCode();
		
		//BC41027 DL:02000000 - No DATE:
		service.sendMessage(SCHEMA, DRIVER_TO_URI, "DL:0200000");
	}
	
	private void sendPorMessages() {
		final String POR_TO_URI = MessageType.POR.getCode();
		
		//BC41029 - Cline - S
		service.sendMessage(SCHEMA, POR_TO_URI, "SNME:CLINE/G1:Brian/G2:Edward");
	}

	public void test(String uri, String query) {
		service.sendMessage(SCHEMA, uri, query);
	}
}

