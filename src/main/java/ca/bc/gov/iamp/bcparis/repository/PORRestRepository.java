package ca.bc.gov.iamp.bcparis.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import ca.bc.gov.iamp.bcparis.exception.por.PORRestException;
import ca.bc.gov.iamp.bcparis.model.por.POROutput;

@Component
public class PORRestRepository {

	private final Logger log = LoggerFactory.getLogger(PORRestRepository.class);
	
	@Value("${endpoint.POR.rest}")
	private String procedureUrl;
	
	@Value("${endpoint.POR.rest.path.cpic}")
	private String path;
	
	@Value("${endpoint.POR.rest.username}")
	private String username;
	
	@Value("${endpoint.POR.rest.password}")
	private String password;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public POROutput callPOR(String surname, String given1, String given2, String given3, String dob) {
	
		try {
			
			final String URL = procedureUrl + path;
			HttpEntity<?> httpEntity = new HttpEntity<IMSRequest>(getHeaders(username, password));
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL);
			addQueryParam(builder, "surname", surname);
			addQueryParam(builder, "given1", given1);
			addQueryParam(builder, "given2", given2);
			addQueryParam(builder, "given3", given3);
			addQueryParam(builder, "dob", dob);

			log.info(String.format("Calling POR Rest Service. URL=%s", builder.build().toString()));
			
			ResponseEntity<POROutput> response = restTemplate.exchange(builder.build().toString(), HttpMethod.GET, httpEntity, POROutput.class);
			
			handleResponse(response);
			
			return response.getBody();
		}catch (Exception e) {
			throw new PORRestException("Exception to call POR Rest Service", e);
		}
	}
	
	private HttpHeaders getHeaders(final String username, final String password) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBasicAuth(username, password);
		return headers;
	}
	
	private void handleResponse(ResponseEntity<POROutput> response) throws Exception {
		if( response.getStatusCode() == HttpStatus.OK) {
			log.info(String.format("POR Rest service response=%s", response.getStatusCode()));
			log.info(String.format("Body=%s", response.getBody()));
		}else {
			String message = String.format("Status code not expected during the POR Rest Service request. Status=%s. Body=%s", 
					response.getStatusCodeValue(), response.getBody());
			throw new PORRestException(message, null);			
		}
	}
	
	private void addQueryParam(UriComponentsBuilder builder, final String name, final String namename) {
		if(StringUtils.hasText(name)) 
			builder.queryParam(name, name);
	}
}
