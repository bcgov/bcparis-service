package ca.bc.gov.iamp.bcparis.repository.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import ca.bc.gov.iamp.bcparis.exception.rest.ResponseCodeNotExpected;

@Component
public class BaseRest {

	@Autowired
	private RestTemplate restTemplate;
	
	private final Logger log = LoggerFactory.getLogger(BaseRest.class);
	
	private final String errorMessage = "Response code not expected during rest request. Status=%s. Body=%s";

	
	public RestTemplate getRestTemplate() {
		return restTemplate;
	}
	
	public void assertResponse(final HttpStatus expected, final HttpStatus received, final String body) {
		log.debug(String.format("Rest response-code=%s", received));
		log.debug(String.format("Rest response-body=%s", body));
		
		if( received != expected) {
			throw new ResponseCodeNotExpected(String.format(errorMessage, received, body));
		}
	}
	
	public HttpHeaders getHeadersWithBasicAuth(final String username, final String password) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBasicAuth(username, password);
		return headers;
	}
	
}
