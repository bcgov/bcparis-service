package ca.bc.gov.iamp.bcparis.service;

import org.junit.Assert;
import org.junit.Test;

public class SatelliteServiceTest {

	private SatelliteService service = new SatelliteService();
	
	@Test
	public void calculate_execution_time_success() {
		
		// 2019 August 29 11:30:22 AM
		String duration = service.calculateExecutionTime("20190829113022");
		
		Assert.assertNotNull(duration);
	}
	
}
