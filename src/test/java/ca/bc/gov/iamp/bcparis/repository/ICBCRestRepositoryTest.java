package ca.bc.gov.iamp.bcparis.repository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.FieldSetter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import ca.bc.gov.iamp.bcparis.repository.query.IMSResponse;

public class ICBCRestRepositoryTest {

	@InjectMocks
	private ICBCRestRepository repo = new ICBCRestRepository();
	
	@Mock
	private RestTemplate rest;
	
	@Mock
	private ResponseEntity<Object> response;
	
	
	@Before
    public void initMocks() throws NoSuchFieldException, SecurityException{
        MockitoAnnotations.initMocks(this);
        Mockito.when(response.getStatusCode()).thenReturn(HttpStatus.OK);
        Mockito.when(response.getBody()).thenReturn(getResponse());
        Mockito.when(rest.postForEntity(Mockito.any(), Mockito.any(), Mockito.any()) ).thenReturn(response);
        
        
        FieldSetter.setField(repo, repo.getClass().getDeclaredField("username"), "mock_username");
        FieldSetter.setField(repo, repo.getClass().getDeclaredField("password"), "mock_password");
    }
	
	@Test
	public void parse_response_success() {
		
		//final String icbcResponse = repo.requestDetails(IMSRequest.builder().build());

	}
	
	private IMSResponse getResponse() {
		return IMSResponse.builder()
				.imsResponse("IMS Response")
				.build();
	}
	
}
