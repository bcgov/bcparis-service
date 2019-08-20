package ca.bc.gov.iamp.bcparis.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

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
	private final DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	
	public void sendMessage(String schema, String to, String query) {
		Layer7Message message = createLayer7Message(schema, SATELLITE_FROM_URI, to, query);
		repository.sendMessage(message);
	}
	
	public Layer7Message createLayer7Message(String schema, String from, String to, String query) {
		String cdata = buildCDATA(schema, from, to, query);
		return createLayer7Message(cdata);
	}
	
	public String buildCDATA(String schema, String from, String to, String query) {
		String date = getDate();
		String text = "BCPARIS Diagnostic Test qwe" + date;
		return schema
				.replace("{FROM}", from)
				.replace("{TO}", to)
				.replace("{TEXT}", text)
				.replace("{QUERY_MESSAGE}", query)
				.replace("{DATE_TIME}", date)
				.replace("{DATE_TIME}", date);
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

		Envelope envelope = Envelope.builder()
				.header(header)
				.body( Body.builder().msgFFmt(cdata).build() )
				.mqmd( MQMD.builder().build())
				.build();
		
		return Layer7Message.builder().envelope(envelope).build();
	}

	private String getDate() {
		return LocalDateTime.now().atZone(ZoneOffset.UTC).format(formater);
	}
	
	public String calculateExecutionTime(String date) {
		ZonedDateTime now = LocalDateTime.now().atZone(ZoneOffset.UTC);
		ZonedDateTime start = LocalDateTime.parse(date, formater).atZone(ZoneOffset.UTC);
		
		Duration duration = Duration.between(start, now);
		return getDurationFormated(duration);
	}

	private String getDurationFormated(Duration duration) {
		return String.format("%tT", duration.toMillis() - TimeZone.getDefault().getRawOffset());
	}

}
