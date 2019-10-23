package ca.bc.gov.iamp.bcparis.processor.datagram;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.util.StringUtils;

import ca.bc.gov.iamp.bcparis.exception.por.PORRestException;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.model.por.POROutput;
import ca.bc.gov.iamp.bcparis.model.por.PORResult;
import ca.bc.gov.iamp.bcparis.repository.PORRestRepository;
import ca.bc.gov.iamp.bcparis.service.MessageService;
import test.util.BCPARISTestUtil;
import test.util.TestUtil;

public class PORProcessorTest {

	@InjectMocks
	private PORProcessor processor = new PORProcessor();
	
	private PORRestRepository repo = Mockito.mock(PORRestRepository.class);
	
	@Spy
	private MessageService service = new MessageService();
	
	@Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }
	
	@Test
	public void proccess_success() {
		final String expected = "SEND MT:M\n" + 
				"FMT:Y\n" + 
				"FROM:BC41029\n" + 
				"TO:BC41127\n" + 
				"TEXT:RE: 0509\\nHC BC40940\\nBC41027\\nSNME:WISKIN/G1:TOMAS/G2:GEORGE/G3:ALPHONSE/DOB:20050505\\n\\n2019051520054820190515200548\n" + 
				"\n" + 
				"0 Records Found, a maximum of 100 will be returned.\n" + 
				"  OrderNo Effective  Role Sex  SNME, G1 G2 G3 DOB (T)Termination\n" + 
				"  \n" + 
				"Role: (D)efendant, (P)rotected, (H)older or (I)nterested Party\n" + 
				"Call 1-888-000-9999 for more information.\n" + 
				"";
		
		Mockito.when(repo.callPOR("WISKIN", "TOMAS", "GEORGE", "ALPHONSE", "20050505")).thenReturn(getPOROutput());
		
		Layer7Message message = processor.process(BCPARISTestUtil.getMessagePOR());
		
		Assert.assertEquals(expected, message.getEnvelope().getBody().getMsgFFmt());
	}
	
	private POROutput getPOROutput() {
		POROutput por = new POROutput();
		por.setStatusMsg("Success");
		por.setResult(new ArrayList<>());
		
		por.getResult().add(new PORResult("0 Records Found, a maximum of 100 will be returned."));
		por.getResult().add(new PORResult("  OrderNo Effective  Role Sex  SNME, G1 G2 G3 DOB (T)Termination"));
		por.getResult().add(new PORResult("  "));
		por.getResult().add(new PORResult("Role: (D)efendant, (P)rotected, (H)older or (I)nterested Party"));
		por.getResult().add(new PORResult("Call 1-888-000-9999 for more information."));
		return por;
	}
	
	@Test
	public void error_during_POR_call() {
		String errorContent = TestUtil.readFile("ICBC/response-error.xml");
		
		final Layer7Message message = BCPARISTestUtil.getMessagePOR();
		
		Mockito.when(repo.callPOR(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
			.thenThrow(new PORRestException("", errorContent, null) );
		
		try {
			processor.process(message);
		}catch (Exception e) {
			//Just ignore
		}
		
		int count = StringUtils.countOccurrencesOf(message.getEnvelope().getBody().getMsgFFmt(), "l7:detailMessage");
		Assert.assertEquals(18, count);
	}
	
}
