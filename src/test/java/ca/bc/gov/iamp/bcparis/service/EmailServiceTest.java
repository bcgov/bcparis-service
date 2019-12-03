package ca.bc.gov.iamp.bcparis.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ca.bc.gov.iamp.bcparis.repository.EmailRepository;

public class EmailServiceTest {

	@Mock
	private EmailRepository repository;
	
	@InjectMocks
	private EmailService service = new EmailService();
	
	@Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }
	
	@Test
	public void test() {
		service.sendEmail("Email body");
		Mockito.verify(repository, Mockito.times(1))
			.sendEmail(Mockito.isNull(), Mockito.isNull(), Mockito.eq("Email body"));
	}
}
