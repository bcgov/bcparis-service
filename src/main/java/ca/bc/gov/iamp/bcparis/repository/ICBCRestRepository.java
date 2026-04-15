package ca.bc.gov.iamp.bcparis.repository;

import ca.bc.gov.iamp.bcparis.repository.IcbcOAuthClient;
import ca.bc.gov.iamp.bcparis.model.message.Layer7Message;
import ca.bc.gov.iamp.bcparis.repository.query.IMSRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class ICBCRestRepository {

    private static final Logger log = LoggerFactory.getLogger(ICBCRestRepository.class);

    private final RestTemplate restTemplate;
    private final IcbcOAuthClient oAuthClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${icbc.api.url}")
    private String icbcApiUrl;

    public ICBCRestRepository(RestTemplate restTemplate,
            IcbcOAuthClient oAuthClient) {
        this.restTemplate = restTemplate;
        this.oAuthClient = oAuthClient;
    }

    public String callIcbcApi(String requestBody, String loginUserId) {
        try {
            return callIcbcApiInternal(requestBody, loginUserId);
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            // If we get 401 Unauthorized, invalidate token and retry once
            if (e.getStatusCode() == org.springframework.http.HttpStatus.UNAUTHORIZED) {
                log.warn("Received 401 Unauthorized from ICBC API. Invalidating cached token and retrying.");
                oAuthClient.invalidateToken();
                return callIcbcApiInternal(requestBody, loginUserId);
            }
            throw e;
        }
    }

    private String callIcbcApiInternal(String requestBody, String loginUserId) {
        String accessToken = oAuthClient.getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(MediaType.parseMediaTypes("application/json"));
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("loginUserId", loginUserId);

        log.debug("ICBC API Request Body: {}", requestBody);
        log.info("Calling ICBC API at {}", icbcApiUrl);
        log.debug("ICBC API request body: {}", requestBody);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(icbcApiUrl, HttpMethod.POST, entity, String.class);
        log.info("ICBC API response status: {}", response.getStatusCode());
        log.debug("ICBC API response body: {}", response.getBody());
        return response.getBody();
    }

    public String requestDetails(Layer7Message message, IMSRequest imsRequest) {
        String requestBody;
        try {
            // Use 'requestString' as the JSON key instead of 'imsRequest'
            requestBody = objectMapper.writeValueAsString(new SimpleRequestString(imsRequest.getImsRequest()));
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize IMSRequest to JSON", e);
        }
        String loginUserId = (message.getEnvelope() != null && message.getEnvelope().getHeader() != null)
                ? message.getEnvelope().getHeader().getUserId()
                : "";
        return callIcbcApi(requestBody, loginUserId);
    }

    // Helper class to match the required JSON structure
    private static class SimpleRequestString {
        public String requestString;

        public SimpleRequestString(String requestString) {
            this.requestString = requestString;
        }
    }

    public HttpHeaders getHeaders(Layer7Message l7message, String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(username, password);
        // Example: add auditTransactionId if available
        String auditTransactionId = "";
        if (l7message != null && l7message.getEnvelope() != null && l7message.getEnvelope().getMqmd() != null) {
            auditTransactionId = l7message.getEnvelope().getMqmd().getMessageIdByte();
            if (auditTransactionId == null || auditTransactionId.isEmpty()) {
                auditTransactionId = l7message.getEnvelope().getMqmd().getCorrelationIdByte();
            }
        }
        headers.add("auditTransactionId", auditTransactionId != null ? auditTransactionId : "");
        return headers;
    }
}
