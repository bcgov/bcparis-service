package ca.bc.gov.iamp.bcparis.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import ca.bc.gov.iamp.bcparis.exception.icbc.ICBCRestException;
import ca.bc.gov.iamp.bcparis.exception.layer7.Layer7RestException;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.repository.rest.BaseRest;

@Service
public class Layer7MessageRepository extends BaseRest{

	private final Logger log = LoggerFactory.getLogger(Layer7MessageRepository.class);
	
	@Value("${endpoint.layer7.rest}")
	private String messageEndpoint;
	
	@Value("${endpoint.layer7.rest.path.put}")
	private String path;
	
	@Value("${endpoint.layer7.rest.header.username}")
	private String username;
	
	@Value("${endpoint.layer7.rest.header.password}")
	private String password;
	
	
	@NewSpan("layer7-mq")
	public String sendMessage(Layer7Message message) {
		try {
			final String URL = messageEndpoint + path;
			HttpEntity<?> httpEntity = new HttpEntity<Layer7Message>(message,  getHeadersWithBasicAuth(username, password));
			
			log.debug(String.format("Calling Layer7 Rest Service. URL=%s, Layer7Message=%s", URL, message ));
			
			ResponseEntity<String> response = getRestTemplate().postForEntity(URL, httpEntity, String.class);
		
			assertResponse(HttpStatus.OK, response.getStatusCode(), response.getBody() );
			return response.getBody();
		}
		catch (HttpServerErrorException e) {
			throw new ICBCRestException(
					String.format("Response Body=%s", e.getLocalizedMessage(), e.getResponseBodyAsString()),
					e.getResponseBodyAsString(), e);
		}
		catch (Exception e) {
			throw new Layer7RestException(
					String.format("Message=%s", e.getLocalizedMessage()), 
					e.getLocalizedMessage(), e);
		}
	}
	
}
