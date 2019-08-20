package ca.bc.gov.iamp.bcparis.processor.datagram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.service.SatelliteService;

@Service
public class SatelliteProcessor {

	@Autowired
	private SatelliteService service;
	
	final String NEW_LINE = "\n";
	private final Logger log = LoggerFactory.getLogger(SatelliteProcessor.class);
	
	private final String SCHEMA = "SEND MT:M" + NEW_LINE +
			  "FMT:Y" + NEW_LINE +
			  "FROM:{FROM}" + NEW_LINE + 
			  "TO:{TO}" + NEW_LINE + 
			  "TEXT:{TEXT}REG:2156746" + NEW_LINE +
			  NEW_LINE +
			  "{QUERY_MESSAGE}" + 
			  NEW_LINE + NEW_LINE + 
			  "{DATE_TIME}{DATE_TIME}";

	public Layer7Message process(Layer7Message message) {
		log.debug("Processing Satellite message.");
		return message;
	}
	
	public void sendVehicleMessages() {
//		final String VEHICLE_TO_URI = "BC41028";
//		final String DRIVER_LIC_2 = "LIC:233AWB/H/LIC:GVW143/H/LIC:007F\n" + 
//				"JR/H/LIC:JXX477/REG:957167\n" + 
//				"/VIN:1GNEL19W1XB163160/VIN:163160/P:Y/VIN:163160/P:Y/RSVP:16";
		
//		service.sendMessage(SCHEMA, VEHICLE_TO_URI, "LIC:PCG829/H");
//		service.sendMessage(SCHEMA, VEHICLE_TO_URI, "REG:2156746");
//		service.sendMessage(SCHEMA, VEHICLE_TO_URI, "REG:02156746");
//		service.sendMessage(SCHEMA, VEHICLE_TO_URI, DRIVER_LIC_2);
//		service.sendMessage(SCHEMA, VEHICLE_TO_URI, "RVL:314208701580602/");
//		service.sendMessage(SCHEMA, VEHICLE_TO_URI, "RNS:SMITH/G1:JOHN/G2:/DOB:/");
	}
	
	public void sendDriverMessages() {
		final String DRIVER_TO_URI = "BC41027";		
//		service.sendMessage(SCHEMA, DRIVER_TO_URI, "DL:0200000");
//		service.sendMessage(SCHEMA, DRIVER_TO_URI, "DL:4529693/DATE:20070122");
		service.sendMessage(SCHEMA, DRIVER_TO_URI, "SNME:SMITH/G1:JOHN/");
//		service.sendMessage(SCHEMA, DRIVER_TO_URI, "SNME:SMITH/G1:JOHN/DOB:19601208/SEX:M");
	}

}

