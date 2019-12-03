package ca.bc.gov.iamp.bcparis.job;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import ca.bc.gov.iamp.bcparis.processor.datagram.SatelliteProcessor;

public class SatelliteJobTest {

	@InjectMocks
	private SatelliteJob job = new SatelliteJob();
	
	@Mock
	private SatelliteProcessor processor;
	
	@Mock
	private JobExecutionContext context;
	
	@Mock
	private JobDetail details;
	
	@Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }
	
	@Test
	public void should_call_sendSatelliteMessages() throws JobExecutionException {
		Mockito.when(context.getJobDetail()).thenReturn(details);
		Mockito.when(details.getKey()).thenReturn(new JobKey("Mock Key"));
		
		job.execute(context);
		
		Mockito.verify(processor, Mockito.times(1)).sendSatelliteMessages();
	}
}
