package ca.bc.gov.iamp.bcparis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ca.bc.gov.iamp.bcparis.repository.EmailRepository;

@Service
public class EmailService {

	@Autowired
	private EmailRepository emailRepository;
	
	@Value("${endpoint.iamp-email-service.email.subject}")
	private String subject;
	
	@Value("${endpoint.iamp-email-service.email.to}")
	private String to;
	
	public void sendEmail(String body) {
		emailRepository.sendEmail(subject, to, body);
	}
	
}
