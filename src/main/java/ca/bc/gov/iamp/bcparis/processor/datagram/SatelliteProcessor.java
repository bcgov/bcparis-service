package ca.bc.gov.iamp.bcparis.processor.datagram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.repository.Layer7MessageRepository;
import ca.bc.gov.iamp.bcparis.service.Layer7MessageService;

@Service
public class SatelliteProcessor {

	@Autowired
	private Layer7MessageRepository layer7repository;
	
	private final Logger log = LoggerFactory.getLogger(SatelliteProcessor.class);
		
	private final String VEHICLE_FROM_URI = "BC41127";
	private final String VEHICLE_TO_URI = "BC41027";
	private final String VEHICLE_INTER_DELAY  = "1";
	private final String VEHICLE_DL  = "DL:0200000";
	private final String VEHICLE_DL_DATE  = "DL:4529693/DATE:20070122";
	private final String VEHICLE_DL_SNME  = "SNME:SMITH/G1:JOHN/";
	private final String VEHICLE_DL_SNME_DOB_SEX  = "SNME:SMITH/G1:JOHN/DOB:19601208/SEX:M";
	
	private final String VEHICLE_SCHEMA = "SN:D07308-0421 MT:MUF MSID:AC03-040421-20:14:51\n" + 
			"FROM:${FROM}\n" + 
			"TO:${TO}\n" + 
			"TEXT:\n" + 
			"RE: 2181\n" + 
			"HC BC41127\n" + 
			"\n" + 
			"BC41027\n" + 
			"\n" + 
			"${QUERY_MESSAGE}\n" + 
			"\n" + 
			"2004042120150220040421201502";
	
	
	public Layer7Message process(Layer7Message message) {
		log.debug("Processing Satellite message.");
		return message;
	}
	
	private void sendVehicleMessages() {
		String message1 = buildMessage(VEHICLE_SCHEMA, VEHICLE_FROM_URI, VEHICLE_TO_URI, VEHICLE_DL);
		
		
	}
	
	private String buildMessage(String schema, String from, String to, String query) {
		return schema
				.replaceAll("${FROM}", from)
				.replaceAll("${TO}", to)
				.replaceAll("${QUERY_MESSAGE}", query);
	}
	
}
