package ca.bc.gov.iamp.bcparis.config;
import java.util.Properties;

import javax.sql.DataSource;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import ca.bc.gov.iamp.bcparis.job.SatelliteJob;

@Configuration
public class QuartzConfiguration {
	
	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private QuartzProperties quartzProperties;
	 
	@Autowired
	private DataSource dataSource;
	
	@Bean
	public SpringBeanJobFactory springBeanJobFactory() {
		AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		return jobFactory;
	}

	@Bean
	@Primary
	@ConditionalOnProperty(name = "job.satellite.enable", havingValue = "true", matchIfMissing = false)
	public SchedulerFactoryBean scheduler(@Qualifier("satelliteJobTrigger") Trigger satelliteTrigger) throws Exception {

		SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
		Properties properties = new Properties();
		properties.putAll(quartzProperties.getProperties());

		schedulerFactory.setQuartzProperties(properties);
		schedulerFactory.setDataSource(dataSource);

		schedulerFactory.setJobFactory(springBeanJobFactory());
		schedulerFactory.setTriggers(satelliteTrigger);
		schedulerFactory.setStartupDelay(30);
		schedulerFactory.afterPropertiesSet();

		return schedulerFactory;
	}
	
	@Bean
	@Qualifier("satelliteJobTrigger")
	@ConditionalOnProperty(name = "job.satellite.enable", havingValue = "true", matchIfMissing = false)
	public CronTriggerFactoryBean satelliteJobTrigger(
			@Qualifier("satelliteJobDetail") JobDetail jobDetail,
			@Value("${job.satellite.cron-expression:0 * * ? * * *}") String satelliteJobCron) {
		
		CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
		trigger.setCronExpression(satelliteJobCron);
		trigger.setName("Qrtz_Satellite_Trigger");
		trigger.setJobDetail(jobDetail);
		return trigger;
	}
	
	@Bean
	@Qualifier("satelliteJobDetail")
	@ConditionalOnProperty(name = "job.satellite.enable", havingValue = "true", matchIfMissing = false)
	public JobDetailFactoryBean satelliteJobDetail() {

		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(SatelliteJob.class);
		jobDetailFactory.setName("Qrtz_Satellite_Job_Detail");
		jobDetailFactory.setDescription("Invoke Transition Job service...");
		jobDetailFactory.setDurability(true);
		return jobDetailFactory;
	}
	 
}
