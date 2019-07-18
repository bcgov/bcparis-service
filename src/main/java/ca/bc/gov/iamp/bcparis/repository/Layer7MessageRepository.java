package ca.bc.gov.iamp.bcparis.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ca.bc.gov.iamp.bcparis.exception.rest.MessagePostException;

@Service
public class Layer7MessageRepository {

	//@Value("${message.endpoint}")
	private String messageEndpoint = "http://bcgov.com.br:443";

	@Autowired
	private RestTemplate restTemplate;
	
	public void sendMessage(String messageContent) {
		try {

			HttpEntity<?> httpEntity = new HttpEntity<String>(messageContent,  getHeaders());
			
			ResponseEntity<String> response = restTemplate.postForEntity(messageEndpoint, httpEntity, String.class);
		
			handleResponse(response);
		}catch (Exception e) {
			throw new MessagePostException("Exception to post to Message Repository.", e);
		}
	}
	
	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_XML);
		return headers;
	}
	
	private void handleResponse(ResponseEntity<String> response) throws Exception {
		if( response.getStatusCode() != HttpStatus.ACCEPTED) {
			String message = String.format("Status code not expected during the Message Repository request. Status=%s. Body=%s", 
					response.getStatusCodeValue(), response.getBody());
			
			throw new MessagePostException(message);
		}
	}
	
}
