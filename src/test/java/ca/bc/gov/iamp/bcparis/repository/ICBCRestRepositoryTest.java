package ca.bc.gov.iamp.bcparis.repository;

import ca.bc.gov.iamp.bcparis.model.message.Envelope;
import ca.bc.gov.iamp.bcparis.model.message.body.Body;
import ca.bc.gov.iamp.bcparis.model.message.body.MQMD;
import ca.bc.gov.iamp.bcparis.model.message.header.Header;
import ca.bc.gov.iamp.bcparis.model.message.header.MsgSrvc;
import ca.bc.gov.iamp.bcparis.model.message.header.Origin;
import ca.bc.gov.iamp.bcparis.model.message.header.Routing;
import org.aspectj.bridge.MessageUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import ca.bc.gov.iamp.bcparis.exception.icbc.ICBCRestException;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.repository.query.IMSRequest;
import ca.bc.gov.iamp.bcparis.repository.query.IMSResponse;
import test.util.BCPARISTestUtil;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ICBCRestRepositoryTest {

	@InjectMocks
	private ICBCRestRepository repo = new ICBCRestRepository();
	
	@Mock
	private RestTemplate rest;
	
	@Before
    public void initMocks() throws NoSuchFieldException, SecurityException{
		ReflectionTestUtils.setField(repo,"username", "mock_username");
		ReflectionTestUtils.setField(repo,"password", "mock_password");
    }
	
	@Test
	public void request_details_success() {

        when(rest.postForEntity(Mockito.anyString(), Mockito.any(HttpEntity.class), Mockito.any()) )
    		.thenReturn(new ResponseEntity<>(getResponse(), HttpStatus.OK));

		final String icbcResponse = repo.requestDetails(BCPARISTestUtil.getMessageDriverDL(), IMSRequest.builder().build());
		
		Assert.assertEquals(icbcResponse, "IMS Response");
	}
	@Test
	public void request_details_mqmd_messageId() {

		Layer7Message message = createLayer7Message_withMessageId();
		HttpHeaders results = repo.getHeaders(message,"username","password");

		Assert.assertEquals("414D5120434251332020202020202020D9BF035D85206E23", results.get("auditTransactionId").get(0));
	}
	@Test
	public void request_details_mqmd_correlationId() {
		Layer7Message message = createLayer7Message_withCorrelationId();
		HttpHeaders results = repo.getHeaders(message,"username","password");

		Assert.assertEquals("414D5120424D565133564943202020205D0AD28120386102", results.get("auditTransactionId").get(0));
			}
	@Test
	public void request_details_no_auditTransactionId() {
		Layer7Message message = createLayer7Message_noAuditTransactionId();
		HttpHeaders results = repo.getHeaders(message,"username","password");

		Assert.assertNotNull(results.get("auditTransactionId"));
	}
	@Test(expected=ICBCRestException.class)
	public void request_details_rest_exception() {

        when(rest.postForEntity(Mockito.anyString(), Mockito.any(HttpEntity.class), Mockito.any()) )
    		.thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"));

		repo.requestDetails(BCPARISTestUtil.getMessageDriverDL(), IMSRequest.builder().build());
	}
	
	@Test(expected=ICBCRestException.class)
	public void request_details_not_found() {

		 when(rest.postForEntity(Mockito.anyString(), Mockito.any(HttpEntity.class), Mockito.any()) )
 		.thenReturn(new ResponseEntity<>(getResponse(), HttpStatus.NOT_FOUND));
		
		repo.requestDetails(BCPARISTestUtil.getMessageDriverDL(), IMSRequest.builder().build());
	}
	
	private IMSResponse getResponse() {
		return IMSResponse.builder()
				.imsResponse("IMS Response")
				.build();
	}

	private Layer7Message createLayer7Message_withMessageId() {
		 String NEW_LINE = "\n";
		 String cdata = "SEND MT:M" + NEW_LINE +
				"FMT:Y" + NEW_LINE +
				"FROM:{FROM}" + NEW_LINE +
				"TO:{TO}" + NEW_LINE +
				"TEXT:{TEXT}" + NEW_LINE +
				NEW_LINE +
				"{QUERY_MESSAGE}" +
				NEW_LINE + NEW_LINE +
				"{DATE_TIME}{DATE_TIME}";

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
				.mqmd( MQMD.builder().messageIdByte("41 4D 51 20 43 42 51 33 20 20 20 20 20 20 20 20 D9 BF 03 5D 85 20 6E 23").correlationIdByte("41 4D 51 20 42 4D 56 51 33 56 49 43 20 20 20 20 5D 0A D2 81 20 38 61 02").build())
				.build();

		return Layer7Message.builder().envelope(envelope).build();
	}

	private Layer7Message createLayer7Message_withCorrelationId() {
		String NEW_LINE = "\n";
		String cdata = "SEND MT:M" + NEW_LINE +
				"FMT:Y" + NEW_LINE +
				"FROM:{FROM}" + NEW_LINE +
				"TO:{TO}" + NEW_LINE +
				"TEXT:{TEXT}" + NEW_LINE +
				NEW_LINE +
				"{QUERY_MESSAGE}" +
				NEW_LINE + NEW_LINE +
				"{DATE_TIME}{DATE_TIME}";

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
				.mqmd( MQMD.builder().correlationIdByte("41 4D 51 20 42 4D 56 51 33 56 49 43 20 20 20 20 5D 0A D2 81 20 38 61 02").build())
				.build();

		return Layer7Message.builder().envelope(envelope).build();
	}

	private Layer7Message createLayer7Message_noAuditTransactionId() {
		String NEW_LINE = "\n";
		String cdata = "SEND MT:M" + NEW_LINE +
				"FMT:Y" + NEW_LINE +
				"FROM:{FROM}" + NEW_LINE +
				"TO:{TO}" + NEW_LINE +
				"TEXT:{TEXT}" + NEW_LINE +
				NEW_LINE +
				"{QUERY_MESSAGE}" +
				NEW_LINE + NEW_LINE +
				"{DATE_TIME}{DATE_TIME}";

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
}
