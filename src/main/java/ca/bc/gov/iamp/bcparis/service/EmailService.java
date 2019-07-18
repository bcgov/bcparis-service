package ca.bc.gov.iamp.bcparis.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

//		@Value("classpath:/sample/test-attachment.file")
//		private Resource attachment;
	
	public void sendEmail() {


//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
//		map.add("subject", "subject");
//		map.add("to", "to@to.com");
//		map.add("to", "to2@to2.com");
//		map.add("cc", "cc@cc.com");
//		map.add("bcc", "bcc@bcc.com");
//		map.add("body", "body");
//		FileSystemResource a = new FileSystemResource(attachment.getFile());
//		map.add("attachments", a);
//
//		HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
//
//		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/v1/emails"), HttpMethod.POST, entity, String.class);
	}
	
}
