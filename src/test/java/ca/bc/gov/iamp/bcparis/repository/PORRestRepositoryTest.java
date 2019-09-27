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

import ca.bc.gov.iamp.bcparis.exception.por.PORRestException;
import ca.bc.gov.iamp.bcparis.model.por.POROutput;

public class PORRestRepositoryTest {

	@InjectMocks
	private PORRestRepository repo = new PORRestRepository();
	
	@Mock
	private RestTemplate rest;
	
	@Before
    public void initMocks() throws NoSuchFieldException, SecurityException{
        MockitoAnnotations.initMocks(this);
        
        FieldSetter.setField(repo, repo.getClass().getDeclaredField("procedureUrl"), "procedureUrl");
        FieldSetter.setField(repo, repo.getClass().getDeclaredField("path"), "path");
        FieldSetter.setField(repo, repo.getClass().getDeclaredField("username"), "mock_username");
        FieldSetter.setField(repo, repo.getClass().getDeclaredField("password"), "mock_password");
    }
	
	@Test
	public void request_success() {

        Mockito.when(rest.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class), Mockito.any(), Mockito.eq(POROutput.class) ))
    			.thenReturn(new ResponseEntity<>(new POROutput(), HttpStatus.OK));
		
		final POROutput resp = repo.callPOR("surname", "given1", "given2", "given3", "dob");
		
		Assert.assertNotNull(resp);
	}
	
	@Test(expected=PORRestException.class)
	public void request_exception() {

        Mockito.when(rest.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class), Mockito.any(), Mockito.eq(POROutput.class) ))
    			.thenReturn(new ResponseEntity<>(new POROutput(), HttpStatus.NOT_FOUND));
		
		final POROutput resp = repo.callPOR("surname", "given1", "given2", "given3", "dob");
		
		Assert.assertNotNull(resp);
	}
	
}
