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
		
		//BC41028 JISTRAN Single Reg
		service.sendMessage(SCHEMA, VEHICLE_TO_URI, "REG:2156746");
		
		//BC41028 JISTRAN Single 8 digit Reg
		service.sendMessage(SCHEMA, VEHICLE_TO_URI, "REG:02156746");
		
		//BC41028 JISTRAN string out commands
		service.sendMessage(SCHEMA, VEHICLE_TO_URI, "LIC:233AWB/H/LIC:GVW143/H/LIC:007FJR/H/LIC:JXX477/");
		
		//BC41028 JISTRN2 RVL
		service.sendMessage(SCHEMA, VEHICLE_TO_URI, "RVL:314208701580602/");
		
		//BC41028 JISTRN2 RNS
		service.sendMessage(SCHEMA, VEHICLE_TO_URI, "RNS:SMITH/G1:JOHN/G2:/DOB:/");
	}
	
	private void sendDriverMessages() {
		final String DRIVER_TO_URI = MessageType.DRIVER.getCode();
		
		//BC41027 DL:02000000 - No DATE:
		service.sendMessage(SCHEMA, DRIVER_TO_URI, "DL:0200000");
		
		//BC41027  Single Lic - Null in Prod DL:4529693/DATE:20070122
		service.sendMessage(SCHEMA, DRIVER_TO_URI, "DL:4529693/DATE:20070122");
		
		//BC41027 SNME
		service.sendMessage(SCHEMA, DRIVER_TO_URI, "SNME:SMITH/G1:JOHN/");
		
		//BC41027 SNME with dob SEX
		service.sendMessage(SCHEMA, DRIVER_TO_URI, "SNME:SMITH/G1:JOHN/DOB:19601208/SEX:M");
	}
	
	private void sendPorMessages() {
		final String POR_TO_URI = MessageType.POR.getCode();
		
		//BC41029 - Cline - S
		service.sendMessage(SCHEMA, POR_TO_URI, "SNME:CLINE/G1:Brian/G2:Edward");
		
		//BC41029 - Cline - P
		service.sendMessage(SCHEMA, POR_TO_URI, "SNME:CLINE/G1:Drista");
		
		//BC41029 SNME:SMITH/G1:John
		service.sendMessage(SCHEMA, POR_TO_URI, "SNME:SMITH/G1:John");
	}

	public void test(String uri, String query) {
		service.sendMessage(SCHEMA, uri, query);
	}
}

