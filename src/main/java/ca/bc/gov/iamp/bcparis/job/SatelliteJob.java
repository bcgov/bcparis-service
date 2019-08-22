package ca.bc.gov.iamp.bcparis.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.iamp.bcparis.processor.datagram.SatelliteProcessor;

//@Component
@DisallowConcurrentExecution
public class SatelliteJob implements Job{

	private final Logger logger = LoggerFactory.getLogger(SatelliteJob.class);

	private SatelliteProcessor processor;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		logger.info("Job ** {} ** fired @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());

		try {
			processor.sendSatelliteMessages();
		} catch (Exception e) {
			//exceptionHandler.handleFlowErrors(e);
		}

		logger.info("Next job scheduled @ {}", context.getNextFireTime());
	}

}
