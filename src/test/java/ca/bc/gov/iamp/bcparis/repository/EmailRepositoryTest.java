package ca.bc.gov.iamp.bcparis.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.FieldSetter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class EmailRepositoryTest {

	@InjectMocks
	private EmailRepository repo = new EmailRepository();
	
	@Mock
	private RestTemplate rest;
	
	@Before
    public void initMocks() throws NoSuchFieldException, SecurityException{
        MockitoAnnotations.initMocks(this);
        
        FieldSetter.setField(repo, repo.getClass().getDeclaredField("path"), "mock_path");
        FieldSetter.setField(repo, repo.getClass().getDeclaredField("url"), "mock_url");
        FieldSetter.setField(repo, repo.getClass().getDeclaredField("username"), "mock_username");
        FieldSetter.setField(repo, repo.getClass().getDeclaredField("password"), "mock_password");
    }
	
	@Test
	public void request_details_success() {
        Mockito.when(
    		rest.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class), Mockito.any(), Mockito.eq(String.class) ))
    			.thenReturn(new ResponseEntity<>("Success", HttpStatus.OK));
		
		ResponseEntity<String> response = repo.sendEmail("subject", "to", "body");
		
		Assert.assertEquals(response.getBody(), "Success");
		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
}
