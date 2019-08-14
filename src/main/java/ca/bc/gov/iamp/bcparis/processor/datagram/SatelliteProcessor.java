package ca.bc.gov.iamp.bcparis.processor.datagram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.bc.gov.iamp.bcparis.model.message.Envelope;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.model.message.body.Body;
import ca.bc.gov.iamp.bcparis.model.message.body.MQMD;
import ca.bc.gov.iamp.bcparis.model.message.header.Header;
import ca.bc.gov.iamp.bcparis.model.message.header.MsgSrvc;
import ca.bc.gov.iamp.bcparis.model.message.header.Origin;
import ca.bc.gov.iamp.bcparis.model.message.header.Routing;
import ca.bc.gov.iamp.bcparis.repository.Layer7MessageRepository;

@Service
public class SatelliteProcessor {

	@Autowired
	private Layer7MessageRepository layer7repository;
	
	private final Logger log = LoggerFactory.getLogger(SatelliteProcessor.class);
		
	private final String VEHICLE_FROM_URI = "BC41127";
	private final String VEHICLE_TO_URI = "BC41027";
	//private final String VEHICLE_INTER_DELAY  = "1";
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
	
	public void sendVehicleMessages() {
		String cdata1 = buildMessageCDATA(VEHICLE_SCHEMA, VEHICLE_FROM_URI, VEHICLE_TO_URI, VEHICLE_DL);
		String cdata2 = buildMessageCDATA(VEHICLE_SCHEMA, VEHICLE_FROM_URI, VEHICLE_TO_URI, VEHICLE_DL_DATE);
		String cdata3 = buildMessageCDATA(VEHICLE_SCHEMA, VEHICLE_FROM_URI, VEHICLE_TO_URI, VEHICLE_DL_SNME);
		String cdata4 = buildMessageCDATA(VEHICLE_SCHEMA, VEHICLE_FROM_URI, VEHICLE_TO_URI, VEHICLE_DL_SNME_DOB_SEX);
		
		layer7repository.sendMessage(createLayer7Message(cdata1));
		layer7repository.sendMessage(createLayer7Message(cdata2));
		layer7repository.sendMessage(createLayer7Message(cdata3));
		layer7repository.sendMessage(createLayer7Message(cdata4));
	}
	
	private String buildMessageCDATA(String schema, String from, String to, String query) {
		return schema
				.replaceAll("${FROM}", from)
				.replaceAll("${TO}", to)
				.replaceAll("${QUERY_MESSAGE}", query);
	}
	
	private Layer7Message createLayer7Message(String cdata) {
		
		Header header = Header.builder()
				.role("ROLE 1")
				.origin(Origin.builder().qname("CPIC.MSG.AGENCY.REMOTE").qMgrName("BMVQ3VIC").build())
				.agencyId("AGENCY 1")
				.cpicVer("1.4")
				.userId("UID 1")
				.deviceId("DEVICE ID 1")
				.udf("BCPARISTEST")
				.priority("1")
				.routing(Routing.builder().qname("CPIC.MSG.BMVQ3VIC").qMgrName("BMVQ3VIC").build())
				.msgSrvc(MsgSrvc.builder().msgActn("Send").build())
				.build();
		
		Body body = Body.builder().build();
		
		MQMD mqmd = MQMD.builder().build();
		
		Envelope envelope = Envelope.builder()
				.header(header)
				.body(body)
				.mqmd(mqmd)
				.build();
		
		return Layer7Message.builder().envelope(envelope).build();
	}
}
