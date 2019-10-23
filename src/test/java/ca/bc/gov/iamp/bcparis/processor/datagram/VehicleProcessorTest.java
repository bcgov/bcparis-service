package ca.bc.gov.iamp.bcparis.processor.datagram;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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


public class VehicleProcessorTest {

	@InjectMocks
	private VehicleProcessor processor = new VehicleProcessor();
	
	private ICBCRestRepository icbc = Mockito.mock(ICBCRestRepository.class);
	
	@Spy
	private MessageService service = new MessageService();
	
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
		String expectedParsed = "SEND MT:M\n" + 
				"FMT:Y\n" + 
				"FROM:BC41028\n" + 
				"TO:BC41127\n" + 
				"TEXT:RE: 2505\n" + 
				"\n" + 
				"HC BC41028\n" + 
				"BC41127\n" + 
				"RE VIN:110025/P:Y/RSVP:16\n" + 
				"FULL VIN           MAKE        MODEL  YEAR  REG\n" + 
				"AAAAP2450BY110025  DODGE       2WHDR  1981  07940335\n" + 
				"AAAAB23S9CK110025  DODGE              1982  07945896\n" + 
				"AAAAA21C634110025  SUZUKI      FORSA  1984  08021345\n" + 
				"AAAAY3187J5110025  CHEVROLET   CORVT  1988  10043896\n" + 
				"AAAAA35S2L5110025  SUZUKI      SWIFT  1990  06485491\n" + 
				"AAAAA34S0L5110025  SUZUKI      SWIFT  1990  06716641\n" + 
				"AAAAF52H7M1110025  SKIPPER            1991  06950618\n" + 
				"AAAAG0704PH110025  VOLKSWAGEN  TRANS  1993  08520937\n" + 
				"AAAAT2939SC110025  SPECIAL     SP&amp;SP  1995  09501294\n" + 
				"AAAAF13C6XU110025  TOYOTA      SIENA  1999  08946364\n" + 
				"AAAAC124X27110025  CHEVROLET   CAVAL  2002  00718278\n" + 
				"AAAAT18X75K110025  CHEVROLET   BLAZR  2005  02936978\n" + 
				"AAAAM72D95U110025  HYUNDAI     TUSON  2005  03053788\n" + 
				"AAAAT923475110025  TOYOTA      YARIS  2007  03743005\n" + 
				"AAAAA16458H110025  HONDA       CIVIC  2008  02381256\n" + 
				"     THERE ARE MORE POSSIBLE HITS.\n" + 
				"     CONTACT VEHICLE REGISTRATION &amp; LICENCING AT ICBC\n";
		Assert.assertEquals(expectedParsed, icbcResponse.getEnvelope().getBody().getMsgFFmt());
	}
	
	@Test
	public void create_ims_using_VIN_success() {
		final Layer7Message message = BCPARISTestUtil.getMessageVehicleVIN();
		
		ArgumentCaptor<IMSRequest> argument = ArgumentCaptor.forClass(IMSRequest.class);
		Mockito.when(icbc.requestDetails(argument.capture())).thenReturn("ICBC Response");
		
		processor.process(message);
	
		Mockito.verify(icbc, Mockito.times(1)).requestDetails(argument.capture());
		
		Assert.assertTrue(
			argument.getValue().getImsRequest().startsWith("JISTRAN HC BC41127 BC41028 VIN:1FTEW1EF3GKF29092"));
	}
	
	@Test
	public void create_ims_using_LIC_success() {
		final Layer7Message message = BCPARISTestUtil.getMessageVehicleLIC();
		
		ArgumentCaptor<IMSRequest> argument = ArgumentCaptor.forClass(IMSRequest.class);
		Mockito.when(icbc.requestDetails(argument.capture())).thenReturn("ICBC Response");
		
		processor.process(message);
	
		Mockito.verify(icbc, Mockito.times(1)).requestDetails(argument.capture());
		Assert.assertTrue(argument.getValue().getImsRequest().startsWith("JISTRAN HC BC41127 BC41028 LIC:PN890H"));
	}
	
	@Test
	public void create_ims_using_RVL_success() {
		final Layer7Message message = BCPARISTestUtil.getMessageVehicleRVL();
		
		ArgumentCaptor<IMSRequest> argument = ArgumentCaptor.forClass(IMSRequest.class);
		Mockito.when(icbc.requestDetails(argument.capture())).thenReturn("ICBC Response");
		
		processor.process(message);
		
		Mockito.verify(icbc, Mockito.times(1)).requestDetails(argument.capture());
		Assert.assertTrue(argument.getValue().getImsRequest().startsWith("JISTRN2 HC BC41127 BC41028 RVL:845513634081303"));
	}
	
	@Test
	public void create_ims_using_RNS_success() {
		final Layer7Message message = BCPARISTestUtil.getMessageVehicleRNS();
		
		ArgumentCaptor<IMSRequest> argument = ArgumentCaptor.forClass(IMSRequest.class);
		Mockito.when(icbc.requestDetails(argument.capture())).thenReturn("ICBC Response");
		
		processor.process(message);
		
		Mockito.verify(icbc, Mockito.times(1)).requestDetails(argument.capture());
		Assert.assertTrue(argument.getValue().getImsRequest().startsWith("JISTRN2 HC BC41127 BC41028 RNS:845513634081303/"));
	}
	
	@Test
	public void create_query_params_list_success() {
		final Layer7Message message = BCPARISTestUtil.getMessageVehicleMultipleParams();
		
		ArgumentCaptor<IMSRequest> argument = ArgumentCaptor.forClass(IMSRequest.class);
		Mockito.when(icbc.requestDetails(argument.capture())).thenReturn("ICBC Response");
		
		processor.process(message);
		
		Mockito.verify(icbc, Mockito.times(8)).requestDetails(argument.capture());
		
		int countTEXT = StringUtils.countOccurrencesOf(message.getEnvelope().getBody().getMsgFFmt(), "TEXT:BCPARIS Diagnostic Test qwe20190827173834");
		int countICBCResponse = StringUtils.countOccurrencesOf(message.getEnvelope().getBody().getMsgFFmt(), "ICBC Response");
		
		Assert.assertEquals(1, countTEXT);
		Assert.assertEquals(8, countICBCResponse);
	}

	@Test
	public void error_during_ICBC_call() {
		String errorContent = TestUtil.readFile("ICBC/response-error-2.xml");
		
		final Layer7Message message = BCPARISTestUtil.getMessageVehicleMultipleParams();
		
		Mockito.when(icbc.requestDetails(Mockito.any())).thenThrow(new ICBCRestException("", errorContent, null) );
		
		try {
			processor.process(message);
		}catch (Exception e) {
			//Just ignore
		}
		
		int count = StringUtils.countOccurrencesOf(message.getEnvelope().getBody().getMsgFFmt(), "l7:detailMessage");
		Assert.assertEquals(14, count);
	}
	
}
