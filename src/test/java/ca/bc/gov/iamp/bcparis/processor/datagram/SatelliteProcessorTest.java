package ca.bc.gov.iamp.bcparis.processor.datagram;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonProcessingException;

import ca.bc.gov.iamp.bcparis.model.MessageType;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.service.SatelliteService;
import test.util.BCPARISTestUtil;

public class SatelliteProcessorTest {

	@InjectMocks
	private SatelliteProcessor processor = new SatelliteProcessor();
	
	private SatelliteService service = Mockito.mock(SatelliteService.class);
	
	@Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }
	
	@Test
	public void bypass_satellite_message_success() throws JsonProcessingException {
		Layer7Message message = BCPARISTestUtil.getMessageSatellite();
		Object response = processor.process( message );
		
		Assert.assertEquals(response, "OK");
	}
	
	@Test
	public void send_satellite_messages_success() {
		
		final List<String> expectedDriver = Arrays.asList(
				"DL:0200000", 
				"DL:4529693/DATE:20070122", 
				"SNME:SMITH/G1:JOHN/", 
				"SNME:SMITH/G1:JOHN/DOB:19601208/SEX:M");
		
		final List<String> expectedVehicle = Arrays.asList(
				"LIC:PCG829/H", 
				"REG:2156746", 
				"REG:02156746", 
				"LIC:233AWB/H/LIC:GVW143/H/LIC:007FJR/H/LIC:JXX477/",
				"RVL:314208701580602/",
				"RNS:SMITH/G1:JOHN/G2:/DOB:/");
		
		final List<String> expectedPOR = Arrays.asList(
				"SNME:CLINE/G1:Brian/G2:Edward", 
				"SNME:CLINE/G1:Drista", 
				"SNME:SMITH/G1:John");
		
		processor.sendSatelliteMessages();
		
		for(String s : expectedDriver) {
			Mockito.verify(service, Mockito.times(1))
				.sendMessage(Mockito.anyString(), Mockito.eq(MessageType.DRIVER.getCode()), Mockito.eq(s));
		}
		
		for(String s : expectedVehicle) {
			Mockito.verify(service, Mockito.times(1))
				.sendMessage(Mockito.anyString(), Mockito.eq(MessageType.VEHICLE.getCode()), Mockito.eq(s));
		}
		
		for(String s : expectedPOR) {
			Mockito.verify(service, Mockito.times(1))
				.sendMessage(Mockito.anyString(), Mockito.eq(MessageType.POR.getCode()), Mockito.eq(s));
		}
	}
	
}
