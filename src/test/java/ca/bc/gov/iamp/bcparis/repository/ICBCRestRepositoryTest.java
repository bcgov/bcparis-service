package ca.bc.gov.iamp.bcparis.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.FieldSetter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import ca.bc.gov.iamp.bcparis.exception.icbc.ICBCRestException;
import ca.bc.gov.iamp.bcparis.repository.query.IMSRequest;
import ca.bc.gov.iamp.bcparis.repository.query.IMSResponse;

public class ICBCRestRepositoryTest {

	@InjectMocks
	private ICBCRestRepository repo = new ICBCRestRepository();
	
	@Mock
	private RestTemplate rest;
	
	@Before
    public void initMocks() throws NoSuchFieldException, SecurityException{
        MockitoAnnotations.initMocks(this);
        
        FieldSetter.setField(repo, repo.getClass().getDeclaredField("username"), "mock_username");
        FieldSetter.setField(repo, repo.getClass().getDeclaredField("password"), "mock_password");
    }
	
	@Test
	public void request_details_success() {

        Mockito.when(rest.postForEntity(Mockito.anyString(), Mockito.any(HttpEntity.class), Mockito.any()) )
    		.thenReturn(new ResponseEntity<>(getResponse(), HttpStatus.OK));
		
		final String icbcResponse = repo.requestDetails(IMSRequest.builder().build());
		
		Assert.assertEquals(icbcResponse, "IMS Response");
	}
	
	@Test(expected=ICBCRestException.class)
	public void request_details_rest_exception() {

        Mockito.when(rest.postForEntity(Mockito.anyString(), Mockito.any(HttpEntity.class), Mockito.any()) )
    		.thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"));
		
		repo.requestDetails(IMSRequest.builder().build());
	}
	
	@Test(expected=ICBCRestException.class)
	public void request_details_not_found() {

		 Mockito.when(rest.postForEntity(Mockito.anyString(), Mockito.any(HttpEntity.class), Mockito.any()) )
 		.thenReturn(new ResponseEntity<>(getResponse(), HttpStatus.NOT_FOUND));
		
		repo.requestDetails(IMSRequest.builder().build());
	}
	
	private IMSResponse getResponse() {
		return IMSResponse.builder()
				.imsResponse("IMS Response")
				.build();
	}
	
}
