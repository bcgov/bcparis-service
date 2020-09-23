package ca.bc.gov.iamp.bcparis.processor.datagram;

import ca.bc.gov.iamp.bcparis.message.DriverProcessor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.util.StringUtils;

import ca.bc.gov.iamp.bcparis.exception.icbc.ICBCRestException;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.repository.ICBCRestRepository;
import ca.bc.gov.iamp.bcparis.repository.query.IMSRequest;
import ca.bc.gov.iamp.bcparis.service.MessageService;
import test.util.BCPARISTestUtil;
import test.util.TestUtil;

public class DriverProcessorTest {

	@InjectMocks
	private DriverProcessor processor = new DriverProcessor();
	
	private ICBCRestRepository icbc = Mockito.mock(ICBCRestRepository.class);
	
	@Spy
	private MessageService service = new MessageService();
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }
	
	@Test
	public void proccess_success() {
		final String mockICBCResponse = TestUtil.readFile("ICBC/response-driver");
		final Layer7Message message = BCPARISTestUtil.getMessageDriverSNME();
		
		Mockito.when(icbc.requestDetails(Mockito.any(Layer7Message.class), Mockito.any(IMSRequest.class)))
			.thenReturn(mockICBCResponse);
		
		final Layer7Message icbcResponse = processor.process(message);
		final String expectedParsed = "SEND MT:M\n" + 
				"FMT:Y\n" + 
				"FROM:BC41027\n" + 
				"TO:BC41127\n" +
				"TEXT:RE: 0509\n" + 
				"\n" + 
				"HC  BC41027\n" + 
				"BC41127\n" + 
				"RE SNME:SMITH/G1:JANE/G2:MARY/DOB:19000101/SEX:F\n" + 
				"BCDL: 7088384       STATUS: NORMAL            PRIMARY DL STATUS:   NONE\n" + 
				"SMITH, JUDY MADELINE                          LEARNER DL STATUS:   NONE\n" + 
				"M/A BOX 195                                   TEMPORARY DL STATUS: NONE\n" + 
				"DUNCAN BC\n" + 
				"5175 TZOUHALEM RD                             LICENCE TYPE:CLIENT STUB\n" + 
				"V9L 3X3\n" + 
				"                                                OTHER JUR DL:\n" + 
				"                                                DL#:\n" + 
				"DOB: 1900-01-01  SEX: F     KEYWORD: TEST DATA\n" + 
				"EYE COLOUR:             HAIR COLOUR:\n" + 
				"    HEIGHT: 000 CM           WEIGHT:    0.0 KG\n" + 
				"\n" + 
				"\n" + 
				"END OF DRIVING RECORD.\n" + 
				"       ";
		Assert.assertEquals(expectedParsed, icbcResponse.getEnvelope().getBody().getMsgFFmt());
	}


	@Test
	public void process_special_chars() {
		final String mockICBCResponse = TestUtil.readFile("ICBC/response-driver-special-chars");
		final Layer7Message message = BCPARISTestUtil.getMessageDriverSNME();

		Mockito.when(icbc.requestDetails(Mockito.any(Layer7Message.class), Mockito.any(IMSRequest.class)))
				.thenReturn(mockICBCResponse);

		final Layer7Message icbcResponse = processor.process(message);
		final String expectedParsed = "SEND MT:M\n" +
				"FMT:Y\n" +
				"FROM:BC41027\n" +
				"TO:BC41127\n" +
				"TEXT:RE: 0509\n" +
				"\n" +
				" HC  BC41027\n" +
				"BC41127\n" +
				"RE  SNME:SMITH/G1:JANE/G2:MARY/DOB:19000101/SEX:F\n" +
				"BCDL: 7088384       STATUS: NORMAL            PRIMARY DL STATUS:   NONE\n" +
				"SMITH, JUDY MADELINE                          LEARNER DL STATUS:   NONE\n" +
				"M/A BOX 195                                   TEMPORARY DL STATUS: NONE\n" +
				"DUNCAN BC\n" +
				"5175 TZOUHALEM RD                             LICENCE TYPE:CLIENT STUB\n" +
				"V9L 3X3\n" +
				"                                                OTHER JUR DL:\n" +
				"                                                DL#:\n" +
				"DOB: 1900-01-01  SEX: F     KEYWORD: TEST DATA\n" +
				"EYE COLOUR:             HAIR COLOUR:\n" +
				"    HEIGHT: 000 CM           WEIGHT:    0.0 KG\n" +
				"\n" +
				"\n" +
				"END OF DRIVING RECORD.\n";
		Assert.assertEquals(expectedParsed, icbcResponse.getEnvelope().getBody().getMsgFFmt());
	}

	@Test
	public void create_ims_using_CNME_success() {
		final Layer7Message message = BCPARISTestUtil.getMessageDriverSNME();
		
		ArgumentCaptor<IMSRequest> argument = ArgumentCaptor.forClass(IMSRequest.class);
		Mockito.when(icbc.requestDetails(Mockito.any(Layer7Message.class), argument.capture())).thenReturn("ICBC Response");
		
		processor.process(message);
	
		Mockito.verify(icbc, Mockito.times(1)).requestDetails(Mockito.any(Layer7Message.class), argument.capture());
		Assert.assertEquals("DSSMTCPC HC BC41127 BC41027 QD SNME:NEWMAN/G1:OLDSON/G2:MIKE/DOB:19900214", argument.getValue().getImsRequest());
	}
	
	
	@Test
	public void create_ims_using_DL_success() {
		final Layer7Message message = BCPARISTestUtil.getMessageDriverDL();
		
		ArgumentCaptor<IMSRequest> argument = ArgumentCaptor.forClass(IMSRequest.class);
		Mockito.when(icbc.requestDetails(Mockito.any(Layer7Message.class), argument.capture())).thenReturn("ICBC Response");
		
		processor.process(message);
	
		Mockito.verify(icbc, Mockito.times(1)).requestDetails(Mockito.any(Layer7Message.class), argument.capture());
		Assert.assertEquals("DSSMTCPC HC BC41127 BC41027 QD DL:3559874", argument.getValue().getImsRequest());
	}
	
	@Test
	public void create_query_params_list_success() {
		final Layer7Message message = BCPARISTestUtil.getMessageDriverMultipleParams();
		
		ArgumentCaptor<IMSRequest> argument = ArgumentCaptor.forClass(IMSRequest.class);
		Mockito.when(icbc.requestDetails(Mockito.any(Layer7Message.class), argument.capture())).thenReturn("ICBC Response");
		
		processor.process(message);
		
		Mockito.verify(icbc, Mockito.times(2)).requestDetails(Mockito.any(Layer7Message.class), argument.capture());
		
		int count = StringUtils.countOccurrencesOf(message.getEnvelope().getBody().getMsgFFmt(), "TEXT:BCPARIS Diagnostic Test qwe20190827173834");
		Assert.assertEquals(1, count);
	}

	@Test
	public void invalid_query() {
		final Layer7Message message = BCPARISTestUtil.getInvalidDriverQuery();

		ArgumentCaptor<IMSRequest> argument = ArgumentCaptor.forClass(IMSRequest.class);
		Mockito.when(icbc.requestDetails(Mockito.any(Layer7Message.class), argument.capture())).thenReturn("ICBC Response");

		processor.process(message);

		int count = StringUtils.countOccurrencesOf(message.getEnvelope().getBody().getMsgFFmt(), "Unable to parse/formatting error");
		Assert.assertEquals(1, count);
	}
	
	@Test
	public void error_during_ICBC_call() {
		String errorContent = TestUtil.readFile("ICBC/response-error.xml");
		
		final Layer7Message message = BCPARISTestUtil.getMessageDriverDL();
		
		Mockito.when(icbc.requestDetails(Mockito.any(Layer7Message.class), Mockito.any())).thenThrow(new ICBCRestException("", errorContent, null) );
		
		try {
			processor.process(message);
		}catch (Exception e) {
			//Just ignore
		}
		
		int count = StringUtils.countOccurrencesOf(message.getEnvelope().getBody().getMsgFFmt(), "l7:detailMessage");
		Assert.assertEquals(18, count);
	}
	
}
