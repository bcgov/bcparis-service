package ca.bc.gov.iamp.bcparis.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ca.bc.gov.iamp.bcparis.exception.rest.RestException;
import ca.bc.gov.iamp.bcparis.repository.rest.BaseRest;

@Service
public class Layer7MessageRepository extends BaseRest{

	@Value("${endpoint.layer7.rest}")
	private String messageEndpoint;
	
	@Value("${endpoint.layer7.rest.path.put}")
	private String path;
	
	@Value("${endpoint.layer7.rest.header.username}")
	private String username;
	
	@Value("${endpoint.layer7.rest.header.password}")
	private String password;
	
	public String sendMessage(String messageContent) {
		try {

			HttpEntity<?> httpEntity = new HttpEntity<String>(messageContent,  getHeadersWithBasicAuth(messageContent, messageContent));
			
			ResponseEntity<String> response = getRestTemplate().postForEntity(messageEndpoint, httpEntity, String.class);
		
			assertResponse(HttpStatus.OK, response.getStatusCode(), response.getBody() );
			return response.getBody();
		}catch (Exception e) {
			throw new RestException("Exception to post to Layer 7 Message Repository.", e);
		}
	}
	
}
