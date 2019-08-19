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
	
	private final Logger log = LoggerFactory.getLogger(SatelliteProcessor.class);
	
	private final String VEHICLE_TO_URI = "BC41027";
	private final String VEHICLE_DL  = "DL:0200000";
	private final String VEHICLE_DL_DATE  = "DL:4529693/DATE:20070122";
	private final String VEHICLE_DL_SNME  = "SNME:SMITH/G1:JOHN/";
	private final String VEHICLE_DL_SNME_DOB_SEX  = "SNME:SMITH/G1:JOHN/DOB:19601208/SEX:M";
	//private final String VEHICLE_INTER_DELAY  = "1";
	
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
	
	private final String DRIVER_TO_URI = "BC41028";
	private final String DRIVER_LIC = "LIC:PCG829/H";
	private final String DRIVER_REG = "REG:2156746";
	private final String DRIVER_REG_2 = "REG:02156746";
	private final String DRIVER_LIC_2 = "LIC:233AWB/H/LIC:GVW143/H/LIC:007F\n" + 
										"JR/H/LIC:JXX477/REG:957167\n" + 
										"/VIN:1GNEL19W1XB163160/VIN:163160/P:Y/VIN:163160/P:Y/RSVP:16";
	private final String DRIVER_RVL = "RVL:314208701580602/";
	private final String DRIVER_RNS = "RNS:SMITH/G1:JOHN/G2:/DOB:/";
	private final String DRIVER_SCHEMA = "";
	
	
	public Layer7Message process(Layer7Message message) {
		log.debug("Processing Satellite message.");
		return message;
	}
	
	public void sendVehicleMessages() {
		service.sendMessage(VEHICLE_SCHEMA, VEHICLE_TO_URI, VEHICLE_DL);
		service.sendMessage(VEHICLE_SCHEMA, VEHICLE_TO_URI, VEHICLE_DL_DATE);
		service.sendMessage(VEHICLE_SCHEMA, VEHICLE_TO_URI, VEHICLE_DL_SNME);
		service.sendMessage(VEHICLE_SCHEMA, VEHICLE_TO_URI, VEHICLE_DL_SNME_DOB_SEX);
	}
	
	public void sendDriveMessages() {
		service.sendMessage(DRIVER_SCHEMA, DRIVER_TO_URI, DRIVER_LIC);
		service.sendMessage(DRIVER_SCHEMA, DRIVER_TO_URI, DRIVER_REG);
		service.sendMessage(DRIVER_SCHEMA, DRIVER_TO_URI, DRIVER_REG_2);
		service.sendMessage(DRIVER_SCHEMA, DRIVER_TO_URI, DRIVER_LIC_2);
		service.sendMessage(DRIVER_SCHEMA, DRIVER_TO_URI, DRIVER_RVL);
		service.sendMessage(DRIVER_SCHEMA, DRIVER_TO_URI, DRIVER_RNS);
	}

}

