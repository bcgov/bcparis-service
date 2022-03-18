package ca.bc.gov.iamp.bcparis.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import ca.bc.gov.iamp.bcparis.exception.por.PORRestException;
import ca.bc.gov.iamp.bcparis.model.por.POROutput;

import static org.mockito.Mockito.when;
@RunWith(MockitoJUnitRunner.class)
public class PORRestRepositoryTest {

	@InjectMocks
	private PORRestRepository repo = new PORRestRepository();
	
	@Mock
	private RestTemplate rest = new RestTemplate();
	
	@Before
    public void initMocks() throws NoSuchFieldException, SecurityException{
		ReflectionTestUtils.setField(repo, "procedureUrl", "procedureUrl");
		ReflectionTestUtils.setField(repo,"path", "path");
		ReflectionTestUtils.setField(repo, "username", "mock_username");
		ReflectionTestUtils.setField(repo, "password", "mock_password");
    }
	
	@Test
	public void request_success() {

        when(rest.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class), Mockito.any(), Mockito.eq(POROutput.class) ))
    			.thenReturn(new ResponseEntity<>(new POROutput(), HttpStatus.OK));
		
		final POROutput resp = repo.callPOR("surname", "given1", "given2", "given3", "dob");
		
		Assert.assertNotNull(resp);
	}
	
	@Test(expected=PORRestException.class)
	public void request_exception() {

        when(rest.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class), Mockito.any(), Mockito.eq(POROutput.class) ))
    			.thenReturn(new ResponseEntity<>(new POROutput(), HttpStatus.NOT_FOUND));
		
		final POROutput resp = repo.callPOR("surname", "given1", "given2", "given3", "dob");
		
		Assert.assertNotNull(resp);
	}
	
}
