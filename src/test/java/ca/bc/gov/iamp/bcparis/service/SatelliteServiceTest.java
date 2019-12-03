package ca.bc.gov.iamp.bcparis.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ca.bc.gov.iamp.bcparis.model.MessageType;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.repository.Layer7MessageRepository;

public class SatelliteServiceTest {

	final String NEW_LINE = "\n";
	private final String SCHEMA = "SEND MT:M" + NEW_LINE +
			  "FMT:Y" + NEW_LINE +
			  "FROM:{FROM}" + NEW_LINE + 
			  "TO:{TO}" + NEW_LINE + 
			  "TEXT:{TEXT}" + NEW_LINE +
			  NEW_LINE +
			  "{QUERY_MESSAGE}" + 
			  NEW_LINE + NEW_LINE + 
			  "{DATE_TIME}{DATE_TIME}";
	
	@InjectMocks
	private SatelliteService service = new SatelliteService();
	
	@Mock
	private Layer7MessageRepository repository;
	
	@Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }
	
	@Test
	public void calculate_execution_time_success() {
		// 2019 August 29 11:30:22 AM
		String duration = service.calculateExecutionTime("20190829113022");
		Assert.assertNotNull(duration);
	}
	
	@Test
	public void send_message_success() {
		service.sendMessage(SCHEMA, MessageType.VEHICLE.getCode(), "LIC:233AWB/H/LIC:GVW143/H/LIC:007F\n" + 
				"JR/H/LIC:JXX477/REG:957167\n" + 
				"/VIN:1GNEL19W1XB163160/VIN:163160/P:Y/VIN:163160/P:Y/RSVP:16");
		
		Mockito.verify(repository, Mockito.times(1)).sendMessage(Mockito.any(Layer7Message.class));
	}
	
}
