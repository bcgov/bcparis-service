package ca.bc.gov.iamp.bcparis.processor.datagram;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.repository.ICBCRestRepository;
import ca.bc.gov.iamp.bcparis.repository.query.IMSRequest;
import test.util.BCPARISTestUtil;
import test.util.TestUtil;

public class DriverProcessorTest {

	@InjectMocks
	private DriverProcessor processor = new DriverProcessor();
	
	private ICBCRestRepository icbc = Mockito.mock(ICBCRestRepository.class);
	
	@Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }
	
	@Test
	public void proccess_success() {
		final String mockICBCResponse = TestUtil.readFile("ICBC/response-driver");
		final Layer7Message message = BCPARISTestUtil.getMessageDriverSNME();
		
		Mockito.when(icbc.requestDetails(Mockito.any(IMSRequest.class)))
			.thenReturn(mockICBCResponse);
		
		final Layer7Message icbcResponse = processor.process(message);
		Assert.assertEquals(mockICBCResponse, icbcResponse.getEnvelope().getBody().getMsgFFmt());
	}
	
	@Test
	public void create_ims_using_CNME_success() {
		final Layer7Message message = BCPARISTestUtil.getMessageDriverSNME();
		
		final String ims = processor.createIMS(message);
		
		Assert.assertEquals("DSSMTCPC HC BC41127 BC41027 QD SNME:NEWMAN/G1:OLDSON/G2:MIKE/DOB:19900214", ims);
	}
	
	@Test
	public void create_ims_using_DL_success() {
		final Layer7Message message = BCPARISTestUtil.getMessageDriverDL();
		
		final String ims = processor.createIMS(message);
		
		Assert.assertEquals("DSSMTCPC HC BC41127 BC41027 QD DL:3559874", ims);
	}
	
}
