package ca.bc.gov.iamp.bcparis.processor.datagram;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.repository.ICBCRestRepository;
import ca.bc.gov.iamp.bcparis.repository.IMSRequest;
import test.util.BCPARISTestUtil;
import test.util.TestUtil;


public class VehicleProcessorTest {

	@InjectMocks
	private VehicleProcessor processor = new VehicleProcessor();
	
	private ICBCRestRepository icbc = Mockito.mock(ICBCRestRepository.class);
	
	@Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }
	
	@Test
	public void proccess_success() {
		final String mockICBCResponse = TestUtil.readFile("ICBC/response-vehicle");
		final Layer7Message message = BCPARISTestUtil.getMessageVehicleVIN();
		
		Mockito.when(icbc.requestDetails(Mockito.any(IMSRequest.class)))
			.thenReturn(mockICBCResponse);
		
		final Layer7Message icbcResponse = processor.process(message);
		Assert.assertEquals(mockICBCResponse, icbcResponse.getEnvelope().getBody().getMsgFFmt());
	}
	
	@Test
	public void create_ims_using_VIN_success() {
		final Layer7Message message = BCPARISTestUtil.getMessageVehicleVIN();
		final String ims = processor.createIMS(message);
		
		Assert.assertEquals("JISTRAN HC BC41127 BC41028 QD VIN:1FTEW1EF3GKF29092", ims);
	}
	
	@Test
	public void create_ims_using_LIC_success() {
		final Layer7Message message = BCPARISTestUtil.getMessageVehicleLIC();
		final String ims = processor.createIMS(message);
		
		Assert.assertEquals("JISTRAN HC BC41127 BC41028 QD LIC:PN890H", ims);
	}
	
	@Test
	public void create_ims_using_RVL_success() {
		final Layer7Message message = BCPARISTestUtil.getMessageVehicleRVL();
		final String ims = processor.createIMS(message);
		
		Assert.assertEquals("JISTRN2 HC BC41127 BC41028 QD RVL:845513634081303/", ims);
	}
	
}
