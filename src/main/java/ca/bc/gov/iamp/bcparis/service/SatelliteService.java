package ca.bc.gov.iamp.bcparis.service;

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
public class SatelliteService {

	@Autowired
	private Layer7MessageRepository repository;
	
	private final String SATELLITE_FROM_URI = "BC41127";
	
	
	public void sendMessage(String schema, String to, String query) {
		Layer7Message message = createLayer7Message(schema, SATELLITE_FROM_URI, to, query);
		repository.sendMessage(message);
	}
	
	public Layer7Message createLayer7Message(String schema, String from, String to, String query) {
		String cdata = buildVehicleCDATA(schema, from, to, query);
		return createLayer7Message(cdata);
	}
	
	public String buildVehicleCDATA(String schema, String from, String to, String query) {
		return schema
				.replaceAll("${FROM}", from)
				.replaceAll("${TO}", to)
				.replaceAll("${QUERY_MESSAGE}", query);
	}
	
	public Layer7Message createLayer7Message(String cdata) {
		
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
