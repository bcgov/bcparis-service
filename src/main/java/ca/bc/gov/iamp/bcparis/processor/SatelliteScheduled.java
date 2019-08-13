package ca.bc.gov.iamp.bcparis.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SatelliteScheduled {

	private final Logger log = LoggerFactory.getLogger(SatelliteScheduled.class);
	
	@Scheduled(fixedDelayString = "${satellite.schedule.job.fixed.delay}")
	public void sendSatelliteMessages() {
		//log.info("Running Satellite JOB.");
		//log.info("Sending Satellite Messages");
	}
	
}
