package ca.bc.gov.iamp.bcparis.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import ca.bc.gov.iamp.bcparis.repository.rest.BaseRest;

@Repository
public class EmailRepository extends BaseRest{

	@Value("${endpoint.iamp-email-service.rest}")
	private String url;
	
	@Value("${endpoint.iamp-email-service.rest.path}")
	private String path;
	
	@Value("${endpoint.iamp-email-service.rest.username}")
	private String username;
	
	@Value("${endpoint.iamp-email-service.rest.password}")
	private String password;
	
	@NewSpan("email")
	public ResponseEntity<String> sendEmail(String subject, String to, String body) {
	    MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
	    map.add("subject", subject);
	    map.add("to", to);
	    map.add("body", body);

	    HttpHeaders headers = getHeadersWithBasicAuthMultipartFormData(username, password);
	    HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
	    
	    return getRestTemplate().exchange(url + path, HttpMethod.POST, entity, String.class);
	}
	
}
