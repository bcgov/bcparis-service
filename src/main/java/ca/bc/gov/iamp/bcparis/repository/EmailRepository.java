package ca.bc.gov.iamp.bcparis.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Repository
public class EmailRepository {

	@Autowired
	private RestTemplate restTemplate;
	
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

	    HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, getHeaders());
	    
	    return restTemplate.exchange(url + path, HttpMethod.POST, entity, String.class);
	}
	
	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
	    headers.setBasicAuth(username, password);
	    return headers;
	}
	
}
