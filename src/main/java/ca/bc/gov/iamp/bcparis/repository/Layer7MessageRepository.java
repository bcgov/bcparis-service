package ca.bc.gov.iamp.bcparis.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import ca.bc.gov.iamp.bcparis.exception.layer7.Layer7RestException;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
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
	
	
	public String sendMessage(Layer7Message message) {
		try {
			HttpEntity<?> httpEntity = new HttpEntity<Layer7Message>(message,  getHeadersWithBasicAuth(username, password));

			ResponseEntity<String> response = getRestTemplate().postForEntity(messageEndpoint + path, httpEntity, String.class);
		
			assertResponse(HttpStatus.OK, response.getStatusCode(), response.getBody() );
			return response.getBody();
		}
		catch (HttpServerErrorException e) {
			throw new Layer7RestException("Exception to post to Layer 7 Rest Service. Body:" + e.getResponseBodyAsString(), e);
		}
		catch (Exception e) {
			throw new Layer7RestException("Exception to post to Layer 7 Rest Service.", e);
		}
		
	}
	
}
