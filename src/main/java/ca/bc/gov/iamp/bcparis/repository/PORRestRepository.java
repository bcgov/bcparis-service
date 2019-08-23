package ca.bc.gov.iamp.bcparis.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import ca.bc.gov.iamp.bcparis.exception.por.PORRestException;
import ca.bc.gov.iamp.bcparis.model.por.POROutput;
import ca.bc.gov.iamp.bcparis.repository.query.IMSRequest;
import ca.bc.gov.iamp.bcparis.repository.rest.BaseRest;

@Component
public class PORRestRepository extends BaseRest{

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
	
	@NewSpan("por")
	public POROutput callPOR(String surname, String given1, String given2, String given3, String dob) {
	
		try {
			
			String URL = procedureUrl + path;
			HttpEntity<?> httpEntity = new HttpEntity<IMSRequest>(getHeadersWithBasicAuth(username, password));
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL);
			addQueryParam(builder, "surname", surname);
			addQueryParam(builder, "given1", given1);
			addQueryParam(builder, "given2", given2);
			addQueryParam(builder, "given3", given3);
			addQueryParam(builder, "dob", dob);

			URL = builder.build().toString();
			log.debug(String.format("Calling POR Rest Service. URL=%s", URL));
			
			ResponseEntity<POROutput> response = restTemplate.exchange(URL, HttpMethod.GET, httpEntity, POROutput.class);

			assertResponse(HttpStatus.OK, response.getStatusCode(), response.getBody().toString());
			
			return response.getBody();
		}
		catch (HttpServerErrorException e) {
			throw new PORRestException("Exception to POR Rest Service. Body:" + e.getResponseBodyAsString(), e);
		}
		catch (Exception e) {
			throw new PORRestException("Exception to call POR Rest Service", e);
		}
	}
	
	private void addQueryParam(UriComponentsBuilder builder, final String name, final String value) {
		if(StringUtils.hasText(value)) 
			builder.queryParam(name, value);
	}
}
