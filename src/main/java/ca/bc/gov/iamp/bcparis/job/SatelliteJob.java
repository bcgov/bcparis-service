package ca.bc.gov.iamp.bcparis.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.bc.gov.iamp.bcparis.processor.datagram.SatelliteProcessor;

@Component
@DisallowConcurrentExecution
public class SatelliteJob implements Job{

	private final Logger logger = LoggerFactory.getLogger(SatelliteJob.class);

	@Autowired
	private SatelliteProcessor processor;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		logger.info("Job ** {} ** fired @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());

		processor.sendSatelliteMessages();

		logger.info("Next job scheduled @ {}", context.getNextFireTime());
	}

}
